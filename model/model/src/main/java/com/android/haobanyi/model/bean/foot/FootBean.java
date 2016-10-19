package com.android.haobanyi.model.bean.foot;

/**
 * Created by fWX228941 on 2016/8/12.
 *
 * @作者: 付敏
 * @创建日期：2016/08/12
 * @邮箱：466566941@qq.com
 * @当前文件描述：足迹
 */
public class FootBean {
    private long ProductID;
    private String ProductName;
    private String MinPrice;
    private String ImagePath;

    public FootBean(long productID, String productName, String minPrice, String imagePath) {
        ProductID = productID;
        ProductName = productName;
        MinPrice = minPrice;
        ImagePath = imagePath;
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

    public String getMinPrice() {
        return MinPrice;
    }

    public void setMinPrice(String minPrice) {
        MinPrice = minPrice;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
