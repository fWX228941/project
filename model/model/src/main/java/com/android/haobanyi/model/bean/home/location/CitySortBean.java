package com.android.haobanyi.model.bean.home.location;

/**
 * Created by fWX228941 on 2016/5/19.
 *
 * @作者: 付敏
 * @创建日期：2016/05/19
 * @邮箱：466566941@qq.com
 * @当前文件描述：带拼音排序的城市列表
 */
public class CitySortBean {
    // 包括两个字段，一个是城市名，一个是拼音首字母
    private String name;   //显示的数据
    private String sortLetters;  //显示数据拼音的首字母

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
