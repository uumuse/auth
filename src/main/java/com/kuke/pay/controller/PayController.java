package com.kuke.pay.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKePayConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.UserOauth;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.pay.bean.PayBill;
import com.kuke.pay.bean.PayBillMsg;
import com.kuke.pay.bean.PayProPrice;
import com.kuke.pay.mapper.PayBillMapper;
import com.kuke.pay.service.CommentService;
import com.kuke.pay.service.PayBillService;
import com.kuke.pay.util.PayMethord;
import com.kuke.pay.util.XMLUtil;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.MD5;
import com.kuke.util.MessageFormatUtil;
import com.kuke.util.StringUtil;

@Controller
@RequestMapping(value = "/kuke/payment")
public class PayController extends BaseController{
	
	private static Logger logger = LoggerFactory.getLogger(PayController.class);
	
	@Autowired
	private PayBillService payBillService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private PayBillMapper payBillMapper;
	@Autowired
	private UserCenterService userCenterService;
	
	/**
	 * 支付接口
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/pay")
	public String pay(HttpServletRequest request, HttpServletResponse response){
		//参数
		Map<String, String> params = this.getParameterMap(request);
		String pay_type  = dealNull(params.get("pay_type"));// 支付类型
		System.out.println("pay_type:"+pay_type);
		String bill_type  = dealNull(params.get("bill_type"));// 0充值  1支付
		String keyword = dealNull(params.get("keyword"));// 订单号
		String md5str = dealNull(params.get("md5str"));// md5加密串
		String checked = dealNull(params.get("check")); //是否使用帐户余额
		System.out.println("PayControllerTOpay:"+params);
		String object_name = "Kuke Library!";
		params.put("object_name", object_name);
		User user = this.getLoginUser();
		String uid = user.getUid();
		params.put("uid", uid);
		String result = "";
		if(user != null && KuKeAuthConstants.SUCCESS.equals(user.getUser_status())){
			try {
				//1.查询订单信息
				PayBill payBill = payBillService.getPayBillByKeyword(keyword);
				int payStatus = payBill.getPay_status();
				if(payStatus != 1){//1:待支付  2:交易成功  3:取消订单
					return "redirect:/kuke/userCenter/userBill";//用户订单
				}
				//2.验证MD5
				double cost_price = payBill.getCost_price();
				Map<String, String> userMoney = commentService.getUserMoneyByUid(uid);
				double remain_money = 0.0;
				double org_money = 0.0;
				if(userMoney != null){
					String remain_moneyStr = userMoney.get("remain_money") == null?"0":userMoney.get("remain_money");
					String org_moneyStr = userMoney.get("org_money") == null?"0":userMoney.get("org_money");
					remain_money = Double.valueOf(remain_moneyStr);//个人余额
					org_money = Double.valueOf(org_moneyStr);//机构余额
				}
				MD5 md5 = new MD5();
				String md5str_new = md5.getMD5ofStr(uid + keyword + "kukePay" + doubleFormat(remain_money) + doubleFormat(org_money));
				if ((!md5str.equals("")) && (md5str.equals(md5str_new))) {//验证通过
					//机构总钱数
//					double orgMoneys = getOrgMoneys(payBill.getPay_pro_price_id(), user.getOrg_id());;
					//获取用户帐户余额
//					if(!"".equals(user.getOrg_id()) && org_money > 0.0){//有机构,且有余额
//						//机构余额大于等于订单价 , 机构支付
//						if(org_money >= cost_price){
//							//防止出现重复记录 添加msg表之前 清空为支付方式的记录
//							payBillService.delPayBillMsgByKeyWord(keyword);
//							//添加账户机构支付记录
//							PayBillMsg payBillMsg = new PayBillMsg(8, keyword, cost_price, new Date(), 1);
//							payBillService.insertPayBillMsg(payBillMsg);
//							//扣除帐户机构中的余额
//							payBillService.updatePayStatusWithOrgRemain(uid, keyword, cost_price);
//							return "redirect:/kuke/payment/getSuccessPay?keyword="+keyword;//用户订单
//						}else{//
//							//机构余额小于订单价 , 机构支付 + (个人支付或第三方支付或(个人+第三方))
//							//参数为1 表示使用了帐户 余额,账户机构支付
//							if(!checked.equals("") && checked.equals("1")){
//								//防止出现重复记录 添加msg表之前 清空为支付状态的记录
//								payBillService.delPayBillMsgByKeyWord(keyword);
//								//(帐户余额+机构余额) 大于等于 剩余订单价  不需要第三方支付 ，扣除帐户机构余额和个人部分余额即可
//								if((remain_money + org_money) >= cost_price){
//									//添加账户机构支付记录:全部支付
//									PayBillMsg payBillMsg = new PayBillMsg(8, keyword, org_money, new Date(), 1);
//									payBillService.insertPayBillMsg(payBillMsg);
//									//添加帐户个人支付记录
//									PayBillMsg payBillMsgP = new PayBillMsg(1, keyword, cost_price - org_money, new Date(), 1);
//									payBillService.insertPayBillMsg(payBillMsgP);
//									//扣除帐户余额
//									payBillService.updatePayStatusWithRemain(uid, keyword, cost_price);
//									return "redirect:/kuke/payment/getSuccessPay?keyword="+keyword;//用户订单
//								}else{
//									//(帐户余额+机构余额)小于订单金额，账户机构支付全部,帐户个人支付全部，第三方支付另外一部分
//									if((remain_money + org_money) > 0.0){
//										//添加帐户机构支付记录:全部
//										PayBillMsg payBillMsg = new PayBillMsg(8, keyword, org_money, new Date(), 1);
//										payBillService.insertPayBillMsg(payBillMsg);
//										//添加帐户个人支付记录:全部
//										PayBillMsg payBillMsgP = new PayBillMsg(1, keyword, remain_money, new Date(), 1);
//										payBillService.insertPayBillMsg(payBillMsgP);
//									}
//									//差额 需要用第三方支付的 
//									double differ = cost_price - remain_money - org_money;
//									params.put("differ", String.valueOf(differ));
//									//第三方支付
//									otherPay(request, response, payBill, params);
//									result = dealNull((String)request.getAttribute("resultUrl"));
//									return result;
//								}
//							}else{//第三方支付
//								//防止出现重复记录 添加msg表之前 清空为支付状态的记录
//								payBillService.delPayBillMsgByKeyWord(keyword);
//								//添加账户机构支付记录:全部支付
//								PayBillMsg payBillMsg = new PayBillMsg(8, keyword, org_money, new Date(), 1);
//								payBillService.insertPayBillMsg(payBillMsg);
//								
//								double differ = cost_price - org_money;
//								params.put("differ", String.valueOf(differ));
//								//第三方支付
//								otherPay(request, response, payBill, params);
//								result = dealNull((String)request.getAttribute("resultUrl"));
//								return result;
//							}
//						}
//					}else{
						//参数为1 表示使用了帐户 余额支付
						if(!checked.equals("") && checked.equals("1")){
							//防止出现重复记录 添加msg表之前 清空为支付方式的记录
							payBillService.delPayBillMsgByKeyWord(keyword);
							//帐户余额大于订单总价  不需要第三方支付 ，扣除帐户余额即可
							if(remain_money >= cost_price){
								//添加帐户支付记录
								PayBillMsg payBillMsg = new PayBillMsg(1, keyword, cost_price, new Date(), 1);
								payBillService.insertPayBillMsg(payBillMsg);
								//扣除帐户余额
								payBillService.updatePayStatusWithPersonalRemain(uid, keyword, cost_price);
								return "redirect:/kuke/payment/getSuccessPay?keyword="+keyword;//用户订单
							}else{
								//帐户余额小于订单金额，帐户支付一部分，第三方支付另外一部分
								if(remain_money > 0.0){
									//添加帐户支付记录
									PayBillMsg payBillMsg = new PayBillMsg(1, keyword, remain_money, new Date(), 1);
									payBillService.insertPayBillMsg(payBillMsg);
								}
								//差额 需要用第三方支付的 
								double differ = cost_price - remain_money;
								params.put("differ", String.valueOf(differ));
								//第三方支付
								otherPay(request, response, payBill, params);
								result = dealNull((String)request.getAttribute("resultUrl"));
								return result;
							}
						}else{//第三方支付
							//防止出现重复记录 添加msg表之前 清空为支付状态的记录
							payBillService.delPayBillMsgByKeyWord(keyword);
							params.put("differ", String.valueOf(payBill.getCost_price()));
							//第三方支付
							otherPay(request, response, payBill, params);
							result = dealNull((String)request.getAttribute("resultUrl"));
							return result;
						}
//					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "redirect:/kuke/userCenter/userBill";
		}else{//未登录
			return KuKeUrlConstants.userLogin_url;
		}
	}
	/**
	 * 查询订单状态
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getKeyWordStatus")
	public @ResponseBody ResponseMsg getKeyWordStatus(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String payType = request.getParameter("payType");//支付类型  alipay wechatpay
		String KeyWord = request.getParameter("KeyWord");//订单号
		User user = this.getLoginUser();
		if(user != null){
			try {
				PayBill payBill = payBillService.getPayBillByKeyword(KeyWord);
				if(payBill != null && "2".equals(payBill.getPay_status())){//交易成功
					return new ResponseMsg(true, "1", "成功", "", "2");
				}else{
					if(payBill == null){
						return new ResponseMsg(false, KeyWord+"订单不存在");
					}else {
						String pay_status = String.valueOf(payBill.getPay_status());
						if("alipay".equals(payType)){//支付宝
							String post_url = KuKeUrlConstants.alipayApplyQuery;
							List<NameValuePair> nvps = new ArrayList<NameValuePair>();
							nvps.add(new BasicNameValuePair("payBillKeyword", KeyWord));
							nvps.add(new BasicNameValuePair("ssoid", user.getSsoid()));
							String result = HttpClientUtil.executeServicePOST(post_url, nvps);
							System.out.println("getKeyWordStatus result:"+result);
							ResponseMsg msg = new ResponseMsg(result);
							if(msg.getFlag()){//查询成功
								JSONObject json = JSONObject.fromObject(msg.getData());
								if(KuKeAuthConstants.SUCCESS.equals(json.getString("result"))){
									Map<String, String> map = new HashMap<String, String>();
									map.put("keyword", KeyWord);
									map.put("trade_no", json.getString("trade_no"));
									int res = payBillService.finishPayBill(map);
									if(res > 0){
										return new ResponseMsg(true, "1", "成功", "", "2");
									}else{
										return new ResponseMsg(true, "1", "成功", "", pay_status);
									}
								}else{
									return new ResponseMsg(true, "1", "成功", "",pay_status);
								}
							}else{
								return new ResponseMsg(true, "1", "成功", "",pay_status);
							}
						}else if("wechatpay".equals(payType)){//微信
							String post_url = KuKeUrlConstants.wechatApplyQuery;
							List<NameValuePair> nvps = new ArrayList<NameValuePair>();
							nvps.add(new BasicNameValuePair("payBillKeyword", KeyWord));
							nvps.add(new BasicNameValuePair("ssoid", user.getSsoid()));
							String result = HttpClientUtil.executeServicePOST(post_url, nvps);
							System.out.println("getKeyWordStatus result:"+result);
							ResponseMsg msg = new ResponseMsg(result);
							if(msg.getFlag()){//查询成功
								JSONObject json = JSONObject.fromObject(msg.getData());
								if(KuKeAuthConstants.SUCCESS.equals(json.getString("result"))){
									Map<String, String> map = new HashMap<String, String>();
									map.put("keyword", KeyWord);
									map.put("trade_no", json.getString("trade_no"));
									int res = payBillService.finishPayBill(map);
									if(res > 0){
										return new ResponseMsg(true, "1", "成功", "", "2");
									}else{
										return new ResponseMsg(true, "1", "成功", "", pay_status);
									}
								}else{
									return new ResponseMsg(true, "1", "成功", "",pay_status);
								}
							}else{
								return new ResponseMsg(true, "1", "成功", "",pay_status);
							}
						}else{
							return new ResponseMsg(false, "payType应为alipay或wechatpay");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return MessageFormatUtil.noFormatObject(KuKeAuthConstants.FAILED, null);
			}
		}else{
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null);
		}
	}
	/**
	 * 已有订单号的点击去支付选择页面(付款)
	 * 我的订单里待付款的订单
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/choosePayType")
	public String choosePayType(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> paramsMap = getParameterMap(request);
		//参数 keyword
		String pay_bill_keyword = dealNull(paramsMap.get("keyword"));
		
		//得到登录用户
		User user = this.getLoginUser();
		//用户已成功登录
		if(user != null && KuKeAuthConstants.SUCCESS.equals(user.getUser_status())){
			//设置个人信息
			this.getUserInfo(request, response);
			
			String org_id = dealNull((String)request.getAttribute("org_id"));
			
			if("".equals(pay_bill_keyword)){//订单号为空,跳到我的订单
				return "forward:/kuke/userCenter/userBill";
			}
			//判断订单是否已经付款完成
			PayBill payBill = payBillService.getPayBillByKeyword(pay_bill_keyword);
			if(payBill != null){//跳到我的订单
				if(!String.valueOf(payBill.getPay_status()).equals("1")){//已付款或已取消
					return "forward:/kuke/userCenter/userBill";
				}
			}else{
				return "forward:/kuke/userCenter/userBill";
			}
			//帐户余额
			String balance = "0.0";
			String orgbalance = "0.0";
			Map<String,String > commMap = commentService.getUserMoneyByUid(user.getUid());
			if(commMap != null){
				balance = "".equals(dealNull(commMap.get("remain_money")))?"0.00":dealNull(commMap.get("remain_money")); 
				orgbalance = "".equals(dealNull(commMap.get("org_money")))?"0.00":dealNull(commMap.get("org_money"));
			}
			//消费额
			String cost_price = payBillService.getPayBillPriceByKeyword(pay_bill_keyword);
			
			//MD5加密
			MD5 md5 = new MD5();
			//总价加密
			String md5Str = md5.getMD5ofStr(user.getUid() + pay_bill_keyword + "kukePay" + doubleFormat(Double.parseDouble(balance)) + doubleFormat(Double.parseDouble(orgbalance)));//用户ID+订单号+"kukePay"+ 用户余额 + 机构余额
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("balance", Double.valueOf(doubleFormat(Double.valueOf(balance))));//余额
			map.put("orgbalance", Double.valueOf(doubleFormat(Double.valueOf(orgbalance))));//机构余额
			map.put("keyword", pay_bill_keyword);//订单号
			map.put("cost_price", Double.valueOf(doubleFormat(Double.valueOf(cost_price))));//总额
			map.put("md5Str", md5Str);//加密字符串
			map.put("bill_type",String.valueOf(payBill.getBill_type()));
			map.put("itemname",payBill.getItem_name());
			
//			if("".equals(org_id)){//个人用户
				//差额
				double diff_price = 0.0;
				//帐户余额足够支付订单
				if(Double.valueOf(balance) >= Double.valueOf(cost_price)){
					diff_price = 0.0;//差额
				}else{//差额 = 总额 - 余额
					diff_price = Double.valueOf(cost_price) - Double.valueOf(balance);
				}
				map.put("diff_price", doubleFormat(diff_price));//差额
				request.setAttribute("map", map);
				//跳转到支付类型选择页面
				return "/payment/personalPay";
//			}else{//机构用户
//				
//				//差额
//				double diff_price = 0.0;
//				//帐户余额足够支付订单
//				if((Double.valueOf(balance) + Double.valueOf(orgbalance)) >= Double.valueOf(cost_price)){
//					diff_price = 0.0;//差额
//				}else{//差额 = 总额 - 余额 - 机构给的钱
//					diff_price = Double.valueOf(cost_price) - Double.valueOf(balance) - Double.valueOf(orgbalance);
//				}
//				
//				map.put("diff_price", doubleFormat(diff_price));
//				
//				if(diff_price >= Double.valueOf(cost_price)){//俩个机构余额>消费金额的话，orgFlag为true，否则为false
//					map.put("orgFlag", false);
//				}else{
//					map.put("orgFlag", true);
//				}
//				request.setAttribute("map", map);
//				//跳转到支付类型选择页面
//				return "/payment/orgUserPay";
//			}
		}else{
			return KuKeUrlConstants.userLogin_url;
		}
	}
	/**
	 * 生成服务订购 订单
	 */
	@RequestMapping("/goservice")
	public String goservice(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> map = this.getParameterMap(request);
		//参数  pay_pro_price_id
		String price_id = dealNull(request.getParameter("price_id"));
		
		User user = this.getLoginUser();
		if(user != null){
			//生成订单
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("item_id", price_id);
			param.put("pay_pro_price_id", price_id);
			param.put("uid", user.getUid());
			param.put("pay_price", "");
			param.put("bill_type", KuKePayConstants.PAYTOBUY);
			PayBill payBill = payBillService.buildPayBill(param);
			return "redirect:/kuke/payment/choosePayType?keyword="+payBill.getKeyword();
		}else{
			return KuKeUrlConstants.userLogin_url;
		}
	}
	/**
	 * 下载  订单
	 */
	@RequestMapping("/download")
	public String download(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		//参数  
		String item_name = dealNull(params.get("name"));///item_name
		String item_id = dealNull(params.get("id"));///item_id
		String pay_pro_price_id = dealNull(params.get("priceid"));///pay_pro_price_id
		
		User user = this.getLoginUser();
		if(user != null){
			try {
				item_name = URLDecoder.decode(item_name, "UTF-8");
				//生成订单
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("item_id", item_id);
				param.put("item_name", item_name);
				param.put("pay_pro_price_id", pay_pro_price_id);
				param.put("uid", user.getUid());
				param.put("pay_price", "");
				param.put("bill_type", KuKePayConstants.PAYTOBUY);
				PayBill payBill = payBillService.buildPayBill(param);
				
				//记录日志
				payBillService.downLoadLog(request);
				
				return "redirect:/kuke/payment/choosePayType?keyword="+payBill.getKeyword();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}else{
			return KuKeUrlConstants.userLogin_url;
		}
	}
	/**
	 * 充值
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/chargePay")
	public String chargePay(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> map = getParameterMap(request);
		String result = KuKeUrlConstants.userLogin_url;
		try {
			User user = null;
			try{
				user = UserOauth.userLoginByToken(request); 
			}catch (Exception e) {
				e.printStackTrace();
				return KuKeUrlConstants.userLogin_url;
			}
			if(KuKeAuthConstants.SUCCESS.equals(user.getUser_status())){
				//设置个人信息
				this.getUserInfo(request, response);
				
				//参数：pay_type，pay_price ,
				String pay_type = map.get("pay_type");// 支付类型
				String pay_price = dealNull(map.get("pay_price"));//价格
				
				String uid = dealNull(user.getUid());//用户ID
				String object_name = "KUKE Art Center";//支付项目
				PayBill bill = new PayBill();
				//生成订单
				try{
					Integer ID = 500;
					PayProPrice payProPrice = payBillMapper.getPayProPriceById(ID);
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("uid", uid);
					param.put("item_id",String.valueOf(ID));//
					param.put("pay_pro_price_id",String.valueOf(ID));//
					param.put("bill_type",KuKePayConstants.PAYTOCHARGE);//充值
					param.put("pay_price",pay_price);//
					bill = payBillService.buildPayBill(param);
				}catch (Exception e) {
					e.printStackTrace();
				}
				// 判断是否是服务器发送的请求 ---md5加密串
				//防止出现重复记录 添加msg表之前 清空为支付方式的记录
				payBillService.delPayBillMsgByKeyWord(bill.getKeyword());
				
				if (null != pay_type && pay_type.equals("alipay")) {// 支付宝
					//添加订单msg
					PayBillMsg payBillMsgWWW = new PayBillMsg(2, bill.getKeyword(),Double.valueOf(pay_price), new Date(), 1);
					payBillMapper.insertPayBillMsg(payBillMsgWWW);
					//第三方支付宝 2
					PayMethord.gotoAlipay(uid, pay_price, bill.getKeyword(), object_name,request);
				} else if(null != pay_type && pay_type.equals("wechatpay")){// 微信支付
					PayBillMsg payBillMsgWWW = new PayBillMsg(7, bill.getKeyword(),Double.valueOf(pay_price), new Date(), 1);
					payBillMapper.insertPayBillMsg(payBillMsgWWW);
					//第三方微信 7
					String preurl = "/kuke/userCenter/userAccount?position=1";//上一页
					request.setAttribute("preurl", preurl);
					PayMethord.gotoWeChatPay(pay_price, bill.getKeyword(), object_name,request);
				} else if (pay_type != null && pay_type.equals("sdo")) {
					PayBillMsg payBillMsgWWW = new PayBillMsg(4, bill.getKeyword(),Double.valueOf(pay_price), new Date(), 1);
					payBillMapper.insertPayBillMsg(payBillMsgWWW);
					//第三方盛付通
					PayMethord.gotoSDO(uid, pay_price, bill.getKeyword(),object_name,request);
				} else if(pay_type != null&&!pay_type.equals("")) {
					PayBillMsg payBillMsgWWW = new PayBillMsg(5, bill.getKeyword(),Double.valueOf(pay_price), new Date(), 1);
					payBillMapper.insertPayBillMsg(payBillMsgWWW);
					//第三方
					PayMethord.gotoSDOBank(uid, pay_type, pay_price, bill.getKeyword(), object_name, request);
				}
				result = dealNull((String)request.getAttribute("resultUrl"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 用机构总的余额去下载
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/payByOrgMoney")  
	public @ResponseBody ResponseMsg payByOrgMoney(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = this.getParameterMap(request);
		User user = this.getLoginUser();
		return payBillService.payByOrgType(request,response, params, user);
	}
	/**
	 * 获得支付成功结果
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getSuccessPay")  
	public String getSuccessPay(HttpServletRequest request,HttpServletResponse response){ 
		String keyword = dealNull(request.getParameter("keyword"));//支付的订单
		User user = this.getLoginUser();
		if(user != null){
			//查询个人信息
			this.getUserInfo(request, response);
			//查询支付方式
			BigDecimal cost_price = null;
			String paydesc = "";
			if(!"".equals(keyword)){
				//拿到订单 支付方式
				List<Map> msgList = payBillMapper.getPayBillMsgDesc(keyword);
				for(int i = 0; i < msgList.size();i++){
					Map m = msgList.get(i);
					cost_price = (BigDecimal) m.get("cost_price");
					paydesc +=" , "+ m.get("des");
				}
			}
			request.setAttribute("paydesc", StringUtil.formatCommaForString(paydesc));
			request.setAttribute("keyword", keyword);
			request.setAttribute("cost_price", cost_price);
			return "payment/resultPay";
		}else{
			return KuKeUrlConstants.userLogin_url;
		}
	}
	/**
	 * 从微信服务器查询订单状态
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryWxOrder")  
	public @ResponseBody ResponseMsg queryWxOrder(HttpServletRequest request,HttpServletResponse response){ 
		Map<String, Object> map = new HashMap<String, Object>();
		
		Map<String, String> data = new HashMap<String, String>();
		boolean flag = false;//是否查询成功
		String code = "1";
		String msg = "查询失败";
		try {
			String trade_no = dealNull(request.getParameter("trade_no"));//微信订单号
			String payBillKeyword = dealNull(request.getParameter("payBillKeyword"));//订单号，不能为空
			String resXml = payBillService.gotoWxOrderQuery(trade_no, payBillKeyword);
			if(resXml != null && !"".equals(resXml)){
				Map wxReturnMap = XMLUtil.doXMLParse(resXml);
				//trade_state 交易状态:SUCCESS—支付成功;REFUND—转入退款;NOTPAY—未支付;CLOSED—已关闭;REVOKED—已撤销;USERPAYING--用户支付中;PAYERROR--支付失败(其他原因，如银行返回失败)
				//return_code 和 result_code 都为成功时，交易成功
				String return_code = dealNull((String)wxReturnMap.get("return_code"));
				String result_code = dealNull((String)wxReturnMap.get("result_code"));
				String trade_state = dealNull((String)wxReturnMap.get("trade_state"));//
				if("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code) && "SUCCESS".equals(trade_state)){//查询成功
					flag = true;//是否查询成功
					code = "2";
					msg = "查询成功";
					data.put("result", KuKeAuthConstants.SUCCESS);
					data.put("trade_no", dealNull((String)wxReturnMap.get("transaction_id")));
				}else{
					flag = true;//是否查询成功
					code = "2";
					msg = "查询成功";
					data.put("result", KuKeAuthConstants.FAILED);
				}
			}else{
				data.put("result", KuKeAuthConstants.FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseMsg(flag,code, msg,"", data);
	}
	/**
	 * 从支付宝服务器查询订单状态
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryAlipayOrder")  
	public @ResponseBody ResponseMsg queryAlipayOrder(HttpServletRequest request,HttpServletResponse response){ 
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = false;//是否查询成功
		String code = "1";
		String msg = "查询失败";
		Map<String, String> data = new HashMap<String, String>();
		try {
			String trade_no = dealNull(request.getParameter("trade_no"));//支付宝订单号
			String payBillKeyword = dealNull(request.getParameter("payBillKeyword"));//订单号，不能为空
			String resXml = payBillService.gotoAlipayOrderQuery(trade_no, payBillKeyword);
			if(!"".equals(dealNull(resXml))){
				flag = true;
				code = "2";
				msg = "查询成功";
				Map wxReturnMap = XMLUtil.doXMLParse(resXml);
				if(wxReturnMap.get("response") != null){
					Map returnMap = XMLUtil.doXMLParse((String)wxReturnMap.get("response"));
					//trade_status:TRADE_FINISHED TRADE_SUCCESS BUYER_PRE_AUTH:语音支付成功
					String trade_status = dealNull((String)returnMap.get("trade_status"));
					if("TRADE_SUCCESS".equals(trade_status) || "TRADE_FINISHED".equals(trade_status) || "BUYER_PRE_AUTH".equals(trade_status)){//支付成功 
						data.put("result", KuKeAuthConstants.SUCCESS);
						data.put("trade_no", dealNull((String)returnMap.get("trade_no")));
					}else{
						data.put("result", KuKeAuthConstants.FAILED);
					}
				}else{
					data.put("result", KuKeAuthConstants.FAILED);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseMsg(flag,code, msg,"", data);
	}
	/**
	 * 从微信服务器关闭订单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/closeWxOrder")  
	public @ResponseBody Map<String, Object> closeWxOrder(HttpServletRequest request,HttpServletResponse response){ 
		Map<String, Object> map = new HashMap<String, Object>();
		String result = "FAIL";//是否关闭成功
		String msg = "";//
		try {
			String payBillKeyword = dealNull(request.getParameter("payBillKeyword"));//订单号，不能为空
			String resXml = payBillService.closeWxOrder(payBillKeyword);
			Map wxReturnMap = XMLUtil.doXMLParse(resXml);
			//return_code 和 result_code 都为成功时，关单成功
			String return_code = dealNull((String)wxReturnMap.get("return_code"));
			String result_code = dealNull((String)wxReturnMap.get("result_code"));
			if("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)){//查询成功
				result = "SUCCESS";
				msg = "SUCCESS";
			}else{
				msg = dealNull((String)wxReturnMap.get("return_msg"));//交易失败原因
				if("".equals(msg)){
					msg = "关闭微信订单失败！";
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
	 * 生成订单
	 */
	@RequestMapping("/buildBill")
	public @ResponseBody ResponseMsg buildBill(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = this.getParameterMap(request);
		boolean flag = false;
		String code = "3";
		String msg = "生成订单失败";
		String codeDesc = "1:用户未登录;"
						+ "2:生成订单成功;"
						+ "3:生成订单失败;"
						+ "4:name为空;"
						+ "5:id为空;"
						+ "6:pay_pro_price_id为空;"
						+ "7:bill_type为空;"
						+ "8:pay_price为空;";
		PayBill payBill = null;
		if(this.getLoginUser() != null){
			try {
				String item_name = dealNull(params.get("name"));///item_name
				String item_id = dealNull(params.get("id"));///item_id
				String pay_pro_price_id = dealNull(params.get("priceid"));///pay_pro_price_id
				String bill_type = dealNull(params.get("bill_type"));//0充值，1支付
				String pay_price = dealNull(params.get("pay_price"));//0充值价格
				if("".equals(item_name)){
					code = "4";
					msg = "name为空;";
				}else if("".equals(item_id)){
					code = "5";
					msg = "id为空;";
				}else if("".equals(pay_pro_price_id)){
					code = "6";
					msg = "pay_pro_price_id为空;";
				}else if("".equals(bill_type)){
					code = "7";
					msg = "bill_type为空;";
				}else if("".equals(pay_price)){
					code = "8";
					msg = "pay_price为空;";
				}else{
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("item_id", item_id);
					param.put("pay_pro_price_id", pay_pro_price_id);
					param.put("item_name", item_name);
					param.put("uid", this.getLoginUser().getUid());
					param.put("pay_price", pay_price);
					param.put("bill_type", bill_type);
					payBill = payBillService.buildPayBill(param);
					if(payBill != null && payBill.getCost_price() == 0){
						Map<String, String> map = new HashMap<String, String>();
						map.put("keyword", payBill.getKeyword());
						payBillService.finishPayBill(map);
					}
					if("".equals(payBill.getKeyword())){
						code = "3";
						msg = "生成订单失败;";
					}else{
						flag = true;
						code = "2";
						msg = "生成订单成功;";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			code = "1";
			msg = "用户未登录;";
		}
		return new ResponseMsg(flag, code, msg, codeDesc, payBill);
	}
	/**
	 * 生成二维码图片 不存储 直接以流的形式输出到页面
	 * @param code_url
	 * @param response
	 */
	@RequestMapping("/qr_codeImg")  
	public @ResponseBody void getQRCode(String code_url,HttpServletResponse response,HttpServletRequest request){  
		if(code_url==null || "".equals(code_url)){
			 return;  
		}
	   MultiFormatWriter multiFormatWriter = new MultiFormatWriter();  
	   Map hints = new HashMap();  
	   hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); //设置字符集编码类型  
	   BitMatrix bitMatrix = null;  
	   String width =  "".equals(StringUtil.dealNull(request.getParameter("width")))?"300":StringUtil.dealNull(request.getParameter("width"));
	   String height =  "".equals(StringUtil.dealNull(request.getParameter("height")))?"300":StringUtil.dealNull(request.getParameter("height"));
	   try {  
	       bitMatrix = multiFormatWriter.encode(code_url, BarcodeFormat.QR_CODE, Integer.parseInt(width), Integer.parseInt(height),hints);  
	       BufferedImage image = toBufferedImage(bitMatrix);  
	       //输出二维码图片流  
	       try {  
	           ImageIO.write(image, "png", response.getOutputStream());  
	       } catch (IOException e) {  
	           e.printStackTrace();  
	       }  
	   } catch (Exception e1) {  
	       e1.printStackTrace();  
	   }  
	}
	/**
	 * 价格接口
	 */
	@RequestMapping("/iProPrice")
	public @ResponseBody String iPayProPrice(HttpServletRequest request , HttpServletResponse response){
		String result = "";
		String id = (String) getParameterMap(request).get("id");
		String type = (String) getParameterMap(request).get("type");
		String md5Str = (String) getParameterMap(request).get("md5Str");
		if (new MD5().getMD5ofStr(id + type).equals(md5Str)) {
			List<PayProPrice> list = payBillService.getPayProPriceByChannelId(Integer.parseInt(id));
			result = String.valueOf(JSONArray.fromObject(list));
		} else {
			JSONObject obj = new JSONObject();
			obj.put("error", "error");
			result =  String.valueOf(JSONObject.fromObject(obj));
		}
		return result;
	}
	/**
	 * 得到账户的余额
	 */
	@RequestMapping("/getUserMoney")
	public @ResponseBody ResponseMsg getUserMoney(HttpServletRequest request,HttpServletResponse response){
		User user = this.getLoginUser();
		Map<String, String> data = new HashMap<String, String>();
		if(user != null && user.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
			try {
				Map<String, String> userMoney = commentService.getUserMoneyByUid(user.getUid());
				if(userMoney != null){
					data.put("remain_money", "".equals(dealNull(userMoney.get("remain_money")))?"0":dealNull(userMoney.get("remain_money")));
					data.put("org_money", "".equals(dealNull(userMoney.get("org_money")))?"0":dealNull(userMoney.get("org_money")));
				}else{
					data.put("remain_money", "0.00");
					data.put("org_money", "0.00");
				}
				return MessageFormatUtil.noFormatObject(KuKeAuthConstants.SUCCESS,data);
			} catch (Exception e) {
				e.printStackTrace();
				return MessageFormatUtil.noFormatObject(KuKeAuthConstants.FAILED,null);
			}
		}else{
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN,null);
		}
	}
	/**
	 * 得到下载资源所对应的钱
	 */
	@RequestMapping("/getDowndloadMoney")
	public @ResponseBody ResponseMsg getDowndloadMoney(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = this.getParameterMap(request);
		User user = this.getLoginUser();
		if(user != null && user.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				//参数  type , item_id
				String type = dealNull(params.get("type"));
				param.put("type", type);
				param.put("item_id", dealNull(params.get("item_id")));
				param.put("bill_type",KuKePayConstants.PAYTOBUY);
				if("1".equals(type)){//单曲
					param.put("pay_pro_price_id", KuKePayConstants.AUDIODOWN);
				}else if("2".equals(type)){//专辑
					param.put("pay_pro_price_id", KuKePayConstants.CATALDOWN);
				}else if("3".equals(type)){//乐谱
					param.put("pay_pro_price_id", KuKePayConstants.MUSICBOOKDOWN);
				}else if(!"".equals(dealNull(params.get("pay_pro_price_id")))){
					param.put("pay_pro_price_id", dealNull(params.get("pay_pro_price_id")));
				}else{
					return new ResponseMsg(false, "pay_pro_price_id 不能为空");
				}
				//计算价格
				Map<String, Object> map = payBillService.getPayPrice(param);
				return MessageFormatUtil.noFormatObject(KuKeAuthConstants.SUCCESS,map);
			} catch (Exception e) {
				e.printStackTrace();
				return MessageFormatUtil.noFormatObject(KuKeAuthConstants.FAILED,null);
			}
		}else{
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN,null);
		}
	}
	/**
	 * 第三方支付
	 * @param request
	 * @param response
	 * @param payBill
	 * @param params
	 */
	private void otherPay(HttpServletRequest request, HttpServletResponse response,PayBill payBill,Map<String, String> params){
		//参数
		String pay_type  = dealNull(params.get("pay_type"));// 支付类型
		String bill_type  = dealNull(params.get("bill_type"));// 0充值  1支付
		String keyword = dealNull(params.get("keyword"));// 订单号
		String md5str = dealNull(params.get("md5str"));// md5加密串
		String checked = dealNull(params.get("check")); //是否使用帐户余额
		String uid = dealNull(params.get("uid")); //uid
		String object_name = dealNull(params.get("object_name"));
		String differ = dealNull(params.get("differ"));//支付的钱
		if (null != pay_type && pay_type.equals("alipay")) {
			//添加订单msg
			PayBillMsg payBillMsgWWW = new PayBillMsg(2,keyword,Double.valueOf(differ),new Date(),1);
			payBillMapper.insertPayBillMsg(payBillMsgWWW);
			//第三方支付宝 2
			PayMethord.gotoAlipay(uid, differ, keyword, object_name,request);
		} else if (pay_type != null && pay_type.equals("wechat")) {
			PayBillMsg payBillMsgWWW = new PayBillMsg(7, keyword,Double.valueOf(differ), new Date(), 1);
			payBillMapper.insertPayBillMsg(payBillMsgWWW);
			//第三方微信 7
			String preurl = "/kuke/payment/choosePayType?keyword="+keyword;//上一页
			request.setAttribute("preurl", preurl);
			//个人信息
			this.getUserInfo(request, response);
			PayMethord.gotoWeChatPay(differ, keyword, object_name,request);
		} else if (pay_type != null && pay_type.equals("sdo")) {
			PayBillMsg payBillMsgWWW = new PayBillMsg(4, keyword,Double.valueOf(differ), new Date(), 1);
			payBillMapper.insertPayBillMsg(payBillMsgWWW);
			//第三方盛付通
			PayMethord.gotoSDO(uid,differ, keyword,object_name,request);
		} else if(pay_type != null && !pay_type.equals("")) {
			PayBillMsg payBillMsgWWW = new PayBillMsg(5, keyword,Double.valueOf(differ), new Date(), 1);
			payBillMapper.insertPayBillMsg(payBillMsgWWW);
			//第三方
			PayMethord.gotoSDOBank(uid, pay_type,differ, keyword, object_name,request);
		}
	}
	
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
	/**
	 * 检查空字符串
	 * @param str
	 * @return
	 */
	public static String dealNull(String str){
		if(str == null || "".equals(str.trim()) || "null".equals(str.trim())){
			str = "";
		}
		return str.trim();
	}
	public String doubleFormat(double b){
		DecimalFormat df = new DecimalFormat("######0.00");
		return df.format(b);
	}
	
}
