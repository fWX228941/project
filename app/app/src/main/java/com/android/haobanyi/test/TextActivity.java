package com.android.haobanyi.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.zxing.CaptureActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.ActionCallbackTripleListener;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.mine.myinformation.UserBean;
import com.android.haobanyi.model.bean.shopping.ShoppingCartBean;
import com.android.haobanyi.model.bean.shopping.conformorder.ShoppingCartChildBean;
import com.android.haobanyi.model.bean.shopping.conformorder.ShoppingCartParentBean;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.bean.shopping.store._ShopRelatedServiceBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by fWX228941 on 2016/6/23.
 *
 * @作者: 付敏
 * @创建日期：2016/06/23
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class TextActivity extends BaseActivity {
    @BindView(R.id.regist)
    Button regist;
    @BindView(R.id.token)
    Button token;
    @BindView(R.id.btn_retrofit_simple_contributors)
    Button btnRetrofitSimpleContributors;
    List<_ShopRelatedServiceBean> testlist01;
    List<_ShopRelatedServiceBean> testlist02;

    @Override
    protected int setLayout() {
        return R.layout.test_activity_main;
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



    @OnClick({R.id.regist, R.id.token})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.regist:
                goRe();
                break;
            case R.id.token:
                goCe();
                break;
        }
    }

    private void goCe() {

    }

    private void goRe() {
        this.appAction.login("15527221406", "13986739680", "10.0.3.15", new ActionCallbackListener<LoginResponseResult>() {
            @Override
            public void onSuccess(LoginResponseResult data) {//其实这个data数据基本上没有什么用
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
            }
        });

    }

    @OnClick(R.id.btn_retrofit_simple_contributors)
    public void onClick() {

                /*
        * 真是神奇
        *
        *         Log.d("TextActivity", "logintoken_matcher.find():" + logintoken_matcher.find());
        Log.d("TextActivity", "nsts_matcher.find() && logintoken_matcher.find():" + (nsts_matcher.find() &&
                logintoken_matcher.find()));\        Log.d("TextActivity", "(nsts_matcher.find()) && (logintoken_matcher.find()):" + ((nsts_matcher.find()) &&
                (logintoken_matcher.find())));
        Log.d("TextActivity", "(nsts_matcher.find()) || (logintoken_matcher.find()):" + ((nsts_matcher.find()) ||
                (logintoken_matcher.find())));
        * 08-15 15:45:01.251 9882-9882/? D/TextActivity: nsts_matcher.find():true
           08-15 15:45:01.251 9882-9882/? D/TextActivity: logintoken_matcher.find():true
            08-15 15:45:01.251 9882-9882/? D/TextActivity: nsts_matcher.find() && logintoken_matcher.find():false


            08-15 15:49:44.960 10543-10543/? D/TextActivity: nsts_matcher.find():true
08-15 15:49:44.960 10543-10543/? D/TextActivity: logintoken_matcher.find():true
08-15 15:49:44.960 10543-10543/? D/TextActivity: nsts_matcher.find() && logintoken_matcher.find():false
08-15 15:49:44.960 10543-10543/? D/TextActivity: (nsts_matcher.find()) && (logintoken_matcher.find()):false
08-15 15:49:44.960 10543-10543/? D/TextActivity: (nsts_matcher.find()) || (logintoken_matcher.find()):false
        * 日了够
        * 在线：http://tool.lu/regex/
        * http://tool.oschina.net/regex/
        * */
        String nsts = null;
        String logintoken = null;
        String result="http://www.haobanyi.com/API/User/QRCodeLogin?loginToken=423523gdfghsd5436w34gfdg43534&nsTS=1471241814";
        result.indexOf("loginToken=");
        Log.d("TextActivity", "result.inde:" + result.indexOf("loginToken="));
        Log.d("TextActivity", "result.indexOf:" + result.indexOf("nsTS="));

        Log.d("TextActivity", result.substring(result.indexOf("loginToken=")+11, result.indexOf("&nsTS=")));
        Log.d("TextActivity", result.substring(result.indexOf("nsTS=")+5));

        Pattern nsts_pattern = Pattern.compile("nsTS\\=(.*?)");
        Pattern logintoken_pattern = Pattern.compile("loginToken\\=(.*?)\\&nsTS");
        Matcher nsts_matcher = nsts_pattern.matcher(result);
        Matcher logintoken_matcher  = logintoken_pattern.matcher(result);
/*        Log.d("TextActivity", "nsts_matcher.find():" + nsts_matcher.find()); 遇见各种奇葩问题 ，难道是这个语句只能执行一次
只要能够达到目的就不要纠结那么多恶劣，这些都先放在一边就是了
        Log.d("TextActivity", "nsts_matcher.find():" + nsts_matcher.find());*/
/*        if (nsts_matcher.find()) {
            Log.d("TextActivity", "进来了吗");
            String nsts_ = nsts_matcher.group(0); // 没有匹配的，就会报错       java.lang.IllegalStateException: No successful match so far
            String logintoken_ = logintoken_matcher.group(0);
            Log.d("TextActivity", "nsts" + nsts);
            Log.d("TextActivity", "nsts_" + nsts_);
            Log.d("TextActivity", "logintoken_" + logintoken_);
            Log.d("TextActivity", "logintoken" + logintoken);

        }*/
       // this.appAction.getVoucherList(1,10);
//        this.appAction.getContactList();
/*        this.appAction.searchShopByKeyword("瑞丰德永", 1, 10, new ActionCallbackTripleListener<List<ShopBean>, String, String>() {


            @Override
            public void onSuccess(List<ShopBean> data01, String data02, String data03) {

            }

            @Override
            public void onFailure(String errorEvent, String message) {

            }
        });*/
//        this.appAction.getAssessmentList(15,0,2,10);
        /*this.appAction.getUserInfo();*/
/*        this.appAction.getVerifyShoppingCart(new ActionCallbackDoubleListener<List<ShoppingCartParentBean>, List<ShoppingCartChildBean>>() {


            @Override
            public void onSuccess(List<ShoppingCartParentBean> parentlist, List<ShoppingCartChildBean> childlist) {

            }

            @Override
            public void onFailure(String errorEvent, String message) {

            }
        });*/
    }
}
