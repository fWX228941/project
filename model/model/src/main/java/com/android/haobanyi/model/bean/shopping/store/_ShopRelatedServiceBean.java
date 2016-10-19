package com.android.haobanyi.model.bean.shopping.store;

/**
 * Created by fWX228941 on 2016/7/8.
 *
 * @作者: 付敏
 * @创建日期：2016/07/08
 * @邮箱：466566941@qq.com
 * @当前文件描述：脱胎于ShopRelatedServiceBean
 */
public class _ShopRelatedServiceBean {


    private long shopId;
    private long ProductID;
    private String ProductName;
    private double MinPrice;
    private String ImagePath;
    private int OrderNum;
    private int IsHot;
    private String Province;
    private String City;
    private String District;


    public _ShopRelatedServiceBean(long shopId,long productID, String productName, double minPrice, String imagePath, int
            orderNum, int isHot, String province, String city, String district) {
        this.shopId =shopId;
        ProductID = productID;
        ProductName = productName;
        MinPrice = minPrice;
        ImagePath = imagePath;
        OrderNum = orderNum;
        IsHot = isHot;
        Province = province;
        City = city;
        District = district;
    }
    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }
    public long getProductID() {
        return ProductID;
    }

    public void setProductID(long productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public double getMinPrice() {
        return MinPrice;
    }

    public void setMinPrice(double minPrice) {
        MinPrice = minPrice;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public int getOrderNum() {
        return OrderNum;
    }

    public void setOrderNum(int orderNum) {
        OrderNum = orderNum;
    }

    public int getIsHot() {
        return IsHot;
    }

    public void setIsHot(int isHot) {
        IsHot = isHot;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }
}
