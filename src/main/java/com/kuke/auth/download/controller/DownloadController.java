/**
 * 下载
 */
package com.kuke.auth.download.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.download.service.DownloadService;
import com.kuke.auth.downloadAuth.service.DownloadAuthService;
import com.kuke.auth.login.bean.User;
import com.kuke.auth.play.service.PlayService;
import com.kuke.auth.ssologin.bean.OrgDownIp;
import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.auth.ssologin.quartz.org.RetrievalOrg;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKePayConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.auth.util.OrgOauth;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.core.engine.ICookie;
import com.kuke.pay.mapper.PayBillMapper;
import com.kuke.pay.service.PayBillServiceImpl;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.MessageFormatUtil;
import com.kuke.util.PlayUtil;
import com.kuke.util.StringUtil;

@Controller
@RequestMapping("/kuke/play")
public class DownloadController extends BaseController{
	static Logger logger = LoggerFactory.getLogger(DownloadController.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private DownloadAuthService downloadAuthService;
	@Autowired
	private DownloadService downloadService;
	@Autowired
	private PayBillServiceImpl payBillService;
	@Autowired
	private PlayService playService;
	@Autowired
	private PayBillMapper payBillMapper;



	//使用机构下载,判断使用IP段下载方式还是开放账号（全部开放或分配账号）,传type,及price_id
	@SuppressWarnings("unused")
	@ResponseBody
	@RequestMapping("/getAuthDownloadUrl")
	private Object download(HttpServletRequest request,HttpServletResponse response) {
		String pay_pro_price_id = request.getParameter("pay_pro_price_id");
		String ssoid = request.getParameter("ssoid");
		String itemcode = request.getParameter("itemcode");
		String lcode = request.getParameter("lcode");
		//1:音频 2：有声读物
		String product_id = "1";
		
		String type = request.getParameter("type");
		String from = request.getParameter("from");
		String from_client = request.getParameter("from_client");
		
		String f = request.getParameter("f");
		if(f!=null&&f.equals("c")&&(pay_pro_price_id!="509")){
			type = "1";
		}
		if(f!=null&&f.equals("c")&&(pay_pro_price_id=="509")){
			type = "4";
		}
		
		//优先IP下载
		Object r = byOrgIp(request, response, product_id, pay_pro_price_id, lcode,itemcode, f, type, from, from_client,ssoid);
		System.out.println(r);
		return r;

	}
	
	/***
	 * 检查资源是否被下载
	 * @param request
	 * @param response
	 * @return
	 */
	public Object checkIsDownload(HttpServletRequest request,HttpServletResponse response){
		User user = this.getLoginUser(String.valueOf(request.getAttribute("ssoid")));
		
		String pay_pro_price_id = StringUtil.dealNull(request.getParameter("pay_pro_price_id"));
		String itemcode = StringUtil.dealNull(request.getParameter("itemcode"));
		String lcode = StringUtil.dealNull(request.getParameter("lcode"));
		
		
		String from = StringUtil.dealNull(request.getParameter("from"));
		String temp_id = "";
		String url = "";
		
		String productid = "";//1:音频  2:有声读物  3视频  4直播 5乐谱
		if(pay_pro_price_id.equals(KuKePayConstants.AUDIODOWN)){//专辑下音频下载
			productid = "1";
		}else if(pay_pro_price_id.equals(KuKePayConstants.CATALDOWN)){//专辑下载
			productid = "1";
		}else if(pay_pro_price_id.equals(KuKePayConstants.MUSICBOOKDOWN)){//乐谱下载
			productid = "1";
		}else if(pay_pro_price_id.equals(KuKePayConstants.SPOKENDOWN)){//有声读物下载
			productid = "1";
		}else if(pay_pro_price_id.equals(KuKePayConstants.SPOKENAUDIODOWN)){//有声读物下音频下载
			productid = "1";
		}

			if(user!=null&&!user.getUid().equals("")){
				temp_id = user.getUid();
			}else{
				//查询机构下载log(ip下载),根据临时id查询机构订单如果在48小时内有效
				//过期则重新扣费
				temp_id = ICookie.get(request, KuKeAuthConstants.ORG_DOWN_REC);
			}
			logger.info("检查被下载id====================>"+temp_id);
			String f = "";
			String itemid = "";
			List<Map<String,String>> tempList = new ArrayList<Map<String,String>>(); 
			if(null != temp_id){
				if(("511,513").indexOf(pay_pro_price_id)>=0){
					//单曲
					f  = "1";
					itemid = lcode;
					url = getDownLoadItemUrl(pay_pro_price_id, lcode);
				}
				if(("510,512").indexOf(pay_pro_price_id)>=0){
					f  = "2";
					itemid = itemcode;
					url = getDownLoadItemUrl(pay_pro_price_id, itemcode);
				}
				if(("509").indexOf(pay_pro_price_id)>=0){
					f  = "3";
					itemid = itemcode;
					url = getDownLoadItemUrl(pay_pro_price_id, itemcode);
				}
				 //优先检查机构订单表
				 tempList = payBillService.getPayBillByTempId(temp_id,lcode,itemcode,f);
				 logger.info("机构订单表tempList================>"+tempList.toString());
				 if(!tempList.isEmpty()){
					 String oper_date = String.valueOf(tempList.get(0).get("last_date"));
					 itemid = String.valueOf(tempList.get(0).get("down_id"));
					 Date date = null;
					try {
						date = sdf.parse(oper_date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					 //订单超过48小时
					 if(new Date().getTime()>(date.getTime() + 48 * 60 * 60 * 1000)){
						//将订单置为隐藏
						 payBillMapper.updateOrgBillStatusByTempId(temp_id,itemid);
						 //web端置空cookie
//						 return new ResponseMsg(false,"2","资源未购买","","");
						 tempList = payBillService.getUserBillCheck(user==null?temp_id:user.getUid(), lcode, itemcode, f);
						 logger.info("个人订单表tempList================>"+tempList.toString());
						 if(!tempList.isEmpty()){
							 if(from!=null&&from.equals("m")){
								 return new ResponseMsg(true,"14","支付成功","个人订单已存在",getParamMobile());
							 }
								 return new ResponseMsg(true,"CLIENT_ALREADY_PAID","个人订单已存在","",url);
						 }else{
//							 if(from!=null&&from.equals("m")){
//								 return new ResponseMsg(false,"2","资源未购买","","");
//							 }
							 return new ResponseMsg(false,"2","资源未购买","","");
						 }
					 }else{
						 if(from!=null&&from.equals("m")){
							 return new ResponseMsg(true,"14","支付成功","机构订单已存在",getParamMobile());
						 }
						 return new ResponseMsg(true,"ORG_ALREADY_PAID","机构订单已存在","",url);
					 }
				 }
				 //检查个人订单
				 else{
					 tempList = payBillService.getUserBillCheck(user==null?temp_id:user.getUid(), lcode, itemcode, f);
					 logger.info("个人订单表tempList================>"+tempList.toString());
					 if(!tempList.isEmpty()){
						 if(from!=null&&from.equals("m")){
							 return new ResponseMsg(true,"CLIENT_ALREADY_PAID","个人订单已存在","",getParamMobile());
						 }
						 return new ResponseMsg(true,"CLIENT_ALREADY_PAID","个人订单已存在","",url);
					 }else{
						 if(from!=null&&from.equals("m")){
							 return new ResponseMsg(false,"2","资源未购买","",getParamMobile());
						 }
						 return new ResponseMsg(false,"2","资源未购买","","");
					 }
				 }
			}
			return new ResponseMsg(false,"2","资源未购买","","");
	}
		


	
	// 处理机构IP下载
	public Object byOrgIp(HttpServletRequest request,HttpServletResponse response,String product_id,
								String pay_pro_price_id,String lcode,String itemcode,String f,String type,
								String from,String from_client,String ssoid) {
		logger.info("%%%%%%%%%%%%%%%%%%%%%%%%机构IP下载开始====================机构IP下载开始=====================%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			//检查是否存在订单
			request.setAttribute("ssoid", ssoid);
			ResponseMsg r = (ResponseMsg)checkIsDownload(request, response);
			String code = r.getCode();
			if(from!=null&&from.equals("m")){
				//移动端检查订单已存在
				if(code!=null&&("14").indexOf(code)>=0){
					return r;
				}
			}
			if(code!=null&&("ORG_ALREADY_PAID,CLIENT_ALREADY_PAID").indexOf(code)>=0){
				return r;
			}
			//User user = this.getLoginUser(ssoid);
			String ip = StringUtil.dealNull(request.getParameter("downip"));
			System.out.println("机构下载中获取的IP========================>："+ip);
			if("".equals(ip)){
				ip = OrgOauth.getIp(request);
			}
			Organization org = null;
			BigDecimal finalPrice = new BigDecimal(0);
			try {
				org = OrgOauth.orgLogin(request, response);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// 根据内存中的机构下载ip信息， 确定ip属于那个机构下载ip账户
			//item_name,item_id,price_id,from_client,payType(8机构用户分配余额，9机构分配余额10机构ip分配余额)
			//org_id,ip,org_down_ip_id
			OrgDownIp orgDownIp = RetrievalOrg.getOrgDownIp(ip);
			if(orgDownIp!=null&&orgDownIp.getStart_ip()!=null){
				orgDownIp  = downloadAuthService.getDownIpById(orgDownIp.getStart_ip(), orgDownIp.getEnd_ip());
				if (StringUtils.isNotBlank(ip) && org != null && orgDownIp != null &&org.getOrg_id() !=null
						&& orgDownIp.getOrg_id().equals(org.getOrg_id())
						&& StringUtils.isNotEmpty(itemcode)
						&& StringUtils.isNotEmpty(pay_pro_price_id)) {
					
					String org_down_ip_id = String.valueOf(orgDownIp.getId());
					
					finalPrice = getFinalPrice(itemcode,pay_pro_price_id);
					
					Double leftMoney =  orgDownIp.getMoney();
					if (BigDecimal.valueOf(leftMoney).compareTo(finalPrice) != -1) {
						Map<String,String> params = new HashMap<String,String>();
						//IP段下载
						String payType = "10";
						params.put("ip", ip);
						params.put("payType", payType);
						
						params.put("product_id", product_id);
						params.put("org_down_ip_id", org_down_ip_id);
						params.put("orgid", orgDownIp.getOrg_id());
						return new ResponseMsg(false, "10", "机构IP余额足够", "10:机构IP余额足够,11:机构开放余额足够，12:机构指定账号余额足够,13:个人余额足够",params);
						// 记录机构下载日志
					}
				}
			}
			//匹配用户所属机构余额
			return byOrgUser(request, response, pay_pro_price_id, lcode,itemcode, product_id, pay_pro_price_id, finalPrice,f,type,ssoid);

	}
	
	
	//机构全部或指定用户可用
	public Object byOrgUser(HttpServletRequest request,HttpServletResponse response,String channel_id,String lcode,
			String itemcode,String product_id,String price_id,BigDecimal finalPrice,String f,String type,String ssoid){
		logger.info("%%%%%%%%%%%%%%%%%%%%%%%%机构用户开始====================机构用户开始=====================%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		Object msg = null;
		boolean flag = true;
		String from = request.getParameter("from");
		String pay_pro_price_id = request.getParameter("pay_pro_price_id");
		User user = this.getLoginUser(ssoid);
		logger.info("%%%%%%%%%%%%%%%%%%%%%%%%机构用户开始::user:::"+user);
		if(user==null||user.getUid().equals("")){
			return MessageFormatUtil.noFormatObject(KuKeAuthConstants.NOMALLOGIN, null);
		}else{
			//检查是否存在订单
			ResponseMsg r = (ResponseMsg)checkIsDownload(request, response);
			logger.info("%%%%%%%//检查是否存在订单%%%%%"+r.toString());
			String code = r.getCode();
			
			
			if(from!=null&&from.equals("m")){
				if(code!=null&&("14").indexOf(code)>=0){
					return r;
				}
			 }
			
			
			
			if(code!=null&&("ORG_ALREADY_PAID,CLIENT_ALREADY_PAID").indexOf(code)>=0){
				return r;
			}
			
			
			Map<String,String> params = new HashMap<String,String>();
			String org_id = user.getOrg_id(); 
			if("".equals(dealNull(org_id))){
				return byUser(request, response, finalPrice, from, type, price_id, lcode,itemcode,ssoid);
			}else{
				List<Map<String,String>> list = downloadService.getAssignInfo(org_id, product_id);
				String assignMoney = "";
				String assignClientMoney = "";
				
				Date org_audio_date = getOrgNaxosDate(list,org_id, product_id);
				//机构权限已过期，走个人余额
				if(org_audio_date == null || org_audio_date.before(new Date())){
					return byUser(request, response, finalPrice, from, type, price_id, lcode,itemcode,ssoid);
				}
				if(!list.isEmpty()){
					String all_flag = list.get(0).get("all_flag");
					String ip = OrgOauth.getIp(request);
					logger.info("机构用户ip=====================ip:"+ip);
					params.put("ip", ip);
					
					params.put("product_id", product_id);
					params.put("org_down_ip_id", "");
					params.put("orgid", user.getOrg_id());
					
					finalPrice = getFinalPrice(itemcode,pay_pro_price_id);
					//机构下用户全部可用
					if(all_flag.equals("1")){
						if(user.getOrg_id().equals(org_id)){
							assignMoney = String.valueOf(list.get(0).get("assign_money"));
							if(new BigDecimal(assignMoney).compareTo(finalPrice)>=0){
								flag = false;
								//机构分配余额
								String payType = "9";
								params.put("payType", payType);
								return new ResponseMsg(false, "11", "机构开放余额足够", "10:机构IP余额足够,11:机构开放余额足够，12:机构指定账号余额足够,13:个人余额足够",params);
							}
						}
					}else{
						//机构下指定用户可用
						if(user.getOrg_id().equals(org_id)){
							assignClientMoney = String.valueOf(list.get(0).get("assign_client_money"));
							System.out.println("getAssignUserInfo:assignClientMoney==============>"+assignClientMoney);
							System.out.println("getAssignUserInfo:finalPrice==============>"+finalPrice);
							//查询此用户是否在机构分配池中
							int count = downloadService.getAssignUserInfo(org_id, product_id, user.getUid());
							System.out.println("downloadService.getAssignUserInfo:count===============>"+count);
							if((count>=1)&&(new BigDecimal(assignClientMoney).compareTo(finalPrice)>=0)){
								flag = false;
								//机构分配账户余额
								String payType = "8";
								params.put("payType", payType);
								return new ResponseMsg(false, "12", "机构开放指定用户余额足够", "10:机构IP余额足够,11:机构开放余额足够，12:机构指定账号余额足够,13:个人余额足够",params);
							}
						}
					}
				}
				if(flag){
					//个人余额
					if(from!=null&&from.equals("m")){
						msg = byUserForM(request,response,finalPrice,f,type,price_id,lcode,itemcode,ssoid);
					}else
						msg = byUser(request,response,finalPrice,f,type,price_id,lcode,itemcode,ssoid);
				}
			  }
			}
		return msg;
		}
	

	
	/***
	 * 移动端个人，不能在点确认之后再次跳到第三方接口，必须重新点下载按钮重走流程
	 * @param request
	 * @param response
	 * @param finalPrice
	 * @param f
	 * @param type
	 * @param price_id
	 * @param lcode
	 * @param itemcode
	 * @param ssoid
	 * @return
	 */
	public Object byUserForM(HttpServletRequest request,HttpServletResponse response, BigDecimal finalPrice,String f,String type,String price_id,
			String lcode,String itemcode,String ssoid){

		String from = request.getParameter("from");
		User user = this.getLoginUser(ssoid);
		if(user==null){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.NOMALLOGIN, null), HttpStatus.OK);
		}else{
				
				//检查是否存在订单
				ResponseMsg r = (ResponseMsg)checkIsDownload(request, response);
				String code = r.getCode();
				if(code!=null&&("14").indexOf(code)>=0){
					return r;
				}
				
				if(type.equals("1")){
					//如资源已被下载则返回true
						if(f!=null&&f.equals("c")){
							return new ResponseMsg(false, "15", "生成订单失败", "15:生成订单失败,16:生成订单成功,13:个人余额足够","");
						}
						return new ResponseMsg(false, "13", "个人余额足够", "10:机构IP余额足够,11:机构开放余额足够，12:机构指定账号余额足够,13:个人余额足够");
				}
				
				else if(type.equals("4")){
					
						if(f!=null&&f.equals("c")){
							return new ResponseMsg(false, "15", "生成订单失败", "15:生成订单失败,16:生成订单成功,13:个人余额足够","");
						}
						return new ResponseMsg(false, "13", "个人余额足够", "10:机构IP余额足够,11:机构开放余额足够，12:机构指定账号余额足够,13:个人余额足够");
				}else{
					return new ResponseMsg(false, "13", "个人余额足够", "10:机构IP余额足够,11:机构开放余额足够，12:机构指定账号余额足够,13:个人余额足够");
				}
		}
	
	}
	/***
	 * 个人余额下载
	 * @param request
	 * @param finalPrice
	 * @return
	 */
	public Object byUser(HttpServletRequest request,HttpServletResponse response, BigDecimal finalPrice,String f,String type,String price_id,
			String lcode,String itemcode,String ssoid){
		String from = request.getParameter("from");
		User user = this.getLoginUser(ssoid);
		if(user==null){
				return new ResponseEntity<ResponseMsg>(MessageFormatUtil.formatStateToObject(KuKeAuthConstants.NOMALLOGIN, null), HttpStatus.OK);
		}else{
				//检查是否存在订单
				ResponseMsg r = (ResponseMsg)checkIsDownload(request, response);
				String code = r.getCode();
				if(code!=null&&("ORG_ALREADY_PAID,CLIENT_ALREADY_PAID").indexOf(code)>=0){
					return r;
				}
				//如资源已被下载则返回true
				return new ResponseMsg(false, "13", "个人余额足够", "10:机构IP余额足够,11:机构开放余额足够，12:机构指定账号余额足够,13:个人余额足够");
				
		}
	}

	
		protected Map getDownloadItemInfo(String item_id,String pay_pro_price_id){
			Map descMap = downloadAuthService.getDownloadItemDesc(item_id,pay_pro_price_id);
			return descMap;
		}
		
		public BigDecimal getFinalPrice(String item_id,String pro_price_id){
//			Map descMap = getDownloadItemInfo(item_id, pro_price_id);
			BigDecimal finalPrice = new BigDecimal(0);
//			if (descMap != null) {
		//		descMap = initReturnMap(jsonObject, item_id, channel_id,
		//				descMap);
				// 扣除账户的钱 对应表 auth.org_down_ip
				if (("510,512").indexOf(pro_price_id)!=-1) {
					Map descMap = getDownloadItemInfo(item_id, pro_price_id);
					finalPrice = (BigDecimal) descMap.get("pro_price");
					Object o =  descMap.get("no_cd");
					String nocd = "";
					if(o != null){
						nocd = String.valueOf(o);
						finalPrice = new BigDecimal(Double.toString(KuKePayConstants.CD_PRICE * (Integer.parseInt(nocd))));;
					}
				}else if(("511,513").indexOf(pro_price_id)!=-1){
					finalPrice = BigDecimal.valueOf(KuKePayConstants.TRACK_PRICE);
				}else if(("509").equals(dealNull(pro_price_id))){
					double num = 0;
					//查询乐谱的数量
					Map<String, String> map = payBillMapper.getMusicBookObjById(item_id);
					if(map != null){
						num = Double.valueOf(map.get("page"));//按张收费
					}
					finalPrice = BigDecimal.valueOf(KuKePayConstants.MBOOK_PRICE*num);
				}
//			}
			return finalPrice;
		}
		
	/**
	* 获得下载item的url
	* @return
	*/
	private String getDownLoadItemUrl(String pay_pro_price_id,String itemcode){
		String url = "";
		try {
			if(KuKePayConstants.MUSICBOOKDOWN.equals(pay_pro_price_id)){//乐谱下载
				Map<String,String> book = playService.getMusicBookById(itemcode);
				String music_book_file = book.get("music_book_file"); 
				logger.info("payByOrgType music_book_file:"+music_book_file);
				
				Map<String,String> map = new HashMap<String,String>();
				url = PlayUtil.initMusicBookPlayUrl(music_book_file);
			}else if(KuKePayConstants.AUDIODOWN.equals(pay_pro_price_id) || KuKePayConstants.SPOKENAUDIODOWN.equals(pay_pro_price_id)){//单曲下载
				String post_url = KuKeUrlConstants.getPlayUrl;//   /kuke/play/getPlayUrl
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("type", "1"));
				nvps.add(new BasicNameValuePair("lcode", itemcode));
				logger.info("请求=============================>>>>>>>>>>payByOrgType getDownLoadItemUrl type:1 lcode:"+itemcode);
				String result = HttpClientUtil.executeServicePOST(post_url, nvps);
				logger.info("请求=============================>>>>>>>>>>payByOrgType getDownLoadItemUrl result:"+result);
				ResponseMsg res = new ResponseMsg(result);
				if(res.getFlag()){
					JSONObject json = JSONObject.fromObject(res.getData().toString());
					url = json.getString("url");
				}
			}else{
				logger.info("payByOrgType getDownLoadItemUrl pay_pro_price_id:"+pay_pro_price_id+" 不支持");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
	
	
	/**
	 * 处理空字符串
	 * @param str
	 * @return
	 */
	private String dealNull(String str){
		if(str == null || "".equals(str.trim()) || "null".equals(str.trim())){
			str = "";
		}
		return str.trim();
	}
	
	
	public Map<String,String> getParamMobile(){
		Map<String,String> params = new HashMap<String,String>();
		params.put("orgid", "");
		params.put("product_id", "");
		params.put("org_down_ip_id", "");
		params.put("payType", "");
		params.put("ip", "");
		return params;
	}
	
	
	
	/***
	 * 
	 * @param list
	 * @param org_id
	 * @param product_id
	 * @return
	 */
	public Date getOrgNaxosDate(List<Map<String,String>> list,String org_id,String product_id){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date org_audio_date = null;
		if(!list.isEmpty()){
			String all_flag = list.get(0).get("all_flag");
			List<Map<String,String>> orgDate = playService.getOrgDate(org_id);
			String naxosDate = "";
			String spokenDate = "";
			for(Map<String,String> map :orgDate){
				if(String.valueOf(map.get("channel_id")).equals("1")){
					naxosDate = String.valueOf(map.get("end_date"));
				}
				if(String.valueOf(map.get("channel_id")).equals("2")){
					spokenDate = String.valueOf(map.get("end_date"));
				}
			}
			try {
				org_audio_date = sdf.parse(naxosDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return org_audio_date;
	}
}
