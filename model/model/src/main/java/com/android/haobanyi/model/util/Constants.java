package com.android.haobanyi.model.util;

import java.net.URL;

/**
 * Created by fWX228941 on 2016/4/26.
 *
 * @作者: 付敏
 * @创建日期：2016/04/26
 * @邮箱：466566941@qq.com
 * @当前文件描述：事件流的标识流，对于app中的util包，其区别在于model中的标识量是全局性质的，是连接3个模块的公共信息流中心
 */
public class Constants {
    //1.broadcast广播与Service之间约定的一个通信标识量ActionConstants
    /*统一格式：_ACTION_ENABLE_xxx   运行访问百度地图*/
    /*private static final String _ACTION_LOCATE = "com.android.action.LOCATE";*/
    //2.application 与broadcast之间的广播约定，其实和1可以合并成一个广播 发送百度地图事件流
    //1.连接百度地图广播，区别在于这个是需要注册的
    public static final String ACTION_ENABLE_LOACTION = "com.android.action.LOCATE";
    /*开机广播*/
    public static final String ACTION_BOOT_COMPLETE = "android.intent.action.BOOT_COMPLETED";
    /*定位相关键值信息location*/
    public static final String LOCATION_PRE = "_location";
    public static final String  latitude= "latitude"+LOCATION_PRE; //经度
    public static final String  lontitude= "lontitude"+LOCATION_PRE; //维度
    public static final String  CountryCode= "CountryCode"+LOCATION_PRE;
    public static final String  country= "country"+LOCATION_PRE;
    public static final String  citycode= "citycode"+LOCATION_PRE;
    public static final String  city= "city"+LOCATION_PRE;
    public static final String  district= "district"+LOCATION_PRE;  // 地区
    public static final String  user_district= "user_district"+LOCATION_PRE; //用户选择的地区暂时分离出来
    public static final String  street= "street"+LOCATION_PRE;
    public static final String  addr= "addr"+LOCATION_PRE;
    public static final String  Describe= "Describe"+LOCATION_PRE;
    public static final String  Poi= "Poi"+LOCATION_PRE; //详细地址

    /*用户输入信息*/
    public static final String  USER_SEARCH= "user_input";//当前用户的最新输入
    public static final String  USER_SEARCH_LIST= "user_search_list";
    /*用户输入的手机号*/
    public static final String  PHONE_NUMBER= "phone_number";
    public static final String  PHONE_PASSWORD = "phone_password";
    /*标识量*/
    public static final String  STATE_FROM_FPW= "state_from_fpw";
    public static final String  TO_EMAIL_OR_TO_PHONEBIND= "to_email_or_to_phonebind";
    public static final String  STATE_FROM_RGT= "state_from_rgt";
    public static final String  STATE_FROM_RGT_SUC= "state_from_rgt_suc";
    public static final String  STATE_FROM_SHOPPING= "state_from_shopping";
    public static final String  STATE_FROM_FRONTVIEW= "state_from_frontview";
    public static final String  PRODUCT_ID= "product_id";
    public static final String  SHOP_ID= "shop_id";
    public static final String  STATE_FROM_SRGA= "state_from_srga";
    /*服务器名称*/
    public static final String  URL = "http://192.168.0.110:8089/";

    /*接口方法名*/
    public final static String LOGIN = "Login".toLowerCase();
    public final static String AUTH_LOGIN = "AuthLogin".toLowerCase();
    public final static String BIND_USER = "BindUser".toLowerCase();
    public final static String MOBILELOGIN = "MobileSmsLogin".toLowerCase();
    //3.注册 http://域名/api/User/Register POST
    public final static String REGISTER = "Register".toLowerCase();
    //4.获取注册验证码 http://域名/api/User/GetSmsOrEmailCodeIdent POST
    public final static String SEND_SMS_CODE = "GetSmsOrEmailCodeIdent".toLowerCase();
    public final static String MODIFYPWD = "ModifyPwd".toLowerCase();
    public final static String FIND_PWD = "FindPwd".toLowerCase();
    public final static String Add_Platform_Consult = "AddPlatformConsult".toLowerCase();
    public final static String RECEIVE_VOUCHERS = "ReceiveVouchers".toLowerCase();

