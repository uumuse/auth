package com.kuke.auth.ssologin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kuke.auth.ssologin.bean.OrgChannel;
import com.kuke.auth.ssologin.bean.OrgDownIp;
import com.kuke.auth.ssologin.bean.OrgIp;
import com.kuke.auth.ssologin.bean.Organization;


public interface OrgSSOMapper {

	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-2-17
	 * 描　述:
	 *    	读取机构基础信息By OrgId
	 * </pre>
	 * @param org_id
	 * @return
	 */
	public Organization getOrganizationById(@Param("orgId")String orgId);
	
	
	/***
	 * g根据机构id获取是否可以直接跳到手机网页的标识（广州图书馆需求）
	 * @param orgId
	 * @return
	 */
	public String getOrganizationToWapFlagById(@Param("orgId")String orgId);
	/**
	 * 
	 * 读取机构基础信息 by orguserid 
	 * @param name
	 * @param pwd
	 * @return 
	 * @author 高勇
	 * @date, 2013-6-9 下午12:36:56
	 *
	 */
	public Organization getOrganizationByOrgUserId(@Param("name")String name,@Param("pwd")String pwd);
 
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-2-17
	 * 描　述:
	 *     加载所有机构登录IP库
	 * </pre>
	 * 
	 * @return
	 */
	public List<OrgIp> getAllOrganizationIp();
	
	
	/**
	 * 
	 * <pre>
	 * 创建人: tc
	 * 创建于: 2013-8-8
	 * 描　述:
	 *     加载所有机构下载IP库
	 * </pre>
	 * 
	 * @return
	 */
	public List<OrgDownIp> getAllOrgDownIp();
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-2-17
	 * 描　述:
	 *     加载所有机构频道库
	 * </pre>
	 * @return
	 */
	public List<OrgChannel> getAllOrganizationChannel();
	

}
