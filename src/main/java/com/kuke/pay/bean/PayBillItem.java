package com.kuke.pay.bean;

import java.util.Date;

/**
 * @author wanyj
 */
public class PayBillItem {
	
	private Integer id;
	private String pay_bill_keyword;
	private String pay_pro_price_id;
	private String item_name;
	private String item_image;
	private String item_id;
	private String item_id_parent;
	private String user_id;
	private Double cost_price;
	private Double discount_price;
	private Integer pay_num;
	private Date last_date;
	private Double total_price;
	private String item_url;
	private Integer pay_status;
	private String pay_channel_id;
	private String pay_channel_type;
	private String pro_price;
	private String pro_price_num;
	
	
	public PayBillItem() {
		super();
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPay_bill_keyword() {
		return pay_bill_keyword;
	}
	public void setPay_bill_keyword(String payBillKeyword) {
		pay_bill_keyword = payBillKeyword;
	}
	public String getPay_pro_price_id() {
		return pay_pro_price_id;
	}
	public void setPay_pro_price_id(String payProPriceId) {
		pay_pro_price_id = payProPriceId;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String itemName) {
		item_name = itemName;
	}
	public String getItem_image() {
		return item_image;
	}
	public void setItem_image(String itemImage) {
		item_image = itemImage;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String itemId) {
		item_id = itemId;
	}
	public String getItem_id_parent() {
		return item_id_parent;
	}
	public void setItem_id_parent(String itemIdParent) {
		item_id_parent = itemIdParent;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String userId) {
		user_id = userId;
	}
	public Double getCost_price() {
		return cost_price;
	}
	public void setCost_price(Double costPrice) {
		cost_price = costPrice;
	}
	public Double getDiscount_price() {
		return discount_price;
	}
	public void setDiscount_price(Double discountPrice) {
		discount_price = discountPrice;
	}
	public Integer getPay_num() {
		return pay_num;
	}
	public void setPay_num(Integer payNum) {
		pay_num = payNum;
	}
	public Date getLast_date() {
		return last_date;
	}
	public void setLast_date(Date lastDate) {
		last_date = lastDate;
	}
	public Double getTotal_price() {
		return total_price;
	}
	public void setTotal_price(Double totalPrice) {
		total_price = totalPrice;
	}
	public String getItem_url() {
		return item_url;
	}
	public void setItem_url(String itemUrl) {
		item_url = itemUrl;
	}
	public Integer getPay_status() {
		return pay_status;
	}
	public void setPay_status(Integer payStatus) {
		pay_status = payStatus;
	}
	public String getPay_channel_id() {
		return pay_channel_id;
	}
	public void setPay_channel_id(String payChannelId) {
		pay_channel_id = payChannelId;
	}
	public String getPay_channel_type() {
		return pay_channel_type;
	}
	public void setPay_channel_type(String payChannelType) {
		pay_channel_type = payChannelType;
	}
	public String getPro_price() {
		return pro_price;
	}
	public void setPro_price(String proPrice) {
		pro_price = proPrice;
	}
	public String getPro_price_num() {
		return pro_price_num;
	}
	public void setPro_price_num(String proPriceNum) {
		pro_price_num = proPriceNum;
	}

}
