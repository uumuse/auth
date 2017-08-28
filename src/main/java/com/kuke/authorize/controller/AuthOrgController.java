package com.kuke.authorize.controller;

import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.ssologin.bean.OrgChannel;
import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.auth.ssologin.util.OrgAuthUtils;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;

@Controller
@RequestMapping("/kuke/authorize/org")
public class AuthOrgController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(AuthOrgController.class);

	/**
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-3-4
	 * 描　述:
	 *     音频播放权限
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/play")
	public @ResponseBody ResponseMsg audio(HttpServletRequest request) {
		String result = KuKeAuthConstants.FAILED;
		boolean flag = false;
		String code = "";
		String msg = "";
		String codeDesc = "NOMALLOGIN：尚未登录;"
						+ "CLOSE：机构没有开通频道;"
						+ "OUTDATE：机构频道超过有效期;"
						+ "MAXONLINE：机构频道超过最大在线人数;"
						+ "SUCCESS：验证成功;"
						+ "FAILED：验证失败;";
		try {
			Map<String, String> params = this.getParameterMap(request);
			String ticket = String.valueOf(params.get("ticket"));// 机构票据
			String channelId = String.valueOf(params.get("c"));// 频道
			// 验证票据
			Organization organization = OrgAuthUtils.getInstance().AuthOrgCookie(ticket);
			if (KuKeAuthConstants.SUCCESS.equals(organization.getOrg_status())) {
				//校验是否频道有播放权限
				OrgChannel orgChannel  = OrgAuthUtils.getInstance().AuthOrg(ticket, organization.getOrg_id(), channelId);
				result = orgChannel.getChannelStatus();
				code = result;
				msg = result;
				if("SUCCESS".equals(result)){
					flag = true;
				}
			}else{
				result = KuKeAuthConstants.NOMALLOGIN;
				code = "NOMALLOGIN";
				msg = "NOMALLOGIN";
			}
		} catch (Exception e) {
			e.printStackTrace();
			code = "FAILED";
			msg = "FAILED";
		}
		return new ResponseMsg(flag, code, msg, codeDesc);
	}
}
