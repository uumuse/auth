package com.kuke.auth.ssologin.quartz.org;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuke.auth.ssologin.bean.OrgChannel;
import com.kuke.auth.ssologin.bean.OrgDownIp;
import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.auth.ssologin.service.OrgService;
import com.kuke.auth.ssologin.service.OrgServiceImpl;
import com.kuke.auth.ssologin.util.OrgAuthUtils;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.core.redis.RedisUtil;
import com.kuke.core.service.SpringContextHolder;
import com.kuke.util.DateUtil;



public class RetrievalOrg {
	
	static Logger logger = LoggerFactory.getLogger(RetrievalOrg.class);
	
	private static RedisUtil redisUtil = SpringContextHolder.getBean("redisUtil");
	
	/**
	 * <pre>
	 * 创建于: 2015-4-1
	 * 描　述:
	 *    通过IP得到机构信息
	 * </pre>
	 * @param remoteAdd
	 * @return Organization
	 */
	public static Organization getOrgInfoAllowIp(String remoteAdd) {
		
		String returnOrgId = "";
		String orgId = "";
		String startIp = "";
		String endIp = "";
		Organization organization = new Organization();
		organization.setOrg_status(KuKeAuthConstants.FAILED);
		//先从redis中取出所有的key,并将Set转成List
		Set<String> orgIpSet = new HashSet<String>();
		orgIpSet = redisUtil.getKeys("org_ip_*");
		List<String> redisKey = new ArrayList<String>(orgIpSet);
		//从redis中取出orgIp集合
		List<Map<String,String>> orgIpListMap = redisUtil.batchHashGetAll(redisKey);
		Iterator<Map<String,String>> tempOrgIpIter = orgIpListMap.iterator();
		boolean flag = false;
		while(tempOrgIpIter.hasNext()){
			if (!"".equals(returnOrgId)) {
				break;
			}
			Map<String,String> tempMap = tempOrgIpIter.next();
			Iterator<String> tempMapKeyIter = tempMap.keySet().iterator();
			while(tempMapKeyIter.hasNext()){
				String value = tempMap.get(tempMapKeyIter.next());
				if(StringUtils.isNotBlank(value));
				String [] mapValue = value.split(",");
//				System.out.println(mapValue.toString());
				startIp = mapValue[0];
//				if(startIp.equals("116.13.208.0")||startIp=="116.13.208.0"){
//					System.out.println(startIp);
//				}
//				if(startIp.equals("190.222.0.0")||startIp=="190.222.0.0"){
//					System.out.println(startIp);
//				}
//				System.out.println(startIp);
				endIp = mapValue[1];
//				System.out.println(endIp);
				if(mapValue.length>2){
					orgId = mapValue[2];
				}else{
					orgId = "";
				}
//				System.out.println(orgId);
//				System.out.println("startIp:"+startIp+"endIp:"+endIp+"orgId:"+orgId);
				if (OrgAuthUtils.getInstance().ipCompare(startIp, remoteAdd, endIp)) {
					returnOrgId = orgId;
					flag = true;
					System.out.println("orgId:"+orgId);
					break;
				} else {
					returnOrgId = "";
				}
				if(flag){
					break;
				}
			}
		}
		if (!"".equals(returnOrgId)) {
			organization = getOrgInfoById(returnOrgId);
			organization.setOrg_status(KuKeAuthConstants.SUCCESS);
		}
		return organization;
	}

	
	/**
	 * <pre>
	 * 创建人: zhangjiran
	 * 创建于: 2015-4-1
	 * 描　述:
	 *    通过IP得到机构下载ip账户信息
	 * </pre>
	 * @param remoteAdd
	 * @return OrgDownIp
	 */
	public static OrgDownIp getOrgDownIp(String remoteAdd) {
		OrgDownIp orgDownIp = null;
		String orgId = "";
		String start_ip = "";
		String end_ip = "";
		String isused = "";
		//先从redis中取出所有的key,并将Set转成List
		Set<String> orgDownIpSet = new HashSet<String>();
		orgDownIpSet = redisUtil.getKeys("org_down_ip_*");
		List<String> orgDownIpIdList = new ArrayList<String>(orgDownIpSet);
		
		List<Map<String,String>> orgDownIpMapList = new ArrayList<Map<String,String>>();
		orgDownIpMapList = redisUtil.batchHashGetAll(orgDownIpIdList);
		Iterator<Map<String,String>> tempOrgDownIpIter = orgDownIpMapList.iterator();
		boolean flag = false;
		while (tempOrgDownIpIter.hasNext()) {
			if (orgDownIp!=null) {
				break;
			}
			Map<String,String> orgDownIpMap = tempOrgDownIpIter.next();
			Iterator<String> orgDownIpIdIter = orgDownIpMap.keySet().iterator();
			while(orgDownIpIdIter.hasNext()){
				String value = orgDownIpMap.get(orgDownIpIdIter.next());
				if(StringUtils.isNotBlank(value)){
					String [] mapValue = value.split(",");
					orgId = mapValue[0];
					start_ip = mapValue[1];
					end_ip = mapValue[2];
					isused = mapValue[3];
					try {
						if (OrgAuthUtils.getInstance().ipCompare(start_ip, remoteAdd, end_ip)) {
							orgDownIp = new OrgDownIp();
							orgDownIp.setOrg_id(orgId);
							orgDownIp.setStart_ip(start_ip);
							orgDownIp.setEnd_ip(end_ip);
							orgDownIp.setIsused(Integer.valueOf(isused));
							flag = true;
							break;
						} else {
							orgDownIp = null;
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(flag){
					break;
				}
			}		
		}
		return orgDownIp;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2012-3-8
	 * 描　述:
	 *    通过ID得到机构信息
	 * </pre>
	 * @param orgId
	 * @return
	 */
	public static Organization getOrgInfoById(String orgId) {

		Organization organization = new Organization();
		organization.setOrg_status(KuKeAuthConstants.FAILED);
		if (!"".equals(orgId)) {
			OrgService orgService = SpringContextHolder.getBean(OrgServiceImpl.class);
			organization = orgService.getOrganizationById(orgId);
			if (null != organization) {
				organization.setOrg_status(KuKeAuthConstants.SUCCESS);
			}
		}
		return organization;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2012-3-8
	 * 描　述:
	 *    通过IP得到机构信息
	 * </pre>
	 * @param orgId
	 * @return
	 */
	public static Organization getOrgInfoByOrgUserId(String name,String pwd) {
		Organization organization = new Organization();
		organization.setOrg_status(KuKeAuthConstants.FAILED);
		if (!"".equals(name) && !"".equals(pwd)) {
			OrgService orgService = SpringContextHolder.getBean(OrgServiceImpl.class);
			organization = orgService.getOrganizationByOrgUserId(name, pwd);
			if (null != organization) {
				organization.setOrg_status(KuKeAuthConstants.SUCCESS);
			}
		}
		return organization;
	}
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2012-3-7
	 * 描　述:
	 *     校验频道信息是否购买
	 * </pre>
	 * @param org_id
	 * @param channel_id
	 * @return
	 */
	public static OrgChannel getOrgChannelAllowOpen(String org_id,String channel_id) {
		Map<String,String> orgChannelInfoMap;
		OrgChannel orgChannel = new OrgChannel();
		//max_online_num,end_date组合字符串
		String orgChannelInfo = "";
		orgChannel.setChannelStatus(KuKeAuthConstants.CLOSE);
		try {
			//从redis中取出机构频道信息
			String redisKey = "org_channel_"+org_id;
			//判断key是否存在于redis
			boolean isExist = redisUtil.containsKey(redisKey);
			logger.debug("isExistisExistisExistisExist============="+isExist);
			if(isExist){
				orgChannelInfoMap = redisUtil.hashGetAll(redisKey);
				orgChannelInfo = orgChannelInfoMap.get(channel_id);
				logger.debug("orgChannelInfoorgChannelInfo============="+orgChannelInfo);
				if(StringUtils.isNotBlank(orgChannelInfo)){
					//redis中的频道信息
//					String orgId = orgChannelInfo.split(",")[0];
//					String channelId = orgChannelInfo.split(",")[1];
					String maxOnlineNum = orgChannelInfo.split(",")[0];
					String endDate = orgChannelInfo.split(",")[1];
					orgChannel.setChannelStatus(KuKeAuthConstants.SUCCESS);
					orgChannel.setChannel_id(channel_id);
					orgChannel.setOrg_id(org_id);
					orgChannel.setMax_online_num(maxOnlineNum);
					orgChannel.setEnd_date(endDate);
				}
				return orgChannel;
			}else{
				return orgChannel;
			}
		} catch (Exception e) {
			logger.debug("检验信息是否购买错误"+e);
			return orgChannel;
		}
	}
	
	

	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2012-3-7
	 * 描　述:
	 *    校验频道信息是否过期
	 * </pre>
	 * @param org_id
	 * @param channel_id
	 * @param tempOrgChannel
	 * @return
	 */
	public static OrgChannel getOrgChannelAllowDate(String org_id,String channel_id, OrgChannel tempOrgChannel) {
		if(null == tempOrgChannel){
			tempOrgChannel = getOrgChannelAllowOpen(org_id, channel_id);
		}
		if(KuKeAuthConstants.SUCCESS.equals(tempOrgChannel.getChannelStatus())){
			tempOrgChannel.setChannelStatus(KuKeAuthConstants.OUTDATE);
			String date = tempOrgChannel.getEnd_date();
			if (null != date && !"".equals(date)) {
				try {
					if (DateUtil.dataCompare(DateUtil.str2date(date,"yyyy-MM-dd"), new Date())) {
						tempOrgChannel.setChannelStatus(KuKeAuthConstants.SUCCESS);
					}
				} catch (Exception e) {
					return tempOrgChannel;
				}
			}
		}
		return tempOrgChannel;
	}
	
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2012-3-7
	 * 描　述:
	 *     校验频道是否超过最大在线人数
	 * </pre>
	 * @param org_id
	 * @param channel_id
	 * @param tempOrgChannel
	 * @return
	 */
	public static OrgChannel getOrgChannelAllowOnline(String org_id,
			String channel_id, OrgChannel tempOrgChannel) {
		if (null == tempOrgChannel)
			tempOrgChannel = getOrgChannelAllowDate(org_id, channel_id,tempOrgChannel);
		return tempOrgChannel;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}