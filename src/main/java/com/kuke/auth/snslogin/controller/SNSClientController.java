package com.kuke.auth.snslogin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.regist.service.IRegistService;
import com.kuke.auth.snslogin.bean.SnsUser;
import com.kuke.auth.snslogin.quartz.ApplicationSns;
import com.kuke.auth.snslogin.service.SNSService;
import com.kuke.auth.ssologin.service.UserSSOService;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.engine.ICookie;
import com.kuke.util.DateUtil;
import com.kuke.util.IdGenerator;

@Controller
@RequestMapping("/kuke/sns")
public class SNSClientController extends BaseController {
	
	/**
	 * sns 票据key
	 */
	private static String SNS_KEY = "Sns_Key";
	
	private static String wechat = "wechat";
	
	private static String weibo = "weibo";
	
	private static String qq = "qq";
	
	private static String ios = "ios";
	
	private static String android = "android";
	
	@Autowired
	private SNSService snsService;
	
	@Autowired
	private IRegistService registService;
	
	@Autowired
	private UserSSOService userSSOService;
	
	/**
	 * wanyj
	 * 描　述:移动端验证通过后调此接口获得新的用户信息(移动端未登录时)
	 * 如果登录时调用
	 * @param request
	 * @param map
	 * @return 
	 */
	@RequestMapping(value = "/client")
	public @ResponseBody ResponseMsg clientAuthSNS(HttpServletRequest request, HttpServletResponse response) {
		String codeDesc = "1:获取用户信息成功;"
						+ "2:获取用户信息失败,获取用户信息出现异常;"
						+ "3:获取用户信息失败,该第三方账号已被其他账号绑定;"
						+ "4:snsid参数为空;"
						+ "5:accesstoken参数为空;"
						+ "6:nickname参数为空;"
						+ "7:snstype参数为空或非"+wechat+"、"+weibo+"、"+qq+"中的一种;"
						+ "8:from_client参数为空或非"+ios+"、"+android+"中的一种;"
						+ "";
		ResponseMsg msg = new ResponseMsg(false, "2", "获取用户信息失败,获取用户信息出现异常;", codeDesc, null);//返回的信息
		/**
		 * 3. 用户昵称,openid等信息注册登录
		 */
		try{
			String snsid = dealNull(request.getParameter("snsid"));//第三方ID
			String accessToken = dealNull(request.getParameter("accesstoken"));//令牌 
			String nickName = dealNull(request.getParameter("nickname"));//nickname 昵称
			String end_time = dealNull(request.getParameter("end_time"));//end_time 令牌结束时间
			String user_image = dealNull(request.getParameter("user_image"));//user_image 用户头像
			String snstype = dealNull(request.getParameter("snstype"));//snstype 第三方类型:wechat weibo qq
			String from_client = dealNull(request.getParameter("from_client"));//from_client: ios android web
			String unionid = dealNull(request.getParameter("unionid"));
			Map<String, String> params = this.getParameterMap(request);
			System.out.println("clientAuthSNS params:"+params);
			if("".equals(snsid)){
				return new ResponseMsg(false, "4", "snsid参数为空", codeDesc, null);//返回的信息
			}
			if("".equals(accessToken)){
				return new ResponseMsg(false, "5", "accesstoken参数为空", codeDesc, null);//返回的信息
			}
			if("".equals(nickName)){
				return new ResponseMsg(false, "6", "nickname参数为空", codeDesc, null);//返回的信息
			}
			if("".equals(snstype) || (!wechat.equals(snstype) && !weibo.equals(snstype) && !qq.equals(snstype))){
				return new ResponseMsg(false, "7", "snstype参数为空或非"+wechat+"、"+weibo+"、"+qq+"中的一种", codeDesc, null);//返回的信息
			}
			if("".equals(from_client) || (!ios.equals(from_client) && !android.equals(from_client))){
				return new ResponseMsg(false, "8", "from_client参数为空或非"+ios+"、"+android+"中的一种", codeDesc, null);//返回的信息
			}
			//accessToken过期时间
			try {
				end_time = DateUtil.getSecondDate(Long.parseLong(String.valueOf(end_time)));
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseMsg(false, "9", "end_time参数错误:"+end_time, codeDesc, null);//返回的信息
			}
			//看openid信息是否注册过
			String user_id = snsService.checkUserSNS_WeChat(snsid);
			String uid = "";
			List<Map<String,String>> wechatList = new ArrayList<Map<String,String>>();
			if(wechat.equals(snstype)){//微信
				System.out.println("weichat client info=====================>snsid"+snsid+",,,unionid:"+unionid);
				wechatList = snsService.checkUnionIdUserSNS_WX(unionid);
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
			}else if(weibo.equals(snstype)){//微博
				user_id = snsService.checkUserSNS_SINA(snsid);
			}else if(qq.equals(snstype)){//qq
				user_id = snsService.checkUserSNS_QQ(snsid);
			}
			System.out.println("clientAuthSNS user_id:"+user_id);
			
			/********************************************************/			
			if (wechatList.isEmpty()&& (null == user_id || "".equals(user_id))) {
				//没有绑定过
				//微信用户
				SnsUser SnsUser = new SnsUser();
				SnsUser.setAccess_token(accessToken);
				SnsUser.setRefresh_token("");
				SnsUser.setSns_id(snsid);
				SnsUser.setSns_type(snstype);
				SnsUser.setSns_name(nickName);
				SnsUser.setNick_name(nickName);
				SnsUser.setEnd_time(end_time);
				SnsUser.setHeadimgurl(user_image);
				SnsUser.setUnionid(unionid);
				//用户未登录时,则快速注册
				String ssnkey = createSNSID(snstype);
				ApplicationSns.getInstance().SNSMapIn(ssnkey, SnsUser);
				ICookie.set(response, SNS_KEY, ssnkey);
				//修改三方登录为快速注册登录
				request.setAttribute("key", ssnkey);
				User user = registService.snsFastRegist("",request, response);
				System.out.println("clientAuthSNS user:"+user);
				SnsUser.setId(IdGenerator.getUUIDHex32());
				SnsUser.setUser_id(user.getUid());
				//绑定
				if(wechat.equals(snstype)){//微信
					snsService.boundUserSNS_WeChat(SnsUser);
				}else if(weibo.equals(snstype)){//微博
					snsService.boundUserSNS_SINA(SnsUser);
				}else if(qq.equals(snstype)){//qq
					snsService.boundUserSNS_QQ(SnsUser);
				}
				if(StringUtils.isNotBlank((String) request.getSession().getAttribute("p"))){
					request.getSession().removeAttribute("p");
					request.setAttribute("out", "0");
				}
				
				//User登录
				user = userSSOService.checkUserLoginById(user.getUid());
				System.out.println("clientAuthSNS User登录:"+user);
				// 验证用户状态
				user = UserAuthUtils.getInstance().UserLogin(user, from_client);
				System.out.println("clientAuthSNS 验证用户状态:"+user);
				return new ResponseMsg(true,"1","获取用户信息成功",codeDesc, user);
			}else{
				/**
				 * 绑定过
				 * 刷新accessToken 更新到数据库
				 */
				SnsUser SnsUser = new SnsUser();
				SnsUser.setUser_id(user_id);
				SnsUser.setAccess_token(accessToken);
				SnsUser.setRefresh_token("");
				SnsUser.setSns_id(snsid);
				SnsUser.setSns_type(snstype);
				SnsUser.setSns_name(nickName);
				SnsUser.setNick_name(nickName);
				SnsUser.setEnd_time(end_time);
//				SnsUser.setHeadimgurl(user_image);
				SnsUser.setUnionid(unionid);
				//更新accessToken
				if(wechat.equals(snstype)){//微信
					snsService.updateUserSNS_WX_AccessToken(SnsUser);	
				}else if(weibo.equals(snstype)){//微博
					snsService.updateUserSNS_SINA_AccessToken(SnsUser);
				}else if(qq.equals(snstype)){//qq
					snsService.updateUserSNS_QQ_AccessToken(SnsUser);
				}
				//User登录
				User user = userSSOService.checkUserLoginById(user_id);
				System.out.println("clientAuthSNS else User登录:"+user);
				//当user_id不存在时
				if((user == null || KuKeAuthConstants.FAILED.equals(user.getUser_status())) && "".equals(user.getUid())){
					String ssnkey = createSNSID(snstype);
					ApplicationSns.getInstance().SNSMapIn(ssnkey, SnsUser);
					ICookie.set(response, SNS_KEY, ssnkey);
					//修改三方登录为快速注册登录
					request.setAttribute("key", ssnkey);
					user = registService.snsFastRegist("",request, response);
				}
				
				// 验证用户状态
				user = UserAuthUtils.getInstance().UserLogin(user, from_client);
				System.out.println("clientAuthSNS else 验证用户状态:"+user);
				UserAuthUtils.setUserCookie(response, user, "1");
				JSONObject o = JSONObject.fromObject(user);
				return new ResponseMsg(true,"1","获取用户信息成功",codeDesc, JSONObject.fromObject(user));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	/**
	 * 创建票据
	 */
	private static String createSNSID(String pk) {
		Date now = new Date();
		long time = now.getTime();
		return "t" + time + "i" + pk + IdGenerator.getUUIDHex32();
	}
	/**
	 * 
	 * @param str
	 * @return
	 */
	private String dealNull(String str){
		if(str == null || "".equals(str.trim()) || "null".equals(str.trim())){
			str = "";
		}
		return str.trim();
	}
}
