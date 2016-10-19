package com.android.haobanyi.adapter.mine;

import android.content.Context;
import android.os.Message;
import android.widget.ImageView;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.mine.MyMessageBean;
import com.android.haobanyi.model.bean.voucher.VoucherBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/6/20.
 *
 * @作者: 付敏
 * @创建日期：2016/06/20
 * @邮箱：466566941@qq.com
 * @当前文件描述：消息界面
 */
public class MyMessageAdapter extends BaseQuickAdapter<MyMessageBean> {


    public MyMessageAdapter(List<MyMessageBean> data) {
        super(R.layout.item_my_message, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, MyMessageBean listBean) {
        helper.setText(R.id.id_txt_message, listBean.getMessageTypeDesc())
                .setText(R.id.id_txt_time, listBean.getCreateDate())
                .setText(R.id.id_txt_description, listBean.getMessageContext());

        /*先是把图片缓存去掉，看还会不会有图片加载错乱问题*/
 /*       Glide.with(mContext)
                .load(listBean.getImagePath())
                .centerCrop()
                .crossFade()
                .placeholder(R.drawable.news_icon1)// 也就是网络加载失败时出现的图片
                .into((ImageView) helper.getView(R.id.list_item_icon));*/

    }
}
