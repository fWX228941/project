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
package com.android.haobanyi.core;

import android.content.Context;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import android.util.Log;
import android.widget.Toast;

import com.android.haobanyi.api.Api;
import com.android.haobanyi.api.ApiImpl;
import com.android.haobanyi.api.net.RetrofitServiceGenerator;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.charge.ChargeBean;
import com.android.haobanyi.model.bean.charge.ChargeListResponse;
import com.android.haobanyi.model.bean.city.AreaList;
import com.android.haobanyi.model.bean.contact.ContactListResponse;
import com.android.haobanyi.model.bean.contact.ContactBean;
import com.android.haobanyi.model.bean.foot.FootBean;
import com.android.haobanyi.model.bean.foot.FootListResponse;
import com.android.haobanyi.model.bean.mine.MyMessageBean;
import com.android.haobanyi.model.bean.mine.myinformation.UserBean;
import com.android.haobanyi.model.bean.order.OrderBean;
import com.android.haobanyi.model.bean.order.OrderChildBean;
import com.android.haobanyi.model.bean.order.OrderListResponse;
import com.android.haobanyi.model.bean.order.OrderParentBean;
import com.android.haobanyi.model.bean.order.OrderResponse;
import com.android.haobanyi.model.bean.order.OrderToPayResponse;
import com.android.haobanyi.model.bean.post.AttrIds;
import com.android.haobanyi.model.bean.post.Invoinces;

import com.android.haobanyi.model.bean.post.Orders;
import com.android.haobanyi.model.bean.redenvelop.RedEnvelopBean;
import com.android.haobanyi.model.bean.redenvelop.RedEnvelopListResponse;
import com.android.haobanyi.model.bean.shopping.conformorder.RedEnvelopeBean;
import com.android.haobanyi.model.bean.shopping.order.Carts;
import com.android.haobanyi.model.bean.shopping.product.EvaBean;
import com.android.haobanyi.model.bean.shopping.store.ShopAcBean;
import com.android.haobanyi.model.bean.shopping.store.ShopActivityBean;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.bean.userpoint.UserPointBean;
import com.android.haobanyi.model.bean.userpoint.UserPointListResponse;
import com.android.haobanyi.model.bean.voucher.VoucherBean;
import com.android.haobanyi.model.bean.voucher.VoucherListResponse;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.home.searching.GeneralSortDataBean;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.shopping.ShoppingCartBean;
import com.android.haobanyi.model.bean.shopping.conformorder.ShoppingCartChildBean;
import com.android.haobanyi.model.bean.shopping.conformorder.ShoppingCartParentBean;
import com.android.haobanyi.model.bean.shopping.store.ShopResponseBean;
import com.android.haobanyi.model.bean.shopping.store.ShopRelatedServiceBean;
import com.android.haobanyi.model.bean.shopping.store._ShopRelatedServiceBean;
import com.android.haobanyi.model.bean.shopping.product.DetailsBean;
import com.android.haobanyi.model.bean.shopping.product.EvaluationBean;
import com.android.haobanyi.model.bean.shopping.product.ProductDetailsBean;
import com.android.haobanyi.model.bean.shopping.product.SatisfySendBean;
import com.android.haobanyi.model.bean.shopping.product.ShopAttrBean;
import com.android.haobanyi.model.bean.shopping.product.VouchersTemplateBean;
import com.android.haobanyi.model.file.FileManager;
import com.android.haobanyi.model.test.testData01;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.EncryptUtil;
import com.android.haobanyi.model.util.HeaderUtil;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.model.util.RegexUtil;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.id.list;
import static android.media.CamcorderProfile.get;

/**
 * AppAction接口的实现类
 *
 * @author fumin
 * @date 16/03/24
 * @version 1.0
 */
public class AppActionImpl implements AppAction, Parcelable {
    private static final String TAG = "fumin02";
    private boolean isLogin = false;    //是否登录成功
    private Context context;
    private Api api;
    private FileManager fileManager;
    public static final  int STATUS_INVALID=0;
    public static final  int STATUS_VALID=1;
    private  boolean isFristTime = true;
    public AppActionImpl(Context context) {
        this.context = context;
        this.api = new ApiImpl();
    }