    //5、获取访问令牌 http://域名/api/User/GetUserAccessToken/?userId={0}&accessToken={1}  GET
//    public final static String GET_ACCESS_TOKEN = "GetUserAccessToken".toLowerCase();
    //6、获取用户信息 http://域名/api/User/GetUserInfo/?userId={0}&accessToken={1} GET
    public final static String GET_USER_INFO = "GetUserInfo".toLowerCase();
    public final static String GET_USERCAPITAL = "GetUserCapital".toLowerCase();
    public final static String GET_USER_POINT = "GetUserPoint".toLowerCase();

    public final static String GET_CHARGE_LIST = "GetChargeList".toLowerCase();
    public final static String GET_RED_ENVELOPE_LIST = "GetRedEnvelopeList".toLowerCase();
    public final static String GET_MY_FOOT_LIST = "GetMyFootList".toLowerCase();
    public final static String Get_RedEnvelope_Temp_List = "GetRedEnvelopeTempList".toLowerCase();

    public final static String GET_VOUCHER_LIST = "GetVoucherList".toLowerCase();
    //7.获取综合排序的服务列表?pageIndex={0}&pageSize={1}" 问号后面就是参数一和参数二了   —— GeneralSortDataBean.java
    public final static String GET_PRODUCT_LIST_BY_NORMALSORT = "GetProductListByNormalSort".toLowerCase();
    //8.获取销量排序的服务列表问号后面就是参数一和参数二了
    public final static String GET_PRODUCT_LIST_BY_HOT_SORT = "GetProductListByHotSort".toLowerCase();
    public final static String GET_SHOP_LIST_BY_NORMALSORT = "GetShopListByNormalSort".toLowerCase();
    public final static String GET_SHOP_LIST_BY_HOTSORT = "GetShopListByHotSort".toLowerCase();
    public final static String GET_SHOP_PRODUCT_LIST_BY_HOTSORT = "GetShopProductListByHotSort".toLowerCase();
    public final static String GET_SHOP_PRODUCT_LIST_BY_NORMALSORT = "GetShopProductListByNormalSort".toLowerCase();
    public final static String GET_SHOP_ACTIVITY = "GetShopActivity".toLowerCase();
    public final static String SEARCH_PRODUCT_BY_KEYWORD = "SearchProductByKeyword".toLowerCase();
    public final static String SEARCH_SHOP_BY_KEYWORD = "SearchShopByKeyword".toLowerCase();
    public final static String GET_ORDER = "getOrder".toLowerCase();
    public final static String CONFIRM_ORDER = "ConfirmOrder".toLowerCase();
    public final static String CLOSE_ORDER = "CloseOrder".toLowerCase();
    public final static String GET_SHOPPING_CART = "getShoppingCart".toLowerCase();
    public final static String RECEIVE_RED_ENVELOPE = "ReceiveRedEnvelope".toLowerCase();
    public final static String GET_CONTACT_LIST = "GetContactList".toLowerCase();
    public final static String DEL_CONTACT = "DelContact".toLowerCase();
    public final static String SET_DEFAULT_CONTACT = "SetDefaultContact".toLowerCase();

    public final static String GET_VERIFY_SHOPPING_CART = "getVerifyShoppingCart".toLowerCase();
    public final static String SELECT_CART_ITEM = "selectCartItem".toLowerCase();
    public final static String CANCEL_CART_ITEM = "cancelCartItem".toLowerCase();
    public final static String SELECT_CART_GROUP_ITEM = "SelectCartGroupItem".toLowerCase();
    public final static String CANCEL_CART_GROUP_ITEM = "CancelCartGroupItem".toLowerCase();
    public final static String SELECT_CART_SHOP_ITEM = "SelectCartShopItem".toLowerCase();
    public final static String CANCEL_CART_SHOP_ITEM = "CancelCartShopItem".toLowerCase();


