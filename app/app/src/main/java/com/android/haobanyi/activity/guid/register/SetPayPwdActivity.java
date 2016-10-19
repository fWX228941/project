package com.android.haobanyi.activity.guid.register;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import com.android.haobanyi.model.bean.mine.myinformation.UserBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ClearEditText;
import com.android.haobanyi.view.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

import static android.R.attr.data;

/**
 * Created by fWX228941 on 2016/10/10.
 *
 * @作者: 付敏
 * @创建日期：2016/10/10
 * @邮箱：466566941@qq.com
 * @当前文件描述：重置设置支付密码界面
 * activity_set_paypwd
 *
 */

public class SetPayPwdActivity extends BaseActivity {
    @BindView(R.id.edit_pwd_old)
    ClearEditText set_pwd;
    @BindView(R.id.edit_pwd_new)
    ClearEditText comform_pwd;
    @BindView(R.id.edit_pwd_account)
    ClearEditText account_pwd;

    private String Set_pwd; //设置支付密码
    private String Comform_pwd;//确认支付密码
    private String Account_pwd;//登录密码


    @BindView(R.id.btn_login)
    Button btn_conform;
    @BindView(R.id.id_hint)
    TextView idHint;
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    private static final int SET_TO_PAY_PWD = 04;

    @Override
    protected int setLayout() {
        return R.layout.activity_set_paypwd;
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
                SetPayPwdActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });

        titleBar.setTitle("设置支付密码");
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
        set_pwd.addTextChangedListener(new TextWatcher() {
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
                    set_pwd.setText(str1);

                    set_pwd.setSelection(start);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        comform_pwd.addTextChangedListener(new TextWatcher() {
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
                    comform_pwd.setText(str1);

                    comform_pwd.setSelection(start);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        account_pwd.addTextChangedListener(new TextWatcher() {
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
                    account_pwd.setText(str1);

                    account_pwd.setSelection(start);

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

    //只能输入六位数字

    @OnClick(R.id.btn_login)
    public void onClick() {
          Set_pwd = set_pwd.getText().toString(); //设置支付密码
          Comform_pwd = comform_pwd.getText().toString();//确认支付密码
          Account_pwd = account_pwd.getText().toString();//登录密码
          if (btn_conform.isEnabled()){
              btn_conform.setEnabled(false);
          }
          if (TextUtils.isEmpty(Account_pwd)){
              setErrorMessage("登录密码不能为空");
              return;

          }
          if (TextUtils.isEmpty(Set_pwd)){
              setErrorMessage("设置支付密码不能为空");
              return;

          }
          if (TextUtils.isEmpty(Comform_pwd)){
              setErrorMessage("确认支付密码不能为空");
              return;
          }
          if (!Set_pwd.equals(Comform_pwd)){
              setErrorMessage("前后支付密码不一致");
              return;
          }

          requestCount=45;
          setPayPwd();


    }

    private void setPayPwd() {
        this.appAction.setPayPwd(Account_pwd, Comform_pwd, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage(data.getMessage());
                successFinish();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if (!btn_conform.isEnabled()){
                    btn_conform.setEnabled(true);
                }
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    setPayPwd();
                }else if ("996".equals(errorCode)){
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    idHint.setText(" ");
                    loginForToken();
                }else if ("003".equals(errorCode)) {
                    setErrorMessage(errorMessage);
                } else {
                    setErrorMessage(errorMessage);
                }
            }
        });
    }

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(SetPayPwdActivity.this, LoginActivity.class, SET_TO_PAY_PWD);//跳转到登录界面
    }
    private void successFinish() {
        idHint.setText(" ");
        if (!btn_conform.isEnabled()){
            btn_conform.setEnabled(true);
        }
        SetPayPwdActivity.this.finish();
        SetPayPwdActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
    }


    private void setErrorMessage(String hint) {
        ToastUtil.showHintMessage(hint);
        idHint.setVisibility(View.VISIBLE);
        idHint.setText(hint);
        if (!btn_conform.isEnabled()){
            btn_conform.setEnabled(true);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //LOGIN_TO_MODIFY_PWD
        switch (requestCode) {
            case SET_TO_PAY_PWD:
                handleLogic();
                break;
        }
    }

}
