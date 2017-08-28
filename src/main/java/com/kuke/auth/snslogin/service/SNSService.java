package com.kuke.auth.snslogin.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kuke.auth.snslogin.bean.SnsUser;

public interface SNSService {
	
	/**
	 * 解除新浪绑定
	 * @param userID
	 * @return
	 */
	public int delUserSNS_SINA(String userID);
	/**
	 * 解除qq绑定
	 * @param userID
	 * @return
	 */
	public int delUserSNS_QQ(String userID);
	/**
	 * 解除微信绑定
	 * @param userID
	 * @return
	 */
	public int delUserSNS_WeChat(String userID);
	
	public Map<String, String> snsBoundMap(String userID);

	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-2-22
	 * 描　述:
	 *      校验绑定 第三方 新浪微博
	 * </pre>
	 * @param openId
	 * @return
	 */
	public String checkUserSNS_SINA(String snsID);
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-2-21
	 * 描　述:
	 *    	校验绑定 第三方 QQ
	 * </pre>
	 * @param openId
	 * @return
	 */
	public String checkUserSNS_QQ(String snsID);
	/**
	 * 
	 * <pre>
	 * 创建人: wanyj
	 * 创建于: 2016-09-29
	 * 描　述:
	 *    	校验绑定 第三方 微信
	 * </pre>
	 * @param snsID : 第三方用户ID
	 * @return
	 */
	public String checkUserSNS_WeChat(String snsID);
	
	public List<Map<String,String>> checkUnionIdUserSNS_WX(String unionid);
	
	/**
	 * 
	 * <pre>
	 * 创建人: wanyj
	 * 创建于: 2016-09-29
	 * 描　述:
	 *    	校验绑定 第三方 微信
	 * </pre>
	 * @param snsID : 第三方用户ID
	 * @return
	 */
	public String checkUserSNS_Phone(String uid);
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-2-21
	 * 描　述:
	 *      绑定 第三方 新浪微博
	 * </pre>
	 * @param userId
	 * @param openId
	 * @return
	 */
	public int boundUserSNS_SINA(SnsUser snsUser);
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-2-21
	 * 描　述:
	 *      绑定 第三方 QQ
	 * </pre>
	 * @param userId
	 * @param openId
	 * @return
	 */
	public int boundUserSNS_QQ(SnsUser snsUser);
	/**
	 * 
	 * <pre>
	 * 创建人: wanyj
	 * 创建于: 2016-9-29
	 * 描　述:
	 *      绑定 第三方 微信
	 * </pre>
	 * @param snsUser
	 * @return
	 */
	public int boundUserSNS_WeChat(SnsUser snsUser);
	/**
	 * 
	 * <pre>
	 * 创建人: wanyj
	 * 创建于: 2016-9-29
	 * 描　述:
	 *      绑定 第三方 微信
	 * </pre>
	 * @param snsUser
	 * @return
	 */
	public int boundUserSNS_Phone(String uid,String phone);
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-2-25
	 * 描　述:
	 *      更新 第三方 新浪微博 accessToken
	 * </pre>
	 * @param openId
	 * @return
	 */
	public int updateUserSNS_SINA_AccessToken(SnsUser snsUser);
	/**
	 * 
	 * <pre>
	 * 创建人: wanyj
	 * 创建于: 2016-9-29
	 * 描　述:
	 *      更新 第三方 WX accessToken
	 * </pre>
	 * @param snsUser
	 * @return
	 */
	public int updateUserSNS_WX_AccessToken(SnsUser snsUser);
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-2-25
	 * 描　述:
	 *      更新 第三方 QQ accessToken
	 * </pre>
	 * @param openId
	 * @return
	 */
	public int updateUserSNS_QQ_AccessToken(SnsUser snsUser);
	/**
	 * 校验userId用户是否绑定有新浪账户
	 * @param userId
	 * @return 有：返回snsid, 无：返回 null
	 */
	public Map<String, Object> checkUserBind_SINA(String userId);
	/**
	 * 校验userId用户是否绑定有QQ账户
	 * @param userId
	 * @return 有：返回snsid, 无：返回 null
	 */
	public Map<String, Object> checkUserBind_QQ(String userId);
	/**
	 * 校验userId用户是否绑定有微信账户
	 * @param userId
	 * @return 有：返回snsid, 无：返回 null
	 */
	public Map<String, Object> checkUserBind_WeChat(String userId);
	/**
	 * 解除手机绑定
	 * @param uid
	 * @return
	 */
	public int updateUserSNS_phone(String uid);
	/**
	 * 解除邮箱绑定
	 * @param uid
	 * @return
	 */
	public int updateUserSNS_email(String uid);
}
