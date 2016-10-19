package com.android.haobanyi.api.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.android.haobanyi.api.R;
import com.android.haobanyi.api.net.RetrofitServiceGenerator;
import com.android.haobanyi.api.utils.EncryptUtil;
import com.android.haobanyi.model.bean.home.searching.GeneralSortDataBean;
import com.android.haobanyi.model.util.DateUtil;
import com.google.gson.Gson;


import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fWX228941 on 2016/5/12.
 *
 * @作者: 付敏
 * @创建日期：2016/05/12
 * @邮箱：466566941@qq.com
 * @当前文件描述：用于测试接口
 */
public class TextActivity extends Activity implements View.OnClickListener {
    String head;
    String head_01;
    String head_02;
    String head_03;
    String head_002;
    Date epoch;
    String head_0002;
    String head_00002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b= (Button) findViewById(R.id.btn_retrofit_simple_contributors);
        b.setOnClickListener(this);

        /*head 和head_01 二选一，写一个判断逻辑，参数有USER ID, 请求失效就是时间戳的问题*/
         head = EncryptUtil.makeMD5("10060"+"b3c24e309405c38e800fa968c74059ba"+"GetOrderList".toLowerCase()+"hby.mobile.client");//带用户ID 的值
        head_01 = EncryptUtil.makeMD5("getProductListByNormalSort".toLowerCase() + "hby.mobile.client");
        Log.d("TextActivity", "01=="+"getProductListByNormalSort".toLowerCase() + "hby.mobile.client");

        head_002 = Long.toString(Calendar.getInstance().getTimeInMillis());
        head_02 = DateUtil.getTimeStamp02();
        //getGMTUnixTime 和getGMTUnixTimeByCalendar
        head_0002 = DateUtil.getTimeStamp01();
        head_00002 = Long.toString(DateUtil.getGMTUnixTimeByCalendar());
        head_03 = EncryptUtil.makeMD5(head_02 + "getProductListByNormalSort".toLowerCase() + "hby.mobile.client");
        Log.d("TextActivity", "03==" + head_02 + "getProductListByNormalSort".toLowerCase() + "hby.mobile.client");

    }

    @Override
    public void onClick(View v) {
        /*
        * 测试数据：测试Ip:10.0.3.15Login
        * 账号：15527221406
        * 密码：13986739680GetProductListByNormalSort
        * 验证码：205512
        * UserID:10060
        * AccessToken:"b3c24e309405c38e800fa968c74059ba",

        * ExpiresIn:7200
        * */
        /*如果没有获取到数据，就会出现错误，容错处理*/
        if (v.getId() == R.id.btn_retrofit_simple_contributors){
            Log.d("TextActivity", "01=="+"getProductListByNormalSort".toLowerCase() + "hby.mobile.client");
            Log.d("TextActivity", "03==" + head_02 + "getProductListByNormalSort".toLowerCase() + "hby.mobile.client");
            Log.d("TextActivity","日期head_02   "+head_02+"  ");
            Log.d("TextActivity","日期head_002  "+head_002+"  ");
            Log.d("TextActivity","日期head_0002  "+head_0002+"  ");
            Log.d("TextActivity","日期head_00002   "+head_00002+"  ");



/*            Call<LoginResponseResult> call1 =  RetrofitServiceGenerator.getApi().loginTest(head_01,head_02,head_03,"15527221406",
                    "13986739680", "10.0.3.15");
            call1.enqueue(new Callback<LoginResponseResult>() {
                @Override
                public void onResponse(Call<LoginResponseResult> call, Response<LoginResponseResult> response) {

                }

                @Override
                public void onFailure(Call<LoginResponseResult> call, Throwable t) {

                }
            });*/
/*            Call<GeneralSortDataBean> call =  RetrofitServiceGenerator.getApi().getProductListByNormalSort(head_01,
                    head_02,
                    head_03, 0, 20);
            call.enqueue(new Callback<GeneralSortDataBean>() {
                @Override
                public void onResponse(Call<GeneralSortDataBean> call, Response<GeneralSortDataBean> response) {
                    //Log.d("fumin01+json: ", new Gson().toJson(response.body()));//body部分是不包含code和message部分的

                }

                @Override
                public void onFailure(Call<GeneralSortDataBean> call, Throwable t) {
                    Log.d("fumin02+json: ", new Gson().toJson(call));
                }
            });*/                  //Log.d("fumin01+json: ", new Gson().toJson(response));
    }
    }

}
