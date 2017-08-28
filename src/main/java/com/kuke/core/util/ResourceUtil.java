/**
 * Project Name:live_app
 * Package Name:com.kuke.core.util
 * File Name:ResourceUtil.java 
 * Create Time:2012-4-5 下午05:42:25
 * Copyright (c) 2006 ~ 2012 Kuke Tech Dept All rights reserved.
 */
package com.kuke.core.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.kuke.core.base.Constants;

/**
 *Class ResourceUtil Descripton goes here
 * 
 * @author：zhjt
 *@version: Revision:2.0.0, 2012-4-5 下午05:42:25
 */
public class ResourceUtil
{

	/**
	 * 根据Key从指定的配置文件获键值对 
	 * 若没有该key返回"NULL"
	 * @param key
	 * @return
	 */
	public static String getResVByK(String key)
	{
		// Locale locale = LocaleContextHolder.getLocale();
		String defaultValue;
		try
		{
			defaultValue = ResourceBundle.getBundle(Constants.BUNDLE_CONFIG_KEY).getString(key);
		} catch (MissingResourceException mse)
		{
			defaultValue = "NULL";
		}

		return defaultValue;
	}
	
	
	public static void main(String[] args) {
		String authurl = ResourceUtil.getResVByK("AUTH.URL");
		System.out.println(authurl);
	}

}