    @Override
    public void login(String loginName, final String password, String ip, final ActionCallbackListener<LoginResponseResult>
            listener) {
        if (TextUtils.isEmpty(loginName)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, ErrorEvent.MESSAGE_NULL_SITU1);  //先调用这个方法中的内容，这个方法叫回调
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, ErrorEvent.MESSAGE_NULL_SITU2);
            }
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(loginName);
        if (!matcher.matches() && !RegexUtil.validateEmail(loginName)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU1);
            }
            return;// 需要做一层拦截
        }
        if ( !RegexUtil.validatePassword(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU3);
            }
            return;// 需要做一层拦截
        }
        String head01=HeaderUtil.getHeadindexNotUser1(Constants.LOGIN);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.LOGIN);


        RetrofitServiceGenerator.getApi().login(head01
                , head02
                , head03
                , loginName, password, ip).enqueue(new Callback<LoginResponseResult>() {
            PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");

            @Override
            public void onResponse(Call<LoginResponseResult> call, Response<LoginResponseResult> response) {

                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        LoginResponseResult.DataBean bean = response.body().getData();
                        Log.d(TAG, "登录");
                        Log.d(TAG, bean.getAccessToken());
                        Log.d(TAG, "bean.getUserID():" + bean.getUserID());
                        Log.d(TAG, "bean.getExpiresIn():" + bean.getExpiresIn());
                        complexPreferences.putString(Constants.ACCESS_TOKEN, bean.getAccessToken());
                        complexPreferences.putLong(Constants.USERID, bean.getUserID());
                        complexPreferences.putLong(Constants.EXPIRES_IN, bean.getExpiresIn());//System
                        complexPreferences.putString(Constants.USER_CURRENT_PWD, password);


                        // .currentTimeMillis();
                        complexPreferences.putLong(Constants.START_TIME, System.currentTimeMillis() / 1000L);
                        isLogin = true;
                        complexPreferences.putBoolean(Constants.ISLOGIN, isLogin);
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        isLogin = false;
                        complexPreferences.putBoolean(Constants.ISLOGIN, isLogin);
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    /*即使这种场景，也是不能获取到response的，503 Service Unavailable  服务不可用，说明是服务器的原因,*/
                    isLogin = false;
                    complexPreferences.putBoolean(Constants.ISLOGIN, isLogin);
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<LoginResponseResult> call, Throwable t) {
                isLogin = false;
                complexPreferences.putBoolean(Constants.ISLOGIN, isLogin);
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    @Override
    public void loginByPhone(String loginName, String code, String ip, final ActionCallbackListener<LoginResponseResult>
            listener) {
        if (TextUtils.isEmpty(loginName)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, ErrorEvent.MESSAGE_NULL_SITU1);  //先调用这个方法中的内容，这个方法叫回调
            }
            return;
        }
        if (TextUtils.isEmpty(code)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "验证码不能为空");
            }
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(loginName);
        if (!matcher.matches() && !RegexUtil.validateEmail(loginName)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_ILLEGAL,"手机号格式不正确");
            }
            return;// 需要做一层拦截
        }

        String head01=HeaderUtil.getHeadindexNotUser1(Constants.MOBILELOGIN);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.MOBILELOGIN);


        RetrofitServiceGenerator.getApi().loginByPhone(head01
                , head02
                , head03
                , loginName, code, ip).enqueue(new Callback<LoginResponseResult>() {
            PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");

            @Override
            public void onResponse(Call<LoginResponseResult> call, Response<LoginResponseResult> response) {

                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        LoginResponseResult.DataBean bean = response.body().getData();
                        Log.d(TAG, "登录");
                        Log.d(TAG, bean.getAccessToken());
                        Log.d(TAG, "bean.getUserID():" + bean.getUserID());
                        Log.d(TAG, "bean.getExpiresIn():" + bean.getExpiresIn());
                        complexPreferences.putString(Constants.ACCESS_TOKEN, bean.getAccessToken());
                        complexPreferences.putLong(Constants.USERID, bean.getUserID());
                        complexPreferences.putLong(Constants.EXPIRES_IN, bean.getExpiresIn());//System
                        // 这个是没有什么密码的


                        // .currentTimeMillis();
                        complexPreferences.putLong(Constants.START_TIME, System.currentTimeMillis() / 1000L);
                        isLogin = true;
                        complexPreferences.putBoolean(Constants.ISLOGIN, isLogin);


                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        isLogin = false;
                        complexPreferences.putBoolean(Constants.ISLOGIN, isLogin);
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    /*即使这种场景，也是不能获取到response的，503 Service Unavailable  服务不可用，说明是服务器的原因,*/
                    isLogin = false;
                    complexPreferences.putBoolean(Constants.ISLOGIN, isLogin);
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<LoginResponseResult> call, Throwable t) {
                isLogin = false;
                complexPreferences.putBoolean(Constants.ISLOGIN, isLogin);
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }


    private String classifyErrorEvent(int code, String message) {
        switch (code){
            case ErrorEvent.CODE_INVALID_REQUEST_INT:
                return ErrorEvent.MESSAGE_INVALID_REQUEST;

            case ErrorEvent.CODE_EXPIRED_REQUEST_INT:  // 如果是请求失效，这就是时间戳的问题了，这种情况要单独处理
                return ErrorEvent.MESSAGE_EXPIRED_REQUEST;

            case ErrorEvent.CODE_DEVICE_INFORMATION_INT:
                return ErrorEvent.MESSAGE_EXPIRED_REQUEST;

            case ErrorEvent.CODE_TOKEN_EXPIRED_INT:
                return ErrorEvent.MESSAGE_TOKEN_EXPIRED;

            case ErrorEvent.CODE_SUCCESS_REQUEST_INT:
                return ErrorEvent.MESSAGE_SUCCESS_REQUEST;
            case ErrorEvent.CODE_SERVER_NO_DATA_INT:
                return ErrorEvent.MESSAGE_SERVER_NO_DATA;
            default:
                Log.d(TAG, "其他非主流异常情况："+message);
                return message;
        }
    }


    @Override
    public void sendSmsCode(final String phoneNum, final ActionCallbackListener<ResponseCode> listener) {
        /**
         * ，当用户浏览某个商品，点击购买时，App首先会判断用户是否已经登录，如未登录，
         * 则会跳转到登录页面让用户先登录。如果已经登录，但token已经过期，那需要先去获取新的token，之后才能进行下一步的购物操作。这些逻辑处理，也是业务层的工
         */
        if (TextUtils.isEmpty(phoneNum)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, ErrorEvent.MESSAGE_NULL_SITU3);
            }
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (!matcher.matches()&&!RegexUtil.validateEmail(phoneNum)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU1);
            }
            return;
        }

        String head01=HeaderUtil.getHeadindexNotUser1(Constants.SEND_SMS_CODE);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SEND_SMS_CODE);

        Log.d("RegisterActivity02", "字段为："+phoneNum);
        RetrofitServiceGenerator.getApi().sendSmsCode4Register(head01
                , head02
                , head03
                , phoneNum).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));


                    }
                } else {
                    Log.i("fumin", "response02");
                    Log.d(TAG, "服务器的问题");

                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }
            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);

            }
        });

    }



    @Override
    public void register(String phoneNum, String code, String password, String ip, final ActionCallbackListener<ResponseCode> listener) {
        if (TextUtils.isEmpty(phoneNum)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, ErrorEvent.MESSAGE_NULL_SITU3);
            }
            return;
        }
        /*其实这里还可以处理一层就是验证码的对比判断，当然了取代用户手写其实更好*/
        if (TextUtils.isEmpty(code)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, ErrorEvent.MESSAGE_NULL_SITU4);
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, ErrorEvent.MESSAGE_NULL_SITU2);
            }
            return;
        }

        if ( !RegexUtil.validatePassword(password)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU3);
            }
            return;// 需要做一层拦截
        }


        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (!matcher.matches()&&!RegexUtil.validateEmail(phoneNum)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU1);
            }
            return;
        }

        String head01=HeaderUtil.getHeadindexNotUser1(Constants.REGISTER);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.REGISTER);


        /*很多部分都是重复的，其实是可以重构的*/
        RetrofitServiceGenerator.getApi().registerByPhone(head01
                , head02
                , head03
                , phoneNum, code, password, ip).enqueue(new Callback
                <ResponseCode>() {


            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }


    @Override
    public void findPwd(String phoneNum, String resetPassword, String code, final
    ActionCallbackListener<ResponseCode> listener) {

        if (TextUtils.isEmpty(phoneNum)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, ErrorEvent.MESSAGE_NULL_SITU3);
            }
            return;
        }
        /*其实这里还可以处理一层就是验证码的对比判断，当然了取代用户手写其实更好*/
        if (TextUtils.isEmpty(code)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, ErrorEvent.MESSAGE_NULL_SITU4);
            }
            return;
        }
        if (TextUtils.isEmpty(resetPassword)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, ErrorEvent.MESSAGE_NULL_SITU2);
            }
            return;
        }

        if ( !RegexUtil.validatePassword(resetPassword)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU3);
            }
            return;// 需要做一层拦截
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (!matcher.matches()&&!RegexUtil.validateEmail(phoneNum)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU1);
            }
            return;
        }

        String head01=HeaderUtil.getHeadindexNotUser1(Constants.FIND_PWD);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.FIND_PWD);

        RetrofitServiceGenerator.getApi().findPwd(head01
                , head02
                , head03
                , phoneNum, resetPassword, code).enqueue(new Callback
                <ResponseCode>() {


            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }


            }
            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void modifypwd(String OldPassword, String NewPassword, final ActionCallbackListener<ResponseCode> listener) {

        if (TextUtils.isEmpty(NewPassword)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, ErrorEvent.MESSAGE_NULL_SITU4);
            }
            return;
        }
        //只要对新密码约束就好了

        if ( !RegexUtil.validatePassword(NewPassword)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU3);
            }
            return;// 需要做一层拦截
        }



        NTPTime.getInstance().updteNTPTime();
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        long userId =complexPreferences.getLong(Constants.USERID, 0l);
        String token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexUser1(userId + token + Constants.MODIFYPWD);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02 = HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.MODIFYPWD);

        RetrofitServiceGenerator.getApi().modifypwd(head01
                , head02
                , head03
                , userId, token, OldPassword, NewPassword).enqueue(new Callback
                <ResponseCode>() {


            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }
            }
            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void addPlatformConsult(String content, String phone, final ActionCallbackListener<ResponseCode> listener) {
        if (TextUtils.isEmpty(content)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "反馈内容不能为空");
            }
            return;
        }

        String ConsultContext = phone+" "+content;


        NTPTime.getInstance().updteNTPTime();
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        long userId =complexPreferences.getLong(Constants.USERID, 0l);
        String token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexUser1(userId + token + Constants.Add_Platform_Consult);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02 = HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.Add_Platform_Consult);

        RetrofitServiceGenerator.getApi().addPlatformConsult(head01
                , head02
                , head03
                , userId, token, ConsultContext).enqueue(new Callback
                <ResponseCode>() {


            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }


            }
            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    @Override
    public void receiveVouchers(String voucherscode, final ActionCallbackListener<ResponseCode> listener) {
        if (TextUtils.isEmpty(voucherscode)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "代金券卡密号码不能为空");
            }
            return;
        }



        NTPTime.getInstance().updteNTPTime();
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        long userId =complexPreferences.getLong(Constants.USERID, 0l);
        String token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexUser1(userId + token + Constants.RECEIVE_VOUCHERS);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02 = HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.RECEIVE_VOUCHERS);

        RetrofitServiceGenerator.getApi().receiveVouchers(head01
                , head02
                , head03
                , userId, token, voucherscode).enqueue(new Callback
                <ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }


            }
            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    @Override
    public void ReceiveVouchers(long tempId, final ActionCallbackListener<ResponseCode> listener) {
        NTPTime.getInstance().updteNTPTime();
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        long userId =complexPreferences.getLong(Constants.USERID, 0l);
        String token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexUser1(userId + token + Constants.RECEIVE_VOUCHERS);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02 = HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.RECEIVE_VOUCHERS);

        RetrofitServiceGenerator.getApi().ReceiveVouchers(head01
                , head02
                , head03
                , userId, token, tempId).enqueue(new Callback
                <ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }


            }
            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }


    /*
    * 使用场所：首页
    *
    * */
    @Override
    public void getProductListByNormalSort01(final int currentPage, int PageSize, final
    ActionCallbackListener<List<SortDataBean>> listener) {//过滤一层返回的数据类型
        /*01，参数的合法性检查*/
        if (currentPage < 0){
            listener.onFailure(ErrorEvent.MESSAGE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU2);
        }
        /*02.向API层调用接口，并且添加消息头,提交请求时，必须是一致确定的值*/
        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_PRODUCT_LIST_BY_NORMALSORT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_PRODUCT_LIST_BY_NORMALSORT);
        RetrofitServiceGenerator.getApi().getProductListByNormalSort(head01
                , head02
                , head03
                , currentPage
                , PageSize).enqueue(new Callback<GeneralSortDataBean>() {
            @Override
            public void onResponse(Call<GeneralSortDataBean> call, Response<GeneralSortDataBean> response) {

                /*参考http://blog.csdn.net/jason0539/article/details/9935955  list初始化*/
                // ArrayList<SortDataBean> relist = new ArrayList<SortDataBean>();//只有相同类型之间才能直接复制，不同类型之间不能 ,这种错误简直就是典型，list置为空在调用，就是空指针异常了

                if (response.isSuccessful()) {
/*                    String time = response.headers().get("Date");// 这个是当时留下来的一个坑
                    try {
                        if ((time != null) && !time.equals("")) {
                            final SimpleDateFormat sdf = new SimpleDateFormat(
                                    "EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));

                            Date serverDateUAT = sdf.parse(time);

                            long deltaBetweenServerAndClientTime = serverDateUAT
                                    .getTime()
                                    + 8 * 60 * 60 * 1000
                                    - System.currentTimeMillis();
                            *//*先待定*//*
                            Log.d("AppActionImpl", "serverDateUAT.getTime():" + serverDateUAT.getTime());
                            Log.d("AppActionImpl", "deltaBetweenServerAndClientTime:" +
                                    deltaBetweenServerAndClientTime);
                        }
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }*/
                    if (101 == response.body().getCode()) {
                        List<GeneralSortDataBean.DataBean.ListBean> list = response.body().getData().getList();
                        SortDataBean sortDataBean = null;
                        List<SortDataBean> relist = new ArrayList<SortDataBean>();
                        for (int i = 0; i < list.size(); i++) {
                            long ProductID = list.get(i).getProductID();
                            String City = list.get(i).getCity();
                            String District = list.get(i).getDistrict();
                            String ImagePath = list.get(i).getImagePath();
                            String ProductName = list.get(i).getProductName();
                            int OrderNum = list.get(i).getOrderNum();
                            double MinPrice = list.get(i).getMinPrice();
                            sortDataBean =new SortDataBean(ProductID,City, District, ImagePath, ProductName, OrderNum, MinPrice);
                            relist.add(sortDataBean);
                        }
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "product");
                        complexPreferences.putListObject("newest_Page", relist);
                        complexPreferences.putListObject(response.body().getData().getPageIndex(), relist);

                        /*在这里对服务器数据流做一层筛选和过滤，并且通知告示view层，数据源已经准备好了，你只需要处理就是了，list为空的情况也是需要考虑的*/
/*                            complexPreferences.putLong(Constants.time_getproductlistbynormalsort, System
                                    .currentTimeMillis());*/
                        listener.onSuccess(relist);


                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }
            }


            @Override
            public void onFailure(Call<GeneralSortDataBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);

            }
        });


    }
    /*用户相关*/
    @Override
    public void getOrderList(Map<String, Object> params,String orderType, final ActionCallbackQuadrupleListener<List<OrderParentBean>,String,String,String>
            listener) {

        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_ORDER_LIST);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_ORDER_LIST);


        RetrofitServiceGenerator.getApi().getOrderList(head01
                , head02
                , head03
                , userId, token, orderType, params).enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(Call<OrderListResponse> call, Response<OrderListResponse> response) {
                if (response.isSuccessful()) {
                    /*其实还有一个好处就是不要分的那么明确*/
                    if (response.body().getCode() == 101){
                        OrderListResponse.DataBean data  = response.body().getData();
                        String totalCount = data.getTotalCount();
                        String pageIndex = data.getPageIndex();
                        List<OrderListResponse.DataBean.ListBean> soucelist = data.getList();
                        List<OrderParentBean> list = new ArrayList<OrderParentBean>();
                        /*
                        *
                        *             StringBuffer sb = new StringBuffer(256);
            for (int i = 0;i<shopAttrlistData.size();i++){
                sb.append(shopAttrlistData.get(i).getName() + " ");
            }
            tv_other_service.setText(sb.toString());
                        *
                        * */
                        StringBuffer orderIds = new StringBuffer(256);
                        for (int i=0;i<soucelist.size();i++){
                            OrderListResponse.DataBean.ListBean databean =  soucelist.get(i);
                             long OrderID = databean.getOrderID();// 订单ID
                             long UserID = databean.getUserID();// 用户ID
                             long ShopID = databean.getShopID();// 店铺ID
                             String Price = databean.getPrice();// 订单价格
                             int Status = databean.getStatus();// 订单状态
                             String ShopName = databean.getShopName();
                             String ShopLogo = databean.getShopLogo();
                             if (1==0){
                                 orderIds.append(Long.toString(OrderID));
                             }else {
                                 orderIds.append(","+Long.toString(OrderID));
                             }

                             List<OrderListResponse.DataBean.ListBean.OrderExtendsBean> extendList = databean.getOrderExtends();
                             int count = extendList.size();
                            Log.d("ces", "count:" + count);
                            Log.d("ces", Price);
                             //先暂时按照数组的数量来算，不管套餐
                             list.add(new OrderParentBean(true, OrderID, UserID, ShopID, Price, Status, ShopName,
                                    ShopLogo,count));
                            for (int j=0;j<extendList.size();j++){
                                  OrderListResponse.DataBean.ListBean.OrderExtendsBean orderExtendsListBean=extendList.get(j);
                                long OrderExtendID = orderExtendsListBean.getOrderExtendID();// 订单ID
                                long ProductID = orderExtendsListBean.getProductID();// 用户ID
                                String ProductName = orderExtendsListBean.getProductName();// 订单价格
                                String ProductImage = orderExtendsListBean.getProductImage();// 订单状态
                                String ProductPrice = orderExtendsListBean.getProductPrice();
                                  String Quantity = orderExtendsListBean.getQuantity();

                                  boolean isLastChild =false;
                                  if (j==(extendList.size()-1)){
                                      //如果是最后一个子item
                                       isLastChild = true;
                                  }

                                  list.add(new OrderParentBean(new OrderChildBean(OrderExtendID,ProductID, ProductName,ProductImage,
                                         ProductPrice, Quantity,isLastChild,count,Price,Status,OrderID)));
                             }
                        }


                        listener.onSuccess(list,totalCount,pageIndex,orderIds.toString());
                    } else {
                        Log.d(TAG, "没有数据01");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response.body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<OrderListResponse> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void addAssessment(Map<String, Object> params, final ActionCallbackListener<ResponseCode> listener) {

        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.ADD_ASSESSMENT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.ADD_ASSESSMENT);


        RetrofitServiceGenerator.getApi().addAssessment(head01
                , head02
                , head03
                , userId, token, params).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    /*其实还有一个好处就是不要分的那么明确*/
                    if (response.body().getCode() == 101){
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据01");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    /*非用户相关的*/
    @Override
    public void getToken(final ActionCallbackListener<LoginResponseResult> listener) {
        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        Log.d(TAG, "userId:" + userId);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        Log.d(TAG, token);

        //这里还得判断过期


        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_ACCESS_TOKEN);
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_ACCESS_TOKEN);



        RetrofitServiceGenerator.getApi().getToken(head01
                , head02
                , head03
                , userId, token).enqueue(new Callback<LoginResponseResult>() {
            @Override
            public void onResponse(Call<LoginResponseResult> call, Response<LoginResponseResult> response) {
                if (response.isSuccessful()) {
                    PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
                    if (101 == response.body().getCode()) {
                        LoginResponseResult.DataBean  bean = response.body().getData();
                        Log.d(TAG, "登录");
                        Log.d(TAG, bean.getAccessToken());
                        Log.d(TAG, "bean.getUserID():" + bean.getUserID());
                        Log.d(TAG, "bean.getExpiresIn():" + bean.getExpiresIn());
                        complexPreferences.putString(Constants.ACCESS_TOKEN, bean.getAccessToken());
                        complexPreferences.putLong(Constants.EXPIRES_IN, bean.getExpiresIn());  //为什么会请求失效呢
                        complexPreferences.putLong(Constants.START_TIME, System.currentTimeMillis() / 1000L);
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    /*即使这种场景，也是不能获取到response的，503 Service Unavailable  服务不可用，说明是服务器的原因*/
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<LoginResponseResult> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void getAssessmentCount(long productId, final ActionCallbackNListener<String> listener) {

        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_ASSESSMENT_COUNT);
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_ASSESSMENT_COUNT);



        RetrofitServiceGenerator.getApi().getAssessmentCount(head01
                , head02
                , head03
                , productId).enqueue(new Callback<LoginResponseResult>() {
            @Override
            public void onResponse(Call<LoginResponseResult> call, Response<LoginResponseResult> response) {
                if (response.isSuccessful()) {
                    PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
                    if (101 == response.body().getCode()) {
                        LoginResponseResult.DataBean dataBean= response.body().getData();
                         String totalCount = dataBean.getTotalCount();
                         String goodCount = dataBean.getGoodCount();
                         String middleCount = dataBean.getMiddleCount();
                         String badCount = dataBean.getBadCount();
                        listener.onSuccess(totalCount,goodCount,middleCount,badCount);
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    /*即使这种场景，也是不能获取到response的，503 Service Unavailable  服务不可用，说明是服务器的原因*/
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<LoginResponseResult> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void getUserInfo(final ActionCallbackListener<UserBean.DataBean> listener) {

        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_USER_INFO);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_USER_INFO);


        RetrofitServiceGenerator.getApi().getUserInfo(head01
                , head02
                , head03
                , userId, token).enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
                        Log.d(TAG, "有数据");
                        UserBean.DataBean user = response.body().getData();
                        complexPreferences.putObject(Constants.USER_INFO, user);
                        listener.onSuccess(user);
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);

            }
        });


    }

    @Override
    public void GetUserCapital(final ActionCallbackDoubleListener<List<ChargeBean>,String> listener) {

        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_USERCAPITAL);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_USERCAPITAL);

        RetrofitServiceGenerator.getApi().getUserCapital(head01
                , head02
                , head03
                , userId, token).enqueue(new Callback<ChargeListResponse>() {
            @Override
            public void onResponse(Call<ChargeListResponse> call, Response<ChargeListResponse> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
                        ChargeListResponse.DataBean dataBean =response.body().getData();
                        String balance = dataBean.getBalances();
                        List<ChargeListResponse.DataBean.ListBean> list= dataBean.getList();
                        List<ChargeBean> relist = new ArrayList<ChargeBean>();
                        for (int i=0;i<list.size();i++){
                            ChargeListResponse.DataBean.ListBean data = list.get(i);
                            String ChargeDetailID = data.getChargeDetailID();
                            String ChargeAmount = data.getChargeAmount();
                            String ChargeTime =data.getChargeTime();
                            String ChargeWay =data.getChargeWay();
                            relist.add(new ChargeBean(ChargeDetailID, ChargeAmount, ChargeTime, ChargeWay) );
                        }
                        complexPreferences.putString(Constants.BALANCE,balance);
                        listener.onSuccess(relist,balance);
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ChargeListResponse> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);

            }
        });



    }

    @Override
    public void getUserPoint(final ActionCallbackDoubleListener<List<UserPointBean>, String> listener) {
        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_USER_POINT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_USER_POINT);

        RetrofitServiceGenerator.getApi().getUserPoint(head01
                , head02
                , head03
                , userId, token).enqueue(new Callback<UserPointListResponse>() {
            @Override
            public void onResponse(Call<UserPointListResponse> call, Response<UserPointListResponse> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        UserPointListResponse.DataBean dataBean =response.body().getData();
                        String point = dataBean.getPoint();
                        List<UserPointListResponse.DataBean.ListBean> list= dataBean.getList();
                        List<UserPointBean> relist = new ArrayList<UserPointBean>();

                        for (int i=0;i<list.size();i++){
                            UserPointListResponse.DataBean.ListBean data = list.get(i);
                             String TypeDesc = data.getTypeDesc();
                             String Integral = data.getIntegral();
                             String CreateDate = data.getCreateDate();
                            relist.add(new UserPointBean(TypeDesc,Integral,CreateDate) );
                        }
                        listener.onSuccess(relist,point);
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<UserPointListResponse> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);

            }
        });

    }

    @Override
    public void getOrderCount(String orderType, Integer PageNo) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        Log.d(TAG, "userId:" + userId);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        Log.d(TAG, token);

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_ORDER_COUNT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_ORDER_COUNT);


        RetrofitServiceGenerator.getApi().getOrderCount(head01
                , head02
                , head03
                , userId, token, orderType, PageNo).enqueue(new Callback<testData01>() {
            @Override
            public void onResponse(Call<testData01> call, Response<testData01> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){

                    } else {
                        Log.d(TAG, "没有数据00");
                        //listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response.body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    //listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response.body().getCode(), response.body().getMessage()));
                }//非法请求

            }

            @Override
            public void onFailure(Call<testData01> call, Throwable t) {

            }
        });



    }

    @Override
    public void getFavoriteProductList(int currentPage, final ActionCallbackTripleListener<List<SortDataBean>,String,String>
            listener) {
        if (currentPage < 0){
            listener.onFailure(ErrorEvent.CODE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU2);
        }

/*        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_FAVORITE_PRODUCT_LIST);
        long time_to_time = complexPreferences.getLong(Constants.TIME_TO_TIME, 0l);
        String head02 = Long.toString((System.currentTimeMillis()+time_to_time)/1000L);
        String head03= EncryptUtil.makeMD5(head02 + Constants.GET_FAVORITE_PRODUCT_LIST + "hby.mobile.client");*/

        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_FAVORITE_PRODUCT_LIST);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_FAVORITE_PRODUCT_LIST);

        RetrofitServiceGenerator.getApi().getFavoriteProductList(head01
                , head02
                , head03
                , userId, token, currentPage).enqueue(new Callback<GeneralSortDataBean>() {
            @Override
            public void onResponse(Call<GeneralSortDataBean> call, Response<GeneralSortDataBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()){
                        List<GeneralSortDataBean.DataBean.ListBean> list = response.body().getData().getList();
                        SortDataBean sortDataBean = null;
                        List<SortDataBean> relist = new ArrayList<SortDataBean>();
                        for (int i = 0; i < list.size(); i++) {
                            long ProductID = list.get(i).getProductID();
                            String ImagePath = list.get(i).getImagePath();
                            String ProductName = list.get(i).getProductName();
                            double MinPrice = list.get(i).getMinPrice();
                            long FpID =list.get(i).getFpID();
                            int IsHot =list.get(i).getIsHot();
                            int IsRecommend =list.get(i).getIsRecommend();
                            //02，第二版，补充字段
                            int OrderNum = list.get(i).getOrderNum();
                            String City = list.get(i).getCity();
                            String District = list.get(i).getDistrict();
                            String Province = list.get(i).getProvince();
                            sortDataBean =new SortDataBean(FpID, ProductID, ProductName, ImagePath, MinPrice, IsHot,
                                    IsRecommend,OrderNum, City, District,Province);
                            relist.add(sortDataBean);
                        }

                        listener.onSuccess(relist, response.body().getData().getPageIndex(), response.body().getData().getTotalCount());


                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<GeneralSortDataBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void getFavoriteProductCount(int PageNo, final ActionCallbackListener<String> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_FAVORITE_PRODUCT_COUNT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_FAVORITE_PRODUCT_COUNT);

        RetrofitServiceGenerator.getApi().getFavoriteProductCount(head01
                , head02
                , head03
                , userId, token, PageNo).enqueue(new Callback<LoginResponseResult>() {
            @Override
            public void onResponse(Call<LoginResponseResult> call, Response<LoginResponseResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        String totalCount = response.body().getData().getTotalCount();
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
                        complexPreferences.putString(Constants.TOTAL_COUNT_PRODUCT, totalCount);
                        listener.onSuccess(totalCount);

                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<LoginResponseResult> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void getUserMessageList(String messageType, int PageNo, int PageSize, final ActionCallbackListener<List<MyMessageBean>> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_USER_MESSAGE_LIST);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_USER_MESSAGE_LIST);

        RetrofitServiceGenerator.getApi().getUserMessageList(head01
                , head02
                , head03
                , userId, token,messageType, PageNo,PageSize).enqueue(new Callback<FootListResponse>() {
            @Override
            public void onResponse(Call<FootListResponse> call, Response<FootListResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        List<FootListResponse.DataBean.ListBean> list = response.body().getData().getList();
                        List<MyMessageBean> relist =new ArrayList<MyMessageBean>();
                        for (int i=0;i<list.size();i++){
                            FootListResponse.DataBean.ListBean bean = list.get(i);
                            long UserMessageID = bean.getUserMessageID();
                            long IsReading = bean.getIsReading();// 1 已读 其他未读
                            String MessageTypeDesc = bean.getMessageTypeDesc();
                            String CreateDate = bean.getCreateDate();
                            String MessageContext = bean.getMessageContext();
                            relist.add(new MyMessageBean(UserMessageID, IsReading, MessageTypeDesc,CreateDate,MessageContext));
                        }

                       listener.onSuccess(relist);

                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                   listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<FootListResponse> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    @Override
    public void getFavoriteShopList(int currentPage, final ActionCallbackTripleListener<List<ShopBean>,String,String>
            listener) {
        if (currentPage < 0){
            listener.onFailure(ErrorEvent.CODE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU2);
        }


        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_FAVORITE_SHOP_LIST);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_FAVORITE_SHOP_LIST);

        RetrofitServiceGenerator.getApi().getFavoriteShopList(head01
                , head02
                , head03
                , userId, token, currentPage).enqueue(new Callback<ShopRelatedServiceBean>() {
            @Override
            public void onResponse(Call<ShopRelatedServiceBean> call, Response<ShopRelatedServiceBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        Log.d(TAG, "有数据");
                        ShopRelatedServiceBean.DataBean databean = response.body().getData();
                         String totalCount = databean.getTotalCount();
                         String pageIndex = databean.getPageIndex();

                         List<ShopRelatedServiceBean.DataBean.ListBean> list = databean.getList();
                         List<ShopBean> relist = new ArrayList<ShopBean>();

                        for (int i=0;i<list.size();i++){
                            ShopRelatedServiceBean.DataBean.ListBean data = list.get(i);
                            long FsID  = data.getFsID();
                            long ShopID  = data.getShopID();
                            String ShopName  = data.getShopName();
                            String ShopLogo  = data.getShopLogo();
                            int OrderNum  = data.getOrderNum();
                            String MainService  = data.getMainService();  //主营服务
                            String District  = data.getDistrict();
                            double ComprehensiveScore  = data.getComprehensiveScore();
                            relist.add(new ShopBean(FsID,ShopID,ShopName, ShopLogo, OrderNum,MainService, District,ComprehensiveScore));
                        }
                        listener.onSuccess(relist,pageIndex,totalCount);

                    } else {
                        Log.d(TAG, "没有数据01");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ShopRelatedServiceBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void getFavoriteShopCount(int PageNo, final ActionCallbackListener<String> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_FAVORITE_SHOP_COUNT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_FAVORITE_SHOP_COUNT);

        RetrofitServiceGenerator.getApi().getFavoriteShopCount(head01
                , head02
                , head03
                , userId, token, PageNo).enqueue(new Callback<LoginResponseResult>() {
            @Override
            public void onResponse(Call<LoginResponseResult> call, Response<LoginResponseResult> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        String totalCount = response.body().getData().getTotalCount();
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
                        Log.d(TAG, "有数据");
                        complexPreferences.putString(Constants.TOTAL_COUNT_SHOP, totalCount);

                        listener.onSuccess(totalCount);

                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<LoginResponseResult> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void submitPayment(final String OrderIds, final ActionCallbackListener<LoginResponseResult.DataBean> listener) {
        Log.d("minfu", "来了？");
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        final Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        final String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        final String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.SUBMIT_PAYMENT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        final String head02=HeaderUtil.getHeadindex2(time);
        final String head03=HeaderUtil.getHeadindex3(Constants.SUBMIT_PAYMENT);

        /*
        *
        * 访问网络不能在主程序中进行，异步不需要，同步需要在单独线程中进行
        * http://blog.csdn.net/mad1989/article/details/25964495
        *
        * */
        /*
        *
        *
        * Caused by: java.lang.NumberFormatException: Expected a long but was 20160922180457388788710065 at line 1 column 80 path $.data.OrderPayId
        * 难道是解析数据时发生了异常
        *   Caused by: java.lang.NumberFormatException: Expected a long but was 20160922180457388788710065 at line 1 column 80 path $.data.OrderPayId
        *   http://blog.csdn.net/xiaodongvtion/article/details/8835668
        *   数字格式异常，类型解析出错
        *    {"code":101,"message":"处理成功！","data":{"OrderPayId":"20160922180457388788710065","Amount":0.01,"OrderDesc":"香港公司注册一站式服务","Balance":0.00}}
        *  "20160922180457388788710065"  [带引号的无论如何都是string类型]
        * */

/*                //把网络访问的代码放在这里
                try {
                    Response<LoginResponseResult> response = RetrofitServiceGenerator.getApi().submitPayment(head01
                            , head02
                            , head03
                            , userId, token, OrderIds).execute();//   android.os.NetworkOnMainThreadException
                    Log.d(TAG, "11");
                    Log.d(TAG, "13"+response.isSuccessful());
                    if (response.isSuccessful()) {
                        if (response.body().getCode() == 101){
                            listener.onSuccess( response.body().getData());

                        } else {
                            Log.d(TAG, "没有数据00");
                            listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                    .body().getCode(), response.body().getMessage()));
                        }

                    } else {
                        Log.d(TAG, "服务器的问题");
                        listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                    }

                } catch (IOException e) {
                    listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
                    e.printStackTrace();
                }*/


        RetrofitServiceGenerator.getApi().submitPayment(head01
                , head02
                , head03
                , userId, token, OrderIds).enqueue(new Callback<LoginResponseResult>() {
            @Override
            public void onResponse(Call<LoginResponseResult> call, Response<LoginResponseResult> response) {
                Log.d(TAG, "11");
                Log.d(TAG, "13"+response.isSuccessful());
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
                        complexPreferences.putString(Constants.BALANCE,String.valueOf(response.body().getData().getBalance()));
                        listener.onSuccess( response.body().getData());

                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }
            /*数据解析失败也可能导致onFailture的触发*/
            @Override
            public void onFailure(Call<LoginResponseResult> call, Throwable t) {
                Log.d(TAG, "12");
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    /*暂时设计的用户无关*/
    @Override
    public void getProduct(long productId, final ActionCallbackListener<ProductDetailsBean>
            listener) {


        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);

        String head04;
        if (isLogin){
            long userId =complexPreferences.getLong(Constants.USERID, 0l);
            head04 = String.valueOf(userId);
        }else {
            head04 =null;
        }


        //获取服务详情
        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_PRODUCT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_PRODUCT);



        RetrofitServiceGenerator.getApi().getProduct(head01
                , head02
                , head03
                ,head04, productId).enqueue(new Callback<DetailsBean>() {
            @Override
            public void onResponse(Call<DetailsBean> call, Response<DetailsBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        DetailsBean.DataBean.ProductBean productBean =  response.body().getData().getProduct();
                        DetailsBean.DataBean.ShopBean shopBean= response.body().getData().getShop();
                         long ProductID = productBean.getProductID();
                         String ProductName = productBean.getProductName();
                         String ImagePath = productBean.getImagePath();
                         double MinPrice = productBean.getMinPrice();
                         double DiscountPrice = productBean.getDiscountPrice();
                         int OrderNum = productBean.getOrderNum();
                         long ShopID= shopBean.getShopID();
                         String ShopName = shopBean.getShopName();
                         String ShopLogo = shopBean.getShopLogo();
                         String ShopAdd = shopBean.getShopAdd();
                         double ComprehensiveScore = shopBean.getComprehensiveScore();
                        ProductDetailsBean data = new ProductDetailsBean( ShopID,  ProductID,  ProductName,  ImagePath,  MinPrice,
                        DiscountPrice,  OrderNum,  ShopName,  ShopLogo,  ShopAdd,ComprehensiveScore);
                        /*
                        *   @文件名：product
                        *   @说明：存放productBean
                        *   @key: "productid"+ProductID [long]
                        *
                        * */
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "product");
                        complexPreferences.putObject("productid"+ProductID, data);
                        listener.onSuccess(data);
                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<DetailsBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }
    /*添加了另外想要的几个数据*/
    @Override
    public void getProduct01(long productId, final ActionCallbackFivefoldListener<ProductDetailsBean,List<SatisfySendBean>,List<VouchersTemplateBean>,List<ShopAttrBean>, List<DetailsBean.DataBean.CLBean>> listener) {
        //获取服务详情


        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);

        String head04;
        if (isLogin){
             long userId =complexPreferences.getLong(Constants.USERID, 0l);
             head04 = String.valueOf(userId);
        }else {
             head04 =null;
        }

        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_PRODUCT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_PRODUCT);



        RetrofitServiceGenerator.getApi().getProduct(head01
                , head02
                , head03
                , head04, productId).enqueue(new Callback<DetailsBean>() {
            @Override
            public void onResponse(Call<DetailsBean> call, Response<DetailsBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){

                        DetailsBean.DataBean.ProductBean productBean =  response.body().getData().getProduct();
                        DetailsBean.DataBean.ShopBean shopBean= response.body().getData().getShop();

                        List<DetailsBean.DataBean.SSBean> list = response.body().getData().getSSList();
                        SatisfySendBean DataBean = null;
                        List<SatisfySendBean> SatisfySendrelist = new ArrayList<SatisfySendBean>();

                        List<DetailsBean.DataBean.VTBean> VTlist = response.body().getData().getVTList();
                        VouchersTemplateBean VouchersTemplatDataBean = null;
                        List<VouchersTemplateBean> VouchersTemprelist = new ArrayList<VouchersTemplateBean>();

                        List<DetailsBean.DataBean.ProductBean.ShopAttrBean> ShopAttrlist = productBean.getShopAttList();
                        ShopAttrBean ShopAttrDataBean = null;
                        List<ShopAttrBean> ShopAttrrelist = new ArrayList<ShopAttrBean>();

                        for (int i =0;i<ShopAttrlist.size();i++){
                            long ShopAttrID = ShopAttrlist.get(i).getShopAttrID();
                            String Name = ShopAttrlist.get(i).getName();
                            String Price = ShopAttrlist.get(i).getPrice();
                            ShopAttrDataBean = new ShopAttrBean( ShopAttrID, Name, Price);
                            ShopAttrrelist.add(ShopAttrDataBean);
                        }



                        for (int i =0;i<VTlist.size();i++){
                            long VouchersTemplateID = VTlist.get(i).getVouchersTemplateID();
                            String Price = VTlist.get(i).getPrice();
                            String StartTime = VTlist.get(i).getStartTime();
                            String EndTime  = VTlist.get(i).getEndTime();
                            String Limit = VTlist.get(i).getLimit();
                            boolean IsExist = VTlist.get(i).isIsExist();
                            VouchersTemplatDataBean = new VouchersTemplateBean(VouchersTemplateID, Price,StartTime,EndTime,Limit,IsExist);
                            VouchersTemprelist.add(VouchersTemplatDataBean);
                        }


                        for (int i =0;i<list.size();i++){
                            long SSendRuleID = list.get(i).getSSendRuleID();;
                            String Price = list.get(i).getPrice() ;
                            String Money =  list.get(i).getMoney();;
                            DataBean = new SatisfySendBean(SSendRuleID, Money, Price);
                            SatisfySendrelist.add(DataBean);
                        }

                        long ProductID = productBean.getProductID();
                        String ProductName = productBean.getProductName();
                        String ImagePath = productBean.getImagePath();
                        double MinPrice = productBean.getMinPrice();
                        double DiscountPrice = productBean.getDiscountPrice();
                        int OrderNum = productBean.getOrderNum();
                        long ShopID= shopBean.getShopID();
                        String ShopName = shopBean.getShopName();
                        String ShopLogo = shopBean.getShopLogo();
                        String ShopAdd = shopBean.getShopAdd();
                        double ComprehensiveScore = shopBean.getComprehensiveScore();
                        String WebDes = productBean.getWebDes();
                        String ShareUrl = productBean.getShareUrl();//分享url”,
                        boolean IsFav = productBean.isFav();

                        ProductDetailsBean data = new ProductDetailsBean( ShopID,  ProductID,  ProductName,  ImagePath,  MinPrice,
                                DiscountPrice,  OrderNum,  ShopName,  ShopLogo,  ShopAdd,ComprehensiveScore,WebDes,ShareUrl,IsFav);
                        ShopBean shopBean1;
                        /*
                        *   @文件名：product
                        *   @说明：存放productBean
                        *   @key: "productid"+ProductID [long]
                        *
                        * */

                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "product");
                        PreferenceUtil complexPreferences01 = PreferenceUtil.getSharedPreference(context, "shop");
                        if(complexPreferences01.contains("shopid"+ShopID)){
                            shopBean1 = complexPreferences01.getObject("shopid"+ShopID,ShopBean.class);
                            shopBean1.setShopName(ShopName);
                            shopBean1.setShopLogo(ShopLogo);
                            shopBean1.setShopAdd(ShopAdd);
                            shopBean1.setComprehensiveScore(ComprehensiveScore);
                            complexPreferences01.putObject("shopid"+ShopID,shopBean);
                        } else {
                            shopBean1 = new ShopBean(ShopID, ShopName, ShopLogo,-1, null, null,
                                    false, ShopAdd, ComprehensiveScore);
                            complexPreferences01.putObject("shopid"+ShopID,shopBean1);
                        }

                        complexPreferences.putObject("productid"+ProductID, data);
                        complexPreferences.putObject("ProductDetailsBean", data);
                        complexPreferences.putListObject("productid" + ProductID + "SSList", SatisfySendrelist);
                        complexPreferences.putListObject("productid" + ProductID+"VTList", VouchersTemprelist);
                        complexPreferences.putListObject("productid" + ProductID+"SAList", ShopAttrrelist);
                        complexPreferences.putListObject("productid" + ProductID+"CLList", response.body().getData().getCityList());
                        listener.onSuccess(data,SatisfySendrelist,VouchersTemprelist,ShopAttrrelist,response.body().getData().getCityList());
                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }

            @Override
            public void onFailure(Call<DetailsBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    @Override
    public void getProductSatisfySend(long productId, final ActionCallbackListener<List<SatisfySendBean>> listener) {



        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);

        String head04;
        if (isLogin){
            long userId =complexPreferences.getLong(Constants.USERID, 0l);
            head04 = String.valueOf(userId);
        }else {
            head04 =null;
        }


        //获取服务详情
        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_PRODUCT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_PRODUCT);
        RetrofitServiceGenerator.getApi().getProduct(head01
                , head02
                , head03
                , head04,productId).enqueue(new Callback<DetailsBean>() {
            @Override
            public void onResponse(Call<DetailsBean> call, Response<DetailsBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        List<DetailsBean.DataBean.SSBean> list = response.body().getData().getSSList();
                        DetailsBean.DataBean.ProductBean productBean =  response.body().getData().getProduct();
                        long ProductID = productBean.getProductID();
                        SatisfySendBean DataBean = null;
                        List<SatisfySendBean> relist = new ArrayList<SatisfySendBean>();

                        for (int i =0;i<list.size();i++){
                             long SSendRuleID = list.get(i).getSSendRuleID();;
                             String Price = list.get(i).getPrice() ;
                             String Money =  list.get(i).getMoney();;
                             DataBean = new SatisfySendBean(SSendRuleID, Money, Price);
                             relist.add(DataBean);
                        }
                        /*
                        *   @文件名：product
                        *   @说明：存放 SSlist 列表
                        *   @key: "productid"+ProductID+"SSList"
                        *
                        * */
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "product");
                        complexPreferences.putListObject("productid" + ProductID+"SSList", relist);
                        listener.onSuccess(relist);
                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");

                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }

            @Override
            public void onFailure(Call<DetailsBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }
    /*获取产品的优惠券*/
    @Override
    public void getVouchersTemplate(long productId, final ActionCallbackListener<List<VouchersTemplateBean>> listener) {



        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);

        String head04;
        if (isLogin){
            long userId =complexPreferences.getLong(Constants.USERID, 0l);
            head04 = String.valueOf(userId);
        }else {
            head04 =null;
        }


        //获取服务详情
        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_PRODUCT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_PRODUCT);
        RetrofitServiceGenerator.getApi().getProduct(head01
                , head02
                , head03
                ,head04, productId).enqueue(new Callback<DetailsBean>() {
            @Override
            public void onResponse(Call<DetailsBean> call, Response<DetailsBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        List<DetailsBean.DataBean.VTBean> list = response.body().getData().getVTList();
                        DetailsBean.DataBean.ProductBean productBean =  response.body().getData().getProduct();
                        long ProductID = productBean.getProductID();
                        VouchersTemplateBean DataBean = null;
                        List<VouchersTemplateBean> relist = new ArrayList<VouchersTemplateBean>();

                        for (int i =0;i<list.size();i++){
                             long VouchersTemplateID = list.get(i).getVouchersTemplateID();
                             String Price = list.get(i).getPrice();
                             String StartTime = list.get(i).getStartTime();
                             String EndTime  = list.get(i).getEndTime();
                             String Limit = list.get(i).getLimit();
                             boolean IsExist = list.get(i).isIsExist();
                             DataBean = new VouchersTemplateBean(VouchersTemplateID, Price,StartTime,EndTime,Limit,IsExist);
                             relist.add(DataBean);
                        }
                        /*
                        *   @文件名：product
                        *   @说明：存放 VTlist 列表
                        *   @key: "productid"+ProductID+"VTList"
                        *
                        * */
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "product");
                        complexPreferences.putListObject("productid" + ProductID+"VTList", relist);
                        listener.onSuccess(relist);
                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }

            @Override
            public void onFailure(Call<DetailsBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void getShopAttr(long productId, final ActionCallbackListener<List<ShopAttrBean>> listener) {

        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);

        String head04;
        if (isLogin){
            long userId =complexPreferences.getLong(Constants.USERID, 0l);
            head04 = String.valueOf(userId);
        }else {
            head04 =null;
        }



        //获取服务详情
        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_PRODUCT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_PRODUCT);
        RetrofitServiceGenerator.getApi().getProduct(head01
                , head02
                , head03
                , head04, productId).enqueue(new Callback<DetailsBean>() {
            @Override
            public void onResponse(Call<DetailsBean> call, Response<DetailsBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        DetailsBean.DataBean.ProductBean productBean =  response.body().getData().getProduct();
                        long ProductID = productBean.getProductID();
                        List<DetailsBean.DataBean.ProductBean.ShopAttrBean> list = productBean.getShopAttList();
                        ShopAttrBean DataBean = null;
                        List<ShopAttrBean> relist = new ArrayList<ShopAttrBean>();

                        for (int i =0;i<list.size();i++){
                             long ShopAttrID = list.get(i).getShopAttrID();
                             String Name = list.get(i).getName();
                             String Price = list.get(i).getPrice();
                            DataBean = new ShopAttrBean( ShopAttrID, Name, Price);
                            relist.add(DataBean);
                        }
                        /*
                        *   @文件名：product
                        *   @说明：存放 ShopAttrlist 列表
                        *   @key: "productid"+ProductID+"SAList"
                        *
                        * */
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "product");
                        complexPreferences.putListObject("productid" + ProductID+"SAList", relist);
                        listener.onSuccess(relist);
                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }

            @Override
            public void onFailure(Call<DetailsBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void getAssessmentList(final long productID, final int assessType, final int pageNo, int pageSize, final ActionCallbackTripleListener<List<EvaBean>,Integer,Integer>
            listener) {
        NTPTime.getInstance().updteNTPTime();
        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_ASSESSMENT_LIST);
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_ASSESSMENT_LIST);
        RetrofitServiceGenerator.getApi().getAssessmentList(head01
                , head02
                , head03
                , productID, assessType, pageNo, pageSize).enqueue(new Callback<EvaluationBean>() {
            @Override
            public void onResponse(Call<EvaluationBean> call, Response<EvaluationBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        EvaluationBean.DataBean source = response.body().getData();
                        int pageIndex = source.getPageIndex();
                        int totalCount = source.getTotalCount();
                        List<EvaluationBean.DataBean.ListBean> list = source.getList();
                        EvaBean EvaBean = null;
                        List<EvaBean> relist = new ArrayList<EvaBean>();
                        for (int i = 0; i < list.size(); i++){
                             long UserAssessmentID = list.get(i).getAssessmentID();
                             String UserName = list.get(i).getName();
                             String UserViews = list.get(i).getViews();
                             String UserAssessmentDate = list.get(i).getAssessmentDate();
                             List<EvaluationBean.DataBean.ListBean.ReplyListBean> replylist = list.get(i).getReplyList();
                             if (replylist.isEmpty()||replylist==null){
                                 boolean ishasReply=false;
                                 EvaBean = new EvaBean(UserAssessmentID ,UserName, UserViews, UserAssessmentDate,
                                 ishasReply);
                                 relist.add(EvaBean);
                             } else {
                                 boolean ishasReply=true;
                                 for (int k=0;k<replylist.size();k++){
                                      long traderAssessmentID = replylist.get(i).getAssessmentID();
                                      String traderName = replylist.get(i).getName();
                                      String traderViews = replylist.get(i).getViews();
                                      String traderAssessmentDate = replylist.get(i).getAssessmentDate();
                                      EvaBean =new EvaBean(UserAssessmentID ,UserName, UserViews, UserAssessmentDate,
                                             ishasReply,traderAssessmentID, traderName, traderViews, traderAssessmentDate);
                                 }
                                 relist.add(EvaBean);

                             }
                        }
                        /*
                        * 存放的是评价的list
                        * 分为单个用户相关，产品相关，店铺相关 三个文件,后期再来延伸吧  暂时先不考虑缓存吧！
                        * */
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "product");

                        //只保证最新的头两页内容
                        if (pageNo<2){
                            complexPreferences.putListObject("productid" + productID + "assessType"+assessType+"EVList", relist);
                        }
                        listener.onSuccess(relist,pageIndex,totalCount); //返回的永远是最新的

                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }

            @Override
            public void onFailure(Call<EvaluationBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void getChargeList(int pageNo, int pageSize, final ActionCallbackTripleListener<List<ChargeBean>, Integer,
                Integer> listener) {

        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_CHARGE_LIST);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_CHARGE_LIST);

        RetrofitServiceGenerator.getApi().getChargeList(head01
                , head02
                , head03
                , userId, token, pageNo, pageSize).enqueue(new Callback<ChargeListResponse>() {
            @Override
            public void onResponse(Call<ChargeListResponse> call, Response<ChargeListResponse> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        ChargeListResponse.DataBean dataBean =response.body().getData();
                        int pageIndex = dataBean.getPageIndex();
                        int totalCount = dataBean.getTotalCount();
                        List<ChargeListResponse.DataBean.ListBean> list= dataBean.getList();
                        List<ChargeBean> relist = new ArrayList<ChargeBean>();
                        for (int i=0;i<list.size();i++){
                            ChargeListResponse.DataBean.ListBean data = list.get(i);
                            String ChargeDetailID = data.getChargeDetailID();
                            String ChargeAmount = data.getChargeAmount();
                            String ChargeTime =data.getChargeTime();
                            String ChargeWay =data.getChargeWay();
                            relist.add(new ChargeBean(ChargeDetailID, ChargeAmount, ChargeTime, ChargeWay) );
                        }
                        listener.onSuccess(relist,pageIndex,totalCount);

                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ChargeListResponse> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);

            }
        });




    }

    @Override
    public void getRedEnvelopeList(int pageNo, int pageSize, final ActionCallbackTripleListener<List<RedEnvelopBean>, Integer, Integer> listener) {
            //用户相关

        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId + token + Constants.GET_RED_ENVELOPE_LIST);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_RED_ENVELOPE_LIST);

        RetrofitServiceGenerator.getApi().getRedEnvelopeList(head01
                , head02
                , head03
                , userId, token, pageNo, pageSize).enqueue(new Callback<RedEnvelopListResponse>() {
            @Override
            public void onResponse(Call<RedEnvelopListResponse> call, Response<RedEnvelopListResponse> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        RedEnvelopListResponse.DataBean dataBean =response.body().getData();
                        int pageIndex = dataBean.getPageIndex();
                        int totalCount = dataBean.getTotalCount();
                        List<RedEnvelopListResponse.DataBean.ListBean> list= dataBean.getList();
                        List<RedEnvelopBean> relist = new ArrayList<RedEnvelopBean>();
                        for (int i=0;i<list.size();i++){
                            RedEnvelopListResponse.DataBean.ListBean data = list.get(i);

                             long RedEnvelopeID = data.getRedEnvelopeID();
                             String Price = data.getPrice();
                             int Status = data.getStatus();
                             long RedEnvelopeTemplateID = data.getRedEnvelopeTemplateID();
                             String OrderLimit = data.getOrderLimit();
                             String EndTime = data.getEndTime();

                            relist.add(new RedEnvelopBean(RedEnvelopeID, RedEnvelopeTemplateID,OrderLimit,EndTime,Price, Status));
                        }
                        listener.onSuccess(relist,pageIndex,totalCount);

                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<RedEnvelopListResponse> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);

            }
        });



    }

    @Override
    public void getMyFootList(int pageNo, int pageSize, final ActionCallbackTripleListener<List<FootBean>, Integer, Integer
                > listener) {
        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId + token + Constants.GET_MY_FOOT_LIST);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_MY_FOOT_LIST);

        RetrofitServiceGenerator.getApi().getMyFootList(head01
                , head02
                , head03
                , userId, token, pageNo, pageSize).enqueue(new Callback<FootListResponse>() {
            @Override
            public void onResponse(Call<FootListResponse> call, Response<FootListResponse> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        FootListResponse.DataBean dataBean =response.body().getData();
                        int pageIndex = dataBean.getPageIndex();
                        int totalCount = dataBean.getTotalCount();
                        List<FootListResponse.DataBean.ListBean> list= dataBean.getList();
                        List<FootBean> relist = new ArrayList<FootBean>();
                        for (int i=0;i<list.size();i++){
                            FootListResponse.DataBean.ListBean data = list.get(i);
                             long ProductID  = data.getProductID();
                             String ProductName  = data.getProductName();
                             String MinPrice  = data.getMinPrice();
                             String ImagePath  = data.getImagePath();
                            relist.add(new FootBean(ProductID, ProductName, MinPrice, ImagePath) );
                        }
                        listener.onSuccess(relist,pageIndex,totalCount);

                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<FootListResponse> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);

            }
        });




    }

    @Override
    public void getRedEnvelopeTempList(int pageNo, int pageSize, final ActionCallbackTripleListener<List<RedEnvelopBean>,
                Integer, Integer> listener) {
        //用户无关
        String head01=HeaderUtil.getHeadindexNotUser1(Constants.Get_RedEnvelope_Temp_List);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.Get_RedEnvelope_Temp_List);

        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);
        String head04 = null;
        if (isLogin){
            Long userId =complexPreferences.getLong(Constants.USERID, 0l);
            head04 = Long.toString(userId);
        }

        RetrofitServiceGenerator.getApi().getRedEnvelopeTempList(head01
                , head02
                , head03
                , head04, pageNo, pageSize).enqueue(new Callback<RedEnvelopListResponse>() {
            @Override
            public void onResponse(Call<RedEnvelopListResponse> call, Response<RedEnvelopListResponse> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        RedEnvelopListResponse.DataBean dataBean =response.body().getData();
                        int pageIndex = dataBean.getPageIndex();
                        int totalCount = dataBean.getTotalCount();
                        List<RedEnvelopListResponse.DataBean.ListBean> list= dataBean.getList();
                        List<RedEnvelopBean> relist = new ArrayList<RedEnvelopBean>();
                        for (int i=0;i<list.size();i++){
                            RedEnvelopListResponse.DataBean.ListBean data = list.get(i);
                            String EachLimit = data.getEachLimit();
                            String RedEnvelopePrice = data.getRedEnvelopePrice();
                            boolean IsReceive = data.isReceive();// 是否领取 true可以
                            boolean IsCanReceive = data.isCanReceive();// 是否能领取 true
                            long RedEnvelopeTemplateID = data.getRedEnvelopeTemplateID();
                            String OrderLimit = data.getOrderLimit();
                            String EndTime = data.getEndTime();
                            relist.add(new RedEnvelopBean(RedEnvelopeTemplateID, RedEnvelopePrice, OrderLimit, EndTime,
                                    EachLimit, IsReceive,IsCanReceive));
                        }
                        listener.onSuccess(relist,pageIndex,totalCount);

                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<RedEnvelopListResponse> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);

            }
        });




    }

    @Override
    public void getVoucherList(int pageNo, int pageSize, final ActionCallbackListener<List<VoucherBean>> listener) {

        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_VOUCHER_LIST);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_VOUCHER_LIST);

        RetrofitServiceGenerator.getApi().getVoucherList(head01
                , head02
                , head03
                , userId, token, pageNo, pageSize).enqueue(new Callback<VoucherListResponse>() {
            @Override
            public void onResponse(Call<VoucherListResponse> call, Response<VoucherListResponse> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                           List<VoucherListResponse.DataBean> list=response.body().getData();

                        List<VoucherBean> relist = new ArrayList<VoucherBean>();
                        for (int i=0;i<list.size();i++){
                            VoucherListResponse.DataBean data = list.get(i);
                             long VouchersID = data.getVouchersID();
                             int Status = data.getStatus();
                             String ShopName = data.getShopName();
                             String Price = data.getPrice();
                             String EndTime = data.getEndTime();
                             String Limit = data.getLimit();
                            relist.add(new VoucherBean(VouchersID,Limit,Price,EndTime,Status,ShopName) );
                        }
                        listener.onSuccess(relist);

                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                 Log.d(TAG, "服务器的问题");
                       listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<VoucherListResponse> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);

            }
        });



    }

    @Override
    public void getProductListByHotSort(int currentPage, int PageSize) {
        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_PRODUCT_LIST_BY_HOT_SORT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_PRODUCT_LIST_BY_HOT_SORT);

        RetrofitServiceGenerator.getApi().getProductListByHotSort(head01
                , head02
                , head03
                , currentPage, PageSize).enqueue(new Callback<GeneralSortDataBean>() {
            @Override
            public void onResponse(Call<GeneralSortDataBean> call, Response<GeneralSortDataBean> response) {


            }

            @Override
            public void onFailure(Call<GeneralSortDataBean> call, Throwable t) {

            }
        });

    }





    /*用户相关*/
    @Override
    public void getShop(long shopId) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        Log.d(TAG, "userId:" + userId);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        Log.d(TAG, token);
        String head01;

        //用户相关
        head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_SHOP);
         /*这个地方需要先判断一下*/

        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_SHOP);

        RetrofitServiceGenerator.getApi().getShop(Long.toString(userId), head01
                , head02
                , head03
                , shopId).enqueue(new Callback<ShopResponseBean>() {
            @Override
            public void onResponse(Call<ShopResponseBean> call, Response<ShopResponseBean> response) {
                //shopBean
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        Log.d(TAG, "有数据");
                        //listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        //listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response.body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    //listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response.body().getCode(), response.body().getMessage()));
                }

            }

            @Override
            public void onFailure(Call<ShopResponseBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                //listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void getAreaList() {
        //写入到文件
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        long time_to_time = complexPreferences.getLong(Constants.TIME_TO_TIME, 0l);
        String head01=EncryptUtil.makeMD5(Constants.GET_AREA_LIST + "hby.mobile.client");
        String head02 = Long.toString((System.currentTimeMillis() + time_to_time) / 1000L);
        String head03= EncryptUtil.makeMD5(head02 + Constants.GET_AREA_LIST + "hby.mobile.client");
        RetrofitServiceGenerator.getApi().GetAreaList(head01
                , head02
                , head03).enqueue(new Callback<AreaList>() {
            @Override
            public void onResponse(Call<AreaList> call, Response<AreaList> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
/*                        SharedPreferences preference = context.getSharedPreferences("preferences_city", Context
                                .MODE_PRIVATE);
                        SharedPreferences.Editor editor = preference.edit();*/
                        Gson GSON = new Gson();
/*                        editor.putString("see", GSON.toJson(response.body()));
                        editor.apply();*/
                        //Log.d(TAG, GSON.toJson(response.body()));
                        Log.d(TAG, GSON.toJson(response.body()));//{"body": 多了一个body字样
                        String databasePath = "/data"+ Environment.getDataDirectory().getAbsolutePath() + File.separator+context.getPackageName()+"/databases";
                        File dFile=new File(databasePath);
                        if (!dFile.exists()) {
                            dFile.mkdir();
                        }
                        String DB_NAME = "preferences_city.json";//数据库名不对外公开
                        String fileName = databasePath + "/" + DB_NAME;
                        try {
                            if (!(new File(fileName).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库

                                InputStream oldDatabaseFile = new ByteArrayInputStream(GSON.toJson(response.body()).getBytes());
                                byte[] buffer = new byte[oldDatabaseFile.available()];
                                FileOutputStream newDatabaseFile = new FileOutputStream(fileName);// 写入数据库中的表中，写入一次，根据路径创建文件
                                int length = 0;
                                while ((length = oldDatabaseFile.read(buffer)) > 0) {
                                    newDatabaseFile.write(buffer, 0, length);/*这段逻辑怎么没有用，感觉就是一个复制*/
                                }
                                newDatabaseFile.flush();
                                newDatabaseFile.close();
                                oldDatabaseFile.close();
                            }
                        } catch (FileNotFoundException e) {
                            Log.e("Database", "File not found");
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.e("Database", "IO exception");
                            e.printStackTrace();
                        }
                        Log.d(TAG, "有数据");


                        //listener.onSuccess(response.body());

                    } else {
                        Log.d(TAG, "没有数据");
                        //listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response.body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    //listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response.body().getCode(), response.body().getMessage()));
                }


            }

            @Override
            public void onFailure(Call<AreaList> call, Throwable t) {

            }
        });


    }

    /*用户相关了*/
    @Override
    public void deleteCartSelectedItem(long ProductId, long GroupSellID, final ActionCallbackListener<String> listener) {
        String strEntity;
        if (GroupSellID ==0){
            //Carts.addProperty("ProductId", ProductId);
            List<Carts.Cartbean01 > list01= new ArrayList<Carts.Cartbean01>();
            Carts.Cartbean01 cart01= new Carts.Cartbean01(ProductId);
            list01.add(cart01);
            Gson gson = new Gson();
            strEntity = gson.toJson(list01);
        } else {
            //Carts.addProperty("GroupSellID", GroupSellID);
            List<Carts.Cartbean02 > list02= new ArrayList<Carts.Cartbean02>();
            Carts.Cartbean02 cart02= new Carts.Cartbean02(GroupSellID);
            list02.add(cart02);
            Gson gson = new Gson();
            strEntity = gson.toJson(list02);
        }
        Log.d("strEntity:", strEntity);

/*        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.DELETE_CART_SELECTEDITEM);
        long time_to_time = complexPreferences.getLong(Constants.TIME_TO_TIME, 0l);
        String head02 = Long.toString((System.currentTimeMillis()+time_to_time)/1000L);
        String head03= EncryptUtil.makeMD5(head02 + Constants.DELETE_CART_SELECTEDITEM + "hby.mobile.client");*/



        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.DELETE_CART_SELECTEDITEM);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.DELETE_CART_SELECTEDITEM);




        RetrofitServiceGenerator.getApi().deleteCartSelectedItem(head01
                , head02
                , head03, userId, token, strEntity).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body().getData().getTotalPrice());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL );
                }
            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void saveGeneralInvoice(String title,boolean isSelected, final ActionCallbackListener<ResponseCode> listener) {

        Invoinces.Invoincesbean bean = new  Invoinces.Invoincesbean(title, isSelected);
        List<Invoinces.Invoincesbean > list= new ArrayList<Invoinces.Invoincesbean>();
        list.add(bean);
        Gson gson = new Gson();
        String strEntity = gson.toJson(list);

/*        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.SAVE_GENERAL_INVOICE);
        long time_to_time = complexPreferences.getLong(Constants.TIME_TO_TIME, 0l);
        String head02 = Long.toString((System.currentTimeMillis()+time_to_time)/1000L);
        String head03= EncryptUtil.makeMD5(head02 + Constants.SAVE_GENERAL_INVOICE + "hby.mobile.client");*/


        // 用户相关的

        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.SAVE_GENERAL_INVOICE);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SAVE_GENERAL_INVOICE);



        RetrofitServiceGenerator.getApi().saveGeneralInvoice(head01
                , head02
                , head03, userId, token, strEntity).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL );
                }
            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });



    }

    @Override
    public void saveVatInvoice(Map<String, String> params, final ActionCallbackListener<ResponseCode> listener) {
        if (TextUtils.isEmpty(params.get(Constants.COMPANYNAME))){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "单位名称不能为空");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }

        if (TextUtils.isEmpty(params.get(Constants.IDENTITYCODE))){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "纳税人识别码不能为空");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }


        if (TextUtils.isEmpty(params.get(Constants.REGISTERADDRESS))){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "注册地址不能为空");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }
        if (TextUtils.isEmpty(params.get(Constants.BANKNAME))){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "开户银行不能为空");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }
        if (TextUtils.isEmpty(params.get(Constants.BANKACCOUNT))){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "银行账户不能为空");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }


