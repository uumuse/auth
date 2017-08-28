package com.kuke.auth.ssologin.quartz.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuke.auth.ssologin.bean.OrgChannel;
import com.kuke.auth.ssologin.bean.OrgDownIp;
import com.kuke.auth.ssologin.bean.OrgIp;
import com.kuke.auth.ssologin.service.OrgService;
import com.kuke.auth.ssologin.service.OrgServiceImpl;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.core.redis.RedisUtil;
import com.kuke.core.service.SpringContextHolder;

public class RedisOrgIpReUnion {
	
private  RedisUtil redisUtil = SpringContextHolder.getBean(RedisUtil.class);
	
	static final Logger logger = LoggerFactory.getLogger(ApplicationOrg.class);
	

	
	private static ApplicationOrg instance = null;

	
	public static synchronized ApplicationOrg getInstance() {
		if (instance == null) {
			instance = new ApplicationOrg();
		}
		return instance;
	}

	/**
	 * <pre>
	 * 创建人: zhangjiran
	 * 创建于: 2015-3-31
	 * 描　述:
	 *    初始化机构IP信息
	 * </pre>
	 */
	@Test
	private void initOrgIpMap() {
		Map<String, List<OrgIp>> tempOrgIpMap = new HashMap<String, List<OrgIp>>();
		OrgService orgService = SpringContextHolder.getBean(OrgServiceImpl.class);
		List<OrgIp> orgIpsDB = orgService.getAllOrganizationIp();
		Iterator<OrgIp> orgIpsIterator = orgIpsDB.iterator();
		
		String orgId = "";
		
		//将同一机构下的数据分组放入集合 
		while(orgIpsIterator.hasNext()){
			OrgIp orgIp = orgIpsIterator.next();
//			if(redisUtil.containsKey("org_ip_"+orgIp.getOrg_id())){
//				redisUtil.delKeysLike("org_ip_"+orgIp.getOrg_id());
//			}
			List<OrgIp> orgIpList = tempOrgIpMap.get(orgIp.getOrg_id());
			if(orgIpList == null){
				orgIpList = new ArrayList<OrgIp>();
			}
			orgIpList.add(orgIp);
			tempOrgIpMap.put(orgIp.getOrg_id(), orgIpList);
		}
		
		HashMap<String,String> tempMap = new HashMap<String,String>();
		Iterator<String> orgIdItar = tempOrgIpMap.keySet().iterator();
		while(orgIdItar.hasNext()){
			orgId = orgIdItar.next();
			List<OrgIp> orgIpList = tempOrgIpMap.get(orgId);
			if(orgIpList != null && orgIpList.size() > 0){
				for(OrgIp orgIp : orgIpList){
					tempMap.put(orgIp.getId(),orgIp.getStart_ip()+","+orgIp.getEnd_ip()+","+orgIp.getOrg_id());
				}
				//将机构IP信息存入redis
				
				redisUtil.hashMultipleSet("org_ip_"+orgId, tempMap);
				if(tempMap != null){
					tempMap.clear();
				}
			}
		}
	}
	
	
	/**
	 * <pre>
	 * 创建人: zhangjiran
	 * 创建于: 2015-3-31
	 * 描　述:
	 *    初始化机构下载IP信息
	 * </pre>
	 */
	private void initOrgDownIpMap() {
		Map<String, List<OrgDownIp>> tempOrgIpMap = new HashMap<String, List<OrgDownIp>>();
		OrgService orgService = SpringContextHolder.getBean(OrgServiceImpl.class);
		List<OrgDownIp> orgIpsDB = orgService.getAllOrgDownIp();
		Iterator<OrgDownIp> orgIpsIterator = orgIpsDB.iterator();
		Map<String,String> tempMap = new HashMap<String,String>();
		//将同一机构下的数据分组放入集合 
		while(orgIpsIterator.hasNext()){
			OrgDownIp orgDownIp = orgIpsIterator.next();
			List<OrgDownIp> orgDownIpList = tempOrgIpMap.get(orgDownIp.getOrg_id());
			if(orgDownIpList == null){
				orgDownIpList = new ArrayList<OrgDownIp>();
			}
			orgDownIpList.add(orgDownIp);
			tempOrgIpMap.put(orgDownIp.getOrg_id(), orgDownIpList);
		}
		
		String orgId = "";
		//将机构下载IP信息存入redis
		Iterator<String> orgIdIter = tempOrgIpMap.keySet().iterator();
		while(orgIdIter.hasNext()){
			orgId =orgIdIter.next();
			List<OrgDownIp> orgDownIpList = tempOrgIpMap.get(orgId);
			if(orgDownIpList != null && orgDownIpList.size() >0){
				for(OrgDownIp orgDownIp : orgDownIpList){
					tempMap.put(String.valueOf(orgDownIp.getId()),orgDownIp.getOrg_id()+","+orgDownIp.getStart_ip()+","+orgDownIp.getEnd_ip()+","+orgDownIp.getIsused()+","+orgDownIp.getLast_date());	
				}
				redisUtil.hashMultipleSet("org_down_ip_"+orgId,tempMap);
				if(tempMap != null){
					tempMap.clear();
				}
			}
		}
	}
	
	
	
