package com.android.haobanyi.model.bean.shopping.product;

/**
 * Created by fWX228941 on 2016/7/22.
 *
 * @作者: 付敏
 * @创建日期：2016/07/22
 * @邮箱：466566941@qq.com
 * @当前文件描述： 脱胎至EvaluationBean
 * 分为：用户评价和商家评价两个部分
 */
public class EvaBean {
    private long UserAssessmentID;
    private String UserName;
    private String UserViews;
    private String UserAssessmentDate;

    public EvaBean(long userAssessmentID, String userName, String userViews, String userAssessmentDate, boolean
            ishasReply, long traderAssessmentID, String traderName, String traderViews, String traderAssessmentDate) {
        UserAssessmentID = userAssessmentID;
        UserName = userName;
        UserViews = userViews;
        UserAssessmentDate = userAssessmentDate;
        this.ishasReply = ishasReply;
        this.traderAssessmentID = traderAssessmentID;
        this.traderName = traderName;
        this.traderViews = traderViews;
        this.traderAssessmentDate = traderAssessmentDate;
    }
    public EvaBean(long userAssessmentID, String userName, String userViews, String userAssessmentDate, boolean
            ishasReply) {
        UserAssessmentID = userAssessmentID;
        UserName = userName;
        UserViews = userViews;
        UserAssessmentDate = userAssessmentDate;
        this.ishasReply = ishasReply;
    }
    public boolean ishasReply() {
        return ishasReply;
    }

    public void setIshasReply(boolean ishasReply) {
        this.ishasReply = ishasReply;
    }

    private boolean ishasReply; //是否有回复
    private long traderAssessmentID;
    private String traderName;
    private String traderViews;
    private String traderAssessmentDate; // 这就是一个整体

    public EvaBean(long userAssessmentID, String userName, String userViews, String userAssessmentDate, long
            traderAssessmentID, String traderName, String traderViews, String traderAssessmentDate) {
        UserAssessmentID = userAssessmentID;
        UserName = userName;
        UserViews = userViews;
        UserAssessmentDate = userAssessmentDate;
        this.traderAssessmentID = traderAssessmentID;
        this.traderName = traderName;
        this.traderViews = traderViews;
        this.traderAssessmentDate = traderAssessmentDate;
    }


    public long getUserAssessmentID() {
        return UserAssessmentID;
    }

    public void setUserAssessmentID(long userAssessmentID) {
        UserAssessmentID = userAssessmentID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserViews() {
        return UserViews;
    }

    public void setUserViews(String userViews) {
        UserViews = userViews;
    }

    public String getUserAssessmentDate() {
        return UserAssessmentDate;
    }

    public void setUserAssessmentDate(String userAssessmentDate) {
        UserAssessmentDate = userAssessmentDate;
    }

    public long getTraderAssessmentID() {
        return traderAssessmentID;
    }

    public void setTraderAssessmentID(long traderAssessmentID) {
        this.traderAssessmentID = traderAssessmentID;
    }

    public String getTraderName() {
        return traderName;
    }

    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    public String getTraderViews() {
        return traderViews;
    }

    public void setTraderViews(String traderViews) {
        this.traderViews = traderViews;
    }

    public String getTraderAssessmentDate() {
        return traderAssessmentDate;
    }

    public void setTraderAssessmentDate(String traderAssessmentDate) {
        this.traderAssessmentDate = traderAssessmentDate;
    }
}
