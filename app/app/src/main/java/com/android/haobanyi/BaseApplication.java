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

import android.app.Application;
import android.content.Intent;

import com.android.haobanyi.activity.home.LocationService;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.core.service.TimeService;
import com.android.haobanyi.model.db.CityDBManager;
import com.jiongbull.jlog.JLog;
import com.jiongbull.jlog.constant.LogLevel;

import java.util.ArrayList;
import java.util.List;


import com.android.haobanyi.core.AppAction;
import com.android.haobanyi.core.AppActionImpl;
import com.android.haobanyi.util.AppConstants;
import com.android.haobanyi.util.NetWorkUtil;
import com.android.haobanyi.util.PreferenceUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import butterknife.ButterKnife;


/**
 * Application类，应用级别的操作都放这里
 *
 *
 */
public class BaseApplication extends Application {
    //1.tag名字到时改
    private static final String TAG = "HaobanyiApp";

    //2.初始化全局参数
    private  AppAction appAction;

    //3.提供一个全局的对象-单例模式
    private static BaseApplication BaseApplication = null;

    //4.手机的IP地址
    private String ipAddress = "0.0.0.0";

    //5.城市数据库
    private CityDBManager cityManager;


    /**
     * 1.存放全局性的变量和常量
     * 2.常量必须是大小=小写
     */
    //public static final String FIRST_OPEN="first_open";
    public LocationService locationService;

    /*微信登录和微信支付*/
    public static IWXAPI api;
    private String APP_ID = "wx86d3a2a38a4da3b1";

    //这个才是程序的起点，先进行初始化的操作
    @Override
    public void onCreate() {
        super.onCreate();
        this.appAction = new AppActionImpl(this);
        this.BaseApplication = this;
        /*1）初始化日志*/
        initDebugSettings();
        //2）初始化网络设置
        initNetwork();
        //3）初始化数据库
        initDatabase();
        //4）初始化定位服务
        locationService = new LocationService(getApplicationContext());
        //5)时间设置
        initTimeService();

        //6)因为涉及到微信回调，所以需要写一个全局的
        registerToWx();
    }

    private void registerToWx() {
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
        api.registerApp(APP_ID);
    }

    private void initTimeService() {
        startService(new Intent(this, TimeService.class));
        NTPTime.getInstance().updteNTPTime();
    }

    public AppAction getAppAction() {
        return appAction;
    }

    synchronized public static BaseApplication getApplication() {
        return BaseApplication;

    }

    //    //注册广播，启动服务
        //    private void initBroadcastReceiver() {
        //        /*定义一个过滤器*/
        //        IntentFilter intentFilter = new IntentFilter();
        //        /*添加通知事件流*/
        //        intentFilter.addAction(Constants.ACTION_ENABLE_LOACTION);
        //        /*实例化广播监听器*/
        //
        //        /*绑定监听器与过滤器*/
        //        registerReceiver(broadcastReceiver,intentFilter);
        //
        //    }

    /*01.初始化网络设置*/
    private void initNetwork() {
        // 初始化手机的IP地址strOperateIP
        if( NetWorkUtil.isNetworkAvailable(this)){
            if (NetWorkUtil.isWifiAvailable(this)){
                ipAddress = NetWorkUtil.getIpAddressWithWifi(this);
            } else {
                ipAddress = NetWorkUtil.getIpAddressWithGPRS();
            }
        }
        //存入文件中进行保存
        PreferenceUtil.setSharedPreference(this,AppConstants.KEY_IP_ADDRESS,ipAddress);

    }

    /*02.初始化日志*/
    private void initDebugSettings() {
        //1.初始化butterknife的日志设置
        ButterKnife.setDebug(BuildConfig.DEBUG);
        /*使用：JLog.json(json)
        *
        * JLog.d(AppConstants.Log_TEST_TAG,"XXX");
        *
        * */
        /*添加打印到文件中的级别*/
        List<LogLevel> logLeveLs = new ArrayList<>();
        logLeveLs.add(LogLevel.DEBUG);
        logLeveLs.add(LogLevel.JSON);
        logLeveLs.add(LogLevel.ERROR);


        /*
        * 初始化log的全局配置，setDebug 参数默认值是
        * true，表示日志会输出到控制台中，在发布的版本时需要把这个变量设置为false
        * setDebug(false);
        * */
        JLog.init(this)
        .setDebug(BuildConfig.DEBUG)
        /*日志会输出到文件中，默认是false*/
        .writeToFile(true)
        /*决定了哪些级别的日志可以输出到文件中*/
        .setLogLevelsForFile(logLeveLs)
        /*配置日志保持的目录名称，默认是jlog并保存在sd卡中，建议使用应用的名称作为日志目录名*/
        .setLogDir(getString(R.string.app_name));
        /*
        * 日志级别：
        *     VERBOSE("VERBOSE"),
                DEBUG("DEBUG"),
                INFO("INFO"),
                WARN("WARN"),
               // ERROR("ERROR"),
               // WTF("WTF"),  这两个是默认的日志界别
                JSON("JSON");
        *
        * */
    }

    //03.初始化各种数据库
    private void initDatabase() {
        cityManager = new CityDBManager(BaseApplication);

    }



}
