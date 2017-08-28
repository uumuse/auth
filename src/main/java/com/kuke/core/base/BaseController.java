package com.kuke.core.base;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.ssologin.service.UserSSOService;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.auth.util.UserOauth;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.engine.ICookie;
import com.kuke.core.redis.RedisUtil;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.ImageUrlUtil;
import com.kuke.util.PageInfo;
import com.kuke.util.WxUtil;

@ControllerAdvice
public class BaseController extends AbstractJsonpResponseBodyAdvice {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserSSOService userSSOService;
	
	@Autowired
	private RedisUtil redisUtil;

	public static String AUTH_URL = String.valueOf(PropertiesHolder
			.getContextProperty("auth.url"));// 请求HTTP地址

	public BaseController() {
		super("jsoncallback");
	}

	protected MediaType getContentType(MediaType contentType,
			ServerHttpRequest request, ServerHttpResponse response) {
		return new MediaType("application", "json", Charset.forName("utf-8"));
	}

	@SuppressWarnings("unchecked")
	public static Map getParameterMap(HttpServletRequest request) {
		// 参数Map
		Map properties = request.getParameterMap();
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

	@SuppressWarnings("unchecked")
	public static void getCondition(HttpServletRequest request) {
		// 参数Map
		Map properties = request.getParameterMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			request.setAttribute(name, value);
		}
	}
	/**
	 * session中记录的用户
	 * 
	 * @return
	 */
	public User getLoginUser() {
		String ssoid = dealNull(request.getParameter("ssoid"));//移动端过来时,必加的参数
		System.out.println("BaseController ssoid:"+ssoid);
		User user = null;
		if("".equals(ssoid)){//web端
			ssoid = ICookie.get(request, KuKeAuthConstants.SSO_USER_COOKIE_NAME);// KuKeDesktopSSOID  (web端得到)
			try {
				user = UserOauth.userLoginByToken(ssoid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{//移动端
			try {
				user = UserOauth.userLoginByToken(ssoid);
				System.out.println("basecontrollerssoid user:"+user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(user != null && KuKeAuthConstants.FAILED.equals(user.getUser_status())){//
			user = null;
		}
		return user;
	}
	/**
	 * session中记录的用户
	 * 
	 * @return
	 */
	public User getLoginUser(HttpServletRequest requestParam) {
		String ssoid = dealNull(requestParam.getParameter("ssoid"));//移动端过来时,必加的参数
		System.out.println("BaseController requestParam ssoid:"+ssoid);
		User user = null;
		if("".equals(ssoid)){//web端
			ssoid = ICookie.get(request, KuKeAuthConstants.SSO_USER_COOKIE_NAME);// KuKeDesktopSSOID  (web端得到)
			try {
				user = UserOauth.userLoginByToken(ssoid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{//移动端
			try {
				user = UserOauth.userLoginByToken(ssoid);
				System.out.println("basecontrollerssoid user:"+user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(user != null && KuKeAuthConstants.FAILED.equals(user.getUser_status())){//
			user = null;
		}
		return user;
	}
	public User getLoginUser(String ssoid) {
		if(ssoid==null||ssoid.equals("null")||ssoid.equals("")){
			ssoid = ICookie.get(request, KuKeAuthConstants.SSO_USER_COOKIE_NAME);// KuKeDesktopSSOID  (web端得到)
		}
		
		User user = null;
		try {
			user = userLoginByToken(ssoid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(user != null && KuKeAuthConstants.FAILED.equals(user.getUser_status())){//
			user = null;
		}
		return user;
	}
		
	public User userLoginByToken(String ticket) throws Exception {
		String post_url = KuKeUrlConstants.userLoginByToken_URL; //   /kuke/ssouser/authcookie
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("ticket", ticket));
		System.out.println("ticket:"+ticket);
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		ResponseMsg msg = new ResponseMsg(result);
		System.out.println("userLoginByToken result:"+result);
		return  new User(msg.getData().toString());
	}	
	/**
	 * 个人中心头部用户信息
	 * 
	 * @param request
	 * @param response
	 */
	public void getUserInfo(HttpServletRequest request,HttpServletResponse response) {
		User user = this.getLoginUser();
		if(user != null){
			String uid = user.getUid();
			// 实时查询
			user = userSSOService.getUserByID(uid);
			// 用户名
			String username = getUserName(user);// 1 nickname 2手机 3邮箱
			// 头像
			String uphoto = ImageUrlUtil.getUserImg(user.getUphoto());
			// 机构名称
			// 有效期:机构个人哪个长显示哪个,机构三个date一般相同,所以最后显示 Audio_date
			String date = dealNull(user.getAudio_date());
			String validdate = "";
			if (!"".equals(date)) {
				validdate = date.split("-")[0] + "年" + date.split("-")[1] + "月"
						+ date.split("-")[2] + "日";
			}
			
			//更新到缓存org_id
			if(!dealNull(user.getOrg_id()).equals(dealNull(this.getLoginUser().getOrg_id()))){
				try{
					String key = UserAuthUtils.getInstance().getUserInfoKey(user.getUid());
					Map<String, String> userMap = new HashMap<String, String>();
					userMap.put("org_id", dealNull(user.getOrg_id()));
					redisUtil.hashMultipleSet(key, userMap);	
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			request.setAttribute("org_id", dealNull(user.getOrg_id()));
			request.setAttribute("orgphoto", ImageUrlUtil.getOrgImg(user.getOrg_id()));
			request.setAttribute("username", username);
			request.setAttribute("uid", uid);
			request.setAttribute("uphoto", uphoto);
			request.setAttribute("orgname", dealNull(user.getOrg_name()));
			request.setAttribute("validdate", validdate);
			request.setAttribute("syscount", user.getCountMsg());
		}
		//头部
		request.setAttribute("wwwurl", KuKeUrlConstants.wwwUrl);
	}

	protected void weixinShare(HttpServletRequest request) {
		String url = String.valueOf(request.getRequestURL());
		String query = request.getQueryString();
		if (query != null) {
			url += "?" + query;
		}
		Long timeStamp = System.currentTimeMillis() / 1000;
		Map<String, Object> weixin = WxUtil.getSignature(
				WxUtil.getJsapiTicket(), WxUtil.getRandomStrNum(13),
				String.valueOf(timeStamp), url);
		request.setAttribute("wx", JSONObject.fromObject(weixin));
	}

	protected Map<String, String> getParamsMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			String value = request.getParameter(key);
			params.put(key, value);
		}
		return params;
	}

	/**
	 * 得到用户的昵称
	 * 
	 * @param user
	 * @return
	 */
	public String getUserName(User user) {
		String username = "";
		if (!"".equals(dealNull(user.getUnickname()))) {// 昵称
			username = dealNull(user.getUnickname());
		} else if (!"".equals(dealNull(user.getUphone()))) {// 手机
			username = dealNull(user.getUphone());
		} else if (!"".equals(dealNull(user.getUemail()))) {// 邮箱
			username = dealNull(user.getUemail());
		}
		if(username.length() >= 20){//限制昵称的显示大小
			username = username.substring(0, 17)+"...";
		}
		return username;
	}

	/**
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
	}

	/**
	 * 分页查询
	 * 
	 * @param serviceObj
	 * @param serviceMethodName
	 * @param paramMap
	 * @return
	 */
	public static PageInfo getServicePageInfo(Object serviceObj,
			String serviceMethodName, Map<String, String> paramMap) {
		PageInfo pageInfo = new PageInfo();
		// 初始化pageSize : 20
		paramMap.put("pageSize",
				"".equals(dealNull(paramMap.get("pageSize"))) ? "20"
						: dealNull(paramMap.get("pageSize")));
		// 初始化currentPage : 1
		paramMap.put("currentPage",
				"".equals(dealNull(paramMap.get("currentPage"))) ? "1"
						: dealNull(paramMap.get("currentPage")));

		if (null == paramMap.get("currentPage")
				|| "".equals(paramMap.get("currentPage"))) {
			paramMap.put("currentPage", "1");
		}
		try {
			/**
			 * args1 服务对象实例 args2 执行服务对象中的方法名称 args3 执行服务对象中的方法的参数Map
			 */
			pageInfo = new PageInfo(Integer.parseInt(paramMap
					.get("currentPage")), Integer.parseInt(paramMap
					.get("pageSize")), serviceObj, serviceMethodName, paramMap);
		} catch (Exception e) {
			e.printStackTrace();
			return pageInfo;
		}
		return pageInfo;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	private static String dealNull(String str) {
		if (str == null || "".equals(str.trim()) || "null".equals(str.trim())) {
			str = "";
		}
		return str.trim();
	}
}
