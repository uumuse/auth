package com.kuke.auth.snslogin.quartz;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuke.auth.login.bean.User;

/**
 * 
 *<pre>
 * 概　述:
 *  	维护在内存中的，所有通过扫码登录的用户信息
 * @author wanyj
 */
public class ApplicationUser {
	
	static final Logger logger = LoggerFactory.getLogger(ApplicationUser.class);
	
	/**
	 * 扫码登录的用户集合
	 * key 		uuid
	 * value    SnsUser
	 */
	protected static ConcurrentMap<String, User> userMap;
		
	private static ApplicationUser instance = null;

	public static synchronized ApplicationUser getInstance() {
		if (instance == null) {
			instance = new ApplicationUser();
		}
		return instance;
	}
	/**
	 * 登录用户绑定uuid
	 * @param uuid
	 * @param user
	 * @return
	 */
	public synchronized boolean userMapIn(String uuid, User user) {
		if (userMap == null) {
			userMap = new ConcurrentHashMap<String, User>();
		}
		userMap.put(uuid, user);
		return true;
	}
	/**
	 * 登录完后,删除相应的登录用户
	 * @param uuid
	 * @return
	 */
	public synchronized boolean userMapOut(String uuid) {
		try{
			userMap.remove(uuid);
		}catch (Exception e) {
		}
		return true;
	}
	/**
	 * 通过uuid得到相应的登录用户
	 * @param uuid
	 * @return
	 */
	public synchronized User userMapInfo(String uuid) {
		try {
			return userMap.get(uuid);
		} catch (Exception e) {
		}
		return null;
	}
}
