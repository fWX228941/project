package com.android.haobanyi.api.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by fWX228941 on 2016/4/28.
 *
 * @作者: 付敏
 * @创建日期：2016/04/28
 * @邮箱：466566941@qq.com
 * @当前文件描述：ViewHolder用于持有并存储itemView布局中的控件，通常是在adapter的getView回调方法中获取
 * 设计参考文档：http://www.cnblogs.com/tianzhijiexian/p/4157889.html
 * http://blog.csdn.net/lmj623565791/article/details/38902805
 * https://github.com/hongyangAndroid/baseAdapter/blob/master/base-adapter-library/src/main/java/com/zhy/base/adapter/ViewHolder.java  更多控件任务参考
 */
public class ViewHolder {
    private static View mConvertView;
    private  SparseArray<View> mViews; //存在的这些引用，也就是值得类型
    private Context mContext;
    private int mLayoutId;
    private int mPosition;

    /*在构造函数中静态的成员函数是可以使用的，而在静态方法中非静态的变量是无法使用滴*/
    public ViewHolder(Context context, View itemView, ViewGroup parent, int position) {
//        默认是10个控件，难道说自动put  http://blog.csdn.net/easyer2012/article/details/37871031  http://blog.csdn.net/wangbadan007007/article/details/49074005
        this.mViews = new SparseArray<View>();
        this.mConvertView = itemView;
        this.mPosition = position;
        this.mContext = context;
        /*建立关联之前的*/
        mConvertView.setTag(this); // 㛑就是第一次说这个ViewHolder中是没有存放任何控件的
    }
    /*
    * 1.两个变化的点：
    *   01.视图item 会变化
    *   02.控件元素会变化   —— 解决方案：不提供任何具体的控件的成员变量，而是提供一个容器，专门存储每个不同item布局的所有控件，这样就不在是一对一的关系，而形成的是多对一的关系
    *   控件存储的容器选为SparseArray，一个似MAP 以键值对的方式保存数据，但是键必须是整形Interger，显然是效率更高的，控件的Id是键，控件的引用是值,多个adapter只需要依赖一个ViewHolder就行了
    * 2.不变的逻辑：
    *   01.通过View.setTag(class ViewHolder) 更通用的是参数扩大为object，使得ViewHolder与view相互绑定，当view视图复用时，直接从与之对应的ViewHolder中拿到View布局的控件
    *   通过view.getTag() 获取响应的ViewHolder .
    * 3.编程目的：省去findViewById 的时间，看来是不建议使用这个
    * 4.重构目的：写出通用的ViewHolder，封装变化的点，使其对于任意的view，提供一个ViewHolder实例对象，让其setTag即可
    * 5.编程步骤：
    *   01.服务端提供两点，其一是提供一个ViewHolder对象，通过提供不同itemView的布局id
    *   02.通过提供id，也就是作为参数给ViewHolder来获取控件引用实例对象
    * 6.进一步提供接口，因为ViewHolder 持有控件对象，而我们最终的目的是利用这些控件对象来添加文字，添加图片，也就是常用控件的常用方法进一步封装
    *
    * */
    public static ViewHolder getViewHolder(Context context,View convertView, ViewGroup parent, int layoutId,int position){
        //如果是首次创建itemView视图，老老实实 这个convertView 在getview 第二次调用的时候就被赋予了mConvertView
        if (null == convertView) {

            //先把itemViewId转化为 itemView
            View itemView = LayoutInflater.from(context).inflate(layoutId,parent,false);
            ViewHolder viewHolder = new ViewHolder(context, itemView, parent, position);
            viewHolder.mLayoutId = layoutId;
            return viewHolder;

        }else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;

        }

    }

    /*
      功能：通过控件的ID获取对应的控件，如果是首次，即没有的情况下，则插入
    * 1.这里实际上是延迟了存储控件的过程，只有当用到的时候才会存储，而且是第一次使用时才会用findViewById，并存放在容器中，到了第二次就直接从对应的
    * 容器中以整形的控件ID值来获取就是了，用容器取代了具体的控件成员变量，
    * 2.先按照正常的使用来获取，在获取具体控件之前，做一层是否为空，也就是是否为第一次的判断逻辑
    * 3.对于不同类型的控件，只要记住一点他们都是继承View，也就是View的子类，就可以利用多态性+继承来实现向下转型，不然是类型的控件，
    * 在运行时都是可以确定的，子类的声明= View类的实例，实现动态的类型转化，所以在返回类型的时候添加一个继承
    *<T extends View>  这种方式值得学习
    *return (T) view;
    * */
    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if (null == view) {
            /*这个是只需要一次findViewById*/
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);

        }
        return (T) view;

    }
    //获取item的布局
    public View getConvertView(){

        return mConvertView;
    }

    public int getLayoutId()
    {
        return mLayoutId;
    }
    public void updatePosition(int position)
    {
        mPosition = position;
    }
    public ViewHolder setTag(int viewId, Object tag)
    {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ViewHolder setTag(int viewId, int key, Object tag)
    {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }
    // 以下方法采取流接口的方式，依据不同类型的控件Id来动态【多态】转化为相应的控件，执行响应的操作


    /*
    *
    * 设置TextView的值  文字显示以及字体颜色，字体样式和链接
    * @param viewId
    * @param text
    * @return ViewHolder
    * */
    public ViewHolder setText(int viewId, String text ){
        TextView textView = getView(viewId);
        textView.setText(text);
        return  this;

    }

    public ViewHolder setText(int viewId, int resId ){
        TextView textView = getView(viewId);
        textView.setText(resId);
        return  this;

    }

    public ViewHolder setTextColor(int viewId, int textColor)
    {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }
    public ViewHolder setTextColorRes(int viewId, int textColorRes)
    {
        TextView view = getView(viewId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setTextColor(mContext.getColor(textColorRes));
        } else {
            view.setTextColor(mContext.getResources().getColor(textColorRes));//在敲代码的时候敲首字母，速度回更快，效率也会更高,所以要求字母大写
        }
        return this;
    }



    /*ImageView图片显示*/
    /*需要添加一个glide框架来加载图片*/
    public ViewHolder setImageResource(int viewId, int resId){
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap){
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);//根据参数来智能匹配还是挺不错的
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable){
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder addLinks(int viewId)
    {
        TextView view = getView(viewId);
        /*地址，邮箱，电话，网站*/
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ViewHolder setTypeface(Typeface typeface, int... viewIds)
    {
        for (int viewId : viewIds)
        {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);

        }
        return this;
    }
    /*ProgressBar 进度条*/
    public ViewHolder setProgress(int viewId, int progress){
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress, int max){
        ProgressBar view = getView(viewId);
//        设置进度条的上限值
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }
    public ViewHolder setMax(int viewId, int max)
    {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }
    /*View设置背景颜色图片,可见性，透明度*/

    public ViewHolder setBackgroundColor(int viewId, int color)
    {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundRes(int viewId, int backgroundRes)
    {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }
    @SuppressLint("NewApi")
    public ViewHolder setAlpha(int viewId, float value)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else
        {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public ViewHolder setVisible(int viewId, boolean visible)
    {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }
    /*RatingBar评分控件*/
    public ViewHolder setRating(int viewId,float rating){
        RatingBar ratingBar = getView(viewId);
        ratingBar.setRating(rating);
        return this;
    }
    public ViewHolder setRating(int viewId, float rating, int max)
    {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /*勾选控件的，是否可勾选*/
    public ViewHolder setChecked(int viewId, boolean checked){
        Checkable v = getView(viewId);
        v.setChecked(checked);
        return this;
    }

    /*事件，长按，触摸，点击*/
    public ViewHolder setOnClickListener(int viewId ,View.OnClickListener listener){
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
    public ViewHolder setOnTouchListener(int viewId,
                                         View.OnTouchListener listener)
    {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId,
                                             View.OnLongClickListener listener)
    {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

































}
