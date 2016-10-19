package com.android.haobanyi.activity.guid.register;

import android.Manifest;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.listener.SmsObserver;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.mine.myinformation.UserBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.AppConstants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.PreferenceUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ClearEditText;
import com.android.haobanyi.view.TitleBar;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/6/3.
 *
 * @作者: 付敏
 * @创建日期：2016/06/03
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class RegisterActivity02 extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.edit_code)
    ClearEditText edit_code;
    @BindView(R.id.btn_send_code)
    Button btn_sendCode;
    @BindView(R.id.edit_password)
    ClearEditText edit_password;
    @BindView(R.id.id_pwd)
    ImageView idPwd;
    @BindView(R.id.btn_next)
    Button btn_register;//btnNext
    @BindView(R.id.id_hint)
    TextView idHint;
    @BindView(R.id.id_pwd_txt)
    TextView td_txt_pwd;
    private String phoneNum ="";//手机号
    private int STATE_FORGETPASSWORD;

    private Handler countTimeHandler = new Handler();// 用于计时操作，这个需要分离一下
    private Timer timer = new Timer();
    private int countTime = 5;
    boolean isSelected = true;
    private long requestCount = 5;
    //验证码
    private SmsObserver mObserver;
    private static final int MSG_RECEIVED_CODE = 1;

    @Override
    protected int setLayout() {
        return R.layout.activity_register_02;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        //获取标识位：标识找回密码
        STATE_FORGETPASSWORD = this.getIntent().getIntExtra(Constants.STATE_FROM_FPW,00);

    }

    @Override
    protected void initViews() {
        initTittleBar();
        initForgetPW();//复用
    }
    private void initForgetPW() {
        if (STATE_FORGETPASSWORD == 01){
            td_txt_pwd.setText("重置密码：");
            td_txt_pwd.setGravity(Gravity.RIGHT);
        } else {
            td_txt_pwd.setText("注册密码：");
            td_txt_pwd.setGravity(Gravity.RIGHT);
        }


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
//                finish();
                if (STATE_FORGETPASSWORD == 01){
                     /*找回密码*/
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.PHONE_NUMBER, phoneNum);
                    bundle.putInt(Constants.STATE_FROM_FPW, 01);
                    IntentUtil.gotoActivityWithData(RegisterActivity02.this, RegisterActivity01.class, bundle,true);
                }else{
                    /*注册*/
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.PHONE_NUMBER, phoneNum);
                    bundle.putInt(Constants.STATE_FROM_FPW, 00);
                    IntentUtil.gotoActivityWithData(RegisterActivity02.this, RegisterActivity01.class,
                            bundle, true);
                }


