package com.android.haobanyi.activity.guid.register;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.listener.SmsObserver;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
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
 * Created by fWX228941 on 2016/8/8.
 *
 * @作者: 付敏
 * @创建日期：2016/08/08
 * @邮箱：466566941@qq.com
 * @当前文件描述：绑定手机activity_bind_mobile 逻辑基本上类似activity_register_02
 * 绑定邮箱界面
 *
 *  这个还需要添加一个界面
 *  ConformMobileOrEmailActivity
 *  视图也要修改下
 *
 *
 */
public class BindMobileOrEmailActivity extends BaseActivity {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.id_pwd_txt)
    TextView id_Txt;
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
    private boolean is_registte_by_phone;
    private int TO_EMAIL_OR_TO_PHONEBIND;
    private boolean isFirstBindPhone;
    private boolean isFirstBindEmail;
    private boolean isVertifyOldPhone;
    private boolean isVertifyOldEmail;
    private static final int BACK_TO_BMOA = 0;
    private static final int LOGIN_TO_BMOA = 3;
    HashMap<String, String> params =new HashMap<String, String>();

    @Override
    protected int setLayout() {
        return R.layout.activity_bind_mobile;
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
    /*更新token的情况就不需要考虑过期了*/
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
        is_registte_by_phone = basePreference.getBoolean(Constants.REGISTTE_BY_PHONE, false);
        Log.d("BindMobileOrEmailActivi", "is_registte_by_phone:" + is_registte_by_phone);
        //判断是从哪个按钮点击过来的
        TO_EMAIL_OR_TO_PHONEBIND = basePreference.getInt(Constants.TO_EMAIL_OR_TO_PHONEBIND, 01);
        Log.d("BindMobileOrEmailActivi", "TO_EMAIL_OR_TO_PHONEBIND:" + TO_EMAIL_OR_TO_PHONEBIND);

        isFirstBindPhone = !is_registte_by_phone && TO_EMAIL_OR_TO_PHONEBIND == 01;
        isFirstBindEmail = is_registte_by_phone && TO_EMAIL_OR_TO_PHONEBIND == 00;
        isVertifyOldPhone = is_registte_by_phone && TO_EMAIL_OR_TO_PHONEBIND == 01;
        isVertifyOldEmail =  !is_registte_by_phone && TO_EMAIL_OR_TO_PHONEBIND == 00;

        initTittleBar();

        if (isFirstBindPhone ||isVertifyOldPhone){
            id_Txt.setText("手机号码：");
        } else if (isFirstBindEmail||isVertifyOldEmail){
            id_Txt.setText("邮箱：");
        }


    }

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(BindMobileOrEmailActivity.this, LoginActivity.class, LOGIN_TO_BMOA);//跳转到登录界面
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
                BindMobileOrEmailActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });

        if (is_registte_by_phone && TO_EMAIL_OR_TO_PHONEBIND == 01){
            titleBar.setTitle("验证旧手机");
        } else if (!is_registte_by_phone && TO_EMAIL_OR_TO_PHONEBIND == 01){
            titleBar.setTitle("绑定手机");
        } else if (is_registte_by_phone && TO_EMAIL_OR_TO_PHONEBIND == 00){
            titleBar.setTitle("绑定邮箱");
        } else if (!is_registte_by_phone && TO_EMAIL_OR_TO_PHONEBIND == 00){
            titleBar.setTitle("验证旧邮箱");

        }
        titleBar.setDividerColor(Color.GRAY);
    }
    @Override
    protected void loadData() {

    }

    @Override
    protected void registerEventListener() {

    }
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
    protected void registerBroadCastReceiver() {


        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission
                        .RECEIVE_SMS, Manifest.permission.READ_SMS)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        mObserver = new SmsObserver(BindMobileOrEmailActivity.this, mHandler);
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
        /*01.保存正确的手机验证码*/
        // 先尝试，不行的话就再多写一个界面

        btnNext.setEnabled(false);
        params.put(Constants.VALIDATECODEOFBIND,edit_code.getText().toString());
        if (isFirstBindPhone){
            params.put(Constants.MOBILE,phoneNum);
            requestCount = 45;
            bindMobile(params);

        }else if (isFirstBindEmail){
            //验证旧手机是要跳转到新的界面的
            params.put(Constants.EMAIL,phoneNum);
            requestCount = 45;
            bindEmail(params);
        }else {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.VALIDATECODE, edit_code.getText().toString());
            if (isVertifyOldPhone){
                bundle.putBoolean(Constants.REGISTTE_BY_PHONE, isVertifyOldPhone); //代表手机

            } else {
                bundle.putBoolean(Constants.REGISTTE_BY_PHONE, !isVertifyOldPhone);//代表邮箱
            }
            IntentUtil.gotoActivityForResultWithData(BindMobileOrEmailActivity.this, ConformMobileOrEmailActivity
                    .class, BACK_TO_BMOA, bundle);//都是要执行finish 的操作

        }

    }

    private void bindEmail(final HashMap<String, String> params) {
        this.appAction.bindEmail(params, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage(data.getMessage());
                idHint.setText(" ");
                btnNext.setEnabled(true);
                finish();
                BindMobileOrEmailActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if (!btnNext.isEnabled()){
                    btnNext.setEnabled(true);
                }

                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    bindEmail(params);
                }else if ("996".equals(errorCode)){
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    setHintMessage(errorMessage);
                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                    setHintMessage(errorMessage);
                }

            }
        });
    }

    private void bindMobile(final HashMap<String, String> params) {
        this.appAction.bindMobile(params, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage(data.getMessage());
                idHint.setText(" ");
                btnNext.setEnabled(true);
                //成功后，回到主界面
                finish();
                BindMobileOrEmailActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if (!btnNext.isEnabled()){
                    btnNext.setEnabled(true);
                }
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    bindMobile(params);
                }else if ("996".equals(errorCode)){
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");//之后的被截断了
                    loginForToken();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    setHintMessage(errorMessage);

                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                    setHintMessage(errorMessage);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BACK_TO_BMOA:
                finish();
                BindMobileOrEmailActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
                break;
            case LOGIN_TO_BMOA:
                handleLogic();//重新绘制一遍就是了，为了避免重复绘制，需要判断下
                break;

        }
    }

    private void toSendCode() {
        phoneNum = edit_phoneNum.getText().toString();
        btn_sendCode.setEnabled(false);
        requestCount = 45;
        sendSmsCode();

    }

    private void sendSmsCode() {
        this.appAction.sendSmsCode(phoneNum, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage(data.getMessage());
                //计时操作
                countTime();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {

                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    sendSmsCode();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                }else {
                    ToastUtil.showHintMessage("请重发一次获取验证码",errorCode,errorMessage);
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

                    @SuppressLint("StringFormatMatches")
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
