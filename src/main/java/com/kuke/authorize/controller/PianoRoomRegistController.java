package com.kuke.authorize.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.authorize.bean.ClientInfo;
import com.kuke.authorize.service.PianoRoomServiceImpl;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.util.MessageFormatUtil;

/***
 * 音乐教室公众号联系方式对接
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/kuke/pianoRoom")
public class PianoRoomRegistController {
	
	@Autowired
	private PianoRoomServiceImpl pianoRoomService;
	/**
	 * 注册音乐教室
	 * @param request
	 * @return
	 */
	@RequestMapping("/regist")
	public @ResponseBody ResponseMsg PianoRoomRegist(HttpServletRequest request){
		JSONObject jsonObject = new JSONObject();
		String name = request.getParameter("name");
		String companyname = request.getParameter("companyname");
		String occupation = request.getParameter("occupation");
		String tel = request.getParameter("tel");
		String qq = request.getParameter("qq");
		String email = request.getParameter("email");
		String profession = request.getParameter("profession");
		
		ClientInfo clientInfo = new ClientInfo();
		clientInfo.setName(name);
		clientInfo.setCompanyname(companyname);
		clientInfo.setOccupation(occupation);
		clientInfo.setTel(tel);
		clientInfo.setQq(qq);
		clientInfo.setEmail(email);
		clientInfo.setProfession(profession);
		try {
			pianoRoomService.addClientInfo(clientInfo);
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
		}
	}
}
