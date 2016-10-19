package com.android.haobanyi.activity.searching;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.haobanyi.BaseFragment;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.shopping.store.StorePageActivity;
import com.android.haobanyi.adapter.shop.ShopListAdapter;
import com.android.haobanyi.core.ActionCallbackDoubleListener;
import com.android.haobanyi.core.ActionCallbackTripleListener;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/8/5.
 *
 * @作者: 付敏
 * @创建日期：2016/08/05
 * @邮箱：466566941@qq.com
 * @当前文件描述：没有缓存，实时搜索
 */
public class KeywordShopFragment  extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener,SwipeRefreshLayout.OnRefreshListener  {
    private static final String TAG = "KeywordShopFragment";
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    String keyWord; //用户输入,这个就是关键字
//    List<ShopBean> listData = new ArrayList<ShopBean>();
    private LayoutInflater inflater;
    private int CurrentPage = 0;// 默认从1开始
    private int PAGESIZE = 10; // 定义的一页多少条数据
    private int totalCount = 0 ;
    private boolean ishasData = false;//当前是否有数据
    private boolean isListToBottom = false; //数据是否到顶了
    private View notLoadingView;
    private ShopListAdapter adapter= new ShopListAdapter( null);;
    private int position;
    @Override
    protected void lazyload() {

    }

    @Override
    protected int setLayout() {
        return R.layout.tab_front_page02;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        position = FragmentPagerItem.getPosition(getArguments());
        Log.d("KeywordProductFragment", "position02:" + position);

        keyWord = getActivity().getIntent().getStringExtra(Constants.USER_SEARCH);
        initRecyclerView();
        requestCount = 5;
        getDataFromServer(keyWord, 1, true);

    }

    private void getDataFromServer(final String keyWord,final int currentPage,final boolean isRefreshOrFirstLoading) {
        if (isRefreshOrFirstLoading) {
            progressActivity.showLoading();//会一直刷新 先测试下
        }
        this.appAction.searchShopByKeyword(keyWord, currentPage, PAGESIZE, new
                ActionCallbackTripleListener<List<ShopBean>, String, String>() {
                    @Override
                    public void onSuccess(List<ShopBean> data, String pageIndex, String total) {
                        progressActivity.showContent();
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

                    @Override
                    public void onFailure(String errorEvent, String errorMessage) {

                        if ("998".equals(errorEvent) && requestCount > 0) {
                            requestCount--;
                            refresh(keyWord, isRefreshOrFirstLoading);
                        } else if ("003".equals(errorEvent)) {
                            ToastUtil.networkUnavailable();
                            handleError(keyWord, isRefreshOrFirstLoading, "易，请检查网络");
                        } else {
                            ToastUtil.showErrorMessage(errorEvent, errorMessage);
                            handleError(keyWord,isRefreshOrFirstLoading, "加载失败，请重新点击加载");
                        }

                    }
                });

    }
    private void handleError(String keyWord,boolean isRefreshOrFirstLoading,String Message) {
        if (!isRefreshOrFirstLoading){
            adapter.showLoadMoreFailedView();
        }else {
            ishasData = (!adapter.getData().isEmpty()) ? true : false;
            if (!ishasData) {
                showError(keyWord, Message);
            } else {
                //请求失效，数据不变
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();
                progressActivity.showContent();
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void showError(final String keyWord,String Message) {
        progressActivity.showError(Message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentPage = 0;
                getDataFromServer(keyWord, 1, true);
            }
        });
    }

    private void refresh(final String keyWord,boolean isRefreshOrFirstLoading) {
        if (!isRefreshOrFirstLoading){
            getDataFromServer(keyWord,CurrentPage + 1, false);
        } else {
/*            ishasData = (!adapter.getData().isEmpty()) ? true : false;
            if (!ishasData) {*/
                getDataFromServer(keyWord, CurrentPage, true);

/*            } else {
                //请求失效，数据不变
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();
                progressActivity.showContent();
                adapter.notifyDataSetChanged();
            }*/
        }

    }

    private void initAdapter(List<ShopBean> listData) {
        adapter = new ShopListAdapter(getActivity(), listData);
        if (null== mSuperRecyclerViewImg.getAdapter()){
            adapter.openLoadAnimation();
            adapter.setOnLoadMoreListener(this);
            adapter.openLoadMore(PAGESIZE);
            mSuperRecyclerViewImg.setAdapter(adapter);
            mSuperRecyclerViewImg.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                    final ShopBean bean = (ShopBean) adapter.getItem(position);
                    final long shopID = bean.getShopID();
                    if (shopID != -1) {
                        requestCount = 5;
                        gotoShop(shopID);
                    }
                }
            });
/*            adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //点击进入不同的店铺
                    final ShopBean bean = (ShopBean) adapter.getItem(position);
                    final long shopID = bean.getShopID();
                    if (shopID != -1) {
                        requestCount = 5;
                        gotoShop(shopID);
                    }

                }
            });*/
           /* ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
            itemTouchHelper.attachToRecyclerView(mSuperRecyclerViewImg.getRecyclerView());
            itemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            adapter.enableSwipeItem();
            adapter.setOnItemSwipeListener(new OnItemSwipeListener() {
                @Override
                public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) {


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
            });*/

        }
    }

    private void gotoShop(final long shopID) {
        this.appAction.getShopwithoutId(shopID, new
                ActionCallbackDoubleListener<ShopBean, List<SortDataBean>>() {
            @Override
            public void onSuccess(ShopBean data01, List<SortDataBean> data02) {
                PreferenceUtil complexPreferences = PreferenceUtil.getSharedPreference(getActivity(),
                        "preferences");
                complexPreferences.putLong(Constants.SHOP_ID, shopID);
                startActivity(new Intent(getActivity(), StorePageActivity.class));
            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if ("998".equals(errorEvent) && requestCount > 0){
                    requestCount--;
                    gotoShop(shopID);
                } else if ("003".equals(errorEvent)) {
                    ToastUtil.networkUnavailable();
                }else {
                    ToastUtil.showErrorMessage(errorEvent, message);
                }

            }});
    }

/*    public View getEmptyView() {
        inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_empty_view,
                (ViewGroup) mSuperRecyclerViewImg.getParent
                        (), false);
        TextView tv = (TextView) view.findViewById(R.id.empty_content);
        tv.setText("易，暂时没有搜索到相关的店铺，请换个词试试，或者去服务列表看看，可能有您需要的！");
        return view;
    }*/

    private void initRecyclerView() {
    /*01.循环视图设置布局管理器*/
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*03.设置下拉刷新的监听器*/
        mSuperRecyclerViewImg.setRefreshListener(this);
        /*05.设置刷新布局的颜色状态*/
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
    }


    @Override
    public void onRefresh() {
        requestCount = 5;
        getDataFromServer(keyWord, 1, true);

    }

    @Override
    public void onLoadMoreRequested() {
        requestCount = 5;
        getDataFromServer(keyWord,CurrentPage + 1, false);
    }
}
