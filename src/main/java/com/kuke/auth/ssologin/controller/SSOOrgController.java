package com.kuke.auth.ssologin.controller;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.auth.ssologin.quartz.org.RetrievalOrg;
import com.kuke.auth.ssologin.service.UserSSOService;
import com.kuke.auth.ssologin.util.OrgAuthUtils;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.LogRecord;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.util.MessageFormatUtil;

@Controller
@RequestMapping("/kuke/ssoorg/")
public class SSOOrgController extends BaseController {
	
	@Autowired
	private UserSSOService userSSOService;
	
	private static Logger logger = LoggerFactory.getLogger(SSOOrgController.class);
	
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * <pre>
	 * 创建人: zhangjiran
	 * 创建于: 2015-4-07
	 * 描　述:
	 *    IP校验，初始化SSOID
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/authaddr")
	public @ResponseBody ResponseMsg authaddr(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		Organization organization =new Organization();
		try{
			Map<String, String> params = this.getParameterMap(request);
			String remoteAddrIp = String.valueOf(params.get("i"));//请求IP
			String from_client = String.valueOf(params.get("from_client"));//web,ios,android
			logger.debug("Organization authaddr remoteAddrIp -->"+remoteAddrIp);
			// 校验IP得到机构信息
			organization = RetrievalOrg.getOrgInfoAllowIp(remoteAddrIp);
			logger.debug("Organization organization -->"+JSONObject.fromObject(organization).toString());
			if (KuKeAuthConstants.SUCCESS.equals(organization.getOrg_status())) {
				//写入redis
				OrgAuthUtils.getInstance().OrgLogin(organization,from_client);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			organization.setOrg_status(KuKeAuthConstants.FAILED);
		}
		return MessageFormatUtil.formatStateToObject(organization.getOrg_status(), organization);
	}
	/**
	 * 
	 * 用户名密码验证  初始化SSOID
	 * @param request
	 * @param response
	 * @param map
	 * @return 
	 * @author zhangjiran
	 * @date, 2015-04-07
	 *
	 */
	@RequestMapping(value = "/authuserinfo")
	public @ResponseBody ResponseMsg authuserinfo(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		Map<String, String> params = this.getParameterMap(request);
		String name = String.valueOf(params.get("n"));
		String pwd = String.valueOf(params.get("p"));
		String from_client = String.valueOf(params.get("from_client"));
		//产品编码
		String productCode = params.get("product_code");
		if(from_client==null){
			from_client = "1";
		}
		//根据用户信息得到机构信息
		Organization organization = RetrievalOrg.getOrgInfoByOrgUserId(name, pwd);
		if (KuKeAuthConstants.SUCCESS.equals(organization.getOrg_status())) {
			//写入redis 
			OrgAuthUtils.getInstance().OrgLogin(organization,from_client);
			logger.debug(MessageFormat.format("{0}  {1}  验证机构登录(userinfo) {2}", name,pwd, organization.getOrg_name(), organization.getOrg_status()));
			//记录机构登录日志
			try {
				String remoteAddrIp = String.valueOf(params.get("i"));
				LogRecord.addUserActionLog(productCode, from_client, null, organization.getOrg_id(), "2", remoteAddrIp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return MessageFormatUtil.formatStateToObject(organization.getOrg_status(), organization);
	}
	/**
	 * <pre>
	 * 创建人: zhangjiran
	 * 创建于: 2015-4-7
	 * 描　述:
	 *     校验USER_ID是否可以登录机构 寒暑假, 初始化SSOID
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/authid")
	public @ResponseBody ResponseMsg authid(HttpServletRequest request, ModelMap map) {
		Map<String, String> params = this.getParameterMap(request);
		//用户ID 机构ID
		String user_id = String.valueOf(params.get("u"));
		String org_id = String.valueOf(params.get("o"));
		String from_client = String.valueOf(params.get("from_client"));
		Organization organization = new Organization();
		organization.setOrg_status(KuKeAuthConstants.FAILED);
		String productCode = params.get("product_code");
		String platform = params.get("platform");
		User user=userSSOService.checkUserLoginById(user_id);
		boolean flag=false;
		try {
			Date now_date=new Date();
			Date date=dateFormat.parse(user.getReg_date());
			if(now_date.before(DateUtils.addYears(date,3))){
				flag=true;
			}
		} catch (ParseException e1) {
			flag=false;
		}
		if(flag){
			//寒暑假
			organization = OrgAuthUtils.getInstance().AuthOrgHoliday(user_id, org_id);
			if (KuKeAuthConstants.SUCCESS.equals(organization.getOrg_status())&&flag) {
				OrgAuthUtils.getInstance().OrgLogin(organization,from_client);
				logger.debug(MessageFormat.format("{0}  寒暑假 验证机构登录   {1}", organization.getOrg_name(), organization.getOrg_status()));
				try {
					String remoteAddrIp = String.valueOf(params.get("i"));
					if(from_client==null){
						from_client="web";
					}
					LogRecord.addUserActionLog(productCode, from_client, null, organization.getOrg_id(), "2", remoteAddrIp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return MessageFormatUtil.formatStateToObject(organization.getOrg_status(), organization);
	}
	/**
	 * 
	 * <pre>
	 * 创建人: zhangjiran
	 * 创建于: 2015-4-7
	 * 描　述:
	 *     校验机构票据是否可以登录机构
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/authcookie")
	public @ResponseBody ResponseMsg authcookie(HttpServletRequest request, ModelMap map) {
		Map<String, String> params = this.getParameterMap(request);
		//机构票据
		String ticket = String.valueOf(params.get("ticket"));
		Organization organization = new Organization();
		organization.setOrg_status(KuKeAuthConstants.FAILED);
		//票据中心验证票据
		organization = OrgAuthUtils.getInstance().AuthOrgCookie(ticket);
		if (KuKeAuthConstants.SUCCESS.equals(organization.getOrg_status())) {
			organization.setOrg_ssoid(ticket);
		}
		
		logger.debug(MessageFormat.format("{0}  票据验证机构登录  {1}",organization.getOrg_name(), organization.getOrg_status()));
		return MessageFormatUtil.formatStateToObject(organization.getOrg_status(), organization);
	}
	/**
	 * <pre>
	 * 创建人: zhangjiran
	 * 创建于: 2015-4-7
	 * 描　述:
	 *     执行退出清理票据
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/authout")
	public @ResponseBody ResponseMsg authout(HttpServletRequest request, ModelMap map) {
		Map<String, String> params = this.getParameterMap(request);
		//机构票据
		String ticket = String.valueOf(params.get("ticket"));
		String channel_id = String.valueOf(params.get("c"));
		
		String result = OrgAuthUtils.getInstance().OrgOut(ticket,channel_id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", result);
		return MessageFormatUtil.formatResultToObject(result);
	}
	/**
	 * <pre>
	 * 创建人: zhangjiran
	 * 创建于: 2015-4-7
	 * 描　述:
	 *      机构ID 频道ID SSOID 写入权限内存
	 *      刷新在线时间
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/initonline")
	public @ResponseBody ResponseMsg initonline(HttpServletRequest request, ModelMap map) {
		Map<String, String> params = this.getParameterMap(request);
		//机构票据
		String ticket = String.valueOf(params.get("ticket"));
		String current_channel_id = String.valueOf(params.get("c"));
		logger.debug("initonline======================="+current_channel_id);
		String result = KuKeAuthConstants.FAILED;
		JSONObject jsonObject = new JSONObject();
		try {
			result = OrgAuthUtils.getInstance().OrgOnline(ticket,current_channel_id);
			jsonObject.put("status", result);
		}catch (Exception e) {
			jsonObject.put("status", result);
		}
		return MessageFormatUtil.formatResultToObject(result);
	}
	@RequestMapping(value = "/test")
	public String test(HttpServletRequest request, ModelMap map) {
//		String a = ApplicationOrg.getInstance().getOrgLoginMap();
//		String b = ApplicationOrg.getInstance().getOrgValidMap();
//		String c = ApplicationOrgOnline.getInstance().getOrgOnlineMap();
//		map.put("a", a);
//		map.put("b", b);
//		map.put("c", c);
		return "forward:/sso/jsonResult.jsp";
	}
}
