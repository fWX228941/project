package com.android.haobanyi.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
/*
* 说明：
* 配合ExpandableAdapter 和 ExpandableAdapterHelper 来作用于ExpandableListView的适配器
*
* 参开设计：https://github.com/ThePacific/adapter
* http://www.jianshu.com/p/f18f77255952
*
* 用法：adapter 也是需要按照功能来分类的
* adapter = new ExpandableAdapter<MenuBean, ExploreBean>(context, R.layout.item_group, R.layout.item_child) {
            @Override
            protected List<ExploreBean> getChildren(int groupPosition) {
                return get(groupPosition).getExploreBeanList();
            }

            @Override
            protected void convertGroupView(final boolean isExpanded, final ExpandableAdapterHelper helper, MenuBean item) {
                helper.setImageResource(R.id.img_explore_icon, item.getIconResId())
                        .setText(R.id.tv_explore_name, item.getDescription())
                        .getItemView().setTag("Example");
            }

            @Override
            protected void convertChildView(boolean isLastChild, final ExpandableAdapterHelper helper, ExploreBean item) {
                helper.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickSnack(helper.getGroupPosition(), helper.getChildPosition());
                    }
                });
                helper.getItemView().setTag("hello world");
            }
        };
*
*
* */
public abstract class ExpandableAdapter<T, V> extends BaseExpandableAdapter<T, V, ExpandableAdapterHelper> {

    public ExpandableAdapter(Context context, @LayoutRes int groupLayoutResId, @LayoutRes int childLayoutResId) {
        super(context, groupLayoutResId, childLayoutResId);
    }

    public ExpandableAdapter(Context context, @LayoutRes int groupLayoutResId, @LayoutRes int childLayoutResId, @Nullable List<T> data) {
        super(context, groupLayoutResId, childLayoutResId, data);
    }

    @Override
    protected ExpandableAdapterHelper getAdapterHelper(int groupPosition, int childPosition, View convertView, ViewGroup parent) {
        if (childPosition == -1) {
            return ExpandableAdapterHelper.get(context, convertView, parent, groupLayoutResId, groupPosition, childPosition);
        }
        return ExpandableAdapterHelper.get(context, convertView, parent, childLayoutResId, groupPosition, childPosition);
    }
}
