package com.kuke.auth.login.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.play.service.PlayService;
import com.kuke.auth.regist.service.impl.RegistService;
import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.redis.RedisUtil;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.ImageUrlUtil;
import com.kuke.util.MD5;

@Controller
@RequestMapping(value = "/kuke/user")
public class UserController extends BaseController {
	
	@Autowired
	private RegistService registService;
	
	@Autowired
	private UserCenterService userCenterService;
	
	@Autowired
	private PlayService PlayService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	/**
	 * 验证邮件绑定链接地址
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/verifyBoundEmail")
	public Object verifyBoundEmail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String email = dealNull(params.get("email"));
		String time = dealNull(params.get("time"));
		String key = dealNull(params.get("key"));
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		String uid = dealNull(params.get("uid"));//uid
		MD5 md5 = new MD5();
		String newKey = md5.getMD5ofStr(new StringBuffer("kukeBoundEmail" + time).reverse().toString()).toUpperCase()
				+ md5.getMD5ofStr(email + "kukeBoundEmail").toUpperCase();
		Long sendTime = Long.parseLong(time);
		Long now = new Date().getTime();
		boolean isTimeOut = (now - sendTime) < 30 * 60 * 1000 ? true : false;
		request.setAttribute("email", email);
		request.setAttribute("time", time);
		request.setAttribute("key", key);
		request.setAttribute("from", from);
		boolean flag = false;
		String msg = "绑定失败";
		if(isTimeOut){
			if (key.equals(newKey)) {
				//看openid信息是否注册过
				int count = userCenterService.checkUserEmail(email);
				if (count == 0) {
					//没有绑定过
					params.put("email", email);
					params.put("id", uid);
					int res = registService.updateEmail(params);
					try{
						//更新到缓存
						String rkey = "userInfo:" + uid;
						Map<String, String> userMap = new HashMap<String, String>();
						userMap.put("email", email);
						this.redisUtil.hashMultipleSet(rkey, userMap);
					}catch (Exception e) {
					}
					flag = true;
					msg = "绑定成功";
				}else{
					msg = "用户已绑定";
				}
			} else {
				msg = "key为通过验证";
				request.setAttribute("result", "errorKey");
			}
		}else{
			msg = "链接已过期";
			request.setAttribute("result", "isTimeOut");
		}
		if(flag){
			return "redirect:/kuke/userCenter/userInf?site=3";
		}else{
			return KuKeUrlConstants.userLogin_url;
		}
	}
	/**
	 * app分享跳转
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/appshare")
	public String appshare(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String l_code = dealNull(params.get("l_code"));//单曲code
		String c = dealNull(params.get("c"));//长整型时间，用于判断时间，7天之内分享的单曲有效
		System.out.println("UserController appshare l_code:"+l_code+" c:"+c);
		String postUrl = KuKeUrlConstants.getTrackUrl;//请求HTTP地址
		List<NameValuePair> navs = new ArrayList<NameValuePair>();
		navs.add(new BasicNameValuePair("lcode", l_code));
		String result = HttpClientUtil.executeServicePOST(postUrl, navs);
		ResponseMsg msg = new ResponseMsg(result);
		JSONObject json = null;
		if(msg.getFlag()){
			json = JSONObject.fromObject(msg.getData());
			//title
			request.setAttribute("title", dealNull(json.get("title").toString())+dealNull(json.get("ctitle").toString())+dealNull(json.getString("trackDesc")));
			//itemcode
			request.setAttribute("item_code", dealNull(json.get("itemcode").toString()));
			//l_code
			request.setAttribute("l_code", dealNull(json.get("lcode").toString()));
			//labelid
			request.setAttribute("labelid", dealNull(json.get("labelid").toString()));
			//图片
			request.setAttribute("image", ImageUrlUtil.getItemCodeImage(dealNull(json.get("itemcode").toString())));
			//时长
			String timing = dealNull(json.get("timing").toString());
			if(timing.length() > 5){
				timing = timing.substring(timing.length()-5, timing.length());
			}
			request.setAttribute("timing", timing);
		}
		request.setAttribute("c", dealNull(c));
		return "share/appshare";
	}
	/**
	 * 分享跳转
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/share")
	public String share(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String l_code = dealNull(params.get("l_code"));//单曲code
		String c = dealNull(params.get("c"));//长整型时间，用于判断时间，7天之内分享的单曲有效
		System.out.println("UserController appshare l_code:"+l_code+" c:"+c);
		String postUrl = KuKeUrlConstants.getTrackUrl;//请求HTTP地址
		List<NameValuePair> navs = new ArrayList<NameValuePair>();
		navs.add(new BasicNameValuePair("lcode", l_code));
		String result = HttpClientUtil.executeServicePOST(postUrl, navs);
		ResponseMsg msg = new ResponseMsg(result);
		JSONObject json = null;
		if(msg.getFlag()){
			json = JSONObject.fromObject(msg.getData());
			//title
			request.setAttribute("title", dealNull(json.get("title").toString())+dealNull(json.get("ctitle").toString())+dealNull(json.getString("trackDesc")));
			//itemcode
			request.setAttribute("item_code", dealNull(json.get("itemcode").toString()));
			//l_code
			request.setAttribute("l_code", dealNull(json.get("lcode").toString()));
			//trackDesc
			request.setAttribute("trackDesc", dealNull(json.get("trackDesc").toString()));
			//labelid
			request.setAttribute("labelid", dealNull(json.get("labelid").toString()));
			//图片
			request.setAttribute("image", ImageUrlUtil.getItemCodeImage(dealNull(json.get("itemcode").toString())));
			//时长
			String timing = dealNull(json.get("timing").toString());
			if(timing.length() > 5){
				timing = timing.substring(timing.length()-5, timing.length());
			}
			request.setAttribute("timing", timing);
		}
		request.setAttribute("c", dealNull(c));
		return "share/share";
	}
	/**
	 * 页面底部跳转
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/copyRight")
	public String copyRight(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String to = dealNull(request.getParameter("to"));
		if("".equals(to)){
			return null;
		}else{
			return "common/footer/"+to;
		}
	}
	/**
	 * 查询单曲是否有320品质
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getTrackKbps")
	public @ResponseBody ResponseMsg getTrackKbps(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String code = "";
			String lcode = request.getParameter("lcode");
			String flag = PlayService.getTrackKbps(lcode);
			if(flag.equals("320")){//有320的
				code = "320";
				return new ResponseMsg(true, "",code);
			}else if(flag.equals("192")){
				code = "192";
				return new ResponseMsg(true, "",code);
			}else{
				return new ResponseMsg(false, "",code);
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
