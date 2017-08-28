package com.kuke.auth.userCenter.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.regist.domain.User;
import com.kuke.auth.regist.service.impl.RegistService;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.util.DateUtil;
import com.kuke.util.MD5;
import com.kuke.util.MailUtil;
import com.kuke.util.MessageFormatUtil;

@Controller
@RequestMapping(value = "/kuke/userCenter")
public class updatePasswordController extends BaseController {

	@Autowired
	private RegistService registService;
	
	@Autowired
	private UserCenterService userCenterService;
	/**
	 * 进入密码修改
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updatePassword")
	public Object updatePassword(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		User user = new User();
		user.setId(this.getLoginUser().getUid());
		user = registService.getUserInfo(user);
		request.setAttribute("position", request.getParameter("position"));
		request.setAttribute("autocompletename", request.getParameter("autocompletename"));
		request.setAttribute("autocompletepass", request.getParameter("autocompletepass"));
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("position", request.getParameter("position"));
		result.put("autocompletename", request.getParameter("autocompletename"));
		result.put("autocompletepass", request.getParameter("autocompletepass"));
		if (null!=user.getEmail() && StringUtils.isBlank(user.getEmail())) {
			if("m".equals(from)){
				result.put("step", "step1");
				result.put("user_name", user.getName());
				result.put("user_password", user.getPassword());
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, result), HttpStatus.OK);
			}else{
				request.setAttribute("step", "step1");
				//用户名
				request.setAttribute("user_name",user.getName());
				//用户密码
				request.setAttribute("user_password",user.getPassword());
				return "/userCenter/updatePasswordEmail";
			}
		} else {
			if("m".equals(from)){
				result.put("email", "user.getEmail()");
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, result), HttpStatus.OK);
			}else{
				request.setAttribute("email", user.getEmail());
				return "/userCenter/updatePassword";
			}
		}
	}
	/**
	 * 更新密码(输入旧密码)
	 * 
	 */
	@RequestMapping(value = "/savePassword")
	public @ResponseBody ResponseMsg savePassword(HttpServletRequest request,HttpServletResponse response) throws Exception {
		boolean flag = false;
		String code = "FAILED";
		String msg = "FAILED";
		String codeDesc = "ERROROLDPASS:密码错误;"
						+ "NOTEQUALS:新密码俩次输入不一致;"
						+ "ERRORLENGTH:新密码长度错误;"
						+ "SUCCESS:修改成功;"
						+ "FAILED:修改失败;"
						+ "";
		if(this.getLoginUser() != null){
			try {
				Map<String, String> params = getParameterMap(request);
				String oldPass = params.get("oldPass");
				String newPass = params.get("newPass");
				String newCPass = params.get("newCPass");
				User user = new User();
				user.setId(this.getLoginUser().getUid());
				user = registService.getUserInfo(user);
				if (!UserAuthUtils.getMd5Password(oldPass).equals(user.getPassword())) {
					code = "ERROROLDPASS";
					msg = "密码错误";
				} else if (!newPass.equals(newCPass)) {
					code = "NOTEQUALS";
					msg = "新密码俩次输入不一致";
				} else if (newPass.length() > 16 || newPass.length() < 6) {
					code = "ERRORLENGTH";
					msg = "新密码长度错误";
				} else {
					if (registService.updateUserPass(user.getId(),UserAuthUtils.getMd5Password(newPass)) == 1) {
						flag = true;
						code = "SUCCESS";
						msg = "修改成功";
						
						//记录日志
						LogUtil.addModUserInfoLog(request);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				code = "FAILED";
				msg = "修改失败";
			}
			return new ResponseMsg(flag, code, msg, codeDesc);
		}else{
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.NOMALLOGIN);
		}
	}

	/**
	 * 验证用户 发激活码到邮箱
	 * 
	 */
	@RequestMapping(value = "/addEmail")
	public @ResponseBody ResponseMsg addEmail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String email = params.get("email");
		String userName = params.get("userName");
		String password = params.get("password");
		User user = new User();
		user.setId(this.getLoginUser().getUid());
		user = registService.getUserInfo(user);
		MD5 md5 = new MD5();
		JSONObject jsonObjects = new JSONObject();
		boolean flag = false;
		String code = "";
		String msg = "";
		String codeDesc = "ERROREMAIL:邮箱格式不对;"
						+ "EXITEMAIL:已经被注册的email;"
						+ "ERRORNAMEPASS:错误的用户名密码;"
						+ "SUCCESS:成功;"
						+ "FAILED:失败";
		// 判断是否没邮箱
		if (StringUtils.isBlank(user.getEmail())) {

			// 校验邮箱
			boolean emailCheck = email
					.matches("\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
			if (!emailCheck || null == email || email.equals("")) {
				code = "ERROREMAIL";
				msg = "ERROREMAIL";
			} else if (registService.checkUserByMail(email)) {
				// 已经被注册的email
				code = "EXITEMAIL";
				msg = "EXITEMAIL";
			} else if (!(userName.equals(user.getName())) || !(password.equals(user.getPassword()))) {
				code = "ERRORNAMEPASS";
				msg = "ERRORNAMEPASS";
			} else {
				sendVerifyCodeEmail(user.getName(), email);
				flag = true;
				code = "SUCCESS";
				msg = "SUCCESS";
				
				//记录日志
				LogUtil.addModUserInfoLog(request);
			}
		}
		return new ResponseMsg(flag, code, msg, codeDesc);
	}

	/**
	 * 检查验证码
	 * 
	 */
	@RequestMapping(value = "/verifyCode")
	public @ResponseBody ResponseMsg verifyCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		HttpSession session = request.getSession();
		String code = params.get("code");
		String email = params.get("email");
		Date verifyCodeTime = (Date) session.getAttribute("verifyCodeTime");
		JSONObject jsonObjects = new JSONObject();
		User user = new User();
		user.setId(this.getLoginUser().getUid());
		boolean flag = false;
		String codeMsg = "";
		String msg = "";
		String codeDesc = "TIMEOUT:时间已超时;"
						+ "SUCCESS:成功;"
						+ "ERRORDATA:错误数据"
						+ "ERRORCODE:错误code;";
		if (new Date().getTime() - verifyCodeTime.getTime() > 30 * 60 * 1000) {
			codeMsg = "TIMEOUT";
			msg = "TIMEOUT";
		} else if (code.equals(request.getSession().getAttribute("code"))) {
			if (registService.updateUserEmail(user.getId(), email) == 1) {
				flag = true;
				codeMsg = "SUCCESS";
				msg = "SUCCESS";
			} else {
				codeMsg = "ERRORDATA";
				msg = "ERRORDATA";
			}
		} else {
			codeMsg = "ERRORCODE";
			msg = "ERRORCODE";
		}
		return new ResponseMsg(flag, codeMsg, msg, codeDesc);
	}

