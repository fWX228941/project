package com.android.haobanyi.activity.collection;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.shopping.product.ShoppingRadioGroupActivity;
import com.android.haobanyi.adapter.ProductListAdapter;
import com.android.haobanyi.core.ActionCallbackFivefoldListener;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.ActionCallbackQuadrupleListener;
import com.android.haobanyi.core.ActionCallbackTripleListener;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.shopping.product.DetailsBean;
import com.android.haobanyi.model.bean.shopping.product.ProductDetailsBean;
import com.android.haobanyi.model.bean.shopping.product.SatisfySendBean;
import com.android.haobanyi.model.bean.shopping.product.ShopAttrBean;
import com.android.haobanyi.model.bean.shopping.product.VouchersTemplateBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.android.haobanyi.view.TitleBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

import static com.baidu.location.b.g.T;

/**
 * Created by fWX228941 on 2016/8/5.
 *
 * @作者: 付敏
 * @创建日期：2016/08/05
 * @邮箱：466566941@qq.com
 * @当前文件描述： 统一一下，收藏的服务   tab_collection_page
 * 08-19 17:40:18.107 20492-20492/? E/RecyclerView: No layout manager attached; skipping layout
 * 这个是我遇到的可以位列前10的奇葩葩葩问题惹
 *initRecyclerView  这个没有添加，终于找到原因了
 *
 */
