package com.kuke.auth.play.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.play.service.PlayService;
import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.auth.ssologin.service.OrgServiceImpl;
import com.kuke.auth.ssologin.service.UserSSOService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.OrgOauth;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.pay.service.PayBillServiceImpl;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.PlayUtil;

@Controller
@RequestMapping("/kuke/play")
public class PlayController extends BaseController {
	@Autowired
	private PlayService playService;
	@Autowired
	private PayBillServiceImpl payBillService;
	@Autowired
	private OrgServiceImpl orgService;
	@Autowired
	private UserSSOService userSSOService;
	
	/***
	 * web验证是否有权限
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getAuth")
	@ResponseBody
	public Object getAuth(HttpServletRequest request,HttpServletResponse response){
			response.setHeader("Access-Control-Allow-Origin", "*");
			Map<String, String> params = getParameterMap(request);
			String from = dealNull(params.get("from"));//m:移动端,否则网页端
			String type = dealNull(params.get("type"));
			System.out.println("getAuth type:"+type);
			if("".equals(type) || "1,2,3,4".indexOf(type) < 0){
				return new ResponseMsg(false, "4", "4:type必须为1,2,3,4中的一种", "", null);
			}
			Organization org = new Organization();
			try {
				org = OrgOauth.orgLogin(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			com.kuke.auth.login.bean.User user = this.getLoginUser();
			if (user != null&&user.getUid()!=null&&!user.getUid().equals("")) {
				//设置头部信息
				this.getUserInfo(request, response);
				String orgId = user.getOrg_id();
				String uid = user.getUid();
				Map<String,Date> audioMap = playService.getBothAudioDate(uid);
				if(audioMap==null){
					return new ResponseMsg(false, "2", "未购买任何服务", "2:未购买任何服务");
				}
				Date org_audio_date = audioMap.get("org_audio_date");
				Date audio_date = audioMap.get("audio_date");
				Date org_video_date = audioMap.get("org_video_date");
				Date finalDate = new Date();
				//音频
				if(type.equals("1") || type.equals("4")){//音频或乐谱
					if(audio_date!=null&&org_audio_date!=null){
						if(audio_date.after(org_audio_date)){
							finalDate = audio_date;
						}else{
							finalDate = org_audio_date;
						}
					}else if(audio_date==null){
						finalDate = org_audio_date;
					}else{
						finalDate = audio_date;
					}
					
				}else{//视频只有机构有权限观看或直播  2   3
					if(org_video_date != null){
						finalDate = org_video_date;
					}else{
						return new ResponseMsg(false, "3", "无视频播放权限", "3:无视频播放权限");
					}
				}
				
				if(new Date().before(finalDate)){
					return new ResponseMsg(true, "1", "权限在有效期内", "1:权限在有效期内，4：权限已过期");
				}else{
					return new ResponseMsg(false, "4", "权限已过期", "4：权限已过期");
				}
			}else if(org != null&&org.getOrg_id()!=null&&!org.getOrg_id().equals("")){
				List<Map<String,String>> orgDateMap = playService.getOrgDate(org.getOrg_id());
				if(orgDateMap==null){
					return new ResponseMsg(false, "2", "未购买任何服务", "1:已购买服务，2：未购买任何服务");
				}
				Date org_audio_date = new Date();
				Date org_video_date = new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
				java.util.Date date=new java.util.Date();  
				String str=sdf.format(date); 
				String audio = "";
				String video = "";
				for(Map<String,String> map:orgDateMap){
					if(String.valueOf(map.get("channel_id")).equals("1")){
						 audio = String.valueOf(map.get("end_date"));
						 try {
							org_audio_date=sdf.parse(audio);
						} catch (ParseException e) {
							e.printStackTrace();
						}  
					}
					if(String.valueOf(map.get("channel_id")).equals("3")){
						 video = String.valueOf(map.get("end_date"));
						 try {
							org_video_date = sdf.parse(video);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
				Date finalDate = new Date();
				//音频
				if(type.equals("1") || type.equals("4")){//音频或乐谱
					finalDate = org_audio_date;
					
				}else{//视频只有机构有权限观看或直播  2   3
					if(org_video_date != null){
						finalDate = org_video_date;
					}else{
						return new ResponseMsg(false, "3", "无视频播放权限", "3：无视频播放权限");
					}
				}
				
				if(new Date().before(finalDate)){
					return new ResponseMsg(true, "1", "权限在有效期内", "1:权限在有效期内，2：权限已过期");
				}else{
					return new ResponseMsg(false, "2", "权限已过期", "1:权限在有效期内，2：权限已过期");
				}
			}else{
				return new ResponseMsg(false,KuKeAuthConstants.NOMALLOGIN, "用户未登录", "","");
			}
			
	}
	
	/***
	 * 移动端请求播放地址
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getAuthPlayUrl")
	@ResponseBody
	public Object getAuthPlayUrl(HttpServletRequest request,HttpServletResponse response){
			response.setHeader("Access-Control-Allow-Origin", "*");
			Map<String, String> params = getParameterMap(request);
			String from = dealNull(params.get("from"));//m:移动端,否则网页端
			String type = dealNull(params.get("type"));
			System.out.println("getAuthDownloadUrl type:"+type);
			if("".equals(type) || "1,2,3,4".indexOf(type) < 0){
				return new ResponseMsg(false, "4", "4:type必须为1,2,3,4中的一种, , 8:权限已过期或未购买会员服务", "", null);
			}
			Organization org = new Organization() ;
			List<Map<String,String>> orgList = new ArrayList<Map<String,String>>();
			
			//优先判断当前IP所属机构
			try {
				org = OrgOauth.orgLogin(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			User user = null;
			User u = this.getLoginUser();
			if(u != null){
				user = userSSOService.checkUserLoginById(dealNull(u.getUid()));
			}
			
			Map<String,Date> userMap = new HashMap<String,Date>();
			
			if (user != null) {
//				//设置头部信息
//				this.getUserInfo(request, response);
//				String uid = user.getUid();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					userMap.put("audio_date", "".equals(dealNull(user.getAudio_date()))?null:sdf.parse(user.getAudio_date()));
					userMap.put("live_date", "".equals(dealNull(user.getLive_date()))?null:sdf.parse(user.getLive_date()));
					userMap.put("video_date", "".equals(dealNull(user.getVideo_date()))?null:sdf.parse(user.getVideo_date()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
//				userMap = playService.getBothAudioDate(uid);
			}
			else {
				if(from.equals("m")){
					return  new ResponseMsg(false,KuKeAuthConstants.NOMALLOGIN, "用户未登录", "", null);
				}
			}
			
			
			//如果当前IP所属机构为空，则判断用户所属机构
			if(org.getOrg_id() == null&& user != null){
				if((user.getOrg_id()!= null)&&(!user.getOrg_id().equals(""))){
					org = orgService.getOrganizationById(user.getOrg_id());
				}
			}
			if(org != null){
				//1:naxos,2:spoken,3:video,4:live
				orgList = playService.getOrgDate(org.getOrg_id());
				System.out.println(org.getOrg_name()+"========================>"+org.getOrg_id());
			}

			
			Date finalDate = new Date();
			
			Object o  = ClientDateCompare(type,userMap,orgList);
			if(String.valueOf(o).indexOf("flag")>0){
				return (ResponseMsg)o;
			}else{
				finalDate = (Date)o;
			}
				if(new Date().before(finalDate)){
					ResponseMsg resp = getPlayUrl(request, response);
					return resp;
				}else{
					if(org.getOrg_id() == null&& user != null){
						return new ResponseMsg(false, "8", "您所在的用户组无视频观看权限", "1:权限在有效期内，2：权限已过期, 8:权限已过期或未购买会员服务");
					}
					return new ResponseMsg(false, "8", "权限已过期", "1:权限在有效期内，2：权限已过期, 8:权限已过期或未购买会员服务");
				}
//			else{
//				return new ResponseMsg(false,KuKeAuthConstants.NOMALLOGIN, "用户未登录", "1:用户已登录，2：用户未登录","");
//			}
			
	}
	
	
	public Object ClientDateCompare(String type,Map<String,Date> userMap,List<Map<String,String>> orgList){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date org_audio_date = new Date();
		Date org_spoken_date = new Date();
		Date org_video_date = new Date();
		Date org_live_date = new Date();
		
		Date audio_date = new Date();
		Date video_date = new Date();
		Date live_date = new Date();
		
		Date finalDate = new Date();
		//机构下用户
		if(null != userMap && !userMap.isEmpty() && !orgList.isEmpty()){
			
			audio_date = userMap.get("audio_date");
			video_date = userMap.get("video_date");
			live_date = userMap.get("live_date");
			
			for(Map<String,String> li : orgList){
				if(String.valueOf(li.get("channel_id")).equals("1")){
					String audio = String.valueOf(li.get("end_date"));
					 try {
						org_audio_date=sdf.parse(audio);
					} catch (ParseException e) {
						e.printStackTrace();
					}  
				}
				if(String.valueOf(li.get("channel_id")).equals("2")){
					String spoken = String.valueOf(li.get("end_date"));
					 try {
						 org_spoken_date=sdf.parse(spoken);
					} catch (ParseException e) {
						e.printStackTrace();
					}  
				}
				if(String.valueOf(li.get("channel_id")).equals("3")){
					String video = String.valueOf(li.get("end_date"));
					 try {
						 org_video_date=sdf.parse(video);
					} catch (ParseException e) {
						e.printStackTrace();
					}  
				}
				if(String.valueOf(li.get("channel_id")).equals("4")){
					String live = String.valueOf(li.get("end_date"));
					 try {
						 org_live_date = sdf.parse(live);
					} catch (ParseException e) {
						e.printStackTrace();
					}  
				}
			}
			
			
			//音频
			if(type.equals("1") || type.equals("4")){//音频或乐谱
				if(audio_date!=null&&org_audio_date!=null){
					if(audio_date.after(org_audio_date)){
						finalDate = audio_date;
					}else{
						finalDate = org_audio_date;
					}
				}else if(audio_date==null && org_audio_date != null){
					finalDate = org_audio_date;
				}else if(org_audio_date==null && audio_date!=null){
					finalDate = audio_date;
				}else{
					return new ResponseMsg(false, "8", "权限已过期或未购买会员服务", "1:权限在有效期内，2：权限已过期, 8:权限已过期或未购买会员服务");
				}
				
			}else{//只有机构在有效期内有权限观看或直播  type=2,3
				if(type!=null&&type.equals("2")){
					if(org_video_date != null && video_date != null){
						if(video_date.before(org_video_date)){
							finalDate = org_video_date;
						}else{
							finalDate = video_date;
							}
						}
					else if(org_video_date != null && video_date == null){
						finalDate = org_video_date;
					}else if(org_video_date == null && video_date != null){
						finalDate = video_date;
					}else{
						return new ResponseMsg(false, "8", "权限已过期或未购买会员服务", "1:权限在有效期内，2：权限已过期, 8:权限已过期或未购买会员服务");
					}
				}else{
					if(org_live_date != null && live_date != null){
						if(live_date.before(org_live_date)){
							finalDate = org_live_date;
						}else{
							finalDate = live_date;
						}
					}
					else if(org_live_date != null && live_date == null){
						finalDate = org_live_date;
					}else if(org_live_date == null && live_date != null){
						finalDate = live_date;
					}else{
						return new ResponseMsg(false, "8", "权限已过期或未购买会员服务", "1:权限在有效期内，2：权限已过期, 8:权限已过期或未购买会员服务");
					}
				}
				
			}
		}
			//机构用户
			if(!orgList.isEmpty() && (userMap==null || userMap.size()<1)){
				for(Map<String,String> li : orgList){
					if(String.valueOf(li.get("channel_id")).equals("1")){
						try {
							org_audio_date = sdf.parse(String.valueOf(li.get("end_date")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if(String.valueOf(li.get("channel_id")).equals("2")){
						try {
							org_spoken_date = sdf.parse(String.valueOf(li.get("end_date")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if(String.valueOf(li.get("channel_id")).equals("3")){
						try {
							org_video_date = sdf.parse(String.valueOf(li.get("end_date")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if(String.valueOf(li.get("channel_id")).equals("4")){
						try {
							org_live_date = sdf.parse(String.valueOf(li.get("end_date")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
				
				//音频
				if(type.equals("1") || type.equals("4")){//音频或乐谱
					if(org_audio_date!=null){
						finalDate = org_audio_date;
					}else{
						return new ResponseMsg(false, "8", "权限已过期或未购买会员服务", "1:权限在有效期内，2：权限已过期, 8:权限已过期或未购买会员服务");
					}
					
				}else{//只有机构在有效期内有权限观看或直播  type=2:video,3:live
					if(type.equals("2")&&org_video_date != null){
						finalDate = org_video_date;
					}else if(type.equals("3")&&org_live_date != null){
						finalDate = org_live_date;
					}else{
						return new ResponseMsg(false, "8", "权限已过期或未购买会员服务", "1:权限在有效期内，2：权限已过期, 8:权限已过期或未购买会员服务");
					}
				}
			}
			
			//个人用户
			if((orgList==null ||orgList.isEmpty()) && userMap != null&&userMap.size()>0){
				audio_date = userMap.get("audio_date");
				video_date = userMap.get("video_date");
				
				if(type.equals("1") || type.equals("4")){//音频或乐谱
					if(audio_date!=null){
						finalDate = audio_date;
					}else{
						return new ResponseMsg(false, "8", "权限已过期或未购买会员服务", "1:权限在有效期内，2：权限已过期, 8:权限已过期或未购买会员服务");
					}
					
				}else{//只有机构在有效期内有权限观看或直播  type=2,3
					if(video_date != null){
						finalDate = video_date;
					}else{
						return new ResponseMsg(false, "8", "权限已过期或未购买会员服务", "1:权限在有效期内，2：权限已过期, 8:权限已过期或未购买会员服务");
					}
				}
				
			}
			//
			if(orgList == null && userMap==null){
				
				return new ResponseMsg(false,KuKeAuthConstants.NOMALLOGIN, "用户未登录", "1:用户已登录，2：用户未登录","");
			}
			return finalDate;
			
	}
	/***
	 * 请求播放地址
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getPlayUrl")
	@ResponseBody
	public ResponseMsg getPlayUrl(HttpServletRequest request,HttpServletResponse response){
		Map<String,String> map = new HashMap<String, String>();
		/****   type:1:音频，2：video，3:live  ****/
		String type = request.getParameter("type");
		type = type==null?"1":type;
		String code = request.getParameter("code");
		String playUrl = "";
		String from  =  request.getParameter("from");
//		com.kuke.auth.login.bean.User user = this.getLoginUser();
//		Organization org = new Organization();
//		try {
//			org = OrgOauth.orgLogin(request, response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (user != null) {
			if(type.equals("1")){
				String lcode = request.getParameter("lcode");
				Map<String, String> params = new HashMap<String, String>();
				params.put("lcode", lcode);
				String result =  HttpClientUtil.executePost(String.valueOf(PropertiesHolder.getContextProperty("www.url"))+"/kuke/dc/common/music/getTrack", params);
				JSONObject data = JSONObject.fromObject(JSONObject.fromObject(result).get("data"));
				if(!data.isEmpty()){
					String item_code = data.getString("itemcode");
					String labelid = data.getString("labelid");
					
					if(from!=null&&from.equals("m")){
						code = playService.getTrackKbpsForM(lcode, code);
					}else{
						code = playService.getTrackKbps(lcode);
					}
					if(code.equals("0")){
						return new ResponseMsg(false, "4", "无匹配音质", "1:获取成功,2:未传篇章,3:网络错误", map);
					}else{
						playUrl = PlayUtil.getPlayAddressOfAudio(labelid, item_code, lcode, code);
						map.put("url", playUrl);
					}
				}else{
					return new ResponseMsg(false, "3", "网络错误", "1:获取成功,2:未传篇章,3:网络错误", map);
				}
			}else if(type.equals("2")){
				String source_id =request.getParameter("source_id");
				String partNo = request.getParameter("partNo");
				
				code = (code==null||code.equals(""))?"200":code;
				if(partNo == ""||partNo.equals("")){
					map.put("url", "");
					return new ResponseMsg(false, "1", "未传篇章", "1:获取成功,2:未传篇章,3:网络错误", map);
				}
				playUrl = PlayUtil.getPlayAddressOfVideo(source_id, partNo, code);
				map.put("url", playUrl);
			}else{//直播
				String source_id =request.getParameter("source_id");
				String partNo = request.getParameter("partNo");
				code = (code==null||code.equals(""))?"200":code;
				playUrl = PlayUtil.getPlayAddressOfLive(source_id, code);
				map.put("url", playUrl);
			}
			return new ResponseMsg(true, "1", "获取成功", "1:获取成功,2:未传篇章,3:网络错误", map);
//	}
//		return new ResponseMsg(false, "5", "用户未登录", "4:用户已登录，5：用户未登录","");
		
	}
	
	/***
	 * web端专辑下载，返回批量的url,参数：type=1&itemcode=xxx
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getAudioItemPlayUrl")
	@ResponseBody
	public ResponseMsg getAudioItemPlayUrl(HttpServletRequest request,HttpServletResponse response){
		Map<String,String> map = new HashMap<String, String>();
		/****   type:1:音频   ****/
		String type = request.getParameter("type");
		type = type==null?"1":type;
		String code = request.getParameter("code");
		String playUrl = "";
		com.kuke.auth.login.bean.User user = this.getLoginUser();
