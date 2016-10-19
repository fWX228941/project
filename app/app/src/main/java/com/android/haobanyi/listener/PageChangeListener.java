package com.android.haobanyi.listener;

import android.support.v4.view.ViewPager;

/**
 * Created by fWX228941 on 2016/3/24.
 * 描述：关联功能引导页WelcomeGuideActivity的页面滑动监听器
 */
public class PageChangeListener implements ViewPager.OnPageChangeListener {
    // 当前页面被滑动时调用
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // arg0 :当前页面，及你点击滑动的页面
        // arg1:当前页面偏移的百分比
        // arg2:当前页面偏移的像素位置
    }
    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int position) {

    }
    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int state) {
        // arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。

    }
}
