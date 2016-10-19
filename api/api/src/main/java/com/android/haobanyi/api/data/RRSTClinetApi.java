package com.android.haobanyi.api.data;


import com.android.haobanyi.api.test.testData;
import com.android.haobanyi.model.bean.charge.ChargeListResponse;
import com.android.haobanyi.model.bean.city.AreaList;
import com.android.haobanyi.model.bean.contact.ContactListResponse;

import com.android.haobanyi.model.bean.foot.FootListResponse;
import com.android.haobanyi.model.bean.mine.myinformation.UserBean;
import com.android.haobanyi.model.bean.order.OrderListResponse;
import com.android.haobanyi.model.bean.order.OrderResponse;
import com.android.haobanyi.model.bean.order.OrderToPayResponse;
import com.android.haobanyi.model.bean.redenvelop.RedEnvelopListResponse;
import com.android.haobanyi.model.bean.shopping.store.ShopActivityBean;
import com.android.haobanyi.model.bean.userpoint.UserPointListResponse;
import com.android.haobanyi.model.bean.voucher.VoucherListResponse;
import com.android.haobanyi.model.bean.login.LoginResponseResult;
import com.android.haobanyi.model.bean.login.ResponseCode;
import com.android.haobanyi.model.bean.home.searching.GeneralSortDataBean;

import com.android.haobanyi.model.bean.shopping.ShoppingCartBean;
import com.android.haobanyi.model.bean.shopping.store.ShopResponseBean;
import com.android.haobanyi.model.bean.shopping.store.ShopRelatedServiceBean;
import com.android.haobanyi.model.bean.shopping.product.DetailsBean;
import com.android.haobanyi.model.bean.shopping.product.EvaluationBean;
import com.android.haobanyi.model.test.testData01;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by fWX228941 on 2016/4/20.
 *
 * @作者: 付敏
 * @创建日期：2016/04/20
 * @邮箱：466566941@qq.com
 * @当前文件描述：满口REST规范的服务器端提供的接口
 */
public interface RRSTClinetApi {



    /*A.非用户相关*/
    public final static String LOGIN = "api/User/Login"; //1.登录 http://域名/api/User/Login
    public final static String SEND_SMS_CODE = "api/User/GetSmsOrEmailCodeIdent";//4.获取注册验证码 http://域名/api/User/GetSmsOrEmailCodeIdent POST
    public final static String REGISTER = "api/User/Register";//3.注册 http://域名/api/User/Register POST
    public final static String MODIFYPWD = "api/User/ModifyPwd";
    public final static String FIND_PWD = "api/User/FindPwd";
    public final static String BIND_EMAIL = "api/User/BindEmail";
    public final static String BIND_MOBILE = "api/User/BindMobile";
    public final static String APPLY_ORDER_REFUND = "api/User/ApplyOrderRefund";
    public final static String UPLOAD_USER_PHOTO = "api/User/UploadUserPhoto";

    public final static String SET_PAY_PWD = "api/User/SetPayPwd";
    public final static String MODIFY_PAY_PWD = "api/User/ModifyPayPwd";
    public final static String Add_Platform_Consult = "api/User/AddPlatformConsult";
    public final static String RECEIVE_VOUCHERS = "api/User/ReceiveVouchers";

    public final static String GET_PRODUCT_LIST_BY_NORMALSORT = "api/User/GetProductListByNormalSort/";//7.获取综合排序的服务列表?pageIndex={0}&pageSize={1}" 问号后面就是参数一和参数二了   —— GeneralSortDataBean.java
    public final static String GET_PRODUCT = "api/User/GetProduct/";//9.获取服务详情/api/User/GetProduct/?productId={0}
    public final static String GET_PRODUCT_LIST_BY_HOT_SORT = "api/User/GetProductListByHotSort/";//8.获取销量排序的服务列表问号后面就是参数一和参数二了
    public final static String GET_SHOP_LIST_BY_NORMALSORT = "api/User/GetShopListByNormalSort/";
    public final static String GET_SHOP_LIST_BY_HOTSORT = "api/User/GetShopListByHotSort/";
    public final static String GET_SHOP_PRODUCT_LIST_BY_NORMALSORT = "api/User/GetShopProductListByNormalSort/";
    public final static String GET_SHOP_PRODUCT_LIST_BY_HOTSORT = "api/User/GetShopProductListByHotSort/";
    public final static String GET_SHOP_ACTIVITY = "api/User/GetShopActivity/";
    public final static String GET_SHOP_VOUCHERS_LIST = "api/User/GetShopVouchersList/";
    public final static String SEARCH_PRODUCT_BY_KEYWORD = "api/User/SearchProductByKeyword/";

