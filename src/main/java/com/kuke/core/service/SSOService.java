/**
 * Project Name:service_app
 * Package Name:com.kuke.core.service
 * File Name:SSOService.java 
 * Create Time:2012-9-21 下午03:41:08
 * Copyright (c) 2006 ~ 2012 Kuke Tech Dept All rights reserved.
 */

package com.kuke.core.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.kuke.core.util.MD5;
import com.kuke.core.util.PlayUtil;
import com.kuke.core.util.ResourceUtil;

/**
 * Class SSOService Descripton goes here
 * 
 * @author：Administrator
 * @version: Revision:2.0.0, 2012-9-21 下午03:41:08
 */
public class SSOService {

	public static String initMp3PlayUrl_HTTP(Map<String, String> mp3) {
		String result = "faild";
		try {
			StringBuffer sb = new StringBuffer();
			String http = "http://";
			String label_id = mp3.get("label_id");
			String item_code = mp3.get("item_code");
			String l_code = mp3.get("l_code");
			String mediaServerIp = ResourceUtil.getResVByK("http.media.server");
			String md5key = ResourceUtil.getResVByK("http.media.password");
			String quality = mp3.get("quality");
			String quality_file = "normbit";
			if (quality != null && quality.equals("192")) {
				quality_file = "highbit";
			} else {
				quality_file = "normbit";
			}

			if (null == label_id || "".equals(label_id)
					|| "null".equals(label_id)) {
				label_id = "Other";
			}
			String type = "_full_wm_" + quality + ".mp3";
			String path = label_id + "/" + item_code + "/" + l_code + type;// NAC/8.551036-37/551036_12_full_wm_192.mp3

			Long time = new Date().getTime();
			MD5 md5 = new MD5();
			String md5str = md5.getMD5ofStr(md5key + time + "/mp3/"
					+ quality_file + "/" + path);

			// playurl="http://221.123.178.146/mp3/"+track+".mp3?xcode="+md5str+"&mid="+time+"&path="+path;
			// http://music.kuke.com/1369100702125/e900848055a58acb545a820ea3d85852/mp3/highbit/NXC/8.503109/03109A_15_full_wm_192.mp3

			sb.append(http).append(mediaServerIp).append("/");
			sb.append(time + "/");
			sb.append(md5str + "/");
			sb.append("mp3/" + quality_file + "/");
			sb.append(path);
			String fileStr = sb.toString();
			result = fileStr;
			System.out.println(result);
		} catch (Exception e) {
		}
		return result;
	}
	
	public static String initMp3PlayUrl320_HTTP(Map<String, String> mp3) {
		String result = "faild";
		try {
			StringBuffer sb = new StringBuffer();
			String http = "http://";
			String label_id = mp3.get("label_id");
			String item_code = mp3.get("item_code");
			String l_code = mp3.get("l_code");
			String mediaServerIp = ResourceUtil.getResVByK("http.media.server");
			String md5key = ResourceUtil.getResVByK("http.media.password");
			String quality = mp3.get("quality");
			String quality_file = "normbit";
			if (quality != null && quality.equals("320")) {
				quality_file = "320kbps";
			} else {
				quality_file = "normbit";
			}

			if (null == label_id || "".equals(label_id)
					|| "null".equals(label_id)) {
				label_id = "Other";
			}
			String type = "_full_wm_" + quality + ".mp3";
			String path = label_id + "/" + item_code + "/" + l_code + type;// NAC/8.551036-37/551036_12_full_wm_192.mp3

			Long time = new Date().getTime();
			MD5 md5 = new MD5();
			String md5str = md5.getMD5ofStr(md5key + time + "/mp3/"
					+ quality_file + "/" + path);

			// playurl="http://221.123.178.146/mp3/"+track+".mp3?xcode="+md5str+"&mid="+time+"&path="+path;
			// http://music.kuke.com/1369100702125/e900848055a58acb545a820ea3d85852/mp3/highbit/NXC/8.503109/03109A_15_full_wm_192.mp3

			sb.append(http).append(mediaServerIp).append("/");
			sb.append(time + "/");
			sb.append(md5str + "/");
			sb.append("mp3/" + quality_file + "/");
			sb.append(path);
			String fileStr = sb.toString();
			result = fileStr;
			System.out.println(result);
		} catch (Exception e) {
		}
		return result;
	}

	public static String initCataloguebioPlayUrl_HTTP(Map<String, String> mp3) {
		String result = "faild";
		try {
			StringBuffer sb = new StringBuffer();
			String http = "http://";
			String label_id = mp3.get("label_id");
			String item_code = mp3.get("item_code");
			String mediaServerIp = ResourceUtil.getResVByK("http.media.server");
			String md5key = ResourceUtil.getResVByK("http.media.password");

			if (null == label_id || "".equals(label_id)
					|| "null".equals(label_id)) {
				label_id = "Other";
			}
			String path = label_id + "/" + item_code + ".rar";// NAC/8.551036-37/551036_12_full_wm_192.mp3

			Long time = new Date().getTime();
			MD5 md5 = new MD5();
			String md5str = md5.getMD5ofStr(md5key + time + "/mp3/download/"
					+ path);

			// playurl="http://221.123.178.146/mp3/"+track+".mp3?xcode="+md5str+"&mid="+time+"&path="+path;

			sb.append(http).append(mediaServerIp).append("/");
			sb.append(time + "/");
			sb.append(md5str + "/");
			sb.append("mp3/download/");
			sb.append(path);
			String fileStr = sb.toString();
			result = fileStr;
			System.out.println(result);
		} catch (Exception e) {
		}
		return result;
	}

	public static String initMusicBookPlayUrl_HTTP(Map<String, String> mp3) {
		String result = "faild";
		try {
			StringBuffer sb = new StringBuffer();
			String http = "http://";
			String music_book_file = mp3.get("music_book_file");
			String mediaServerIp = ResourceUtil.getResVByK("http.media.server");
			String md5key = ResourceUtil.getResVByK("http.media.password");
			String path =  music_book_file + ".pdf";//NAC/8.551036-37/551036_12_full_wm_192.mp3
			
			Long time = new Date().getTime();
			MD5 md5 = new MD5();
			String md5str = md5.getMD5ofStr(md5key+ time+"/mp3/musicbook/"+path );

			//playurl="http://221.123.178.146/mp3/"+track+".mp3?xcode="+md5str+"&mid="+time+"&path="+path;

			
			sb.append(http).append(mediaServerIp).append("/");
			sb.append(time+"/");
			sb.append(md5str+"/");
			sb.append("mp3/musicbook/");
			sb.append(path);
			String fileStr = sb.toString();
			result = fileStr;
			System.out.println(result);
		} catch (Exception e) {
		}
		return result;
}
}
