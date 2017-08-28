package com.kuke.pay.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kuke.auth.downloadAuth.bean.OrgDownLog;
import com.kuke.auth.iosReciptAuthPay.util.IosCheck;
import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.play.service.PlayService;
import com.kuke.auth.ssologin.bean.OrgDownIp;
import com.kuke.auth.ssologin.service.UserSSOService;
import com.kuke.auth.userCenter.mapper.UserCenterMapper;
import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKePayConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.engine.ICookie;
import com.kuke.pay.bean.PayBill;
import com.kuke.pay.bean.PayBillMsg;
import com.kuke.pay.bean.PayBillNotifyResult;
import com.kuke.pay.bean.PayProPrice;
import com.kuke.pay.controller.UpdataAuthThread;
import com.kuke.pay.mapper.PayBillMapper;
import com.kuke.pay.util.Constants;
import com.kuke.pay.util.CreateOrderNum;
import com.kuke.pay.util.WechatPayUtil;
import com.kuke.pay.util.XMLUtil;
import com.kuke.pay.util.alipay.AlipaySubmit;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.IdGenerator;
import com.kuke.util.ImageUrlUtil;
import com.kuke.util.MessageFormatUtil;
import com.kuke.util.PlayUtil;
import com.kuke.util.StringUtil;

@Service
public class PayBillServiceImpl  implements PayBillService {
	
