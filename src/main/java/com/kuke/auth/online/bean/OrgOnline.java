package com.kuke.auth.online.bean;

import java.io.Serializable;

public class OrgOnline implements Serializable {

	private static final long serialVersionUID = 5005279121429034904L;

	private String id;

	private String org_id;

	private String channel_id;

	private String user_number;

	public OrgOnline() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String orgId) {
		org_id = orgId;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channelId) {
		channel_id = channelId;
	}

	public String getUser_number() {
		return user_number;
	}

	public void setUser_number(String userNumber) {
		user_number = userNumber;
	}

}
