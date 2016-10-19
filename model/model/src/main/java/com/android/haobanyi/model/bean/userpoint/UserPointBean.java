package com.android.haobanyi.model.bean.userpoint;

/**
 * Created by fWX228941 on 2016/8/11.
 *
 * @作者: 付敏
 * @创建日期：2016/08/11
 * @邮箱：466566941@qq.com
 * @当前文件描述：会员积分
 */
public class UserPointBean {
    private String TypeDesc;
    private String Integral;
    private String CreateDate;

    public UserPointBean(String typeDesc, String integral, String createDate) {
        TypeDesc = typeDesc;
        Integral = integral;
        CreateDate = createDate;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getTypeDesc() {
        return TypeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        TypeDesc = typeDesc;
    }

    public String getIntegral() {
        return Integral;
    }

    public void setIntegral(String integral) {
        Integral = integral;
    }
}
