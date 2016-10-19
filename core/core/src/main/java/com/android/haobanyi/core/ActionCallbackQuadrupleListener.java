package com.android.haobanyi.core;

/**
 * Created by fWX228941 on 2016/7/19.
 *
 * @作者: 付敏
 * @创建日期：2016/07/19
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 */
public interface ActionCallbackQuadrupleListener<T,W,U,V> {
    public void onSuccess(T data01,W data02,U data03,V data04);
    /**
     * 失败时调用
     *
     * @param errorEvent 错误码
     * @param message    错误信息
     */
    public void onFailure(String errorEvent, String message);
}
