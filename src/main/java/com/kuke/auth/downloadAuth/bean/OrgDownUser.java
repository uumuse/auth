package com.kuke.auth.downloadAuth.bean;

import java.math.BigDecimal;
import java.util.Date;

public class OrgDownUser {
	private String id;
	private String org_id;
	private String user_name;
	private String password;
	private Double money;
	private Date last_date;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Date getLast_date() {
		return last_date;
	}
	public void setLast_date(Date last_date) {
		this.last_date = last_date;
	}

	

}
