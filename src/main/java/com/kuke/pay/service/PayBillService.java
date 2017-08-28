package com.kuke.pay.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kuke.auth.downloadAuth.bean.OrgDownLog;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.ssologin.bean.OrgDownIp;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.pay.bean.PayBill;
import com.kuke.pay.bean.PayBillMsg;
import com.kuke.pay.bean.PayProPrice;

public interface PayBillService {
	
	public boolean getPayStatusByItemId(String lId,String uid,String itemId);
	
	/**
	 * 根据uid查询订单中资源id
	 * @param uid
	 * @return
	 */
	public List<Map<String,String>> getUserBillByUserId(String uid);
	
	public List<Map<String,String>> getPayBillByTempId(String user_id,
			String item_id,String item_code,String f);
	
	
	
	/***
	 * 
	 * @param uid
	 * @param item_id
	 * @param item_code
	 * @param f
	 * @return
	 */
	public List<Map<String,String>> getUserBillCheck(String uid,String item_id,String item_code,String f);
	
	
	public List<Map<String,String>> getIosUserBillCheck(String user_id,String item_id,String item_code,String f);
	/**
	 * 微信app统一下单接口
	 * @param uid
	 * @param payValue
	 * @param payBillKeyword
	 * @param objectName
	 * @param request
	 * @return
	 */
	public String gotoAppWeChatPay(String payValue,String payBillKeyword, String objectName);
	/**
	 * 微信订单查询
	 * @param transaction_id :微信订单
	 * @param out_trade_no :商户订单
	 * @return
	 */
	public String gotoWxOrderQuery(String transaction_id, String payBillKeyword);
	/**
	 * 关闭微信订单
	 * @param payBillKeyword :商户订单
	 * @return
	 */
	public String closeWxOrder(String payBillKeyword);
	/**
	 * 支付宝订单查询
	 * @param transaction_id :微信订单
	 * @param out_trade_no :商户订单
	 * @return
	 */
	public String gotoAlipayOrderQuery(String transaction_id, String payBillKeyword);
	/**
	 * 支付完成结算订单
	 * @param keyword
	 */
	public int finishPayBill(Map<String, String> map);
	/**
	 * 根据用户名和订单号 查询订单  信息比较全的方法 
	 * @param keyword
	 * @return
	 */
	public PayBill getPayBillByKeyword(String keyword);
	/**
	 * 根据keyword  删除msg表的记录
	 * @param keyword
	 */
	public void delPayBillMsgByKeyWord(String keyword);
	/**
	 * 根据keyword  msg表的记录
	 * @param keyword
	 */
	public void insertPayBillMsg(PayBillMsg payBillMsg);
	/**
	 * 单独扣除账户余额
	 * @param uid
	 * @param keyword
	 * @param price
	 */
	public int updatePayStatusWithPersonalRemain(String uid,String keyword, double price);
	/**
	 * 单独扣除账户机构余额
	 * @param uid
	 * @param keyword
	 * @param price
	 */
	public int updatePayStatusWithOrgRemain(String uid,String keyword, double price);
	/**
	 * 单独扣除账户机构余额+机构余额
	 * @param uid
	 * @param keyword
	 * @param price
	 */
	public int updatePayStatusWithOrg(String org_id,String uid, String keyword, double price);
	/**
	 * 单独扣除账户机构余额+机构余额+个人余额
	 * @param uid
	 * @param keyword
	 * @param price
	 */
	public int updatePayStatusWithAllMoney(String org_id,String uid, String keyword, double price); 
	/**
	 * 单独扣除机构余额
	 * @param uid
	 * @param keyword
	 * @param price
	 * @throws Exception 
	 */
	public int updatePayStatusWithOrgMoney(Map<String, Object> param) throws Exception;
	/**
	 * 扣除个人余额,机构账户余额
	 * @param uid
	 * @param keyword
	 * @param price
	 */
	public int updatePayStatusWithRemain(String uid,String keyword, double price);
	/**
	 * 根据pay_channel_id获取pay_pro_price信息
	 * @param id
	 * @return
	 */
	public List getPayProPriceByChannelId(int id);
	/**
	 * 创建订单
	 * @param uid
	 * @return
	 */
	public PayBill buildPayBill(Map<String, Object> params);
	/**
	 *  query PayBill
	 * @param keyword
	 * @param uid
	 * @return
	 */
	public PayBill getPayBillByKeyWordUid(String keyword);
	/**
	 * 根据keyword查询PayBillPrice
	 * @param keyword
	 * @return
	 */
	public String getPayBillPriceByKeyword(String keyword);
	/**
	 *  根据ID得到支付项目名称及价格
	 * @param id
	 * @return
	 */
	public PayProPrice getPayProPriceById(int id);
	/**
	 * 带CD数量的Price
	 * @param item_code
	 * @return
	 */
	public Map<String,String> getPayProPriceNoCD(String item_code);
	/**
	 * 得到支付所需要的钱
	 * @param params
	 * @return
	 */
	public Map<String, Object> getPayPrice(Map<String, Object> params);
	
	
	/**
	 * 记录IP下载订单
	 * @param orgDownLog
	 * @return
	 */
	public int buildOrgIPDownLog(OrgDownLog orgDownLog);
//	public int buildOrgIPDownLog(String pay_pro_price_id,String org_id,String price,String payType,String item_id,String uid,
//			String ip,String cname,String name,String image,String from_client,String account_type,String ip_acc_id,String temp_down_id);
	/**
	 * 得到机构的开放余额
	 * @param params
	 * @return
	 */
	public Map<String,Object> getOrgMoneys(Map<String, String> params);
	/**
	 * 得到IP的余额
	 * @param params
	 * @return
	 */
	public List<OrgDownIp> getOrgIPMoney(Map<String, String> params);
	/**
	 * 得到机构账户的共享余额
	 * @param params
	 * @return
	 */
	public Map<String,Object> getOrgUserMoney(Map<String, String> params);
	

	public int updateOrgIPMoney(double money,String id);
	public int updateOrgMoneys(double money,String orgid,String productid);
	public int updateOrgUserMoney(double money,String orgid,String productid);
	/**
	 * 机构支付方式
	 * @param requset
	 * @param param
	 * @return
	 */
	public ResponseMsg payByOrgType(HttpServletRequest request,HttpServletResponse response,Map<String, String> param,User user);
	/**
	 * ios  支付
	 * @param requset
	 * @param param
	 * @return
	 */
	public ResponseMsg payByIOS(HttpServletRequest request,HttpServletResponse response,User user,String status);
	/**
	 * 根据下载记录日志
	 * @param request
	 */
	public void downLoadLog(HttpServletRequest request);
}
