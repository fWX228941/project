package com.android.haobanyi.activity.guid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.haobanyi.R;
import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.util.AppConstants;
import com.android.haobanyi.util.PreferenceUtil;

/**
 * Created by fWX228941 on 2016/3/23.
 * 描述：启动屏Logo界面
 */
public class SplashActivity extends BaseActivity {
    private boolean isFirstOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //可以单独建立一个PreferenceKeyUtil.java来专门存放设置项的KEY键值
        isFirstOpen=PreferenceUtil.getSharedPreference(this, AppConstants.KEY_FIRST_OPEN,true);
        // 判断是否是第一次开启应用，如果是第一次启动，则先进入功能引导页
        if (isFirstOpen){
            enterWelcomeGuideActivity();
        }else {
            // 如果不是第一次启动app，则正常显示启动屏  并且开启一个线程延迟两秒后跳入主界面
            setContentView(R.layout.activity_splash);
            enterloginActivity();;
        }
    }

    @Override
    protected int setLayout() {
        return 0;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

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

    }

    @Override
    protected void saveState(Bundle outState) {

    }


    private void enterWelcomeGuideActivity() {
        Intent intent = new Intent(this, WelcomeGuideActivity.class);
        startActivity(intent);
        finish();
    }

    private void enterloginActivity() {
        new Handler().postDelayed(new Runnable() {
            //如论如何这个地方肯定是需要调用的
            @Override
            public void run() {
                //Intent intent = new Intent(this, LoginActivity.class); 这真是奇葩问题，匿名内部类
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
