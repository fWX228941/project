package com.android.haobanyi.activity.guid.register;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.view.TitleBar;

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
public class RegisterActivity03 extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.id_textView_tip)
    TextView idTextViewTip;
    @BindView(R.id.id_textView_tip_02)
    TextView idTextViewTip02;
    @BindView(R.id.btn_login)
    Button btnLogin;
    private String phoneNum;//手机号
    private String phonePWD;//登录密码
    private int STATE_FORGETPASSWORD;




    @Override
    protected int setLayout() {
        return R.layout.activity_register_03;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        //获取标识位：标识找回密码
        STATE_FORGETPASSWORD = this.getIntent().getIntExtra(Constants.STATE_FROM_FPW,00);
    }

    @Override
    protected void initViews() {
        initTittleBar();
        initForgetPW();
    }
    private void initForgetPW() {
        idTextViewTip.setText("好办易，恭喜你成功找回密码");
    }
    private void initTittleBar() {
        // 设置背景色  透明灰色  颜色 什么的最后定夺
        titleBar.setBackgroundResource(R.color.white);

        if (STATE_FORGETPASSWORD == 01){
            titleBar.setTitle("找回成功");

        } else {
            titleBar.setTitle("完成注册");
        }

        titleBar.setDividerColor(Color.GRAY);
    }
    @Override
    protected void loadData() {
        //从启动activity中获取手机号码
        phoneNum = this.getIntent().getStringExtra(Constants.PHONE_NUMBER);
        //从启动activity中获取密码
        phonePWD = this.getIntent().getStringExtra(Constants.PHONE_PASSWORD);
        if (!TextUtils.isEmpty(phoneNum)){
            idTextViewTip02.setText("登录名："+phoneNum+"！");
        }

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


    @OnClick(R.id.btn_login)
    public void onClick() {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PHONE_NUMBER, phoneNum);
        bundle.putString(Constants.PHONE_PASSWORD, phonePWD);
        bundle.putInt(Constants.STATE_FROM_RGT_SUC, 03);
        IntentUtil.gotoActivityWithoutData(RegisterActivity03.this, LoginActivity.class, true);
    }
}
