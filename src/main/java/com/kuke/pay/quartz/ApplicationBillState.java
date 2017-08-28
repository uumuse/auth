package com.kuke.pay.quartz;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuke.auth.snslogin.quartz.ApplicationState;

/**
 * 
 *<pre>
 * 概　述:维护在内存中的，所有微信扫码后订单的状态信息
 *</pre>
 */
public class ApplicationBillState {

	
	static final Logger logger = LoggerFactory.getLogger(ApplicationBillState.class);
	
	/**
	 * State集合
	 * key 		keyword
	 * value    statusid
	 */
	protected static ConcurrentMap<String, String> stateMap;
		
	private static ApplicationBillState instance = null;

	public static synchronized ApplicationBillState getInstance() {
		if (instance == null) {
			instance = new ApplicationBillState();
		}
		return instance;
	}
	
	
	private ApplicationBillState(){
		
	}

	public synchronized boolean StateMapIn(String keyword, String statusid) {
		if (stateMap == null) {
			stateMap = new ConcurrentHashMap<String, String>();
		}
		stateMap.put(keyword, statusid);
		return true;
	}
	
	public synchronized boolean StateMapOut(String keyword) {
		try{
			stateMap.remove(keyword);
		}catch (Exception e) {
		}
		return true;
	}
	
	public synchronized String StateMapInfo(String keyword) {
		try {
			return stateMap.get(keyword);
		} catch (Exception e) {
		}
		return null;
	}
	
}
