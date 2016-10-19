package com.android.haobanyi.activity.ailpay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.guid.register.SetPayPwdActivity;
import com.android.haobanyi.activity.mine.settings.UserSettingActivity;
import com.android.haobanyi.activity.wxapi.WeChatPayService;
import com.android.haobanyi.core.ActionCallbackDoubleListener;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.charge.ChargeBean;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.mine.myinformation.UserBean;
import com.android.haobanyi.model.bean.post.Orders;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.PayPwdEditText;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import dmax.dialog.SpotsDialog;

/**
 * Created by fWX228941 on 2016/9/7.
 *
 * @作者: 付敏
 * @创建日期：2016/09/07
 * @邮箱：466566941@qq.com
 * @当前文件描述：弹出一个支付界面
 */
//先用这个类试一下
public class ActionToPay {
    private static final String TAG = "minfu";
    private DialogPlus dialog;
    private DialogPlus pwddialog;
    private TextView id_ailpay;
    private TextView id_weixin_ailpay;
    private TextView id_account_ailpay;
    private TextView id_need_to_pay;
    private static final int TAG_WEIXIN_AILPAY = 1;
    private static final int TAG_AILPAY = 0;
    private static final int TAG_ACCOUNt_AILPAY = 2; //三个支付的标志也是挺重要的
    Context context;  //这个需要提供得
    private int TAG_PAY = 0;
    private long requestCount = 5; //通用继承过来，通用的都是可以继承的
    PreferenceUtil complexPreferences;  //如果是跳到立即购买的话，还是得先判断
    private static final int LOGIN_TO_SPV = 3;
    // 商户PID
    private static final String PARTNER = "2088121920313880";// 支付宝商户标识partnerID 绑定密钥  【密钥说白了就是一组安全信息】
    // 商户收款账号
    private static final String SELLER = "service@haobanyi.com";
    // 商户私钥，pkcs8格式
    private static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAML0SUinVs5Ub0sN\n" +
            "34Gx4R4eUq4e6KRc7ZPOy4MbzFJKzru3FFiaffZU+EqDC+IJ+WimsTY1952xxOHs\n" +
            "Vgq9dxka4D2sPSPliBhgdozzpXDNI+syFNwOQLOoGH687lRAr37Cs6btUmnuBz/o\n" +
            "qtbvB0y33NPKRwYPKOtEKpG6CP65AgMBAAECgYAdH1zrK4Q+IkLV1Waz+xyD4Ue8\n" +
            "lwVtuCwNTnD8TuTAzkf/pqO1cK6sY5kLmHPn+lF1nqEOVhV9Sxb8IM/hntfxd1ld\n" +
            "2x+TXOp8NODmLpUYrtOlOmFvzziumKNjR8P3TMOg5c/cKTt1DFcjsgDLUUtgCNaW\n" +
            "FV0ZOIA14d0lPtUnOQJBAOZxb5B4EnMgVmqaKTRvd9ylbrW/cBUmKeD0vJUYbzI2\n" +
            "vGiQ7KNp0sr2RQE47+nEgxT6vVm2rs5vlTvxgAe/6HsCQQDYk0cdJPhvewa/ztEQ\n" +
            "WCR0HsdtrDte6mBSuWLJJKDJx6JBbloIBdFZqSrHh7qDLU+DpRVyhS+fCFlFzav6\n" +
            "FKFbAkBpdXBBBTIsTZg+oN5KiNipuWu48SsyAo0Xzeifstbwe+wtTmV9Q2icSae6\n" +
            "r3vb13rFlxogfGHzR01caWDWKxURAkEAtKCo5/rEpR3DdEfGf84zoRFmnObPoYry\n" +
            "iSwK4t2EqKWygsXJmvVXNGNtxXpDKVKqBY6yw4+nNwkcg6ZUr79zDwJAcldzWaPn\n" +
            "ZhlM8harL9JKQISoJevVi5S3cxm+9onHpPgq7AMRkBG/G7HGLNSk0vcc5JvvuLKg\n" +
            "N7uM3lRZEGSjNw==";  // 私钥 和PID 用于支付宝接口调用，请求参数，与支付宝交互时，请求数据必须

