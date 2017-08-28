/**
 * Project Name:component_app
 * Package Name:app.kuke.play.action
 * File Name:PlayUtil.java 
 * Create Time:2012-5-3 上午11:02:13
 * Copyright (c) 2006 ~ 2012 Kuke Tech Dept All rights reserved.
 */
package com.kuke.core.util;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.kuke.auth.util.PropertiesHolder;

public class PlayUtil {
	/**
	 *Class PlayUtil
	 *Descripton goes here
	 *@author：Administrator
	 *@version: Revision:2.0.0, 2012-5-3 上午11:02:13
	 */
public static String BuildProtectedURLWithValidity(String media_url, String ip) {
		String password="kukemusicplay";  
		int valid=30;
		String result = null;
		java.util.Date l_datetime = new java.util.Date();
		SimpleDateFormat l_date_format = new SimpleDateFormat("M/d/yyyy h:m:s aaa", Locale.US);
		TimeZone l_timezone = TimeZone.getTimeZone("GMT-0");
		l_date_format.setTimeZone(l_timezone);
		String l_utc_date = l_date_format.format(l_datetime);
		int Valid = valid;
		String to_be_hashed = ip + password + l_utc_date + Integer.toString(Valid);
		String md5_signature = EncoderByMd5(to_be_hashed);
        result = media_url + "?server_time=" + l_utc_date + "~hash_value=" + md5_signature + "~validminutes=" +Integer.toString(Valid);
       // SSOServiceGetPlayUrl(md5_signature);
		return result;
	}

	private static String SSOServiceGetPlayUrl(String sessionid){
		String result = "failed";
		try{
			String authAction =String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"/kuke/playRight?o=add&sessionid=" + sessionid;
			// 执行返回结果
			result = HttpClientUtil.executeService(authAction);
		}catch(Exception e){
			return null;
		}
		return result;
	}
	
	public static String EncoderByMd5(String str){
		if(str==null){
			return "";
		}
		try{
			MD5 md5=new MD5();
			String newstr=md5.getMD5ofStr(str);
			return newstr.trim();
		}catch(Exception e){
			return str.trim();
		}
	}
	
	public static void main(String[] args){
		String aa=BuildProtectedURLWithValidity("mms://192.168.0.29/test_wma/wanyue.wma", "221.123.178.132");
		System.out.println(aa);
	}
}

