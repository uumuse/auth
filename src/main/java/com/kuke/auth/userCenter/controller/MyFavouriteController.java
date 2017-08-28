package com.kuke.auth.userCenter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.ssologin.service.UserSSOService;
import com.kuke.auth.userCenter.bean.Theme;
import com.kuke.auth.userCenter.mapper.UserCenterMapper;
import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.MessageFormatUtil;
import com.kuke.util.PageInfo;
import com.kuke.util.StringUtil;

@Controller
@RequestMapping(value = "/kuke/userCenter")
public class MyFavouriteController extends BaseController {
	
	@Autowired
	private UserSSOService userSSOService;
	
	@Autowired
	private UserCenterService userCenterService;
	
	@Autowired
	private UserCenterMapper userCenterMapper;
	
	/**
	 * 用户我的喜欢：单曲
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getFavoriteTrack")
	public Object getFavoriteTrack(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID
			params.put("userId", this.getLoginUser().getUid());
			params.put("flag", "1");
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getFavoriteTrack", params);
			List favoriteTrackList = pageInfo.getResultList();
			int trackCount = userCenterService.getFavoriteTrackCount(this.getLoginUser().getUid());
			pageInfo.setResultCount(trackCount);
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, pageInfo), HttpStatus.OK);
			}else{
				request.setAttribute("favoriteTrackList", favoriteTrackList);//页面信息数量
				request.setAttribute("tracksize", trackCount);
				request.setAttribute("pageInfo", "pageInfo");//分页对象
//				request.setAttribute("tagType", "t");//单曲签类型，分辨发向哪个下拉分页
				return "userCenter/favorite/favorite";
			}
		}else{//未登录的情况
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.NOMALLOGIN, null), HttpStatus.OK);
			}else{
				return KuKeUrlConstants.userLogin_url;
			}
		}
	}
	
	
	/**
	 * 用户我的喜欢：单曲
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getFavoriteTrackAjax")
	@ResponseBody
	public Object getFavoriteTrackAjax(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String currentPage = request.getParameter("currentPage");
		currentPage = (currentPage==null||currentPage.equals(""))?"0":currentPage;
		String pageSize = request.getParameter("pageSize");
		pageSize = (pageSize==null||pageSize.equals(""))?"20":pageSize;
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID
			params.put("userId", this.getLoginUser().getUid());
			params.put("flag", "1");
			
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getFavoriteTrack", params);
			List favoriteTrackList = pageInfo.getResultList();
			int trackCount = userCenterService.getFavoriteTrackCount(this.getLoginUser().getUid());
			pageInfo.setResultCount(trackCount);
			
			String html = "";
			if(!favoriteTrackList.isEmpty()){
				//html += "<div class='comSite241 comWd02'><div class='comSite242 comSite242a comWd01'><div class='comSite250 comSite250a'>";
				int i = (Integer.parseInt(pageSize)*(Integer.parseInt(currentPage)-1))+1;
				for(Map<String,String> map :(List<Map<String,String>>)favoriteTrackList){
					String lcode = String.valueOf(map.get("lcode"));
					String showable = String.valueOf(map.get("showable"));
					if("0".equals(showable)){//下架
						html += "<div class='comSite251 cf cOff01'>"
								+"<span class='comSite249 pr fl'>"
								+"<em class='comSt218 comSt218b' style=\"background:none;\"></em>"
								+"</span>"
								+"<span class='comSt223 comSt223a fl' onclick='cancelShow(\"2\",\""+String.valueOf(map.get("lcode"))+"|"+String.valueOf(map.get("itemcode"))+"\")'></span>"
								+"<span class='comSt224 fl' style=\"width: 20px;\">"+(i)+"</span>"
								+"<span class='comSt225 fl eps' onclick=\"tips('showOffMessage');\">"
								+String.valueOf(map.get("title")==null?"":map.get("title"))+String.valueOf(map.get("trackDesc")==null?"":map.get("trackDesc"))
								+"</span>"
								+"<span class='comSt226 fl pr'>"
								+"<em>"+String.valueOf(map.get("timing"))+"</em>"
								+"<span class=\"isOff isOff01\">已下架</span>"
								+"</span>"
								+"<a href=\"javascript:void(0)\" onclick=\"tips('showOffMessage');\" class='comSt232 comSt232a fl eps'>"+String.valueOf(map.get("ctitle")==null?"":map.get("ctitle"))+"</a>"
								+"</div>";
					}else{
						html += "<div class='comSite251 cf comSite251a'>"
								+"<span class='comSite249 pr fl'>"
								+"<em class='comSt218 comSt218b'></em>"
								+"<input type='checkbox' class='comSt219 comSt219b' name='trackbox' value='"+String.valueOf(map.get("lcode"))+"|"+String.valueOf(map.get("itemcode"))+"'>"
								+"</span>"
								+"<span class='comSt223 comSt223a fl' onclick='cancelShow(\"2\",\""+String.valueOf(map.get("lcode"))+"|"+String.valueOf(map.get("itemcode"))+"\")'></span>"
								+"<span class='comSt224 fl'>"+(i)+"</span>"
								+"<span class='comSt225 fl eps' onclick='window.open(\""+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"/kuke/wwwnew/play/bridge/dispatch?type=track&op=play&lcodes="+lcode+"\", \"mbox-bridge-iframe\");'>"
								+String.valueOf(map.get("title")==null?"":map.get("title"))+String.valueOf(map.get("trackDesc")==null?"":map.get("trackDesc"))
								+"</span>"
								+"<span class='comSt226 fl pr'>"
								+"<em>"+String.valueOf(map.get("timing"))+"</em>"
								+"<span class='comSt227 comSt227a cf'>"
								+"<em class='comSt228 comSt228a' onclick=\"addToPlayList('2')\"></em>"
								+"<em class='comSt229 comSt229a' onclick=\"addToFolder('2','')\"></em>";
								
								String itemcode = String.valueOf(map.get("itemcode"));
								String title = "";
								if(map.get("title")!=null){
									title = String.valueOf(map.get("title")).replace("'", "");
									html += "<em class='comSt230 comSt230a' onclick='shareTrack(\""+lcode+"\","+"\""+itemcode+"\""+",\""+title+"\")'></em>";
								}else{
									title = String.valueOf(map.get("trackDesc")).replace("'", "");
									html += "<em class='comSt230 comSt230a' onclick='shareTrack(\""+lcode+"\","+"\""+itemcode+"\""+",\""+title+"\")'></em>";
								}
								html += 
								"<em class='comSt231 comSt231a' onclick='download(\"1\",\""+lcode+"\",\""+itemcode+"\",\""+title+"\",\""+"\")'></em>"
								+"</span>"
								+"</span>"
								+"<a onclick='window.open(\""+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"/kuke/wwwnew/play/bridge/dispatch?type=track&op=play&lcodes="+lcode+"\", \"mbox-bridge-iframe\")' class='comSt232 comSt232a fl eps'>"+String.valueOf(map.get("ctitle")==null?"":map.get("ctitle"))+"</a>"
								+"</div>";
					}
					i++;
				}
			}
			Map<String,Object> map = new HashMap<String,Object>();
			currentPage = (currentPage==null?"0":currentPage);
			if(Integer.parseInt(currentPage)>1){
				map.put("scrollInfo", html);//分页html对象
				map.put("flag", true);
				if(favoriteTrackList.isEmpty()){
					map.put("more", false);	
				}
			}
			request.setAttribute("favoriteTrackList", favoriteTrackList);//页面信息数量
			request.setAttribute("tracksize", trackCount);
			request.setAttribute("pageInfo", "pageInfo");//分页对象
			
			return map;
		}else{
			//未登录的情况
			return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.NOMALLOGIN, null), HttpStatus.OK);
		}
	}
	/**
	 * 用户我的喜欢：唱片
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getFavoriteCatal")
	public Object getFavoriteCatal(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID
			params.put("userId", this.getLoginUser().getUid());
			params.put("flag", "1");
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getFavoriteCatal", params);
			List favoriteCatalList = pageInfo.getResultList();
			int cataCount = userCenterService.getFavoriteCatalCount(this.getLoginUser().getUid());
			pageInfo.setResultCount(cataCount);
			for(Map<String,Object> map: (List<Map<String,Object>>)favoriteCatalList){
				if(map.get("pv")==null){
					map.put("pv", "");
				}
				if(map.get("playCount")==null){
					map.put("playCount", "");
				}
				if(map.get("ctitle")==null){
					map.put("ctitle", "");
				}
			}
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, pageInfo), HttpStatus.OK);
			}else{
				request.setAttribute("favoriteCatalList", favoriteCatalList);//页面信息数量
				request.setAttribute("catasize", cataCount);
				request.setAttribute("pageInfo", "pageInfo");//分页对象
				String html = "";
				html += "<div class='comSite250 comSite250a'>";
				int i=0;
				String currentPage = request.getParameter("currentPage");
				currentPage = (currentPage==null?"0":currentPage);
				if(Integer.parseInt(currentPage)>1){
					request.setAttribute("scrollInfo", html);//分页html对象
				}
				request.setAttribute("site", "1");
				return "userCenter/favorite/favorite";
			}
		}else{
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.NOMALLOGIN, null), HttpStatus.OK);
			}else{
				return KuKeUrlConstants.userLogin_url;
			}
		}
	}
	
	
	
	
	
	 /* 用户我的喜欢：下拉唱片分页
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getFavoriteCatalAjax")
	@ResponseBody
	public Object getFavoriteCatalAjax(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID
			params.put("userId", this.getLoginUser().getUid());
			//避开pageinfo中查询所有数据的size
			params.put("flag", "1");
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getFavoriteCatal", params);
			List favoriteCatalList = pageInfo.getResultList();
			int cataCount = userCenterService.getFavoriteCatalCount(this.getLoginUser().getUid());
			pageInfo.setResultCount(cataCount);
			
			for(Map<String,Object> map: (List<Map<String,Object>>)favoriteCatalList){
				if(map.get("pv")==null){
					map.put("pv", "");
				}
				if(map.get("playCount")==null){
					map.put("playCount", "");
				}
				if(map.get("ctitle")==null){
					map.put("ctitle", "");
				}
				
			}
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, pageInfo), HttpStatus.OK);
			}else{
				request.setAttribute("favoriteCatalList", favoriteCatalList);//页面信息数量
				request.setAttribute("catasize", favoriteCatalList.size());
				request.setAttribute("pageInfo", "pageInfo");//分页对象
				String html = "";
				html += "<div class='comSite241 comWd02'><div class='comSite242 comSite242a comWd01'><div class='comSite250 comSite250a'>";
				int i=0;
				if(favoriteCatalList.isEmpty()){
					return new HashMap<String,Object>();
				}
				for(Map<String,String> map :(List<Map<String,String>>)favoriteCatalList){
					String itemcode = String.valueOf(map.get("itemcode"));
					String showable = String.valueOf(map.get("showable"));
					if("0".equals(showable)){//下架
						if(i%4==0){
							html += "<ol class='comSite257 comSite257a cf'>";
						}
						html += "<li class='comSite258 comSite258a cOff02'>"
								+"<div class='comSite259 pr'>"
								+"<div><img onerror=\"javascript:this.src='/images/default_zhuanji_s.jpg';\" src='"+String.valueOf(map.get("imgUrl"))+"' alt='' width='210' height='210'></div>"
								+"<span class=\"offBg01\" onclick=\"tips('showOffMessage');\"></span>"
								+"<span class=\"isOff isOff02\">已下架</span>"
								+"<span class='comSt249 comSt249a' onclick =\"cancelShow('1','"+itemcode+"')\"></span>"
								+"</div>"
								+"<div class='comSite261 pr' onclick=\"tips('showOffMessage');\">"
								+"<p>";
						if(map.get("title")!=null){
							if(String.valueOf(map.get("title")).length()>28){
								html += String.valueOf(map.get("title")).substring(0, 27)+"...";
							}else{
								html += String.valueOf(map.get("title"));
							}
						}
						if(map.get("ctitle")!=null){
							if(String.valueOf(map.get("ctitle")).length()>28){
								html += String.valueOf(map.get("ctitle")).substring(0, 27)+"...";
							}else{
								html += String.valueOf(map.get("ctitle"));
							}
						}
						html += "</p>"
								+"</div>"
								+"</li>";
						if(i%4==3){
							html += "</ol>";
						}
					}else{
						if(i%4==0){
							html += "<ol class='comSite257 comSite257a cf'>";
						}
						html += "<li class='comSite258 comSite258a'>"
								+"<div class='comSite259 pr'>"
								+"<div><img src='"+String.valueOf(map.get("imgUrl"))+"' alt='' width='210' height='210'></div>"
								+"<span class='comSt53 comSt53a' onclick=\"window.open('"+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"/album/"+itemcode+"')\"></span>"
								+"<span class='comSt249 comSt249a' onclick =\"cancelShow('1','"+itemcode+"')\"></span>"
								+"<span class='comSite260 comSite260b'><span class='comSite260a pr'><em class='comSt250 comSt250a'></em>"
								+"<input type='checkbox' class='comSt251 comSt251a' name='catabox' value='"+String.valueOf(map.get("itemcode"))+"'></span></span>"
								+"<a href='#' class='comSt252 comSt252a' onclick=\"window.open('"+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"/album/"+itemcode+"')\"></a>"
								+"</div>"
								+"<div class='comSite261 pr'>"
								+"<p>";
						if(map.get("title")!=null){
							if(String.valueOf(map.get("title")).length()>28){
								html += String.valueOf(map.get("title")).substring(0, 27)+"...";
							}else{
								html += String.valueOf(map.get("title"));
							}
						}
						if(map.get("ctitle")!=null){
							if(String.valueOf(map.get("ctitle")).length()>28){
								html += String.valueOf(map.get("ctitle")).substring(0, 27)+"...";
							}else{
								html += String.valueOf(map.get("ctitle"));
							}
						}
						html += "</p>"
								+"<div class='comSite262 comSite262a'>"
								+"<span class='comSt253 comSt253a' onclick=\"window.open('"+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"/album/"+itemcode+"')\"></span>"
								+"<span class='comSt254 comSt254a' onclick=\"addToFolder('1','')\"></span>"
								+"<span class='comSt255 comSt255a'></span>"
								+"<span class='comSt256 comSt256a' onclick =\"cancelFavor(1)\"></span>"
								+"</div>"
								+"</div>"
								+"</li>";
						if(i%4==3){
							html += "</ol>";
						}
					}
					i++;
				}
				html += "</div></div></div>";
				Map<String,Object> map = new HashMap<String,Object>();
				String currentPage = request.getParameter("currentPage");
				currentPage = (currentPage==null?"0":currentPage);
				if(Integer.parseInt(currentPage)>1){
					map.put("scrollInfo", html);//分页html对象
					map.put("flag", true);
					if(favoriteCatalList.isEmpty()){
						map.put("more", false);	
					}
				}
				return map;
			}
		}else
			return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.NOMALLOGIN, null), HttpStatus.OK);
	}
	/**
	 * 用户我的喜欢：视频
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getFavoriteVedio")
	public Object getFavoriteVedio(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String pageSize = request.getParameter("pageSize");
		pageSize = (pageSize==null||pageSize.equals(""))?"18":pageSize;
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID
			params.put("userId", this.getLoginUser().getUid());
			params.put("pageSize", pageSize);
			params.put("flag", "1");
			
//			//设置参数每页显示数量
//			if(!"m".equals(from)){
//				params.put("pageSize", "999999");
//			}
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getFavoriteVedio", params);
			List favoriteVedioList = pageInfo.getResultList();
			
			int videoCount = userCenterService.getFavoriteVedioCount(this.getLoginUser().getUid());
			pageInfo.setResultCount(videoCount);
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, pageInfo), HttpStatus.OK);
			}else{
				request.setAttribute("videosize", videoCount);
				request.setAttribute("favoriteVedioList", favoriteVedioList);//页面信息数量
				request.setAttribute("site", "2");
				request.setAttribute("pageInfo", "pageInfo");//分页对象
				return "/userCenter/favorite/favorite";
			}
		}else{
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.NOMALLOGIN, null), HttpStatus.OK);
			}else{
				return KuKeUrlConstants.userLogin_url;
			}
		}
	}
	
	/**
	 * 用户我的喜欢：视频下拉分页
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getFavoriteVedioAjax")
	@ResponseBody
	public Object getFavoriteVedioAjax(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String currentPage = request.getParameter("currentPage");
//		currentPage = (currentPage==null||currentPage.equals(""))?"0":currentPage;
		String pageSize = request.getParameter("pageSize");
		pageSize = (pageSize==null||pageSize.equals(""))?"18":pageSize;

		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		Map<String,Object> ma = new HashMap<String,Object>();
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID
			params.put("userId", this.getLoginUser().getUid());
			params.put("pageSize", pageSize);
			params.put("flag", "1");
			
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getFavoriteVedio", params);
			String html = "";
			List favoriteVedioList = pageInfo.getResultList();
			int videoCount = userCenterService.getFavoriteVedioCount(this.getLoginUser().getUid());
			pageInfo.setResultCount(videoCount);
			
			int i = 0;
			for(Map<String,String> map:(List<Map<String,String>>)favoriteVedioList){
				String source_id = String.valueOf(map.get("source_id"));
				String isshow = String.valueOf(map.get("isshow"));
				if("0".equals(isshow)){//下架样式
					if(i%3==0){
						html += "<ol class='comSite257 comSite257b cf'>";
					}
						html +="<li class='comSite263 comSite263a'>"+
								"<div class='comSite264 pr'>"+
									"<div>"+"<img src='"+map.get("imgUrl")+"' alt='' width='280' height='160'>"+"</div>"+
									"<span class=\"offBg01 offBg01a\" onclick=\"tips('showOffMessage');\"></span>"+
									"<span class=\"isOff isOff02\">已下架</span>"+
									"<span class='comSt249 comSt249b' onclick=\"cancelShow('3','"+String.valueOf(map.get("source_id"))+"')\">"+"</span>"+
								"</div>"+
								"<div class='comSite265 comSite265a' onclick=\"tips('showOffMessage');\">"+
									"<h2>"+map.get("cataloguename")+"</h2>"+
									"<p>"+map.get("cataloguecname")+"</p>"+
								"</div>"+
							"</li>";
					if(i%3==2){
						html += "</ol>";
					}
				}else{
					if(i%3==0){
						html += "<ol class='comSite257 comSite257b cf'>";
					}
						html +="<li class='comSite263 comSite263a'>"+
								"<div class='comSite264 pr'>"+
									"<div>"+"<img src='"+map.get("imgUrl")+"' alt='' width='280' height='160'>"+"</div>"+
									"<span class='comSt53 comSt53b'>"+"</span>"+
									"<span class='comSt249 comSt249b' onclick=\"cancelShow('3','"+String.valueOf(map.get("source_id"))+"')\">"+"</span>"+
									"<span class='comSite260 comSite260c'>"+
									"<span class='comSite260a pr'>"+
									"<em class='comSt250 comSt250b' name='videobox' class='comSt251 comSt251b' value='${video.source_id }'>"+"</em>"+
									"<input type='checkbox' name='videobox' class='comSt251 comSt251b' value='"+map.get("source_id")+"'>"+
									"</span>"+
									"</span>"+
									"<span class='comSt257 comSt257a' onclick='window.open(\""+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"/video/"+source_id+"\");'>"+"PLAY</span>"+
								"</div>"+
								"<div class='comSite265 comSite265a'>"+
									"<h2>"+map.get("cataloguename")+"</h2>"+
									"<p>"+map.get("cataloguecname")+"</p>"+
								"</div>"+
							"</li>";
					if(i%3==2){
						html += "</ol>";
					}
				}
				i++;
			}
			request.setAttribute("videosize", favoriteVedioList.size());
			request.setAttribute("favoriteVedioList", favoriteVedioList);//页面信息数量
			request.setAttribute("site", "2");
			request.setAttribute("pageInfo", "pageInfo");//分页对象
			
			
			currentPage = (currentPage==null?"0":currentPage);
			if(Integer.parseInt(currentPage)>1){
				ma.put("scrollInfo", html);//分页html对象
				ma.put("flag", true);
				if(favoriteVedioList.isEmpty()){
					ma.put("more", false);	
				}
			}
			return ma;
		}else{
			ma.put("scrollInfo", "");//分页html对象
			ma.put("flag", false);
			return ma;
		}
	}
	/**
	 * 取消喜欢
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/cancleFavourite")
	public @ResponseBody ResponseMsg cancleFavorite(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		boolean flag = false;
		String code = "FAILED";
		String msg = "失败";
		String codeDesc = "SUCCESS:成功;"
						+ "FAILED:失败;"
						+ "NOMALLOGIN:未登录;"
						+ "NOCHOOSE:未选择数据;";
		if (this.getLoginUser() != null) {
			//资源ID
			String from = request.getParameter("from");
			String site =  request.getParameter("site");
			String type = dealNull(request.getParameter("type"));
			String source_id = request.getParameter("source_id");
			String[] source_ids = StringUtil.formatCommaForString(source_id).split(","); 
			if(source_ids != null && source_ids.length > 0){
				if(!"".equals(type)){//type不能为空
					userCenterService.cancleFavorite(this.getLoginUser().getUid(),source_ids,type);
					flag = true;
					code = "SUCCESS";
					msg = "成功";
				}else{
					code = "PARAMERROR";
					msg = "type error:"+type;
				}
			}else{
				code = "NOCHOOSE";
				msg = "未选择数据";
			}
			request.setAttribute("site", site);
		}else{
			code = "NOMALLOGIN";
			msg = "未登录";
		}
		return new ResponseMsg(flag, code, msg, codeDesc);
	}
	/**
	 * 增加喜欢
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addFavourite")
	public @ResponseBody ResponseMsg addFavourite(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		//资源ID,type
		String type = dealNull(request.getParameter("type"));//类型，对于批量资源，type只能是一个
		String ids = dealNull(request.getParameter("ids"));//逗号分隔
		String user_id = "";
		boolean flag = false;
		String code = "FAILED";
		String msg = "添加失败";
		String codeDesc = "";
		if (this.getLoginUser() != null) {
			user_id = this.getLoginUser().getUid();
			try {
				if(!"".equals(ids) && !"".equals(type)){
					String[] id = ids.split(",");
					//得到用户喜欢的资源
					List<String> list = userCenterService.getFavouriteRes(user_id,id);
					//过滤
					StringBuffer idnew = new StringBuffer("");
					for(int i = 0; i < id.length;i++){
						String temp = dealNull(id[i]);
						if(!"".equals(temp)){
							if(!list.contains(temp)){//不包含时，过滤
								idnew.append(",").append(temp);
							}
						}
					}
					//添加过滤后的
					if(!"".equals(dealNull(idnew.toString()))){
						int res = userCenterService.addFavourite(user_id,idnew.toString(),type);
						flag = true;
						code = "SUCCESS";
						msg = "添加成功";
					}else{
						code = "HASADDED";
						msg = "用户已经喜欢要添加的资源";
					}
				}else{
					code = "ERRORCODE";
					msg = "ids,type不能为空";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			code = "NOMALLOGIN";
			msg = "用户未登录";
		}
		return new ResponseMsg(flag, code, msg, codeDesc);
	}
	/**
	 * 检查某一资源是否被喜欢
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkFavourite")
	public @ResponseBody ResponseMsg checkFavourite(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		User user = this.getLoginUser();
		if(user != null){
			String type = "";
			String source_id = dealNull(params.get("source_id"));
			String user_id = dealNull(user.getUid());
			if("".equals(source_id)){
				return new ResponseMsg(false,"1","source_id为空");
			}else{
				String resid = dealNull(userCenterService.getFavourite(user_id,source_id,type));
				if("".equals(resid)){
					return new ResponseMsg(false,"2",source_id+"未被收藏");
				}else{
					return new ResponseMsg(true,"3",source_id+"被收藏");
				}
			}
		}else{
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null);
		}
	}
	/**
	 * 得到用户喜欢的资源
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getUserFavourite")
	public @ResponseBody ResponseMsg getUserFavourite(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		User user = this.getLoginUser();
		if(user != null){
			String type = "";
			String user_id = dealNull(user.getUid());
			List<String> resList = userCenterService.getUserFavourite(user_id,type);
			return new ResponseMsg(true, "1", "成功", "", resList);
		}else{
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null);
		}
	}
	/**
	 * 得到某一批收藏的资源
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getFavouriteRes")
	public @ResponseBody ResponseMsg getFavouriteRes(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		User user = this.getLoginUser();
		boolean flag = false;
		String code = "3";
		String msg = "获取失败";
		String codeDesc = "1:sources异常;"
				+ "2:获取成功;"
				+ "3:获取失败;"
				+ "4:未登录;";
		List<String> list = null;
		if(user != null){
			String type = "";
			String sources = dealNull(params.get("sources"));//逗号分隔
			String user_id = dealNull(user.getUid());
			String[] source = sources.split(",");
			if("".equals(sources) || source == null || source.length == 0){
				code = "1";
				msg = "sources异常";
			}else{
				list = userCenterService.getFavouriteRes(user_id,source);
				flag = true;
				code = "2";
				msg = "获取成功";
			}
		}else{
			code = "NOMALLOGIN";
			msg = "未登录;";
		}
		return new ResponseMsg(flag, code, msg, codeDesc, list);
	}
	/**
	 * 删除下架的专题
	 * @param params
	 * @return
	 */
	private void delFavouriteTheme(Map params){
		String currentPage = (String) params.get("currentPage");
		if("1".equals(currentPage)){
			
			List<String> idsList = new ArrayList<String>();
			params.put("currentPage" , "1");
			params.put("pageSize" , "999999");
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getFavouriteTheme", params);
			List dataList = pageInfo.getResultList();
			
			String mid = "";
			if(!dataList.isEmpty()){
				for(Object id : dataList){
					mid += String.valueOf(id)+",";
				}
			}
			
			if(!"".equals(mid)){
				Map<String,String> param = new HashMap<String,String>();
				param.put("textItemIds", mid);
				String result = HttpClientUtil.executePost(String.valueOf(PropertiesHolder.getContextProperty("www.url"))+"/kuke/dc/common/module/getTextItems", param);
				Object data = JSONObject.fromObject(result).get("data");
				List<String> oldID = new ArrayList<String>();
				if(!data.equals("参数不能为空")){
					JSONArray list= JSONArray.fromObject(data);
					for(int i = 0; i < list.size(); i++){     
						if(!JSONNull.getInstance().equals(list.get(i))){
							JSONObject jobj =  (JSONObject) list.get(i);
							Theme theme =(Theme)JSONObject.toBean(jobj,Theme.class);
							oldID.add(theme.getItemId());
						}
					}
				}
				List<String> delID = new ArrayList<String>();
				for(int i = 0;i < dataList.size(); i++){
					String id = (String) dataList.get(i);
					if(!oldID.contains(id)){
						delID.add(id);
					}
				}
				if(delID.size() > 0){
					System.out.println(params.get("userId")+" delFavouriteTheme delIDs:"+delID.toString());
					userCenterMapper.delFavouriteTheme(params,delID);
				}
			}
		}
	}
	/**
	 * 得到喜欢的专题
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getFavouriteTheme")
	public @ResponseBody Object getFavouriteTheme(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		User user = this.getLoginUser();
		if(user != null){
				params.put("userId", this.getLoginUser().getUid());
				//先删除:第一页时删除
				delFavouriteTheme(params);
				//在查询
				String user_id = dealNull(user.getUid());
				params.put("rss_type", params.get("type"));//作曲家，演奏家，指挥家，乐团
				//网页端不分页
				params.put("currentPage" , params.get("currentPage"));
				params.put("pageSize" , params.get("pageSize"));
				PageInfo pageInfo = getServicePageInfo(userCenterService,"getFavouriteTheme", params);
				List dataList = pageInfo.getResultList();
				
				String mid = "";
				if(!dataList.isEmpty()){
					for(Object id : dataList){
						mid += String.valueOf(id)+",";
					}
				}else{
					return new ResponseMsg(true, "3", "数据为空", "1:查询成功 ;2:查询失败;3.数据为空;", null);
				}
				Map<String,String> param = new HashMap<String,String>();
				param.put("textItemIds", mid);
				String result = HttpClientUtil.executePost(String.valueOf(PropertiesHolder.getContextProperty("www.url"))+"/kuke/dc/common/module/getTextItems", param);
				Object data = JSONObject.fromObject(result).get("data");
				if(!data.equals("参数不能为空")){
					JSONArray list= JSONArray.fromObject(data);
					List<Object> li = new ArrayList<Object>();
					
					for(int i=0;i<list.size();i++){     
						if(!JSONNull.getInstance().equals(list.get(i))){
							JSONObject jobj =  (JSONObject) list.get(i);
							Theme theme =(Theme)JSONObject.toBean(jobj,Theme.class);
							li.add(theme);
						}
					}
					pageInfo.setResultList(li);
				}
				
				if("m".equals(from)){
					if(data!=null&&(!data.equals("参数不能为空"))){
						return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, pageInfo), HttpStatus.OK);
					}else{
						return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.FAILED, pageInfo), HttpStatus.OK);
					}
				}else
					request.setAttribute("pageInfo", pageInfo);
//					request.setAttribute("site", "1");//页面中艺术家
					return "/userCenter/label/favoriteTheme";
				
		}else{
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.NOMALLOGIN, null), HttpStatus.OK);
			}else{
				return KuKeUrlConstants.userLogin_url;
			}
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
