package com.android.haobanyi.activity.collection;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.shopping.store.StorePageActivity;
import com.android.haobanyi.adapter.shop.ShopListAdapter;
import com.android.haobanyi.core.ActionCallbackDoubleListener;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.ActionCallbackTripleListener;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.android.haobanyi.view.TitleBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by fWX228941 on 2016/8/24.
 *
 * @作者: 付敏
 * @创建日期：2016/08/24
 * @邮箱：466566941@qq.com
 * @当前文件描述：店铺收藏界面
 */
public class ShopCollectionActivity  extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener,SwipeRefreshLayout.OnRefreshListener   {
    private static final String TAG = "ShopCollectionActivity";
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    private LayoutInflater inflater;
    private int CurrentPage = 0;// 默认从1开始
    private int PAGESIZE = 10; // 定义的一页多少条数据
    private int totalCount = 0 ;
    private boolean ishasData = false;//当前是否有数据
    private boolean isListToBottom = false; //数据是否到顶了
    private View notLoadingView;
    private ShopListAdapter adapter = new ShopListAdapter(null);
    private long shopID = -1;

    private static final int LOGIN_TO_SHOP_COLLECTION = 3;

    ShopBean bean;
//    private SpotsDialog dialog;
    @Override
    protected int setLayout() {
        return R.layout.activity_my_voucher;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        progressDialog.show();
        if (!hasTokenRefreshed){
            long refreshTime = basePreference.getLong(Constants.START_TIME,-1000l);
            if (System.currentTimeMillis()/1000L-refreshTime>7200){
                requestCount = 5;
                Log.d(TAG, "执行");
                getToken();
            } else {
                handlelogic();
            }

        } else {
            handlelogic();
        }








    }

    private void handlelogic() {
        initRecyclerView();

        if (isLogin){
            CurrentPage =1;
            requestCount = 45;
            getDataFromServer( CurrentPage, true);
        }else {
            progressDialog.cancel();
            progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看我收藏的店铺列表！");

        }

    }


