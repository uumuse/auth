package com.kuke.auth.third;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kuke.auth.regist.domain.User;
import com.kuke.auth.regist.service.impl.RegistService;
import com.kuke.auth.ssologin.service.UserSSOServiceImpl;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.util.DateUtil;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.IdGenerator;
//@Component
//@ContextConfiguration(locations={"classpath*:/config/spring/spring*.xml"})
//@RunWith(SpringJUnit4ClassRunner.class)
/***
 * 石景山区图书馆资源平台
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/kuke/st")
public class StResourcePlatformController {
	
	private final String AppID = "B25162FB019E4B6B8C9843B0C8998D21";
	private final String AppKey = "C14FF12C2A0111EAC4E33A206B79ADA9";
//	private final String testuser = "001000074485";
//	private final String testpwd = "0";

	
	@Autowired
	private RegistService registService;
	
	@Autowired
	private UserSSOServiceImpl ssoService;
	
	
	@RequestMapping("/resourcePlatform")
	public String UserValid(HttpServletRequest request,HttpServletResponse response){
		String baseURL = "http://webapi.sjstsg.net";
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String token = "";
		Map<String,String> map = new HashMap<String,String>();
		map.put("AppID", AppID);
		map.put("AppKey", AppKey);
		String result = HttpClientUtil.executePost(baseURL+"/v1/Common/GetToken", map);
		System.out.println("GetToken====================>"+result);
		JSONObject json = JSONObject.fromObject(result);
		if(String.valueOf(json.get("Code")).equals("1")){
			System.out.println(json.get("Content"));
			token = String.valueOf(json.get("Content"));
			
			String url = baseURL+"/v1/User/UserValid?token="+token;
			Map<String,String> ma = new HashMap<String,String>();
			
//			ma.put("UserName", testuser);
//			ma.put("Password", testpwd);
			ma.put("UserName", userName);
			ma.put("Password", password);
			String re = HttpClientUtil.executePost(url, ma);
			System.out.println("验证用户UserValid==================>"+re);
			JSONObject js = JSONObject.fromObject(re);
			if(String.valueOf(js.get("Code")).equals("1")){
				System.out.println(js.get("Content"));
//				return "redirect:http://auth.kuke.com/kuke/orgUser?orgName=sjstsg&orgPwd=sjstsg";
				
				com.kuke.auth.login.bean.User u = ssoService.checkUserLoginByPhone(userName,password);

				if(u == null || u.getUser_status().equals(KuKeAuthConstants.FAILED)){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					User user = new User();
					String user_id = IdGenerator.getUUIDHex32();
					user.setId(user_id);
					user.setPhone(userName);
					user.setPassword(UserAuthUtils.getMd5Password(password));
					user.setIsactive("1");
					user.setReg_date(sdf.format(new Date()));
					user.setEnd_date(sdf.format(DateUtil.getafter7Day(new Date())));
					user.setOrg_id("4FBD08309A9211E688308AF1F9E0547B");
					registService.insertUser(user);
					registService.insertUserBasePertain(user_id);
					registService.insertUserAuth(user.getId(),user.getId(),sdf.format(DateUtil.getafter7Day(new Date())));
				}
				
//				String post_url ="http://192.168.0.107/kuke/ssouser/authuser";
				try {
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					nvps.add(new BasicNameValuePair("u", userName));
					nvps.add(new BasicNameValuePair("p", password));
					nvps.add(new BasicNameValuePair("from_client", "web"));
					nvps.add(new BasicNameValuePair("type", "1"));
					String post_url = KuKeUrlConstants.userLoginByUP_URL;
					String res = HttpClientUtil.executeServicePOST(post_url, nvps);
					System.out.println("regist result:"+res);
					if(res==null||res.equals("")){
						return "redirect:"+KuKeUrlConstants.wwwUrl;
					}
					ResponseMsg msgObject = new ResponseMsg(res);
					com.kuke.auth.login.bean.User newuser = new com.kuke.auth.login.bean.User();
					newuser.setUser_status(KuKeAuthConstants.FAILED);
					try {
						newuser = new com.kuke.auth.login.bean.User(msgObject.getData().toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("regist newuser:"+newuser.toString());
					UserAuthUtils.getInstance().setUserCookie(response,newuser,"1");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "redirect:"+KuKeUrlConstants.wwwUrl;
	}
}
