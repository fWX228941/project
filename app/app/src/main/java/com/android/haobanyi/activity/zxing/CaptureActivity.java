package com.android.haobanyi.activity.zxing;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.shopping.product.ShoppingRadioGroupActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;


import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * Created by fWX228941 on 2016/8/15.
 *
 * @作者: 付敏
 * @创建日期：2016/08/15
 * @邮箱：466566941@qq.com
 * @当前文件描述：二維碼界面
 */
public class CaptureActivity extends BaseActivity implements QRCodeView.Delegate {
    private static final int SELECT_PHOTO = 01;
    @BindView(R.id.zxingview)
    ZXingView mQRCodeView;
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.open_flashlight)
    TextView openFlashlight;
    @BindView(R.id.close_flashlight)
    TextView closeFlashlight;
    @BindView(R.id.choose_qrcde_from_gallery)
    TextView chooseQrcdeFromGallery;
    private boolean isLogin = false;
    private static final int LOGIN_TO_SPV = 3;
    PreferenceUtil complexPreferences;
    @Override
    protected int setLayout() {
        return R.layout.activity_capture01;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        NTPTime.getInstance().getCurrentTime();
        complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        //从上面传递id 过来
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);
        if (isLogin) {
            long refreshTime = complexPreferences.getLong(Constants.START_TIME, -1000l);
            if (System.currentTimeMillis() / 1000L - refreshTime > 7200) {
                requestCount = 5;
                getToken();
            } else {
                handleLogic();
            }


        } else {
            loginForToken();
            handleLogic();

        }


    }

    private void getToken() {
        this.appAction.getToken(new ActionCallbackListener<LoginResponseResult>() {

            @Override
            public void onSuccess(LoginResponseResult data) {
                handleLogic();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {

                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    getToken();
                } else if ("003".equals(errorCode)) {
                    Toast.makeText(context, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
                }else {
                    handleLogic();
                }
            }
        });
    }

    private void loginForToken() {
        complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(CaptureActivity.this, LoginActivity.class, LOGIN_TO_SPV);//跳转到登录界面
    }

    private void handleLogic() {
        mQRCodeView.setDelegate(this);//扫描功能设置
        mQRCodeView.showScanRect();
        mQRCodeView.startCamera();
        mQRCodeView.startSpot();

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


    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.setDelegate(this);
        mQRCodeView.startSpot();
        mQRCodeView.showScanRect();
        mQRCodeView.startCamera();



    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        mQRCodeView.stopSpot();
        mQRCodeView.hiddenScanRect();
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }
    /*可以少条码，也可以少二维码*/
    @OnClick({R.id.back, R.id.open_flashlight, R.id.close_flashlight, R.id.choose_qrcde_from_gallery})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                mQRCodeView.startSpot();
                finish();
                CaptureActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
                break;
            case R.id.open_flashlight:
                mQRCodeView.openFlashlight();
                break;
            case R.id.close_flashlight:
                mQRCodeView.closeFlashlight();
                break;
            case R.id.choose_qrcde_from_gallery:
                /*从相册中取*/
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
        }
    }
    /*在这里遇到的奇葩问题，真是让我难道打开*/
    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i("CaptureActivity", "result:" + result);//难道非要点击一下才行 就是一个地址
/*        Toast.makeText(this, "扫描结果"+result, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "扫描成功", Toast.LENGTH_SHORT).show();*/
        //震动
        //vibrate();//其实是在这里失效的，也就是权限问题  果然是需要申请权限的

        /*
        * 截取两个标签之间字符串
        * http://mlxnle.iteye.com/blog/1697880
        * http://josh-persistence.iteye.com/blog/1881270
        * http://www.runoob.com/java/java-regular-expressions.html
        * 规则：http://www.jb51.net/article/31235.htm
        * 例：http://blog.csdn.net/ioe_gaoyong/article/details/7930689
        *  最终考察：
        *  http://blog.csdn.net/u013549463/article/details/52036770
        *
        *          //在线：http://tool.lu/regex/
        //http://tool.oschina.net/regex
        * */

        String logintoken = result.substring(result.indexOf("loginToken=")+11, result.indexOf("&nsTS="));
        String nsts = result.substring(result.indexOf("nsTS=")+5);

//        Toast.makeText(CaptureActivity.this, logintoken, Toast.LENGTH_SHORT).show();
//        Toast.makeText(CaptureActivity.this, nsts, Toast.LENGTH_SHORT).show();
        requestCount = 5;
        qrCodeLogin(logintoken,nsts);

        mQRCodeView.startSpot();
    }

    private void qrCodeLogin( final String logintoken,final String nsts) {
//        Toast.makeText(CaptureActivity.this, "执行了吗？", Toast.LENGTH_SHORT).show();
        this.appAction.qrCodeLogin(logintoken, nsts, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                //跳转到新的界面
                Log.d("CaptureActivity", "登录成功");
                Toast.makeText(CaptureActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                IntentUtil.gotoTopActivityWithoutData(CaptureActivity.this, RadioGroupActivity.class, true);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {

                if ("998".equals(errorCode) && requestCount > 0 ){
                    requestCount--;
                    qrCodeLogin(logintoken, nsts);
                }else if ("996".equals(errorCode)){
                    ToastUtil.showShort("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.showShort("网络异常，请检查网络");
                    mQRCodeView.startSpot();
                    mQRCodeView.showScanRect();
                    mQRCodeView.startCamera();
                }else {
                    Toast.makeText(CaptureActivity.this, "异常代码：" + errorCode +
                            " 异常说明: " + errorMessage, Toast
                            .LENGTH_SHORT).show();
                    ToastUtil.showShort("加载失败，请重新点击加载");
                    mQRCodeView.startSpot();
                    mQRCodeView.showScanRect();
                    mQRCodeView.startCamera();
                }

            }
        });
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mQRCodeView.showScanRect();
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PHOTO) {

            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                if (selectedImage == null) {
                    return;
                }
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                if (selectedImage != null && filePathColumn != null) {
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    if (cursor == null) {
                        return;
                    }
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor
                                .getColumnIndex(filePathColumn[0]);
                        final String picturePath  = cursor.getString(columnIndex);

                    /*
                    这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
                    请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
                     */
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... params) {
                                return QRCodeDecoder.syncDecodeQRCode(picturePath);
                            }

                            @Override
                            protected void onPostExecute(String result) {
                                if (TextUtils.isEmpty(result)) {
                                    Toast.makeText(CaptureActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CaptureActivity.this, result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.execute();


                    }
                    cursor.close();
                }
            }

        }
    }
}
