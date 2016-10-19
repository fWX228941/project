package com.android.haobanyi.model.test;

import java.util.List;

/**
 * Created by fWX228941 on 2016/6/29.
 *
 * @作者: 付敏
 * @创建日期：2016/06/29
 * @邮箱：466566941@qq.com
 * @当前文件描述：  模拟 GetVerifyShoppingCart
 */
public class test02 {


    /**
     * code : 101
     * message : 提交成功！
     * data : [30339]
     */

    private int code;
    private String message;
    private List<Integer> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
