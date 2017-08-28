package com.kuke.auth.downloadAuth.bean;

import java.math.BigDecimal;
import java.util.Date;

public class OrgDownMoney {
	private String org_id;
	private BigDecimal money;
	private String is_user_pass;
	private Date last_date;

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getIs_user_pass() {
		return is_user_pass;
	}

	public void setIs_user_pass(String is_user_pass) {
		this.is_user_pass = is_user_pass;
	}

	public Date getLast_date() {
		return last_date;
	}

	public void setLast_date(Date last_date) {
		this.last_date = last_date;
	}

}
