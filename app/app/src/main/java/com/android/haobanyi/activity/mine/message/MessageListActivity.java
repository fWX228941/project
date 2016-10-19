package com.android.haobanyi.activity.mine.message;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.mine.property.GetVoucherActivity;
import com.android.haobanyi.activity.mine.property.VoucherActivity;
import com.android.haobanyi.adapter.mine.MyMessageAdapter;
import com.android.haobanyi.adapter.mine.MyVoucherAdapter;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.mine.MyMessageBean;
import com.android.haobanyi.model.bean.voucher.VoucherBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.android.haobanyi.view.TitleBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/10/11.
 *
 * @作者: 付敏
 * @创建日期：2016/10/11
 * @邮箱：466566941@qq.com
 * @当前文件描述：不同类型的列表
 * activity_message_list
 */

public class MessageListActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener,SwipeRefreshLayout.OnRefreshListener {
    private static final int LOGIN_TO_VOUCHER = 3;
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;

    private int CurrentPage = 1;// 默认从1开始
    private int PAGESIZE = 10; // 定义的一页多少条数据
    private int totalCount = 0 ;
    private boolean isListToBottom = false; //数据是否到顶了
    private View notLoadingView;
    private boolean ishasData = false;//当前是否有数据
    private LayoutInflater inflater;
    private boolean isFirstTime = true;
    MyMessageAdapter adapter = new MyMessageAdapter(null);
    String messageType = null;

    @Override
    protected int setLayout() {
        return R.layout.activity_my_voucher;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        progressDialog.show();
        //获取消息类别
        messageType = this.getIntent().getStringExtra("messageType");


        if (hasTokenRefreshed){
            handleLogic();
        } else {
            long refreshTime = basePreference.getLong(Constants.START_TIME,-1000l);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                //超过过期时间，更新token
                requestCount = 5;
                getToken();
            } else {
                handleLogic();
            }
        }
    }
    private void getToken() {
        this.appAction.getToken(new ActionCallbackListener<LoginResponseResult>() {
            @Override
            public void onSuccess(LoginResponseResult data) {
                handleLogic();
            }
            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    getToken();
                }else {
                    handleLogic();
                }
            }
        });
    }

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(MessageListActivity.this, LoginActivity.class, LOGIN_TO_VOUCHER);//跳转到登录界面
    }

    private void handleLogic() {
        if (null!=messageType){
            initTittleBar();
            initRecyclerView();
            if (isLogin){
                CurrentPage = 1;
                requestCount = 35;
                getDataFromServer(CurrentPage, true);
            } else {
                if (progressDialog.isShowing()){
                    progressDialog.cancel();
                }
                switch (Integer.parseInt(messageType)){
                    case 1:
                        progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看订单消息！");
                        break;
                    case 2:
                        progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看退款消息！");
                        break;
                    case 3:
                        progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看投诉消息！");
                        break;
                    case 4:
                        progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看举报消息！");
                        break;
                    case 5:
                        progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看商家入驻消息！");
                        break;
                    case 6:
                        progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看安全消息！");
                        break;
                    case 7:
                        progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看资金消息！");
                        break;
                }

            }
        }



    }
    private void getDataFromServer(final int currentPage,final boolean isRefreshOrFirstLoading) {
        if (isRefreshOrFirstLoading) {
            if(!progressDialog.isShowing()){
                progressDialog.show();
            }
        }
        this.appAction.getUserMessageList(messageType,currentPage, PAGESIZE, new ActionCallbackListener<List<MyMessageBean>>() {

            @Override
            public void onSuccess(List<MyMessageBean> data) {
                progressActivity.showContent();
                progressDialog.cancel();
                CurrentPage++;
                // 只执行第一次
                if (isFirstTime) {
                    totalCount = data.size();
                    isFirstTime = false;
                }

                if (data.isEmpty()) {
                    isListToBottom = true;
                }


                if (totalCount == 0) {
                    switch (Integer.parseInt(messageType)){
                        case 1:
                            progressActivity.showEmpty("订单消息为空！");
                            break;
                        case 2:
                            progressActivity.showEmpty("退款消息为空！");
                            break;
                        case 3:
                            progressActivity.showEmpty("投诉消息为空！");
                            break;
                        case 4:
                            progressActivity.showEmpty("举报消息为空！");
                            break;
                        case 5:
                            progressActivity.showEmpty("商家入驻消息为空！");
                            break;
                        case 6:
                            progressActivity.showEmpty("安全消息为空！");
                            break;
                        case 7:
                            progressActivity.showEmpty("资金消息为空！");
                            break;
                    }

                } else {
                    if (adapter.getData().isEmpty()) {
                        initAdapter(data);
                    } else {
                        if (!isRefreshOrFirstLoading) {
                            if (isListToBottom) {
                                adapter.loadComplete();
                                if (notLoadingView == null) {
                                    inflater = LayoutInflater.from(MessageListActivity.this);
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
                            if (!data.isEmpty()){
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
                    ToastUtil.networkUnavailable();
                    handleError(isRefreshOrFirstLoading, "易，请检查网络");
                } else if ("996".equals(errorEvent)){
                    progressActivity.showContent();
                    progressDialog.cancel();

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
            progressDialog.cancel();
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
                getDataFromServer(CurrentPage, true);

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

    private void initAdapter(List<MyMessageBean> listData) {
        adapter = new MyMessageAdapter(listData);
        if (null== mSuperRecyclerViewImg.getAdapter()){
            adapter.openLoadAnimation();
            adapter.setOnLoadMoreListener(this);
            adapter.openLoadMore(PAGESIZE);
            mSuperRecyclerViewImg.setAdapter(adapter);
        }

    }
    private void initRecyclerView() {
    /*01.循环视图设置布局管理器*/
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(MessageListActivity.this));
        /*03.设置下拉刷新的监听器*/
        mSuperRecyclerViewImg.setRefreshListener(this);
        /*05.设置刷新布局的颜色状态*/
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
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
                MessageListActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        switch (Integer.parseInt(messageType)){
            case 1:
                titleBar.setTitle("订单消息");
                break;
            case 2:
                titleBar.setTitle("退款消息");
                break;
            case 3:
                titleBar.setTitle("投诉消息");
                break;
            case 4:
                titleBar.setTitle("举报消息");
                break;
            case 5:
                titleBar.setTitle("商家入驻消息");
                break;
            case 6:
                titleBar.setTitle("安全消息");
                break;
            case 7:
                titleBar.setTitle("资金消息");
                break;
        }
        titleBar.setDividerColor(Color.GRAY);
    }
    @Override
    protected void initViews() {

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
        getDataFromServer(CurrentPage+1, false);

    }
    //LOGIN_TO_VOUCHER
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case LOGIN_TO_VOUCHER:
                handleLogic();//重新绘制一遍就是了，为了避免重复绘制，需要判断下
                break;

        }
    }
}
