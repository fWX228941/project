package com.android.haobanyi.adapter.history;

import android.util.Log;
import android.widget.ImageView;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.foot.FootBean;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.redenvelop.RedEnvelopBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/12.
 *
 * @作者: 付敏
 * @创建日期：2016/08/12
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class MyFootHistoryAdapter  extends BaseQuickAdapter<FootBean> {
    public MyFootHistoryAdapter(List<FootBean> data) {
        super(R.layout.item_normal_product, data);// 不要context 了，这样好吗？
    }
    @Override
    protected void convert(BaseViewHolder helper, FootBean listBean) {
        helper.setText(R.id.item_product_name, listBean.getProductName())
                .setVisible(R.id.list_item_sales, false)
                .setText(R.id.list_item_price, "￥" + listBean.getMinPrice())
                .setVisible(R.id.list_item_district, false)
                .setVisible(R.id.list_item_rate, false);

        Glide.with(mContext)
                .load(listBean.getImagePath())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.service_logo)// 也就是网络加载失败时出现的图片
                .into((ImageView) helper.getView(R.id.list_item_icon));
    }



}
