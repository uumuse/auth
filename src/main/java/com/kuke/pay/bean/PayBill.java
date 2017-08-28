package com.kuke.pay.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wanyj
 */
public class PayBill implements Serializable{
	
	private static final long serialVersionUID = 2145284254041128957L;
	
	private String keyword="";
	private Integer pay_status=1;//1未支付，2支付完成，3订单取消
	private String pay_pro_price_id="";
	private String user_id="";
	private Double cost_price=0.0;
	private Double discount_price=0.0;
	private Double total_price=0.0;
	private Integer bill_type=1;//0充值，1支付
	private String trade_no="";
	private String item_id="";
	private Integer pay_num=1;//几个月
	private String item_name="";
	private String item_image="";
	private String item_id_parent="";
	private String item_url="";
	private String from_client="";
	private String pay_channel_id="";
	private String pay_channel_type="";
	private String pro_price="";
	private String pro_price_num="";
	private Integer invoice_flag=0;
	private Date create_date;
	private Date confirm_date;
	private Date cancel_date;
	private Date last_date;
	
	
	public PayBill() {
		super();
	}
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getPay_status() {
		return pay_status;
	}
	public void setPay_status(Integer payStatus) {
		pay_status = payStatus;
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
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String userId) {
		user_id = userId;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Date getConfirm_date() {
		return confirm_date;
	}
	public void setConfirm_date(Date confirmDate) {
		confirm_date = confirmDate;
	}
	public Date getCancel_date() {
		return cancel_date;
	}
	public void setCancel_date(Date cancelDate) {
		cancel_date = cancelDate;
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
	public Integer getInvoice_flag() {
		return invoice_flag;
	}
	public void setInvoice_flag(Integer invoiceFlag) {
		invoice_flag = invoiceFlag;
	}
	public Integer getBill_type() {
		return bill_type;
	}
	public void setBill_type(Integer billType) {
		bill_type = billType;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getPay_pro_price_id() {
		return pay_pro_price_id;
	}

	public void setPay_pro_price_id(String pay_pro_price_id) {
		this.pay_pro_price_id = pay_pro_price_id;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public Integer getPay_num() {
		return pay_num;
	}

	public void setPay_num(Integer pay_num) {
		this.pay_num = pay_num;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_image() {
		return item_image;
	}

	public void setItem_image(String item_image) {
		this.item_image = item_image;
	}

	public String getItem_id_parent() {
		return item_id_parent;
	}

	public void setItem_id_parent(String item_id_parent) {
		this.item_id_parent = item_id_parent;
	}

	public String getItem_url() {
		return item_url;
	}

	public void setItem_url(String item_url) {
		this.item_url = item_url;
	}

	public String getPay_channel_id() {
		return pay_channel_id;
	}

	public void setPay_channel_id(String pay_channel_id) {
		this.pay_channel_id = pay_channel_id;
	}

	public String getPay_channel_type() {
		return pay_channel_type;
	}

	public void setPay_channel_type(String pay_channel_type) {
		this.pay_channel_type = pay_channel_type;
	}

	public String getPro_price() {
		return pro_price;
	}

	public void setPro_price(String pro_price) {
		this.pro_price = pro_price;
	}

	public String getPro_price_num() {
		return pro_price_num;
	}
	public void setPro_price_num(String pro_price_num) {
		this.pro_price_num = pro_price_num;
	}

	public String getFrom_client() {
		return from_client;
	}

	public void setFrom_client(String from_client) {
		this.from_client = from_client;
	}
	
	
}
