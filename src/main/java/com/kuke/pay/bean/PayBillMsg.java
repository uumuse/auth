package com.kuke.pay.bean;

import java.util.Date;

public class PayBillMsg {
	private Integer pay_detail_id;
	private String pay_bill_keyword;
	private Double pay_price;
	private Date last_date;
	private Integer pay_status;
	
	public PayBillMsg() {
		super();
	}
	
	
	public PayBillMsg(Integer payDetailId, String payBillKeyword,
			Double payPrice, Date lastDate, Integer payStatus) {
		super();
		pay_detail_id = payDetailId;
		pay_bill_keyword = payBillKeyword;
		pay_price = payPrice;
		last_date = lastDate;
		pay_status = payStatus;
	}



	public Integer getPay_detail_id() {
		return pay_detail_id;
	}
	public void setPay_detail_id(Integer payDetailId) {
		pay_detail_id = payDetailId;
	}
	public String getPay_bill_keyword() {
		return pay_bill_keyword;
	}
	public void setPay_bill_keyword(String payBillKeyword) {
		pay_bill_keyword = payBillKeyword;
	}
	public Double getPay_price() {
		return pay_price;
	}
	public void setPay_price(Double payPrice) {
		pay_price = payPrice;
	}
	public Date getLast_date() {
		return last_date;
	}
	public void setLast_date(Date lastDate) {
		last_date = lastDate;
	}
	public Integer getPay_status() {
		return pay_status;
	}
	public void setPay_status(Integer payStatus) {
		pay_status = payStatus;
	}
	
	
	

}
