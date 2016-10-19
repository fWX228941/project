package com.android.haobanyi.model.bean.shopping.conformorder;

/**
 * Created by fWX228941 on 2016/6/28.
 *
 * @作者: 付敏
 * @创建日期：2016/06/28
 * @邮箱：466566941@qq.com
 * @当前文件描述：ShoppingCartBean 分离出来  关联界面 “确认订单” 关联类：ConformOrderActivity
 */
public class ShoppingCartParentBean {
   /*=================getShoppingCart==========================*/
    private long id;//店铺的唯一标识
    private String name; // 店铺名称
    private String ShopLogo; // 店铺小图标，这个imageView 一定要限制宽高，不能保证服务器所给的图片尺寸是符合要求的，等比例缩放
    private String SingleShopTotalCount;//单个店所有服务的数量bug:就是这个值与网站上的不一致
    private String SingleShopTotalPrice; //单个店所有服务的合计价格
    private boolean isChecked = false;  // 店铺的默认的未勾选状态
    private boolean isEditing = false;//默认都是未编辑，未选择
    /*=================getShoppingCart==========================*/

    /*=================getShoppingCart==========================*/
    public ShoppingCartParentBean(long shopID, String shopName, String shopLogo, String orderTotalPrice,String productCount,boolean isChecked, boolean isEditing) {
        id = shopID;
        name = shopName;
        ShopLogo = shopLogo;
        SingleShopTotalPrice = orderTotalPrice;
        SingleShopTotalCount = productCount;
        this.isChecked = isChecked;
        this.isEditing = isEditing;

    }
    /*=================getShoppingCart==========================*/


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSingleShopTotalCount() {
        return SingleShopTotalCount;
    }

    public void setSingleShopTotalCount(String singleShopTotalCount) {
        SingleShopTotalCount = singleShopTotalCount;
    }

    public String getSingleShopTotalPrice() {
        return SingleShopTotalPrice;
    }

    public void setSingleShopTotalPrice(String singleShopTotalPrice) {
        SingleShopTotalPrice = singleShopTotalPrice;
    }

    public ShoppingCartParentBean(Long shopID, String shopName, String shopLogo, double orderTotalPrice, int
            productCount) {
        id = shopID;
        name = shopName;
        ShopLogo = shopLogo;
/*        OrderTotalPrice = orderTotalPrice;
        ProductCount = productCount;*/
    }

    public ShoppingCartParentBean(Long shopID, String shopName, String shopLogo, double orderTotalPrice) {
        id = shopID;
        name = shopName;
        ShopLogo = shopLogo;
        /*OrderTotalPrice = orderTotalPrice;*/

    }

    public ShoppingCartParentBean(Long shopID, String shopName, String shopLogo, String orderTotalPrice,boolean isChecked, boolean isEditing) {
        id = shopID;
        name = shopName;
        ShopLogo = shopLogo;
        /*OrderTotalPrice = orderTotalPrice;*/
        this.isChecked = isChecked;
        this.isEditing = isEditing;

    }

    public ShoppingCartParentBean(Long shopID, String shopName, String shopLogo) {
        id = shopID;
        name = shopName;
        ShopLogo = shopLogo;
    }
    public boolean isEditing() {
        return isEditing;
    }

    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }


    public Long getShopID() {
        return id;
    }

    public void setShopID(Long shopID) {
        id = shopID;
    }

    public String getShopName() {
        return name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setShopName(String shopName) {
        name = shopName;
    }

    public String getShopLogo() {
        return ShopLogo;
    }

    public void setShopLogo(String shopLogo) {
        ShopLogo = shopLogo;
    }
    @Override
    public String toString() {
        return "ShopID = "+id+"; ShopName = "+name+"; ShopLogo = "+ShopLogo;
    }
}
