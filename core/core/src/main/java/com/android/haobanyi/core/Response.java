package com.android.haobanyi.core;

/**
 * Created by fWX228941 on 2016/5/27.
 *
 * @作者: 付敏
 * @创建日期：2016/05/27
 * @邮箱：466566941@qq.com
 * @当前文件描述：json返回数据
 */
public class Response<T> {
    private int code;
    private String message;
    private T obj;           // 单个对象
    private T objList;       // list对象
    private int totalCount;
    private int pageIndex;
    private int pageSize;
    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public boolean isSuccessCode() {
        return code ==101;
    }


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

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public T getObjList() {
        return objList;
    }

    public void setObjList(T objList) {
        this.objList = objList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
