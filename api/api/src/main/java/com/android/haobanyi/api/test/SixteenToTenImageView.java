package com.android.haobanyi.api.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自定义一个16比10的高宽比，以适应屏幕的图片大小
 */
public class SixteenToTenImageView extends ImageView {

    public SixteenToTenImageView(Context context) {
        super(context);
    }

    public SixteenToTenImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SixteenToTenImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*逻辑需要自己定义*/
        int width = MeasureSpec.getSize(widthMeasureSpec);
        // 锁定宽高位16:10
        int height = (int) (width * 10f / 16);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
