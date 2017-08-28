package com.kuke.auth.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.auth.ssologin.service.OrgService;
import com.kuke.auth.ssologin.service.OrgServiceImpl;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.OrgOauth;
import com.kuke.core.base.BaseController;
import com.kuke.core.service.SpringContextHolder;

@Controller
@RequestMapping("/kuke")
public class OrgAuthController extends BaseController {
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/orgUser")
	public String orgUser(HttpServletRequest request,HttpServletResponse response){
		Organization s = null ;
		try {
			s=OrgOauth.orgLogin(request, response);
			System.out.println("orgUser s:"+s.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		OrgService orgService = SpringContextHolder.getBean(OrgServiceImpl.class);
		if(s != null){
			String to_wap = orgService.getOrganizationToWapFlagById(s.getOrg_id());
			if(to_wap!=null&&to_wap.equals("1")){
				System.out.println("wap判断==========================================>"+KuKeUrlConstants.userLogin_url+"?to_wap="+to_wap);
				return KuKeUrlConstants.userLogin_url+"?to_wap="+to_wap;
			}
			return KuKeUrlConstants.userLogin_url;
		}else
			return KuKeUrlConstants.userLogin_url;
	}
}
