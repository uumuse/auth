package com.kuke.auth.ssologin.quartz.user;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.ssologin.service.UserSSOService;
import com.kuke.auth.ssologin.service.UserSSOServiceImpl;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.core.service.SpringContextHolder;




public class RetrievalUser {
	

	/**
	 * 票据登录获取用户ID
	 * @return
	 */
	public static String getUserIdBySSOID(String sso_id) {
		String userId = KuKeAuthConstants.FAILED;
		if (null == ApplicationUser.userLoginMap)
			return userId;
		try{
			String tempUserId = sso_id.substring(sso_id.indexOf("i") + 1, sso_id.length() - 32);
			String temp_result = ApplicationUser.userLoginMap.get(tempUserId);
			if (temp_result != null && temp_result.equals(sso_id)) {
				//有效用户
				userId = tempUserId;
			} else if (temp_result != null && !temp_result.equals(sso_id)) {
				//失效用户
				userId = KuKeAuthConstants.REPLACE;
			} else {
				//没有登录
				userId = KuKeAuthConstants.FAILED;
			}
		}catch (Exception e) {
		}
		return userId;
	}
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2012-3-8
	 * 描　述:
	 *    通过id得到个人信息
	 * </pre>
	 * @param orgId
	 * @return
	 */
	public static User getUserInfoById(String userId) {

		User user = new User();
		user.setUser_status(KuKeAuthConstants.FAILED);
		if (!"".equals(userId)) {
			UserSSOService userSSOService = SpringContextHolder.getBean(UserSSOServiceImpl.class);
			user = userSSOService.checkUserLoginById(userId);
		}
		return user;
	}
	
	
	
	
	
	
	
	
	
}