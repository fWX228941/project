package com.android.haobanyi.activity.guid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.haobanyi.R;
import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.adapter.GuideViewPagerAdapter;
import com.android.haobanyi.core.ActionCallbackListener;
import com.android.haobanyi.listener.PageChangeListener;

import java.util.ArrayList;
import java.util.List;


import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.util.AppConstants;
import com.android.haobanyi.util.PreferenceUtil;

public class WelcomeGuideActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager vp;
    private GuideViewPagerAdapter adapter;
    private List<View> views;
    private Button startBtn;
    // 引导页图片资源  用的是一个数组存放页面布局文件
    //, R.layout.guid_view3
    private static final int[] pics = { R.layout.guid_view1,
            R.layout.guid_view2,R.layout.guid_view3, R.layout.guid_view4 };
    // 底部小点图片
    private ImageView[] dots;
    // 记录当前选中位置
    private int currentIndex;

    // 这个引导页面应该包含全部内容
    @Override
    protected int setLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        getDataFromServer();

    }
    /*加载首页的数据项,这个地方到时候还是需要修改完善一下*/
    private void getDataFromServer() {
        /*程序首次运行时，只加载一次*/
            this.appAction.getProductListByNormalSort01(1, 10, new
                    ActionCallbackListener<List<SortDataBean>>() {
                        @Override
                        public void onSuccess(List<SortDataBean> data) {
                            com.android.haobanyi.model.util.PreferenceUtil complexPreferences = com.android.haobanyi
                                    .model.util.PreferenceUtil.getSharedPreference(context, "product");
                            Log.d(TAG, complexPreferences.getListObject("sortBean", SortDataBean.class).toString());
                            Log.d(TAG, "数据存储成功");

                        }
                        @Override
                        public void onFailure(String errorCode, String errorMessage) {
                        }
                    });
    }
    @Override
    protected void initViews() {
        initImage();
        //initDots();
        loadViewPager();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台,或者手机重启，就设置下次不进入功能引导页
        PreferenceUtil.setSharedPreference(WelcomeGuideActivity.this,AppConstants.KEY_FIRST_OPEN,false);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    //回调函数中的方法调用：

    private void initImage() {
        views = new ArrayList<View>();

        // 初始化引导页视图列表  4张引导页
        for (int i = 0; i < pics.length; i++) {
            View view = LayoutInflater.from(this).inflate(pics[i], null);
            // 这样就实现了嵌套，即在最后的导航也嵌上按钮，这个地方有点费解
            if (i == pics.length - 1) {
                startBtn = (Button) view.findViewById(R.id.btn_login);
                startBtn.setTag("enter");
                startBtn.setOnClickListener(this);
            }

            views.add(view);

        }
    }
    private void loadViewPager() {
        vp = (ViewPager) findViewById(R.id.vp_guide);

        // 初始化adapter
        adapter = new GuideViewPagerAdapter(views);
        vp.setAdapter(adapter);
        vp.setOnPageChangeListener(new PageChangeListener());


    }
    // 点击事件
    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterLoginActivity();
            return;
        }

        int position = (Integer) v.getTag();
        setCurView(position);
    }
    /**
     * 设置当前view
     *
     * @param position
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        vp.setCurrentItem(position);
    }
    // 点击事件涉及到的函数调用
    private void enterLoginActivity() {
        // 在第二次重启的时候就直接跳转到logo界面
        PreferenceUtil.setSharedPreference(WelcomeGuideActivity.this, AppConstants.KEY_FIRST_OPEN, false);
        Intent intent = new Intent(WelcomeGuideActivity.this,
                LoginActivity.class);
        startActivity(intent);

        finish();
    }


}
