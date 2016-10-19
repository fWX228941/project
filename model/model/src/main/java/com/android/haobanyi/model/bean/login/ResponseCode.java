package com.android.haobanyi.model.bean.login;

/**
 * Created by fWX228941 on 2016/4/1.
 *
 * @作者: 付敏
 * @创建日期：2016/04/01
 * @邮箱：466566941@qq.com
 * @当前文件描述：  发送验证码/注册的返回结果
 *
 *
 */
public class ResponseCode {

    private int code;
    private String message;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    /*================UploadUserPhoto/SubmitOrder========================*/
    private String data;
    /*================UploadUserPhoto/SubmitOrder========================*/
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
}
