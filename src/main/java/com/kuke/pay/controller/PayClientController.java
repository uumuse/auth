package com.kuke.pay.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKePayConstants;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.pay.bean.PayBill;
import com.kuke.pay.bean.PayBillMsg;
import com.kuke.pay.bean.PayProPrice;
import com.kuke.pay.mapper.PayBillMapper;
import com.kuke.pay.service.CommentService;
import com.kuke.pay.service.PayBillService;
import com.kuke.pay.util.Constants;
import com.kuke.pay.util.WechatPayUtil;
import com.kuke.pay.util.app.alipay.AlipayCore;
import com.kuke.pay.util.app.alipay.sign.RSA;
import com.kuke.util.MessageFormatUtil;

@Controller
@RequestMapping(value = "/kuke/payment")
public class PayClientController extends BaseController{
	
	private static Logger logger = LoggerFactory.getLogger(PayController.class);
	
	private static String pay_service = String.valueOf(PropertiesHolder.getContextProperty("pay.url"));
	
	@Autowired
	private PayBillService payBillService;
	
	@Autowired
	private PayBillMapper payBillMapper;
	
	@Autowired
	private CommentService commentService;
	
	//常量:android
	private String from_client = "android";
	
	@Autowired
	private UserCenterService userCenterService;
	
	/**
	 * 移动端请求的支付宝的参数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/alipayClientPara")
	public @ResponseBody ResponseMsg alipayPara(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> params = this.getParameterMap(request);
		boolean flag = false;
		String code = "6";
		String msg = "参数生成失败;";
		String codeDesc = "";
		User user = this.getLoginUser();
		if(user != null && user.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
			
			String KeyWord = dealNull(params.get("KeyWord"));//订单号
			//应该有的参数
			String item_id = "";//
			String pay_pro_price_id = "";//项目ID
			String pay_price = "";//支付价格
			String bill_type = "";//支付类型
			String item_name = "";//商品名称
			
			String objectName = "";//商品描述
			
			PayBill payBill = null;
			if(!"".equals(KeyWord)){//订单号不为空
				payBill = payBillService.getPayBillByKeyword(KeyWord);
				
				if(payBill == null){
					code = "9";
					msg = "KeyWord不存在;";
					return new ResponseMsg(flag, code, msg, codeDesc);
				}
				
			}else{
				item_id = dealNull(params.get("item_id"));
				pay_pro_price_id = dealNull(params.get("pay_pro_price_id"));
				pay_price = dealNull(params.get("pay_price"));
				bill_type = dealNull(params.get("bill_type"));
				item_name = dealNull(params.get("item_name"));
				
//				try {
//					item_name = new String(item_name.getBytes("ISO8859-1"),"utf-8");
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
				
				if("".equals(dealNull(params.get("item_id")))){
					code = "1";
					msg = "item_id不能为空;";
				}else if("".equals(dealNull(params.get("pay_pro_price_id")))){
					code = "2";
					msg = "pay_pro_price_id不能为空;";
				}else if("".equals(item_name)){
					code = "3";
					msg = "商品名称不能为空;";
				}else if("".equals(dealNull(params.get("bill_type")))){
					code = "4";
					msg = "bill_type不能为空;";
				}else if("".equals(dealNull(params.get("pay_price")))){
					code = "5";
					msg = "pay_price不能为空;";
				}else if(!KuKePayConstants.checkPay_pro_priceid(pay_pro_price_id)){
					code = "12";
					msg = "pay_pro_price_id不存在";
				}
				if(!"6".equals(code)){//参数不对,返回错误
					return new ResponseMsg(flag, code, msg, codeDesc);
				}
				
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("uid", user.getUid());
				param.put("item_id", item_id);
				param.put("pay_pro_price_id", pay_pro_price_id);
				param.put("pay_price", pay_price);
				param.put("bill_type", bill_type);
				param.put("item_name", item_name);
				param.put("from_client", from_client);
				payBill = payBillService.buildPayBill(param);
				
				KeyWord = payBill.getKeyword();
				
				//记录日志
				payBillService.downLoadLog(request);
			}
			//重新赋值参数
			item_id = dealNull(payBill.getItem_id());
			pay_pro_price_id = dealNull(payBill.getPay_pro_price_id());
			pay_price = dealNull(String.valueOf(payBill.getCost_price()));
			bill_type = dealNull(String.valueOf(payBill.getBill_type()));
			item_name = dealNull(payBill.getItem_name());
			
			PayProPrice payProPrice = payBillMapper.getPayProPriceById(Integer.parseInt(pay_pro_price_id));
			objectName = payProPrice.getChannel_name();//商品描述
			
			Map<String, Object> data = new HashMap<String, Object>();
			
			String notify_url = pay_service + "/kuke/return/alipay";// 通知接收URL
			String return_url = pay_service + "/kuke/notify/alipay"; // 支付完成后跳转返回的网址URL
			
			// 1.构建阿里支付订单参数map
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("partner", Constants.ALIPAY_PARTNER);//"\"" + 
			paramMap.put("seller_id", Constants.ALIPAY_SELLER_ACCOUNT );
			paramMap.put("out_trade_no", KeyWord);
			paramMap.put("subject",  item_name );
			paramMap.put("body",  objectName );
			paramMap.put("total_fee", pay_price );
			paramMap.put("service", Constants.ALIPAY_SERVICE_MOBILE );
			paramMap.put("payment_type", "1" );
			paramMap.put("_input_charset","utf-8" );
			paramMap.put("return_url", notify_url );
			paramMap.put("notify_url", return_url );
			

			// 2.参数map转string
			String paramStr = AlipayCore.createLinkString(paramMap);
			
			// 3.签名
			String signStr = RSA.sign(paramStr, Constants.ALIPAY_RSA_KEY, "utf-8");
			
			// 4.签名URLEncoder编码
			try {
				signStr = URLEncoder.encode(signStr, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			// 5.汇总参数string
			String retrnStr = paramStr + "&sign=" + signStr + "&sign_type=RSA";
			
			
			// 6.插入支付记录
			//防止出现重复记录 添加msg表之前 清空为支付方式的记录
			payBillService.delPayBillMsgByKeyWord(KeyWord);
			//添加支付宝支付记录
			PayBillMsg payBillMsg = new PayBillMsg(2, KeyWord, Double.valueOf(pay_price), new Date(), 1);
			payBillService.insertPayBillMsg(payBillMsg);
			
			
			flag = true;
			code = "8";
			msg = "参数生成成功;";
			data.put("orderStr", retrnStr);
			data.put("payBillKeyword", KeyWord);
			return new ResponseMsg(flag, code, msg, codeDesc, data);
		}else{
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null);
		}
	}
	/**
	 * 利用个人余额支付的接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/clientRemainMoney")
	public @ResponseBody ResponseMsg remain_money(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> params = this.getParameterMap(request);
		boolean flag = false;
		String code = "6";
		String msg = "个人支付失败;";
		String codeDesc = "";
		User user = this.getLoginUser();
		if(user != null && user.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
			
			String KeyWord = dealNull(params.get("KeyWord"));//订单号
			//应该有的参数
			String item_id = "";//
			String pay_pro_price_id = "";//项目ID
			String pay_price = "";//支付价格
			String bill_type = "";//支付类型
			String item_name = "";//商品名称
			
			PayBill payBill = null;
			if(!"".equals(KeyWord)){//订单号不为空
				payBill = payBillService.getPayBillByKeyword(KeyWord);
				
				if(payBill == null){
					code = "9";
					msg = "KeyWord不存在;";
					return new ResponseMsg(flag, code, msg, codeDesc);
				}
				
			}else{
				item_id = dealNull(params.get("item_id"));
				pay_pro_price_id = dealNull(params.get("pay_pro_price_id"));
				pay_price = dealNull(params.get("pay_price"));
				bill_type = dealNull(params.get("bill_type"));
				item_name = dealNull(params.get("item_name"));
				
//				try {
//					item_name = new String(item_name.getBytes("ISO8859-1"),"utf-8");
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
				
				if("".equals(dealNull(params.get("item_id")))){
					code = "1";
					msg = "item_id不能为空;";
				}else if("".equals(dealNull(params.get("pay_pro_price_id")))){
					code = "2";
					msg = "pay_pro_price_id不能为空;";
				}else if("".equals(item_name)){
					code = "3";
					msg = "商品名称不能为空;";
				}else if("".equals(dealNull(params.get("bill_type")))){
					code = "4";
					msg = "bill_type不能为空;";
				}else if("".equals(dealNull(params.get("pay_price"))) && "0".equals(dealNull(params.get("pay_price")))){
					code = "5";
					msg = "pay_price不能为空;";
				}else if(!KuKePayConstants.checkPay_pro_priceid(pay_pro_price_id)){
					code = "12";
					msg = "pay_pro_price_id不存在";
				}
				if(!"6".equals(code)){//参数不对,返回错误
					return new ResponseMsg(flag, code, msg, codeDesc);
				}
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("uid", user.getUid());
				param.put("item_id", item_id);
				param.put("pay_pro_price_id", pay_pro_price_id);
				param.put("pay_price", pay_price);
				param.put("bill_type", bill_type);
				param.put("item_name", item_name);
				param.put("from_client", from_client);
				payBill = payBillService.buildPayBill(param);
				
				KeyWord = payBill.getKeyword();
				
				//记录日志
				payBillService.downLoadLog(request);
			}
			//重新赋值参数
			item_id = dealNull(payBill.getItem_id());
			pay_pro_price_id = dealNull(payBill.getPay_pro_price_id());
			pay_price = dealNull(String.valueOf(payBill.getCost_price()));
			bill_type = dealNull(String.valueOf(payBill.getBill_type()));
			item_name = dealNull(payBill.getItem_name());
			
			Map<String, Object> data = new HashMap<String, Object>();
			if("1".equals(bill_type)){//支付 
				//防止出现重复记录 添加msg表之前 清空为支付方式的记录
				payBillService.delPayBillMsgByKeyWord(KeyWord);
				//添加帐户支付记录
				PayBillMsg payBillMsg = new PayBillMsg(1, KeyWord, Double.valueOf(pay_price), new Date(), 1);
				payBillService.insertPayBillMsg(payBillMsg);
				//查询机构余额是否充足
				Map<String,String > commMap = commentService.getUserMoneyByUid(user.getUid());
				if(commMap != null && (Double.valueOf(commMap.get("remain_money")) - Double.valueOf(pay_price)) >= 0){
					//扣除帐户个人余额
					payBillService.updatePayStatusWithPersonalRemain(user.getUid(), KeyWord, Double.valueOf(pay_price));
					flag = true;
					code = "8";
					msg = "个人支付成功;";
				}else{
					code = "9";
					msg = "个人余额不足;";
				}
				return new ResponseMsg(flag, code, msg, codeDesc, data);
			}else if("0".equals(bill_type)){//充值
				return new ResponseMsg(false, "10", "bill_type=0,为充值", codeDesc);
			}else{
				return new ResponseMsg(false, "11", "bill_type为:"+bill_type, codeDesc);
			}
		}else{
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null);
		}
	}
	/**
	 * 利用机构余额支付的接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/clientOrgMoney")
	public @ResponseBody ResponseMsg org_money(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> params = this.getParameterMap(request);
		User user = this.getLoginUser();
		return payBillService.payByOrgType(request,response, params, user);
	}
	/**
	 * 移动端请求的微信的参数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/wechatClientPara")
	public ResponseMsg wechatClientPara(HttpServletRequest request, HttpServletResponse response){
		
		Map<String, String> params = this.getParameterMap(request);
		boolean flag = false;
		String code = "6";
		String msg = "参数生成失败;";
		String codeDesc = "";
		User user = this.getLoginUser();
		if(user != null && user.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
			
			String downid = KuKePayConstants.AUDIODOWN+","+KuKePayConstants.CATALDOWN+","+KuKePayConstants.MUSICBOOKDOWN;
			
			String KeyWord = dealNull(params.get("KeyWord"));//订单号
			//应该有的参数
			String item_id = "";//
			String pay_pro_price_id = "";//项目ID
			String pay_price = "";//支付价格
			String bill_type = "";//支付类型
			String item_name = "";//商品名称
			
			String objectName = "";//商品描述
			
			PayBill payBill = null;
			if(!"".equals(KeyWord)){//订单号不为空
				payBill = payBillService.getPayBillByKeyword(KeyWord);
				
				if(payBill == null){
					code = "9";
					msg = "KeyWord不存在;";
					return new ResponseMsg(flag, code, msg, codeDesc);
				}
				
			}else{
				item_id = dealNull(params.get("item_id"));
				pay_pro_price_id = dealNull(params.get("pay_pro_price_id"));
				pay_price = dealNull(params.get("pay_price"));
				bill_type = dealNull(params.get("bill_type"));
				item_name = dealNull(params.get("item_name"));
				
				try {
					item_name = new String(item_name.getBytes("ISO8859-1"),"utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				if("".equals(dealNull(params.get("item_id")))){
					code = "1";
					msg = "item_id不能为空;";
				}else if("".equals(dealNull(params.get("pay_pro_price_id")))){
					code = "2";
					msg = "pay_pro_price_id不能为空;";
				}else if("".equals(item_name) && "1".equals(bill_type) &&  downid.indexOf(pay_pro_price_id) < 0){
					code = "3";
					msg = "商品名称不能为空;";
				}else if("".equals(dealNull(params.get("bill_type")))){
					code = "4";
					msg = "bill_type不能为空;";
				}else if("".equals(dealNull(params.get("pay_price")))){
					code = "5";
					msg = "pay_price不能为空;";
				}else if(!KuKePayConstants.checkPay_pro_priceid(pay_pro_price_id)){
					code = "12";
					msg = "pay_pro_price_id不存在";
				}
				if(!"6".equals(code)){//参数不对,返回错误
					return new ResponseMsg(flag, code, msg, codeDesc);
				}
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("uid", user.getUid());
				param.put("item_id", item_id);
				param.put("pay_pro_price_id", pay_pro_price_id);
				param.put("pay_price", pay_price);
				param.put("bill_type", bill_type);
				param.put("item_name", item_name);
				param.put("from_client", from_client);
				payBill = payBillService.buildPayBill(param);
				
				KeyWord = payBill.getKeyword();
				
				//记录日志
				payBillService.downLoadLog(request);
			}
			//重新赋值参数
			item_id = dealNull(payBill.getItem_id());
			pay_pro_price_id = dealNull(payBill.getPay_pro_price_id());
			pay_price = dealNull(String.valueOf(payBill.getCost_price()));
			bill_type = dealNull(String.valueOf(payBill.getBill_type()));
			item_name = dealNull(payBill.getItem_name());
			
			PayProPrice payProPrice = payBillMapper.getPayProPriceById(Integer.parseInt(pay_pro_price_id));
			objectName = payProPrice.getChannel_name();//商品描述
			
			SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();
			
			//获取prepayid
			String prepayid = payBillService.gotoAppWeChatPay(pay_price, KeyWord, objectName);
				
			if (null != prepayid && !"".equals(prepayid)) {
				try {
					//组装参数
					//随机字符串  
					String currTime = WechatPayUtil.getCurrTime();  
					String strTime = currTime.substring(8, currTime.length());  
					String strRandom = WechatPayUtil.buildRandom(4) + "";  
					String noncestr = strTime + strRandom;
					
					packageParams.put("appid", Constants.WECHAT_APP_APPID);  
					packageParams.put("partnerid", Constants.WECHAT_MCH_ID);  
					packageParams.put("prepayid", prepayid);  
					packageParams.put("package","Sign=WXPay");  
					packageParams.put("noncestr", noncestr);  
					packageParams.put("timestamp", String.valueOf(new Date().getTime()).substring(0, 10));  
					String sign = WechatPayUtil.createSign("UTF-8", packageParams,Constants.WECHAT_APP_KEY);  
					packageParams.put("sign", sign); 
					flag = true;
					code = "2";
					msg = "参数生成成功;";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				code = "8";
				msg = "prepayid获取失败;";
			}
			return new ResponseMsg(flag, code, msg, codeDesc, packageParams);
		}else{
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null); 
		}
	}
	/**
	 * 检查空字符串
	 * @param str
	 * @return
	 */
	private String dealNull(String str){
		if(str == null || "".equals(str.trim()) || "null".equals(str.trim())){
			str = "";
		}
		return str.trim();
	}
}
