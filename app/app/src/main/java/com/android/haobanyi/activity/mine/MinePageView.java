package com.android.haobanyi.activity.mine;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.activity.collection.ShopCollectionActivity;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.guid.register.BindMobileOrEmailActivity;
import com.android.haobanyi.activity.guid.register.RegisterActivity01;
import com.android.haobanyi.activity.mine.contact.MyContactListAcitivity;
import com.android.haobanyi.activity.mine.history.MyFootHistoryActivity;
import com.android.haobanyi.activity.mine.message.MyMessageActivity;
import com.android.haobanyi.activity.mine.order.MyOrderListActivity;
import com.android.haobanyi.activity.mine.order.MyRefundListActivity;
import com.android.haobanyi.activity.mine.property.AccountBalanceActivity;
import com.android.haobanyi.activity.mine.property.MyPropertyActivity;
import com.android.haobanyi.activity.mine.property.MyRedEnvelopeListActivity;
import com.android.haobanyi.activity.mine.property.UserPointActivity;
import com.android.haobanyi.activity.mine.property.VoucherActivity;
import com.android.haobanyi.activity.mine.settings.UserSettingActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.mine.myinformation.UserBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.AutoUtil.BitmapUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ActionSheet;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.android.haobanyi.util.Zxing.utils.ConUtils;


/**
 * Created by fWX228941 on 2016/3/31.
 *
 * @作者: 付敏
 * @创建日期：2016/03/31
 * @邮箱：466566941@qq.com
 * @当前文件描述：我的模块
 *
 *
 */
public class MinePageView extends LinearLayout implements ActionSheet.MenuItemClickListener {
    private static final String TAG = "fumin";
    @BindView(R.id.view)
    CircularImageView imageView;
    @BindView(R.id.textView)
    TextView regist;
    @BindView(R.id.textView2)
    TextView login;
    @BindView(R.id.textView3)
    TextView nickName;
    @BindView(R.id.id_txt_service_select)
    LinearLayout idTxtServiceSelect;
    @BindView(R.id.id_txt_shop_collection)
    LinearLayout idTxtShopCollection;
    @BindView(R.id.id_txt_my_service)
    LinearLayout idTxtMyService;
    @BindView(R.id.text_mine_order)
    LinearLayout textMineOrder;
    @BindView(R.id.text_mine_to_pay)
    TextView textMineToPay;
    @BindView(R.id.text_mine_to_handle)
    TextView textMineToHandle;
    @BindView(R.id.text_mine_be_handled)
    TextView textMineBeHandled;
    @BindView(R.id.text_mine_to_evaluated)
    TextView textMineToEvaluated;
    @BindView(R.id.text_mine_refund)
    TextView textMineRefund;
    @BindView(R.id.text_mine_property)
    LinearLayout textMineProperty;
    @BindView(R.id.text_mine_ready_pay)
    TextView textMineReadyPay;
    @BindView(R.id.text_mine_voucher)
    TextView textMineVoucher;
    @BindView(R.id.text_mine_red_envelope)
    TextView textMineRedEnvelope;
    @BindView(R.id.text_mine_credit)
    TextView textMineCredit;
    @BindView(R.id.text_modity_psw)
    LinearLayout textMineContact;
    @BindView(R.id.text_mine_settings)
    LinearLayout textMineSettings;
    @BindView(R.id.id_my_message)
    ImageView idMyMessage;
    PreferenceUtil complexPreferences;
    @BindView(R.id.totalCount_product)
    TextView totalCountProduct;
    @BindView(R.id.totalCount_shop)
    TextView totalCountShop;
    private long requestCount = 5;
    /**
     * 上下文
     */
    private Context context;
    /**
     * 布局加载器
     */
    private LayoutInflater inflater;

