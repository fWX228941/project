/**
 * Copyright (C) 2015. Keegan小钢（http://keeganlee.me）
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.haobanyi.activity.guid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.activity.guid.register.RegisterActivity01;
import com.android.haobanyi.activity.guid.register.ThirdLoginActivity;
import com.android.haobanyi.activity.weiboapi.AccessTokenKeeper;
import com.android.haobanyi.activity.weiboapi.WeiboConstants;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.mine.myinformation.UserBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.NetWorkUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ClearEditText;
import com.android.haobanyi.view.TitleBar;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

import static com.android.haobanyi.R.id.tv_third_login_explain;
import static com.baidu.location.b.g.T;
//import com.sina.weibo.sdk.auth.AuthInfo;


/**
 * 登录
 *
 * @version 1.0 创建时间：16/3/21
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "aaaaaaaaaaaaa";//还有一个坑就是TAG 必须放在开头第一行
    @BindView(R.id.title_bar)
    TitleBar titleBar; //标题栏
    @BindView(R.id.edit_phone)
    ClearEditText edit_phone; //账号输入框
    @BindView(R.id.id_account_record)
    ImageView idAccountRecord; //下拉历史记录
    @BindView(R.id.edit_password)
    ClearEditText edit_password; //密码输入框
    @BindView(R.id.id_pwd)
    ImageView idPwd; //隐藏密码开关
    @BindView(R.id.btn_login)
    Button btn_login; //登录按钮
    @BindView(R.id.id_hint)
    TextView idHint; // 验证提示框
    @BindView(tv_third_login_explain)
    TextView tvThirdLoginExplain; // 第三方登录按钮
    @BindView(R.id.tv_forget_password)
    TextView textview_forget_password; //忘记密码按钮
    @BindView(R.id.login_forget_layout)
    LinearLayout loginForgetLayout;
    @BindView(R.id.id_lv_search_results)
    ListView idLvSearchResults;//下拉历史账号记录列表
    ArrayAdapter resultAdapter;//下拉列表的适配器
    String[] SearchResultDataList;//下拉列表的数据源
    boolean isSelected = true;
    boolean isChecked = true;
    //手机的IP地址
    private String ipAddress;
    private String loginName = "";//手机号
    private String password = "";//登录密码
    private int STATE_REGIST;
    PreferenceUtil complexPreferences;
    private boolean is_check_for_result=false;
    private DialogPlus dialog ; //弹框
    //腾讯QQ 登录
    private Tencent mTencent;
    private long requestCount = 5;
    private static final int LOGIN_TO_SPV = 3;

    // 微博登录
    SsoHandler mSsoHandler;
    AuthInfo mAuthInfo;
    AuthListener mLoginListener;


    @Override
    protected int setLayout() {
        return R.layout.activity_login_01;

    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        // 01.初始化手机的IP地址strOperateIP
        if (NetWorkUtil.isNetworkAvailable(this)) {
            if (NetWorkUtil.isWifiAvailable(this)) {
                ipAddress = NetWorkUtil.getIpAddressWithWifi(this);
            } else {
                ipAddress = NetWorkUtil.getIpAddressWithGPRS();

            }
        }

        //02.刚刚从注册中获取注册手机号和密码，获取标识位：标识注册  注册成功：03
        STATE_REGIST = this.getIntent().getIntExtra(Constants.STATE_FROM_RGT_SUC,00);
        Log.d(TAG, "STATE_REGIST:" + STATE_REGIST);
        //兼容性的问题还是使用文件存储
        complexPreferences  = PreferenceUtil.getSharedPreference(context, "preferences");
        is_check_for_result = complexPreferences.getBoolean(Constants.CHECK_FOR_RESULT, false);

    }

    protected void initViews() {
        /*01.设置标题栏*/
        initTittleBar();
        /*02.自动获取刚刚注册的登录名和密码*/
        initPWD();

    }

    private void initPWD() {
        if (STATE_REGIST == 03){
            loginName = this.getIntent().getStringExtra(Constants.PHONE_NUMBER);
            password = this.getIntent().getStringExtra(Constants.PHONE_PASSWORD);
            edit_phone.setText(loginName);
            edit_password.setText(password);
        }
    }

    /*设置标题栏*/
    private void initTittleBar() {
        // 设置背景色  透明灰色  颜色 什么的最后定夺
        titleBar.setBackgroundResource(R.color.white);
//        if (!is_check_for_result){
            titleBar.setLeftImageResource(R.drawable.back_icon);
            // 设置监听器
            titleBar.setLeftClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 如果是因为token失效而登录的
                    if (is_check_for_result){
                        complexPreferences.putBoolean(Constants.ISLOGIN, false);
                        complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT,false);
                        finshCurrentActivity();
                    }else {
                        finshCurrentActivity();
                    }

                }
            });

       //}
        // 设置返回箭头和文字以及颜色
        titleBar.setActionTextColor(Color.BLACK);
        titleBar.addAction(new TitleBar.TextAction("免费注册") {
            @Override
            public void performAction(View view) {
                toRegister();
            }
        });

        titleBar.setTitleBackground(R.drawable.top_navagation_logo);
        titleBar.setDividerColor(Color.GRAY);
    }

    private void finshCurrentActivity() {
        finish();
        LoginActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
    }

    @Override
    protected void loadData() {
        //01.获取下拉账号记录列表的数据
        getAccountListData();

    }
    /*
    * 目前只保存最近的一条记录
    * */
    private void getAccountListData() {

        complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        String data = complexPreferences.getString(Constants.REGISTTEDPN,loginName);
        Log.d(TAG, data);

        if (TextUtils.isEmpty(data)){
            //如果什么数据都没有则隐藏
            idAccountRecord.setVisibility(View.GONE);
        } else {
            //数据更新
            SearchResultDataList = new String[]{data};
             if (null == resultAdapter){
            /*ArrayAdapter没有数据源限制，都是会被转化为list*/
                resultAdapter = new ArrayAdapter<>(this,R.layout.simple_list_item_1,SearchResultDataList);
            }
        }


    }

    @Override
    protected void registerEventListener() {
        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /*EditText禁止输入空格
            * 参考：http://blog.csdn.net/d920247632/article/details/49678665
            *http://www.jb51.net/article/64721.htm
            * 如果是动态长度控制：就使用：InputFilter
            * */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    edit_password.setText(str1);

                    edit_password.setSelection(start);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void registerBroadCastReceiver() {

    }

    @Override
    protected void saveState(Bundle outState) {

    }


    // 登录之后增加一个弹框提示，多了一个合法性验证的提示，所以还需要添加一个文本显示框
    public void toLogin() {
        loginName = edit_phone.getText().toString();
        password = edit_password.getText().toString();
        //打桩测试
        if (loginName.equals("ceshi")&&password.equals("ceshi")){
            tvThirdLoginExplain.setVisibility(View.VISIBLE);
            return;
        }


        btn_login.setEnabled(false);
        this.appAction.login(loginName, password, ipAddress, new ActionCallbackListener<LoginResponseResult>() {
            @Override
            public void onSuccess(LoginResponseResult data) {//其实这个data数据基本上没有什么用
                ToastUtil.showSuccessfulMessage("登录成功");
                setHintMessage(" ");
                saveLoginName(loginName);
                Log.d(TAG, "is_check_for_result:" + is_check_for_result);

                if (is_check_for_result) {
                    complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, false);
                    LoginActivity.this.finish();// 要注意规范，必须是当前类调用finish()方法，今天遇到的坑有点多，三个了！
                    //添加一个刷新
                } else {
                    IntentUtil.gotoTopActivityWithoutData(LoginActivity.this, RadioGroupActivity.class, false);
                }

            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount>0) {
                    requestCount--;
                    toLogin();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                }else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                    setHintMessage(errorMessage);
                }
                btn_login.setEnabled(true);
            }
        });


    }

    private void setHintMessage(String errorMessage) {
        idHint.setVisibility(View.VISIBLE);
        idHint.setText(errorMessage);
        btn_login.setEnabled(true);
    }

    //保存注册成功的注册名
    private void saveLoginName(String loginName ) {
         complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
         complexPreferences.putString(Constants.REGISTTEDPN,loginName);
         //也就是说注册的账号，只能是手机号或者邮箱
         if (loginName.contains("@")){
             //说明是邮箱登录
             complexPreferences.putBoolean(Constants.REGISTTE_BY_PHONE, false);
         } else {
             complexPreferences.putBoolean(Constants.REGISTTE_BY_PHONE,true);
         }



    }

    private void getUserInfo() {
        this.appAction.getUserInfo(new ActionCallbackListener<UserBean.DataBean>() {
            @Override
            public void onSuccess(UserBean.DataBean data) {


            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount>0) {
                    requestCount--;
                    getUserInfo();
                } else if ("003".equals(errorCode)) {
                    Toast.makeText(context, "易，请检查网络", Toast.LENGTH_SHORT).show();
                }  else if ("996".equals(errorCode)) {
                    loginForToken();
                } else {
                    Toast.makeText(context, "异常代码：" + errorCode + " 异常说明: " + errorMessage, Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "加载失败，请重新点击加载", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void loginForToken() {
        complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(LoginActivity.this, LoginActivity.class, LOGIN_TO_SPV);//跳转到登录界面
    }


    @OnClick({R.id.id_account_record, R.id.id_pwd, R.id.btn_login, tv_third_login_explain, R.id
            .tv_forget_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_account_record:
                selectAccountRecord();
                break;
            case R.id.id_pwd:
                changePwdState();
                break;
            case R.id.btn_login:
                requestCount = 10;
                toLogin();
                break;
            case tv_third_login_explain:
                thirdlogin();
                break;
            case R.id.tv_forget_password:
                toResetPassword();
                break;
        }
    }

    //第三方登录item_dialog_third_login
    private void thirdlogin() {
        //弹出一个对话框出来，
        dialog = DialogPlus.newDialog(LoginActivity.this)
                .setContentHolder(new ViewHolder(R.layout.item_dialog_third_login))
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)   // 如果设置为不可取消，真的可以省去很多麻烦呢！
                .setExpanded(false)  //设置为不可滑动
                .setOnBackPressListener(new OnBackPressListener() {
                    @Override
                    public void onBackPressed(DialogPlus dialogPlus) {
                        Log.d(TAG, " back button is pressed");
                        dialog.dismiss();

                    }
                })
                .setOnClickListener(dialogClickListener)
                .create();
        dialog.show();

    }

    OnClickListener dialogClickListener = new OnClickListener() {
        @Override
        public void onClick(DialogPlus dialog, View view) {
            switch (view.getId()) {
                case R.id.qq:
                    Log.d(TAG, "  点击qq腾讯");
                    ToastUtil.showHintMessage("QQ登录");
                    qqlogin();
                    break;
                case R.id.weibo:
                    Log.d(TAG, "  点击微博");
                    ToastUtil.showHintMessage("微博登录");
                    weibologin();
                    break;
                case R.id.weixin:
                    Log.d(TAG, "  点击微信");
                    ToastUtil.showHintMessage("微信登录");
                    weixinlogin();
                    break;
                case R.id.message:
                    Log.d(TAG, "  点击短信");
                    ToastUtil.showHintMessage("短信登录");
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.IS_REGISTE_BY_MESSAAGE, true);//代表邮箱
                    IntentUtil.gotoActivityWithData(LoginActivity.this,ThirdLoginActivity.class,bundle,false);
                    break;
                case R.id.cancel:
                    dialog.dismiss();
                    break;
            }
        }
    };

    private void weibologin() {
        //1.初始化微博
         mAuthInfo = new AuthInfo(this, WeiboConstants.APP_KEY, WeiboConstants.REDIRECT_URL, WeiboConstants.SCOPE);
        //2.登陆认证对应的listener，认证授权，当前采用 all In one的方式授权，此种授权方式会根据手机是否安装微博客户端来决定使用sso授权还是网页授权，如果安装有微博客户端 则调用微博客户端授权
         mSsoHandler = new SsoHandler(this, mAuthInfo);
         mLoginListener = new AuthListener();
         mSsoHandler . authorize(mLoginListener);

    }

    /**
     * 微博登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            //01.从bundle中解析token
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            //02.保存Token到SharedPreferences
            if (accessToken != null && accessToken.isSessionValid()) {
                ToastUtil.showSuccessfulMessage("授权成功");
                AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Token", accessToken.getToken());
                params.put("RefreshToken", accessToken.getRefreshToken());
                params.put("LoginType", "3");
                //QQ传递 OpenId 和 Token
                requestCount = 45;
                authLogin(params);

            }else {
                String code = values.getString("code", "");
                if (!TextUtils.isEmpty(code)) {
                    ToastUtil.showHintMessage("应用签名不正确"+",code:"+code);
                }

            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ToastUtil.showErrorMessage(e.getMessage());
        }

        @Override
        public void onCancel() {
            ToastUtil.showHintMessage("取消授权");

        }
    }

    private void qqlogin() {
        mTencent = Tencent.createInstance("1105580503", this.getApplicationContext());
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", tencentLoginListener);
        }
    }
    //104.认证失败，第三方登陆回掉
    private void authLogin(final HashMap<String, String> params) {
//        Toast.makeText(LoginActivity.this, "03", Toast.LENGTH_SHORT).show();//先不删除，用于测试
        this.appAction.authLogin(params, new ActionCallbackListener<UserBean
                .DataBean>() {
            @Override
            public void onSuccess(UserBean.DataBean data) {
                //如果是绑定的就直接跳转到登录界面，如果不是绑定的就，【新增加一个界面，不然复杂度太高了】
                Log.d("WXEntryActivity", "data.isBind():" + data.isBind());
//                Toast.makeText(LoginActivity.this, "04", Toast.LENGTH_SHORT).show();
//                Toast.makeText(LoginActivity.this, "data.isBind():" + data.isBind(), Toast.LENGTH_SHORT).show();
                if (data.isBind()) {
                    requestCount = 45;
                    getUserInfo();
                } else {
//                    Toast.makeText(LoginActivity.this, "05", Toast.LENGTH_SHORT).show();
                    String Rdm = data.getRdm();
                    if (!TextUtils.isEmpty(Rdm)) {
                        //跳转到第三方登陆得界面
                        Bundle bundle = new Bundle();
                        bundle.putString("Rdm", Rdm);
                        bundle.putString("LoginType", "2");
//                        Toast.makeText(LoginActivity.this, "06", Toast.LENGTH_SHORT).show();
                        IntentUtil.gotoActivityWithData(LoginActivity.this, ThirdLoginActivity.class, bundle,
                                true);
                    }
                }


            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    authLogin(params);
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();

                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);

                }

            }
        });
    }


    /*微信登录，检查可行性*/
    private void weixinlogin() {
        if (!BaseApplication.api.isWXAppInstalled()) {
            ToastUtil.showHintMessage("您还未安装微信客户端");
            return;
        }
        /*发情登录，当跳转到 微信验证登录的界面，微信那边已经写好了的界面，我们只需要在自己的app这边发送请求就会自动跳转到微信的页面*/
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";//请求中scope是固定的形式，用来请求微信验证登录
        req.state = "diandi_wx_login";
        BaseApplication.api.sendReq(req);
    }

    @OnItemClick(R.id.id_lv_search_results)
    public void onPick(AdapterView<?> parent, View view, int position, long id){//携带的四个参数任何组合，item 的id / item view 在 adapter 中的位置
        //01.从列表中获取关联的适配器，再从适配器中获取item，最后是获取其中的数据，
         String text = idLvSearchResults.getAdapter().getItem(position).toString();//因为是实时性，所以一直在变
         if (TextUtils.isEmpty(text)){
             //02.选择哪一条记录就覆盖保存到账号输入框中
             edit_phone.setText(text);
             //03.需要设置光标
             edit_phone.setSelection(text.length());
             //04.隐藏视图，同时图标进行改变
             idLvSearchResults.setVisibility(View.GONE);
             isSelected = !isSelected;
             idAccountRecord.setImageResource(R.drawable.up_arrow);
         }




    }
    //注册
    private void toRegister() {
        IntentUtil.gotoActivityWithoutData(LoginActivity.this, RegisterActivity01.class, false);
        //finish();  finish 需要谨慎调用，它是会把当前页面从堆栈中删除的，否则就会出现直接回退到launcher界面,坑死宝宝了
    }

    //重设密码
    private void toResetPassword() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.STATE_FROM_FPW, 01);
        IntentUtil.gotoActivityWithData(LoginActivity.this, RegisterActivity01.class, bundle, false);
    }

    //选择保存成功的账号登录
    private void selectAccountRecord() {
        // 1.数据源：用户输入成功后保存的记录
        // 2.当我点击的时候就会弹出一个下拉列表，并让用户选择后，自动覆盖保存
        if (isSelected){
            idLvSearchResults.setVisibility(View.VISIBLE);//login_ud_arrow
            idAccountRecord.setImageResource(R.drawable.login_ud_arrow);
        } else {
            idLvSearchResults.setVisibility(View.GONE);
            idAccountRecord.setImageResource(R.drawable.up_arrow);
        }
        isSelected = !isSelected;
        // 3.第一次关联适配器，以后还是同样的适配器，只需要更新数据源即可,同一个视图可以关联不同的适配器，不同的适配器打包不同的数据源和item视图
        if (null == idLvSearchResults.getAdapter()) {
            idLvSearchResults.setAdapter(resultAdapter);
        } else {
            resultAdapter.notifyDataSetChanged();
        }

    }
    //隐藏或者显示密码【两种点击状态】
    private void changePwdState() {

        Log.d(TAG, "isChecked:" + isChecked);
        if (isChecked) {
            //显示密码
            Log.d(TAG, "显示密码");
            idPwd.setImageResource(R.drawable.o_eyes);
            edit_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //03.需要设置光标
            password = edit_password.getText().toString();
            edit_password.setSelection(password.length());
        } else {
            //隐藏密码
            Log.d(TAG, "隐藏密码");
            idPwd.setImageResource(R.drawable.c_eyes);
            edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //03.需要设置光标
            password = edit_password.getText().toString();
            edit_password.setSelection(password.length());
        }
        isChecked = !isChecked;
    }
    //进一步处理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "is_check_for_result:" );
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "is_check_for_result:" + is_check_for_result);
            if (is_check_for_result) {
                complexPreferences.putBoolean(Constants.ISLOGIN, false);
                complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, false);
                finshCurrentActivity();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 官方文档没没没没没没没没没没没这句代码, 但是很很很很很很重要, 不然不会回调!


        if(requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN ||
                requestCode == com.tencent.connect.common.Constants.REQUEST_APPBAR) {
                Tencent.onActivityResultData(requestCode, resultCode, data, tencentLoginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
        //微博的授权方式需要添加如下代码
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }


    }

    IUiListener tencentLoginListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            ToastUtil.showSuccessfulMessage("授权成功");
            JSONObject object = (JSONObject) o;
            try {
                String openid = object.getString("openid");
                String access_token = object.getString("access_token");
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("OpenId", openid);
                params.put("Token", access_token);
                params.put("LoginType", "2");

/*                Toast.makeText(LoginActivity.this, openid, Toast.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this, access_token, Toast.LENGTH_SHORT).show();*/
                //QQ传递 OpenId 和 Token
                requestCount = 45;
                authLogin(params);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(UiError uiError) {
            Log.i(TAG, "#onError " + uiError.errorMessage);
            ToastUtil.showErrorMessage("登录失败");

        }

        @Override
        public void onCancel() {
            Log.i(TAG, "#onCancel 取消");
            ToastUtil.showErrorMessage("取消授权");
        }
    };
}
