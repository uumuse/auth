package com.kuke.sych.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.regist.service.impl.RegistService;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.core.util.DateUtil;

/***
 * 将wap里注册的用户同步到阿里新库中
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/kuke")
public class SychUser {
	@Autowired
	private RegistService registService;
	
	@RequestMapping("/SychWapUser")
	@ResponseBody
	public String SychUser(HttpServletRequest request,HttpServletResponse response){
		try {
			request.setCharacterEncoding("UTF-8");  
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String user_id = request.getParameter("id");
		String nickName = request.getParameter("nick_name");
//		try {
//			nickName = URLDecoder.decode(request.getParameter("nick_name"), "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String key = request.getParameter("key");

		com.kuke.auth.regist.domain.User user = new com.kuke.auth.regist.domain.User();
		user.setId(user_id);
		user.setType("1");
		user.setName(nickName);
		user.setEmail(email);
		user.setPassword(UserAuthUtils.getMd5Password(password));
		user.setOrg_id(key==null?"":key);
		user.setIsactive("1");
		user.setReg_date(sdf.format(new Date()));
		user.setEnd_date(sdf.format(DateUtil.getafter7Day(new Date())));
		
		List list = registService.selectUserByEmail(user);
		if(list!=null && list.size()<1){
			registService.insertUserSych(user);
			registService.insertUserBasePertains(user_id,nickName);
			//新注册用户更新userauth，audio_date加7天
			registService.insertUserAuth(user.getId(),user.getId(),sdf.format(DateUtil.getafter7Day(new Date())));
			return "邮箱或用户Id已存在";
		}
		return "数据插入成功";
	}

}
