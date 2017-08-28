package com.kuke.authorize.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kuke.auth.util.PropertiesHolder;
import com.kuke.core.base.BaseController;
import com.kuke.core.util.MD5;

/***
 * 广图-超星接口
 * @author JYZ
 *
 */
@Controller
@RequestMapping("/kuke/authorize")
public class AuthGTAction extends BaseController {
	
	Logger logger = LoggerFactory.getLogger(AuthGTAction.class);
	
	@RequestMapping(value = "/gtAuth")
	public String authGT(HttpServletRequest request,HttpServletResponse response){
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHH");// 构造格式化模板
		
		TimeZone time = TimeZone.getTimeZone("");
		TimeZone.setDefault(time);// 设置时区
		sdf.setTimeZone(time);
		Calendar calendar = Calendar.getInstance();// 获取实例
		Date date = calendar.getTime(); // 获取Date对象
		String dateStr = new String();
		dateStr = sdf.format(date);// 对象进行格式化，获取字符串格式的输出
		logger.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^dateStr^^^^^^^^^^^^^^^^^^^^^^^^"+dateStr);
		
		String uid =  request.getParameter("uid");
		String signkey =  request.getParameter("signkey");
		logger.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^signkey^^^^^^^^^^^^^^^^^^^^^^^^"+signkey);
		String secretKey = "G(Z@L*!IB";
		
		String md5Str = new MD5().getMD5ofStr(uid+secretKey+dateStr).toUpperCase();
		logger.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^uid+secretKey+dateStr^^^^^^^^^^^^^^^^^^^^^^^^"+uid+secretKey+dateStr);
		logger.debug("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^md5Str^^^^^^^^^^^^^^^^^^^^^^^^"+md5Str);
		if(md5Str.equals(signkey)){
			logger.debug("^^^^^^^^^^^^^^^^md5Str.equals(signkey)^^^^^^^^^^^^^^^^^");
			return "redirect:http://auth.kuke.com/kuke/orgUser?orgName=gzlib&orgPwd=gzlib83336277";
		}else{
			logger.debug("^^^^^^^^^^^^^^^^md5Str. NOT .equals(signkey)^^^^^^^^^^^^^^^^^");
			return "redirect:"+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"));
		}
	}
	

}
