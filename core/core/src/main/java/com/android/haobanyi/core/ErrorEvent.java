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
package com.android.haobanyi.core;

/**
 *
 *
 * @author fumin
 * @date 16/3/24
 * @version 1.0
 * 说明：定制各种错误事件，定义好本地的错误码和错误信息，保证每个错误都能正常解析
 * 所以在客户端，对于这种情况要统一处理，并且要统一添加Log跳转界面获取弹出对话框提示的交给V层。其他的应用场景交给C层处理
 *
 * 处理逻辑：
 * 1.用户相关：
 *  1）请求失效：重新请求（998）
 *  2）token失效：跳转到登录界面，并返回到启动界面（996）
 *  3）断网：切换到断网界面，并给出提示（003）
 *  4）其他异常：测试阶段，暂不处理，给出错误提示码和响应的提示文字 （数据由后台反馈，bug处理）
 *
 *
 *
 *
 *
 */
public class ErrorEvent {
    /*错误信息以及错误代码*/
    //非法请求 999  应用场景：
    //请求失效 998
    //请求参数异常 997
    //AccessToken失效  996
    //其他待分类错误场景 000
    //参数为空  001
    //参数不合法  002
    //请求成功  101
    //网络连接失败 003

    public static final  String PARAM_NULL = "参数为空！"; //参数为空
    public static final  String PARAM_ILLEGAL = "参数不合法！"; //参数不合法
    public static final  String NETWORK_FAIL = "网络连接失败，请检查网络！"; //网络连接失败

    /*
    * 当出现{"Message":"找不到与请求 URI“http://www.haobanyi.com/api/User/DeleteCartSelectedItem/”匹配的 HTTP 资源。","MessageDetail":"在控制器“User”上找不到与该请求匹配的操作。"}D/OkHttp: X-Powered-By: ASP.NET
这种错误时就是url拼接不正确，我调用userId 和accessToken
    *
    * */

    public static final  String MESSAGE_PARAM_NULL = "参数为空！";
    public static final  String MESSAGE_NULL_SITU1 = "登录名为空！";//situation one
    public static final  String MESSAGE_NULL_SITU2 = "密码为空！";
    public static final  String MESSAGE_NULL_SITU3 = "手机号为空！";
    public static final  String MESSAGE_NULL_SITU4 = "验证码为空！";
    public static final  String CODE_PARAM_NULL = "001";
    public static final  int CODE_PARAM_NULL_INT = 001;

    public static final  String MESSAGE_PARAM_ILLEGAL = "参数不合法！";
    public static final  String MESSAGE_ILLEGAL_SITU1 = "手机号或邮箱格式不正确！";
    public static final  String MESSAGE_ILLEGAL_SITU3 = "密码格式不正确！";
    public static final  String MESSAGE_ILLEGAL_SITU2 = "当前页数小于零！";
    public static final  String CODE_PARAM_ILLEGAL = "002";
    public static final  int CODE_PARAM_ILLEGAL_INT = 002;

    /*
    *参数请求失败，连接失效
    *
    * */
    public static final  String MESSAGE_NETWORK_FAIL = "网络连接失败，请检查网络！";
    public static final  String CODE_NETWORK_FAIL = "003";
    public static final int CODE_NETWORK_FAIL_INT = 003;

    /*
    * 特别说明：像服务器端那边的问题：会爆出：500 Internal Server Error 的问题，并且提示：此错误页可能包含敏感信息，因为 ASP.NET 通过 &lt;customErrors mode="Off"/&gt; 被配置为显示详细错误消息。
    * 请考虑在生产环境中使用 &lt;customErrors mode="On"/&gt; 或 &lt;customErrors mode="RemoteOnly"/&gt;。-->
    * response.body(). 会为空
    *
    * */
    public static final  String MESSAGE_SERVER_FAIL = "服务器端存在问题";
    public static final  String CODE_SERVER_FAIL = "004";
    public static final int CODE_SERVER_FAIL_INT = 004;

    /*
    * {"code":102,"message":"没有查找到对应的数据！","data":null}  这种情况服务器没有数据，其实也是可以归类于 服务器端存在问题
    * 这个和请求成功，返回空的数据有着本质的区别
    *  {"code":103,"message":"服务正在审核或已下架！","data":null}
    *  这些情况都可以总结为上一条错误
    * */
    public static final String MESSAGE_SERVER_NO_DATA = "没有查找到对应的数据！";
    public static final String CODE_SERVER_NO_DATA = "102";
    public static final int CODE_SERVER_NO_DATA_INT = 102;

    /*
    * 可以确定时消息头的问题，参数不正确
    * 每一个参数都有可能出错，尤其是复制黏贴的时候，唉，说什么呢！我只能说粗心大意
    *
    *
    * */
    /*说这个是header请求头错误，消息头的顺序不能变更，不然就会出错,userID 和token过期导致，消息头都是错误的*/
    public static final String MESSAGE_INVALID_REQUEST = "非法请求！";// 根据消息头信息判断是否是移动客户端进行数据请求  消息头有问题.参数出现问题
    public static final String CODE_INVALID_REQUEST = "999";
    public static final int CODE_INVALID_REQUEST_INT = 999;
    /*
    * 01.如果必然性的出现这个问题，那么就要检查消息头是否写错
    * 02.非必然性，请求失效了， 这个让我很差异，难道是时间戳的关系，下次验证下  -》果然是时间戳 ，出现了概率性不准的问题，需要校正下
    * 这种情况要单独处理  提供一个返回时间戳的接口     【log地点：没有数据】
    * 最好的解决方案还是在服务器端：我到时候和廖敏说下
    * 让它提供一个空接口，返回服务器当前时间，在利用我本地时间，相减，得到一个时间差，也就是校正时间  ，一次就行
    * 03.其实还是存在概率性失效问题，即使时间戳是正确的。  这种情况也会时不时的发生，所以当遇到这种情况时，需要特殊处理，仿佛查询，直至有数据为止
    * 所有的参数都是正确的，还是会出现概率性的失效问题
    *
    *  这种情况要单独处理
    *  这个也可能是token失效
    *
    * */
    public static final String MESSAGE_EXPIRED_REQUEST = "请求失效！";// 根据消息头信息，判断改请求是否过期
    public static final String CODE_EXPIRED_REQUEST = "998";
    public static final int CODE_EXPIRED_REQUEST_INT = 998;

    public static final String MESSAGE_DEVICE_INFORMATION = "请求参数异常！";// 根据消息头信息判断是否包含设备信息
    public static final String CODE_DEVICE_INFORMATION = "997";
    public static final int CODE_DEVICE_INFORMATION_INT = 997;

    public static final String MESSAGE_TOKEN_EXPIRED = "AccessToken失效！";// 根据消息头信息判断是否包含设备信息  如果出现这个问题，一种是跳转到登录界面
    public static final String CODE_TOKEN_EXPIRED = "996";
    public static final int CODE_TOKEN_EXPIRED_INT = 996;

    public static final String MESSAGE_OTHER_ERROR = "其他异常请求待分类处理：";
    public static final String CODE_OTHER_ERROR = "000";
    public static final int CODE_OTHER_ERROR_INT = 000;

    public static final String MESSAGE_SUCCESS_REQUEST = "请求成功！";
    public static final String CODE_SUCCESS_REQUEST = "101";
    public static final int CODE_SUCCESS_REQUEST_INT = 101;
    /*
    * 1.注册三种异常：注册成功/失效/出错
    *
    *
    * */


}