/*        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.SAVE_VAT_INVOICE);
        long time_to_time = complexPreferences.getLong(Constants.TIME_TO_TIME, 0l);
        String head02 = Long.toString((System.currentTimeMillis()+time_to_time)/1000L);
        String head03= EncryptUtil.makeMD5(head02 + Constants.SAVE_VAT_INVOICE + "hby.mobile.client");*/

        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.SAVE_VAT_INVOICE);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SAVE_VAT_INVOICE);

        RetrofitServiceGenerator.getApi().saveVatInvoice(head01
                , head02
                , head03
                , userId, token, params).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());
                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(),
                                response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void bindEmail(Map<String, String> params, final ActionCallbackListener<ResponseCode> listener) {
        if (TextUtils.isEmpty(params.get(Constants.EMAIL))){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "邮箱不能为空");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }
        if (!RegexUtil.validateEmail(Constants.EMAIL)){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "邮箱格式不正确");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }
        if (TextUtils.isEmpty(params.get(Constants.VALIDATECODEOFBIND))){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "验证码不能为空");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }
        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.BIND_EMAIL);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.BIND_EMAIL);

        RetrofitServiceGenerator.getApi().bindEmail(head01
                , head02
                , head03
                , userId, token, params).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());


                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(),
                                response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void bindMobile(Map<String, String> params, final ActionCallbackListener<ResponseCode> listener) {
        if (TextUtils.isEmpty(params.get(Constants.MOBILE))){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "手机号不能为空");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }
        if (!RegexUtil.validateMobile(Constants.MOBILE)){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "手机号格式不正确");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }
        if (TextUtils.isEmpty(params.get(Constants.VALIDATECODEOFBIND))){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "验证码不能为空");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }
        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.BIND_MOBILE);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.BIND_MOBILE);

        RetrofitServiceGenerator.getApi().bindMobile(head01
                , head02
                , head03
                , userId, token, params).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());


                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                        .body().getCode(),
                                response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void applyOrderRefund(Map<String, Object> params, final ActionCallbackListener<ResponseCode> listener) {

        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.APPLY_ORDER_REFUND);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.APPLY_ORDER_REFUND);

        RetrofitServiceGenerator.getApi().applyOrderRefund(head01
                , head02
                , head03
                , userId, token, params).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());


                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                        .body().getCode(),
                                response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    @Override
    public void uploadUserPhoto(String path, final ActionCallbackListener<String> listener) {

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = new File(path);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "upload user phone";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);//这个内容的类型很重要
        /*
        * 题外话：如果是application/json,  json格式，那么服务器就会反序列化了
        * 头部就要表明不是发送的json数据
        *
        * */


        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.UPLOAD_USER_PHOTO);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.UPLOAD_USER_PHOTO);

        RetrofitServiceGenerator.getApi().uploadUserPhoto(head01
                , head02
                , head03
                , userId, token, description, body).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body().getData());


                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(),
                                response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                   listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    @Override
    public void setPayPwd(String AccountPwd, String PayPwd, final ActionCallbackListener<ResponseCode> listener) {

        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.SET_PAY_PWD);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SET_PAY_PWD);

        RetrofitServiceGenerator.getApi().setPayPwd(head01
                , head02
                , head03
                , userId, token, AccountPwd, PayPwd).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        //设置已经设置支付密码的标识
                        UserBean.DataBean user = complexPreferences.getObject(Constants.USER_INFO, UserBean.DataBean.class);
                        user.setIsHasPayPwd(true);
                        complexPreferences.putObject(Constants.USER_INFO, user);

                        listener.onSuccess(response.body());


                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(),
                                response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    @Override
    public void modifyPayPwd(String AccountPwd, String ValidateCode, String PayPwd, final ActionCallbackListener<ResponseCode> listener) {

        if (TextUtils.isEmpty(PayPwd)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, ErrorEvent.MESSAGE_NULL_SITU4);
            }
            return;
        }
        //只要对新密码约束就好了

        if ( !RegexUtil.validatePassword(PayPwd)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU3);
            }
            return;// 需要做一层拦截
        }
        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.MODIFY_PAY_PWD);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.MODIFY_PAY_PWD);

        RetrofitServiceGenerator.getApi().modifyPayPwd(head01
                , head02
                , head03
                , userId, token, AccountPwd, ValidateCode,PayPwd).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());


                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(),
                                response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    @Override
    public void addCartItem(long ShopId, long ProductId, long GroupSellID, int Quantity,long districtID, List<Long> AttributeIdList, final ActionCallbackListener<ResponseCode> listener) {
        /*
        * GroupSellID：0和非0 标志是否为套餐服务，先把这个搞完后，再来谈论套餐服务
        * Quantity：数量
        * AttributeIdList：其他服务的id，当前产品的其他服务
        * 顺序排序下，减少误差
        *
        * retrofit传递json  参考
        * http://stackoverflow.com/questions/21398598/how-to-post-raw-whole-json-in-the-body-of-a-retrofit-request
        * */
        NTPTime.getInstance().updteNTPTime();
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        long userId =complexPreferences.getLong(Constants.USERID, 0l);
        String token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexUser1(userId + token + Constants.ADD_CART_ITEM);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02 = HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.ADD_CART_ITEM);//head02+Constants.ADD_CART_ITEM 1,2,3的顺序不能改动



