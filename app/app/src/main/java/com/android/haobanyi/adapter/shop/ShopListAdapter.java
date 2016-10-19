package com.android.haobanyi.adapter.shop;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.shopping.store.ShopBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/6.
 *
 * @作者: 付敏
 * @创建日期：2016/08/06
 * @邮箱：466566941@qq.com
 * @当前文件描述：item_normal_shop
 */
public class ShopListAdapter extends BaseItemDraggableAdapter<ShopBean> {
    public ShopListAdapter(Context context, List<ShopBean> data) {
        super(R.layout.item_normal_shop, data);
    }
    public ShopListAdapter( List<ShopBean> data) {
        super(R.layout.item_normal_shop, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, ShopBean listBean) {
        helper.setText(R.id.item_product_name, listBean.getShopName())
                .setText(R.id.id_mainservice_, listBean.getMainService())
                .setText(R.id.list_item_district, listBean.getDistrict());//double 转化为string类型
        if (Double.toString(listBean.getComprehensiveScore())==null){
            helper.setText(R.id.comprehensivescore, "暂时还没有服务介绍");
        }else {
            helper.setText(R.id.comprehensivescore, Double.toString(listBean.getComprehensiveScore()));
        }
        Glide.with(mContext)
                .load(listBean.getShopLogo())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.service_logo)// 也就是网络加载失败时出现的图片
                .into((ImageView) helper.getView(R.id.list_item_icon));
    }
    // 这个一定要覆写，不然会出现下标越界问题，这个就是删除的两个步骤
    @Override
    public void onItemSwiped(final RecyclerView.ViewHolder viewHolder) {

    }
}
