package com.android.haobanyi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.haobanyi.R;
import com.android.haobanyi.util.AutoUtil.DensityUtils;

/**
 * Created by fWX228941 on 2016/5/11.
 *
 * @作者: 付敏
 * @创建日期：2016/05/11
 * @邮箱：466566941@qq.com
 * @当前文件描述：自定义侧边栏
 */
public class SideBar extends View {
    /*1.26个字母提示，用数组就是了，涉及到的数据*/
    /*2.上下文*/
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    // 26个字母
    public static String[] lettersHint = { "热门","A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#" };
    private int choose = -1;// 选中
    private Paint paint = new Paint();
//	private LinearLayout linearLayout;

    private TextView mTextDialog;//是当用手点击或者滚动侧边栏时，屏幕中央会显示的大写字母的视窗
    private Context mContext;
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }


    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public SideBar(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
//	获取宽和高只有在计算view布局时onDraw才会真正生效，否则像在构造函数中调用这两个方法都没有意义，返回的结果也是0
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / lettersHint.length;// 获取每一个字母的高度

        for (int i = 0; i < lettersHint.length; i++) {
            //设置颜色，这里Android内部定义的有Color类包含了一些常见颜色定义
            paint.setColor(Color.GRAY);
            // paint.setColor(Color.WHITE);
            //设置字体，Typeface包含了字体的类型，粗细，还有倾斜、颜色等。
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            //  //是否抗锯齿
            paint.setAntiAlias(true);
            //void  setTextSize(float textSize)  //设置字体大小  字体大小需要使用密度工具
            paint.setTextSize(DensityUtils.dip2px(mContext, 10));
            // 选中的状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                //设置伪粗体文本
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(lettersHint[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(lettersHint[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();//通过MotionEvent.getAction()方法来得到用户触发操作时哪一个具体的事件，动态，移动产生大量的事件，event对象记录发生的操作
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * lettersHint.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
//		由activity来控制
        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;//
                //每次事件都是需要重新绘制控件视图
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
//			右侧边栏的背景
                setBackgroundResource(R.drawable.sidebar_background);
                if (oldChoose != c) {
                    if (c >= 0 && c < lettersHint.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(lettersHint[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(lettersHint[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;// 事件会发给当前View，并有dispatchTouchEvent方法，也就是当前方法来消费，同时事件停止向下传递
    }
//	自定义接口类型
    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author coder
     *
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }



}
