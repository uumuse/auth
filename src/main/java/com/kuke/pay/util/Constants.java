package com.kuke.pay.util;

import com.kuke.auth.util.PropertiesHolder;

public class Constants {
	
	private static final String PAYMENT_URL =  String.valueOf(PropertiesHolder.getContextProperty("pay.url"));
	
	public static final String PAYMENT_PRICE_URL = PAYMENT_URL + "/kuke/payment/iProPrice";
	
	//积分接口链接
	public static final String POINTS_RULE_URL = PAYMENT_URL + "/kuke/points/rule";
	
	//md5 常量串
	public static final String MD5_STR = "Kuke!2013@AuTH";
	
	/*************************************微信*************************************/
	/**
	 * 微信公众账号ID
	 */
	public static final String WECHAT_APPID = "wx36130539f831fe09";
	/**
	 * app微信公众账号ID
	 */
	public static final String WECHAT_APP_APPID = "wx36130539f831fe09";
	/**
	 * 商户号
	 */
	public static final String WECHAT_MCH_ID = "1390744302";
	/**
	 * key
	 */
	public static final String WECHAT_KEY = "10251826mMts0qt65u3bT28sq4vy5ar9";
	/**
	 * app   key
	 */
	public static final String WECHAT_APP_KEY = "10251826mMts0qt65u3bT28sq4vy5ar9";
	/**
	 * 回调url
	 */
	public static final String WECHAT_NOTIFY_URL = PAYMENT_URL + "/kuke/notify/wechat";
	/**
	 * 发起电脑IP
	 */
	public static final String WECHAT_SPBILL_CREATE_IP = "192.168.0.81";
	/**
	 * 被扫支付API
	 */
	public static final String WECHAT_PAY_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**
	 * 被扫支付查询API
	 */
	public static final String WECHAT_PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";
	/**
	 * 退款API
	 */
	public static String WECHAT_REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	/**
	 * 退款查询API
	 */
	public static String WECHAT_REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";
	/**
	 * 关闭订单
	 */
	public static String WECHAT_PAY_CLOSE_API = "https://api.mch.weixin.qq.com/pay/closeorder";
	/**
	 * 证书存放位置
	 */
	public static String WECHAT_CERTLOCALPATH = "WEB-INF\\cert\\apiclient_cert.p12";
	
	/*************************************支付宝*************************************/
	//支付宝接口请求连接地址
	//public static final String ALIPAY_REQUEST_URL = "https://www.alipay.com/cooperate/gateway.do?";
	public static final String ALIPAY_REQUEST_URL = "https://mapi.alipay.com/gateway.do?";
	//快速支付交易服务
	public static final String ALIPAY_SERVICE = "create_direct_pay_by_user";
	//编码集
	public static final String ALIPAY_CHARSET = "UTF-8";
	//支付宝合作伙伴id
	public static final String ALIPAY_PARTNER = "2088001667023791";
	//NOTIFY_URL
	public static final String ALIPAY_NOTIFY_URL = PAYMENT_URL + "/kuke/notify/alipay";
	//卖家支付宝账户
	public static final String ALIPAY_SELLER_ACCOUNT = "service@kuke.com";
	//partner对应交易安全校验码
	public static final String ALIPAY_PRIVATE_KEY = "w001yn7mkvqh44cuqfjji4gm2uk0pyve";
	//无密退款交易服务
	public static final String ALIPAY_REFUND_SERVICE = "refund_fastpay_by_platform_nopwd";
	//无密退款回调地址
	public static final String ALIPAY_REFUND_NOTIFY_URL = PAYMENT_URL + "/kuke/notify/alipayRefund";
	//日志记录路径,方便测试
//	public static final String ALIPAY_LOG_PATH = "D:\\alipayLOG\\";
	//签名方式
	public static final String ALIPAY_SIGN_TYPE = "MD5";
	
	//快速支付交易APP服务
	public static final String ALIPAY_SERVICE_MOBILE = "mobile.securitypay.pay";
	public static final String ALIPAY_SERVICE_APP = "alipay.trade.app.pay";
	public static final String ALIPAY_APPID = "2016011801103373";
	//RSA private KEY
	public static final String ALIPAY_RSA_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJTL0RfDMgCNQ2Ma"+
												"ZdwpSK1AR6Gx4R8Gv0gjDnk55nAjLAqDQadr8A+UW6u3OXcwJgp6zkW0HMbUBMcT"+
												"D5jS1NQ4jcOv6Qc/vpTIkkSW47NCCmTinEFKLWi/zxJC4BWGw5nDc0D369V3X5Vq"+
												"oIY88M0X5eKYD8CNdZQvQcwxvFutAgMBAAECgYBzAgJwangYgxdl3z6B6Ar7lmaH"+
												"bTTCxXVNrYL/Yihh3l2pFoDFhiE7+YDNfcZWAIo2qWsL50YizDeRI4N3rOXAoOqi"+
												"GCYw6sEhq2YcFmuhOxQgbN1iwgs92TUPJXCCi9vKlCegRfVR6haUG2Xms9gdXMro"+
												"50zqn33HADnNmMsAQQJBAMRuI4agaamNn2FcDBS5Cuch+nuMb3FS2n7i6FZO9y7W"+
												"xhZ+Ma+kEmwGv6dm9yKBVfMr/Ai4pAGkdYhXt5p185ECQQDB65tbMD2kAhE96c4N"+
												"edZTbncKy60edawf01+EXpSlWPiHNZ1d6aFrW0UboX6uAdUWxCa4ytvlXPDLOhU5"+
												"hOBdAkBEqe93NwnYgC4PfXqnn8uE4ibKG1w1c+323uCykuXrFJ6n7DL4H9Vkt7M1"+
												"FFQdVrKxz1VJwylpfZCsOWVv8/1RAkAXQ2JxwAnFARJ8cO3tI+6YN6pEO0bbXpZ1"+
												"gu/0ezwt0cyOu9nTCFH7BQn3VJpT2ZhskL9sb7mNfGbOmnstWUlpAkASME2lOAln"+
												"R/PHGB8/K4Go4YHKqWijjCRVQja0YOtk9YYYKJhB90Qci61PW2S466K9fXb+cBAR"+
												"AwAJ1oWdHsx3";
	//RSA private KEY
	public static final String ALIPAY_RSA_PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	
	//第一次下订单的保存优惠信息
	public static final String ORDER_DISCOUNT = "DISCOUNT";
	//第二次保存优惠信息 在结算订单的时候修改优惠券状态
	public static final String BILL_DISCOUNT = "BILLCOUNT";
}