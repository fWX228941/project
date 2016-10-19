package com.android.haobanyi.activity.mine.receipt;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.android.haobanyi.BaseActivity;
import com.android.haobanyi.R;


import com.android.haobanyi.view.TitleBar;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/8/4.
 *
 * @作者: 付敏
 * @创建日期：2016/08/04
 * @邮箱：466566941@qq.com
 * @当前文件描述：发票
 *
 */
public class ReceiptActivity extends BaseActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.smartTabLayout)
    SmartTabLayout smartTabLayout;
    private String[] categoryArray;
    @Override
    protected int setLayout() {
        return R.layout.activity_receipt;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        categoryArray = getResources().getStringArray(R.array.category_receipt_list);
    }

    @Override
    protected void initViews() {
        initTittleBar();
        initViewPager();
    }
    private void initViewPager() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(categoryArray[0], ReceiptGIFragment.class)
                .add(categoryArray[1], ReceiptVATFragment.class)
                .create());
        viewpager.setAdapter(adapter);// 左右滑动页面关联fragment适配器
        //02.设置ViewPager缓存个数，意味这个多次调用onViewCreated方法
        viewpager.setOffscreenPageLimit(adapter.getCount());
        //03.关联smartTabLayout和ViewPager
        smartTabLayout.setViewPager(viewpager);

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
                ReceiptActivity.this.overridePendingTransition(R.anim.exit_enter, R.anim.exit_exi);

            }
        });

        titleBar.setTitle("发票工具");
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


}
