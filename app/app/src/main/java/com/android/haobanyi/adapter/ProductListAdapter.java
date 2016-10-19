package com.android.haobanyi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import java.util.List;

/**
 * Created by fWX228941 on 2016/5/24.
 *
 * @作者: 付敏
 * @创建日期：2016/05/24
 * @邮箱：466566941@qq.com
 * @当前文件描述：服务适配器
 *
 */
public class ProductListAdapter extends BaseItemDraggableAdapter<SortDataBean> {

    public ProductListAdapter(Context context, List<SortDataBean> data) {
        super(R.layout.item_normal_product, data);
    }
    public ProductListAdapter(List<SortDataBean> data) {
        super(R.layout.item_normal_product, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SortDataBean listBean) {


        //1.数据都有，为什么映射失败了呢
        helper.setText(R.id.item_product_name, listBean.getProductName())
                .setText(R.id.list_item_sales, "销量" + Integer.toString(listBean.getOrderNum()) + "单")
                .setText(R.id.list_item_price, "￥" + Double.toString(listBean.getMinPrice()));

        //2.这个还要分情况考虑,有的有，有的没有，店铺首页和首页就不同
        if (null == listBean.getProvince() && (null!=listBean.getDistrict())){
            helper.setText(R.id.list_item_district, listBean.getDistrict());
        } else {
            helper.setText(R.id.list_item_district, listBean.getProvince()+listBean.getCity() + listBean.getDistrict());
        }


        SimpleRatingBar ratingBar = helper.getView(R.id.list_item_rate);
        ratingBar.setRating(listBean.getComprehensiveScore());
        /*
        * Set it to true if you want to disable user interaction,Set it to false if you want to allow user interaction (this is the default value).不可交互 thus user interaction will be deactivated
        * */
        ratingBar.setIndicator(true);

        //3.先是把图片缓存去掉，看还会不会有图片加载错乱问题
        Glide.with(mContext)
                .load(listBean.getImagePath())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.service_logo)
                .into((ImageView) helper.getView(R.id.list_item_icon));
    }

    //4.覆写回调方法，这样父类的同名方法就不会被调用了
    @Override
    public void onItemSwiped(final RecyclerView.ViewHolder viewHolder) {

    }



}
