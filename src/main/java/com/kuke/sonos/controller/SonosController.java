package com.kuke.sonos.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.play.service.PlayService;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.util.IdGenerator;
import com.kuke.sonos.service.SonosService;
import com.kuke.util.PlayUtil;

@Controller
@RequestMapping("/kuke/sonos")
public class SonosController {
	@Autowired
	private PlayService playService;
	
	@Autowired
	private SonosService sonosService;
//	String url = PlayUtil.getPlayAddressOfAudio(labelid, itemcode, lcode, code);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	private String version = IdGenerator.getUUIDHex32();
	/***
	 * 保存客户端播放列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/musicqueue/v1.0/trackList")
	public @ResponseBody ResponseMsg trackList(HttpServletRequest request , HttpServletResponse response){
		String uid = request.getParameter("uid");
		String serviceid = request.getParameter("serviceId");
		String playlist = request.getParameter("playlist");
		String[] tmp = playlist.split(";");
		List<String> list = Arrays.asList(tmp);
		int count = sonosService.getPlayList(uid,serviceid,0,1).size();
		if(count > 0){
			sonosService.deletePlayList(uid);
		}
		try {
			sonosService.addPlayList(uid, serviceid,list);
			return new ResponseMsg(true, "列表上传成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseMsg(false, "列表上传出错");
		}
	}
	
	/***
	 * 上下文
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/musicqueue/v1.0/context",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> context(HttpServletRequest request , HttpServletResponse response){
		String uid = request.getParameter("uid");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("contextVersion", "kuke_context_version_"+sdf.format(new Date()));
		map.put("queueVersion", "v_"+sdf.format(new Date()));
		
		Map<String,Object> policymap = new HashMap<String,Object>();
		policymap.put("canSkip", true);
		policymap.put("limitedSkips", true);
		policymap.put("canSkipToItem", false);
		policymap.put("canSkipBack", false);
		policymap.put("canSeek", false);
		policymap.put("canCrossfade", true);
		policymap.put("showNNextTracks", 3);
		policymap.put("showNPreviousTracks", 0);
		map.put("playbackPolicies", policymap);
		
		Map<String,Object> containermap = new HashMap<String,Object>();
		policymap.put("type", "tracklist");
		policymap.put("name", "kuke");
			Map<String,Object> container_id_map = new HashMap<String,Object>();
			container_id_map.put("serviceId", uid);///?
			container_id_map.put("objectId", uid);///?
			container_id_map.put("accountId", uid);///?
		map.put("id", container_id_map);
			Map<String,Object> service_map = new HashMap<String,Object>();
			service_map.put("name", "kuke");
		map.put("service", service_map);
			Map<String,Object> report_map = new HashMap<String,Object>();
			report_map.put("sendUpdateAfterMillis", 30000);
			map.put("reports", report_map);
		return map;
	}
	
	/***
	 * 详情
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/musicqueue/v1.0/itemWindow",method = RequestMethod.GET,produces="application/json;charset=UTF-8")
	public Map<String,Object> itemWindow(HttpServletRequest request , HttpServletResponse response){
		String queueVersion = request.getParameter("queueVersion");
		String[] ss = queueVersion.split(",");
		String itemId = request.getParameter("itemId");
		
		Map<String,Object> map = new HashMap<String,Object>();
//		 https://www.example.com/musicqueue/v2.0/itemWindow?itemId=123456&previousWindowSize=5&upcomingWindowSize=20
//		&queueVersion=asdf3cbjal235jazz
		String uid = ss[0];
		String serviceId = ss[1];
		
		String previousWindowSize = request.getParameter("previousWindowSize");
		String upcomingWindowSize = request.getParameter("upcomingWindowSize");
			
		map = sonosService.getItemWindow(serviceId, uid, queueVersion, itemId, previousWindowSize, upcomingWindowSize);
		return map;
	}
	
	/***
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="musicqueue/v1.0/timePlayed",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> timePlayed(HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Content-Type", "application/json");  
	    response.setStatus(200); 
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("success", "success");
		return map; 
	}
	
	
	@RequestMapping(value="musicqueue/v1.0/version",method = RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, String> version(HttpServletRequest request , HttpServletResponse response){
		response.setHeader("Content-Type", "application/json");  
	       //content-type为application/x-json;charset=UTF-8  
	    response.setStatus(200);
	    Map<String, String> map = new HashMap<String, String>();
	    
	    map.put("queueVersion", version);
		return map; 
	}
	
	/***
	 * 获取播放地址
	 * @param labelid
	 * @param item_code
	 * @param l_code
	 * @return
	 */
	@RequestMapping("musicqueue/v1.0/getPlayUrl")
	public @ResponseBody String getPlayUrl(@RequestParam String labelid,@RequestParam String item_code,@RequestParam String l_code){
		String code = playService.getTrackKbps(l_code);
		String url = PlayUtil.getPlayAddressOfAudio(labelid, item_code, l_code, code);
		return url;
	}
}
