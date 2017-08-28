package com.kuke.auth.ssologin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.auth.ssologin.bean.OrgChannel;
import com.kuke.auth.ssologin.bean.OrgDownIp;
import com.kuke.auth.ssologin.bean.OrgIp;
import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.auth.ssologin.mapper.OrgSSOMapper;
import com.kuke.auth.ssologin.service.OrgService;
import com.kuke.core.redis.RedisCached;

@Service
public class OrgServiceImpl implements OrgService {

	@Autowired
	private OrgSSOMapper orgSSOMapper;
	
	
	@Override
	@RedisCached(name = "getOrganizationById", key = "orgInfo:")
	public Organization getOrganizationById(String orgId) {
		return orgSSOMapper.getOrganizationById(orgId);
	}
	@Override
	public Organization getOrganizationByOrgUserId(String name,String pwd){
		return orgSSOMapper.getOrganizationByOrgUserId(name, pwd);
	}
	@Override
	public List<OrgIp> getAllOrganizationIp() {
		return orgSSOMapper.getAllOrganizationIp();
	}

	@Override
	public List<OrgDownIp> getAllOrgDownIp() {
		return orgSSOMapper.getAllOrgDownIp();
	}
	
	@Override
	public List<OrgChannel> getAllOrganizationChannel() {
		return orgSSOMapper.getAllOrganizationChannel();
	}
	@Override
	public String getOrganizationToWapFlagById(String orgId) {
		return orgSSOMapper.getOrganizationToWapFlagById(orgId);
	}


	
	
	

}
