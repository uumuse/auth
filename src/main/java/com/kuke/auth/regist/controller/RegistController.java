package com.kuke.auth.regist.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.regist.domain.User;
import com.kuke.auth.regist.service.impl.RegistService;
import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.OrgOauth;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.util.DateUtil;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.IdGenerator;
import com.kuke.util.RandomNumUtil;
import com.kuke.util.SendPhoneMsgUtil;

@Controller
@RequestMapping("/kuke")
public class RegistController {
	
	@Autowired
	private RegistService registService;
	
	/***
	 * 检测手机号是否存在
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/checkMobile")
	@ResponseBody
	public Object checkMobile(String mobile){
		User user = null;
		try {
			user = registService.getUserByPhone(mobile);
		} catch (Exception e) {
			return new ResponseMsg(false,"3","查询错误","1:此号码已存在,2:此号码可注册,3:查询错误");
		}
		if(user!=null){
			return new ResponseMsg(false,"1","此号码已存在","1:此号码已存在,2:此号码可注册,3:查询错误");
		}else{
			return new ResponseMsg(true,"2","此号码可注册","1:此号码已存在,2:此号码可注册,3:查询错误");
		}
	}
	/***
	 * 下发短信
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/issued")
	@ResponseBody
	public ResponseMsg issued(String mobile){
		//下发短信接口
		String randomNum = RandomNumUtil.createRandomVcode();
		String result =  SendPhoneMsgUtil.sendSms(mobile,randomNum, "1");
		if(result.contains("<error>0</error>")){
			registService.insertMobileCode(mobile, randomNum);
			return new ResponseMsg(true,"2","下发成功!","1:下发失败,2:下发成功,3:查询错误");
		}else{
			return new ResponseMsg(false,"1","下发失败!","1:下发失败,2:下发成功,3:查询错误");
		}
	}
	@RequestMapping("/issuedWeb")
	@ResponseBody
	public String issuedWeb(HttpServletRequest request,HttpServletResponse response){
		String mobile = request.getParameter("mobile");
//		ResponseMsg res  = new ResponseMsg();
		JSONObject res = new JSONObject();
		//下发短信接口
		String randomNum = RandomNumUtil.createRandomVcode();
		String result =  SendPhoneMsgUtil.sendSms(mobile,randomNum, "1");
//		String result =  "<error>0</error>";
		if(result.contains("<error>0</error>")){
			registService.insertMobileCode(mobile, randomNum);
			res.put("flag", true);
			res.put("code", "2");
			res.put("msg", "下发成功!");
			res.put("codeDesc", "1:下发失败,2:下发成功,3:查询错误");
//			res = new ResponseMsg(true,"2","下发成功!","1:下发失败,2:下发成功,3:查询错误","");
		}else{
			res.put("flag", false);
			res.put("code", "1");
			res.put("msg", "下发失败!");
			res.put("codeDesc", "1:下发失败,2:下发成功,3:查询错误");
//			res = new ResponseMsg(false,"1","下发失败!","1:下发失败,2:下发成功,3:查询错误","");
		}
//		String a = jsoncallback != null ? jsoncallback + "("+String.valueOf(JSONObject.fromObject(res))+")" : String.valueOf(JSONObject.fromObject(res));
//		return a;
//		return JSONObject.fromObject(res);
		return res.toString();
//		return jsoncallback != null ? jsoncallback + "("+(JSONObject.fromObject(res))+")" : (res);
	}
	
	@RequestMapping("/getCodeByMobile")
	@ResponseBody
	public ResponseMsg getCodeByMobile(String mobile){
		String code;
		try {
			code = registService.getCodeByMobile(mobile);
			Map<String,String> map = new HashMap<String,String>();
			if(code!=null){
				map.put("code", code);
				return new ResponseMsg(true,"2","验证码查询成功","1:验证码未下发,2:验证码查询成功,3:网络错误",map);
			}else
				return new ResponseMsg(false,"1","验证码未下发","1:验证码未下发,2:验证码查询成功,3:网络错误",null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseMsg(false,"3","网络错误","1:验证码未下发,2:验证码查询成功,3:网络错误",null);
		}
		
	}
	
	/***
	 * 检查手机号并下发短信
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/checkAndIssue")
	@ResponseBody
	public ResponseMsg checkAndIssue(String mobile){
		User user = null;
		try {
			user = registService.getUserByPhone(mobile);
		} catch (Exception e) {
			return new ResponseMsg(false,"3","查询错误","1:此号码已存在,2:此号码可注册,3:查询错误");
		}
		if(user==null){
			String randomNum = RandomNumUtil.createRandomVcode();
			String result =  SendPhoneMsgUtil.sendSms(mobile,randomNum, "1");
			if(result.contains("<error>0</error>")){
				registService.insertMobileCode(mobile, randomNum);
				return new ResponseMsg(true,"2","下发成功!","1:下发失败,2:下发成功,3:查询错误");
			}else{
				return new ResponseMsg(false,"1","下发失败!","1:下发失败,2:下发成功,3:查询错误");
			}
		}else{
			return new ResponseMsg(false,"1","此号码已存在","1:此号码已存在,2:此号码可注册,3:查询错误");
		}
	}
	

	/***
	 * 注册页
	 * @param request
	 * @return
	 */
	@RequestMapping("/registIndex")
	public Object registIndex(ModelAndView mav,HttpServletRequest request){
		mav.setViewName("/regist");
		return mav;
	}
	