    public final static String GET_USERCAPITAL = "api/User/GetUserCapital/";
    public final static String GET_USER_POINT = "api/User/GetUserPoint/";

    public final static String SEARCH_SHOP_BY_KEYWORD = "api/User/SearchShopByKeyword/";
    public final static String GETASSESSMENTLIST = "api/User/GetAssessmentList";

    public final static String GET_CHARGE_LIST = "api/User/GetChargeList";
    public final static String GET_RED_ENVELOPE_LIST = "api/User/GetRedEnvelopeList";
    public final static String GET_MY_FOOT_LIST = "api/User/GetMyFootList";
    public final static String QR_CODE_LOGIN = "api/User/QRCodeLogin";


    public final static String Get_RedEnvelope_Temp_List = "api/User/GetRedEnvelopeTempList";

    public final static String GET_VOUCHER_LIST = "api/User/GetVoucherList";


    /*B.用户相关*/
    public final static String GET_ACCESS_TOKEN = "api/User/GetUserAccessToken/";//5、获取访问令牌 http://域名/api/User/GetUserAccessToken/?userId={0}&accessToken={1}  GET
    public final static String GET_ASSESSMENT_COUNT = "api/User/GetAssessmentCount/";//5、获取访问令牌 http://域名/api/User/GetUserAccessToken/?userId={0}&accessToken={1}  GET

    public final static String RECEIVE_RED_ENVELOPE = "api/User/ReceiveRedEnvelope/";
    public final static String GET_ORDER_LIST = "api/User/GetOrderList/"; //12.获取我的订单列表  【用户中心】
    public final static String ADD_ASSESSMENT = "api/User/AddAssessment/"; //12.获取我的订单列表  【用户中心】
    public final static String CANCEL_ORDER = "api/User/CancelOrder/"; //

    public final static String GET_ORDER_COUNT = "api/User/GetOrderCount/";//12.获取我的订单数量
    public final static String GET_FAVORITE_SHOP_LIST = "api/User/GetFavoriteShopList/";//14.获取我收藏的店铺列表 添加了pageNo



    public final static String GET_FAVORITE_SHOP_COUNT = "api/User/GetFavoriteShopCount/";
    public final static String PAY_BY_CAPITAL = "api/User/PayByCapital/";
    public final static String SUBMIT_PAYMENT = "api/User/SubmitPayment/";
    public final static String SUBMIT_CHARGE = "api/User/SubmitCharge/";
    public final static String GET_FAVORITE_PRODUCT_LIST = "api/User/GetFavoriteProductList/";//13.获取我收藏的服务列表 添加了pageNo
    public final static String GET_FAVORITE_PRODUCT_COUNT = "api/User/GetFavoriteProductCount/";//13.获取我收藏的服务数量 添加了pageNo
    public final static String GET_USER_MESSAGE_LIST = "api/User/GetUserMessageList/";
    public final static String GET_ORDER = "api/User/GetOrder/";
    public final static String CONFIRM_ORDER = "api/User/ConfirmOrder/";
    public final static String CLOSE_ORDER = "api/User/CloseOrder/";

    public final static String GET_SHOPPING_CART = "api/User/GetShoppingCart/";

    public final static String GET_CONTACT_LIST = "api/User/GetContactList/";
    public final static String DEL_CONTACT = "api/User/DelContact/";
    public final static String SET_DEFAULT_CONTACT = "api/User/SetDefaultContact/";

