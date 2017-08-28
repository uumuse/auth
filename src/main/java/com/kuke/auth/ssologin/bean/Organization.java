package com.kuke.auth.ssologin.bean;

import java.io.Serializable;

import net.sf.json.JSONObject;

import com.kuke.auth.util.KuKeAuthConstants;

public class Organization implements Serializable {

	private static final long serialVersionUID = -2360290680208439362L;

	private String org_id;

	private String org_name;

	private String org_area;

	private String org_url;

	private String org_type_id;

	private String org_type_name;

	private String reg_date;
	
	private String org_status;
	
	private String org_ssoid;
	

	public Organization() {
		org_status = KuKeAuthConstants.FAILED;
	}
	
	public Organization(String res) throws Exception {
		JSONObject json = JSONObject.fromObject(res);
		try {
			org_id = json.getString("org_id");
			org_name = json.getString("org_name");
			org_area = json.getString("org_area");
			org_url = json.getString("org_url");
			org_type_id = json.getString("org_type_id");
			org_type_name = json.getString("org_type_name");
			reg_date = json.getString("reg_date");
			org_ssoid = json.getString("org_ssoid");
			org_status = json.getString("org_status");
		} catch (Exception e) {
			org_status = KuKeAuthConstants.FAILED;
		}
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String orgId) {
		org_id = orgId;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String orgName) {
		org_name = orgName;
	}

	public String getOrg_area() {
		return org_area;
	}

	public void setOrg_area(String orgArea) {
		org_area = orgArea;
	}

	public String getOrg_url() {
		return org_url;
	}

	public void setOrg_url(String orgUrl) {
		org_url = orgUrl;
	}

	public String getOrg_type_id() {
		return org_type_id;
	}

	public void setOrg_type_id(String orgTypeId) {
		org_type_id = orgTypeId;
	}

	public String getOrg_type_name() {
		return org_type_name;
	}

	public void setOrg_type_name(String orgTypeName) {
		org_type_name = orgTypeName;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String regDate) {
		reg_date = regDate;
	}

	public String getOrg_status() {
		return org_status;
	}

	public void setOrg_status(String orgStatus) {
		org_status = orgStatus;
	}

	public String getOrg_ssoid() {
		return org_ssoid;
	}

	public void setOrg_ssoid(String orgSsoid) {
		org_ssoid = orgSsoid;
	}

	@Override
	public String toString() {
		return "Organization [org_id=" + org_id + ", org_name=" + org_name
				+ ", org_area=" + org_area + ", org_url=" + org_url
				+ ", org_type_id=" + org_type_id + ", org_type_name="
				+ org_type_name + ", reg_date=" + reg_date + ", org_status="
				+ org_status + ", org_ssoid=" + org_ssoid + "]";
	}
	
}
