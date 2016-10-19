package com.android.haobanyi.activity.shopping.shopping;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.ailpay.ActionToPay;
import com.android.haobanyi.activity.ailpay.PayResult;
import com.android.haobanyi.activity.ailpay.SignUtils;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.mine.contact.AddContactActivity;
import com.android.haobanyi.activity.mine.contact.MyContactListAcitivity;
import com.android.haobanyi.activity.mine.order.MyOrderListActivity;
import com.android.haobanyi.activity.mine.receipt.ReceiptActivity;
import com.android.haobanyi.adapter.comformorder.ConformOrderExpandableListAdapter;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.ActionCallbackSixfoldListener;
import com.android.haobanyi.model.bean.contact.ContactBean;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.post.Orders;
import com.android.haobanyi.model.bean.shopping.ShoppingCartBean;
import com.android.haobanyi.model.bean.shopping.conformorder.RedEnvelopeBean;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.android.haobanyi.view.TitleBar;
import com.android.haobanyi.wxapi.WXPayEntryActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import android.os.Handler;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


import butterknife.BindView;
import butterknife.OnClick;

import static com.android.haobanyi.R.id.id_account_ailpay;
import static com.baidu.location.b.g.C;

/**
 * Created by fWX228941 on 2016/6/27.
 *
 * @作者: 付敏
 * @创建日期：2016/06/27
 * @邮箱：466566941@qq.com
 * @当前文件描述：确认订单界面
 * 不同点在于结算的部分只包含勾选部分
 * 这个是需要优化的
 */

/*
* 平台红包的情况暂时不考虑，各种红包设置为缺省值
*
* */
public class ConformOrderActivity extends BaseActivity {


    private static final int ADD_CONTACT = 00; //添加联系人
    private static final int ADD_RECEIOPT = 01; //添加发票
    private static final int TO_SET_PAY_PWD = 04;


    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.id_elv_listview)
    ExpandableListView Listview;
    @BindView(R.id.id_hoabanyi_bonus)
    RelativeLayout hoabanyiBonus;//好办易红包
    @BindView(R.id.confirm_footer)
    LinearLayout confirm_footer;//底部


    @BindView(R.id.id_tv_bonus)
    TextView id_tv_bonus;//好办易红包
    @BindView(R.id.id_submit_order)
    TextView idSubmitOrder;// 提交订单

    @BindView(R.id.id_tv_totalCount)
    TextView id_tv_totalCount;
    @BindView(R.id.id_tv_totalPrice)
    TextView id_tv_totalPrice;
    @BindView(R.id.tvPostPrice)
    TextView tvPostPrice;

    RelativeLayout address; //地址
    RelativeLayout hoabanyiReceipt;//发票
    private static final String TAG = "minfu";
    private DialogPlus dialog ; //弹框
    private TextView id_ailpay;
    private TextView id_weixin_ailpay;
    private TextView id_account_ailpay;
    private TextView id_need_to_pay;
    private static final int TAG_WEIXIN_AILPAY = 1;
    private static final int TAG_AILPAY = 0;
    private static final int TAG_ACCOUNt_AILPAY = 2;
    private int TAG_PAY = 0; //默认是支付宝
    private static final int LOGIN_TO_CONFORM_ORDER = 3;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    String contactname ;
    String contactmobile ;
    String contactaddress ;
    TextView id_contactname ;
    TextView id_contactmobile ;
    TextView id_contactaddress ;
    LinearLayout li_has_contact;
    LinearLayout li_has_no_contact;
    View headerView = null; //更新头视图
    Orders.OrdersBean params = null;//订单参数
    List<Orders.OrdersBean.ShopsBean> shopsBeanlist;
    private int invoiceType = 9;//默认是不需要发票的
    /*支付宝开发*/
    // 商户PID
    public static final String PARTNER = "2088121920313880";// 支付宝商户标识partnerID 绑定密钥  【密钥说白了就是一组安全信息】
    // 商户收款账号
    public static final String SELLER = "service@haobanyi.com";
    // 商户私钥，pkcs8格式
    private Bundle bundle;

    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAML0SUinVs5Ub0sN\n" +
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
    List<Map<String, Object>> parentMapList = new ArrayList<Map<String, Object>>();
    List<List<Map<String, Object>>> childMapList_list = new ArrayList<List<Map<String, Object>>>();
    //List<ContactBean> contactReList= new ArrayList<ContactBean>();;
    ContactBean contactBean =null;
    List<RedEnvelopeBean> redEnvelopeReList = new ArrayList<RedEnvelopeBean>();

    private String totalPrice = "0";
    private String totalCount = "0";
    //以下三个字段是确认后的购物车列表
    private String totaldiscountMoney = null;

    ConformOrderExpandableListAdapter adapter ;
    private TextView id_tv_receipt_name;
