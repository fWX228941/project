package com.android.haobanyi.model.bean.shopping.store;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fWX228941 on 2016/7/20.
 *
 * @作者: 付敏
 * @创建日期：2016/07/20
 * @邮箱：466566941@qq.com
 * @当前文件描述： 店铺完整的消息
 */
public class ShopBean {
    /*以GetShop为主，多的字段再来添加*/

    private long ShopID;
    private String ShopName;
    private String ShopLogo;
    private int OrderNum;
    private String MainService;  //主营服务
    private String District;  //地区名称
    private boolean IsFav;
    private String ShopAdd;
    private double ComprehensiveScore;

    //新增三个字段
    private int ProductCount;  //服务数量
    private int VouchersCount; //优惠券数量
    private int ActivityCount; // 活动总数量

    /* ==============GetFavoriteShopList =================*/
    private long FsID;
    /* ==============GetFavoriteShopList =================*/

    public String getMeaasage() {
        return meaasage;
    }

    public void setMeaasage(String meaasage) {
        this.meaasage = meaasage;
    }
    /*果然是考虑不周吧！*/
    /* ==============留言 =================*/
    String meaasage;
    public ShopBean(long shopID, String Meaasage) {
        ShopID = shopID;
        meaasage = Meaasage;
    }
    /* ==============留言 =================*/

    /* ==============SearchShopByKeyword 构造函数=================*/
    public ShopBean(long shopID, String shopName, String shopLogo, int orderNum,String mainService, String district,double comprehensiveScore) {
        ShopID = shopID;
        ShopName = shopName;
        ShopLogo = shopLogo;
        OrderNum = orderNum;
        District = district;
        MainService = mainService;
        ComprehensiveScore = comprehensiveScore;
    }
    /* ==============SearchShopByKeyword 构造函数=================*/


    /* ==============GetFavoriteShopList 构造函数=================*/
    public ShopBean(long fsID,long shopID, String shopName, String shopLogo, int orderNum,String mainService, String district,double comprehensiveScore) {
        FsID = fsID;
        ShopID = shopID;
        ShopName = shopName;
        ShopLogo = shopLogo;
        OrderNum = orderNum;
        District = district;
        MainService = mainService;
        ComprehensiveScore = comprehensiveScore;
    }
    /* ==============GetFavoriteShopList 构造函数=================*/


    public int getActivityCount() {
        return ActivityCount;
    }

    public void setActivityCount(int activityCount) {
        ActivityCount = activityCount;
    }

    public int getProductCount() {
        return ProductCount;
    }

    public void setProductCount(int productCount) {
        ProductCount = productCount;
    }

    public int getVouchersCount() {
        return VouchersCount;
    }

    public void setVouchersCount(int vouchersCount) {
        VouchersCount = vouchersCount;
    }


    /*收藏这个属性*/
    public ShopBean(long shopID, String shopName, String shopLogo, int orderNum, String mainService, String district,
                    boolean isFav, String shopAdd, double comprehensiveScore) {
        ShopID = shopID;
        ShopName = shopName;
        ShopLogo = shopLogo;
        OrderNum = orderNum;
        MainService = mainService;
        District = district;
        IsFav = isFav;
        ShopAdd = shopAdd;
        ComprehensiveScore = comprehensiveScore;
    }


    public long getShopID() {
        return ShopID;
    }

    public void setShopID(long shopID) {
        ShopID = shopID;
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

    public int getOrderNum() {
        return OrderNum;
    }

    public void setOrderNum(int orderNum) {
        OrderNum = orderNum;
    }

    public String getMainService() {
        return MainService;
    }

    public void setMainService(String mainService) {
        MainService = mainService;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public boolean isFav() {
        return IsFav;
    }

    public void setIsFav(boolean isFav) {
        IsFav = isFav;
    }

    public String getShopAdd() {
        return ShopAdd;
    }

    public void setShopAdd(String shopAdd) {
        ShopAdd = shopAdd;
    }

    public double getComprehensiveScore() {
        return ComprehensiveScore;
    }

    public void setComprehensiveScore(double comprehensiveScore) {
        ComprehensiveScore = comprehensiveScore;
    }


    public long getFsID() {
        return FsID;
    }

    public void setFsID(long fsID) {
        FsID = fsID;
    }
}
