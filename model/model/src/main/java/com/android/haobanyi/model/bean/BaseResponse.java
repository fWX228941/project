package com.android.haobanyi.model.bean;

import java.util.List;

/**
 * Created by fWX228941 on 2016/9/23.
 *
 * @作者: 付敏
 * @创建日期：2016/09/23
 * @邮箱：466566941@qq.com
 * @当前文件描述：响应实体类，也是需要重构。
 */

public class BaseResponse {
    protected int code;
    protected String message;
    public int getCode() {
        return code;
    }

    protected void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    protected void setMessage(String message) {
        this.message = message;
    }

}
