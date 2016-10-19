package com.android.haobanyi.model.bean.mine;

/**
 * Created by fWX228941 on 2016/6/20.
 *
 * @作者: 付敏
 * @创建日期：2016/06/20
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public class MyMessageBean {
    public long getUserMessageID() {
        return UserMessageID;
    }

    public void setUserMessageID(long userMessageID) {
        UserMessageID = userMessageID;
    }

    public long getIsReading() {
        return IsReading;
    }

    public void setIsReading(long isReading) {
        IsReading = isReading;
    }

    public String getMessageTypeDesc() {
        return MessageTypeDesc;
    }

    public void setMessageTypeDesc(String messageTypeDesc) {
        MessageTypeDesc = messageTypeDesc;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getMessageContext() {
        return MessageContext;
    }

    public void setMessageContext(String messageContext) {
        MessageContext = messageContext;
    }

    private long UserMessageID;
    private long IsReading;// 1 已读 其他未读
    private String MessageTypeDesc;
    private String CreateDate;
    private String MessageContext;


    public MyMessageBean(long userMessageID, long isReading, String messageTypeDesc, String createDate, String
            messageContext) {
        UserMessageID = userMessageID;
        IsReading = isReading;
        MessageTypeDesc = messageTypeDesc;
        CreateDate = createDate;
        MessageContext = messageContext;
    }
}
