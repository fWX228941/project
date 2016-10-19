package com.android.haobanyi.adapter.shop;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.shopping.store._ShopRelatedServiceBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/26.
 *
 * @作者: 付敏
 * @创建日期：2016/07/26
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class StoreServiceListAdapter extends BaseQuickAdapter<_ShopRelatedServiceBean> {
    private static final String TAG = "oh";
    public StoreServiceListAdapter(Context context, List<_ShopRelatedServiceBean> data) {
        super(R.layout.item_store_service, data);//难道是布局需要换下item_store_service 你是怎么了？
    }
    public StoreServiceListAdapter(List<_ShopRelatedServiceBean> data) {
        super(R.layout.item_store_service, data);//难道是布局需要换下item_store_service 你是怎么了？
    }


    @Override
    protected void convert(BaseViewHolder helper, _ShopRelatedServiceBean listBean) {
/*        Log.d(TAG, listBean.getCity());
        Log.d(TAG, listBean.getProductName());
        Log.d(TAG, Integer.toString(listBean.getOrderNum()));
        Log.d(TAG, Double.toString(listBean.getMinPrice()));
        Log.d(TAG, listBean.getDistrict());
        Log.d(TAG, "helper.getView(R.id.item_product_name):" + helper.getView(R.id.item_product_name_01));
        Log.d(TAG, "helper.getView(R.id.list_item_price):" + helper.getView(R.id.list_item_price_01));
        Log.d(TAG, "helper.getView(R.id.list_item_district)==null:" + (helper.getView(R.id.list_item_district_01) == null));*/
        /*数据都有，为什么映射失败了呢*/
        if (null!=listBean.getProductName()){
            helper.setText(R.id.item_product_name_01, listBean.getProductName());

        }

        if (null!= Integer.toString(listBean.getOrderNum())){
            helper.setText(R.id.list_item_sales_01, "销量" + Integer.toString(listBean.getOrderNum()) + "单");

        }
        if (null!=Double.toString(listBean.getMinPrice())){
            helper.setText(R.id.list_item_price_01, "￥" + Double.toString(listBean.getMinPrice()));

        }

        helper.setText(R.id.list_item_district_01, listBean.getProvince()+listBean.getCity() + listBean.getDistrict());
/*        .setOnClickListener(R.id.item_product_name, new
                OnItemChildClickListener()).setRating(R.id.list_item_rate_01, 50)



        先是把图片缓存去掉，看还会不会有图片加载错乱问题*/

        SimpleRatingBar ratingBar = (SimpleRatingBar)helper.getView(R.id.list_item_rate);
//        ratingBar.setRating(listBean.get());
        /*
        * Set it to true if you want to disable user interaction
Set it to false if you want to allow user interaction (this is the default value).
        *
        * */
        ratingBar.setIndicator(true);//不可交互 thus user interaction will be deactivated.

        Glide.with(mContext)
                .load(listBean.getImagePath())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.service_logo)// 也就是网络加载失败时出现的图片
                .into((ImageView) helper.getView(R.id.list_item_icon_01));
    }




}
