/**
 * 下载
 */
package com.kuke.auth.downloadAuth.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.downloadAuth.bean.OrgDownLog;
import com.kuke.auth.downloadAuth.bean.OrgDownMoney;
import com.kuke.auth.downloadAuth.bean.OrgDownUser;
import com.kuke.auth.downloadAuth.service.DownloadAuthService;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.ssologin.bean.OrgDownIp;
import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.auth.ssologin.quartz.org.RetrievalOrg;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.LogRecord;
import com.kuke.auth.util.OrgOauth;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.auth.util.UserOauth;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.service.SSOService;
import com.kuke.util.MD5;
import com.kuke.util.MessageFormatUtil;

@Controller
@RequestMapping("/kuke/download")
public class DownloadAuthController extends BaseController {
	static Logger logger = LoggerFactory.getLogger(DownloadAuthController.class);
	
	@Autowired
	private DownloadAuthService downloadAuthService;
	/**
	 * 下载方式判断
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/load")
	public @ResponseBody ResponseMsg downAuthorize(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		String item_id = params.get("item_id");
		String channel_id = params.get("channel_id");
		String byUser = params.get("flag");
		byUser = byUser==null?"":byUser;
		Organization org = null;
		JSONObject jsonObject = new JSONObject();
		boolean flag = false;
		String code = "FAILED";
		String msg = "FAILED";
		String codeDesc = "NOTLOGIN:未登录;"
						+ "SUCCESS:成功;"
						+ "FAILED:失败;"
						+ "BYORGUSERPASS:使用机构用户名密码下载;"
						+ "NOMONEY:余额不足;"
						+ "ERROR:错误;";
		
		try {
			//机构登录
			org = OrgOauth.orgLogin(request, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		OrgDownMoney orgDownMoney = null;
		if (org != null) {
			//机构账户的钱
			orgDownMoney = downloadAuthService.getOrgDownMoney(org.getOrg_id());
		}
		if (org != null && org.getOrg_status().equals(KuKeAuthConstants.SUCCESS) && !byUser.equals("byUser") && orgDownMoney != null) {
			// 使用机构下载
			jsonObject = downByOrg(jsonObject, channel_id, item_id,org.getOrg_id(), request, response, params, orgDownMoney);
			if("success".equals(jsonObject.get("result"))){
				flag = true;
				code = "SUCCESS";
				msg = "SUCCESS";
			}else{
				code = jsonObject.getString("result").toUpperCase();
				msg = jsonObject.getString("result").toUpperCase();
			}
		} else {
			// 使用网站帐号下载
			String user_id = "";
			String user_status = "";
			try {
				User user = UserOauth.userLoginByToken(request);
				user_status = user.getUser_status();
				user_id = user.getUid();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (StringUtils.isNotEmpty(user_id) && user_status.equals(KuKeAuthConstants.SUCCESS)) {
				// 使用网站帐号下载
				jsonObject = downByUser(jsonObject, channel_id, item_id,user_id, request, params);
				if("success".equals(jsonObject.get("result"))){
					flag = true;
					code = "SUCCESS";
					msg = "SUCCESS";
				}else{
					code = jsonObject.getString("result").toUpperCase();
					msg = jsonObject.getString("result").toUpperCase();
				}
			} else {
				jsonObject.put("result", "notlogin");
				code = "NOTLOGIN";
				msg = "NOTLOGIN";
			}
		}
		return new ResponseMsg(flag, code, msg, codeDesc, jsonObject);

	}
	/**
	 * 网站用户下载
	 * @param jsonObject
	 * @param channel_id
	 * @param item_id
	 * @param user_id
	 * @param request
	 * @param params
	 * @return
	 */
	private JSONObject downByUser(JSONObject jsonObject, String channel_id,
			String item_id, String user_id, HttpServletRequest request,
			Map<String, String> params) {
		try {

			if (	StringUtils.isNotEmpty(channel_id)//通道ID,
					&& StringUtils.isNotEmpty(item_id)//music.music_book的ID
					&& (channel_id.equals("50") || channel_id.equals("51") || channel_id.equals("52"))) {
				//乐谱下载 ,专辑下载,音频下载
				String retStr = UserOauth.userDownload(request, item_id,channel_id);
				// retStr="SUCCESS";
				params.put("userId", user_id);
				//得到下载资源的描述信息
				Map descMap = downloadAuthService.getDownloadItemDesc(item_id,channel_id);
				if (descMap != null) {
					if (retStr.equals("SUCCESS")) {
						// 有下载权限，初始化返回下载页面所需参数
						descMap = initReturnMap(jsonObject, item_id,
								channel_id, descMap);
						// 记录下载日志
						recordLog(getIp(request),
								(String) descMap.get("item_code"), item_id,
								(String) descMap.get("ctitle"),
								(String) descMap.get("etitle"), user_id, "1",
								"", "", "web",
								String.valueOf(descMap.get("pro_price_id")));
						jsonObject.put("result", "success");
					} else {
						// 无下载权限，初始化添加购物车所需参数
						String priceArr[] = { "403", "404", "405", "406" };// 乐谱下载
																			// ,专辑下载
																			// ,音频下载,有声读物下载
						String key = String.valueOf(PropertiesHolder
								.getContextProperty("passKey"));
						String passKey = "";
						String bshref = "";
						String bsimage = "";
						if (channel_id.equals("50")) {
							passKey = new MD5().getMD5ofStr(item_id
									+ priceArr[0] + key + user_id);
							bshref = "/kuke/musicbook/list";
							bsimage = getMusicBookImageUrl((String) descMap
									.get("music_book_file"));
						} else if (channel_id.equals("51")) {
							String ctype = downloadAuthService
									.getChannleIdByItemCode(item_id);
							passKey = new MD5().getMD5ofStr(item_id
									+ priceArr[1] + key + user_id);
							if (StringUtils.isNotEmpty(ctype)
									&& ctype.equals("2")) {
								descMap = downloadAuthService
										.getDownloadItemDesc(item_id, "406");
								
								passKey = new MD5().getMD5ofStr(item_id
										+ priceArr[3] + key + user_id);
							}
							//ctype=1,修改之前收费机制,按cd数收费,每cd10￥
							BigDecimal p = (BigDecimal) descMap.get("pro_price");
							String n = String.valueOf(descMap.get("nocd"));
							int pr = p.intValue() * Integer.parseInt(n);
							descMap.put("pro_price", pr);
							bshref = "/kuke/library/item?item_code=" + item_id;
							bsimage = getCataloguebioImageUrl(item_id);
						} else if (channel_id.equals("52")) {
							passKey = new MD5().getMD5ofStr(item_id
									+ priceArr[2] + key + user_id);
							bshref = "/kuke/library/item?item_code="
									+ (String) descMap.get("item_code")
									+ "&l_code=" + item_id;
							bsimage = getMp3ImageUrl((String) descMap
									.get("item_code"));
						}
						descMap.put("passKey", passKey);
						descMap.put("bshref", bshref);
						descMap.put("bsimage", bsimage);
						jsonObject.put("result", "failed");
					}
				} else {
					jsonObject.put("result", "error");
				}

				jsonObject.put("descMap", JSONObject.fromObject(descMap)
						.toString());

			} else {
				jsonObject.put("result", "error");
			}

		} catch (Exception e) {
			logger.debug("用户下载错误！", e);
			jsonObject.put("result", "error");
		}
		return jsonObject;
	}
	/**
	 * 使用机构下载,判断使用帐密方式还是ip方式
	 * @param jsonObject
	 * @param channel_id
	 * @param item_id
	 * @param org_id
	 * @param request
	 * @param response
	 * @param params
	 * @param orgDownMoney
	 * @return
	 */
	private JSONObject downByOrg(JSONObject jsonObject, String channel_id,
			String item_id, String org_id, HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params,
			OrgDownMoney orgDownMoney) {
		if (orgDownMoney != null && orgDownMoney.getIs_user_pass().equals("1")) {
			// 机构用户名密码方式下载
			jsonObject.put("result", "byOrgUserPass");
		} else if (orgDownMoney != null && orgDownMoney.getIs_user_pass().equals("2")) {
			// 机构ip下载处理
			jsonObject = byOrgIp(jsonObject, channel_id, item_id, org_id,request, response, params);
		}
		return jsonObject;
	}
	/**
	 * 检查机构帐密下载账号和密码是否正确
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkOrgUP")
	public @ResponseBody ResponseMsg checkOrgUP(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		JSONObject jsonObject = new JSONObject();
		String u = params.get("u");
		String p = params.get("p");
		Organization org = new Organization();
		try {
			org = OrgOauth.orgLogin(request, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (StringUtils.isNotEmpty(u) && StringUtils.isNotEmpty(p)) {
			OrgDownUser orgDownUser = downloadAuthService.getDownUserByUP(u, p,
					org.getOrg_id());
			if (org != null && orgDownUser != null && org.getOrg_id().equals(orgDownUser.getOrg_id())) {
				return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
			} else {
				return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
			}
		}else{
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
		}
	}
	/**
	 * 处理机构帐密下载请求
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadByOrgUP")
	public @ResponseBody ResponseMsg subByOrgUserPass(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		JSONObject jsonObject = new JSONObject();
		boolean flag = false;
		String code = "FAILED";
		String msg = "FAILED";
		String codeDesc = "SUCCESS:成功;"
						+ "FAILED:失败;"
						+ "NOMONEY:余额不足;"
						+ "ERROR:错误;";
		try {
			String item_id = params.get("item_id");
			String channel_id = params.get("channel_id");
			String u = params.get("u");
			String p = params.get("p");
			Organization org = new Organization();
			try {
				org = OrgOauth.orgLogin(request, response);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (StringUtils.isNotEmpty(u) && StringUtils.isNotEmpty(p)
					&& StringUtils.isNotEmpty(item_id)
					&& StringUtils.isNotEmpty(channel_id)) {

				OrgDownUser orgDownUser = downloadAuthService.getDownUserByUP(u, p, org.getOrg_id());
				if (org != null && orgDownUser!=null&& org.getOrg_id().equals(orgDownUser.getOrg_id())) {
					Map descMap = downloadAuthService.getDownloadItemDesc(item_id, channel_id);
					if (descMap != null) {
						descMap = initReturnMap(jsonObject, item_id,channel_id, descMap);
						// 扣除账户的钱 对应表 auth.org_down_user
						BigDecimal finalPrice = new BigDecimal(0);
						if (String.valueOf(descMap.get("pro_price_id")).equals("406")) {//zhuanji
							finalPrice = new BigDecimal(String.valueOf(descMap.get("pro_price")));
							//查出专辑下cd数
							String  nocd = String.valueOf(descMap.get("nocd"));
							//按cd数收费
							finalPrice = new BigDecimal(finalPrice.intValue()*Integer.parseInt(nocd));
							descMap.put("pro_price", finalPrice);
						} else {//
							//405 单曲(一个的价钱) , 还有乐章(sql已计算价钱:单价*page)
							finalPrice = (BigDecimal) descMap.get("pro_price");
						}
						
						Double leftMoney = orgDownUser.getMoney();
						int resNum = 0;
						if (BigDecimal.valueOf(leftMoney).compareTo(finalPrice) != -1) {
							resNum = downloadAuthService.updateOrgDownUser(
									orgDownUser.getId(),
									finalPrice.doubleValue());
							if (resNum == 1) {
								// 记录机构下载日志
								recordOrgDownLog(org.getOrg_id(),
										String.valueOf(descMap
												.get("pro_price_id")),
										finalPrice.doubleValue(), item_id, u,
										getIp(request), (String) descMap.get("ctitle"),
										(String) descMap.get("etitle"),
										(String) descMap.get("bsimage"),
										Integer.parseInt(orgDownUser.getId()),"1");
								recordLog(getIp(request),
										(String) descMap.get("item_code"), item_id,
										(String) descMap.get("ctitle"),
										(String) descMap.get("etitle"), org.getOrg_id(), "2",
										"", "", "web",
										String.valueOf(descMap.get("pro_price_id")));
								jsonObject.put("result", "success");
								flag = true;
								code = "SUCCESS";
								msg = "SUCCESS";
							} else {
								jsonObject.put("result", "error");
								code = "ERROR";
								msg = "ERROR";
								descMap.remove("durl");
								descMap.remove("durl320");
							}
						} else {
							jsonObject.put("result", "noMoney");
							code = "NOMONEY";
							msg = "NOMONEY";
							descMap.remove("durl");
							descMap.remove("durl320");
						}

					} else {
						jsonObject.put("result", "error");
						code = "ERROR";
						msg = "ERROR";
					}
					jsonObject.put("descMap", JSONObject.fromObject(descMap).toString());
				} else {
					jsonObject.put("result", "error");
					code = "ERROR";
					msg = "ERROR";
				}
			} else {
				jsonObject.put("result", "error");
				code = "ERROR";
				msg = "ERROR";
			}
		} catch (Exception e) {
			jsonObject.put("result", "error");
			logger.debug("机构帐密下载错误！", e);
		}
		return new ResponseMsg(flag, code, msg, codeDesc, jsonObject);
	}
	/**
	 * 处理机构IP下载
	 * @param jsonObject
	 * @param channel_id
	 * @param item_id
	 * @param org_id
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 */
	public JSONObject byOrgIp(JSONObject jsonObject, String channel_id,
			String item_id, String org_id, HttpServletRequest request,
			HttpServletResponse response, Map<String, String> params) {

		try {
			String ip=getIp(request);
			Organization org = new Organization();
			try {
				org = OrgOauth.orgLogin(request, response);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// 根据内存中的机构下载ip信息， 确定ip属于那个机构下载ip账户
			OrgDownIp orgDownIp = RetrievalOrg.getOrgDownIp(ip);
			if (StringUtils.isNotBlank(ip) && org != null && orgDownIp != null
					&& orgDownIp.getOrg_id().equals(org.getOrg_id())
					&& StringUtils.isNotEmpty(item_id)
					&& StringUtils.isNotEmpty(channel_id)) {

				orgDownIp = downloadAuthService.getDownIpById(orgDownIp.getStart_ip(),orgDownIp.getEnd_ip());
				Map descMap = downloadAuthService.getDownloadItemDesc(item_id,channel_id);
				if (descMap != null) {
					descMap = initReturnMap(jsonObject, item_id, channel_id,descMap);
					// 扣除账户的钱 对应表 auth.org_down_ip
					BigDecimal finalPrice = new BigDecimal(0);
					if (String.valueOf(descMap.get("pro_price_id")).equals("406")) {
						finalPrice = new BigDecimal(String.valueOf(descMap.get("pro_price")));
					} else {
						finalPrice = (BigDecimal) descMap.get("pro_price");
						Object o =  descMap.get("nocd");
						String nocd = "";
						if(o != null){
							nocd = String.valueOf(o);
							finalPrice = new BigDecimal(finalPrice.intValue()*Integer.parseInt(nocd));
						}
					}
					Double leftMoney = orgDownIp.getMoney();
					int resNum = 0;
					if (BigDecimal.valueOf(leftMoney).compareTo(finalPrice) != -1) {
						resNum = downloadAuthService.updateOrgDownIp(
								orgDownIp.getId(), finalPrice.doubleValue());
						if (resNum == 1) {
							// 记录机构下载日志
							recordOrgDownLog(
									org.getOrg_id(),
									String.valueOf(descMap.get("pro_price_id")),
									finalPrice.doubleValue(), item_id, "", ip,
									(String) descMap.get("ctitle"),
									(String) descMap.get("etitle"),
									(String) descMap.get("bsimage"),
									orgDownIp.getId(), "2");
							recordLog(getIp(request),
									(String) descMap.get("item_code"), item_id,
									(String) descMap.get("ctitle"),
									(String) descMap.get("etitle"), org.getOrg_id(), "2",
									"", "", "web",        
									String.valueOf(descMap.get("pro_price_id")));
							jsonObject.put("result", "success");
						} else {
							jsonObject.put("result", "noMoney");
							descMap.remove("durl");
							descMap.remove("durl320");
						}
					} else {
						jsonObject.put("result", "noMoney");
						descMap.remove("durl");
						descMap.remove("durl320");
					}

				} else {
					jsonObject.put("result", "noMoney");
				}
				jsonObject.put("descMap", JSONObject.fromObject(descMap).toString());
			} else {
				jsonObject.put("result", "noMoney");
			}
		} catch (Exception e) {
			logger.debug("机构ip下载错误！", e);
		}
		return jsonObject;
	}
	/**
	 * 初始化返回下载页面所需参数
	 * @param jsonObject
	 * @param item_id
	 * @param channel_id
	 * @param descMap
	 * @return
	 */
	 private Map initReturnMap(JSONObject jsonObject, String item_id,String channel_id, Map descMap) {
		String bsimage = "";
		Map<String, String> mp3 = new HashMap<String, String>();
		Map<String, String> mp3_320 = new HashMap<String, String>();
		String url = "";
		String url320 = "";
		if (channel_id.equals("51")) {// 专辑
			bsimage = getCataloguebioImageUrl(item_id);
			String ctype = downloadAuthService.getChannleIdByItemCode(item_id);
			if (StringUtils.isNotEmpty(ctype) && ctype.equals("2")) {
				descMap = downloadAuthService.getDownloadItemDesc(item_id,
						"406");
//				BigDecimal price = (BigDecimal) descMap.get("pro_price");
//				String n = String.valueOf(descMap.get("nocd"));
//				int pr = price.intValue() * Integer.parseInt(n);
//				descMap.put("pro_price", pr);

			}
			mp3.put("label_id", (String) descMap.get("labelid"));
			mp3.put("item_code", item_id);
			url = SSOService.initCataloguebioPlayUrl_HTTP(mp3);
		} else if (channel_id.equals("52")) {// 单曲
			bsimage = getMp3ImageUrl((String) descMap.get("item_code"));
			mp3.put("quality", "192");
			mp3.put("label_id", (String) descMap.get("labelid"));
			mp3.put("item_code", (String) descMap.get("item_code"));
			mp3.put("l_code", (String) descMap.get("id"));
			url = SSOService.initMp3PlayUrl_HTTP(mp3);
			if(descMap.get("kbps320") != null && "1".equals(descMap.get("kbps320").toString())){
				mp3_320.put("quality", "320");
				mp3_320.put("label_id", (String) descMap.get("labelid"));
				mp3_320.put("item_code", (String) descMap.get("item_code"));
				mp3_320.put("l_code", (String) descMap.get("id"));
				url320 = SSOService.initMp3PlayUrl320_HTTP(mp3_320);
			}
		} else if (channel_id.equals("50")) {// 乐谱
			bsimage = getMusicBookImageUrl((String) descMap
					.get("music_book_file"));
			mp3.put("music_book_file", (String) descMap.get("music_book_file"));
			url = SSOService.initMusicBookPlayUrl_HTTP(mp3);

		}
		jsonObject.put("durl", url);
		jsonObject.put("durl320", url320);
		descMap.put("bsimage", bsimage);

		// 过滤特殊字符
		StringTokenizer token = new StringTokenizer(
				(String) descMap.get("ctitle"), "\r\n");
		StringTokenizer etoken = new StringTokenizer(
				(String) descMap.get("etitle"), "\r\n");
		descMap.put("ctitle", escaping(token));
		descMap.put("etitle", escaping(etoken));

		return descMap;
	}

	// 过滤特殊字符
	private String escaping(StringTokenizer token) {
		StringBuffer buffer = new StringBuffer();
		while (token.hasMoreTokens()) {
			String temp = token.nextToken();
			buffer.append("" + convert(temp));
		}
		return buffer.toString();
	}

	public static String convert(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		StringBuffer buf = new StringBuffer();
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '>') {
				buf.append("&gt;");
			} else if (ch == '&') {
				buf.append("&amp;");
			} else if (ch == '\'') {
				buf.append("‘");
			} else if (ch == ',') {
				buf.append("，");
			} else if (ch == '"') {
				buf.append("”");
			} else if (ch == '®') {
				buf.append("&reg;");
			} else if (ch == '©') {
				buf.append("&copy;");
			} else if (ch == '™') {
				buf.append("&trade;");
			} else if (ch == ' ') {
				buf.append("&nbsp;");
			} else if (ch == '×') {
				buf.append("&divide;");
			} else if (ch == '÷') {
				buf.append("&divide;");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	// 获取乐谱图片地址
	private String getMusicBookImageUrl(String name) {
		return String.valueOf(PropertiesHolder.getContextProperty("imgUrl"))
				+ "/images/audio/musicbook/cover/" + name.substring(0, 1) + "/"
				+ name + ".jpg";
	}

	// 获取专辑图片地址
	private String getCataloguebioImageUrl(String name) {
		return String.valueOf(PropertiesHolder.getContextProperty("imgUrl"))
				+ "/images/audio/cata/" + name.substring(0, 1) + "/" + name
				+ ".gif";
	}

	// 获取单曲图片地址
	private String getMp3ImageUrl(String name) {
		return String.valueOf(PropertiesHolder.getContextProperty("imgUrl"))
				+ "/images/audio/cata/" + name.substring(0, 1) + "/" + name
				+ ".gif";
	}

	
	// 记录下载日志
	private void recordLog(String ip, String item_code, String item_id,
			String cname, String ename, String user_id, String user_type,
			String down_type, String channel_type, String from_client,
			String channel_id) {
		try {
			if (channel_id.equals("403")) {
				//写入数据库
				LogRecord.addUserDownLog(ip, item_id, "", cname, ename,
						user_id, user_type, "3", "1", "1");
				//写入文本
				LogRecord.addUserDownLog(ip, item_id, "", cname, ename,
						user_id, user_type, "3", "1", "1");
				LogRecord.addUserDownLog("3", from_client, user_id, null, item_code, item_id, down_type, ip);
			} else if (channel_id.equals("404")) {
				//写入数据库
				LogRecord.addUserDownLog(ip, "", item_id, cname, ename,
						user_id, user_type, "1", "1", "1");
				//写入文本
				LogRecord.addUserDownLog(ip, "", item_id, cname, ename,
						user_id, user_type, "1", "1", "1");
				LogRecord.addUserDownLog("1", from_client, user_id, null, item_code, item_id, down_type, ip);
			} else if (channel_id.equals("405")) {
				//写入数据库
				LogRecord.addUserDownLog(ip, item_id, item_code, cname, ename,
						user_id, user_type, "2", "1", "1");
				//写入文本
				LogRecord.addUserDownLog(ip, item_id, item_code, cname, ename,
						user_id, user_type, "2", "1", "1");
				LogRecord.addUserDownLog("2", from_client, user_id, null, item_code, item_id, down_type, ip);
			} else if (channel_id.equals("406")) {
				//写入数据库
				LogRecord.addUserDownLog(ip, "", item_id, cname, ename,
						user_id, user_type, "1", "2", "1");
				//写入文本
				LogRecord.addUserDownLog(ip, "", item_id, cname, ename,
						user_id, user_type, "1", "2", "1");
				LogRecord.addUserDownLog("1", from_client, user_id, null, item_code, item_id, down_type, ip);
			}
		} catch (Exception e) {
			logger.debug("记录下载日志错误！", e);
		}

	}

	private void recordOrgDownLog(String org_id, String type, Double price,
			String down_id, String account_name, String ip, String cname,
			String ename, String image, int ip_acc_id, String account_type) {
		OrgDownLog orgDownLog = new OrgDownLog();
		orgDownLog.setOrg_id(org_id);
		orgDownLog.setType(type);
		orgDownLog.setPrice(price);
		orgDownLog.setDown_id(down_id);
		orgDownLog.setAccount_name(account_name);
		orgDownLog.setIp(ip);
		orgDownLog.setCname(cname);
		orgDownLog.setEname(ename);
		orgDownLog.setImage(image);
		orgDownLog.setIp_acc_id(ip_acc_id);
		orgDownLog.setAccount_type(account_type);
		downloadAuthService.recordOrgDownLog(orgDownLog);

	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	private String getIp(HttpServletRequest request) {
		String ip=request.getHeader("x-forwarded-for");
 		if(ip==null){
			ip=request.getRemoteAddr();
		}
 		String ips[]=ip.split(",");
 		return ips[0];
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