    public final static String GET_VERIFY_SHOPPING_CART = "api/User/GetVerifyShoppingCart/";
    public final static String SELECT_CART_ITEM = "api/User/SelectCartItem/";
    public final static String CANCEL_CART_ITEM = "api/User/CancelCartItem/";
    public final static String SELECT_CART_GROUP_ITEM = "api/User/SelectCartGroupItem/";
    public final static String CANCEL_CART_GROUP_ITEM = "api/User/CancelCartGroupItem/";
    public final static String SELECT_CART_SHOP_ITEM = "api/User/SelectCartShopItem/";
    public final static String CANCEL_CART_SHOP_ITEM = "api/User/CancelCartShopItem/";
    public final static String ADD_CART_ITEM = "api/User/AddCartItem/";
    public final static String ADD_CART_ITEM_OF_BUY_NOW = "api/User/AddCartItemOfBuyNow/";
    public final static String DELETE_CART_SELECTEDITEM = "api/User/DeleteCartSelectedItem/";

    public final static String SAVE_GENERAL_INVOICE = "api/User/SaveGeneralInvoice/";
    public final static String SAVE_VAT_INVOICE = "api/User/SaveVatInvoice/";






    public final static String SELECT_CART_ALL_ITEM = "api/User/SelectCartAllItem/";
    public final static String CANCEL_CART_ALL_ITEM = "api/User/CancelCartAllItem/";
    public final static String UPDATE_CART_QUANTITY = "api/User/UpdateCartQuantity/";
    public final static String UPDATE_CART_GROUP_SELL_QUANTITY = "api/User/UpdateCartGroupSellQuantity/";
    public final static String SELECT_CART_OTHER_SERVICE = "api/User/SelectCartOtherService/";  // 暂时先放着
    public final static String SAVE_CART_FAVORITE_PRODUCT = "api/User/SaveCartFavoriteProduct/";
    public final static String SUBMIT_ORDER = "api/User/SubmitOrder/";
    public final static String GET_VERIFY_ORDER_INFO = "api/User/GetVerifyOrderInfo/";

    public final static String DELETE_FAV_PRODUCT = "api/User/DeleteFavProduct/";
    public final static String FAV_PRODUCT = "api/User/FavProduct/";
    public final static String DELETE_FAV_SHOP = "api/User/DeleteFavShop/";

    public final static String FAV_SHOP = "api/User/FavShop/";
    public final static String ADDCONTACT = "api/User/AddContact/";
    public final static String EDIT_CONTACT = "api/User/EditContact/";  // 暂时先放着
    public final static String AUTH_LOGIN = "api/User/AuthLogin/";  //
    public final static String BIND_USER = "api/User/BindUser/";  // 暂时先放着






    /*C.可选*/
    public final static String GETSHOP = "api/User/GetShop/";
    public final static String GETAREALIST = "api/User/GetAreaList/";




    //6、获取用户信息 http://域名/api/User/GetUserInfo/?userId={0}&accessToken={1} GET
    public final static String GET_USER_INFO = "api/User/GetUserInfo/";



    //10.获取省份列表
    public final static String GET_PROVINCE_LIST = "api/User/GetProvinceList/";
    //11.获取城市列表
    public final static String GET_CITY_LIST_BY_PROVINCE = "api/User/GetCityListByProvince/";
    //12.获取地区列表
    public final static String GET_DISTRICT_LIST_BY_CITY = "api/User/GetDistrictListByCity/";







