package com.android.haobanyi.model.bean.charge;

/**
 * Created by fWX228941 on 2016/8/10.
 *
 * @作者: 付敏
 * @创建日期：2016/08/10
 * @邮箱：466566941@qq.com
 * @当前文件描述： 充值明细和余额
 */
public class ChargeBean {
    private String ChargeDetailID;
    private String ChargeAmount;
    private String ChargeTime;
    private String ChargeWay;

    public ChargeBean(String chargeDetailID, String chargeAmount, String chargeTime, String chargeWay) {
        ChargeDetailID = chargeDetailID;
        ChargeAmount = chargeAmount;
        ChargeTime = chargeTime;
        ChargeWay = chargeWay;
    }

    public String getChargeDetailID() {
        return ChargeDetailID;
    }

    public void setChargeDetailID(String chargeDetailID) {
        ChargeDetailID = chargeDetailID;
    }

    public String getChargeAmount() {
        return ChargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        ChargeAmount = chargeAmount;
    }

    public String getChargeTime() {
        return ChargeTime;
    }

    public void setChargeTime(String chargeTime) {
        ChargeTime = chargeTime;
    }

    public String getChargeWay() {
        return ChargeWay;
    }

    public void setChargeWay(String chargeWay) {
        ChargeWay = chargeWay;
    }
}
