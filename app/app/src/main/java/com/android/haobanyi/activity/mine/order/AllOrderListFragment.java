package com.android.haobanyi.activity.mine.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.BaseFragment;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.ailpay.ActionToPay;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.shopping.store.StorePageActivity;
import com.android.haobanyi.adapter.order.MultipleItemOrderAdapter;
import com.android.haobanyi.core.ActionCallbackDoubleListener;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.ActionCallbackQuadrupleListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.order.OrderChildBean;
import com.android.haobanyi.model.bean.order.OrderParentBean;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ActionSheet;
import com.android.haobanyi.view.ProgressActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.category;

/**
 * Created by fWX228941 on 2016/8/8.
 *
 * @作者: 付敏
 * @创建日期：2016/08/08
 * @邮箱：466566941@qq.com
 * @当前文件描述：全部订单界面
 * activity_order_header
 * activity_order_footer
 * activity_order_content
 * getOrderList
 * ,ActionSheet.MenuItemClickListener
 */
public class AllOrderListFragment extends BaseFragment  implements BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "StoreServiceListFragment";
    private static final int BACK_TO_ORDERLIST = 01;
    private static final int TO_SET_PAY_PWD = 04;
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;

    private int CurrentPage = 0;// 默认从1开始
    private int PAGESIZE = 10; // 定义的一页多少条数据
    MultipleItemOrderAdapter adapter = new MultipleItemOrderAdapter(null);
    private int totalCount = 0 ;
    private boolean isListToBottom = false; //数据是否到顶了
    private View notLoadingView;
    private LayoutInflater inflater;
    private boolean ishasData = false;//当前是否有数据

    private int position;
    private String[] mCategoryArray;
    String mCategory;
    private boolean flag =false; //标识
    private long requestCount = 5;
    PreferenceUtil complexPreferences;
    private boolean isLogin =false;
    private static final int LOGIN_TO_SPV = 3;
    ActionSheet menuView;
    String orderIds; //全部合并一起支付
    Bundle bundle;
    OrderParentBean parentBean;
    OrderChildBean childBean;
    long orderId = 0;
    @Override
    protected void lazyload() {

    }

    @Override
    protected int setLayout() {
        //frontpage_empty_view [空View]    tab_front_page02
        return R.layout.tab_front_page02;
    }
    /*这个过程其实真心是复杂的，哎，what a fuck 呀的*/
    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        NTPTime.getInstance().getCurrentTime();
        complexPreferences = PreferenceUtil.getSharedPreference(getActivity(), "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);
        if (isLogin){
            long refreshTime = complexPreferences.getLong(Constants.START_TIME,-1000l);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                //超过过期时间，更新token
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
        BaseApplication.getApplication().getAppAction().getToken(new ActionCallbackListener<LoginResponseResult>() {

            @Override
            public void onSuccess(LoginResponseResult data) {
                handleLogic();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    getToken();
                } else {
                    handleLogic();
                }


            }
        });
    }

    @SuppressLint("LongLogTag")
    private void handleLogic() {
          /*说明position是从0开始的*/
        position = FragmentPagerItem.getPosition(getArguments());
        //这里需要判断一下
        flag = getActivity().getIntent().getBooleanExtra(Constants.IS_COME_FROM_MYREFUND, false);
        if (flag){
            mCategoryArray = getResources().getStringArray(R.array.category_my_refund_list);
        } else {
            mCategoryArray = getResources().getStringArray(R.array.category_order_list);// 根据这个来划分也是极好的
        }
        mCategory = mCategoryArray[position];
        initRecyclerView();
        if (isLogin){
            requestCount = 45;
            CurrentPage = 1;
            Log.d(TAG, "position:" + position);
            Log.d(TAG, "category01"+mCategory);
            getDataFromServer(mCategory,CurrentPage, true);
        }else {
            progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看我的订单列表！");
        }

    }

    private void loginForToken() {
        complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(getActivity(), LoginActivity.class, BACK_TO_ORDERLIST);
    }

    @SuppressLint("LongLogTag")
    private void getDataFromServer(final String category, final int currentPage, final boolean isRefreshOrFirstLoading) {
        if (isRefreshOrFirstLoading) {
            progressActivity.showLoading();//会一直刷新 先测试下
        }
        String orderType = "1";
        Map<String, Object> params =new HashMap<String,Object>();
        params.put("PageNo", currentPage);
        Log.d(TAG, "category02"+category);
        if (category.equals("全部")){
            params.put("StatusFlag", 0);
        }else if (category.equals("待付款")){
            params.put("StatusFlag", 1);
        }else if (category.equals("待办理")){
            params.put("StatusFlag", 2);
        }else if (category.equals("待确认")){
            params.put("StatusFlag", 4);
        }else if (category.equals("待评价")){
            params.put("IsWaitAssessment", 1);
        }else if (category.equals("所有")){
            params.put("IsAllRefund", 1);
        }else if (category.equals("待商家审核")){
            params.put("StatusFlag", 6);
        }else if (category.equals("已完成")){
            params.put("StatusFlag", 7);
        }


        this.appAction.getOrderList(params, orderType, new
                ActionCallbackQuadrupleListener<List<OrderParentBean>, String, String, String>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(List<OrderParentBean> data, String total, String pageIndex, String OrderIds) {
                        progressActivity.showContent();
                        CurrentPage = Integer.parseInt(pageIndex);
                        totalCount = Integer.parseInt(total);
                        orderIds = OrderIds;
                        //首先就得判断是否有数据，因为这个店在这个区域可能是没有任何服务的
                        if (data.isEmpty()) {
                            isListToBottom = true;
                        }
                        if (totalCount == 0) {
                            initAdapter(data);
                            adapter.setEmptyView(true, getEmptyView());//如果是希望可以刷新的就用adapter
                        } else {
                            if (adapter.getData().isEmpty()) {
                                Log.d(TAG, "1");
                                initAdapter(data);
                            } else {

                                //如果放在这里进行替换呢？
                                if (!isRefreshOrFirstLoading) {
                                    Log.d(TAG, "isListToBottom:" + isListToBottom);
                                    if (isListToBottom) {
                                        adapter.loadComplete();
                                        if (notLoadingView == null) {
                                            inflater = LayoutInflater.from(getActivity());
                                            notLoadingView = inflater.inflate(R.layout.not_loading,
                                                    (ViewGroup) mSuperRecyclerViewImg.getParent
                                                            (), false);
                                        }
                                        adapter.addFooterView(notLoadingView);
                                    } else {
                                        adapter.addData(data);
                                    }
                                } else {
                                    Log.d(TAG, "3");
                                    isListToBottom = false;
                                    if (!data.isEmpty()) {
                                        adapter.setNewData(data);//有数据才会更新，没有数据是不会更新的
                                        adapter.openLoadMore(PAGESIZE);
                                        adapter.removeAllFooterView();// 这几个必须添加上
                                    }
                                }
                            }

                        }
                        mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                        mSuperRecyclerViewImg.hideMoreProgress();
                    }


                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(String errorEvent, String errorMessage) {
                        //01.因为各种异常情况没有获取到数据
                        //02.断网
                        //03.长期不登录导致的token失效
                        //04.两种情况下加载缓存数据，一种是失效情况下，还有一种是频繁加载
                        Log.d(TAG, errorEvent);

                        if ("998".equals(errorEvent) && requestCount > 0) {
                            requestCount--;
                            refresh(isRefreshOrFirstLoading);
                        } else if ("003".equals(errorEvent)) {
                            ToastUtil.networkUnavailable();
                            handleError(category, isRefreshOrFirstLoading, "易，请检查网络");
                        } else if ("996".equals(errorEvent)) {
                            progressActivity.showContent();
                            ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                            loginForToken();
                        } else {
                            ToastUtil.showErrorMessage(errorEvent, errorMessage);
                            handleError(category,isRefreshOrFirstLoading, "加载失败，请重新点击加载");

                        }

                    }
                });
    }
    private void handleError(String category, boolean isRefreshOrFirstLoading, String Message) {
        if (!isRefreshOrFirstLoading){
            adapter.showLoadMoreFailedView();
        }else {
            ishasData = (!adapter.getData().isEmpty()) ? true : false;
            if (!ishasData) {
                showError(category, Message);//向这种情况也是需要分类的
            } else {
                //请求失效，数据不变
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();
                progressActivity.showContent();
                adapter.notifyDataSetChanged();
            }
        }
    }
    private void showError(final String category,String message) {
        progressActivity.showError(message, new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                CurrentPage = 1;
                requestCount = 45;
                Log.d(TAG, "position:" + position);
                Log.d(TAG, "category03"+mCategory);
                getDataFromServer(category, CurrentPage, true);
            }
        });
    }

    private void initAdapter(final List<OrderParentBean> listData) {
        adapter = new MultipleItemOrderAdapter(listData);
        if (null== mSuperRecyclerViewImg.getAdapter()){
            adapter.openLoadAnimation();
            adapter.setOnLoadMoreListener(this);
            adapter.openLoadMore(PAGESIZE);
            mSuperRecyclerViewImg.setAdapter(adapter);

            mSuperRecyclerViewImg.addOnItemTouchListener(new OnItemChildClickListener() {
                @Override
                public void SimpleOnItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    Log.d("AllOrderListFragment", "position:" + position);
                    parentBean = (OrderParentBean) adapter.getItem(position);
                    Log.d("AllOrderListFragment", "parentBean:" + parentBean);
                    childBean = (OrderChildBean) parentBean.t;//也不知道这个行不行
                    Log.d(TAG, "childBean:" + childBean);
                    int status = childBean.getStatus();
                    Log.d("AllOrderListFragment", "status:" + status);
                    orderId = childBean.getOrderID();
                    Log.d("AllOrderListFragment", "orderId:" + orderId);



                    switch (view.getId()) {
                        case R.id.evaluation:
                            //评价
                            bundle = new Bundle();
                            bundle.putLong("OrderExtendID", childBean.getOrderExtendID());
                            Log.d(TAG, "childBean.getOrderExtendID():" + childBean.getOrderExtendID());
                            IntentUtil.gotoActivityForResultWithData(getActivity(), OrderEvaluateActivity.class,
                                    BACK_TO_ORDERLIST, bundle);
                            break;
                        case R.id.tv_get_vr:
                            Log.d("AllOrderListFragment", "执行");
                            //两种情况，一种是取消订单
                            if (status == 1) {
                                //取消订单
                                cancleOrder(orderId, position);

                            } else if (status == 4) {
                                //申请退款
                                bundle = new Bundle();
                                bundle.putLong("OrderID", orderId);
                                bundle.putString("Price", childBean.getPrice());
                                Log.d(TAG, childBean.getPrice());
                                Log.d(TAG, "orderId:" + orderId);
                                IntentUtil.gotoActivityForResultWithData(getActivity(), RefundOrderActivity.class,
                                        BACK_TO_ORDERLIST, bundle);



                            } else if (status == 5) {
                                //关闭订单CloseOrder
                                requestCount = 15;
                                closeOrder(orderId);
                            }
                            break;
                        case R.id.tv_get_vt:

                            Log.d("AllOrderListFragment", "执行zhifu");
                            if (status == 1) {
                                //去支付
                                Log.d("AllOrderListFragment", "支付");
                                Log.d("AllOrderListFragment", orderIds);
                                // 单个支付  orderId
                                //所有支付 orderIds
                                Log.d("AllOrderListFragment", "orderId:" + orderId);
                                if (orderId!=0) {
                                    String totalPrice = childBean.getPrice();
                                    ActionToPay actionToPay = new ActionToPay(getActivity(),totalPrice);
                                    actionToPay.popupSubmitOrderDialog(null, Long.toString(orderId));
                                    actionToPay.setOnPictureStateChangedListener(new ActionToPay.OnPayBackListener() {
                                        @Override
                                        public void onPayDone() {
                                            ToastUtil.showSuccessfulMessage("支付成功");
                                            refreshServer();
                                        }
                                    });
                                }

                            } else if (status == 2 || status == 3 || status == 5) {
                                //申请退款
                                bundle = new Bundle();
                                bundle.putLong("OrderID", orderId);
                                bundle.putString("Price", childBean.getPrice());
                                Log.d(TAG, "orderId:" + orderId);
                                Log.d(TAG, childBean.getPrice());
                                IntentUtil.gotoActivityForResultWithData(getActivity(), RefundOrderActivity.class,
                                        BACK_TO_ORDERLIST, bundle);

                            } else if (status == 4) {
                                //确认订单
                                requestCount = 15;
                                conformOrder(orderId);
                            }
                            break;
                    }

                }
            });

            //跳转到订单详情
            mSuperRecyclerViewImg.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                    parentBean = (OrderParentBean) adapter.getItem(position);
                    Log.d("AllOrderListFragment", "parentBean:" + parentBean);
                    childBean = (OrderChildBean) parentBean.t;//也不知道这个行不行
                    Log.d(TAG, "childBean:" + childBean);
                    if (null!= childBean){
                        orderId = childBean.getOrderID();
                    }
                    Log.d(TAG, "orderId:" + orderId);
                    Log.d(TAG, "parentBean.isHeader:" + parentBean.isHeader);
                    if (parentBean.isHeader) {
                        //跳转到店铺页
                        final long shopID = parentBean.getShopID();
                        if (shopID != -1) {
                            requestCount = 15;
                            gotoShop(shopID);
                        }

                    } else {
                        //订单详情
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constants.ORDER_ID, orderId);
                        Log.d(TAG, "orderId:" + orderId);
                        IntentUtil.gotoActivityWithData(getActivity(), OrderActivity.class, bundle, false);


                    }
                }
            });

        }

    }

    private void closeOrder(final long orderId) {
        this.appAction.closeOrder(orderId, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage("关闭成功");
                refreshServer();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                //{"code":103,"message":"订单不存在或不允许关闭！","data":null}
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    closeOrder(orderId);
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

    private void conformOrder(final long orderId) {
        this.appAction.confirmOrder(orderId, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                ToastUtil.showSuccessfulMessage("确认成功");
                refreshServer();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    conformOrder(orderId);
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                }else if ("996".equals(errorCode)){
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    ToastUtil.showErrorMessage(errorCode, errorMessage);
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void refreshServer() {
        CurrentPage =1;
        requestCount = 45;
        Log.d(TAG, "position:" + position);
        Log.d(TAG, "category04"+mCategory);
        getDataFromServer(mCategory, CurrentPage, true);//重新刷新一下
    }

    private void gotoShop(final long shopID) {
        this.appAction.getShopwithoutId(shopID, new
                ActionCallbackDoubleListener<ShopBean, List<SortDataBean>>() {
                    @Override
                    public void onSuccess(ShopBean data01, List<SortDataBean> data02) {
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference
                                (getActivity(),
                                        "preferences");
                        complexPreferences.putLong(Constants.SHOP_ID, shopID);

                        startActivity(new Intent(getActivity(), StorePageActivity.class));
                    }

                    @Override
                    public void onFailure(String errorEvent, String message) {
                        if ("998".equals(errorEvent) && requestCount > 0) {
                            requestCount--;
                            gotoShop(shopID);
                        } else if ("003".equals(errorEvent)) {
                            ToastUtil.networkUnavailable();
                        } else {
                            ToastUtil.showErrorMessage(errorEvent, message);

                        }

                    }
                });
    }

    /*取消订单*/
    private void cancleOrder(final long orderId, final int position) {
        getActivity().setTheme(R.style.ActionSheetStyleIOS7);
        menuView = new ActionSheet(getActivity());
        menuView.setCancelButtonTitle("取消");// before
        // add items
        menuView.addItems("信息填写错误，重新拍","我不想买了","其他原因");
        menuView.setItemClickListener(new ActionSheet.MenuItemClickListener() {
            @Override
            public void onItemClick(int itemPosition) {
                requestCount = 25;
                switch (itemPosition) {
                    case 0:
                        cancelOrder(orderId, "信息填写错误，重新拍",position);
                        break;
                    case 1:
                        cancelOrder(orderId,"我不想买了", position);
                        break;
                    case 2:
                        cancelOrder(orderId, "其他原因", position);
                        break;

                }
            }
        });
        menuView.setCancelableOnTouchMenuOutside(false);
        menuView.showMenu();


    }

    @SuppressLint("LongLogTag")
    private void cancelOrder(final long orderId, final String message, final int position) {
        Log.d(TAG, message);
        this.appAction.cancelOrder(orderId, message, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                //删除，并且更新
                menuView.dismissMenu();
                ToastUtil.showSuccessfulMessage("成功取消");
                refreshServer();


            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode)&& requestCount > 0) {
                    requestCount--;
                    cancelOrder(orderId, message, position);
                } else if ("003".equals(errorCode)) {
                    menuView.dismissMenu();
                    ToastUtil.networkUnavailable();

                }else if ("996".equals(errorCode)){
                    menuView.dismissMenu();
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    menuView.dismissMenu();
                    ToastUtil.showErrorMessage(errorCode,errorMessage);
                }



            }
        });
    }
    /*@Override
    public void onItemClick(int itemPosition) {
        switch (itemPosition) {
            case 0:
                this.appAction.cancelOrder();


                break;
            case 1:


                break;
            case 2:


                break;

        }
    }*/

    private void refresh(boolean isRefreshOrFirstLoading) {
        if (!isRefreshOrFirstLoading){
            getDataFromServer(mCategory,CurrentPage + 1, false);
        } else {
            getDataFromServer(mCategory, CurrentPage, true);

        }

    }
    private View getEmptyView() {
        inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_empty_view,
                (ViewGroup) mSuperRecyclerViewImg.getParent
                        (), false);
        TextView tv = (TextView) view.findViewById(R.id.empty_content);
        tv.setText("您还没有相关的订单，去挑选几个吧！");
        return view;
    }


    private void initRecyclerView() {
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSuperRecyclerViewImg.setRefreshListener(this);
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onRefresh() {
        CurrentPage =1;
        requestCount = 45;
        Log.d(TAG, "position:" + position);
        Log.d(TAG, "category05"+mCategory);
        getDataFromServer(mCategory,CurrentPage, true);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onLoadMoreRequested() {
        requestCount = 45;
        Log.d(TAG, "position:" + position);
        Log.d(TAG, "category06"+mCategory);
        getDataFromServer(mCategory,CurrentPage + 1, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //评价完以后放回到这里
            case BACK_TO_ORDERLIST:
                handleLogic();
                break;
            case TO_SET_PAY_PWD:
                handleLogic();
                break;

        }
    }
}
