package com.kuke.pay.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kuke.pay.bean.PayBill;
import com.kuke.pay.bean.PayBillRefund;


public interface PayBillRefundMapper {
	/**
	 * 根据退款订单号，查询退款订单信息
	 * @param refund_keyword
	 * @return
	 */
	public PayBillRefund getRefundBillInfoByID(@Param("refund_keyword") String refund_keyword);
	/**
	 * 根据退款订单号，更新退款订单结果信息
	 * @param refund_keyword
	 * @param result
	 * @return
	 */
	public int updateRefundBillInfoByID(@Param("refund_keyword") String refund_keyword,@Param("result") String result);
	/**
	 * 插入一个 退款订单
	 * @param payBillRefund
	 * @return
	 */
	public int insertRefundBill(PayBillRefund payBillRefund);
	
	
	public List<String> getAllKeyWord();
	public List<PayBill> getTheKeyWord(@Param("count") String count);
	public int insertPayBill(@Param("payBill")List<PayBill> payBill);
	
}
