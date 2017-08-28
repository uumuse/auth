package com.kuke.auth.snslogin.quartz;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuke.auth.snslogin.bean.SnsUser;


/**
 * 
 *<pre>
 * 概　述:
 *  	维护在内存中的，所有Sns信息
 * @author maxin
 * @version 最后修改日期 2012-3-1
 * 编辑人: 
 * 编辑日期: 2012-3-1
 * 编辑说明: 维护在内存中的，所有机构信息
 *</pre>
 */
public class ApplicationSns {
	
	static final Logger logger = LoggerFactory.getLogger(ApplicationSns.class);
	
	/**
	 * Sns集合
	 * key 		sns_rand_id
	 * value    SnsUser
	 */
	protected static ConcurrentMap<String, SnsUser> snsMap;
		
	private static ApplicationSns instance = null;

	public static synchronized ApplicationSns getInstance() {
		if (instance == null) {
			instance = new ApplicationSns();
		}
		return instance;
	}

	public synchronized boolean SNSMapIn(String sns_id, SnsUser snsUser) {
		if (snsMap == null) {
			snsMap = new ConcurrentHashMap<String, SnsUser>();
		}
		snsMap.put(sns_id, snsUser);
		return true;
	}
	
	public synchronized boolean SNSMapOut(String sns_id) {
		try{
			snsMap.remove(sns_id);
		}catch (Exception e) {
		}
		return true;
	}
	
	public synchronized SnsUser SNSMapInfo(String ssoid) {
		try {
			return snsMap.get(ssoid);
		} catch (Exception e) {
		}
		return null;
	}
	
}
