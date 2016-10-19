package com.android.haobanyi.activity.wxapi;

/**
 * 创建人 : fumin<br>
 * 创建时间 : 2016-10-08上午10:12:59<br>
 * 版本 :	[v1.0]<br>
 * 类描述 : 微信支付所需参数及配置<br>
 */
public class WeChatConstans {
	
	/** 应用从官方网站申请到的合法appId */
    public static final String APP_ID = "wx86d3a2a38a4da3b1";
	/** 微信开放平台和商户约定的密钥 */
    public static final String APP_SECRET = "f7c7b0a5d3a2ce56fbfcb7f505f335b1";
    /** 商家向财付通申请的商家id */
    public static final String PARTNER_ID = "1366068802";
    /** 微信公众平台商户模块和商户约定的密钥 */
    public static final String PARTNER_KEY = "s9686a83x52c36f958nm7fph9pl263kj";
    /** 微信统一下单接口 */
	public static final String WECHAT_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /** 回调接口 */
    public static final String NOTIFY_URL = "http://www.haobanyi.com/Pay/Notify/HbyCommerce-Plugin-Payment-WeiXinPay_Native";

}
