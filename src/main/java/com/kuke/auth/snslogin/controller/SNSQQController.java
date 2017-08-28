package com.kuke.auth.snslogin.controller;

import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

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

@Controller
@RequestMapping("/kuke/sns")
public class SNSQQController extends BaseController{
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
	 * 创建人: maxin
	 * 创建于: 2013-2-21
	 * 描　述:
	 *    跳转到QQ登录授权页面
	 * </pre>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qq")
	public String goSNS(HttpServletRequest request) {
		String flag = "1";//qq
		String url = "";
		try {
			String state_id = IdGenerator.getUUIDHex32().toLowerCase();
			url = new com.qq.connect.oauth.Oauth().getAuthorizeURL(state_id);
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
	 * 创建人: maxin
	 * 创建于: 2013-2-21
	 * 描　述:
	 *    验证通过后QQ回调地址
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/qq_auth")
	public Object authSNS(HttpServletRequest request, HttpServletResponse response , ModelMap map) {
		/**
		 * CSRF 判断
		 */
		String flag = null;
		String state = null;
		String from_client = null;
		try {
			state = String.valueOf(this.getParameterMap(request).get("state"));
			flag = ApplicationState.getInstance().StateMapInfo(state);
			//验证state
			if (null == flag || "".equals(flag) || "null".equals(flag) || !"1".equals(flag)) {
				flag = "1";
			}
			ApplicationState.getInstance().StateMapOut(state);
		} catch (Exception e) {
			return "forward:/jsp/sns/back.jsp";
		}
		String snsid = null;
		String nickName = null;
		String accessToken = null;
		String end_time = null;
		try{
			com.qq.connect.javabeans.AccessToken accessTokenObj = (new com.qq.connect.oauth.Oauth()).getAccessTokenByQueryString(request.getQueryString(), state);
			if (accessTokenObj.getAccessToken().equals("")) {
				// 我们的网站被CSRF攻击了或者用户取消了授权
				// 做一些数据统计工作
				// 跳转到登录页面
				return "forward:/jsp/sns/back.jsp";
			} else {
				accessToken = accessTokenObj.getAccessToken();
		        // 利用获取到的accessToken 去获取当前用的openid -------- start
				com.qq.connect.api.OpenID openIDObj =  new com.qq.connect.api.OpenID(accessToken);
				snsid = openIDObj.getUserOpenID();
				//去获取用户在Qzone的昵称等信息
				com.qq.connect.api.qzone.UserInfo qzoneUserInfo = new com.qq.connect.api.qzone.UserInfo(accessToken, snsid);
				com.qq.connect.javabeans.qzone.UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
				nickName = userInfoBean.getNickname();
				//accessToken过期时间
				end_time = DateUtil.getSecondDate(Long.parseLong(String.valueOf(accessTokenObj.getExpireIn())));
			}
		}  catch (Exception e) {
			return "forward:/jsp/sns/back.jsp";
		}
		/**
		 * 3. 用户昵称,openid等信息注册登录
		 */
		try{
			//看openid信息是否注册过
			String user_id = snsService.checkUserSNS_QQ(snsid);
			if (null == user_id || "".equals(user_id)) {
				//QQ用户
				SnsUser qqSnsUser = new SnsUser();
				qqSnsUser.setAccess_token(accessToken);
				qqSnsUser.setRefresh_token("");
				qqSnsUser.setSns_id(snsid);
				qqSnsUser.setSns_name(URLEncoder.encode(nickName,"UTF-8"));
				qqSnsUser.setSns_type("qq");
				qqSnsUser.setNick_name(URLEncoder.encode(nickName,"UTF-8"));
				qqSnsUser.setEnd_time(end_time);
				qqSnsUser.setHeadimgurl("");
				/**
				 * 当前存在登录用户
				 */
				User user = UserOauth.userLoginByToken(request);
				if(user.getUser_status() != null && user.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
					//用户已登录
					qqSnsUser.setId(IdGenerator.getUUIDHex32());
					qqSnsUser.setUser_id(user.getUid());
					snsService.boundUserSNS_QQ(qqSnsUser);	
					map.put("bound", "1");
					map.put("flag", "1");
					map.put("boundType", "qq");
					map.put("boundName",nickName);
					
					//记录日志
					LogUtil.addModUserInfoLog(request);
					
				}else{
					String ssnkey = createSNSID("qq");
					ApplicationSns.getInstance().SNSMapIn(ssnkey, qqSnsUser);
					ICookie.set(response, SNS_KEY, ssnkey);
					//修改三方登录为快速注册登录
					request.setAttribute("key", ssnkey);
					User new_user = registService.snsFastRegist("",request, response);
					qqSnsUser.setId(IdGenerator.getUUIDHex32());
					qqSnsUser.setUser_id(new_user.getUid());
					snsService.boundUserSNS_QQ(qqSnsUser);
					map.addAttribute("SnsUser", qqSnsUser);/////test
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
				if(currentUser.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
					currentUserId = currentUser.getUid();
				}
				SnsUser qqSnsUser = new SnsUser();
				qqSnsUser.setUser_id(user_id);
				qqSnsUser.setSns_id(snsid);
				qqSnsUser.setAccess_token(accessToken);
				qqSnsUser.setRefresh_token("");
				qqSnsUser.setSns_name(URLEncoder.encode(nickName,"UTF-8"));
				qqSnsUser.setSns_type("qq");
				qqSnsUser.setNick_name(URLEncoder.encode(nickName,"UTF-8"));
				qqSnsUser.setEnd_time(end_time);
				qqSnsUser.setHeadimgurl("");
				snsService.updateUserSNS_QQ_AccessToken(qqSnsUser);	
				//SSO登录
				User user = null;
				if(StringUtils.isBlank(currentUserId)){
					 //直接使用QQ账号登陆
					user = UserAuthUtils.getInstance().UserLogin(user_id,from_client);
					if((user == null || KuKeAuthConstants.FAILED.equals(user.getUser_status())) && "".equals(user.getUid())){
						String ssnkey = createSNSID("qq");
						ApplicationSns.getInstance().SNSMapIn(ssnkey, qqSnsUser);
						ICookie.set(response, SNS_KEY, ssnkey);
						//修改三方登录为快速注册登录
						request.setAttribute("key", ssnkey);
						user = registService.snsFastRegist(user_id,request, response);
					}
					//写sso_id到Cookies
					UserAuthUtils.setUserCookie(response, user, "1");
					map.addAttribute("SnsUser", qqSnsUser);//
					
					//记录日志
					LogUtil.addLoginLog(user.getUid(), user.getOrg_id(), request);
					
				}else{//用户已登录
//					一个库客账号绑定一个qq账号
//					 user = UserAuthUtils.getInstance().UserLogin(currentUserId,from_client);
					 if(!currentUserId.equals(user_id)){
						 map.put("flag", "0");
					 	 map.put("bound", "1");
						 map.put("boundType", "qq");
						 map.put("boundMessage","您要绑定的QQ账号,已经被绑定!");
					 }
					 
					//记录日志
					LogUtil.addModUserInfoLog(request);
				}
				//跳转到页面
				return "forward:/jsp/sns/back.jsp";
			}
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
