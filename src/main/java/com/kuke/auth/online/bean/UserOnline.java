package com.kuke.auth.online.bean;

import java.io.Serializable;

public class UserOnline implements Serializable {

	private static final long serialVersionUID = -6040718523300800848L;

	private String id;

	private String user_id;

	private String channel_id;

	public UserOnline() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String userId) {
		user_id = userId;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channelId) {
		channel_id = channelId;
	}

}
