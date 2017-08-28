package com.kuke.auth.downloadAuth.bean;

import java.util.Date;

public class OrgDownLog {
//	String org_id, String type, String price,
//	String down_id, String account_name, String last_date, String ip,
//	String cname, String ename, String image
	
	private String id;
	private String org_id;
	private String type;
	private Double price;
	private String pay_detail_id;
	private String down_id;
	private String account_name;
	private Date last_date;
	private String ip;
	private String cname;
	private String ename;
	private String image;
	private String from_client;
	private int ip_acc_id;
	private String account_type;
	private String temp_down_id;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getDown_id() {
		return down_id;
	}
	public void setDown_id(String down_id) {
		this.down_id = down_id;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public Date getLast_date() {
		return last_date;
	}
	public void setLast_date(Date last_date) {
		this.last_date = last_date;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getIp_acc_id() {
		return ip_acc_id;
	}
	public void setIp_acc_id(int ip_acc_id) {
		this.ip_acc_id = ip_acc_id;
	}
	public String getAccount_type() {
		return account_type;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	public String getPay_detail_id() {
		return pay_detail_id;
	}
	public void setPay_detail_id(String pay_detail_id) {
		this.pay_detail_id = pay_detail_id;
	}
	public String getFrom_client() {
		return from_client;
	}
	public void setFrom_client(String from_client) {
		this.from_client = from_client;
	}
	public String getTemp_down_id() {
		return temp_down_id;
	}
	public void setTemp_down_id(String temp_down_id) {
		this.temp_down_id = temp_down_id;
	}
	

}
