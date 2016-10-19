package com.android.haobanyi.wxapi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.activity.guid.register.ThirdLoginActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.mine.myinformation.UserBean;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import java.util.HashMap;

import static com.baidu.location.b.g.T;


/*
* 回调类,其实是自动寻找和跳转
* 即：从微信确认登录之后又返回了WXEnrtyActivity
*implements IWXAPIEventHandler
* */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    protected long requestCount = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                /*
        * [1] 注册到微信 将该app注册到微信,APP_ID 替换为你的应用从官方网站申请到的合法appId,通过WXAPIFactory工厂，获取IWXAPI的实例
        * 要使你的程序启动后微信终端能响应你的程序，必须在代码中向微信终端注册你的id
        * */

        /*
        * 【4】.在WXEntryActivity中将接收到的intent及实现了IWXAPIEventHandler接口的对象传递给IWXAPI接口的handleIntent方法
        *
        * */
        BaseApplication.api.handleIntent(getIntent(), this);
    }



    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        BaseApplication.api.handleIntent(intent, this);


	}
    /*private void handleIntent(Intent intent) {
        SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
        // 被执行
        Toast.makeText(this, "被执行01", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, resp.errCode, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "resp.errCode == BaseResp.ErrCode.ERR_OK:" + (resp.errCode == BaseResp.ErrCode.ERR_OK),
                Toast.LENGTH_SHORT).show();
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            if (resp != null) {
                String code = resp.code;
                String state = resp.state;
                Log.d("WXEntryActivity", code);
                Log.d("WXEntryActivity", state);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Code", code);
                params.put("State", state);
                Log.d("WXEntryActivity", "同意了");
                requestCount = 5;
                authLogin(params);
            }
        }
    }*/
    /*
    *【2】 微信发送请求到第三方应用时，即微信发送的请求将调用此方法，程序需要接收微信发送的请求，第三方登录暂时不用考虑这个回调方法
    *
    * */
	@Override
	public void onReq(BaseReq req) {
	}

    /*
    * 【3】.接收发送到微信请求的响应结果，将回调到onResp方法，
    *  对于第三方登录的需求：需要在此回调函数中写处理逻辑
    * */

    /*
    * 【5】.处理逻辑概述：登录的功能比分享稍微复杂，需要和微信服务器进行至少三次的数据交换(第一步获取code, 第二步通过code获取access_token以及其他的凭证， 第三步再通过access_token来调用微信的接口)。
    *
    *
    *
    * */

	@Override
	public void onResp(BaseResp baseResp) {














/*        Toast.makeText(this, "baseResp:" + baseResp, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "baseResp==null:" + (baseResp == null), Toast.LENGTH_SHORT).show();*/
        Log.d("WXEntryActivity", "执行了");
/*
        Toast.makeText(this, baseResp.errCode, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "执行了", Toast.LENGTH_SHORT).show();

        Toast.makeText(this, baseResp.errCode, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "baseResp.errCode == BaseResp.ErrCode.ERR_OK:" + (baseResp.errCode == BaseResp.ErrCode
                .ERR_OK), Toast.LENGTH_SHORT).show();*/


/*        Toast.makeText(this, baseResp.errCode, Toast.LENGTH_SHORT).show();

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:*/
//                Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
                Log.d("WXEntryActivity", "发送成功");
                SendAuth.Resp sendResp = (SendAuth.Resp) baseResp;
//        Toast.makeText(this, "sendResp != null:" + (sendResp != nul), Toast.LENGTH_SHORT).show();
        ToastUtil.showHintMessage("授权成功");
                if (sendResp != null) {
                    String code = sendResp.code;
                    String state = sendResp.state;
                    Log.d("WXEntryActivity", code);
                    Log.d("WXEntryActivity", state);
        /*            Toast.makeText(this, code, Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, state, Toast.LENGTH_SHORT).show();*/
                    if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(state)){
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("Code", code);
                        params.put("State", state);
                        params.put("LoginType", "1");
                        Log.d("WXEntryActivity", "同意了");
                        requestCount = 5;
                        authLogin(params);
                    }else {
                        Toast.makeText(this, "取消登录", Toast.LENGTH_SHORT).show();
                        finish();
                    }


                }else {
                    Toast.makeText(this, "取消登录", Toast.LENGTH_SHORT).show();
                    finish();
                }
/*                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, "发送取消", Toast.LENGTH_SHORT).show();
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Toast.makeText(this, "发送被拒绝", Toast.LENGTH_SHORT).show();
                break;
            default:
                //发送返回
                Toast.makeText(this, "发送返回", Toast.LENGTH_SHORT).show();
                break;
        }
        finish();*/
    }
    //支付成功后是会再这里执行得
    private void authLogin(final HashMap<String, String> params) {
//        Toast.makeText(WXEntryActivity.this, "03", Toast.LENGTH_SHORT).show();
        BaseApplication.getApplication().getAppAction().authLogin(params, new ActionCallbackListener<UserBean
                .DataBean>() {
            @Override
            public void onSuccess(UserBean.DataBean data) {
                //如果是绑定的就直接跳转到登录界面，如果不是绑定的就，【新增加一个界面，不然复杂度太高了】
                Log.d("WXEntryActivity", "data.isBind():" + data.isBind());
/*                Toast.makeText(WXEntryActivity.this, "04", Toast.LENGTH_SHORT).show();
                Toast.makeText(WXEntryActivity.this, "data.isBind():" + data.isBind(), Toast.LENGTH_SHORT).show();*/
                if (data.isBind()) {
                    requestCount = 5;
                    getUserInfo();
                } else {
//                    Toast.makeText(WXEntryActivity.this, "05", Toast.LENGTH_SHORT).show();
                    String Rdm = data.getRdm();
                    if (!TextUtils.isEmpty(Rdm)) {
                        //跳转到第三方登陆得界面
                        Bundle bundle = new Bundle();
                        bundle.putString("Rdm", Rdm);
                        bundle.putString("LoginType", "1");
//                        Toast.makeText(WXEntryActivity.this, "06", Toast.LENGTH_SHORT).show();
                        IntentUtil.gotoTopActivityWithData(WXEntryActivity.this, ThirdLoginActivity.class, bundle,
                                true);
                    }
                }


            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode)&& requestCount > 0) {
                    requestCount--;
                    authLogin(params);
                } else {
                    ToastUtil.showHintMessage("登录失败",errorCode,errorMessage);
                }

            }
        });
    }

    private void getUserInfo() {
//        Toast.makeText(WXEntryActivity.this, "02", Toast.LENGTH_SHORT).show();
        //先保障正确的流程
        BaseApplication.getApplication().getAppAction().getUserInfo(new ActionCallbackListener<UserBean.DataBean>() {
            @Override
            public void onSuccess(UserBean.DataBean data) {
//                Toast.makeText(WXEntryActivity.this, "01", Toast.LENGTH_SHORT).show();
                IntentUtil.gotoTopActivityWithoutData(WXEntryActivity.this, RadioGroupActivity.class, false);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    getUserInfo();
                }else {
                    Toast.makeText(WXEntryActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}