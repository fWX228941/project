package com.android.haobanyi.activity.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.haobanyi.R;
import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.activity.searching.KeywordSearchActivity;
import com.android.haobanyi.activity.shopping.product.ShoppingRadioGroupActivity;
import com.android.haobanyi.adapter.ProductListAdapter;
import com.android.haobanyi.core.ActionCallbackFivefoldListener;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.ActionCallbackTripleListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.shopping.product.DetailsBean;
import com.android.haobanyi.model.bean.shopping.product.ProductDetailsBean;
import com.android.haobanyi.model.bean.shopping.product.SatisfySendBean;
import com.android.haobanyi.model.bean.shopping.product.ShopAttrBean;
import com.android.haobanyi.model.bean.shopping.product.VouchersTemplateBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.NetworkImageHolderView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;



/**
 *
 *
 * @作者: 付敏
 * @创建日期：2016/03/31
 * @邮箱：466566941@qq.com
 * @当前文件描述：首页
 *
 */
public class FrontPageView extends RelativeLayout implements OnItemClickListener,BaseQuickAdapter.RequestLoadMoreListener,SwipeRefreshLayout.OnRefreshListener  {

    private static final String TAG = "minfu";
    ConvenientBanner  convenientBanner ;//顶部广告栏控件
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    private long requestCount = 15;
    //1.上下文
    private Context context;
    //2.布局加载器
    private LayoutInflater inflater;
    /*3.顶部广告栏控件所需的图片列表集 ，list中存放的是图片资源的int ID值 基本类型转化为其包装类*/
    private ArrayList<Integer> localImageslist = new ArrayList<>();
    /*当前的页数*/
    private int CurrentPage = 0;// 默认从1开始
    private int PAGESIZE = 10; // 定义的一页多少条数据
    private ProductListAdapter adapter = new ProductListAdapter(null);
    private View notLoadingView;
    private int totalCount =0 ;
    private boolean isListToBottom = false; //数据是否到顶了
    private boolean isColorChanged = true; //颜色是否变更
    private boolean isLogin = false;    //是否登录成功
    PreferenceUtil complexPreferences;
    SpotsDialog dialog;
    private View errorView;
    private View emptyView;

    /*
    * 5.自定义复杂视图
    *   步骤如下：
    *   1）覆写构造函数，添加上下文和布局加载器
    *   2）初始化视图
    *   3）为控件绑定事件
    *   4）检查是否登入
    * */
    public FrontPageView(Context context) {
        super(context);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        init();
    }

