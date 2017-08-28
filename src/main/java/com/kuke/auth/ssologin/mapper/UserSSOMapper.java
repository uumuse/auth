package com.kuke.auth.ssologin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kuke.auth.login.bean.User;

public interface UserSSOMapper {

	/**
	 * 用户名密码登录
	 * @param loginName
	 * @param loginPwd
	 * @return
	 */
	public User checkUserLogin(@Param("loginName") String loginName,@Param("loginPwd") String loginPwd);
	/**
	 * 手机密码登录
	 * @param loginName
	 * @param loginPwd
	 * @return
	 */
	public User checkUserLoginByPhone(@Param("loginName") String loginName,@Param("loginPwd") String loginPwd);
	/**
	 * 邮箱密码登录
	 * @param loginName
	 * @param loginPwd
	 * @return
	 */
	public User checkUserLoginByEmail(@Param("loginName") String loginName,@Param("loginPwd") String loginPwd);
	/**
	 * 查询机构过期时间
	 * @param org_id
	 * @return
	 */
	public List<Map<String, Object>> queryOrgChannelDate(@Param("org_id") String org_id);
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-2-17
	 * 描　述:
	 *     用户名ID  验证登录
	 * </pre>
	 * 
	 * @param login_name
	 * @param login_pwd
	 * @return
	 */
	public User checkUserLoginById(@Param("userId") String userId);
	/**
	 * 
	 * @param phone
	 * @return
	 */
	public User getUserByID(@Param("uid") String uid);
}
