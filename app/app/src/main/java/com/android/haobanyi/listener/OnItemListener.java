package com.android.haobanyi.listener;

import android.view.View;
import android.widget.CompoundButton;

/**
 * Created by fWX228941 on 2016/5/31.
 *
 * @作者: 付敏
 * @创建日期：2016/05/31
 * @邮箱：466566941@qq.com
 * @当前文件描述：用于定义设置commonAdapter item中子控件的事件监听器，和item的各种事件监听器，用于包装具体实现
 */
public class OnItemListener {
    /*
    * 三种常见事件，分别是：点击，长按，状态选择
    *   01.positon:代表item的位置
    *   02.view 代表item的子控件
    * */
    public void onItemChildClick(View view ,int positon){}
    public boolean onItemChildLongClick(View view , int position){return false;}
    public void onItemChildCheckedChanged(CompoundButton view ,int position,boolean isChecked){};

    /*当前item的事件*/
    public void onRecyclerViewItemClick(View view, int positon){}
    public boolean onRecyclerViewItemLongClick(View view, int position){return false;}
}
