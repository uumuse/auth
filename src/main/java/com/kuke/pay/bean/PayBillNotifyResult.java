package com.kuke.pay.bean;

import java.util.Date;

public class PayBillNotifyResult {
	
	private String pay_bill_keyword;
	private String result;
	private Date last_date;
	
	public PayBillNotifyResult() {
		super();
	}
	
	
	public PayBillNotifyResult(String payBillKeyword, String result,
			Date lastDate) {
		super();
		pay_bill_keyword = payBillKeyword;
		this.result = result;
		last_date = lastDate;
	}
	
	
	public String getPay_bill_keyword() {
		return pay_bill_keyword;
	}
	public void setPay_bill_keyword(String payBillKeyword) {
		pay_bill_keyword = payBillKeyword;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Date getLast_date() {
		return last_date;
	}
	public void setLast_date(Date lastDate) {
		last_date = lastDate;
	}
	
	

}
