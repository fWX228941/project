package com.android.haobanyi.adapter.redenvelope;


import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.charge.ChargeBean;
import com.android.haobanyi.model.bean.redenvelop.RedEnvelopBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/11.
 *
 * @作者: 付敏
 * @创建日期：2016/08/11
 * @邮箱：466566941@qq.com
 * @当前文件描述：领取红包，所有红包列表适配器
 */
public class AllRedEnvelopeListAdapter extends BaseQuickAdapter<RedEnvelopBean> {
    public AllRedEnvelopeListAdapter(List<RedEnvelopBean> data) {
        super(R.layout.item_fragment_shop_voucher, data);// 不要context 了，这样好吗？
    }

    @Override
    protected void convert(BaseViewHolder helper, RedEnvelopBean listBean) {
        helper.setText(R.id.price, listBean.getRedEnvelopePrice())
        .setText(R.id.orderlimit,"满"+listBean.getOrderLimit()+"使用")
        .setText(R.id.eachlimit,"每人限领"+listBean.getEndTime()+"张")
        .setText(R.id.endtime,"有效期至："+listBean.getEndTime())
                .addOnClickListener(R.id.button1);
         //这里只是展示视图，至于事件不在这里处理
         // 先判断是否过期
         if (listBean.isCanReceive()){
             helper.setVisible(R.id.imageView1,false)
             .setVisible(R.id.imageView2,true)
             .setVisible(R.id.button1,false)
             .setVisible(R.id.button2,false)
             .setVisible(R.id.button3,true);//过期
         } else {

             if (listBean.isReceive()){
                 helper.setVisible(R.id.imageView1,true)
                         .setVisible(R.id.imageView2,false)
                         .setVisible(R.id.button1,true)//没有领取
                         .setVisible(R.id.button2,false)
                         .setVisible(R.id.button3,false);

             } else {
                 //已经领取了
                 helper.setVisible(R.id.imageView1,false)
                         .setVisible(R.id.imageView2,true)
                         .setVisible(R.id.button1,false)
                         .setVisible(R.id.button2,true)
                         .setVisible(R.id.button3,false);
             }


         }


    }


}
