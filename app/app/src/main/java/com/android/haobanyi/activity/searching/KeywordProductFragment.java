package com.android.haobanyi.activity.searching;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.BaseFragment;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.collection.ShopCollectionActivity;
import com.android.haobanyi.activity.shopping.product.ShoppingRadioGroupActivity;
import com.android.haobanyi.adapter.ProductListAdapter;

import com.android.haobanyi.core.ActionCallbackFivefoldListener;
import com.android.haobanyi.core.ActionCallbackQuadrupleListener;
import com.android.haobanyi.core.ActionCallbackTripleListener;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.shopping.product.DetailsBean;
import com.android.haobanyi.model.bean.shopping.product.ProductDetailsBean;
import com.android.haobanyi.model.bean.shopping.product.SatisfySendBean;
import com.android.haobanyi.model.bean.shopping.product.ShopAttrBean;
import com.android.haobanyi.model.bean.shopping.product.VouchersTemplateBean;

import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.R.attr.cacheColorHint;
import static android.R.attr.category;

/**
 * Created by fWX228941 on 2016/8/5.
 *
 * @作者: 付敏
 * @创建日期：2016/08/05
 * @邮箱：466566941@qq.com
 * @当前文件描述：服务综合
 */
public class KeywordProductFragment  extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener,SwipeRefreshLayout.OnRefreshListener  {
    private static final String TAG = "KeywordProductFragment";
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    String keyWord; //用户输入,这个就是关键字
    private int CurrentPage = 0;// 默认从1开始
    private int PAGESIZE = 10; // 定义的一页多少条数据
    private int totalCount = 0 ;
    private boolean ishasData = false;//当前是否有数据
    private boolean isListToBottom = false; //数据是否到顶了
    private View notLoadingView;
    private ProductListAdapter adapter = new ProductListAdapter( null);;//直接初始化，可以省去很多麻烦
    private LayoutInflater inflater;
    private int sortType= 0;
    private boolean sortMode =false;
/*    String mCategory;
    private String[] mCategoryArray;*/
    private int position =0;
//    private boolean onlyForOnce =true;//每次创建销毁，没有什么很大的意义，干脆

    @Override
    protected void lazyload() {

    }

    @Override
    protected int setLayout() {
        return R.layout.tab_front_page02;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
//        position = FragmentPagerItem.getPosition(getArguments());  这个有问
        KeywordSearchActivity activity = (KeywordSearchActivity) getActivity();
        activity.setOnPageChangedListener(new KeywordSearchActivity.OnPageChangedListener() {
            @Override
            public void onPassPosition(int pos) {
//                position =pos;
                handleLogic();
                Log.d(TAG, "position01:" + position);
            }
        });
//KeywordProductFragment





        if (basePreference.getBoolean("onlyForOnce",false)){
            Log.d(TAG, "只有第一次执行");
            handleLogic();
            basePreference.putBoolean("onlyForOnce",false);
        }

    }

    private void handleLogic() {
/*        mCategoryArray = getResources().getStringArray(R.array.keyword_list);
        mCategory = mCategoryArray[position];*/
        initRecyclerView();

        keyWord = getActivity().getIntent().getStringExtra(Constants.USER_SEARCH);

        CurrentPage = 1;
        requestCount = 45;
        Log.d(TAG, "position01:" + position); //分别传递123过来  这个我得看看
        getDataFromServer(position,keyWord,CurrentPage, true);
    }

