package com.android.haobanyi.activity.mine.history;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.shopping.product.ShoppingRadioGroupActivity;
import com.android.haobanyi.adapter.history.MyFootHistoryAdapter;
import com.android.haobanyi.core.ActionCallbackFivefoldListener;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.ActionCallbackTripleListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.foot.FootBean;
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
import com.android.haobanyi.view.ProgressActivity;
import com.android.haobanyi.view.TitleBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;

import butterknife.BindView;
import dmax.dialog.SpotsDialog;

/**
 * Created by fWX228941 on 2016/8/8.
 *
 * @作者: 付敏
 * @创建日期：2016/08/08
 * @邮箱：466566941@qq.com
 * @当前文件描述：我的足迹界面
 *
 * 仿照MyRedEnvelopeListActivity 这个类来写
 * MyFootHistoryAdapter
 */
public class MyFootHistoryActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener,SwipeRefreshLayout.OnRefreshListener {

    private static final int LOGIN_TO_MYHISTORY = 3;
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    MyFootHistoryAdapter adapter = new MyFootHistoryAdapter(null);
    private int CurrentPage = 0;// 默认从1开始
    private int PAGESIZE = 10; // 定义的一页多少条数据
    private int totalCount = 0 ;
    private boolean isListToBottom = false; //数据是否到顶了
    private View notLoadingView;
    private boolean ishasData = false;//当前是否有数据

    private LayoutInflater inflater;
    private long productID = -1;

    @Override
    protected int setLayout() {
        return R.layout.activity_my_voucher;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }

        if (!hasTokenRefreshed){
            long refreshTime = basePreference.getLong(Constants.START_TIME,-1000l);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                requestCount = 5;
                getToken();
            } else {
                handleLogic();
            }

        } else {
            handleLogic();

        }

    }

    private void getToken() {
        BaseApplication.getApplication().getAppAction().getToken(new ActionCallbackListener<LoginResponseResult>() {

            @Override
            public void onSuccess(LoginResponseResult data) {
                handleLogic();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    getToken();
                } else {
                    handleLogic();
                }

            }
        });
    }

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(MyFootHistoryActivity.this, LoginActivity.class, LOGIN_TO_MYHISTORY);//跳转到登录界面
    }

    private void handleLogic() {
        initTittleBar();
        initRecyclerView();
        if (isLogin){
            CurrentPage = 1;
            requestCount = 35;
            getDataFromServer(CurrentPage, true);
        }else {
            if (progressDialog.isShowing()){
                progressDialog.cancel();
            }

            progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看我的足迹！");
        }


    }
    private void initRecyclerView() {
    /*01.循环视图设置布局管理器*/
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(MyFootHistoryActivity.this));
        /*03.设置下拉刷新的监听器*/
        mSuperRecyclerViewImg.setRefreshListener(this);
        /*05.设置刷新布局的颜色状态*/
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
    }
    private void getDataFromServer(final int currentPage,final boolean isRefreshOrFirstLoading) {
        if (isRefreshOrFirstLoading) {
            if (!progressDialog.isShowing()){
                progressDialog.show();
            }

        }
        this.appAction.getMyFootList(currentPage, PAGESIZE, new ActionCallbackTripleListener<List<FootBean>,
                Integer, Integer>() {


            @Override
            public void onSuccess(List<FootBean> data, Integer pageIndex, Integer total) {
                progressActivity.showContent();
                progressDialog.cancel();
                CurrentPage = pageIndex;
                totalCount = total;
                if (data.isEmpty()) {
                    isListToBottom = true;
                }


                if (totalCount == 0) {
                    progressActivity.showEmpty("红包列表为空");
                } else {
                    if (adapter.getData().isEmpty()) {
                        initAdapter(data);

                    } else {

                        if (!isRefreshOrFirstLoading) {
                            if (isListToBottom) {
                                adapter.loadComplete();
                                if (notLoadingView == null) {
                                    inflater = LayoutInflater.from(MyFootHistoryActivity.this);
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
                                adapter.setNewData(data);
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
            public void onFailure(String errorEvent, String message) {
                if ("998".equals(errorEvent) && requestCount > 0) {
                    requestCount--;
                    refresh(isRefreshOrFirstLoading);
                } else if ("003".equals(errorEvent)) {
                    handleError(isRefreshOrFirstLoading, "易，请检查网络");
                } else if ("996".equals(errorEvent)) {
                    progressActivity.showContent();
                    if (progressDialog.isShowing()){
                        progressDialog.cancel();
                    }
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    ToastUtil.showErrorMessage(errorEvent, message);
                    handleError(isRefreshOrFirstLoading, "加载失败，请重新点击加载");
                }
            }
        });
    }
    private void handleError(boolean isRefreshOrFirstLoading,String Message) {
        if (!isRefreshOrFirstLoading){
            adapter.showLoadMoreFailedView();
        }else {
            if (progressDialog.isShowing()){
                progressDialog.cancel();
            }

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
        progressActivity.showError(message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentPage = 1;
                requestCount = 35;
                getDataFromServer(1, true);
            }
        });
    }

    private void refresh(boolean isRefreshOrFirstLoading) {
        if (!isRefreshOrFirstLoading){
            getDataFromServer(CurrentPage+1, false);//如果是下滑加载时一直请求,直到请求成功为止
        } else {
            getDataFromServer(CurrentPage, true);
        }


    }

    private void initAdapter(List<FootBean> listData) {
        adapter = new MyFootHistoryAdapter(listData);
        if (null== mSuperRecyclerViewImg.getAdapter()){
            adapter.openLoadAnimation();
            adapter.setOnLoadMoreListener(this);
            adapter.openLoadMore(PAGESIZE);
            mSuperRecyclerViewImg.setAdapter(adapter);
            mSuperRecyclerViewImg.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                    progressDialog.show();
                    gotoProduct(position);
                }
            });

        }

    }

    private void gotoProduct(int position) {
        final FootBean bean = (FootBean) adapter.getItem(position);
        productID = bean.getProductID();
        if (productID !=-1){
            requestCount = 35;
            getProduct(productID);
        }

    }

    private void getProduct(final long productID) {
        this.appAction.getProduct01(productID, new
                ActionCallbackFivefoldListener<ProductDetailsBean, List<SatisfySendBean>,
                                        List<VouchersTemplateBean>, List<ShopAttrBean>, List<DetailsBean.DataBean.CLBean>>() {
                    @Override
                    public void onSuccess(ProductDetailsBean data, List<SatisfySendBean> data02,
                                          List<VouchersTemplateBean> data03, List<ShopAttrBean> data04, List<DetailsBean.DataBean.CLBean> data05) {
                        /*
                        * 如果是当前的activity ，一定不要用context，不然
                        * 会报错误：android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
                        * */
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constants.PRODUCT_ID, productID);
                        bundle.putLong(Constants.SHOP_ID, data.getShopID());
                        progressDialog.cancel();
                        IntentUtil.gotoActivityWithData(MyFootHistoryActivity.this, ShoppingRadioGroupActivity.class,
                                bundle, false);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        if ("998".equals(errorCode) && requestCount > 0){
                            requestCount--;
                            getProduct(productID);
                        }else if ("003".equals(errorCode)) {
                            progressDialog.cancel();
                            ToastUtil.networkUnavailable();
                        }else {
                            progressDialog.cancel();
                            ToastUtil.showErrorMessage(" 异常说明:" +
                                    errorMessage + "," + errorCode);


                        }

                    }
                });
    }

    @Override
    protected void initViews() {

    }
    private void initTittleBar() {
        // 设置背景色  透明灰色  颜色 什么的最后定夺
        titleBar.setBackgroundResource(R.color.white);
        // 设置返回箭头和文字以及颜色
        titleBar.setLeftImageResource(R.drawable.back_icon);



        // 设置监听器
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MyFootHistoryActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitle("我的足迹");
        titleBar.setDividerColor(Color.GRAY);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void registerEventListener() {

    }

    @Override
    protected void registerBroadCastReceiver() {

    }

    @Override
    protected void saveState(Bundle outState) {

    }

    @Override
    public void onRefresh() {
        CurrentPage = 1;
        requestCount = 35;
        getDataFromServer(CurrentPage, true);
    }

    @Override
    public void onLoadMoreRequested() {
        requestCount = 35;
        getDataFromServer(CurrentPage + 1, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case LOGIN_TO_MYHISTORY:
                handleLogic();
                break;

        }
    }
}