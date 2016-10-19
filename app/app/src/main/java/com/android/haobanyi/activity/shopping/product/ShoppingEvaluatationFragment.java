package com.android.haobanyi.activity.shopping.product;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.android.haobanyi.BaseFragment;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.searching.ShopListActivity;
import com.android.haobanyi.adapter.shoppingevaluation.ShoppingEvaluationAdapter;
import com.android.haobanyi.core.ActionCallbackNListener;
import com.android.haobanyi.core.ActionCallbackTripleListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.shopping.product.EvaBean;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/7/5.
 *
 * @作者: 付敏
 * @创建日期：2016/07/05
 * @邮箱：466566941@qq.com
 * @当前文件描述：服务评价页面  评价界面 item_list_shopping_evaluate   ShoppingEvaluationAdapter  EvaBean  getassessmentlist
 */
public class ShoppingEvaluatationFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener,SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ShoppingEvaluatationFragment";
    @BindView(R.id.radio_group)
    RadioGroup group_tab;//这个就是数据源不同。默认是全部
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    @BindView(R.id.superRecyclerView_img)
    SuperRecyclerView mSuperRecyclerViewImg;
    @BindView(R.id.all_radio_button)
    RadioButton radio_all;
    @BindView(R.id.good_evalation_radio_button)
    RadioButton radio_good_evalation;
    @BindView(R.id.middle_evalation_radio_button)
    RadioButton radio_middle_evalation;
    @BindView(R.id.bad_evalation_radio_button)
    RadioButton radio_bad_evalation;
    private ShoppingEvaluationAdapter adapter= new ShoppingEvaluationAdapter(null);