	private static Logger logger = LoggerFactory.getLogger(PayBillServiceImpl.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private PayBillMapper payBillMapper;
	@Autowired
	private CommentService commentService;
	@Autowired
	private UserCenterService userCenterService;
	@Autowired
	private UserSSOService userSSOService;
	@Autowired
	private PlayService playService;
	@Autowired
	private UserCenterMapper userCenterMapper;
	
	/**
	 * 根据uid查询状态成功订单中资源id
	 */
	public List<Map<String,String>> getUserBillByUserId(String uid){
		return payBillMapper.getUserBillByUserId(uid);
	}
//	/**
//	 * 微信支付
//	 */
//	@Override
	/**
	 * app 微信支付:获取prepay_id
	 */
	@Override
	public String gotoAppWeChatPay(String payValue,String payBillKeyword, String objectName){
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
			packageParams.put("appid", Constants.WECHAT_APP_APPID);  
			packageParams.put("mch_id", Constants.WECHAT_MCH_ID);  
			packageParams.put("nonce_str", nonce_str);  
			packageParams.put("body",objectName);  
			packageParams.put("out_trade_no", payBillKeyword);  
			packageParams.put("total_fee", total_fee);  
			packageParams.put("spbill_create_ip", Constants.WECHAT_SPBILL_CREATE_IP);  
			packageParams.put("notify_url", Constants.WECHAT_NOTIFY_URL);  
			packageParams.put("trade_type", "APP");  
			packageParams.put("time_start", time_start);  
			packageParams.put("time_expire", time_expire);          
			String sign = WechatPayUtil.createSign("UTF-8", packageParams,Constants.WECHAT_APP_KEY);  
			packageParams.put("sign", sign);  
			
			String requestXML = WechatPayUtil.getRequestXml(packageParams);  
			System.out.println("gotoAppWeChatPay 请求xml："+requestXML);  
			
			String resXml = HttpClientUtil.postData(Constants.WECHAT_PAY_API, requestXML);  
			System.out.println("gotoAppWeChatPay 返回xml："+resXml); 
			
			Map wxReturnMap = XMLUtil.doXMLParse(resXml);  
			String prepay_id = (String) wxReturnMap.get("prepay_id"); 
			return prepay_id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 微信订单查询
	 */
	@Override
	public String gotoWxOrderQuery(String transaction_id, String payBillKeyword){
		String resXml = "";
		try {
			//随机字符串  
			String currTime = WechatPayUtil.getCurrTime();  
			String strTime = currTime.substring(8, currTime.length());  
			String strRandom = WechatPayUtil.buildRandom(4) + "";  
			String nonce_str = strTime + strRandom;  
			
			// 回调接口   
			//设置过期时间
			SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();  
			packageParams.put("appid", Constants.WECHAT_APPID);  
			packageParams.put("mch_id", Constants.WECHAT_MCH_ID);  
			packageParams.put("transaction_id", transaction_id);  
			packageParams.put("out_trade_no", payBillKeyword);  
			packageParams.put("nonce_str", nonce_str);
			String sign = WechatPayUtil.createSign("UTF-8", packageParams,Constants.WECHAT_KEY);  
			packageParams.put("sign", sign);  
			
			String requestXML = WechatPayUtil.getRequestXml(packageParams);  
			System.out.println("gotoWxOrderQuery 请求xml："+requestXML);  
			
			resXml = HttpClientUtil.postData(Constants.WECHAT_PAY_QUERY_API, requestXML);  
			System.out.println("gotoWxOrderQuery 返回xml："+resXml);  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resXml; 
	}
	/**
	 * 支付宝订单查询
	 */
	@Override
	public String gotoAlipayOrderQuery(String transaction_id, String payBillKeyword){
		String resXml = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//把请求参数打包成数组  
	        Map<String, String> sParaTemp = new HashMap<String, String>();  
	        sParaTemp.put("service", "single_trade_query");  
	        sParaTemp.put("partner", Constants.ALIPAY_PARTNER);  
	        sParaTemp.put("_input_charset", Constants.ALIPAY_CHARSET);//编码
	        sParaTemp.put("sign_type", Constants.ALIPAY_SIGN_TYPE);//MD5
	        sParaTemp.put("out_trade_no", payBillKeyword);//商户订单号
	          
	        //建立请求  
	        resXml = AlipaySubmit.buildRequest("", "", sParaTemp);
			System.out.println("resXml:"+resXml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resXml; 
	}
	/**
	 * 关闭微信订单
	 */
	@Override
	public String closeWxOrder(String payBillKeyword){
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
			packageParams.put("out_trade_no", payBillKeyword);  
			packageParams.put("nonce_str", nonce_str);
			String sign = WechatPayUtil.createSign("UTF-8", packageParams,Constants.WECHAT_KEY);  
			packageParams.put("sign", sign);  
			
			String requestXML = WechatPayUtil.getRequestXml(packageParams);  
			System.out.println("closeWxOrder 请求xml："+requestXML);  
			
			resXml = HttpClientUtil.postData(Constants.WECHAT_PAY_CLOSE_API, requestXML);  
			System.out.println("closeWxOrder 返回xml："+resXml);  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resXml; 
	}
	@Override
	public PayBill getPayBillByKeyword(String keyword) {
		return payBillMapper.getPayBillPriceByKeyword(keyword);
	}
	/**
	 * @desc 支付完成结算订单 
	 * @date 2013-7-23 9:50
	 * @author lyf
	 * @param @param keyword
	 * @param @param detailId
	 * @return null
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int finishPayBill(Map<String, String> params) {
		try {
			String keyword = params.get("keyword");
			String trade_no = params.get("trade_no");
			System.out.println("finishPayBill  keyword:"+keyword+" trade_no:"+trade_no);
			//拿到订单详细信息
			PayBill payBill = payBillMapper.getPayBillByKeywordUid(keyword);
			if(payBill == null){
				return -1;
			}
			int billType = payBill.getBill_type();
			String uid = payBill.getUser_id();
			System.out.println("finishPayBill billType:"+billType);
			//支付
			if(billType == 1){//支付
				//拿到订单 支付方式
				List<Map> msgList = payBillMapper.getPayBillMsgsByKeyword(keyword);
				Iterator<Map> it = msgList.iterator();
				while (it.hasNext()) {
					Map map = it.next();
					int detailId = (Integer)map.get("pay_detail_id");
					if(detailId == 1){//判断是否 用帐户个人余额支付
						BigDecimal payPrice = (BigDecimal)map.get("pay_price");
						double pp = payPrice.doubleValue();
						//修改帐户个人余额
						Map userMoney = payBillMapper.getUserMoneyDB(uid);
						double remainMoney = ((BigDecimal)userMoney.get("remain_money")).doubleValue();
						if(remainMoney < payPrice.doubleValue()){
							return -1;
						}
						payBillMapper.updateUserMoneyByUid(uid, Double.valueOf(remainMoney) - payPrice.doubleValue());
					}else if(detailId == 8){//判断是否用帐户机构余额支付
						BigDecimal payPrice = (BigDecimal)map.get("pay_price");
						double pp = payPrice.doubleValue();
						//修改帐户机构余额
						Map userMoney = payBillMapper.getUserMoneyDB(uid);
						double org_money = ((BigDecimal)userMoney.get("org_money")).doubleValue();
						if(org_money < payPrice.doubleValue()){
							return -1;
						}
						payBillMapper.updateUserMoneyByUid(uid, Double.valueOf(org_money) - payPrice.doubleValue());
					}else if(detailId == 9){//判断是否用机构余额支付
						BigDecimal payPrice = (BigDecimal)map.get("pay_price");
						double pp = payPrice.doubleValue();
						User user = userSSOService.getUserByID(uid);
						//修改机构余额
						String orgMoneys = payBillMapper.getOrgMoney(user.getOrg_id());
						double org_money = Double.valueOf(orgMoneys);
						if(org_money < payPrice.doubleValue()){
							return -1;
						}
						payBillMapper.updateOrgMoney(user.getOrg_id(), org_money - payPrice.doubleValue());
					}
				}
				//更新权限 开启了一个线程
				new UpdataAuthThread(payBill).run();
			}else{//充值
				Map userMoney = payBillMapper.getUserMoneyDB(uid);
				if(userMoney == null){
					//防止usermoney表中没该用户数据
					Map iummap = new HashMap();
					iummap.put("id",new CreateOrderNum().createOrdNum());
					iummap.put("user_id", uid);
					iummap.put("remain_money",payBill.getTotal_price());
					iummap.put("org_money","0");
					payBillMapper.insertUserMoney(iummap);
				}else{
					double remainMoney = Double.valueOf(String.valueOf(userMoney.get("remain_money") !=null && !userMoney.get("remain_money").equals("null") && !userMoney.get("remain_money").equals("")?userMoney.get("remain_money"):"0"));
					payBillMapper.updateUserMoneyByUid(uid, Double.valueOf(remainMoney)+payBill.getTotal_price());
				}
			}
			//修改支付状态为2 已支付
			payBillMapper.setBillStatusByKeyword(keyword,new Date(),trade_no);
			payBillMapper.updateBillMsgStatusByKeyword(keyword);
			//添加result信息
			PayBillNotifyResult payBillNotifyResult = new PayBillNotifyResult(keyword, "success", new Date());
			payBillMapper.insertPayBillNotifyResult(payBillNotifyResult);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * 删除 支付方式记录
	 * @param @param keyword
	 * @return 
	 */
	@Override
	public void delPayBillMsgByKeyWord(String keyword) {
		payBillMapper.delPayBillMsg(keyword);
	}
	/**
	 * 增加 支付方式记录
	 * @param @param keyword
	 * @return 
	 */
	@Override
	public void insertPayBillMsg(PayBillMsg payBillMsg) {
		payBillMapper.insertPayBillMsg(payBillMsg);
	}
	/**
	 * 单独使用帐户余额支付
	 * @param uid
	 * @param keyword
	 * @param price :要支付的价钱
	 * @return 
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int updatePayStatusWithPersonalRemain(String uid, String keyword, double price) {
		int reslult = -1;
		try {
			PayBill payBill = payBillMapper.getPayBillByKeywordUid(keyword);
			Map<String,String > commMap = commentService.getUserMoneyByUid(uid);
			if(commMap == null){
				throw new Exception();
			}
			double differ = Double.valueOf(commMap.get("remain_money")) - price;//还剩differ块钱
			if(differ >= 0 && price > 0){
				try{
					payBillMapper.updateUserMoneyByUid(uid, differ);
				}catch (Exception e) {
					e.printStackTrace();
				}
				//修改paybill 
				payBillMapper.setBillStatusByKeyword(keyword,new Date(),"");
				//修改msg表 订单状态
				payBillMapper.updateBillMsgStatusByKeyword(keyword);
				
				reslult = 1;
				
				//更新权限 开启了一个线程
				new UpdataAuthThread(payBill).run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reslult;
	}
	/**
	 * 单独使用帐户机构余额支付
	 * @param @param uid
	 * @param @param keyword
	 * @return 
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int updatePayStatusWithOrgRemain(String uid, String keyword, double price) {
		int reslult = -1;
		try {
			PayBill payBill = payBillMapper.getPayBillByKeywordUid(keyword);//信息比较全的方法
			Map<String,String > commMap = commentService.getUserMoneyByUid(uid);
			if(commMap == null){
				throw new Exception();
			}
			double differ = Double.valueOf(commMap.get("org_money")) - price;//还剩differ块钱
			if(differ >= 0 && price > 0){
				try{
					payBillMapper.updateUserOrg_MoneyByUid(uid, differ);
				}catch (Exception e) {
					e.printStackTrace();
				}
				//修改paybill 
				payBillMapper.setBillStatusByKeyword(keyword,new Date(),"");
				//修改msg表 订单状态
				payBillMapper.updateBillMsgStatusByKeyword(keyword);
				
				reslult = 1;
				
				//更新权限 开启了一个线程
				new UpdataAuthThread(payBill).run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reslult;
	}
	/**
	 * 单独使用机构余额支付
	 * @param @param uid
	 * @param @param keyword
	 * @return 
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int updatePayStatusWithOrgMoney(Map<String, Object> param) throws Exception{
		String downip_id = (String) param.get("downip_id");
		String payType = (String) param.get("payType");
		String orgid = (String) param.get("orgid");
		String productid = (String) param.get("productid");
		Double differ = (Double) param.get("differ");
		int reslult = -1;
		if(differ >= 0){//
			try{
				if(KuKePayConstants.pay_org_ip.equals(payType)){//ip 扣费
					this.updateOrgIPMoney(differ, downip_id);
				}else if(KuKePayConstants.pay_org.equals(payType)){//机构 扣费
					this.updateOrgMoneys(differ, orgid, productid);
				}else if(KuKePayConstants.pay_org_user.equals(payType)){//机构用户 扣费
					this.updateOrgUserMoney(differ, orgid, productid);
			}
				reslult = 1;
			}catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		return reslult;
	}
	/**
	 * 使用帐户个人余额
	 * @param @param uid
	 * @param @param keyword
	 * @return 
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int updatePayStatusWithRemain(String uid, String keyword, double price) {
		int reslult = -1;
		try {
			PayBill payBill = payBillMapper.getPayBillByKeywordUid(keyword);
			Map<String,String > commMap = commentService.getUserMoneyByUid(uid);
			if(commMap == null){
				throw new Exception();
			}
			double differ = Double.valueOf(commMap.get("org_money")) + Double.valueOf(commMap.get("remain_money")) - price;
			if(differ >= 0 && price > 0 && (price > Double.valueOf(commMap.get("org_money")))){
				//更新机构余额
				try{
					payBillMapper.updateUserOrg_MoneyByUid(uid, 0.00);
				}catch (Exception e) {
					e.printStackTrace();
				}
				//更新个人余额
				try{
					payBillMapper.updateUserMoneyByUid(uid, differ);
				}catch (Exception e) {
					e.printStackTrace();
				}
				//修改paybill 
				payBillMapper.setBillStatusByKeyword(keyword,new Date(),"");
				//修改msg表 订单状态
				payBillMapper.updateBillMsgStatusByKeyword(keyword);
				
				reslult = 1;
				
				//更新权限 开启了一个线程
				new UpdataAuthThread(payBill).run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reslult;
	}
	/**
	 * 使用帐户机构余额+机构余额支付(此时机构余额不足+个人余额 >= price)
	 * @param @param uid
	 * @param @param keyword
	 * @return 
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int updatePayStatusWithOrg(String org_id,String uid, String keyword, double price) {
		int reslult = -1;
		try {
			PayBill payBill = payBillMapper.getPayBillByKeywordUid(keyword);
			Map<String,String> commMap = commentService.getUserMoneyByUid(uid);
			String orgMoneys = "".equals(dealNull(commentService.getOrgMoney(org_id)))?"0.0":dealNull(commentService.getOrgMoney(org_id));
			if(commMap == null){
				throw new Exception();
			}
			double differ = Double.valueOf(commMap.get("org_money")) + Double.valueOf(orgMoneys) - price;
			if(differ >= 0 && price > 0 && (price > Double.valueOf(orgMoneys))){//消费价格大于机构总钱数
				//更新机构余额
				try{
					payBillMapper.updateOrgMoney(org_id, 0.00);
				}catch (Exception e) {
					e.printStackTrace();
				}
				//更新账户机构余额
				try{
					payBillMapper.updateUserOrg_MoneyByUid(uid, differ);
				}catch (Exception e) {
					e.printStackTrace();
				}
				//修改paybill 
				payBillMapper.setBillStatusByKeyword(keyword,new Date(),"");
				//修改msg表 订单状态
				payBillMapper.updateBillMsgStatusByKeyword(keyword);
				
				reslult = 1;
				
				//更新权限 开启了一个线程
				new UpdataAuthThread(payBill).run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reslult;
	}
	/**
	 * 使用帐户个人余额+机构余额支付+账户机构余额
	 * @param @param uid
	 * @param @param keyword
	 * @return 
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public int updatePayStatusWithAllMoney(String org_id,String uid, String keyword, double price) {
		int reslult = -1;
		try {
			PayBill payBill = payBillMapper.getPayBillByKeywordUid(keyword);
			Map<String,String > commMap = commentService.getUserMoneyByUid(uid);
			String orgMoneys = "".equals(dealNull(commentService.getOrgMoney(org_id)))?"0.0":dealNull(commentService.getOrgMoney(org_id));
			if(commMap == null){
				throw new Exception();
			}
			double differ =Double.valueOf(orgMoneys) + Double.valueOf(commMap.get("org_money")) + Double.valueOf(commMap.get("remain_money")) - price;
			if(differ >= 0 && price > 0 && (price > (Double.valueOf(orgMoneys) + Double.valueOf(commMap.get("org_money"))))){
				//更新机构余额
				try{
					payBillMapper.updateOrgMoney(org_id, 0.00);
				}catch (Exception e) {
					e.printStackTrace();
				}
				//更新账户机构余额
				try{
					payBillMapper.updateUserOrg_MoneyByUid(uid, 0.00);
				}catch (Exception e) {
					e.printStackTrace();
				}
				//更新账户个人余额
				try{
					payBillMapper.updateUserMoneyByUid(uid, differ);
				}catch (Exception e) {
					e.printStackTrace();
				}
				//修改paybill 
				payBillMapper.setBillStatusByKeyword(keyword,new Date(),"");
				//修改msg表 订单状态
				payBillMapper.updateBillMsgStatusByKeyword(keyword);
				
				reslult = 1;
				
				//更新权限 开启了一个线程
				new UpdataAuthThread(payBill).run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reslult;
	}
	/**
	 * 
	 */
	@Override
	public List getPayProPriceByChannelId(int id) {
		return payBillMapper.getProPriceByChannelId(id);
	}
	/**
	 * 创建订单
	 * @param 用户id
	 * @return 返回订单号
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public PayBill buildPayBill(Map<String, Object> params) {
		PayBill payBill = new PayBill();
		try {
			//随机生成订单号
			String payBill_keyword = new CreateOrderNum().createOrdNum();
			
			//参数
			String uid = dealNull((String)params.get("uid"));//用户ID
			String item_id = dealNull((String)params.get("item_id"));//购买单元的ID
			String pay_pro_price_id = dealNull((String)params.get("pay_pro_price_id"));//支付的项目ID
			String bill_type = dealNull((String)params.get("bill_type"));//0充值，1支付
			String pay_price = dealNull((String)params.get("pay_price"));//0充值价格      ios支付价格
			String item_name = dealNull((String)params.get("item_name"));//购买单元的名称
			String pay_status = dealNull((String)params.get("pay_status"));//订单状态:默认为  1       针对ios4状态:重新检测
			String trade_no = dealNull((String)params.get("trade_no"));//trade_no  针对ios第三方流水号
			String from_client = "".equals(dealNull((String)params.get("from_client")))?"web":dealNull((String)params.get("from_client"));//来源
			
			
			Map<String, Object> itemMap = this.getItemMap(params);
			//去查
			String downid = KuKePayConstants.AUDIODOWN+","+KuKePayConstants.CATALDOWN+","+KuKePayConstants.MUSICBOOKDOWN+","+KuKePayConstants.SPOKENDOWN+","+KuKePayConstants.SPOKENAUDIODOWN;
			
			if(downid.indexOf(pay_pro_price_id) < 0){//不是下载的话,item_name赋值
				item_name = dealNull((String)itemMap.get("item_name"));//购买单元的名称
				item_id = "";
			}
			
			String item_url = dealNull((String)itemMap.get("item_url"));//购买单元的地址
			String item_image = dealNull((String)itemMap.get("item_image"));//购买单元的图片
			//计算价格
			Map<String, Object> priceMap = this.getPayPrice(params);
			
			//添加订单
			payBill.setBill_type(Integer.valueOf(bill_type));//0充值，1支付
			payBill.setCost_price((Double)priceMap.get("cost_price"));
			if("9173A4A0FE5111E6BD7BC530B0B2A5D9".equals(uid)){
				payBill.setCost_price(0.01);//测试
			}else{
				payBill.setTotal_price((Double)priceMap.get("total_price"));
			}
			payBill.setDiscount_price((Double)priceMap.get("discount_price"));
			payBill.setKeyword(payBill_keyword);
			payBill.setLast_date(new Date());
			payBill.setPay_status("".equals(pay_status)?1:Integer.parseInt(pay_status));//待支付
			payBill.setCreate_date(new Date());
			payBill.setUser_id(uid);
			payBill.setItem_id(item_id);//item_id
			payBill.setItem_image(item_image);//item_image
			payBill.setItem_name(item_name);//item_name
			payBill.setPay_num(1);//数量
			payBill.setItem_url(item_url);//地址
			payBill.setFrom_client(from_client);//来源
			payBill.setLast_date(new Date());
			payBill.setPay_pro_price_id(pay_pro_price_id);//pay_pro_price 的主键
			payBill.setTrade_no(trade_no);
			//插入DB
			payBillMapper.insertPayBill(payBill);
		} catch (Exception e) {
			e.printStackTrace();
			payBill.setKeyword("");
		}
		return payBill;
	}
	/**
	 * 得到cost_price,discount_price,total_price
	 * @return
	 */
	@Override
	public Map<String, Object> getPayPrice(Map<String, Object> params){
		Map<String, Object> result = new HashMap<String, Object>();
		Double cost_price = 0.0;
		Double discount_price = 0.0;
		Double total_price = 0.0;
		//参数
		String item_id =(String) params.get("item_id");
		String pay_pro_price_id =(String) params.get("pay_pro_price_id");
		String bill_type = (String) params.get("bill_type");
		String pay_price = (String) params.get("pay_price");
		String from_client = (String) params.get("from_client");
		try {
			if("ios".equals(from_client)){//苹果订单    价格来自客户端
				total_price = Double.valueOf(pay_price);
				cost_price = total_price - discount_price;
			}else{
				if("1".equals(bill_type)){//支付
					//根据ID得到支付项目名称及价格
					PayProPrice payProPrice = getPayProPriceById(Integer.parseInt(pay_pro_price_id));
					//只能写死   乐谱下载 音频下载  专辑下载
					double num = 1;
					if(KuKePayConstants.MUSICBOOKCHANNELID.equals(String.valueOf(payProPrice.getPay_channel_id()))){//乐谱下载
						//查询乐谱的数量
						Map<String, String> map = payBillMapper.getMusicBookObjById(item_id);
						if(map != null){
							num = Double.valueOf(map.get("page"));//按张收费
						}
					}else if(KuKePayConstants.AUDIODOWNCHANNELID.equals(String.valueOf(payProPrice.getPay_channel_id()))){//音频下载
						num = 1;
					}else if(KuKePayConstants.CATALDOWNCHANNELID.equals(String.valueOf(payProPrice.getPay_channel_id()))){//专辑下载,有声读物下载
						Map cdMap = getPayProPriceNoCD(item_id);
						if(cdMap != null && !cdMap.equals("")){
							Object o = cdMap.get("no_cd");
							if( o != null && !o.equals("")){
								//获取专辑下cd数量
								num = Integer.parseInt(o.toString());
							}
						}
					}
					//计算价格
					total_price = payProPrice.getPro_price()*num;
					cost_price = total_price - discount_price;
				}else if("0".equals(bill_type)){//充值以传过来的价格为准
					total_price = Double.valueOf(pay_price);
					cost_price = total_price - discount_price;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("cost_price", cost_price);
		result.put("discount_price", discount_price);
		result.put("total_price", total_price);
		return result;
	}
	/**
	 * 根据item_id得到item_image,item_url
	 * @param params
	 * @return
	 */
	private Map<String, Object> getItemMap(Map<String, Object> params){
		
		Map<String, Object> result = new HashMap<String, Object>();
		//参数
		String item_id =(String) params.get("item_id");
		String pay_pro_price_id =(String) params.get("pay_pro_price_id");
		String bill_type = (String) params.get("bill_type");
		String pay_price = (String) params.get("pay_price");
		
		String item_image = "";
		String item_url = "";
		String item_name = "";
		
		if(KuKePayConstants.CATALDOWN.equals(pay_pro_price_id)){//专辑下载
			item_image = getCataloguebioImageUrl(item_id);
			item_url =KuKeUrlConstants.getCaltlog+item_id;
		}else if(KuKePayConstants.AUDIODOWN.equals(pay_pro_price_id)){//音频下载
			Map<String, Object> map = new HashMap<String, Object>();
			map = userCenterService.getItemcodeByLcode(item_id);
			String itemCode = dealNull((String)map.get("item_code"));
			item_image = getMp3ImageUrl(itemCode);
			item_url =KuKeUrlConstants.getCaltlog+itemCode;
		}else{//包括乐谱下载
			PayProPrice payProPrice = payBillMapper.getPayProPriceById(Integer.parseInt(pay_pro_price_id));
			if(payProPrice != null){//商品图片
				item_image = payProPrice.getPro_image();
			}
			item_url = "";
			
			if(!KuKePayConstants.MUSICBOOKDOWN.equals(pay_pro_price_id)){//乐谱下载的名称为乐谱名称，其他则拼接
				if(KuKePayConstants.KUKEERCHARGE.equals(pay_pro_price_id)){//充值
					item_name = payProPrice.getChannel_name();
				}else{
					item_name = payProPrice.getChannel_name()+":"+payProPrice.getPro_name();
				}
			}
		}
		result.put("item_name", item_name);
		result.put("item_image", item_image);
		result.put("item_url", item_url);
		return result;
	}
	
	// 获取单曲图片地址
	private String getMp3ImageUrl(String itemCode) {//根据单曲ID查询专辑ID
		if(itemCode.length() >= 1){
			return ImageUrlUtil.getItemCodeImage(itemCode);
		}else{
			return "";
		}
	}
	// 获取乐谱图片地址
//	private String getMusicBookImageUrl(String id) {
//		if(id.length() >= 1){
//			return ImageUrlUtil.getMusicBookImageUrl(id);
//		}else{
//			return "";
//		}
//	}
	// 获取专辑图片地址
	private String getCataloguebioImageUrl(String item_code) {
		if(item_code.length() >= 1){
			return ImageUrlUtil.getItemCodeImage(item_code);
		}else{
			return "";
		}
	}
	/**
	 * 根据用户名和订单号 查询订单
	 * @param @param keyword
	 * @param @param uid
	 * @return 
	 */
	@Override
	public PayBill getPayBillByKeyWordUid(String keyword) {
		return payBillMapper.getPayBillByKeywordUid(keyword);
	}
	/**
	 * @param 商品单号 keyword
	 * @return 订单总价
	 */
	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public String getPayBillPriceByKeyword(String keyword) {
		PayBill payBill = payBillMapper.getPayBillPriceByKeyword(keyword);
		//结算后的总价
		double total_price = payBill.getCost_price();
		return String.valueOf(total_price);
	}
	@Override
	public PayProPrice getPayProPriceById(int id) {
		PayProPrice payProPrice = payBillMapper.getPayProPriceById(id);
		return payProPrice;
	}
	@Override
	public Map<String, String> getPayProPriceNoCD(String item_code) {
		return payBillMapper.getPayProPriceNoCD(item_code);
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
	
	@Override
	public boolean getPayStatusByItemId(String itemId,String uid,String lId) {
		List list = payBillMapper.getPayStatusByItemId(itemId,uid,lId);
		if(list!=null&&list.size()>0){
			Map m = (Map) list.get(0);
			Long count = (Long) m.get("count");
			
			if(count>0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	@Override
	public int buildOrgIPDownLog(OrgDownLog orgDownLog) {
		return payBillMapper.buildOrgIPDownLog(orgDownLog);
	}
	/**
	 * 得到机构的开放余额
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> getOrgMoneys(Map<String, String> params) {
		return payBillMapper.getOrgMoneys(params);
	}
	/**
	 * 得到IP的余额
	 * @param params
	 * @return
	 */
	@Override
	public List<OrgDownIp> getOrgIPMoney(Map<String, String> params) {
		return payBillMapper.getOrgIPMoney(params);
	}
	/**
	 * 得到机构账户的共享余额
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> getOrgUserMoney(Map<String, String> params) {
		return payBillMapper.getOrgUserMoney(params);
	}
	
	
	/**
	 * 更新机构IP余额 
	 */
	@Override
	public int updateOrgIPMoney(double money, String id) {
		return payBillMapper.updateOrgIPMoney(money, id);
	}
	/**
	 * 更新机构开放余额 
	 */
	@Override
	public int updateOrgMoneys(double money, String orgid, String productid) {
		return payBillMapper.updateOrgMoneys(money, orgid, productid);
	}
	/**
	 * 更新机构用户分配余额
	 */
	@Override
	public int updateOrgUserMoney(double money, String orgid, String productid) {
		return payBillMapper.updateOrgUserMoney(money, orgid, productid);
	}
	/**
	 * 机构方式支付
	 */
	@Override
	public ResponseMsg payByOrgType(HttpServletRequest request,HttpServletResponse response,Map<String, String> params,User user) {
		logger.info("payByOrgType is start....");
		logger.info("payByOrgType params:"+params);
		logger.info("payByOrgType user:"+user);
		//返回结果
		boolean flag = false;
		String code = "fail";
		String msg = "机构支付失败";
		String url = "";//地址
		String from ="";
		try {
			//参数  
			String item_name = dealNull(request.getParameter("item_name"));///item_name
			String itemcode = dealNull(request.getParameter("itemcode"));///itemcode
			String lcode = dealNull(request.getParameter("lcode"));///lcode
			String pay_pro_price_id = dealNull(request.getParameter("pay_pro_price_id"));///pay_pro_price_id:509 510 511 512 513
			String from_client = dealNull(request.getParameter("from_client"));//客户端
			from = dealNull(request.getParameter("from"));
			String type = dealNull(request.getParameter("type"));
			//产品参数 , 根据pay_pro_price_id求得
			String productid = "";//1:音频  2:有声读物  3视频  4直播 5乐谱
			if(pay_pro_price_id.equals(KuKePayConstants.AUDIODOWN)){//专辑下音频下载
				productid = "1";
			}else if(pay_pro_price_id.equals(KuKePayConstants.CATALDOWN)){//专辑下载
				productid = "1";
			}else if(pay_pro_price_id.equals(KuKePayConstants.MUSICBOOKDOWN)){//乐谱下载
				productid = "1";
			}else if(pay_pro_price_id.equals(KuKePayConstants.SPOKENDOWN)){//有声读物下载
				productid = "1";
			}else if(pay_pro_price_id.equals(KuKePayConstants.SPOKENAUDIODOWN)){//有声读物下音频下载
				productid = "1";
			}
			params.put("productid", productid);
			params.put("type", type);
			//参数校验
			if("".equals(item_name)){
				return new ResponseMsg(false, "ERRORPARAM", "name参数错误", "", null);
			}else if("".equals(itemcode)){
				return new ResponseMsg(false, "ERRORPARAM", "id参数错误", "", null);
			}else if("".equals(pay_pro_price_id) || !KuKePayConstants.checkPay_pro_priceid(pay_pro_price_id)){
				return new ResponseMsg(false, "ERRORPARAM", "priceid参数错误", "", null);
			}
			
			//调权限  获取支付方式
			String post_url = KuKeUrlConstants.payTypeAuth_URL;//kuke/play/getAuthDownloadUrl
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			
			String ip = getIp(request);
			logger.info("payByOrgType  获得ip：====================>"+ip);
			//ip = "106.2.204.210";
			logger.info("payByOrgType itemcode:"+itemcode+" ip:"+ip+" product_id:1"+" pay_pro_price_id:"+pay_pro_price_id+" ssoid:"+((user == null?"":user.getSsoid())));
			nvps.add(new BasicNameValuePair("itemcode",itemcode));
			nvps.add(new BasicNameValuePair("lcode",lcode));
			nvps.add(new BasicNameValuePair("downip",ip));
			nvps.add(new BasicNameValuePair("product_id","1"));
			nvps.add(new BasicNameValuePair("pay_pro_price_id",pay_pro_price_id));
			nvps.add(new BasicNameValuePair("type",type));
			nvps.add(new BasicNameValuePair("ssoid",user!=null?user.getSsoid():""));
			nvps.add(new BasicNameValuePair("from",from));
			nvps.add(new BasicNameValuePair("f","c"));
			logger.info("payByOrgType----------------getAuthDownloadUrl"+nvps.toString());
			String result = HttpClientUtil.executeServicePOST(post_url, nvps).toString();
			logger.info("payByOrgType result:"+result);
			ResponseMsg resultMsg = new ResponseMsg(result);
			
			code = resultMsg.getCode();
			if(from!=null&&from.equals("m")){
				if(code!=null&&("14,15").indexOf(code)>=0){
					return resultMsg;
				}
			}
			if(code!=null&&("ORG_ALREADY_PAID,CLIENT_ALREADY_PAID").indexOf(code)>=0){
				return resultMsg;
			}
			
			String temp_id = "";
			if(user!=null&&!user.getUid().equals("")){
				temp_id = user.getUid();
			}else{
				//查询机构下载log(ip下载),根据临时id查询机构订单如果在48小时内有效
				temp_id = ICookie.get(request, KuKeAuthConstants.ORG_DOWN_REC);
			}
			
			if(KuKeAuthConstants.NOMALLOGIN.equals(resultMsg.getCode())){
				return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null);//未登录
			}
			JSONObject json = JSONObject.fromObject(resultMsg.getData().toString());
			if(json.isNullObject() || json.isEmpty()){
				code = resultMsg.getCode();
				return new ResponseMsg(false, code, resultMsg.getMsg()+" 未匹配到机构支付", "");
			}
			String payType = dealNull(json.getString("payType"));
			String orgid = dealNull(json.getString("orgid"));
			String org_down_ip_id = dealNull(json.getString("org_down_ip_id"));
			if("".equals(payType) || "10,9,8".indexOf(payType) < 0){
				return new ResponseMsg(false, "未匹配到机构支付");
			}
			if("10".equals(payType)){//扣费方式:8机构账户分配余额  9机构分配余额  10机构IP分配余额
				if("".equals(orgid)){
					return new ResponseMsg(false, "IP支付时未查到IP所在机构");
				}
				if("".equals(org_down_ip_id)){
					return new ResponseMsg(false, "IP支付时未查到IP信息");
				}
			}
			logger.info("payByOrgType payType:"+payType+", orgid:"+orgid+", org_down_ip_id:"+org_down_ip_id);
			
			//更新账户所需的参数
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("payType",payType);
			p.put("productid",productid);
			
			
			//IP下载
			String temp_down_id = temp_id==null?IdGenerator.getUUIDHex32():temp_id;
			if(KuKePayConstants.pay_org_ip.equals(payType)){
				
				params.put("ip",ip);//查询参数
				p.put("orgid",orgid);//扣款参数
				
				List<OrgDownIp> list = getOrgIPMoney(params);
				logger.info("payByOrgType list:"+list.size());
				if(list != null && list.size() == 1){
					OrgDownIp orgDownIp = list.get(0);
					logger.info("payByOrgType orgDownIp:"+orgDownIp);
					if(orgDownIp.getId() == Integer.parseInt(org_down_ip_id)){
						double cost_price = 0.0;
						if(("511,513").indexOf(pay_pro_price_id)>=0){
							cost_price = this.getDownItemPrice(pay_pro_price_id, lcode);
						}
						if(("509,510,512").indexOf(pay_pro_price_id)>=0){
							cost_price = this.getDownItemPrice(pay_pro_price_id, itemcode);
						}
						
						logger.info("payByOrgType cost_price:"+cost_price);
						if(cost_price <= orgDownIp.getMoney()){//钱数足够
							//建单
							//加入临时cookieId
							Map<String, String> desc =  new HashMap<String, String>();
							if(("511,513").indexOf(pay_pro_price_id)>=0){
								desc = getDownLoadItemInfo(pay_pro_price_id, lcode);
							}
							if(("509,510,512").indexOf(pay_pro_price_id)>=0){
								desc = getDownLoadItemInfo(pay_pro_price_id, itemcode);
							}
									
							OrgDownLog orgDownLog = new OrgDownLog();
							orgDownLog.setType(pay_pro_price_id);//509 乐谱 510专辑 511 单曲 512有声读物
							orgDownLog.setOrg_id(orgid);
							orgDownLog.setPrice(cost_price);
							orgDownLog.setPay_detail_id(payType);
							
							if(("511,513").indexOf(pay_pro_price_id)>=0){
								orgDownLog.setDown_id(lcode);
							}
							if(("509,510,512").indexOf(pay_pro_price_id)>=0){
								orgDownLog.setDown_id(itemcode);
							}
							
							
							orgDownLog.setAccount_name(user == null?"":user.getUid());
							orgDownLog.setIp(ip);
							orgDownLog.setCname(desc.get("item_cname"));
							orgDownLog.setEname(desc.get("item_name"));
							orgDownLog.setImage(desc.get("item_image"));
							orgDownLog.setFrom_client(from_client);
							orgDownLog.setIp_acc_id(Integer.parseInt(org_down_ip_id));
							orgDownLog.setAccount_type("2");
							orgDownLog.setTemp_down_id(temp_down_id);
							buildOrgIPDownLog(orgDownLog);
//							buildOrgIPDownLog(pay_pro_price_id,orgid,Double.toString(cost_price),payType,item_id,user == null?"":user.getUid(),
//									ip,desc.get("item_cname"),desc.get("item_name"),desc.get("item_image"),
//									from_client,(org_down_ip_id),"2",temp_down_id);
							
							//记录日志
							downLoadLog(request);
                            
							//扣款
							p.put("downip_id",org_down_ip_id);//扣款参数
							p.put("differ",orgDownIp.getMoney()-cost_price);//扣款参数
							int count = updatePayStatusWithOrgMoney(p);
							
							//获取url
							if(count == 1){//扣款成功
								logger.info("扣款成功  temp_down_id："+temp_down_id);
								ICookie.set(response, KuKeAuthConstants.ORG_DOWN_REC,temp_down_id, 2*24*3600,"/", ".kuke.com");
								if(("511,513").indexOf(pay_pro_price_id)>=0){
									url = getDownLoadItemUrl(pay_pro_price_id, lcode);
								}else{
									url = getDownLoadItemUrl(pay_pro_price_id, itemcode);
								}
								
								logger.info("payByOrgType url:"+url);
							}
							flag = true;
							code = "PAYSUCCESS";
							msg = "支付成功";
						}else{
							code = "NOMONEY";
							msg = "余额不足";
						}
					}else{
						code = "ORG_DOWN_IP_IDError";
						msg = "ORG_DOWN_IP_ID:"+org_down_ip_id+"与IP:"+ip+"不对应";
					}
				}else{
					code = "IPError";
					msg = "IP:"+ip+"对应的账户异常";
				}
			}else{
				if(user != null){
					logger.info("payByOrgType user:"+user);
					
					params.put("orgid", user.getOrg_id());//查询的参数
					params.put("userid", user.getUid());//扣款的参数
					p.put("orgid", user.getOrg_id());//扣款的参数
					
					//2.求账户余额
					double money = 0.00;//账户的余额
					Map<String, Object> temp = new HashMap<String, Object>();
					
					if(KuKePayConstants.pay_org.equals(payType)){//机构开放余额
						temp = getOrgMoneys(params);
					}else if(KuKePayConstants.pay_org_user.equals(payType)){//机构账户分配余额
						temp = getOrgUserMoney(params);
					}
					money = temp == null?0.00:(Double) temp.get("money");//账户余额
					double itemMoney = getDownItemPrice(pay_pro_price_id, itemcode);//商品价格
					if(money < itemMoney){
						return new ResponseMsg(false, "13", "个人余额足够", "10:机构IP余额足够,11:机构开放余额足够，12:机构指定账号余额足够,13:个人余额足够");
					}
					
					//3.余额足够,生成log
					Map<String, String> desc = new HashMap<String, String>();
					if(("511,513").indexOf(pay_pro_price_id)>=0){
						desc =  getDownLoadItemInfo(pay_pro_price_id, lcode);
					}else{
						desc =  getDownLoadItemInfo(pay_pro_price_id, itemcode);
					}
					OrgDownLog orgDownLog = new OrgDownLog();
					orgDownLog.setOrg_id(user.getOrg_id());
					orgDownLog.setType(pay_pro_price_id);//509 乐谱 510专辑 511 单曲 512有声读物
					orgDownLog.setPrice(itemMoney);//价格
					orgDownLog.setPay_detail_id(payType);
					if(("511,513").indexOf(pay_pro_price_id)>=0){
						orgDownLog.setDown_id(lcode);
					}else{
						orgDownLog.setDown_id(itemcode);
					}
					
					orgDownLog.setAccount_name(user == null?"":user.getUid());
					orgDownLog.setIp(ip);
					orgDownLog.setCname(desc.get("item_cname"));
					orgDownLog.setEname(desc.get("item_name"));
					orgDownLog.setImage(desc.get("item_image"));
					orgDownLog.setFrom_client(from_client);
					orgDownLog.setAccount_type("1");
					orgDownLog.setTemp_down_id(temp_down_id);
					buildOrgIPDownLog(orgDownLog);
//					buildOrgIPDownLog(user.getOrg_id(),pay_pro_price_id,Double.toString(itemMoney),payType,item_id,user == null?"":user.getUid(),
//							ip,desc.get("item_cname"),desc.get("item_name"),desc.get("item_image"),from_client,"1","0",temp_down_id);
					
					//记录日志
					downLoadLog(request);
					
					//4.扣款
					try {
						//4.1扣除机构中的余额
						p.put("downip_id",org_down_ip_id);
						p.put("differ",money - itemMoney);
						int count = updatePayStatusWithOrgMoney(p);
						
						//5.获取url
						if(count == 1){//扣款成功
							if(("511,513").indexOf(pay_pro_price_id)>=0){
								url = getDownLoadItemUrl(pay_pro_price_id, lcode);
							}else{
								url = getDownLoadItemUrl(pay_pro_price_id, itemcode);
							}
//							url = getDownLoadItemUrl(pay_pro_price_id, lcode);
							ICookie.set(response, KuKeAuthConstants.ORG_DOWN_REC,temp_down_id==null?IdGenerator.getUUIDHex32():temp_down_id, 2*24*3600,"/", ".kuke.com");
							logger.info("payByOrgType url:"+url);
						}
						if(from!=null&&from.equals("m")){
							return new ResponseMsg(true, "PAYSUCCESS", "支付成功", "", "");
						}else{
							return new ResponseMsg(true, "PAYSUCCESS", "支付成功", "", url);
						}
					} catch (Exception e) {
						e.printStackTrace();
						return new ResponseMsg(false, "PAYERROR", "支付失败", "", null);
					}
				}else{
					return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null);//未登录
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
			logger.info("payByOrgType Exception:"+e.getMessage());
		}
		return new ResponseMsg(flag, code, msg, "",url);
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public static Map getParameterMap(HttpServletRequest request) {
		// 参数Map
		Map properties = request.getParameterMap();
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}
	
	
	/**
	 * 获得下载item的url
	 * @return
	 */
	private String getDownLoadItemUrlForM(String pay_pro_price_id,String item_id,String from){
		String url = "";
		try {
			if(KuKePayConstants.MUSICBOOKDOWN.equals(pay_pro_price_id)){//乐谱下载
				Map<String,String> book = playService.getMusicBookById(item_id);
				String music_book_file = book.get("music_book_file"); 
				logger.info("payByOrgType music_book_file:"+music_book_file);
				
				Map<String,String> map = new HashMap<String,String>();
				url = PlayUtil.initMusicBookPlayUrl(music_book_file);
			}else if(KuKePayConstants.AUDIODOWN.equals(pay_pro_price_id) || KuKePayConstants.SPOKENAUDIODOWN.equals(pay_pro_price_id)){//单曲下载
				String post_url = KuKeUrlConstants.getPlayUrl;//   /kuke/play/getPlayUrl
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("type", "1"));
				nvps.add(new BasicNameValuePair("lcode", item_id));
				nvps.add(new BasicNameValuePair("from", from));
				logger.info("payByOrgType getDownLoadItemUrl type:1 lcode:"+item_id);
				String result = HttpClientUtil.executeServicePOST(post_url, nvps);
				logger.info("payByOrgType getDownLoadItemUrl result:"+result);
				ResponseMsg res = new ResponseMsg(result);
				if(res.getFlag()){
					JSONObject json = JSONObject.fromObject(res.getData().toString());
					url = json.getString("url");
				}
			}else{
				logger.info("payByOrgType getDownLoadItemUrl pay_pro_price_id:"+pay_pro_price_id+" 不支持");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
	
	/**
	 * 获得下载item的url
	 * @return
	 */
	private String getDownLoadItemUrl(String pay_pro_price_id,String item_id){
		String url = "";
		try {
			if(KuKePayConstants.MUSICBOOKDOWN.equals(pay_pro_price_id)){//乐谱下载
				Map<String,String> book = playService.getMusicBookById(item_id);
				String music_book_file = book.get("music_book_file"); 
				logger.info("payByOrgType music_book_file:"+music_book_file);
				
				Map<String,String> map = new HashMap<String,String>();
				url = PlayUtil.initMusicBookPlayUrl(music_book_file);
			}else if(KuKePayConstants.AUDIODOWN.equals(pay_pro_price_id) || KuKePayConstants.SPOKENAUDIODOWN.equals(pay_pro_price_id)){//单曲下载
				String post_url = KuKeUrlConstants.getPlayUrl;//   /kuke/play/getPlayUrl
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("type", "1"));
				nvps.add(new BasicNameValuePair("lcode", item_id));
				logger.info("payByOrgType getDownLoadItemUrl type:1 lcode:"+item_id);
				String result = HttpClientUtil.executeServicePOST(post_url, nvps);
				logger.info("payByOrgType getDownLoadItemUrl result:"+result);
				ResponseMsg res = new ResponseMsg(result);
				if(res.getFlag()){
					JSONObject json = JSONObject.fromObject(res.getData().toString());
					url = json.getString("url");
				}
			}else{
				logger.info("payByOrgType getDownLoadItemUrl pay_pro_price_id:"+pay_pro_price_id+" 不支持");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
	/**
	 * 得到下载所需要的钱
	 * @param pro_price_id
	 * @param org_id
	 * @return
	 */
	private double getDownItemPrice(String pay_pro_price_id,String item_id){
		double result = 0.00;
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			//参数  type , item_id
			param.put("pay_pro_price_id", pay_pro_price_id);
			param.put("item_id", dealNull(item_id));
			param.put("bill_type",KuKePayConstants.PAYTOBUY);
			//计算价格
			Map<String, Object> map = getPayPrice(param);
			result = (Double) map.get("cost_price");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 获得下载item的url
	 * @return
	 */
	private Map<String, String> getDownLoadItemInfo(String pay_pro_price_id,String item_id){
		Map<String, String> result = new HashMap<String, String>();
		String item_image = "";
		String item_cname = "";
		String item_name = "";
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if(KuKePayConstants.CATALDOWN.equals(pay_pro_price_id) || KuKePayConstants.SPOKENDOWN.equals(pay_pro_price_id)){//专辑下载,有声读物
				item_image = ImageUrlUtil.getItemCodeImage(item_id);
				map = userCenterMapper.getItemcode(item_id);
				item_cname = dealNull(map==null?"":(String) map.get("ctitle"));
				item_name = dealNull(map==null?"":(String) map.get("title"));
			}else if(KuKePayConstants.AUDIODOWN.equals(pay_pro_price_id) || KuKePayConstants.SPOKENAUDIODOWN.equals(pay_pro_price_id)){//音频下载
				map = userCenterMapper.getItemcode(item_id);
				String itemCode = dealNull(map==null?"":(String)map.get("item_code"));
				item_image = dealNull(ImageUrlUtil.getItemCodeImage(itemCode));
				item_cname = dealNull(map==null?"":(String)map.get("ctitle"));
				item_name = "".equals(dealNull(map==null?"":(String)map.get("title")))?dealNull(map==null?"":(String)map.get("track_desc")):dealNull(map==null?"":(String)map.get("title"));
			}else if(KuKePayConstants.MUSICBOOKDOWN.equals(pay_pro_price_id)){//包括乐谱下载
				map = userCenterMapper.getMusicBook(item_id);
				String music_book_file = map==null?"":(String)map.get("music_book_file");
				item_image = dealNull(ImageUrlUtil.getMusicBookImage(music_book_file));
				item_cname = dealNull(map==null?"":(String) map.get("ctitle"));
				item_name = dealNull(map==null?"":(String) map.get("etitle"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("item_image",item_image);
		result.put("item_cname",item_cname);
		result.put("item_name",item_name);
		return result;
	}
	@Override
	public List<Map<String, String>> getUserBillCheck(String uid,
			String item_id, String item_code, String f) {
		List<Map<String, String>> list = payBillMapper.getUserBillCheck(uid, item_id, item_code, f);
		return list;
	}
	
	private String getIp(HttpServletRequest request) {
		String ip=request.getHeader("x-forwarded-for");
 		if(ip==null){
			ip=request.getRemoteAddr();
		}
 		String ips[]=ip.split(",");
 		return ips[0];
	}
	@Override
	public List<Map<String, String>> getPayBillByTempId(String user_id,
			String item_id, String item_code, String f) {
		List<Map<String, String>> list = payBillMapper.getPayBillByTempId(user_id, item_id, item_code, f);
		return list;
	}
	@Override
	public ResponseMsg payByIOS(HttpServletRequest request,HttpServletResponse response,User user,String status) {
		String receipt = StringUtil.dealNull(request.getParameter("receipt"));
		String from = request.getParameter("from");
		boolean flag = false;
		String msg = "";
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> params = this.getParameterMap(request);
		System.out.println("params:"+params);
		try {
			//1.建单
			if(user != null && user.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
				String KeyWord = dealNull(params.get("KeyWord"));//订单号
				//应该有的参数
				String item_id = "";//
				String pay_pro_price_id = "";//项目ID
				String pay_price = "";//支付价格
				String bill_type = "";//支付类型
				String item_name = "";//商品名称
				int pay_status = 4;//支付状态
				String trade_no = "";//苹果appstore流水号
				
				
				
				
				PayBill payBill = null;
				if(!"".equals(KeyWord)){//订单号不为空
					payBill = getPayBillByKeyword(KeyWord);
					if(payBill == null){
						msg = "KeyWord不存在;";
						return new ResponseMsg(flag,"12", msg,"1:KeyWord不存在");
					}
					else{
//						JSONObject json = IosUtil.checkThread(receipt,request);
//						IosCheckThread check = new IosCheckThread(receipt,response);
						ResponseMsg res = IosCheck.check(receipt);
						if(res.getCode().equals("10")){
							payBillMapper.setBillStatusByKeyword(KeyWord, new Date(), trade_no);
						}
						return res;
//						System.out.println("json:"+json.toString());
//						status = json.getString("status");
//						if(status.equals("0")){
//							msg = "校验成功";
//							return new ResponseMsg(flag,"10",msg,"订单校验成功;");
//						}else{
//							msg = "校验失败";
//							return new ResponseMsg(flag,"11",msg,"订单校验失败;");
//						}
					}
					
				}else{
//					status = "0";
					//赋值状态
					if("0".equals(status)){
						flag = true;
					}
//					else{
//						msg = "检测状态为:"+status;
//					}
					item_id = dealNull(params.get("item_id"));
					pay_pro_price_id = dealNull(params.get("pay_pro_price_id"));
					item_name = dealNull(params.get("item_name"));
					pay_price = dealNull(params.get("pay_price"));//支付的价格
					bill_type = "1";//支付
					
					if("".equals(dealNull(params.get("item_id"))) && KuKePayConstants.DOWNID.indexOf(pay_pro_price_id) >= 0){
						msg = "item_id不能为空;";
					}else if("".equals(dealNull(params.get("pay_price")))){//价格
						msg = "pay_price 不能为空";
					}else if("".equals(dealNull(params.get("pay_pro_price_id")))){
						msg = "pay_pro_price_id不能为空;";
					}else if("".equals(item_name) && KuKePayConstants.DOWNID.indexOf(pay_pro_price_id) >= 0){
						msg = "下载的商品名称不能为空;";
					}else if(!KuKePayConstants.checkPay_pro_priceid(pay_pro_price_id)){
						msg = "pay_pro_price_id不存在";
					}
					//参数不对,返回错误
					if(!"".equals(msg)){
						return new ResponseMsg(flag,"12", msg,"");
					}
					//赋值状态
					if("0".equals(status)){
						pay_status = 2;//支付成功
						flag = true;
					}else{
						pay_status = 4;//ios  重新检测
						msg = "检测状态为:"+status;
					}
					
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("uid", user.getUid());
					param.put("item_id", item_id);
					param.put("pay_pro_price_id", pay_pro_price_id);
					param.put("pay_price", pay_price);
					param.put("bill_type", bill_type);
					param.put("item_name", item_name);
					param.put("from_client", "ios");
					param.put("pay_status",String.valueOf(pay_status));
					param.put("trade_no",dealNull(params.get("transactionIdentifier")));
					
					//记录日志
					downLoadLog(request);
					
					//根据苹果订单唯一交易标识查询订单
					PayBill bill = payBillMapper.selectPayBillByTradeNo(dealNull(params.get("transactionIdentifier")));
					if(bill==null){
						payBill = buildPayBill(param);
						KeyWord = payBill.getKeyword();//2.重新赋值参数
						item_id = dealNull(payBill.getItem_id());
						pay_pro_price_id = dealNull(payBill.getPay_pro_price_id());
						
						//3.更新权限
						if(flag){//检测成功
							//状态
							if(!"2".equals(payBill.getPay_status())){
								//添加帐户支付记录
								PayBillMsg payBillMsg = new PayBillMsg(11, KeyWord,payBill.getCost_price(), new Date(), 2);
								insertPayBillMsg(payBillMsg);
								
								//修改paybill 
								payBillMapper.setBillStatusByKeyword(KeyWord,new Date(),dealNull(params.get("transactionIdentifier")));
								//4.下载返回url
								String url = "";
								if(KuKePayConstants.DOWNID.indexOf(pay_pro_price_id) >= 0 && flag){//检测成功
									if(from.equals("m")){
										url = getDownLoadItemUrlForM(pay_pro_price_id, item_id,from);
									}
								}
								//权限更新
								PayBill payBillInfo = payBillMapper.getPayBillByKeywordUid(KeyWord);
								new UpdataAuthThread(payBillInfo).run();
								map.put("url",url);
								return new ResponseMsg(flag,"10", "生成订单成功","", map);
							}
						}
						return new ResponseMsg(flag,"11", "订单校验失败","","");
					}
					else{
						if(bill.getPay_status()==2){
							 return new ResponseMsg(true,"CLIENT_ALREADY_PAID","个人订单已存在","","");
						}else{
							 return new ResponseMsg(true,"CLIENT_ALREADY_PAID_WARN","您已购买过该单曲但订单异常","","");
						}
					}
					
				}
			}else{
				return new ResponseMsg(flag, KuKeAuthConstants.NOMALLOGIN,"用户未登录","");
		} 
	}catch (Exception e) {
		e.printStackTrace();
		msg = e.getMessage();
		return new ResponseMsg(false, "12",msg,"生成订单失败", "");
			}
		}
	
	@Override
	public List<Map<String, String>> getIosUserBillCheck(String user_id,
			String item_id, String item_code, String f) {
		List<Map<String, String>> list = payBillMapper.getIosUserBillCheck(user_id, item_id, item_code, f);
		return list;
	}
	/**
	 * 根据 pay_pro_price_id 记录下载日志
	 * @param request
	 */
	public void downLoadLog(HttpServletRequest request){
		Map<String, String> params = this.getParameterMap(request);
		String pay_pro_price_id = dealNull(params.get("pay_pro_price_id"));
		String item_id = dealNull(params.get("item_id"));
		if("".equals(pay_pro_price_id)){
			pay_pro_price_id = dealNull(params.get("priceid"));
		}
		//记录日志
		if(KuKePayConstants.AUDIODOWN.equals(pay_pro_price_id) || KuKePayConstants.SPOKENAUDIODOWN.equals(pay_pro_price_id)){
			Map<String, Object> map = new HashMap<String, Object>();
			map = userCenterService.getItemcodeByLcode(item_id);
			String item_code = map == null?"":dealNull((String)map.get("item_code"));
			LogUtil.addDownTrackLog(item_id,item_code,request);
		}else if(KuKePayConstants.CATALDOWN.equals(pay_pro_price_id) || KuKePayConstants.SPOKENDOWN.equals(pay_pro_price_id)){
			LogUtil.addDownTrackLog(item_id,item_id, request);
		}else if(KuKePayConstants.MUSICBOOKDOWN.equals(pay_pro_price_id)){
			LogUtil.addDownTrackLog(item_id,"", request);
		}
	}
}
