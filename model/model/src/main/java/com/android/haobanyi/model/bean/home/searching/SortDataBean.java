package com.android.haobanyi.model.bean.home.searching;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fWX228941 on 2016/5/27.
 *
 * @作者: 付敏
 * @创建日期：2016/05/27
 * @邮箱：466566941@qq.com
 * @当前文件描述：综合排序排序的服务列表
 * 每个字段都会很有作用的，如果纯在共同的字段，尽管添加，因为是同一个productID 是不会冲突的
 * 先细分
 */
public class SortDataBean implements Parcelable {
    private long ProductID;
    private String ProductName;
    private double MinPrice;
    private String ImagePath;
    private int OrderNum;
    private int IsHot;
    private String ShopName;  // 这个字段，可要可不要
    private String Province;
    private String City;
    private String District;

    //新增字段 由GetProduct提供,不一定需要
    private double DiscountPrice;
    private Object MobileDes;  // 这个不确定是什么
    private int IsRecommend;
    private boolean IsFav;  // true 收藏，false 没有收藏
    private long ShopID;   //有些默认为0的情况
    private String ShopLogo;
    private String ShopAdd;
    private float ComprehensiveScore;  // 评分如果没有则默认就是0.0




    /* ==============GetFavoriteProductList =================*/
    private long FpID;
    //01.第一版本
    public SortDataBean(long fpID, long productID, String productName, String imagePath, double minPrice, int isHot,
                        int isRecommend) {
        FpID = fpID;
        ProductID = productID;
        ProductName = productName;
        ImagePath = imagePath;
        MinPrice = minPrice;
        IsHot = isHot;
        IsRecommend = isRecommend;
    }
    //02.第一版本  补充三个字段
    public SortDataBean(long fpID, long productID, String productName, String imagePath, double minPrice, int isHot,
                        int isRecommend,int orderNum, String city, String district) {
        FpID = fpID;
        ProductID = productID;
        ProductName = productName;
        ImagePath = imagePath;
        MinPrice = minPrice;
        IsHot = isHot;
        IsRecommend = isRecommend;
        City = city;
        District = district;
        OrderNum = orderNum;
    }
    /* ==============getFavoriteProductList =================*/
    public SortDataBean(long fpID, long productID, String productName, String imagePath, double minPrice, int isHot,
                        int isRecommend,int orderNum, String city, String district,String province) {
        FpID = fpID;
        ProductID = productID;
        ProductName = productName;
        ImagePath = imagePath;
        MinPrice = minPrice;
        IsHot = isHot;
        Province = province;
        IsRecommend = isRecommend;
        City = city;
        District = district;
        OrderNum = orderNum;
    }
    /* ==============getFavoriteProductList =================*/

    public long getFpID() {
        return FpID;
    }
    public void setFpID(long fpID) {
        FpID = fpID;
    }
    /* ==============GetFavoriteProductList =================*/


