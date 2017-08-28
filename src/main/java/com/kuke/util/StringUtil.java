package com.kuke.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.coobird.thumbnailator.Thumbnails;

public class StringUtil {
	
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式  
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式  
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式  
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符  
    /**
	 * 去除标题的, " 防止影响js
	 * @param CommaString
	 * @return
	 */
	public static String formatTtile(String title){
		if(title != null){
			title = dealNull(title);
			title = title.replaceAll("'", "").replaceAll("\"", "");
		}else{
			title = dealNull(title);
		}
		return title;
	}
	/**
	 * 去除字符串两边的,
	 * @param CommaString
	 * @return
	 */
	public static String formatCommaForString(String CommaString){
		if(CommaString != null){
			CommaString = dealNull(CommaString);
			CommaString = CommaString.startsWith(",")?CommaString.substring(1):CommaString;
			CommaString = CommaString.endsWith(",")?CommaString.substring(0, CommaString.length()-1):CommaString;
		}else{
			CommaString = dealNull(CommaString);
		}
		return CommaString;
	}
	/**
	 * 检查空字符串
	 * @param str
	 * @return
	 */
	public static String dealNull(String str){
		if(str == null || "".equals(str.trim()) || "null".equals(str.trim())){
			str = "";
		}
		return str.trim();
	}
	/**
	 * 去除字符串中的:单引号,
	 * @param str
	 * @return
	 */
	public static String formatString(String str){
		if(str != null){
			str = str.replaceAll("'", "‘");
		}
		return str;
	}
	
	 /** 
     * @param htmlStr 
     * @return 
     *  删除Html标签 
     */  
    public static String delHTMLTag(String htmlStr) {  
    	if(htmlStr != null){
//    		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);  
//    		Matcher m_script = p_script.matcher(htmlStr);  
//    		htmlStr = m_script.replaceAll(""); // 过滤script标签  
//    		
//    		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);  
//    		Matcher m_style = p_style.matcher(htmlStr);  
//    		htmlStr = m_style.replaceAll(""); // 过滤style标签  
//    		
//    		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
//    		Matcher m_html = p_html.matcher(htmlStr);  
//    		htmlStr = m_html.replaceAll(""); // 过滤html标签  
//    		
//    		Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);  
//    		Matcher m_space = p_space.matcher(htmlStr);  
//    		htmlStr = m_space.replaceAll(""); // 过滤空格回车标签  
    		
    		htmlStr = htmlStr.replaceAll("<", "").replaceAll(">", "");
    		
    		htmlStr = htmlStr.trim();
    		
    		htmlStr = htmlStr.replaceAll(" ", "");
    		
    		return htmlStr.trim(); // 返回文本字符串  
    	}else{
    		return htmlStr; // 返回文本字符串  
    	}
        
    }
	
	public static void main(String[] args) {
//		String md5Key = "KukeGetUser!@#$";
//		MD5 md5 = new MD5();
//		System.out.println(md5.getMD5ofStr("A44B1790C81211E6B237CF07793A6517"+md5Key));
//		ResponseMsg msg = new ResponseMsg(true, "1", "上传成功", "1:上传成功;2:上传失败;3:图片类型错误,可上传类型jpg|JPG|gif|GIF|png|PNG;4:图片大小不能大于5M", "http://img.kuke.com/images/upload/photo/fb4a51bd-17e0-4184-b7ad-2a48af819979.jpg");
//		System.out.println(JSONObject.fromObject(msg));
//		System.out.println(md5.getMD5ofStr("0"));
//		String s = "2013052919563713B69E50C";
//		System.out.println(s.substring(0, 14));
//		SimpleDateFormat sm = new SimpleDateFormat("yyyyMMddhhmmss");
//	    Calendar calendar = Calendar.getInstance();    
//	    try {
//	    	System.out.println(sm.parse(s.substring(0, 14)));
//			calendar.setTime(sm.parse(s.substring(0, 14)));
//			calendar.add(Calendar.SECOND, 1);    
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}   
//	    System.out.println(sm.format(calendar.getTime()));
//		try {
//			System.out.println(URLDecoder.decode("3208dc84e4f40ee992118aeac991523b%26%7CZleuMMcebA%7C1FF1BD10C7D511DCBF06B26C6D344382%7C1484124846232%7C1", "UTF-8"));
//			System.out.println(URLDecoder.decode("de0cd2099794dd39d5e15064c1697570%26%7CC4mUrUjGs6%7C1FF1BD10C7D511DCBF06B26C6D344382%7C1484126248260%7C1", "UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String time = "1484296552245";
//		String email = "zhaowei@kuke.com";
//		String newKey = md5.getMD5ofStr(new StringBuffer(KuKeAuthConstants.FINDPASSWORD + time).reverse().toString()).toUpperCase()
//				+ md5.getMD5ofStr(email + KuKeAuthConstants.FINDPASSWORD).toUpperCase();
//		System.out.println(newKey);
//		try {
//			Thumbnails.of("d://G0100014132546.jpg").size(200,200).toFile("d://G0100014132546.jpg");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println(formatString("don't"));
	}
}
