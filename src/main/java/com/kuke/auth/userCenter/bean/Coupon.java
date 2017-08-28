package com.kuke.auth.userCenter.bean;

import java.util.Date;

public class Coupon {
	
	private String id;//coupon_log 的id
	private String name;
	private String image;
	private String image_big;
	private int type;
	private Date end_date;
	
	private String coupon_states;
	private String coupon_key;
	private String comment;
	
	

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getImage_big() {
		return image_big;
	}
	public void setImage_big(String image_big) {
		this.image_big = image_big;
	}
	public String getCoupon_states() {
		return coupon_states;
	}
	public void setCoupon_states(String coupon_states) {
		this.coupon_states = coupon_states;
	}
	public String getCoupon_key() {
		return coupon_key;
	}
	public void setCoupon_key(String coupon_key) {
		this.coupon_key = coupon_key;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	

}
