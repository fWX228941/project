package com.android.haobanyi.activity.mine.property;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;

import com.android.haobanyi.activity.guid.register.BindMobileOrEmailActivity;
import com.android.haobanyi.adapter.userpoint.UserPointAdapter;
import com.android.haobanyi.core.ActionCallbackDoubleListener;
import com.android.haobanyi.core.ActionCallbackListener;

import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.userpoint.UserPointBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.android.haobanyi.view.TitleBar;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/8/8.
 *
 * @作者: 付敏
 * @创建日期：2016/08/08
 * @邮箱：466566941@qq.com
 * @当前文件描述：用户积分
 * item_user_point
 *  参考账户余额  其实可以复用，算了
 *  activity_user_point
 *  参考AccountBalanceActivity
 *  UserPointBean
 *  GetUserPoint
 */
public class UserPointActivity extends BaseActivity  implements SwipeRefreshLayout.OnRefreshListener  {
    private static final int LOGIN_TO_USERPOINT = 3;
    @BindView(R.id.item)
    RelativeLayout item;
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.list_item_point)
    TextView item_point;
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    private boolean ishasData = false;//当前是否有数据
    private int totalCount = 0 ;
    UserPointAdapter adapter = new UserPointAdapter(null);
    String point;
    @Override
    protected int setLayout() {
        return R.layout.activity_user_point;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

        progressDialog.show();
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
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(UserPointActivity.this, LoginActivity.class, LOGIN_TO_USERPOINT);//跳转到登录界面
    }

    private void handleLogic() {
        initTittleBar();
        initRecyclerView();
        if (isLogin){
            requestCount = 45;
            getDataFromServer(true);
        }else {
            if (progressDialog.isShowing()){
                progressDialog.cancel();
            }
            progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看我的用户积分！");

        }

    }

    private void getDataFromServer(final boolean isRefreshOrFirstLoading) {
        if (isRefreshOrFirstLoading) {
            if(!progressDialog.isShowing()){
                progressDialog.show();
            }
        }
        this.appAction.getUserPoint(new ActionCallbackDoubleListener<List<UserPointBean>, String>() {
            @Override
            public void onSuccess(List<UserPointBean> list, String point_) {
                progressActivity.showContent();
                progressDialog.cancel();
                totalCount = list.size();
                point = point_;

                //初始化视图

                item_point.setText(point);
                item.setVisibility(View.VISIBLE);

                if (totalCount == 0) {
                    progressActivity.showEmpty("暂时没有积分");
                } else {
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
                    getDataFromServer(true);
                } else if ("003".equals(errorEvent)) {
                    ToastUtil.networkUnavailable();
                    handleError("易，请检查网络");
                } else if ("996".equals(errorEvent)){
                    progressActivity.showContent();
                    progressDialog.cancel();
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                }else {
                    ToastUtil.showErrorMessage(errorEvent, message);
                    handleError("加载失败，请重新点击加载");

                }
            }
        });


    }

    private void handleError(String Message) {
        progressDialog.cancel();
        ishasData = (!adapter.getData().isEmpty())  ? true : false;
        if (!ishasData) {
            item.setVisibility(View.GONE);
            showError(Message);
        } else {
            //请求失效，数据不变
            item_point.setText(point);
            item.setVisibility(View.VISIBLE);
            mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
            mSuperRecyclerViewImg.hideMoreProgress();
            progressActivity.showContent();
            adapter.notifyDataSetChanged();
        }
    }
    private void showError(String message) {
        progressActivity.showError(message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCount = 45;
                getDataFromServer(true);

            }
        });
    }
/*
    private void refresh() {
        ishasData = (!adapter.getData().isEmpty()) ? true : false;
        if (!ishasData) {
            getDataFromServer(true);
        } else {
            //请求失效，数据不变
            item_point.setText(point);
            item.setVisibility(View.VISIBLE);
            mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
            mSuperRecyclerViewImg.hideMoreProgress();
            progressActivity.showContent();
            adapter.notifyDataSetChanged();
        }
    }*/


    private void initAdapter(List<UserPointBean> listData) {
        adapter = new UserPointAdapter(listData);
        if (null== mSuperRecyclerViewImg.getAdapter()){
            adapter.openLoadAnimation();
            mSuperRecyclerViewImg.setAdapter(adapter);
        }

    }
    private void initRecyclerView() {
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(this));
        mSuperRecyclerViewImg.setRefreshListener(this);
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
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
                UserPointActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitle("我的积分");
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
        requestCount = 45;
        getDataFromServer(true);
    }
    //LOGIN_TO_USERPOINT

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case LOGIN_TO_USERPOINT:
                handleLogic();//重新绘制一遍就是了，为了避免重复绘制，需要判断下
                break;

        }
    }
}
