package com.kuke.auth.log.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.login.controller.LoginController;
import com.kuke.common.utils.ResponseMsg;

@Controller
@RequestMapping(value = "/kuke/log")
public class LogController {
	
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	/**
	 * 记录日志
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/add")
	public @ResponseBody ResponseMsg addLog(HttpServletRequest request,HttpServletResponse response){
		boolean flag = false;
		String msg = "";
		try {
			LogUtil.addVisitLog(request);//add日志
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		return new ResponseMsg(flag,msg);
	}
}
