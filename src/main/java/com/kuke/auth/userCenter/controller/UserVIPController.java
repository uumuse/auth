package com.kuke.auth.userCenter.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.userCenter.bean.UserAuthorize;
import com.kuke.auth.userCenter.mapper.UserCenterMapper;
import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.util.MessageFormatUtil;

@Controller
@RequestMapping(value = "/kuke/userCenter")
public class UserVIPController extends BaseController {
	
	@Autowired
	private UserCenterService userCenterService;
	
	@Autowired
	private UserCenterMapper userCenterMapper;
	/**
	 * 会员中心
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/vipCenter")
	public String vipCenter(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		//设置个人信息
		this.getUserInfo(request, response);
		User user = this.getLoginUser();
		Map<String, String> data = null;
		boolean userFlag = false;
		if(user != null){//用户登录时
			userFlag = true;
			params.put("userId", this.getLoginUser().getUid());
			data = userCenterService.getUserAuthorize(params);
			if(data != null){
				if("inDate".equals(dealNull(data.get("audio_flag")))){
					request.setAttribute("userFlagMessage", "会员服务到期时间为：");
				}else{
					request.setAttribute("userFlagMessage", "会员服务已过期：");
				}
				request.setAttribute("dateFlag", true);
			}else{
				request.setAttribute("dateFlag", false);
			}
			
			//记录日志
			LogUtil.addVisitLog(request);
			
		}
		request.setAttribute("data", data);
		request.setAttribute("userFlag", userFlag);
		return "/vipCenter/vipCenter";
	}
	/**
	 * 会员中心
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userVIPInfo")
	public Object userVIPInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		com.kuke.auth.login.bean.User user = this.getLoginUser();
		if (user != null) {
			params.put("userId", user.getUid());
			UserAuthorize userAuthorize = userCenterMapper.getUserAuthorize(params);
			userAuthorize = (userAuthorize == null) ? new UserAuthorize() : userAuthorize;
			Date nowDate = new Date();
			if (userAuthorize.getAudio_date() != null && (userAuthorize.getAudio_date().getTime() > nowDate.getTime())) {
				userAuthorize.setAudio_flag("inDate");
			} else if (userAuthorize.getAudio_date() != null && (userAuthorize.getAudio_date().getTime() < nowDate.getTime())) {
				userAuthorize.setAudio_flag("outDate");
			}
			if (userAuthorize.getLive_date() != null && (userAuthorize.getLive_date().getTime() > nowDate.getTime())) {
				userAuthorize.setLive_flag("inDate");
			} else if (userAuthorize.getLive_date() != null && (userAuthorize.getLive_date().getTime() < nowDate.getTime())) {
				userAuthorize.setLive_flag("outDate");
			}
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			if("m".equals(from)){
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("user", user);
				result.put("userAuthorize", userAuthorize);
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, result), HttpStatus.OK);
			}else{
				request.setAttribute("user", user);
				request.setAttribute("userAuthorize", userAuthorize);
				return "/userCenter/vipCenter/userVIPInfo";
			}
		}else{
			if("m".equals(from)){
				Map<String, Object> result = new HashMap<String, Object>();
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.FAILED, result), HttpStatus.OK);
			}else{
				return KuKeUrlConstants.userLogin_url;
			}
		}
	}
	/**
	 * 开通会员页，显示产品及产品价格(续期)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/vipProInfo")
	public Object vipProInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		com.kuke.auth.login.bean.User user = this.getLoginUser();
		if (user != null) {
			//个人信息
			this.getUserInfo(request, response);
			
			boolean flag = false;
			if(user.getOrg_id() != null && !"".equals(user.getOrg_id())){
				flag = true;
			}
			params.put("flag", flag?"true":"false");
			List<List<Map<String, String>>> proInfo = userCenterService.getVIPProInfo(params);
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			if("m".equals(from)){
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("audioPro", proInfo.get(0));
				if(flag){
					result.put("videoPro", proInfo.get(1));
				}
				result.put("livePro", proInfo.get(2));
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, result), HttpStatus.OK);
			}else{
				request.setAttribute("audioPro", conmpareList(proInfo.get(0)));
				if(flag){
					request.setAttribute("videoPro", proInfo.get(1));
				}
				request.setAttribute("livePro", proInfo.get(2));
				return "/vipCenter/openVip";
			}
		}else{
			if("m".equals(from)){
				Map<String, Object> result = new HashMap<String, Object>();
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.FAILED, result), HttpStatus.OK);
			}else{
				return KuKeUrlConstants.userLogin_url;
			}
		}
	}
	/**
	 * 从小到大排序  map
	 * @param map
	 * @return
	 */
	public static List<Map<String, String>> conmpareList(List<Map<String, String>> data){
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		List<Integer> comList = new ArrayList<Integer>();
		Map<Integer, String> maptemp = new HashMap<Integer, String>();
		for (int i = 0; i < data.size(); i++) {
			Map<String, String> map = data.get(i);
			comList.add(Integer.parseInt(map.get("month")));
			maptemp.put(Integer.parseInt(map.get("month")), map.get("id")+"|"+map.get("price"));
		}
		Collections.sort(comList);
		for(int i = 0;i < comList.size();i++){
			int key = comList.get(i);
			Map<String, String> m = new HashMap<String, String>();
			m.put("month", String.valueOf(key));
			m.put("id", maptemp.get(key).split("\\|")[0]);
			m.put("price", maptemp.get(key).split("\\|")[1]);
			list.add(m);
		}
		return list;
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
