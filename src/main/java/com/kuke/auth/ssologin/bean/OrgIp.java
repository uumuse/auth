package com.kuke.auth.ssologin.bean;

import java.io.Serializable;

public class OrgIp implements Serializable {

	private static final long serialVersionUID = 3510335787354575661L;
	
	private String id;
	
	private String org_id;

	private String start_ip;

	private String end_ip;

	private OrgIp() {

	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String orgId) {
		org_id = orgId;
	}

	public String getStart_ip() {
		return start_ip;
	}

	public void setStart_ip(String startIp) {
		start_ip = startIp;
	}

	public String getEnd_ip() {
		return end_ip;
	}

	public void setEnd_ip(String endIp) {
		end_ip = endIp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	

}