/*        jsonParams.put("ProductId", ProductId);
        jsonParams.put("GroupSellID", GroupSellID);
        jsonParams.put("Quantity", Quantity);
        jsonParams.put("AttributeIdList", AttributeIdList);*/

//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),new JSONObject(jsonParams).toString());
//        Log.d("BBB", "body:" + body); (new JSONObject(jsonParams)).toString()ShopId, ProductId, GroupSellID, Quantity, AttributeIdList)
        List<Carts.Cartbean > list= new ArrayList<Carts.Cartbean>();
        Carts.Cartbean cart= new Carts.Cartbean(ShopId, ProductId, GroupSellID, Quantity,districtID, AttributeIdList);
        list.add(cart);
        Gson gson = new Gson();
        String strEntity = gson.toJson(list);
        Log.d("ceshi", strEntity);
//        HashMap<String, Object> jsonParams =new HashMap<String, Object>();

//        jsonParams.put("Carts", list);
/*        String strEntity01 = gson.toJson(jsonParams);
        Log.d("ceshi", strEntity01);*/




       /* c1.setProducts(list);
        String strEntity02 = gson.toJson(c1);
        Log.d("ceshi", strEntity02);*/
        //new JSONObject(carts).toString()

//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), strEntity);
/*
        JsonObject obj = new JsonObject();
        JsonObject Carts = new JsonObject();
        Carts.addProperty("ShopId",ShopId);
        Carts.addProperty("ProductId", ProductId);
        Carts.addProperty("GroupSellID", GroupSellID);
        Carts.addProperty("Quantity", Quantity);  //数据是不行的
        JsonArray array =new JsonArray();
        array.add(Carts);
        obj.add("Carts", array);
        Log.d("ceshi", obj.toString());
        Log.d("ceshi", "obj:" + obj);*/
