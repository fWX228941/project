package com.android.haobanyi.activity.mine.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.core.service.NTPTime;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.model.util.PreferenceUtil;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.view.TitleBar;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/8/18.
 *
 * @作者: 付敏
 * @创建日期：2016/08/18
 * @邮箱：466566941@qq.com
 * @当前文件描述：我的退款界面
 * 复用也不是无止境的复用，往往会增加复杂度，这个时候就是模式问题了
 * 再多写一个无所谓了
 */
public class MyRefundListActivity extends BaseActivity {
    private boolean isLogin = false;
    private static final int LOGIN_TO_SPV = 3;
    PreferenceUtil complexPreferences;
    @BindView(R.id.smartTabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    private String[] categoryArray; //测试数据   // 到时这里需要再次重构一下，
    Bundle bundle = new Bundle();
    private long requestCount = 5;

    @Override
    protected int setLayout() {
        return  R.layout.activity_my_order_list;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

        if(!progressDialog.isShowing()){
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


    private void handleLogic() {
        categoryArray = getResources().getStringArray(R.array.category_my_refund_list);
        initTittleBar();
        initViewPager();


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
                MyRefundListActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
            }
        });

        titleBar.setTitle("我的订单");
        titleBar.setDividerColor(Color.GRAY);
    }

    private void initViewPager() {
        //name 和 position  都需要标注下
        bundle.putBoolean(Constants.IS_COME_FROM_MYREFUND, true);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(categoryArray[0], AllOrderListFragment.class,bundle)
                .add(categoryArray[1], AllOrderListFragment.class,bundle)
                .add(categoryArray[2], AllOrderListFragment.class,bundle)
                .create());

        viewpager.setAdapter(adapter);// 左右滑动页面关联fragment适配器
        viewpager.setCurrentItem(0);
        //02.设置ViewPager缓存个数，意味这个多次调用onViewCreated方法
        viewpager.setOffscreenPageLimit(1);//adapter.getCount() 缓存一个就好了
        //03.关联smartTabLayout和ViewPager
        smartTabLayout.setViewPager(viewpager);

        if(progressDialog.isShowing()){
            progressDialog.cancel();
        }


    }
    @Override
    protected void initViews() {

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
}
