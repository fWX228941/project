package com.android.haobanyi.adapter.shoppingevaluation;

import android.content.Context;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.home.searching.SortDataBean;
import com.android.haobanyi.model.bean.shopping.product.EvaBean;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/7/6.
 *
 * @作者: 付敏
 * @创建日期：2016/07/06
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class ShoppingEvaluationAdapter extends BaseItemDraggableAdapter<EvaBean> {
    public ShoppingEvaluationAdapter(Context context, List<EvaBean> data) {
        super(R.layout.item_list_shopping_evaluate, data);
    }
    public ShoppingEvaluationAdapter( List<EvaBean> data) {
        super(R.layout.item_list_shopping_evaluate, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, EvaBean evaBean) {
        helper.setText(R.id.UserName,"用户："+evaBean.getUserName())
        .setText(R.id.UserViews,evaBean.getUserViews())
        .setText(R.id.UserAssessmentDate,evaBean.getUserAssessmentDate());
        if (evaBean.ishasReply()){
            helper.setVisible(R.id.has_reply,true);
            helper.setText(R.id.TraderName,"用户："+evaBean.getTraderName())
            .setText(R.id.TraderViews,evaBean.getTraderViews())
            .setText(R.id.TraderAssessmentDate, evaBean.getTraderAssessmentDate());
        } else {
          helper.setVisible(R.id.has_reply,false);
        }

    }
}
