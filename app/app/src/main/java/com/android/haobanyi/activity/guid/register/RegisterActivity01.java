package com.android.haobanyi.activity.guid.register;

import android.graphics.Color;
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
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.RegexUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.view.ClearEditText;
import com.android.haobanyi.view.TitleBar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class RegisterActivity01 extends BaseActivity {
    private static final String TAG = "fumin";
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.edit_phone)
    ClearEditText editPhone;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.id_hint)
    TextView idHint;
    @BindView(R.id.tv_third_explain)
    TextView tvThirdExplain;
    private int STATE_FORGETPASSWORD;
    private String phoneNumFrom ="";
    @Override
    protected int setLayout() {
        return R.layout.activity_register_01;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        //01：找回密码   00：注册
        STATE_FORGETPASSWORD = this.getIntent().getIntExtra(Constants.STATE_FROM_FPW,00);
        //02.从上一级返回过来的手机号码
        phoneNumFrom = this.getIntent().getStringExtra(Constants.PHONE_NUMBER);

    }

    @Override
    protected void initViews() {
        initTittleBar();
        initForgetPW();

    }

    private void initForgetPW() {
//        tvThirdExplain.setVisibility(View.GONE);
        //从上一级返回过来的手机号码
        if (!TextUtils.isEmpty(phoneNumFrom)){
            btnNext.setClickable(true);
            btnNext.setBackgroundResource(R.drawable.bg_btn_clickable);
            editPhone.setText(phoneNumFrom);
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
                finish();
                RegisterActivity01.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
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

    }

    @Override
    protected void registerEventListener() {
        /*监听用户输入，只有输入完之后，就变色，怎么判断这个输入完*/
        editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "输入前");


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "输入中");
                if (TextUtils.isEmpty(editPhone.getText().toString())) {
                    btnNext.setClickable(false);
                    btnNext.setBackgroundResource(R.drawable.bg_btn_normal_01);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "输入后");
                btnNext.setClickable(true);
                btnNext.setBackgroundResource(R.drawable.bg_btn_clickable);
            }
        });

    }

    @Override
    protected void registerBroadCastReceiver() {

    }

    @Override
    protected void saveState(Bundle outState) {

    }


    @OnClick({R.id.btn_next,R.id.tv_third_explain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                goToNext();
                break;
            case R.id.tv_third_explain:
                goToThirdExplain();
                break;
        }
    }
    //第三方协议
    private void goToThirdExplain() {

        IntentUtil.gotoActivityWithoutData(RegisterActivity01.this, ThirdExplainActivity.class, false);


    }

    private void goToNext() {
        String phoneNum = editPhone.getText().toString();
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (TextUtils.isEmpty(phoneNum)){
            idHint.setVisibility(View.VISIBLE);
            idHint.setText("手机号或邮箱不能为空");
            btnNext.setClickable(false);
            btnNext.setBackgroundResource(R.drawable.bg_btn_normal_01);
            /*且不包括@*/
        } else if (!matcher.matches() && !RegexUtil.validateEmail(phoneNum)) {
            Log.d(TAG, "手机号或者邮箱格式不正确");
            idHint.setVisibility(View.VISIBLE);
            idHint.setText("手机号或者邮箱格式不正确");
            btnNext.setClickable(true);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PHONE_NUMBER, phoneNum);
            if (STATE_FORGETPASSWORD == 01){
                bundle.putInt(Constants.STATE_FROM_FPW, 01);
            } else {
                bundle.putInt(Constants.STATE_FROM_FPW, 00);
            }
            IntentUtil.gotoActivityWithData(RegisterActivity01.this, RegisterActivity02.class, bundle, true);
        }

    }
}
