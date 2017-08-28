package com.kuke.auth.ssologin.service;

import com.kuke.auth.login.bean.User;

public interface UserSSOService {
	/**
	 * 手机 密码登录
	 * @param loginName
	 * @param loginPwd
	 * @return
	 */
	public User checkUserLoginByPhone(String loginName, String loginPwd);
	/**
	 * 用邮箱 密码登录
	 * @param loginName
	 * @param loginPwd
	 * @return
	 */
	public User checkUserLoginByEmail(String loginName, String loginPwd);
	/**
	 * 用户名 密码登录
	 * @param loginName
	 * @param loginPwd
	 * @return
	 */
	public User checkUserLogin(String loginName, String loginPwd);
	/**
	 * UID登录
	 * @param userId
	 * @return
	 */
	public User checkUserLoginById(String userId);
	/**
	 * 包括未读消息数量
	 * @param uid
	 * @return
	 */
	public User getUserByID(String uid);
}
