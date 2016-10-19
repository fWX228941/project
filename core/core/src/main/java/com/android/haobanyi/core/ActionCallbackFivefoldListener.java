package com.android.haobanyi.core;

/**
 * Created by fWX228941 on 2016/8/2.
 *
 * @作者: 付敏
 * @创建日期：2016/08/02
 * @邮箱：466566941@qq.com
 * @当前文件描述：传递5个列表项
 */
public interface ActionCallbackFivefoldListener<T,W,U,V,Q> {
    public void onSuccess(T data01,W data02,U data03,V data04,Q datas);
    /**
     * 失败时调用
     *
     * @param errorEvent 错误码
     * @param message    错误信息
     */
    public void onFailure(String errorEvent, String message);
}