    /*
    * 图片路径
    * */
    private String filePath;
    private Bitmap tmpBitmap;
    private String url = "";
    UserBean.DataBean user;
    RadioGroupActivity activity = new RadioGroupActivity();
    private static final int SELECT_PHOTO = 0;// 选择一张照片时用到
    private static final int SELECT_CAMERO = 1;// 拍照片时用到
    private boolean isLogin = false;
    private static final int LOGIN_TO_SPV = 3;
    /*Bundle bundle = new Bundle();*/
    private int PAGE_NO = 1;//第一页
    String totalCount_product = "0";
    String totalCount_shop = "0";


    public MinePageView(Context context) {
        super(context);
        this.context = context;
        this.activity = (RadioGroupActivity) context;
        inflater = LayoutInflater.from(context);
        init();
    }

    public MinePageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.activity = (RadioGroupActivity) context;
        inflater = LayoutInflater.from(context);
        init();
    }


    /*
    * 包括两个部分：
    * 设置布局+初始化控件
    *
    * */
    private void init() {
        initData();
        setLayout();
        refreshCount();
        initViews();
        setUpListener();

    }

    private void setUpListener() {
        activity.setOnPictureStateChangedListener(new RadioGroupActivity.OnPictureStateChangedListener() {
            @Override
            public void onPictureChanged(String picpath) {
                Log.d(TAG, "被触发加载");
                if (!TextUtils.isEmpty(picpath)) {
//                    showImage(picpath);
                    requestCount = 45;
                    uploadUserPhoto(picpath);

                } else {
                    ToastUtil.showHintMessage("获取图片失败");
                }

            }
        });


        activity.setOnRreshChangedListener(new RadioGroupActivity.OnRreshListener() {
            @Override
            public void onRresh() {
                Log.d("CollectionPageView", "点击后腿时，被触发");
                // 触发后，应该重新获取login的状态值
                isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);//这一行不能少
                initViews();
                refreshCount();

            }
        });
    }

    private void uploadUserPhoto(final String picpath) {
        BaseApplication.getApplication().getAppAction().uploadUserPhoto(picpath, new ActionCallbackListener<String>() {


            @Override
            public void onSuccess(String data) {
                //
                ToastUtil.showSuccessfulMessage("上传成功");
                Glide.with(context)
                        .load(data)
                        .centerCrop()
                        .crossFade()
                        .placeholder(R.drawable.service_logo)// 也就是网络加载失败时出现的图片
                        .into(imageView);
                if (null!=data && null!=user && null!=complexPreferences){
                    user.setPhoto(data);
                    complexPreferences.putObject(Constants.USER_INFO, user);
                }


            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                //上传失败，请重新上传
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    uploadUserPhoto(picpath);
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    //不变
                    ToastUtil.showHintMessage("上传失败",errorCode, errorMessage);

                }

            }
        });
    }

    private void initData() {
        NTPTime.getInstance().getCurrentTime();
        complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");

        //首先就得判断是否已经登录
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);
        refreshToken();


    }
    private void refreshToken() {
        if (isLogin){
            long refreshTime = complexPreferences.getLong(Constants.START_TIME,-1000l);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                //超过过期时间，更新token
                requestCount = 5;
                getToken();
            }

        }
    }

    private void getToken() {
        BaseApplication.getApplication().getAppAction().getToken(new ActionCallbackListener<LoginResponseResult>() {

            @Override
            public void onSuccess(LoginResponseResult data) {

            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    getToken();
                }
            }
        });
    }

    //服务数量和店铺数量，这个地方需要添加缓存。暂时没有添加。
    private void refreshCount() {
        if (isLogin){
            requestCount = 10;
            getFavoriteProductCount();
            getFavoriteShopCount();
        }else {
            totalCountShop.setText("0");
            totalCountProduct.setText("0");
        }

    }

    private void getFavoriteShopCount() {
        BaseApplication.getApplication().getAppAction().getFavoriteShopCount(PAGE_NO, new
                ActionCallbackListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        totalCount_shop = data;
                        totalCountShop.setText(totalCount_shop);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {

                        if ("998".equals(errorCode) && requestCount > 0) {
                            requestCount--;
                            getFavoriteShopCount();
                        } else {
                            totalCount_shop = complexPreferences.getString(Constants.TOTAL_COUNT_SHOP, totalCount_shop);
                            totalCountShop.setText(totalCount_shop);
                        }
                        Log.d(TAG, "异常代码：" + errorCode + " 异常说明: " + errorMessage);//有的真的不需要说明，  有些说明是不是有点多，我想
                    }
                });
    }

    private void getFavoriteProductCount() {
        BaseApplication.getApplication().getAppAction().getFavoriteProductCount(PAGE_NO, new
                ActionCallbackListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        totalCount_product = data;
                        totalCountProduct.setText(totalCount_product);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        if ("998".equals(errorCode) && requestCount > 0) {
                            requestCount--;
                            getFavoriteProductCount();
                        }else {
                            totalCount_product = complexPreferences.getString(Constants.TOTAL_COUNT_SHOP, totalCount_product);
                            totalCountProduct.setText(totalCount_product);
                        }
                        Log.d(TAG, "异常代码：" + errorCode + " 异常说明: " + errorMessage);//有的真的不需要说明，  有些说明是不是有点多，我想
                    }
                });
    }

    private void loginForToken() {
        complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(context, LoginActivity.class, LOGIN_TO_SPV);//跳转到登录界面
    }
    /*设置布局，添加视图*/
    private void setLayout() {
        View view = (LinearLayout) inflater.inflate(R.layout.tab_mine_page_01, null);
        ButterKnife.bind(this, view);
        this.addView(view, new LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }


    private void initViews() {
        if (isLogin) {
            //更新头像
            requestCount = 10;
            getUserInfo();

        } else {
            regist.setVisibility(VISIBLE);
            login.setVisibility(VISIBLE);
            nickName.setVisibility(GONE);
            complexPreferences.removeItem(Constants.PHONE_PATH);
            imageView.setImageResource(R.drawable.mine_head_default);
        }

    }
    private void getUserInfo() {
        BaseApplication.getApplication().getAppAction().getUserInfo(new ActionCallbackListener<UserBean.DataBean>() {
             @Override
             public void onSuccess(UserBean.DataBean data) {
                 user = data;
                 if (null != user) {
                     loadUserInfo();
/*                     String imagePath = complexPreferences.getString(Constants.PHONE_PATH, "");
                     showImage(imagePath);// 这个保存的是本地照片，很重要。*/
                 }else {
                     user = complexPreferences.getObject(Constants.USER_INFO, UserBean.DataBean.class);
                     loadUserInfo();

                 }


             }
             /*加载了多次，只保留一次看看*/
             @Override
             public void onFailure(String errorCode, String errorMessage) {
                 if ("998".equals(errorCode) && requestCount > 0) {
                     requestCount--;
                     getUserInfo();
                 } else if ("003".equals(errorCode)) {
                     Log.d(TAG, "难道是多次调用了");
                     ToastUtil.networkUnavailable();
                     user = complexPreferences.getObject(Constants.USER_INFO, UserBean.DataBean.class);
                     loadUserInfo();
                 } else if ("996".equals(errorCode)) {
                     ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                     loginForToken();
                 } else {
                     //如果加载失败使用缓存
                     user = complexPreferences.getObject(Constants.USER_INFO, UserBean.DataBean.class);
                     loadUserInfo();

                 }

             }
         });
    }

    private void loadUserInfo() {
        regist.setVisibility(GONE);
        login.setVisibility(GONE);
        nickName.setVisibility(VISIBLE);
        nickName.setText(user.getNickName());
        Glide.with(context)
                .load(user.getPhoto())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.service_logo)// 也就是网络加载失败时出现的图片
                .into(imageView);
    }


    @OnClick({R.id.view, R.id.textView, R.id.textView3, R.id.textView2, R.id.id_txt_service_select, R.id
            .id_txt_shop_collection, R.id
            .id_txt_my_service, R.id.text_mine_order, R.id.text_mine_to_pay, R.id.text_mine_to_handle, R.id
            .text_mine_be_handled, R.id.text_mine_to_evaluated, R.id.text_mine_refund, R.id.text_mine_property, R.id
            .text_mine_ready_pay, R.id.text_mine_voucher, R.id.text_mine_red_envelope, R.id.text_mine_credit, R.id
            .text_modity_psw, R.id.text_mine_settings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view:
                pickPhone();
                break;
            case R.id.textView://注册
                goToRegister();
                break;
            case R.id.textView2://登录
                Log.d(TAG, "登录点击");
                goToLogin();
                break;
            case R.id.id_txt_service_select:
                goToCellectionPageView();
                break;
            case R.id.id_txt_shop_collection:
                goToShopCellection();
                break;
            case R.id.id_txt_my_service:
                gotoMyfoot();
                break;
            case R.id.text_mine_order:
                gotoOrder(0);
                break;
            case R.id.text_mine_to_pay:
                gotoOrder(1);
                break;
            case R.id.text_mine_to_handle:
                gotoOrder(2);
                break;
            case R.id.text_mine_be_handled:
                gotoOrder(3);
                break;
            case R.id.text_mine_to_evaluated:
                gotoOrder(4);
                break;
            case R.id.text_mine_refund:
                totoMyFund();
                break;
            case R.id.text_mine_property:
                gotoProperty();
                break;
            case R.id.text_mine_ready_pay:
                gotoAccountBalance();
                break;
            case R.id.text_mine_voucher:
                gotoVoucher();
                break;
            case R.id.text_mine_red_envelope:
                gotoBonus();
                break;
            case R.id.text_mine_credit:
                gotoPoint();
                break;
            case R.id.text_modity_psw:
                gotoContact();
                break;
            case R.id.text_mine_settings:
                gotoSettings();
                break;
        }
    }

    private void totoMyFund() {
        IntentUtil.gotoActivityWithoutData(context, MyRefundListActivity.class, false);
    }

    private void gotoSettings() {
        IntentUtil.gotoActivityWithoutData(context, UserSettingActivity.class, false);
    }

    private void gotoContact() {
        IntentUtil.gotoActivityWithoutData(context, MyContactListAcitivity.class, false);
    }

    private void gotoPoint() {
        IntentUtil.gotoActivityWithoutData(context, UserPointActivity.class, false);
    }

    private void gotoBonus() {
        IntentUtil.gotoActivityWithoutData(context, MyRedEnvelopeListActivity.class, false);
    }

    private void gotoVoucher() {
        IntentUtil.gotoActivityWithoutData(context, VoucherActivity.class, false);
    }

    private void gotoAccountBalance() {
        IntentUtil.gotoActivityWithoutData(context, AccountBalanceActivity.class, false);
    }

    private void gotoProperty() {
        IntentUtil.gotoActivityWithoutData(context, MyPropertyActivity.class, false);
    }

    private void gotoOrder(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.POSITION, position);
        Log.d(TAG, "position:" + position);
        IntentUtil.gotoActivityWithData(context, MyOrderListActivity.class, bundle, false);
    }

    private void gotoMyfoot() {
        IntentUtil.gotoActivityWithoutData(context, MyFootHistoryActivity.class, false);
    }

    private void goToShopCellection() {
        IntentUtil.gotoActivityWithoutData(context, ShopCollectionActivity.class, false);
    }

    /* 跳转到收藏
    *  关于跳转：只要是跳转到首页的几个页面，无论是怎么跳转过来的，都应该清除之前的页面
    *
    * */
    private void goToCellectionPageView() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.STATE_FROM_SHOPPING, 04);
        IntentUtil.gotoActivityWithData(context, RadioGroupActivity.class, bundle, false);
    }


    private void pickPhone() {

        if (isLogin) {
             pickPhoneLogic();
        } else {
            complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, true);
            IntentUtil.gotoActivityForResult(activity, LoginActivity.class, LOGIN_TO_SPV);//跳转到登录界面
        }


    }

    private void pickPhoneLogic() {
        context.setTheme(R.style.ActionSheetStyleIOS7);
        Log.d(TAG, "挑选照片");
        ActionSheet menuView = new ActionSheet(context);
        menuView.setCancelButtonTitle("取消");// before
        // add items
        menuView.addItems("本地照片");
        menuView.setItemClickListener(this);
        menuView.setCancelableOnTouchMenuOutside(true);
        menuView.showMenu();
    }


    private void goToRegister() {
        Log.d(TAG, "点击登陆");
        IntentUtil.gotoActivityWithoutData(context, RegisterActivity01.class, false);
    }

    //进入登录界面
    private void goToLogin() {
        IntentUtil.gotoActivityWithoutData(context, LoginActivity.class, false);
    }

    //进入到我的消息goToMyMessage
    @OnClick(R.id.id_my_message)
    public void onClick() {
        IntentUtil.gotoActivityWithoutData(context, MyMessageActivity.class, false);
    }

    /*用于拍照，选择相册*/
    @Override
    public void onItemClick(int itemPosition) {
        switch (itemPosition) {
            case SELECT_PHOTO:
                selectPicture();
                break;
/*            case SELECT_CAMERO:
                takePicture();
                break;*/
        }

    }

    /*
    * 现场拍照有问题，因该是路径
    * */
