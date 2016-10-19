package com.android.haobanyi.adapter.charge;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.charge.ChargeBean;
import com.android.haobanyi.model.bean.contact.ContactBean;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/10.
 *
 * @作者: 付敏
 * @创建日期：2016/08/10
 * @邮箱：466566941@qq.com
 * @当前文件描述： 余额和充值明细ChargeBean
 */
public class ChargeListAdapter extends BaseQuickAdapter<ChargeBean> {
    public ChargeListAdapter(List<ChargeBean> data) {
        super(R.layout.item_account_balance, data);// 不要context 了，这样好吗？
    }

    @Override
    protected void convert(BaseViewHolder helper, ChargeBean listBean) {
        helper.setText(R.id.id_chargeway,listBean.getChargeWay())
                .setText(R.id.id_chargeamount, listBean.getChargeAmount())
                .setText(R.id.id_chargedetailid,"充值单号："+listBean.getChargeDetailID())
                .setText(R.id.id_chargetime, listBean.getChargeTime());

    }
}
