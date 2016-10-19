package com.android.haobanyi.activity.searching;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.shopping.store.StorePageActivity;
import com.android.haobanyi.adapter.shop.ShopListAdapter;
import com.android.haobanyi.core.ActionCallbackDoubleListener;
import com.android.haobanyi.core.ActionCallbackTripleListener;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.android.haobanyi.view.TitleBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.List;

import butterknife.BindView;
import dmax.dialog.SpotsDialog;

/**
 * Created by fWX228941 on 2016/8/24.
 *
 * @作者: 付敏
 * @创建日期：2016/08/24
 * @邮箱：466566941@qq.com
 * @当前文件描述：商铺列表ShopNormal
 */
public class ShopListActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener,SwipeRefreshLayout.OnRefreshListener   {
    private static final String TAG = "ShopListActivity";
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    private LayoutInflater inflater;
    private int CurrentPage = 0;// 默认从1开始 个性化的就自己定制
    private int PAGESIZE = 10; // 定义的一页多少条数据
    private int totalCount = 0 ;
    private boolean ishasData = false;//当前是否有数据
    private boolean isListToBottom = false; //数据是否到顶了
    private View notLoadingView;
    private ShopListAdapter adapter = new ShopListAdapter(null);
    private long shopID = -1;
    @Override
    protected int setLayout() {
        //都是activity
        return R.layout.activity_my_voucher;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        initRecyclerView();
        CurrentPage =1;
        requestCount = 35;
        getDataFromServer( CurrentPage, true);
    }
    private void getDataFromServer(final int currentPage,final boolean isRefreshOrFirstLoading) {
        if (isRefreshOrFirstLoading) {
            progressDialog.show();
        }

        this.appAction.getShopListByNormalSort(currentPage, PAGESIZE, new
                ActionCallbackTripleListener<List<ShopBean>, String, String>() {
                    @Override
                    public void onSuccess(List<ShopBean> data, String pageIndex, String total) {
                        progressActivity.showContent();
                        progressDialog.cancel();// 先后顺序也不要破坏
                        CurrentPage = Integer.parseInt(pageIndex);
                        totalCount = Integer.parseInt(total);
                        if (data.isEmpty()) {
                            isListToBottom = true;
                        }

                        if (totalCount == 0) {
                            // 关键字没有搜索到服务
                            //adapter.setEmptyView(true, getEmptyView());
                            progressActivity.showEmpty("易，暂时没有搜索到相关的店铺，请换个词试试，或者去服务列表看看，可能有您需要的！");
                        } else {
                            // 关键字搜索到了服务
                            if (adapter.getData().isEmpty()) {
                                initAdapter(data);
                            } else {
                                if (!isRefreshOrFirstLoading) {
                                    if (isListToBottom) {

                                        adapter.loadComplete();
                                        if (notLoadingView == null) {
                                            inflater = LayoutInflater.from(ShopListActivity.this);
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
                        mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                        mSuperRecyclerViewImg.hideMoreProgress();
                    }
                    /*这个地方没有执行*/
                    @Override
                    public void onFailure(String errorEvent, String errorMessage) {
                        if ("998".equals(errorEvent) && requestCount > 0) {
                            requestCount--;
                            refresh(isRefreshOrFirstLoading);
                        } else if ("003".equals(errorEvent)) {
                            ToastUtil.networkUnavailable();
                            handleError(isRefreshOrFirstLoading, "易，请检查网络");
                        } else {
                            ToastUtil.showErrorMessage(errorEvent, errorMessage);
                            handleError(isRefreshOrFirstLoading, "加载失败，请重新点击加载");
                        }


                    }
                });

    }
    // 一定要分清加载和刷新，
    private void handleError(boolean isRefreshOrFirstLoading,String Message) {
        if (!isRefreshOrFirstLoading){
            adapter.showLoadMoreFailedView();
        }else {
            progressDialog.cancel();
            ishasData = (!adapter.getData().isEmpty()) ? true : false;
            if (!ishasData) {
                progressActivity.showContent();
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

    private void showError(String Message) {
        progressActivity.showError(Message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentPage = 1;
                requestCount = 35;
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
            mSuperRecyclerViewImg.setAdapter(adapter);
            mSuperRecyclerViewImg.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                    //点击进入不同的店铺
                    final ShopBean bean = (ShopBean) adapter.getItem(position);
                    shopID = bean.getShopID();
                    if (shopID != -1) {
                        requestCount = 35;
                        getShopwithoutId();
                    }
                }
            });

        }
    }

    private void getShopwithoutId() {
        this.appAction.getShopwithoutId(shopID, new
                ActionCallbackDoubleListener<ShopBean, List<SortDataBean>>() {
                    @Override
                    public void onSuccess(ShopBean data01, List<SortDataBean> data02) {
                        PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(context,
                                "preferences");
                        complexPreferences.putLong(Constants.SHOP_ID, shopID);
                        IntentUtil.gotoActivityWithoutData(ShopListActivity.this,
                                StorePageActivity.class, false);
                    }

                    @Override
                    public void onFailure(String errorEvent, String message) {
                        if ("998".equals(errorEvent) && requestCount > 0) {
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
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(ShopListActivity.this));
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
                ShopListActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });
        titleBar.setTitle("店铺列表");
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
        requestCount = 35;
        getDataFromServer(CurrentPage, true);
    }

    @Override
    public void onLoadMoreRequested() {
        requestCount = 35;
        getDataFromServer(CurrentPage + 1, false);
    }
}
