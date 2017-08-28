package com.kuke.auth.userCenter.bean;

import java.util.Date;

public class UserBill {
	
	private String keyword;
	private String pay_status;
	private String cost_price;
	private String bill_type;
	private String user_id;
	
	private String item_id;
	private String item_image;
	private String item_name;
	private String item_url;
	private String trade_no;
	
	private String pay_pro_price_id;
	
	private String pay_channel_id;
	private String pay_channel_name;
	private String pay_channel_type;
	
	private Date cancel_date;
	private Date confirm_date;
	private Date create_date;
	
	private String bspkey;
	private String rar;
	
	private String showable;//上下架
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getPay_status() {
		return pay_status;
	}
	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}
	public String getCost_price() {
		return cost_price;
	}
	public void setCost_price(String cost_price) {
		this.cost_price = cost_price;
	}
	public String getBill_type() {
		return bill_type;
	}
	public void setBill_type(String bill_type) {
		this.bill_type = bill_type;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getItem_image() {
		return item_image;
	}
	public void setItem_image(String item_image) {
		this.item_image = item_image;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getItem_url() {
		return item_url;
	}
	public void setItem_url(String item_url) {
		this.item_url = item_url;
	}
	public String getPay_pro_price_id() {
		return pay_pro_price_id;
	}
	public void setPay_pro_price_id(String pay_pro_price_id) {
		this.pay_pro_price_id = pay_pro_price_id;
	}
	public String getPay_channel_id() {
		return pay_channel_id;
	}
	public void setPay_channel_id(String pay_channel_id) {
		this.pay_channel_id = pay_channel_id;
	}
	public String getPay_channel_name() {
		return pay_channel_name;
	}
	public void setPay_channel_name(String pay_channel_name) {
		this.pay_channel_name = pay_channel_name;
	}
	public String getPay_channel_type() {
		return pay_channel_type;
	}
	public void setPay_channel_type(String pay_channel_type) {
		this.pay_channel_type = pay_channel_type;
	}
	public Date getCancel_date() {
		return cancel_date;
	}
	public void setCancel_date(Date cancel_date) {
		this.cancel_date = cancel_date;
	}
	public Date getConfirm_date() {
		return confirm_date;
	}
	public void setConfirm_date(Date confirm_date) {
		this.confirm_date = confirm_date;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getBspkey() {
		return bspkey;
	}
	public void setBspkey(String bspkey) {
		this.bspkey = bspkey;
	}
	public String getRar() {
		return rar;
	}
	public void setRar(String rar) {
		this.rar = rar;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public String getShowable() {
		return showable;
	}
	public void setShowable(String showable) {
		this.showable = showable;
	}
}