    public final static String MOBILELOGIN = "api/User/MobileSmsLogin";
    /*对于post请求，
        1.如果参数是对象 则在参数前添加@Body
        2.如果参数是基本类型 则在参数前添加@Field(服务器得到参数名)并且如果是多个参数，需要添加注释 @FormUrlEncoded
        不然会导致程序崩溃，出现参数不合法，java.lang.IllegalArgumentException: @Field parameters can only be used with form encoding.
        参数域必须被组织成FormUrlEncoded 表单格式
        3.@FormUrlEncoded 这也就是表单的方式传递键值对，添加FormUrlEncoded，然后通过@Field添加参数即可

     */
    //  返回类型
    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(LOGIN)   // foo.example("Bob Smith", "Jane Doe")} yields a request body of{name=Bob+Smith&name=Jane+Doe}.@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<LoginResponseResult> login(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Field("strAccount") String loginName,@Field("strPwd") String password,@Field("strOperateIP") String imei);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(GETASSESSMENTLIST)   // foo.example("Bob Smith", "Jane Doe")} yields a request body of{name=Bob+Smith&name=Jane+Doe}.@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<EvaluationBean> getAssessmentList(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Field("ProductID")long ProductID ,@Field("AssessType")Integer AssessType,@Field("PageNo")Integer PageNo ,@Field("PageSize")Integer PageSize );

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(GET_CHARGE_LIST)   // foo.example("Bob Smith", "Jane Doe")} yields a request body of{name=Bob+Smith&name=Jane+Doe}.@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<ChargeListResponse> getChargeList(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field("PageNo")Integer PageNo ,@Field("PageSize")Integer PageSize );

    /*
    * 这个原因找的好辛苦了
    * @FormUrlEncoded 只有@field 才行  这些关键字要理解很清楚才是
    *
    * */
    @Headers("Hby-Req-Device:Android/1.0")
    @POST(QR_CODE_LOGIN)   // foo.example("Bob Smith", "Jane Doe")} yields a request body of{name=Bob+Smith&name=Jane+Doe}.@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<ResponseCode> qrCodeLogin(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("loginToken")String loginToken ,@Query("nsTS")String nsTS );



    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(GET_RED_ENVELOPE_LIST)   // foo.example("Bob Smith", "Jane Doe")} yields a request body of{name=Bob+Smith&name=Jane+Doe}.@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<RedEnvelopListResponse> getRedEnvelopeList(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field("PageNo")Integer PageNo ,@Field("PageSize")Integer PageSize );


    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(GET_MY_FOOT_LIST)   // foo.example("Bob Smith", "Jane Doe")} yields a request body of{name=Bob+Smith&name=Jane+Doe}.@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<FootListResponse> getMyFootList(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field("PageNo")Integer PageNo ,@Field("PageSize")Integer PageSize );



    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(Get_RedEnvelope_Temp_List)   // foo.example("Bob Smith", "Jane Doe")} yields a request body of{name=Bob+Smith&name=Jane+Doe}.@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<RedEnvelopListResponse> getRedEnvelopeTempList(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Header("Hby-Req-UserId") String Hby_Req_UserId,@Field("PageNo")Integer PageNo ,@Field("PageSize")Integer PageSize );


    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(GET_VOUCHER_LIST)   // foo.example("Bob Smith", "Jane Doe")} yields a request body of{name=Bob+Smith&name=Jane+Doe}.@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<VoucherListResponse> getVoucherList(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field("PageNo")Integer PageNo ,@Field("PageSize")Integer PageSize );



    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(MODIFYPWD)
    Call<ResponseCode> modifypwd(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field("Pwd")String Pwd,@Field("NewPwd")String NewPwd);


    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(FIND_PWD)
    Call<ResponseCode> findPwd(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Field("strAccount")String strAccount,@Field("strPwd")String strPwd,@Field("strSmsCode")String strSmsCode);


    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(BIND_EMAIL)
    Call<ResponseCode> bindEmail(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@FieldMap Map<String, String> params);
    //BIND_MOBILE
    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(BIND_MOBILE)
    Call<ResponseCode> bindMobile(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@FieldMap Map<String, String> params);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(APPLY_ORDER_REFUND)
    Call<ResponseCode> applyOrderRefund(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@FieldMap Map<String, Object> params);//上传多张图片

    @Headers("Hby-Req-Device:Android/1.0")
    @Multipart
    @POST(UPLOAD_USER_PHOTO)
    Call<ResponseCode> uploadUserPhoto(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Part("description") RequestBody description,
                                       @Part MultipartBody.Part file);//上传多张图片，改变用户行为，这样很


    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(SET_PAY_PWD)
    Call<ResponseCode> setPayPwd(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field ("Pwd") String Pwd,@Field ("PayPwd") String PayPwd);


    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(MODIFY_PAY_PWD)
    Call<ResponseCode> modifyPayPwd(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field ("Account") String Account,@Field ("ValidateCode") String ValidateCode,@Field ("PayPwd") String PayPwd);


    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(Add_Platform_Consult)
    Call<ResponseCode> addPlatformConsult(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field ("ConsultContext") String ConsultContext);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(RECEIVE_VOUCHERS)
    Call<ResponseCode> receiveVouchers(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field ("VouchersCode") String VouchersCode);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(RECEIVE_VOUCHERS)
    Call<ResponseCode> ReceiveVouchers(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("tempId")long tempId);


