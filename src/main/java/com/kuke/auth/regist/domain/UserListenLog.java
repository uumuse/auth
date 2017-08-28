package com.kuke.auth.regist.domain;

import java.io.Serializable;


public class UserListenLog  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String listen_user_id;
	private String l_code;
	private String listen_type;
	private String listen_date;
	private String item_code;
	private String from_client;
	private String cname;
	private String ename;
	private String listen_ip;
	private String channel_type;
	private String org_user_id;
	private String ischarged;
	public String getListen_user_id() {
		return listen_user_id;
	}
	public void setListen_user_id(String listen_user_id) {
		this.listen_user_id = listen_user_id;
	}
	public String getL_code() {
		return l_code;
	}
	public void setL_code(String l_code) {
		this.l_code = l_code;
	}
	public String getListen_type() {
		return listen_type;
	}
	public void setListen_type(String listen_type) {
		this.listen_type = listen_type;
	}
	public String getListen_date() {
		return listen_date;
	}
	public void setListen_date(String listen_date) {
		this.listen_date = listen_date;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getFrom_client() {
		return from_client;
	}
	public void setFrom_client(String from_client) {
		this.from_client = from_client;
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
	public String getListen_ip() {
		return listen_ip;
	}
	public void setListen_ip(String listen_ip) {
		this.listen_ip = listen_ip;
	}
	public String getChannel_type() {
		return channel_type;
	}
	public void setChannel_type(String channel_type) {
		this.channel_type = channel_type;
	}
	public String getOrg_user_id() {
		return org_user_id;
	}
	public void setOrg_user_id(String org_user_id) {
		this.org_user_id = org_user_id;
	}
	public String getIscharged() {
		return ischarged;
	}
	public void setIscharged(String ischarged) {
		this.ischarged = ischarged;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
