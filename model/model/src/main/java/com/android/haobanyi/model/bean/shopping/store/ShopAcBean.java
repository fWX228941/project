package com.android.haobanyi.model.bean.shopping.store;

/**
 * Created by fWX228941 on 2016/7/21.
 *
 * @作者: 付敏
 * @创建日期：2016/07/21
 * @邮箱：466566941@qq.com
 * @当前文件描述：过滤至ShopActivityBean
 */
public class ShopAcBean {
    private int Flag;
    private long Id;
    private String Title;
    private String Desc;
    private String StartTime;
    private String EndTime;
    private String CreateTime;
    private String Content;

    public ShopAcBean(int flag, long id, String title, String desc, String startTime, String endTime, String
            createTime, String content) {
        Flag = flag;
        Id = id;
        Title = title;
        Desc = desc;
        StartTime = startTime;
        EndTime = endTime;
        CreateTime = createTime;
        Content = content;
    }


    public int getFlag() {
        return Flag;
    }

    public void setFlag(int flag) {
        Flag = flag;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