//    String  OrderIds= null;
    @Override
    protected int setLayout() {
        return R.layout.activity_conform_order;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }

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

    private void handleLogic() {
        initTittleBar();

        if (isLogin){
            requestCount = 45;
            getDataFromServer();
        } else {
            if (progressDialog.isShowing()){
                progressDialog.cancel();
            }
            loginForToken();
        }


    }



    private void getToken() {
        this.appAction.getToken(new ActionCallbackListener<LoginResponseResult>() {

            @Override
            public void onSuccess(LoginResponseResult data) {
                requestCount = 45;
                getDataFromServer();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {


                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    getToken();
                } else {
                    requestCount = 45;
                    getDataFromServer();
                }
            }
        });
    }

    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(ConformOrderActivity.this, LoginActivity.class, LOGIN_TO_CONFORM_ORDER);//跳转到登录界面
    }

    //从服务器获取确认订单中服务器的信息
    private void getDataFromServer() {
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }

        this.appAction.getVerifyShoppingCart(new ActionCallbackSixfoldListener<List<Map<String, Object>>,
                List<List<Map<String, Object>>>, ContactBean, List<RedEnvelopeBean>, ShoppingCartBean.DataBean,
                Orders.OrdersBean>() {

            @Override
            public void onSuccess(List<Map<String, Object>> parentMapListData, List<List<Map<String, Object>>>
                    childMapList_listData,
                                  ContactBean contact, List<RedEnvelopeBean> redEnvelopeList, ShoppingCartBean
                                          .DataBean dataBean, Orders.OrdersBean ordersBean) {
                progressDialog.cancel();
                progressActivity.showContent();
                parentMapList = parentMapListData;
                Log.d(TAG, "parentMapList.size():" + parentMapList.size());
                childMapList_list = childMapList_listData;
                contactBean = contact;
                redEnvelopeReList = redEnvelopeList;
                totalPrice = dataBean.getTotalPrice();
                totalCount = dataBean.getTotalCount();
                totaldiscountMoney = dataBean.getDiscountMoney();
                params = ordersBean;
/*                Log.d(TAG, totalPrice);
                Log.d(TAG, totalCount);
                Log.d(TAG, totaldiscountMoney);*/
                initOrder();
                /*3.设置header视图*/
                initHeaderView();
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if ("998".equals(errorEvent) && requestCount > 0) {
                    requestCount--;
                    getDataFromServer();
                } else if ("003".equals(errorEvent)) {
                    ToastUtil.networkUnavailable();
                } else if ("996".equals(errorEvent)) {
                    progressDialog.cancel();
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    ToastUtil.showErrorMessage(errorEvent, message);
                    showError("加载异常");
                }

            }
        });

    }

    private void showError(String Message) {
        //视图视图要隐藏
        confirm_footer.setVisibility(View.GONE);
        progressDialog.cancel();
        progressActivity.showError(Message, "点击重新加载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCount = 45;
                getDataFromServer();
            }
        });
    }

    @Override
    protected void initViews() {



    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initOrder() {
            if ((null!= parentMapList) && (!parentMapList.isEmpty())
                    &&(null!=childMapList_list)&&(!childMapList_list.isEmpty())){

                adapter = new ConformOrderExpandableListAdapter(ConformOrderActivity.this, parentMapList, childMapList_list);
                Listview.setAdapter(adapter);
                for (int i = 0; i < parentMapList.size(); i++) {
                    Listview.expandGroup(i);
                }

                Listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        return true;//拦截
                    }
                });
                adapter.setOnLoginForTokenListener(new ConformOrderExpandableListAdapter.OnLoginForTokenListener() {
                    @Override
                    public void onLoginForToken() {
                        loginForToken();
                    }
                });

            }


    }
    /*自己打印log 看看效果就是了*/
    private void initHeaderView() {
        //在这里添加视图
        //好办易红包
        confirm_footer.setVisibility(View.VISIBLE);
        if (redEnvelopeReList.isEmpty()){
            hoabanyiBonus.setVisibility(View.GONE);
        } else {
            hoabanyiBonus.setVisibility(View.VISIBLE); //这个还需要赋值，第一个值
            for (int i=0;i<redEnvelopeReList.size();i++){
                id_tv_bonus.setText(redEnvelopeReList.get(i).getPrice());
            }
        }

        // 总数，总价
        if (null!=totalCount){
            id_tv_totalCount.setText("共"+totalCount+"个，合计");
        }

        if (null!= totalPrice){
            id_tv_totalPrice.setText("￥"+totalPrice);
        }

        if (null!=totaldiscountMoney){
            tvPostPrice.setText("优惠￥"+totaldiscountMoney);
        }



       /*
        * 参考：http://blog.csdn.net/android_drawing/article/details/46341163
        * 1.向列表的头部添加布局，将整个xml文件添加
        * 2.重载方法表示，是否可触发点击事件
        * 3.规范：在添加头部的时候，必须在绑定adapter之前，新的adapter将包含header和footer
        * 4.还有一个就是position的位置，是占1的
        * 5.点击事件：headView和footerView都可以响应onItemClick方法，headView的position为0，
        * footerView的position最大。不过可以给headView和footerView设置OnClickListener来覆盖OnItemClick，这样，你点击headview或者footerView
        * 将触发OnClickListener而不是onItemClick().
        * */


        headerView = View.inflate(context, R.layout.activity_conform_order_header, null);
        //只添加一次
        Listview.addHeaderView(headerView);//是否可以点击，这个方法需要详细讲解才是
        address =  (RelativeLayout)headerView.findViewById(R.id.id_address);
        li_has_contact =  (LinearLayout)headerView.findViewById(R.id.li_has_contact);
        li_has_no_contact =  (LinearLayout)headerView.findViewById(R.id.li_has_no_contact);
        id_contactname =  (TextView)headerView.findViewById(R.id.id_contactname);
        id_contactmobile =  (TextView)headerView.findViewById(R.id.id_contactmobile);
        id_contactaddress =  (TextView)headerView.findViewById(R.id.id_contactaddress);
        id_tv_receipt_name =  (TextView)headerView.findViewById(R.id.id_tv_receipt_name);

        /*
        * 联系人逻辑
        * */
        final boolean hasContact = null==contactBean?false:true;
        if (hasContact){
            initContactView();
        } else {
            initEmptyContactView();
        }

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtil.showShort("跳转到管理联系信息");
                if (hasContact){
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.IS_ONLY_CHOOSE_CONTACT,true);
                    IntentUtil.gotoActivityForResultWithData(ConformOrderActivity.this, MyContactListAcitivity.class,
                            ADD_CONTACT, bundle);
                } else {
                    IntentUtil.gotoActivityForResult(ConformOrderActivity.this,AddContactActivity.class,ADD_CONTACT);
                }
            }
        });


        hoabanyiReceipt = (RelativeLayout)headerView.findViewById(R.id.id_hoabanyi_receipt);
        hoabanyiReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtil.showShort("跳转到发票开具界面");
                IntentUtil.gotoActivityForResult(ConformOrderActivity.this, ReceiptActivity.class, ADD_RECEIOPT);
            }
        });

    }

    private void initContactView() {
        li_has_contact.setVisibility(View.VISIBLE);
        li_has_no_contact.setVisibility(View.GONE);
        id_contactname.setText("联系人："+contactBean.getContactName());
        id_contactmobile.setText(contactBean.getContactMobile());
        id_contactaddress.setText("地址：" + contactBean.getContactAddress());
    }

    private void initEmptyContactView() {
        li_has_contact.setVisibility(View.GONE);
        li_has_no_contact.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_CONTACT:
                //addContactInformation(data);//在这里更新  第一版逻辑
                //第二版逻辑，直接刷新
                Listview.removeHeaderView(headerView);//先删除再更新
           /*     requestCount = 15;
                getDataFromServer();*/
                handleLogic();
                break;
            /*即使按后退键也是会正常触发这个回调，只不过data就是了*/
            case ADD_RECEIOPT:
                //分为个人发票，企业发票和不需要发票三种
                addReceiptInformation(data);//在这里更新 ，发票信息更新下
                break;
            case LOGIN_TO_CONFORM_ORDER:
                Listview.removeHeaderView(headerView);//先删除再更新
                handleLogic();
                break;
            case TO_SET_PAY_PWD:
                Listview.removeHeaderView(headerView);//先删除再更新
                handleLogic();
                break;

        }
    }

    private void addReceiptInformation(Intent data) {
        //这个地方崩溃了，因为有一种场景是会触发为空的data 为空&& (invoiceType != 9)
        Log.d(TAG, "data:" + data);
        if (null != data) {
            invoiceType = data.getIntExtra(Constants.INVOICE_TYPE, 9);//Caused by: java.lang.NullPointerException:
            // Attempt to invoke virtual method 'int android.content.Intent.getIntExtra(java.lang.String, int)' on a
            // null object reference
            Log.d(TAG, "invoiceType:" + invoiceType);
            switch (invoiceType) {
                case 1:
                    id_tv_receipt_name.setText("普通发票：" + data.getStringExtra(Constants.RECEIPT_NAME));
                    break;
                case 2:
                    id_tv_receipt_name.setText("增值税发票：" + data.getStringExtra(Constants.RECEIPT_NAME));
                    break;
                case 9:
                    id_tv_receipt_name.setText("发票信息：不需要发票");
                    break;
            }
        } else {
            id_tv_receipt_name.setText("发票信息：不需要发票");
        }
    }

