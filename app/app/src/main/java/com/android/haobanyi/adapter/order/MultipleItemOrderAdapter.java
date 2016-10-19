package com.android.haobanyi.adapter.order;

import android.util.Log;
import android.widget.ImageView;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.order.OrderChildBean;
import com.android.haobanyi.model.bean.order.OrderParentBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/16.
 *
 * @作者: 付敏
 * @创建日期：2016/08/16
 * @邮箱：466566941@qq.com
 * @当前文件描述：改为分组吧，真是sheet ，测试一下也是没有问题的，
 * 订单列表适配器
 */
public class MultipleItemOrderAdapter extends BaseSectionQuickAdapter<OrderParentBean> {
    public MultipleItemOrderAdapter( int layoutResId, int sectionHeadResId, List data) {
        super( layoutResId, sectionHeadResId, data);
    }

    public MultipleItemOrderAdapter(List data) {
        super( R.layout.activity_order_content,  R.layout.activity_order_header, data);
    }
    /*为子item设置监听器*/
    @Override
    protected void convertHead(BaseViewHolder helper, OrderParentBean item) {
        Glide.with(mContext)
                .load(item.getShopLogo())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.shop_icon)
                .into((ImageView) helper.getView(R.id.list_item_icon));
        helper.setText(R.id.tv_title_parent,item.getShopName());
        switch (item.getStatus()){
            case 1://待付款
                helper.setText(R.id.id_tv_status,"待买家付款");
                break;
            case 2://待办理
                helper.setText(R.id.id_tv_status,"买家已付款");
                break;
            case 3://办理中
                helper.setText(R.id.id_tv_status,"买家已付款");
                break;
            case 4://待确认
                helper.setText(R.id.id_tv_status,"待买家确认");
                break;
            case 5://已完成
                helper.setText(R.id.id_tv_status,"交易成功");
                break;
            case 6://待退款
                helper.setText(R.id.id_tv_status,"待审核");
                break;
            case 7://已退款
                helper.setText(R.id.id_tv_status,"交易完成");
                break;
            case 8://无效订单
                helper.setVisible(R.id.id_tv_status, false);
                break;

        }
        //为这个item设置监听器

    }

    @Override
    protected void convert(BaseViewHolder helper, OrderParentBean item) {
        OrderChildBean childBean =  (OrderChildBean)item.t;
        Glide.with(mContext)
                .load(childBean.getProductImage())
                .centerCrop()
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) helper.getView(R.id.id_iv_logo));
        helper.setText(R.id.productname,childBean.getProductName())
        .setText(R.id.id_tv_discount_price,"￥"+childBean.getProductPrice())
        .setText(R.id.id_tv_count,String.format(mContext.getString(R.string.product_count), childBean.getQuantity()))
        .setText(R.id.id_tv_content,String.format(mContext.getString(R.string.order_content_formate), Integer.toString(childBean.getCount()),childBean.getPrice()))
        .addOnClickListener(R.id.tv_get_vr)
        .addOnClickListener(R.id.evaluation)
        .addOnClickListener(R.id.tv_get_vt);
        //order_content_formate
        Log.d("ces", "item.getCount():" + item.getCount());//难道需要把这个值过寄给
        Log.d("ces","item.getPrice():" + item.getPrice());
        if (childBean.isLastChild()){
            helper.setVisible(R.id.footer,true);
        }else {
            helper.setVisible(R.id.footer,false);
        }
        //看来子的状态也是需要传递一份的,子视图，只能使用子视图的数据，父视图也只能使用父视图的数据
        switch (childBean.getStatus()){
            case 1://待付款
                helper.setVisible(R.id.evaluation,false)
                        .setText(R.id.tv_get_vr,"取消订单")
                .setText(R.id.tv_get_vt,"去支付");
                break;
            case 2://待办理
                helper.setVisible(R.id.evaluation,false)
                        .setVisible(R.id.tv_get_vr, false)
                        .setText(R.id.tv_get_vt, "申请退款");
                break;
            case 3://办理中
                helper.setVisible(R.id.evaluation,false)
                        .setVisible(R.id.tv_get_vr,false)
                        .setText(R.id.tv_get_vt, "申请退款");
                break;
            case 4://待确认
                helper.setVisible(R.id.evaluation,false)
                        .setText(R.id.tv_get_vr,"申请退款")
                        .setText(R.id.tv_get_vt, "确认订单");
                break;
            case 5://已完成
                helper.setVisible(R.id.evaluation,true)
                        .setText(R.id.tv_get_vr,"关闭订单")
                        .setText(R.id.tv_get_vt, "申请退款");
                break;
            case 6://待退款
                helper.setVisible(R.id.evaluation,false)
                        .setVisible(R.id.tv_get_vr,false)
                        .setVisible(R.id.tv_get_vt, false);//统统隐藏
                break;
            case 7://已退款
                helper.setVisible(R.id.evaluation,false)
                        .setVisible(R.id.tv_get_vr,false)
                        .setVisible(R.id.tv_get_vt, false);
                break;
            case 8://无效订单
                helper.setVisible(R.id.evaluation,false)
                        .setVisible(R.id.tv_get_vr,false)
                        .setVisible(R.id.tv_get_vt, false);
                break;

        }

    }
}
