package com.android.haobanyi.model.bean.shopping.product;

/**
 * Created by fWX228941 on 2016/7/15.
 *
 * @作者: 付敏
 * @创建日期：2016/07/15
 * @邮箱：466566941@qq.com
 * @当前文件描述：服务优惠券
 */
public class VouchersTemplateBean {
    /**
     "VTList":[{"VouchersTemplateID":55,"Price":10.00,"StartTime":"2016-06-22 18:10:28","EndTime":"2016-07-22 00:00:00","Limit":100.00,"IsExist":false,"EachLimit":1},
     {"VouchersTemplateID":10052,"Price":20.00,"StartTime":"2016-07-06 10:16:58","EndTime":"2016-07-21 00:00:00","Limit":-10.00,"IsExist":false,"EachLimit":2},
     {"VouchersTemplateID":10053,"Price":20.00,"StartTime":"2016-07-06 10:17:23","EndTime":"2016-07-21 00:00:00","Limit":30.00,"IsExist":false,"EachLimit":2}]
     */

    private long VouchersTemplateID;
    private String Price;
    private String StartTime;
    private String EndTime;
    private String Limit;// 订单限额
    private boolean IsExist;// true已领取，false 可领取

    public VouchersTemplateBean(long vouchersTemplateID, String price, String startTime, String endTime, String
            limit, boolean isExist, int eachLimit) {
        VouchersTemplateID = vouchersTemplateID;
        Price = price;
        StartTime = startTime;
        EndTime = endTime;
        Limit = limit;
        IsExist = isExist;
        EachLimit = eachLimit;
    }

    public int getEachLimit() {
        return EachLimit;
    }

    public void setEachLimit(int eachLimit) {
        EachLimit = eachLimit;
    }

    private int EachLimit;// 0 不限制，每人限领次数   这个字段用法

    public VouchersTemplateBean(long vouchersTemplateID, String price, String startTime, String endTime, String
            limit, boolean isExist) {
        VouchersTemplateID = vouchersTemplateID;
        Price = price;
        StartTime = startTime;
        EndTime = endTime;
        Limit = limit;
        IsExist = isExist;
    }

    public long getVouchersTemplateID() {
        return VouchersTemplateID;
    }

    public void setVouchersTemplateID(long VouchersTemplateID) {
        this.VouchersTemplateID = VouchersTemplateID;
    }


    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
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

    public String getLimit() {
        return Limit;
    }

    public void setLimit(String Limit) {
        this.Limit = Limit;
    }

    public boolean isIsExist() {
        return IsExist;
    }

    public void setIsExist(boolean IsExist) {
        this.IsExist = IsExist;
    }

}
