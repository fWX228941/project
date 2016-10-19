package com.android.haobanyi.util.AutoUtil;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by fWX228941 on 2016/4/28.
 *
 * @作者: 付敏
 * @创建日期：2016/04/28
 * @邮箱：466566941@qq.com
 * @当前文件描述：常用视图单位转换类
 */
public class DensityUtils {
    private DensityUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
        /*
        *  方法二：和下面的是重复的么？
        *        public static int dip2px(Context context, float dp) {
                 float scale = context.getResources().getDisplayMetrics().density;
                 return (int) (dp * scale + 0.5f);
                 }
        *
        *
        *
        * */
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * sp转px
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

}