/*    private void takePicture() {
        filePath = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File mediaFile = ConUtils.getOutputMediaFile(activity);
        filePath = mediaFile.getAbsolutePath();
        Uri fileUri = Uri.fromFile(mediaFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intent, SELECT_CAMERO);
        *//*showImage(filePath);*//*

    }*/
    /*在程序启动的时候就一定添加，部分功能将无法正常使用，添加一次后，不会再让你添加第二次了*/
    private void selectPicture() {
        //需要申请权限,果然还是有用的，其实自己写也是挺好的，尤其是定位的功能，在首页
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        activity.startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        ToastUtil.showShort(permissions.toString() + "权限拒绝");

                    }
                });

        //context.startActivityForResult(photoPickerIntent, SELECT_PHOTO);
/*        Uri selectedImage = photoPickerIntent.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        if (selectedImage != null && filePathColumn != null) {
            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor == null) {
                return;
            }
            if (cursor.moveToFirst()) {
                int columnIndex = cursor
                        .getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                showImage(filePath);

            }
            cursor.close();
        }*/
    }
    /*
    * 用法：
    * String imagePath = complexPreferences.getString(Constants.PHONE_PATH, "");
      showImage(imagePath);
    *
    *
    * */
    /*原来zxing 有一个相册类ConUtils还是挺好的*/
    private void showImage(String imagePath) {
        Log.d(TAG, "显示照片");
        if (!TextUtils.isEmpty(imagePath)) {
/*            Glide.with(context)
                    .load(imagePath)
                    .centerCrop()
                    .crossFade()
                    .placeholder(R.drawable.mine_head_default)// 也就是网络加载失败时出现的图片
                    .into(imageView);*/
            //tmpBitmap = ConUtils.getBitmapConsiderExif(imagePath); //用这个实现本地加载图片 tmpBitmap = BitmapUtil
            // .getBitmapFromPath(imagePath);都是可以的,Zxing 的一個
            tmpBitmap = BitmapUtil.getBitmapFromPath(imagePath);
            imageView.setImageBitmap(tmpBitmap);
            complexPreferences.putString(Constants.PHONE_PATH, imagePath);

        }

    }

}
