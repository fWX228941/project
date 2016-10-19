package com.android.haobanyi.model.bean.home.searching;

/**
 * Created by fWX228941 on 2016/6/23.
 *
 * @作者: 付敏
 * @创建日期：2016/06/23
 * @邮箱：466566941@qq.com
 * @当前文件描述：获取销量排序的服务列表
 * 其实和SortDataBean 一样。
 * 问题： 后期优化，合并为一个类，用key来标识
 *
 */
public class HotSortDataBean {
    private int ProductID;
    private String ProductName;
    private double MinPrice;
    private String ImagePath;
    private int OrderNum;
    private int IsHot;
    private String ShopName;
    private String Province;
    private String City;
    private String District;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    //添加一个positon 字段用于判断是在哪一个item中
    private int position;
    public HotSortDataBean(String City,String District,String ImagePath,String ProductName,int OrderNum,double MinPrice){
        this.City=City;
        this.District = District;
        this.ImagePath = ImagePath;
        this.ProductName = ProductName;
        this.OrderNum = OrderNum;
        this.MinPrice = MinPrice;

    }
    public HotSortDataBean(String City,String District,String ImagePath,String ProductName,int OrderNum,double MinPrice,int position){
        this.City=City;
        this.District = District;
        this.ImagePath = ImagePath;
        this.ProductName = ProductName;
        this.OrderNum = OrderNum;
        this.MinPrice = MinPrice;
        this.position = position;
    }
    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
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

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
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
