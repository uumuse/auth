package com.kuke.core.interseptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.UserOauth;
import com.kuke.core.engine.ICookie;

public class Interceptor extends HandlerInterceptorAdapter {

	private static Logger logger = LoggerFactory.getLogger(Interceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		/**
		 * 用户信息
		 */
		String ssoid = dealNull(request.getParameter("ssoid"));//移动端过来时,必加的参数
		
		if("".equals(ssoid)){//web端
			logger.debug("web端请求start ...");
			ssoid = ICookie.get(request, KuKeAuthConstants.SSO_USER_COOKIE_NAME);// KuKeDesktopSSOID  (web端得到)
			User user = UserOauth.userLoginByToken(ssoid);
			if(!KuKeAuthConstants.SUCCESS.equals(user.getUser_status())){
//				response.sendRedirect("/kuke/login/index");
				return true;
			}else{
				request.setAttribute("userLoginMap", user);
				return true;
			}
		}else{//移动端
			logger.debug("移动端请求start ...");
			User user = UserOauth.userLoginByToken(ssoid);
			if(!KuKeAuthConstants.SUCCESS.equals(user.getUser_status())){//不成功,不添加userLoginMap;后台判断得不到userLoginMap,返回未登录的信息
				return true;
			}else{
				request.setAttribute("userLoginMap", user);
				return true;
			}
		}
//		/**
//		 * 用户信息
//		 */
//		String uid = dealNull(request.getParameter("uid"));//移动端过来时:必加的参数 uid
//		String from_client = dealNull(request.getParameter("from_client"));//移动端过来时:必加的参数来自的客户端
//		//from_client : web , ios , android
//		if("".equals(uid) || "".equals(from_client)){//web端
//			logger.debug("web端请求start ...");
//			User user = UserOauth.userLoginByToken(request);
//			if(!KuKeAuthConstants.SUCCESS.equals(user.getUser_status())){
//				response.sendRedirect("/kuke/login/index");
//				return false;
//			}else{
//				request.setAttribute("userLoginMap", user);
//				return true;
//			}
//		}else{//移动端
//			logger.debug("移动端请求start ...");
//			User user = UserOauth.userLoginByToken(uid, from_client);
//			if(!KuKeAuthConstants.SUCCESS.equals(user.getUser_status())){//不成功,不添加userLoginMap;后台判断得不到userLoginMap,返回未登录的信息
//				return true;
//			}else{
//				request.setAttribute("userLoginMap", user);
//				return true;
//			}
//		}

	}
	/**
	 * 判断空字符串
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
