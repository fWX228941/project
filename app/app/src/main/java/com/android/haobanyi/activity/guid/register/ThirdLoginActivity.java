package com.android.haobanyi.activity.guid.register;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.listener.SmsObserver;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.mine.myinformation.UserBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.model.util.RegexUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.NetWorkUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ClearEditText;
import com.android.haobanyi.view.TitleBar;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/8/31.
 *
 * @作者: 付敏
 * @创建日期：2016/08/31
 * @邮箱：466566941@qq.com
 * @当前文件描述：第三方绑定登录，同时也是短信登录界面
 */
public class ThirdLoginActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.id_pwd_txt)
    TextView id_Txt;
    @BindView(R.id.vertify_code)
    TextView vertify_code;
    @BindView(R.id.edit_password)
    ClearEditText edit_phoneNum;
    @BindView(R.id.edit_code)
    ClearEditText edit_code;
    @BindView(R.id.btn_send_code)
    Button btn_sendCode;
    @BindView(R.id.id_tv_infor)
    TextView idTvInfor;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.id_hint)
    TextView idHint;
    private String phoneNum ="";//手机号
    //验证码
    private SmsObserver mObserver;
    private static final int MSG_RECEIVED_CODE = 1;

    private Handler countTimeHandler = new Handler();// 用于计时操作，这个需要分离一下
    private Timer timer = new Timer();
    private int countTime = 5;
    private boolean is_registe_by_messaage = false;
    PreferenceUtil complexPreferences;
    //手机的IP地址
    private String ipAddress;
    private long requestCount = 5;
    @Override
    protected int setLayout() {
        return R.layout.activity_bind_mobile;

    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        //是短信登录，还是第三方登录
        is_registe_by_messaage = this.getIntent().getBooleanExtra(Constants.IS_REGISTE_BY_MESSAAGE, false);
        initTittleBar();

        if (NetWorkUtil.isNetworkAvailable(this)) {
            if (NetWorkUtil.isWifiAvailable(this)) {
                ipAddress = NetWorkUtil.getIpAddressWithWifi(this);
            } else {
                ipAddress = NetWorkUtil.getIpAddressWithGPRS();

            }
        }


    }
    private void initTittleBar() {
        // 设置背景色  透明灰色  颜色 什么的最后定夺
        titleBar.setBackgroundResource(R.color.white);
        if (is_registe_by_messaage){
            titleBar.setTitle("短信登录");
        }else {
            titleBar.setTitle("绑定第三方登录");
        }

        titleBar.setDividerColor(Color.GRAY);
    }
    //读取短信内容是需要权限的
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_RECEIVED_CODE) {
                String code = (String) msg.obj;
                edit_code.setText(code);
            }
        }
    };
    @Override
    protected void initViews() {
        //如果是短信登录
        idTvInfor.setVisibility(View.GONE);
        if (is_registe_by_messaage){
            id_Txt.setVisibility(View.VISIBLE);
            vertify_code.setVisibility(View.VISIBLE);
            edit_phoneNum.setHint(" ");
            edit_code.setHint(" ");
            btnNext.setText("登录");

        }else {
            // 隐藏部分视图，更新字段，以符合设计图
            id_Txt.setVisibility(View.GONE);
            vertify_code.setVisibility(View.GONE);
            edit_phoneNum.setHint("请输入手机号或邮箱");
            edit_code.setHint("验证码");
            btnNext.setText("绑定账户");
        }

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void registerEventListener() {
        edit_phoneNum.addTextChangedListener(new TextWatcher() {
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
                    edit_phoneNum.setText(str1);
                    edit_phoneNum.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void registerBroadCastReceiver() {
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        mObserver = new SmsObserver(ThirdLoginActivity.this, mHandler);
                        Uri uri = Uri.parse("content://sms");
                        getContentResolver().registerContentObserver(uri, true, mObserver);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        ToastUtil.showShort(permissions.toString() + "权限拒绝");

                    }
                });

    }

    @Override
    protected void saveState(Bundle outState) {

    }
    @OnClick({R.id.btn_send_code, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_code:
                toSendCode();
                break;
            case R.id.btn_next:
                gotoNext();
                break;
        }
    }

    private void gotoNext() {
        final String account = edit_phoneNum.getText().toString();
        if (TextUtils.isEmpty(account)){
            setHintMessage("手机号或邮箱不能为空");
            return;
        }

        if ( !RegexUtil.validateMobile(account)&& !RegexUtil.validateEmail(account)){
            setHintMessage("手机号或邮箱格式不正确");
            return;
        }

        String validateCode =edit_code.getText().toString();

        if (TextUtils.isEmpty(validateCode)){
            setHintMessage("验证码不能为空");
            return;
        }
        btnNext.setEnabled(false);
        if (is_registe_by_messaage){
            requestCount = 5;
            loginByPhone(account, validateCode);

        }else {
            HashMap<String, String> params =new HashMap<String, String>();
            String loginType =this.getIntent().getStringExtra("LoginType");
            String rdm =this.getIntent().getStringExtra("Rdm");
            Log.d("ThirdLoginActivity", loginType);
            Log.d("ThirdLoginActivity", rdm);
            params.put("Account",account);
            params.put("ValidateCode",validateCode);
            params.put("LoginType",loginType);
            params.put("Rdm",rdm);
            requestCount = 5;
            bindUser(account, params);
        }


    }

    private void loginByPhone(final String account, final String validateCode) {
        this.appAction.loginByPhone(account, validateCode, ipAddress, new ActionCallbackListener<LoginResponseResult>() {
            @Override
            public void onSuccess(LoginResponseResult data) {
                successLogin(account);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    loginByPhone(account, validateCode);
                } else if ("003".equals(errorCode)) {
                    setHintMessage(errorMessage);
                    Toast.makeText(context, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
                } else {
                    setHintMessage(errorMessage);
                    Toast.makeText(context, "异常代码：" + errorCode + " 异常说明: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bindUser(final String account, final HashMap<String, String> params) {
        this.appAction.bindUser(params, new ActionCallbackListener<UserBean.DataBean>() {
            @Override
            public void onSuccess(UserBean.DataBean data) {
                successLogin(account);
            }
            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    bindUser(account, params);
                }else if ("003".equals(errorCode)) {
                    setHintMessage(errorMessage);
                    Toast.makeText(context, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
                }else {
                    setHintMessage(errorMessage);
                    Toast.makeText(context, errorMessage+"请重发一次获取验证码", Toast.LENGTH_SHORT).show();
                }
                //setHintMessage(errorMessage);//递归完以后还是会执行到这一步，所以必须去掉
            }
        });
    }

    private void successLogin(String account) {
        idHint.setText(" ");
        btnNext.setEnabled(true);
        saveLoginName(account);
        requestCount = 5;
        getUserInfo();
    }

    private void setHintMessage(String message){
        idHint.setVisibility(View.VISIBLE);
        idHint.setText(message);
        btnNext.setEnabled(true);

    }
    private void saveLoginName(String loginName ) {
        complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        complexPreferences.putString(Constants.REGISTTEDPN,loginName);
        //也就是说注册的账号，只能是手机号或者邮箱
        if (loginName.contains("@")){
            //说明是邮箱登录
            complexPreferences.putBoolean(Constants.REGISTTE_BY_PHONE, false);
        } else {
            complexPreferences.putBoolean(Constants.REGISTTE_BY_PHONE,true);
        }



    }

    private void getUserInfo() {
        this.appAction.getUserInfo(new ActionCallbackListener<UserBean.DataBean>() {
            @Override
            public void onSuccess(UserBean.DataBean data) {
                ToastUtil.showLong("登录成功");
                IntentUtil.gotoTopActivityWithoutData(ThirdLoginActivity.this, RadioGroupActivity.class, false);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {

                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    getUserInfo();
                } else if ("003".equals(errorCode)) {
                    Toast.makeText(context, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "异常代码：" + errorCode + " 异常说明: " + errorMessage, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    //为什么一发送就销毁了？
    private void toSendCode() {
        phoneNum = edit_phoneNum.getText().toString();
        btn_sendCode.setEnabled(false);
        requestCount = 5;
        sendSmsCode();

    }

    private void sendSmsCode() {
        this.appAction.sendSmsCode(phoneNum, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                Toast.makeText(context, R.string.toast_code_has_sent, Toast.LENGTH_SHORT).show();
                //计时操作
                countTime();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    sendSmsCode();
                }else if ("003".equals(errorCode)) {
                    Toast.makeText(context, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, errorMessage+"请重发一次获取验证码", Toast.LENGTH_SHORT).show();
                }
                btn_sendCode.setEnabled(true);
            }
        });
    }

    //计时操作
    private void countTime() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                countTimeHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (countTime < 1) {
                            setButtonStatusOn();
                        } else {
                            setButtonStatusOff();
                        }

                    }

                    private void setButtonStatusOn() {
                        timer.cancel();
                        btn_sendCode.setText(context.getText(R.string.re_send_code));
                        btn_sendCode.setTextColor(context.getResources().getColor(R.color.grey_hint));
                        countTime = 5;
                        btn_sendCode.setEnabled(true);

                    }

                    private void setButtonStatusOff() {
                        btn_sendCode.setText(String.format(context.getResources().getString(R.string.count_down),
                                countTime--));
                        btn_sendCode.setTextColor(context.getResources().getColor(R.color.font_red));
                    }

                });
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
    }
}
