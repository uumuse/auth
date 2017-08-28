package com.kuke.auth.userCenter.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.regist.service.impl.RegistService;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.userCenter.dto.Result;
import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.util.DateUtil;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.MD5;
import com.kuke.util.MailUtil;
import com.kuke.util.MessageFormatUtil;

@Controller
@RequestMapping(value = "/kuke/findPassword")
public class findPasswordController extends BaseController {

	@Autowired
	private RegistService registService;
	
	@Autowired
	private UserCenterService userCenterService;
	
	@RequestMapping(value = "/findpwd")
	public String findpwd(HttpServletRequest request,HttpServletResponse response){
		this.getUserInfo(request, response);
		return "findpwd/reset";
	}
	/**
	 * 验证邮箱是否存在于用户数据库中
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkUserEmail")
	public @ResponseBody ResponseMsg checkUserEmail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		int count = 0;
		String userEmail = request.getParameter("userEmail");
		if(userEmail != null && !"".equals(userEmail)){
			count = userCenterService.checkUserEmail(userEmail);
		}	
		result.put("result", String.valueOf(count));
		if(count == 0){
			return MessageFormatUtil.formatStateToObject(KuKeAuthConstants.FAILED, result);
		}else{
			return MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, result);
		}
	}
	/**
	 * 验证手机号是否存在于用户数据库中
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkUserPhone")
	public @ResponseBody ResponseMsg checkUserPhone(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		int count = 0;
		String phoneNum = request.getParameter("phoneNum");
		if(phoneNum != null && !"".equals(phoneNum)){
			count = userCenterService.checkBoundPhone(phoneNum);
		}	
		result.put("result", count);
		if(count == 0){
			return MessageFormatUtil.formatStateToObject(KuKeAuthConstants.FAILED, result);
		}else{
			return MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, result);
		}
	}
	/* 找回密码开始 */
	/**
	 * 发送找回密码的手机验证码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendFindPhoneNum")
	public Object sendFindPhoneNum(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String mobile = params.get("mobile");
		System.out.println("mobile:"+mobile);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		boolean flag = false;
		String code = "FAILED";
		String msg = "FAILED";
		String codeDesc = "FAILED:失败;"
						+ "SUCCESS:成功;";
		Map<String, Object> data = new HashMap<String, Object>();
		this.getUserInfo(request, response);//设置url
		try {
			//下发短信接口
			String post_url = KuKeUrlConstants.userSendPhoneNum;
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("mobile", mobile));
			String result = HttpClientUtil.executeServicePOST(post_url, nvps);
			ResponseMsg msgObject = new ResponseMsg(result);
			if(msgObject.getFlag()){//成功
				data.put("mobile", mobile);
				flag = true;
				
				//记录日志
				LogUtil.addFindPassLog(request);
				
				if("m".equals(from)){
					code = "SUCCESS";
					msg = "SUCCESS";
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(flag, code, msg, codeDesc, data), HttpStatus.OK);
				}else{
					if(flag){
						request.setAttribute("mobile", mobile);
						return "findpwd/reset_inputcode";
					}else{
						return "findpwd/findPassword";
					}
				}
			}else{//失败
				if("m".equals(from)){
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(flag, code, msg, codeDesc, data), HttpStatus.OK);
				}else{
					if(flag){
						return "findpwd/reset_inputcode";
					}else{
						return "findpwd/findPassword";
					}
				}
			}
		} catch (Exception e) {
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(flag, code, msg, codeDesc, data), HttpStatus.OK);
			}else{
				if(flag){
					return "findpwd/reset_inputcode";
				}else{
					return "findpwd/findPassword";
				}
			}
		}
	}
	/**
	 * 验证手机验证码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/verifyPhoneNum")
	public Object verifyPhoneNum(HttpServletRequest request,HttpServletResponse response) throws Exception {

		Map<String, String> params = getParameterMap(request);
		//参数
		String mobile = dealNull(request.getParameter("mobile"));
		String phonecode = dealNull(request.getParameter("code"));
		String pwd = dealNull(request.getParameter("pwd"));//移动端传过来密码,直接修改
		String time = String.valueOf(new Date().getTime());
		String key = "";
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		//数据库中记录的验证码
		String origin_code = registService.getCodeByMobile(mobile);
		
		boolean flag = false;
		String code = "FAILED";
		String msg = "验证失败";
		String codeDesc = "FAILED:验证失败;"
						+ "SUCCESS:验证成功;"
						+ "ERRORCODE:验证码错误;";
		Map<String, String> data = new HashMap<String, String>();
		MD5 md5 = new MD5();
		
		key = md5.getMD5ofStr(new StringBuffer(KuKeAuthConstants.FINDPASSWORD + time).reverse().toString()).toUpperCase() + md5.getMD5ofStr(mobile + KuKeAuthConstants.FINDPASSWORD).toUpperCase();
		this.getUserInfo(request, response);//设置url
		if("m".equals(from)){
			String mcode = "";
			String mmsg = "";
			if("".equals(pwd)){
				mcode = "1";
				mmsg = "pwd不能为空";
			}else if(pwd.length() < 6 || pwd.length() > 16){
				mcode = "2";
				mmsg = "pwd应该为6-16位";
			}else if("".equals(mobile)){
				mcode = "3";
				mmsg = "手机不能为空";
			}else if("".equals(phonecode)){
				mcode = "4";
				mmsg = "验证码不能为空";
			}
			if(!"".equals(mcode)){
				return new ResponseMsg(flag, mcode, mmsg, codeDesc);
			}
			data.put("mobile", mobile);
			data.put("code", phonecode);
			data.put("time", time);
			data.put("key", key);
		}else{
			request.setAttribute("mobile", mobile);
			request.setAttribute("code", phonecode);
			request.setAttribute("time", time);
			request.setAttribute("key", key);
		}
		if (phonecode.equals(origin_code)) {
			flag = true;
		} else{
			code = "ERRORCODE";
			msg = "验证码错误";
		}
		if("m".equals(from)){//移动端需要更新密码
			if(flag){//验证通过,改密码
				try {
					flag = true;
					code = "SUCCESS";
					msg = "验证成功";
					registService.resetPassByPhone(UserAuthUtils.getMd5Password(pwd), mobile);
				} catch (Exception e) {
					e.printStackTrace();
					flag = false;
				}
			}
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(flag, code, msg, codeDesc, data), HttpStatus.OK);
		}else{
			request.setAttribute("mobile", mobile);
			if(flag){
				return "findpwd/reset_inputpwd";
			}else{
				request.setAttribute("result", msg);
				return "findpwd/reset_inputcode";
			}
		}
	}
	/**
	 * 重新发送手机验证码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/reSendFindPhoneNum")
	public @ResponseBody ResponseMsg reSendFindPhoneNum(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			Map<String, String> params = getParameterMap(request);
			String mobile = params.get("mobile");
			//下发短信接口
			String post_url = KuKeUrlConstants.userSendPhoneNum;
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("mobile", mobile));
			String result = HttpClientUtil.executeServicePOST(post_url, nvps);
			ResponseMsg msgObject = new ResponseMsg(result);
			if(msgObject.getFlag()){
				return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
			}else{
				return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
		}
	}
	/**
	 * 发送找回密码的邮件
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sendFindEmail")
	public Object sendFindEmail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String email = params.get("email");
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		boolean flag = false;
		String code = "4";
		String msg = "发送失败";
		String codeDesc = "1:邮箱格式错误;"
						+ "2:邮箱未注册;"
						+ "3:发送成功;"
						+ "4:发送失败;";
		Map<String, String> data = new HashMap<String, String>();
		//格式检查
		boolean emailCheck = email.matches("\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		this.getUserInfo(request, response);//设置url
		if (emailCheck) {
			//注册检查
			if(registService.checkUserByMail(email)){
				try {
					sendFindPassEmail(email,from);
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
					if("m".equals(from)){
						data.put("email", email);//邮箱
						data.put("emailLogin", emailLogin);//登录邮箱的地址
						data.put("mailSitName", mailSitName);//域名
					}else{
						request.setAttribute("email", email);//邮箱
						request.setAttribute("emailLogin", emailLogin);//登录邮箱的地址
						request.setAttribute("mailSitName", mailSitName);//域名
					}
					flag = true;
					code = "3";
					msg = "发送成功";
					
					//记录日志
					LogUtil.addFindPassLog(request);
					
				} catch (Exception e) {
					code = "4";
					msg = "发送失败:"+e.getMessage();
					e.printStackTrace();
				}
			}else{
				code = "2";
				msg = "邮箱未注册";
			}
		} else {
			code = "1";
			msg = "邮箱格式错误";
		}
		if("m".equals(from)){
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(flag, code, msg, codeDesc, data), HttpStatus.OK);
		}else{
			if(flag){
				return "findpwd/reset_sendEmailSuccess";
			}else{
				return "findpwd/reset";
			}
		}

	}
	/**
	 * 重新发送找回密码邮件
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/reSendFindEmail")
	public @ResponseBody ResponseMsg reSendFindEmail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			Map<String, String> params = getParameterMap(request);
			String email = params.get("email");
			String from = dealNull(params.get("from"));//m:移动端,否则网页端
			JSONObject jsonObject = new JSONObject();
			//发送邮件
			sendFindPassEmail(email,from);
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
		}
	}
	/**
	 * 验证邮件链接地址
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/verifyEmailUrl")
	public Object verifyEmailUrl(HttpServletRequest request,HttpServletResponse response) throws Exception {

		Map<String, String> params = getParameterMap(request);
		String email = dealNull(params.get("email"));
		String time = dealNull(params.get("time"));
		String key = dealNull(params.get("key"));
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		MD5 md5 = new MD5();
		String newKey = md5.getMD5ofStr(new StringBuffer(KuKeAuthConstants.FINDPASSWORD + time).reverse().toString()).toUpperCase()
				+ md5.getMD5ofStr(email + KuKeAuthConstants.FINDPASSWORD).toUpperCase();
		Long sendTime = Long.parseLong(time);
		Long now = new Date().getTime();
		boolean isTimeOut = (now - sendTime) < 30 * 60 * 1000 ? true : false;
		boolean flag = false;
		String code = "FAILED";
		String msg = "密码重置失败";
		String codeDesc = "TIMEOUT:时间超时;"
						+ "SUCCESS:验证成功;"
						+ "ERRORKEY:KEY验证错误;";
		Map<String, String> data = new HashMap<String, String>();
		request.setAttribute("email", email);
		request.setAttribute("time", time);
		request.setAttribute("key", key);
		request.setAttribute("from", from);
		request.setAttribute("type", "email");
		this.getUserInfo(request, response);//设置url
		if (key.equals(newKey) && isTimeOut) {
			return "findpwd/reset_inputpwd";
		} else {
			if (!isTimeOut) {
				code = "TIMEOUT";
				msg = "时间已超时";
				request.setAttribute("result", "时间超时");
			} else if (!key.equals(newKey)) {
				code = "ERRORKEY";
				msg = "KEY验证错误";
				request.setAttribute("result", "KEY验证错误");
			}
			request.setAttribute("msg", msg);
			request.setAttribute("url",KuKeUrlConstants.wwwUrl);
			return "findpwd/reset_success";
		}
	}

	/**
	 * 重置密码(邮件过去的)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resetPassword")
	public Object resetPassword(HttpServletRequest request,HttpServletResponse response) throws Exception {

		Map<String, String> params = getParameterMap(request);
		String email = params.get("email");
		String key = params.get("key");
		String time = params.get("time");
		String password = params.get("pass");
		String rePass = params.get("rePass");
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		MD5 md5 = new MD5();
		String newKey = md5.getMD5ofStr(
				new StringBuffer(KuKeAuthConstants.FINDPASSWORD + time).reverse()
						.toString()).toUpperCase()
				+ md5.getMD5ofStr(email + KuKeAuthConstants.FINDPASSWORD).toUpperCase();
		boolean flag = false;
		String code = "";
		String msg = "";
		String codeDesc = "ERRORKEY:KEY验证错误;"
						+ "PASSNOTEQUAL:俩次密码不一致;"
						+ "ERRORLENGTH:密码长度错误"
						+ "FAILED:密码重置失败"
						+ "SUCCESS:密码已重置";
		this.getUserInfo(request, response);//设置url
		if (key.equals(newKey) && password.equals(rePass)) {
			registService.resetPassByEmail(UserAuthUtils.getMd5Password(password), email);
			flag = true;
			code = "SUCCESS";
			msg = "密码已重置";
		} else {
			if (!key.equals(newKey)) {
				code = "ERRORKEY";
				msg = "KEY验证错误";
			} else if (!password.equals(rePass)) {
				code = "PASSNOTEQUAL";
				msg = "俩次密码不一致";
			} else if (password.length() > 16 || password.length() < 6) {
				code = "ERRORLENGTH";
				msg = "密码长度错误";
			} else {
				code = "FAILED";
				msg = "密码重置失败";
			}
		}
		if("m".equals(from)){
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(flag, code, msg, codeDesc), HttpStatus.OK);
		}else{
			request.setAttribute("result", code);
			request.setAttribute("msg", msg);
			request.setAttribute("wwwurl", KuKeUrlConstants.wwwUrl);
			return "findpwd/reset_success";
		}
	}
	/**
	 * 重置密码(手机过去的)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resetPasswordForphone")
	public Object resetPasswordForphone(HttpServletRequest request,HttpServletResponse response) throws Exception {

		Map<String, String> params = getParameterMap(request);
		String mobile = dealNull(params.get("mobile"));
		String key = dealNull(params.get("key"));
		String time = dealNull(params.get("time"));
		String password = dealNull(params.get("pass"));
		String rePass = dealNull(params.get("rePass"));
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		MD5 md5 = new MD5();
		
		String newKey = md5.getMD5ofStr(
				new StringBuffer(KuKeAuthConstants.FINDPASSWORD + time).reverse()
						.toString()).toUpperCase()
				+ md5.getMD5ofStr(mobile + KuKeAuthConstants.FINDPASSWORD).toUpperCase();
		boolean flag = false;
		String code = "";
		String msg = "";
		String codeDesc = "ERRORKEY:KEY验证错误;"
						+ "PASSNOTEQUAL:俩次密码不一致;"
						+ "ERRORLENGTH:密码长度错误"
						+ "FAILED:密码重置失败"
						+ "SUCCESS:密码已重置";
		this.getUserInfo(request, response);//设置url
		if (key.equals(newKey) && password.equals(rePass) && password.length() <= 16 && password.length() >= 6) {
			registService.resetPassByPhone(UserAuthUtils.getMd5Password(password), mobile);
			flag = true;
			code = "SUCCESS";
			msg = "密码已重置";
		} else {
			if (!key.equals(newKey)) {
				code = "ERRORKEY";
				msg = "KEY验证错误";
			} else if (!password.equals(rePass)) {
				code = "PASSNOTEQUAL";
				msg = "两次密码不一致";
			} else if (password.length() > 16 || password.length() < 6) {
				code = "ERRORLENGTH";
				msg = "密码长度错误";
			} else {
				code = "FAILED";
				msg = "密码重置失败";
			}
		}
		if("m".equals(from)){
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(flag, code, msg, codeDesc), HttpStatus.OK);
		}else{
			request.setAttribute("result", code);
			request.setAttribute("msg", msg);
			request.setAttribute("url", KuKeUrlConstants.wwwUrl);
			return "findpwd/reset_success";
		}
	}
	/**
	 * 发邮件
	 * @param email
	 * @return
	 * @throws Exception 
	 */
	private Result sendFindPassEmail(String email,String from) throws Exception {
		try {
			MD5 md5 = new MD5();
			Date date = new Date();
			String dateStr = DateUtil.date2str(date);
			// 组合激活码
			StringBuffer active_url = new StringBuffer();
			long timeNum = date.getTime();
			active_url.append(String.valueOf(PropertiesHolder.getContextProperty("auth.url"))
							  +"/kuke/findPassword/verifyEmailUrl?email=" + email
					          + "&time=" + timeNum + "&from=" + from + "&key=");
			active_url.append(md5.getMD5ofStr(
							new StringBuffer(
									KuKeAuthConstants.FINDPASSWORD + timeNum).reverse().toString()).toUpperCase()
							+ md5.getMD5ofStr(email + KuKeAuthConstants.FINDPASSWORD).toUpperCase());
			// 组合文本
			StringBuffer content = new StringBuffer();
			content.append(email
					+ "，您好：<br/>  欢迎使用库客找回密码功能。请点击以下链接重置您的密码<br/>");
			content.append("<a href='"+active_url.toString()+"' target='_blank'>"+active_url.toString()+"</a>");
			content.append("<br/><span style='color: red;'>该链接30分钟内有效</span>");
			content.append("<br/> 如果您没有申请找回密码，请忽略此邮件。");
			content.append("<br/>如果您有任何疑问，请与库客客服联系：400-650-1812。时间：周一至周五 9点-18点");
			content.append("<br/><strong>库客音乐</strong>");
			content.append("<br/><a href='"+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"'>"+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"</a> <br/>");
			content.append("" + DateUtil.date2str(new Date(),"yyyy-MM-dd"));
			MailUtil.sendMail(email, "库客音乐-找回密码", content.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}
	/* 找回密码结束 */
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
	public static void main(String[] args) {
		System.out.println();
	}
}
