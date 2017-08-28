package com.kuke.pay.bean;

import java.io.Serializable;
import java.util.Date;

public class PayProPrice implements Serializable{
	
	private int id;
	private int pay_channel_id;
	private String pro_name;
	private String pro_image;
	private double pro_price;
	private Date start_date;
	private Date end_date;
	private int is_show;
	private Date last_date;
	private int num;
	private String channel_name;
	
	
	public PayProPrice() {
		super();
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPay_channel_id() {
		return pay_channel_id;
	}
	public void setPay_channel_id(int payChannelId) {
		pay_channel_id = payChannelId;
	}
	public String getPro_name() {
		return pro_name;
	}
	public void setPro_name(String proName) {
		pro_name = proName;
	}
	public String getPro_image() {
		return pro_image;
	}
	public void setPro_image(String proImage) {
		pro_image = proImage;
	}
	public double getPro_price() {
		return pro_price;
	}
	public void setPro_price(double proPrice) {
		pro_price = proPrice;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date startDate) {
		start_date = startDate;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date endDate) {
		end_date = endDate;
	}
	public int getIs_show() {
		return is_show;
	}
	public void setIs_show(int isShow) {
		is_show = isShow;
	}
	public Date getLast_date() {
		return last_date;
	}
	public void setLast_date(Date lastDate) {
		last_date = lastDate;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(String channelName) {
		channel_name = channelName;
	}
	

}
