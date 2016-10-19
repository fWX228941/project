package com.android.haobanyi.activity.shopping.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.android.haobanyi.BaseFragment;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.adapter.shop.ShopVoucherAdapter;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.voucher.VoucherBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/7/8.
 *
 * @作者: 付敏
 * @创建日期：2016/07/08
 * @邮箱：466566941@qq.com
 * @当前文件描述：item_fragment_shop_voucher
 */
public class StoreVoucherFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener  {
    private static final int BACK_TO_STORE_VOUCHER = 01;
    private ShopVoucherAdapter adapter = new ShopVoucherAdapter( null);
//    private List<VoucherBean> listData = new ArrayList<VoucherBean>();//不初始化，默认就是空了
    private List<VoucherBean> cachedlistData;
    private static final String TAG = "minfu";
    private long shopId=-1;
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    private boolean ishasCachedData = false;//缓存是否有数据
    private boolean ishasData = false;//当前是否有数据
    private int totalCount = 0 ;
    private LayoutInflater inflater;
    PreferenceUtil complexPreferences;
    @Override
    protected void lazyload() {
    }

    @Override
    protected int setLayout() {
        return R.layout.tab_front_page02;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        shopId = getActivity().getIntent().getLongExtra(Constants.SHOP_ID, PreferenceUtil.getSharedPreference(getActivity(), "preferences").getLong(Constants.SHOP_ID, -1l));
        PreferenceUtil complexPreferences01 = PreferenceUtil.getSharedPreference(getActivity(),
                "shop");
        cachedlistData = complexPreferences01.getListObject("shopid" + shopId + "ShopVoucher", VoucherBean.class);
        ishasCachedData = (null != cachedlistData && !cachedlistData.isEmpty())?true:false;
        initRecyclerView();

        //这个不存在提前缓存
        if (ishasCachedData){
            initAdapter(cachedlistData);
        } else {
            if (shopId!=-1){
                requestCount = 20;
                getDataFromServer(shopId, true);
            }
        }
    }
    private void initAdapter(List<VoucherBean> listData) {
        adapter = new ShopVoucherAdapter( listData);
        if (null== mSuperRecyclerViewImg.getAdapter()){
            mSuperRecyclerViewImg.addOnItemTouchListener(new OnItemChildClickListener() {
                @Override
                public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    VoucherBean bean = (VoucherBean) baseQuickAdapter.getItem(i);
                    long vouchersTemplateID = bean.getVouchersTemplateID();
                    switch (view.getId()) {
                        case R.id.button1:
                            //临取店铺代金券
                            requestCount = 20;
                            receiveVouchers(vouchersTemplateID);

                        break;
                    }
                }
            });
 /*           ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
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
            });*/
            adapter.openLoadAnimation();
            mSuperRecyclerViewImg.setAdapter(adapter);
        }

    }

    private void receiveVouchers(final long vouchersTemplateID) {
        this.appAction.ReceiveVouchers(vouchersTemplateID, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage("领取成功");
                refreshServer();

            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    receiveVouchers(vouchersTemplateID);
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);

                }

            }
        });
    }
    private void loginForToken() {
        complexPreferences = PreferenceUtil.getSharedPreference(getActivity(), "preferences");
        complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(getActivity(), LoginActivity.class, BACK_TO_STORE_VOUCHER);
    }
    @Override
    public void onRefresh() {
        requestCount = 20;
        getDataFromServer(shopId, true);
    }

    private void getDataFromServer(final long shopId,final boolean isRefreshOrFirstLoading) {
        if (isRefreshOrFirstLoading) {
            progressActivity.showLoading();//会一直刷新 先测试下
        }
        this.appAction.getShopVouchersList(shopId, new ActionCallbackListener<List<VoucherBean>>() {
            @Override
            public void onSuccess(List<VoucherBean> list) {
                progressActivity.showContent();

                if (totalCount == 0) {
                    initAdapter(list);
                    adapter.setEmptyView(true, getEmptyView());//如果是希望可以刷新的就用adapter
                } else {
                    if (adapter == null) {
                        adapter = new ShopVoucherAdapter(getActivity(), null);
                    }
                    //判断数据是否到底了,没有数据了就到底了
                    if (adapter.getData().isEmpty()) {
                        initAdapter(list);
                    } else {
                      /*  if (!listData.isEmpty() && !list.isEmpty()) {
                            listData.clear();  //只有请求成功了
                            listData = list;
                        }*/
                        if (cachedlistData != null) {
                            cachedlistData.clear();
                        }

                        if (!isRefreshOrFirstLoading) {
                            adapter.addData(list);
                        } else {
                            if (!list.isEmpty()) {
                                adapter.setNewData(list);
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
                    handleError("易，请检查网络");
                } else {
                    handleError("加载失败，请重新点击加载");
                    Toast.makeText(getActivity(), "异常代码：" + errorEvent + " 异常说明: " + message, Toast
                            .LENGTH_SHORT).show();
                }

            }
        });
    }

    private void handleError(String Message) {
        ishasData = (!adapter.getData().isEmpty())  ? true : false;
        if (!ishasData && !ishasCachedData) {
            showError(Message);
        } else if (!ishasData && ishasCachedData){
            adapter.setNewData(cachedlistData);
            adapter.removeAllFooterView();
        }else {
            //请求失效，数据不变
            mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
            mSuperRecyclerViewImg.hideMoreProgress();
            progressActivity.showContent();
            adapter.notifyDataSetChanged();
        }
    }

    private void showError(String Message) {
        progressActivity.showError(Message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCount = 20;
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
            //既然是请求失效了
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
        tv.setText("易，该店铺暂时缺少代金券活动！");
        return view;
    }

    private void initRecyclerView() {
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSuperRecyclerViewImg.setRefreshListener(this);
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //评价完以后放回到这里
            case BACK_TO_STORE_VOUCHER:
                refreshServer();
                break;

        }
    }

    private void refreshServer() {
        requestCount = 20;
        getDataFromServer(shopId, true);
    }
}
