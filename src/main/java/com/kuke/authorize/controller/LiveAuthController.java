package com.kuke.authorize.controller;

import java.text.MessageFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.ssologin.bean.OrgChannel;
import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.auth.ssologin.util.OrgAuthUtils;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.authorize.service.UserAuthorizeService;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.service.SpringContextHolder;

@Controller
@RequestMapping("/kuke/authorize")
public class LiveAuthController extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(LiveAuthController.class);
	
	@Autowired
	private UserAuthorizeService userAuthorizeService;
	
	/**
	 * 直播权限
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/live")
	public @ResponseBody ResponseMsg live(HttpServletRequest request, ModelMap map) {
		String item_id = request.getParameter("item_id");// LIVE_ID
		String retStr = "";//返回参数
		String orgToken = request.getParameter("orgToken");//ICookie.get(request, KuKeAuthConstants.SSO_ORG_COOKIE_NAME);//机构Token
		String userToken = request.getParameter("userToken");//ICookie.get(request, KuKeAuthConstants.SSO_USER_COOKIE_NAME);//个人Token
		Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);//本地国际化
		logger.debug("orgToken=========================="+orgToken);
		logger.debug("userToken========================="+userToken);
		boolean flag = false;
		String code = "";
		String msg = "";
		String codeDesc = "CLOSE:尚未开通此项服务;"
						+ "OUTDATE:服务已到期;"
						+ "MAXONLINE:已达到登录最大上限数;"
						+ "SUCCESS:播放;"
						+ "NOMALLOGIN:尚未登录"
						+ "FAILED:失败"
						+ "NOBUY:未购买";
		if (null != orgToken && !"".equals(orgToken)) {
			Organization org = OrgAuthUtils.getInstance().AuthOrgCookie(orgToken);
			if (org.getOrg_status().equals(KuKeAuthConstants.SUCCESS)) {
				//机构登录用户
				OrgChannel  orgChannel =  OrgAuthUtils.getInstance().AuthOrg(org.getOrg_ssoid(), org.getOrg_id(), KuKeAuthConstants.LIVE);
				logger.debug("orgorgorgorgorgorgorg===================="+orgChannel.getChannelStatus());
				//机构播放权限
				if(KuKeAuthConstants.CLOSE.equals(orgChannel.getChannelStatus())){
					//尚未开通此项服务,请联系客服!
					flag = false;
					code = "CLOSE";
					msg = "CLOSE";
					retStr = "message|"+ this.getLocaleMessage("noBuyChannle",null,locale);
				}else if(KuKeAuthConstants.OUTDATE.equals(orgChannel.getChannelStatus())){
					//服务已到期,请联系客服!
					flag = false;
					code = "OUTDATE";
					msg = "OUTDATE";
					retStr = "message|"+ this.getLocaleMessage("noValidFlag",null,locale);
				}else if(KuKeAuthConstants.MAXONLINE.equals(orgChannel.getChannelStatus())){
					// 已达到登录最大上限数,请稍后再试!
					flag = false;
					code = "MAXONLINE";
					msg = "MAXONLINE";
					retStr = "message|"+ this.getLocaleMessage("overMaxFlag",null,locale);
				}else{
					flag = true;
					code = "SUCCESS";
					msg = "SUCCESS";
					retStr = "true|";
				}
			}
		}
		if (retStr.indexOf("message") > -1 || retStr == "") {
			if (null != userToken && !"".equals(userToken)) {
				User user = UserAuthUtils.getInstance().AuthUserCookie(userToken,"");
				if (user.getUser_status().equals(KuKeAuthConstants.SUCCESS)) {
					// 有效期 秒
					String expires_in = null;
					// 票据验证通过
					expires_in = userAuthorizeService.playLiveMonthByUserId(user.getUid());
					if (null == expires_in || "".equals(expires_in)) {
						// 时段过期 单次点播
						expires_in = userAuthorizeService.playLiveVideoByUserId(user.getUid(),item_id);
					}
					if (null != expires_in && !"".equals(expires_in)) {
						// 播放权限
						flag = true;
						code = "SUCCESS";
						msg = "SUCCESS";
						retStr = "true|";
					}else{
						//尚未购买
						flag = false;
						code = "NOBUY";
						msg = "NOBUY";
						retStr = "true|buy|未购买";
					}
				} 
			} 
		}
		if(retStr==""){
			// 用户失效
			flag = false;
			code = "NOMALLOGIN";
			msg = "NOMALLOGIN";
			retStr = "nomalLogin|未登录";
		}
		if("".equals(code)){
			flag = false;
			code = "FAILED";
			msg = "FAILED";
		}
		logger.debug(MessageFormat.format("{0}  验证LIVE播放权限", retStr));
		return new ResponseMsg(flag, code, msg, codeDesc);
	}
	/**
	 * 本地化
	 */
	private String getLocaleMessage(String key, Object[] params, Locale locale) {
		return SpringContextHolder.getApplicationContext().getMessage(key,params, locale);
	}
	
	/**
	 * String转换为xml
	 */
	public String XMLString(String str) {
		String XMLString = "";
		if (null != str && !"".equals(str)) {
			String strArray[] = str.split("\\|");
			if (strArray[0].equals("nomalLogin")) {
				// 没有登录
				XMLString += "<info type=\"" + strArray[0] + "\" title=\""+ escapeHTML(strArray[1]) + "\" />";
			} else if (strArray[0].equals("message")) {
				if (null != strArray[1] && strArray[1].equals("payment")) {
					// 去充值系统
					XMLString += "<info type=\"" + strArray[1] + "\" title=\""+ escapeHTML(strArray[2]) + "\" />";
				} else {
					// 错误提示信息
					XMLString += "<info type=\"" + strArray[0] + "\" title=\""+ escapeHTML(strArray[1]) + "\"/>";
				}
			} else if (strArray[0].equals("true")) {
				if (strArray.length > 1 && strArray[1].equals("buy")) {
					// 确定购买
					XMLString += "<info type=\"" + strArray[1] + "\" title=\""+ escapeHTML(strArray[2]) + "\" />";
				} else {
					//可以播放
					XMLString += "<info type=\"" + strArray[0] + "\"/>";
				}
			}
		}
		XMLString += "";
		return XMLString;
	}
	/**
	 * 
	 * @param s
	 * @return
	 */
	private static String escapeHTML(String s) {
		if (s != null && !"".equals(s)) {
			s = s.replaceAll("&", "&amp;");
			s = s.replaceAll("<", "&lt;");
			s = s.replaceAll(">", "&gt;");
			s = s.replaceAll("\"", "&quot;");
			s = s.replaceAll("'", "&apos;");
		} else {
			s = "";
		}
		return s;
	}
}
