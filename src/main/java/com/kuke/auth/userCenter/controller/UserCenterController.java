package com.kuke.auth.userCenter.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.regist.domain.User;
import com.kuke.auth.regist.service.impl.RegistService;
import com.kuke.auth.ssologin.service.UserSSOService;
import com.kuke.auth.userCenter.bean.Artist;
import com.kuke.auth.userCenter.mapper.UserCenterMapper;
import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.auth.util.UserOauth;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.redis.RedisUtil;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.ImageUrlUtil;
import com.kuke.util.MessageFormatUtil;
import com.kuke.util.PageInfo;
import com.kuke.util.StringUtil;

@Controller
@RequestMapping(value = "/kuke/userCenter")
public class UserCenterController extends BaseController {

	@Autowired
	private RegistService registService;

	@Autowired
	private UserCenterService userCenterService;
	
	@Autowired
	private UserSSOService userSSOService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private UserCenterMapper userCenterMapper;
	
	public static String SubscribeLabelImgurl = String.valueOf(PropertiesHolder.getContextProperty("SubscribeLabelImgurl"));
	/**
	 * 用户我的订阅:厂牌
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getSubscribeLabel")
	public Object getSubscribeLabel(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID
			params.put("userId", this.getLoginUser().getUid());
			//pageSize：每页数量
			String pageSize = KuKeAuthConstants.LABLESIZE;
			if(!"m".equals(from)){
				params.put("pageSize" , pageSize);
			}
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getSubscribeLabel", params);
			List subscribeLabelList = pageInfo.getResultList();
			//厂牌封面
			List list = getSubscribeLableIMG(subscribeLabelList);
			pageInfo.setResultList(list);
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, pageInfo), HttpStatus.OK);
			}else{
				request.setAttribute("subscribeLabelList", list);//页面信息数量
				request.setAttribute("count", pageInfo.getResultCount());//页面信息数量
				request.setAttribute("site", "0");//页面中厂牌
				return "userCenter/label/label";
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
	 * 用户我的订阅:厂牌ajax
	 * 网页端
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getSubscribeLabelAjax")
	public @ResponseBody ResponseMsg getSubscribeLabelAjax(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		if (this.getLoginUser() != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			//设置参数当前登录用户ID
			params.put("userId", this.getLoginUser().getUid());
			//pageSize：每页数量
			String pageSize = KuKeAuthConstants.LABLESIZE;
			params.put("pageSize" , pageSize);
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getSubscribeLabel", params);
			List subscribeLabelList = pageInfo.getResultList();
			//厂牌封面
			List list = getSubscribeLableIMG(subscribeLabelList);
			
			//要拼接的html信息:
			String scrollInfo = getLableHtml(list, "");
			boolean more = true;
			//list数量如果小于煤业数量，则没有更多了
			if(list.size() < Integer.parseInt(pageSize)){
				more = false;
			}
			map.put("scrollInfo", scrollInfo);
			map.put("more", more);
			return new ResponseMsg(true, "1", "查询成功","1：查询成功", map); 
			
		}else{
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null);
		}
	}
	/**
	 * 删除下架的艺术家
	 * @param params
	 * @return
	 */
	private void delFavouriteArtist(Map params){
		String currentPage = StringUtil.dealNull((String)params.get("currentPage"));
		if("1".equals(currentPage) || "".equals(currentPage)){
			
			List subscribeArtistList =userCenterMapper.getSubscribeArtist(params, 0, 999999);
			
			String mid = "";//所有的艺术家资源ID
			Map<String,String> param = new HashMap<String,String>();
			for(int i=0;i<subscribeArtistList.size();i++){
				mid += String.valueOf(((Map)subscribeArtistList.get(i)).get("rss_source_id"))+",";
			}
			param.put("musicianIds", StringUtil.formatCommaForString(mid));
			System.out.println("musicianIds:"+mid);
			if(!"".equals(dealNull(mid))){//mid参数为空,即艺术家列表为空
				String result = HttpClientUtil.executePost(KuKeUrlConstants.getMusicians, param);
				//艺术家封面
				ResponseMsg msg = new ResponseMsg(result);
				Object data = msg.getData();
				if(msg.getFlag() && data!=null){//true  并且   data不为空
					JSONArray list = JSONArray.fromObject(data);
					List<String> hasID = new ArrayList<String>();
					
					for(int i=0;i<list.size();i++){     
						JSONObject jobj =  (JSONObject) list.get(i);
						Artist artist =(Artist)JSONObject.toBean(jobj,Artist.class);
						if(artist != null){
							hasID.add(artist.getMusicianId());
						}
					}
					//要删除的ID
					String delid = "";
					for(int i = 0; i < mid.split(",").length;i++){
						String id = StringUtil.dealNull(mid.split(",")[i]);
						if(!hasID.contains(id)){
							delid = "," + id;
						}
					}
					delid = StringUtil.formatCommaForString(delid);
					
					if(!"".equals(delid)){
						userCenterMapper.cancleSubscribe((String)params.get("userId"),delid.split(","),"71");
					}
					
				}
			}
		}
	}
	/**
	 * 用户我的订阅:艺术家
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getSubscribeArtist")
	public Object getSubscribeArtist(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID
			params.put("userId", this.getLoginUser().getUid());
			//先删除
			delFavouriteArtist(params);
			//pageSize：每页数量
			String pageSize = KuKeAuthConstants.ARTISTSIZE;
			if(!"m".equals(from)){
				params.put("pageSize" , pageSize);
			}
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getSubscribeArtist", params);
			List subscribeArtistList = pageInfo.getResultList();
			
			String mid = "";//所有的艺术家资源ID
			Map<String,String> param = new HashMap<String,String>();
			for(int i=0;i<subscribeArtistList.size();i++){
				mid += String.valueOf(((Map)subscribeArtistList.get(i)).get("rss_source_id"))+",";
			}
			param.put("musicianIds", StringUtil.formatCommaForString(mid));
			if(!"".equals(dealNull(mid))){//mid参数为空,即艺术家列表为空
				String result = HttpClientUtil.executePost(KuKeUrlConstants.getMusicians, param);
				//艺术家封面
				ResponseMsg msg = new ResponseMsg(result);
				Object data = msg.getData();
				if(msg.getFlag() && data!=null){//true  并且   data不为空
					JSONArray list = JSONArray.fromObject(data);
					List<Object> li = new ArrayList<Object>();
					
					for(int i=0;i<list.size();i++){     
						JSONObject jobj =  (JSONObject) list.get(i);
						Artist artist =(Artist)JSONObject.toBean(jobj,Artist.class);
						//封面:
						artist.setMusicianImg(ImageUrlUtil.getArtistImg(artist.getMusicianId()));
						String mtype = artist.getMusicianType();
//						String mType ="";
//						if(mtype!=null){
//							mType = mtype[0];
//						}
						artist.setmType(mtype);
						li.add(artist);
					}
					pageInfo.setResultList(li);
				}else{
					pageInfo.setResultList(null);
				}
			}
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, pageInfo), HttpStatus.OK);
			}else{
				request.setAttribute("list", pageInfo.getResultList());
				request.setAttribute("site", "1");//页面中艺术家
				request.setAttribute("count",pageInfo.getResultCount());//页面中艺术家
				return "userCenter/label/artist";
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
	 * 用户我的订阅:艺术家ajax
	 * 网页端
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getSubscribeArtistAjax")
	public @ResponseBody ResponseMsg getSubscribeArtistAjax(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID
			params.put("userId", this.getLoginUser().getUid());
			//先删除
			delFavouriteArtist(params);
			//pageSize：每页数量
			String pageSize = KuKeAuthConstants.ARTISTSIZE;
			params.put("pageSize" , pageSize);
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getSubscribeArtist", params);
			List subscribeArtistList = pageInfo.getResultList();
			
			String mid = "";//所有的艺术家资源ID
			Map<String,String> param = new HashMap<String,String>();
			for(int i=0;i<subscribeArtistList.size();i++){
				mid += String.valueOf(((Map)subscribeArtistList.get(i)).get("rss_source_id"))+",";
			}
			param.put("musicianIds", StringUtil.formatCommaForString(mid));
			if(!"".equals(dealNull(mid))){//mid参数为空,即艺术家列表为空
				String result = HttpClientUtil.executePost(KuKeUrlConstants.getMusicians, param);
				//艺术家封面
				ResponseMsg msg = new ResponseMsg(result);
				Object data = msg.getData();
				if(msg.getFlag() && data!=null){//true  并且   data不为空
					JSONArray list = JSONArray.fromObject(data);
					List<Object> li = new ArrayList<Object>();
					
					for(int i=0;i<list.size();i++){     
						JSONObject jobj =  (JSONObject) list.get(i);
						Artist artist =(Artist)JSONObject.toBean(jobj,Artist.class);
						//封面:
						artist.setMusicianImg(ImageUrlUtil.getArtistImg(artist.getMusicianId()));
						String mtype = artist.getMusicianType();
//						String mType ="";
//						if(mtype!=null){
//							mType = mtype[0];
//						}
						artist.setmType(mtype);
						li.add(artist);
					}
					pageInfo.setResultList(li);
				}else{
					pageInfo.setResultList(null);
				}
			}
			//返回
			Map<String, Object> map = new HashMap<String, Object>();
			//要拼接的html信息:
			String scrollInfo = getArtistHtml(pageInfo.getResultList(), "");
			boolean more = true;
			//list数量如果小于煤业数量，则没有更多了
			if(pageInfo.getResultList().size() < Integer.parseInt(pageSize)){
				more = false;
			}
			map.put("scrollInfo", scrollInfo);
			map.put("more", more);
			return new ResponseMsg(true, "1", "查询成功","1：查询成功", map); 
		}else{
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null);
		}
	}
	/**
	 * 检查是否被订阅
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/checkSubscribe")
	public @ResponseBody ResponseMsg checkSubscribe(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		com.kuke.auth.login.bean.User user = this.getLoginUser();
		if(user != null){
			String type = dealNull(params.get("type"));
			String source_id = dealNull(params.get("source_id"));
			String user_id = dealNull(user.getUid());
			if("".equals(source_id)){
				return new ResponseMsg(false,"1","source_id为空");
			}else{
				String resid = dealNull(userCenterService.checkSubscribe(user_id,source_id,type));
				if("".equals(resid)){
					return new ResponseMsg(false,"2",source_id+"未被订阅",null);
				}else{
					return new ResponseMsg(true,"3",source_id+"被订阅",null);
				}
			}
		}else{
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null);
		}
	}
	/**
	 * 取消订阅(批量)
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/cancleSubscribe")
	public @ResponseBody ResponseMsg cancleSubscribe(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		boolean flag = false;
		String code = "FAILED";
		String msg = "失败";
		String codeDesc = "SUCCESS:成功;"
						+ "FAILED:失败;"
						+ "NOMALLOGIN:未登录;"
						+ "NOCHOOSE:未选择数据;";
		if (this.getLoginUser() != null) {
			// 资源ID  ,type
			String source_id = dealNull(request.getParameter("source_id"));//逗号分隔
			String type = dealNull(request.getParameter("type"));//订阅类型
			String[] source_ids = StringUtil.formatCommaForString(source_id).split(",");
			if(source_ids != null && source_ids.length > 0){
				userCenterService.cancleSubscribe(this.getLoginUser().getUid(),source_ids,type);
				flag = true;
				code = "SUCCESS";
				msg = "成功";
			}else{
				code = "NOCHOOSE";
				msg = "未选择数据";
			}
		}else{
			code = "NOMALLOGIN";
			msg = "未登录";
		}
		return new ResponseMsg(flag, code, msg, codeDesc);
	}
	/**
	 * 增加订阅(批量)
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/addSubscribe")
	public @ResponseBody ResponseMsg addSubscribe(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		boolean flag = false;
		String code = "FAILED";
		String msg = "失败";
		String codeDesc = "SUCCESS:成功;"
						+ "FAILED:失败;"
						+ "NOMALLOGIN:未登录;"
						+ "ERRORCODE:source_id,type参数异常;";
		try {
			if (this.getLoginUser() != null) {
				//资源ID
				String source_id = dealNull(request.getParameter("source_id"));//逗号分隔
				String[] source_ids = StringUtil.formatCommaForString(source_id).split(",");
				String type = dealNull(request.getParameter("type"));//厂牌  或  艺术家 11   12  71
				if(source_ids != null && source_ids.length > 0 && !"".equals(type)){
					if(KuKeAuthConstants.LABEL.indexOf(type)< 0 && !KuKeAuthConstants.ARTIST.equals(type)){
						return new ResponseMsg(false, "TYPEERROR", "type应为"+KuKeAuthConstants.LABEL+","+KuKeAuthConstants.ARTIST+"中的一种", codeDesc);
					}else{
						List<String> source = new ArrayList<String>();
						for(int i = 0; i < source_ids.length; i++){
							source.add(dealNull(new String(source_ids[i].getBytes("ISO8859-1"),"UTF-8")));
						}
						//去重
						List<String> list = userCenterService.getSubscribe(this.getLoginUser().getUid(), source_ids, type);
						for(int i = 0; i < list.size(); i++){
							String source_idTemp = list.get(i);
							if(source.contains(source_idTemp)){
								source.remove(source_idTemp);
							}
						}
						//添加
						if(source != null && source.size() > 0){
							userCenterService.addSubscribe(this.getLoginUser().getUid(),source,type);
						}
						flag = true;
						code = "SUCCESS";
						msg = "成功";
					}
				}else{
					code = "ERRORCODE";
					msg = "source_id,type参数异常";
				}
			}else{
				code = "NOMALLOGIN";
				msg = "未登录";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseMsg(flag, code, msg, codeDesc);
	}
	/**
	 * 下载权限
	 * 
	 */
	@RequestMapping(value = "/downAuthorize")
	public @ResponseBody ResponseMsg downAuthorize(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		JSONObject jsonObject = new JSONObject();
		String item_id = params.get("item_id");
		String channel_id = params.get("channel_id");
		if (this.getLoginUser() != null && StringUtils.isNotEmpty(channel_id) && StringUtils.isNotEmpty(item_id)) {
			String retStr = UserOauth.userDownload(request, item_id, channel_id);
			User user = new User();
			user.setId(this.getLoginUser().getUid());
			params.put("userId", user.getId());
			
			if (retStr.equals("SUCCESS")) {
				return new ResponseMsg(false, KuKeAuthConstants.SUCCESS);
			} else {
				jsonObject.put("result", "failed");
				return new ResponseMsg(false, KuKeAuthConstants.FAILED);
			}
		} else {
			jsonObject.put("result", "failed");
			return new ResponseMsg(false, KuKeAuthConstants.FAILED);
		}

	}
	/**
	 * 用户最近播放:最多100条
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getRecentPlay")
	public Object getRecentPlay(HttpServletRequest request, ModelMap map){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			List<Map<String,String>> list = userCenterService.getRecentPlay(this.getLoginUser().getUid());
			if("m".equals(from)){
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("recentPlayList", list);
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, result), HttpStatus.OK);
			}else{
				request.setAttribute("recentPlayList", list);
				return "/userCenter/my/recentPlay";
			}
		}else{
			if("m".equals(from)){
				Map<String, Object> result = new HashMap<String, Object>();
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.FAILED, result), HttpStatus.OK);
			}else{
				return "forward:/WEB-INF/jsp/login.jsp";
			}
		}
	}
	/**
	 * 得到厂牌的封面
	 * @param list
	 */
	private List getSubscribeLableIMG(List list){
		List resultList = new ArrayList();
		if(list != null){
			for(int i = 0; i < list.size(); i++){
				Map m = (Map) list.get(i);
				if(m.get("displayCname")==null||m.get("displayCname").equals("")){
					m.put("displayCname", "");
				}
				//设置厂牌封面
				m.put("logoImg", ImageUrlUtil.getLableImg((String)m.get("labelid")));
				resultList.add(m);
			}
			return resultList;
		}else{
			return list;
		}
	}
	/**
	 * 得到厂牌页面的html内容
	 * @param folderLists
	 * @param type
	 * @return
	 */
	private String getLableHtml(List list,String type){
		StringBuffer html = new StringBuffer("<ol class=\"comSite294 cf\">");//html信息
		for(int i = 1; i <= list.size();i++){
			Map map = (Map) list.get(i-1);
			String showable = StringUtil.dealNull(map.get("showable").toString());
			if("0".equals(showable)){//下架
				html.append("<li class=\"comSite295 comSite295a cOff03\">");
				html.append("	<div class=\"comSite295cc1 pr\" onclick=\"tips('showOffMessage');\">");
				html.append("	  <div class=\"comSite296\");\"><img src=\""+(String)map.get("logoImg")+"\" alt=\"\" style=\"height: 90px;\"></div>");
				html.append("	  <p class=\"comSt279\");\"><a href=\"javascript:void(0)\">"+(String)map.get("displayName")+"</a></p>");
//				html.append("	  <div class=\"comSite297\"><a href=\"javascript:void(0)\" class=\"comSt280 comSt280a\" onclick=\"cancleSubscribeL('"+map.get("rssType")+"','"+(String)map.get("labelid")+"');\">取消订阅</a></div>");
				html.append("	  <span class=\"offBg01 offBg01b\"></span>");
				html.append("	  <span class=\"isOff isOff02\">已下架</span>");
				html.append("	  <div class=\"comSite298 comSite298a\">");
				html.append("	  		<div class=\"comSite299 pr\">");
//				html.append("	  			<span class=\"comSt281 comSt281a\"></span>");
//				html.append("	  			<input type=\"checkbox\" name=\"labeldata\" class=\"comSt282 comSt282a\" isclick=\"false\" checked=\"false\" value=\""+(String)map.get("labelid")+"\">");
				html.append("	  		</div>");
				html.append("	  </div>");
//				html.append("	  <span class=\"comSt284 comSt284b\"></span>");
				html.append("	</div>");
				html.append("	<div class=\"comSite297\"><a href=\"javascript:void(0)\" class=\"comSt280 comSt280a\" onclick=\"cancleSubscribeL('"+map.get("rssType")+"','"+(String)map.get("labelid")+"');\">取消订阅</a></div>");
				html.append("</li>");
				if(i % 5 == 0 && i != list.size()){
					html.append("</ol>");
					html.append("<ol class=\"comSite294 cf\">");
				}
			}else{
				html.append("<li class=\"comSite295 comSite295a\">");
				html.append("	<div class=\"comSite295cc1 pr\">");
				html.append("	  <div class=\"comSite296\");\"><img onclick=\"openLabel('"+(String)map.get("labelid")+"')\" src=\""+(String)map.get("logoImg")+"\" alt=\"\" style=\"height: 90px;cursor: pointer;\"></div>");
				html.append("	  <p class=\"comSt279\");\"><a href=\"javascript:void(0)\" onclick=\"openLabel('"+(String)map.get("labelid")+"');\">"+(String)map.get("displayName")+"</a></p>");
//				html.append("	  <div class=\"comSite297\"><a href=\"javascript:void(0)\" class=\"comSt280 comSt280a\" onclick=\"cancleSubscribeL('"+map.get("rssType")+"','"+(String)map.get("labelid")+"');\">取消订阅</a></div>");
				html.append("	  <div class=\"comSite298 comSite298a\">");
				html.append("	  		<div class=\"comSite299 pr\">");
//				html.append("	  			<span class=\"comSt281 comSt281a\"></span>");
				html.append("	  			<input type=\"checkbox\" name=\"labeldata\" class=\"comSt282 comSt282a\" isclick=\"false\" checked=\"false\" value=\""+(String)map.get("labelid")+"\">");
				html.append("	  		</div>");
				html.append("	  </div>");
//				html.append("	  <span class=\"comSt284 comSt284b\"></span>");
				html.append("	</div>");
				html.append("	<div class=\"comSite297\"><a href=\"javascript:void(0)\" class=\"comSt280 comSt280a\" onclick=\"cancleSubscribeL('"+map.get("rssType")+"','"+(String)map.get("labelid")+"');\">取消订阅</a></div>");
				html.append("</li>");
				if(i % 5 == 0 && i != list.size()){
					html.append("</ol>");
					html.append("<ol class=\"comSite294 cf\">");
				}
			}
		}
		html.append("</ol>");
		return html.toString();
	}
	/**
	 * 得到艺术家页面的html内容
	 * @param folderLists
	 * @param type
	 * @return
	 */
	private String getArtistHtml(List list,String type){
		StringBuffer html = new StringBuffer("<ol class=\"comSite294 cf\">");//html信息
		for(int i = 1; i <= list.size();i++){
			Artist a = (Artist) list.get(i-1);
			html.append("<li class=\"comSite295 comSite295b1 pr\">");
			html.append("	<div class=\"comSite295cc1 pr\">");
			html.append("	  <div class=\"comSite296\"><img onclick=\"openArtist('"+a.getMusicianId()+"','"+a.getmType()+"');\" src=\""+a.getMusicianImg()+"\" alt=\"\" width=\"120\" height=\"120\" style=\"cursor: pointer;\"></div>");
			html.append("	  <p class=\"comSt279\"><a  href=\"javascript:void(0)\"  onclick=\"openArtist('"+a.getMusicianId()+"','"+a.getmType()+"');\" style=\"cursor: pointer;\">"+a.getFullName()+"</a></p>");
//			html.append("	  <div class=\"comSite297\"><a href=\"javascript:void(0)\" class=\"comSt280 comSt280a\" onclick=\"cancleSubscribeA('71','"+a.getMusicianId()+"');\">取消订阅</a></div>");
			html.append("	  <div class=\"comSite298 comSite298b1\">");
			html.append("	  		<div class=\"comSite299 pr\">");
//			html.append("	  			<span class=\"comSt281 comSt281b1\"></span>");
			html.append("	  			<input type=\"checkbox\" name=\"artistdata\" class=\"comSt282 comSt282b1\" isclick=\"false\" checked=\"false\" value=\""+a.getMusicianId()+"\">");
			html.append("	  		</div>");
			html.append("	  </div>");
//			html.append("	  <span class=\"comSt284 comSt284b comSt284b1\"></span>");
			html.append("	</div>");
			html.append("	<div class=\"comSite297\"><a href=\"javascript:void(0)\" class=\"comSt280 comSt280a\" onclick=\"cancleSubscribeA('71','"+a.getMusicianId()+"');\">取消订阅</a></div>");
			html.append("</li>");
			if(i % 5 == 0 && i != list.size()){
				html.append("</ol>");
				html.append("<ol class=\"comSite294 cf\">");
			}
		}
		html.append("</ol>");
		return html.toString();
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
	public static void main(String[] args) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("musicianId", "15"));
		nvps.add(new BasicNameValuePair("musicianType", "0"));
		String post_url = KuKeUrlConstants.getMusicians;
		String result = "";
		try {
			result = HttpClientUtil.executeServicePOST(post_url, nvps);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ResponseMsg msg = new ResponseMsg(result);
		System.out.println(msg);
	}
}
