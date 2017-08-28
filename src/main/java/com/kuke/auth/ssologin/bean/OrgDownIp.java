package com.kuke.auth.ssologin.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrgDownIp implements Serializable {

	private static final long serialVersionUID = -3541401967520183363L;

	private int id;
	
	private String org_id;

	private String start_ip;

	private String end_ip;
	
	private Double money;
	
	private Date last_date;
	
	private int isused;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	public String getStart_ip() {
		return start_ip;
	}

	public void setStart_ip(String start_ip) {
		this.start_ip = start_ip;
	}

	public String getEnd_ip() {
		return end_ip;
	}

	public void setEnd_ip(String end_ip) {
		this.end_ip = end_ip;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getLast_date() {
		return last_date;
	}

	public void setLast_date(Date last_date) {
		this.last_date = last_date;
	}

	public int getIsused() {
		return isused;
	}

	public void setIsused(int isused) {
		this.isused = isused;
	}

	@Override
	public String toString() {
		return "OrgDownIp [id=" + id + ", org_id=" + org_id + ", start_ip="
				+ start_ip + ", end_ip=" + end_ip + ", money=" + money
				+ ", last_date=" + last_date + ", isused=" + isused + "]";
	}
}
