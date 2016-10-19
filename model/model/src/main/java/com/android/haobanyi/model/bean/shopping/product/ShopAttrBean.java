package com.android.haobanyi.model.bean.shopping.product;

/**
 * Created by fWX228941 on 2016/7/15.
 *
 * @作者: 付敏
 * @创建日期：2016/07/15
 * @邮箱：466566941@qq.com
 * @当前文件描述： 其他服务
 *
 * {"ShopAttrID":1,"Name":"其他服务","Price":1}
 * 对于不存在的字段 -1 和 null
 *
 */
public class ShopAttrBean {


    /**
     * ShopAttrID : 1
     * Name : 其他服务
     * Price : 1
     */

    private long ShopAttrID;
    private String AttrName;
    private String Price;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    private boolean isSelected = false ;

    public ShopAttrBean(long shopAttrID, String name, String price) {
        ShopAttrID = shopAttrID;
        AttrName = name;
        Price = price;
    }

    public long getShopAttrID() {
        return ShopAttrID;
    }

    public void setShopAttrID(long ShopAttrID) {
        this.ShopAttrID = ShopAttrID;
    }

    public String getName() {
        return AttrName;
    }

    public void setName(String Name) {
        this.AttrName = Name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }
}
