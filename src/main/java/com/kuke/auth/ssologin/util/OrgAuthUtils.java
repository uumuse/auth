package com.kuke.auth.ssologin.util;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuke.core.redis.RedisUtil;
import com.kuke.core.service.SpringContextHolder;
import com.kuke.util.MD5;
import com.kuke.util.RandomCode;
import com.kuke.util.StringUtil;
import com.kuke.auth.ssologin.bean.OrgChannel;
import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.auth.ssologin.quartz.org.RetrievalOrg;
import com.kuke.auth.util.KuKeAuthConstants;

public class OrgAuthUtils {

	static Logger logger = LoggerFactory.getLogger(OrgAuthUtils.class);

	private static OrgAuthUtils instance = null;

	private static RedisUtil redisUtil = SpringContextHolder.getBean("redisUtil");

	private OrgAuthUtils() {

	}

	public static OrgAuthUtils getInstance() {
		if (instance == null) {
			instance = new OrgAuthUtils();
		}

		return instance;
	}

	/**
	 * 将用户信息写入redis
	 */
	public static String OrgLogin(Organization organization,
			String from_client) {
		String result = KuKeAuthConstants.FAILED;
		if (organization != null) {
			try {
				logger.info("enter OrgLogin redis ready to write,org info:::::："+JSONArray.fromObject(organization)+"~~~~~~~~~~");
				//将机构完整信息写入redis
				//org_id,org_name,org_area,org_url,reg_date,org_type_id,org_type_name 
				String key = "redisOrgInfo:"+organization.getOrg_id();
				Map<String,String> orgInfoMap = new HashMap<String,String>();
				if(organization.getOrg_id()!=null&&!organization.getOrg_id().equals("")){
					orgInfoMap.put("org_id", organization.getOrg_id());
				}
				if(organization.getOrg_name()!=null&&!organization.getOrg_name().equals("")){
					orgInfoMap.put("org_name", organization.getOrg_name());
				}
				if(organization.getOrg_area()!=null&&!organization.getOrg_area().equals("")){
					orgInfoMap.put("org_area", organization.getOrg_area());
				}
				if(organization.getOrg_url()!=null&&!organization.getOrg_url().equals("")){
					orgInfoMap.put("org_url", organization.getOrg_url());
				}
				if(organization.getOrg_url()!=null&&!organization.getOrg_url().equals("")){
					orgInfoMap.put("reg_date", organization.getReg_date());
				}
				if(organization.getOrg_type_id()!=null&&!organization.getOrg_type_id().equals("")){
					orgInfoMap.put("org_type_id", organization.getOrg_type_id());
				}
				if(organization.getOrg_type_name()!=null&&!organization.getOrg_type_name().equals("")){
					orgInfoMap.put("org_type_name", organization.getOrg_type_name());
				}
				logger.info("OrgLogin init orgInfoMap done orgInfoMap size:"+orgInfoMap.size()+", begin writing into redis~~~~~~~~~~~~~");
				redisUtil.hashMultipleSet(key, orgInfoMap);
				logger.info("OrgLogin writing to redis done~~~~~~~~~~~~~~~~~~~~~~");
				// IP验证通过 生成随机数，并将随机数与用户信息写入redis
				// 随机码(A~Z,a~z,0~9)
				String code = RandomCode.getRandomString(10);
				String redisKey = "";
				from_client = "".equals(StringUtil.dealNull(from_client))?"web":from_client;
				if (StringUtils.isNotBlank(from_client)) {
					if ("web".equals(from_client)) {
						redisKey = "org_web_" + organization.getOrg_id()+"_"+code;
					} else if ("ios".equals(from_client)) {
						redisKey = "org_mobel_ios_" + organization.getOrg_id()+"_"+code;
					} else if ("android".equals(from_client)) {
						redisKey = "org_mobel_android_"+ organization.getOrg_id()+"_"+code;
					}else{
						redisKey = "org_web_" + organization.getOrg_id()+"_"+code;
					}
				}
//				System.out.println("redis登录信息:"+redisKey);
				String channel_id = KuKeAuthConstants.NAXOS;
				String time = String.valueOf(new Date().getTime());
				MD5 md5 = new MD5();
				String secondCookieValue = redisKey + "|" + code + "|"
						+ organization.getOrg_id() + "|" + time + "|"
						+ channel_id;
				String firstCookieValue = md5.getMD5ofStr(secondCookieValue);
				String cookieValue = firstCookieValue + "&" + secondCookieValue;
//				System.err.println(secondCookieValue);
				// 将cookeVlaue进行DES加密
				try {
//					cookieValue = AES.Encrypt(cookieValue,
//							AES.cKey);
					//为防止加密后的字符串出现特殊字符导致无法添加cookie,这里要将加密好的字符串在进行URL编码
					cookieValue = URLEncoder.encode(cookieValue, "UTF-8");
				} catch (Exception e1) {
					logger.debug("加密失败");
				}
				organization.setOrg_ssoid(cookieValue);
				// 将机构信息写入redis
				Map<String, String> orgMap = new HashMap<String, String>();
				orgMap.put("loginCode", code);
				orgMap.put("loginTime", time);
				orgMap.put("channel_id", "0");

				redisUtil.setExpire(redisKey,orgMap, 5*60);
				result = KuKeAuthConstants.SUCCESS;
				return result;
			} catch (Exception e) {
				logger.info("~~~~~~~~~~~~~~~~~check OrgAuthUtils.OrgLogin redisUtil.hashMultipleSet(key, orgInfoMap)~~~~~~~~~~~~~~");
				e.printStackTrace();
				return result;
			}
		}
		return result;
	}

