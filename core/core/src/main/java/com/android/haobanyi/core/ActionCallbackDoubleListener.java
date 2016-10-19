package com.android.haobanyi.core;

import java.util.List;

/**
 * Created by fWX228941 on 2016/6/29.
 *
 * @作者: 付敏
 * @创建日期：2016/06/29
 * @邮箱：466566941@qq.com
 * @当前文件描述：传递两个数据的监听器
 */
public interface ActionCallbackDoubleListener<T,W> {

    public void onSuccess(T data01,W data02);

    /**
     * 失败时调用
     *
     * @param errorEvent 错误码
     * @param message    错误信息
     */
    public void onFailure(String errorEvent, String message);


}
