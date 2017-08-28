package com.kuke.auth.userCenter.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.regist.domain.User;
import com.kuke.auth.regist.service.impl.RegistService;
import com.kuke.auth.snslogin.bean.SnsUser;
import com.kuke.auth.snslogin.service.SNSService;
import com.kuke.auth.ssologin.service.UserSSOService;
import com.kuke.auth.userCenter.dto.Result;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.engine.ICookie;
import com.kuke.core.redis.RedisUtil;
import com.kuke.pay.service.CommentService;
import com.kuke.util.DateUtil;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.IdGenerator;
import com.kuke.util.ImageUrlUtil;
import com.kuke.util.MD5;
import com.kuke.util.MailUtil;
import com.kuke.util.MessageFormatUtil;
import com.kuke.util.StringUtil;

@Controller
@RequestMapping(value = "/kuke/userCenter")
public class UserSettingsController extends BaseController {
	
	private static String SNS_KEY = "Sns_Key";
	
	private static String wechat = "wechat";
	
	private static String weibo = "weibo";
	
	private static String qq = "qq";
	
	private static String ios = "ios";
	
	private static String android = "android";
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private SNSService snsService;
	
	@Autowired
	private RegistService registService;
	
	@Autowired
	private UserSSOService userSSOService;
	
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 进入基本资料页
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userInf")
	public Object userInf(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		//1.得到当前登录用户
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			
			String userId = this.getLoginUser().getUid();
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			if("m".equals(from)){
				//User登录
				com.kuke.auth.login.bean.User user = userSSOService.checkUserLoginById(userId);
				//余额
				Map<String, String> userMoney = commentService.getUserMoneyByUid(this.getLoginUser().getUid());
				if(userMoney != null){
					user.setRemain_money("".equals(dealNull(userMoney.get("remain_money")))?"0.00":dealNull(userMoney.get("remain_money")));
					user.setOrg_money("".equals(dealNull(userMoney.get("org_money")))?"0.00":dealNull(userMoney.get("org_money")));
				}
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, user), HttpStatus.OK);
			}else{
				User user = new User();
				user.setId(this.getLoginUser().getUid());
				user = registService.getUserInfo(user);
				//
				String birthday = user.getBirthday();
				if ( null!=birthday && StringUtils.isNotBlank(birthday)) {
					Date d = DateUtil.str2date(birthday, "yyyy-MM-dd HH:mm:ss");
					String year = DateUtil.date2str(d, "yyyy");
					String month = DateUtil.date2str(d, "MM");
					String day = DateUtil.date2str(d, "dd");
					request.setAttribute("year", year);//生日年
					request.setAttribute("month", month);//生日月
					request.setAttribute("day", day);//生日日
				}
				//昵称
				user.setNick_name("".equals(dealNull(user.getNick_name()))?"":URLDecoder.decode(user.getNick_name()));
				
				
				request.setAttribute("thisYear",DateUtil.date2str(new Date(), "yyyy"));//当前年(展示用)
				request.setAttribute("user", user);//用户信息
				request.setAttribute("site", dealNull(request.getParameter("site")));//导航位置信息:0 1 2 3
				request.setAttribute("uphoto", ImageUrlUtil.getUserImg(user.getImage()));
				request.setAttribute("noPlayUrl", "1");
				return "/userCenter/userInf";
			}
		}else{
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.NOMALLOGIN, null), HttpStatus.OK);
			}else{
				return KuKeUrlConstants.userLogin_url;
			}
		}
	}
	/**
	 * 修改昵称
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateNickName")
	public @ResponseBody ResponseMsg updateNickName(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		if (this.getLoginUser() != null) {
			//参数
			String nickname = params.get("nickname");//addSourceToClass
			if(!"".equals(dealNull(nickname))){
				User user = new User();
				user.setId(this.getLoginUser().getUid());
				user = registService.getUserInfo(user);
				params.put("nick_name", nickname);
				params.put("id", user.getId());
				int res = registService.updateNickName(params);
				
				try{
					//更新到缓存
					String key = "userInfo:" + user.getId();
					Map<String, String> userMap = new HashMap<String, String>();
					userMap.put("nickname", params.get("nick_name"));
					this.redisUtil.hashMultipleSet(key, userMap);
				}catch (Exception e) {
				}
				
				//记录日志
				LogUtil.addModUserInfoLog(request);
				
				return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
			}else{
				return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
			}
		}else{
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
		}
	}
	/**
	 * 修改邮箱
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateEmail")
	public @ResponseBody ResponseMsg updateEmail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		if (this.getLoginUser() != null) {
			//参数
			String email = params.get("email");//生日年

			User user = new User();
			user.setId(this.getLoginUser().getUid());
			user = registService.getUserInfo(user);
			params.put("email", email);
			params.put("id", user.getId());
			int res = registService.updateEmail(params);
			
			try{
				//更新到缓存
				String key = "userInfo:" + user.getId();
				Map<String, String> userMap = new HashMap<String, String>();
				userMap.put("email", params.get("email"));
				this.redisUtil.hashMultipleSet(key, userMap);
			}catch (Exception e) {
			}
			
			//记录日志
			LogUtil.addModUserInfoLog(request);
			
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
		}else{
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
		}
	}
	/**
	 * 更新个人资料
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateUserInf")
	public @ResponseBody ResponseMsg updateUserInf(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		if (this.getLoginUser() != null) {
			//更新的信息 params中
			String nickname = params.get("nickname");//昵称
			String signature = params.get("signature");//签名
			String sex = params.get("sex");//性别  male , female , secrecy
			String constellation = params.get("constellation");//星座
			String province = params.get("province");//省
			String city = params.get("city");//市
			
			String year = params.get("year");//生日年
			String month = params.get("month");//生日月
			String day = params.get("day");//生日天
			
			String birthdayStr = null;
			if(year != null && month != null && day != null && !"".equals(year) && !"".equals(month) && !"".equals(day)){
				birthdayStr = year + "-" + month + "-" + day;
			}
			
			if(birthdayStr != null){
				params.put("birthday", birthdayStr);
			}
			params.put("id", this.getLoginUser().getUid());
			params.put("nickname", StringUtil.delHTMLTag(nickname));
			params.put("signature", StringUtil.delHTMLTag(signature));
			int res = registService.updateUserInf(params);
			try{
				//更新到缓存
				String key = "userInfo:" + this.getLoginUser().getUid();
				Map<String, String> userMap = new HashMap<String, String>();
				userMap.put("nickname", params.get("nickname"));
				userMap.put("sex", params.get("sex")==null?"":params.get("sex"));
				this.redisUtil.hashMultipleSet(key, userMap);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			//记录日志
			LogUtil.addModUserInfoLog(request);
			
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
		}else{
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.NOMALLOGIN);
		}
	}
	/**
	 * 更改用户头像
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/headImage")
	public @ResponseBody ResponseMsg headImage(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		if (this.getLoginUser() != null) {
			com.kuke.auth.login.bean.User user = this.getLoginUser();
			data.put("uphoto", "/images/upload/photo/"+user.getUphoto());
			ICookie.clear(response, "userPhoto");//清楚cookie
			return MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, data);
		}else{
			return MessageFormatUtil.formatStateToObject(KuKeAuthConstants.NOMALLOGIN, null);
		}
	}
	/**
	 * 解除微信绑定
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/unBoundWeChat")
	public @ResponseBody ResponseMsg unBoundWeChat(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//1.得到当前登录用户
		com.kuke.auth.login.bean.User user = this.getLoginUser();
		ResponseMsg msg = null;
		if(user != null) {
			try {
				snsService.delUserSNS_WeChat(user.getUid());
				msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
				
				//记录日志
				LogUtil.addModUserInfoLog(request);
				
			} catch (Exception e) {
				msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
			}
		}else{
			msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
		}
		return msg;
	}
	/**
	 * 解除QQ绑定
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/unBoundQQ")
	public @ResponseBody ResponseMsg unBoundQQ(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ResponseMsg msg = null;
		//1.得到当前登录用户
		com.kuke.auth.login.bean.User user = this.getLoginUser();
		if(user != null) {
			try {
				snsService.delUserSNS_QQ(user.getUid());
				msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
				
				//记录日志
				LogUtil.addModUserInfoLog(request);
				
			} catch (Exception e) {
				msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
			}
		}else{
			msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.NOMALLOGIN);
		}
		return msg;
	}
	
	/**
	 * 解除新浪绑定
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/unBoundSINA")
	public @ResponseBody ResponseMsg unBoundSINA(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ResponseMsg msg = null;
		//1.得到当前登录用户
		com.kuke.auth.login.bean.User user = this.getLoginUser();
		if(user != null) {
			try {
				snsService.delUserSNS_SINA(user.getUid());
				msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
				
				//记录日志
				LogUtil.addModUserInfoLog(request);
				
			} catch (Exception e) {
				msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
			}
		}else{
			msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.NOMALLOGIN);
		}
		return msg;
	}
	/**
	 * 解除手机绑定
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/unBoundPhone")
	public @ResponseBody ResponseMsg unBoundPhone(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ResponseMsg msg = null;
		//1.得到当前登录用户
		com.kuke.auth.login.bean.User user = this.getLoginUser();
		if(user != null) {
			try {
				snsService.updateUserSNS_phone(user.getUid());
				msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
				
				//记录日志
				LogUtil.addModUserInfoLog(request);
				
			} catch (Exception e) {
				msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
			}
		}else{
			msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.NOMALLOGIN);
		}
		return msg;
	}
	/**
	 * 解除邮箱绑定
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/unBoundEmail")
	public @ResponseBody ResponseMsg unBoundEmail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ResponseMsg msg = null;
		//1.得到当前登录用户
		com.kuke.auth.login.bean.User user = this.getLoginUser();
		if(user != null) {
			try {
				snsService.updateUserSNS_email(user.getUid());
				msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
				
				//记录日志
				LogUtil.addModUserInfoLog(request);
				
			} catch (Exception e) {
				msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
			}
		}else{
			msg = MessageFormatUtil.formatResultToObject(KuKeAuthConstants.NOMALLOGIN);
		}
		return msg;
	}
	/**
	 * 进入绑定设置页,绑定设置初始化
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/boundInit")
	public @ResponseBody ResponseMsg boundInit(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		Map<String, String> data = new HashMap<String, String>();
		String state = KuKeAuthConstants.FAILED;
		//1.得到当前登录用户
		if (this.getLoginUser() != null) {
			User user = new User();
			String user_id = this.getLoginUser().getUid();
			user.setId(this.getLoginUser().getUid());
			user = registService.getUserInfo(user);
			int count = 0;
			//qq绑定判断
			Map<String, Object> qq = snsService.checkUserBind_QQ(user_id);
			if(qq != null && !"".equals(dealNull((String)qq.get("sns_id")))){
				data.put("QQBoundFlag", "1");
				data.put("QQBoundName", "".equals(dealNull((String)qq.get("sns_name")))?"":dealNull(URLDecoder.decode((String)qq.get("sns_name"))));
				count++;
			}else{
				data.put("QQBoundFlag", "0");
				data.put("QQBoundName", "");
			}
			//微信绑定判断
			Map<String, Object> wechat = snsService.checkUserBind_WeChat(user_id);
			if(wechat != null && !"".equals(dealNull((String)wechat.get("sns_id")))){
				data.put("WeChatBoundFlag", "1");
				data.put("WeChatBoundName", "".equals(dealNull((String)wechat.get("sns_name")))?"":dealNull(URLDecoder.decode((String)wechat.get("sns_name"))));
				count++;
			}else{
				data.put("WeChatBoundFlag", "0");
				data.put("WeChatBoundName", "");
			}
			//微博绑定判断
			Map<String, Object> weibo = snsService.checkUserBind_SINA(user_id);
			if(weibo != null && !"".equals(dealNull((String)weibo.get("sns_id")))){
				System.out.println((String)weibo.get("sns_name"));
				System.out.println("".equals((String)weibo.get("sns_name")));
				data.put("SINABoundFlag", "1");
				data.put("SINABoundName", "".equals(dealNull((String)weibo.get("sns_name")))?"":dealNull(URLDecoder.decode((String)weibo.get("sns_name"))));
				count++;
			}else{
				data.put("SINABoundFlag", "0");
				data.put("SINABoundName", "");
			}
			//手机绑定判断
			if(user.getPhone() != null && !"".equals(user.getPhone()) && !"null".equals(user.getPhone())){//***********
				data.put("PhoneBoundFlag", "1");
				data.put("PhoneBoundName", user.getPhone().length() == 11?user.getPhone().substring(0, 3)+"****"+user.getPhone().substring(7, 11):user.getPhone());
				count++;
			}else{
				data.put("PhoneBoundFlag", "0");
				data.put("PhoneBoundName", "");
			}
			//邮箱绑定判断
			if(user.getEmail() != null && !"".equals(user.getEmail()) && !"null".equals(user.getEmail())){
				data.put("EmailBoundFlag", "1");
				data.put("EmailBoundName", user.getEmail());
				count++;
			}else{
				data.put("EmailBoundFlag", "0");
				data.put("EmailBoundName", "");
			}
			data.put("count",String.valueOf(count));
			state = KuKeAuthConstants.SUCCESS;
			
			//记录日志
			LogUtil.addVisitLog(request);
			
		}else{
			state = KuKeAuthConstants.NOMALLOGIN;
		}
		return MessageFormatUtil.formatStateToObject(state, data);
	}
	/**
	 * wanyj
	 * 描　述:移动端绑定(移动端)
	 * @param request
	 * @param map
	 * @return 
	 */
	@RequestMapping(value = "/clientForBound")
	public @ResponseBody ResponseMsg clientForBound(HttpServletRequest request, HttpServletResponse response) {
		String codeDesc = "1:绑定成功;"
						+ "2:绑定失败;"
						+ "3:用户已绑定;"
						+ "4:snsid参数为空;"
						+ "5:accesstoken参数为空;"
						+ "6:nickname参数为空;"
						+ "7:snstype参数为空或非"+wechat+"、"+weibo+"、"+qq+"中的一种;"
						+ "8:用户未登录;";
		ResponseMsg msg = new ResponseMsg(false, "2", "获取用户信息失败,获取用户信息出现异常;", codeDesc, null);//返回的信息
		/**
		 * 3. 用户昵称,openid等信息注册登录
		 */
		if(this.getLoginUser() != null){
			try{
				String snsid = dealNull(request.getParameter("snsid"));//第三方ID
				String accessToken = dealNull(request.getParameter("accesstoken"));//令牌 
				String nickName = dealNull(request.getParameter("nickname"));//nickname 昵称
				String end_time = dealNull(request.getParameter("end_time"));//end_time 令牌结束时间
				String snstype = dealNull(request.getParameter("snstype"));//snstype 第三方类型:wechat weibo qq
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
				//accessToken过期时间
				try {
					end_time = DateUtil.getSecondDate(Long.parseLong(String.valueOf(end_time)));
				} catch (Exception e) {
					e.printStackTrace();
					return new ResponseMsg(false, "9", "end_time参数错误:"+end_time, codeDesc, null);//返回的信息
				}
				//看openid信息是否注册过
				String user_id = "";
				if(wechat.equals(snstype)){//微信
					user_id = snsService.checkUserSNS_WeChat(snsid);
				}else if(weibo.equals(snstype)){//微博
					user_id = snsService.checkUserSNS_SINA(snsid);
				}else if(qq.equals(snstype)){//qq
					user_id = snsService.checkUserSNS_QQ(snsid);
				}
				if (null == user_id || "".equals(user_id)) {
					//没有绑定过
					//微信用户
					SnsUser SnsUser = new SnsUser();
					SnsUser.setAccess_token(accessToken);
					SnsUser.setRefresh_token("");
					SnsUser.setSns_id(snsid);
					SnsUser.setSns_type(snstype);
					SnsUser.setNick_name(nickName);
					SnsUser.setEnd_time(end_time);
					/**
					 * 当前存在登录用户
					 */
					com.kuke.auth.login.bean.User user = this.getLoginUser();
					
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
					
					//记录日志
					LogUtil.addModUserInfoLog(request);
					
					return new ResponseMsg(true,"1","绑定成功;",codeDesc);
				}else{
					return new ResponseMsg(false,"3","用户已被绑定;",codeDesc);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			return new ResponseMsg(false,"8","用户未登录;",codeDesc);
		}
		return msg;
	}
	/**
	 * wanyj
	 * 描　述:网页端绑定第三方
	 * @param request
	 * @param map
	 * @return 
	 */
	@RequestMapping(value = "/boundSns")
	public @ResponseBody ResponseMsg boundSns(HttpServletRequest request, HttpServletResponse response) {
		String type = dealNull(request.getParameter("type"));//类型:phone weibo  wechat
		if(this.getLoginUser() != null){
			String url = "";
			if("qq".equals(type)){
				url = KuKeUrlConstants.userQQLogin_URL;
			}else if("wechat".equals(type)){
				url = KuKeUrlConstants.userWeChatLogin_URL;
			}else if("weibo".equals(type)){
				url = KuKeUrlConstants.userSinaLogin_URL;
			}
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			String status = KuKeAuthConstants.FAILED;
			try{
				String result = HttpClientUtil.executeServicePOST(url, nvps);
				ResponseMsg msg = new ResponseMsg(result);
				if(msg.getFlag()){
					status = KuKeAuthConstants.SUCCESS;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			//记录日志
			LogUtil.addModUserInfoLog(request);
			
			return MessageFormatUtil.formatResultToObject(status);
		}else{
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.NOMALLOGIN);
		}
	}
	/**
	 * wanyj
	 * 描　述:绑定手机
	 * @param request
	 * @param map
	 * @return 
	 */
	@RequestMapping(value = "/boundPhone")
	public @ResponseBody ResponseMsg boundPhone(HttpServletRequest request, HttpServletResponse response) {
		String codeDesc = "1:绑定成功;"
						+ "2:绑定失败;"
						+ "3:用户已绑定;"
						+ "4:验证码不正确;"
						+ "5:用户未登录;";
		boolean flag = false;
		String code = "1";
		String msg = "绑定失败";
		Map<String, Object> data = new HashMap<String, Object>();
		if(this.getLoginUser() != null){
			String userID = getLoginUser().getUid();
			try{
				String mobile = dealNull(request.getParameter("mobile"));//第三方ID
				String mobilecode = dealNull(request.getParameter("code"));//验证码
				data.put("mobile", mobile.length() == 11?mobile.substring(0, 3)+"****"+mobile.substring(7, 11):mobile);
				//看当前用户是否已有手机号
				String phone = snsService.checkUserSNS_Phone(userID);
				if (null == phone || "".equals(phone)) {
					//没有绑定过
					//数据库中记录的验证码
					String origin_code = registService.getCodeByMobile(mobile);
					if(mobilecode.equals(origin_code)){
						snsService.boundUserSNS_Phone(userID,mobile);
						flag = true;
						code = "1";
						msg = "绑定成功";
						
						//记录日志
						LogUtil.addModUserInfoLog(request);
						
					}else{
						code = "4";
						msg = "验证码不正确";
					}
				}else{
					code = "3";
					msg = "用户已绑定";
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			code = "5";
			msg = "用户未登录";
		}
		return new ResponseMsg(flag, code, msg, codeDesc, data);
	}
	/**
	 * 验证手机验证码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/verifyPhoneCode")
	public @ResponseBody  ResponseMsg verifyPhoneCode(HttpServletRequest request,HttpServletResponse response) throws Exception {

		Map<String, String> params = getParameterMap(request);
		//参数
		String mobile = request.getParameter("mobile");
		String phonecode = request.getParameter("code");
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		//数据库中记录的验证码
		String origin_code = registService.getCodeByMobile(mobile);
		
		boolean flag = false;
		String code = "FAILED";
		String msg = "FAILED";
		String codeDesc = "FAILED:失败;"
						+ "SUCCESS:验证成功;"
						+ "ERRORCODE:验证码错误;";
		Map<String, String> data = new HashMap<String, String>();
		data.put("mobile", mobile);
		data.put("code", phonecode);
		if (phonecode.equals(origin_code)) {
			flag = true;
			code = "SUCCESS";
			msg = "SUCCESS";
		} else{
			code = "ERRORCODE";
			msg = "ERRORCODE";
		}
		return new ResponseMsg(flag, code, msg, codeDesc, data);
	}
	/**
	 * 发送找回密码的邮件
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendBoundEmail")
	public @ResponseBody ResponseMsg sendBoundEmail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String email = params.get("email");
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		boolean flag = false;
		String code = "4";
		String msg = "发送失败";
		String codeDesc = "1:邮箱格式错误;"
						+ "2:邮箱已注册;"
						+ "3:发送成功;"
						+ "4:发送失败;";
		Map<String, String> data = new HashMap<String, String>();
		com.kuke.auth.login.bean.User user = this.getLoginUser();
		//格式检查
		boolean emailCheck = email.matches("\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		if (emailCheck) {
			//注册检查
			if(!registService.checkUserByMail(email)){
				try {
					sendFindPassEmail(email,from,user.getUid());
					String emailLogin0 = email.substring(email.indexOf("@") + 1);
					
					Map<String, String> map = new HashMap<String, String>();
					map.put("qq.com", "http://mail.qq.com");
					map.put("gmail.com", "http://mail.google.com");
					map.put("sina.com", "http://mail.sina.com.cn");
					map.put("163.com", "http://mail.163.com");
					map.put("126.com", "http://mail.126.com");
					map.put("yeah.net", "http://www.yeah.net");
					map.put("sohu.com", "http://mail.sohu.com");
					map.put("tom.com", "http://mail.tom.com");
					map.put("sogou.com", "http://mail.sogou.com");
					map.put("139.com", "http://mail.10086.cn");
					map.put("hotmail.com", "http://www.hotmail.com");
					map.put("live.com", "http://login.live.com");
					map.put("live.cn", "http://login.live.cn");
					map.put("live.com.cn", "http://login.live.com.cn");
					map.put("189.com", "http://webmail16.189.cn/webmail");
					map.put("yahoo.com.cn", "http://mail.cn.yahoo.com");
					map.put("yahoo.cn", "http://mail.cn.yahoo.com");
					map.put("foxmail.coom", "http://www.foxmail.com");
					map.put("eyou.com", "http://www.eyou.com");
					map.put("21cn.com", "http://mail.21cn.com");
					map.put("188.com", "http://www.188.com");
					map.put("kuke.com", "http://mail.kuke.com");
					String key = emailLogin0;
					String emailLogin="";
					if (map.containsKey(key)) {
						emailLogin = map.get(key);
					} else {
						emailLogin = "http://" + key;
					}
					
					String mailSitName = emailLogin0.substring(0,emailLogin0.indexOf("."));
					data.put("email", email);//邮箱
					data.put("emailLogin", emailLogin);//登录邮箱的地址
					data.put("mailSitName", mailSitName);//域名
					flag = true;
					code = "3";
					msg = "发送成功";
					
					//记录日志
					LogUtil.addModUserInfoLog(request);
					
				} catch (Exception e) {
					code = "4";
					msg = "发送失败:"+e.getMessage();
					e.printStackTrace();
				}
			}else{
				code = "2";
				msg = "邮箱已注册";
			}
		} else {
			code = "1";
			msg = "邮箱格式错误";
		}
		return new ResponseMsg(flag, code, msg, codeDesc, data);
	}
	/**
	 * 发邮件
	 * @param email
	 * @return
	 * @throws Exception 
	 */
	private Result sendFindPassEmail(String email,String from,String uid) throws Exception {
		try {
			MD5 md5 = new MD5();
			Date date = new Date();
			// 组合激活码
			StringBuffer active_url = new StringBuffer();
			long timeNum = date.getTime();
			active_url.append(String.valueOf(PropertiesHolder.getContextProperty("auth.url"))
							  +"/kuke/user/verifyBoundEmail?email=" + email
					          + "&time=" + timeNum + "&uid="+uid+"&from=" + from + "&key=");
			active_url.append(md5.getMD5ofStr(
							new StringBuffer(
							"kukeBoundEmail" + timeNum).reverse().toString()).toUpperCase()
							+ md5.getMD5ofStr(email + "kukeBoundEmail").toUpperCase());
			// 组合文本
			StringBuffer content = new StringBuffer();
			content.append(email
					+ "，您好：<br/>  欢迎使用库客绑定邮箱功能。请点击以下链接绑定您的邮箱<br/>");
			content.append("<a href='"+active_url.toString()+"' target='_blank'>"+active_url.toString()+"</a>");
			content.append("<br/><span style='color: red;'>该链接30分钟内有效</span>");
			content.append("<br/> 如果您没有申请绑定邮箱，请忽略此邮件。");
			content.append("<br/>如果您有任何疑问，请与库客客服联系：400-650-1812。时间：周一至周五 9点-18点");
			content.append("<br/><strong>库客音乐</strong>");
			content.append("<br/><a href='"+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"'>"+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"</a> <br/>");
			content.append("" + DateUtil.date2str(new Date(),"yyyy-MM-dd"));
			MailUtil.sendMail(email, "库客音乐-绑定邮箱", content.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
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
