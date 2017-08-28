package com.kuke.auth.regist.domain;

import java.io.Serializable;

public class UserDownLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String item_code;
	private String l_code;
	private String cname;
	private String from_client;
	private String channel_type;
	private String ename;
	private String user_id;
	private String user_type;
	private String down_date;
	private String down_ip;
	private String down_type;
	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String userType) {
		user_type = userType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItem_code() {
		return item_code;
	}

	public void setItem_code(String itemCode) {
		item_code = itemCode;
	}

	public String getL_code() {
		return l_code;
	}

	public void setL_code(String lCode) {
		l_code = lCode;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getFrom_client() {
		return from_client;
	}

	public void setFrom_client(String fromClient) {
		from_client = fromClient;
	}

	public String getChannel_type() {
		return channel_type;
	}

	public void setChannel_type(String channelType) {
		channel_type = channelType;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String userId) {
		user_id = userId;
	}

	public String getDown_date() {
		return down_date;
	}

	public void setDown_date(String downDate) {
		down_date = downDate;
	}

	public String getDown_ip() {
		return down_ip;
	}

	public void setDown_ip(String downIp) {
		down_ip = downIp;
	}

	public String getDown_type() {
		return down_type;
	}

	public void setDown_type(String downType) {
		down_type = downType;
	}
}
