

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
 * limitations under the License.*/


package com.android.haobanyi.api;
import com.android.haobanyi.api.ApiResponse;
import android.content.Context;


import com.android.haobanyi.model.bean.login.ResponseCode;

import java.util.List;

/**
 * Api接口
 *
 * @author Keegan小钢
 * @date 15/6/21
 * @version 1.0*/


public interface Api {
    // 发送验证码public final static String SEND_SMS_CODE = "service.sendSmsCode4Register";
    public final static String SEND_SMS_CODE = "/API/User/GetSmsOrEmailCodeIdent";
    // 注册
    //public final static String REGISTER = "customer.registerByPhone";  测试
    public final static String REGISTER = "/API/User/Register";

    // 登录  public final static String LOGIN = "customer.loginByApp";/API/User/
    public final static String LOGIN = "/API/User/Login";
    // 券列表
    public final static String LIST_COUPON = "issue.listNewCoupon";  // 这个才是真正的接口，what a fuck!  这个必须由服务器来提供



}
