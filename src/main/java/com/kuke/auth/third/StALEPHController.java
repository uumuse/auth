package com.kuke.auth.third;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kuke.common.utils.ResponseMsg;
import com.kuke.util.HttpClientUtil;

/***
 * 首图ALEPH接口对接
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/kuke/st")
public class StALEPHController {
	private final String user_name = "www-stkkyy";
	private final String user_password = "123456";
//	private final String reader_id = "000120145016";
//	private final String reader_verification = "930625";
	
//	private final String reader_id = "000100181313";
//	private final String reader_verification = "201711";
	
//	private final String reader_id = "000100180033";
//	private final String reader_verification = "123456";
	/***
	 * 首图aleph系统首页
	 * @param request
	 * @param response
	 * @return
	 * 192.168.0.107/kuke/st/stAlephLib
	 */
	@RequestMapping("/stAlephLib")
	public ModelAndView StAlephLib(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("third/st_aleph_login");
	}
	/***
	 * 不是单点登录，需输入首图读者卡号及密码。
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/stALEPH")
	@ResponseBody
	public Object StALEPH(HttpServletRequest request,HttpServletResponse response){
		String reader_id = request.getParameter("id");
		String verification = request.getParameter("verification");
		String library = request.getParameter("library");
		String url = "http://aleph2.clcn.net.cn:8991/X";
		Map<String,String> map = new HashMap<String, String>();
		map.put("OP", "login");
		map.put("user_name", user_name);
		map.put("user_password", user_password);
		map.put("library", "stl50");
		String result = HttpClientUtil.executePost(url, map);
		Document dom = null;
		try {
			dom = DocumentHelper.parseText(result);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root=dom.getRootElement();
		String session_id=root.element("session-id").getText();
		System.out.println("session-id====================>"+session_id);
		if(!session_id.isEmpty()){
			String authUrl = "http://aleph2.clcn.net.cn:8991/X";
			map.clear();
			map.put("OP", "bor-auth_valid");
			map.put("id", reader_id);
			map.put("verification", verification);
			map.put("session",session_id);
			map.put("CON_LNG", "chi");
			String re = HttpClientUtil.executePost(authUrl, map);
			System.out.println("bor-auth_valid==================>"+re);
			if(re.contains("z303-id")){
				return new ResponseMsg(true, "http://auth.kuke.com/kuke/orgUser?orgName=stlib&orgPwd=stlib");
//				return "redirect:http://auth.kuke.com/kuke/orgUser?orgName=stlib&orgPwd=stlib";
			}else{
				return new ResponseMsg(false, "");
//				return "redirect:http://www.kuke.com";
			}
		}
		return "redirect:http://www.kuke.com";
	}

}