//    private List<EvaBean> listData = new ArrayList<EvaBean>(); //当前页的当前
    private List<EvaBean> cachedlistData;
    private int CurrentPage = 0;// 默认从1开始
    private int PAGESIZE = 10; // 定义的一页多少条数据
    private int totalCount = 0 ;
    private boolean isListToBottom = false; //数据是否到顶了
    private View notLoadingView;
    private boolean ishasData = false;//当前是否有数据
    private boolean ishasCachedData = false;//缓存是否有数据
    private int AssessType =0;//默认还全部好评
    private long productID = -1;
    private LayoutInflater inflater;
    public final static int PRODUCR_ALL_EVALATION = 0;
    public final static int PRODUCR_GOOD_EVALATION = 1;
    public final static int PRODUCR_MIDDLE_EVALATION = 2;
    public final static int PRODUCR_BAD_EVALATION = 3;
    @Override
    protected void lazyload() {

    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_shopping_evaluation;
    }
    /*当进入到activity时，多个内嵌的fragment 是会同时执行初始化操作*/
    @SuppressLint("LongLogTag")
    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        productID = getActivity().getIntent().getLongExtra(Constants.PRODUCT_ID, -1);
        NTPTime.getInstance().getCurrentTime();
        initRecyclerView();

        Log.d(TAG, "afterCreate");
        if (productID !=-1) {
            CurrentPage = 1;
            requestCount = 7;
            getDataFromServer(CurrentPage, 0, true);
            refreshCount();
        }
        //变更状态
        checkUserStateLisener();
    }

    private void refreshCount() {
        this.appAction.getAssessmentCount(productID, new ActionCallbackNListener<String>() {
            @Override
            public void onSuccess(String... datas) {
                radio_all.setText("全部(" + datas[0] + ")");
                radio_good_evalation.setText("好评(" + datas[1] + ")");
                radio_middle_evalation.setText("中评(" + datas[2] + ")");
                radio_bad_evalation.setText("差评(" + datas[3] + ")");
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(String errorEvent, String message) {
                if ("998".equals(errorEvent) && requestCount > 0) {
                    requestCount--;
                    refreshCount();
                } else if ("003".equals(errorEvent)) {
                    Toast.makeText(getActivity(), "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "异常代码：" + errorEvent + " 异常说明: " + message);
                }

            }
        });
    }


    private void getDataFromServer(final int currentPage, final int assessType,final boolean isRefreshOrFirstLoading){
        if (isRefreshOrFirstLoading) {
            progressActivity.showLoading();//会一直刷新 先测试下
        }
        this.appAction.getAssessmentList(productID, assessType, currentPage, PAGESIZE, new
                ActionCallbackTripleListener<List<EvaBean>, Integer, Integer>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(List<EvaBean> data, Integer pageIndex, Integer total) {
                        progressActivity.showContent();
                        CurrentPage = pageIndex;
                        totalCount = total;
                        //好，中，差评在分别给出一个评价总数量
                        if (data.isEmpty()) {
                            isListToBottom = true;
                        }
                        //首先就得判断是否有数据，因为这个店在这个区域可能是没有任何服务的，有数据后，先要绑定adapter ，即使没有，也是可以下拉刷新的
                        Log.d(TAG, "totalCount:" + totalCount);
                        /*动态设置级别的评价总数,需要后台提供三个字段，当我第一次刷新时只有全部评价*/
                        switch (assessType) {
                            case PRODUCR_ALL_EVALATION:
                                radio_all.setText("全部(" + totalCount + ")");
                                break;
                            case PRODUCR_GOOD_EVALATION:
                                radio_good_evalation.setText("好评(" + totalCount + ")");
                                break;
                            case PRODUCR_MIDDLE_EVALATION:
                                radio_middle_evalation.setText("中评(" + totalCount + ")");
                                break;
                            case PRODUCR_BAD_EVALATION:
                                radio_bad_evalation.setText("差评(" + totalCount + ")");
                                break;
                        }

                        if (totalCount == 0) {
                            initAdapter(data);
                            adapter.setEmptyView(true, getEmptyView());//如果是希望可以刷新的就用adapter
                        } else {
                            //判断数据是否到底了,没有数据了就到底了,这里必须判断一次
                            if (adapter == null) {
                                adapter = new ShoppingEvaluationAdapter(getActivity(), null);
                            }

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
                                        adapter.setNewData(data);
                                        adapter.openLoadMore(PAGESIZE);
                                        adapter.removeAllFooterView();// 这几个必须添加上
                                    }

                                }
                            }
                        }
                        mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false); // 隐藏旋转的图标
                        mSuperRecyclerViewImg.hideMoreProgress();
                    }

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onFailure(String errorCode, String message) {
                        /*
                        * 对于请求失效这种情况，其实就是不能盲目的加布局，要分三种情况
                        * 1.情况一：没有任何缓冲的数据，也就是首次加载就失败了 。
                        * 2.情况二：已经有数据，刷新操作
                        * 3.情况三：已经有数据，加载更多操作
                        * 分细一点还是好的  情况二和三其实不变，只有情况一才会需要修改下
                        *
                        * 01.如果当前没有数据，缓存也没有数据，则弹出加载按钮  先处理这种情况
                        * 02.如果当前没有数据，缓存有数据，则加载缓存的数据
                        * 03.如果当前有数据，缓存没有数据   【这种情况不可能出现】
                        * 04.如果当前有数据，缓存有数据   【正常，不做任何处理】
                        *
                        * 还有一种场景：当我切换评价时，难道要设置4个list，分别用来存放不同评价类型的数据。这个需要数据来验证
                        * 原则：尽量避免出现请求失效的情况
                        * */


                        if ("998".equals(errorCode) && requestCount > 0) {
                            requestCount--;
                            refresh(isRefreshOrFirstLoading);
                        } else if ("003".equals(errorCode)) {
                            //一定要放点击按钮，是无法下拉刷新和上拉加载的，并且隐藏了所有的下拉图标和加载图标
                            handleError(isRefreshOrFirstLoading, "易，请检查网络");

                        } else {
                            Toast.makeText(getActivity(), "异常代码：" + errorCode + " 异常说明: " +
                                    message, Toast.LENGTH_SHORT).show();
                            handleError(isRefreshOrFirstLoading, "加载失败，请重新点击加载");


                        }


                    }
                });
    }

    private void handleError(boolean isRefreshOrFirstLoading,String Message) {
        if (!isRefreshOrFirstLoading){
            adapter.showLoadMoreFailedView();
        }else {
            PreferenceUtil complexPreferences01 = PreferenceUtil.getSharedPreference(getActivity(),
                    "product");
            Log.d(TAG, "AssessType:" + AssessType);
            cachedlistData = complexPreferences01.getListObject("productid" + productID + "assessType"+AssessType+"EVList", EvaBean.class);
            ishasCachedData = (null != cachedlistData && !cachedlistData.isEmpty()) ? true : false;
            ishasData = (!adapter.getData().isEmpty()) ? true : false;
            if (!ishasData && !ishasCachedData) {
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

    private void showError(String message) {
        progressActivity.showError(message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentPage = 1;
                getDataFromServer(CurrentPage, AssessType, true);

            }
        });
    }

    @SuppressLint("LongLogTag")
    private void refresh(boolean isRefreshOrFirstLoading) {
        if (!isRefreshOrFirstLoading){
            getDataFromServer(CurrentPage + 1, AssessType, false);
        } else {
/*            PreferenceUtil complexPreferences01 = PreferenceUtil.getSharedPreference(getActivity(),
                    "product");
            Log.d(TAG, "AssessType:" + AssessType);
            cachedlistData = complexPreferences01.getListObject("productid" + productID + "assessType"+AssessType+"EVList", EvaBean.class);
            Log.d(TAG, "ishasData:" + ishasData);
            ishasCachedData = (null != cachedlistData && !cachedlistData.isEmpty())?true:false;
            ishasData = (!adapter.getData().isEmpty()) ? true : false;
            if (!ishasData && !ishasCachedData){*/
                getDataFromServer(CurrentPage, AssessType, true);
/*            } else {
                mSuperRecyclerViewImg.getSwipeToRefresh().setRefreshing(false);
                mSuperRecyclerViewImg.hideMoreProgress();
                progressActivity.showContent();
                adapter.notifyDataSetChanged();

            }*/
        }


    }

    private View getEmptyView() {
        inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_empty_view,
                (ViewGroup) mSuperRecyclerViewImg.getParent
                        (), false);
        TextView tv = (TextView) view.findViewById(R.id.empty_content);
        tv.setText("易，沙发空缺，去评价个，下拉刷新下试试！");

        return view;
    }

    private void initAdapter(List<EvaBean> listData) {
        adapter = new ShoppingEvaluationAdapter(listData);
        if (null== mSuperRecyclerViewImg.getAdapter()){
            adapter.openLoadAnimation();
            adapter.setOnLoadMoreListener(this);
            adapter.openLoadMore(PAGESIZE);
            mSuperRecyclerViewImg.setAdapter(adapter);

        }

    }

    //检查用户点击状态，好，中，差
    private void checkUserStateLisener() {
        group_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    //全部评论
                    case R.id.all_radio_button:
                        AssessType=PRODUCR_ALL_EVALATION;
                        Log.d(TAG, "1");
                        requestCount = 5;
                        getDataFromServer(1, AssessType, true);
                        break;
                    //好评
                    case R.id.good_evalation_radio_button:
                        AssessType=PRODUCR_GOOD_EVALATION;
                        Log.d(TAG, "2");
                        requestCount = 5;
                        getDataFromServer(1, AssessType, true);
                        break;
                    //中评
                    case R.id.middle_evalation_radio_button:
                        AssessType=PRODUCR_MIDDLE_EVALATION;
                        Log.d(TAG, "3");
                        requestCount = 5;
                        getDataFromServer(1, AssessType, true);
                        break;
                    //差评
                    case R.id.bad_evalation_radio_button:
                        AssessType=PRODUCR_BAD_EVALATION;
                        Log.d(TAG, "4");
                        requestCount = 5;
                        getDataFromServer(1, AssessType, true);
                        break;
                }

            }
        });

    }

    private void initRecyclerView() {
    /*01.循环视图设置布局管理器*/
        mSuperRecyclerViewImg.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*03.设置下拉刷新的监听器*/
        mSuperRecyclerViewImg.setRefreshListener(this);
        /*05.设置刷新布局的颜色状态*/
        mSuperRecyclerViewImg.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color
                .holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onRefresh() {
        Log.d(TAG, "refresh");
        CurrentPage = 1;
        requestCount = 5;
        getDataFromServer(CurrentPage, AssessType, true);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onLoadMoreRequested() {
        Log.d(TAG, "loadMore");
        requestCount = 5;
        getDataFromServer(CurrentPage + 1, AssessType, false);

    }
}
