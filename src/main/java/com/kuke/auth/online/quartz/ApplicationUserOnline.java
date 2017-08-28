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

import com.kuke.auth.online.bean.UserOnline;
import com.kuke.auth.online.service.OnlineService;
import com.kuke.auth.online.service.OnlineServiceImpl;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.core.service.SpringContextHolder;
import com.kuke.util.DateUtil;


public class ApplicationUserOnline {
	
	static final Logger logger = LoggerFactory.getLogger(ApplicationUserOnline.class);
	
	/**
	 * 普通用户当前在线人数
	 * userId,<channleId,loginTime>
	 */
	protected static ConcurrentMap<String, Map<String, String>> userOnlineMap;
	
	
	private static ApplicationUserOnline instance = null;

	
	public static synchronized ApplicationUserOnline getInstance() {
		if (instance == null) {
			instance = new ApplicationUserOnline();
		}
		return instance;
	}

	
	/**
	 * USER_ID、CHANNEL_ID更新ORG、Channel最后操作时间
	 */
	public synchronized boolean userSSOUpdateTime(String user_id,
			String channel_id) {
		if (null == userOnlineMap)
			userOnlineMap = new ConcurrentHashMap<String, Map<String, String>>();
		Map<String, String> tempChannelMap = null;
		try {
			String currentTime = DateUtil.date2str(new Date());
			tempChannelMap = userOnlineMap.get(user_id);
			if(null==tempChannelMap)
				tempChannelMap = new HashMap<String, String>();
			tempChannelMap.put(channel_id, currentTime);
			userOnlineMap.put(user_id, tempChannelMap);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	
	
	
	/**
	 * 退出后删除在线人数
	 */
	public synchronized void userSSODeleteTime(String user_id, String channel_id) {
		Map<String, String> map = null;
		if (null != userOnlineMap) {
			map = userOnlineMap.get(user_id);
			if (null != map && map.size() > 0) {
				map.remove(channel_id);
				userOnlineMap.put(user_id, map);
			}
		}
	}
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2012-3-12
	 * 描　述:
	 *    操作USER在线人数内存
	 * </pre>
	 * @param currentTime
	 */
	private void operateUserOnlineMap() {
		try{
		Date currentTime = new Date();
		String user_id = "";
		Map<String, String> SSOMap = null;
		if (null != userOnlineMap) {
			Set<String> userKey = userOnlineMap.keySet();
			Iterator<String> userIdIter = userKey.iterator();
			while (userIdIter.hasNext()) {
				user_id = userIdIter.next();
				if ("".equals(user_id))
					break;
				SSOMap = userOnlineMap.get(user_id);
				if (null == SSOMap)
					break;
				//清除当前用户、频道下的过期在线人数
				SSOMap = clearUserSSOMapTime(currentTime, SSOMap);
				userOnlineMap.put(user_id, SSOMap);
				//当前个人用户在线人数存入DB
				dbUserOnlineMap(user_id, SSOMap);
			}		
		}	}catch(Exception ex){
			logger.debug("添加个人在线人数异常/user");
		}
	}
	
	
	/**
	 * 内存清除300秒
	 */
	private static Map<String, String> clearUserSSOMapTime(Date currentTime,
			Map<String, String> SSOMap) {
		Map<String, String> map = new HashMap<String, String>();
		map = SSOMap;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Iterator<Entry<String, String>> it = SSOMap.entrySet().iterator();
		try {
			int outSecond = Integer.parseInt(String.valueOf(PropertiesHolder.getContextProperty("online.out.second")));
			while (it.hasNext()) {
				Map.Entry<String,String> pairs = (Map.Entry<String,String>) it.next();
				Date mapTime = sdf.parse(pairs.getValue());
				if (DateUtil.getSecondValue4Sub(mapTime, currentTime) > outSecond) {	
					it.remove();		
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
	private static void dbUserOnlineMap(String user_id,
			Map<String, String> SSOMap) {
		try {
			OnlineService onlineService = SpringContextHolder.getBean(OnlineServiceImpl.class);
			Set<String> SSOKey = SSOMap.keySet();
			Iterator<String> iterator = SSOKey.iterator();
			while(iterator.hasNext()){
				try{
					UserOnline userOnline = new UserOnline();
					userOnline.setChannel_id(iterator.next());
					userOnline.setUser_id(user_id);
					onlineService.addUserOnline(userOnline);					
				}catch (Exception e) {
				}

			}			
		} catch (Exception e) {
		}
	}


	public String getUserOnlineMap() {
		if (null == userOnlineMap)
			return "";
		return userOnlineMap.toString();
	}
	
	public void reloadInit() {
		logger.debug("刷新个人在线人数");
		operateUserOnlineMap();
	}		
}
