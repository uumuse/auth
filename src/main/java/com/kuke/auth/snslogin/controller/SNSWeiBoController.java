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
import com.kuke.util.weibo4j.Oauth;

@Controller
@RequestMapping("/kuke/sns")
public class SNSWeiBoController extends BaseController {
	
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
	 * 创建于: 2013-2-22
	 * 描　述:
	 *     跳转到新浪微博授权页面
	 * </pre>
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/weibo")
	public String goSNS(HttpServletRequest request,HttpServletResponse response) {
		String flag = String.valueOf(this.getParameterMap(request).get("flag"));
		if (flag == null || "".equals(flag) || "null".equals(flag)) {
			flag = "1";
		}
		String url = "";
		Oauth oauth = new Oauth();
		try {
			String state = IdGenerator.getUUIDHex32().toLowerCase();
			url = oauth.authorize("code", state,true);
			//绑定CSRF
			ApplicationState.getInstance().StateMapIn(state, flag);
		} catch (Exception e) {
			return "forward:/jsp/sns/error.jsp";
		}
		return "redirect:" + url;
	}
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-2-22
	 * 描　述:
	 *      验证通过后新浪微博回调地址
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/weibo_auth")
	public Object authSNS(HttpServletRequest request, HttpServletResponse response , ModelMap map) {
		
		//CSRF 判断
		String flag = null;
		String from_client = null;
		try {
			String state = String.valueOf(this.getParameterMap(request).get("state"));
			from_client = String.valueOf(this.getParamsMap(request).get("from_client"));
			flag = ApplicationState.getInstance().StateMapInfo(state);
			if (null == flag || "".equals(flag) || "null".equals(flag)) {
				flag = "1";
			}
			if(StringUtils.isNotBlank(request.getParameter("p"))){
				request.getSession().setAttribute("p", "userInfo");
			}
			ApplicationState.getInstance().StateMapOut(state);
		} catch (Exception e) {
			return "forward:/jsp/sns/back.jsp";
		}
		//授权成功后返回code
		String code = String.valueOf(this.getParameterMap(request).get("code"));
		com.kuke.util.weibo4j.Oauth oauth = new com.kuke.util.weibo4j.Oauth();
		com.kuke.util.weibo4j.Users um = new com.kuke.util.weibo4j.Users();
		String snsid = null;
		String nickName = null;
		String accessToken = null;
		String end_time = null;
		String image = null;
		try {
			//通过授权后的code 得到授权的token 获取 授权用户id
			com.kuke.util.weibo4j.http.AccessToken accessTokenBean = oauth.getAccessTokenByCode(code);
			if (accessTokenBean.getAccessToken().equals("")) {
				// 我们的网站被CSRF攻击了或者用户取消了授权
				// 做一些数据统计工作
				// 跳转到登录页面
				// 跳转到出错页面、重新引导到授权页面.
				return "forward:/jsp/sns/back.jsp";
			} else {
				accessToken = accessTokenBean.getAccessToken();
				//获取 uid
				snsid = accessTokenBean.getUid();
				//设置 token
				um.client.setToken(accessToken);
				//获取 uid的用户信息
				com.kuke.util.weibo4j.model.User userInfo = um.showUserById(snsid);
				nickName = userInfo.getScreenName();
				image = userInfo.getProfileImageUrl();
				//accessToken过期时间
				end_time = DateUtil.getSecondDate(Long.parseLong(String.valueOf(accessTokenBean.getExpireIn())));
			}
		} catch (com.kuke.util.weibo4j.model.WeiboException e) {
			e.printStackTrace();
			return "forward:/jsp/sns/back.jsp";
		} catch (Exception e) {
			return "forward:/jsp/sns/back.jsp";
		}
		try{
			String user_id = snsService.checkUserSNS_SINA(snsid);
			if (null == user_id || "".equals(user_id)) {
				//微博用户信息
				SnsUser sinaSnsUser = new SnsUser();
				sinaSnsUser.setAccess_token(accessToken);
				sinaSnsUser.setRefresh_token("");
				sinaSnsUser.setSns_id(snsid);
				sinaSnsUser.setSns_name(URLEncoder.encode(nickName,"UTF-8"));
				sinaSnsUser.setSns_type("weibo");
				sinaSnsUser.setNick_name(URLEncoder.encode(nickName,"UTF-8"));
				sinaSnsUser.setEnd_time(end_time);
				sinaSnsUser.setNext_url(request.getHeader("referer"));
				sinaSnsUser.setHeadimgurl(image);
				/**
				 * 当前存在登录用户
				 */
				User user = UserOauth.userLoginByToken(request);
				if(user.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
					sinaSnsUser.setId(IdGenerator.getUUIDHex32());
					sinaSnsUser.setUser_id(user.getUid());
					snsService.boundUserSNS_SINA(sinaSnsUser);
					map.put("bound", "1");
					map.put("flag", "1");
					map.put("boundType", "weibo");
					map.put("boundName",nickName);
					
					//记录日志
					LogUtil.addModUserInfoLog(request);
				}else{
					//sns_key
					String ssnkey = createSNSID("weibo");
					ApplicationSns.getInstance().SNSMapIn(ssnkey, sinaSnsUser);
					ICookie.set(response, SNS_KEY, ssnkey);
					
					//修改三方登录为快速注册登录
					request.setAttribute("key", ssnkey);
					User new_user = registService.snsFastRegist("",request, response);
					sinaSnsUser.setId(IdGenerator.getUUIDHex32());
					sinaSnsUser.setUser_id(new_user.getUid());
					snsService.boundUserSNS_SINA(sinaSnsUser);
					map.addAttribute("SnsUser", sinaSnsUser);
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
				SnsUser sinaSnsUser = new SnsUser();
				sinaSnsUser.setUser_id(user_id);
				sinaSnsUser.setAccess_token(accessToken);
				sinaSnsUser.setRefresh_token("");
				sinaSnsUser.setSns_id(snsid);
				sinaSnsUser.setSns_name(URLEncoder.encode(nickName,"UTF-8"));
				sinaSnsUser.setSns_type("weibo");
				sinaSnsUser.setNick_name(URLEncoder.encode(nickName,"UTF-8"));
				sinaSnsUser.setEnd_time(end_time);
				sinaSnsUser.setNext_url(request.getHeader("referer"));
				sinaSnsUser.setHeadimgurl(image);
				
				snsService.updateUserSNS_SINA_AccessToken(sinaSnsUser);	
				//SSO登录
				User user = null;
				if(StringUtils.isBlank(currentUserId)){
					//直接使用微博账号登陆
					user = UserAuthUtils.getInstance().UserLogin(user_id,from_client);
					if((user == null || KuKeAuthConstants.FAILED.equals(user.getUser_status())) && "".equals(user.getUid())){
						//sns_key
						String ssnkey = createSNSID("weibo");
						ApplicationSns.getInstance().SNSMapIn(ssnkey, sinaSnsUser);
						ICookie.set(response, SNS_KEY, ssnkey);
						//修改三方登录为快速注册登录
						request.setAttribute("key", ssnkey);
						user = registService.snsFastRegist(user_id,request, response);
					}
					//写sso_id到Cookies
					UserAuthUtils.setUserCookie(response, user, "1");
					map.addAttribute("SnsUser", sinaSnsUser);
					
					//记录日志
					LogUtil.addLoginLog(user.getUid(), user.getOrg_id(), request);
					
				}else{
					//一个库客账号绑定一个qq账号
//					 user = UserAuthUtils.getInstance().UserLogin(currentUserId,from_client);
					 if(!currentUserId.equals(user_id)){
						 	map.put("flag", "0");
						 	map.put("bound", "1");
							map.put("boundType", "weibo");
							map.put("boundMessage","您要绑定的微博账号,已经被绑定!");
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
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-3-18
	 * 描　述:
	 *        解绑新浪
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/out")
	public String outSNS(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) {
		
		try {
			User user = UserOauth.userLoginByToken(request);
			if (user.getUser_status().equals(KuKeAuthConstants.SUCCESS)) {
				this.snsService.delUserSNS_SINA(user.getUid());
				map.put("out", "1");
			} else {
				map.put("out", "0");
			}
		} catch (Exception e) {
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
