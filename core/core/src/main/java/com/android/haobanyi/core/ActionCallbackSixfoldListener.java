package com.android.haobanyi.core;

/**
 * Created by fWX228941 on 2016/9/5.
 *
 * @作者: 付敏
 * @创建日期：2016/09/05
 * @邮箱：466566941@qq.com
 * @当前文件描述：传递六个列表项
 */
public interface ActionCallbackSixfoldListener<T,W,U,V,Q,X>  {
    public void onSuccess(T data01,W data02,U data03,V data04,Q datas,X data05);
    /**
     * 失败时调用
     *
     * @param errorEvent 错误码
     * @param message    错误信息
     */
    public void onFailure(String errorEvent, String message);
}