	/**
	 * 校验Org票据是否存在
	 */
	public Organization AuthOrgCookie(String ticket) {
		Organization organization = new Organization();
		organization.setOrg_ssoid(ticket);
		organization.setOrg_status(KuKeAuthConstants.FAILED);
		// 解密cookeieValue
		try {
			if (StringUtils.isNotBlank(ticket)) {
				//现将cookieValue进行URL解码
				String cookieValue = URLDecoder.decode(ticket, "UTF-8");
//				cookieValue = AES.Decrypt(cookieValue,AES.cKey);
				MD5 md5 = new MD5();
				System.out.println("==============orgTicket=============="+cookieValue);
				String firstValue = cookieValue.split("&")[0];
				String secondValue = cookieValue.split("&")[1];
				if (firstValue.equals(md5.getMD5ofStr(secondValue))) {
					// redisKey+"|"+code+"|"+organization.getOrg_id()+"|"+time+"|"+channel_id
					String[] orgInfo = secondValue.split("\\|");
					String redisKey = orgInfo[0];
					String code = orgInfo[1];
					String orgId = orgInfo[2];
//					String channel_id = orgInfo[4];
					String orgInfoKey = "redisOrgInfo:"+orgId;
					// 从redis中取出机构信息
					////org_id,org_name,org_area,org_url,reg_date,org_type_id,org_type_name
					if(redisUtil.containsKey(orgInfoKey)){
						Map<String,String> orgInfoMap = new HashMap<String,String>();
						orgInfoMap = redisUtil.hashGetAll("redisOrgInfo:"+orgId);
						organization.setOrg_area(orgInfoMap.get("org_area"));
						organization.setOrg_id(orgInfoMap.get("org_id"));
						organization.setOrg_name(orgInfoMap.get("org_name"));
						organization.setOrg_type_id(orgInfoMap.get("org_type_id"));
						organization.setOrg_type_name(orgInfoMap.get("org_type_name"));
						organization.setOrg_url(orgInfoMap.get("org_url"));
						organization.setReg_date(orgInfoMap.get("reg_date"));
						organization.setOrg_status(KuKeAuthConstants.FAILED);
						redisUtil.hashMultipleSet(orgInfoKey, orgInfoMap);
					}else{
						organization = RetrievalOrg.getOrgInfoById(orgId);
						Map<String,String> orgInfoMap = new HashMap<String,String>();
						orgInfoMap.put("org_id", organization.getOrg_id());
						orgInfoMap.put("org_name", organization.getOrg_name());
						orgInfoMap.put("org_area", organization.getOrg_area());
						orgInfoMap.put("org_url", organization.getOrg_url());
						orgInfoMap.put("reg_date", organization.getReg_date());
						orgInfoMap.put("org_type_id", organization.getOrg_type_id());
						orgInfoMap.put("org_type_name", organization.getOrg_type_name());
						redisUtil.hashMultipleSet(orgInfoKey, orgInfoMap);
					}
					if (redisUtil.containsKey(redisKey)) {
						Map<String, String> tempMap = redisUtil
								.hashGetAll(redisKey);
						String redisCode = tempMap.get("loginCode");
						if (code.equals(redisCode)) {
							// cookie中随机数与redis随机数相等,通过验证
							organization.setOrg_status(KuKeAuthConstants.SUCCESS);
							// 重写redis
							redisUtil.setExpire(redisKey,tempMap, 5*60);
						}else{
							// 随机数不相等提示重新登陆
							return organization;
						}
					} else {
						// redis中不存在用户信息,cookie中存在,自动登录，并将入户信息写入redis
						organization.setOrg_status(KuKeAuthConstants.SUCCESS);
						//登陆信息写入redis
						Map<String,String> orgInfoMap = new HashMap<String,String>();
						orgInfoMap.put("loginCode", code);
						orgInfoMap.put("loginTime", String.valueOf(new Date().getTime()));
						orgInfoMap.put("channel_id", "0");
						redisUtil.setExpire(redisKey,orgInfoMap, 5*60);
					}
				}
			}

		} catch (Exception e) {
			logger.debug("AuthOrgCookieError!!!!"+e);
		}
		return organization;
	}