    /*
    *
    *  java.lang.IllegalArgumentException: @Body parameters cannot be used with form or multi-part encoding. (parameter #6)
    *  因为body 本身就被组织成 multi-part encoding
    *
    *      @Headers({"Hby-Req-Device:Android/1.0",
            "Content-type:application/json:charset=UTF-8"
    })
    *
    *
    * */
    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
   //"Content-type:application/json:charset=UTF-8" 参考文档：http://www.jianshu.com/p/3e13e5d34531
    @POST(ADD_CART_ITEM)//@Body RequestBody params  @Field("ShopId")long ShopId,@Field("ProductId")long ProductId,@Field("GroupSellID")long GroupSellID,@Field("Quantity") int Quantity,@Field("AttributeIdList") String[] AttributeIdList
    Call<ResponseCode> addCartItem(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field ("Carts") String params);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    //"Content-type:application/json:charset=UTF-8" 参考文档：http://www.jianshu.com/p/3e13e5d34531
    @POST(ADD_CART_ITEM_OF_BUY_NOW)//@Body RequestBody params  @Field("ShopId")long ShopId,@Field("ProductId")long ProductId,@Field("GroupSellID")long GroupSellID,@Field("Quantity") int Quantity,@Field("AttributeIdList") String[] AttributeIdList
    Call<ResponseCode> addCartItemOfBuyNow(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field ("Carts") String params);




    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(DELETE_CART_SELECTEDITEM)//@Body RequestBody params  @Field("ShopId")long ShopId,@Field("ProductId")long ProductId,@Field("GroupSellID")long GroupSellID,@Field("Quantity") int Quantity,@Field("AttributeIdList") String[] AttributeIdList
    Call<ShoppingCartBean> deleteCartSelectedItem(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field ("Carts") String params);


    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    //"Content-type:application/json:charset=UTF-8" 参考文档：http://www.jianshu.com/p/3e13e5d34531
    @POST(SAVE_GENERAL_INVOICE)//@Body RequestBody params  @Field("ShopId")long ShopId,@Field("ProductId")long ProductId,@Field("GroupSellID")long GroupSellID,@Field("Quantity") int Quantity,@Field("AttributeIdList") String[] AttributeIdList
    Call<ResponseCode> saveGeneralInvoice(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field ("Invoinces") String params);


    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    //"Content-type:application/json:charset=UTF-8" 参考文档：http://www.jianshu.com/p/3e13e5d34531
    @POST(SAVE_VAT_INVOICE)//@Body RequestBody params  @Field("ShopId")long ShopId,@Field("ProductId")long ProductId,@Field("GroupSellID")long GroupSellID,@Field("Quantity") int Quantity,@Field("AttributeIdList") String[] AttributeIdList
    Call<ResponseCode> saveVatInvoice(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@FieldMap Map<String, String> params);