    public final static String SELECT_CART_ALL_ITEM = "SelectCartAllItem".toLowerCase();
    public final static String GET_AREA_LIST = "GetAreaList".toLowerCase();
    public final static String CANCEL_CART_ALL_ITEM = "CancelCartAllItem".toLowerCase();
    public final static String ADD_CONTACT = "AddContact".toLowerCase();
    public final static String EDIT_CONTACT = "EditContact".toLowerCase();
    public final static String UPDATE_CART_QUANTITY = "UpdateCartQuantity".toLowerCase();
    public final static String UPDATE_CART_GROUP_SELL_QUANTITY = "UpdateCartGroupSellQuantity".toLowerCase();
    public final static String SELECT_CART_OTHER_SERVICE = "SelectCartOtherService".toLowerCase();
    public final static String SAVE_CART_FAVORITE_PRODUCT = "SaveCartFavoriteProduct".toLowerCase();
    public final static String SUBMIT_ORDER = "SubmitOrder".toLowerCase();
    public final static String DELETE_FAV_PRODUCT = "DeleteFavProduct".toLowerCase();
    public final static String FAV_PRODUCT = "FavProduct".toLowerCase();

    public final static String DELETE_FAV_SHOP = "DeleteFavShop".toLowerCase();
    public final static String FAV_SHOP = "FavShop".toLowerCase();
    public final static String CANCEL_ORDER = "CancelOrder".toLowerCase();





    public final static String GET_SHOP_VOUCHERS_LIST = "GetShopVouchersList".toLowerCase();
    //9.获取服务详情/api/User/GetProduct/?productId={0}
    public final static String GET_PRODUCT = "GetProduct".toLowerCase();
    public final static String GET_ASSESSMENT_LIST = "GetAssessmentList".toLowerCase();
    public final static String GET_SHOP = "GetShop".toLowerCase();
    public final static String ADD_CART_ITEM = "AddCartItem".toLowerCase();

    public final static String ADD_CART_ITEM_OF_BUY_NOW = "AddCartItemOfBuyNow".toLowerCase();
    public final static String DELETE_CART_SELECTEDITEM = "DeleteCartSelectedItem".toLowerCase();
    public final static String SAVE_GENERAL_INVOICE = "SaveGeneralInvoice".toLowerCase();
    public final static String SAVE_VAT_INVOICE = "SaveVatInvoice".toLowerCase();

    public final static String BIND_EMAIL = "BindEmail".toLowerCase();
    public final static String BIND_MOBILE = "BindMobile".toLowerCase();
    public final static String APPLY_ORDER_REFUND = "ApplyOrderRefund".toLowerCase();
    public final static String UPLOAD_USER_PHOTO = "UploadUserPhoto".toLowerCase();
    public final static String SET_PAY_PWD = "SetPayPwd".toLowerCase();
    public final static String MODIFY_PAY_PWD = "ModifyPayPwd".toLowerCase();


//    public final static String GET_SHOP = "GetShop".toLowerCase();

    //10.获取省份列表
    public final static String GET_PROVINCE_LIST = "GetProvinceList".toLowerCase();
    //11.获取城市列表
    public final static String GET_CITY_LIST_BY_PROVINCE = "GetCityListByProvince".toLowerCase();
    //12.获取地区列表
    public final static String GET_DISTRICT_LIST_BY_CITY = "GetDistrictListByCity".toLowerCase();
    //12.获取我的订单列表
    public final static String GET_ORDER_LIST = "GetOrderList".toLowerCase();
    public final static String ADD_ASSESSMENT = "AddAssessment".toLowerCase();
    //12.获取我的订单数量 添加了pageNo
    public final static String GET_ORDER_COUNT = "GetOrderCount".toLowerCase();
    public final static String GET_ACCESS_TOKEN = "GetUserAccessToken".toLowerCase();
    public final static String GET_ASSESSMENT_COUNT = "GetAssessmentCount".toLowerCase();
    //13.获取我收藏的服务列表 添加了pageNo
    public final static String GET_FAVORITE_PRODUCT_LIST = "GetFavoriteProductList".toLowerCase();
    //13.获取我收藏的服务数量 添加了pageNo
    public final static String GET_FAVORITE_PRODUCT_COUNT = "GetFavoriteProductCount".toLowerCase();
    public final static String GET_USER_MESSAGE_LIST = "GetUserMessageList".toLowerCase();
    //14.获取我收藏的店铺列表 添加了pageNo
    public final static String GET_FAVORITE_SHOP_LIST = "GetFavoriteShopList".toLowerCase();
    //15.获取我收藏的店铺数量 添加了pageNo
    public final static String GET_FAVORITE_SHOP_COUNT = "GetFavoriteShopCount".toLowerCase();
    public final static String SUBMIT_PAYMENT = "SubmitPayment".toLowerCase();
    public final static String QR_CODE_LOGIN = "QRCodeLogin".toLowerCase();

