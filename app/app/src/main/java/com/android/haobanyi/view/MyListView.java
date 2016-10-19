package com.android.haobanyi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by fWX228941 on 2016/6/12.
 *
 * @作者: 付敏
 * @创建日期：2016/06/12
 * @邮箱：466566941@qq.com
 * @当前文件描述：解决可滑动控件冲突问题，显示混乱\
 * 参考设计：http://51up.club/2015/07/23/解决scrollview中嵌套listview、gridview的问题/
 * listView高度无法计算,嵌套中的子ListView是无法滑动的，因为子控件的滑动事件会被外面的ScrollView吃掉，只能强行的截取滑动的相关事件了
 * 但在加载更多和上拉刷新，会出现不停的测量，不停的刷新数据，手机屏幕乱跳等现象。解决办法：暂无。有能力再解决。
 *
 */
public class MyListView extends ListView{
    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
