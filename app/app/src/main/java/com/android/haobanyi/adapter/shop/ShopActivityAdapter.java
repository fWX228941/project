package com.android.haobanyi.adapter.shop;

import android.content.Context;
import android.view.View;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.shopping.store.ShopAcBean;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/21.
 *
 * @作者: 付敏
 * @创建日期：2016/07/21
 * @邮箱：466566941@qq.com
 * @当前文件描述：店铺活动
 */
public class ShopActivityAdapter extends BaseItemDraggableAdapter<ShopAcBean> {
    public ShopActivityAdapter(Context context,List<ShopAcBean> data) {
        super(R.layout.item_fragment_shop_activity, data);
    }
    public ShopActivityAdapter(List<ShopAcBean> data) {
        super(R.layout.item_fragment_shop_activity, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, ShopAcBean shopAcBean) {
        helper.setText(R.id.title_tv,shopAcBean.getTitle())
        .setText(R.id.content_tv,shopAcBean.getContent())
        .setText(R.id.start_time,shopAcBean.getStartTime()+"-")
        .setText(R.id.end_time,shopAcBean.getEndTime());

    }
}
