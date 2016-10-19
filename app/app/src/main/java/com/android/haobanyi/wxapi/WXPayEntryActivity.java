package com.android.haobanyi.wxapi;
import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.activity.mine.order.MyOrderListActivity;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import dmax.dialog.SpotsDialog;

import static com.baidu.location.b.g.B;


public  class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "WXPayEntryActivity";
	protected SpotsDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		progressDialog =  new SpotsDialog(this);
		if (!progressDialog.isShowing()){
			progressDialog.show();
		}
		BaseApplication.api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		BaseApplication.api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (progressDialog.isShowing()){
			progressDialog.cancel();
		}
		/*响应支付支付回调*/
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == BaseResp.ErrCode.ERR_OK){
				//支付成功
				ToastUtil.showHintMessage("支付成功");//刷新页面
				IntentUtil.gotoActivityWithoutData(WXPayEntryActivity.this, MyOrderListActivity.class, true);
			}else {
				//支付失败
				ToastUtil.showHintMessage("支付失败");
				IntentUtil.gotoActivityWithoutData(WXPayEntryActivity.this, MyOrderListActivity.class, true);
			}

		}
	}
}