    public FrontPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        dialog =  new SpotsDialog(context);
        dialog.show();
        /*
        * java.lang.RuntimeException: Unable to start activity ComponentInfo{com.android.haobanyi/com.android.haobanyi.activity.RadioGroupActivity}: java.lang.ClassCastException: android.widget.TextView cannot be cast to dmax.dialog.ProgressLayout
        *
        * */
        Log.d(TAG, "初始化");
        /*01.设置根视图*/
        setLayout();
        /*02.初始化列表*/
        initRecyclerView();
        NTPTime.getInstance().getCurrentTime();
        initAdapter(null);
        initViews();
        mSuperRecyclerViewImg.setAdapter(adapter);
        CurrentPage = 1;
        requestCount = 15;
        getDataFromServer(CurrentPage, true);
        complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);
        refreshToken();
    }

    private void refreshToken() {
        if (isLogin){
            long refreshTime = complexPreferences.getLong(Constants.START_TIME,-1000L);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                requestCount = 5;
                getToken();
            }

        }
    }

    private void getToken() {
        BaseApplication.getApplication().getAppAction().getToken(new ActionCallbackListener<LoginResponseResult>() {
            @Override
            public void onSuccess(LoginResponseResult data) {

            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    getToken();
                }


            }
        });
    }
    /*
    * 两个参数：
    * @parame: currentPage 当前页数
    * @parame: isRefreshOrFirstLoading 首次登陆或者刷新
    * */
    private void getDataFromServer(final int currentPage, final boolean isRefreshOrFirstLoading) {
        BaseApplication.getApplication().getAppAction().getProductListByNormalSort(currentPage, PAGESIZE, new
                ActionCallbackTripleListener<List<SortDataBean>, String, String>() {
                    @Override
                    public void onSuccess(List<SortDataBean> data, String pageIndex, String total) {
                        dialog.cancel();
                        CurrentPage = Integer.parseInt(pageIndex);
                        totalCount = Integer.parseInt(total);
                        if (data.isEmpty()) {
                            isListToBottom = true;
                        }
                        if (totalCount == 0) {
                            adapter.setEmptyView(true, getEmptyView());
                            mSuperRecyclerViewImg.setAdapter(adapter);
                        } else {
                            if (adapter.getData().isEmpty()) {
                                adapter.setNewData(data);
                                adapter.openLoadMore(PAGESIZE);
                                adapter.removeAllFooterView();
                            } else {
                                if (!isRefreshOrFirstLoading) {
                                    Log.d(TAG, "下拉加载前：" + adapter.getData()
                                            .size());
                                    if (isListToBottom) {
                                        mSuperRecyclerViewImg.hideMoreProgress();
                                        adapter.loadComplete();
                                        if (notLoadingView == null) {
                                            notLoadingView = inflater.inflate(R.layout.not_loading,
                                                    (ViewGroup) mSuperRecyclerViewImg.getParent
                                                            (), false);
                                        }
                                        adapter.addFooterView(notLoadingView);//可以暂时先把这个给添加上
                                    } else {
                                        adapter.addData(data);
                                        Log.d(TAG, "下拉加载后：" + adapter.getData()
                                                .size());
                                    }
                                } else {
                                    Log.d(TAG, "刷新");
                                    if (!data.isEmpty()) {
                                        isListToBottom = false;
                                        adapter.setNewData(data);
                                        adapter.openLoadMore(PAGESIZE);
                                        adapter.removeAllFooterView();// 这几个必须添加上
                                    }

                                    Log.d(TAG, "第一次加载后：" + adapter.getData()
                                            .size());
                                }
                            }
                        }
                        mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);//隐藏刷新的图标
                        mSuperRecyclerViewImg.hideMoreProgress(); //数据成功后，上拉加载和下拉刷新的图标去除
                    }
                    @Override
                    public void onFailure(String errorEvent, String errorMessage) {
                        Log.d(TAG, errorEvent);
                        Log.d(TAG, errorMessage);
                        if ("998".equals(errorEvent) && requestCount > 0) {
                            requestCount--;
                            refreshData(isRefreshOrFirstLoading);
                        }else if ("003".equals(errorEvent)) {
                            ToastUtil.networkUnavailable();
                            handleError(isRefreshOrFirstLoading);
                        } else {
                            handleError(isRefreshOrFirstLoading);
                        }

                    }
                });
    }
    /*处理错误情况*/
    private void handleError(boolean isRefreshOrFirstLoading) {
        if (!isRefreshOrFirstLoading){
            adapter.showLoadMoreFailedView();
        }else {
            dialog.cancel();
            List<SortDataBean> cachedData = complexPreferences.getListObject("newest_normal_sort_product_list",
                    SortDataBean.class);
            boolean ishasData = (null != adapter && !adapter.getData().isEmpty());
            boolean ishasCachedData = (null != cachedData && !cachedData.isEmpty());
            if (!ishasData && !ishasCachedData) {
                adapter.setEmptyView(true, getErrorView());
                mSuperRecyclerViewImg.setAdapter(adapter);
            } else if (!ishasData){
                adapter.setNewData(cachedData);
                adapter.openLoadMore(PAGESIZE);
                adapter.removeAllFooterView();
            } else {
                //请求失效，数据不变
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void refreshData(boolean isRefreshOrFirstLoading) {
        if (!isRefreshOrFirstLoading){
            getDataFromServer(CurrentPage+1, false);//如果是下滑加载时一直请求,直到请求成功为止
        }else {
            CurrentPage = 1;
            getDataFromServer(CurrentPage, true);
        }
    }

    /*加载视图*/
    private View getErrorView() {
        if (null==errorView){
            errorView = inflater.inflate(R.layout.frontpage_error_view,
                    (ViewGroup) mSuperRecyclerViewImg.getParent
                            (), false);
            errorView.findViewById(R.id.errorStateButton).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CurrentPage = 1;
                    getDataFromServer(CurrentPage, true);
                }
            });
        }
        return errorView;
    }

    public View getEmptyView() {
        if (null==emptyView){
            emptyView= inflater.inflate(R.layout.frontpage_empty_view,
                    (ViewGroup) mSuperRecyclerViewImg.getParent
                            (), false);
            TextView tv = (TextView) emptyView.findViewById(R.id.errorStateContentTextView);
            tv.setText("易，该区域没有相关宝贝，请换个城市试试！");

        }

        return emptyView;
    }

    private void initAdapter(List<SortDataBean> data) {
        adapter = new ProductListAdapter(data);
        mSuperRecyclerViewImg.addOnItemTouchListener(new com.chad.library.adapter.base.listener
                .OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                dialog.show();
                final SortDataBean bean = adapter.getItem(position);
                final long productID = bean.getProductID();
                requestCount = 15;
                getProduct(productID);
            }
        });
        adapter.setOnLoadMoreListener(this);
        adapter.openLoadMore(PAGESIZE);
        adapter.openLoadAnimation();
    }

    private void getProduct(final long productID) {
        BaseApplication.getApplication().getAppAction().getProduct01(productID, new
                ActionCallbackFivefoldListener<ProductDetailsBean,List<SatisfySendBean>,List<VouchersTemplateBean>,List<ShopAttrBean>, List<DetailsBean.DataBean.CLBean>>() {
            @Override
            public void onSuccess(ProductDetailsBean data, List<SatisfySendBean> data02, List<VouchersTemplateBean> data03, List<ShopAttrBean> data04, List<DetailsBean.DataBean.CLBean> data05) {
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.PRODUCT_ID, productID);
                bundle.putLong(Constants.SHOP_ID, data.getShopID());
                dialog.cancel();
                IntentUtil.gotoActivityWithData(context, ShoppingRadioGroupActivity.class, bundle, false);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    getProduct(productID);
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    dialog.cancel();
                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                    dialog.cancel();
                }

            }
        });
    }


    /*6.设置布局，添加视图*/
    private void setLayout() {
        View view = (RelativeLayout) inflater.inflate(R.layout.tab_front_page02, null);
        ButterKnife.bind(this, view);
        this.addView(view, new LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    /*7.设置界面初始化控件*/
    private void initViews() {
        // 添加头部
        initHeadView();
        // 1）设置广告栏*
        setConveninetBanner();

    }

    private void initHeadView() {

        View headerView = View.inflate(context, R.layout.front_page_custom_header, null);
        adapter.addHeaderView(headerView);
        //https://github.com/saiwu-bigkoo/Android-ConvenientBanner
        convenientBanner = (ConvenientBanner) headerView.findViewById(R.id.convenientBanner);//顶部广告栏控件

        headerView.findViewById(R.id.business_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearch("工商注册");
            }
        });
        headerView.findViewById(R.id.txt_type_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearch("商标专利");
            }
        });
        headerView.findViewById(R.id.intellectual_property).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearch("知识产权");
            }
        });
        headerView.findViewById(R.id.id_foreign_trade_agency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearch("外贸代理");
            }
        });

        headerView.findViewById(R.id.id_legal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearch("法律服务");
            }
        });
        headerView.findViewById(R.id.id_economics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearch("财会税务");
            }
        });
        headerView.findViewById(R.id.id_hongkong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearch("香港公司");
            }
        });
        headerView.findViewById(R.id.id_domestic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearch("国内公司");
            }
        });


    }
    /*直接跳转到搜索界面*/
    private void gotoSearch(String keyword) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_SEARCH, keyword);
        IntentUtil.gotoActivityWithData(context, KeywordSearchActivity.class, bundle,false);  //false 保存页面，true不保存
    }


    private void initRecyclerView() {
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(context));
        mSuperRecyclerViewImg.setRefreshListener(this);
        /*05.设置刷新布局的颜色状态*/
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);


        /*
        * 参考设计：http://blog.csdn.net/u010940300/article/details/49252395
        * http://blog.cgsdream.org/2015/06/11/recyclerview/
        * RecyclerView 滑动检测 （上滑 up）（下滑 down）（顶部 top）（底部 bottom）
        * 监听滑动
        * */
        mSuperRecyclerViewImg.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if( !recyclerView.canScrollVertically(-1)){
                    isColorChanged =true;
                    onHeaderColorChangedListener.onHeaderColorChanged(true);
                }  /*else if (!recyclerView.canScrollVertically(1)) {
                //滑动到底部
                } else if (dy < 0) {
                //向下滑
                } else if (dy > 0) {
                //向上滑
                }*/
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState){
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //滚动停止时
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        //拖拽滚动时
                        if (isColorChanged){
                            onHeaderColorChangedListener.onHeaderColorChanged(false);
                            isColorChanged =false;
                        }

                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //动画滚动时
                        break;
                }

            }
        });
    }
    /*
    * 7.1.设置广告轮播栏
    * 参考设计：http://blog.csdn.net/lmj623565791/article/details/51339751
    * */
    private void setConveninetBanner() {
        //1.初始化网络加载框架
        //2.初始化资源图片   ic_test_01  这里仅仅是名字下标，不要搞错了 当图片没有显示时，肯定是图片索引的问题或者布局加载的问题，名字不要搞错
        for (int position = 1; position < 5; position++) {
            localImageslist.add(getResId("ic_test_0" + position, R.drawable.class));
        }
        /*
        * 3.加载图片，设置轮播的页面,添加各种属性里面封装了一个ViewPager
        * @params ViewHolder  创建item视图，并且保存视图控件
        * @params List<T> datas  数据集合，准确说在这里是图片集合
        * */
        if (null!=convenientBanner){
            convenientBanner.setPages(new CBViewHolderCreator() {
                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, localImageslist)
                    //4.设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                    .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                    //5.设置指示器的方向  暂时放右边
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
//                .setOnItemClickListener(this)  暂时取消注册
                    .startTurning(3000);
            //6.设置翻页的效果，不需要翻页效果可用不设，这里存在白屏问题，很飘，不好看，而且也没有滑动时的缩放效果，不自动循环播放
            //7.setPageTransformer(new ZoomOutPageTransformer()); 看来就是这个效果的问题了，以后改进
        }

    }

    /*
    *8.利用反射和泛型通过资源类以及文件名获取资源ID
    *@param name 具体的资源文件名，也就是变量名
    *@param c 类名  建议是R资源内部类
    * */
    private int getResId(String name, Class<?> c) {
        try {
            // 1）关键字可是不能随便作为参数,局部变量，通过变量名获取类的具体变量域 Returns a  Field object with the given field name which is
            // declared in the class【客户端】
            Field idField = c.getDeclaredField(name);
            // 2）要的是具体的变量值 也就是资源编号ab_front_page_index_demo_04=0x7f020000  通过变量名获取相应的变量值
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("fumin", "获取资源Id失败");
            return -1;
        }
    }


    /*10.Viewpage页面的点击*/
    @Override
    public void onItemClick(int position) {
    }


    @Override
    public void onRefresh() {
        dialog.show();
        Log.d(TAG, "因为是刷新，所以需要把数据库和缓存中的数据都清楚掉，再调用一次接口");
        CurrentPage =1;
        requestCount = 15;
        getDataFromServer(CurrentPage, true);

    }

    @Override
    public void onLoadMoreRequested() {
        //没有网络的情况下再来下拉刷新，则直接把下拉给隐藏。
        requestCount = 15;
        getDataFromServer(CurrentPage + 1, false);

    }
    /*用于触发head 变色*/
    OnHeaderColorChangedListener onHeaderColorChangedListener;
    public interface OnHeaderColorChangedListener {
        void onHeaderColorChanged(boolean isTop);
    }
    public void setOnHeaderColorChangedListener(OnHeaderColorChangedListener onHeaderColorChangedListener) {
        this.onHeaderColorChangedListener = onHeaderColorChangedListener;
    }

}
