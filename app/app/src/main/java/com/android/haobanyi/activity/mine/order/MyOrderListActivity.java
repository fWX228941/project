package com.android.haobanyi.activity.mine.order;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;
import com.android.haobanyi.activity.RadioGroupActivity;
import com.android.haobanyi.activity.guid.LoginActivity;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.util.Constants;
import com.android.haobanyi.util.IntentUtil;
import com.android.haobanyi.view.TitleBar;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import butterknife.BindView;

import static android.databinding.tool.processing.Scope.exit;

/**
 * Created by fWX228941 on 2016/8/8.
 *
 * @作者: 付敏
 * @创建日期：2016/08/08
 * @邮箱：466566941@qq.com
 * @当前文件描述：我的订单界面 activity_my_order_list
 * AllOrderListFragment
 * UnpaidOrderListFragment
 * UnHandledOrderListFragment
 * UnConformOrderListFragment
 * UnEvaluateListFragment
 */
public class MyOrderListActivity extends BaseActivity {
    @BindView(R.id.smartTabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.title_bar)
    TitleBar titleBar;
//    private static final int LOGIN_TO_SPV = 3;
    private String[] categoryArray; //测试数据
    int positon = 0;
    private boolean isBackToHome = false;

    @Override
    protected int setLayout() {
        return R.layout.activity_my_order_list;
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
                if ("998".equals(errorCode) && requestCount > 0) {
                    requestCount--;
                    getToken();
                } else {
                    handleLogic();
                }

            }
        });
    }

/*    private void loginForToken() {
        basePreference.putBoolean(Constants.CHECK_FOR_RESULT, true);
        IntentUtil.gotoActivityForResult(MyOrderListActivity.this, LoginActivity.class, LOGIN_TO_SPV);//跳转到登录界面
    }*/

    /*所有逻辑放在这里统一处理*/
    private void handleLogic() {
        categoryArray = getResources().getStringArray(R.array.category_order_list);
        positon = this.getIntent().getIntExtra(Constants.POSITION, 0);
        isBackToHome= this.getIntent().getBooleanExtra("back_to_homePage", false);
        Log.d("MyOrderListActivity", "isBackToHome:" + isBackToHome);
        //这个地方一定不能改，因为涉及到时序问题
        initTittleBar();
        initViewPager();

    }

    @Override
    protected void initViews() {


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
                Log.d("MyOrderListActivity", "isBackToHome:" + isBackToHome);
                if (isBackToHome) {
                    IntentUtil.gotoActivityWithoutData(MyOrderListActivity.this, RadioGroupActivity.class,true);
                }else {
                    finish();
                    MyOrderListActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);
                }

            }
        });

        titleBar.setTitle("我的订单");
        titleBar.setDividerColor(Color.GRAY);
    }

    private void initViewPager() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(categoryArray[0], AllOrderListFragment.class)
                .add(categoryArray[1], AllOrderListFragment.class)
                .add(categoryArray[2], AllOrderListFragment.class)
                .add(categoryArray[3], AllOrderListFragment.class)
                .add(categoryArray[4], AllOrderListFragment.class)
                .create());
        Log.d("MyOrderListActivity", "positon01:" + positon);
        //viewpager.setCurrentItem(positon,true);
        //viewpager.setCurrentItem(positon);//这个失效了，真是蛋疼
        adapter.getPage(positon);
        viewpager.setAdapter(adapter);// 左右滑动页面关联fragment适配器
        /*这里有一个顺序问题：
        *
        *       viewpager.setCurrentItem(positon); 必须在viewpager.setAdapter(adapter)之后
        *https://segmentfault.com/q/1010000004254038
        * */
        viewpager.setCurrentItem(positon);//这个失效了，真是蛋疼
        //02.设置ViewPager缓存个数，意味这个多次调用onViewCreated方法
        /*
        * http://blog.csdn.net/liao277218962/article/details/50675570
        *,这表示你的预告加载的页面数量是1,假设当前有四个Fragment的tab,显示一个,预先加载下一个.这样你在移动前就已经加载了下一个界面
        * */
        viewpager.setOffscreenPageLimit(0);//adapter.getCount()  这样是不是只会调用一次了 调用了两次
        //03.关联smartTabLayout和ViewPager
        smartTabLayout.setViewPager(viewpager);
        if(progressDialog.isShowing()){
            progressDialog.cancel();
        }

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

    //bundle.putBoolean("back_to_homePage",true);


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //如果是从购物车支付过来的后退返回到首页
            Log.d("MyOrderListActivity", "isBackToHome:" + isBackToHome);
            Log.d("MyOrderListActivity", "this.getIntent().gefalse):" + this
                    .getIntent().getBooleanExtra("back_to_homePage", false));
            if (this.getIntent().getBooleanExtra("back_to_homePage", false)) {
                Log.d("MyOrderListActivity", "d");
                IntentUtil.gotoActivityWithoutData(this, RadioGroupActivity.class,true);
                return true;//直接拦截，并且只拦截一次就好了
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
