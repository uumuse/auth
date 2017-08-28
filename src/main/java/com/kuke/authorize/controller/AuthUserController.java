package com.kuke.authorize.controller;

import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.authorize.service.UserAuthorizeService;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;

@Controller
@RequestMapping("/kuke/authorize/user")
public class AuthUserController extends BaseController {

	static Logger logger = LoggerFactory.getLogger(AuthUserController.class);

	@Autowired
	private UserAuthorizeService userAuthorizeService;

	/**
	 * 验证下载权限 
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/download")
	public @ResponseBody ResponseMsg download(HttpServletRequest request, ModelMap map) {
		String result = KuKeAuthConstants.FAILED;
		boolean flag = false;
		String code = "";
		String msg = "";
		String codeDesc = "";
		try {
			Map<String, String> params = this.getParameterMap(request);
			// 个人票据
			String ticket = String.valueOf(params.get("ticket"));
			//下载ID
			String download_id = String.valueOf(params.get("download_id"));
			//下载类型
			String download_type = String.valueOf(params.get("download_type"));
			//有效期 秒
			String expires_in = null;
			// 验证票据
			User user = UserAuthUtils.getInstance().AuthUserCookie(ticket,"");
			if (KuKeAuthConstants.SUCCESS.equals(user.getUser_status())) {
				// 票据验证通过
				expires_in = userAuthorizeService.downloadAudioByUserId(user.getUid(),download_id,download_type);
				if (null != expires_in && !"".equals(expires_in)) {
					//下载权限没有过期
					//返回下载地址
					result = KuKeAuthConstants.SUCCESS;
					flag = true;
					code = "SUCCESS";
					msg = "SUCCESS";
				}
			}else{
				result = KuKeAuthConstants.NOMALLOGIN;
				flag = false;
				code = "NOMALLOGIN";
				msg = "NOMALLOGIN";
			}
		} catch (Exception e) {
			flag = false;
			code = "FAILED";
			msg = "FAILED";
		}
		logger.debug(MessageFormat.format("验证个人下载权限  {0}", result));
		return new ResponseMsg(flag, code, msg, codeDesc);
	}
	
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-3-4
	 * 描　述:
	 *     音频权限
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/audio")
	public @ResponseBody ResponseMsg audio(HttpServletRequest request, ModelMap map) {
		String result = KuKeAuthConstants.FAILED;
		boolean flag = false;
		String code = "";
		String msg = "";
		String codeDesc = "NOMALLOGIN：尚未登录;"
						+ "FAILED：失败;"
						+ "SUCCESS：验证成功;";
		try {
			Map<String, String> params = this.getParameterMap(request);
			// 个人票据
			String ticket = String.valueOf(params.get("ticket"));
			//有效期 秒
			String expires_in = null;
			// 验证票据
			User user = UserAuthUtils.getInstance().AuthUserCookie(ticket,"");
			if (KuKeAuthConstants.SUCCESS.equals(user.getUser_status())) {
				// 票据验证通过
				expires_in = userAuthorizeService.playAudioMonthByUserId(user.getUid());
				if (null != expires_in && !"".equals(expires_in)) {
					//播放权限
					//返回播放地址
					result = KuKeAuthConstants.SUCCESS;
					flag = true;
					code = "SUCCESS";
					msg = "SUCCESS";
				}
			}else{
				result = KuKeAuthConstants.NOMALLOGIN;
				flag = false;
				code = "NOMALLOGIN";
				msg = "NOMALLOGIN";
			}
		} catch (Exception e) {
			flag = false;
			code = "FAILED";
			msg = "FAILED";
		}
		logger.debug(MessageFormat.format("验证个人音频播放权限 {0}", result));
		return new ResponseMsg(flag, code, msg, codeDesc);
	}
	
	
	/**
	 * 
	 * <pre>
	 * 创建人: maxin
	 * 创建于: 2013-3-4
	 * 描　述:
	 *     Live权限
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/live")
	public @ResponseBody ResponseMsg live(HttpServletRequest request, ModelMap map) {
		String result = KuKeAuthConstants.FAILED;
		boolean flag = false;
		String code = "";
		String msg = "";
		String codeDesc = "NOMALLOGIN：尚未登录"
						+ "FAILED：失败"
						+ "SUCCESS：验证成功";
		try {
			Map<String, String> params = this.getParameterMap(request);
			// 个人票据
			String ticket = String.valueOf(params.get("ticket"));
			//Live ID
			String live_id = String.valueOf(params.get("live_id"));
			//有效期 秒
			String expires_in = null;
			// 验证票据
			User user = UserAuthUtils.getInstance().AuthUserCookie(ticket,"");
			if (KuKeAuthConstants.SUCCESS.equals(user.getUser_status())) {
				// 票据验证通过
				expires_in = userAuthorizeService.playLiveMonthByUserId(user.getUid());
				if (null != expires_in && !"".equals(expires_in)) {
					//播放权限没有过期
				}else{
					//时段过期
					//单次点播
					expires_in = userAuthorizeService.playLiveByUserId(user.getUid(),live_id);
				}
				if (null != expires_in && !"".equals(expires_in)) {
					//播放权限
					result = KuKeAuthConstants.SUCCESS;
					flag = true;
					code = "SUCCESS";
					msg = "SUCCESS";
				}
			}else{
				result = KuKeAuthConstants.NOMALLOGIN;
				flag = false;
				code = "NOMALLOGIN";
				msg = "NOMALLOGIN";
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			code = "FAILED";
			msg = "FAILED";
		}
		logger.debug(MessageFormat.format("验证个人Live播放权限 {0}", result));
		return new ResponseMsg(flag, code, msg, codeDesc);
	}
	
}
