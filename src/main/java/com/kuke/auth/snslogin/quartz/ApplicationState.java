package com.kuke.auth.snslogin.quartz;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuke.auth.snslogin.quartz.ApplicationState;

/**
 * 
 *<pre>
 * 概　述:
 *  	维护在内存中的，所有State信息
 * @author maxin
 * @version 最后修改日期 2012-3-1
 * 编辑人: 
 * 编辑日期: 2012-3-1
 * 编辑说明: 维护在内存中的，所有机构信息
 *</pre>
 */
public class ApplicationState {

	
	static final Logger logger = LoggerFactory.getLogger(ApplicationState.class);
	
	/**
	 * State集合
	 * key 		state_id
	 * value    redirect_url
	 */
	protected static ConcurrentMap<String, String> stateMap;
		
	private static ApplicationState instance = null;

	public static synchronized ApplicationState getInstance() {
		if (instance == null) {
			instance = new ApplicationState();
		}
		return instance;
	}
	
	
	private ApplicationState(){
		
	}

	public synchronized boolean StateMapIn(String state_id, String redirect_url) {
		if (stateMap == null) {
			stateMap = new ConcurrentHashMap<String, String>();
		}
		stateMap.put(state_id, redirect_url);
		return true;
	}
	
	public synchronized boolean StateMapOut(String state_id) {
		try{
			stateMap.remove(state_id);
		}catch (Exception e) {
		}
		return true;
	}
	
	public synchronized String StateMapInfo(String state_id) {
		try {
			return stateMap.get(state_id);
		} catch (Exception e) {
		}
		return null;
	}
	
}