//ShopId,ProductId,GroupSellID,Quantity,AttributeIdList
        //RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), strEntity02);
        RetrofitServiceGenerator.getApi().addCartItem(head01
                , head02
                , head03
                , userId, token,strEntity).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }
            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });



    }

    @Override
    public void selectCartOtherService(long productId,long groupSellId,List< AttrIds.AttrIdsbean > list, final ActionCallbackListener<ResponseCode> listener) {

        NTPTime.getInstance().updteNTPTime();
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        long userId =complexPreferences.getLong(Constants.USERID, 0l);
        String token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexUser1(userId + token + Constants.SELECT_CART_OTHER_SERVICE);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02 = HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SELECT_CART_OTHER_SERVICE);


        Gson gson = new Gson();
        String strEntity = gson.toJson(list);

        RetrofitServiceGenerator.getApi().selectCartOtherService(head01
                , head02
                , head03
                , userId, token, productId, groupSellId, strEntity).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                Log.d(TAG, "response.isSuccessful():" + response.isSuccessful());
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        Log.d(TAG, "1");
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }
            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void addCartItemOfBuyNow(long ShopId, long ProductId, long GroupSellID, int Quantity, long districtID,
                                    List<Long>  AttributeIdList, final ActionCallbackListener<ResponseCode> listener) {
           /*
        * GroupSellID：0和非0 标志是否为套餐服务，先把这个搞完后，再来谈论套餐服务
        * Quantity：数量
        * AttributeIdList：其他服务的id，当前产品的其他服务
        * 顺序排序下，减少误差
        *
        * retrofit传递json  参考
        * http://stackoverflow.com/questions/21398598/how-to-post-raw-whole-json-in-the-body-of-a-retrofit-request
        * */
        NTPTime.getInstance().updteNTPTime();
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        long userId =complexPreferences.getLong(Constants.USERID, 0l);
        String token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexUser1(userId + token + Constants.ADD_CART_ITEM_OF_BUY_NOW);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02 = HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.ADD_CART_ITEM_OF_BUY_NOW);//head02+Constants.ADD_CART_ITEM 1,2,3的顺序不能改动


        List<Carts.Cartbean > list= new ArrayList<Carts.Cartbean>();
        Carts.Cartbean cart= new Carts.Cartbean(ShopId, ProductId, GroupSellID, Quantity,districtID, AttributeIdList);
        list.add(cart);
        Gson gson = new Gson();
        String strEntity = gson.toJson(list);
        Log.d("ceshi01", strEntity);
        /*这个地方我是什么时候改了的*/
        RetrofitServiceGenerator.getApi().addCartItemOfBuyNow(head01
                , head02
                , head03
                , userId, token, strEntity).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }
            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    /*
    * 用户无关
    *
    * */
    @Override
    public void getShopwithoutId(long shopId,final ActionCallbackDoubleListener<ShopBean,List<SortDataBean>>
            listener) {
        Log.d("ShoppingServiceFragment", "被执行");
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);
        String head04;
        if (isLogin){
            long userId =complexPreferences.getLong(Constants.USERID, 0l);
            head04 = String.valueOf(userId);
        }else {
            head04 =null;
        }



        String head01 = HeaderUtil.getHeadindexNotUser1(Constants.GET_SHOP);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02 = HeaderUtil.getHeadindex2(time);
        String head03 = HeaderUtil.getHeadindex3(Constants.GET_SHOP);
        Log.d("ShoppingServiceFragment", "shopId:,里面" + shopId);  // 这个地方呗执行了两次
        RetrofitServiceGenerator.getApi().getShopwithoutId(head01
                , head02
                , head03
                , head04, shopId).enqueue(new Callback<ShopResponseBean>() {
            @Override
            public void onResponse(Call<ShopResponseBean> call, Response<ShopResponseBean> response) {
                //shopBean
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        Log.d(TAG, "有数据");
                        ShopResponseBean.DataBean resouce = response.body().getData();
                        List<ShopResponseBean.DataBean.HotProductBean> list = resouce.getHotProductList();
                        //存放热门服务列表
                        SortDataBean sortDataBean = null;
                        List<SortDataBean> relist = new ArrayList<SortDataBean>();
                        for (int i = 0; i < list.size(); i++) {
                            long ProductID = list.get(i).getProductID();
                            String District = resouce.getDistrict();
                            String ImagePath = list.get(i).getImagePath();
                            String ProductName = list.get(i).getProductName();
                            int OrderNum = list.get(i).getOrderNum();
                            double MinPrice = list.get(i).getMinPrice();
                            sortDataBean = new SortDataBean(ProductID, "", District, ImagePath, ProductName,
                                    OrderNum, MinPrice);
                            relist.add(sortDataBean);
                        }
                        long ShopID = resouce.getShopID();
                        PreferenceUtil complexPreferences01 = PreferenceUtil.getSharedPreference(context,
                                "shop");
                        complexPreferences01.putListObject("shopid" + ShopID + "HPList", relist);
                        String ShopName = resouce.getShopName();
                        String ShopLogo = resouce.getShopLogo();
                        int OrderNum = resouce.getOrdersNum();
                        String MainService = resouce.getMainService();  //主营服务
                        String District = resouce.getDistrict();  //地区名称
                        boolean IsFav = resouce.isIsFav();

                        ShopBean shopBean;
                        //如果包含,分两种情况，一种情况是部分字段更新，还有一种情况是全部属性更新
                        if (complexPreferences01.contains("shopid" + ShopID)) {
                            shopBean = complexPreferences01.getObject("shopid" + ShopID, ShopBean.class);
                            shopBean.setShopName(ShopName);
                            shopBean.setShopLogo(ShopLogo);
                            shopBean.setOrderNum(OrderNum);
                            shopBean.setMainService(MainService);
                            shopBean.setDistrict(District);
                            shopBean.setIsFav(IsFav);
                            complexPreferences01.putObject("shopid" + ShopID, shopBean);
                        } else {
                            shopBean = new ShopBean(ShopID, ShopName, ShopLogo, OrderNum, MainService, District,
                                    IsFav, null, -1);
                            complexPreferences01.putObject("shopid" + ShopID, shopBean);
                        }
                        listener.onSuccess(shopBean, relist);
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ShopResponseBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void getShopProductListByNormalSort(final long shopId, int currentPage, int PageSize, final ActionCallbackTripleListener<List<_ShopRelatedServiceBean>,String,String>
            listener) {
        if (currentPage < 0){
            listener.onFailure(ErrorEvent.CODE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU2);
        }
        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_SHOP_PRODUCT_LIST_BY_NORMALSORT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_SHOP_PRODUCT_LIST_BY_NORMALSORT);

        RetrofitServiceGenerator.getApi().getShopProductListByNormalSort(head01
                , head02
                , head03
                , shopId, currentPage, PageSize).enqueue(new Callback<ShopRelatedServiceBean>() {
            @Override
            public void onResponse(Call<ShopRelatedServiceBean> call, Response<ShopRelatedServiceBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        List<ShopRelatedServiceBean.DataBean.ListBean> list = response.body().getData().getList();
                        _ShopRelatedServiceBean sortDataBean = null;
                        List<_ShopRelatedServiceBean> relist = new ArrayList<_ShopRelatedServiceBean>();
                        for (int i = 0; i < list.size(); i++) {
                            long ProductID = list.get(i).getProductID();
                            String City = list.get(i).getCity();
                            String District = list.get(i).getDistrict();
                            String ImagePath = list.get(i).getImagePath();
                            String ProductName = list.get(i).getProductName();
                            int OrderNum = list.get(i).getOrderNum();
                            double MinPrice = list.get(i).getMinPrice();
                            int IsHot = list.get(i).getIsHot();
                            String Province = list.get(i).getProvince();
                            /*
                            *   @文件名：shop
                            *   @存放内容: 每个店铺的相关消息 以及 店铺下的服务 【shopId+服务id】
                            *   只存储当前shop用户看见的最新页
                            * */
                            sortDataBean =new _ShopRelatedServiceBean(shopId, ProductID, ProductName, MinPrice, ImagePath, OrderNum, IsHot, Province, City, District);

                            relist.add(sortDataBean);
                        }

                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "shop");
                        complexPreferences.putListObject("shopId"+shopId+"newest_Page", relist);
                        listener.onSuccess(relist,response.body().getData().getPageIndex(),response.body().getData().getTotalCount());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }

            @Override
            public void onFailure(Call<ShopRelatedServiceBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void getShopProductListByHotSort(final long shopId, int currentPage, int PageSize, final ActionCallbackListener<List<_ShopRelatedServiceBean>> listener) {
        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_SHOP_PRODUCT_LIST_BY_HOTSORT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_SHOP_PRODUCT_LIST_BY_HOTSORT);

        RetrofitServiceGenerator.getApi().getShopProductListByHotSort(head01
                , head02
                , head03
                , shopId, currentPage, PageSize).enqueue(new Callback<ShopRelatedServiceBean>() {
            @Override
            public void onResponse(Call<ShopRelatedServiceBean> call, Response<ShopRelatedServiceBean> response) {
                //ShopSearchingNormalBean
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        Log.d(TAG, "有数据");
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "shop");
                        List<ShopRelatedServiceBean.DataBean.ListBean> list = response.body().getData().getList();// 几页数据就意味着几个对象，但无论多少条数据都是要装进同一个对象中，这个同一个对象就是view层
                        List<_ShopRelatedServiceBean> _list = new ArrayList<_ShopRelatedServiceBean>();
                        for (int i=0;i<list.size();i++){
                             long productID = list.get(i).getProductID();
                             String productName = list.get(i).getProductName();
                             double minPrice = list.get(i).getMinPrice();
                             String imagePath = list.get(i).getImagePath();
                             int orderNum = list.get(i).getOrderNum();
                             int isHot = list.get(i).getIsHot();
                             String province = list.get(i).getProvince();
                             String city = list.get(i).getCity();
                             String district = list.get(i).getDistrict();
                             _list.add(new _ShopRelatedServiceBean(shopId, productID,  productName,  minPrice,  imagePath,
                             orderNum,  isHot,  province,  city,  district) );
                        }
                        complexPreferences.putListObject("current_page", _list);
                        complexPreferences.putListObject(response.body().getData().getPageIndex(), _list);// 标识着一页数据remove
                        listener.onSuccess(_list);
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }

            @Override
            public void onFailure(Call<ShopRelatedServiceBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                //listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);

            }
        });

    }

    @Override
    public void getShopActivity(final long shopId, final ActionCallbackListener<List<ShopAcBean>> listener) {
        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_SHOP_ACTIVITY);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_SHOP_ACTIVITY);

        RetrofitServiceGenerator.getApi().getShopActivity(head01
                , head02
                , head03
                , shopId).enqueue(new Callback<ShopActivityBean>() {
            @Override
            public void onResponse(Call<ShopActivityBean> call, Response<ShopActivityBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        List<ShopActivityBean.DataBean> list= response.body().getData();
                        ShopAcBean shopAcBean = null;
                        List<ShopAcBean> relist = new ArrayList<ShopAcBean>();
                        for (int i = 0; i < list.size(); i++){

                             int Flag = list.get(i).getFlag();
                             long Id= list.get(i).getId();
                             String Title= list.get(i).getTitle();
                             String Desc= list.get(i).getDesc();
                             String StartTime= list.get(i).getStartTime();
                             String EndTime= list.get(i).getEndTime();
                             String CreateTime= list.get(i).getCreateTime();
                             List<ShopActivityBean.DataBean.RuleBean> rulist =  list.get(i).getRules();
                             StringBuilder sb = new StringBuilder();
                             String formatString ="单笔订单满%1$s元减%2$s元\n";
                             for (int j = 0; j< rulist.size(); j++){
                                 /*
                                 * 字符串优化：http://zhidao.baidu.com/link?url=KVH5La5JpU-Uc49sK1IXxPjK-lLQr33fEZwthBOdtcyypVCCwVAH4KrDb47sn_Xu8IpCNcdgUjhiAKcRfq1wp_
                                 * 1.如果要操作少量的数据用 = String
                                   2.单线程操作字符串缓冲区 下操作大量数据 = StringBuilder
                                   3.多线程操作字符串缓冲区 下操作大量数据 = StringBuffer
                                   http://www.jb51.net/article/37871.htm
                                   以格式化的方法添加
                                 * */
                                  String Price = rulist.get(j).getPrice();
                                  String Money = rulist.get(j).getMoney();
                                 sb.append(String.format(formatString,Price,Money));
                             }
                             String Content = sb.toString();
                             shopAcBean = new ShopAcBean(Flag, Id, Title,Desc,StartTime, EndTime, CreateTime,Content);
                             relist.add(shopAcBean);
                        }
                        PreferenceUtil complexPreferences01 = PreferenceUtil.getSharedPreference(context, "shop");
                        complexPreferences01.putListObject("shopid" + shopId + "ShopActivity", relist);
                        Log.d(TAG, "有数据");
                        listener.onSuccess(relist);
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ShopActivityBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                //listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);

            }
        });

    }

    @Override
    public void getShopVouchersList(final long shopId, final ActionCallbackListener<List<VoucherBean>> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);

        String head04;
        if (isLogin){
            long userId =complexPreferences.getLong(Constants.USERID, 0l);
            head04 = String.valueOf(userId);
        }else {
            head04 =null;
        }


        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_SHOP_VOUCHERS_LIST);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_SHOP_VOUCHERS_LIST);

        RetrofitServiceGenerator.getApi().getShopVouchersList(head01
                , head02
                , head03
                ,head04, shopId).enqueue(new Callback<VoucherListResponse>() {
            @Override
            public void onResponse(Call<VoucherListResponse> call, Response<VoucherListResponse> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        Log.d(TAG, "有数据");
                        List<VoucherListResponse.DataBean> list= response.body().getData();
                        VoucherBean voucherBean = null;
                        List<VoucherBean> relist = new ArrayList<VoucherBean>();
                        for (int i = 0; i < list.size(); i++){
                             long VouchersTemplateID = list.get(i).getVouchersTemplateID();
                             String Title = list.get(i).getTitle();
                             String VouchersDes = list.get(i).getVouchersDes();
                             String StartTime = list.get(i).getStartTime();
                             String EndTime = list.get(i).getEndTime();
                             String Price = list.get(i).getPrice();
                             String Image = list.get(i).getImage();
                             boolean IsExist  = list.get(i).isExist();
                             int EachLimit = list.get(i).getEachLimit();
                            voucherBean = new VoucherBean(VouchersTemplateID, Title, VouchersDes, StartTime, EndTime,
                                    Price, Image,IsExist,EachLimit);
                            relist.add(voucherBean);
                        }
                        PreferenceUtil complexPreferences01 = PreferenceUtil.getSharedPreference(context, "shop");
                        complexPreferences01.putListObject("shopid" + shopId + "ShopVoucher", relist);
                        Log.d(TAG, "有数据");
                        listener.onSuccess(relist);
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<VoucherListResponse> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);

            }
        });

    }

    @Override
    public void searchProductByKeyword(String keyword, int currentPage, int PageSize,int sortType,boolean sortMode, final ActionCallbackTripleListener<List<SortDataBean>,String,String>
            listener) {
        if (currentPage < 0){
            listener.onFailure(ErrorEvent.CODE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU2);
        }



/*        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        long time_to_time = complexPreferences.getLong(Constants.TIME_TO_TIME, 0l);
        String head01=EncryptUtil.makeMD5(Constants.SEARCH_PRODUCT_BY_KEYWORD + "hby.mobile.client");
        String head02 = Long.toString((System.currentTimeMillis() + time_to_time) / 1000L);
        String head03= EncryptUtil.makeMD5(head02 + Constants.SEARCH_PRODUCT_BY_KEYWORD + "hby.mobile.client");*/


        String head01=HeaderUtil.getHeadindexNotUser1(Constants.SEARCH_PRODUCT_BY_KEYWORD);
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SEARCH_PRODUCT_BY_KEYWORD);


        RetrofitServiceGenerator.getApi().searchProductByKeyword(head01
                , head02
                , head03
                , keyword, currentPage, PageSize,sortType,sortMode).enqueue(new Callback<GeneralSortDataBean>() {
            @Override
            public void onResponse(Call<GeneralSortDataBean> call, Response<GeneralSortDataBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        List<GeneralSortDataBean.DataBean.ListBean> list = response.body().getData().getList();
                        SortDataBean sortDataBean = null;
                        List<SortDataBean> relist = new ArrayList<SortDataBean>();
                        for (int i = 0; i < list.size(); i++) {
                            long ProductID = list.get(i).getProductID();
                            String City = list.get(i).getCity();
                            String District = list.get(i).getDistrict();
                            String ImagePath = list.get(i).getImagePath();
                            String ProductName = list.get(i).getProductName();
                            int OrderNum = list.get(i).getOrderNum();
                            double MinPrice = list.get(i).getMinPrice();
                            sortDataBean =new SortDataBean(ProductID,City, District, ImagePath, ProductName, OrderNum, MinPrice);
                            relist.add(sortDataBean);
                        }
                        listener.onSuccess(relist, response.body().getData().getPageIndex(), response.body().getData().getTotalCount());
                    } else {
                        Log.d(TAG, "没有数据");//其实在这里都是需要添加逻辑的
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }

            @Override
            public void onFailure(Call<GeneralSortDataBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void searchShopByKeyword(String keyword, int currentPage, int PageSize, final ActionCallbackTripleListener<List<ShopBean>,String,String>
            listener) {
        if (currentPage < 0){
            listener.onFailure(ErrorEvent.CODE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU2);
        }


/*        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        long time_to_time = complexPreferences.getLong(Constants.TIME_TO_TIME, 0l);
        String head01=EncryptUtil.makeMD5(Constants.SEARCH_SHOP_BY_KEYWORD + "hby.mobile.client");
        String head02 = Long.toString((System.currentTimeMillis() + time_to_time) / 1000L);
        String head03= EncryptUtil.makeMD5(head02 + Constants.SEARCH_SHOP_BY_KEYWORD + "hby.mobile.client");*/

        String head01=HeaderUtil.getHeadindexNotUser1(Constants.SEARCH_SHOP_BY_KEYWORD);
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SEARCH_SHOP_BY_KEYWORD);


        RetrofitServiceGenerator.getApi().searchShopByKeyword(head01
                , head02
                , head03
                , keyword, currentPage, PageSize).enqueue(new Callback<ShopRelatedServiceBean>() {
            @Override
            public void onResponse(Call<ShopRelatedServiceBean> call, Response<ShopRelatedServiceBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        Log.d(TAG, "有数据");
                        //搜索暂时不考虑缓存
                        List<ShopRelatedServiceBean.DataBean.ListBean> list = response.body().getData().getList();
                        List<ShopBean> relist = new ArrayList<ShopBean>();
                        for (int i=0;i<list.size();i++){
                             long ShopID  = list.get(i).getShopID();
                             String ShopName  = list.get(i).getShopName();
                             String ShopLogo  = list.get(i).getShopLogo();
                             int OrderNum  = list.get(i).getOrderNum();
                             String MainService  = list.get(i).getMainService();  //主营服务
                             String District  = list.get(i).getDistrict();
                             double ComprehensiveScore  = list.get(i).getComprehensiveScore();
                             relist.add(new ShopBean(ShopID,ShopName, ShopLogo, OrderNum,MainService, District,ComprehensiveScore));
                        }

                        listener.onSuccess(relist,response.body().getData().getPageIndex(), response.body().getData().getTotalCount());


                    } else {
/*                        PreferenceUtil complexPreferencesPre = PreferenceUtil.getSharedPreference(context, "preferences");
                        long time_to_time = System.currentTimeMillis()-Long.parseLong(response.headers().get
                                ("OkHttp-Received-Millis"));
                        complexPreferencesPre.putLong(Constants.TIME_TO_TIME,time_to_time);*/

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }


            }

            @Override
            public void onFailure(Call<ShopRelatedServiceBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void getShopListByNormalSort(int currentPage, int PageSize, final ActionCallbackTripleListener<List<ShopBean>,
                String, String> listener) {
        if (currentPage < 0){
            listener.onFailure(ErrorEvent.CODE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU2);
        }


/*        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        long time_to_time = complexPreferences.getLong(Constants.TIME_TO_TIME, 0l);
        String head01=EncryptUtil.makeMD5(Constants.SEARCH_SHOP_BY_KEYWORD + "hby.mobile.client");
        String head02 = Long.toString((System.currentTimeMillis() + time_to_time) / 1000L);
        String head03= EncryptUtil.makeMD5(head02 + Constants.SEARCH_SHOP_BY_KEYWORD + "hby.mobile.client");*/

        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_SHOP_LIST_BY_NORMALSORT);
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_SHOP_LIST_BY_NORMALSORT);


        RetrofitServiceGenerator.getApi().getShopListByNormalSort(head01
                , head02
                , head03, currentPage, PageSize).enqueue(new Callback<ShopRelatedServiceBean>() {
            @Override
            public void onResponse(Call<ShopRelatedServiceBean> call, Response<ShopRelatedServiceBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        Log.d(TAG, "有数据");
                        //搜索暂时不考虑缓存
                        List<ShopRelatedServiceBean.DataBean.ListBean> list = response.body().getData().getList();
                        List<ShopBean> relist = new ArrayList<ShopBean>();
                        for (int i=0;i<list.size();i++){
                            long ShopID  = list.get(i).getShopID();
                            String ShopName  = list.get(i).getShopName();
                            String ShopLogo  = list.get(i).getShopLogo();
                            int OrderNum  = list.get(i).getOrderNum();
                            String MainService  = list.get(i).getMainService();  //主营服务
                            String District  = list.get(i).getDistrict();
                            double ComprehensiveScore  = list.get(i).getComprehensiveScore();
                            relist.add(new ShopBean(ShopID,ShopName, ShopLogo, OrderNum,MainService, District,ComprehensiveScore));
                        }

                        listener.onSuccess(relist,response.body().getData().getPageIndex(), response.body().getData().getTotalCount());


                    } else {
/*                        PreferenceUtil complexPreferencesPre = PreferenceUtil.getSharedPreference(context, "preferences");
                        long time_to_time = System.currentTimeMillis()-Long.parseLong(response.headers().get
                                ("OkHttp-Received-Millis"));
                        complexPreferencesPre.putLong(Constants.TIME_TO_TIME,time_to_time);*/

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }
            }

            @Override
            public void onFailure(Call<ShopRelatedServiceBean> call, Throwable t) {
                // 这个地方压根就没有执行
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void getShopListByHotSort(int currentPage, int PageSize, final ActionCallbackTripleListener<List<ShopBean>, String, String> listener) {
        if (currentPage < 0){
            listener.onFailure(ErrorEvent.CODE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU2);
        }



        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_SHOP_LIST_BY_HOTSORT);
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_SHOP_LIST_BY_HOTSORT);

        RetrofitServiceGenerator.getApi().getShopListByHotSort(head01
                , head02
                , head03
                , currentPage, PageSize).enqueue(new Callback<ShopRelatedServiceBean>() {
            @Override
            public void onResponse(Call<ShopRelatedServiceBean> call, Response<ShopRelatedServiceBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        Log.d(TAG, "有数据");
                        //搜索暂时不考虑缓存
                        List<ShopRelatedServiceBean.DataBean.ListBean> list = response.body().getData().getList();
                        List<ShopBean> relist = new ArrayList<ShopBean>();
                        for (int i=0;i<list.size();i++){
                            long ShopID  = list.get(i).getShopID();
                            String ShopName  = list.get(i).getShopName();
                            String ShopLogo  = list.get(i).getShopLogo();
                            int OrderNum  = list.get(i).getOrderNum();
                            String MainService  = list.get(i).getMainService();  //主营服务
                            String District  = list.get(i).getDistrict();
                            double ComprehensiveScore  = list.get(i).getComprehensiveScore();
                            relist.add(new ShopBean(ShopID,ShopName, ShopLogo, OrderNum,MainService, District,ComprehensiveScore));
                        }
                        listener.onSuccess(relist,response.body().getData().getPageIndex(), response.body().getData().getTotalCount());


                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }


            }

            @Override
            public void onFailure(Call<ShopRelatedServiceBean> call, Throwable t) {

            }
        });

    }

    @Override
    public void getOrder(long orderId, final ActionCallbackListener<OrderBean> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_ORDER);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_ORDER);

        RetrofitServiceGenerator.getApi().getOrder(head01
                , head02
                , head03
                , userId, token, orderId).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                //什么都没有吧映射

                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        OrderResponse.DataBean souceBean =response.body().getData();
                         int Status  = souceBean.getStatus();
                         String ContactName  = souceBean.getContactName();
                         String ContactDistrict = souceBean.getContactDistrict();
                         String ContactAddress = souceBean.getContactAddress();
                         String ContactPhone = souceBean.getContactPhone();
                         String ContactMobile = souceBean.getContactMobile();
                         long ShopID = souceBean.getShopID();
                         String BuyerMsg = souceBean.getBuyerMsg();
                         String ShopName = souceBean.getShopName();
                         String ShopLogo = souceBean.getShopLogo();
                         String Price= souceBean.getPrice();
                         int PayMode= souceBean.getPayMode();
                         int InvoiceType= souceBean.getInvoiceType();
                         String CreateDate = souceBean.getCreateDate();


                         OrderResponse.DataBean.InvoiceInfoBean  invoiceInfoBean= souceBean.getInvoiceInfo();
                         String Title = invoiceInfoBean.getTitle();
                         String Content = invoiceInfoBean.getContent();

                         //暂时只显示一个服务
                         List<OrderResponse.DataBean.OrderExtendsBean> orderExtendsBeanList = souceBean.getOrderExtends();
                         OrderBean bean=null;
                         for (int i=0;i<orderExtendsBeanList.size();i++){
                             OrderResponse.DataBean.OrderExtendsBean orderExtendsBean = orderExtendsBeanList.get(i);
                             long OrderExtendID = orderExtendsBean.getOrderExtendID() ;
                             long ProductID= orderExtendsBean.getProductID();
                             String ProductName= orderExtendsBean.getProductName();
                             String ProductImage= orderExtendsBean.getProductImage();
                             String ProductPrice= orderExtendsBean.getProductPrice();
                             String Quantity= orderExtendsBean.getQuantity();
                             bean= new OrderBean( Status, ContactName,ContactDistrict,ContactAddress,ContactPhone,ContactMobile, BuyerMsg,ShopID,ShopName,ShopLogo,OrderExtendID,
                             ProductID, ProductName, ProductImage, ProductPrice,Quantity, Price, PayMode, InvoiceType, Title, Content, CreateDate);
                         }
                        listener.onSuccess(bean);


                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });



    }

    @Override
    public void confirmOrder(long orderId, final ActionCallbackListener<ResponseCode> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.CONFIRM_ORDER);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.CONFIRM_ORDER);
        RetrofitServiceGenerator.getApi().confirmOrder(head01
                , head02
                , head03
                , userId, token, orderId).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {

                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());
                    } else {
                    Log.d(TAG, "没有数据");
                    listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response
                            .body().getCode(), response.body().getMessage()));
                }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void closeOrder(long orderId, final ActionCallbackListener<ResponseCode> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.CLOSE_ORDER);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.CLOSE_ORDER);
        RetrofitServiceGenerator.getApi().closeOrder(head01
                , head02
                , head03
                , userId, token, orderId).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {

                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    @Override
    public void getShoppingCart(final ActionCallbackFivefoldListener <List<Map<String,Object>> , List<List<Map<String, Object>>>,String,String,Boolean>  listener) {



        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_SHOPPING_CART);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_SHOPPING_CART);


        RetrofitServiceGenerator.getApi().getShoppingCart(head01
                , head02
                , head03
                , userId, token).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {

                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {


                        //以下两项就是我们想要上传给view层的
                        List<List<Map<String, Object>>> childMapList_list = new ArrayList<List<Map<String, Object>>>();
                        List<Map<String, Object>> parentMapList = new ArrayList<Map<String, Object>>();
                        ShoppingCartBean.DataBean dataBean = response.body().getData();
                        String totalPrice = dataBean.getTotalPrice();
                        String totalCount = dataBean.getTotalCount();
                        boolean isAllChecked = true;

                        List<ShoppingCartBean.DataBean.ListBean> list = dataBean.getList();
                        for (int i = 0; i < list.size(); i++) {
                            Map<String, Object> mappedParentItemBean = new HashMap<String, Object>();
                            //父bean
                            Long shopID = list.get(i).getShopID();
                            String shopName = list.get(i).getShopName();
                            String shopLogo = list.get(i).getShopLogo();
                            String OrderTotalPrice = list.get(i).getOrderTotalPrice();//单个店铺类的总额
                            String ProductCount = list.get(i).getProductCount() ;
                            boolean isParentChecked = true;


                            List<ShoppingCartBean.DataBean.ListBean.GroupSellProductsBean> groupProductsBeanList =
                                    list.get(i).getGroupSellProducts();
                            List<ShoppingCartBean.DataBean.ListBean.SingleProductsBean> singleProductsBeanList = list
                                    .get(i).getSingleProducts();
                            List<Map<String, Object>> childMapList = new ArrayList<Map<String, Object>>();
                            List<Map<String, Object>> GroupchildMapList = new ArrayList<Map<String, Object>>();
                            List<Map<String, Object>> SinglechildMapList = new ArrayList<Map<String, Object>>();

                            for (int j = 0; j < groupProductsBeanList.size(); j++) {
                                List<ShoppingCartBean.DataBean.ListBean.GroupSellProductsBean.ProductsBean>
                                        productsBeanList = groupProductsBeanList.get(j).getProducts();

                                /*
                                * 这里还有四个属性，暂时不知道怎么用？
                                * 套餐总体相关
                                *       “GroupSellID”:1,
                                        “GroupSellPrice”:101,						// 套餐总价格
                                        “GroupSellName”:”套餐名称”,					// 套餐总名称
                                        “IsSelected”: true,						// 是否选中【套餐是否被选中】  这个字段可要可不要，其实是一致的，如果出现不一致的
                                        情况，那就是后台服务器的问题了，所以不必纠结的
                                * */

                                for (int k = 0; k < productsBeanList.size(); k++) {
                                    Map<String, Object> mappedChildItemBean = new HashMap<String, Object>();
                                    long productID = productsBeanList.get(k).getProductID();
                                    String productName = productsBeanList.get(k).getProductName();
                                    String productImage = productsBeanList.get(k).getProductImage();
                                    double productPrice = productsBeanList.get(k).getProductPrice();
                                    double discountMoney = productsBeanList.get(k).getDiscountMoney();
                                    int quantity = productsBeanList.get(k).getQuantity();
                                    boolean isSelected = productsBeanList.get(k).isIsSelected();
                                    if (!isSelected){
                                        isParentChecked = false;
                                    }
                                    long groupSellID = productsBeanList.get(k).getGroupSellID();
                                    int type= -1; //表示非套餐
                                    if(k==0){
                                        type =0;//    public static final int TYPE_START = 0;
                                    }else if (k== productsBeanList.size()-1){
                                        type =3;//   public static final int TYPE_MIDDLE = 1;
                                    }else {
                                        type =1;//   public static final int TYPE_END = 3;
                                    }
                                    //子套餐有是个什么场景，这个也得看情况，不过先按照一般的情况处理才是
                                    List<ShoppingCartBean.DataBean.ListBean.GroupSellProductsBean.ProductsBean.ShopAttrListBean>
                                            shopAttrList =productsBeanList.get(k).getShopAttrList();
                                    List<ShoppingCartChildBean.ShopAttrListBean> reshopAttrList = new ArrayList<ShoppingCartChildBean.ShopAttrListBean>();

                                    for (int l=0;l<shopAttrList.size();l++){
                                         long ShopAttrID = shopAttrList.get(l).getShopAttrID();
                                         String AttrName = shopAttrList.get(l).getAttrName();
                                         double Price = shopAttrList.get(l).getPrice();
                                         boolean IsSelected = shopAttrList.get(l).isSelected();
                                         reshopAttrList.add(new ShoppingCartChildBean.ShopAttrListBean(ShopAttrID, AttrName, Price, IsSelected));

                                    }

                                    ShoppingCartChildBean childBean = new ShoppingCartChildBean(productID,
                                            productName, productImage, productPrice,
                                            discountMoney, quantity, isSelected, groupSellID, STATUS_VALID, false,type,reshopAttrList);
                                    mappedChildItemBean.put("childName", childBean);
                                    GroupchildMapList.add(mappedChildItemBean);
                                }

                            }
                            //同一家店的，无论是套餐还是非套餐都是要装进来的
                            childMapList.addAll(GroupchildMapList);


                            for (int j = 0; j < singleProductsBeanList.size(); j++) {
                                Map<String, Object> mappedChildItemBean = new HashMap<String, Object>();
                                Long productID = singleProductsBeanList.get(j).getProductID();
                                String productName = singleProductsBeanList.get(j).getProductName();
                                String productImage = singleProductsBeanList.get(j).getProductImage();
                                double productPrice = singleProductsBeanList.get(j).getProductPrice();
                                double discountMoney = singleProductsBeanList.get(j).getDiscountMoney();
                                int quantity = singleProductsBeanList.get(j).getQuantity();
                                boolean isSelected = singleProductsBeanList.get(j).isIsSelected();
                                if (!isSelected){
                                    isParentChecked = false;
                                }
                                long groupSellID = singleProductsBeanList.get(j).getGroupSellID();


                                List<ShoppingCartBean.DataBean.ListBean.SingleProductsBean.ShopAttrListBean>
                                        shopAttrList =singleProductsBeanList.get(j).getShopAttrList();
                                List<ShoppingCartChildBean.ShopAttrListBean> reshopAttrList = new ArrayList<ShoppingCartChildBean.ShopAttrListBean>();

                                for (int l=0;l<shopAttrList.size();l++){
                                    long ShopAttrID = shopAttrList.get(l).getShopAttrID();
                                    String AttrName = shopAttrList.get(l).getAttrName();
                                    double Price = shopAttrList.get(l).getPrice();
                                    boolean IsSelected = shopAttrList.get(l).isSelected();
                                    reshopAttrList.add(new ShoppingCartChildBean.ShopAttrListBean(ShopAttrID, AttrName, Price, IsSelected));

                                }

                                ShoppingCartChildBean childBean = new ShoppingCartChildBean(productID, productName,
                                        productImage,
                                        productPrice, discountMoney, quantity, isSelected, groupSellID, STATUS_VALID,
                                        false,-1,reshopAttrList);  //不同的对象和一个对象的差别

                                mappedChildItemBean.put("childName", childBean);
                                SinglechildMapList.add(mappedChildItemBean);
                            }

                            childMapList.addAll(SinglechildMapList);

                            childMapList_list.add(childMapList);


                            ShoppingCartParentBean parentBean = new ShoppingCartParentBean(shopID, shopName,
                                    shopLogo, OrderTotalPrice,ProductCount,isParentChecked, false); //这个true 需要变更下
                            mappedParentItemBean.put("parentName", parentBean); //就设置为单价就好了，并且不变化
                            parentMapList.add(mappedParentItemBean);
                            if (!isParentChecked){
                                isAllChecked = false;

                            }

                        }
                        listener.onSuccess(parentMapList, childMapList_list, totalPrice, totalCount,isAllChecked);
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }

            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void receiveRedEnvelope(long tempId, final ActionCallbackListener<ResponseCode> listener) {
        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.RECEIVE_RED_ENVELOPE);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.RECEIVE_RED_ENVELOPE);

        RetrofitServiceGenerator.getApi().receiveRedEnvelope(head01
                , head02
                , head03
                , userId, token, tempId).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {

                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());


                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }
                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);

                }
            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }

        });


    }

    @Override
    public void getContactList(final ActionCallbackListener<List<ContactBean>> listener) {


        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_CONTACT_LIST);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_CONTACT_LIST);


        RetrofitServiceGenerator.getApi().getContactList(head01
                , head02
                , head03
                , userId, token).enqueue(new Callback<ContactListResponse>() {
            @Override
            public void onResponse(Call<ContactListResponse> call, Response<ContactListResponse> response) {

                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                         List<ContactListResponse.DataBean> list= response.body().getData();
                         List<ContactBean> relist = new ArrayList<ContactBean>();
                         for (int i=0;i<list.size();i++){
                              ContactListResponse.DataBean bean = list.get(i);
                              long ContactManageID = bean.getContactManageID();
                              long UserID = bean.getUserID();
                              long ProvinceID = bean.getProvinceID();
                              long CityID = bean.getCityID();
                              long DistrictID = bean.getDistrictID();
                              String ContactName = bean.getContactName();
                              int Sex = bean.getSex();
                              String ProvinceName = bean.getProvinceName();
                              String CityName = bean.getCityName();
                              String DistrictName = bean.getDistrictName();
                              String ContactAddress = bean.getContactAddress();
                              String ContactPhone = bean.getContactPhone();
                              String ContactMobile = bean.getContactMobile();
                              String ContactEmail = bean.getContactEmail();
                              int IsDefault = bean.getIsDefault();
                              relist.add(new ContactBean(ContactManageID, UserID,ProvinceID,CityID,DistrictID,ContactName, Sex,
                                      ProvinceName, CityName,DistrictName,ContactAddress, ContactPhone, ContactMobile, ContactEmail, IsDefault));
                         }
                        listener.onSuccess(relist);


                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }
                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);

                }
            }

            @Override
            public void onFailure(Call<ContactListResponse> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }

        });



    }

    @Override
    public void delContact(long contactManagerId , final ActionCallbackListener<ResponseCode> listener) {


        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.DEL_CONTACT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.DEL_CONTACT);

        RetrofitServiceGenerator.getApi().delContact(head01
                , head02
                , head03, userId, token, contactManagerId).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {

                        listener.onSuccess(response.body());


                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }
                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);

                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void setDefaultContact(long contactManagerId , final ActionCallbackListener<ResponseCode> listener) {

        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.SET_DEFAULT_CONTACT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SET_DEFAULT_CONTACT);

        RetrofitServiceGenerator.getApi().setDefaultContact(head01
                , head02
                , head03, userId, token, contactManagerId).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {

                        listener.onSuccess(response.body());


                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }
                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);

                }
            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }


    @Override
    public void getVerifyShoppingCart(final ActionCallbackSixfoldListener <List<Map<String,Object>> , List<List<Map<String, Object>>>,ContactBean,List<RedEnvelopeBean>,ShoppingCartBean.DataBean,Orders.OrdersBean> listener) {

        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.GET_VERIFY_SHOPPING_CART);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_VERIFY_SHOPPING_CART);

        RetrofitServiceGenerator.getApi().getVerifyShoppingCart(head01
                , head02
                , head03
                , userId, token).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        //提交订单的参数，没有的字段到app层进行封装变化
                        List<Orders.OrdersBean.ShopsBean> paramsshop =new  ArrayList<Orders.OrdersBean.ShopsBean>();



                        List<List<Map<String, Object>>> childMapList_list = new ArrayList<List<Map<String, Object>>>();
                        List<Map<String, Object>> parentMapList = new ArrayList<Map<String, Object>>();
                        ShoppingCartBean.DataBean dataBean =response.body().getData();

                        List<ContactBean> contactReList= new ArrayList<ContactBean>();
                        List<ShoppingCartBean.DataBean.ContactBean> contactList =dataBean.getContactList();
                        ContactBean defaultbean = null;
                        long ContactManageID = -1;
                        for (int l =0 ;l<contactList.size();l++){
                            ShoppingCartBean.DataBean.ContactBean bean=contactList.get(l);//重新解析了一遍，其实还是为了降低复杂度

                             ContactManageID = bean.getContactManageID();
                             String ContactName = bean.getContactName();
                             String ContactAddress = bean.getContactAddress();
                             String ContactMobile = bean.getContactMobile();
                             String ContactPhone = bean.getContactPhone();
                             int IsDefault  = bean.getIsDefault();
                             if(IsDefault ==1){
                                  defaultbean = new ContactBean( ContactManageID,  ContactName, ContactAddress, ContactMobile,
                                         ContactPhone, IsDefault);
                             }

                             contactReList.add(new ContactBean( ContactManageID,  ContactName, ContactAddress, ContactMobile,
                                     ContactPhone, IsDefault));
                        }

                        List<RedEnvelopeBean> redEnvelopeReList = new ArrayList<RedEnvelopeBean>();
                        List<ShoppingCartBean.DataBean.RedEnvelopeBean> redEnvelopeList =dataBean.getRedEnvelopeList();
                        String  redEnvelopeID = null;
                        // 平台红包，难道可是供选择的？这个得看下，实际上平台红包只有一个没得选【有则显示，默认都是已经选择】
                        for (int m =0;m<redEnvelopeList.size();m++){
                            ShoppingCartBean.DataBean.RedEnvelopeBean bean = redEnvelopeList.get(m);
                             long RedEnvelopeID = bean.getRedEnvelopeID();
                             redEnvelopeID = Long.toString(RedEnvelopeID);
                             String Price = bean.getPrice();
                             boolean IsSelected = bean.isSelected();// 难道这个就是默认已经选择了
                             RedEnvelopeBean rebean = new RedEnvelopeBean(RedEnvelopeID, Price, IsSelected);
                             redEnvelopeReList.add(rebean);
                        }


                        List<ShoppingCartBean.DataBean.ListBean> list =dataBean.getList();
                        for (int i = 0; i < list.size(); i++) {
                            //Map<String, Object> mappedChildItemBean = new HashMap<String, Object>();
                            Map<String , Object> mappedParentItemBean = new HashMap<String, Object>();
                            /*这里面就是一组*/
                            Long shopID = list.get(i).getShopID();
                            String shopName = list.get(i).getShopName();
                            String shopLogo = list.get(i).getShopLogo();
                            /*以下两个字段比较特殊，从功能上是属性parent,但是在具体业务需求上划分为child更好*/
                            ShoppingCartParentBean parentBean = new ShoppingCartParentBean( shopID,  shopName,  shopLogo);
                            Orders.OrdersBean.ShopsBean shopsBean  =new Orders.OrdersBean.ShopsBean(shopID);//is not an encloseing class，需要设置为静态得
                            paramsshop.add(shopsBean);

                            mappedParentItemBean.put("parentBeanName", parentBean);
                            parentMapList.add(mappedParentItemBean);

                            // 以下这些个字段交付给ShoppingCartChildBean ，分别是单个商店的总价，总数 ，优惠活动的两个字段和代金券
                             String OrderTotalPrice = list.get(i).getOrderTotalPrice();
                             String ProductCount = list.get(i).getProductCount();
                             String SatisfySendMoney = list.get(i).getSatisfySendMoney();
                             String SatisfySendPrice = list.get(i).getSatisfySendPrice();

                             List<ShoppingCartBean.DataBean.ListBean.VouchersBean> VouchersList= list.get(i).getVouchersList();
                             List<ShoppingCartChildBean.VouchersBean> reVouchersList = new ArrayList<ShoppingCartChildBean.VouchersBean>();

                             for (int n =0;n<VouchersList.size();n++){
                                  long VouchersID = VouchersList.get(n).getVouchersID();
                                  String Price = VouchersList.get(n).getPrice();
                                  boolean IsSelected = VouchersList.get(n).isSelected();
                                  ShoppingCartChildBean.VouchersBean bean = new ShoppingCartChildBean.VouchersBean(VouchersID, Price,IsSelected);
                                  reVouchersList.add(bean);
                             }





                            List<ShoppingCartBean.DataBean.ListBean.GroupSellProductsBean> groupProductsBeanList = list.get(i).getGroupSellProducts();
                            List<ShoppingCartBean.DataBean.ListBean.SingleProductsBean> singleProductsBeanList = list.get(i).getSingleProducts();
                            List<Map<String, Object>> childMapList = new ArrayList<Map<String, Object>>();
                            List<Map<String, Object>> GroupchildMapList = new ArrayList<Map<String, Object>>();
                            List<Map<String, Object>> SinglechildMapList = new ArrayList<Map<String, Object>>();
                                for (int j=0;j<groupProductsBeanList.size();j++){
                                    List<ShoppingCartBean.DataBean.ListBean.GroupSellProductsBean.ProductsBean> productsBeanList = groupProductsBeanList.get(j).getProducts();
                                    for (int k=0;k<productsBeanList.size();k++) {
                                        Map<String, Object> mappedChildItemBean = new HashMap<String, Object>();
                                        long ProductID =productsBeanList.get(k).getProductID() ;
                                        String productName = productsBeanList.get(k).getProductName();
                                        String productImage = productsBeanList.get(k).getProductImage();
                                        double productPrice = productsBeanList.get(k).getProductPrice();
                                        double discountMoney = productsBeanList.get(k).getDiscountMoney();
                                        int quantity = productsBeanList.get(k).getQuantity();
                                        boolean isSelected = productsBeanList.get(k).isIsSelected();
                                        long groupSellID = productsBeanList.get(k).getGroupSellID();
                                        ShoppingCartChildBean childBean = new ShoppingCartChildBean(ProductID,productName, productImage, productPrice,
                                                discountMoney, quantity, isSelected, groupSellID,OrderTotalPrice,ProductCount,SatisfySendMoney,
                                                SatisfySendPrice,reVouchersList);
                                        mappedChildItemBean.put("childBeanName", childBean);
                                        GroupchildMapList.add(mappedChildItemBean);

                                    }

                                }
                                childMapList.addAll(GroupchildMapList);

                                for (int j=0;j<singleProductsBeanList.size();j++) {
                                    Map<String, Object> mappedChildItemBean = new HashMap<String, Object>();
                                    long productID = singleProductsBeanList.get(j).getProductID();
                                    String productName = singleProductsBeanList.get(j).getProductName();
                                    String productImage = singleProductsBeanList.get(j).getProductImage();
                                    double productPrice = singleProductsBeanList.get(j).getProductPrice();
                                    double discountMoney = singleProductsBeanList.get(j).getDiscountMoney();
                                    int quantity = singleProductsBeanList.get(j).getQuantity();
                                    boolean isSelected = singleProductsBeanList.get(j).isIsSelected();
                                    long groupSellID = singleProductsBeanList.get(j).getGroupSellID();
                                    ShoppingCartChildBean childBean = new ShoppingCartChildBean(productID, productName, productImage,
                                            productPrice, discountMoney, quantity, isSelected, groupSellID,OrderTotalPrice,ProductCount,SatisfySendMoney,
                                            SatisfySendPrice,reVouchersList);  //不同的对象和一个对象的差别
                                    mappedChildItemBean.put("childBeanName", childBean);
                                    SinglechildMapList.add(mappedChildItemBean);
                                }
                                childMapList.addAll(SinglechildMapList);
                            childMapList_list.add(childMapList);

                        }
                        //两个额地方需要修改：一个是发票类型，一个是留言
                        Orders.OrdersBean params = new Orders.OrdersBean(ContactManageID,1,redEnvelopeID,9, paramsshop);
                        listener.onSuccess(parentMapList,childMapList_list,defaultbean,redEnvelopeReList,dataBean,params);

                    } else {
                           listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response.body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void selectCartItem(long productId, final ActionCallbackListener<String> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        Log.d(TAG, "userId:" + userId);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        Log.d(TAG, token);

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId + token + Constants.SELECT_CART_ITEM);
        NTPTime.getInstance().updteNTPTime();
        //long time = NTPTime.getInstance().getCurrentTime();
        //String head02=HeaderUtil.getHeadindex2(time);
        String head02 = Long.toString(NTPTime.getInstance().getCurrentTime()/1000L);
        //String head03=HeaderUtil.getHeadindex3(Constants.SELECT_CART_ITEM);
        String head03= EncryptUtil.makeMD5(head02 + Constants.SELECT_CART_ITEM + "hby.mobile.client");

        RetrofitServiceGenerator.getApi().selectCartItem(head01
                , head02
                , head03
                , userId, token, productId).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {

                        listener.onSuccess(response.body().getData().getTotalPrice());
                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response.body().getCode(), response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }


            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void cancelCartItem(long productId, final ActionCallbackListener<String> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        Log.d(TAG, "userId:" + userId);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        Log.d(TAG, token);

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.CANCEL_CART_ITEM);
        NTPTime.getInstance().updteNTPTime();
/*        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.CANCEL_CART_ITEM);*/
        //long time = NTPTime.getInstance().getCurrentTime();
        //String head02=HeaderUtil.getHeadindex2(time);
        String head02 = Long.toString(NTPTime.getInstance().getCurrentTime() / 1000L);
        //String head03=HeaderUtil.getHeadindex3(Constants.SELECT_CART_ITEM);
        String head03= EncryptUtil.makeMD5(head02 + Constants.CANCEL_CART_ITEM + "hby.mobile.client");



        RetrofitServiceGenerator.getApi().cancelCartItem(head01
                , head02
                , head03
                , userId, token, productId).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {

                        listener.onSuccess(response.body().getData().getTotalPrice());
                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response.body().getCode(), response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题,没有调用成功");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }

        });


    }

    @Override
    public void selectCartGroupItem(long groupSellId, final ActionCallbackListener<String> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.SELECT_CART_GROUP_ITEM);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SELECT_CART_GROUP_ITEM);
        RetrofitServiceGenerator.getApi().selectCartGroupItem(head01
                , head02
                , head03
                , userId, token, groupSellId).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body().getData().getTotalPrice());
                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response.body().getCode(), response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题,没有调用成功");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }


            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void cancelCartGroupItem(long groupSellId, final ActionCallbackListener<String> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.CANCEL_CART_GROUP_ITEM);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.CANCEL_CART_GROUP_ITEM);
        RetrofitServiceGenerator.getApi().cancelCartGroupItem(head01
                , head02
                , head03
                , userId, token, groupSellId).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {

                        listener.onSuccess(response.body().getData().getTotalPrice());
                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response.body().getCode(), response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题,没有调用成功");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }


            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });



    }

    @Override
    public void selectCartShopItem(long shopId, final ActionCallbackListener<String> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        Log.d(TAG, "userId:" + userId);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        Log.d(TAG, token);
        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.SELECT_CART_SHOP_ITEM);
        NTPTime.getInstance().updteNTPTime();