//                RegisterActivity02.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });

        if (STATE_FORGETPASSWORD == 01){
            titleBar.setTitle(R.string.title_activity_re_password);

        } else {
            titleBar.setTitle(R.string.title_activity_register);
        }
        titleBar.setDividerColor(Color.GRAY);
    }

    @Override
    protected void loadData() {
        //从启动activity中获取手机号码
       phoneNum = this.getIntent().getStringExtra(Constants.PHONE_NUMBER);


    }

    @Override
    protected void registerEventListener() {
        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "输入前");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "输入中");
                if (TextUtils.isEmpty(edit_password.getText().toString())) {
                    btn_register.setClickable(false);
                    btn_register.setBackgroundResource(R.drawable.bg_btn_normal_01);
                }

                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    edit_password.setText(str1);

                    edit_password.setSelection(start);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "输入后");
                btn_register.setClickable(true);
                btn_register.setBackgroundResource(R.drawable.bg_btn_clickable);
            }
        });

    }

    @Override
    protected void registerBroadCastReceiver() {
        //监听短信，获取验证码


        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission
                        .RECEIVE_SMS, Manifest.permission.READ_SMS)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        mObserver = new SmsObserver(RegisterActivity02.this, mHandler);
                        Uri uri = Uri.parse("content://sms");
                        getContentResolver().registerContentObserver(uri, true, mObserver);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        ToastUtil.showShort(permissions.toString() + "权限拒绝");

                    }
                });

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
    protected void saveState(Bundle outState) {

    }


    @OnClick({R.id.btn_send_code, R.id.id_pwd, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_code:
                requestCount = 5;
                toSendCode();
                break;
            case R.id.id_pwd:
                changePwdState();
                break;
            case R.id.btn_next:
                toRegisterOrFoundPwd();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
    }

    private void toRegisterOrFoundPwd() {
        String code = edit_code.getText().toString();
        final String password = edit_password.getText().toString();
        String ip = PreferenceUtil.getSharedPreference(this, AppConstants.KEY_IP_ADDRESS, "0.0.0.0");
        Log.d("fumin", "ip:" + ip);
        btn_register.setEnabled(false);
        if (STATE_FORGETPASSWORD == 01){
            /*找回密码*/
            requestCount = 5;
            findPwd(code, password);

        } else {
            /*注册*/
            requestCount = 5;
            register(code, password, ip);
        }

    }

    private void register(final String code, final String password, final String ip) {
        this.appAction.register(phoneNum, code, password, ip, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                idHint.setText(" ");
                btn_register.setEnabled(true);
                ToastUtil.showShort(data.getMessage());
                Toast.makeText(context, R.string.toast_register_success, Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PHONE_NUMBER, phoneNum);
                bundle.putInt(Constants.STATE_FROM_FPW, 00);
                IntentUtil.gotoActivityWithData(RegisterActivity02.this, RegisterActivity03.class, bundle, true);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    register(code, password, ip);
                }else if ("003".equals(errorCode)) {
                    setHintMessage(errorMessage);
                    Toast.makeText(context, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
                    Log.d("RegisterActivity02", "网络异常，请检查网络");
                }else {
                    setHintMessage(errorMessage);
                    Toast.makeText(context, "异常代码：" + errorCode + " 异常说明: " + errorMessage, Toast.LENGTH_SHORT).show();
                }

                btn_register.setEnabled(true);//顺序也是挺重要的，这样998 的情况只会出现一次

            }
        });
    }

    private void findPwd(final String code, final String password) {
        this.appAction.findPwd(phoneNum, password, code, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                idHint.setText(" ");
                btn_register.setEnabled(true);
                ToastUtil.showShort(data.getMessage());
                Toast.makeText(context, R.string.toast_register_success, Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PHONE_NUMBER, phoneNum);
                bundle.putString(Constants.PHONE_PASSWORD, password);
                bundle.putInt(Constants.STATE_FROM_FPW, 01);
                // getUserInfo(bundle);
                IntentUtil.gotoActivityWithData(RegisterActivity02.this, RegisterActivity03.class, bundle, true);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                btn_register.setEnabled(true);
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    findPwd(code, password);
                }else if ("003".equals(errorCode)) {
                    setHintMessage(errorMessage);
                    Toast.makeText(context, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
                }else {
                    setHintMessage(errorMessage);
                    Toast.makeText(context, "异常代码：" + errorCode + " 异常说明: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setHintMessage(String errorMessage) {
        idHint.setVisibility(View.VISIBLE);
        idHint.setText(errorMessage);
    }

    /*暂时不用*/
/*    private void getUserInfo(final Bundle bundle) {
        this.appAction.getUserInfo(new ActionCallbackListener<UserBean.DataBean>() {
            @Override
            public void onSuccess(UserBean.DataBean data) {
                IntentUtil.gotoActivityWithData(RegisterActivity02.this, RegisterActivity03.class, bundle, true);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {

            }
        });
    }*/

    //发送验证码
    private void toSendCode() {
        btn_sendCode.setEnabled(false);
        Log.d("RegisterActivity02", phoneNum);
        //countTime();
        this.appAction.sendSmsCode(phoneNum, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                if (!phoneNum.contains("@")){
                    ToastUtil.showShort(R.string.toast_code_has_sent);
                }else {
                    ToastUtil.showShort("验证码已发送到邮箱");
                }
                countTime();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    toSendCode();
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
    //隐藏或者显示密码【两种点击状态】
    private void changePwdState() {

        if (isSelected){
            //显示密码
            idPwd.setImageResource(R.drawable.o_eyes);
            edit_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //03.需要设置光标
            edit_password.setSelection(edit_password.getText().toString().length());
        } else {
            //隐藏密码
            idPwd.setImageResource(R.drawable.c_eyes);
            edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //03.需要设置光标
            edit_password.setSelection(edit_password.getText().toString().length());
        }
        isSelected = !isSelected;
    }
}
