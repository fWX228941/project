package com.android.haobanyi.activity.shopping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.haobanyi.BaseApplication;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.activity.shopping.shopping.ConformOrderActivity;
import com.android.haobanyi.adapter.ShoppingExpandableListAdapter;
import com.android.haobanyi.core.ActionCallbackFivefoldListener;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.util.ToastUtil;
import com.android.haobanyi.view.ProgressActivity;
import com.android.haobanyi.view.TitleBar;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

/**
 * Created by fWX228941 on 2016/6/6.
 *
 * @作者: 付敏
 * @创建日期：2016/06/06
 * @邮箱：466566941@qq.com
 * @当前文件描述：购物车列表
 * bug保留部分：
 *  1.当购物车没有东西时的情况
 *  部分参考设计：https://github.com/louisgeek/LouisShopCart
 *
 */
public class ShoppingPageView extends RelativeLayout {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.id_elv_listview)
    ExpandableListView expandableListView;
    @BindView(R.id.id_cb_select_all)
    CheckBox id_cb_select_all;
    @BindView(R.id.id_tv_totalPrice)
    TextView id_tv_totalPrice;
    @BindView(R.id.id_tv_totalCount_jiesuan)
    TextView id_tv_totalCount_jiesuan;
    @BindView(R.id.id_ll_normal_all_state)
    LinearLayout id_ll_normal_all_state;
    @BindView(R.id.id_tv_save_star_all)
    TextView idTvSaveStarAll;
    @BindView(R.id.id_tv_delete_all)
    TextView idTvDeleteAll;
    @BindView(R.id.id_ll_editing_all_state)
    LinearLayout id_ll_editing_all_state;
    @BindView(R.id.id_rl_foot)
    RelativeLayout id_rl_foot;
    @BindView(R.id.progressActivity)
    ProgressActivity progressActivity;
    private static final String TAG = "fumin_test";
    private boolean isLogin = false;    //是否登录成功
    RadioGroupActivity activity = new RadioGroupActivity(); //一个桥梁，用于实现跳转
    private static final int LOGIN_TO_SPV = 3;//登录成功后，跳转到购物车页面的标识量
    private boolean isHasData =false;
    private boolean isAllowSumUp =false; //是否允许结算
    private long requestCount = 5;
    /**
     * 上下文
     */
    private Context context;
    /**
     * 布局加载器
     */
    private LayoutInflater inflater;
    /*进度条*/
    SpotsDialog dialog;
    //数据源
    List<Map<String, Object>> parentMapList = new ArrayList<Map<String, Object>>(); //定义父列表项list数据集合
    List<List<Map<String, Object>>> childMapList_list = new ArrayList<List<Map<String, Object>>>();//定义子列表项List数据集合,里面的数据集合也就是一个一个列表
    String totalPrice;
    String totalCount;
    //适配器
    ShoppingExpandableListAdapter shoppingExpandableListAdapter;
    PreferenceUtil complexPreferences;
    public ShoppingPageView(Context context) {
        super(context);
        this.context = context;
        this.activity = (RadioGroupActivity) context;
        inflater = LayoutInflater.from(context);
        init();
    }

    public ShoppingPageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        this.activity = (RadioGroupActivity) context;
        inflater = LayoutInflater.from(context);
        init();
    }

    public ShoppingPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.activity = (RadioGroupActivity) context;
        inflater = LayoutInflater.from(context);
        init();
    }

    public ShoppingPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.activity = (RadioGroupActivity) context;
        inflater = LayoutInflater.from(context);
        init();
    }
    /*
    * 购物车环节不纯在缓存，不纯在下拉刷新。
    * */
    private void init() {
        dialog =  new SpotsDialog(context);
        dialog.show();
        setLayout();
        hideTabBar();
        initTittleBar();

        NTPTime.getInstance().getCurrentTime();
        //01，在登录界面首先检查用户是否已经登录
        complexPreferences = PreferenceUtil.getSharedPreference(context, "preferences");
        isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);
        Log.d("AA", "isLogin:" + isLogin);


        if (isLogin){
            long refreshTime = complexPreferences.getLong(Constants.START_TIME,-1000l);
                if (System.currentTimeMillis()/1000L-refreshTime>7200){
                    requestCount = 5;
                    getToken();
                } else {
                    handleLogic();
                }



        } else {
            handleLogic();
        }

        activity.setOnRreshChangedListener(new RadioGroupActivity.OnRreshListener() {
            @Override
            public void onRresh() {
                isLogin = complexPreferences.getBoolean(Constants.ISLOGIN, false);
                handleLogic();
            }
        });

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

    private void loginForToken() {
        complexPreferences.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(activity, LoginActivity.class, LOGIN_TO_SPV);//跳转到登录界面
    }

    private void handleLogic() {
        if (isLogin){
            requestCount = 25;
            GetShoppingCart();
        }else {
            dialog.cancel();
            hideTabBar();
            progressActivity.showEmpty("亲，暂时还没有登录，请先到用户设置的登录界面登录，然后才能查看我的购物车列表！");
        }

    }


    private void GetShoppingCart() {
        dialog.show();
        BaseApplication.getApplication().getAppAction().getShoppingCart(new ActionCallbackFivefoldListener<List<Map
                <String, Object>>, List<List<Map<String, Object>>>, String, String, Boolean>() {


            @Override
            public void onSuccess(List<Map<String, Object>> ParentMapList, List<List<Map<String, Object>>>
                    ChildMapList_list, String TotalPrice, String TotalCount, Boolean IsAllChecked) {
                dialog.cancel();
                progressActivity.showContent();
                //说明是没有产品的
                if (ParentMapList.isEmpty()) {
                    //02.底部的view也是需要隐藏的
                    isHasData = false;
                    initViews(isHasData);
                    progressActivity.showEmpty("易，购物车还是空的，请挑选几个中意的服务吧！");
                    setTittleBarListener(false);
                } else {
                    setTittleBarListener(true);
                    Log.d(TAG, "这个地方");
                    parentMapList = ParentMapList;
                    childMapList_list = ChildMapList_list;
                    totalPrice = TotalPrice;
                    totalCount = TotalCount;
                    initAdapter();
                    //初始化三个参数
                    //结算数量
                    if (totalCount.equals("0")) {
                        isAllowSumUp = false;
                    } else {
                        isAllowSumUp = true;
                    }
                    id_cb_select_all.setVisibility(VISIBLE);
                    id_cb_select_all.setChecked(IsAllChecked);//由后台决定
                    //难道是这个地方出现问题了？
                    isHasData = true;
                    initViews(isHasData);
                }

            }

            @Override
            public void onFailure(String errorEvent, String message) {
                if ("998".equals(errorEvent) && requestCount > 0) {
                    requestCount--;
                    GetShoppingCart();
                } else if ("003".equals(errorEvent)) {
                    dialog.cancel();
                    initViews(false);
                    ToastUtil.networkUnavailable();
                    showError("易，请检查网络");

                } else if ("996".equals(errorEvent)) {
                    dialog.cancel();
                    initViews(false);
                    ToastUtil.showHintMessage("长时间没有登录或同一个账号在不同客户端上同时登录，为保证亲的安全，请重新登录");
                    loginForToken();
                } else {
                    initViews(false);
                    dialog.cancel();
                    ToastUtil.showErrorMessage(errorEvent, message);
                    showError("加载失败，请重新点击加载");
                }



            }
        });

    }
    private void showError(String Message) {
        progressActivity.showError(Message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCount = 15;
                GetShoppingCart();
            }
        });
    }




    private void setLayout() {
        View view = (RelativeLayout) inflater.inflate(R.layout.tab_shopping_page, null);
        ButterKnife.bind(this, view);
        this.addView(view, new LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void initViews(boolean IsHasData) {
        if (IsHasData){
            //显示底部栏
            id_rl_foot.setBackgroundResource(R.color.white);
            id_tv_totalPrice.setVisibility(View.VISIBLE);
            id_tv_totalCount_jiesuan.setVisibility(View.VISIBLE);
/*            idTvSaveStarAll.setVisibility(View.VISIBLE);
            idTvDeleteAll.setVisibility(View.VISIBLE);*/
            id_tv_totalPrice.setText(String.format(context.getString(R.string.total), totalPrice));//合计价格
            id_tv_totalCount_jiesuan.setText(String.format(context.getString(R.string.jiesuan), totalCount));//结算数量
            id_rl_foot.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            //隐藏底部栏
            hideTabBar();

        }


    }

    private void hideTabBar() {
        /*
        *
        * setBackgroundResource会被静态的android:background="@color/white" 颜色给覆盖掉，setBackgroundColor不会
        * 参考：http://blog.csdn.net/little_soybean/article/details/51656577
        * */
        id_rl_foot.setBackgroundColor(context.getResources().getColor(R.color.tittle_view_background_white_));
//        id_rl_foot.setBackgroundColor(R.color.listview_driver);
        id_tv_totalPrice.setVisibility(View.INVISIBLE);
        id_tv_totalCount_jiesuan.setVisibility(View.INVISIBLE);
        id_cb_select_all.setVisibility(View.INVISIBLE);
        id_rl_foot.setBackgroundResource(R.color.tittle_view_background_white_);
        idTvSaveStarAll.setVisibility(View.INVISIBLE);
        idTvDeleteAll.setVisibility(View.INVISIBLE);
    }

    private void setUpListener() {
        shoppingExpandableListAdapter.setOnPriceCountChangeListener(new ShoppingExpandableListAdapter
                .OnPriceCountChangeListener() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void onPriceCountChange(int totalCount, String totalPrice) {
                id_tv_totalPrice.setText(String.format(context.getString(R.string.total), totalPrice));//合计价格
                id_tv_totalCount_jiesuan.setText(String.format(context.getString(R.string.jiesuan), totalCount));//结算数量
                if (totalCount == 0) {
                    isAllowSumUp = false;
                } else {
                    isAllowSumUp = true;
                }
            }
        });

        shoppingExpandableListAdapter.setOnCheckingStateChangeListener(new ShoppingExpandableListAdapter
                .OnCheckingStateChangeListener() {
            @Override
            public void onCheckedBoxNeedChange(boolean allParentIsChecked) {
                id_cb_select_all.setVisibility(VISIBLE);
                id_cb_select_all.setChecked(allParentIsChecked);
            }
        });

        shoppingExpandableListAdapter.setOnEditingStateChangedListener(new ShoppingExpandableListAdapter
                .OnEditingStateChangedListener() {
            @Override
            public void onEditingTvChange(boolean isEditingAll) {

                changeFootShowDeleteView(isEditingAll);//这边类似的功能 后期待使用观察者模式

            }
        });

        shoppingExpandableListAdapter.setOnCheckHasGoodsListener(new ShoppingExpandableListAdapter
                .OnCheckHasGoodsListener() {
            @Override
            public void onCheckHasGoods(boolean isHasGoods) {
                setupViewsShow(isHasGoods);

            }
        });

        shoppingExpandableListAdapter.setOnLoginForTokenListener(new ShoppingExpandableListAdapter
                .OnLoginForTokenListener() {
            @Override
            public void onLoginForToken() {
                loginForToken();
            }
        });
        shoppingExpandableListAdapter.setOnRefreshServerListener(new ShoppingExpandableListAdapter.OnRefreshServerListener() {

            @Override
            public void onRefreshServer() {
                init();//重新更新
            }
        });

        // 监听回退事件
        shoppingExpandableListAdapter.setOnPopUpWindowsListener(new ShoppingExpandableListAdapter.OnPopUpWindowsListener() {


            @Override
            public void onPopUp(boolean isHasPopWindws) {
                Log.d("kkkkkk", "isHasPopWindws:" + isHasPopWindws);
                activity.setIsHasPopWindows(isHasPopWindws);
            }
        });

    }

    private void initAdapter() {

        /*
        * 添加逻辑：
        *
        *        if(list==null){
        *        提示，赶紧选几件
                }

                调用流程：new ShoppingExpandableListAdapter-》调用hasStableIds-》调用getGroupCount-》
                setAdapter-》调用getGroupId-》groupPosition从0开始 -》调用getChildrenCount-》childMapList_list.get(groupPosition)和childMapList_list.get(groupPosition).size()
                都是有值的[{childName=com.android.haobanyi.model.bean.shopping.ShoppingChildItemBean@20882d5b}

                搞完以后添加视图 调用getGroupView -》调用getChildView
        * */


        shoppingExpandableListAdapter = new ShoppingExpandableListAdapter(context, parentMapList, childMapList_list);
        Log.d(TAG, "初始化");
        expandableListView.setAdapter(shoppingExpandableListAdapter);
        Log.d(TAG, "相互绑定");
        //expandableListView.getCount();当所有项设置为默认展开
        for (int i = 0; i < parentMapList.size(); i++) {
            Log.d(TAG, "前i:" + i);
            expandableListView.expandGroup(i);
            Log.d(TAG, "后i:" + i);
        }

        /*
        * 展开，原理很简单，截获，拦截点击操作
        * http://www.xuebuyuan.com/875180.html
        * http://blog.csdn.net/mimi5821741/article/details/49128575
        * */
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;//拦截
            }
        });

        /*3.在有数据的情况下，设置监听器才有意义*/
        setUpListener();

    }

    private void initTittleBar() {
        titleBar.setBackgroundResource(R.color.white);
        titleBar.setTitle("购物车");
        titleBar.setDividerColor(Color.GRAY);

    }

    private void setTittleBarListener(Boolean flag) {
        if (isLogin){
            if (flag){
                titleBar.setActionTextColor(Color.BLACK);
                titleBar.addAction(new TitleBar.TextAction("编辑全部") {
                    @Override
                    public void performAction(View v) {
                /*这里逻辑需要修改下*/
                        // 点击编辑全部
                        if (v instanceof TextView) {
                            TextView tv = (TextView) v;
                            // 当为编辑状态时 视图部分发生变更
                            if (ShoppingExpandableListAdapter.FINISH_EDITING.equals(tv.getText())) {
                                shoppingExpandableListAdapter.setupEditingAll(false);
                                tv.setText("编辑全部");
                                changeFootShowDeleteView(false);//这边类似的功能 后期待使用观察者模式

                            } else {
                                shoppingExpandableListAdapter.setupEditingAll(true);
                                tv.setText(ShoppingExpandableListAdapter.FINISH_EDITING);
                                changeFootShowDeleteView(true);//这边类似的功能 后期待使用观察者模式
                            }

                        }
                    }
                });
            }

        }
    }

    /*底部视图发生变更*/
    public void changeFootShowDeleteView(boolean showDeleteView) {

        if (showDeleteView) {
            idTvSaveStarAll.setVisibility(View.VISIBLE);
            idTvDeleteAll.setVisibility(View.VISIBLE);
            id_ll_normal_all_state.setVisibility(View.INVISIBLE);
            id_ll_editing_all_state.setVisibility(View.VISIBLE);
        } else {

            id_ll_normal_all_state.setVisibility(View.VISIBLE);
            id_ll_editing_all_state.setVisibility(View.INVISIBLE);
        }
    }
    /*这有一个初始状态，当没有购物车时，会有提示，请购物*/
    private void setupViewsShow(boolean isHasGoods) {
        if (isHasGoods) {
            expandableListView.setVisibility(View.VISIBLE);
         //   id_rl_cart_is_empty.setVisibility(View.GONE);
//            id_rl_foot.setVisibility(View.VISIBLE);
        } else {
            progressActivity.showEmpty("易，购物车还是空的，请挑选几个中意的服务吧！");
            titleBar.removeActionAt(0);
            expandableListView.setVisibility(View.GONE);
            hideTabBar();


        }
    }
    @OnClick({R.id.id_cb_select_all, R.id.id_tv_totalCount_jiesuan, R.id.id_tv_save_star_all, R.id.id_tv_delete_all})
    public void onClick(View view) {
        switch (view.getId()) {
            //01.全选
            case R.id.id_cb_select_all:
                selectAll();
                break;
            //02.结算
            case R.id.id_tv_totalCount_jiesuan:
                gotoSumAccount();
                break;
            //03.移动到收藏夹
            case R.id.id_tv_save_star_all:
                moveToFavorite();
                break;
            //04.全部删除
            case R.id.id_tv_delete_all:
                deleteAll();
                break;
        }
    }

    private void gotoSumAccount() {
        //这里不需要作判断， 其实全部删除的时候的时候就已经，结算非0的才允许删除
        if (isAllowSumUp){
            IntentUtil.gotoActivityWithoutData(context, ConformOrderActivity.class, false);
        }

    }



    private void selectAll() {
            final boolean isSelectAll = id_cb_select_all.isChecked();
            Log.d("zhge", "isSelectAll:" + isSelectAll); // 这个居然是false  ，初始状态是false 必须要手动点击
            if (!isSelectAll){
                requestCount = 15;
                cancelCartAllItem();
            }else {
                requestCount = 15;
                selectCartAllItem();
            }
    }

    private void selectCartAllItem() {
        BaseApplication.getApplication().getAppAction().selectCartAllItem(new ActionCallbackListener<String>() {
            @Override
            public void onSuccess(String tatalPrice) {
                dialog.cancel();
                shoppingExpandableListAdapter.setupAllChecked(true, tatalPrice);
            }

            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    //如果出现请求失效的情况，才如实处理
                    dialog.show();
                    selectCartAllItem();
                }else if ("996".equals(errorCode)){
                    dialog.cancel();
                    id_cb_select_all.setVisibility(VISIBLE);
                    id_cb_select_all.setChecked(false);
                    loginForToken();
                }else if ("003".equals(errorCode)){
                    dialog.cancel();
//                    shoppingExpandableListAdapter.setupAllCheckedFailed(false);  //调用其实是重新绘制，老实说这种态度我是真厌恶
                    id_cb_select_all.setVisibility(VISIBLE);
                    id_cb_select_all.setChecked(false);
                    ToastUtil.networkUnavailable();
                }else{
                    dialog.cancel();
//                    shoppingExpandableListAdapter.setupAllCheckedFailed(false);
                    id_cb_select_all.setVisibility(VISIBLE);
                    id_cb_select_all.setChecked(false);
                    ToastUtil.showErrorMessage(" 加载失败: " + errorMessage + "," + errorCode);
                }

            }
        });
    }

    private void cancelCartAllItem() {
        BaseApplication.getApplication().getAppAction().cancelCartAllItem(new ActionCallbackListener<String>() {
            @Override
            public void onSuccess(String tatalPrice) {
                dialog.cancel();
                shoppingExpandableListAdapter.setupAllChecked(false,tatalPrice);
            }
            @Override
            public void onFailure(String errorCode, String errorMessage) {
                if ("998".equals(errorCode) && requestCount > 0){
                    requestCount--;
                    dialog.show();
                    cancelCartAllItem();
                }else if ("996".equals(errorCode)){
                    dialog.cancel();
                    id_cb_select_all.setVisibility(VISIBLE);
                    id_cb_select_all.setChecked(true);
                    loginForToken();
                }else if ("003".equals(errorCode)){
                    //如果是取消失败了，则不取消了保存不变
                    dialog.cancel();
//                    shoppingExpandableListAdapter.setupAllCheckedFailed(true);//如果还发生这种错误就重新刷新下
                    id_cb_select_all.setVisibility(VISIBLE);
                    id_cb_select_all.setChecked(true);
                    ToastUtil.networkUnavailable();
                }else{
                    dialog.cancel();
//                    shoppingExpandableListAdapter.setupAllCheckedFailed(true);
                    id_cb_select_all.setVisibility(VISIBLE);
                    id_cb_select_all.setChecked(true);
                    ToastUtil.showErrorMessage(" 加载失败: " + errorMessage + "," + errorCode);
                }


            }
        });
    }

    private void deleteAll() {
        shoppingExpandableListAdapter.removeAllGoods();
    }

    private void moveToFavorite() {
        shoppingExpandableListAdapter.addToFavGoods();
    }
}
