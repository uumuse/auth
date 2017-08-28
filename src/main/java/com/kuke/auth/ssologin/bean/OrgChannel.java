package com.kuke.auth.ssologin.bean;

import java.io.Serializable;

public class OrgChannel implements Serializable {

	private static final long serialVersionUID = -8567968260007931225L;

	private String id;
	
	private String org_id;

	private String channel_id;

	private String max_online_num;

	private String end_date;

	private String channelStatus;// 频道状态

	public OrgChannel() {

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

	public String getMax_online_num() {
		return max_online_num;
	}

	public void setMax_online_num(String maxOnlineNum) {
		max_online_num = maxOnlineNum;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String endDate) {
		end_date = endDate;
	}

	public String getChannelStatus() {
		return channelStatus;
	}

	public void setChannelStatus(String channelStatus) {
		this.channelStatus = channelStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
}
