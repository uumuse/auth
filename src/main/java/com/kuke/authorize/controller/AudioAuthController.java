package com.kuke.authorize.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.OrgOauth;
import com.kuke.auth.util.UserOauth;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.util.MessageFormatUtil;

@Controller
@RequestMapping("/kuke/authorize")
public class AudioAuthController extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(AudioAuthController.class);
	
	/**
	 * 验证音频权限总接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/audio")
	public @ResponseBody ResponseMsg audio(HttpServletRequest request){
		if(getLoginUser() != null){
			String channelId = dealNull(request.getParameter("channelId"));
			try {
				String orgReslut="";
				String userReslut="";
				orgReslut=OrgOauth.orgPlayAuth(request, channelId);
				ResponseMsg orgmsg = new ResponseMsg(orgReslut);
				if(orgmsg.getFlag()){
					return orgmsg;
				}else{
					userReslut=UserOauth.userPlayAudio(request);
					ResponseMsg usermsg = new ResponseMsg(userReslut);
					return usermsg;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return MessageFormatUtil.formatStateToObject(KuKeAuthConstants.FAILED, null);
			}
		}else{
			return MessageFormatUtil.formatStateToObject(KuKeAuthConstants.NOMALLOGIN, null);
		}
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
