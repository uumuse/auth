package com.kuke.pay.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.util.PropertiesHolder;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.pay.bean.PayBill;
import com.kuke.pay.bean.PayBillRefund;
import com.kuke.pay.quartz.ApplicationBillState;
import com.kuke.pay.service.PayBillRefundService;
import com.kuke.pay.service.PayBillService;
import com.kuke.pay.util.CheckURL;
import com.kuke.pay.util.Constants;
import com.kuke.pay.util.Sign;
import com.kuke.pay.util.SignatureHelper_return;
import com.kuke.pay.util.WechatPayUtil;
import com.kuke.pay.util.XMLUtil;
import com.kuke.pay.util.alipay.AlipayNotify;
import com.kuke.util.MD5;

/**
 * @author lyf
 *
 */
@Controller
@RequestMapping(value = "/kuke/notify")
public class PayNotifyController extends BaseController{
	
	@Autowired
	private PayBillService payBillService;
	
	@Autowired
	private PayBillRefundService payBillRefundService;
	
	private String pay_service = String.valueOf(PropertiesHolder.getContextProperty("pay.url"));
	/**
	 * 供支付宝调用的通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/alipay")
	public @ResponseBody String alipayNotify(HttpServletRequest request,HttpServletResponse response){
		System.out.println("alipayNotify 进来了。。");
		String partner = Constants.ALIPAY_PARTNER;
		String privateKey = Constants.ALIPAY_PRIVATE_KEY;
		Map parmasMap = getParameterMap(request);
		System.out.println("alipayNotify parmasMap："+parmasMap);
		// 如果您服务器不支持https交互，可以使用http的验证查询地址
		String alipayNotifyURL = "http://notify.alipay.com/trade/notify_query.do?"
				+ "&partner="
				+ partner
				+ "&notify_id="
				+ parmasMap.get("notify_id");
		//获取支付宝ATN返回结果，true是正确的订单信息，false是无效的
		String responseTxt = CheckURL.check(alipayNotifyURL);
		System.out.println("alipayNotify responseTxt："+responseTxt);
		Map<String, String> params = new HashMap<String, String>();
		//获取POST 过来参数设置到新的params中`
		Iterator<String> it = parmasMap.keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			String values =  (String) parmasMap.get(name);
			params.put(name, values);
		}
		String mysign = SignatureHelper_return.sign(params, privateKey);
		boolean flag = mysign.equals(parmasMap.get("sign")) && responseTxt.equals("true");
		if(flag){
			try{
				String keyword = params.get("out_trade_no");//商户订单号
				String trade_no = params.get("trade_no");//支付宝交易流水号
				String trade_status = (String) params.get("trade_status");//交易状态
				if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){//交易成功
					System.out.println("alipayNotify success..");
					PayBill payBill = payBillService.getPayBillByKeyword(keyword);
					if(null != payBill && 1 == payBill.getPay_status()){//待付款状态时处理
						Map<String, String> map = new HashMap<String, String>();
						map.put("keyword", keyword);
						map.put("trade_no", trade_no);
						payBillService.finishPayBill(map);
					}
					return "success";
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("alipayNotify error sign");
		}
		return "fail";
	}
	/**
	 * 供支付宝退款调用的通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/alipayRefund")
	public @ResponseBody String alipayRefund(HttpServletRequest request,HttpServletResponse response, ModelMap modelMap){
		String result = "";
		try {
			//获取支付宝POST过来反馈信息
			Map<String,String> params = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				params.put(name, valueStr);
			}
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//订单号
			String refund_keyword = new String(request.getParameter("batch_no").getBytes("ISO-8859-1"),"UTF-8");
			//批量退款数据中转账成功的笔数
			String success_num = new String(request.getParameter("success_num").getBytes("ISO-8859-1"),"UTF-8");
			//批量退款数据中的详细信息
			String result_details = new String(request.getParameter("result_details").getBytes("ISO-8859-1"),"UTF-8");
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			if(AlipayNotify.verify(params)){//验证成功
				PayBillRefund payBillRefund = payBillRefundService.getRefundBillInfoByID(refund_keyword);//退款详细信息
				if(payBillRefund != null && "0".equals(payBillRefund.getResult())){//未更新result
					if("1".equals(success_num)){//退款成功一比
						payBillRefundService.updateRefundBillInfoByID(refund_keyword, "1");
					}else{
						result = "fail";
					}
				}else{
					result = "success";	//请不要修改或删除
				}
			}else{//验证失败
				result = "fail";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 检查订单是否已支付完成,即状态2
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkBill")
	public @ResponseBody ResponseMsg checkBill(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		String payBillKeyword = dealNull(request.getParameter("payBillKeyword"));//订单号
		String num = dealNull(request.getParameter("num"));//全局秒数
		long inTime = new Date().getTime();
		String stateid = null;
		Boolean bool = true;
		while(bool){//查询五秒后返回
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//检测登录
			stateid = ApplicationBillState.getInstance().StateMapInfo(payBillKeyword);
			System.out.println("stateid:" + stateid);
			if(stateid != null && "2".equals(stateid)){//交易完成
				bool = false;
				ApplicationBillState.getInstance().StateMapOut(payBillKeyword);
				break;
			}else{
				if(new Date().getTime() - inTime > 3000){
					bool = false;
				}
			}
		}
		if(stateid != null && "2".equals(stateid)){
			result.put("flag", "true");
			result.put("stateid", stateid);
		}else{
			result.put("flag", "false");
			result.put("stateid", "");
		}
		System.out.println("checkBill over!stateid:"+stateid);
		System.out.println(num);
		return new ResponseMsg(true, "1", "信息返回成功", "信息返回成功", result);
	}
	/**
	 * 供微信调用的通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/wechat")
	public @ResponseBody String wechatNotify(HttpServletRequest request,HttpServletResponse response){
		System.out.println(" 0 wechatNotify 进来了。。");
		String resXml = "";//返回给微信的参数
		try {
			//1.读取参数
			InputStream inputStream ;
			StringBuffer sb = new StringBuffer();
			inputStream = request.getInputStream();
			String s ;
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((s = in.readLine()) != null){
				sb.append(s);
			}
			in.close();
			inputStream.close();
			System.out.println(" 1 wechatNotify sb.toString()："+sb.toString());
			//2.解析xml成map
			Map<String, String> params = new HashMap<String, String>();
			params = XMLUtil.doXMLParse(sb.toString());
			System.out.println(" 2 wechatNotify params："+params);
			
			//3.过滤空 设置 TreeMap
			SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();		
			Iterator it = params.keySet().iterator();
			while (it.hasNext()) {
				String parameter = (String) it.next();
				String parameterValue = params.get(parameter);
				
				String v = "";
				if(null != parameterValue) {
					v = parameterValue.trim();
				}
				packageParams.put(parameter, v);
			}
			System.out.println(" 3 wechatNotify packageParams："+packageParams);
			//4.账号信息
			String key = Constants.WECHAT_KEY;
			String keyword = dealNull((String)packageParams.get("out_trade_no"));//订单号
			String trade_no = dealNull((String)packageParams.get("transaction_id"));//微信支付订单号
			String total_fee = dealNull((String)packageParams.get("total_fee"));//订单总金额
			//判断签名是否正确
			if( WechatPayUtil.isTenpaySign("UTF-8",packageParams,key)) {
				if("SUCCESS".equals((String)packageParams.get("result_code"))){//支付成功
					PayBill payBill = payBillService.getPayBillByKeyword(keyword);//订单详细信息
					if(payBill != null){
//						if(!"".equals(total_fee) && (Double.parseDouble(total_fee) == payBill.getTotal_price())){//金额是否相同
							if(null != payBill && 1 == payBill.getPay_status()){//待支付
								Map<String, String> map = new HashMap<String, String>();
								map.put("keyword", keyword);
								map.put("trade_no", trade_no);
								payBillService.finishPayBill(map);
							}
							System.out.println("wechatNotify 支付成功。。 ");
							resXml = resultToWX("SUCCESS", "OK");
							ApplicationBillState.getInstance().StateMapIn(keyword, "2");//订单状态加入内存
//						}else{
//							System.out.println("wechatNotify 支付失败,错误信息，金额被纂改 ：" + packageParams.get("err_code")+"-----订单号："+keyword+"*******支付失败时间："  
//				                    +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "total_fee:"+total_fee+"  自己的价格:"+payBill.getTotal_price());  
//							resXml = resultToWX("FAIL", "金额被纂改 "); 
//						}
					}else{
						System.out.println("wechatNotify 支付失败,错误信息，订单号不存在：" + packageParams.get("err_code")+"-----订单号："+keyword+"*******支付失败时间："  
	                    +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));  
						resXml = resultToWX("FAIL", "参数格式校验错误,订单不存在"); 
					}
				}else{
					System.out.println("wechatNotify 支付失败,错误信息：" + packageParams.get("err_code")+"-----订单号："+keyword+"*******支付失败时间："  
		                    +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));  
							resXml = resultToWX("FAIL", "参数格式校验错误");
				}
			}else{
				System.out.println("支付失败,错误信息：通知签名验证失败-----订单号："+keyword+"*******支付失败时间："  
	                    +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));  
						resXml = resultToWX("FAIL", "通知签名验证失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resXml;
	}
	/**
	 * 盛付通调用通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/sdo")
	public String sdoBankNotify(HttpServletRequest request,HttpServletResponse response){
		System.out.println("sdoBankNotify start...");
		Map<String, String> paramsMap = getParameterMap(request);
		System.out.println("sdoBankNotify paramsMap:"+paramsMap);
		try {
			// 获取参数
			String _amount = paramsMap.get("Amount");// 订单金额
			String _payAmount = paramsMap.get("PayAmount");// 实际支付金额
			String _orderNo = paramsMap.get("OrderNo");// 商户订单号
			String _serialNo = paramsMap.get("serialno");// 支付序列号
			String _status = paramsMap.get("Status");// 支付状态
			// "01"表示成功
			String _merchantNo = paramsMap.get("MerchantNo");// 商户号
			String _payChannel = paramsMap.get("PayChannel");// 实际支付渠道
			String _discount = paramsMap.get("Discount");// 实际折扣率
			String _signType = paramsMap.get("SignType");// 签名方式。1-RSA
			// 2-Md5
			String _payTime = paramsMap.get("PayTime");// 支付时间
			String _currencyType = paramsMap.get(
					"CurrencyType");// 货币类型
			String _productNo = paramsMap.get("ProductNo");// 产品编号
			String _productDesc = new String(paramsMap.get(
					"ProductDesc").getBytes("ISO_8859_1"), "UTF-8");// 产品描述
			String _remark1 = new String(paramsMap.get(
					"Remark1").getBytes("ISO_8859_1"), "UTF-8");// 产品备注1
			String _remark2 = new String(paramsMap.get(
					"Remark2").getBytes("ISO_8859_1"), "UTF-8");// 产品备注2
			String _exInfo = new String(paramsMap.get("ExInfo").getBytes("ISO_8859_1"), "UTF-8");// 额外的返回信息
			String _mac = paramsMap.get("MAC");// 签名字符串

			boolean verifyResult = Sign.verifySign(_amount, _payAmount,
					_orderNo, _serialNo, _status, _merchantNo, _payChannel,
					_discount, _signType, _payTime, _currencyType, _productNo,
					_productDesc, _remark1, _remark2, _exInfo, _mac);

			if (verifyResult) {
				// out.print("OK");//签名成功时必须输出"OK"
				System.out.println("sdoBankNotify sign:OK");
				request.setAttribute("sign", "OK");
				if (_status.equals("01")) {
					// 更新成功订单
					// 验签成功,商户的业务逻辑处理...
					// ......
					// 处理成功，向手机支付平台发送接收到后台通知成功的信息；请执行如下：（注：请不要在out.println其他的信息）
					try {
						PayBill payBill = payBillService.getPayBillByKeyword(_orderNo);
						if(null != payBill && 1 == payBill.getPay_status()){//订单不为空，且是待支付状态
							System.out.println("sdoBankNotify 订单存在："+_orderNo);
							Map<String, String> map = new HashMap<String, String>();
							map.put("keyword", _orderNo);
							map.put("trade_no", _serialNo);//外部交易流水号
							payBillService.finishPayBill(map);
							System.out.println("sdoBankNotify 订单处理成功!");
							return "forward:/kuke/payment/getSuccessPay?keyword="+_orderNo;
						}else{
							System.out.println("sdoBankNotify 订单不存在："+_orderNo+" 或状态不为1");
						}
					} catch (Exception ex) {
						System.out.println("sdoBankNotify ex："+ex.getMessage());
						ex.printStackTrace();
					}
				} else {
					// 订单状态不是01
					System.out.println("sdoBankNotify _status："+_status);
				}
			} else {
				System.out.println("sdoBankNotify sign:field");
				System.out.println("验签失败！");//
				request.setAttribute("sign", "field");
			}
		} catch (Exception ex) {
			System.out.println("sdoBankNotify ex："+ex.getMessage());
			ex.printStackTrace();
		}
		return "forward:/kuke/userCenter/userBill";
	}
	/**
	 * 云网调用的通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	public String getPayNotify(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, String> paramsMap = getParameterMap(request);
		
		String c_mid = paramsMap.get("c_mid");
		String c_order = paramsMap.get("c_order");
		String c_orderamount = paramsMap.get("c_orderamount");
		String c_ymd = paramsMap.get("c_ymd");
		String c_transnum = paramsMap.get("c_transnum");
		String c_succmark = paramsMap.get("c_succmark");
		String c_moneytype = paramsMap.get("c_moneytype");
		String c_cause = paramsMap.get("c_cause");
		String c_memo1 = paramsMap.get("c_memo1"); // 参数一为用户returnUrl
		String c_memo2 = paramsMap.get("c_memo2"); // 参数二为用户id
		String c_signstr = paramsMap.get("c_signstr");
		String c_pass = "w7z4gths7a"; // 商户自己的支付密钥
		
		// 对支付通知信息进行MD5加密
		MD5 md5 = new MD5();
		String srcStr = c_mid + c_order + c_orderamount + c_ymd + c_transnum
				+ c_succmark + c_moneytype + c_memo1 + c_memo2 + c_pass;
		String r_signstr = md5.getMD5ofStr(srcStr).toLowerCase();
		// String cncardIP=request.getRemoteAddr();
		
		// 在此可对请求IP地址进行校对。
		
		// 校验商户网站对通知信息的MD5加密的结果和云网支付网关提供的MD5加密结果是否一致
		
		// 在此也可以进行商户号，支付金额等信息进行校对，这由开发者自己来定。
		
		if (!r_signstr.equals(c_signstr)) {
			System.out.print("签名验证失败");
		} else {
			if (c_succmark.equals("Y")) {
				// 支付成功,更新订单为有效状态
				//更新订单...
				
				
				
				// 返回通知结果
				request.setAttribute("sign",
					"<result>1</result><reURL>"+pay_service+"/kuke/supply/cnCardReturn</reURL>");
				// 在此进行数据操作。
			}
		}
		return "";
	}
	public  String getStr(String str){
		if(str == null){
			return "";
		}	
		return str;
	}
	@RequestMapping("/test")
	public String test(HttpServletRequest request,HttpServletResponse response, ModelMap modelMap){
		Map<String, String> map = new HashMap<String, String>();
		map.put("keyword", "20130725182812E8610680F");
		map.put("tradeno", "123456789");
		payBillService.finishPayBill(map);
		return "forward:/payment/success.jsp";
	}
	private String dealNull(String str){
		if(str == null || "null".equals(str)){
			str = "";
		}
		return str.trim();
	}
	/**
	 * 返回给微信的参数
	 * @param status
	 * @param reason
	 * @return
	 */
	private String resultToWX(String status,String reason){
		return  "<xml>" + "<return_code><![CDATA["+status+"]]></return_code>"  
				+ "<return_msg><![CDATA["+reason+"]]></return_msg>" + "</xml> ";
	}
}
