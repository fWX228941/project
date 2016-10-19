package com.android.haobanyi.activity.mine.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.guid.register.BindMobileOrEmailActivity;
import com.android.haobanyi.activity.guid.register.GetVerifyCodeActivity;
import com.android.haobanyi.activity.guid.register.ModifyPwdActivity;
import com.android.haobanyi.activity.guid.register.SetPayPwdActivity;
import com.android.haobanyi.model.bean.mine.myinformation.UserBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.view.TitleBar;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/8/8.
 *
 * @作者: 付敏
 * @创建日期：2016/08/08
 * @邮箱：466566941@qq.com
 * @当前文件描述： activity_user_setting  用户设置
 */
public class UserSettingActivity extends BaseActivity {


    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.text_modity_psw)
    LinearLayout TextMineContact;
    @BindView(R.id.text_phone_vertify)
    LinearLayout TextPhoneVertify;
    @BindView(R.id.text_message_vertify)
    LinearLayout TextMessageVertify;
    @BindView(R.id.text_pswd)
    LinearLayout TextPswd;
    @BindView(R.id.text_user_feedback)
    LinearLayout TextUserFeedback;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.id_hint)
    TextView idHint;

    private static final int TO_MODIFYPWD = 1;//跳转到修改密码界面/支付密码界面


    @Override
    protected int setLayout() {
        return R.layout.activity_user_setting;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

        handleLogic();

    }

    private void handleLogic() {

        initTittleBar();
        //登录和退出登录
        if (isLogin){
            btnLogin.setText("退出登录");
        }else {
            btnLogin.setText("登录");
        }
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
                UserSettingActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitle("设置");
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



    @OnClick({R.id.text_modity_psw, R.id.text_phone_vertify, R.id.text_message_vertify, R.id.text_pswd, R.id
            .text_user_feedback, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_modity_psw:
                gotoModifyPwd();//modify_pwd_activity
                break;
            case R.id.text_phone_vertify:
                totoBind(01);
                break;
            case R.id.text_message_vertify:
                totoBind(00);
                break;
            case R.id.text_pswd:
                gotoModifyPayPwd();//modify_pwd_activity
                break;
            case R.id.text_user_feedback:
                gotoFeedback();
                break;
            case R.id.btn_login:
                //这个标识码和以前的不同
                unregisterPwd();
                break;
        }
    }
    /*退出登录*/
    private void unregisterPwd() {
        /*如果是在已经登录的情况下，则显示登录按钮，如果是在风登录的情况下，则退出登录*/
        if (isLogin){
            basePreference.putBoolean(Constants.ISLOGIN, false);
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.STATE_FROM_SHOPPING, 03);
            IntentUtil.gotoTopActivityWithData(UserSettingActivity.this, RadioGroupActivity.class, bundle, true);
        }else {
            IntentUtil.gotoActivityWithoutData(UserSettingActivity.this, LoginActivity.class, false);
        }




    }

    private void gotoFeedback() {
        IntentUtil.gotoActivityForResult(UserSettingActivity.this, UserFeedbackActivity.class, TO_MODIFYPWD);
    }

    private void gotoModifyPayPwd() {
//        Boolean is_first_modify_pay_pwd =basePreference.getBoolean(Constants.IS_FIRST_MODIFY_PAY_PWD, true);//第一次为true ，以后的每一次都是设置为false 只执行一次
        UserBean.DataBean user = basePreference.getObject(Constants.USER_INFO, UserBean.DataBean.class);

        if (user.isHasPayPwd()){
            IntentUtil.gotoActivityForResult(UserSettingActivity.this, ModifyPwdActivity.class, TO_MODIFYPWD);
        } else {
            IntentUtil.gotoActivityForResult(UserSettingActivity.this, SetPayPwdActivity.class, TO_MODIFYPWD); //一定要注意细节，不然很容易就出现问题了

        }
    }

    /*跳转到绑定界面*/
    private void totoBind(int value) {
        //activity之间传递参数失效
/*        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TO_EMAIL_OR_TO_PHONEBIND, value);*/
        basePreference.putInt(Constants.TO_EMAIL_OR_TO_PHONEBIND, value);
        Log.d("UserSettingActivity", "value:" + value);
/*        Log.d("UserSettingActivity", "bundle.getInt(Constants.TO_EMAIL_OR_TO_PHONEBIND):" + bundle.getInt(Constants
                .TO_EMAIL_OR_TO_PHONEBIND));*/
        // 先存入文件中，难道是跨包了？

        // 01 代表手机绑定，00代表邮箱绑定  难道是因为跨包的原因
        IntentUtil.gotoActivityForResult(UserSettingActivity.this, BindMobileOrEmailActivity.class, TO_MODIFYPWD);


    }

    /*修改密码*/
    private void gotoModifyPwd() {
        // 一个逻辑就是是否登录
        // 如果是手机注册的就显示手机，如果是邮箱注册的就显示邮箱
/*        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_MODIFY_PAY_PWD, false);*/
        basePreference.putBoolean(Constants.IS_MODIFY_PAY_PWD, false);

        IntentUtil.gotoActivityForResult(UserSettingActivity.this, ModifyPwdActivity.class, TO_MODIFYPWD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TO_MODIFYPWD:
                finish();
                handleLogic();
            break;
        }
    }
}
