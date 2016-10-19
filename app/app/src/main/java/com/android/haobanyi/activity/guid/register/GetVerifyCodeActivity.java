package com.android.haobanyi.activity.guid.register;

import android.Manifest;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.listener.SmsObserver;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
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
 * Created by fWX228941 on 2016/8/9.
 *
 * @作者: 付敏
 * @创建日期：2016/08/09
 * @邮箱：466566941@qq.com
 * @当前文件描述：获取验证码界面
 */
public class GetVerifyCodeActivity extends BaseActivity {

    private static final int BACK_TO_GVCA = 0;
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
    PreferenceUtil complexPreferences;
    String phoneOrEmail;
    private long requestCount = 5;
    @Override
    protected int setLayout() {
        return R.layout.activity_bind_mobile;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

        // 不需要用户输入
        complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        is_registte_by_phone = complexPreferences.getBoolean(Constants.REGISTTE_BY_PHONE, true);
        phoneOrEmail =  complexPreferences.getString(Constants.REGISTTEDPN, "");

    }

    @Override
    protected void initViews() {
        initTittleBar();
        if (is_registte_by_phone){
            id_Txt.setText("手机号码");
        } else {
            id_Txt.setText("邮箱");
        }
        edit_phoneNum.setText(phoneOrEmail);


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
                GetVerifyCodeActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });

        if (is_registte_by_phone){
            titleBar.setTitle("手机验证");
        } else {
            titleBar.setTitle("邮箱验证");
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
                        mObserver = new SmsObserver(GetVerifyCodeActivity.this, mHandler);
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
        Bundle bundle = new Bundle();
        bundle.putString(Constants.VALIDATECODE, edit_code.getText().toString());
        bundle.putBoolean(Constants.IS_MODIFY_PAY_PWD, true); //代表手机
        complexPreferences.putBoolean(Constants.IS_MODIFY_PAY_PWD, true);
        IntentUtil.gotoActivityForResultWithData(GetVerifyCodeActivity.this, ModifyPwdActivity
                .class, BACK_TO_GVCA, bundle);//都是要执行finish 的操作
    }
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
