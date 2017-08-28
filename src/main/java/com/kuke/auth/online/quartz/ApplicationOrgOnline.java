package com.kuke.auth.online.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuke.auth.online.bean.OrgOnline;
import com.kuke.auth.online.service.OnlineService;
import com.kuke.auth.online.service.OnlineServiceImpl;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.core.service.SpringContextHolder;
import com.kuke.util.DateUtil;
import com.kuke.util.IdGenerator;







public class ApplicationOrgOnline {
	
	static final Logger logger = LoggerFactory.getLogger(ApplicationOrgOnline.class);
	
	/**
	 * 机构当前在线人数
	 *  org_id,<channleId<ssoID,loginTime>
	 *  
	 */
	protected static ConcurrentMap<String, Map<String,Map<String,String>>> orgOnlineMap;
	
	
	private static ApplicationOrgOnline instance = null;

	
	public static synchronized ApplicationOrgOnline getInstance() {
		if (instance == null) {
			instance = new ApplicationOrgOnline();
		}
		return instance;
	}

	
	/**
	 * ORG_ID、ORGSSOID、CHANNEL_ID更新ORG、Channel最后操作时间
	 */
	public synchronized boolean orgSSOUpdateTime(String org_id,
			String channel_id, String ssoid) {
		if (null == orgOnlineMap)
			orgOnlineMap = new ConcurrentHashMap<String, Map<String, Map<String, String>>>();
		Map<String, Map<String, String>> tempOrgMap = null;
		Map<String, String> tempChannelMap = null;
		try {
			String currentTime = DateUtil.date2str(new Date());
			tempOrgMap = orgOnlineMap.get(org_id);
			if (null == tempOrgMap)
				tempOrgMap = new HashMap<String, Map<String, String>>();
			tempChannelMap = tempOrgMap.get(channel_id);
			if (null == tempChannelMap)
				tempChannelMap = new HashMap<String, String>();
			tempChannelMap.put(ssoid, currentTime);
			tempOrgMap.put(channel_id, tempChannelMap);
			orgOnlineMap.put(org_id, tempOrgMap);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * 退出后删除在线人数
	 */
	public synchronized void orgSSODeleteTime(String org_id, String channel_id, String sso_id) {
		Map<String, Map<String, String>> map_org = null;
		Map<String, String> map_channel = null;
		if (null != orgOnlineMap) {
			map_org = orgOnlineMap.get(org_id);
			if (null != map_org && map_org.size() > 0) {
				map_channel = map_org.get(channel_id);
				if (null != map_channel && map_channel.size() > 0) {
					map_channel.remove(sso_id);
					map_org.put(channel_id, map_channel);
					orgOnlineMap.put(org_id, map_org);
				}
			}
		}
	}
	
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2012-3-9
	 * 描　述:
	 *    操作ORG在线人数内存
	 * </pre>
	 */
	private void operateOrgOnlineMap() {
		try{
		String org_id = "";
		String channel_id = "";
		Map<String, Map<String, String>> channelMap = null;
		Map<String, String> SSOMap = null;
		if (null != orgOnlineMap) {
			Set<String> orgKey = orgOnlineMap.keySet();
			Iterator<String> orgIdIter = orgKey.iterator();
			while (orgIdIter.hasNext()) {
				org_id = orgIdIter.next();
				if("".equals(org_id))
					break;
				channelMap = orgOnlineMap.get(org_id);
				if (null == channelMap)
					break;
				Set<String> SSOKey = channelMap.keySet();
				Iterator<String> SSOIter = SSOKey.iterator();
				while (SSOIter.hasNext()) {
					channel_id = SSOIter.next();
					SSOMap = channelMap.get(channel_id);
					if (null == SSOMap)
						break;
					//清除当前机构、频道下的过期在线人数
					SSOMap = clearOrgSSOMapTime(SSOMap);
					channelMap.put(channel_id, SSOMap);
					orgOnlineMap.put(org_id, channelMap);		
					//当前机构用户在线人数存入DB
					dbOrgOnlineMap(org_id, channel_id, SSOMap);					
					//多线程清除当前机构、频道下的权限内存
					OrgThread org = new OrgThread(org_id, channel_id, SSOMap);
					org.start();

				}
			}
		}}catch(Exception ex){
			logger.debug("添加机构在线人数异常/org");
		}
	}
	
	
	/**
	 * 内存清除300秒
	 */
	private static Map<String, String> clearOrgSSOMapTime(Map<String, String> SSOMap) {
		Map<String, String> map = new HashMap<String, String>();
		map = SSOMap;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentTime = new Date();
		Iterator<Entry<String, String>> it = SSOMap.entrySet().iterator();
		try {
			int outSecond = Integer.parseInt(String.valueOf(PropertiesHolder.getContextProperty("online.out.second")));
			while (it.hasNext()) {
				Map.Entry<String,String> pairs = (Map.Entry<String,String>) it.next();
				Date mapTime = sdf.parse(pairs.getValue());
				if (DateUtil.getSecondValue4Sub(mapTime, currentTime) > outSecond) {	
					it.remove();
					map.remove(pairs.getKey());			
				}
			}							
		} catch (Exception e) {
			return map;
		}
		return map;
	}
	
	
	/**
	 * 内存入库
	 */
	private static void dbOrgOnlineMap(String org_id,String channel_id,
			Map<String, String> SSOMap) {
		try {
			int org_number = 0;
			OnlineService onlineService = SpringContextHolder.getBean(OnlineServiceImpl.class);
			Set<String> SSOKey = SSOMap.keySet();
			Iterator<String> iterator = SSOKey.iterator();
			while (iterator.hasNext()) {
				if(!"".equals(iterator.next()))
					org_number++;
			}	
			if (org_number != 0) {
				OrgOnline orgOnline = new OrgOnline();
				orgOnline.setId(IdGenerator.getUUIDHex32());
				orgOnline.setChannel_id(channel_id);
				orgOnline.setOrg_id(org_id);
				orgOnline.setUser_number(String.valueOf(org_number));
				onlineService.addOrgOnline(orgOnline);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getOrgOnlineMap() {
		if (null == orgOnlineMap)
			return "";
		return orgOnlineMap.toString();
	}
	
	public void reloadInit() {
		logger.debug("刷新机构在线人数");
		operateOrgOnlineMap();
	}
	
class OrgThread extends Thread {
		
	private String org_id;
		
	private String channel_id;
		
	private Map<String, String> map;
	
	public OrgThread() {

	}
		
		
	public OrgThread(String org_id, String channel_id,Map<String, String> map) {
		this.org_id = org_id;
		this.channel_id = channel_id;
		this.map = map;
	}

//	public void run() {
//		try {
//			ApplicationOrg.getInstance().operateOnlineMap(org_id, channel_id, map);
//		} catch (Exception e) {
//
//		}
//	}	
}
		
}
