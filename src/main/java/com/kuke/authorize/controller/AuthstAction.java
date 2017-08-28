package com.kuke.authorize.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.core.base.BaseController;
import com.kuke.core.util.MD5;
/***
 * 超星-首图接口
 * @author 
 *
 */
@Controller
@RequestMapping("/kuke/authorize")
public class AuthstAction extends BaseController {
	@RequestMapping(value = "/stAuth")
	@ResponseBody
	public String userInf(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Logger logger = LoggerFactory.getLogger(AuthstAction.class);
		String uname =  request.getParameter("uname");
		String time = request.getParameter("time");
		String ak = request.getParameter("ak");
		logger.debug("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%------ak::::::"+ak);
		String md5Str = new MD5().getMD5ofStr(uname+"_"+time+"_shoutu");
		logger.debug("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%-------md5Str::::::"+md5Str);
		if(ak!=null&&ak.equals(md5Str.toUpperCase())){
			return "success";
		}else{
			return "fail";
			}
	}
}
