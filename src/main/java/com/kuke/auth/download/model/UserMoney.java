package com.kuke.auth.download.model;

public class UserMoney {
	private int id;
	private String user_id;
	private float remain_money;
	private String ope_message;
	private String last_update;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public float getRemain_money() {
		return remain_money;
	}
	public void setRemain_money(float remain_money) {
		this.remain_money = remain_money;
	}
	public String getOpe_message() {
		return ope_message;
	}
	public void setOpe_message(String ope_message) {
		this.ope_message = ope_message;
	}
	public String getLast_update() {
		return last_update;
	}
	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}
	
	
}
