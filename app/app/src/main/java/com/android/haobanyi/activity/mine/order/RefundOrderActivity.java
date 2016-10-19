package com.android.haobanyi.activity.mine.order;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ActionSheet;
import com.android.haobanyi.view.StickyScrollView;
import com.android.haobanyi.view.TitleBar;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * Created by fWX228941 on 2016/8/18.
 *
 * @作者: 付敏
 * @创建日期：2016/08/18
 * @邮箱：466566941@qq.com
 * @当前文件描述：申请退款界面 activity_refund_order
 */
public class RefundOrderActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate,ActionSheet.MenuItemClickListener {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.price)
    TextView tv_price;
    @BindView(R.id.refundreason)
    LinearLayout refundreason;
    @BindView(R.id.refundprice)
    EditText refundprice;
    @BindView(R.id.refund_info)
    EditText refundInfo;
    @BindView(R.id.snpl_moment_add_photos)
    BGASortableNinePhotoLayout mPhotosSnpl;
    @BindView(R.id.ScrollView)
    StickyScrollView ScrollView;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.refundreason_)
    TextView refund_reason;
    @BindView(R.id.footer)
    RelativeLayout footer;
    private static final int LOGIN_TO_REFUND_ORDER = 3;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    private static final int MAX_PHOTO_COUNT = 3;  // 最多3张图片
    private int refund_reason_int = -1;
    private long orderId=-1;
    private String price;


    @Override
    protected int setLayout() {
        return R.layout.activity_refund_order;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        if (hasTokenRefreshed){
            handleLogic();
        } else {
            long refreshTime = basePreference.getLong(Constants.START_TIME,-1000l);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                //超过过期时间，更新token
                requestCount = 5;
                getToken();
            } else {
                handleLogic();
            }
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
                } else {
                    handleLogic();
                }

            }
        });
    }

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(RefundOrderActivity.this, LoginActivity.class, LOGIN_TO_REFUND_ORDER);//跳转到登录界面
    }

    private void handleLogic() {
        initTittleBar();
        orderId = this.getIntent().getLongExtra("OrderID",-1);
        Log.d("RefundOrderActivity", "orderId:" + orderId);
        price = this.getIntent().getStringExtra("Price");
        Log.d("RefundOrderActivity", price);
        tv_price.setText("最多可退"+price+"￥");
        refundprice.setText(price);

    }
    private void initTittleBar() {
        // 设置背景色  透明灰色  颜色 什么的最后定夺
        titleBar.setBackgroundResource(R.color.white);
        // 设置返回箭头和文字以及颜色
        titleBar.setLeftImageResource(R.drawable.back_icon);
        // 设置监听器
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                RefundOrderActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitle("申请退款");
        titleBar.setDividerColor(Color.GRAY);
    }

    @Override
    protected void initViews() {
        initPhoneSelecter();
    }
    /*图片选择器*/
    private void initPhoneSelecter() {
        mPhotosSnpl.setDelegate(this);
        mPhotosSnpl.setIsPlusSwitchOpened(true);
        mPhotosSnpl.setIsSortable(true);
        mPhotosSnpl.init(this);
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
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position,
                                        ArrayList<String> models) {
        //这个就需要权限了,添加多个权限
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        choicePhotoWrapper();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        ToastUtil.showShort(permissions.toString() + "权限拒绝");

                    }
                });

    }

    private void choicePhotoWrapper() {
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "haobanyi");
        startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, MAX_PHOTO_COUNT, mPhotosSnpl
                .getData()), REQUEST_CODE_CHOOSE_PHOTO);

    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int
            position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position,
                                     String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, MAX_PHOTO_COUNT, models, models,
                position, false), REQUEST_CODE_PHOTO_PREVIEW);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*这个才是选择的图片*/
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            //源代码依赖适配器，我也是醉了
            mPhotosSnpl.setData(BGAPhotoPickerActivity.getSelectedImages(data));
            ArrayList<String> paths = BGAPhotoPickerActivity.getSelectedImages(data);
            for (int i =0;i<paths.size();i++) {
                Log.d("RefundOrderActivity", paths.get(i));//这个应该就是地址了，还是要测试下才行啊！
            }
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
        }else if (requestCode == LOGIN_TO_REFUND_ORDER){
            handleLogic();
        }
    }
    @OnClick({R.id.refundreason, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refundreason:
                chooseReson();
                break;
            case R.id.submit:
                requestCount = 45;
                submitAndPost();
                break;
        }
    }
    /*调用两个接口，一个是上传图片到服务器，一个是*/
    private void submitAndPost() {
        //一起上传吗？还包括图片上传
        HashMap<String, Object> params = new HashMap<String, Object>();

        //这里需要进行判断操作

        if (refund_reason.getText().toString().equals("请点击选择一条原因（必选项）")){
            ToastUtil.showHintMessage("请填写退款原因");
            return;
        }else {
            params.put("RefundReason",refund_reason_int);
        }

        if (Double.parseDouble(refundprice.getText().toString())>Double.parseDouble(price)|| Double.parseDouble(refundprice.getText().toString())<0 ){
            ToastUtil.showHintMessage("最多可退"+price);
            return;
        }else {
            params.put("RefundPrice",refundprice.getText().toString());
        }

        if ( TextUtils.isEmpty(refundInfo.getText().toString())){
            ToastUtil.showHintMessage("请写明退款原因");
            return;
        }else {
            params.put("RefundSpec",refundInfo.getText().toString());
        }





        this.appAction.applyOrderRefund(params, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showHintMessage("退款成功");
                finish();
                RefundOrderActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    submitAndPost();
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                }

            }
        });




    }

    /*选择原因*/
    private void chooseReson() {
        setTheme(R.style.ActionSheetStyleIOS7);
        ActionSheet menuView = new ActionSheet(RefundOrderActivity.this);
        menuView.setCancelButtonTitle("取消");// before
        // add items
        menuView.addItems("商家未办理","商家无法办理","其他原因");
        menuView.setItemClickListener(this);
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();


    }

    @Override
    public void onItemClick(int itemPosition) {
        switch (itemPosition) {
            case 0:
                refund_reason.setText("商家未办理");
                refund_reason_int = 1;
                break;
            case 1:
                refund_reason.setText("商家无法办理");
                refund_reason_int = 2;
                break;
            case 2:
                refund_reason.setText("其他原因");
                refund_reason_int = 3;
                break;
            default:
                refund_reason.setText("请点击选择一条原因（必选项）");
                refund_reason_int = -1; //表示没有选择，做出提示
                break;

        }
    }
}
