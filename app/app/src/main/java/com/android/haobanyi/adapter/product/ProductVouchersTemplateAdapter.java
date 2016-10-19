package com.android.haobanyi.adapter.product;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.shopping.product.VouchersTemplateBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/19.
 *
 * @作者: 付敏
 * @创建日期：2016/07/19
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class ProductVouchersTemplateAdapter extends CommonAdapter<VouchersTemplateBean> {
    private Context context ;
    public ProductVouchersTemplateAdapter(Context context, int layoutId, List<VouchersTemplateBean> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }
    public ProductVouchersTemplateAdapter(Context context, List<VouchersTemplateBean> datas) {
        super(context, R.layout.item_fragment_product_voucherstemplate, datas);
        this.context = context;
    }
    /*
    * 这里用户行为约定：
    * */
    @Override
    protected void convert(ViewHolder viewHolder, VouchersTemplateBean item, int position) {
        viewHolder.setText(R.id.tv_price_vt,String.format(context.getString(R.string.price_vt),item.getPrice()));
        viewHolder.setText(R.id.tv_limit,String.format(context.getString(R.string.limit),item.getLimit()));
        viewHolder.setText(R.id.tv_time_range,String.format(context.getString(R.string.limit_time),item.getStartTime(),item.getEndTime()));
        /*这里需要写一个监听器，是否领取了红包，因为状态是需要改变的*/
        TextView tv = (TextView)viewHolder.getView(R.id.tv_get_vt);
        if (item.isIsExist()){
            viewHolder.setText(R.id.tv_get_vt, "已领取");
            viewHolder.setTextColor(R.id.tv_get_vt, R.color.grey_c0);

            tv.setClickable(false);
        } else {
            viewHolder.setText(R.id.tv_get_vt,"领取");
            tv.setClickable(true);
        }
/*
        viewHolder.setOnClickListener(R.id.tv_get_vt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });*/
    }
}
