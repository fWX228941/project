package com.android.haobanyi.activity.shopping.store;


import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.android.haobanyi.BaseFragment;
import com.android.haobanyi.R;

import com.android.haobanyi.adapter.shop.ShopActivityAdapter;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.shopping.store.ShopAcBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/7/8.
 *
 * @作者: 付敏
 * @创建日期：2016/07/08
 * @邮箱：466566941@qq.com
 * @当前文件描述： 店铺活动   item_fragment_shop_activity   【领取这些活动的逻辑我也没有搞清楚】
 *
 * 页面有两种：一种是分页，一种是不分页
 *
 *
 */
public class StoreActivityFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener  {
    private static final String TAG = "minfu";
    private long shopId=-1;
    private ShopActivityAdapter adapter = new ShopActivityAdapter(null);
//    private List<ShopAcBean> listData = new ArrayList<ShopAcBean>();//不初始化，默认就是空了
    private List<ShopAcBean> cachedlistData;
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    private boolean ishasCachedData = false;//缓存是否有数据
    private int totalCount = 0 ;
    private LayoutInflater inflater;
    private boolean ishasData = false;//当前是否有数据

    @Override
    protected void lazyload() {
    }



    @Override
    protected int setLayout() {
        return R.layout.tab_front_page02;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        NTPTime.getInstance().getCurrentTime();
        shopId = getActivity().getIntent().getLongExtra(Constants.SHOP_ID, PreferenceUtil.getSharedPreference(getActivity(), "preferences").getLong(Constants.SHOP_ID, -1l));
        PreferenceUtil complexPreferences01 = PreferenceUtil.getSharedPreference(getActivity(),
                "shop");
        cachedlistData = complexPreferences01.getListObject("shopid" + shopId + "ShopActivity", ShopAcBean.class);
        ishasCachedData = (null != cachedlistData && !cachedlistData.isEmpty())?true:false;
        //03.初始化列表，并且调用服务器，绑定数据
        initRecyclerView();



        if (ishasCachedData){
            initAdapter(cachedlistData);
        } else {
            if (shopId!=-1){
                requestCount = 25;
                getDataFromServer(shopId, true);
            }
        }
    }


    private void getDataFromServer(final long shopId,final boolean isRefreshOrFirstLoading) {
        if (isRefreshOrFirstLoading) {
            progressActivity.showLoading();//会一直刷新 先测试下
        }
        this.appAction.getShopActivity(shopId, new ActionCallbackListener<List<ShopAcBean>>() {
            @Override
            public void onSuccess(List<ShopAcBean> list) {
                progressActivity.showContent();
                totalCount = list.size();
                //没有数据,因为是一次性刷完，不存在上拉加载
                if (totalCount == 0) {
                    initAdapter(list);
                    adapter.setEmptyView(true, getEmptyView());//如果是希望可以刷新的就用adapter
                } else {
                    //判断数据是否到底了,没有数据了就到底了
                    if (adapter.getData().isEmpty()) {
                        initAdapter(list);
                    } else {
                        //缓存中的数据也是需要清理掉的
                        if (cachedlistData != null) {
                            cachedlistData.clear();
                        }
                        if (!isRefreshOrFirstLoading) {
                            adapter.addData(list);
                        } else {
                            if (!list.isEmpty()) { // 如果是删除操作，这个地方还有一个bug
                                adapter.setNewData(list);
                                adapter.removeAllFooterView();
                            }
                        }
                    }

                }
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();

            }

            @Override
            public void onFailure(String errorEvent, String message) {

                if ("998".equals(errorEvent)&& requestCount > 0) {
                    requestCount--;
                    getDataFromServer(shopId, true);
//                    refresh(shopId);
                } else if ("003".equals(errorEvent)) {

                    handleError(shopId,"易，请检查网络");
                } else {
                    handleError(shopId, "加载失败，请重新点击加载");
                    Toast.makeText(getActivity(), "异常代码：" + errorEvent + " 异常说明: " + message, Toast
                            .LENGTH_SHORT).show();
                }

            }
        });


    }
    private void handleError(final long shopId,String Message) {
        ishasData = (!adapter.getData().isEmpty())  ? true : false;
        if (!ishasData) {
            showError(shopId,Message);
        } else {
            //请求失效，数据不变
            mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
            mSuperRecyclerViewImg.hideMoreProgress();
            progressActivity.showContent();
            adapter.notifyDataSetChanged();
        }
    }



    private void showError(final long shopId,String Message) {
        progressActivity.showError(Message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromServer(shopId, true);

            }
        });
    }
    private void refresh(final long shopId) {
        ishasData = (!adapter.getData().isEmpty()) ? true : false;
        if (!ishasData && !ishasCachedData) {
            getDataFromServer(shopId, true);
        } else {
            //请求失效，数据不变
            mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
            mSuperRecyclerViewImg.hideMoreProgress();
            progressActivity.showContent();
            adapter.notifyDataSetChanged();
        }
    }

    private View getEmptyView() {
        inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_empty_view,
                (ViewGroup) mSuperRecyclerViewImg.getParent
                        (), false);
        TextView tv = (TextView) view.findViewById(R.id.empty_content);
        tv.setText("易，该店铺暂时缺少店铺活动！");
        return view;
    }
    private void initAdapter(List<ShopAcBean> listData) {
        adapter = new ShopActivityAdapter(listData);
        if (null== mSuperRecyclerViewImg.getAdapter()){

/*            ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(adapter);
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
            adapter.openLoadAnimation();
            mSuperRecyclerViewImg.setAdapter(adapter);
        }

    }
    private void initRecyclerView() {
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSuperRecyclerViewImg.setRefreshListener(this);
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
    }

    @Override
    public void onRefresh() {
        requestCount = 25;
        getDataFromServer(shopId,true);
    }


}
