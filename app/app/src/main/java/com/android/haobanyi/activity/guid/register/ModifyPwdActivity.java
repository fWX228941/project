package com.android.haobanyi.activity.guid.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
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
 * @当前文件描述：修改密码界面 支付密码界面
 */
public class ModifyPwdActivity extends BaseActivity {

    @BindView(R.id.edit_pwd_old)
    ClearEditText editPwdOld;
    @BindView(R.id.edit_pwd_new)
    ClearEditText editPwdNew;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.id_hint)
    TextView idHint;
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    private String oldPWD;
    private String NewPWD;
    private String NewPWD_;
    private boolean isModifyPayPwd =false;
    private String ValidateCode;
    private static final int LOGIN_TO_MODIFY_PWD = 3;

    @Override
    protected int setLayout() {
        return R.layout.modify_pwd_activity;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        if (!hasTokenRefreshed){
            long refreshTime = basePreference.getLong(Constants.START_TIME,-1000l);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                requestCount = 5;
                Log.d(TAG, "执行");
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
                } else {
                    handleLogic();
                }

            }
        });
    }

    private void handleLogic() {
        initTittleBar();
        isModifyPayPwd = basePreference.getBoolean(Constants.IS_MODIFY_PAY_PWD, true);
        //isModifyPayPwd = this.getIntent().getBooleanExtra(Constants.IS_MODIFY_PAY_PWD, true);
        if (isModifyPayPwd){
            ValidateCode =  this.getIntent().getStringExtra(Constants.VALIDATECODE); //旧的验证码
        }
    }

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(ModifyPwdActivity.this, LoginActivity.class, LOGIN_TO_MODIFY_PWD);//跳转到登录界面
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
                ModifyPwdActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        if (isModifyPayPwd){
            titleBar.setTitle("修改支付密码");//这个逻辑还是需要修改下
        } else {
            titleBar.setTitle("修改密码");
        }

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
        editPwdOld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    editPwdOld.setText(str1);

                    editPwdOld.setSelection(start);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        editPwdNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "输入前");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    editPwdNew.setText(str1);

                    editPwdNew.setSelection(start);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    protected void saveState(Bundle outState) {

    }


    @OnClick(R.id.btn_login)
    public void onClick() {
        /*
        * 两处地方需要保存密码，一个是修改密码，一个是登录成功login
        *  这会的逻辑再来添加吧
        * */
        if (btn_login.isEnabled()){
            btn_login.setEnabled(false);
        }

        oldPWD = basePreference.getString(Constants.USER_CURRENT_PWD, "");//还要判断是否登录  【这个是用户当前登录密码】
        NewPWD = editPwdNew.getText().toString();
        NewPWD_= editPwdOld.getText().toString();
        if (NewPWD_.isEmpty()){
            failedFinish("设置新密码不能为空");
            return;

        } else if (!NewPWD_.equals(NewPWD)){
            failedFinish("前后设置不一致");
            return;

        }
        if (isModifyPayPwd){
            Boolean is_first_modify_pay_pwd =basePreference.getBoolean(Constants.IS_FIRST_MODIFY_PAY_PWD, true);
            Log.d("ModifyPwdActivity", "is_first_modify_pay_pwd:" + is_first_modify_pay_pwd);
            if (is_first_modify_pay_pwd){
                requestCount = 45;
                //setPayPwd(basePreference);
            }else {
                requestCount = 45;
                modifyPayPwd();
            }
        }else {
            requestCount = 45;
            modifypwd();
        }
    }

    private void modifypwd() {
        this.appAction.modifypwd(oldPWD, NewPWD, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage(data.getMessage());
                successFinish();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if (!btn_login.isEnabled()){
                    btn_login.setEnabled(true);
                }

                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    modifypwd();
                }else if ("996".equals(errorCode)){
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    failedFinish(errorMessage);
                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                    failedFinish(errorMessage);
                }


            }
        });
    }

    private void modifyPayPwd() {
        this.appAction.modifyPayPwd(oldPWD, ValidateCode, NewPWD, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage(data.getMessage());
                successFinish();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if (!btn_login.isEnabled()){
                    btn_login.setEnabled(true);
                }

                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    modifyPayPwd();
                }else if ("996".equals(errorCode)){
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    failedFinish(errorMessage);

                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                    failedFinish(errorMessage);
                }

            }
        });
    }



    private void failedFinish(String errorMessage) {
        if (!btn_login.isEnabled()){
            btn_login.setEnabled(true);
        }
        idHint.setVisibility(View.VISIBLE);
        idHint.setText(errorMessage);
    }

    private void successFinish() {
        idHint.setText(" ");
        if (!btn_login.isEnabled()){
            btn_login.setEnabled(true);
        }

        ModifyPwdActivity.this.finish();
        ModifyPwdActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_TO_MODIFY_PWD:
                handleLogic();//重新绘制一遍就是了，为了避免重复绘制，需要判断下
                break;
        }
    }
}
