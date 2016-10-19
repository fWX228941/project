package com.android.haobanyi.adapter.shop;

import android.content.Context;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.voucher.VoucherBean;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/21.
 *
 * @作者: 付敏
 * @创建日期：2016/07/21
 * @邮箱：466566941@qq.com
 * @当前文件描述：店铺代缴券和我的代金券还不一样
 */
public class ShopVoucherAdapter  extends BaseItemDraggableAdapter<VoucherBean> {
    public ShopVoucherAdapter(Context context,List<VoucherBean> data) {
        super(R.layout.item_fragment_shop_voucher, data);
    }
    public ShopVoucherAdapter(List<VoucherBean> data) {
        super(R.layout.item_fragment_shop_voucher, data);
    }


    @Override
    protected void convert(BaseViewHolder help, VoucherBean voucherBean) {
        /*  {"code":101,"message":"获取成功！","data":[{"VouchersTemplateID":20065,"StartTime":"2016-09-14 15:27:31","EndTime":"2016-10-14 15:27:31","Price":10.00,"Limit":20.00,"IsExist":false,"EachLimit":2}]}
*/
        /*把这个逻辑添加上吧，TMD 太丑陋了，真心丑陋啊*/
        //eachlimit
        help.setText(R.id.price, voucherBean.getPrice())
        .setText(R.id.orderlimit, "满"+voucherBean.getLimit()+"￥使用")
        .setText(R.id.eachlimit, "每人限领"+Integer.toString(voucherBean.getEachLimit())+"张")
        .setText(R.id.endtime,String.format(mContext.getString(R.string.endTime_content_formate), voucherBean.getStartTime(),voucherBean.getEndTime()))
        ;
        if (voucherBean.isExist()){
            //已经领取
            help. setVisible(R.id.button1,false)
                    .setVisible(R.id.button2,true)
                    .setVisible(R.id.button3,false)
            .setVisible(R.id.imageView1,false)
            .setVisible(R.id.imageView2,true);
        }else {
            //暂时没有领取
            help. setVisible(R.id.button1,true)//没有领取
                    .setVisible(R.id.button2,false)
                    .setVisible(R.id.button3,false)
            .setVisible(R.id.imageView1,true)
            .setVisible(R.id.imageView2,false);
            help.addOnClickListener(R.id.button1);
        }




    }
}