	/**
	 * 
	 * <pre>
	 * 创建人: zhangjiran
	 * 创建于:  2015-04-15
	 * 描　述:
	 *    	初始化机构CHANNEL信息
	 * </pre>
	 */
	private void initOrgChannelMap() {
		Map<String, List<OrgChannel>> tempOrgChannelMap = new HashMap<String, List<OrgChannel>>();
		OrgService orgService = SpringContextHolder.getBean(OrgServiceImpl.class);
		List<OrgChannel> orgChannelsDB = orgService.getAllOrganizationChannel();
		Iterator<OrgChannel> orgChannelsIterator = orgChannelsDB.iterator();
		
		//将数据按照机构分组放入集合
		while (orgChannelsIterator.hasNext()) {
			OrgChannel orgChannel = orgChannelsIterator.next();
			orgChannel.setChannelStatus(KuKeAuthConstants.FAILED);
			if(StringUtils.isNotBlank(orgChannel.getOrg_id())){
				List<OrgChannel> OrgChannelList = tempOrgChannelMap.get(orgChannel.getOrg_id());
				if(OrgChannelList == null){
					OrgChannelList = new ArrayList<OrgChannel>();
				}
				OrgChannelList.add(orgChannel);
				tempOrgChannelMap.put(orgChannel.getOrg_id(), OrgChannelList);
			}
		}
		//将频道信息存入redis
		Iterator<String> orgIdIter = tempOrgChannelMap.keySet().iterator();
		Map<String,String> tempMap = new HashMap<String,String>();
		String hashKey = "";
		String hashValue = "";
		String orgId = "";
		while(orgIdIter.hasNext()){
			orgId = orgIdIter.next();
			if(orgId.equals("24924F50147E11E38F5096E4C353C1F6")){
				System.out.println(orgId);
			}
			List<OrgChannel> orgChannelList = tempOrgChannelMap.get(orgId);
			for(OrgChannel orgChannel : orgChannelList){
				hashKey = orgChannel.getChannel_id();
//				hashValue = orgChannel.getOrg_id()+","+orgChannel.getChannel_id()+","+orgChannel.getMax_online_num()+","+orgChannel.getEnd_date();
				hashValue = orgChannel.getMax_online_num()+","+orgChannel.getEnd_date();
				tempMap.put(hashKey, hashValue);
			}
			//将同一机构下的频道信息存入redis
			redisUtil.hashMultipleSet("org_channel_"+orgId, tempMap);
			if(tempMap != null && tempMap.size() > 0){
				tempMap.clear();
			}
		}
	}
	
	

	
	
	/**
	 * 
	 * <pre>
	 * 创建人: zhangjiran
	 * 创建于: 2015-04-15
	 * 描　述:
	 *     重新加载数据
	 * </pre>
	 */
	public void reloadInit(){
		try{
		initOrgIpMap();
		initOrgDownIpMap();
		initOrgChannelMap();
		logger.debug("加载机构 IP 数据" );
		logger.debug("加载机构下载 IP 数据" );
		logger.debug("加载机构 Channel 数据" );
		}catch(Exception ex){
			ex.printStackTrace();
			logger.debug("加载机构数据异常/org_data");
		}
	}
	

	

}
