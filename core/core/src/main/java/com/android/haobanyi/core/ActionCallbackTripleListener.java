package com.android.haobanyi.core;

/**
 * Created by fWX228941 on 2016/7/12.
 *
 * @作者: 付敏
 * @创建日期：2016/07/12
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 *  采用肯变长参数试验下，动态决定传递参数的个数
 */
public interface ActionCallbackTripleListener<T,W,U> {
    public void onSuccess(T data01,W data02,U data03);
    /**
     * 失败时调用
     *
     * @param errorEvent 错误码
     * @param message    错误信息
     */
    public void onFailure(String errorEvent, String message);

}
