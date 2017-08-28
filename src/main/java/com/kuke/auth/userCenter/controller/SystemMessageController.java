package com.kuke.auth.userCenter.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.regist.mapper.UserMapper;
import com.kuke.auth.userCenter.bean.UserMessage;
import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.util.MessageFormatUtil;
import com.kuke.util.PageInfo;
import com.kuke.util.StringUtil;

@Controller
@RequestMapping(value = "/kuke/userCenter")
public class SystemMessageController extends BaseController{
	
	@Autowired
	private UserCenterService userCenterService;
	
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 系统消息
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getSysMessageList")
	public Object getSysMessageList(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			//设置个人信息
			this.getUserInfo(request, response);
			//设置参数当前登录用户ID
			params.put("userId", this.getLoginUser().getUid());
			if(!"m".equals(from)){
				//设置参数每页显示数量
				params.put("pageSize", "5");
			}
			PageInfo pageInfo = getServicePageInfo(userCenterService,"getSysMessageList", params);
			List sysMessageList = pageInfo.getResultList();
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, pageInfo), HttpStatus.OK);
			}else{
				request.setAttribute("sysMessageList", sysMessageList);//页面信息数量
				request.setAttribute("pageInfo", pageInfo);//页面信息数量
				request.setAttribute("count", pageInfo.getResultCount());//页面信息数量
				return "/sysMessage/sysMessageList";
			}
		}else{
			if("m".equals(from)){
				return MessageFormatUtil.formatStateToObject(KuKeAuthConstants.NOMALLOGIN, null);
			}else{
				return KuKeUrlConstants.userLogin_url;
			}
		}
	}
	/**
	 * 系统消息（未读消息数量,ajax）
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getNoReadSysMessage")
	public @ResponseBody ResponseMsg getNoReadSysMessage(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		boolean flag = false;
		String code = "FAILED";
		String msg = "FAILED";
		String codeDesc = "SUCCESS:成功;"
						+ "FAILED:失败;"
						+ "NOLMALLOGIN:未登录;";
		Map<String, String> data = new HashMap<String, String>();
		try {
			if (this.getLoginUser() != null) {
				int count = userCenterService.getNoReadSysMessage(this.getLoginUser().getUid());
				flag = true;
				code = "SUCCESS";
				msg = "SUCCESS";
				data.put("count", String.valueOf(count));
			}else{
				code = "NOLMALLOGIN";
				msg = "NOLMALLOGIN";
			}
		} catch (Exception e) {
			code = "FAILED";
			msg = "FAILED";
		}
		return new ResponseMsg(flag, code, msg, codeDesc, data);
	}
	/**
	 * 系统消息详细信息(单个系统消息)
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getSingleMessage")
	public Object getSingleMessage(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		if (this.getLoginUser() != null) {
			//更新此消息的未读状态为已读
			userMapper.updateUserMessage("1", dealNull(params.get("id")));
			
			//设置个人信息
			this.getUserInfo(request, response);
			
			//参数:from   id
			UserMessage userMessage = userCenterService.getSingleMessage(params);
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			if("m".equals(from)){
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("userMessage", userMessage);
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, result), HttpStatus.OK);
			}else{
				request.setAttribute("userMessage", userMessage);//页面信息数量
				return "/sysMessage/singleMessage";
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
	 * 删除系统消息（多个）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/delSysMessage")
	public @ResponseBody ResponseMsg delSysMessage(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> params = getParameterMap(request);
		boolean flag = false;
		String code = "";
		String msg = "";
		String codeDesc = "SUCCESS:成功;"
						+ "FAILED:失败;"
						+ "NOMALLOGIN:未登录;"
						+ "NOCHOOSE:未选择数据;";
		if (this.getLoginUser() != null) {
			//参数id:多个的话，英文逗号隔开
			String id = request.getParameter("id");//逗号分隔
			String[] ids = StringUtil.formatCommaForString(id).split(",");
			if(ids != null && ids.length > 0){
				//设置参数当前登录用户ID
				params.put("userId", this.getLoginUser().getUid());
				userCenterService.delSysMessage(params,ids);
				flag = true;
				code = "SUCCESS";
				msg = "SUCCESS";
			}else{
				code = "NOCHOOSE";
				msg = "NOCHOOSE";
			}
		}else{
			code = "NOMALLOGIN";
			msg = "NOMALLOGIN";
		}
		return new ResponseMsg(flag, code, msg, codeDesc);
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