	/**
	 * 校验当前日期是否在寒暑假中
	 */
	public Organization AuthOrgHoliday(String user_id, String org_id) {
		Date date = new Date();
		int month = date.getMonth() + 1;
		boolean flag = false;
		Organization organization = new Organization();
		organization.setOrg_status(KuKeAuthConstants.FAILED);
		try {
			// String roamingMonth =
			// String.valueOf(PropertiesHolder.getContextProperty("org.roaming.month"));
			// if (roamingMonth.indexOf(String.valueOf(month)) != -1) {
			flag = true;
			// }
			if (flag) {
				organization = RetrievalOrg.getOrgInfoById(org_id);
			}
		} catch (Exception e) {
			return organization;
		}
		return organization;
	}

	/**
	 * 删除机构信息
	 */
	public String OrgOut(String ticket, String channel_id) {
		String result = KuKeAuthConstants.FAILED;
		try {
			if (StringUtils.isNotBlank(ticket)) {
				String cookieValue = ticket;
//				String cookieValue = DESUtil.decrypt(ticket,
//						KuKeAuthConstants.DESKEY);
				String secondValue = cookieValue.split("&")[1];
				String redisKey = secondValue.split("\\|")[0];
				redisUtil.delKeysLike(redisKey);
				result = KuKeAuthConstants.SUCCESS;
			}
		} catch (Exception e) {
			return result;
		}
		return result;
	}

	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-3-5
	 * 描　述:
	 *      校验机构
	 * </pre>
	 * 
	 * @param sso_id
	 * @param org_id
	 * @param channel_id
	 * @return
	 */
	public OrgChannel AuthOrg(String sso_id, String org_id, String channel_id) {
		OrgChannel orgChannel = new OrgChannel();
		orgChannel.setChannelStatus(KuKeAuthConstants.CLOSE);
		logger.debug("sso_idsso_idsso_idsso_id==========="+sso_id);
		if(StringUtils.isNotBlank(sso_id)){
			//解析cookeValue
			try {
				//现将cookieValue进行URL解码
				String cookieValue = URLDecoder.decode(sso_id, "UTF-8");
//				cookieValue = AES.Decrypt(cookieValue,
//						AES.cKey);
				MD5 md5 = new MD5();
				String firstValue = cookieValue.split("&")[0];
				String secondValue = cookieValue.split("&")[1];
				int current_channel_online_num = 0;
				if (firstValue.equals(md5.getMD5ofStr(secondValue))) {
					String[] orgInfo = secondValue.split("\\|");
					String redisKey = orgInfo[0];
					//从redis中取出机构登陆信息
					List<Map<String,String>> orgLoginMap = new ArrayList<Map<String,String>>();
					if(StringUtils.isNotBlank(redisKey)){
						Set<String> keySet = new HashSet<String>();
						keySet = redisUtil.getKeys("*_"+org_id+"_*");
						List<String> keyList = new ArrayList<String>(keySet);
						orgLoginMap = redisUtil.batchHashGetAll(keyList);
						if(orgLoginMap != null && orgLoginMap.size() != 0){
							Iterator<Map<String,String>> orgLoginMapIter = orgLoginMap.iterator();
							while(orgLoginMapIter.hasNext()){
								if(orgLoginMapIter.next().get("channel_id").equals(channel_id)){
									current_channel_online_num++;
								}
							}
						}
					}
					logger.debug("current_channel_online_num======"+current_channel_online_num);
					/**
					 * 检查频道是否开通
					 */
					orgChannel = RetrievalOrg.getOrgChannelAllowOpen(org_id, channel_id);
					if (KuKeAuthConstants.SUCCESS.equals(orgChannel.getChannelStatus())) {
						/**
						 * 检查频道是否过期
						 */
						orgChannel = RetrievalOrg.getOrgChannelAllowDate(org_id,
								channel_id, orgChannel);
						if (KuKeAuthConstants.SUCCESS.equals(orgChannel.getChannelStatus())) {
							/**
							 * 检查是否超过最大在线人数
							 */
							orgChannel = RetrievalOrg.getOrgChannelAllowOnline(org_id,channel_id, orgChannel);
							int channel_max_online_num = Integer.valueOf(orgChannel.getMax_online_num());
							if (current_channel_online_num <= channel_max_online_num) {
								orgChannel.setChannelStatus(KuKeAuthConstants.SUCCESS);
								if(redisUtil.containsKey(redisKey)){
									Map<String,String> tempMap = new HashMap<String,String>();
									tempMap = redisUtil.hashGetAll(redisKey);
									tempMap.put("channel_id", channel_id);
//									redisUtil.hashMultipleSet(redisKey, tempMap);
									redisUtil.setExpire(redisKey,tempMap, 5*60);
									logger.debug("current_channel_id=========="+tempMap.get("channel_id"));
								}
							} else {
								orgChannel.setChannelStatus(KuKeAuthConstants.MAXONLINE);
							}
						}
					}
				}
				
			}catch (Exception e) {
				logger.debug("error!!!"+e);
				e.printStackTrace();
				return orgChannel;
			}
		}
		return orgChannel;
	}