/*
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SELECT_CART_SHOP_ITEM);
*/


        //long time = NTPTime.getInstance().getCurrentTime();
        //String head02=HeaderUtil.getHeadindex2(time);
        String head02 = Long.toString(NTPTime.getInstance().getCurrentTime()/1000L);
        //String head03=HeaderUtil.getHeadindex3(Constants.SELECT_CART_ITEM);
        String head03= EncryptUtil.makeMD5(head02 + Constants.SELECT_CART_SHOP_ITEM + "hby.mobile.client");

        RetrofitServiceGenerator.getApi().selectCartShopItem(head01
                , head02
                , head03
                , userId, token, shopId).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {

                        listener.onSuccess(response.body().getData().getTotalPrice());
                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(),
                                response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void cancelCartShopItem(long shopId, final ActionCallbackListener<String> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        Log.d(TAG, "userId:" + userId);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        Log.d(TAG, token);
        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId + token + Constants.CANCEL_CART_SHOP_ITEM);
        NTPTime.getInstance().updteNTPTime();
/*        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.CANCEL_CART_SHOP_ITEM);*/

        //long time = NTPTime.getInstance().getCurrentTime();
        //String head02=HeaderUtil.getHeadindex2(time);
        String head02 = Long.toString(NTPTime.getInstance().getCurrentTime() / 1000L);
        //String head03=HeaderUtil.getHeadindex3(Constants.SELECT_CART_ITEM);
        String head03= EncryptUtil.makeMD5(head02 + Constants.CANCEL_CART_SHOP_ITEM + "hby.mobile.client");

        RetrofitServiceGenerator.getApi().cancelCartShopItem(head01
                , head02
                , head03
                , userId, token, shopId).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {

                        listener.onSuccess(response.body().getData().getTotalPrice());
                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(),
                                response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }
            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void selectCartAllItem(final ActionCallbackListener<String> listener) {
/*        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.SELECT_CART_ALL_ITEM);
        NTPTime.getInstance().updteNTPTime();

        long time_to_time = complexPreferences.getLong(Constants.TIME_TO_TIME, 0l);
        //String head02 = Long.toString(NTPTime.getInstance().getCurrentTime()/1000L);System.currentTimeMillis()
        String head02 = Long.toString((System.currentTimeMillis()+time_to_time)/1000L);
        //String head03=HeaderUtil.getHeadindex3(Constants.SELECT_CART_ITEM);
        String head03= EncryptUtil.makeMD5(head02 + Constants.SELECT_CART_ALL_ITEM + "hby.mobile.client");*/

        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.SELECT_CART_ALL_ITEM);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SELECT_CART_ALL_ITEM);

        RetrofitServiceGenerator.getApi().selectCartAllItem(head01
                , head02
                , head03
                , userId, token).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {

                        listener.onSuccess(response.body().getData().getTotalPrice());
                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(),
                                response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void cancelCartAllItem(final ActionCallbackListener<String> listener) {

/*        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.CANCEL_CART_ALL_ITEM);
        NTPTime.getInstance().updteNTPTime();
        long time_to_time = complexPreferences.getLong(Constants.TIME_TO_TIME, 0l);
        //String head02 = Long.toString(NTPTime.getInstance().getCurrentTime()/1000L);System.currentTimeMillis()
        String head02 = Long.toString((System.currentTimeMillis()+time_to_time)/1000L);
        //String head03=HeaderUtil.getHeadindex3(Constants.SELECT_CART_ITEM);
        String head03= EncryptUtil.makeMD5(head02 + Constants.CANCEL_CART_ALL_ITEM + "hby.mobile.client");*/




        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.CANCEL_CART_ALL_ITEM);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.CANCEL_CART_ALL_ITEM);


        RetrofitServiceGenerator.getApi().cancelCartAllItem(head01
                , head02
                , head03
                , userId, token).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {

                        listener.onSuccess(response.body().getData().getTotalPrice());
                    } else {

                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(),
                                response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void updateCartQuantity(long productId, int quantity, final ActionCallbackListener<String> listener) {

        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.UPDATE_CART_QUANTITY);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.UPDATE_CART_QUANTITY);
        RetrofitServiceGenerator.getApi().updateCartQuantity(head01
                , head02
                , head03
                , userId, token, productId, quantity).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        listener.onSuccess(response.body().getData().getTotalPrice());
                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void updateCartGroupSellQuantity(long groupSellId, int quantity, final ActionCallbackListener<String> listener) {

        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId + token + Constants.UPDATE_CART_GROUP_SELL_QUANTITY);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.UPDATE_CART_GROUP_SELL_QUANTITY);
        RetrofitServiceGenerator.getApi().updateCartGroupSellQuantity(head01
                , head02
                , head03
                , userId, token, groupSellId, quantity).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        listener.onSuccess(response.body().getData().getTotalPrice());
                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void saveCartFavoriteProduct(List<Carts.Cartbean03 > list, final ActionCallbackListener<String> listener) {

        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        Log.d(TAG, "userId:" + userId);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        Log.d(TAG, token);

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId + token + Constants.SAVE_CART_FAVORITE_PRODUCT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SAVE_CART_FAVORITE_PRODUCT);

