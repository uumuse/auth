package com.kuke.util;

import java.util.Date;
import java.util.HashMap;

import com.kuke.auth.userCenter.bean.UserFolder;
import com.kuke.auth.util.PropertiesHolder;

public class ImageUrlUtil {
	/**
	 * 得到专辑的封面
	 * @param ItemCode
	 * @return
	 */
	public static String getItemCodeImage(String item_code){
		if("".equals(StringUtil.dealNull(item_code))){
			return "";
		}else{
			item_code = StringUtil.dealNull(item_code);
			String imgUrl = "";
			imgUrl  = String.valueOf(PropertiesHolder.getContextProperty("imgUrl")) + "/images/audio/cata200/"+item_code.substring(0, 1)+"/"+item_code+".jpg";
			return imgUrl;
		}
	}
	
	/***
	 * 获得乐谱封面
	 */
	//http://image.kuke.com/images/audio/musicbook/cover/B/BeethovenMajorWorksforOrchestra59.jpg
	public static String getMusicBookImage(String music_book_file){
		if("".equals(StringUtil.dealNull(music_book_file))){
			return "";
		}else{
			music_book_file = StringUtil.dealNull(music_book_file);
			String imgUrl = "";
			imgUrl  = String.valueOf(PropertiesHolder.getContextProperty("imgUrl")) + "/images/audio/musicbook/cover/"+music_book_file.substring(0, 1)+"/"+music_book_file+".jpg";
			return imgUrl;
		}
	}
	/**
	 * 得到用户头像
	 * @param user
	 * @return
	 */
	public static String getUserImg(String image){
		image = StringUtil.dealNull(image);
		if("".equals(image)){
			return String.valueOf(PropertiesHolder.getContextProperty("auth.url"))+"/images/default_headimg_s.jpg";
		}else{
			String photoUrl = "";
			if(image.startsWith("http")){
				photoUrl = image;
			}else{
				photoUrl = String.valueOf(PropertiesHolder.getContextProperty("imgUrl"));
				photoUrl += String.valueOf(PropertiesHolder.getContextProperty("user.photo.url"));
				photoUrl += image;
				if(HttpClientUtil.executePost(photoUrl, new HashMap<String, String>()).contains("404") || HttpClientUtil.executePost(photoUrl, new HashMap<String, String>()).contains("301")){
					photoUrl = String.valueOf(PropertiesHolder.getContextProperty("auth.url"))+"/images/default_headimg_s.jpg";//
				}
			}
			return photoUrl;
		}
	}
	/**
	 * 得到机构封面
	 * @param user
	 * @return
	 */
	public static String getOrgImg(String orgid){
		if("".equals(StringUtil.dealNull(orgid))){
			return "";
		}else{
			String photoUrl = String.valueOf(PropertiesHolder.getContextProperty("imgUrl"));
			photoUrl += String.valueOf(PropertiesHolder.getContextProperty("org.logo.url"));
			photoUrl += orgid + ".gif";
			return photoUrl;
		}
	}
	/**
	 * 得到夹子封面
	 * @param user
	 * @return
	 */
	public static String getFolderImg(UserFolder folder){
		if("".equals(StringUtil.dealNull(folder.getImgurl()))){
			return "";
		}else{
			return String.valueOf(PropertiesHolder.getContextProperty("imgUrl")) + String.valueOf(PropertiesHolder.getContextProperty("user.folder.url")) + folder.getImgurl();
		}
	}
	/**
	 * 得到厂牌封面
	 * @param user
	 * @return
	 */
	public static String getLableImg(String lableid){
		String lableUrl = "/images/audio/label/max/";
		if("".equals(StringUtil.dealNull(lableid))){
			return "";
		}else{
			lableid = StringUtil.dealNull(lableid);
			String imgUrl = String.valueOf(PropertiesHolder.getContextProperty("imgUrl")) + lableUrl + lableid +".jpg";
			if(HttpClientUtil.executePost(imgUrl, new HashMap<String, String>()).contains("404") || HttpClientUtil.executePost(imgUrl, new HashMap<String, String>()).contains("301")){
				imgUrl = String.valueOf(PropertiesHolder.getContextProperty("imgUrl")) + lableUrl +"default.jpg";
			}
			return imgUrl;
		}
	}
	/**
	 * 得到艺术家封面
	 * @param user
	 * @return
	 */
	public static String getArtistImg(String musicionid){
		
		String musicionUrl = "/images/audio/musician/min/";
		
		if("".equals(StringUtil.dealNull(musicionid))){
			return "";
		}else{
			musicionid = StringUtil.dealNull(musicionid);
			String imgUrl = String.valueOf(PropertiesHolder.getContextProperty("imgUrl")) + musicionUrl + musicionid +".jpg";
			if(HttpClientUtil.executePost(imgUrl, new HashMap<String, String>()).contains("404") || HttpClientUtil.executePost(imgUrl, new HashMap<String, String>()).contains("301")){
				imgUrl = String.valueOf(PropertiesHolder.getContextProperty("imgUrl")) + musicionUrl +"default.jpg";
			}
			return imgUrl;
		}
	}
	public static void main(String[] args) {
		System.out.println(new Date().getTime());
	}
}