    /*手机验证码登录*/
    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(MOBILELOGIN)
    Call<LoginResponseResult> loginByPhone(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Field("strAccount") String loginName,@Field("strSmsCode") String code,@Field("strOperateIP") String ip);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(REGISTER)  // 测试验证码时，还多了一个data字段@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<ResponseCode> registerByPhone(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Field("strAccount")String phoneNum,@Field("strSmsCode")String code,@Field("strPwd")String password,@Field("strOperateIP") String ip);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(SEND_SMS_CODE)//@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<ResponseCode> sendSmsCode4Register(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Field("strAccount")String phoneNum);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(GET_ORDER_LIST)
    Call<OrderListResponse> getOrderList(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("orderType")String orderType,@FieldMap Map<String, Object> params);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(ADD_ASSESSMENT)
    Call<ResponseCode> addAssessment(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@FieldMap Map<String, Object> params);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(CANCEL_ORDER)
    Call<ResponseCode> cancelOrder(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("orderId")long orderId,@Field("Reason")String  Reason);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(GET_ORDER_COUNT)
    Call<testData01> getOrderCount(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("orderType")String orderType,@Field("PageNo")Integer PageNo);



    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(SELECT_CART_OTHER_SERVICE)
    Call<ResponseCode> selectCartOtherService(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("productId")long productId,@Query("groupSellId")long groupSellId,@Field ("AttrIds") String params);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(ADDCONTACT)
    Call<ResponseCode> addContact(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@FieldMap Map<String, String> params);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(EDIT_CONTACT)
    Call<ResponseCode> editContact(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@FieldMap Map<String, String> params);



    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(AUTH_LOGIN)
    Call<UserBean> authLogin(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@FieldMap Map<String, String> params);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(BIND_USER)
    Call<UserBean> bindUser(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@FieldMap Map<String, String> params);






    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(GET_FAVORITE_PRODUCT_LIST)//@Field("PageSize")int PageSize
    Call<GeneralSortDataBean> getFavoriteProductList(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field("PageNo")Integer PageNo);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(GET_FAVORITE_PRODUCT_COUNT)
    Call<LoginResponseResult> getFavoriteProductCount(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field("PageNo")Integer PageNo);


    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(GET_USER_MESSAGE_LIST)
    Call<FootListResponse> getUserMessageList(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("messageType")String messageType,@Field("PageNo")Integer PageNo,@Field("PageSize")Integer PageSize);



    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(GET_FAVORITE_SHOP_LIST)   //这个pageNo 字段是否要添加
    Call<ShopRelatedServiceBean> getFavoriteShopList(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field("PageNo")Integer PageNo);


    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(GET_FAVORITE_SHOP_COUNT)
    Call<LoginResponseResult> getFavoriteShopCount(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field("PageNo")Integer PageNo);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(PAY_BY_CAPITAL)
    Call<ResponseCode> payByCapital(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field("OrderPayId")String orderPayId,@Field("PayPwd")String payPwd);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(SUBMIT_PAYMENT)
    Call<LoginResponseResult> submitPayment(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field("OrderIds")String OrderIds);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(SUBMIT_CHARGE)
    Call<ResponseCode> submitCharge(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken);


