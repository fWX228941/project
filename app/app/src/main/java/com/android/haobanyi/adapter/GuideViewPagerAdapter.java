package com.android.haobanyi.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by fWX228941 on 2016/3/24.
 * 描述：关联功能引导页WelcomeGuideActivity的页面适配器
 */
public class GuideViewPagerAdapter extends PagerAdapter {
    private List<View> views;
    public GuideViewPagerAdapter(List<View> views) {
        super();
        this.views = views;
    }

    /**
     * 这几个构造方法到时优化
     * @return
     */
    @Override
    public int getCount() {
        if (null != views) {
            return views.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return  view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(views.get(position), 0);
        return views.get(position);
    }
}