    private static final int SDK_PAY_FLAG = 1;
    /*微信支付*/
    private SpotsDialog progressDialog;
    private String totalPrice ="0";
    private String balance ="0";
    private PayPwdEditText payPwdEditText;
    private String pwd_=null;
    private static final int TO_SET_PAY_PWD = 04;//跳转到支付密码的标识量

    public ActionToPay(Context context) {
        this.context = context;
        progressDialog =  new SpotsDialog(context);
    }

    public ActionToPay(Context context,String totalPrice) {
        this.context = context;
        progressDialog =  new SpotsDialog(context);
        this.totalPrice = totalPrice;
    }

    public void popupSubmitOrderDialog(final Orders.OrdersBean params, final String orderIds){
        dialog = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.item_dialog_payment))
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setExpanded(false)  //设置为不可滑动
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialog) {


                    }
                })
                .setOnBackPressListener(new OnBackPressListener() {
                    @Override
                    public void onBackPressed(DialogPlus dialogPlus) {

                    }
                })
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.id_ailpay:
                                Log.d(TAG, "  点击支付宝");
                                //默认是以支付宝
                                setDrawable(0);
                                break;
                            case R.id.id_weixin_ailpay:
                                Log.d(TAG, "  点击微信支付");
                                setDrawable(1);

                                break;
                            case R.id.id_account_ailpay:
                                Log.d(TAG, "  点击余额支付");
                                /*
                                * SubmitPayment,GetUserCapital   搞一个全局的变量，暂时只有这两个变量，最新就存储一下，最好每次都用这个来设置一下
                                *
                                *
                                * */
                                if(Double.parseDouble(totalPrice)<Double.parseDouble(balance)){
                                    setDrawable(2);
                                }
                                break;
                            case R.id.pay:
                                Log.d(TAG, "  点击确认支付");
                                if (TAG_PAY == TAG_ACCOUNt_AILPAY ){
                                // 在余额支付前先要预先判断是否设置了支付密码，如果没有设置，则跳转到第一次支付界面，所以还是得分开，先设置支付密码，设置成功了，跳转回来，实际上这个也只会执行一次，不用每次去判断的，最好是新调用后判断
                                // 这里其实还涉及到一个多用户，有些东西是不能根据本地来判断的，目的就是变为true嘛！用方法名唯一标识就是了，后期添加
                                    complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
                                    UserBean.DataBean user = complexPreferences.getObject(Constants.USER_INFO, UserBean.DataBean.class);
                                    if (!user.isHasPayPwd()){
                                        //没有支付密码，则跳转到支付密码
                                        IntentUtil.gotoActivityForResult(context, SetPayPwdActivity.class, TO_SET_PAY_PWD); //一定要注意细节，不然很容易就出现问题了

                                    }else {
                                        //如果是余额支付，先弹框
                                        pwddialog = DialogPlus.newDialog(context)
                                                .setContentHolder(new ViewHolder(R.layout.item_password_payment))
                                                .setGravity(Gravity.TOP)
                                                .setCancelable(true)
                                                .setExpanded(false)
                                                .setOnClickListener(new OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogPlus dialog, View view) {
                                                        switch (view.getId()) {
                                                            case R.id.id_forget_pwd:
                                                                IntentUtil.gotoActivityForResult(context, SetPayPwdActivity.class, TO_SET_PAY_PWD);
                                                                break;
                                                        }
                                                    }
                                                }).create();
                                        View view_ = pwddialog.getHolderView();
                                        payPwdEditText = (PayPwdEditText) view_.findViewById(R.id.ppe_pwd);
                                        payPwdEditText.initStyle(R.drawable.edit_num_bg, 6, 0.33f, R.color.cardview_dark_background, R.color.cardview_dark_background, 20);
                                        pwddialog.show();
                                        payPwdEditText.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
                                            @Override
                                            public void onFinish(String pwd) {
                                                pwd_ = pwd;
                                                if (null!=pwd_){
                                                    if (!progressDialog.isShowing()){
                                                        progressDialog.show();
                                                    }
                                                    pay(TAG_PAY,params,orderIds);
                                                    pwddialog.dismiss();
                                                }

                                            }
                                        });
                                    }

                                }else {

                                    if (!progressDialog.isShowing()){
                                        progressDialog.show();
                                    }
                                    pay(TAG_PAY,params,orderIds);
                                }

                                dialog.dismiss();// 只是后腿跳转，跳转到哪里
                                break;
                        }
                    }
                })
                .create();
        dialog.show();
        /*初始化dialog中视图对象*/
        initDialogView();

        //获取余额
        requestCount = 35;
        GetUserCapital();


    }

    private void GetUserCapital() {
        BaseApplication.getApplication().getAppAction().GetUserCapital(new ActionCallbackDoubleListener<List<ChargeBean>, String>() {
            @Override
            public void onSuccess(List<ChargeBean> data01, String balance_) {
                balance = balance_;
                id_account_ailpay.setText(balance);

            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if ("998".equals(errorEvent) && requestCount > 0) {
                    requestCount--;
                    GetUserCapital();
                } else {
                     complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
                     balance = complexPreferences.getString(Constants.BALANCE,"0");
                     id_account_ailpay.setText(balance);
                }
            }
        });
    }

    private void initDialogView() {
        View view = dialog.getHolderView();
        id_ailpay = (TextView) view.findViewById(R.id.id_ailpay);
        id_weixin_ailpay = (TextView) view.findViewById(R.id.id_weixin_ailpay);
        //id_account_ailpay
        id_account_ailpay = (TextView) view.findViewById(R.id.id_account_ailpay);
        id_need_to_pay = (TextView) view.findViewById(R.id.id_need_to_pay);
        id_need_to_pay.setText(totalPrice+"￥");
    }

    /*
    * 动态加载图片
    * 参考设置：https://software.intel.com/zh-cn/blogs/2014/09/17/android-textview-drawableleft-drawableright
    *
    * 按照原有比例来设置：http://blog.csdn.net/wulianghuan/article/details/24421179
    * */
    private void setDrawable(int tag) {
        Drawable RightDrawable = context.getResources().getDrawable(R.drawable.choice_icon);
        Drawable leftDrawable_weixin = context.getResources().getDrawable(R.drawable.weixin_pay);
        Drawable leftDrawable_ailpay = context.getResources().getDrawable(R.drawable.alipay);
        Drawable leftDrawable_content_pay = context.getResources().getDrawable(R.drawable.content_pay);
        /*必须要设置图片大小，否则图片不显示*/
/*        RightDrawable.setBounds(0, 0, RightDrawable.getMinimumWidth(), RightDrawable.getMinimumHeight());
        leftDrawable_weixin.setBounds(0, 0, RightDrawable.getMinimumWidth(), RightDrawable.getMinimumHeight());
        leftDrawable_ailpay.setBounds(0, 0, RightDrawable.getMinimumWidth(), RightDrawable.getMinimumHeight());*/

        switch (tag) {
            case TAG_AILPAY:
                id_ailpay.setCompoundDrawablesWithIntrinsicBounds(leftDrawable_ailpay, null, RightDrawable, null);
                id_weixin_ailpay.setCompoundDrawablesWithIntrinsicBounds(leftDrawable_weixin, null, null, null);
                id_account_ailpay.setCompoundDrawablesWithIntrinsicBounds(leftDrawable_content_pay, null, null, null);
                TAG_PAY = tag;
                break;
            case TAG_WEIXIN_AILPAY:
                id_weixin_ailpay.setCompoundDrawablesWithIntrinsicBounds(leftDrawable_weixin, null, RightDrawable, null);
                id_ailpay.setCompoundDrawablesWithIntrinsicBounds(leftDrawable_ailpay, null, null, null);
                id_account_ailpay.setCompoundDrawablesWithIntrinsicBounds(leftDrawable_content_pay, null, null, null);
                TAG_PAY = tag;
                break;
            case TAG_ACCOUNt_AILPAY:
                //点击余额支付，怎么跳边了
                Log.d(TAG, "跳转过来");
                id_account_ailpay.setCompoundDrawablesWithIntrinsicBounds(leftDrawable_content_pay, null, RightDrawable, null);
                id_ailpay.setCompoundDrawablesWithIntrinsicBounds(leftDrawable_ailpay, null, null, null);
                id_weixin_ailpay.setCompoundDrawablesWithIntrinsicBounds(leftDrawable_weixin, null, null, null);
                TAG_PAY = tag;
                break;
        }

    }

    /*
    *  支付逻辑：
    *  服务器那边支付成功后，跳转到当前页面，并弹出一个toast提示框，同时当前视图内容清空
    *
    *
    * */
     /*
    * 1.先要依据用户信息中的标志位IsHasPayPwd来判断，是否有支付密码，没有则先调用SetPayPwd来设置支付密码【长度，大小规则要写网页端后台保持一致】
    * 2.设置成功后重新选择连接
    * 3.设置成功后，把标志位设置为true
    * 4.当有支付密码后，判断账户余额中是否有足值的钱来支付，如果有则弹框输入密码，并支付成功PayByCapital  前提是需要支付单号，步骤同其他支付方式
    * 5.如果余额不足，则提示用户，并且跳转到充值界面，充值成功后再次进行4操作，跳转过来返回就是了
    * 如果当前账户余额小于 < 商品价格，则不可点击
    * 当前手机端是无法充值的
    *
    * */

    private void pay(int tag, Orders.OrdersBean params, final String orderIds) {

        requestCount = 45;
        // 如果是从我的订单列表中过来就不需要这一步骤了，两个决定
        if (null==params && null != orderIds){
            Log.d(TAG, "执行");
            submitPayment(orderIds,tag);

        }else if (null!=params && null == orderIds){
            submitOrder(params,tag);
        }



      /*  switch (tag) {
            case TAG_AILPAY:
                Log.d(TAG, "走支付宝渠道");
                //走支付渠道之前，先要调用接口
                //不一定存在红包id,没有则传空
                requestCount = 45;
                // 如果是从我的订单列表中过来就不需要这一步骤了，两个决定
                if (null==params && null != orderIds){
                    Log.d(TAG, "执行");
                    submitPayment(orderIds,tag);

                }else if (null!=params && null == orderIds){
                    submitOrder(params,tag);
                }

                break;
            case TAG_WEIXIN_AILPAY:
                Log.d(TAG, "走微信支付渠道");
                requestCount = 45;
                // 如果是从我的订单列表中过来就不需要这一步骤了，两个决定
                if (null==params && null != orderIds){
                    Log.d(TAG, "执行");
                    submitPayment(orderIds,tag);

                }else if (null!=params && null == orderIds){
                    submitOrder(params,tag);
                }
                break;
            case TAG_ACCOUNt_AILPAY:
                Log.d(TAG, "走余额支付渠道");



                break;
        }*/
    }

    private void submitOrder(final Orders.OrdersBean params, final int type) {
        Log.d(TAG, "01");// 没有则传入空
        BaseApplication.getApplication().getAppAction().submitOrder(params.getRedID(), params, new
                ActionCallbackListener<List<Integer>>() {
                    @Override
                    public void onSuccess(List<Integer>  data) {
                        Log.d(TAG, "02");
                        Log.d(TAG, "返回的结果" + data);

                        if (null !=data){
                            StringBuffer OrderIds = new StringBuffer(256);
                            int size = data.size();
                            if (size==1){
                                OrderIds.append(Integer.toString(data.get(0)));
                            } else {
                                for (int i =0;i<data.size();i++){
                                    OrderIds.append(Integer.toString(data.get(i)) + ",");//如果有多个，则逗号分开
                                }
                            }

                            Log.d(TAG, OrderIds.toString());
                            if (!TextUtils.isEmpty(OrderIds.toString())){
                                Log.d(TAG, "j");
                                requestCount = 45;
                                submitPayment(OrderIds.toString(),type );//"data":[30287,30288]
                            }
                        }





                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        if ("998".equals(errorCode) && requestCount > 0) {
                            requestCount--;
                            submitOrder(params,type);
                        } else if ("996".equals(errorCode)) {
                            if (progressDialog.isShowing()){
                                progressDialog.cancel();
                            }
                            loginForToken();
                        } else if ("003".equals(errorCode)) {
                            if (progressDialog.isShowing()){
                                progressDialog.cancel();
                            }
                            ToastUtil.networkUnavailable();
                        } else {
                            if (progressDialog.isShowing()){
                                progressDialog.cancel();
                            }
                            ToastUtil.showHintMessage("交易失败",errorCode,errorMessage);
                        }
                    }
                });
    }
    private void loginForToken() {
        complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(context, LoginActivity.class, LOGIN_TO_SPV);//跳转到登录界面
    }
    private void submitPayment(final String data, final int type) {
        Log.d(TAG, "06？");
        BaseApplication.getApplication().getAppAction().submitPayment(data, new ActionCallbackListener
                <LoginResponseResult.DataBean>() {
            @Override
            public void onSuccess(LoginResponseResult.DataBean data) {

                Log.d(TAG, "07");
                switch (type){
                    case TAG_AILPAY://支付宝
                        ailpayPay(data.getOrderPayId(), data.getOrderDesc(), data.getAmount());
                        if (progressDialog.isShowing()){
                            progressDialog.cancel();
                        }
                        break;
                    case TAG_WEIXIN_AILPAY://微信
                        Log.d("ActionToPay", "执行微信");
                        WeChatPayService weChatPay = new WeChatPayService(context,data.getOrderPayId(), data.getOrderDesc(), data.getAmount());
                        weChatPay.pay();
                        if (progressDialog.isShowing()){
                            progressDialog.cancel();
                        }
                        break;
                    case TAG_ACCOUNt_AILPAY:
                        Log.d(TAG, "走余额支付渠道");//密码输入错误三次将自动冻结资金payByCapital
                        if (!TextUtils.isEmpty(pwd_)){
                            requestCount = 45;
                            payByCapital(data, type);
                        }
                        break;


                }




            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {


                if ("998".equals(errorCode)&& requestCount > 0 ) {
                    requestCount--;
                    submitPayment(data,type );
                } else if ("996".equals(errorCode)) {
                    if (progressDialog.isShowing()){
                        progressDialog.cancel();
                    }
                    loginForToken();
                } else if ("003".equals(errorCode)) {
                    if (progressDialog.isShowing()){
                        progressDialog.cancel();
                    }
                    ToastUtil.networkUnavailable();//已经在订单中心了，就不要刷新了
                } else {
                    if (progressDialog.isShowing()){
                        progressDialog.cancel();
                    }
                    ToastUtil.showHintMessage("交易失败",errorCode,errorMessage);
                }

            }
        });

    }

    private void payByCapital(final LoginResponseResult.DataBean data, final int type) {
        BaseApplication.getApplication().getAppAction().payByCapital(data.getOrderPayId(), pwd_, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage("支付成功");
                onPayBackListener.onPayDone();//支付成功后刷新一下就是了
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode)&& requestCount > 0 ) {
                    requestCount--;
                    payByCapital(data, type);
                } else if ("996".equals(errorCode)) {
                    if (progressDialog.isShowing()){
                        progressDialog.cancel();
                    }
                    loginForToken();
                } else if ("003".equals(errorCode)) {
                    if (progressDialog.isShowing()){
                        progressDialog.cancel();
                    }
                    ToastUtil.networkUnavailable();//已经在订单中心了，就不要刷新了
                } else {
                    if (progressDialog.isShowing()){
                        progressDialog.cancel();
                    }
                    ToastUtil.showHintMessage("交易失败",errorCode,errorMessage);
                }
            }
        });
    }

    //支付宝支付
    private void ailpayPay(String orderPayId, String orderDesc, String amount) {
        /*1.这里是需要验证两种逻辑的，一种是存在支付宝客户端，一种是没有支付宝客户端，将会触发SDK内部进行H5支付*/
        /*2，参数合法性检查*/
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)){
            new AlertDialog.Builder(context).setTitle("警告").setMessage("需要配置商户ID | 商户私钥| 商户收款账号")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            ((Activity)context).finish();// 并把当前界面给关闭掉
                        }
                    }).show();
            return;
        }
        /*服务器需要提供：签约合作者身份ID /签约卖家支付宝账号 /商户网站唯一订单号 /商品名称 /商品详情 /商品金额/签名/签名方式*/
        /*请求参数至少包含19个*/
        Log.d(TAG, orderPayId);
        Log.d(TAG, orderDesc);
        Log.d(TAG, amount);
        String orderInfo = getOrderInfo(orderPayId, orderDesc, amount);  // 这个就是订单信息
        Log.d(TAG, orderInfo);
        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);  // 服务端添加签名逻辑 ，私钥放在服务端
        Log.d(TAG, sign);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
            Log.d(TAG, sign);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String payInfo =orderInfo + "&sign=\"" + sign + "\"&" + getSignType();;//这个就需要服务器端来返回了
        /*3.发起支付调用*/
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                //1.构造支付任务对象
                PayTask alipay = new PayTask((Activity) context);
                //2.调用支付接口，并获取支付结果
                String result = alipay.pay(payInfo, true);
                //3.结果出来后，把结果数据另开一个线程来处理
                Message msg = new Message();
                msg.obj = result;
                msg.what = SDK_PAY_FLAG;//处理返回结果
                mHandler.sendMessage(msg);
            }
        };
        Thread thread = new Thread(payRunnable);
        thread.start();
    }
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://www.haobanyi.com/Pay/Notify/HbyCommerce-Plugin-Payment-Alipay" + "\"";//整个域名添加：由http://notify.msp.hk/notify.htm

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }
    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }
    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        Log.d(TAG, "签名");
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @SuppressWarnings("unused")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SDK_PAY_FLAG:{
                    PayResult payResult = new PayResult((String) msg.obj); // 这个逻辑也是放在服务器端
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知。
                     */
                    String resultInfo = payResult.getResult();// 返回支付状态和支付信息
                    String resultStatus = payResult.getResultStatus();
                    // 1.resultStatus==“9000”:代表支付成功，
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtil.showSuccessfulMessage("支付成功");
                        onPayBackListener.onPayDone();//支付成功后刷新一下就是了
                    } else {
                        /*其他的一律为支付失败*/
                        // 2."8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showHintMessage("支付结果确认中");

                        } else {
                            //3.其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtil.showHintMessage("支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }

        }
    };

    OnPayBackListener onPayBackListener;
    public interface OnPayBackListener{
        void onPayDone();
    }
    public void setOnPictureStateChangedListener(OnPayBackListener onPayBackListener) {
        this.onPayBackListener = onPayBackListener;
    }

    /*==================微信支付=======================*/
