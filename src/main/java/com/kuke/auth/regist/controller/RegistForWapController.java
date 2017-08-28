package com.kuke.auth.regist.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kuke.auth.regist.domain.User;
import com.kuke.auth.regist.service.impl.RegistService;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.util.DateUtil;
import com.kuke.util.IdGenerator;

@Controller
@RequestMapping("/kuke")
public class RegistForWapController {
	@Autowired
	private RegistService registService;
	
	/***
	 * 云cd保存用户信息
	 * @param mav
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/registWap")
	@ResponseBody
	public Object registWap(ModelAndView mav,HttpServletRequest request,HttpServletResponse response,String from){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ResponseEntity<ResponseMsg> re = null;
		
		String org_id = "";
		String code = request.getParameter("code");
		String mobile = request.getParameter("mobile");
		String pwd = request.getParameter("pwd");
		String from_client = request.getParameter("from_client");
		String key = request.getParameter("key");
		String origin_code = registService.getCodeByMobile(mobile);
		from = from==null?"":from;

		if(!key.equals("null")&&key!=null&&StringUtils.isNotBlank(key)){
					key = key.substring(8, 40);
					org_id = key;
				}
		User u = registService.getUserByPhone(mobile);
		if(u!=null){
			re = new ResponseEntity<ResponseMsg>(new ResponseMsg(false, "2", "用户已存在", "1:注册成功,2:用户已存在",null), HttpStatus.OK);
			return re;
		}
		//从移动端发过来请求
		User user = new User();
//			if(code.equals(origin_code)){
				String user_id = IdGenerator.getUUIDHex32();
				user.setId(user_id);
				user.setPassword(UserAuthUtils.getMd5Password(pwd));
				user.setPhone(mobile);
				user.setIsactive("1");
				user.setOrg_id(org_id);
				user.setFrom_client(from_client);
				user.setReg_date(sdf.format(new Date()));
				user.setEnd_date(sdf.format(DateUtil.getafter7Day(new Date())));
				try {
					registService.insertUser(user);
					registService.insertUserBasePertain(user_id);
					//新注册用户更新userauth，audio_date加7天
					registService.insertUserAuth(user.getId(),user.getId(),sdf.format(DateUtil.getafter7Day(new Date())));
					re = new ResponseEntity<ResponseMsg>(new ResponseMsg(true, "1", "注册成功", "1:注册成功,2:注册失败"), HttpStatus.OK);
					return re;
				} catch (Exception e) {
					e.printStackTrace();
					return re = new ResponseEntity<ResponseMsg>(new ResponseMsg(false, "2", "注册失败", "1:注册成功,2:注册失败"), HttpStatus.OK);
				}
			
	}
}
