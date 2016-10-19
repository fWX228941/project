package com.android.haobanyi.activity.shopping.store;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.BaseFragment;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.shopping.product.ShoppingRadioGroupActivity;
import com.android.haobanyi.adapter.shop.StoreServiceListAdapter;
import com.android.haobanyi.core.ActionCallbackFivefoldListener;
import com.android.haobanyi.core.ActionCallbackQuadrupleListener;
import com.android.haobanyi.core.ActionCallbackTripleListener;
import com.android.haobanyi.core.service.NTPTime;

import com.android.haobanyi.model.bean.shopping.product.DetailsBean;
import com.android.haobanyi.model.bean.shopping.product.ProductDetailsBean;
import com.android.haobanyi.model.bean.shopping.product.SatisfySendBean;
import com.android.haobanyi.model.bean.shopping.product.ShopAttrBean;
import com.android.haobanyi.model.bean.shopping.product.VouchersTemplateBean;
import com.android.haobanyi.model.bean.shopping.store._ShopRelatedServiceBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/7/20.
 *
 * @作者: 付敏
 * @创建日期：2016/07/20
 * @邮箱：466566941@qq.com
 * @当前文件描述：店铺首页   获取综合排序的店铺服务列表  getShopProductListByNormalSort
 */
public class StoreServiceListFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "StoreServiceListFragment";
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;

    private int CurrentPage = 0;// 默认从1开始
    private int PAGESIZE = 10; // 定义的一页多少条数据
    private StoreServiceListAdapter adapter = new StoreServiceListAdapter(null) ; // 难道适配器不能复用？
//    private List<_ShopRelatedServiceBean> listData = new ArrayList<_ShopRelatedServiceBean>();//不初始化，默认就是空了
    private List<_ShopRelatedServiceBean> cachedlistData;   // 优化的，把这边都换成sortBean 啊！
    private int totalCount = 0 ;
    private long shopId=-1;
    private boolean isListToBottom = false; //数据是否到顶了
    private View notLoadingView;
    private LayoutInflater inflater;
    private boolean ishasCachedData = false;//缓存是否有数据
    private boolean ishasData = false;//当前是否有数据

    @Override
    protected void lazyload() {

    }

    @Override
    protected int setLayout() {
        return R.layout.tab_front_page02;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        NTPTime.getInstance().getCurrentTime();
        //01.先获取shopId
        shopId = getActivity().getIntent().getLongExtra(Constants.SHOP_ID, PreferenceUtil.getSharedPreference(getActivity(), "preferences").getLong(Constants.SHOP_ID, -1l));
        Log.d("ShoppingServiceFragment", "在storeFrontPageshopId:" + shopId);
        //02.先加载缓存数据。在网络加载失效，没有数据的情况下才会如此，再有网的情况下，我还是希望每次都是最新刷新
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(getActivity(), "shop");
        cachedlistData = complexPreferences.getListObject("shopId"+shopId+"newest_Page", _ShopRelatedServiceBean.class);
        ishasCachedData = (null != cachedlistData && !cachedlistData.isEmpty())?true:false;
        //03.初始化列表，并且调用服务器，绑定数据
        initRecyclerView();
        //05.更新刷新页下标
        CurrentPage =0;
        /*
        * 分页，下拉刷新，只是多了一个预加载
        * */
        if (ishasCachedData){
            initAdapter(cachedlistData);
        } else {
            if (shopId!=-1){
                requestCount = 5;
                getDataFromServer(1, true);
            }
        }
    }
    private void initAdapter(List<_ShopRelatedServiceBean> listData) {
        adapter = new StoreServiceListAdapter(listData);
        if (null== mSuperRecyclerViewImg.getAdapter()){
            adapter.openLoadAnimation();
            adapter.setOnLoadMoreListener(this);
            adapter.openLoadMore(PAGESIZE);
            mSuperRecyclerViewImg.setAdapter(adapter);
            mSuperRecyclerViewImg.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                    final _ShopRelatedServiceBean bean = (_ShopRelatedServiceBean) adapter.getItem(position);
                    final long productID = bean.getProductID();
                    requestCount = 5;
                    getProduct(bean, productID);
                }
            });
