package com.kuke.auth.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.kuke.util.HttpClientUtil;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.engine.ICookie;

public class UserOauth {
	/**
	 * 用户名密码登录
	 * @param response
	 * @param userName
	 * @param userPwd
	 * @return
	 * @throws Exception
	 */
	public static User userLoginByUP(HttpServletResponse response,Map<String, String> map) throws Exception {
		String post_url = KuKeUrlConstants.userLoginByUP_URL;//   /kuke/ssouser/authuser
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("u", map.get("userName")));
		nvps.add(new BasicNameValuePair("p", map.get("userPwd")));
		nvps.add(new BasicNameValuePair("from_client",map.get("from_client")));
		nvps.add(new BasicNameValuePair("userid",map.get("userid")));
		nvps.add(new BasicNameValuePair("type",map.get("type")));//1.iphone 2.email 3.(其他)用户名  (不为12,则为3)
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		System.out.println("userLoginByUP result:"+result);
		ResponseMsg msg = new ResponseMsg(result);
		User user = new User(String.valueOf(msg.getData()));
		if (user.getUser_status() != null && user.getUser_status().equals(KuKeAuthConstants.SUCCESS)) {
			UserAuthUtils.setUserCookie(response, user, "1");
		}
		return user;
	}
	/**
	 * Cookies登录
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static User userLoginByToken(HttpServletRequest request) throws Exception {
		String post_url = KuKeUrlConstants.userLoginByToken_URL; //   /kuke/ssouser/authcookie
		String token = ICookie.get(request, KuKeAuthConstants.SSO_USER_COOKIE_NAME);// KuKeDesktopSSOID
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("ticket", token));
		System.out.println("ticket:"+token);
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		System.out.println(result);
		ResponseMsg msg = new ResponseMsg(result);
		System.out.println("userLoginByToken result:"+result);
		return  new User(msg.getData().toString());
	}
	/**
	 * ticket登录
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static User userLoginByToken(String ticket) throws Exception {
		String post_url = KuKeUrlConstants.userLoginByToken_URL; //   /kuke/ssouser/authcookie
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("ticket", ticket));
		System.out.println("ticket:"+ticket);
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		ResponseMsg msg = new ResponseMsg(result);
		System.out.println("userLoginByToken result:"+result);
		return  new User(String.valueOf(msg.getData()));
	}
	/**
	 * 根据user_id获取认证信息
	 * @param user_id
	 * @return
	 * @throws Exception
	 */
	public static User userVerify(String user_id) throws Exception {
			String post_url = KuKeUrlConstants.userVerify_URL;  // /kuke/ssouser/verify
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("user_id", user_id));
			String result = HttpClientUtil.executeServicePOST(post_url, nvps);
			ResponseMsg msg = new ResponseMsg(result);
			return  new User(msg.getData().toString());
	}
	/**
	 * 更新用户内存信息
	 * @param user_id
	 * @param type
	 * @param value
	 * @throws Exception
	 */
	public static void userUpdateRAM(String user_id,String type,String value ) throws Exception {
			String post_url = KuKeUrlConstants.userUpdateRAM_URL;// /kuke/ssouser/userupdateRAM
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("user_id", user_id));
			nvps.add(new BasicNameValuePair("type", type));
			nvps.add(new BasicNameValuePair("value", value));
			HttpClientUtil.executeServicePOST(post_url, nvps);
	}
	/**
	 * 刷新在线Time
	 * @param request
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject userRefreshOnline(HttpServletRequest request,String channelId) throws Exception {
		String post_url = KuKeUrlConstants.userRefreshOnline_URL;//  /kuke/ssouser/initonline
		String token = ICookie.get(request, KuKeAuthConstants.SSO_USER_COOKIE_NAME);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("ticket", token));
		nvps.add(new BasicNameValuePair("c", channelId));
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		return JSONObject.fromObject(result);
	}
	/**
	 * 登出
	 * @param request
	 * @param response
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject userOut(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String post_url = KuKeUrlConstants.userOut_URL;// /kuke/ssouser/authout
		String token = ICookie.get(request,KuKeAuthConstants.SSO_USER_COOKIE_NAME);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("ticket", token));
//		nvps.add(new BasicNameValuePair("c", channelId));
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		result = result.replaceAll("\\\\", "");
		result = result.startsWith("\"")?result.substring(1):result;
		result = result.endsWith("\"")?result.substring(0,result.length()-1):result;
		ICookie.clear(response, KuKeAuthConstants.SSO_USER_COOKIE_NAME);
		return JSONObject.fromObject(result);
	}
	/**
	 * 音频 视频 Live 包月 播放
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String userPlayAudio(HttpServletRequest request) throws Exception {
		String post_url = KuKeUrlConstants.userPlayAudio_URL;  // /kuke/authorize/user/audio
		String token = ICookie.get(request,KuKeAuthConstants.SSO_USER_COOKIE_NAME);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("ticket", token));
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		return result;
	}
	/**
	 * 视频单次播放
	 * @param request
	 * @param video_id
	 * @return
	 * @throws Exception
	 */
	public static String userPlayVideo(HttpServletRequest request,String video_id) throws Exception {
		String post_url = KuKeUrlConstants.userPlayVideo_URL;  //  /kuke/authorize/video
		String orgToken = ICookie.get(request, KuKeAuthConstants.SSO_ORG_COOKIE_NAME);//机构Token
		String userToken = ICookie.get(request,KuKeAuthConstants.SSO_USER_COOKIE_NAME);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("userToken", userToken));
		nvps.add(new BasicNameValuePair("orgToken", orgToken));
		nvps.add(new BasicNameValuePair("video_id", video_id));
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		return result;
	}
	/**
	 * Live单次播放
	 * @param request
	 * @param live_id
	 * @return
	 * @throws Exception
	 */
	public static String userPlayLive(HttpServletRequest request,String live_id) throws Exception {
		String post_url = KuKeUrlConstants.userPlayLive_URL;
		String token = ICookie.get(request,KuKeAuthConstants.SSO_USER_COOKIE_NAME);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("ticket", token));
		nvps.add(new BasicNameValuePair("live_id", live_id));
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		return result;
	}
	/**
	 * 音频下载  图谱下载
	 * @param request
	 * @param download_id
	 * @param download_type
	 * @return
	 * @throws Exception
	 */
	public static String userDownload(HttpServletRequest request,String download_id, String download_type) throws Exception {
		String post_url = KuKeUrlConstants.userDownLoad_URL; // /kuke/authorize/user/download
		String token = ICookie.get(request,KuKeAuthConstants.SSO_USER_COOKIE_NAME);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("ticket", token));
		nvps.add(new BasicNameValuePair("download_id", download_id));
		nvps.add(new BasicNameValuePair("download_type", download_type));
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		ResponseMsg msg = new ResponseMsg(result);
		return msg.getCode();
	}
	/**
	 * andriod用户名密码登录
	 * @param response
	 * @param userName
	 * @param userPwd
	 * @return
	 * @throws Exception
	 */
	public static User userLoginByUPForAndriod(HttpServletResponse response,String userName, String userPwd) throws Exception {
		String post_url = KuKeUrlConstants.userLoginByUPForAndriod_URL;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("u", userName));
		nvps.add(new BasicNameValuePair("p", userPwd));
		nvps.add(new BasicNameValuePair("from_client", "andriod"));
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		User user = new User(result);
		if (user.getUser_status().equals(KuKeAuthConstants.SUCCESS)) {
			UserAuthUtils.setUserCookie(response, user, "1");
		}
		return new User(result);
	}
	/**
	 * andriod cookie登录
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static User userLoginByTokenForAndriod(HttpServletRequest request) throws Exception {
		String post_url = KuKeUrlConstants.userLoginByTokenForAndriod_URL;
		String token = ICookie.get(request, KuKeAuthConstants.SSO_USER_COOKIE_NAME);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("ticket", token));
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		return  new User(result);
	}
	public static void main(String[] args) {
//		String token = "829827ab2aba658ff47848af547d6aa4%2526user_web_000004303D8511DFBB8F932FFC38CAAF%257CVKC0Tbpr8O%257C000004303D8511DFBB8F932FFC38CAAF%257C1476100534153";
//		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		nvps.add(new BasicNameValuePair("ticket", token));
//		System.out.println("ticket:"+token);
//		String result = null;
//		try {
//			result = HttpClientUtil.executeServicePOST("http://auths.kuke.com/kuke/ssouser/authcookie", nvps);
//			System.out.println("userLoginByToken result:"+result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("musicianId", "15"));
			nvps.add(new BasicNameValuePair("musicianType", "0"));
			String post_url = KuKeUrlConstants.getMusicians;
			String result = "";
			try {
				result = HttpClientUtil.executeServicePOST(post_url, nvps);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ResponseMsg msg = new ResponseMsg(result);
			System.out.println(msg);
			JSONObject json = JSONObject.fromObject(msg.getData());
			System.out.println(json.get("fullName"));
	}
}
