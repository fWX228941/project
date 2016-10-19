package com.android.haobanyi.adapter.shopping;

import android.content.Context;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.shopping.conformorder.ShoppingCartChildBean;
import com.android.haobanyi.model.bean.shopping.product.ShopAttrBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/29.
 *
 * @作者: 付敏
 * @创建日期：2016/08/29
 * @邮箱：466566941@qq.com
 * @当前文件描述：购物车中的其他服务
 */
public class ShoppingAttrAdapter extends CommonAdapter<ShoppingCartChildBean.ShopAttrListBean> {
    public ShoppingAttrAdapter(Context context, List<ShoppingCartChildBean.ShopAttrListBean> datas) {
        super(context, R.layout.item_fragment_product_shopattr, datas);
    }
    @Override
    protected void convert(ViewHolder help, ShoppingCartChildBean.ShopAttrListBean item, int position) {
        help.setText(R.id.id_item_tv_tag,item.getAttrName());
        if (item.isSelected()){
            help.setBackgroundRes(R.id.id_item_tv_tag,R.drawable.bg_btn_normal);
        } else {
            help.setBackgroundRes(R.id.id_item_tv_tag,R.drawable.bg_btn_normal_01);
        }
    }
}