    private void getToken() {
        this.appAction.getToken(new ActionCallbackListener<LoginResponseResult>() {
            @Override
            public void onSuccess(LoginResponseResult data) {
                handlelogic();
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                Log.d(TAG, "errorCode"+errorCode);
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    getToken();
                } else {
                    handlelogic();
                }
            }
        });
    }
    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(ShopCollectionActivity.this, LoginActivity.class, LOGIN_TO_SHOP_COLLECTION);//跳转到登录界面
    }

    private void getDataFromServer(final int currentPage,final boolean isRefreshOrFirstLoading) {
        Log.d(TAG, "进来");
        if (isRefreshOrFirstLoading) {
            progressDialog.show();
        }
        this.appAction.getFavoriteShopList(currentPage, new
                ActionCallbackTripleListener<List<ShopBean>, String, String>() {
                    @Override
                    public void onSuccess(List<ShopBean> data, String pageIndex, String total) {

                        Log.d(TAG, "有数据");
                        progressActivity.showContent();
                        progressDialog.cancel();
                        CurrentPage = Integer.parseInt(pageIndex);
                        totalCount = Integer.parseInt(total);
                        if (data.isEmpty()) {
                            isListToBottom = true;
                        }

                        if (totalCount == 0) {
                            // 关键字没有搜索到服务
                            //adapter.setEmptyView(true, getEmptyView());
                            progressActivity.showEmpty("亲，你的收藏店铺列表为空！");//这个框架真心很多坑，真是受不了，害死我了，到时一定要替换过来
                        } else {
                            // 关键字搜索到了服务
                            if (adapter.getData().isEmpty()) {
                                initAdapter(data);

                            } else {
                                if (!isRefreshOrFirstLoading) {
                                    if (isListToBottom) {
                                        adapter.loadComplete();
                                        if (notLoadingView == null) {
                                            inflater = LayoutInflater.from(ShopCollectionActivity.this);
                                            notLoadingView = inflater.inflate(R.layout.not_loading,
                                                    (ViewGroup) mSuperRecyclerViewImg.getParent
                                                            (), false);
                                        }
                                        adapter.addFooterView(notLoadingView);
                                    } else {
                                        adapter.addData(data);
                                    }
                                } else {
                                    isListToBottom = false;
                                    if (!data.isEmpty()) {
                                        adapter.setNewData(data);//有数据才会更新，没有数据是不会更新的
                                        adapter.openLoadMore(PAGESIZE);
                                        adapter.removeAllFooterView();// 这几个必须添加上
                                    }
                                }
                            }
                        }
                        progressDialog.cancel();// Android 果然是各种奇葩问题真的
                        mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);//放在外面和里面是一样的，暂时不要修改
                        mSuperRecyclerViewImg.hideMoreProgress();
                    }

                    @Override
                    public void onFailure(String errorEvent, String errorMessage) {
                        if ("998".equals(errorEvent) && requestCount > 0) {
                            requestCount--;
                            refresh(isRefreshOrFirstLoading);
                        } else if ("003".equals(errorEvent)) {
                            ToastUtil.networkUnavailable();
                            progressDialog.cancel();
                            handleError(isRefreshOrFirstLoading, "易，请检查网络");
                        } else if ("996".equals(errorEvent)) {
                            progressDialog.cancel();
                            progressActivity.showContent(); //怎么刷新了这么久
                            ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                            loginForToken();
                        } else {
                            ToastUtil.showErrorMessage(errorEvent, errorMessage);
                            handleError(isRefreshOrFirstLoading, "加载失败，请重新点击加载");
                        }

                    }
                });

    }
    private void handleError(boolean isRefreshOrFirstLoading,String Message) {
        Log.d(TAG, "没有数据");

        if (!isRefreshOrFirstLoading){
            adapter.showLoadMoreFailedView();
        }else {
            progressDialog.cancel();
            ishasData = (!adapter.getData().isEmpty()) ? true : false;
            Log.d(TAG, "ishasData:" + ishasData);
            if (!ishasData) {
                showError(Message);
            } else {
                //请求失效，数据不变
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();
                progressActivity.showContent();
                adapter.notifyDataSetChanged();
            }
        }
    }
    /*在activity和fragment中还是存在很大的差异*/
    private void showError(String message) {
        progressActivity.showError(message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentPage = 1;
                requestCount = 45;
                getDataFromServer(CurrentPage, true);
            }
        });
    }

    private void refresh(boolean isRefreshOrFirstLoading) {
        if (!isRefreshOrFirstLoading){
            getDataFromServer(CurrentPage+1, false);//如果是下滑加载时一直请求,直到请求成功为止
        } else {
            getDataFromServer(CurrentPage, true);
        }


    }

    private void initAdapter(List<ShopBean> listData) {
        adapter = new ShopListAdapter(listData);
        if (null== mSuperRecyclerViewImg.getAdapter()){
            adapter.openLoadAnimation();
            adapter.setOnLoadMoreListener(this);
            adapter.openLoadMore(PAGESIZE);
            mSuperRecyclerViewImg.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                    Log.d(TAG, "pos01:" + position);//0-4
                    Log.d(TAG, "adapter.getItemCount():" + adapter.getItemCount());//adapter.getItemCount() 这个为什么是1呢！
                    bean = (ShopBean) adapter.getItem(position);
                    shopID = bean.getShopID();
                    Log.d(TAG, "shopID:" + shopID);
                    if (shopID != -1) {
                        requestCount = 5;
                        getShopwithoutId();
                    }
                }
            });
            ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
            itemTouchHelper.attachToRecyclerView(mSuperRecyclerViewImg.getRecyclerView());
            itemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            adapter.enableSwipeItem();
            OnItemSwipeListener onItemSwipeListener =new OnItemSwipeListener() {
                @Override
                public void onItemSwipeStart(final RecyclerView.ViewHolder viewHolder, final int pos) {
                    final SweetAlertDialog pDialog = new SweetAlertDialog(ShopCollectionActivity.this, SweetAlertDialog.WARNING_TYPE);//只能这样写
                    pDialog.setTitleText("确定要删除吗？")
                            .setConfirmText("删除")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    pDialog.dismissWithAnimation();
                                    Log.d(TAG, "pos:" + pos);//lectionActivity: pos:4
//                                    int pos01 = adapter.getViewHolderPosition(viewHolder);  这个可以删除掉 -1
                                    Log.d(TAG, "adapter.getItemCount():" + adapter.getItemCount());//adapter.getItemCount() 这个为什么是1呢！
                                    bean = (ShopBean) adapter.getItem(pos);//java.lang.IndexOutOfBoundsException: Invalid index 4, size is 4  这个问题，只有等到明天来
                                    shopID = bean.getShopID();
                                    Log.d(TAG, "shopID:" + shopID);
                                    if (shopID != -1) {
                                        deleteFavShop(pos);
                                    }

                                }
                            })
                            .setCancelText("取消")
                            .show();
                    pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            adapter.notifyDataSetChanged();
                            pDialog.dismissWithAnimation();
                        }
                    });
                }

                @Override
                public void clearView(RecyclerView.ViewHolder viewHolder, int i) {

                }

                @Override
                public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int i) {

                }

                @Override
                public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1,
                                              boolean b) {

                }
            };
            adapter.setOnItemSwipeListener(onItemSwipeListener);
            mSuperRecyclerViewImg.setAdapter(adapter);

        }
    }

    private void deleteFavShop(final int position) {
        this.appAction.deleteFavShop(shopID, new ActionCallbackListener<ResponseCode>() {
            @Override
            public void onSuccess(ResponseCode data) {
                Log.d("CollectionPageView", "成功删除");
                adapter.remove(position);
                if (null == adapter.getData()){
                    progressActivity.showEmpty("亲，你的收藏店铺列表为空！");
                }

            }
            @Override
            public void onFailure(String errorCode, String errorMessage) {

                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    deleteFavShop(position);
                } else if ("003".equals(errorCode)) {
                    ToastUtil.networkUnavailable();
                    adapter.notifyDataSetChanged();
                } else if ("996".equals(errorCode)) {
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.showHintMessage("删除失败",errorCode,errorMessage);
                    adapter.notifyDataSetChanged();
                }

            }
        });

    }

    private void getShopwithoutId() {
        this.appAction.getShopwithoutId(shopID, new
                ActionCallbackDoubleListener<ShopBean, List<SortDataBean>>() {
                    @Override
                    public void onSuccess(ShopBean data01, List<SortDataBean> data02) {
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context,
                                "preferences");
                        complexPreferences.putLong(Constants.SHOP_ID, shopID);
                        IntentUtil.gotoActivityWithoutData(ShopCollectionActivity.this,
                                StorePageActivity.class, false);
                    }
                    @Override
                    public void onFailure(String errorEvent, String message) {
                        if ("998".equals(errorEvent) && requestCount>0) {
                            requestCount--;
                            getShopwithoutId();
                        } else if ("003".equals(errorEvent)) {
                            ToastUtil.networkUnavailable();
                        } else {
                            ToastUtil.showErrorMessage(errorEvent, message);
                        }
                    }
                });
    }

    private void initRecyclerView() {
    /*01.循环视图设置布局管理器*/
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(ShopCollectionActivity.this));
        /*03.设置下拉刷新的监听器*/
        mSuperRecyclerViewImg.setRefreshListener(this);
        /*05.设置刷新布局的颜色状态*/
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
    }
    @Override
    protected void initViews() {
        initTittleBar();
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
                ShopCollectionActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitle("店铺收藏");
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

    @Override
    public void onRefresh() {
        CurrentPage =1;
        requestCount = 45;
        getDataFromServer(CurrentPage, true);
    }

    @Override
    public void onLoadMoreRequested() {
        requestCount = 45;
        getDataFromServer(CurrentPage + 1, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case LOGIN_TO_SHOP_COLLECTION:
                handlelogic();//非首页的情况就自己来触发，fragment 也是一样的
                break;

        }
    }
}
