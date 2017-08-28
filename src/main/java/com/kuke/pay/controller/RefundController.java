package com.kuke.pay.controller;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.common.utils.ResponseMsg;
import com.kuke.pay.PayAuth;
import com.kuke.pay.bean.PayBill;
import com.kuke.pay.bean.PayBillRefund;
import com.kuke.pay.mapper.PayBillRefundMapper;
import com.kuke.pay.service.PayBillRefundService;
import com.kuke.pay.util.XMLUtil;
import com.kuke.util.IdGenerator;

@Controller
@RequestMapping(value = "/kuke/payment")
public class RefundController {
	
	private static Logger logger = LoggerFactory.getLogger(RefundController.class);
	
	@Autowired
	private PayBillRefundService payBillRefundService;
	
	@Autowired
	private PayBillRefundMapper payBillRefundMapper;
	
	/**
	 * 向微信服务器发起退款申请
	 * @param request	
	 * @param response
	 * @return
	 */
	@RequestMapping("/wechatApplyRefund")  
	public @ResponseBody String wechatApplyRefund(HttpServletRequest request,HttpServletResponse response){ 
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "FAIL";//是否关闭成功
		String msg = "";//
		try {
			String refund_keyword = dealNull(request.getParameter("refund_keyword"));//退款订单号，不能为空
			PayBillRefund  payBillRefund = payBillRefundService.getRefundBillInfoByID(refund_keyword);
			if(payBillRefund != null && !"".equals(dealNull(payBillRefund.getRefund_keyword()))){
				//商户订单号,商户退款单号,订单金额,退款金额参数不能为空
				if(!"".equals(dealNull(payBillRefund.getKeyword())) && !"".equals(dealNull(payBillRefund.getKeyword())) && !"".equals(dealNull(payBillRefund.getTotal_price())) && !"".equals(dealNull(payBillRefund.getRefund_price()))){
					String resXml = payBillRefundService.wechatApplyRefund(payBillRefund,request);
					Map wxReturnMap = XMLUtil.doXMLParse(resXml);
					//return_code 和 result_code 都为成功时，关单成功
					String return_code = dealNull((String)wxReturnMap.get("return_code"));
					String result_code = dealNull((String)wxReturnMap.get("result_code"));
					if("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)){//查询成功
						result = "SUCCESS";
						msg = "SUCCESS";
					}else{
						msg = dealNull((String)wxReturnMap.get("return_msg")+","+(String)wxReturnMap.get("err_code"));//交易失败原因
						if("".equals(msg)){
							msg = "微信退款申请失败！";
						}
					}
				}else{
					msg = "商户订单号,商户退款单号,订单金额,退款金额参数不能为空";
				}
			}else{
				msg = "退款订单号不存在";
			}
			map.put("result", result);
			map.put("msg", msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject userJSON = JSONObject.fromObject(map);
		return userJSON.toString();
	}
	/**
	 * 点击退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toRefund")
	public @ResponseBody Map<String, Object> toRefund(HttpServletRequest request,HttpServletResponse response){ 
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			//参数:订单号,原因
			String keyword = dealNull(request.getParameter("keyword"));
			String reason = dealNull(request.getParameter("reason"));
			Map<String, Object> result = payBillRefundService.createRefundBill(keyword, reason);
			if("true".equals(result.get("flag"))){
				List<String> refundList = (List<String>) result.get("list");
				String resultHttp = "";
				for(int i = 0; i < refundList.size();i++){
					String refund_keyword = refundList.get(i);
					PayBillRefund payBillRefund = payBillRefundService.getRefundBillInfoByID(refund_keyword);
					if("1".equals(payBillRefund.getPay_detail_id())){//退还到账户
						payBillRefundService.refundToUserACount(payBillRefund.getUser_id(), payBillRefund.getKeyword(), payBillRefund.getRefund_price());
					}else if("2".equals(payBillRefund.getPay_detail_id())){//支付宝
						resultHttp = PayAuth.applyRefund("2", refund_keyword);
					}else if("7".equals(payBillRefund.getPay_detail_id())){//微信
						resultHttp = PayAuth.applyRefund("7", refund_keyword);
					}
				}
				JSONObject json = JSONObject.fromObject(resultHttp);
				if(!"SUCCESS".equals(json.get("result")) || !"SUCCESS".equals(json.get("msg"))){
					map.put("flag", "false");
					map.put("msg", "退款申请失败!");
				}else{
					map.put("flag", "true");
					map.put("msg", "退款申请成功!");
				}
			}else{
				map = result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 从微信服务器查询退款订单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/wechatQueryRefund")  
	public @ResponseBody Map<String, Object> wechatQueryRefund(HttpServletRequest request,HttpServletResponse response){ 
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "FAIL";//是否退款成功
		String msg = "";//
		try {
			String refund_keyword = dealNull(request.getParameter("refund_keyword"));//订单号，不能为空
			String resXml = payBillRefundService.wechatQueryRefund(refund_keyword);
			Map wxReturnMap = XMLUtil.doXMLParse(resXml);
			//return_code 和 result_code 都为成功时，退款成功
			String return_code = dealNull((String)wxReturnMap.get("return_code"));
			String result_code = dealNull((String)wxReturnMap.get("result_code"));
			if("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)){//退款成功
				result = "SUCCESS";
				msg = "SUCCESS";
				PayBillRefund payBillRefund = payBillRefundService.getRefundBillInfoByID(refund_keyword);//退款详细信息
				if(payBillRefund != null && "0".equals(payBillRefund.getResult())){//未更新result
					payBillRefundService.updateRefundBillInfoByID(refund_keyword, "1");
				}
			}else{
				msg = dealNull((String)wxReturnMap.get("err_code"));//交易失败原因
				if("".equals(msg)){
					msg = "微信退单查询失败！";
				}
			}
			map.put("result", result);
			map.put("msg", msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 向支付宝服务器发起无密退款申请
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/alipayApplyRefund")  
	public @ResponseBody String alipayApplyRefund(HttpServletRequest request,HttpServletResponse response){ 
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "FAIL";//是否申请成功
		String msg = "";//
		try {
			String refund_keyword = dealNull(request.getParameter("refund_keyword"));//退款订单号，不能为空
			PayBillRefund  payBillRefund = payBillRefundService.getRefundBillInfoByID(refund_keyword);
			if(payBillRefund != null && !"".equals(dealNull(payBillRefund.getRefund_keyword()))){
				//商户订单号,商户退款单号,订单金额,退款金额参数不能为空
				if(!"".equals(dealNull(payBillRefund.getKeyword())) && !"".equals(dealNull(payBillRefund.getKeyword())) && !"".equals(dealNull(payBillRefund.getTotal_price())) && !"".equals(dealNull(payBillRefund.getRefund_price()))){
					String resXml = payBillRefundService.alipayApplyRefund(payBillRefund,request);
					Map wxReturnMap = XMLUtil.doXMLParse(resXml);
					//is_success为成功时，申请成功
					String is_success = dealNull((String)wxReturnMap.get("is_success"));
					if("T".equals(is_success)){//申请成功
						result = "SUCCESS";
						msg = "SUCCESS";
					}else{
						result = is_success;
						msg = dealNull((String)wxReturnMap.get("error"));//交易失败原因
						if("".equals(msg)){
							msg = "申请退款失败";
						}
					}
				}else{
					msg = "商户订单号,商户退款单号,订单金额,退款金额参数不能为空";
				}
			}else{
				msg = "退款订单号不存在";
			}
			map.put("result", result);
			map.put("msg", msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject userJSON = JSONObject.fromObject(map);
		return userJSON.toString();
	}
	
	@RequestMapping("/testPayBill") 
	public @ResponseBody void test(HttpServletRequest request,HttpServletResponse response){//2017-01-11 10:26:46
		try {
			List<String> KeyWordList = payBillRefundMapper.getAllKeyWord();
			System.out.println("KeyWordList:"+KeyWordList.size());
			log("KeyWordList:"+KeyWordList.size());
			List<PayBill> insertList = new ArrayList<PayBill>();
			for(int i = 2; i <= 111;i++){
				List<PayBill> list = payBillRefundMapper.getTheKeyWord(String.valueOf(i));
				System.out.println("count = "+i+"的大小："+list.size());
				log("count = "+i+"的大小："+list.size());
				for(int j = 0; j < list.size();j++){
					PayBill payBill = list.get(j);
					String keyword = payBill.getKeyword();
					if(KeyWordList.contains(keyword)){
						String temp = keyword.substring(0, 14);
						while(KeyWordList.contains(keyword)){
							temp = addOneSecond(temp);
							keyword = temp + IdGenerator.getUUIDHex32().substring(0, 9);
						} 
						payBill.setKeyword(keyword);
						KeyWordList.add(keyword);
					}
					if(payBill.getCreate_date() == null){
						SimpleDateFormat sm = new SimpleDateFormat("yyyyMMddhhmmss");
						try {
							payBill.setCreate_date(sm.parse(keyword.substring(0, 14)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					insertList.add(payBill);
				}
			}
			log("insert size:"+insertList.size());
			System.out.println("insert size:"+insertList.size());
			int sizes = payBillRefundMapper.insertPayBill(insertList);
			System.out.println("update size:"+sizes);
			log("update size:"+sizes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String dealNull(String str){
		if(str == null || "null".equals(str)){
			str = "";
		}
		return str.trim();
	}
	public static void log(String s)throws Exception{
			//调试信息文件为路径+文件名前缀+YYYYMMDD的日期后缀+.TXT后缀
			PrintWriter out=new PrintWriter(new FileWriter("d://20170206jilu.txt",true),true);
			out.println(s);
			out.close();
	}
	private String addOneSecond(String source) { 
		SimpleDateFormat sm = new SimpleDateFormat("yyyyMMddhhmmss");
	    Calendar calendar = Calendar.getInstance();    
	    try {
			calendar.setTime(sm.parse(source));
			calendar.add(Calendar.SECOND, 1);    
		} catch (ParseException e) {
			e.printStackTrace();
		}    
	    return sm.format(calendar.getTime());    
	} 
	
}
