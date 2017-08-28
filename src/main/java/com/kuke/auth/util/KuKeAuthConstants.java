package com.kuke.auth.util;


public class KuKeAuthConstants {


	
	/**
	 * 单点登陆Cookie name 个人用户
	 */
	public static final String SSO_USER_COOKIE_NAME = "KuKeDesktopSSOID";
	
	/**
	 * 单点登陆Cookie name 机构用户
	 */
	public static final String SSO_ORG_COOKIE_NAME="KuKeDesktopSSOORGID";
	
	
	public static final String ORG_DOWN_REC= "org_down_rec";
	
	/**
	 * 
	 */
	public static final String LOGIN_COOKIE_NAME="loguiqueid";
	
	/**
	 * NAXOS频道ID
	 */
	public static final String NAXOS = "1";
	
	/**
	 * SPOKEN频道ID
	 */
	public static final String SPOKEN = "2";
	
	/**
	 * VIDEO频道ID
	 */
	public static final String VIDEO = "3";
	
	/**
	 * LIVE频道ID
	 */
	public static final String LIVE = "4";
	
	/**
	 * FM频道ID
	 */
	public static final String FM = "5";
	
	
	/**
	 * 圈子频道ID
	 */
	public static final String SNS = "6";
	
	
	
	/**
	 * 验证成功结果
	 */
	public static final String SUCCESS = "SUCCESS";
	
	
	/**
	 * 验证失败结果
	 */
	public static final String FAILED = "FAILED";
	
	
	/**
	 * 被加入黑名单
	 */
	public static final String FREEZED = "FREEZED";
	
	
	/**
	 * 验证重复结果
	 */
	public static final String REPLACE = "REPLACE";
	
	/**
	 * 激活
	 */
	public static final String ACTIVE = "ACTIVE";
	
	/**
	 * 尚未登录
	 */
	public static final String NOMALLOGIN = "NOMALLOGIN";
	
	/**
	 * 是否自动登录
	 */
	public final static String autoflag = "autoflag";
	
	/**
	 * 验证机构没有开通频道
	 */
	public static final String CLOSE = "CLOSE";
	
	
	/**
	 * 验证机构频道超过有效期
	 */
	public static final String OUTDATE = "OUTDATE";
	
	
	/**
	 * 验证机构频道超过最大在线人数
	 */
	public static final String MAXONLINE = "MAXONLINE";
	
	
	/**
	 * 分享到新浪微博的类型
	 */
	public static final String SINA = "0";
	
	
	/**
	 * 分享到腾讯的类型
	 */
	public static final String QQ = "1";
	
	
	/**
	 * 分享到人人的类型
	 */
	public static final String RENREN = "2";
	
	/**
	 * 加密秘钥
	 */
	public final static String DESKEY = "kuke!@#$";
	
	/**
	 * 找回密码key
	 */
	public static final String FINDPASSWORD = "kukeFindPassword";
	
	public static final String LABEL = "11,12";
	public static final String ARTIST = "71";
	
	public static final String FOLDERTRACK = "1";//单曲夹
	public static final String FOLDERCATLOG = "2";//专辑夹
	
	public static final String FOLDERSIZE = "8";//夹子每页数量
	
	public static final String TRACKFOLDERSIZE = "20";//单曲夹每页单曲数量
	public static final String CATLOGFOLDERSIZE = "8";//专辑夹每页专辑数量
	
	public static final String LABLESIZE = "10";//厂牌每页数量
	public static final String ARTISTSIZE = "10";//艺术家每页数量
	
}
