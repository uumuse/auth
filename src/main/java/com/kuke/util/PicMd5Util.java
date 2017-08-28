package com.kuke.util;

public class PicMd5Util {
	                                         
	public static final String PIC_MD5_KEY = "kuke2014image*#@!";
	
	public static String getPicMd5(String type,String pic_name){
		MD5 md5 = new MD5();
		String md5str = md5.getMD5ofStr(type+PIC_MD5_KEY+pic_name);
		return md5str;
	}
	
	public static String getPicMd5(String type){
		MD5 md5 = new MD5();
		String md5str = md5.getMD5ofStr(type+PIC_MD5_KEY);
		return md5str;
	}
}
