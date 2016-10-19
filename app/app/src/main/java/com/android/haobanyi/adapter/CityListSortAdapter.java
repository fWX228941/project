package com.android.haobanyi.adapter;

import android.content.Context;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.home.location.CitySortBean;
import com.android.haobanyi.view.ViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/5/19.
 *
 * @作者: 付敏
 * @创建日期：2016/05/19
 * @邮箱：466566941@qq.com
 * @当前文件描述：带拼音的城市列表适配器，铺展item视图  【重构后纯在一些问题，但是我实在不知道是什么原因】取代方案CityListSortAdapter01
 * 使用重构方案出现了几个问题：
 *
 *  1.一个是我向下滑动时，出现问题  显然是配置出现了问题  适配器出现了问题，决定重新来写一遍
    2.输入城市名出现问题
    3.一个是点击热门标签时出现问题
 */
public class CityListSortAdapter extends CommonAdapter<CitySortBean> implements SectionIndexer {
    @BindView(R.id.catalog)
    TextView Letter;   //看来butterk的使用条件非常严苛,这样用还不行
    @BindView(R.id.title)
    TextView title;
    public CityListSortAdapter(Context context, List<CitySortBean> mDatas, int mItemLayoutId) {
        super(context, mDatas, mItemLayoutId);
    }
    public CityListSortAdapter(Context context, List<CitySortBean> mDatas) {
        super(context, mDatas);
    }
    @Override
    public void handleWork(ViewHolder holder, CitySortBean citySortBean, int position) {
        //01.获取分类的首字母的char ascil值
        int section = getSectionForPosition(position);//这个要改
        //02.如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
            holder.setVisible(R.id.catalog,true);
            holder.setText(R.id.title,citySortBean.getSortLetters());
        }else{
            holder.setVisible(R.id.catalog, false);
        }
        holder.setText(R.id.title, citySortBean.getName());//title.setText(citySortBean.getName());

    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置,定制化方法就交给自己来处理
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mDatas.get(i).getSortLetters(); // 这里是否需要自己定义一个list有待商榷
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
         return mDatas.get(position).getSortLetters().charAt(0);
    }
    //16.更新listView中的数据
    public void updateListView(List<CitySortBean> list){ // 因为涉及到具体转型了，所以覆盖下
        this.mDatas = list;
        notifyDataSetChanged();
    }

}