    /*总共18个字段，复杂度不低*/
    public SortDataBean(long productID, String productName, double minPrice, String imagePath, int orderNum, int
            isHot, String shopName, String province, String city, String district, double discountPrice, Object
            mobileDes, int isRecommend, boolean isFav, long shopID, String shopLogo, String shopAdd, float
            comprehensiveScore) {
        ProductID = productID;
        ProductName = productName;
        MinPrice = minPrice;
        ImagePath = imagePath;
        OrderNum = orderNum;
        IsHot = isHot;
        ShopName = shopName;
        Province = province;
        City = city;
        District = district;
        DiscountPrice = discountPrice;
        MobileDes = mobileDes;
        IsRecommend = isRecommend;
        IsFav = isFav;
        ShopID = shopID;
        ShopLogo = shopLogo;
        ShopAdd = shopAdd;
        ComprehensiveScore = comprehensiveScore;
    }
    /*包含10个字段，主要用于GetShopProductListByHotSort   GetShopProductListByNormalSort 店铺相关的服务列表 */
    public SortDataBean(long shopID,long productID, String productName, double minPrice, String imagePath, int orderNum, int
            isHot, String province, String city, String district) {
        ShopID = shopID;
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
    /*包含9个字段，主要用于GetShopProductListByHotSort   GetShopProductListByNormalSort 店铺相关的服务列表*/
    public SortDataBean(long productID, String productName, double minPrice, String imagePath, int orderNum, int
            isHot, String province, String city, String district) {
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
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    //添加一个positon 字段用于判断是在哪一个item中
    private int position;
    public SortDataBean(String City,String District,String ImagePath,String ProductName,int OrderNum,double MinPrice){
        this.City=City;
        this.District = District;
        this.ImagePath = ImagePath;
        this.ProductName = ProductName;
        this.OrderNum = OrderNum;
        this.MinPrice = MinPrice;

    }



    public SortDataBean(long ProductID,String City,String District,String ImagePath,String ProductName,int OrderNum,double MinPrice ,float
            comprehensiveScore){
        this.ProductID = ProductID;
        this.City=City;
        this.District = District;
        this.ImagePath = ImagePath;
        this.ProductName = ProductName;
        this.OrderNum = OrderNum;
        this.MinPrice = MinPrice;
        this.ComprehensiveScore = comprehensiveScore;
    }


    /*================getProductListByNormalSort===========================*/
    public SortDataBean(long ProductID,String City,String District,String ImagePath,String ProductName,int OrderNum,double MinPrice ,float
            comprehensiveScore,String province){
        Province = province;
        this.ProductID = ProductID;
        this.City=City;
        this.District = District;
        this.ImagePath = ImagePath;
        this.ProductName = ProductName;
        this.OrderNum = OrderNum;
        this.MinPrice = MinPrice;
        this.ComprehensiveScore = comprehensiveScore;
    }
    /*================getProductListByNormalSort===========================*/

    public SortDataBean(long ProductID,String City,String District,String ImagePath,String ProductName,int OrderNum,double MinPrice){
        this.ProductID = ProductID;
        this.City=City;
        this.District = District;
        this.ImagePath = ImagePath;
        this.ProductName = ProductName;
        this.OrderNum = OrderNum;
        this.MinPrice = MinPrice;

    }
    public SortDataBean(String City,String District,String ImagePath,String ProductName,int OrderNum,double MinPrice,int position){
        this.City=City;
        this.District = District;
        this.ImagePath = ImagePath;
        this.ProductName = ProductName;
        this.OrderNum = OrderNum;
        this.MinPrice = MinPrice;
        this.position = position;
    }
    public SortDataBean(long ProductID,String City,String District,String ImagePath,String ProductName,int OrderNum,double MinPrice,int position){
        this.ProductID = ProductID;
        this.City=City;
        this.District = District;
        this.ImagePath = ImagePath;
        this.ProductName = ProductName;
        this.OrderNum = OrderNum;
        this.MinPrice = MinPrice;
        this.position = position;
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

    public double getDiscountPrice() {
        return DiscountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        DiscountPrice = discountPrice;
    }

    public Object getMobileDes() {
        return MobileDes;
    }

    public void setMobileDes(Object mobileDes) {
        MobileDes = mobileDes;
    }

    public int getIsRecommend() {
        return IsRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        IsRecommend = isRecommend;
    }

    public boolean isFav() {
        return IsFav;
    }

    public void setIsFav(boolean isFav) {
        IsFav = isFav;
    }

    public long getShopID() {
        return ShopID;
    }

    public void setShopID(long shopID) {
        ShopID = shopID;
    }

    public String getShopLogo() {
        return ShopLogo;
    }

    public void setShopLogo(String shopLogo) {
        ShopLogo = shopLogo;
    }

    public String getShopAdd() {
        return ShopAdd;
    }

    public void setShopAdd(String shopAdd) {
        ShopAdd = shopAdd;
    }

    public float getComprehensiveScore() {
        return ComprehensiveScore;
    }

    public void setComprehensiveScore(float comprehensiveScore) {
        ComprehensiveScore = comprehensiveScore;
    }
    /*自定义格式化规则*/

/*    @Override
    public String toString() {
        *//*搞一个简单的规则会更好*//*
        return getPosition()+getImagePath()+getProductName()+getOrderNum()+getMinPrice()+getCity()+getDistrict();
    }*/


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.ProductID);
        dest.writeString(this.ProductName);
        dest.writeDouble(this.MinPrice);
        dest.writeString(this.ImagePath);
        dest.writeInt(this.OrderNum);
        dest.writeInt(this.IsHot);
        dest.writeString(this.ShopName);
        dest.writeString(this.Province);
        dest.writeString(this.City);
        dest.writeString(this.District);
        dest.writeDouble(this.DiscountPrice);
        dest.writeInt(this.IsRecommend);
        dest.writeByte(IsFav ? (byte) 1 : (byte) 0);
        dest.writeLong(this.ShopID);
        dest.writeString(this.ShopLogo);
        dest.writeString(this.ShopAdd);
        dest.writeFloat(this.ComprehensiveScore);
        dest.writeLong(this.FpID);
        dest.writeInt(this.position);
    }

    protected SortDataBean(Parcel in) {
        this.ProductID = in.readLong();
        this.ProductName = in.readString();
        this.MinPrice = in.readDouble();
        this.ImagePath = in.readString();
        this.OrderNum = in.readInt();
        this.IsHot = in.readInt();
        this.ShopName = in.readString();
        this.Province = in.readString();
        this.City = in.readString();
        this.District = in.readString();
        this.DiscountPrice = in.readDouble();
        this.MobileDes = in.readParcelable(Object.class.getClassLoader());
        this.IsRecommend = in.readInt();
        this.IsFav = in.readByte() != 0;
        this.ShopID = in.readLong();
        this.ShopLogo = in.readString();
        this.ShopAdd = in.readString();
        this.ComprehensiveScore = in.readFloat();
        this.FpID = in.readLong();
        this.position = in.readInt();
    }

    public static final Parcelable.Creator<SortDataBean> CREATOR = new Parcelable.Creator<SortDataBean>() {
        @Override
        public SortDataBean createFromParcel(Parcel source) {
            return new SortDataBean(source);
        }

        @Override
        public SortDataBean[] newArray(int size) {
            return new SortDataBean[size];
        }
    };
}
