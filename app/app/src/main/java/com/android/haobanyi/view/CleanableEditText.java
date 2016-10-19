package com.android.haobanyi.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.android.haobanyi.R;


/**
 * Created by fWX228941 on 2016/4/7.
 *
 * @作者: 付敏
 * @创建日期：2016/04/07
 * @邮箱：466566941@qq.com
 * @当前文件描述：带有右侧删除功能图标的文本编辑框 【这个存在bug，需要修补，不然无法显示出效果】
 *
 */
public class CleanableEditText extends EditText{
    /*分别需要的图标，日志，上下文*/
    private static final String TAG = "CleanableEditText";
    private Drawable right_clean_icon;
    private Context mcontext;
    private static final int DRAWABLE_RIGHT = 2;

    /*构造函数*/
    public CleanableEditText(Context context) {
        super(context);
        this.mcontext=context;
        initCleanableEditText();
    }

    public CleanableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mcontext=context;
        initCleanableEditText();
    }

    public CleanableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mcontext=context;
        initCleanableEditText();
    }

    /*初始化*/
    private void initCleanableEditText() {
        right_clean_icon = getCompoundDrawables()[DRAWABLE_RIGHT];
        if (null == right_clean_icon){
            right_clean_icon = mcontext.getResources().getDrawable(R.drawable.icon_clean);
        }

        //2.默认设置隐藏图标
        setEditTextDrawable(false);
        //3.设置输入框里面内容发生改变的监听
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setEditTextDrawable(true);
            }
        });
        //4.设置焦点改变的监听
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setEditTextDrawable(hasFocus);
            }
        });
    }

    private void setEditTextDrawable(boolean isFocused) {
        if (isFocused && length()>0){
            setCompoundDrawables(null,null,right_clean_icon,null);
        } else {
            setCompoundDrawables(null,null,null,null);
        }

    }

    /*说明：isInnerWidth, isInnerHeight为ture，触摸点在删除图标之内，则视为点击了删除图标
    *event.getX() 获取相对应自身左上角的X坐标
    * event.getY() 获取相对应自身左上角的Y坐标
    * getWidth() 获取控件的宽度
    * getHeight() 获取控件的高度
    * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
    * getPaddingRight() 获取删除图标右边缘到控件右边缘的距离
    *isInnerWidth:
    *getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
    * getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
    *isInnerHeight:
    *distance 删除图标顶部边缘到控件顶部边缘的距离
    * distance + height 删除图标底部边缘到控件顶部边缘的距离
    * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //点击事件的三层逻辑
        if (null != right_clean_icon && event.getAction() == MotionEvent.ACTION_UP){
            //需要判断触摸点是否在删除图标范围内isInnerWidth && isInnerHeight
            if(isInsideIcon(right_clean_icon,event)){
                requestFocus();
                setText("");
                event.setAction(MotionEvent.ACTION_CANCEL);
            }


        }
        return super.onTouchEvent(event);
    }

    private boolean isInsideIcon(Drawable right_clean_icon, MotionEvent event) {
        //获取相对应自身左上角的X坐标
        int x = (int) event.getX();
        //获取相对应自身左上角的Y坐标
        int y = (int) event.getY();
        //获取删除图标的边界，返回一个Rect对象
        Rect rect = right_clean_icon.getBounds();
        //获取删除图标的高度
        int height = rect.height();
        //计算图标底部到控件底部的距离
        int distance = (getHeight() - height) /2;
        //判断触摸点是否在水平范围内
        boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight())) && (x < (getWidth() - getPaddingRight()));
        //触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
        boolean isInnerHeight = (y > distance) && (y < (distance + height));

        return isInnerWidth && isInnerHeight;

    }

    @Override
    protected void onDetachedFromWindow() {
        this.right_clean_icon = null;
        super.onDetachedFromWindow();
    }

    //   要求至少21的API
//    public CleanableEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
}