    /*存放在com.android.haobanyi_preferences.xml文件中的KEY值*/
    public final static String ACCESS_TOKEN = "access_token";
    public final static String USERID = "userid";
    public final static String USER_INFO = "user_info";
    public final static String TOTAL_COUNT_PRODUCT = "totalCountProduct";//收藏服务的数量
    public final static String TOTAL_COUNT_SHOP = "totalCountShop";//收藏店铺的数量
    public final static String USER_CURRENT_PWD = "user_current_pwd";//用户当前保存的密码
    public final static String EXPIRES_IN = "expires_in";//过期时间
    public final static String ISLOGIN = "islogin"; //是否登录
    public static final String REGISTTEDPN= "registtedpn"; //登录成功过的账号
    public static final String REGISTTE_BY_PHONE= "registte_by_phone"; //登录成功过的账号
    public static final String IS_MODIFY_PAY_PWD= "isModifyPayPwd"; //修改密码
    public static final String IS_FIRST_MODIFY_PAY_PWD= "isFirstModifyPayPwd"; //修改密码
    public static final String IS_REGISTE_BY_MESSAAGE= "is_registe_by_messaage"; //是短信登录，还是第三方登录



    public final static String START_TIME = "start_time";  //开始时间，用于计算时间差
    //最大缓存过期时间,当前设定为5分钟   后期设置为30分钟：1800000
    public final static Long VALUE_MAX_EXPIRE_IN = 300000l;
    public final static String KEY_EXPIRE_ = "key_expire_";//Constants.,System.currentTimeMillis(),"preferences"
    public final static String time_getproductlistbynormalsort = KEY_EXPIRE_+"gplbns";
    public static final String  PHONE_PATH= "phone_path";

    public final static String CHECK_FOR_RESULT = "check_for_result"; //回传的标识量

    public final static String TIME_TO_TIME = "time_to_time";//时间差

    /*联系人*/
    public final static String CONTACT_NAME = "contactname";
    public final static String CONTACT_ADDRESS = "contactaddress"; //具体地址
    public final static String CONTACT_ADDRESS_ = "contactaddress_"; // 省+市+区
    public final static String CONTACT_MOBILE = "contactmobile";
    public final static String IS_EDIT_CONTACT = "is_edit_contact";//编辑已有联系人和新增新联系人的区分标准
    public final static String IS_ONLY_CHOOSE_CONTACT = "is_only_choose_contact";//从确认订单还是从设置界面中跳转到联系人列表界面的区分标准

    public final static String PROVINCE_ID = "ProvinceID";
    public final static String CITY_ID = "CityID";
    public final static String DISTRICT_ID = "DistrictID";
    public final static String PROVINCE_NAME = "ProvinceName";
    public final static String CITY_NAME = "CityName";
    public final static String DISTRICT_NAME = "DistrictName";
    public final static String CONTACT_MANAGE_ID = "ContactManageID";
    /*发票抬头*/
    public final static String RECEIPT_NAME = "receiptname";
    public final static String INVOICE_TYPE = "invoiceType";//发票类别

    /*发票信息*/
    public final static String COMPANYNAME = "CompanyName";
    public final static String IDENTITYCODE = "IdentityCode";
    public final static String REGISTERADDRESS = "RegisterAddress";
    public final static String BANKNAME = "BankName";
    public final static String BANKACCOUNT = "BankAccount";

    /*绑定手机/邮箱*/
    public final static String EMAIL = "Email";
    public final static String VALIDATECODE = "ValidateCode";
    public final static String VALIDATECODEOFBIND = "ValidateCodeOfBind";
    public final static String MOBILE = "Mobile";
    /*订单位置*/
    public final static String POSITION = "position";

    /*订单*/
    public static final String  ORDER_ID= "order_id";
    public static final String  IS_COME_FROM_MYREFUND= "is_come_from_myrefund";

    /*账户余额*/
    public static final String  BALANCE= "balance";







}
