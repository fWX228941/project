/*
package com.android.haobanyi.core.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.haobanyi.api.Api;
import com.android.haobanyi.api.ApiImpl;
import com.android.haobanyi.model.bean.home.location.Location;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;


*/
/**
 * Created by fWX228941 on 2016/4/26.
 *
 * @作者: 付敏
 * @创建日期：2016/04/26
 * @邮箱：466566941@qq.com
 * @当前文件描述：程序启动时，就需要开启一个单独的线程，连接百度服务器，来获取城市等相关信息
 *//*

public class LocationService extends IntentService{
    private static final String TAG = "LocationService";
    */
/*这个其实强耦合了，需要解耦，事件总线机制*//*

    private Location location;
    */
/**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     *//*

    public LocationService(String name) {
        super(name);
    }
    */
/*必须要添加一个Declare a default no-argument constructor for IntentService 无参构造函数，并且还得
    * 比附：
    * public ReminderService() {
         super("ReminderService");
      }
    * *//*

    public LocationService(){
        super(TAG);

    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Api api= new ApiImpl();
        */
/*处理*//*

        if (Constants.ACTION_ENABLE_LOACTION == intent.getAction()){
            Log.d("fumin","loacation");
            //1.发起连接
            try {
                Log.d("fumin","location.getAddress():"+api.locateByApp(getApplicationContext()));
                location=api.locateByApp(getApplicationContext());
                Log.d("fumin","location.getAddress():"+location.getAddress());
                //2.存放到本地缓存中
                PreferenceUtil.setSharedPreference(this,Constants.address,location.getAddress(),"location");
                PreferenceUtil.setSharedPreference(this,Constants.city,location.getCity(),"location");
                PreferenceUtil.setSharedPreference(this,Constants.country,location.getCountry(),"location");
                PreferenceUtil.setSharedPreference(this,Constants.district,location.getDistrict(),"location");
                PreferenceUtil.setSharedPreference(this,Constants.province,location.getProvince(),"location");
            } catch (Exception e){
                Log.d("fumin","定位出错");
            }


        }

    }
}
*/
