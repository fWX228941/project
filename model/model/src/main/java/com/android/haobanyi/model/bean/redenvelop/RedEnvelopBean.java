package com.android.haobanyi.model.bean.redenvelop;

/**
 * Created by fWX228941 on 2016/8/11.
 *
 * @作者: 付敏
 * @创建日期：2016/08/11
 * @邮箱：466566941@qq.com
 * @当前文件描述：红包
 */
public class RedEnvelopBean {
    /*===============GetRedEnvelopeList=====================*/
    private long RedEnvelopeID;
    private String Price;
    private int Status;

    public RedEnvelopBean(long redEnvelopeID, long redEnvelopeTemplateID, String orderLimit, String endTime, String
            price, int status) {
        RedEnvelopeID = redEnvelopeID;
        RedEnvelopeTemplateID = redEnvelopeTemplateID;
        OrderLimit = orderLimit;
        EndTime = endTime;
        Price = price;
        Status = status;
    }
    /*===============GetRedEnvelopeList=====================*/

    /*===============common=====================*/
    private long RedEnvelopeTemplateID;
    private String OrderLimit;
    private String EndTime;
    /*===============common=====================*/

    /*===============GetRedEnvelopeTempList=====================*/
    private String EachLimit;
    private String RedEnvelopePrice;
    private boolean IsReceive;// 是否领取 true可以
    private boolean IsCanReceive;// 是否能领取 true

    public RedEnvelopBean(long redEnvelopeTemplateID, String redEnvelopePrice, String orderLimit, String endTime,
                          String eachLimit, boolean isReceive, boolean isCanReceive) {
        RedEnvelopeTemplateID = redEnvelopeTemplateID;
        RedEnvelopePrice = redEnvelopePrice;
        OrderLimit = orderLimit;
        EndTime = endTime;
        EachLimit = eachLimit;
        IsReceive = isReceive;
        IsCanReceive = isCanReceive;
    }
    /*===============GetRedEnvelopeTempList=====================*/


    public long getRedEnvelopeID() {
        return RedEnvelopeID;
    }

    public void setRedEnvelopeID(long redEnvelopeID) {
        RedEnvelopeID = redEnvelopeID;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public long getRedEnvelopeTemplateID() {
        return RedEnvelopeTemplateID;
    }

    public void setRedEnvelopeTemplateID(long redEnvelopeTemplateID) {
        RedEnvelopeTemplateID = redEnvelopeTemplateID;
    }

    public String getOrderLimit() {
        return OrderLimit;
    }

    public void setOrderLimit(String orderLimit) {
        OrderLimit = orderLimit;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getEachLimit() {
        return EachLimit;
    }

    public void setEachLimit(String eachLimit) {
        EachLimit = eachLimit;
    }

    public String getRedEnvelopePrice() {
        return RedEnvelopePrice;
    }

    public void setRedEnvelopePrice(String redEnvelopePrice) {
        RedEnvelopePrice = redEnvelopePrice;
    }

    public boolean isReceive() {
        return IsReceive;
    }

    public void setIsReceive(boolean isReceive) {
        IsReceive = isReceive;
    }

    public boolean isCanReceive() {
        return IsCanReceive;
    }

    public void setIsCanReceive(boolean isCanReceive) {
        IsCanReceive = isCanReceive;
    }

}