	/***
	 * 保存用户信息
	 * @param mav
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/regist")
	public Object regist(ModelAndView mav,HttpServletRequest request,HttpServletResponse response,String from){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ResponseEntity<ResponseMsg> re = null;
		
		String code = request.getParameter("code");
		String mobile = request.getParameter("mobile");
		String pwd = request.getParameter("pwd");
		String from_client = request.getParameter("from_client");
		String origin_code = registService.getCodeByMobile(mobile);
		from = from==null?"":from;
		Organization org = new Organization();
		try {
			org = OrgOauth.orgLogin(request, response);
			System.out.println("try 中:====== "+org.toString());
		} catch (Exception e1) {
			System.out.println("e 中========="+org.toString());
			e1.printStackTrace();
			org.setOrg_id("");
		}
		System.out.println("try 后:==============================================================================:"+org.toString());
		User u = registService.getUserByPhone(mobile);
		if(u!=null){
			re = new ResponseEntity<ResponseMsg>(new ResponseMsg(false, "2", "用户已存在", "1:注册成功,2:用户已存在",null), HttpStatus.OK);
			return re;
		}
		//从移动端发过来请求
		User user = new User();
		if(from.equals("m")){
			if(code.equals(origin_code)){
				String user_id = IdGenerator.getUUIDHex32();
				user.setId(user_id);
				user.setPassword(UserAuthUtils.getMd5Password(pwd));
				user.setPhone(mobile);
				user.setIsactive("1");
				user.setOrg_id(org==null?"":org.getOrg_id());
				user.setFrom_client(from_client);
				user.setReg_date(sdf.format(new Date()));
				user.setEnd_date(sdf.format(DateUtil.getafter7Day(new Date())));
				try {
					registService.insertUser(user);
					registService.insertUserBasePertain(user_id);
					//新注册用户更新userauth，audio_date加7天
					registService.insertUserAuth(user.getId(),user.getId(),sdf.format(DateUtil.getafter7Day(new Date())));
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					nvps.add(new BasicNameValuePair("u", mobile));
					nvps.add(new BasicNameValuePair("p", pwd));
					nvps.add(new BasicNameValuePair("from_client", from_client));
					nvps.add(new BasicNameValuePair("type", "1"));
					String post_url = KuKeUrlConstants.userLoginByUP_URL;
//					String post_url ="http://192.168.0.107/kuke/ssouser/authuser";
					String result = HttpClientUtil.executeServicePOST(post_url, nvps);
					ResponseMsg msgObject = new ResponseMsg(result);
					return new ResponseEntity<ResponseMsg>(msgObject, HttpStatus.OK);
				} catch (Exception e) {
					e.printStackTrace();
					return re = new ResponseEntity<ResponseMsg>(new ResponseMsg(false, "2", "注册失败", "1:注册成功,2:注册失败"), HttpStatus.OK);
				}
//				 return re;
			}else{
				 re = new ResponseEntity<ResponseMsg>(new ResponseMsg(false, "3", "验证码错误或密码不一致", "3:验证码错误或密码不一致"), HttpStatus.OK);
				 return re;
			}
		}else{
			//web端请求
			if(code.equals(origin_code)){
				//insert user info and mobile info stay still
				String user_id = IdGenerator.getUUIDHex32();
				user.setId(user_id);
				user.setPhone(mobile);
				user.setPassword(UserAuthUtils.getMd5Password(pwd));
				user.setIsactive("1");
				user.setReg_date(sdf.format(new Date()));
				user.setEnd_date(sdf.format(DateUtil.getafter7Day(new Date())));
				user.setOrg_id(org.getOrg_id());
				user.setFrom_client(from_client);
				registService.insertUser(user);
				registService.insertUserBasePertain(user_id);
				registService.insertUserAuth(user.getId(),user.getId(),sdf.format(DateUtil.getafter7Day(new Date())));
//				String post_url ="http://192.168.0.107/kuke/ssouser/authuser";
				try {
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					nvps.add(new BasicNameValuePair("u", mobile));
					nvps.add(new BasicNameValuePair("p", pwd));
					nvps.add(new BasicNameValuePair("from_client", from_client));
					nvps.add(new BasicNameValuePair("type", "1"));
					String post_url = KuKeUrlConstants.userLoginByUP_URL;
					String result = HttpClientUtil.executeServicePOST(post_url, nvps);
					System.out.println("regist result:"+result);
					ResponseMsg msgObject = new ResponseMsg(result);
					com.kuke.auth.login.bean.User newuser = new com.kuke.auth.login.bean.User();
					newuser.setUser_status(KuKeAuthConstants.FAILED);
					try {
						newuser = new com.kuke.auth.login.bean.User(msgObject.getData().toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("regist newuser:"+newuser.toString());
					UserAuthUtils.getInstance().setUserCookie(response,newuser,"1");
					
					//记录注册日志
					LogUtil.addRegistLog(newuser.getUid(), newuser.getOrg_id(), request);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				re = new ResponseEntity<ResponseMsg>(new ResponseMsg(true, "1", "注册成功", "1:注册成功,2:注册失败"), HttpStatus.OK);
				return re;
			}else{
				re = new ResponseEntity<ResponseMsg>(new ResponseMsg(false, "1", "验证码不正确", "1:验证码不正确"), HttpStatus.OK);
				return re;
			}
		}
			
	}
	
//	@RequestMapping("/registForApp")
//	public Object registForApp(ModelAndView mav,HttpServletRequest request){
//		User user = new User();
//		String mobile = request.getParameter("mobile");
////		String code = request.getParameter("code");
////		String origin_code = registService.getCodeByMobile(mobile);
////		if(code.equals(origin_code)){
//			//insert user info and mobile info stay still
//			user.setId(IdGenerator.getUUIDHex32());
//			user.setPhone(mobile);
//			try {
//				registService.insertUser(user);
//				return new ResponseMsg(true,"2","注册成功!","1:注册失败,2:注册成功,3:数据插入错误");
//			} catch (Exception e) {
//				e.printStackTrace();
//				return new ResponseMsg(true,"1","注册失败!","1:注册失败,2:注册成功,3:数据插入错误");
//			}
////		}else{
////		}
//			
//	}
}
