package com.kuke.auth.snslogin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kuke.auth.snslogin.bean.SnsUser;

public interface SNSMapper {
	
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-3-18
	 * 描　述:
	 *       解绑第三方新浪
	 * </pre>
	 * @param userID
	 * @return
	 */
	public int delUserSNS_SINA(@Param("userID") String userID);
	/**
	 * 解除qq
	 * @param userID
	 * @return
	 */
	public int delUserSNS_QQ(@Param("userID") String userID);
	/**
	 * 解除微信
	 * @param userID
	 * @return
	 */
	public int delUserSNS_WeChat(@Param("userID") String userID);
	
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-3-18
	 * 描　述:
	 *      绑定集合
	 * </pre>
	 * @param userID
	 * @return
	 */
	public Map<String, String> snsBoundMap(@Param("userID") String userID);
	
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
	public String checkUserSNS_SINA(@Param("snsID") String snsID);
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
	 * 创建人: maxin
	 * 创建于: 2013-2-21
	 * 描　述:
	 *    	校验绑定 第三方 QQ
	 * </pre>
	 * @param openId
	 * @return
	 */
	public String checkUserSNS_QQ(@Param("snsID") String snsID);
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
	 * 
	 * <pre>
	 * 创建人: wanyj
	 * 创建于: 2016-09-29
	 * 描　述:
	 *    	校验绑定 第三方 微信
	 * </pre>
	 * @param snsID
	 * @return
	 */
	public String checkUserSNS_WX(@Param("snsID") String snsID);
	
	//通过unionid验证是否注册
	public List<Map<String,String>> checkUnionIdUserSNS_WX(@Param("unionid") String unionid);
	
	/**
	 * 
	 * <pre>
	 * 创建人: wanyj
	 * 创建于: 2016-9-29
	 * 描　述:
	 *      绑定 第三方 WX
	 * </pre>
	 * @param snsUser
	 * @return
	 */
	public int boundUserSNS_WX(SnsUser snsUser);
	
	/**
	 * 
	 * <pre>
	 * 创建人: wanyj
	 * 创建于: 2016-9-29
	 * 描　述:
	 *      更新 第三方 微信 accessToken
	 * </pre>
	 * @param snsUser
	 * @return
	 */
	public int updateUserSNS_WX_AccessToken(SnsUser snsUser);
	
	/**
	 * 获取userId的绑定的QQ信息
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getQQInfo(@Param("userId")String userId);
	
	/**
	 * 获取userId的绑定的微信信息
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getWeChatInfo(@Param("userId")String userId);
	
	/**
	 * 获取userId的绑定的sina微博信息
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getSinaInfo(@Param("userId")String userId);
	/**
	 * 检查用户是否已绑定手机
	 * <pre>
	 * 创建人: wanyj
	 * 创建于: 2016-09-29
	 * </pre>
	 * @param snsID
	 * @return
	 */
	public String checkUserSNS_Phone(@Param("uid") String uid);
	/**
	 * 
	 * <pre>
	 * 创建人: wanyj
	 * 创建于: 2016-9-29
	 * 描　述:
	 *      绑定 手机
	 * </pre>
	 * @param snsUser
	 * @return
	 */
	public int boundUserSNS_Phone(@Param("uid") String uid,@Param("phone") String phone);
	/**
	 * 解除手机绑定
	 * <pre>
	 * 创建人: wanyj
	 * 创建于: 2016-9-29
	 * 描　述:
	 *      绑定 手机
	 * </pre>
	 * @param snsUser
	 * @return
	 */
	public int updateUserSNS_phone(@Param("uid") String uid);
	/**
	 * 解除邮箱绑定
	 * <pre>
	 * 创建人: wanyj
	 * 创建于: 2016-9-29
	 * </pre>
	 * @param snsUser
	 * @return
	 */
	public int updateUserSNS_email(@Param("uid") String uid);
}
