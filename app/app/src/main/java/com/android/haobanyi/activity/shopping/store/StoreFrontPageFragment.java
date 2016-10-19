package com.android.haobanyi.activity.shopping.store;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.android.haobanyi.adapter.ProductListAdapter;
import com.android.haobanyi.core.ActionCallbackDoubleListener;
import com.android.haobanyi.core.ActionCallbackFivefoldListener;
import com.android.haobanyi.core.ActionCallbackQuadrupleListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.shopping.product.DetailsBean;
import com.android.haobanyi.model.bean.shopping.product.ProductDetailsBean;
import com.android.haobanyi.model.bean.shopping.product.SatisfySendBean;
import com.android.haobanyi.model.bean.shopping.product.ShopAttrBean;
import com.android.haobanyi.model.bean.shopping.product.VouchersTemplateBean;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/7/8.
 *
 * @作者: 付敏
 * @创建日期：2016/07/08
 * @邮箱：466566941@qq.com
 * @当前文件描述：店铺下的热门服务   热门服务就用SortDataBean  商家信息就用 ShopBean
 *
 * 只有两种情况下才会有缓存，一种是提前预加载，一种是请求失效
 *
 */
public class StoreFrontPageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener  {
    private static final String TAG = "minfu";
    private long shopId=-1;
    private ProductListAdapter adapter= new ProductListAdapter(null);
//    private List<SortDataBean> listData = new ArrayList<SortDataBean>();//不初始化，默认就是空了
    private List<SortDataBean> cachedlistData;
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    private boolean ishasData = false;//当前是否有数据
    private boolean ishasCachedData = false;//缓存是否有数据
    private int totalCount = 0 ;
    private LayoutInflater inflater;
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
        shopId = getActivity().getIntent().getLongExtra(Constants.SHOP_ID, PreferenceUtil.getSharedPreference(getActivity(), "preferences").getLong(Constants.SHOP_ID, -1l)); //这个值是为1的\
        Log.d("ShoppingServiceFragment", "在storeFrontPageshopId:" + shopId);
        PreferenceUtil complexPreferences01 = PreferenceUtil.getSharedPreference(getActivity(),
                "shop");
        cachedlistData = complexPreferences01.getListObject("shopid" + shopId + "HPList", SortDataBean.class);
        ishasCachedData = (null != cachedlistData && !cachedlistData.isEmpty())?true:false;
        /*
        * 1.已经存在预加载，有数据则加载，没有数据则重新获取
        *
        * */
        //03.初始化列表，并且调用服务器，绑定数据
        initRecyclerView();
        if (ishasCachedData){
            initAdapter(cachedlistData);
        } else {
            if (shopId!=-1){
                requestCount = 5;
                getDataFromServer(shopId, true);
            }
        }

    }

    private void initAdapter(List<SortDataBean> listData) {
        adapter = new ProductListAdapter(listData);
        if (null== mSuperRecyclerViewImg.getAdapter()){
            adapter.openLoadAnimation();
            mSuperRecyclerViewImg.setAdapter(adapter);

            mSuperRecyclerViewImg.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                    final SortDataBean bean = (SortDataBean) adapter.getItem(position);
                    final long productID = bean.getProductID();
                    requestCount = 5;
                    getProduct(bean, productID);
                }
            });
