package com.kuke.auth.play.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.util.PropertiesHolder;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.util.DateUtil;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.PlayUtil;

@Controller
@RequestMapping("/kuke/share")
public class SharePlayController {
	/***
	 * 单曲分享url，播放期限为分享时间后7天
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getSharePlayUrl")
	@ResponseBody
	public ResponseMsg getSharePlayUrl(HttpServletRequest request,HttpServletResponse response){
//		c=141111111111111111
		String c = request.getParameter("c");
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(Long.parseLong(c)); 
		Date sDate = ca.getTime();
		Date expireDate = DateUtil.getafter7Day(sDate);
		
		if(new Date().before(expireDate)){
			Map<String,String> map = new HashMap<String, String>();
			/****  type:1:音频 ****/
			String type = request.getParameter("type");
			type = type==null?"1":type;
			String code = request.getParameter("code");
			String playUrl = "";
			String lcode = request.getParameter("lcode");
			Map<String, String> params = new HashMap<String, String>();
			params.put("lcode", lcode);
			String result =  HttpClientUtil.executePost(String.valueOf(PropertiesHolder.getContextProperty("www.url"))+"/kuke/dc/common/music/getTrack", params);
			JSONObject data = JSONObject.fromObject(JSONObject.fromObject(result).get("data"));
			if(!data.isEmpty()){
				String item_code = data.getString("itemcode");
				String labelid = data.getString("labelid");
				
				code = code==null?"192":"320";
				playUrl = PlayUtil.getPlayAddressOfAudio(labelid, item_code, lcode, code);
				map.put("url", playUrl);
				return new ResponseMsg(true, "1", "获取成功", "1:获取成功,2:链接已过期,3:网络错误", map);
			}else{
				return new ResponseMsg(false, "3", "网络错误", "1:获取成功,2:链接已过期,3:网络错误", null);
			}
			
		}else
		return new ResponseMsg(false, "2", "链接已过期", "1:获取成功,2:链接已过期,3:网络错误", null);
	}
	
	
}
