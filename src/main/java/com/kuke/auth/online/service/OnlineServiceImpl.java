package com.kuke.auth.online.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.auth.online.mapper.OnlineMapper;
import com.kuke.auth.online.service.OnlineService;
import com.kuke.auth.online.bean.OrgOnline;
import com.kuke.auth.online.bean.UserOnline;

@Service
public class OnlineServiceImpl implements OnlineService {

	@Autowired
	private OnlineMapper onlineMapper;

	@Override
	public int addOrgOnline(OrgOnline orgOnline) {
		return onlineMapper.addOrgOnline(orgOnline);
	}

	@Override
	public int addUserOnline(UserOnline userOnline) {
		return onlineMapper.addUserOnline(userOnline);
	}

}
