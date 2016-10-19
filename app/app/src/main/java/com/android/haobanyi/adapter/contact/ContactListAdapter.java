package com.android.haobanyi.adapter.contact;


import android.util.Log;
import android.view.View;

import com.android.haobanyi.R;
import com.android.haobanyi.model.bean.contact.ContactBean;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/10.
 *
 * @作者: 付敏
 * @创建日期：2016/08/10
 * @邮箱：466566941@qq.com
 * @当前文件描述：联系人列表适配器
 */
public class ContactListAdapter  extends BaseItemDraggableAdapter<ContactBean> {
    boolean isHint = false;
    public ContactListAdapter(List<ContactBean> data) {
        super(R.layout.item_my_contact, data);// 不要context 了，这样好吗？
    }

    public ContactListAdapter(List<ContactBean> data,boolean isHint) {
        super(R.layout.item_my_contact, data);// 不要context 了，这样好吗？
        this.isHint = isHint;
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactBean listBean) {
        helper.setText(R.id.id_contactname,"联系人："+listBean.getContactName())
        .setText(R.id.id_contactmobile, listBean.getContactMobile())
        .setText(R.id.id_contactaddress,"地址："+listBean.getProvinceName()+listBean.getCityName()+listBean.getDistrictName()+listBean.getContactAddress())
        .setChecked(R.id.all_radio_button, listBean.IsDefault())
        .addOnClickListener(R.id.id_edit_)
        .addOnClickListener(R.id.id_delete_)
        .addOnClickListener(R.id.all_radio_button);
        /*
        * https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/添加Item事件
        * 子控件失效问题，真是被坑死了。所给的demo 都是有问题的
        *
        * */
        if (listBean.IsDefault()){
            helper.getView(R.id.all_radio_button).setClickable(false);//设置为不可点击,感觉视乎是产生了冲突
        }
        Log.d(TAG, "isHint:" + isHint);
        if (isHint){
            helper.getView(R.id.hint_contact).setVisibility(View.GONE);
        }else {
            helper.setVisible(R.id.hint_contact,true);
        }

    }


}
