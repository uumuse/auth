package com.kuke.pay.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kuke.pay.bean.PayBillRefund;

public interface PayBillRefundService {
	/**
	 * 根据退款订单号，查询退款订单信息
	 * @param refund_keyword
	 * @return
	 */
	public PayBillRefund getRefundBillInfoByID(String refund_keyword);
	/**
	 * 根据退款订单号，更新退款订单结果信息
	 * @param refund_keyword
	 * @param result
	 * @return
	 */
	public int updateRefundBillInfoByID(String refund_keyword,String result);
	/**
	 * 新建退款订单,返回退款订单号集合
	 * @param payBillRefund
	 * @return
	 */
	public Map<String, Object> createRefundBill(String keyword,String reason);
	/**
	 * 向微信发起退款申请
	 * @param PayBillRefund 退款详情单
	 * @return
	 */
	public String wechatApplyRefund(PayBillRefund payBillRefund,HttpServletRequest request);
	/**
	 * 查询微信退款订单
	 * @param payBillKeyword :商户订单
	 * @return
	 */
	public String wechatQueryRefund(String refund_keyword);
	/**
	 * 向支付宝发起无密退款申请
	 * @param PayBillRefund 退款详情单
	 * @return
	 */
	public String alipayApplyRefund(PayBillRefund payBillRefund,HttpServletRequest request);
	/**
	 * 退换到账户
	 * @param uid
	 * @param keyword
	 * @param price
	 * @return
	 */
	public String refundToUserACount(String uid,String keyword,String price);
}
