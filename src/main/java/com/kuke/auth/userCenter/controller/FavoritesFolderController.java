package com.kuke.auth.userCenter.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.userCenter.bean.CatalsOfFolder;
import com.kuke.auth.userCenter.bean.TracksOfFolder;
import com.kuke.auth.userCenter.bean.UserFolder;
import com.kuke.auth.userCenter.mapper.UserCenterMapper;
import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.engine.ICookie;
import com.kuke.util.ImageUrlUtil;
import com.kuke.util.MessageFormatUtil;
import com.kuke.util.PageInfo;
import com.kuke.util.PicClientUtil;
import com.kuke.util.SaveImage;
import com.kuke.util.StringUtil;
/**
 * 我的夹子
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/kuke/userCenter")
public class FavoritesFolderController extends BaseController{
	
	private static Logger logger = LoggerFactory.getLogger(FavoritesFolderController.class);
	
	public static String everyCD = String.valueOf(PropertiesHolder.getContextProperty("everyCD"));//每张CD的价格
	
	@Autowired
	private UserCenterService userCenterService;
	
	@Autowired
	private UserCenterMapper userCenterMapper;
	/**
	 * 用户的夹子,包括单曲,专辑;分类好
	 * 触发的下拉滚动事件
	 * 网页端
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getUserFolderAjax")
	public @ResponseBody ResponseMsg getUserFolderAjax(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		Map<String, Object> map = new HashMap<String, Object>(); 
		User user = this.getLoginUser();
		if(user != null){
			//设置参数当前登录用户ID
			String foldertype = "".equals(dealNull(params.get("foldertype")))?KuKeAuthConstants.FOLDERTRACK:dealNull(params.get("foldertype"));
			params.put("type", foldertype);//1:单曲  2：专辑
			params.put("userId", this.getLoginUser().getUid());
			String pageSize = KuKeAuthConstants.FOLDERSIZE;//设置每页数量
			params.put("pageSize" , pageSize);
			//用户下的所有的夹子
			PageInfo pageInfo = getServicePageInfo(userCenterService,"queryUserFolders", params);
			List folderLists = pageInfo.getResultList();
			//处理夹子封面
			folderLists = getFolderImg(folderLists);
			//要拼接的html信息:
			String scrollInfo = "";
			if(KuKeAuthConstants.FOLDERTRACK.equals(foldertype)){//单曲
				scrollInfo = getFolderHtml(folderLists, KuKeAuthConstants.FOLDERTRACK);
			}else if(KuKeAuthConstants.FOLDERCATLOG.equals(foldertype)){//专辑
				scrollInfo = getFolderHtml(folderLists, KuKeAuthConstants.FOLDERCATLOG);
			}
			boolean more = true;
			//list数量如果小于煤业数量，则没有更多了
			if(folderLists.size() < Integer.parseInt(pageSize)){
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
	 * 用户的夹子,包括单曲,专辑;分类好
	 * 网页端
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getUserFolder")
	public Object getUserFolder(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID
			String foldertype = "".equals(dealNull(params.get("foldertype")))?KuKeAuthConstants.FOLDERTRACK:dealNull(params.get("foldertype"));
			params.put("type", foldertype);//1:单曲  2：专辑
			params.put("userId", this.getLoginUser().getUid());
			String pageSize = KuKeAuthConstants.FOLDERSIZE;//设置每页数量
			params.put("pageSize" , pageSize);
			//用户下的所有的夹子
			PageInfo pageInfo = getServicePageInfo(userCenterService,"queryUserFolders", params);
			List folderLists = pageInfo.getResultList();
			//处理夹子封面
			folderLists = getFolderImg(folderLists);
			//单曲夹
			List<UserFolder> trackFolder = new ArrayList<UserFolder>();
			//唱片夹
			List<UserFolder> CatalFolder = new ArrayList<UserFolder>();
			for(int i = 0; i < folderLists.size(); i++){
				UserFolder temp =(UserFolder) folderLists.get(i);
				if("1".equals(temp.getType())){//单曲夹
					trackFolder.add(temp);
				}else if("2".equals(temp.getType())){
					CatalFolder.add(temp);
				}
			}
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			
			if("m".equals(from)){
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("trackFolder", trackFolder);//单曲
				result.put("CatalFolder", CatalFolder);//专辑
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, result), HttpStatus.OK);
			}else{
				request.setAttribute("trackFolder", trackFolder);//单曲
				request.setAttribute("CatalFolder", CatalFolder);//专辑
				request.setAttribute("pageInfo", pageInfo);//专辑
				request.setAttribute("foldertype", foldertype);//
				request.setAttribute("count", pageInfo.getResultCount());//
				if("2".equals(dealNull(params.get("foldertype")))){//专辑
					return "/userCenter/folder/folderCatlog";
				}else{//单曲
					return "/userCenter/folder/folderTrack";
				}
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
	 * 我的夹子，单曲项
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getTrackFolder")
	public Object getTrackFolder(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID
			params.put("userId", this.getLoginUser().getUid());
			//网页端不分页
			if(!"m".equals(from)){
				params.put("pageSize" , "9999999");
			}
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getTrackFolderList", params);
			List trackFolderList = pageInfo.getResultList();
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.noFormatObject(KuKeAuthConstants.SUCCESS, pageInfo), HttpStatus.OK);
			}else{
				request.setAttribute("trackFolderList", trackFolderList);//页面信息数量
				request.setAttribute("pageInfo", pageInfo);//分页信息
				//用户信息
				return "/userCenter/folder/trackFolder";
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
	 * 我的夹子，唱片项
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getCatalFolder")
	public Object getCatalFolder(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID 
			params.put("userId", this.getLoginUser().getUid());
			//网页端不分页
			if(!"m".equals(from)){
				params.put("pageSize" , "9999999");
			}
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getCatalFolderList", params);
			List catalFolderList = pageInfo.getResultList();
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.noFormatObject(KuKeAuthConstants.SUCCESS, pageInfo), HttpStatus.OK);
			}else{
				request.setAttribute("catalFolderList", catalFolderList);//页面信息数量
				request.setAttribute("pageInfo", pageInfo);//分页信息
				return "/userCenter/my/catalFolder";
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
	 * 单个单曲夹中的单曲信息
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getTracksOfFolder")
	public Object getTracksOfFolder(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		Map<String, Object> data = new HashMap<String, Object>();
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID,还有单曲夹musicfolder_id
			params.put("userId", this.getLoginUser().getUid());
			if(!"".equals(dealNull(params.get("musicfolder_id")))){//夹子ID非空
				//网页端不分页
				if(!"m".equals(from)){
					params.put("pageSize" ,KuKeAuthConstants.TRACKFOLDERSIZE);
				}
				//查询单曲数据
				PageInfo pageInfo = getServicePageInfo(userCenterService,"getTracksOfFolder", params);
				List tracksOfFolder = pageInfo.getResultList();
				//查询夹子信息
				Map<String, String> param = new HashMap<String, String>();
				param.put("folderid", dealNull(params.get("musicfolder_id")));
				param.put("userId", this.getLoginUser().getUid());
				List<UserFolder> userFolder = userCenterService.queryMyFolders(param);
				//处理夹子封面
				userFolder = getFolderImg(userFolder);
				
				//记录日志
				LogUtil.addVisitLog(request);
				
				if("m".equals(from)){//直接装入pageInfo
					return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, pageInfo), HttpStatus.OK);
				}else{
					request.setAttribute("tracksOfFolder", setTrackData(tracksOfFolder));//页面信息数量
					request.setAttribute("count", pageInfo.getResultCount());//页面信息数量
					request.setAttribute("userFolder", userFolder.size() > 0?userFolder.get(0):new UserFolder());//夹子信息
					request.setAttribute("musicfolder_id", dealNull(params.get("musicfolder_id")));//分页信息
					request.setAttribute("nexturl", "/kuke/userCenter/getUserFolder");//下一页面地址
					return "/userCenter/folder/detailTrackOfFolder";
				}
			}else{
				if("m".equals(from)){
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(false, "musicfolder_id为空"), HttpStatus.OK);
				}else{
					return "/userCenter/folder/detailTrackOfFolder";
				}
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
	 * 单个单曲夹中的单曲信息ajax
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getTracksOfFolderAjax")
	public @ResponseBody ResponseMsg getTracksOfFolderAjax(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.getLoginUser() != null) {
			//设置参数当前登录用户ID,还有单曲夹musicfolder_id
			params.put("userId", this.getLoginUser().getUid());
			if(!"".equals(dealNull(params.get("musicfolder_id")))){//夹子ID非空
				String pageSize = KuKeAuthConstants.TRACKFOLDERSIZE;//设置每页数量
				params.put("pageSize" , pageSize);
				//查询单曲数据
				PageInfo pageInfo = getServicePageInfo(userCenterService,"getTracksOfFolder", params);
				List tracksOfFolder = pageInfo.getResultList();
				
				//要拼接的html信息:
				String scrollInfo = getTrackFolderHtml(tracksOfFolder,pageInfo);
				boolean more = true;
				//list数量如果小于煤业数量，则没有更多了
				if(tracksOfFolder.size() < Integer.parseInt(pageSize)){
					more = false;
				}
				map.put("scrollInfo", scrollInfo);
				map.put("more", more);
			}else{
				map.put("scrollInfo", "");
				map.put("more", true);
			}
			return new ResponseMsg(true, "1", "查询成功","1：查询成功", map); 
		}else{
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null);
		}
	}
	/**
	 * 单个夹子中唱片信息
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getCatalsOfFolder")
	public Object getCatalsOfFolder(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			//设置头部信息
			this.getUserInfo(request, response);
			
			if(!"".equals(dealNull(params.get("musicfolder_id")))){//夹子ID非空
				//设置参数当前登录用户ID  music_folderid:夹子ID everyCD:每张cd的价格
				params.put("userId", this.getLoginUser().getUid());
				params.put("everyCD", everyCD);//价格
				//网页端不分页
				if(!"m".equals(from)){
					params.put("pageSize" , KuKeAuthConstants.CATLOGFOLDERSIZE);
				}
				PageInfo pageInfo = getServicePageInfo(userCenterService,"getCatalsOfFolder", params);
				List catalsOfFolder = pageInfo.getResultList();
				//查询夹子信息
				Map<String, String> param = new HashMap<String, String>();
				param.put("folderid", dealNull(params.get("musicfolder_id")));
				param.put("userId", this.getLoginUser().getUid());
				List<UserFolder> userFolder = userCenterService.queryMyFolders(param);
				userFolder = getFolderImg(userFolder);//处理夹子封面
				
				//记录日志
				LogUtil.addVisitLog(request);
				
				if("m".equals(from)){
					return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, pageInfo), HttpStatus.OK);
				}else{
					request.setAttribute("catalsOfFolder",setCatalListData(catalsOfFolder));//页面信息数量
					request.setAttribute("count", pageInfo.getResultCount());//页面信息数量
					request.setAttribute("userFolder", userFolder.size() > 0?userFolder.get(0):"");//夹子信息
					request.setAttribute("musicfolder_id", dealNull(params.get("musicfolder_id")));//
					request.setAttribute("foldertype", "2");//夹子类型
					request.setAttribute("nexturl", "/kuke/userCenter/getUserFolder?foldertype=2");//下一页面地址
					return "/userCenter/folder/detailCatlogOfFolder";
				}
			}else{
				if("m".equals(from)){
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(false, "musicfolder_id为空"), HttpStatus.OK);
				}else{
					return "/userCenter/folder/detailCatlogOfFolder";
				}
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
	 * 单个夹子中唱片信息ajax
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getCatalsOfFolderAjax")
	public @ResponseBody ResponseMsg getCatalsOfFolderAjax(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.getLoginUser() != null) {
			if(!"".equals(dealNull(params.get("musicfolder_id")))){//夹子ID非空
				//设置参数当前登录用户ID  music_folderid:夹子ID everyCD:每张cd的价格
				params.put("userId", this.getLoginUser().getUid());
				params.put("everyCD", everyCD);//价格
				String pageSize = KuKeAuthConstants.CATLOGFOLDERSIZE;//设置每页数量
				params.put("pageSize" , pageSize);
				PageInfo pageInfo = getServicePageInfo(userCenterService,"getCatalsOfFolder", params);
				List catalsOfFolder = pageInfo.getResultList();
				//要拼接的html信息:
				String scrollInfo = getCatlogFolderHtml(catalsOfFolder);
				boolean more = true;
				//list数量如果小于煤业数量，则没有更多了
				if(catalsOfFolder.size() < Integer.parseInt(pageSize)){
					more = false;
				}
				map.put("scrollInfo", scrollInfo);
				map.put("more", more);
			}else{
				map.put("scrollInfo", "");
				map.put("more", true);
			}
			return new ResponseMsg(true, "1", "查询成功","1：查询成功", map); 
		}else{
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null);
		}
	}
	/**
	 * 得到某个用户的所有夹子信息
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/queryMyFolders")
	public @ResponseBody ResponseMsg queryMyFolders(HttpServletRequest request, ModelMap map){
		Map<String, String> params = getParameterMap(request);
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = KuKeAuthConstants.FAILED;
		List<UserFolder> userFolderList = new ArrayList<UserFolder>();
		if (this.getLoginUser() != null) {
			try {
				//设置参数当前登录用户ID
				params.put("userId", this.getLoginUser().getUid());
				//参数：type (type为空,就查询所有的)， userId必须的,folderid(查询某一个夹子),folder_name(等于某个名称的夹子)
				userFolderList = userCenterService.queryMyFolders(params);
				resultStr = KuKeAuthConstants.SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			resultStr = KuKeAuthConstants.NOMALLOGIN;
		}
		return MessageFormatUtil.formatStateToJSONArray(resultStr, userFolderList);
	}
	/**
	 * 得到某个夹子的资源信息
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getFolderSource")
	public @ResponseBody ResponseMsg getFolderSource(HttpServletRequest request, ModelMap map){
		Map<String, String> params = getParameterMap(request);
		Map<String, Object> result = new HashMap<String, Object>();
		String resultStr = KuKeAuthConstants.FAILED;
		String sourceString = "";
		if (this.getLoginUser() != null) {
			try {
				//设置参数当前登录用户ID
				params.put("userId", this.getLoginUser().getUid());
				//参数：type (type为空,就查询所有的)， userId必须的,musicfolder_id(查询某一个夹子)
				String type = dealNull(request.getParameter("type"));
				List<String> list = userCenterMapper.getSourceOfFolder(params);
				if("1".equals(type)){//单曲
					for(int i = 0; i < list.size(); i++){
						String temp = dealNull(list.get(i));
						sourceString +="," +  (temp.split("\\|").length > 0?temp.split("\\|")[0]:"");
					}
					if(sourceString.length() > 0){
						sourceString = userCenterMapper.getNoOffFolderLcode(StringUtil.dealNull(sourceString).split(","));
					}
				}else if("2".equals(type)){//专辑
					for(int i = 0; i < list.size(); i++){
						String temp = dealNull(list.get(i));
						sourceString +="," +  temp;
					}
					if(sourceString.length() > 0){
						sourceString = userCenterMapper.getNoOffSourceItem(StringUtil.dealNull(sourceString).split(","));
					}
				}else{
					return new ResponseMsg(false, "type错误："+type);
				}
				resultStr = KuKeAuthConstants.SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			resultStr = KuKeAuthConstants.NOMALLOGIN;
		}
		return MessageFormatUtil.noFormatObject(resultStr, sourceString);
	}
	/**
	 * 新建夹子
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/createFavoritesFolder")
	public @ResponseBody ResponseMsg createFavoritesFolder(HttpServletRequest request, ModelMap map){
		Map<String, String> params = getParameterMap(request);
		boolean flag = false;
		String code = "FAILED";
		String msg = "创建失败";
		String codeDesc = "NOMALLOGIN:未登录;"
						+ "CODEERROR:type,folder_name不能为空;"
						+ "HASNAME:folder_name已经存在;"
						+ "FAILED:创建失败;"
						+ "SUCCESS:创建成功;"
						+ "ERROR:夹子创建成功,资源添加失败;";
		if (this.getLoginUser() != null) {
			try {
				//设置参数当前登录用户ID
				params.put("userId", this.getLoginUser().getUid());
				if(!"".equals(dealNull((String)params.get("type"))) && !"".equals(dealNull((String)params.get("folder_name")))){
					Map<String, String> param = new HashMap<String, String>();
					param.put("folder_name", dealNull((String)params.get("folder_name")));
					String type = dealNull((String)params.get("type"));
					param.put("type", type);
					param.put("userId", this.getLoginUser().getUid());
					if(userCenterService.queryMyFolders(param).size() <= 0){
						//参数：type ， userId ，folder_name必须的
						UserFolder f = userCenterService.createFavoritesFolder(params);
						
						//资源ID
						String source_id = dealNull(request.getParameter("source_id"));//逗号分隔
						if(!"".equals(source_id)){//资源不为空,则添加
							userCenterService.addSourceToFolder(this.getLoginUser().getUid(),String.valueOf(f.getId()),source_id.split(","));
						}
						//夹子ID:
						String premusicfolder_id = dealNull(request.getParameter("premusicfolder_id"));
						if(!"".equals(premusicfolder_id)){
							userCenterService.addFolderSourceToFolder(this.getLoginUser().getUid(), String.valueOf(f.getId()),premusicfolder_id,type);
						}
						
						flag = true;
						code = "SUCCESS";
						msg = "创建成功";
					}else{
						code = "HASNAME";
						msg = "folder_name已经存在";
					}
				}else{
					code = "CODEERROR";
					msg = "type,folder_name不能为空";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			flag = false;
			code = "NOMALLOGIN";
			msg = "未登录";
		}
		return new ResponseMsg(flag, code, msg, codeDesc);
	}
	/**
	 * 修改夹子名称
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/editFavFolderName")
	public @ResponseBody ResponseMsg editFavFolderName(HttpServletRequest request, ModelMap map){
		Map<String, String> params = getParameterMap(request);
		Map<String, String> result = new HashMap<String, String>();
		String resultStr = KuKeAuthConstants.FAILED;
		if (this.getLoginUser() != null) {
			try {
				//设置参数当前登录用户ID
				params.put("userId", this.getLoginUser().getUid());
				//参数：musicfolder_id ， userId ，folder_name必须的
				int count = userCenterService.editFavFolderName(params);
				resultStr = KuKeAuthConstants.SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			resultStr = KuKeAuthConstants.NOMALLOGIN;
		}
		return MessageFormatUtil.formatResultToObject(resultStr);
	}
	
	/**
	 * 删除夹子
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/delFavoritesFolder")
	public @ResponseBody ResponseMsg delFavoritesFolder(HttpServletRequest request, ModelMap map){
		Map<String, String> params = getParameterMap(request);
		Map<String, String> result = new HashMap<String, String>();
		String resultStr = KuKeAuthConstants.FAILED;
		if (this.getLoginUser() != null) {
			try {
				//设置参数当前登录用户ID
				params.put("userId", this.getLoginUser().getUid());
				String musicfolder_id = request.getParameter("musicfolder_id");//逗号分隔
				String[] musicfolder_ids = StringUtil.formatCommaForString(musicfolder_id).split(",");
				if(musicfolder_ids != null && musicfolder_ids.length > 0){
					//参数：musicfolder_id ， userId 必须的
					//删除夹子
					userCenterService.delFavoritesFolder(this.getLoginUser().getUid(),musicfolder_ids);
					//删除夹子关系
					userCenterService.delFavoritesFolderRelation(this.getLoginUser().getUid(), musicfolder_ids);
					resultStr = KuKeAuthConstants.SUCCESS;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			resultStr = KuKeAuthConstants.NOMALLOGIN;
		}
		return MessageFormatUtil.formatResultToObject(resultStr);
	}
	/**
	 * 删除夹子中的单曲或专辑
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/delFavoritesSourceOfFolder")
	public @ResponseBody ResponseMsg delFavoritesSourceOfFolder(HttpServletRequest request, ModelMap map){
		Map<String, String> params = getParameterMap(request);
		Map<String, String> result = new HashMap<String, String>();
		String resultStr = KuKeAuthConstants.FAILED;
		if (this.getLoginUser() != null) {
			try {
				//资源ID
				String source_id = request.getParameter("source_id");//逗号分隔
				String[] source_ids = StringUtil.formatCommaForString(source_id).split(",");
				if(source_ids != null && source_ids.length > 0){
					//夹子的ID
					String musicfolder_id = request.getParameter("musicfolder_id");
					if(!"".equals(dealNull(musicfolder_id))){
						//参数：musicfolder_id ， userId 必须的
						//删除夹子关系中的source_ids
						userCenterService.delFavoritesSourceOfFolder(this.getLoginUser().getUid(), musicfolder_id,source_ids);
						resultStr = KuKeAuthConstants.SUCCESS;
					}else{
						resultStr = KuKeAuthConstants.FAILED;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			resultStr = KuKeAuthConstants.NOMALLOGIN;
		}
		return MessageFormatUtil.formatResultToObject(resultStr);
	}
	/**
	 * 唱片夹资源添加到唱片夹
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/addFolderSourceToFolder")
	public @ResponseBody ResponseMsg addFolderSourceToFolder(HttpServletRequest request){
		Map<String, String> params = getParameterMap(request);
		boolean flag = false;
		String msg = "";
		if (this.getLoginUser() != null) {
			try {
				//夹子ID
				String premusicfolder_id = dealNull(request.getParameter("premusicfolder_id"));
				//要添加到的夹子ID
				String musicfolder_id = dealNull(request.getParameter("musicfolder_id"));
				//类型
				String type = dealNull(request.getParameter("type"));
				if(!"".equals(premusicfolder_id) && !"".equals(musicfolder_id)){//前置夹子的ID
					//参数：musicfolder_id ， userId 必须的
					//删除夹子关系中的source_ids
					int resultCount = userCenterService.addFolderSourceToFolder(this.getLoginUser().getUid(), musicfolder_id,premusicfolder_id,type);
					flag = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new ResponseMsg(flag, msg);
		}else{
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.NOMALLOGIN);
		}
	}
	/**
	 * 检查资源是否下架
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/checkOffSource")
	public @ResponseBody ResponseMsg checkOffSource(HttpServletRequest request){
		Map<String, String> params = getParameterMap(request);
		boolean flag = false;
		String msg = "";
		String type = StringUtil.dealNull(params.get("type"));
		if (this.getLoginUser() != null) {
			params.put("userId", this.getLoginUser().getUid());
//			params.put("musicfolder_id", premusicfolder_id);//
			//之前的夹子ID
			List list = userCenterMapper.getSourceOfFolder(params);
			StringBuffer source_id = new StringBuffer();
			for(int i = 0; i < list.size(); i++){
				String s = (String) list.get(i);
				source_id.append(","+s);
			}
			//过滤掉失效资源:已下架
			String source = "";
			if("2".equals(type)){//专辑
				source = userCenterMapper.getNoOffSourceItem(StringUtil.formatCommaForString(source_id.toString()).split(","));
			}else{
				String lcode = "";
				for(int i = 0;i < source_id.toString().split(",").length; i++){
					if(!"".equals(StringUtil.dealNull(source_id.toString().split(",")[i]))){
						lcode += "," + (StringUtil.dealNull(source_id.toString().split(",")[i])).split("\\|")[0];
					}
				}
				
				String lcodeSource = StringUtil.dealNull(userCenterMapper.getNoOffFolderLcode(StringUtil.formatCommaForString(lcode).split(",")));
				
				for(int i = 0;i < source_id.toString().split(",").length; i++){
					if(!"".equals(StringUtil.dealNull(source_id.toString().split(",")[i]))){
						String groupSouce = StringUtil.dealNull(source_id.toString().split(",")[i]);//组合资源
						String lid = (StringUtil.dealNull(source_id.toString().split(",")[i])).split("\\|")[0];
						for(int j = 0;j < lcodeSource.split(",").length; j++){
							if(!"".equals(StringUtil.dealNull(lcodeSource.split(",")[j]))){
								if(lid.equals(StringUtil.dealNull(lcodeSource.split(",")[j]))){
									source += "," + groupSouce;
								}
							}
						}
					}
				}
			}
			source = StringUtil.formatCommaForString(source);
			String[] source_ids = source.length() > 0?source.split(","):null;
			if(source_ids != null && source_ids.length > 0){
				return new ResponseMsg(false, "");
			}else{
				return new ResponseMsg(true, "");//资源为0  或资源已下架
			}
		}else{
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.NOMALLOGIN);
		}
	}
	/**
	 * 资源添加到唱片夹
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/addSourceToFolder")
	public @ResponseBody ResponseMsg addSourceToFolder(HttpServletRequest request){
		Map<String, String> params = getParameterMap(request);
		Map<String, String> result = new HashMap<String, String>();
		String resultStr = KuKeAuthConstants.FAILED;
		boolean flag = false;
		String code = "2";
		String msg = "失败";
		String codeDesc = "1:成功;"
				+ "2：失败;";
		if (this.getLoginUser() != null) {
			try {
				//参数 资源ID
				String source_id = dealNull(request.getParameter("source_id"));//逗号分隔  l_code|item_code
				String musicfolder_id = dealNull(request.getParameter("musicfolder_id"));
				
				params.put("userId", this.getLoginUser().getUid());
				params.put("musicfolder_id", musicfolder_id);
				//要添加的夹子ID中的资源
				String type = "";
				List<Map<String, Object>> lists = userCenterMapper.getSourTypeOfFolder(params);
				List<String> list = new ArrayList<String>();
				for(int i = 0; i < lists.size();i++){
					Map<String, Object> tempMap= lists.get(i);
					type = (String) tempMap.get("type");
					list.add((String) tempMap.get("source_id"));
				}
				
				String[] source_ids = StringUtil.formatCommaForString(source_id).split(",");
				StringBuffer b = new StringBuffer("");
				for(int i = 0; i < source_ids.length ;i++){
					String temp = dealNull(source_ids[i]);
					if(!"".equals(temp)){
						if("|".equals(temp) || temp.startsWith("|") || temp.endsWith("|")){
							b = new StringBuffer("");
							code = "3";
							msg = "source_id参数有错误,type:"+type;
							break;
						}else if("1".equals(dealNull(type))){//单曲
							if(temp.indexOf("|") < 0){//没有|
								b = new StringBuffer("");
								code = "3";
								msg = "source_id参数有错误,type:"+type;
								break;
							}
						}else if("2".equals(dealNull(type))){//单曲
							if(temp.indexOf("|") >= 0){//有|
								b = new StringBuffer("");
								code = "3";
								msg = "source_id参数有错误,type:"+type;
								break;
							}
						}
						b.append("," + temp.replaceAll(" ", ""));
					}
				}
				if(b.toString() != null && !"".equals(b.toString()) && b.toString().split(",").length > 0){
					//夹子的ID
					//参数：musicfolder_id ， userId 必须的
					//删除夹子关系中的source_ids
					userCenterService.addSourceToFolder(list,this.getLoginUser().getUid(), musicfolder_id,b.toString().split(","));
					flag = true;
					code = "1";
					msg = "成功";
				}else{
					code = "3";
					msg = "source_id参数有错误";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new ResponseMsg(flag, code, msg, codeDesc);
		}else{
			return new ResponseMsg(flag,KuKeAuthConstants.NOMALLOGIN, "未登录", "", null);
		}
	}
	/**
	 * 上传夹子封面到临时地址
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/folderImgUpload")
	public @ResponseBody ResponseMsg folderImgUpload(MultipartHttpServletRequest request,HttpServletResponse response) {
		System.out.println("folderImgUpload");
		boolean flag = false;
		String code = "UPLOAD_FAIL";
		String msg = "上传失败";
		String codeDesc = "FORMAT_ERROR:文件类型错误;"
						+ "UPLOAD_SUCCESS:上传成功;"
						+ "SIZE_ERROR:文件大小错误;"
						+ "UPLOAD_FAIL:上传失败;"
						+ "NOMALLOGIN:用户未登录;";
		Map<String, String> data = new HashMap<String, String>();
		if(this.getLoginUser() != null){
			try {
				request.setCharacterEncoding("UTF-8");
				MultipartFile patch = request.getFile("uploadPhoto");
				// 上传文件临时地址
				String uploadPath = String.valueOf(PropertiesHolder.getContextProperty("user.folder.tmp.url"));
				// 保存到的真实地址
				String savePath = request.getSession().getServletContext().getRealPath(uploadPath);
				// 上传的文件名 
				String uploadFileName = patch.getOriginalFilename();
				// 获取文件后缀名
				String fileType = StringUtils.substringAfterLast(uploadFileName, ".");
				// 文件类型判断
				if (String.valueOf(PropertiesHolder.getContextProperty("upload.file.type")).indexOf(fileType) == -1) {//jpg|JPG|gif|GIF|png|PNG
					code = "FORMAT_ERROR";
					msg = "文件类型错误";
				}else{
					if(patch.getSize() > 5242880){//文件大小不能超过5兆
						code = "SIZE_ERROR";
						msg = "文件大小错误";
					}else{
						// 图片新名字
						String newSaveName = UUID.randomUUID().toString();
						// 图片新名字.jpg
						String finalName =  newSaveName + ("".equals(fileType) ? "" : "." + fileType);
						// 创建文件
						File saveFile = new File(savePath + File.separator + finalName);
						// 判断文件夹是否存在，不存在则创建
						if (!saveFile.getParentFile().exists()) {
							saveFile.getParentFile().mkdirs();
						}
						
						// 写入文件
						FileUtils.writeByteArrayToFile(saveFile, patch.getBytes());
						
						//上传图片到服务器临时地址
						boolean uploadFlag = PicClientUtil.uploadFile(KuKeUrlConstants.touploadurl, saveFile , finalName ,"user_tmp");
						System.out.println("uploadFlag:"+uploadFlag);
						if(uploadFlag){
							
							String path = String.valueOf(PropertiesHolder.getContextProperty("imgUrl")) + String.valueOf(PropertiesHolder.getContextProperty("user.tmp.url")) + finalName;
							System.out.println(" folderImgUpload path:"+path);
							// 保存文件的基本信息到本地
							ICookie.set(response, "userFolder", path);
							
							data.put("url", path);//本地地址
							
							flag = true;
							code = "UPLOAD_SUCCESS";
							msg = "上传成功";
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				code = "UPLOAD_FAIL";
				msg = "上传失败";
			}
		}else{
			code = "NOMALLOGIN";
			msg = "用户未登录";
		}
		return new ResponseMsg(flag, code, msg, codeDesc,data);
	}
	/**
	 * 保存夹子封面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/saveFolderImg")
	public @ResponseBody ResponseMsg saveFolderImg(HttpServletRequest request,HttpServletResponse response) {
		boolean flag = false;
		String code = "UPLOAD_FAIL";
		String msg = "上传失败";
		String codeDesc = "FORMAT_ERROR:文件类型错误;"
						+ "UPLOAD_SUCCESS:上传成功;"
						+ "SIZE_ERROR:文件大小错误;"
						+ "UPLOAD_FAIL:上传失败;"
						+ "NOMALLOGIN:用户未登录;";
		Map<String, String> data = new HashMap<String, String>();
		File tempImgFile = null;
		if(this.getLoginUser() != null){
			try {
				// 参数:musicfolder_id
				String musicfolder_id = request.getParameter("musicfolder_id");
				// 服务器上文件地址
				String tmpUrl = ICookie.get(request, "userFolder");
				tempImgFile = new File(tmpUrl);
				
				//保存到本地
				SaveImage.saveImageToDisk(tmpUrl, request.getSession().getServletContext().getRealPath(String.valueOf(PropertiesHolder.getContextProperty("user.folder.tmp.url")))+"/"+tempImgFile.getName());
				File file = new File(request.getSession().getServletContext().getRealPath(String.valueOf(PropertiesHolder.getContextProperty("user.folder.tmp.url")))+"/"+tempImgFile.getName());
				
				//上传图片到服务器
				String touploadurl = "http://Upload.kuke.com/v2upload";
				boolean uploadFlag = PicClientUtil.uploadFile(touploadurl, file , tempImgFile.getName() ,"folder");
				System.out.println("uploadFlag:"+uploadFlag);
				if(uploadFlag){//上传成功
					//保存夹子封面
					Map<String, String> params = new HashMap<String, String>();
					params.put("musicfolder_id", musicfolder_id);
					params.put("imgUrl",  tempImgFile.getName() );//文件名
					userCenterService.saveFavFolderImg(params);
					flag = true;
					code = "UPLOAD_SUCCESS";
					msg = "上传成功";
					data.put("url",String.valueOf(PropertiesHolder.getContextProperty("imgUrl")) + String.valueOf(PropertiesHolder.getContextProperty("user.folder.url")) + tempImgFile.getName() );
					//清楚cookie
					ICookie.clear(response, "userFolder");
				}
			}catch (Exception e) {
				e.printStackTrace();
				code = "UPLOAD_FAIL";
				msg = "上传失败";
				if(tempImgFile != null){
					try {
						FileUtils.forceDelete(tempImgFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}else{
			code = "NOMALLOGIN";
			msg = "用户未登录";
		}
		return new ResponseMsg(flag, code, msg, codeDesc,data);
	}
	/**
	 * 得到夹子封面
	 * @return
	 */
	private List<UserFolder> getFolderImg(List folderList){
		if(folderList != null){
			List<UserFolder> list = new ArrayList<UserFolder>();
			for(int i = 0; i < folderList.size(); i++){
				UserFolder folder =(UserFolder) folderList.get(i);
				String item_code = "";
				String source_id = dealNull(folder.getSource_id());
				if("1".equals(dealNull(folder.getType()))){//单曲
					if(!"".equals(source_id)){//资源不为空
						item_code = source_id.split("\\|").length > 0?source_id.split("\\|")[1]:"";//单曲ID|专辑ID
					}
					//设置专辑封面
					if(!"".equals(item_code) && item_code.length() > 0){
						folder.setItemcodeurl(ImageUrlUtil.getItemCodeImage(item_code));
					}
				}else if("2".equals(dealNull(folder.getType()))){//专辑
					if(!"".equals(source_id)){
						item_code = source_id;
					}
				}
				//设置夹子封面
				if("".equals(dealNull(folder.getImgurl()))){//如果未上传,则用专辑封面
					if(!"".equals(item_code) && item_code.length() > 0){
						folder.setImgurl(ImageUrlUtil.getItemCodeImage(item_code));
					}
				}else{
					String imgUrl = ImageUrlUtil.getFolderImg(folder);
					folder.setImgurl(imgUrl);
				}
				list.add(folder);
			}
			return list;
		}else{
			return folderList;
		}
	}
	/**
	 * 得到夹子下专辑列表封面
	 * @return
	 */
	private List setCatalListData(List<CatalsOfFolder> list){
		if(list != null){
			List<CatalsOfFolder> result = new ArrayList<CatalsOfFolder>();
			for(int i = 0; i < list.size(); i++){
				CatalsOfFolder catalsOfFolder =(CatalsOfFolder) list.get(i);
				//专辑封面
				if(!"".equals(dealNull(catalsOfFolder.getSource_id()))){
					String item_code = dealNull(catalsOfFolder.getSource_id());
					catalsOfFolder.setImgurl(ImageUrlUtil.getItemCodeImage(item_code));
				}
				//专辑标题
				String tilte = dealNull(catalsOfFolder.getCtitle());
				if("".equals(tilte)){
					tilte = dealNull(catalsOfFolder.getTitle());
				}
				catalsOfFolder.setCatalTitle(StringUtil.formatTtile(tilte));
				result.add(catalsOfFolder);
			}
			return result;
		}else{
			return list;
		}
	}
	/**
	 * 单曲夹子详细信息
	 * 设置单曲名称，设置专辑名称
	 * @return
	 */
	private List<TracksOfFolder> setTrackData(List<TracksOfFolder> list){
		if(list != null){
			List<TracksOfFolder> result = new ArrayList<TracksOfFolder>();
			for(int i = 0; i < list.size(); i++){
				TracksOfFolder tracksOfFolder =(TracksOfFolder) list.get(i);
				//1.设置单曲名称
				String trackTitle = "";
				if(!"".equals(dealNull(tracksOfFolder.getCtitle()))){
					trackTitle = dealNull(tracksOfFolder.getCtitle());
				}else if(!"".equals(dealNull(tracksOfFolder.getTitle()))){
					trackTitle = dealNull(tracksOfFolder.getTitle());
				}else if(!"".equals(dealNull(tracksOfFolder.getTrackDesc()))){
					trackTitle = dealNull(tracksOfFolder.getTrackDesc());
				}else{
					tracksOfFolder.setTrackTitle("");
				}
				tracksOfFolder.setTrackTitle(StringUtil.formatTtile(trackTitle));
				
				//2.设置专辑名称
				String catalTitle = "";
				if(!"".equals(dealNull(tracksOfFolder.getMcctitle()))){
					catalTitle = dealNull(tracksOfFolder.getMcctitle());
				}else if(!"".equals(dealNull(tracksOfFolder.getMctitle()))){
					catalTitle = dealNull(tracksOfFolder.getMctitle());
				}
				tracksOfFolder.setCalogTitle(catalTitle.replaceAll("'", ""));
				
				result.add(tracksOfFolder);
			}
			return result;
		}else{
			return list;
		}
	}
	/**
	 * 得到夹子页面的html内容
	 * @param folderLists
	 * @param type
	 * @return
	 */
	private String getFolderHtml(List folderLists,String type){
		if(KuKeAuthConstants.FOLDERTRACK.equals(type)){//1:单曲
			StringBuffer html = new StringBuffer("<ol class=\"comSite274 comSite274a cf\">");//html信息
			for(int i = 1; i <= folderLists.size();i++){
				UserFolder f = (UserFolder) folderLists.get(i-1);
				html.append("<li class=\"comSite275 comSite275a\">");
				html.append("	  <div class=\"comSite276 pr\">");
				html.append("		<div><img src=\""+f.getImgurl()+"\" alt=\"\" width=\"210\" height=\"210\"></div>");
				html.append("		<span class=\"comSt53 comSt53c\" onclick=\"toDetail('1','"+f.getId()+"');\"  style=\"cursor: pointer;\"></span>");
				html.append("		<a href=\"javascript:void(0)\" class=\"comSt252 comSt252b\" onclick=\"toDlPlay('1','"+f.getId()+"','play');\"></a>");
				html.append("	  </div>");
				html.append("	  <div class=\"comSite261 comSite261a pr\">");
				html.append("	  	<p>"+f.getFoldername()+"（<span>"+f.getCountres()+"</span>）</p>");
				html.append("	  	<div class=\"comSite262 comSite262c\">");
				html.append("			<span title=\"添加到播放列表\" class=\"comSt253 comSt253c\" onclick=\"toDlPlay('1','"+f.getId()+"','add');\"></span>");
				html.append("			<span title=\"添加到单曲夹\" class=\"comSt254 comSt254c\" onclick=\"addToFolder('1','"+f.getId()+"','"+f.getCountres()+"');\"></span>");
				html.append("			<span title=\"删除\" class=\"comSt256 comSt256c\" onclick=\"showDel('1','"+f.getId()+"');\"></span>");
				html.append("	  	</div>");
				html.append("	  </div>");
				html.append("</li>");
				if(i % 4 == 0 && i != folderLists.size()){
					html.append("</ol>");
					html.append("<ol class=\"comSite274 comSite274a cf\">");
				}
			}
			html.append("</ol>");
			return html.toString();
		}else if(KuKeAuthConstants.FOLDERCATLOG.equals(type)){//2:专辑
			StringBuffer html = new StringBuffer("<ol class=\"comSite274 comSite274a cf\">");//html信息
			for(int i = 1; i <= folderLists.size();i++){
				UserFolder f = (UserFolder) folderLists.get(i-1);
				html.append("<li class=\"comSite275 comSite275a\">");
				html.append("	  <div class=\"comSite276 pr\">");
				html.append("		<div><img src=\""+f.getImgurl()+"\" alt=\"\" width=\"210\" height=\"210\"></div>");
				html.append("		<span class=\"comSt53 comSt53c\" onclick=\"toDetail('2','"+f.getId()+"');\"  style=\"cursor: pointer;\"></span>");
				html.append("		<a href=\"javascript:void(0)\" class=\"comSt252 comSt252b\" onclick=\"toDlPlay('2','"+f.getId()+"','play');\"></a>");
				html.append("	  </div>");
				html.append("	  <div class=\"comSite261 comSite261a pr\">");
				html.append("	  	<p>"+f.getFoldername()+"（<span>"+f.getCountres()+"</span>）</p>");
				html.append("	  	<div class=\"comSite262 comSite262c\">");
				html.append("			<span title=\"添加到播放列表\" class=\"comSt253 comSt253c\" onclick=\"toDlPlay('2','"+f.getId()+"','add');\"></span>");
				html.append("			<span title=\"添加到单曲夹\" class=\"comSt254 comSt254c\" onclick=\"addToFolder('2','"+f.getId()+"','"+f.getCountres()+"');\"></span>");
				html.append("			<span title=\"删除\" class=\"comSt256 comSt256c\" onclick=\"showDel('2','"+f.getId()+"');\"></span>");
				html.append("	  	</div>");
				html.append("	  </div>");
				html.append("</li>");
				if(i % 4 == 0 && i != folderLists.size()){
					html.append("</ol>");
					html.append("<ol class=\"comSite274 comSite274a cf\">");
				}
			}
			html.append("</ol>");
			return html.toString();
		}else{
			return "";
		}
	}
	/**
	 * 得到单曲夹详细页面的html
	 * @param tracksOfFolder
	 * @return
	 */
	private String getTrackFolderHtml(List tracksOfFolder,PageInfo pageInfo){
		tracksOfFolder = setTrackData(tracksOfFolder);
		StringBuffer html = new StringBuffer("");//html信息
		for(int i = 1; i <= tracksOfFolder.size();i++){
			TracksOfFolder t = (TracksOfFolder) tracksOfFolder.get(i-1);
			if("0".equals(t.getShowable())){//下架样式
				html.append("<div class=\"comSite251 cf comSite251b cOff04\">");
				html.append("	  <a href=\"javascript:void(0);\" class=\"comSt268\" onclick=\"tips('showOffMessage');\"></a>");
				html.append("	  <span class=\"comSt224 fl\"  style=\"width: 20px;\">"+(i+(pageInfo.getCurrentPage()-1)*pageInfo.getPageSize())+"</span>");
				html.append("	  <span class=\"comSt225 fl eps\" onclick=\"tips('showOffMessage');\">"+t.getTrackTitle()+"</span>");
				html.append("	  <span class=\"comSt226 fl pr\">");
				html.append("	  	<em>"+t.getTiming()+"</em>");
				html.append("	  	<span class=\"comSt227 comSt227b cf\"></span>");
				html.append("	  	<span class=\"isOff isOff03\">已下架</span>");
				html.append("	  </span>");
				html.append("	  <a href=\"javascript:void(0);\" onclick=\"tips('showOffMessage');\" class=\"comSt232 comSt232b fl eps\">"+t.getCalogTitle()+"</a>");
				html.append("	  <em title=\"删除\" class=\"comSt256 comSt256c1\" onclick=\"delSource('1','${item.source_id}')\"></em>");
				html.append("</div>");
			}else{
				html.append("<div class=\"comSite251 cf comSite251b\">");
				html.append("	  <a href=\"javascript:void(0);\" onclick=\"preTrackPlay('"+t.getLcode()+"');\" class=\"comSt268 comSt268a\"></a>");
				html.append("	  <span class=\"comSt224 fl\"  style=\"width: 20px;\">"+(i+(pageInfo.getCurrentPage()-1)*pageInfo.getPageSize())+"</span>");
				html.append("	  <span class=\"comSt225 fl eps\">"+t.getTrackTitle()+"</span>");
				html.append("	  <span class=\"comSt226 fl pr\">");
				html.append("	  	<em>"+t.getTiming()+"</em>");
				html.append("	  	<span class=\"comSt227 comSt227b cf\">");
				html.append("	  		<em title=\"添加到播放列表\" class=\"comSt228 comSt228b\" onclick=\"preTrackPlayAdd('"+t.getLcode()+"');\"></em>");
				html.append("	  		<em title=\"添加到单曲夹\" class=\"comSt229 comSt229b\" onclick=\"addSourceToFolder('1','"+t.getSource_id()+"','"+t.getMusicfolder_id()+"');\"></em>");
				html.append("			<em title=\"分享\" class=\"comSt230 comSt230b\" onclick=\"toShare('"+ImageUrlUtil.getItemCodeImage(t.getItemcode())+"','"+t.getTrackTitle()+"','"+t.getLcode()+"');\"></em>");
				html.append("			<em title=\"下载\" class=\"comSt231 comSt231b\" onclick=\"downloadOfFolder('1','"+t.getSource_id()+"','"+t.getTrackTitle()+"');\"></em>");
				html.append("	  	</span>");
				html.append("	  </span>");
				html.append("	  <a href=\"javascript:void(0);\" class=\"comSt232 comSt232b fl eps\" onclick=\"openCatalog();\">"+t.getCalogTitle()+"</a>");
				html.append("</div>");
			}
		}
		return html.toString();
	}
	/**
	 * 得到唱片夹详细页面的html
	 * @param tracksOfFolder
	 * @return
	 */
	private String getCatlogFolderHtml(List catalsOfFolder){
		catalsOfFolder = this.setCatalListData(catalsOfFolder);
		StringBuffer html = new StringBuffer("<ol class=\"comSite274 cf\">");//html信息
		for(int i = 1; i <= catalsOfFolder.size();i++){
			CatalsOfFolder c = (CatalsOfFolder) catalsOfFolder.get(i-1);
			if("0".equals(c.getShowable())){//下架样式
				html.append("<li class=\"comSite275 comSite275a cOff02 cOff02a\">");
				html.append("	  <div class=\"comSite276 pr\" onclick=\"tips('showOffMessage');\">");
				html.append("	  	<div><img onerror=\"javascript:this.src='/images/default_zhuanji_s.jpg';\" src=\""+c.getImgurl()+"\" alt=\"\" width=\"210\" height=\"210\"></div>");
				html.append("	    <span class=\"offBg01\"></span>");
				html.append("	    <span class=\"isOff isOff02\">已下架</span>");
				html.append("	  </div>");
				html.append("	  <div class=\"comSite261 pr\">");
				html.append("	  	<p  style=\"height: 39px;\">"+c.getCatalTitle()+"</p>");
				html.append("	  	<div class=\"comSite262 comSite262b\">");
				html.append("			<span class=\"comSt253 comSt253b\"></span>");
				html.append("			<span class=\"comSt254 comSt254b\"></span>");
				if("".equals(dealNull(c.getFid()))){
					html.append("			<span class=\"comSt267 comSt267b\"></span>");
				}else{
					html.append("			<span class=\"comSt267 comSt267b  comOn23 comOn30\"></span>");
				}
				html.append("			<span class=\"comSt255 comSt255b\"></span>");
				html.append("			<span title=\"删除\" class=\"comSt256\" onclick=\"delSource('2','"+c.getItemcode()+"')\"></span>");
				html.append("		</div>");
				html.append("	  </div>");
				html.append("</li>");
				if(i % 4 == 0 && i != catalsOfFolder.size()){
					html.append("</ol>");
					html.append("<ol class=\"comSite274 cf\">");
				}
			}else{
				html.append("<li class=\"comSite275 comSite275a\">");
				html.append("	  <div class=\"comSite276 pr\">");
				html.append("	  	<div><img onerror=\"javascript:this.src='/images/default_zhuanji_s.jpg';\" src=\""+c.getImgurl()+"\" alt=\"\" width=\"210\" height=\"210\"></div>");
				html.append("	  	<span class=\"comSt53 comSt53c\"  style=\"cursor: pointer;\"  onclick=\"openCatalog('"+c.getItemcode()+"');\"></span>");
				html.append("	    <a  href=\"javascript:void(0);\" onclick=\"preCatlogPlay('"+c.getItemcode()+"');\" class=\"comSt252 comSt252b\"></a>");
				html.append("	  </div>");
				html.append("	  <div class=\"comSite261 pr\">");
				html.append("	  	<p  style=\"height: 39px;\">"+c.getCatalTitle()+"</p>");
				html.append("	  	<div class=\"comSite262 comSite262b\">");
				html.append("			<span title=\"添加到播放列表\" class=\"comSt253 comSt253b\" onclick=\"preCatlogPlayAdd('"+c.getItemcode()+"');\"></span>");
				html.append("			<span title=\"添加到唱片夹\" class=\"comSt254 comSt254b\" onclick=\"addSourceToFolder('2','"+c.getItemcode()+"','"+c.getMusicfolder_id()+"');\"></span>");
				if("".equals(dealNull(c.getFid()))){
					html.append("			<span title=\"添加喜欢\" class=\"comSt267 comSt267b\" onclick=\"addFavourite('1','"+c.getItemcode()+"',this);\"></span>");
				}else{
					html.append("			<span title=\"取消喜欢\" class=\"comSt267 comSt267b  comOn23 comOn30\" onclick=\"cancleFavourite('1','"+c.getItemcode()+"',this);\"></span>");
				}
				html.append("			<span title=\"下载\" class=\"comSt255 comSt255b\" onclick=\"downloadOfFolder('2','"+c.getItemcode()+"','"+c.getCatalTitle()+"');\"></span>");
				html.append("			<span title=\"删除\" class=\"comSt256 comSt256b\" onclick=\"delSource('2','"+c.getItemcode()+"')\"></span>");
				html.append("		</div>");
				html.append("	  </div>");
				html.append("</li>");
				if(i % 4 == 0 && i != catalsOfFolder.size()){
					html.append("</ol>");
					html.append("<ol class=\"comSite274 cf\">");
				}
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
		String item_code = "";
		String source_id = "10012001_06|100.105-2";
		System.out.println(source_id.indexOf("|") >= 0);
		System.out.println(source_id.split("\\|")[1]);
		if(!"".equals(source_id)){//资源不为空
			item_code = source_id.split("\\|").length > 0?source_id.split("\\|")[1]:"";//单曲ID|专辑ID
		}
		System.out.println(item_code);
	}
}
