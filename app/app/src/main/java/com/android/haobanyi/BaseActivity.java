/**
 * Copyright (C) 2015. Keegan小钢（http://keeganlee.me）
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.haobanyi;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.AppAction;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.ToastUtil;

import static com.baidu.location.b.g.T;

/**
 * Activity抽象基类
 * @author 付敏
 * @version 1.0 创建时间：16/3/20
 * 描述：AppCompatActivity  应该替换这个类比价好 import android.support.v7.app.AppCompatActivity;
 */
public abstract class BaseActivity extends FragmentActivity {
    //设置TAG标识
    protected String TAG ;
    //标识量：无布局内容ID
    private static final int NO_LAYOUT_CONTENT_ID = 0;
    // 上下文实例
    protected Context context;
    // 应用全局的实例
    protected BaseApplication application;
    // 核心层的Action实例
    protected AppAction appAction;
    protected long requestCount = 5; //通用继承过来，通用的都是可以继承的

    //进度条
    protected SpotsDialog progressDialog;

    //全局性的缓存
    protected PreferenceUtil basePreference;

    //是否登录
    protected boolean isLogin =false;//减少重复创建对象

    //是否更新token成功
    protected boolean  hasTokenRefreshed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.设置界面布局
        int layoutID = setLayout();
        if (NO_LAYOUT_CONTENT_ID != layoutID) {
            setContentView(layoutID);
            ButterKnife.bind(this);
        }

        //两种方式都是可以的
        this.TAG = getClass().getSimpleName();
        this.context = getApplicationContext();
        this.application = BaseApplication.getApplication();
        this.appAction = application.getAppAction();
        /*绑定视图*/
        ButterKnife.bind(this);
        /*更新时间戳*/
        NTPTime.getInstance().getCurrentTime();
        /*在有网的情况下，更新token*/
        basePreference = PreferenceUtil.getSharedPreference(context, "preferences");
        refreshToken();
        //进度条
        progressDialog =  new SpotsDialog(this);
        //2.恢复状态数据
        initVariables(savedInstanceState);
        //4.初始化数据，这个数据也包括从其他界面中传递过来的数据 【更改下顺序，先加载数据，后初始化视图】
        loadData();
        //3.初始化界面控件
        initViews();
        //5.注册事件监听
        registerEventListener();
        //6.注册广播接收器
        registerBroadCastReceiver();
    }
    private void refreshToken() {

        isLogin = basePreference.getBoolean(Constants.ISLOGIN, false);
        if (isLogin){
            //超过过期时间，更新token
            long refreshTime = basePreference.getLong(Constants.START_TIME,-1000l);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                requestCount = 5;
                getToken();
            }
        }
    }

    private void getToken() {
        appAction.getToken(new ActionCallbackListener<LoginResponseResult>() {
            @Override
            public void onSuccess(LoginResponseResult data) {
                hasTokenRefreshed = true;
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                //5秒的限制，其余情况要不给出提示，要不给出按钮，要不什么都不做
                if ("998".equals(errorCode) && requestCount>0) {
                        requestCount--;
                        getToken();
                }else {
                    hasTokenRefreshed = false;
                }

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveState(outState);
        super.onSaveInstanceState(outState);
    }



    //1.设置界面布局是需要子类来实现的
    protected abstract int setLayout();
    //2.初始化全局变量，有些数据是在绘制界面之前获取
    protected abstract void initVariables(Bundle savedInstanceState);
    //3.初始化界面控件
    protected abstract void initViews();
    //4.初始化数据
    protected abstract void loadData();
    //5.注册事件监听
    protected abstract void registerEventListener();
    //6.注册广播接收器
    protected abstract void registerBroadCastReceiver();
    //7.activity销毁前保存状态数据，和initVariables（）方法遥相呼应
    protected abstract void saveState(Bundle outState);
    /**
     * 7.通过泛型来简化findViewById
     */
    protected final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            Log.e(TAG, "Could not cast View to concrete class.", ex);
            throw ex;
        }
    }
    /*
    *     public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setTitleText("数据加载中...");
        }
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
    *
    * */

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
