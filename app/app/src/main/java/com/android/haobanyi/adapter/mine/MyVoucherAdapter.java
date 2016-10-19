package com.android.haobanyi.adapter.mine;

import android.content.Context;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.voucher.VoucherBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/11.
 *
 * @作者: 付敏
 * @创建日期：2016/08/11
 * @邮箱：466566941@qq.com
 * @当前文件描述：我的代金券适配器
 *  不实现左右删除
 */
public class MyVoucherAdapter extends BaseQuickAdapter<VoucherBean>{
    public MyVoucherAdapter(List<VoucherBean> data) {
        super(R.layout.item_my_voucher, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VoucherBean listBean) {
        helper.setText(R.id.shop_name,listBean.getShopName())
        .setText(R.id.end_time,"有效期至："+listBean.getEndTime())
        .setText(R.id.tv_price,listBean.getPrice())
        .setText(R.id.tv_limit,"满"+listBean.getLimit()+"使用");
        switch (listBean.getStatus()){
            case 1://未使用
                helper.setVisible(R.id.imageView1,true)
                .setVisible(R.id.imageView2,false)
                .setVisible(R.id.imageView3,false);
                break;
            case 2: //已使用
                helper.setVisible(R.id.imageView1,false)
                .setVisible(R.id.imageView2, true)
                .setVisible(R.id.imageView3, true);
                break;
            case 3://已过期
                helper.setVisible(R.id.imageView1,false)
                .setVisible(R.id.imageView2,true)
                .setVisible(R.id.imageView3,false);
                break;
        }


    }
}
