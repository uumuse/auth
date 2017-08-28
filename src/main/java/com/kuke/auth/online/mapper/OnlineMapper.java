package com.kuke.auth.online.mapper;

import com.kuke.auth.online.bean.OrgOnline;
import com.kuke.auth.online.bean.UserOnline;

public interface OnlineMapper {

	
	/**
	 * 添加用户在线人数
	 * 
	 * @param userBuy
	 * @return
	 */
	public int addUserOnline(UserOnline userOnline);

	/**
	 * 添加机构在线人数
	 * 
	 * @param orgOnline
	 * @return
	 */
	public int addOrgOnline(OrgOnline orgOnline);
	

}
