package com.kuke.pay.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuke.pay.bean.PayBill;
import com.kuke.pay.bean.PayBillMsg;
import com.kuke.pay.bean.PayBillRefund;
import com.kuke.pay.mapper.PayBillMapper;
import com.kuke.pay.mapper.PayBillRefundMapper;
import com.kuke.pay.util.Constants;
import com.kuke.pay.util.CreateOrderNum;
import com.kuke.pay.util.RefundRequest;
import com.kuke.pay.util.WechatPayUtil;
import com.kuke.pay.util.alipay.AlipaySubmit;
import com.kuke.util.HttpClientUtil;

@Service
@Transactional(readOnly=true)
public class PayBillRefundServiceImpl implements PayBillRefundService{

	@Autowired
	private PayBillRefundMapper payBillRefundMapper;
	
	@Autowired
	private PayBillService payBillService;
	
	@Autowired
	private PayBillMapper payBillMapper;
	
	@Autowired
	private CommentService commentService;
	
	@Override
	public PayBillRefund getRefundBillInfoByID(String refund_keyword) {
		return payBillRefundMapper.getRefundBillInfoByID(refund_keyword);
	}
	
	@Override
	public String wechatApplyRefund(PayBillRefund payBillRefund,HttpServletRequest request) {
		String resXml = "";
		try {
			//获得当前目录
	        String path = request.getSession().getServletContext().getRealPath("/");
			
			//随机字符串  
			String currTime = WechatPayUtil.getCurrTime();  
			String strTime = currTime.substring(8, currTime.length());  
			String strRandom = WechatPayUtil.buildRandom(4) + "";  
			String nonce_str = strTime + strRandom;  
			
			//设置参数
			SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();  
			packageParams.put("appid", Constants.WECHAT_APPID);  
			packageParams.put("mch_id", Constants.WECHAT_MCH_ID);  
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("out_trade_no", payBillRefund.getKeyword());
			packageParams.put("out_refund_no", payBillRefund.getRefund_keyword());
			packageParams.put("total_fee",(int) (Double.parseDouble(payBillRefund.getTotal_price())*100));
			packageParams.put("refund_fee",(int) (Double.parseDouble(payBillRefund.getRefund_price())*100));
			packageParams.put("op_user_id", Constants.WECHAT_MCH_ID);//一般为商户号
			String sign = WechatPayUtil.createSign("UTF-8", packageParams,Constants.WECHAT_KEY);  
			packageParams.put("sign", sign);
			
			String requestXML = WechatPayUtil.getRequestXml(packageParams);  
			System.out.println("applyRefund 请求xml："+requestXML);  
			
			RefundRequest refundRequest = new RefundRequest();
			resXml = refundRequest.httpsRequest(Constants.WECHAT_REFUND_API, requestXML, path);  
			System.out.println("applyRefund 返回xml："+resXml); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resXml; 
	}
	@Override
	public String wechatQueryRefund(String refund_keyword) {
		String resXml = "";
		try {
			//随机字符串  
			String currTime = WechatPayUtil.getCurrTime();  
			String strTime = currTime.substring(8, currTime.length());  
			String strRandom = WechatPayUtil.buildRandom(4) + "";  
			String nonce_str = strTime + strRandom;  
			
			//设置参数
			SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();  
			packageParams.put("appid", Constants.WECHAT_APPID);  
			packageParams.put("mch_id", Constants.WECHAT_MCH_ID);  
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("out_trade_no", refund_keyword);  
			String sign = WechatPayUtil.createSign("UTF-8", packageParams,Constants.WECHAT_KEY);  
			packageParams.put("sign", sign);  
			
			String requestXML = WechatPayUtil.getRequestXml(packageParams);  
			System.out.println("queryRefund 请求xml："+requestXML);  
			
			resXml = HttpClientUtil.postData(Constants.WECHAT_PAY_CLOSE_API, requestXML);  
			System.out.println("queryRefund 返回xml："+resXml);  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resXml; 
	}

	@Override
	public String alipayApplyRefund(PayBillRefund payBillRefund,HttpServletRequest request) {
		String resXml = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//把请求参数打包成数组  
	        Map<String, String> sParaTemp = new HashMap<String, String>();  
	        sParaTemp.put("service", "refund_fastpay_by_platform_nopwd");  
	        sParaTemp.put("partner", Constants.ALIPAY_PARTNER);  
	        sParaTemp.put("_input_charset", Constants.ALIPAY_CHARSET);//编码
	        sParaTemp.put("sign_type", Constants.ALIPAY_SIGN_TYPE);//编码
	        sParaTemp.put("notify_url", Constants.ALIPAY_REFUND_NOTIFY_URL);//回调url  
	        sParaTemp.put("batch_no", payBillRefund.getRefund_keyword()); //退款批次号：回调的时候根据此字段修改退款申请的状态 
	        sParaTemp.put("refund_date", sdf.format(payBillRefund.getRefund_date()));//退款请求时间  
	        sParaTemp.put("batch_num", "1");//退款总笔数  
	        sParaTemp.put("detail_data", payBillRefund.getKeyword()+"^"+payBillRefund.getTotal_price()+"^"+payBillRefund.getReason());//  
	          
	        //建立请求  
	        resXml = AlipaySubmit.buildRequest("", "", sParaTemp);
			System.out.println("resXml:"+resXml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resXml; 
	}

	@Override
	public int updateRefundBillInfoByID(String refund_keyword, String result) {
		return payBillRefundMapper.updateRefundBillInfoByID(refund_keyword,result);
	}
	/**
	 * 新建退款订单,返回退款订单号集合
	 * 	退款订单号:result.get("list")
	 * @param keyword : 关联的订单
	 * @param refund_price : 退款金额
	 * @param reason : 退款原因
	 * @return
	 */
	@Override
	public Map<String, Object> createRefundBill(String keyword,String reason) {
		Map<String, Object> result = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			PayBill payBill = payBillService.getPayBillByKeyword(keyword);
			if(payBill != null && "2".equals(payBill.getPay_status())){//交易完成的订单
				List<Map> payTypeList = payBillMapper.getPayBillMsgsByKeyword(keyword);
				double pay_priceMsg = 0;//PayBillMsg中的总单价
				for(int i = 0; i < payTypeList.size();i++){
					Map m = payTypeList.get(i);
					String pay_price =(String) m.get("pay_price");
					pay_priceMsg += Double.parseDouble(pay_price);
				}
				if(pay_priceMsg == payBill.getTotal_price()){//PayBillMsg中的总单价 == payBill中的总单价
					List<String> refund_keywordList = new ArrayList<String>();
					for(int i = 0; i < payTypeList.size();i++){
						Map m = payTypeList.get(i);
						//随机生成退款订单号
						String refund_keyword = new CreateOrderNum().createOrdNum();
						PayBillRefund payBillRefund = new PayBillRefund();
						payBillRefund.setKeyword(keyword);
						payBillRefund.setUser_id(payBill.getUser_id());
						payBillRefund.setReason(reason);
						payBillRefund.setRefund_date(sdf.parse(sdf.format(new Date())));
						payBillRefund.setRefund_keyword(refund_keyword);
						payBillRefund.setRefund_price((String)m.get("pay_price"));
						payBillRefund.setResult("0");
						payBillRefund.setTotal_price(String.valueOf(pay_priceMsg));
						payBillRefund.setTrade_no(payBill.getTrade_no());
						payBillRefund.setPay_detail_id((String)m.get("pay_detail_id"));
						payBillRefundMapper.insertRefundBill(payBillRefund);
						refund_keywordList.add(refund_keyword);
					}
					result.put("flag", "true");
					result.put("list", refund_keywordList);
				}else{
					result.put("flag", "false");
					result.put("msg", "PayBillMsg中的总单价与PayBill中的总单价不一致!");
				}
			}else{
				result.put("flag", "false");
				result.put("msg", "订单未交易完成!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String refundToUserACount(String uid, String keyword, String price) {
		String flag = "fail";
		try {
			Map<String,String > commMap = commentService.getUserMoneyByUid(uid);
			String remain = commMap.get("remain_money");
			double b = Double.valueOf(commMap.get("remain_money")) + Double.valueOf(price);
			//修改帐户余额
			Map<String, String> ummap = payBillMapper.getUserMoneyDB(uid);
			if(ummap == null){
				//防止usermoney表中没该用户数据
				Map iummap = new HashMap();
				iummap.put("id",new CreateOrderNum().createOrdNum());
				iummap.put("user_id", uid);
				iummap.put("remain_money",price);
				iummap.put("last_update",new Date());
				try{
					payBillMapper.insertUserMoney(iummap);
				}catch (Exception e) {
				}
			}else{
				try{
					payBillMapper.updateUserMoneyByUid(uid, b);
					flag = "true";
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