/*    private void addContactInformation(Intent data) {
        if (null !=data ){
            contactname = data.getStringExtra(Constants.CONTACT_NAME);
            contactaddress = data.getStringExtra(Constants.CONTACT_ADDRESS);
            contactmobile = data.getStringExtra(Constants.CONTACT_MOBILE);
            li_has_contact.setVisibility(View.VISIBLE);
            li_has_no_contact.setVisibility(View.GONE);
            id_contactname.setText("联系人："+contactname);
            id_contactmobile.setText(contactaddress);
            id_contactaddress.setText("地址："+contactmobile);
        }

    }*/

    private void initTittleBar() {
        // 设置背景色  透明灰色  颜色 什么的最后定夺
        titleBar.setLeftImageResource(R.drawable.back_icon);
        // 设置监听器
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ConformOrderActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setBackgroundResource(R.color.white);

        titleBar.setTitle("确认订单");
        titleBar.setDividerColor(Color.GRAY);
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

//R.id.id_hoabanyi_bonus 平台红包不选择
    @OnClick({ R.id.id_submit_order})
    public void onClick(View view) {
        switch (view.getId()) {
/*            case R.id.id_hoabanyi_bonus:
                //"弹出平台红包的视图"
                ToastUtil.showShort("弹出平台红包的视图");
                break;*/
            case R.id.id_submit_order:
                //"提交订单，选择支付方式"
                Log.d(TAG, "提交订单，选择支付方式");

                popupSubmitOrderDialog();
                break;
        }
    }

    private void popupSubmitOrderDialog() {

        //获取到所有的提交订单的数据
        List<Map<Long, Object>>  mappedMeaagList =  adapter.getMappedMessageList();
        if (!mappedMeaagList.isEmpty() && null!=mappedMeaagList){
            shopsBeanlist= params.getShops();
            for (int j=0;j<mappedMeaagList.size();j++){
                //存在一个对象里面好一些
                for (int i=0;i<shopsBeanlist.size();i++){
                     long shopID =shopsBeanlist.get(i).getShopID();
                    //两个循环一一对比
                    if ( mappedMeaagList.get(j).containsKey(shopID)){
                        ShopBean shopBean = (ShopBean) mappedMeaagList.get(j).get(shopID);
                        shopsBeanlist.get(i).setMessage(shopBean.getMeaasage());//如果店铺存在留言，则把留言存储起来
                    }
                }
            }

        }
        params.setInvoiceType(invoiceType);
        //请先选择联系人信息
        if (params.getContactManageID()==-1) {
            //为0就表示不存在
            Toast.makeText(this, "请先填写联系地址", Toast.LENGTH_SHORT).show();
            return;

        }

        ActionToPay actionToPay = new ActionToPay(ConformOrderActivity.this,totalPrice);
        actionToPay.popupSubmitOrderDialog(params,null);
        // 支付宝情况
        actionToPay.setOnPictureStateChangedListener(new ActionToPay.OnPayBackListener() {
            @Override
            public void onPayDone() {
                ToastUtil.showSuccessfulMessage("支付成功");//支付宝有一个问题就是后台数据没有更新
                IntentUtil.gotoActivityWithoutData(ConformOrderActivity.this, MyOrderListActivity.class, true);
            }
        });





        /*
        * 说明：可展开的就是先展示部分，可以由下往上拉，可添头（标题栏）可添尾（取消，确定）可定制viewhoder视图 可选择方式
        * 参考设计：还可以设置动漫效果
        * 场景：当我按软键盘的后退键时：依次调用onBackPressed -》onCancel -》onDismiss
        *       当我没有按后退键，而是其他，依次调用onCancel -》onDismissGravity.CENTER
        * */

        /*dialog = DialogPlus.newDialog(ConformOrderActivity.this)
                .setContentHolder(new ViewHolder(R.layout.item_dialog_payment))
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setExpanded(false)  //设置为不可滑动
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        Log.d(TAG, "当调用dialog。dismiss时 也就是对话框消失时，触发，dismissed");

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialog) {

                        Log.d(TAG, "对话框被cancelled by back button or clicking outside");

                    }
                })
                .setOnBackPressListener(new OnBackPressListener() {
                    @Override
                    public void onBackPressed(DialogPlus dialogPlus) {
                        Log.d(TAG, " back button is pressed");

                    }
                })
                *//* .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                        Log.d(TAG, "  list or grid items is clicked");

                    }
                })*//*
                .setOnClickListener(dialogClickListener)
                .create();
        dialog.show();
        *//*初始化dialog中视图对象*//*
        initDialogView();*/
    }

    private void initDialogView() {
        View view = dialog.getHolderView();
        id_ailpay = (TextView) view.findViewById(R.id.id_ailpay);
        id_weixin_ailpay = (TextView) view.findViewById(R.id.id_weixin_ailpay);
        id_account_ailpay = (TextView) view.findViewById(R.id.id_account_ailpay);
        id_need_to_pay = (TextView) view.findViewById(R.id.id_need_to_pay);
        if (null!=totalPrice){
            id_need_to_pay.setText(totalPrice);
        }
    }
    OnClickListener dialogClickListener = new OnClickListener() {
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
                    setDrawable(2);
                    break;
                case R.id.pay:
                    Log.d(TAG, "  点击确认支付");
                    pay(TAG_PAY);
                    dialog.dismiss();
                    break;
            }
        }
    };
    /*
    * 动态加载图片
    * 参考设置：https://software.intel.com/zh-cn/blogs/2014/09/17/android-textview-drawableleft-drawableright
    *
    * 按照原有比例来设置：http://blog.csdn.net/wulianghuan/article/details/24421179
    * */
    private void setDrawable(int tag) {
        Drawable RightDrawable = getResources().getDrawable(R.drawable.choice_icon);
        Drawable leftDrawable_weixin = getResources().getDrawable(R.drawable.weixin_pay);
        Drawable leftDrawable_ailpay = getResources().getDrawable(R.drawable.alipay);
        Drawable leftDrawable_content_pay = getResources().getDrawable(R.drawable.content_pay);
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
    private void pay(int tag) {
        bundle=new Bundle();
        switch (tag) {
            case TAG_AILPAY:
                Log.d(TAG, "走支付宝渠道");
                //走支付渠道之前，先要调用接口
                //不一定存在红包id,没有则传空
                Log.d(TAG, "params.getRedID():" + params.getRedID());
                requestCount = 45;
                submitOrder();
                break;
            case TAG_WEIXIN_AILPAY:
                Log.d(TAG, "走微信支付渠道");
                break;
            case TAG_ACCOUNt_AILPAY:
                Log.d(TAG, "走余额支付渠道");
                break;
        }
    }
    /*一旦提交订单无论成功还是失败都是需要刷新一下的*/
    private void submitOrder() {
        Log.d(TAG, "01");// 没有则传入空
        this.appAction.submitOrder(params.getRedID(), params, new ActionCallbackListener<List<Integer>>() {
            @Override
            public void onSuccess(List<Integer> data) {
                Log.d(TAG, "02");
                Log.d(TAG, "返回的结果" + data);
                StringBuffer OrderIds = new StringBuffer(256);
                if (null !=data){
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
                        submitPayment(OrderIds.toString());//"data":[30287,30288]
                    }
                }



            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode)&& requestCount > 0 ) {
                    requestCount--;
                    submitOrder();
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");

                    loginForToken();
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    bundle.putBoolean("back_to_homePage",true);
                    IntentUtil.gotoTopActivityWithData(ConformOrderActivity.this, MyOrderListActivity.class,bundle,true);
                } else {
                    ToastUtil.showHintMessage("交易失败",errorCode,errorMessage);
                    bundle.putBoolean("back_to_homePage",true);
                     IntentUtil.gotoTopActivityWithData(ConformOrderActivity.this, MyOrderListActivity.class,bundle,true);
                }
            }
        });
    }
    // 这里面的任何一个环节都可能导致崩溃的
    private void submitPayment(final String data) {
        Log.d(TAG, "06？");
        this.appAction.submitPayment(data, new ActionCallbackListener<LoginResponseResult.DataBean>() {
            @Override
            public void onSuccess(LoginResponseResult.DataBean data) {
                Log.d("ConformOrderActivity", data.getOrderPayId());
                Log.d("ConformOrderActivity", data.getAmount());
                ailpayPay(data.getOrderPayId(), data.getOrderDesc(),data.getAmount());
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode)&& requestCount > 0 ) {
                    requestCount--;
                    submitPayment(data);
                } else if ("996".equals(errorCode)) {
                    loginForToken();
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    bundle.putBoolean("back_to_homePage",true);
                     IntentUtil.gotoTopActivityWithData(ConformOrderActivity.this, MyOrderListActivity.class,bundle,true);
                } else {
                    bundle.putBoolean("back_to_homePage",true);
                    ToastUtil.showHintMessage("交易失败",errorCode,errorMessage);
                     IntentUtil.gotoTopActivityWithData(ConformOrderActivity.this, MyOrderListActivity.class,bundle,true);
                }
            }
        });

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
                    Log.d(TAG, resultInfo);
                    Log.d(TAG, resultStatus);
                    // 1.resultStatus==“9000”:代表支付成功，
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtil.showSuccessfulMessage("支付成功");
                        //无论是支付成功还是失败都跳转到订单界面，向这种情况就不跳回来了
                        bundle.putBoolean("back_to_homePage",true);
                         IntentUtil.gotoTopActivityWithData(ConformOrderActivity.this, MyOrderListActivity.class,bundle,true);



                    } else {
                        /*其他的一律为支付失败*/
                        // 2."8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtil.showHintMessage("支付结果确认中");
                            bundle.putBoolean("back_to_homePage",true);
                             IntentUtil.gotoTopActivityWithData(ConformOrderActivity.this, MyOrderListActivity.class,bundle,true);

                        } else {
                            //3.其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtil.showHintMessage("支付失败");
                            bundle.putBoolean("back_to_homePage",true);
                             IntentUtil.gotoTopActivityWithData(ConformOrderActivity.this, MyOrderListActivity.class,bundle,true);
                        }
                    }
                    break;
                }
                default:
                    break;
            }

        }
    };
    //支付宝支付
    private void ailpayPay(String orderPayId, String orderDesc, String amount) {
        /*1.这里是需要验证两种逻辑的，一种是存在支付宝客户端，一种是没有支付宝客户端，将会触发SDK内部进行H5支付*/
        /*2，参数合法性检查*/
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)){
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置商户ID | 商户私钥| 商户收款账号")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            finish();// 并把当前界面给关闭掉
                        }
                    }).show();
            return;
        }
        /*服务器需要提供：签约合作者身份ID /签约卖家支付宝账号 /商户网站唯一订单号 /商品名称 /商品详情 /商品金额/签名/签名方式*/
        /*请求参数至少包含19个*/

        Log.d(TAG, "1"+orderPayId);
        Log.d(TAG, "2"+orderDesc);
        Log.d(TAG, "3"+amount);
        String orderInfo = getOrderInfo(orderPayId, orderDesc, amount);  // 这个就是订单信息
        Log.d(TAG, "4"+orderInfo);
        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);  // 服务端添加签名逻辑 ，私钥放在服务端
        Log.d(TAG, "5"+sign);
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
                PayTask alipay = new PayTask(ConformOrderActivity.this);
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
    /**
     * create the order info. 创建订单信息
     *
     */
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
        orderInfo += "&notify_url=" + "\"" + " http://www.haobanyi.com/Pay/Notify/HbyCommerce-Plugin-Payment-Alipay" + "\"";//整个域名添加：由http://notify.msp.hk/notify.htm

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
//LOGIN_TO_CONFORM_ORDER


}
