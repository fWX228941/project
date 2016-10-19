package com.android.haobanyi.adapter.product;

import android.content.Context;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.shopping.product.ShopAttrBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/19.
 *
 * @作者: 付敏
 * @创建日期：2016/07/19
 * @邮箱：466566941@qq.com
 * @当前文件描述：服务详情中的
 */
public class ProductShopAttrAdapter extends CommonAdapter<ShopAttrBean> {
    public ProductShopAttrAdapter(Context context, int layoutId, List<ShopAttrBean> datas) {
        super(context, layoutId, datas);
    }
    public ProductShopAttrAdapter(Context context, List<ShopAttrBean> datas) {
        super(context, R.layout.item_fragment_product_shopattr, datas);
    }

    @Override
    protected void convert(ViewHolder help, ShopAttrBean item, int position) {
        help.setText(R.id.id_item_tv_tag,item.getName());
        if (item.isSelected()){
            help.setBackgroundRes(R.id.id_item_tv_tag,R.drawable.bg_btn_normal);
        } else {
            help.setBackgroundRes(R.id.id_item_tv_tag,R.drawable.bg_btn_normal_01);
        }


    }
}
