package com.kuke.auth.ssologin.quartz.user;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 *<pre>
 * 概　述:
 *  	维护在内存中的，所有机构信息
 * @author maxin
 * @version 最后修改日期 2012-3-1
 * 编辑人: 
 * 编辑日期: 2012-3-1
 * 编辑说明: 维护在内存中的，所有用户信息
 *</pre>
 */
public class ApplicationUser {
	
	static final Logger logger = LoggerFactory.getLogger(ApplicationUser.class);
	

	/**
	 * 机构票据登录集合
	 * key 		sso_id
	 * value    org_id
	 */
	protected static ConcurrentMap<String, String> userLoginMap;
	
	
	private static ApplicationUser instance = null;
	
	
	private ApplicationUser(){
		
	}

	
	public static synchronized ApplicationUser getInstance() {
		if (instance == null) {
			instance = new ApplicationUser();
		}
		return instance;
	}

	/**
	 * 加入 个人票据登录集合
	 * @return
	 */
	public synchronized boolean userSSOLoginMapIn(String user_id, String ssoid) {
		userLoginMap.put(user_id, ssoid);
		return true;
	}
	
	/**
	 * 退出 个人票据登录集合
	 * @return
	 */
	public synchronized boolean userSSOLoginMapOut(String user_id) {
		userLoginMap.remove(user_id);
		return true;
	}
	
	
	

	
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2012-3-9
	 * 描　述:
	 * 	        初始化个人票据集合
	 * </pre>
	 */
	public void initUserAuthMap() {
		if (null == userLoginMap) {
			userLoginMap = new ConcurrentHashMap<String, String>();
		}
	}
	
	


	
	public String getUserLoginMap() {
		if (null == userLoginMap)
			return "";
		return userLoginMap.toString();
	}
	

	
}
