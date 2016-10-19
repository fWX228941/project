package com.android.haobanyi.api;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fWX228941 on 2016/3/28.
 * 用于手机验证码的返回结果
 *  return
 *  废弃2016/04/01
 */
public class ErrorCode  {


    /**
     * code : 1
     * message : 发送成功
     */

    private int code;
    private String message;

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

    public boolean issendSmsCodeSuccess() {
        return 1==getCode();
    }
}
