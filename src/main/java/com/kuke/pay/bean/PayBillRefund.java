package com.kuke.pay.bean;

import java.io.Serializable;
import java.util.Date;

public class PayBillRefund implements Serializable{
	
	private static final long serialVersionUID = -5851584533279972437L;
	
	private String refund_keyword;
	private String keyword;
	private String pay_detail_id;//0：支付宝退款；1：微信退款
	private String total_price;
	private String refund_price;
	private String user_id;
	private String reason;
	private String result;//退款结果 0 失败 1 成功
	private String trade_no;//第三方交易订单号
	private Date refund_date;
	
	
	public PayBillRefund() {
		super();
	}

	public String getRefund_keyword() {
		return refund_keyword;
	}

	public void setRefund_keyword(String refund_keyword) {
		this.refund_keyword = refund_keyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getTotal_price() {
		return total_price;
	}

	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}

	public String getRefund_price() {
		return refund_price;
	}

	public void setRefund_price(String refund_price) {
		this.refund_price = refund_price;
	}

	
	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getRefund_date() {
		return refund_date;
	}

	public void setRefund_date(Date refund_date) {
		this.refund_date = refund_date;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getPay_detail_id() {
		return pay_detail_id;
	}

	public void setPay_detail_id(String pay_detail_id) {
		this.pay_detail_id = pay_detail_id;
	}
	
}