/*        List<Carts.Cartbean03 > list= new ArrayList<Carts.Cartbean03>();
        Carts.Cartbean03 cart= new Carts.Cartbean03(shopId, productId, groupSellId);
        list.add(cart);*/
        Gson gson = new Gson();
        String strEntity = gson.toJson(list);
        Log.d("ceshi", strEntity);


        RetrofitServiceGenerator.getApi().saveCartFavoriteProduct(head01
                , head02
                , head03
                , userId, token, strEntity).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {

                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101) {

                        listener.onSuccess(response.body().getData().getTotalPrice());

                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body
                            ().getCode(), response.body().getMessage()));
                }

            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {

                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void submitOrder(String redId,Orders.OrdersBean list, final ActionCallbackListener<List<Integer>> listener) {
        Log.d(TAG, "14");
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        //这里还得判断过期

        String head01=HeaderUtil.getHeadindexNotUser1(userId + token + Constants.SUBMIT_ORDER);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SUBMIT_ORDER);


        Gson gson = new Gson();
        String strEntity = gson.toJson(list);
        Log.d("ceshi", strEntity);

        /*接口压根就没有调用成功*/
        RetrofitServiceGenerator.getApi().submitOrder(head01
                , head02
                , head03
                , userId, token,redId,strEntity).enqueue(new Callback<OrderToPayResponse>() {
            @Override
            public void onResponse(Call<OrderToPayResponse> call, Response<OrderToPayResponse> response) {
                Log.d(TAG, "15");
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        Log.d(TAG, "17");// 这是为什么呢！
                        listener.onSuccess(response.body().getData());

                    } else {
                        Log.d(TAG, "没有数据00");

                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body
                            ().getCode(), response.body().getMessage()));
                }

            }

            @Override
            public void onFailure(Call<OrderToPayResponse> call, Throwable t) {

                Log.d(TAG, "16");
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    @Override
    public void payByCapital(String orderPayId, String payPwd, final ActionCallbackListener<ResponseCode> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        //这里还得判断过期

        String head01=HeaderUtil.getHeadindexNotUser1(userId + token + Constants.SUBMIT_ORDER);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.SUBMIT_ORDER);




        /*接口压根就没有调用成功*/
        RetrofitServiceGenerator.getApi().payByCapital(head01
                , head02
                , head03
                , userId, token,orderPayId,payPwd).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                Log.d(TAG, "15");
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        Log.d(TAG, "17");// 这是为什么呢！
                        listener.onSuccess(response.body());

                    } else {
                        Log.d(TAG, "没有数据00");

                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body
                            ().getCode(), response.body().getMessage()));
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {

                Log.d(TAG, "16");
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });
    }

    @Override
    public void deleteAllCartSelectedItem(List<Carts.Cartbean01 > list01,List<Carts.Cartbean02 > list02, final ActionCallbackListener<String> listener) {
        Gson gson = new Gson();
        String strEntity=null;
        if (list01.isEmpty() && !list02.isEmpty()){
            strEntity = gson.toJson(list02);
        }else if (!list01.isEmpty() && list02.isEmpty()){
            strEntity = gson.toJson(list01);

        }else if (!list01.isEmpty() && !list02.isEmpty()){
            //如果即包含套餐，也包含非套餐 暂时先不考虑

        }
        //我都是直接在外网测试的
        if (null!=strEntity){
           Log.d("strEntity:", strEntity);

        }


/*        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.DELETE_CART_SELECTEDITEM);
        long time_to_time = complexPreferences.getLong(Constants.TIME_TO_TIME, 0l);
        String head02 = Long.toString((System.currentTimeMillis()+time_to_time)/1000L);
        String head03= EncryptUtil.makeMD5(head02 + Constants.DELETE_CART_SELECTEDITEM + "hby.mobile.client");*/


        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.DELETE_CART_SELECTEDITEM);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.DELETE_CART_SELECTEDITEM);




        RetrofitServiceGenerator.getApi().deleteCartSelectedItem(head01
                , head02
                , head03, userId, token, strEntity).enqueue(new Callback<ShoppingCartBean>() {
            @Override
            public void onResponse(Call<ShoppingCartBean> call, Response<ShoppingCartBean> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body().getData().getTotalPrice());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL );
                }
            }

            @Override
            public void onFailure(Call<ShoppingCartBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void deleteFavProduct(long productId, final ActionCallbackListener<ResponseCode> listener) {

        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.DELETE_FAV_PRODUCT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.DELETE_FAV_PRODUCT);
        RetrofitServiceGenerator.getApi().deleteFavProduct(head01
                , head02
                , head03
                , userId, token, productId).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                        .body().getCode(),
                                response.body().getMessage()));                       }

                } else {
                    Log.d(TAG, "服务器问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }

    @Override
    public void favProduct(long productId , final ActionCallbackListener<ResponseCode> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");

        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.FAV_PRODUCT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.FAV_PRODUCT);

        RetrofitServiceGenerator.getApi().favProduct(head01
                , head02
                , head03
                , userId, token, productId).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        listener.onSuccess(response.body());

                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                        .body().getCode(),
                                response.body().getMessage()));                       }

                } else {
                    Log.d(TAG, "服务器问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {

                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }



    @Override
    public void favShop(long shopId, final ActionCallbackListener<ResponseCode> listener) {
        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.FAV_SHOP);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.FAV_SHOP);
        RetrofitServiceGenerator.getApi().favShop(head01
                , head02
                , head03
                , userId, token, shopId).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {

                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void deleteFavShop(long shopId, final ActionCallbackListener<ResponseCode> listener) {

        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");

        //这里还得判断过期
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.DELETE_FAV_SHOP);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.DELETE_FAV_SHOP);
        RetrofitServiceGenerator.getApi().deleteFavShop(head01
                , head02
                , head03
                , userId, token, shopId).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void addContact(Map<String, String> params, final ActionCallbackListener<ResponseCode> listener) {

        /*

        if (TextUtils.isEmpty(params.get("ContactName"))){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "联系人姓名不能为空");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }

        if (TextUtils.isEmpty(params.get("ContactMobile"))){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, ErrorEvent.MESSAGE_NULL_SITU3);  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }

        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(params.get("ContactMobile"));
        if (!matcher.matches()) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_ILLEGAL, "手机号格式不正确！");
            }
            return;
        }

        if (TextUtils.isEmpty(params.get("ProvinceID"))){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "请选择联系地址");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }
        if (TextUtils.isEmpty(params.get("ContactAddress"))){
            if (listener != null) {
                listener.onFailure(ErrorEvent.MESSAGE_PARAM_NULL, "详细地址不能为空");  //先调用这个方法中的内容，这个方法叫回调
            }
            return;

        }* */
        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.ADD_CONTACT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.ADD_CONTACT);


        RetrofitServiceGenerator.getApi().addContact(head01
                , head02
                , head03
                , userId, token,params).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());


                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(),
                                response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });



    }

    @Override
    public void editContact(Map<String, String> params, final ActionCallbackListener<ResponseCode> listener) {

        final PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.EDIT_CONTACT);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.EDIT_CONTACT);


        RetrofitServiceGenerator.getApi().editContact(head01
                , head02
                , head03
                , userId, token,params).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body());


                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(),
                                response.body().getMessage()));                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void authLogin(Map<String, String> params, final ActionCallbackListener<	UserBean.DataBean> listener) {

        String head01=HeaderUtil.getHeadindexNotUser1(Constants.AUTH_LOGIN);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.AUTH_LOGIN);


        RetrofitServiceGenerator.getApi().authLogin(head01
                , head02
                , head03
                , params).enqueue(new Callback<UserBean>() {
            PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");

            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {

                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        UserBean.DataBean bean = response.body().getData();

                        if (bean.isBind()){
                            complexPreferences.putString(Constants.ACCESS_TOKEN, bean.getAccessToken());
                            complexPreferences.putLong(Constants.USERID, bean.getUserID());
                            complexPreferences.putLong(Constants.EXPIRES_IN, bean.getExpiresIn());
                            isLogin = true;
                            complexPreferences.putBoolean(Constants.ISLOGIN, isLogin);
                        }

                        listener.onSuccess(response.body().getData());
                    } else {
                        isLogin = false;
                        complexPreferences.putBoolean(Constants.ISLOGIN, isLogin);
                        Toast.makeText(context, "response.body().getCode():" + response.body().getCode(), Toast
                                .LENGTH_SHORT).show();
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    isLogin = false;
                    complexPreferences.putBoolean(Constants.ISLOGIN, isLogin);
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                isLogin = false;
                complexPreferences.putBoolean(Constants.ISLOGIN, isLogin);
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void bindUser(Map<String, String> params, final ActionCallbackListener<UserBean.DataBean> listener) {

        String head01=HeaderUtil.getHeadindexNotUser1(Constants.BIND_USER);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.BIND_USER);


        RetrofitServiceGenerator.getApi().bindUser(head01
                , head02
                , head03
                , params).enqueue(new Callback<UserBean>() {
            PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");

            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {

                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {
                        listener.onSuccess(response.body().getData());
                    } else {

                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);
                }

            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void cancelOrder(long orderId, String reason, final ActionCallbackListener<ResponseCode> listener) {

        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.CANCEL_ORDER);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.CANCEL_ORDER);

        RetrofitServiceGenerator.getApi().cancelOrder(head01
                , head02
                , head03
                , userId, token, orderId,reason).enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 101){
                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据00");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response.body().getCode(), response.body().getMessage()));
                }

            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });

    }

    @Override
    public void EditContact(Map<String, String> params, ActionCallbackListener<ResponseCode> listener) {
        //逻辑再来添加编辑联系人

    }

    @Override
    public void qrCodeLogin(String logintoken, String nsts, final ActionCallbackListener<ResponseCode> listener) {

        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        Long  userId =complexPreferences.getLong(Constants.USERID, 0l);
        String  token =complexPreferences.getString(Constants.ACCESS_TOKEN, "");
        String head01=HeaderUtil.getHeadindexNotUser1(userId+token+Constants.QR_CODE_LOGIN);
        NTPTime.getInstance().updteNTPTime();
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.QR_CODE_LOGIN);

        RetrofitServiceGenerator.getApi().qrCodeLogin(head01
                , head02
                , head03
                , userId, token, logintoken,nsts).enqueue(new Callback
                <ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                if (response.isSuccessful()) {
                    if (101 == response.body().getCode()) {

                        listener.onSuccess(response.body());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()),classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    /*粗心大意啊！如果按照没有数据来写会崩溃的*/
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL, ErrorEvent.MESSAGE_SERVER_FAIL);

                }


            }
            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }


    @Override
    public void getProductListByNormalSort(int currentPage, int PageSize, final ActionCallbackTripleListener<List<SortDataBean>,String,String>
            listener) {
        /*01，参数的合法性检查*/
        if (currentPage < 0){
            listener.onFailure(ErrorEvent.CODE_PARAM_ILLEGAL, ErrorEvent.MESSAGE_ILLEGAL_SITU2);
        }
        /*02.向API层调用接口，并且添加消息头,提交请求时，必须是一致确定的值*/
        String head01=HeaderUtil.getHeadindexNotUser1(Constants.GET_PRODUCT_LIST_BY_NORMALSORT);
        long time = NTPTime.getInstance().getCurrentTime();
        String head02=HeaderUtil.getHeadindex2(time);
        String head03=HeaderUtil.getHeadindex3(Constants.GET_PRODUCT_LIST_BY_NORMALSORT);
        RetrofitServiceGenerator.getApi().getProductListByNormalSort(head01
                ,head02
                ,head03
                ,currentPage
                ,PageSize).enqueue(new Callback<GeneralSortDataBean>() {
            @Override
            public void onResponse(Call<GeneralSortDataBean> call, Response<GeneralSortDataBean> response) {
                if (response.isSuccessful()) {
                    if (isFristTime){
                        PreferenceUtil complexPreferencesPre = PreferenceUtil.getSharedPreference(context, "preferences");
                        long time_to_time = System.currentTimeMillis()-Long.parseLong(response.headers().get
                                ("OkHttp-Received-Millis"));
                        complexPreferencesPre.putLong(Constants.TIME_TO_TIME,time_to_time);//实际上这种情况也不能完全杜绝的
                        isFristTime=false;
                    }
                    if (101 == response.body().getCode()) {

                        List<GeneralSortDataBean.DataBean.ListBean> list = response.body().getData().getList();
                        SortDataBean sortDataBean = null;
                        List<SortDataBean> relist = new ArrayList<SortDataBean>();
                        for (int i = 0; i < list.size(); i++) {
                            long ProductID = list.get(i).getProductID();
                            String City = list.get(i).getCity();
                            String District = list.get(i).getDistrict();
                            String ImagePath = list.get(i).getImagePath();
                            String ProductName = list.get(i).getProductName();
                            int OrderNum = list.get(i).getOrderNum();
                            double MinPrice = list.get(i).getMinPrice();
                            float ComprehensiveScore = list.get(i).getComprehensiveScore();
                            String Province = list.get(i).getProvince();
                            /*
                            *   @文件名：product
                            *   @存放内容：每个bean的内容
                            *
                            * */
                            sortDataBean =new SortDataBean(ProductID,City, District, ImagePath, ProductName, OrderNum, MinPrice,ComprehensiveScore,Province);
                            relist.add(sortDataBean);
                        }
                        /*
                        *   @文件名：preferences
                        *   @存放内容：最新页的列表 和不同页的列表
                        *
                        * */
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
                        complexPreferences.putListObject("newest_normal_sort_product_list", relist);
                        complexPreferences.putListObject("number_"+response.body().getData().getPageIndex()+"_normal_sort_product_list", relist);
                        listener.onSuccess(relist, response.body().getData().getPageIndex(), response.body().getData().getTotalCount());
                    } else {
                        Log.d(TAG, "没有数据");
                        listener.onFailure(Integer.toString(response.body().getCode()), classifyErrorEvent(response
                                .body().getCode(), response.body().getMessage()));
                    }

                } else {
                    Log.d(TAG, "服务器的问题");
                    listener.onFailure(ErrorEvent.CODE_SERVER_FAIL,ErrorEvent.MESSAGE_SERVER_FAIL );
                }
            }

            @Override
            public void onFailure(Call<GeneralSortDataBean> call, Throwable t) {
                Log.d(TAG, "没有数据02");
                listener.onFailure(ErrorEvent.CODE_NETWORK_FAIL, ErrorEvent.MESSAGE_NETWORK_FAIL);
            }
        });


    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable((Parcelable) this.context, flags);
        dest.writeParcelable((Parcelable) this.api, flags);
    }

    protected AppActionImpl(Parcel in) {
        this.context = in.readParcelable(Context.class.getClassLoader());
        this.api = in.readParcelable(Api.class.getClassLoader());
    }

    public static final Parcelable.Creator<AppActionImpl> CREATOR = new Parcelable.Creator<AppActionImpl>() {
        @Override
        public AppActionImpl createFromParcel(Parcel source) {
            return new AppActionImpl(source);
        }

        @Override
        public AppActionImpl[] newArray(int size) {
            return new AppActionImpl[size];
        }
    };
}