	/**
	 * 重新发送添加邮箱 验证码邮件
	 * 
	 */
	@RequestMapping(value = "/reSendCodeEmail")
	public @ResponseBody ResponseMsg reSendCodeEmail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			Map<String, String> params = getParameterMap(request);
			String email = params.get("email");
			User user = new User();
			user.setId(this.getLoginUser().getUid());
			user = registService.getUserInfo(user);
			sendVerifyCodeEmail(user.getName(), email);
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
		}
	}

	// 发激活码邮件
	public void sendVerifyCodeEmail(String userName, String userMail) {
		try {
			Random random = new Random();
			String s = "abcdefghijklmnpqrstuvwxyz123456789"; // 设置备选验证码:包括"a-z"和数字"1-9"
			String code = "";
			for (int i = 0; i < 4; i++) {
				String ch = String
						.valueOf(s.charAt(random.nextInt(s.length())));
				code += ch;
			}
			HttpSession session = getRequest().getSession();
			session.setAttribute("code", code);
			session.setAttribute("verifyCodeTime", new Date());
			// 组合文本
			StringBuffer content = new StringBuffer();
			content.append(userMail + "，您好： <br/>   您的验证码为： <br/>");
			content.append(code+"<br/>");
			content.append(" <span style='color: red;'>验证码30分钟内有效</span> <br/>");
			content.append("如果您没有设置安全邮箱，请忽略此邮件。  <br/>");
			content.append("如果您有任何疑问，请与库客客服联系：400-650-1812。时间：周一至周五 9点-18点<br/>");
			content.append("<strong>库客音乐</strong><br/>");
			content.append("<a href='"+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"'>"+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"</a> <br/>");
			content.append("" + DateUtil.date2str(new Date(),"yyyy-MM-dd"));

			MailUtil.sendMail(userMail, "库客音乐-设置安全邮箱", content.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
