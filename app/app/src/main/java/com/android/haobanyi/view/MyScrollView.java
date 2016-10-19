package com.android.haobanyi.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by fWX228941 on 2016/4/14.
 *
 * @作者: 付敏
 * @创建日期：2016/04/14
 * @邮箱：466566941@qq.com
 * @当前文件描述：自定义带滑动监听接口和各种动漫效果的ScrollView
 */
public class MyScrollView extends ScrollView{
    /** 滑动监听 */
    private OnScrollListener onScrollListener;
    /** 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较 */
    private int lastScrollY;

    public MyScrollView(Context context) {
        super(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置滚动接口
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /** 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中 */
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            int scrollY = MyScrollView.this.getScrollY();

            // 此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if (onScrollListener != null) {
                onScrollListener.onScroll(scrollY);
            }

        };

    };

    /**
     * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候，
     * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候，
     * MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理
     * MyScrollView滑动的距离
     *
     * @version 1.0
     * @createTime 2015-5-4,下午5:32:44
     * @updateTime 2015-5-4,下午5:32:44
     * @createAuthor luming
     * @updateAuthor luming
     * @updateInfo
     * @param ev
     *            触摸事件
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (onScrollListener != null) {
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:// 触摸结束
                handler.sendMessageDelayed(handler.obtainMessage(), 20);
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener {
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         */
        public void onScroll(int scrollY);
    }
}