    private void getDataFromServer(int position,final String keyWord,final int currentPage,final boolean isRefreshOrFirstLoading) {
        if (isRefreshOrFirstLoading) {
            progressActivity.showLoading();//会一直刷新 先测试下
        }

    /*尝试复用失败了，伤感*/
        switch (position){
            case 0:
                Log.d(TAG, "00");
                sortType= 0;
                sortMode =false;
            break;
            case 2:
                Log.d(TAG, "02");
                //价格递增
                sortType= 1;
                sortMode =true;
                break;
            case 3:
                Log.d(TAG, "03");
                sortType= 1;
                sortMode =false;
                break;
        }




        this.appAction.searchProductByKeyword(keyWord, currentPage, PAGESIZE, sortType,sortMode, new
                ActionCallbackTripleListener<List<SortDataBean>, String, String>() {
                    @Override
                    public void onSuccess(List<SortDataBean> data, String pageIndex, String total) {
                        progressActivity.showContent();
                        CurrentPage = Integer.parseInt(pageIndex);
                        totalCount = Integer.parseInt(total);
                        Log.d(TAG, "totalCount:" + totalCount);
                        if (data.isEmpty()) {
                            isListToBottom = true;
                        }

                        if (totalCount == 0) {
                            // 关键字没有搜索到服务
                            //adapter.setEmptyView(true, getEmptyView());   //activity和fragment就用progressActivity
                            // 而view 就用adapter
                            progressActivity.showEmpty("易，暂时没有搜索到相关的服务，请换个词试试，或者去店铺列表看看，可能有您需要的！");
                        } else {
                            // 关键字搜索到了服务
                            if (adapter.getData().isEmpty()) {
                                initAdapter(data);
                            } else {
                                if (!isRefreshOrFirstLoading) {
                                    if (isListToBottom) {

                                        adapter.loadComplete();
                                        if (notLoadingView == null) {
                                            inflater = LayoutInflater.from(getActivity());
                                            notLoadingView = inflater.inflate(R.layout.not_loading,
                                                    (ViewGroup) mSuperRecyclerViewImg.getParent
                                                            (), false);
                                        }
                                        adapter.addFooterView(notLoadingView);


                                    } else {
                                        adapter.addData(data);
                                    }
                                } else {
                                    isListToBottom = false;
                                    if (!data.isEmpty()) {
                                        adapter.setNewData(data);//有数据才会更新，没有数据是不会更新的
                                        adapter.openLoadMore(PAGESIZE);
                                        adapter.removeAllFooterView();// 这几个必须添加上
                                    }
                                }
                            }
                        }
                        mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                        mSuperRecyclerViewImg.hideMoreProgress();
                    }

                    @Override
                    public void onFailure(String errorEvent, String errorMessage) {
                        if ("998".equals(errorEvent) && requestCount > 0) {
                            requestCount--;
                            refresh(keyWord, isRefreshOrFirstLoading);
                        } else if ("003".equals(errorEvent)) {
                            ToastUtil.networkUnavailable();
                            handleError(keyWord,isRefreshOrFirstLoading, "易，请检查网络");
                        } else {
                            ToastUtil.showErrorMessage(errorEvent, errorMessage);
                            handleError(keyWord,isRefreshOrFirstLoading, "加载失败，请重新点击加载");
                        }

                    }
                });

    }
    private void handleError(String keyWord,boolean isRefreshOrFirstLoading,String Message) {
        if (!isRefreshOrFirstLoading){
            adapter.showLoadMoreFailedView();
        }else {
            ishasData = (!adapter.getData().isEmpty()) ? true : false;
            if (!ishasData) {
                showError(keyWord, Message);
            } else {
                //请求失效，数据不变
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();
                progressActivity.showContent();
                adapter.notifyDataSetChanged();
            }
        }
    }


    private void showError(final String keyWord,String Message) {
        progressActivity.showError(Message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentPage = 1;
                Log.d(TAG, "position03:" + position);
                getDataFromServer(position,keyWord, CurrentPage, true);
            }
        });
    }

    private void refresh(final String keyWord,boolean isRefreshOrFirstLoading)  {
        if (!isRefreshOrFirstLoading){
            getDataFromServer(position,keyWord,CurrentPage + 1, false);
        } else {

            getDataFromServer(position,keyWord, CurrentPage, true);
        }


    }

    private void initAdapter(List<SortDataBean> listData) {
        adapter = new ProductListAdapter(getActivity(), listData);
        if (null== mSuperRecyclerViewImg.getAdapter()){
            adapter.openLoadAnimation();
            adapter.setOnLoadMoreListener(this);
            adapter.openLoadMore(PAGESIZE);
            mSuperRecyclerViewImg.setAdapter(adapter);
            mSuperRecyclerViewImg.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                    final SortDataBean bean = (SortDataBean) adapter.getItem(position);
                    final long productID = bean.getProductID();
                    requestCount = 45;

                    getProduct(bean, productID);

                }
            });


        }

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
                        IntentUtil.gotoActivityWithData(getActivity(), ShoppingRadioGroupActivity
                                        .class,
                                bundle, false);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {

                        if ("998".equals(errorCode) && requestCount > 0){
                            requestCount--;
                            getProduct(bean, productID);
                        } else if ("003".equals(errorCode)) {
                            ToastUtil.networkUnavailable();
                        }else {
                            ToastUtil.showErrorMessage(errorCode, errorMessage);
                        }

                    }
                });
    }

    private void initRecyclerView() {
    /*01.循环视图设置布局管理器*/
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*03.设置下拉刷新的监听器*/
        mSuperRecyclerViewImg.setRefreshListener(this);
        /*05.设置刷新布局的颜色状态*/
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
    }
    @Override
    public void onRefresh() {
        requestCount = 45;
        CurrentPage=1;
        Log.d(TAG, "position04:" + position);
        getDataFromServer(position,keyWord, CurrentPage, true);
    }

    @Override
    public void onLoadMoreRequested() {
        requestCount = 45;
        Log.d(TAG, "position05:" + position);
        getDataFromServer(position,keyWord,CurrentPage + 1, false);

    }

/*    public View getEmptyView() {
        inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_empty_view,
                (ViewGroup) mSuperRecyclerViewImg.getParent
                        (), false);
        TextView tv = (TextView) view.findViewById(R.id.empty_content);
        tv.setText("易，暂时没有搜索到相关的服务，请换个词试试，或者去店铺列表看看，可能有您需要的！");
        return view;
    }*/
}
