/**
 * Copyright (C) 2015. Keegan小钢（http://keeganlee.me）
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.haobanyi.api;

/**
 * Api响应结果的封装类.所有的接口都放在这里
 *
 * @author fumin
 * @date 16/03/25
 * @version 1.0
 */
public class ApiResponse<T> {

    /* 是否要生成静态的 ，泛型类的所有实例共享*/
    private String event;    // 返回码，1为成功

/*    private int code; 难道参数名需要一致，这样才能够统一，形成一一对应的关系
        这个问题下次测试   除非里面包含对象
    private String message;*/
    private int code;   // 返回码   101位成功
    private String message;      // 返回信息
    private T obj;           // 单个对象
    private T objList;       // 数组对象
    private int currentPage; // 当前页数
    private int pageSize;    // 每页显示数量
    private int maxCount;    // 总条数
    private int maxPage;     // 总页数

    public ApiResponse(String event, String message) {
        this.event = event;
        this.message = message;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public boolean isSuccessCode() {
        return code ==101;
    }
    /**
     * 服务端接口响应结构的封装类，比如验证码：
     * 其返回码event
     * @return
     */
    public boolean isSuccess() {
        return event.equals("101");
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
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

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }
    //测试的接口
    public boolean issendSmsCodeSuccess() {
        return event.equals("1");
    }
}
