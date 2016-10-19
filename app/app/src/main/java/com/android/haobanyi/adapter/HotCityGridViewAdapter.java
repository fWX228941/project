package com.android.haobanyi.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.haobanyi.R;
import com.android.haobanyi.listener.OnItemListener;
import com.android.haobanyi.model.bean.home.location.CityBean;
import com.android.haobanyi.view.ViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/5/19.
 *
 * @作者: 付敏
 * @创建日期：2016/05/19
 * @邮箱：466566941@qq.com
 * @当前文件描述：热门城市，铺展item视图
 */
public class HotCityGridViewAdapter extends CommonAdapter<CityBean> {
    @BindView(R.id.id_item_tv_tag)
    TextView idItemTvHotcity;  //那这个其实可以删除了
    /*1.activity才是正在的客户端，才会提供数据源和itemLayout*/
    public HotCityGridViewAdapter(Context context, List<CityBean> mDatas, int mItemLayoutId) {
        super(context, mDatas, mItemLayoutId);
    }

    @Override
    public void handleWork(ViewHolder holder, CityBean cityBean, int position) {
        holder.setText(R.id.id_item_tv_tag, cityBean.getName());
    }
}
