package com.kuke.pay.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kuke.auth.util.PropertiesHolder;
import com.kuke.core.base.BaseController;
import com.kuke.pay.bean.PayBill;
import com.kuke.pay.service.PayBillService;
import com.kuke.pay.util.CheckURL;
import com.kuke.pay.util.Constants;
import com.kuke.pay.util.Sign;
import com.kuke.pay.util.SignatureHelper_return;
import com.kuke.util.MD5;

/**
 * @author lyf
 * @version 2013-03-12 16:11
 *
 */
@Controller
@RequestMapping("/kuke/return")
public class PayReturnController extends BaseController{
	
	@Autowired
	private PayBillService payBillService;
	
	private String pay_service = String.valueOf(PropertiesHolder.getContextProperty("pay.url"));
	/**
	 * 供支付宝调用的返回地址的Action接口   
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/alipay")
	public String alipayReturn(HttpServletRequest request,HttpServletResponse response){
		System.out.println("alipayReturn 进来了。。");
		String partner = Constants.ALIPAY_PARTNER;
		String parivateKey = Constants.ALIPAY_PRIVATE_KEY;	
		Map paramsMap = getParameterMap(request);
		System.out.println("alipayReturn parmasMap："+paramsMap);
		// 如果您服务器不支持https交互，可以使用http的验证查询地址
		String alipayNotifyURL = "http://notify.alipay.com/trade/notify_query.do?"
			+ "&partner="
			+ partner
			+ "&notify_id="
			+ paramsMap.get("notify_id");
		
		String sign = (String) paramsMap.get("sign");
		//获取支付宝atn返回结果， true是正确的订单信息，false是无效
		String responseTxt = CheckURL.check(alipayNotifyURL);
		System.out.println("alipayReturn responseTxt："+responseTxt);
		Map<String, String> params = new HashMap<String, String>();
		//获取post过来的参数   设置到新的params中
		Iterator<String> it = paramsMap.keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			String values = (String) paramsMap.get(name);
			params.put(name, values);
		}
		// 打印，收到消息比对sign的计算结果和传递来的sign是否匹配
		String mysign = SignatureHelper_return.sign(params, parivateKey);
		String out_trade_no = String.valueOf(paramsMap.get("out_trade_no"));
		if(mysign.equals(sign) && responseTxt.equals("true")){
			System.out.println("alipayReturn SUCCESS");
			return "redirect:/kuke/payment/getSuccessPay?keyword="+out_trade_no;
		}else{
			System.out.println("alipayReturn error sign");
			return "redirect:/kuke/userCenter/userBill";
		}
	}
	
	public String getResturnUser(HttpServletRequest request,HttpServletResponse response){
		Map<String,String> paramsMap = getParameterMap(request);
		String c_mid = paramsMap.get("c_mid");
		String c_order = paramsMap.get("c_order");
		String c_orderamount = paramsMap.get("c_orderamount");
		String c_ymd = paramsMap.get("c_ymd");
		String c_transnum = paramsMap.get("c_transnum");
		String c_succmark = paramsMap.get("c_succmark");
		String c_moneytype = paramsMap.get("c_moneytype");
		String c_cause = paramsMap.get("c_cause");
		String c_memo1 = paramsMap.get("c_memo1");
		String c_memo2 = paramsMap.get("c_memo2");
		String c_signstr = paramsMap.get("c_signstr");
		
		String c_pass = "w7z4gths7a";
		//  对支付通知信息进行MD5加密
		MD5 md5 = new MD5();
		String srcStr = c_mid + c_order + c_orderamount + c_ymd + c_transnum
				+ c_succmark + c_moneytype + c_memo1 + c_memo2 + c_pass;
		String r_signstr = md5.getMD5ofStr(srcStr).toLowerCase();
		
		// String cncardIP=request.getRemoteAddr();
		// 在此可对请求IP地址进行校对。

		// ---校验商户网站对通知信息的MD5加密的结果和云网支付网关提供的MD5加密结果是否一致

		// ---在此也可以进行商户号，支付金额等信息进行校对，这由开发者自己来定。

		if (!r_signstr.equals(c_signstr)) {
			System.out.print("签名验证失败");
		} else {
			if (c_succmark.equals("Y")) {
			}
		}
		request.setAttribute("returnUrl", this.getReturnUrl(c_order));
		
		return "";
	}
	/**
	 * 盛付通支付完成通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/sdo")
	public String sdoBankReturn(HttpServletRequest request,HttpServletResponse response){
		System.out.println("sdoBankReturn start... ");
		Map<String, String> paramsMap = getParameterMap(request);
		System.out.println("sdoBankReturn paramsMap:"+paramsMap);
		try{
//			获取参数
			String _amount = paramsMap.get("Amount");//订单金额
			String _payAmount = paramsMap.get("PayAmount");//实际支付金额
			String _orderNo = paramsMap.get("OrderNo");//商户订单号
			String _serialNo = paramsMap.get("serialno");//支付序列号
			String _status = paramsMap.get("Status");//支付状态 "01"表示成功
			String _merchantNo = paramsMap.get("MerchantNo");//商户号
			String _payChannel = paramsMap.get("PayChannel");//实际支付渠道
			String _discount = paramsMap.get("Discount");//实际折扣率
			String _signType = paramsMap.get("SignType");//签名方式。1-RSA 2-Md5
			String _payTime = paramsMap.get("PayTime");//支付时间
			String _currencyType = paramsMap.get("CurrencyType");//货币类型
			String _productNo = paramsMap.get("ProductNo");//产品编号
			String _productDesc = new String(paramsMap.get("ProductDesc").getBytes("ISO_8859_1"),"GBK");//产品描述
			String _remark1 = new String(paramsMap.get("Remark1").getBytes("ISO_8859_1"),"GBK");//产品备注1
			String _remark2 = new String(paramsMap.get("Remark2").getBytes("ISO_8859_1"),"GBK");//产品备注2
			String _exInfo = new String(paramsMap.get("ExInfo").getBytes("ISO_8859_1"),"GBK");//额外的返回信息
			String _mac = paramsMap.get("MAC");//签名字符串

			boolean verifyResult=Sign.verifySign(_amount,_payAmount,_orderNo,_serialNo,_status
					,_merchantNo,_payChannel,_discount,_signType,_payTime,_currencyType
					,_productNo,_productDesc,_remark1,_remark2,_exInfo,_mac);
			if(verifyResult){
				if(_status.equals("01")){
					// out.print("OK");//签名成功时必须输出"OK"
					System.out.println("sdoBankReturn sign:OK");
					request.setAttribute("sign", "OK");
					if (_status.equals("01")) {
						// 更新成功订单
						// 验签成功,商户的业务逻辑处理...
						// ......
						// 处理成功，向手机支付平台发送接收到后台通知成功的信息；请执行如下：（注：请不要在out.println其他的信息）
						try {
							PayBill payBill = payBillService.getPayBillByKeyword(_orderNo);
							if(null != payBill && 1 == payBill.getPay_status()){//订单不为空，且是待支付状态
								System.out.println("sdoBankReturn 订单存在："+_orderNo);
								Map<String, String> map = new HashMap<String, String>();
								map.put("keyword", _orderNo);
								map.put("trade_no", _serialNo);//外部交易流水号
								payBillService.finishPayBill(map);
								System.out.println("sdoBankReturn 订单处理成功!");
								return "forward:/kuke/payment/getSuccessPay?keyword="+_orderNo;
							}else{
								System.out.println("sdoBankReturn 订单不存在："+_orderNo+" 或状态不为1");
							}
						} catch (Exception ex) {
							System.out.println("sdoBankReturn ex："+ex.getMessage());
							ex.printStackTrace();
						}
					} else {
						// 订单状态不是01
						System.out.println("sdoBankReturn _status："+_status);
					}
				}else{
					System.out.println("sdoBankReturn sign:field");
					System.out.println("验签失败！");//
					request.setAttribute("sign", "field");
				}// 验签成功
			}
		}catch(Exception ex){
			System.out.println("sdoBankReturn ex："+ex.getMessage());
			ex.printStackTrace();
		}
		return "redirect:/kuke/userCenter/userBill";
	}
	public String getReturnUrl(String keyWord){
		String url=pay_service+"/kuke/bill/pay_success?keyword="+keyWord;
		return url;
	}
}