//		if (user != null) {
			List<String> urlList = new ArrayList<String>();
			if(type.equals("1")){
				String itemcode = request.getParameter("itemcode");
				Map<String,String> params = new HashMap<String,String>();
				params.put("itemcode", itemcode);
				System.out.println(String.valueOf(PropertiesHolder.getContextProperty("www.url"))+"/kuke/dc/common/music/getCataloguebio");
				String trackInfo = HttpClientUtil.executePost(String.valueOf(PropertiesHolder.getContextProperty("www.url"))+"/kuke/dc/common/music/getCataloguebio", params);
				
				JSONObject data = JSONObject.fromObject(trackInfo).getJSONObject("data");
				JSONArray list= JSONArray.fromObject(data.get("trackList"));
				
				for(int i=0;i<list.size();i++){     
					JSONObject jobj =  (JSONObject) list.get(i);
//					Track track =(Track)JSONObject.toBean(jobj,Track.class);
					String labelid = jobj.getString("labelid");
					String lcode = jobj.getString("lcode");		
					code = code==null?"192":"320";
					playUrl = PlayUtil.getPlayAddressOfAudio(labelid, itemcode, lcode, code);
					urlList.add(playUrl);
				}
			}
			return new ResponseMsg(true, "1", "获取成功", "1:获取成功,2:未传篇章,3:网络错误", urlList);
//	}else
//		return new ResponseMsg(false, "2", "用户未登录", "1:用户已登录，2：用户未登录");
		
	}
	
	/***
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ResponseMsg getMusicBookUrl(HttpServletRequest request,HttpServletResponse response){
		String source_id = request.getParameter("itemcode"); 
		if(source_id==null){
			source_id = request.getParameter("source_id"); 
		}
		Map<String,String> book = playService.getMusicBookById(source_id);
		String music_book_file = book.get("music_book_file"); 
		
		Map<String,String> map = new HashMap<String,String>();
		String playUrl = PlayUtil.initMusicBookPlayUrl(music_book_file);
		map.put("url", playUrl);
		return new ResponseMsg(true, "1", "获取成功", "1:获取成功,2:获取失败", map);
		
	}
	
	private String dealNull(String str){
		if(str == null || "".equals(str.trim()) || "null".equals(str.trim())){
			str = "";
		}
		return str.trim();
	}
	
	public String getIp(HttpServletRequest request){
		String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
	}
}
