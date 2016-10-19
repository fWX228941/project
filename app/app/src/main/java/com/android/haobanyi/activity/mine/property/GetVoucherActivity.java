package com.android.haobanyi.activity.mine.property;

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
import com.android.haobanyi.view.ValidationCodeView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/8/11.
 *
 * @作者: 付敏
 * @创建日期：2016/08/11
 * @邮箱：466566941@qq.com
 * @当前文件描述：领取代金券界面 activity_get_voucher
 */
public class GetVoucherActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.edit_password)
    ClearEditText editPassword;
    @BindView(R.id.edit_code)
    ClearEditText editCode;
    @BindView(R.id.btn_send_code)
    ValidationCodeView validationCode;
    @BindView(R.id.btn_next)
    Button btn_sendCode;
    @BindView(R.id.id_hint)
    TextView idHint;
    String voucherscode;//卡密号码

    private static final int LOGIN_TO_GET_VOUNCHER = 3;
    @Override
    protected int setLayout() {
        return R.layout.activity_get_voucher;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {


    }

    @Override
    protected void initViews() {
        initTittleBar();
    }
    private void initTittleBar() {
        // 设置背景色  透明灰色  颜色 什么的最后定夺
        titleBar.setBackgroundResource(R.color.white);
        // 设置返回箭头和文字以及颜色
        titleBar.setLeftImageResource(R.drawable.back_icon);

        titleBar.addAction(new TitleBar.TextAction("我的代金券") {
            @Override
            public void performAction(View v) {
                // 跳转到领取代金券界面
                IntentUtil.gotoActivityWithoutData(GetVoucherActivity.this, VoucherActivity.class, false);
            }
        });

        // 设置监听器
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                GetVoucherActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitle("领取代金券");
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
        if (btn_sendCode.isEnabled()){
            btn_sendCode.setEnabled(false);
        }

        if (validationCode.isEquals(editCode.getText().toString())){
            voucherscode = editPassword.getText().toString();
            requestCount = 45;
            receiveVouchers();
        }else {
            //验证码不一致
            idHint.setVisibility(View.VISIBLE);
            if (voucherscode.isEmpty()){
                idHint.setText("代金券密卡卡号为空");

            }else {
                idHint.setText("验证码输入错误");
            }
            if (!btn_sendCode.isEnabled()){
                btn_sendCode.setEnabled(true);
            }

        }

    }

    private void receiveVouchers() {
        this.appAction.receiveVouchers(voucherscode, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage(data.getMessage());
                if (!btn_sendCode.isEnabled()){
                    btn_sendCode.setEnabled(true);
                }
                setHintMessage(" ");
                // 那就返回到我的代金券界面
                IntentUtil.gotoActivityWithoutData(GetVoucherActivity.this, VoucherActivity.class, false);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if (!btn_sendCode.isEnabled()){
                    btn_sendCode.setEnabled(true);
                }
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    receiveVouchers();
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    setHintMessage(errorMessage);
                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                    setHintMessage("加载失败，请重新点击加载");
                }

            }
        });
    }
    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(GetVoucherActivity.this, LoginActivity.class, LOGIN_TO_GET_VOUNCHER);//跳转到登录界面
    }
    private void setHintMessage(String errorMessage) {
        if (!btn_sendCode.isEnabled()){
            btn_sendCode.setEnabled(true);
        }
        idHint.setVisibility(View.VISIBLE);
        idHint.setText(errorMessage);

    }

    //LOGIN_TO_GET_VOUNCHER

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case LOGIN_TO_GET_VOUNCHER:
                initTittleBar();        //重新绘制一遍就是了，为了避免重复绘制，需要判断下
                break;

        }

    }
}