/*            adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                *//*posion从0开始是依次递增的*//*
                public void onItemClick(View view, int position) {
                    final SortDataBean bean = (SortDataBean) adapter.getItem(position);
                    final long productID = bean.getProductID();
                    requestCount = 5;
                    getProduct(bean, productID);
                }
            });*/

            ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
            itemTouchHelper.attachToRecyclerView(mSuperRecyclerViewImg.getRecyclerView());
            itemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            adapter.enableSwipeItem();
            adapter.setOnItemSwipeListener(new OnItemSwipeListener() {
                @Override
                public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) {


                }

                @Override
                public void clearView(RecyclerView.ViewHolder viewHolder, int i) {

                }

                @Override
                public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int i) {

                }

                @Override
                public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1,
                                              boolean b) {

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
                                .class, bundle, false);
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

    private void getDataFromServer(final long shopId,final boolean isRefreshOrFirstLoading) {
        if (isRefreshOrFirstLoading) {
            progressActivity.showLoading();//会一直刷新 先测试下
        }
        //这个不是分页加载
        this.appAction.getShopwithoutId(shopId, new ActionCallbackDoubleListener
                <ShopBean, List<SortDataBean>>() {


            @Override
            public void onSuccess(ShopBean data, List<SortDataBean> list) {
                progressActivity.showContent();
                totalCount = list.size(); // 因为不是分页，所以这个就是全部数据了


                //没有数据,因为是一次性刷完，不存在上拉加载
                if (totalCount == 0) {
                    initAdapter(list);
                    adapter.setEmptyView(true, getEmptyView());//如果是希望可以刷新的就用adapter
                } else {
                    //缓存中的数据也是需要清理掉的
//                    if (cachedlistData != null) {
//                        cachedlistData.clear();
//                    }

                    //判断数据是否到底了,没有数据了就到底了
                    if (adapter.getData().isEmpty()) {
                        initAdapter(list);//这个其实也只是会执行一次
                    } else {

                        if (!isRefreshOrFirstLoading) {
                            adapter.addData(list);
                        } else {
                            if (!list.isEmpty()) {
                                adapter.setNewData(list);//有数据才会更新，没有数据是不会更新的
                                adapter.removeAllFooterView();
                            }
                        }
                    }
                }
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();
            }

            @Override
            public void onFailure(String errorEvent, String message) {

                if ("998".equals(errorEvent) && requestCount > 0) {
                    requestCount--;
                    getDataFromServer(shopId, true);
//                    refresh(shopId);
                } else if ("003".equals(errorEvent)) {
                    handleError(shopId, "易，请检查网络");
                } else {
                    handleError(shopId, "加载失败，请重新点击加载");
                    Toast.makeText(getActivity(), "异常代码：" + errorEvent + " 异常说明: " + message, Toast
                            .LENGTH_SHORT).show();
                }

            }
        });

    }

    private void handleError(final long shopId,String Message) {
        ishasData = (!adapter.getData().isEmpty())  ? true : false;
        if (!ishasData && !ishasCachedData) {
            showError(shopId,Message);
        } else if (!ishasData && ishasCachedData){
            adapter.setNewData(cachedlistData);//有数据才会更新，没有数据是不会更新的
            adapter.removeAllFooterView();
        }else {
            //请求失效，数据不变
            mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
            mSuperRecyclerViewImg.hideMoreProgress();
            progressActivity.showContent();
            adapter.notifyDataSetChanged();
        }
    }

    private void showError(final long shopId,String Message) {
        progressActivity.showError(Message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromServer(shopId, true);

            }
        });
    }
    private void refresh(final long shopId) {
        ishasData = (!adapter.getData().isEmpty()) ? true : false;
        if (!ishasData && !ishasCachedData) {
            getDataFromServer(shopId, true);
        } else {
            //请求失效，数据不变
            mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
            mSuperRecyclerViewImg.hideMoreProgress();
            progressActivity.showContent();
            adapter.notifyDataSetChanged();
        }
    }

    private View getEmptyView() {
        inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_empty_view,
                (ViewGroup) mSuperRecyclerViewImg.getParent
                        (), false);
        TextView tv = (TextView) view.findViewById(R.id.empty_content);
        tv.setText("易，该店铺暂时缺少热门服务！");
        return view;
    }
    private View getErrorView() {
        inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.frontpage_error_view,
                (ViewGroup) mSuperRecyclerViewImg.getParent
                        (), false);
        view.findViewById(R.id.errorStateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromServer(shopId, true);
            }
        });
        return view;
    }

    private void initRecyclerView() {
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSuperRecyclerViewImg.setRefreshListener(this);
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        //调用服务器
    }
    @Override
    public void onRefresh() {
        requestCount = 5;
        getDataFromServer(shopId,true);
    }

}
