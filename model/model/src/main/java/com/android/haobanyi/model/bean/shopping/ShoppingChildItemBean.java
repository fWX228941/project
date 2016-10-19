package com.android.haobanyi.model.bean.shopping;

/**
 * Created by fWX228941 on 2016/6/6.
 *
 * @作者: 付敏
 * @创建日期：2016/06/06
 * @邮箱：466566941@qq.com
 * @当前文件描述：从获取购物车列表获取的数据过滤出子列表的数据*/


public class ShoppingChildItemBean {
//这个状态暂时不能理解

    public static final  int STATUS_INVALID=0;
    public static final  int STATUS_VALID=1;
    //===============================================
    private String id;
    private String name;
/** 商品宣传图片 */

    private String imageLogo;
//* 商品规格，商品描述

    private String desc;
//* 原价，市场价

    private double price;
//* 现价，折扣价

    private double discountPrice;
//购买数量

    private int count;
//* 状态 ，两种状态之间进行切换，对应的就是视图的切换

    private int status;
//* 是否被选中

    private boolean isChecked;
//* 是否是编辑状态

    private boolean isEditing;
//因为涉及到套餐，所以还需要添加一个tag状态标识量

    private boolean isHasPkgService;//  是否包含套餐，0不是套餐，非0是套餐

    public ShoppingChildItemBean(String id, String name, String imageLogo, String desc, double price, double
            discountPrice, int count, int status, boolean isChecked, boolean isEditing) {
        this.id = id;
        this.name = name;
        this.imageLogo = imageLogo;
        this.desc = desc;
        this.price = price;
        this.discountPrice = discountPrice;
        this.count = count;
        this.status = status;
        this.isChecked = isChecked;
        this.isEditing = isEditing;
    }
//不包含折扣价字段

    public ShoppingChildItemBean(String id, String name, String imageLogo, String desc, double price, int count, int status, boolean isChecked, boolean isEditing) {
        this.id = id;
        this.name = name;
        this.imageLogo = imageLogo;
        this.desc = desc;  //这个其实是服务名
        this.price = price;
        this.count = count;
        this.status = status;
        this.isChecked = isChecked;
        this.isEditing = isEditing;
    }
    public ShoppingChildItemBean(){

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLogo() {
        return imageLogo;
    }

    public void setImageLogo(String imageLogo) {
        this.imageLogo = imageLogo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setIsEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }
}
