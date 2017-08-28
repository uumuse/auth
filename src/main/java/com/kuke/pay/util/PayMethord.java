package com.kuke.pay.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.kuke.auth.util.PropertiesHolder;
import com.kuke.pay.sdo.config.Configuration;
import com.kuke.pay.sdo.config.Configuration_1;
import com.kuke.util.HttpClientUtil;

public class PayMethord {
	
	private static String pay_service = String.valueOf(PropertiesHolder.getContextProperty("pay.url"));
	public static void gotoWeChatPay(String payValue,String payBillKeyword, String objectName,HttpServletRequest request){
		try {
			//随机字符串  
			String currTime = WechatPayUtil.getCurrTime();  
			String strTime = currTime.substring(8, currTime.length());  
			String strRandom = WechatPayUtil.buildRandom(4) + "";  
			String nonce_str = strTime + strRandom;  
			//total_fee :分为单位，payValue：元为单位
			int total_fee = (int) (Double.parseDouble(payValue)*100);
			
			// 回调接口   
			//设置过期时间
			String time_start =  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());  
			Calendar ca = Calendar.getInstance();  
			ca.setTime(new Date());  
			ca.add(Calendar.DATE, 1);           
			String time_expire =  new SimpleDateFormat("yyyyMMddHHmmss").format(ca.getTime());  
			SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();  
			packageParams.put("appid", Constants.WECHAT_APPID);  
			packageParams.put("mch_id", Constants.WECHAT_MCH_ID);  
			packageParams.put("nonce_str", nonce_str);  
			packageParams.put("body",objectName);  
			packageParams.put("out_trade_no", payBillKeyword);  
			packageParams.put("total_fee", total_fee);  
			packageParams.put("spbill_create_ip", Constants.WECHAT_SPBILL_CREATE_IP);  
			packageParams.put("notify_url", Constants.WECHAT_NOTIFY_URL);  
			packageParams.put("trade_type", "NATIVE");  
			packageParams.put("time_start", time_start);  
			packageParams.put("time_expire", time_expire);          
			String sign = WechatPayUtil.createSign("UTF-8", packageParams,Constants.WECHAT_KEY);  
			packageParams.put("sign", sign);  
			
			String requestXML = WechatPayUtil.getRequestXml(packageParams);  
			System.out.println("gotoWeChatPay 请求xml："+requestXML);  
			
			String resXml = HttpClientUtil.postData(Constants.WECHAT_PAY_API, requestXML);  
			System.out.println("gotoWeChatPay 返回xml："+resXml); 
			
			Map wxReturnMap = XMLUtil.doXMLParse(resXml);  
			String urlCode = (String) wxReturnMap.get("code_url");   
			request.setAttribute("urlCode", urlCode);//二维码
			request.setAttribute("payBillKeyword", payBillKeyword);//订单号
			request.setAttribute("total_fee", payValue);//订单号
			request.setAttribute("nexturl", "/kuke/payment/userBill");//来源自己定义下一页
			request.setAttribute("preurl", dealNull((String)request.getAttribute("preurl")));//来源自己定义下一页
			request.setAttribute("bill_type", dealNull(request.getParameter("bill_type")));//0充值   1支付
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("resultUrl","payment/wechatPay");
	}
	/**
	 * 支付宝
	 * @param uid
	 * @param payValue
	 * @param payBillKeyword
	 * @param objectName
	 * @param request
	 * @param modelMap
	 * @return
	 */
	public static void gotoAlipay(String uid, String payValue,
			String payBillKeyword, String objectName, HttpServletRequest request) {
		if (null != uid && !uid.equals("")) {
			String pay_gateway = Constants.ALIPAY_REQUEST_URL; // 支付接口
			String service = Constants.ALIPAY_SERVICE;// 快速支付交易服务

			String sign_type = "MD5";
			String input_charset = Constants.ALIPAY_CHARSET;
			String partner = Constants.ALIPAY_PARTNER;// 支付宝合作伙伴id(帐户内提取)
			String key = Constants.ALIPAY_PRIVATE_KEY;// 支付宝安全校验码(帐户内提取)
			String body = objectName;// 商品描述，推荐格式：商品名称(订单编号：订单编号)

			String total_fee = payValue;// 订单总价
			String payment_type = "1";// 支付宝类型 1代表商品购买
			String seller_email = Constants.ALIPAY_SELLER_ACCOUNT;// 卖家支付宝帐户

			String subject = objectName;
			String show_url = "";
			String notify_url = pay_service + "/kuke/notify/alipay";// 通知接收URL  
			String return_url = pay_service + "/kuke/return/alipay"; // 支付完成后跳转返回的网址URL
			//String return_url =  "http://119.253.39.66:1699/kuke/notify/alipay";
			/******************************************************************/
			String paymethod = "bankPay";// 赋值:bankPay(网银);cartoon(卡通)，directPay(余额),creditPay(信用支付)
			// 三种付款方式都要，参数为空
			String enable_paymethod = "bankPay^creditCardExpress^debitCardExpress^directPay";
			String defaultbank = "ICBCB2C";
			// ICBCB2C 中国工商银行
			// CMB 招商银行
			// CCB 中国建设银行
			// ABC 中国农业银行
			// SPDB 上海浦东发展银行
			// SPDBB2B 上海浦东发展银行(B2B)
			// CIB 兴业银行
			// GDB 广东发展银行
			// SDB 深圳发展银行
			// CMBC 中国民生银行
			// COMM 交通银行
			// POSTGC 邮政储蓄银行
			// CITIC 中信银行
			String ItemUrl = Payment.CreateUrl(pay_gateway, service, sign_type,
					payBillKeyword, input_charset, partner, key, show_url,
					body, total_fee, payment_type, seller_email, subject,
					notify_url, return_url, paymethod, defaultbank,enable_paymethod);
			request.setAttribute("pay_gateway", pay_gateway);
			request.setAttribute("service", service);
			request.setAttribute("sign_type", sign_type);
			request.setAttribute("payBillKeyword", payBillKeyword);
			request.setAttribute("input_charset", input_charset);
			request.setAttribute("partner", partner);
			request.setAttribute("key", key);
			request.setAttribute("body", body);
			request.setAttribute("total_fee", total_fee);
			request.setAttribute("payment_type", payment_type);
			request.setAttribute("seller_email", seller_email);
			request.setAttribute("subject", subject);
			request.setAttribute("show_url", show_url);
			request.setAttribute("notify_url", notify_url);
			request.setAttribute("return_url", return_url);
			request.setAttribute("paymethod", paymethod);
			request.setAttribute("defaultbank", defaultbank);
			request.setAttribute("enable_paymethod", enable_paymethod);
			request.setAttribute("ItemUrl", ItemUrl);
		}
		request.setAttribute("resultUrl","payment/alipay");
	}
	/**
	 * 盛付通 直连
	 * @param uid
	 * @param payType
	 * @param payValue
	 * @param out_trade_no
	 * @param subject_name
	 * @param modelMap
	 * @return
	 */
	public static void gotoSDOBank(String uid,String payType,String payValue,String out_trade_no,String subject_name,HttpServletRequest request){
		if (uid != null && !uid.equals("")) {
			String _orderNo = out_trade_no;
			String _amount = payValue;
			String _merchantNo = Configuration.merchantNo;
			String _merchantUserId = uid;
			String _orderTime = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
			String _productNo = subject_name;
			String _productDesc = subject_name;
			String _remark1 = "remark1";
			String _remark2 = "remark2";
			String _bankCode = payType;
			String _productURL = "http://producturl";
			String _mac = Sign.makeSign(_amount, _orderNo, _merchantNo,
					_merchantUserId, _orderTime, _productNo, _productDesc,
					_remark1, _remark2, _bankCode, _productURL);
			request.setAttribute("_orderNo", _orderNo);
			request.setAttribute("_amount", _amount);
			request.setAttribute("_merchantNo", _merchantNo);
			request.setAttribute("_merchantUserId", _merchantUserId);
			request.setAttribute("_orderTime", _orderTime);
			request.setAttribute("_productNo", _productNo);
			request.setAttribute("_productDesc", _productDesc);
			request.setAttribute("_remark1", _remark1);
			request.setAttribute("_remark2", _remark2);
			request.setAttribute("_bankCode", _bankCode);
			request.setAttribute("_productURL", _productURL);
			request.setAttribute("_mac", _mac);
			
			request.setAttribute("paymentGateWayURL", Configuration.paymentGateWayURL);
			request.setAttribute("version", Configuration.version);
			request.setAttribute("merchantNo",Configuration.merchantNo );
			request.setAttribute("payChannel", Configuration.payChannel);
			request.setAttribute("postBackURL", Configuration.postBackURL);
			request.setAttribute("notifyURL", Configuration.notifyURL);
			request.setAttribute("backURL", Configuration.backURL);
			request.setAttribute("currencyType",Configuration.currencyType );
			request.setAttribute("notifyURLType",Configuration.notifyURLType );
			request.setAttribute("signType", Configuration.signType);
			request.setAttribute("defaultChannel", Configuration.defaultChannel);
		} 
		request.setAttribute("resultUrl", "payment/sdobanks");
	}
	/**
	 * 盛付通 非直连
	 * @param uid
	 * @param payValue
	 * @param out_trade_no
	 * @param subject_name
	 * @param modelMap
	 * @return
	 */
	public static void gotoSDO(String uid,String payValue,String out_trade_no,String subject_name,HttpServletRequest request){
		if((null != uid)&&(!"".equals(uid))){
			String _orderNo = out_trade_no;
			String _amount = payValue;
			String _merchantNo = Configuration_1.merchantNo;
			String _merchantUserId = uid;
			String _orderTime = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
			String _productNo = subject_name;
			String _productDesc = subject_name;
			String _remark1 = "remark1";
			String _remark2 = "remark2";
			String _bankCode = Configuration_1.bankCode;
			String _productURL = "http://producturl";
			String _mac = Sign.makeSign_1(_amount, _orderNo, _merchantNo,
						_merchantUserId, _orderTime, _productNo, _productDesc,
						_remark1, _remark2, _bankCode, _productURL);
			request.setAttribute("_orderNo", _orderNo);
			request.setAttribute("_amount", _amount);
			request.setAttribute("_merchantNo", _merchantNo);
			request.setAttribute("_merchantUserId", _merchantUserId);
			request.setAttribute("_orderTime", _orderTime);
			request.setAttribute("_productNo", _productNo);
			request.setAttribute("_productDesc", _productDesc);
			request.setAttribute("_remark1", _remark1);
			request.setAttribute("_remark2", _remark2);
			request.setAttribute("_bankCode", _bankCode);
			request.setAttribute("_productURL", _productURL);
			request.setAttribute("_mac", _mac);
			request.setAttribute("_url", Configuration_1.paymentGateWayURL);
			request.setAttribute("_version", Configuration_1.version);
			request.setAttribute("_merchantNo", Configuration_1.merchantNo);
			request.setAttribute("_payChannel", Configuration_1.payChannel);
			request.setAttribute("_postBackURL", Configuration_1.postBackURL);
			request.setAttribute("_notifyURL", Configuration_1.notifyURL);
			request.setAttribute("_backURL",Configuration_1.backURL);
			request.setAttribute("_currencyType", Configuration_1.currencyType);
			request.setAttribute("_notifyURLType", Configuration_1.notifyURLType);
			request.setAttribute("_signType", Configuration_1.signType);
			request.setAttribute("_defaultChannel", Configuration_1.defaultChannel);
		}
		request.setAttribute("resultUrl", "payment/sdo");
	}
	/**
	 * 检查空字符串
	 * @param str
	 * @return
	 */
	private static String dealNull(String str){
		if(str == null || "".equals(str.trim()) || "null".equals(str.trim())){
			str = "";
		}
		return str.trim();
	}
}
