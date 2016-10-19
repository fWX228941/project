package com.android.haobanyi.model.bean.shopping.conformorder;

/**
 * Created by fWX228941 on 2016/8/2.
 *
 * @作者: 付敏
 * @创建日期：2016/08/02
 * @邮箱：466566941@qq.com
 * @当前文件描述：平台红包，接口：GetVerifyShoppingCart
 */
public class RedEnvelopeBean {
    private long RedEnvelopeID;
    private String Price;
    private boolean IsSelected;

    public RedEnvelopeBean(long redEnvelopeID, String price, boolean isSelected) {
        RedEnvelopeID = redEnvelopeID;
        Price = price;
        IsSelected = isSelected;
    }


    public long getRedEnvelopeID() {
        return RedEnvelopeID;
    }

    public void setRedEnvelopeID(long redEnvelopeID) {
        RedEnvelopeID = redEnvelopeID;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public boolean isSelected() {
        return IsSelected;
    }

    public void setIsSelected(boolean isSelected) {
        IsSelected = isSelected;
    }
}
