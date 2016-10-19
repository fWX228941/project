package com.android.haobanyi.activity.mine.order;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.order.OrderBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.android.haobanyi.view.StickyScrollView;
import com.android.haobanyi.view.TitleBar;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;

import static com.baidu.location.b.g.T;

/**
 * Created by fWX228941 on 2016/8/17.
 *
 * @作者: 付敏
 * @创建日期：2016/08/17
 * @邮箱：466566941@qq.com
 * @当前文件描述：订单详情 activity_order.xml
 * 我真是笑死了，如果没有则隐藏。
 */
public class OrderActivity extends BaseActivity {
    @BindView(R.id.shop_logo)
    ImageView shopLogo;
    @BindView(R.id.id_iv_logo)
    ImageView productLogo;
    @BindView(R.id.id_tv_status)
    TextView idTvStatus;
    @BindView(R.id.id_l2_parent)
    RelativeLayout idL2Parent;
    @BindView(R.id.id_contactname)
    TextView idContactname;
    @BindView(R.id.id_contactmobile)
    TextView idContactmobile;
    @BindView(R.id.id_contactaddress)
    TextView idContactaddress;
    @BindView(R.id.content_li_03)
    LinearLayout contentLi03;
    @BindView(R.id.li_has_contact)
    LinearLayout liHasContact;
    @BindView(R.id.id_address)
    RelativeLayout idAddress;
    @BindView(R.id.buyermsg)
    TextView buyer_msg;
    @BindView(R.id.tv_title_parent)
    TextView parent_name;
    @BindView(R.id.id_shop_click)
    RelativeLayout idShopClick;
    @BindView(R.id.productname)
    TextView productname;
    @BindView(R.id.id_tv_discount_price)
    TextView discountPrice;
    @BindView(R.id.tv_items_child_desc)
    TextView tvItemsChildDesc;
    @BindView(R.id.id_tv_count)
    TextView idTvCount;
    @BindView(R.id.evaluation)
    TextView evaluation;
    @BindView(R.id.id_ll_normal)
    LinearLayout idLlNormal;
    @BindView(R.id.id_product_click)
    RelativeLayout idProductClick;
    @BindView(R.id.id_Price_)
    TextView idprice;
    @BindView(R.id.id_Price)
    RelativeLayout idPrice;
    @BindView(R.id.id_shop_bonus_)
    TextView idshopBonus;
    @BindView(R.id.id_shop_bonus)
    RelativeLayout idShopBonus;
    @BindView(R.id.id_VouchersPrice_)
    TextView vouchersPrice;
    @BindView(R.id.id_VouchersPrice)
    RelativeLayout idVouchersPrice;
    @BindView(R.id.haobaoyi_bonus_)
    TextView haobaoyibonus;
    @BindView(R.id.haobaoyi_bonus)
    RelativeLayout haobaoyiBonus;
    @BindView(R.id.id_price_actual_)
    TextView price_actual;
    @BindView(R.id.id_price_actual)
    RelativeLayout idPriceActual;
    @BindView(R.id.id_telephone_click)
    RelativeLayout idTelephoneClick;
    @BindView(R.id.id_paymode_)
    TextView pay_mode;
    @BindView(R.id.id_paymode)
    RelativeLayout idPaymode;
    @BindView(R.id.id_invoiceinfo_)
    TextView Invoiceinfo;
    @BindView(R.id.id_invoiceinfo)
    RelativeLayout idInvoiceinfo;
    @BindView(R.id.id_invoiceinfo_title)
    TextView idInvoiceinfoTitle;
    @BindView(R.id.id_invoiceinfo_content)
    TextView idInvoiceinfoContent;
    @BindView(R.id.id_order_number)
    TextView idOrderNumber;
    @BindView(R.id.id_order_createdate)
    TextView idOrderCreatedate;
    @BindView(R.id.ScrollView)
    StickyScrollView ScrollView;
    @BindView(R.id.tv_get_vt)
    TextView get_vt;
    @BindView(R.id.tv_get_vr)
    TextView get_vr;
    private static final int LOGIN_TO_ORDER = 3;
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    private long orderId;
    OrderBean data = new OrderBean();
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;

    @Override
    protected int setLayout() {
        return R.layout.activity_order;
    }
    /*方案一：没有获取不准跳转。方案二：添加刷新按钮*/
    @Override
    protected void initVariables(Bundle savedInstanceState) {
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }

