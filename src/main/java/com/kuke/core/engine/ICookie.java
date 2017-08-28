/**
 * Project Name:live_app
 * Package Name:com.kuke.core.web.util
 * File Name:ICookie.java 
 * Create Time:2012-4-5 下午05:00:06
 * Copyright (c) 2006 ~ 2012 Kuke Tech Dept All rights reserved.
 */
package com.kuke.core.engine;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kuke.auth.util.PropertiesHolder;

/**
 *Class ICookie 处理 Cookie
 * 
 * @author：zhjt
 *@version: Revision:2.0.0, 2012-4-5 下午05:00:06
 */
public class ICookie
{
	// cookie前缀
	private static String pre = "ikuke_";

	// 默认cookie密钥
	private static String defaultKey = "kuke";

	// 安全级别
	private static String level = "normal";

	// 获取配置的前缀
	private static String getPre() {
		if ("NULL" != PropertiesHolder.getContextProperty("safePre")) {// ikuke_
			return String.valueOf(PropertiesHolder.getContextProperty("safePre"));
		} else {
			return pre;
		}
	}

	/**
	 * 废弃
	 * @return
	 */
	private static String getLevel() {
		if ("NULL" != PropertiesHolder.getContextProperty("safeLevel")) {
			return String.valueOf(PropertiesHolder.getContextProperty("safeLevel"));
		} else {
			return level;
		}
	}

	/**
	 * 设置cookie
	 * @param response
	 * @param name
	 * @param value
	 * @param time
	 * @param path
	 * @param domain
	 */
	public static void set(HttpServletResponse response, String cookieName,
			String value, int time, String path, String domain) {
		Cookie cookie = new Cookie(getPre() + cookieName, null);
		cookie.setDomain(domain);
		if (time <= 0){
			time = -1;
		}
		cookie.setMaxAge(time);
		cookie.setValue(value);
		cookie.setPath(path);
		response.addCookie(cookie);
	}
	
	/**
	 * 设置cookie
	 * @param response
	 * @param name
	 * @param value
	 */
	public static void set(HttpServletResponse response, String cookieName,
			String value) {
		set(response, cookieName, value, -1, "/", ".kuke.com");
	}

	/**
	 * 得到cookie值
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static String get(HttpServletRequest request, String cookieName,boolean isPre) {
		String cookieValue = null;
		Cookie[] diskCookies = request.getCookies();
		if (diskCookies != null) {
			for (int i = 0; i < diskCookies.length; ++i) {
				if (diskCookies[i].getName().equals( isPre == true ? getPre()+ cookieName : "" + cookieName)) {
					cookieValue = diskCookies[i].getValue();
					return cookieValue;
				}
			}
		}
		return cookieValue;
	}
	
	public static String get(HttpServletRequest request, String cookieName) {
		String cookieValue = null;
		Cookie[] diskCookies = request.getCookies();
		if (diskCookies != null) {
			for (int i = 0; i < diskCookies.length; ++i) {
				if (diskCookies[i].getName().equals(getPre() + cookieName)) {
					cookieValue = diskCookies[i].getValue();
					return cookieValue;
				}
			}
		}
		return cookieValue;
	}

	/**
	 * 清除指定cookie
	 * 
	 * @param response
	 * @param cookieName
	 */
	public static void clear(HttpServletResponse response, String cookieName) {
		set(response, cookieName, null);// (response,cookieName,0);
	}
	
	/**
	 * 清除所有cookie	 * 
	 * @param request
	 * @param response
	 */
	public static void clearAll(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("clearAll is start!");
		Cookie[] diskCookies = request.getCookies();
		if (diskCookies != null) {
			for (int i = 0; i < diskCookies.length; ++i) {
				if (diskCookies[i].getName().indexOf(getPre()) > -1) {
					clear(response, diskCookies[i].getName());
				}
			}
		}
	}
}