/*            adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                *//*posion从0开始是依次递增的*//*
                public void onItemClick(View view, int position) {
                    final _ShopRelatedServiceBean bean = (_ShopRelatedServiceBean) adapter.getItem(position);
                    final long productID = bean.getProductID();
                    requestCount = 5;
                    getProduct(bean, productID);

                }
            });*/


        }

    }

    private void getProduct(final _ShopRelatedServiceBean bean, final long productID) {
        this.appAction.getProduct01(productID, new
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
                        if ("998".equals(errorCode) && requestCount > 0) {
                            requestCount--;
                            getProduct(bean, productID);
                        } else if ("003".equals(errorCode)) {
                            ToastUtil.showShort("网络异常，请检查网络");
                        } else {
                            Toast.makeText(getActivity(), "异常代码：" + errorCode +
                                    " 异常说明: " + errorMessage, Toast
                                    .LENGTH_SHORT).show();
                            ToastUtil.showShort("加载失败，请重新点击加载");
                        }
                    }
                });
    }

    private void initRecyclerView() {
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSuperRecyclerViewImg.setRefreshListener(this);
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        //调用服务器



    }

    private void getDataFromServer(final int currentPage, final boolean isRefreshOrFirstLoading) {
        if (isRefreshOrFirstLoading) {
            progressActivity.showLoading();//会一直刷新 先测试下
        }
        this.appAction.getShopProductListByNormalSort(shopId, currentPage, PAGESIZE, new
                ActionCallbackTripleListener<List<_ShopRelatedServiceBean>, String, String>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(List<_ShopRelatedServiceBean> data, String pageIndex, String total) {
                        progressActivity.showContent();
                        CurrentPage = Integer.parseInt(pageIndex);
                        totalCount = Integer.parseInt(total);
                        //首先就得判断是否有数据，因为这个店在这个区域可能是没有任何服务的
                        if (data.isEmpty()) {
                            isListToBottom = true;
                        }
                        if (totalCount == 0) {
                            initAdapter(data);
                            adapter.setEmptyView(true, getEmptyView());//如果是希望可以刷新的就用adapter
                        } else {
                            if (adapter == null) {
                                adapter = new StoreServiceListAdapter(getActivity(), null);
                            }

                            if (adapter.getData().isEmpty()) {
                                Log.d(TAG, "1");
                                initAdapter(data);
                            } else {

                                //如果放在这里进行替换呢？
  /*                              if (cachedlistData != null) {
                                    cachedlistData.clear();
                                }*/

                                Log.d(TAG, "2");
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
                                    Log.d(TAG, "3");
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


                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(String errorEvent, String errorMessage) {
                        //01.因为各种异常情况没有获取到数据
                        //02.断网
                        //03.长期不登录导致的token失效
                        //04.两种情况下加载缓存数据，一种是失效情况下，还有一种是频繁加载
                        Log.d(TAG, errorEvent);
                        if ("998".equals(errorEvent) && requestCount > 0) {
                            requestCount--;
                            refresh(isRefreshOrFirstLoading);
                        } else if ("003".equals(errorEvent)) {

                            handleError(isRefreshOrFirstLoading,"易，请检查网络");
                        } else {
                            handleError(isRefreshOrFirstLoading, "加载失败，请重新点击加载");
                            Toast.makeText(getActivity(), "异常代码：" + errorEvent + " 异常说明: " + errorMessage, Toast
                                    .LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleError(boolean isRefreshOrFirstLoading,String Message) {
        if (!isRefreshOrFirstLoading){
            adapter.showLoadMoreFailedView();
        }else {
            ishasData = (!adapter.getData().isEmpty()) ? true : false;
            if (!ishasData && !ishasCachedData) {
                showError( Message);
            } else if (!ishasData && ishasCachedData){
                adapter.setNewData(cachedlistData);//有数据才会更新，没有数据是不会更新的
                adapter.openLoadMore(PAGESIZE);
                adapter.removeAllFooterView();// 这几个必须添加上

            }else {
                //请求失效，数据不变
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();
                progressActivity.showContent();
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void showError(String Message) {
        progressActivity.showError(Message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentPage = 0;
                getDataFromServer(1, true);
            }
        });
    }

    private void refresh(boolean isRefreshOrFirstLoading) {
        if (!isRefreshOrFirstLoading){
            getDataFromServer(CurrentPage+1, false);//如果是下滑加载时一直请求,直到请求成功为止
        } else {
         /*   ishasData = (!adapter.getData().isEmpty()) ? true : false;
            if (!ishasData && !ishasCachedData){*/
                getDataFromServer(CurrentPage, true);
            /*} else {
                //请求失效，数据不变
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();
                progressActivity.showContent();
                adapter.notifyDataSetChanged();
            }*/
        }


    }

    private View getEmptyView() {
        inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_empty_view,
                (ViewGroup) mSuperRecyclerViewImg.getParent
                        (), false);
        TextView tv = (TextView) view.findViewById(R.id.empty_content);
        tv.setText("易，该商家很懒，暂时还没有放置任何服务，请换家店铺试试！");
        return view;
    }


    @Override
    public void onRefresh() {
        CurrentPage =1;
        requestCount = 5;
        getDataFromServer(CurrentPage, true);
    }

    @Override
    public void onLoadMoreRequested() {
        requestCount = 5;
        getDataFromServer(CurrentPage + 1, false);
    }


}
