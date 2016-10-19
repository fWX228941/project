package com.android.haobanyi.model.bean.shopping.product;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fWX228941 on 2016/7/6.
 *
 * @作者: 付敏
 * @创建日期：2016/07/06
 * @邮箱：466566941@qq.com
 * @当前文件描述：从DetailsBean中过滤过来的数据
 * 可以暂时先废弃  被SortDataBean所取代
 *
 * 新增字段："WebDes":"<p style=\"font-size:14px;vertical-align:baseline;color:#666666;font-family:'Microsoft YaHei', verdana, Arial;background:#FFFFFF;\">\r\n\t服务内容：1. 办理三证合一的营业执照（备注：三证合一是指营业执照，组织机构代码证，税务登记证）\r\n</p>\r\n<p style=\"font-size:14px;vertical-align:baseline;color:#666666;font-family:'Microsoft YaHei', verdana, Arial;background:#FFFFFF;\">\r\n\t&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;2. 刻章工本费实报，（公章、财务章、私章）&nbsp;\r\n</p>\r\n<p style=\"font-size:14px;vertical-align:baseline;color:#666666;font-family:'Microsoft YaHei', verdana, Arial;background:#FFFFFF;\">\r\n\t&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;3. 免费税务筹划及税务咨询。\r\n</p>\r\n<p style=\"font-size:14px;vertical-align:baseline;font-family:'Microsoft YaHei', verdana, Arial;background:#FFFFFF;\">\r\n\t<span style=\"color:#666666;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;4. 指导填开发票。<br />\r\n<br />\r\n服务地区：<span style=\"color:#666666;font-family:'Microsoft YaHei', verdana, Arial;font-size:14px;line-height:normal;background-color:#FFFFFF;\">深圳</span><br />\r\n<br />\r\n服务时间：<span style=\"color:#666666;font-family:'Microsoft YaHei', verdana, Arial;font-size:14px;line-height:normal;background-color:#FFFFFF;\">资料收集齐全提交后，3-7个工作日左右办结公司整套资料；<br />\r\n</span><br />\r\n需准备材料： 1.&nbsp;</span><span class=\"spans\" style=\"color:#666666;font-size:14px;vertical-align:baseline;line-height:30px;font-family:'Microsoft YaHei', verdana, Arial;background-color:#FFFFFF;\">公司名称（准备两三个备用）；2.&nbsp;</span><span class=\"spans\" style=\"color:#666666;font-size:14px;vertical-align:baseline;line-height:30px;font-family:'Microsoft YaHei', verdana, Arial;background-color:#FFFFFF;\">注册资本；3.&nbsp;</span><span class=\"spans\" style=\"color:#666666;font-size:14px;vertical-align:baseline;line-height:30px;font-family:'Microsoft YaHei', verdana, Arial;background-color:#FFFFFF;\">经营范围；4.&nbsp;</span><span class=\"spans\" style=\"color:#666666;font-size:14px;vertical-align:baseline;line-height:30px;font-family:'Microsoft YaHei', verdana, Arial;background-color:#FFFFFF;\">股东投资比例；5.&nbsp;</span><span class=\"spans\" style=\"color:#666666;font-size:14px;vertical-align:baseline;line-height:30px;font-family:'Microsoft YaHei', verdana, Arial;background-color:#FFFFFF;\">法人、股东、监事身份证 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;正面照片；6.&nbsp;</span><span class=\"spans\" style=\"color:#666666;font-size:14px;vertical-align:baseline;line-height:30px;font-family:'Microsoft YaHei', verdana, Arial;background-color:#FFFFFF;\">经营地址；7.&nbsp;</span><span class=\"spans\" style=\"color:#666666;font-size:14px;vertical-align:baseline;line-height:30px;font-family:'Microsoft YaHei', verdana, Arial;background-color:#FFFFFF;\">法人、股东、监事网银U盾或个人数字证书 （备注：银行为中行、工行、建行、 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;农行）；</span><br />\r\n<br />\r\n<span><span style=\"color:#666666;font-family:'Microsoft YaHei', verdana, Arial;font-size:14px;line-height:21px;background-color:#FFFFFF;\">服务标准</span>：&nbsp;<span style=\"color:#666666;font-family:'Microsoft YaHei', verdana, Arial;font-size:14px;line-height:21px;background-color:#FFFFFF;\">1.&nbsp;</span></span><span class=\"spans\" style=\"color:#666666;font-size:14px;vertical-align:b
        解析不了
 *
 *
 */
public class ProductDetailsBean implements Parcelable {
    private long ProductID;
    private String ProductName;
    private String ImagePath;
    private double MinPrice;
    private double DiscountPrice;
    private int OrderNum;
    private long ShopID;
    private String ShopName;
    private String ShopLogo;
    private String ShopAdd;

    public String getWebDes() {
        return WebDes;
    }

    public void setWebDes(String webDes) {
        WebDes = webDes;
    }

    private String WebDes;

    public double getComprehensiveScore() {
        return ComprehensiveScore;
    }

