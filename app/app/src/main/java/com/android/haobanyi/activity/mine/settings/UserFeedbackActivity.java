package com.android.haobanyi.activity.mine.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.guid.register.BindMobileOrEmailActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ClearEditText;
import com.android.haobanyi.view.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/8/8.
 *
 * @作者: 付敏
 * @创建日期：2016/08/08
 * @邮箱：466566941@qq.com
 * @当前文件描述：用户反馈界面
 */
public class UserFeedbackActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.edit_feedback)
    ClearEditText editFeedback;
    @BindView(R.id.edit_feedback_phone)
    ClearEditText editFeedbackPhone;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.id_hint)
    TextView idHint;
    String content;
    String content_phone;
    private static final int LOGIN_TO_USER_FEED_BACK = 3;

    @Override
    protected int setLayout() {
        return R.layout.activity_user_feedback;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        handleLogic();
    }



    private void handleLogic() {
        initTittleBar();
    }

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(UserFeedbackActivity.this, LoginActivity.class, LOGIN_TO_USER_FEED_BACK);//跳转到登录界面
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
                UserFeedbackActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitle("用户反馈");
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

    @OnClick(R.id.btn_next)
    public void onClick() {
        content =editFeedback.getText().toString();
        content_phone = editFeedbackPhone.getText().toString();
        if (btnNext.isEnabled()){
            btnNext.setEnabled(false);
        }

        requestCount = 45;
        addPlatformConsult();
    }

    private void addPlatformConsult() {
        this.appAction.addPlatformConsult(content, content_phone, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage(data.getMessage());
                setHintMessage(" ");
                if (!btnNext.isEnabled()){
                    btnNext.setEnabled(true);
                }
                //成功后，回到主界面
                finish();
                UserFeedbackActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);

            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if (!btnNext.isEnabled()){
                    btnNext.setEnabled(true);
                }

                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    addPlatformConsult();
                }else if ("996".equals(errorCode)){
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    setHintMessage(errorMessage);
                } else {
                    ToastUtil.showErrorMessage( errorCode , errorMessage);
                    setHintMessage("异常代码：" + errorCode + " 异常说明: " + errorMessage);
                }


            }
        });
    }

    private void setHintMessage(String errorMessage) {
        if (!btnNext.isEnabled()){
            btnNext.setEnabled(true);
        }

        idHint.setVisibility(View.VISIBLE);
        idHint.setText(errorMessage);
    }
    //LOGIN_TO_USER_FEED_BACK

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_TO_USER_FEED_BACK:
                handleLogic();//重新绘制一遍就是了，为了避免重复绘制，需要判断下
                break;

        }
    }
}
