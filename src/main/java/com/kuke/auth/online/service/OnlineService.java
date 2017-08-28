package com.kuke.auth.online.service;

import com.kuke.auth.online.bean.OrgOnline;
import com.kuke.auth.online.bean.UserOnline;

public interface OnlineService {

	public int addUserOnline(UserOnline userOnline);

	public int addOrgOnline(OrgOnline orgOnline);

}
