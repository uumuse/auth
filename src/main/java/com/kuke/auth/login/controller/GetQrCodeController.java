package com.kuke.auth.login.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.snslogin.quartz.ApplicationUser;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.UserOauth;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.engine.ICookie;

/**
 * 生成二维码图片以及uuid
 * @author wanyj
 *
 */
@Controller
@RequestMapping("/kuke/qrcode")
public class GetQrCodeController extends BaseController {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 检测登录(手机客户端扫码登录)
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/check")
	public @ResponseBody ResponseMsg check(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		String uuid = request.getParameter("uuid");
		System.out.println("in");
		System.out.println("uuid:" + uuid);
		long inTime = new Date().getTime();
		User user = null;
		Boolean bool = true;
		
		boolean flag = false;
		String msg = "FAILED";
		
		while (bool) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//检测登录
			user = ApplicationUser.getInstance().userMapInfo(uuid);
			System.out.println("user:" + user);
			if(user != null){
				bool = false;
				ApplicationUser.getInstance().userMapOut(uuid);
				break;
			}else{
				if(new Date().getTime() - inTime > 3000){
					bool = false;
				}
			}
		}
		if(user != null){
			flag = true;
			msg = "SUCCESS";
			User webUser = UserAuthUtils.getInstance().UserLogin(user.getUid(), "web");
			if (webUser .getUser_status() != null && webUser.getUser_status().equals(KuKeAuthConstants.SUCCESS)) {
				//KuKeDesktopSSOID
				UserAuthUtils.setUserCookie(response, webUser, "1");
				flag = true;
				msg = "SUCCESS";
			}
			result.put("user", user);
		}
		return new ResponseMsg(flag, msg, result);
	} 
	/**
	 * 手机扫码登录调用接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/phoneLogin")
	public @ResponseBody ResponseMsg phoneLogin(HttpServletRequest request,HttpServletResponse response){
		boolean flag = false;
		String msg = "FAILED";
		String uuid = request.getParameter("uuid");
		String ssoid = request.getParameter("ssoid");//已登录用户的ssoid
		User user = null;
		try {
			// userid得到user信息
			user = UserOauth.userLoginByToken(ssoid);
			if(user.getUser_status() != null && user.getUser_status().equals(KuKeAuthConstants.SUCCESS)){
					//将登陆信息存入内存map
					if(ApplicationUser.getInstance().userMapIn(uuid, user)){
						flag = true;
						msg = "SUCCESS";
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseMsg(flag, msg);
	}
}
