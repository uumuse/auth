package com.kuke.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/***
 * 部署在新www项目中，老的访问路径www.kuke.com/kuke/orgUser
 * 重新指向auth项目中auth.kuke.com/kuke/orgUser
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/kuke")
public class AuthRedirectController {

	/**
	 * *修改上传www时将orgUser1改为orgUser，现在与auth中orgUser冲突故改名
	 */
	@RequestMapping("/orgUser1")
	public String orgUser(HttpServletRequest request,HttpServletResponse response){
		String orgName = request.getParameter("orgName");
		String orgPwd = request.getParameter("orgPwd");
		return "redirect:http://auth.kuke.com/kuke/orgUser?orgName="+orgName+"&orgPwd="+orgPwd;
	}
}
