package com.android.haobanyi.activity.mine.property;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
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
 * @当前文件描述：我的财产界面
 */
public class MyPropertyActivity extends BaseActivity {
    PreferenceUtil complexPreferences;
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.text_balance)
    LinearLayout textBalance;
    @BindView(R.id.text_my_coupon)
    LinearLayout textMyCoupon;
    @BindView(R.id.text_haobanyi_bonus)
    LinearLayout textHaobanyiBonus;
    @BindView(R.id.text_modity_psw)
    LinearLayout textModityPsw;

    @Override
    protected int setLayout() {
        return R.layout.activity_my_property;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
/*        NTPTime.getInstance().getCurrentTime();
        complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);
        if (isLogin) {
            long refreshTime = complexPreferences.getLong(Constants.START_TIME, -1000l);
            if (System.currentTimeMillis() / 1000L - refreshTime > 7200) {
                //超过过期时间，更新token
                requestCount = 5;
                getToken();
            } else {
                handleLogic();
            }


        } else {
            handleLogic();
        }*/


    }

 /*   private void getToken() {
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

    private void loginForToken() {
        complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(MyPropertyActivity.this, LoginActivity.class, LOGIN_TO_SPV);//跳转到登录界面
    }*/

/*    private void handleLogic() {


    }*/

    @Override
    protected void initViews() {
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
                MyPropertyActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitle("我的财产");
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



    @OnClick({R.id.text_balance, R.id.text_my_coupon, R.id.text_haobanyi_bonus, R.id.text_modity_psw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_balance:
                IntentUtil.gotoActivityWithoutData(MyPropertyActivity.this, AccountBalanceActivity.class, false);
                break;
            case R.id.text_my_coupon:
                IntentUtil.gotoActivityWithoutData(MyPropertyActivity.this, VoucherActivity.class, false);
                break;
            case R.id.text_haobanyi_bonus:
                IntentUtil.gotoActivityWithoutData(MyPropertyActivity.this, MyRedEnvelopeListActivity.class, false);
                break;
            case R.id.text_modity_psw:
                //会员积分
                IntentUtil.gotoActivityWithoutData(MyPropertyActivity.this, UserPointActivity.class, false);
                break;
        }
    }
}
