package com.kuke.auth.feedback.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.feedback.service.FeedBackService;
import com.kuke.auth.log.util.LogUtil;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.util.StringUtil;


@Controller
@RequestMapping("/kuke/userCenter")
public class FeedBackController {
	
	
	@Autowired
	private FeedBackService feedBackService;
	/**
	 * 用户反馈
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/feedback")
	public ResponseMsg feedback(HttpServletRequest request,HttpServletResponse response){
		String content = StringUtil.dealNull(request.getParameter("content"));
		String contact = StringUtil.dealNull(request.getParameter("contact"));
		String email = StringUtil.dealNull(request.getParameter("email"));
		String from_client = StringUtil.dealNull(request.getParameter("from_client"));//web   android  ios
		Map<String, Object> map =new HashMap<String, Object>();
		if(content == null){
			return new ResponseMsg(false, "1", "提交内容不能为空", "1:提交内容不能为空,2:联系方式不能为空",null);
		}
		if("web".equals(from_client)){//网页端
			if("".equals(contact) && "".equals(email)){
				return new ResponseMsg(false, "2", "联系方式不能都为空", "1:提交内容不能为空,2:联系方式不能为空",null);
			}else if(!"".equals(contact)){//电话号码不为空
				if(!isMobile(contact) && !isPhone(contact)){
					return new ResponseMsg(false, "4", "联系电话应为手机号或电话号码", "1:提交内容不能为空,2:联系方式不能为空",null);
				}
			}else if(!"".equals(email)){
				if(!isEmail(email)){
					return new ResponseMsg(false, "5", "请填写合法的E-MAIL", "1:提交内容不能为空,2:联系方式不能为空",null);
				}
			}
		}else{//app
			if("".equals(contact)){
				return new ResponseMsg(false, "2", "联系方式不能为空", "1:提交内容不能为空,2:联系方式不能为空",null);
			}else{//不为空,校验
				if(!isMobile(contact) && !isPhone(contact) && !isEmail(contact)){
					return new ResponseMsg(false, "4", "联系方式应为手机号或电话号码或合法邮箱", "1:提交内容不能为空,2:联系方式不能为空",null);
				}else{
					//手机\邮箱  分开
					if(isEmail(contact)){//邮箱
						email = contact;
						contact = "";
					}else{
						email = "";
					}
				}
			}
		}
		feedBackService.saveFeedBack(content, contact , email , from_client);
		
		//记录日志
		LogUtil.addVisitLog(request);
		
		return new ResponseMsg(true, "3", "反馈成功-感谢您使用库客音乐", "1:反馈失败,2:反馈成功",null);
		
	}
	/**
	 * 邮件校验
	 * @param str
	 * @return
	 */
	public boolean isEmail(String str){
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
		Pattern regex = Pattern.compile(check);    
		Matcher matcher = regex.matcher(str); 
		return  matcher.matches();
	}
	/** 
	  * 手机号验证 
	  * @return 验证通过返回true 
	  */  
	 public boolean isMobile(String str) {  
	     Pattern p = null;  
	     Matcher m = null;  
	     boolean b = false;  
	     p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号  
	     m = p.matcher(str);  
	     b = m.matches();  
	     return b;  
	 }  
	 /** 
	  * 电话号码验证 
	  * @return 验证通过返回true 
	  */  
	 public boolean isPhone(String str) {  
	     Pattern p1 = null, p2 = null;  
	     Matcher m = null;  
	     boolean b = false;  
	     p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的  
	     p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的  
	     if (str.length() > 9) {  
	        m = p1.matcher(str);  
	        b = m.matches();  
	     } else {  
	         m = p2.matcher(str);  
	        b = m.matches();  
	     }  
	     return b;  
	 }
}