    public void setComprehensiveScore(double comprehensiveScore) {
        ComprehensiveScore = comprehensiveScore;
    }

    public static Creator<ProductDetailsBean> getCREATOR() {
        return CREATOR;
    }

    private double ComprehensiveScore;  //这个是干嘛用的

    public ProductDetailsBean(long shopID, long productID, String productName, String imagePath, double minPrice, double
            discountPrice, int orderNum, String shopName, String shopLogo, String shopAdd) {
        ShopID = shopID;
        ProductID = productID;
        ProductName = productName;
        ImagePath = imagePath;
        MinPrice = minPrice;
        DiscountPrice = discountPrice;
        OrderNum = orderNum;
        ShopName = shopName;
        ShopLogo = shopLogo;
        ShopAdd = shopAdd;
    }
    /*新增加了一个ComprehensiveScore评分字段*/
    public ProductDetailsBean(long shopID, long productID, String productName, String imagePath, double minPrice, double
            discountPrice, int orderNum, String shopName, String shopLogo, String shopAdd,double ComprehensiveScore) {
        ShopID = shopID;
        ProductID = productID;
        ProductName = productName;
        ImagePath = imagePath;
        MinPrice = minPrice;
        DiscountPrice = discountPrice;
        OrderNum = orderNum;
        ShopName = shopName;
        ShopLogo = shopLogo;
        ShopAdd = shopAdd;
        this.ComprehensiveScore = ComprehensiveScore;
    }
    public ProductDetailsBean(long shopID, long productID, String productName, String imagePath, double minPrice, double
            discountPrice, int orderNum, String shopName, String shopLogo, String shopAdd,double ComprehensiveScore,String WebDes) {
        ShopID = shopID;
        ProductID = productID;
        ProductName = productName;
        ImagePath = imagePath;
        MinPrice = minPrice;
        DiscountPrice = discountPrice;
        OrderNum = orderNum;
        ShopName = shopName;
        ShopLogo = shopLogo;
        ShopAdd = shopAdd;
        this.ComprehensiveScore = ComprehensiveScore;
        this.WebDes = WebDes;
    }

    public String getShareUrl() {
        return ShareUrl;
    }

    public void setShareUrl(String shareUrl) {
        ShareUrl = shareUrl;
    }

    public boolean isFav() {
        return IsFav;
    }

    public void setIsFav(boolean isFav) {
        IsFav = isFav;
    }
    /*========================getProduct01================================*/
    private String ShareUrl;//分享url”,
    private boolean IsFav;
    public ProductDetailsBean(long shopID, long productID, String productName, String imagePath, double minPrice, double
            discountPrice, int orderNum, String shopName, String shopLogo, String shopAdd,double ComprehensiveScore,String WebDes,String shareUrl,boolean isFav) {
        ShopID = shopID;
        ProductID = productID;
        ProductName = productName;
        ImagePath = imagePath;
        MinPrice = minPrice;
        DiscountPrice = discountPrice;
        OrderNum = orderNum;
        ShopName = shopName;
        ShopLogo = shopLogo;
        ShopAdd = shopAdd;
        this.ComprehensiveScore = ComprehensiveScore;
        this.WebDes = WebDes;
        this.ShareUrl = shareUrl;
        IsFav =isFav;
    }

    /*========================getProduct01================================*/

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

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public double getMinPrice() {
        return MinPrice;
    }

    public void setMinPrice(double minPrice) {
        MinPrice = minPrice;
    }

    public double getDiscountPrice() {
        return DiscountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        DiscountPrice = discountPrice;
    }

    public int getOrderNum() {
        return OrderNum;
    }

    public void setOrderNum(int orderNum) {
        OrderNum = orderNum;
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

    public String getShopAdd() {
        return ShopAdd;
    }

    public void setShopAdd(String shopAdd) {
        ShopAdd = shopAdd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.ProductID);
        dest.writeString(this.ProductName);
        dest.writeString(this.ImagePath);
        dest.writeDouble(this.MinPrice);
        dest.writeDouble(this.DiscountPrice);
        dest.writeInt(this.OrderNum);
        dest.writeLong(this.ShopID);
        dest.writeString(this.ShopName);
        dest.writeString(this.ShopLogo);
        dest.writeString(this.ShopAdd);
    }

    protected ProductDetailsBean(Parcel in) {
        this.ProductID = in.readLong();
        this.ProductName = in.readString();
        this.ImagePath = in.readString();
        this.MinPrice = in.readDouble();
        this.DiscountPrice = in.readDouble();
        this.OrderNum = in.readInt();
        this.ShopID = in.readLong();
        this.ShopName = in.readString();
        this.ShopLogo = in.readString();
        this.ShopAdd = in.readString();
    }

    public static final Parcelable.Creator<ProductDetailsBean> CREATOR = new Parcelable.Creator<ProductDetailsBean>() {
        @Override
        public ProductDetailsBean createFromParcel(Parcel source) {
            return new ProductDetailsBean(source);
        }

        @Override
        public ProductDetailsBean[] newArray(int size) {
            return new ProductDetailsBean[size];
        }
    };
}
