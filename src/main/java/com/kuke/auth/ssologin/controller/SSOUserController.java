package com.kuke.auth.ssologin.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.ssologin.quartz.user.RetrievalUser;
import com.kuke.auth.ssologin.service.UserSSOService;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.UserOauth;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.engine.ICookie;
import com.kuke.core.redis.RedisUtil;
import com.kuke.util.MessageFormatUtil;

@Controller
@RequestMapping("/kuke/ssouser")
public class SSOUserController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(SSOUserController.class);

	@Autowired
	private UserSSOService userSSOService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private UserCenterService userCenterService;
	/**
	 * <pre>
	 * 创建人: zhangjiran
	 * 创建于: 2015-4-7
	 * 描　述:
	 *    验证用户名和密码 登录
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/authuser")
	public @ResponseBody ResponseMsg authuser(HttpServletRequest request,HttpServletResponse response, ModelMap map) {
		Map<String, String> params = this.getParameterMap(request);
		String user_name = dealNull(String.valueOf(params.get("u")));//用户名/邮箱/手机
		String user_pwd = dealNull(String.valueOf(params.get("p")));//密码
		String auto  = dealNull(String.valueOf(params.get("auto")));//自动登录: 1:自动登录  其他:不自动登录
		String type = dealNull(String.valueOf(params.get("type")));//用户登录的类型:1.iphone 2.email 3.(其他)用户名  (不为12,则为3)
		String from_client = dealNull(String.valueOf(params.get("from_client")));//平台:web,ios,android
		User user = null;
		boolean flag = false;
		String code = "1";
		String msg = "登录失败";
		String codeDesc = "1:登录失败;"
						+ "2:用户名或密码错误;"
						+ "3:登录成功;"
						+ "4:用户状态异常;"
						+ "5:from_client参数异常;";
		boolean f = true;
		try {
			if("web,ios,android".indexOf(from_client) >= 0 && !"".equals(from_client)){
				if("1".equals(type)){
					// 验证 手机  密码
					user = userSSOService.checkUserLoginByPhone(user_name, UserAuthUtils.getMd5Password(user_pwd));
				}else if("2".equals(type)){
					// 验证 邮箱 密码
					user = userSSOService.checkUserLoginByEmail(user_name, UserAuthUtils.getMd5Password(user_pwd));
				}else if("UID".equals(type)){
					// 用户Id登录
					user = userSSOService.checkUserLoginById(dealNull(String.valueOf(params.get("userid"))));
				}else{
					// 验证用户名 密码
					user = userSSOService.checkUserLogin(user_name, UserAuthUtils.getMd5Password(user_pwd));
				}
				if(user == null || user.getUser_status().equals(KuKeAuthConstants.FAILED)){
					f = false;
					code = "2";
					msg = "用户名或密码错误";
				}
			}else{
				f = false;
				code = "5";
				msg = "from_client参数异常";
			}
		} catch (Exception e) {
			logger.debug("SSOUserController authuser 验证用户名密码出错!");
			e.printStackTrace();
		}
		if(f){
			// 验证用户状态
			user = UserAuthUtils.getInstance().UserLogin(user, from_client);
			if (user .getUser_status() != null && user.getUser_status().equals(KuKeAuthConstants.SUCCESS)) {
				
				UserAuthUtils.setUserCookie(response, user,auto);
				flag = true;
				code = "3";
				msg = "登录成功";
			}else{
				flag = false;
				code = "4";
				msg = "用户状态异常";
			}
		}
		JSONObject userJSON = new JSONObject();
		if(user != null){
			userJSON = JSONObject.fromObject(user);
			
			//登录日志cookie
			UserAuthUtils.setLoginCookie(response, user.getUid());
			//记录日志
			LogUtil.addLoginLog(user.getUid(), user.getOrg_id(), request);
			
		}
		userJSON.put("type", type);
		return new ResponseMsg(flag, code, msg, codeDesc, userJSON);
	}
	/**
	 * Cookies登录
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/authcookie")
	public @ResponseBody ResponseMsg authcookie(HttpServletRequest request, ModelMap map) {
		Map<String, String> params = this.getParameterMap(request);
		// 个人票据(相当于cookievalue)
		String ticket = String.valueOf(params.get("ticket"));
		User user = new User();
		user.setUser_status(KuKeAuthConstants.FAILED);
		// 验证票据
		user = UserAuthUtils.getInstance().AuthUserCookie(ticket,"");
		if (KuKeAuthConstants.SUCCESS.equals(user.getUser_status())) {
			// 票据验证通过 准备登录
			user.setSsoid(ticket);
		}
	    return MessageFormatUtil.formatStateToObject(user.getUser_status(), user);
	}
	/**
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/verify")
	public @ResponseBody ResponseMsg userVerify(HttpServletRequest request, ModelMap map) {
		Map<String, String> params = this.getParameterMap(request);
		String user_id = String.valueOf(params.get("user_id"));
		User user = new User();
		try {
			user = RetrievalUser.getUserInfoById(user_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != user) {
			user.setUser_status(KuKeAuthConstants.SUCCESS);
		}
		return MessageFormatUtil.formatStateToObject(user.getUser_status(), user);
	}
	/**
	 * 
	 * @param request
	 */
	@RequestMapping(value = "/userupdateRAM")
	public @ResponseBody ResponseMsg userUpdateRAM(HttpServletRequest request) {
		Map<String, String> params = this.getParameterMap(request);
		String user_id = String.valueOf(params.get("user_id"));
		String type = String.valueOf(params.get("type"));
		String value = String.valueOf(params.get("value"));
		try {
			// 更新到缓存
			String key = "userInfo:" + user_id;
			Map<String, String> userMap = new HashMap<String, String>();
			userMap.put(type, value);
			redisUtil.hashMultipleSet(key, userMap);
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
		}
	}

	/**
	 * <pre>
	 * 创建人: zhangjiran
	 * 创建于: 2015-4-7
	 * 描　述:
	 *     执行退出清理票据
	 * </pre>
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/authout")
	public @ResponseBody ResponseMsg authout(HttpServletRequest request,HttpServletResponse response) {
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		
		//记录日志
		LogUtil.addExitLog(request);
		
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
	 * 移动第三方执行登出的逻辑
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/authoutClient")
	public @ResponseBody ResponseMsg authoutClient(HttpServletRequest request,HttpServletResponse response) {
		Map<String, String> params = this.getParameterMap(request);
		String status = KuKeAuthConstants.FAILED;
		try {
			String ssoid = String.valueOf(params.get("ssoid"));//ticket
			//清理redis
			String result = UserAuthUtils.getInstance().UserOut(ssoid);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status", result);
			//清理cookie
			ICookie.clear(response, KuKeAuthConstants.SSO_USER_COOKIE_NAME);
			status = KuKeAuthConstants.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MessageFormatUtil.formatResultToObject(status);
	}
	/**
	 * 
	 * <pre>
	 * 创建人: zhangjiran
	 * 创建于: 2015-4-7
	 * 描　述:
	 *      机构ID 频道ID SSOID 写入权限内存
	 *      刷新在线时间
	 * </pre>
	 * 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/initonline")
	public @ResponseBody ResponseMsg initonline(HttpServletRequest request) {
		try {
			Map<String, String> params = this.getParameterMap(request);
			// 机构票据
			String ticket = String.valueOf(params.get("ticket"));
			String channel_id = String.valueOf(params.get("c"));
//			 String result = UserAuthUtils.getInstance().UserOnline(ticket,channel_id);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status", KuKeAuthConstants.SUCCESS);
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
		} catch (Exception e) {
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
		}
	}
	
	/**
	 * 检查用户是否已绑定手机
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkUserPhone")
	public @ResponseBody ResponseMsg checkUserPhone(HttpServletRequest request) {
		User user = null;
		try {
			// userid得到user信息
			user = UserOauth.userLoginByToken(request);
			if("".equals(dealNull(user.getUphone()))){
				return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
			}else{
				return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
		}
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
	@RequestMapping(value = "/test")
	public String test(HttpServletRequest request, ModelMap map) {
//		Set<String> set = redisUtil.getKeys("org_ip_*");
//		Set<String> set1 = redisUtil.getKeys("org_down_ip_*");
//		Set<String> set2 = redisUtil.getKeys("org_channel_*");
//		List<String> list = new ArrayList<String>(set);
//		List<String> list1 = new ArrayList<String>(set1);
//		List<String> list2 = new ArrayList<String>(set2);
//		List<Map<String,String>> listMap = redisUtil.batchHashGetAll(list2);
//		System.out.println("机构IP集合:"+list.size());
//		System.out.println("机构下载IP集合:"+list1.size());
//		System.out.println("频道信息集合:"+list2.size());
//		request.setAttribute("set", set);
//		request.setAttribute("set1", set1);
//		request.setAttribute("set2", set2);
		String redisKey = "org_web_014F9370ADD011E0BE9DEDB738314C94_111";
		String redisKey1 = "org_mobel_ios_014F9370ADD011E0BE9DEDB738314C94_222";
		String redisKey2 = "org_mobel_android_014F9370ADD011E0BE9DEDB738314C94_333";
		String redisKey3 = "org_mobel_ios_014F9370ADD011E0BE9DEDB738314C94_444";
		String redisKey4 = "org_web_014F9370ADD011E0BE9DEDB738314C94_555";
		String redisKey5 = "org_web_014F9370ADD011E0BE9DEDB738314C94_666";
		String redisKey6 = "org_web_014F9370ADD011E0BE9DEDB738314C95_777";
//		redisUtil.delKeysLike(redisKey);
//		redisUtil.delKeysLike(redisKey1);
//		redisUtil.delKeysLike(redisKey2);
//		redisUtil.delKeysLike(redisKey3);
//		redisUtil.delKeysLike(redisKey4);
//		redisUtil.delKeysLike(redisKey5);
//		redisUtil.delKeysLike(redisKey6);
		Map<String,String> tempMap = new HashMap<String,String>();
		tempMap.put("1", "2");
//		redisUtil.hashMultipleSet(redisKey,  tempMap);
//		redisUtil.hashMultipleSet(redisKey1, tempMap);
//		redisUtil.hashMultipleSet(redisKey2, tempMap);
//		redisUtil.hashMultipleSet(redisKey3, tempMap);
//		redisUtil.hashMultipleSet(redisKey4, tempMap);
//		redisUtil.hashMultipleSet(redisKey5, tempMap);
//		redisUtil.hashMultipleSet(redisKey6, tempMap);
//		String a = redisUtil.getString(redisKey1);
//		String b = redisUtil.getString(redisKey2);
//		String c = redisUtil.getString(redisKey3);
//		String d = redisUtil.getString(redisKey4);
//		String e = redisUtil.getString(redisKey5);
//		String f = redisUtil.getString(redisKey6);
//		String g = redisUtil.getString(redisKey);
//		Set<String> keySet = new HashSet<String>();
//		keySet = redisUtil.getKeys("*_014F9370ADD011E0BE9DEDB738314C94_*");
//		List<String> keyList = new ArrayList<String>(keySet);
//		List<Map<String,String>> orgLoginMap = redisUtil.batchHashGetAll(keyList);
//		request.setAttribute("orgLoginMap", orgLoginMap);
//		request.setAttribute("keySet", keySet);
//		redisKey = "testExpire";
		
//		if(redisUtil.containsKey(redisKey)){
//			redisUtil.setExpire(redisKey, tempMap,5);
//			System.out.println("aaaaa");
//		}else{
//			System.out.println("bbbb");
//		}
//		request.setAttribute("testMap", testMap);
		Set<String> orgIpSet = new HashSet<String>();
//		orgIpSet = redisUtil.getKeys("org_ip_AD3F95E0C7F611DCA4D2890DC22AF3D3");
		List<String> redisKeys = new ArrayList<String>(orgIpSet);
		//从redis中取出orgIp集合
//		List<Map<String,String>> orgIpListMap = redisUtil.batchHashGetAll(redisKeys);
//		request.setAttribute("orgIpListMap", orgIpListMap);
		return "/testResult";
	}
}
