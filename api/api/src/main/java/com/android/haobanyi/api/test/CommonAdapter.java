package com.android.haobanyi.api.test;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by fWX228941 on 2016/5/6.
 *
 * @作者: 付敏
 * @创建日期：2016/05/06
 * @邮箱：466566941@qq.com
 * @当前文件描述：通用的适配器
 */
//只要有一个方法是抽象方法，则类也要声明为抽象类
public abstract class CommonAdapter<T> extends BaseAdapter {
    /*
    * 1.设计思想：不同的listView/gridView 需要不同的实体对象model/bean/entity ，而视图对象是存储在一个list列表中
    * 存放不同类型的实体对象，如何让listView支持不同类型的对象呢！这个时候就需要一个泛型，不止是针对基本类型，也是针对
    * 对象类型
    *
    * 2.编程步骤：需要在构造函数中就把数据打包传递过来,一涉及到泛型，那么只能是在类声明时，自定义，子类来具体填充数据,这样类中才能使用，作为参数，类型和返回类型
    *
    * 3.子类的工作：子类只需要继承，并且自己实现getView，然后在getView中使用ViewHolder，可以进一步优化
    *
    * 4.两个变化的部分，一个是数据，一个是布局ID 都作为参数传到布局参数中
    * */
    protected LayoutInflater mInflater;
    protected List<T> mDatas;
    protected Context mContext;//如果是需要父类的成员变量也能被使用，建议用protect关键字
    protected int mItemLayoutId;// final不能随便定义
    public CommonAdapter(Context context, List<T> mDatas, int mItemLayoutId){
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.mContext = context;
        this.mItemLayoutId =mItemLayoutId;

    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public  T  getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /*前三项都是针对数据的，所以论数据项，也就是实体集合的重要性*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        * 1.原则：把变化的部分抽取出来，让具体的子类来实现，而把不变的部分写在父类中
        * 2.两个地方是不变的，
        *   一个是获取ViewHolder 实例，除了一个变化的参数就是layoutId
        *   一个是最后返回的ConvertView 不变
        * 3.变化：获取具体的控件以及对控件的具体操作
        *
        *
        * */
        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, parent, mItemLayoutId,position);//像这种变化不确定的量往往是由子类来具体决定的，既然这样可以作为构造参数传递，因为是必不可少的元素
        /*自定义逻辑 ，可以进一步抽象

        *更多demo ：http://blog.csdn.net/lmj623565791/article/details/38902805
        *  子类需要处理的工作
        * */
        handleWork(viewHolder, getItem(position));

        return viewHolder.getConvertView();

    }
    public abstract void handleWork(ViewHolder holder, T t);

}































