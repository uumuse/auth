package com.kuke.auth.ssologin.service;

import java.util.List;

import com.kuke.auth.ssologin.bean.OrgChannel;
import com.kuke.auth.ssologin.bean.OrgDownIp;
import com.kuke.auth.ssologin.bean.OrgIp;
import com.kuke.auth.ssologin.bean.Organization;


public interface OrgService {

	public Organization getOrganizationById(String orgId);
	
	public String getOrganizationToWapFlagById(String orgId);
	
	public Organization getOrganizationByOrgUserId(String name,String pwd);
	
	public List<OrgIp> getAllOrganizationIp();
	
	public List<OrgDownIp> getAllOrgDownIp();

	public List<OrgChannel> getAllOrganizationChannel();

}
