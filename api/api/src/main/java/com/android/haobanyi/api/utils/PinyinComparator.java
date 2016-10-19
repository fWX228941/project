package com.android.haobanyi.api.utils;

import com.android.haobanyi.model.bean.home.location.CitySortBean;

import java.util.Comparator;

/**
 * Created by fWX228941 on 2016/5/19.
 *
 * @作者: 付敏
 * @创建日期：2016/05/19
 * @邮箱：466566941@qq.com
 * @当前文件描述：对包含拼音城市列表的排序，比较两个item，自定义比较规则和算法
 */
public class PinyinComparator implements Comparator<CitySortBean> {

    @Override
    public int compare(CitySortBean o1, CitySortBean o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}
