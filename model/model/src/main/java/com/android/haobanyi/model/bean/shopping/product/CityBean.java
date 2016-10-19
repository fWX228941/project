package com.android.haobanyi.model.bean.shopping.product;

/**
 * Created by fWX228941 on 2016/7/15.
 *
 * @作者: 付敏
 * @创建日期：2016/07/15
 * @邮箱：466566941@qq.com
 * @当前文件描述：服务地址列表
 * {"CityID":199,"Name":"测试"}
 *
 */
public class CityBean {

    /**
     * CityID : 199
     * Name : 测试
     */

    private long CityID;
    private String Name;

    public long getCityID() {
        return CityID;
    }

    public void setCityID(long CityID) {
        this.CityID = CityID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
}
