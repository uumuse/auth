/**
 * Project Name:live_app
 * Package Name:com.kuke.core.util
 * File Name:UrlUtil.java 
 * Create Time:2012-4-6 上午11:57:16
 * Copyright (c) 2006 ~ 2012 Kuke Tech Dept All rights reserved.
 */
package com.kuke.core.util;

/**
 *Class UrlUtil Descripton goes here
 * 
 * @author：Administrator
 *@version: Revision:2.0.0, 2012-4-6 上午11:57:16
 */
public class UrlUtil {

	/**
	 * 
	 * <pre>
	 * 创建人: zhjt
	 * 创建于: 2008-2-23
	 * 描　述:
	 *    url编码
	 * </pre>
	 * 
	 * @param oldUrl
	 * @return
	 */

	public static String urlEncode(String oldUrl,String enCode) {
		String newUrl = "";
		try {
			if (null != oldUrl && !"".equals(oldUrl)) {
				newUrl = java.net.URLEncoder.encode(oldUrl, enCode);
			}
		} catch (Exception e) {
		}

		return newUrl;
	}

	/**
	 * 
	 * <pre>
	 * 创建人: zhjt
	 * 创建于: 2008-2-23
	 * 描　述:
	 *    url解码
	 * 
	 * </pre>
	 * 
	 * @param oldUrl
	 * @return
	 */
	public static String urlDecode(String oldUrl, String enCode) {
		String newUrl = "";
		try {

			if (null != oldUrl && !"".equals(oldUrl)) {
				newUrl = java.net.URLDecoder.decode(oldUrl, enCode);
			}
		} catch (Exception e) {

		}
		return newUrl;
	}

}
