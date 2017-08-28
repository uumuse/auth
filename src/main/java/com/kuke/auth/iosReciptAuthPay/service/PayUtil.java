package com.kuke.auth.iosReciptAuthPay.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kuke.auth.play.service.PlayService;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.KuKePayConstants;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.engine.ICookie;
import com.kuke.pay.mapper.PayBillMapper;
import com.kuke.pay.service.PayBillServiceImpl;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.PlayUtil;
import com.kuke.util.StringUtil;

@Component
public class PayUtil {
	static Logger logger = LoggerFactory.getLogger(PayUtil.class);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private PayBillServiceImpl payBillService;
	@Autowired
	private PlayService playService;
	@Autowired
	private PayBillMapper payBillMapper;
	/***
	 * 检查资源是否被下载
	 * @param request
	 * @param response
	 * @return
	 */
	public Object checkIsDownload(HttpServletRequest request,HttpServletResponse response){
//		User user = this.getLoginUser(String.valueOf(request.getAttribute("ssoid")));
		String uid = request.getParameter("uid");
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

			if(uid!=null&&!uid.equals("")){
				temp_id = uid;
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
				 //优先检查
					 tempList = payBillService.getUserBillCheck((uid==null&&uid.equals(""))?temp_id:uid, lcode, itemcode, f);
					 logger.info("个人订单表tempList================>"+tempList.toString());
					 if(!tempList.isEmpty()){
						 if(from!=null&&from.equals("m")){
							 return new ResponseMsg(true,"CLIENT_ALREADY_PAID","个人订单已存在","","");
						 }
					 }else{
						 if(from!=null&&from.equals("m")){
							 return new ResponseMsg(false,"2","资源未购买","","");
						 }
					 }
			}
			return new ResponseMsg(false,"2","资源未购买","","");
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
}
