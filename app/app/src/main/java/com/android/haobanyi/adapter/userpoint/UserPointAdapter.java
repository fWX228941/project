package com.android.haobanyi.adapter.userpoint;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.userpoint.UserPointBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/11.
 *
 * @作者: 付敏
 * @创建日期：2016/08/11
 * @邮箱：466566941@qq.com
 * @当前文件描述：用户积分
 */
public class UserPointAdapter extends BaseQuickAdapter<UserPointBean> {
    public UserPointAdapter(List<UserPointBean> data) {
        super(R.layout.item_user_point, data);// 不要context 了，这样好吗？
    }

    @Override
    protected void convert(BaseViewHolder helper, UserPointBean listBean) {
        helper.setText(R.id.typedesc,listBean.getTypeDesc())
                .setText(R.id.createdate, listBean.getCreateDate())
                .setText(R.id.integral,"+"+listBean.getIntegral());

    }
}
