package com.android.haobanyi.model.bean.order;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * Created by fWX228941 on 2016/8/16.
 *
 * @作者: 付敏
 * @创建日期：2016/08/16
 * @邮箱：466566941@qq.com
 * @当前文件描述：  先用于测试，多item的，如果不行就换section的
 * OrderChildBean
 */
public class OrderParentBean extends SectionEntity<OrderChildBean> {
    private long OrderID;// 订单ID
    private long UserID;// 用户ID
    private long ShopID;// 店铺ID
    private String Price;// 订单价格
    private int Status;// 订单状态
    private String ShopName;
    private String ShopLogo;


    private int Count;
    /*到时候再来一一一一添加就是了*/
    public OrderParentBean(boolean isHeader,long orderID, long userID, long shopID, String price, int status, String shopName, String
            shopLogo,int count) {
        super(isHeader, shopName);
        OrderID = orderID;
        UserID = userID;
        ShopID = shopID;
        Price = price;
        Status = status;
        ShopName = shopName;
        ShopLogo = shopLogo;
        Count = count;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public OrderParentBean(OrderChildBean t) {
        super(t);
    }
    public long getOrderID() {
        return OrderID;
    }

    public void setOrderID(long orderID) {
        OrderID = orderID;
    }

    public long getUserID() {
        return UserID;
    }

    public void setUserID(long userID) {
        UserID = userID;
    }

    public long getShopID() {
        return ShopID;
    }

    public void setShopID(long shopID) {
        ShopID = shopID;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getShopLogo() {
        return ShopLogo;
    }

    public void setShopLogo(String shopLogo) {
        ShopLogo = shopLogo;
    }





}