    /*
    * 对于get请求：
    *   1.当无参时为空
    *   2。当存在参数时，用@Query(参数) 也就是url ？后面的参数，参数类型要制定正确
    *   3.当参数有很多时，可以通过@queryMap 注释 和map对象参数来指定每个表单项的Key，value值如：@QueryMap Map<String,String> map
    *  下面需要修改
    *  没有使用的都是需要修改的
    * */
    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_ACCESS_TOKEN)  // 这个坑就是有userId 的并不一定是用户相关的
    Call<LoginResponseResult> getToken(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken);


    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_ASSESSMENT_COUNT)  // 这个坑就是有userId 的并不一定是用户相关的
    Call<LoginResponseResult> getAssessmentCount(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("productId")long productId);



    @Headers("Hby-Req-Device:Android/1.0")
    @GET(RECEIVE_RED_ENVELOPE)  // 这个坑就是有userId 的并不一定是用户相关的
    Call<ResponseCode> receiveRedEnvelope(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("tempId")long tempId);



    @Headers("Hby-Req-Device:Android/1.0")
    @POST(GET_ORDER)
    Call<OrderResponse> getOrder(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("orderId")long orderId);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(CONFIRM_ORDER)
    Call<ResponseCode> confirmOrder(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("orderId")long orderId);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(CLOSE_ORDER)
    Call<ResponseCode> closeOrder(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("orderId")long orderId);



    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_SHOPPING_CART)
    Call<ShoppingCartBean> getShoppingCart(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken);


    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_CONTACT_LIST)
    Call<ContactListResponse> getContactList(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken);


    @Headers("Hby-Req-Device:Android/1.0")
    @GET(DEL_CONTACT)
    Call<ResponseCode> delContact(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("contactManagerId")long contactManagerId);





    @Headers("Hby-Req-Device:Android/1.0")
    @GET(SET_DEFAULT_CONTACT)
    Call<ResponseCode> setDefaultContact(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("contactManagerId")long contactManagerId);



    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_VERIFY_SHOPPING_CART)
    Call<ShoppingCartBean> getVerifyShoppingCart(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken);


    @Headers("Hby-Req-Device:Android/1.0")
    @GET(SELECT_CART_ITEM)
    Call<ShoppingCartBean> selectCartItem(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("productId")long productId);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(CANCEL_CART_ITEM)
    Call<ShoppingCartBean> cancelCartItem(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("productId")long productId);


     @Headers("Hby-Req-Device:Android/1.0")
     @GET(SELECT_CART_GROUP_ITEM)
     Call<ShoppingCartBean> selectCartGroupItem(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("groupSellId")long groupSellId);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(CANCEL_CART_GROUP_ITEM)
    Call<ShoppingCartBean> cancelCartGroupItem(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("groupSellId")long groupSellId);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(SELECT_CART_SHOP_ITEM)
    Call<ShoppingCartBean> selectCartShopItem(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("shopId")long shopId);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(CANCEL_CART_SHOP_ITEM)
    Call<ShoppingCartBean> cancelCartShopItem(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("shopId")long shopId);


    @Headers("Hby-Req-Device:Android/1.0")
    @GET(SELECT_CART_ALL_ITEM)
    Call<ShoppingCartBean> selectCartAllItem(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(CANCEL_CART_ALL_ITEM)
    Call<ShoppingCartBean> cancelCartAllItem(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken);
    /*
    *  java.lang.IllegalArgumentException: Form-encoded method must contain at least one @Field.
    *
    * */
    @Headers("Hby-Req-Device:Android/1.0")
    @GET(UPDATE_CART_QUANTITY)
    Call<ShoppingCartBean> updateCartQuantity(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("productId")long productId,@Query("quantity")Integer quantity);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(UPDATE_CART_GROUP_SELL_QUANTITY)
    Call<ShoppingCartBean> updateCartGroupSellQuantity(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("groupSellId")long productId,@Query("quantity")Integer quantity);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(SAVE_CART_FAVORITE_PRODUCT)
    Call<ShoppingCartBean> saveCartFavoriteProduct(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Field ("Carts") String params);


    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(SUBMIT_ORDER) //要看看到底是什么
    Call<OrderToPayResponse> submitOrder(@Header("Hby-Req-From") String Hby_Req_From, @Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time, @Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth, @Query("userId")long userId, @Query("accessToken")String accessToken, @Query("redId")String redId, @Field ("Orders") String params);

    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(GET_VERIFY_ORDER_INFO) //要看看到底是什么
    Call<ShoppingCartBean> getVerifyOrderInfo(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("redId")long redId,@Field ("Vouchers") String params);


    @Headers("Hby-Req-Device:Android/1.0")
    @GET(DELETE_FAV_PRODUCT)
    Call<ResponseCode> deleteFavProduct(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("productId")long productId);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(FAV_PRODUCT)
    Call<ResponseCode> favProduct(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("productId")long productId);


    @Headers("Hby-Req-Device:Android/1.0")
    @GET(DELETE_FAV_SHOP)
    Call<ResponseCode> deleteFavShop(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("shopId")long shopId);


    @Headers("Hby-Req-Device:Android/1.0")
    @GET(FAV_SHOP)
    Call<ResponseCode> favShop(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken,@Query("shopId")long shopId);









    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_USER_INFO)
    Call<UserBean> getUserInfo(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_PRODUCT_LIST_BY_NORMALSORT)
    Call<GeneralSortDataBean> getProductListByNormalSort(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("pageIndex")Integer pageIndex ,@Query("pageSize")Integer pageSize);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_PRODUCT_LIST_BY_HOT_SORT)  //@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<GeneralSortDataBean> getProductListByHotSort(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("pageIndex")Integer pageIndex ,@Query("pageSize")Integer pageSize);


    @Headers("Hby-Req-Device:Android/1.0")
    @GET(SEARCH_PRODUCT_BY_KEYWORD)  //@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<GeneralSortDataBean> searchProductByKeyword(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("keyword")String keyword ,@Query("pageIndex")Integer pageIndex ,@Query("pageSize")Integer pageSize,@Query("sortType")Integer sortType,@Query("sortMode")boolean sortMode );


    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_USERCAPITAL)  //@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<ChargeListResponse> getUserCapital(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_USER_POINT)  //@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<UserPointListResponse> getUserPoint(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("userId")long userId,@Query("accessToken")String accessToken);









    @Headers("Hby-Req-Device:Android/1.0")
    @GET(SEARCH_SHOP_BY_KEYWORD)  //@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<ShopRelatedServiceBean> searchShopByKeyword(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("keyword")String keyword ,@Query("pageIndex")Integer pageIndex ,@Query("pageSize")Integer pageSize);




    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_SHOP_PRODUCT_LIST_BY_NORMALSORT)  //@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<ShopRelatedServiceBean> getShopProductListByNormalSort(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("shopId")long shopId,@Query("pageIndex")Integer pageIndex ,@Query("pageSize")Integer pageSize);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_SHOP_PRODUCT_LIST_BY_HOTSORT)  //@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<ShopRelatedServiceBean> getShopProductListByHotSort(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("shopId")long shopId,@Query("pageIndex")Integer pageIndex ,@Query("pageSize")Integer pageSize);


    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_SHOP_LIST_BY_NORMALSORT)  //@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<ShopRelatedServiceBean> getShopListByNormalSort(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("pageIndex")Integer pageIndex ,@Query("pageSize")Integer pageSize);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_SHOP_LIST_BY_HOTSORT)  //@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,
    Call<ShopRelatedServiceBean> getShopListByHotSort(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("pageIndex")Integer pageIndex ,@Query("pageSize")Integer pageSize);
    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GETSHOP)
    Call<ShopResponseBean> getShop(@Header("Hby-Req-UserId") String Hby_Req_UserId,@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("shopId")long shopId);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GETSHOP)
    Call<ShopResponseBean> getShopwithoutId(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Header("Hby-Req-UserId") String Hby_Req_UserId,@Query("shopId")long shopId);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GETAREALIST)
    Call<AreaList> GetAreaList(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_SHOP_ACTIVITY)
    Call<ShopActivityBean> getShopActivity(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("shopId")long shopId);

    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_SHOP_VOUCHERS_LIST)
    Call<VoucherListResponse> getShopVouchersList(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Header("Hby-Req-UserId") String Hby_Req_UserId,@Query("shopId")long shopId);


    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_PRODUCT)
    Call<DetailsBean> getProduct(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Header("Hby-Req-UserId") String Hby_Req_UserId,@Query("productId")long productId);
    /*用测试数据，数据量太大，程序直接崩溃了*/
    @Headers("Hby-Req-Device:Android/1.0")
    @GET(GET_PROVINCE_LIST)
    Call<testData> getProvinceList();
    @GET(GET_CITY_LIST_BY_PROVINCE)
    Call<testData> getCityListByProvince(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("provinceId")Integer provinceId);
    @GET(GET_DISTRICT_LIST_BY_CITY)
    Call<testData> getDistrictListByCity(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Query("cityId")Integer cityId);

    /*一下为测试接口*/
    /*Only one HTTP method is allowed. Found: GET and POST.*/
    @Headers("Hby-Req-Device:Android/1.0")
    @FormUrlEncoded
    @POST(LOGIN)   // foo.example("Bob Smith", "Jane Doe")} yields a request body of{name=Bob+Smith&name=Jane+Doe}.
    Call<LoginResponseResult> loginTest(@Header("Hby-Req-From") String Hby_Req_From,@Header("Hby-Req-Rxp-Time") String Hby_Req_Rxp_Time,@Header("Hby-Req-Rxp-Auth") String Hby_Req_Rxp_Auth,@Field("strAccount") String loginName,@Field("strPwd") String password,@Field("strOperateIP") String imei);


}
