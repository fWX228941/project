package com.android.haobanyi.model.bean.voucher;

/**
 * Created by fWX228941 on 2016/7/21.
 *
 * @作者: 付敏
 * @创建日期：2016/07/21
 * @邮箱：466566941@qq.com
 * @当前文件描述：代金券
 */
public class VoucherBean {
    /*===================GetShopVouchersList====================*/
    private long VouchersTemplateID;
    private String Title;
    private String VouchersDes;
    private String StartTime;
    private String Image;
    /*新增两个字段*/
    private boolean IsExist;
    private int EachLimit;
    /*新增连个字段*/
    /*===================GetShopVouchersList====================*/

    /*===========================公共字段=======================*/
    private String Price;
    private String EndTime;
    private String Limit;
    /*============================公共字段======================*/



    /*===================GetVoucherList========================*/
    private long VouchersID;
    private int Status;
    private String ShopName;

    public VoucherBean(long vouchersID, String limit, String price, String endTime, int status, String shopName) {
        VouchersID = vouchersID;
        Limit = limit;
        Price = price;
        EndTime = endTime;
        Status = status;
        ShopName = shopName;
    }
    /*==================GetVoucherList=========================*/


    /*===================GetShopVouchersList===================*/
    public VoucherBean(long vouchersTemplateID, String title, String vouchersDes, String startTime, String endTime,
                       String price, String image,boolean isExist,int eachLimit) {
        VouchersTemplateID = vouchersTemplateID;
        Title = title;
        VouchersDes = vouchersDes;
        StartTime = startTime;
        EndTime = endTime;
        Price = price;
        Image = image;
        IsExist = isExist;
        EachLimit = eachLimit;
    }
    /*===================GetShopVouchersList====================*/


    public int getEachLimit() {
        return EachLimit;
    }

    public void setEachLimit(int eachLimit) {
        EachLimit = eachLimit;
    }

    public boolean isExist() {
        return IsExist;
    }

    public void setExist(boolean exist) {
        IsExist = exist;
    }
    public String getLimit() {
        return Limit;
    }

    public void setLimit(String limit) {
        Limit = limit;
    }

    public long getVouchersID() {
        return VouchersID;
    }

    public void setVouchersID(long vouchersID) {
        VouchersID = vouchersID;
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

    public long getVouchersTemplateID() {
        return VouchersTemplateID;
    }

    public void setVouchersTemplateID(long VouchersTemplateID) {
        this.VouchersTemplateID = VouchersTemplateID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getVouchersDes() {
        return VouchersDes;
    }

    public void setVouchersDes(String VouchersDes) {
        this.VouchersDes = VouchersDes;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }
}
