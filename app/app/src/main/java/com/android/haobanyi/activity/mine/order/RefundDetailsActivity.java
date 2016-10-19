package com.android.haobanyi.activity.mine.order;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fWX228941 on 2016/8/18.
 *
 * @作者: 付敏
 * @创建日期：2016/08/18
 * @邮箱：466566941@qq.com
 * @当前文件描述：退款详情页 activity_refund_details
 *
 */
public class RefundDetailsActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.refund_number)
    TextView refundNumber;
    @BindView(R.id.refund_reason)
    TextView refundReason;
    @BindView(R.id.refund_price)
    TextView refundPrice;
    @BindView(R.id.refund_info)
    TextView refundInfo;
    private boolean isLogin = false;
    private static final int LOGIN_TO_SPV = 3;
    PreferenceUtil complexPreferences;
    private long requestCount = 5;
    @Override
    protected int setLayout() {
        return R.layout.activity_refund_details;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        /*这块内容分离出来，重构*/
        NTPTime.getInstance().getCurrentTime();
        complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);
        if (isLogin) {
            long refreshTime = complexPreferences.getLong(Constants.START_TIME, -1000l);
            if (System.currentTimeMillis() / 1000L - refreshTime > 7200) {
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
                } else if ("003".equals(errorCode)) {
                    Toast.makeText(context, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
                }else {
                    handleLogic();
                }

            }
        });
    }

    private void loginForToken() {
        complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(RefundDetailsActivity.this, LoginActivity.class, LOGIN_TO_SPV);//跳转到登录界面
    }

    private void handleLogic() {
        initTittleBar();

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
                RefundDetailsActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitle("退款详情");
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

}
