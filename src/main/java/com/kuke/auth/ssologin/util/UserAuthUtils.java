package com.kuke.auth.ssologin.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.regist.service.impl.RegistService;
import com.kuke.auth.ssologin.quartz.user.RetrievalUser;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.core.engine.ICookie;
import com.kuke.core.redis.RedisUtil;
import com.kuke.core.service.SpringContextHolder;
import com.kuke.util.MD5;
import com.kuke.util.RandomCode;
import com.kuke.util.StringUtil;

public class UserAuthUtils {
	
	static Logger logger = LoggerFactory.getLogger(UserAuthUtils.class);

	private static UserAuthUtils instance = null;
	
	private static RedisUtil redisUtil = SpringContextHolder.getBean("redisUtil");
	
	private static RegistService registService = SpringContextHolder.getBean(RegistService.class);
	
	private UserAuthUtils() {

	}
	public static UserAuthUtils getInstance() {
		if (instance == null) {
			instance = new UserAuthUtils();
		}
		return instance;
	}
	/**
	 * 返回机构User信息
	 */
	public User UserLogin(User user,String from_client) {
		if (null != user){
			System.out.println("UserAuthUtils UserLogin isactive:"+user.getIsactive());
			if ("0".equals(user.getIsactive())) {
				System.out.println("UserAuthUtils UserLogin:账号未激活");
				user.setUser_status(KuKeAuthConstants.ACTIVE);
			}else if("3".equals(user.getIsactive())){
				System.out.println("UserAuthUtils UserLogin:账号冻结加入黑名单");
				//账号冻结加入黑名单
				user.setUser_status(KuKeAuthConstants.FREEZED);
			}else{
				System.out.println("UserAuthUtils UserLogin:账号可以正常使用");
				//账号可以正常使用
				user.setUser_status(KuKeAuthConstants.SUCCESS);
				
				//将个人用户详细信息存入redis
				String userInfoKey = "userInfo:"+user.getUid();
				Map<String,String> userInfoMap = new HashMap<String,String>();
				
				userInfoSet(userInfoMap, user, "setKey");
				
				redisUtil.hashMultipleSet(userInfoKey, userInfoMap);
				
				// 将用户登陆信息存入redis
				String time = String.valueOf(new Date().getTime());
				String code = RandomCode.getRandomString(10);
				
				String redisKey = getRedisKey(from_client, user.getUid());
				
				Map<String, String> userMap = new HashMap<String, String>();
				userMap.put("loginCode", code);
				userMap.put("loginTime", time);
				userMap.put("audio_date", user.getAudio_date());
				userMap.put("video_date", user.getVideo_date());
				userMap.put("live_date", user.getLive_date());
				
				redisUtil.setExpire(redisKey,userMap,7*24*3600);
				System.out.println("loginCode 已写进redis："+code);
				//ssoid
				MD5 md5 = new MD5();
				String secondCookieValue = redisKey + "|" + code + "|" + user.getUid() + "|" + time;
				String firstCookieValue = md5.getMD5ofStr(secondCookieValue);
				String cookieValue = firstCookieValue + "^" + secondCookieValue;
				try {//将cookieValue用DES方式加密
					cookieValue = URLEncoder.encode(cookieValue, "UTF-8");
					user.setSsoid(cookieValue);
				} catch (Exception e) {
					logger.debug("加密出现错误");
				}
			}
		}else{
			user = new User();
			user.setUser_status(KuKeAuthConstants.FAILED);
		}
		return user;
	}
	/**
	 * 校验User是否存在
	 * @param ticket
	 * @return
	 */
	public User AuthUserCookie(String ticket,String md5Str) {
		User user = new User();
		user.setUser_status(KuKeAuthConstants.FAILED);
		// 获取COOKIE中的个人信息
		String cookieValue = ticket;
		System.out.println("ticket==============================="+ticket);
		// redisKey+"|"+code+"|"+user.getUid()+"|"+time
		if (StringUtils.isNotBlank(cookieValue)) {
			//将cookvalue解密
			try {
				cookieValue = URLDecoder.decode(cookieValue, "UTF-8");
			} catch (Exception e) {
			}
			if(cookieValue.split("\\^").length > 1){//cookieValue = firstCookieValue + "^" + secondCookieValue;
				String firstCookieValue = cookieValue.split("\\^")[0];
				String secondCookieValue = cookieValue.split("\\^")[1];
				MD5 md5 = new MD5();
				
				// 将redisKey+"|"+code+"|"+user.getUid()+"|"+time的值进行MD5与前面的值进行比对
				String md5SecondCookieValue = md5.getMD5ofStr(secondCookieValue);
				
				if (firstCookieValue.equals(md5SecondCookieValue)) {
					
					Map<String,String> tempMap = new HashMap<String,String>();
					String redisKey = secondCookieValue.split("\\|")[0];
					String code = secondCookieValue.split("\\|")[1];
					String userId = secondCookieValue.split("\\|")[2];
					String userInfoKey = "userInfo:"+userId;
					//从redis中取出用户详细信息
					if(redisUtil.containsKey(userInfoKey)){
						Map<String,String> userInfoMap = new HashMap<String,String>();
						
						userInfoMap = redisUtil.hashGetAll(userInfoKey);
						
						userInfoSet(userInfoMap, user, "setUser");
						
					}else{//数据库中查询
						user = RetrievalUser.getUserInfoById(userId);
						Map<String,String> userInfoMap = new HashMap<String,String>();
						
						userInfoSet(userInfoMap, user, "setKey");
						
						redisUtil.hashMultipleSet(userInfoKey, userInfoMap);
					}
					Map<String, String> userMap = new HashMap<String,String>();
					/**
					 * 1.如果redis中随机数为空，直接登陆，并重新写入用户信息.
					 * 2.如果redis中随机数与cookie中随机数相等，直接登陆.
					 * 3.redis中随机数不相等，返回重新登陆
					 */
					if (!redisUtil.containsKey(redisKey)) {
						//redis随机数为空
						Date date = new Date();
						tempMap.put("loginCode", code);
						tempMap.put("loginTime", String.valueOf(date.getTime()));
						tempMap.put("audio_date", user.getAudio_date());
						tempMap.put("video_date", user.getVideo_date());
						tempMap.put("live_date", user.getLive_date());
						redisUtil.setExpire(redisKey,tempMap,7*24*3600);
						if(StringUtils.isNotBlank(user.getUid())){
							user.setUser_status(KuKeAuthConstants.SUCCESS);	
						}
						return user;
					} else {//有
						// 从缓存中取出随机码
						userMap = redisUtil.hashGetAll(redisKey);
						String randomCode = userMap.get("loginCode");//null
						System.out.println("code================================="+code);
						System.out.println("randomCode==============================="+randomCode);
						if(randomCode == null && !"".equals(md5Str)){//移动端过来的,如果ssoid已过期
							System.out.println("移动端过来的   md5Str:"+md5Str);
							if(md5Str.equals(getMd5Str(userId))){
								System.out.println("移动端过来的   md5Str验证成功!");
								//重新生成ssoid，并入redis
								user = UserAuthUtils.getInstance().UserLogin(userId,getFrom_Client(redisKey));
							}else{
								System.out.println("移动端过来的   md5Str验证失败!");
							}
						}else{
							if (code.equals(randomCode)) {//验证码没过期
								// 随机码相同，为同一用户登陆
								if(StringUtils.isNotBlank(user.getUid())){
									user.setUser_status(KuKeAuthConstants.SUCCESS);	
									tempMap.put("loginCode", randomCode);
									tempMap.put("loginTime", String.valueOf(new Date().getTime()));
//									tempMap.put("audio_date", user.getAudio_date());
//									tempMap.put("video_date", user.getVideo_date());
//									tempMap.put("live_date", user.getLive_date());
									redisUtil.setExpire(redisKey, tempMap,7*24*3600);
									
									//赋值日期等
									user.setAudio_date(userMap.get("audio_date"));
									user.setVideo_date(userMap.get("video_date"));
									user.setLive_date(userMap.get("live_date"));
								}
								if(!"".equals(md5Str)){//移动端过来的,如果ssoid已过期
									System.out.println("没过期 移动端过来的   md5Str:"+md5Str);
									if(md5Str.equals(getMd5Str(userId))){
										System.out.println("没过期 移动端过来的   md5Str验证成功!");
										//重新给移动端赋值ssoid
										user.setSsoid(ticket);
									}else{
										user.setUser_status(KuKeAuthConstants.FAILED);
										System.out.println("没过期 移动端过来的   md5Str验证失败!");
									}
								}
							}
						}
						return user;
					}
				}
				return user;
			}else{
				return user;
			}
		} else {
			return user;
		}
	}
	/**
	 * 移动组专用,传uid和from_client参数
	 * @param uid : 
	 * @param from_client : ios android web 
	 * @return
	 */
	public User AuthUserUid(String uid,String from_client) {
		User user = new User();
		user.setUser_status(KuKeAuthConstants.FAILED);
		if (StringUtils.isNotBlank(uid)) {
					String userInfoKey = "userInfo:"+uid;
					//从redis中取出用户详细信息
					if(redisUtil.containsKey(userInfoKey)){
						Map<String,String> userInfoMap = new HashMap<String,String>();
						userInfoMap = redisUtil.hashGetAll(userInfoKey);
						
						userInfoSet(userInfoMap, user, "setUser");
						
					}else{
						user = RetrievalUser.getUserInfoById(uid);
						Map<String,String> userInfoMap = new HashMap<String,String>();
						
						userInfoSet(userInfoMap, user, "setKey");
						
						redisUtil.hashMultipleSet(userInfoKey, userInfoMap);
					}
					Map<String, String> userMap = new HashMap<String,String>();
					/**
					 * 1.如果redis中随机数为空，直接登陆，并重新写入用户信息.
					 * 2.如果redis中随机数与cookie中随机数相等，直接登陆.
					 * 3.redis中随机数不相等，返回重新登陆
					 */
					String redisKey = getRedisKey(from_client, user.getUid());
					if (!redisUtil.containsKey(redisKey)) {
						Map<String, String> tempMap = new HashMap<String, String>();
						//redis随机数为空
						Date date = new Date();
						tempMap.put("loginTime", String.valueOf(date.getTime()));
						tempMap.put("audio_date", user.getAudio_date());
//						tempMap.put("org_end_date", user.getOrg_end_date());
						tempMap.put("video_date", user.getVideo_date());
						tempMap.put("live_date", user.getLive_date());
						redisUtil.setExpire(redisKey,tempMap, 7*24*3600);
						if(StringUtils.isNotBlank(user.getUid())){
							user.setUser_status(KuKeAuthConstants.SUCCESS);	
						}
						return user;
					}else{
						if(StringUtils.isNotBlank(user.getUid())){
							user.setUser_status(KuKeAuthConstants.SUCCESS);	
						}
					} 
				return user;
		} else {
			return user;
		}
	}
	/**
	 * 用户登出,删除redis信息
	 */
	public String UserOut(String ticket) {
		String result = KuKeAuthConstants.FAILED;
		
		try {
			// 解密cookevalue 获取COOKIE中的个人信息
			if(StringUtils.isNotBlank(ticket)){
				String cookieValue = ticket;
				String secondCookieValue = cookieValue.split("\\^")[1];
				String redisKey = secondCookieValue.split("\\|")[0];
				if(redisUtil.containsKey(redisKey)){
					//删除redis中用户信息
					redisUtil.delKeysLike(redisKey);
					result = KuKeAuthConstants.SUCCESS;			
				}
			}
		} catch (Exception e) {
			return result;
		}
		return result;
	}
	/**
	 * 通过userId和from_client得到User信息
	 * @param userId
	 * @param from_client
	 * @return
	 */
	public User UserLogin(String userId,String from_client) {
		User user = new User();	
		user = RetrievalUser.getUserInfoById(userId);
		return  UserLogin(user,from_client);
	}
	/**
	 * 拼接redisKey
	 * @param from_client
	 * @param Uid
	 * @return
	 */
	private String getRedisKey(String from_client,String Uid){
		String redisKey = "";
		if ("web".equals(from_client)) {
			redisKey = "user_web_" + Uid;
		} else if ("ios".equals(from_client)) {
			redisKey = "user_mobile_ios_" + Uid;
		} else if ("android".equals(from_client)) {
			redisKey = "user_mobile_android_" + Uid;
		}else{
			redisKey = "user_web_" + Uid;
		}
		return redisKey;
	}
	/**
	 * 
	 * @return
	 */
	public String getUserInfoKey(String Uid){
		return "userInfo:"+Uid;
	}
	/**
	 * 拼接redisKey
	 * @param from_client
	 * @param Uid
	 * @return
	 */
	private String getFrom_Client(String redisKey){
		String from_client = "";
		if(redisKey.startsWith("user_web_")){
			from_client = "web";
		}else if(redisKey.startsWith("user_mobile_ios_")){
			from_client = "ios";
		}else if(redisKey.startsWith("user_mobile_android_")){
			from_client = "android";
		}else{
			from_client = "web";
		}
		return redisKey;
	}
	/**
	 * 得到验证移动端Md5Str
	 * @param uid
	 * @return
	 */
	public static String getMd5Str(String uid){
		String md5Key = "KukeGetUser!@#$";
		MD5 md5 = new MD5();
		return md5.getMD5ofStr(uid+md5Key);
	}
	/**
	 * 得打MD5加密的密码
	 * @param uid
	 * @return
	 */
	public static String getMd5Password(String pwd){
		if(!"".equals(pwd)){
			MD5 md5 = new MD5();
			return md5.getMD5ofStr(pwd);
		}else{
			return pwd;
		}
	}
	/**
	 * 设置用户COOKIE
	 * @param user
	 * @return
	 */
	public static boolean setUserCookie(HttpServletResponse response,User user,String autoFlag){
		boolean flag = false;
		try {
			if(user != null && user.getUser_status() != null && user.getUser_status().equals(KuKeAuthConstants.SUCCESS)) {
				if("1".equals(autoFlag)){//7 天
					ICookie.set(response, KuKeAuthConstants.SSO_USER_COOKIE_NAME,user.getSsoid(),7*24*3600, "/", ".kuke.com");
					ICookie.set(response, KuKeAuthConstants.autoflag,"1login",7*24*3600, "/", ".kuke.com");
				}else{// 1 天
					ICookie.set(response, KuKeAuthConstants.SSO_USER_COOKIE_NAME,user.getSsoid(),1*24*3600, "/", ".kuke.com");
					ICookie.set(response, KuKeAuthConstants.autoflag,"0login",1*24*3600, "/", ".kuke.com");
				}
				
				//日志cookie
				setLoginCookie(response, user.getUid());
				
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 设置用户日志COOKIE
	 * @param user
	 * @return
	 */
	public static boolean setLoginCookie(HttpServletResponse response,String value){
		boolean flag = false;
		try {
			if(!"".equals(StringUtil.dealNull(value))){
				ICookie.set(response, KuKeAuthConstants.LOGIN_COOKIE_NAME,value,1200, "/", ".kuke.com");//20分钟
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 设置用户信息
	 * @param userInfoMap
	 * @param user
	 * @param type
	 */
	private void userInfoSet(Map<String,String> userInfoMap,User user,String type){
		
		if(type != null && "setUser".equals(type)){
			if(user != null){
				user.setUid(userInfoMap.get("user_id"));
				user.setUnickname(userInfoMap.get("nickname"));
				user.setUphoto(userInfoMap.get("photo"));
				user.setUsex(userInfoMap.get("sex"));
				user.setUphone(userInfoMap.get("phone"));
				user.setUemail(userInfoMap.get("email"));
				user.setReg_date(userInfoMap.get("reg_date"));
				user.setReg_date(userInfoMap.get("end_date"));
				user.setOrg_id(userInfoMap.get("org_id"));
				user.setOrg_name(userInfoMap.get("org_name"));
				user.setIsactive(userInfoMap.get("active"));
				user.setIs_verify(userInfoMap.get("is_verify"));
				user.setIsPop(userInfoMap.get("isPop"));
			}
		}else if(type != null && "setKey".equals(type)){
			if(userInfoMap != null){
				userInfoMap.put("user_id", user.getUid());
				userInfoMap.put("nickname", user.getUnickname());
				userInfoMap.put("photo", user.getUphoto());
				userInfoMap.put("sex", user.getUsex());
				userInfoMap.put("phone", user.getUphone());
				userInfoMap.put("email", user.getUemail());
				userInfoMap.put("reg_date", user.getReg_date());
				userInfoMap.put("end_date", user.getEnd_date());
				userInfoMap.put("org_id", user.getOrg_id());
				userInfoMap.put("org_name", user.getOrg_name());
				userInfoMap.put("active", user.getIsactive());
				userInfoMap.put("is_verify",user.getIs_verify());
				userInfoMap.put("isPop", user.getIsPop());
			}
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
}
