package com.kuke.core.util;

/**
 * 
  * <pre>
  * 创建人: 梁利伟
  * 创建于: Oct 31, 2008
  * 最后修改日期：Oct 31, 2008
  * 描　述:
  *    	 系统常量
  * </pre>
  * @return
 */
public class SystemConstants {
	
	
	/**
	 * 权限请求HTTP地址
	 */
	public static final String PASSPORT_URL = "http://auth-m.kuke.com:8080";	
	
	/**
	 * 默认自定义分页page_size
	 */
	public static final int PAGE_SIZE = 10;
	/**
	 * 
	 */
	public static final String DISPLAY_ID = "tableId";
	 /**
     * 按顺序排列
     */
    public static final String ORDER_ASC  = "1";

    /**
     * 按逆序排列
     */
    public static final String ORDER_DESC = "2";
    
    
	/**
	 * 验证成功结果
	 */
	public static final String SUCCESS = "SUCCESS";
	
	
	/**
	 * 验证失败结果
	 */
	public static final String FAILED = "FAILED"; 
	
	
	
	/**
	 * 验证重复结果
	 */
	public static final String REPLACE = "REPLACE";
	
	
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
	 * 包时用户
	 */
	public static final String USER_TYPE_TIME = "95";
	
	/**
	 * 包时段用户
	 */
	public static final String USER_TYPE_DATE = "96";
	
	/**
	 * 注册用户
	 */
	public static final String USER_AUTH_REG = "1";
	
    
	/**
	 * 用户默认的到期时间
	 */
	public static final String USER_DEFAULT_END_DATE = "2050-01-01";
    
  
}