public class CollectionPageView extends RelativeLayout implements BaseQuickAdapter.RequestLoadMoreListener,SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    //1.上下文
    private Context context;
    //2.布局加载器
    private LayoutInflater inflater;
    private boolean isLogin = false;
    PreferenceUtil complexPreferences;
    RadioGroupActivity activity = new RadioGroupActivity();
    private static final int LOGIN_TO_SPV = 3; // 与购物车界面共享同一个功能  ，这个会不会出现混乱，得验证下
    private int CurrentPage = 0;// 默认从1开始
    private int PAGESIZE = 5; // 定义的一页多少条数据
    private int totalCount = 0 ;
    private boolean ishasData = false;//当前是否有数据
    private boolean isListToBottom = false; //数据是否到顶了
    private View notLoadingView;
    private ProductListAdapter adapter = new ProductListAdapter(null);//直接初始化，可以省去很多麻烦
    private long requestCount = 5;
    /*删除的弹框*/
    SweetAlertDialog pDialog;
    SpotsDialog dialog;
    SortDataBean bean;

    public CollectionPageView(Context context) {
        super(context);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.activity = (RadioGroupActivity) context;
        init();
    }

    public CollectionPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.activity = (RadioGroupActivity) context;
        init();
    }

    private void init() {
        dialog =  new SpotsDialog(context);
        dialog.show();
        setLayout();
        initTittleBar();
        initRecyclerView();

        complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);
        Log.d("CollectionPageView", "isLogin:" + isLogin);//后退后这里就没有被执行到
        if (isLogin) {
            long refreshTime = complexPreferences.getLong(Constants.START_TIME, -1000l);
            if (System.currentTimeMillis() / 1000L - refreshTime > 7200) {
                //超过过期时间，更新token
                requestCount = 5;
                getToken();
            } else {
                handleLogic();
            }

        } else {
            handleLogic();
        }

        activity.setOnRreshChangedListener(new RadioGroupActivity.OnRreshListener() {
            @Override
            public void onRresh() {
                Log.d("CollectionPageView", "点击后腿时，被触发");
                // 触发后，应该重新获取login的状态值
                isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);//这一行不能少
                handleLogic();//这个貌似没有被执行，这个监听失效了
            }
        });

    }

    private void getToken() {
        BaseApplication.getApplication().getAppAction().getToken(new ActionCallbackListener<LoginResponseResult>() {
            @Override
            public void onSuccess(LoginResponseResult data) {
                handleLogic();
            }

            /*
            * token的情况确实需要特殊处理，即使token更新失败，也往下处理逻辑
            *
            * */
            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    getToken();
                } else {
                    handleLogic();
                }
            }
        });
    }

    private void loginForToken() {
        complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(activity, LoginActivity.class, LOGIN_TO_SPV);//跳转到登录界面
    }

    private void handleLogic() {
        Log.d("CollectionPageView", "触发后isLogin:" + isLogin);
        if (isLogin){
            CurrentPage =1;
            requestCount = 35;

            // 退回来的逻辑还是在这里面，没有走其他
            Log.d("CollectionPageView", "beizhixing");
            getDataFromServer(CurrentPage, true); //获取服务列表
        }else {
            dialog.cancel();
            progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看我收藏的服务！");
        }

    }
    private void initRecyclerView() {
    /*01.循环视图设置布局管理器*/
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(context));
        /*03.设置下拉刷新的监听器*/
        mSuperRecyclerViewImg.setRefreshListener(this);
        /*05.设置刷新布局的颜色状态*/
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
    }

    private void getDataFromServer(final int currentPage, final boolean isRefreshOrFirstLoading) {
        if (isRefreshOrFirstLoading) {
            dialog.show();
        }
        BaseApplication.getApplication().getAppAction().getFavoriteProductList(currentPage, new
                ActionCallbackTripleListener<List<SortDataBean>, String, String>() {
                    @Override
                    public void onSuccess(List<SortDataBean> data, String pageIndex, String total) {
                        progressActivity.showContent();
                        dialog.cancel();
                        CurrentPage = Integer.parseInt(pageIndex);
                        totalCount = Integer.parseInt(total);
                        if (data.isEmpty()) {
                            isListToBottom = true;
                        }
                        if (totalCount == 0) {
                            // 关键字没有搜索到服务
                            adapter.setEmptyView(true, getEmptyView()); //这个使用报错
                            mSuperRecyclerViewImg.setAdapter(adapter);
                        } else {
                            // 关键字搜索到了服务
                            if (adapter.getData().isEmpty()) {
                                initAdapter(data);
                            } else {

                                if (!isRefreshOrFirstLoading) {
                                    if (isListToBottom) {
                                        adapter.loadComplete();
                                        if (notLoadingView == null) {
                                            notLoadingView = inflater.inflate(R.layout.not_loading,
                                                    (ViewGroup) mSuperRecyclerViewImg.getParent
                                                            (), false);
                                        }
                                        adapter.addFooterView(notLoadingView);//可以暂时先把这个给添加上
                                    } else {
                                        adapter.addData(data);
                                    }
                                } else {
                                    isListToBottom = false;
                                    if (!data.isEmpty()) {
                                        adapter.setNewData(data);
                                        adapter.openLoadMore(PAGESIZE);
                                        adapter.removeAllFooterView();// 这几个必须添加上
                                    }
                                }
                            }
                        }
                        mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);//隐藏刷新的图标  这个位置千万不能变更，一旦出现 {"code":101,"message":"获取成功","data":{"totalCount":2,"pageIndex":1,"pageSize":10,"list":[]}}这种没有数据的情况
                        mSuperRecyclerViewImg.hideMoreProgress();
                    }

                    @Override
                    public void onFailure(String errorEvent, String errorMessage) {
                        Log.d("CollectionPageView", errorEvent);
                        if ("998".equals(errorEvent) && requestCount > 0) {
                            requestCount--;
                            refresh(isRefreshOrFirstLoading);
                        } else if ("003".equals(errorEvent)) {
                            dialog.cancel();
                            ToastUtil.networkUnavailable();
                            handleError(isRefreshOrFirstLoading,"易，请检查网络");
                        } else if ("996".equals(errorEvent)) {
                            dialog.cancel();
                            ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                            loginForToken();
                        } else {
                            dialog.cancel();
                            ToastUtil.showErrorMessage(errorEvent, errorMessage);
                            handleError(isRefreshOrFirstLoading, "加载失败，请重新点击加载");
                        }

                    }
                });


    }
    /*处理错误情况*/
    private void handleError(boolean isRefreshOrFirstLoading,String Message) {
        if (!isRefreshOrFirstLoading){
            adapter.showLoadMoreFailedView();
        }else {
            ishasData = (!adapter.getData().isEmpty()) ? true : false;
            if (!ishasData) {
                showError(Message);
            } else {
                //请求失效，数据不变
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();
                progressActivity.showContent();
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void showError(String message) {
        progressActivity.showError(message, new OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentPage = 1;
                requestCount = 35;
                getDataFromServer(CurrentPage, true);
            }
        });
    }

    private void refresh(boolean isRefreshOrFirstLoading) {
        if (!isRefreshOrFirstLoading){
            getDataFromServer(CurrentPage+1, false);
        } else {
            getDataFromServer(CurrentPage, true);
        }

    }

    private void initAdapter(List<SortDataBean> listData) {
        adapter = new ProductListAdapter(listData);
        if (null== mSuperRecyclerViewImg.getAdapter()){
            adapter.openLoadAnimation();
            adapter.setOnLoadMoreListener(this);
            adapter.openLoadMore(PAGESIZE);
            mSuperRecyclerViewImg.setAdapter(adapter);





            mSuperRecyclerViewImg.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                    dialog.show();
                    bean = (SortDataBean) adapter.getItem(position);
                    final long productID = bean.getProductID();
                    requestCount = 15;
                    getProduct(bean, productID);
                }
            });
            ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
            itemTouchHelper.attachToRecyclerView(mSuperRecyclerViewImg.getRecyclerView());
            itemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            adapter.enableSwipeItem();
            adapter.setOnItemSwipeListener(new OnItemSwipeListener() {
                @Override
                public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, final int position) {
                    pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                    pDialog.setTitleText("确定要删除吗？")
                            .setConfirmText("删除")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    pDialog.dismissWithAnimation();
                                    bean = (SortDataBean) adapter.getItem(position);
                                    requestCount = 15;
                                    deleteFavProduct(position);

                                }
                            })
                            .setCancelText("取消")
                            .show();
                    pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            adapter.notifyDataSetChanged();
                            pDialog.dismissWithAnimation();
                        }
                    });


                }

                @Override
                public void clearView(RecyclerView.ViewHolder viewHolder, int positon) {

                }

                @Override
                public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int positon) {

                }

                @Override
                public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1,
                                              boolean b) {

                }
            });

        }

    }

    private void deleteFavProduct(final int position) {
        BaseApplication.getApplication().getAppAction().deleteFavProduct(bean
                .getProductID(), new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                Log.d("CollectionPageView", "成功删除");
                adapter.remove(position);
                complexPreferences.putBoolean("is_refresh_minePage", false);
                //如果删除到没有数据了，则为空
                if (null == adapter.getData()){
                    progressActivity.showEmpty("易，暂时还没有收藏任何服务！");
                }

            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {

                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    deleteFavProduct(position);
                } else if ("003".equals(errorCode)) {
                    Toast.makeText(context, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "异常代码：" + errorCode + " 异常说明: " + errorMessage, Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void getProduct(final SortDataBean bean, final long productID) {
        BaseApplication.getApplication().getAppAction().getProduct01(productID, new
                ActionCallbackFivefoldListener<ProductDetailsBean, List<SatisfySendBean>,
                                        List<VouchersTemplateBean>, List<ShopAttrBean>, List<DetailsBean.DataBean.CLBean>>() {
                    @Override
                    public void onSuccess(ProductDetailsBean data, List<SatisfySendBean> data02,
                                          List<VouchersTemplateBean> data03, List<ShopAttrBean>
                                                  data04, List<DetailsBean.DataBean.CLBean> data05) {
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constants.PRODUCT_ID, productID);
                        dialog.cancel();
                        IntentUtil.gotoActivityWithData(context, ShoppingRadioGroupActivity
                                        .class,
                                bundle, false);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {

                        Log.d("CollectionPageView", errorCode);
                        if ("998".equals(errorCode) && requestCount>0) {
                            requestCount--;
                            getProduct(bean, productID);
                        } else if ("003".equals(errorCode)) {
                            dialog.cancel();
                            ToastUtil.networkUnavailable();
                        } else {
                            dialog.cancel();
                            ToastUtil.showErrorMessage("异常代码：" + errorCode + " 异常说明: " + errorMessage);                        }

                    }
                });
    }

    private void setLayout() {
        View view = (RelativeLayout) inflater.inflate(R.layout.tab_collection_page, null);
        ButterKnife.bind(this, view);
        this.addView(view, new LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void initTittleBar() {
        titleBar.setBackgroundResource(R.color.white);
        titleBar.setTitle("收藏列表");
        titleBar.setDividerColor(Color.GRAY);
    }
    public View getEmptyView() {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_empty_view,
                (ViewGroup) mSuperRecyclerViewImg.getParent
                        (), false);
        TextView tv = (TextView) view.findViewById(R.id.empty_content);
        tv.setText("易，暂时还没有收藏任何服务！");
        return view;
    }


    public View getEmptyView(String hint) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_empty_view,
                (ViewGroup) mSuperRecyclerViewImg.getParent
                        (), false);
        TextView tv = (TextView) view.findViewById(R.id.empty_content);
        tv.setText(hint);
        return view;
    }
    @Override
    public void onRefresh() {
        CurrentPage = 1;
        requestCount = 10;
        getDataFromServer( CurrentPage, true);
    }

    @Override
    public void onLoadMoreRequested() {
        requestCount = 10;
        getDataFromServer(CurrentPage + 1, false);

    }
}
