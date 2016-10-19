package com.android.haobanyi.core;

/**
 * Created by fWX228941 on 2016/7/18.
 *
 * @作者: 付敏
 * @创建日期：2016/07/18
 * @邮箱：466566941@qq.com
 * @当前文件描述：
 * 可变参数：http://www.cnblogs.com/shishm/archive/2012/01/31/2332656.html
 * 不定参数量：String.formate 就是一个典型
 * 有一个极限性就是同一个类型
 * 数组的方式存取就是了
 *
 */
public interface ActionCallbackNListener<T extends Object> {
    public void onSuccess(T... datas);
    /**
     * 失败时调用
     *
     * @param errorEvent 错误码
     * @param message    错误信息
     */
    public void onFailure(String errorEvent, String message);
}
