package com.android.haobanyi.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.haobanyi.listener.OnItemListener;
import com.android.haobanyi.view.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fWX228941 on 2016/5/6.
 *
 * @作者: 付敏
 * @创建日期：2016/05/06
 * @邮箱：466566941@qq.com
 * @当前文件描述：通用的适配器
 * 参考设计方案：http://blog.csdn.net/lmj623565791/article/details/38902805
 *              https://github.com/canyinghao/CanAdapter  深入添加监听器
 *              http://blog.csdn.net/bboyfeiyu/article/details/48806125
 *              https://github.com/hehonghui/commonadapter
 *
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
    *
    * 5.监听也是分为两种情况的，一种是为整个item注册用户事件，一种是为item的部分子控件注册用户事件
    * */
    protected LayoutInflater mInflater;
    protected List<T> mDatas = new ArrayList<T>();//所有的list都必须要初始化，不然一旦没有数据，就会发生空指针异常
    protected Context mContext;//如果是需要父类的成员变量也能被使用，建议用protect关键字
    protected int mItemLayoutId;// final不能随便定义
    protected OnItemListener mOnItemListener;//添加监听器 ，这里也是提供饿了两种方式
    /*
    * @param context 上下文
    * @datas 数据源
    * @itemlayoutid item布局
    * 采用构建者模式的创建构造函数比较靠谱
    * */
    public CommonAdapter(Context context,List<T> mDatas,int mItemLayoutId){
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.mContext = context;
        this.mItemLayoutId =mItemLayoutId;

    }
    //嵌套构造函数
    public CommonAdapter(Context context,List<T> mDatas){
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.mContext = context;
    }
    //没有数据的构造函数，等数据好了，再来加载视图
    public CommonAdapter(Context context,int mItemLayoutId){
        mInflater = LayoutInflater.from(context);
        this.mItemLayoutId =mItemLayoutId;
        this.mContext = context;
    }

    // 1.获取item数量
    @Override
    public int getCount() {
        return mDatas.size();
    }
    // 2.获取单个item的Bean
    @Override
    public  T  getItem(int position) {
        return mDatas.get(position);
    }
    // 3.获取item id
    @Override
    public long getItemId(int position) {
        return position;
    }
    // 4.获取item视图
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
        * 延伸扩展：视图创建好以后就需要绑定用户各种事件了
        * */
        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, parent, mItemLayoutId,position);//像这种变化不确定的量往往是由子类来具体决定的，既然这样可以作为构造参数传递，因为是必不可少的元素
        //自定义逻辑 ，可以进一步抽象，子类需要处理的工作,负责每个item的绘制，其实还需要添加监听器，
        // 当前的handleWork 其实只是在绘制图而已,为子控件设置监听器
        viewHolder.setOnItemListener(mOnItemListener);
        /*这里还可以细分，分为设置子控件视图和子控件监听器
        * handleWork(viewHolder, getItem(position),position){
        *     protected abstract void setView(ViewHolder helper,  T bean, int position);
              protected abstract void setItemListener(ViewHolder helper);
        * }
        *
        * */
        handleWork(viewHolder, getItem(position),position);

        return viewHolder.getConvertView();

    }
    public abstract void handleWork(ViewHolder holder, T t, int position);

    // 5.自定义方法,就是把list的所有方法封装了一遍,同时Collections中对list方法的操作也是可以封装一遍的，增删改查: 获取所有的数据源,
    //获取数据
    public List<T> getList(){
        return mDatas;
    }
    // 6.追加新的listdata数据到旧的list列表的头部
    //数据添加到头部
    public void appendNewList(List<T> datas){
        if (null != datas && !datas.isEmpty()){
            // list添加单个元素/添加一个集合，如果是两个参的方法，则第一个参数是location位置，依次来指定插入的位置，数据默认是追加到尾部
            mDatas.addAll(0,datas);
            //每次数据源发生变更时，都需要调用如下方法，以通知数据集发生变化的,会强制调用getView来刷新每个item的内容，触发onChanged方法调用
            notifyDataSetChanged();
        }

    }
    //16.更新listView中的数据
    // 设置数据
    public void updateListView(List<T> datas){
        this.mDatas.clear();
        if (datas != null && !datas.isEmpty()) {
            mDatas.addAll(datas);
        }
        //进行一到转型会不会更好
        notifyDataSetChanged();
    }
    // 7.添加新的listdata数据到旧的list列表的尾部
    //  追加数据到尾部
    public void appendMoreList(List<T> datas ){
        if (datas != null && !datas.isEmpty()) {
            mDatas.addAll(this.mDatas.size(),datas);
            notifyDataSetChanged();
        }
    }
    //8.清空数据，添加新的数据
    public void addNewList(List<T> datas){
        mDatas.clear();
        if (datas != null && !datas.isEmpty()) {
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }
    //9.清空数据
    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }
    //10.删除指定索引下的item数据，
    //删除指定索引数据条目
    public void removeItem(int position){
        mDatas.remove(position);
        notifyDataSetChanged();
    }
    //11.删除指定item数据
    public void removeItem(T itemBean){
        mDatas.remove(itemBean);
        notifyDataSetChanged();
    }
    //12.在指定位置添加数据
    public void addItem(int position, T model) {
        mDatas.add(position, model);
        notifyDataSetChanged();
    }
    /**
     * 在集合头部添加数据条目
     *
     * @param model
     */
    public void addFirstItem(T model) {
        addItem(0, model);
    }

    /**
     * 在集合末尾添加数据条目
     *
     * @param model
     */
    public void addLastItem(T model) {
        addItem(mDatas.size(), model);
    }

    //13.修改替换指定索引的数据
    public void modifyItem(int location, T newModel) {
        mDatas.set(location, newModel);
        notifyDataSetChanged();
    }
    //14.修改替换指定数据项
    public void modifyItem(T oldModel, T newModel) {
        modifyItem(mDatas.indexOf(oldModel), newModel);// 当提示参数类型有问题时，需要同时检查方法，也许是方法本身存在问题
    }
    //15.交换两个数据项数据条目的位置
    public void moveItem(int fromPosition, int toPosition) {
        Collections.swap(mDatas, fromPosition, toPosition);
        notifyDataSetChanged();
    }






}































