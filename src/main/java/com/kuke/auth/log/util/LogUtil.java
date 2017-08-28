package com.kuke.auth.log.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.OrgOauth;
import com.kuke.core.engine.ICookie;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.StringUtil;

import cz.mallat.uasparser.UserAgentInfo;

public class LogUtil {
	
	/**
	 * 下载乐谱日志
	 * @param sourceId
	 * @param itemcode
	 * @param request
	 */
	public static void addDownBookLog(String sourceId,HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		map.put("logType", "23");
		map.put("sourceId",sourceId);
		addLog(map,request);
	}
	/**
	 * 下载专辑日志
	 * @param sourceId
	 * @param itemcode
	 * @param request
	 */
	public static void addDownCatalLog(String itemcode,HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		map.put("logType", "22");
		map.put("sourceId",itemcode);
		map.put("itemcode",itemcode);
		addLog(map,request);
	}
	/**
	 * 下载单曲日志
	 * @param sourceId
	 * @param itemcode
	 * @param request
	 */
	public static void addDownTrackLog(String sourceId,String itemcode,HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		map.put("logType", "21");
		map.put("sourceId",sourceId);
		map.put("itemcode",itemcode);
		addLog(map,request);
	}
	/**
	 * 找回密码日志
	 * @param request
	 */
	public static void addFindPassLog(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		map.put("logType", "35");
		addLog(map,request);
	}
	/**
	 * 修改个人资料
	 * @param request
	 */
	public static void addModUserInfoLog(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		map.put("logType", "34");
		addLog(map,request);
	}
	/**
	 * 退出日志
	 * @param request
	 */
	public static void addExitLog(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		map.put("logType", "33");
		addLog(map,request);
	}
	/**
	 * 注册日志
	 * @param sourceId
	 * @param itemcode
	 * @param request
	 */
	public static void addRegistLog(String userId,String orgId,HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		map.put("logType", "31");
		map.put("userId",userId);
		map.put("orgId",orgId);
		addLog(map,request);
	}
	/**
	 * 登录日志
	 * @param sourceId
	 * @param itemcode
	 * @param request
	 */
	public static void addLoginLog(String userId,String orgId,HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		map.put("logType", "32");
		map.put("userId",userId);
		map.put("orgId",orgId);
		addLog(map,request);
	}
	/**
	 * 访问资源日志
	 * @param sourceId
	 * @param itemcode
	 * @param request
	 */
	public static void addVisitLog(String sourceId,String itemcode,HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		map.put("logType", "4");
		map.put("sourceId",sourceId);
		map.put("itemcode",itemcode);
		addLog(map,request);
	}
	/**
	 * 单纯访问日志
	 * @param request
	 */
	public static void addVisitLog(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		map.put("logType", "4");
		addLog(map,request);
	}
	/**
	 * 添加日志
	 * @param logType:日志类型
	 */
	private  static  void  addLog(Map<String, String> map,HttpServletRequest request){
		
		String logType = StringUtil.dealNull(map.get("logType"));
		String sourceId = StringUtil.dealNull(map.get("sourceId"));
		String itemcode = StringUtil.dealNull(map.get("itemcode"));
		String userId = StringUtil.dealNull(map.get("userId"));
		String orgId = StringUtil.dealNull(map.get("orgId"));
		
		String time = "0";//时间
		String ip = "0";  //
		String userType = "0";
		String product = "0";
		String client = "0";
		String browser = "0";
		String playTime = "0";
		String url = "0";
		String userAgent  = "0";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		time = sdf.format(new Date());//time
		ip = OrgOauth.getIp(request);//ip
		if(!"m".equals(StringUtil.dealNull(request.getParameter("from")))){//大站过来的
			product = "1";
		}else{
			product = StringUtil.dealNull(request.getParameter("product"));
		}
		product = "".equals(product)?"0":product;
		url = request.getRequestURL().toString();
		
		Map<String, String> systemMap = getSystemInfo(request.getHeader("User-Agent"));
		client = systemMap.get("client");
		browser = systemMap.get("browser");
		userAgent = systemMap.get("userAgent");
		
		Map<String, String> cookieMap = getCookieValue(request);
		userId = ("".equals(userId) || "0".equals(userId))?cookieMap.get("userId"):userId;
		orgId = ("".equals(orgId) || "0".equals(orgId))?cookieMap.get("orgId"):orgId;
		if("".equals(userId) || "0".equals(userId)){
			userType = "3";
		}else{
			if("".equals(orgId) || "0".equals(orgId)){
				userType = "2";
			}else{
				userType = "1";
			}
		}
		
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("logType", "".equals(StringUtil.dealNull(logType))?"0":StringUtil.dealNull(logType));
		param.put("sourceId", "".equals(StringUtil.dealNull(sourceId))?"0":StringUtil.dealNull(sourceId));
		param.put("itemcode", "".equals(StringUtil.dealNull(itemcode))?"0":StringUtil.dealNull(itemcode));
		param.put("time", "".equals(StringUtil.dealNull(time))?"0":StringUtil.dealNull(time));
		param.put("ip", "".equals(StringUtil.dealNull(ip))?"0":StringUtil.dealNull(ip));
		param.put("userId", "".equals(StringUtil.dealNull(userId))?"0":StringUtil.dealNull(userId));
		param.put("orgId", "".equals(StringUtil.dealNull(orgId))?"0":StringUtil.dealNull(orgId));
		param.put("userType", "".equals(StringUtil.dealNull(userType))?"0":StringUtil.dealNull(userType));
		param.put("product", "".equals(StringUtil.dealNull(product))?"0":StringUtil.dealNull(product));
		param.put("client", "".equals(StringUtil.dealNull(client))?"0":StringUtil.dealNull(client));
		param.put("browser", "".equals(StringUtil.dealNull(browser))?"0":StringUtil.dealNull(browser));
		param.put("playTime", "".equals(StringUtil.dealNull(playTime))?"0":StringUtil.dealNull(playTime));
		param.put("url", "".equals(StringUtil.dealNull(url))?"0":StringUtil.dealNull(url));
		param.put("userAgent", "".equals(StringUtil.dealNull(userAgent))?"0":StringUtil.dealNull(userAgent));
		
		System.out.println("logJson="+JSONObject.fromObject(param).toString());
		
		String post_url = KuKeUrlConstants.LogURL;//  
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("logJson", JSONObject.fromObject(param).toString()));
		try {
			HttpClientUtil.executeServicePOST(post_url, nvps);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 得到cookie中的值
	 * @param request
	 * @return
	 */
	private static Map<String, String> getCookieValue(HttpServletRequest request){
		
		Map<String, String> map = new HashMap<String, String>();
		//得到票据
		String userCookie = ICookie.get(request,KuKeAuthConstants.SSO_USER_COOKIE_NAME);
		String orgCookie = ICookie.get(request,KuKeAuthConstants.SSO_ORG_COOKIE_NAME);
		
		map.put("userId", userCookie(userCookie));
		map.put("orgId", orgCookie(orgCookie));
		
		return map;
		
	}
	/**
	 * 得到用户ID
	 * @param cookie
	 * @return
	 */
	private static String userCookie(String cookie){
		String userId = "0";
		if(!"".equals(StringUtil.dealNull(cookie))){
			try {
				cookie = URLDecoder.decode(cookie, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(cookie.split("\\^").length > 1){//cookieValue = firstCookieValue + "^" + secondCookieValue;
				String secondCookieValue = cookie.split("\\^")[1];
				if(secondCookieValue.split("\\|").length > 2){
					userId = secondCookieValue.split("\\|")[2];
				}
			}
		}
		return userId;
	}
	/**
	 * 得到机构ID
	 * @param cookie
	 * @return
	 */
	private static String orgCookie(String cookie){
		String orgId = "0";
		if(!"".equals(StringUtil.dealNull(cookie))){
			try {
				cookie = URLDecoder.decode(cookie, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(cookie.split("&").length > 1){//cookieValue = firstCookieValue + "^" + secondCookieValue;
				String secondCookieValue = cookie.split("&")[1];
				if(secondCookieValue.split("\\|").length > 2){
					orgId = secondCookieValue.split("\\|")[2];
					
				}
			}
		}
		return orgId;
	}
	/**
	 * 系统信息
	 * @param userAgent
	 * @return
	 */
	private static Map<String, String> getSystemInfo(String userAgent) {
		String client = "0";
		String browser = "0";
		userAgent = "".equals(StringUtil.dealNull(userAgent))?"0":userAgent;
		try {
			UserAgentInfo userAgentInfo = UserAgentUtil.uasParser.parse(userAgent);
			String osName = userAgentInfo.getOsName();
			if (!osName.isEmpty()) {
				if(osName.contains("Windows")){
					client = "14";
				}else if(osName.contains("Mac")){
					client = "11";
				}else if(osName.contains("Unix")){
					client = "12";
				}else if(osName.contains("Linux")){
					client = "13";
				}else if(osName.contains("Android")){
					client = "21";
				}else if(osName.contains("iOS")){
					client = "22";
				}else{
					client = "3";
				}
			}
			String uaName = userAgentInfo.getUaName();
			if (!uaName.isEmpty()) {
				browser = uaName;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("client", client);
		map.put("browser", browser);
		map.put("userAgent", userAgent);
		return map;
	}
}