        if (!hasTokenRefreshed){
            long refreshTime = basePreference.getLong(Constants.START_TIME,-1000l);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                requestCount = 5;
                getToken();
            } else {
                handleLogic();
            }

        } else {
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
                } else {
                    handleLogic();
                }


            }
        });
    }

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(OrderActivity.this, LoginActivity.class, LOGIN_TO_ORDER);//跳转到登录界面
    }

    private void handleLogic() {
        orderId = this.getIntent().getLongExtra(Constants.ORDER_ID, -1);
        initTittleBar();
        getDataFromServer();


    }

    private void getDataFromServer() {


        if (isLogin){
            if (orderId != -1) {
                requestCount = 45;
                getOrder();
            }

        }else {
            if (progressDialog.isShowing()){
                progressDialog.cancel();
            }

            progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看订单详情！");
        }

    }

    private void getOrder() {
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }
        this.appAction.getOrder(orderId, new ActionCallbackListener<OrderBean>() {
            @Override
            public void onSuccess(OrderBean Data) {
                progressActivity.showContent();
                data = Data;
                if (null != data) {
                    init(data);
                }
                progressDialog.cancel();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {

                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    getOrder();
                }else if ("996".equals(errorCode)){
                    progressActivity.showContent();
                    progressDialog.cancel();
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                }else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    showError("易，请检查网络");
                } else {

                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                    showError("加载失败，请重新点击加载");
                }


            }
        });
    }

    private void showError(String message) {
        progressActivity.showContent();
        if (progressDialog.isShowing()){
            progressDialog.cancel();
        }

        progressActivity.showError(message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCount = 45;
                getOrder();
            }
        });
    }

    private void init(final OrderBean data) {
        Glide.with(OrderActivity.this)
                .load(data.getProductImage())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.logo)
                .into(productLogo);
        Glide.with(OrderActivity.this)
                .load(data.getShopLogo())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.shop_icon)
                .into(shopLogo);

        switch (data.getStatus()){
            case 1://待付款
                idTvStatus.setText("待买家付款");
                evaluation.setVisibility(View.GONE);
                get_vr.setText("取消订单");
                get_vt.setText("去支付");
                break;
            case 2://待办理
                idTvStatus.setText("买家已付款");
                evaluation.setVisibility(View.GONE);
                get_vr.setVisibility(View.GONE);
                get_vt.setText("申请退款");
                break;
            case 3://办理中
                idTvStatus.setText("买家已付款");
                evaluation.setVisibility(View.GONE);
                get_vr.setVisibility(View.GONE);
                get_vt.setText("申请退款");
                break;
            case 4://待确认
                idTvStatus.setText("待买家确认");
                evaluation.setVisibility(View.GONE);
                get_vr.setText("申请退款");
                get_vt.setText("确认订单");
                break;
            case 5://已完成
                idTvStatus.setText("交易成功");
                evaluation.setVisibility(View.VISIBLE);
                get_vr.setText("删除订单");
                get_vt.setText("申请退款");
                break;
            case 6://待退款
                idTvStatus.setText("待审核");
                evaluation.setVisibility(View.GONE);
                get_vr.setVisibility(View.GONE);
                get_vt.setVisibility(View.GONE);//统统隐藏
                break;
            case 7://已退款
                idTvStatus.setText("交易完成");
                evaluation.setVisibility(View.GONE);
                get_vr.setVisibility(View.GONE);
                get_vt.setVisibility(View.GONE);
                break;
            case 8://无效订单
                idTvStatus.setVisibility(View.GONE);
                evaluation.setVisibility(View.GONE);
                get_vr.setVisibility(View.GONE);
                get_vt.setVisibility(View.GONE);
                break;
        }

        productname.setText(data.getProductName());
        discountPrice.setText("￥" + data.getProductPrice());
        idTvCount.setText(String.format(this.getString(R.string.product_count), data.getQuantity()));

        idContactname.setText(data.getContactName());
        if (!TextUtils.isEmpty(data.getContactMobile())){
            idContactmobile.setText(data.getContactMobile());
        } else {
            idContactmobile.setText(data.getContactPhone());
        }

        idContactaddress.setText(data.getContactDistrict()+data.getContactAddress());
        if (!TextUtils.isEmpty(data.getBuyerMsg())){
            buyer_msg.setText(data.getBuyerMsg());
        } else {
            buyer_msg.setText("暂无留言消息");
        }
        parent_name.setText(data.getShopName());
         //代金券什么的都先放着
        price_actual.setText(data.getPrice());
        /*String 为空 的情况，千万不能调用isEmpty*/
        if (data.getPayMode()==1){
            pay_mode.setText("在线支付");
        }else if (data.getPayMode()==2){
            pay_mode.setText("上门收款");
        }
        /*还少一个发票信息*/
        idInvoiceinfoTitle.setText("发票抬头："+data.getTitle());
        idInvoiceinfoContent.setText("发票内容："+data.getContent());
        /*差一个订单编号*/
        idOrderCreatedate.setText("创建时间："+data.getCreateDate());
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
                OrderActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitle("订单详情");
        titleBar.setDividerColor(Color.GRAY);
    }

    @Override
    protected void initViews() {
        //在这里进行赋值操作


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



    @OnClick({R.id.id_shop_click, R.id.id_product_click, R.id.id_telephone_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_shop_click:
                break;
            case R.id.id_product_click:
                break;
            case R.id.id_telephone_click:
                break;
        }
    }
    /*1.从登录界面回来后，重新刷新就是了*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case LOGIN_TO_ORDER:
                handleLogic();
            break;

        }
    }
}
