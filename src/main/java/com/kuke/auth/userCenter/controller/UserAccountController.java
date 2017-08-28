package com.kuke.auth.userCenter.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.regist.service.impl.RegistService;
import com.kuke.auth.userCenter.bean.UserBill;
import com.kuke.auth.userCenter.service.UserCenterService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.pay.bean.PayBill;
import com.kuke.pay.service.CommentService;
import com.kuke.pay.service.PayBillService;
import com.kuke.util.MessageFormatUtil;
import com.kuke.util.PageInfo;
import com.kuke.util.StringUtil;

@Controller
@RequestMapping(value = "/kuke/userCenter")
public class UserAccountController extends BaseController {
	
	@Autowired
	private RegistService registService;
	
	@Autowired
	private UserCenterService userCenterService;
	
	@Autowired
	private PayBillService payBillService;
	
	@Autowired
	private CommentService commentService;
	
	/**
	 * 进入我的账户页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userAccount")
	public Object userAccount(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		Map<String, Object> data = new HashMap<String, Object>();
		User user = this.getLoginUser();
		if (this.getLoginUser() != null) {
			//设置个人信息
			this.getUserInfo(request, response);
			//余额
			Map<String, String> userMoney = commentService.getUserMoneyByUid(this.getLoginUser().getUid());
			if(userMoney != null){
				user.setRemain_money("".equals(dealNull(userMoney.get("remain_money")))?"0.00":dealNull(userMoney.get("remain_money")));
				user.setOrg_money("".equals(dealNull(userMoney.get("org_money")))?"0.00":dealNull(userMoney.get("org_money")));
			}
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			if("m".equals(from)){
				data.put("userMoney",  userMoney);
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.SUCCESS, data), HttpStatus.OK);
			}else{
				request.setAttribute("userAccount","".equals(user.getUphone())?("".equals(user.getUemail())?"":user.getUemail()):user.getUphone());
				request.setAttribute("user", user);
				request.setAttribute("site", "0");
				request.setAttribute("position", "".equals(dealNull(params.get("position")))?"0":dealNull(params.get("position")));
				return "/userCenter/account/user_Account";
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
	 * 我的账户里:我的服务状态
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userServStatus")
	public Object userServStatus(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		Map<String, String> data = null;
		if (this.getLoginUser() != null) {
			//设置个人信息
			this.getUserInfo(request, response);
			
			//查询数据
			params.put("userId", this.getLoginUser().getUid());
			data = userCenterService.getUserAuthorize(params);
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.noFormatObject(KuKeAuthConstants.SUCCESS, data), HttpStatus.OK);
			}else{
				request.setAttribute("info", data);
				request.setAttribute("site", "2");//标签页位置
				return "/userCenter/account/user_service";
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
	 * 进入我的订单页
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userBill")
	public Object userBill(HttpServletRequest request,HttpServletResponse response) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> params = getParameterMap(request);
		String from = dealNull(params.get("from"));//m:移动端,否则网页端
		String from_client = dealNull(params.get("from_client"));//m:移动端,否则网页端
		User user = this.getLoginUser();
		if (this.getLoginUser() != null) {
			//设置个人信息
			this.getUserInfo(request, response);
			
			request.setAttribute("uid", this.getLoginUser().getUid());
			params.put("userId", user.getUid());
			//网页端设置为5
			if(!"m".equals(from)){
				params.put("pageSize", "5");
			}
			// 订单pageInfo
			PageInfo pageInfo = null;
			if("ios".equals(from_client)){
				pageInfo = getServicePageInfo(userCenterService,"getBillInfIOS", params);
			}else{
				pageInfo = getServicePageInfo(userCenterService,"getBillInf", params);
			}
			
			//更新订单状态为失效订单
			pageInfo = updatePayBillStatus(pageInfo);
			
			//记录日志
			LogUtil.addVisitLog(request);
			
			if("m".equals(from)){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.noFormatObject(KuKeAuthConstants.SUCCESS, pageInfo), HttpStatus.OK);
			}else{
				request.setAttribute("pageInfo", pageInfo);
				request.setAttribute("list", formatBillList(pageInfo.getResultList()));
				request.setAttribute("site", "1");
				return "/userCenter/account/user_bill";
			}
		}else{
			if("m".equals(from)){
				Map<String, Object> result = new HashMap<String, Object>();
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.NOMALLOGIN, result), HttpStatus.OK);
			}else{
				return KuKeUrlConstants.userLogin_url;
			}
		}
	}
	private List formatBillList(List list){
		List<UserBill> newList = new ArrayList<UserBill>();
		for(int i = 0; i < list.size();i++){
			UserBill userBill = (UserBill) list.get(i);
			if(userBill != null){
				String itemname = userBill.getItem_name();
				userBill.setItem_name(StringUtil.formatString(itemname));
				newList.add(userBill);
			}
		}
		return newList;
	}
	/**
	 * 更新订单状态为失效订单
	 * @param pageInfo
	 * @return
	 */
	private PageInfo updatePayBillStatus(PageInfo pageInfo) throws Exception{
		List newList = new ArrayList();
		List list = pageInfo.getResultList();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
		Date now = calendar.getTime();
		String keywords = "";
		try {
			String userid = "";
			//查询
			for(int i = 0; i < list.size(); i++){
				UserBill userBill = (UserBill) list.get(i);
				Date billDate = userBill.getCreate_date();
				if("1".equals(userBill.getPay_status())){//对于待支付的订单
					if(billDate.before(now)){
						userBill.setPay_status("0");
						keywords += "," + userBill.getKeyword();
					}
				}
				if("".equals(userid)){
					userid = userBill.getUser_id(); 
				}
				newList.add(userBill);
			}
			//更新
			keywords = StringUtil.formatCommaForString(keywords);
			if(!"".equals(keywords)){
				userCenterService.invalidBill(userid,keywords.split(","));
			}
			
			pageInfo.setResultList(newList);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return pageInfo;
	}
	/**
	 * 校验订单是否失效
	 * 
	 */
	@RequestMapping(value = "/queryLoseBill")
	public @ResponseBody ResponseMsg queryLoseBill(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String KeyWord = dealNull(request.getParameter("KeyWord"));
		boolean flag = false;
		try {
			PayBill payBill = payBillService.getPayBillByKeyword(KeyWord);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
			Date now = calendar.getTime();
			if(payBill.getCreate_date().before(now)){
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseMsg( flag, "");
	}
	/**
	 * 取消订单
	 * 
	 */
	@RequestMapping(value = "/cancleBill")
	public @ResponseBody ResponseMsg cancleBill(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		if (this.getLoginUser() != null) {
			try {
				com.kuke.auth.login.bean.User user = this.getLoginUser();
				//参数：keyWord,userId
				String keyWord = dealNull(params.get("keyWord"));
				params.put("userId", user.getUid());
				
				PayBill paybill = payBillService.getPayBillByKeyword(keyWord);
				if(paybill != null){
					if(!"2".equals(String.valueOf(paybill.getPay_status()))){
						int result = userCenterService.cancleBill(params);
						if (result != 0) {
							return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS);
						} else {
							return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
						}
					}else{
						return new ResponseMsg(false, "2", keyWord+"订单已支付成功,不能取消", "");
					}
				}else{
					return new ResponseMsg(false, "1", keyWord+"订单不存在", "");
				}
			} catch (Exception e) {
				e.printStackTrace();
				return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED);
			}
		} else {
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.NOMALLOGIN);
		}
	}
	/**
	 * 删除订单记录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteBill")
	public @ResponseBody ResponseMsg deleteBill(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map<String, String> params = getParameterMap(request);
		JSONObject jsonObjects = new JSONObject();
		User user = this.getLoginUser();
		if (this.getLoginUser() != null) {
			try {
				//参数：keyword,userId
				params.put("userId", user.getUid());
				int result = userCenterService.deleteBill(params);
				return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.SUCCESS); 
			} catch (Exception e) {
				e.printStackTrace();
				return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.FAILED); 
			}
		} else {
			return MessageFormatUtil.formatResultToObject(KuKeAuthConstants.NOMALLOGIN); 
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
