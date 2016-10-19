package com.android.haobanyi.adapter.product;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.shopping.product.SatisfySendBean;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/18.
 *
 * @作者: 付敏
 * @创建日期：2016/07/18
 * @邮箱：466566941@qq.com
 * @当前文件描述：单个的bean
 */
public class ProductSatisfySendAdapter extends CommonAdapter<SatisfySendBean> {
    public ProductSatisfySendAdapter(Context context, int layoutId, List<SatisfySendBean> datas) {
        super(context, layoutId, datas);
    }
    public ProductSatisfySendAdapter(Context context, List<SatisfySendBean> datas) {
        super(context, R.layout.item_fragment_product_satisfysend, datas);
    }
    @Override
    protected void convert(ViewHolder viewHolder, SatisfySendBean item, int position) {
        viewHolder.setText(R.id.tv_price,item.getPrice());
        viewHolder.setText(R.id.tv_money,item.getMoney());
        /*这个用来点击单个子item*/
/*        viewHolder.setOnClickListener(R.id.item_satisfy_send, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "我被点击了", Toast.LENGTH_SHORT).show();
                //点击后赋值，并且调用接口，如果是购物的话

            }
        });*/

    }
}