/*    public void weixinpay(){

        if (!BaseApplication.api.isWXAppInstalled()) {
            ToastUtil.showLong("您还未安装微信客户端");
            return;
        }


        boolean isPaySupported = BaseApplication.api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        //是否支付微信支付
        Log.d(TAG, "isPaySupported:" + isPaySupported);
        if (isPaySupported){
            try{
                //当用户下单后，会将商品信息传递给服务器端，服务器端将生成必要的参数
                *//*
                * APP端调起支付的参数列表
                * 暂时先把拼装参数的部分放在客户端
                *
                * http://blog.csdn.net/qq_17387361/article/details/52484376?locationNum=1
                *
                * http://blog.csdn.net/dgs960825/article/details/51959180?locationNum=4
                * 
                * *//*
                        PayReq req = new PayReq();
                        //微信开放平台审核通过的应用APPID
                        req.appId			= "wx86d3a2a38a4da3b1";//签名必须是app正式版的签名，这个需要修改下
                        //微信支付分配的商户号
                        req.partnerId		= "1366068802";
                        req.prepayId		= json.getString("prepayid");//微信返回的支付交易会话ID  这个需要合成
                        req.nonceStr		= createRandomString(32);//随机字符串，不长于32位,生成随机数算法，保证签名不可预测
                        req.timeStamp		= HeaderUtil.getHeadindex2(NTPTime.getInstance().getCurrentTime());//时间戳,标准北京时间，时区为东八区，自1970年1月1日 0点0分0秒以来的秒数。注意：部分系统取到的值为毫秒级，需要转换成秒(10位数字)。
                        Log.d(TAG, req.timeStamp);
                        req.packageValue	= "Sign=WXPay";
                        req.sign			= json.getString("sign");// 这个而是挺复杂的

                        req.extData			= "app data"; // optional
                        ToastUtil.showHintMessage("正常调起支付");
                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                        BaseApplication.api.sendReq(req);


            }catch(Exception e){
                Log.e("PAY_GET", "异常："+e.getMessage());
                ToastUtil.showHintMessage("异常："+e.getMessage());
            }
        }else {
            ToastUtil.showHintMessage("不支持微信支付");
        }

    }

    *//**
     * 随机生成字符串
     * @param length 字符串的长度
     * @return       随机字符串
     *//*
    private  String createRandomString(int length) {
        String source = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int position = random.nextInt(source.length());
            builder.append(source.charAt(position));
        }
/*
      *//*  return builder.toString();
    }*/


}
