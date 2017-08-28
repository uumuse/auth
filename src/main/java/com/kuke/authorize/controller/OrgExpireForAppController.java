package com.kuke.authorize.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.util.MessageFormatUtil;

@Controller
@RequestMapping(value = "/kuke/app")
public class OrgExpireForAppController {
	
	@Autowired
	private UserCenterService userCenterService;
	/**
	 * 机构过期时间
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value = "/getOrgExpireTime")
	public @ResponseBody ResponseMsg getOrgExpireTime(@RequestParam String orgId){
		Map<String,String> map = new HashMap<String, String>();
		if(orgId == null || "".equals(orgId.trim())){
			return MessageFormatUtil.formatStateToObject(KuKeAuthConstants.FAILED, map);
		}else{
			try {
				map = userCenterService.getOrgExpireTime(orgId);
				return MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, map);
			} catch (Exception e) {
				e.printStackTrace();
				return MessageFormatUtil.formatStateToObject(KuKeAuthConstants.FAILED, map);
			}
		}
	}

}
