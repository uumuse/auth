package com.kuke.auth.login.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.regist.mapper.UserMapper;
import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.auth.ssologin.quartz.org.RetrievalOrg;
import com.kuke.auth.ssologin.service.UserSSOServiceImpl;
import com.kuke.auth.ssologin.util.OrgAuthUtils;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.userCenter.service.UserCenterServiceImpl;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.OrgOauth;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.auth.util.UserOauth;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.engine.ICookie;
import com.kuke.core.redis.RedisUtil;
import com.kuke.core.service.SpringContextHolder;
import com.kuke.util.IdGenerator;
import com.kuke.util.ImageUrlUtil;
import com.kuke.util.MessageFormatUtil;
import com.kuke.util.StringUtil;
import com.kuke.util.WxJSUtil;
import com.kuke.util.WxUtil;

/**
 * 
 * user Loging url :
 * http://auth-m.kuke.com/kuke/ssouser/authuser?u=maxingg&p=111111&c= result :{
 * "isactive":"1", 是否激活 1激活 0 没激活
 * "org_id":"AD3F95E0C7F611DCA4D2890DC22AF3D3",机构id
 * "reg_date":"2013-02-19 12:07:47.0",注册时间 "ssoid":
 * "t1361849397329i19DD38708F6611E1AE46BE05F097A129C09D0A807FBD11E2B099A021C6E33735"
 * ,票据ID "uid":"19DD38708F6611E1AE46BE05F097A129",用户ID "uname":"maxingg",登录名称
 * "unickname":"糖醋肉丸子V",昵称 "uphoto":"20121218123810.jpg",头像地址
 * "user_status":"SUCCESS",用户登录状态 SUCCESS成功 REPLACE重复登录 FAILD登录失败 ACTIVE尚未激活
 * "utype":"99"}用户类型
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(value = "/kuke/login")
public class LoginController extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private UserSSOServiceImpl userSSOServiceImpl;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RedisUtil redisUtil;
	
	/**
	 * 进入iframe登录页面
	 */
	@RequestMapping(value = "/go")
	public String redirectLoginIframe(HttpServletRequest request,HttpServletResponse response) throws Exception {
		return "/login/login";
	}
	/**
	 * 进入登录页面
	 */
	@RequestMapping(value = "/index")
	public String redirectLoginIndex(HttpServletRequest request,HttpServletResponse response) throws Exception {
		return KuKeUrlConstants.userLogin_url;
	}
	/**
	 * 进入登录页面
	 */
	@RequestMapping(value = "/outBreforeIndex")
	public String outBreforeIndex(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//得到票据
		String token = ICookie.get(request,KuKeAuthConstants.SSO_USER_COOKIE_NAME);
		//清理redis
		UserAuthUtils.getInstance().UserOut(token);
		//清理cookie
		ICookie.clear(response, KuKeAuthConstants.SSO_USER_COOKIE_NAME);
		return KuKeUrlConstants.userLogin_url;
	}
	/**
	 * 加载用户信息,机构信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/load")
	public @ResponseBody ResponseMsg loadUserInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = this.getParameterMap(request);
		boolean flag = false;
		String msg = "FAILED";
		JSONObject data = new JSONObject();
		data.put("userNickName", "");
		data.put("userPhoto", "");
		data.put("userStatus", KuKeAuthConstants.FAILED);
		data.put("orgPhoto", "");
		//用户信息
		try {
			//是否自动登录:
			String autoflag = dealNull(ICookie.get(request, KuKeAuthConstants.autoflag));// autoflag
			System.out.println("autoflag:"+autoflag);
			if("1".equals(autoflag) || autoflag.indexOf("login") >= 0 || "0".equals(autoflag)){
				User user = UserOauth.userLoginByToken(request);
				if (KuKeAuthConstants.SUCCESS.equals(user.getUser_status())) {
					//未读消息数量
					UserCenterServiceImpl sysService = SpringContextHolder.getBean(UserCenterServiceImpl.class);
					int count = sysService.getNoReadSysMessage(user.getUid());
					
					flag = true;
					msg = "SUCCESS";
					data.put("userNickName", getUserName(user));
					data.put("userPhoto", ImageUrlUtil.getUserImg(user.getUphoto()));
					data.put("userId", user.getUid());
					data.put("orgid", user.getOrg_id());
					data.put("userStatus", KuKeAuthConstants.SUCCESS);
					data.put("sysCount", count);
					//设置自动登录的时间
				}
				//绑定手机提醒:登录时,并且手机字段为空
				if("".equals(dealNull(user.getUphone())) && "0".equals(user.getIsPop())){
					data.put("phoneFlag", "1");
					userMapper.updateUserTip(user.getUid());
					redisUtil.hashSet("userInfo:"+user.getUid(),"isPop", "1");
				}
			}
			if("0login".equals(autoflag)){//自登陆开始  分自动登录的账号有效期为一天 || "0".equals(autoflag)
				ICookie.set(response, KuKeAuthConstants.autoflag,"0",1*24*3600, "/", ".kuke.com");
				data.put("autoflag", "0");
			}else if("1login".equals(autoflag) || "1".equals(autoflag)){
				//自动登录
				ICookie.set(response, KuKeAuthConstants.autoflag,"1",7*24*3600, "/", ".kuke.com");
				data.put("autoflag", "1");
			}else if("0".equals(autoflag)){
				//
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//机构信息：先按照IP登录，如果IP登录成功，则与个人机构没关系；如果IP登录失败，则显示个人机构图标；
		try {
			Organization org = OrgOauth.orgLogin(request, response);
			if (KuKeAuthConstants.SUCCESS.equals(org.getOrg_status())) {
				String photoUrl = String.valueOf(PropertiesHolder.getContextProperty("imgUrl"));
				photoUrl += String.valueOf(PropertiesHolder.getContextProperty("org.logo.url"));
				photoUrl += org.getOrg_id() + ".gif";
				flag = true;
				msg = "SUCCESS";
				data.put("orgPhoto", photoUrl);
				data.put("orgID",org.getOrg_id());
			}
			System.out.println("机构自动登录  result："+org.getOrg_status());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//如果IP登录失败；则按照个人的来
		String orgID = dealNull((String)data.get("orgID"));//机构
		String orgid = dealNull((String)data.get("orgid"));//个人
		if("".equals(orgID) && !"".equals(orgid)){//机构ID
			data.put("orgPhoto", ImageUrlUtil.getOrgImg(orgid));
			data.put("orgID",orgid);
			//查询机构
			Organization organization = RetrievalOrg.getOrgInfoById(orgid);
			//写入redis
			String result = OrgAuthUtils.getInstance().OrgLogin(organization,"web");
			System.out.println("ip登录失败，用户机构登录  result："+result);
		}
		
		return new ResponseMsg(flag, msg, data);
	}
	/**
	 * 校验机构信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loadOrg")
	public @ResponseBody ResponseMsg loadOrg(HttpServletRequest request,HttpServletResponse response) throws Exception {
		boolean flag = false;
		String code= "";
		String msg = "FAILED";
		JSONObject data = new JSONObject();
		try {
			Organization org = OrgOauth.orgLogin(request, response);
			if (KuKeAuthConstants.SUCCESS.equals(org.getOrg_status())) {
				String photoUrl = String.valueOf(PropertiesHolder.getContextProperty("imgUrl"));
				photoUrl += String.valueOf(PropertiesHolder.getContextProperty("org.logo.url"));
				photoUrl += org.getOrg_id() + ".gif";
				flag = true;
				msg = "SUCCESS";
				data.put("orgPhoto", photoUrl);
				data.put("orgID",org.getOrg_id());
			}
		} catch (Exception e) {
			msg = e.getMessage();
			e.printStackTrace();
		}
		return new ResponseMsg(flag, code, msg,"",data);
	}
	/**
	 * 用户用于ssoid失效时登录(移动端)
	 */
	@RequestMapping(value = "/getUserOfMD5")
	public @ResponseBody ResponseMsg getUserOfMD5(HttpServletRequest request,HttpServletResponse response)throws Exception {
		try {
			String ssoid = dealNull(request.getParameter("ssoid"));//移动端过来时,必加的参数
			String md5Str = dealNull(request.getParameter("md5Str"));//移动端过来时,必加的参数
			if("".equals(ssoid)){
				return new ResponseMsg(false, "1", "ssoid不能为空", "");
			}
			if("".equals(md5Str)){
				return new ResponseMsg(false, "2", "md5Str不能为空", "");
			}
			System.out.println(" getUserOfMD5  ssoid:"+ssoid+" md5Str:"+md5Str);
			User user = UserAuthUtils.getInstance().AuthUserCookie(ssoid,md5Str);
			ssoid = user.getSsoid();
			if(KuKeAuthConstants.SUCCESS.equals(user.getUser_status())){//如果登陆并且验证成功的话
				user = userSSOServiceImpl.checkUserLoginById(user.getUid());
				user.setSsoid(ssoid);
				return new ResponseMsg(true, "3", "登录成功", "", user);
			}else{
				return new ResponseMsg(false, "5", "登陆验证失败", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseMsg(false, "4", "登录失败", "");
	}
	
	/**
	 * 用户退出(网页端)
	 */
	@RequestMapping(value = "/out")
	public @ResponseBody ResponseMsg redirectOut(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
//		String user_id = "";
//		try {
//			User user = UserOauth.userLoginByToken(request);
//			user_id = user.getUid();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		try {
			//得到票据
			String token = ICookie.get(request,KuKeAuthConstants.SSO_USER_COOKIE_NAME);
			//清理redis
			UserAuthUtils.getInstance().UserOut(token);
			//清理cookie
			ICookie.clear(response, KuKeAuthConstants.SSO_USER_COOKIE_NAME);
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
		}
	}
	/**
	 * 进入重新发邮件页面
	 */
	@RequestMapping(value = "/reSendActiveEmail")
	public String reSendActiveEmail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		return "/login/reSendActiveEmail";
	}
	/**
	 * 得到微信JSSDKconfig值
	 */
	@RequestMapping(value = "/getWXToken")
	public @ResponseBody Map<String, Object> getWXToken(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String url = String.valueOf(request.getRequestURL());
		String query = request.getQueryString();
		if(query!=null){
			url += "?"+query;
		}
		Long timeStamp = System.currentTimeMillis()/1000;
		Map<String, Object> weixin = WxJSUtil.getSignature(WxUtil.getJsapiTicket(), WxUtil.getRandomStrNum(13), timeStamp.toString(), url);
		return weixin;
	}
	/**
	 * 
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