	/**
	 * 得到新的Ip补全0的
	 */
	private static String getAllIp(String oldIp) {
		String newIp = "";
		Pattern p = Pattern.compile("[.]");
		String[] temp = p.split(oldIp);
		for (int i = 0; i < temp.length; i++) {
			switch (temp[i].length()) {
			case 1:
				newIp += "00" + temp[i];
				break;
			case 2:
				newIp += "0" + temp[i];
				break;
			default:
				newIp += temp[i];
				break;
			}
		}
		return newIp;
	}

	public static long ipToLong(String strIp) {
		long[] ip = new long[4];
		// 先找到IP地址字符串中.的位置
		try {
			// 将每个.之间的字符串转换成整型
			int position1 = strIp.indexOf(".");
			int position2 = strIp.indexOf(".", position1 + 1);
			int position3 = strIp.indexOf(".", position2 + 1);

			ip[0] = Long.parseLong(strIp.substring(0, position1));
			ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
			ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
			ip[3] = Long.parseLong(strIp.substring(position3 + 1));
		} catch (Exception e) {
			// System.out.println(ip[0] + "~" + ip[1] + "~" + ip[2] + "~" +
			// ip[3]);
		}
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	public boolean ipCompare(String a, String b, String c) {
		return ipToLong(a) <= ipToLong(b) && ipToLong(b) <= ipToLong(c);
	}


	public String OrgOnline(String ticket, String current_channel_id) {
		String result = KuKeAuthConstants.FAILED;
		try {
			String cookieValue = URLDecoder.decode(ticket, "UTF-8");
//			String cookeValue = AES.Decrypt(ticket,
//					AES.cKey);
//			 redisKey+"|"+code+"|"+organization.getOrg_id()+"|"+time+"|"+channel_id;
			String secondCookidValue = cookieValue.split("&")[1];
			String redisKey = secondCookidValue.split("\\|")[0];
			//修改redis中的机构频道信息
			logger.debug("orgOnline==============="+cookieValue);
//			Map<String,String> tempMap = new HashMap<String,String>();
			logger.debug("redisKeyredisKeyredisKeyredisKey======="+redisKey);
			logger.debug("isexist==========rediskey====="+redisUtil.containsKey(redisKey));
//			if(redisUtil.containsKey(redisKey)){
//				tempMap = redisUtil.hashGetAll(redisKey);
//				tempMap.put("channel_id", current_channel_id);
//				redisUtil.hashMultipleSet(redisKey, tempMap);
//				redisUtil.setExpire(redisKey,tempMap, 5*60);
//				logger.debug("current_channel_id=========="+tempMap.get("channel_id"));
//			}
			result = KuKeAuthConstants.SUCCESS;
		} catch (Exception e) {
			return result;
		}
		return result;
	}

}
