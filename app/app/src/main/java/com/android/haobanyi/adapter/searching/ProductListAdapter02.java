package com.android.haobanyi.adapter.searching;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.haobanyi.R;
import com.android.haobanyi.adapter.CommonAdapter;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.view.ViewHolder;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;

/**
 * Created by fWX228941 on 2016/6/14.
 *
 * @作者: 付敏
 * @创建日期：2016/06/14
 * @邮箱：466566941@qq.com
 * @当前文件描述：第一版留个纪念
 */

public class ProductListAdapter02  extends CommonAdapter<SortDataBean> {
    @BindView(R.id.list_item_icon)
    ImageView listItemIcon;
    @BindView(R.id.item_product_name)
    TextView itemProductName;
    @BindView(R.id.list_item_rate)
    RatingBar listItemRate;
    @BindView(R.id.list_item_sales)
    TextView listItemBrows;
    @BindView(R.id.li_text01)
    LinearLayout liText01;
    @BindView(R.id.list_item_price)
    TextView listItemPrice;
    @BindView(R.id.list_item_district)
    TextView listItemBrowser;
    @BindView(R.id.li_text02)
    RelativeLayout liText02;
    private String productname;
    private String ordernum;
    private String minprice;
    private String location;
    private String pictureUrl;
    private String  URL= "http://192.168.0.110:8089/";

    public ProductListAdapter02(Context context, List<SortDataBean> mDatas, int mItemLayoutId) {
        super(context, mDatas, mItemLayoutId);
    }
    public ProductListAdapter02(Context context, int mItemLayoutId) {
        super(context, mItemLayoutId);
    }

    @Override
    public void handleWork(ViewHolder holder, SortDataBean listBean, int position) {
        Log.d("ProductListAdapter", "listBean.getPosition():" + listBean.getPosition());



        pictureUrl = listBean.getImagePath();
        Log.d("ProductListAdapter", pictureUrl);
        listItemIcon = holder.getView(R.id.list_item_icon);
        //在这里加载图片资源
        Glide.with(mContext)
                .load(URL + pictureUrl)
                .centerCrop()
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)// 也就是网络加载失败时出现的图片
                .into(listItemIcon);

        // bug：对于数据最好进行一层判断
        productname = listBean.getProductName();
        Log.d("ProductListAdapter", productname);
        if (null != productname) {
//01.产品名

            holder.setText(R.id.item_product_name, productname);
        } else {
            holder.setText(R.id.item_product_name, "出错了");
        }

//02.好评率，这个到时需要修改

        holder.setRating(R.id.list_item_rate, 50);
        ordernum = Integer.toString(listBean.getOrderNum());
        Log.d("ProductListAdapter", ordernum);
//03.销量,一定要进行类型转化

        if (null != productname) {
//01.产品名

            holder.setText(R.id.list_item_sales, "销量" + ordernum + "单");

        } else {
            holder.setText(R.id.list_item_sales, "出错了");
        }

        minprice = Double.toString(listBean.getMinPrice());
        Log.d("ProductListAdapter", minprice);
//04.价格

        if (null != productname) {
//01.产品名

            holder.setText(R.id.list_item_price, "￥" + minprice);

        } else {
            holder.setText(R.id.list_item_price, "出错了");
        }

        location = listBean.getCity() + listBean.getDistrict();
        Log.d("ProductListAdapter", location);
//05.地理坐标

        if (null != productname) {
//01.产品名

            holder.setText(R.id.list_item_district, location);

        } else {
            holder.setText(R.id.list_item_district, "出错了");
        }


    }
}
