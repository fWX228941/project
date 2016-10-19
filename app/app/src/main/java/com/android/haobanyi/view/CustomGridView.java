package com.android.haobanyi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by fWX228941 on 2016/5/18.
 *
 * @作者: 付敏
 * @创建日期：2016/05/18
 * @邮箱：466566941@qq.com
 * @当前文件描述：自定义gridView，热门城市所占空间
 */
public class CustomGridView extends GridView{
    public CustomGridView(Context context) {
        super(context);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /*
    * 1.View.onMeasure方法指定控件在屏幕上的大小
    * 2.其宽高的两个参数是由上一层的控件传入进来的，不是一般的尺寸数值，而是将模式和尺寸结合在一起的测量数值，由MeasureSpec类控制
    * 3.MeasureSpec 常用的三个方法 getMode(int) 根据测量数值提取模式  getSize(int) 根据测量数值提取尺寸大小
    * makeMeasureSpec(int size,int mode)根据提供的尺寸值和模式创建一个测量数值
    * 4.模式常用：  exactly 精确尺寸/ at_most 最大尺寸  父控件允许给出的最大空间，这样告诉下层的View，最大的空间也只有windowsize那么大了，我们在
    * 绘制子View的时候就会根据父View的MeasureSpec和自身的LayoutParams来测量和绘制自身了。
    * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*5.参考文档：http://blog.csdn.net/YanghuiNipurean/article/details/51134025 每次测量高度的时候，将gridView的高度改为
        * AT_MOST模式计算测量，那么子控件的规格大小就是SpecMode-exactly SpecSize-childSize  这样实现任意动态添加hotCity的标签
        * 这个也是解决嵌套冲突的方法之一，参考http://blog.csdn.net/yuhailong626/article/details/20639217
        * */
        /*6.http://wenda.jikexueyuan.com/question/27719/  一旦涉及到多级view嵌套时，按照下面的方式覆写
            http://blog.csdn.net/hanhailong726188/article/details/46136569
          7.<< 左移  >> 右移  为什么sise 是Integer.MAX_VALUE >>2 因为在Android里面，一个控件占据的模式和大小时通过一个整形INT 来表示的
          32位 ，其中最高两位表示模式，低30位表示控件尺寸大小，我们要生成的gridview控件先取integer.max
          _value来获取int值的最大值，然后右移两位得到控件的最大尺寸，低30位。
        * */
        int expandHeightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >>2 , MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandHeightSpec);
    }



































}
