package com.kuke.auth.snslogin.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import sun.util.logging.resources.logging;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.regist.service.IRegistService;
import com.kuke.auth.snslogin.bean.SnsUser;
import com.kuke.auth.snslogin.quartz.ApplicationSns;
import com.kuke.auth.snslogin.quartz.ApplicationState;
import com.kuke.auth.snslogin.service.SNSService;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.UserOauth;
import com.kuke.core.base.BaseController;
import com.kuke.core.engine.ICookie;
import com.kuke.util.DateUtil;
import com.kuke.util.IdGenerator;
import com.kuke.util.WxUtil;
import com.kuke.util.wechat.WXAccessToken;

@Controller
@RequestMapping("/kuke/sns")
public class SNSWeChatController extends BaseController{
	/**
	 * sns 票据key
	 */
	private static String SNS_KEY = "Sns_Key";
	
	@Autowired
	private SNSService snsService;
	
	@Autowired
	private IRegistService registService;
	
	/**
	 * 
	 * <pre>
	 * 创建人: wanyj
	 * 创建于: 2016-9-28
	 * 描　述:
	 *    跳转到微信登录授权页面
	 * </pre>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/wechat")
	public String goSNS(HttpServletRequest request) {
		String flag = String.valueOf(this.getParameterMap(request).get("flag"));
		if (flag == null || "".equals(flag) || "null".equals(flag)) {
			flag = "1";
		}
		if(StringUtils.isNotBlank(request.getParameter("p"))){
			request.getSession().setAttribute("p", "userInfo");
		}
		String url = "";
		try {
			String state_id = IdGenerator.getUUIDHex32().toLowerCase();
			url = WxUtil.authorizeUrl(state_id);
			//绑定CSRF
			ApplicationState.getInstance().StateMapIn(state_id, flag);
		} catch (Exception e) {
			return "forward:/jsp/sns/error.jsp";
		}
		return "redirect:" + url;
	}
	
	/**
	 * 
	 * <pre>
	 * 创建人: wanyj
	 * 描　述:
	 *    验证通过后微信回调地址
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/wechat_auth")
	public Object authSNS(HttpServletRequest request, HttpServletResponse response , ModelMap map) {
		/**
		 * 1.回调地址, CSRF判断
		 */
		String flag = null;
		String state = null;
		String from_client = null;
		try {
			state = String.valueOf(this.getParameterMap(request).get("state"));
			from_client = String.valueOf(this.getParamsMap(request).get("from_client"));
			flag = ApplicationState.getInstance().StateMapInfo(state);
			if (null == flag || "".equals(flag) || "null".equals(flag)) {
				flag = "1";
			}
			ApplicationState.getInstance().StateMapOut(state);
		} catch (Exception e) {
			return "forward:/jsp/sns/back.jsp";
		}
		String unionid = null;
		String snsid = null;
		String nickName = null;
		String accessToken = null;
		String end_time = null;
		String image = null;//用户头像
		/**
		 * 2. 调用接口,通过code获取accessToken,openid信息,然后通过openid获取用户信息
		 */
		try{
			WXAccessToken accessTokenObj = WxUtil.getAccessTokenByQueryString(request, state);
			if (accessTokenObj.getAccess_token().equals("")) {
				// 我们的网站被CSRF攻击了或者用户取消了授权
				// 做一些数据统计工作
				// 跳转到登录页面
				return "forward:/jsp/sns/back.jsp";
			} else {
				accessToken = accessTokenObj.getAccess_token();
		        // 利用获取到的accessToken 去获取当前用的openid -------- start
				snsid = accessTokenObj.getOpenid();
				unionid = accessTokenObj.getUnionid();
				//去获取用户昵称等信息,调用接口,获取用户信息
				JSONObject jsonObject = WxUtil.getUserInfo(accessTokenObj);
				System.out.println("weichat info=========================>"+jsonObject.toString());
				nickName = jsonObject.getString("nickname");
				//accessToken过期时间
				end_time = DateUtil.getSecondDate(Long.parseLong(String.valueOf(accessTokenObj.getExpires_in())));
				//头像
				image = jsonObject.getString("headimgurl");
			}
		}catch (Exception e) {
			return "forward:/jsp/sns/back.jsp";
		}
		/**
		 * 3. 用户昵称,openid等信息注册登录
		 */
		try{
			//看unionid信息是否注册过
			List<Map<String,String>> wechatList = snsService.checkUnionIdUserSNS_WX(unionid);
			String user_id = snsService.checkUserSNS_WeChat(snsid);;
			String uid = null;
			if(!wechatList.isEmpty()&&wechatList.size()>1){
				user_id = snsService.checkUserSNS_WeChat(snsid);
				}
			else if(!wechatList.isEmpty()&&wechatList.size()==1){
				//根据unionid查出一个结果，区分出是新用户还是两个账户的老用户
				user_id = String.valueOf(wechatList.get(0).get("user_id"));
				uid = snsService.checkUserSNS_WeChat(snsid);
				if(uid !=null &&(!uid.equals(user_id))){
					user_id = uid;
				}
			}else{
				user_id = snsService.checkUserSNS_WeChat(snsid);
			}
			//新用户
			if(wechatList.isEmpty()&& (user_id==null||user_id.equals(""))){

				//微信用户
				SnsUser WXSnsUser = new SnsUser();
				WXSnsUser.setAccess_token(accessToken);
				WXSnsUser.setRefresh_token("");
				WXSnsUser.setSns_id(snsid);
//				WXSnsUser.setSns_name(URLEncoder.encode(nickName,"UTF-8"));
				WXSnsUser.setSns_name(nickName);
				WXSnsUser.setSns_type("wechat");
				WXSnsUser.setNick_name(nickName);
				WXSnsUser.setEnd_time(end_time);
				WXSnsUser.setHeadimgurl(image);
				WXSnsUser.setUnionid(unionid);
				/**
				 * 当前存在登录用户
				 */
				User user = UserOauth.userLoginByToken(request);
				if(user.getUser_status() != null && user.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
					//用户已登录
					WXSnsUser.setId(IdGenerator.getUUIDHex32());
					WXSnsUser.setUser_id(user.getUid());
					snsService.boundUserSNS_WeChat(WXSnsUser);	
					map.put("bound", "1");
					map.put("flag", "1");
					map.put("boundType", "wechat");
					map.put("boundName",nickName);
					
					//记录日志
					LogUtil.addModUserInfoLog(request);
				}else{
					//当前无登录用户
					String ssnkey = createSNSID("wechat");
					ApplicationSns.getInstance().SNSMapIn(ssnkey, WXSnsUser);
					ICookie.set(response, SNS_KEY, ssnkey);
					
					//修改三方登录为快速注册登录
					request.setAttribute("key", ssnkey);
					User new_user = registService.snsFastRegist("",request, response);
					WXSnsUser.setId(IdGenerator.getUUIDHex32());
					WXSnsUser.setUser_id(new_user.getUid());
					snsService.boundUserSNS_WeChat(WXSnsUser);
					map.addAttribute("SnsUser", WXSnsUser);
				}
				if(StringUtils.isNotBlank((String) request.getSession().getAttribute("p"))){
					request.getSession().removeAttribute("p");
					request.setAttribute("out", "0");
				}
				//跳转到绑定页面
				return "forward:/jsp/sns/back.jsp";
			
			}else{

				/**
				 * 绑定过
				 * 刷新accessToken 更新到数据库
				 */
				//获取当前登陆用户信息
				User currentUser = UserOauth.userLoginByToken(request);
				String currentUserId = "";
				if(currentUser.getUser_status() != null && currentUser.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
					currentUserId = currentUser.getUid();
				}
				SnsUser WXSnsUser = new SnsUser();
				WXSnsUser.setUser_id(user_id);
				WXSnsUser.setAccess_token(accessToken);
				WXSnsUser.setRefresh_token("");
				WXSnsUser.setSns_id(snsid);
				WXSnsUser.setSns_name(nickName);
				WXSnsUser.setSns_type("wechat");
				WXSnsUser.setNick_name(nickName);
				WXSnsUser.setEnd_time(end_time);
//				WXSnsUser.setHeadimgurl(image);
				WXSnsUser.setUnionid(unionid);
				snsService.updateUserSNS_WX_AccessToken(WXSnsUser);	
				//SSO登录
				User user = null;
				if(StringUtils.isBlank(currentUserId)){//注册
					 //直接使用微信账号登陆
					user = UserAuthUtils.getInstance().UserLogin(user_id,from_client);
					if((user == null || KuKeAuthConstants.FAILED.equals(user.getUser_status())) && "".equals(user.getUid())){
						String ssnkey = createSNSID("wechat");
						ApplicationSns.getInstance().SNSMapIn(ssnkey, WXSnsUser);
						ICookie.set(response, SNS_KEY, ssnkey);
						//修改三方登录为快速注册登录
						request.setAttribute("key", ssnkey);
						user = registService.snsFastRegist(user_id,request, response);
					}
					//写sso_id到Cookies
					UserAuthUtils.setUserCookie(response, user, "1");
					map.addAttribute("SnsUser", WXSnsUser);
					
					//记录日志
					LogUtil.addLoginLog(user.getUid(), user.getOrg_id(), request);
					
				}else{//绑定
					//一个库客账号绑定一个qq账号
//					 user = UserAuthUtils.getInstance().UserLogin(currentUserId,from_client);
					 if(!currentUserId.equals(user_id)){
						 	map.put("bound", "1");
							map.put("flag", "0");
							map.put("boundType", "wechat");
							map.put("boundMessage","您要绑定的微信账号,已经被绑定!");
					 }
					 
					//记录日志
					LogUtil.addModUserInfoLog(request);
				}
				//跳转到页面
				return "forward:/jsp/sns/back.jsp";
			
			}
			
			
			/****************之前逻辑******************/
//			String user_id = snsService.checkUserSNS_WeChat(snsid);
//			if (null == user_id || "".equals(user_id)) {
//				//微信用户
//				SnsUser WXSnsUser = new SnsUser();
//				WXSnsUser.setAccess_token(accessToken);
//				WXSnsUser.setRefresh_token("");
//				WXSnsUser.setSns_id(snsid);
//				WXSnsUser.setSns_name(URLEncoder.encode(nickName,"UTF-8"));
//				WXSnsUser.setSns_type("wechat");
//				WXSnsUser.setNick_name(URLEncoder.encode(nickName,"UTF-8"));
//				WXSnsUser.setEnd_time(end_time);
//				WXSnsUser.setHeadimgurl(image);
//				WXSnsUser.setUnionid(unionid);
//				/**
//				 * 当前存在登录用户
//				 */
//				User user = UserOauth.userLoginByToken(request);
//				if(user.getUser_status() != null && user.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
//					//用户已登录
//					WXSnsUser.setId(IdGenerator.getUUIDHex32());
//					WXSnsUser.setUser_id(user.getUid());
//					snsService.boundUserSNS_WeChat(WXSnsUser);	
//					map.put("bound", "1");
//					map.put("flag", "1");
//					map.put("boundType", "wechat");
//					map.put("boundName",nickName);
//					
//					//记录日志
//					LogUtil.addModUserInfoLog(request);
//				}else{
//					//当前无登录用户
//					String ssnkey = createSNSID("wechat");
//					ApplicationSns.getInstance().SNSMapIn(ssnkey, WXSnsUser);
//					ICookie.set(response, SNS_KEY, ssnkey);
//					
//					//修改三方登录为快速注册登录
//					request.setAttribute("key", ssnkey);
//					User new_user = registService.snsFastRegist("",request, response);
//					WXSnsUser.setId(IdGenerator.getUUIDHex32());
//					WXSnsUser.setUser_id(new_user.getUid());
//					snsService.boundUserSNS_WeChat(WXSnsUser);
//					map.addAttribute("SnsUser", WXSnsUser);
//				}
//				if(StringUtils.isNotBlank((String) request.getSession().getAttribute("p"))){
//					request.getSession().removeAttribute("p");
//					request.setAttribute("out", "0");
//				}
//				//跳转到绑定页面
//				return "forward:/jsp/sns/back.jsp";
//			}else{
//				/**
//				 * 绑定过
//				 * 刷新accessToken 更新到数据库
//				 */
//				//获取当前登陆用户信息
//				User currentUser = UserOauth.userLoginByToken(request);
//				String currentUserId = "";
//				if(currentUser.getUser_status() != null && currentUser.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
//					currentUserId = currentUser.getUid();
//				}
//				SnsUser WXSnsUser = new SnsUser();
//				WXSnsUser.setUser_id(user_id);
//				WXSnsUser.setAccess_token(accessToken);
//				WXSnsUser.setRefresh_token("");
//				WXSnsUser.setSns_id(snsid);
//				WXSnsUser.setSns_name(URLEncoder.encode(nickName,"UTF-8"));
//				WXSnsUser.setSns_type("wechat");
//				WXSnsUser.setNick_name(URLEncoder.encode(nickName,"UTF-8"));
//				WXSnsUser.setEnd_time(end_time);
//				WXSnsUser.setHeadimgurl(image);
//				WXSnsUser.setUnionid(unionid);
//				snsService.updateUserSNS_WX_AccessToken(WXSnsUser);	
//				//SSO登录
//				User user = null;
//				if(StringUtils.isBlank(currentUserId)){//注册
//					 //直接使用微信账号登陆
//					user = UserAuthUtils.getInstance().UserLogin(user_id,from_client);
//					if((user == null || KuKeAuthConstants.FAILED.equals(user.getUser_status())) && "".equals(user.getUid())){
//						String ssnkey = createSNSID("wechat");
//						ApplicationSns.getInstance().SNSMapIn(ssnkey, WXSnsUser);
//						ICookie.set(response, SNS_KEY, ssnkey);
//						//修改三方登录为快速注册登录
//						request.setAttribute("key", ssnkey);
//						user = registService.snsFastRegist(user_id,request, response);
//					}
//					//写sso_id到Cookies
//					UserAuthUtils.setUserCookie(response, user, "1");
//					map.addAttribute("SnsUser", WXSnsUser);
//					
//					//记录日志
//					LogUtil.addLoginLog(user.getUid(), user.getOrg_id(), user.getUid(), request);
//					
//				}else{//绑定
//					//一个库客账号绑定一个qq账号
////					 user = UserAuthUtils.getInstance().UserLogin(currentUserId,from_client);
//					 if(!currentUserId.equals(user_id)){
//						 	map.put("bound", "1");
//							map.put("flag", "0");
//							map.put("boundType", "wechat");
//							map.put("boundMessage","您要绑定的微信账号,已经被绑定!");
//					 }
//					 
//					//记录日志
//					LogUtil.addModUserInfoLog(request);
//				}
//				//跳转到页面
//				return "forward:/jsp/sns/back.jsp";
//			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "forward:/jsp/sns/back.jsp";
	}
	/**
	 * 创建票据
	 */
	private static String createSNSID(String pk) {
		Date now = new Date();
		long time = now.getTime();
		return "t" + time + "i" + pk + IdGenerator.getUUIDHex32();
	}
}
