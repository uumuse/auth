package com.kuke.authorize.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuke.auth.util.KuKePayConstants;
import com.kuke.auth.util.PayOauth;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.authorize.service.PayAuthorizeService;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.base.BaseController;
import com.kuke.util.MD5;
import com.kuke.util.MessageFormatUtil;

@Controller
@RequestMapping("/kuke/authorize/pay")
public class PayAuthController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(VideoAuthController.class);
	
	@Autowired
	private PayAuthorizeService payAuthorizeService;
	/**
	 * 更新权限
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/update")
	public @ResponseBody ResponseMsg pay(HttpServletRequest request, HttpServletResponse response) {
		
		String retStr = KuKePayConstants.FAILED;// 返回参数
		String user_id = request.getParameter("uid");
		String bill_channel_id = request.getParameter("bill_channel_id");// 业务ID
		String bill_item_id = request.getParameter("bill_item_id");// 业务ID
		String bill_item_type = request.getParameter("bill_item_type");// 类型
		String bill_item_num = request.getParameter("bill_item_num");// 数量
		String bill_item_price = request.getParameter("bill_item_price");// 价格
		
		String bill_keyword = request.getParameter("keyWord");// 订单号
		String bill_num = request.getParameter("bill_num");// 订单项数量
		String bill_status = request.getParameter("bill_status");// 订单状态
		String md5_str = request.getParameter("md5_str");
		String key = String.valueOf(PropertiesHolder.getContextProperty("passKey"));
		String code = new MD5().getMD5ofStr(bill_keyword + bill_status + key+ user_id);
		
		if (!code.equals(md5_str)) {//md5验证
			return new ResponseMsg(false, "1", "md5_str未通过", "");
		}
		if ("1".equals(bill_num) && KuKePayConstants.PAY_MONEY.equals(bill_item_type)) {//充值
			try {
				retStr = payAuthorizeService.updateMoney(user_id,bill_item_num);
				retStr = KuKePayConstants.SUCCESS;
			} catch (Exception e) {
				retStr = KuKePayConstants.FAILED;
			}
		}else{//支付
			// 权限业务
			if (KuKePayConstants.PAY_AUDIO.equals(bill_item_type)) {
				if ("5".equals(bill_channel_id)) {//音频包月
					try{
						payAuthorizeService.updateAudioDate(user_id, bill_item_num);
						retStr = KuKePayConstants.SUCCESS;
					}catch (Exception e) {
						retStr = KuKePayConstants.FAILED;
					}
				}
			}else if (KuKePayConstants.PAY_VIDEO.equals(bill_item_type)){
				if ("30".equals(bill_channel_id)) {//视频包月
					try {
						payAuthorizeService.updateVideoDate(user_id,bill_item_num);
						retStr = KuKePayConstants.SUCCESS;
					} catch (Exception e) {
						retStr = KuKePayConstants.FAILED;
					}
				}else if ("31".equals(bill_channel_id)) {//视频点播 
					try {
						payAuthorizeService.updateVideo(user_id,bill_item_id);
						retStr = KuKePayConstants.SUCCESS;
					}catch (Exception e) {
						retStr = KuKePayConstants.FAILED;
					}
				}
			}else if (KuKePayConstants.PAY_LIVE.equals(bill_item_type)){
				if ("40".equals(bill_channel_id)) {//live（包月）
					try {
						payAuthorizeService.updateLiveDate(user_id,bill_item_num,bill_item_price);
						retStr = KuKePayConstants.SUCCESS;
					}catch (Exception e) {
						retStr = KuKePayConstants.FAILED;
					}
				} else if ("41".equals(bill_channel_id)) {//live（直播）
					try {
						payAuthorizeService.updateLive(user_id,bill_item_id, bill_item_price);
						retStr = KuKePayConstants.SUCCESS;
					}catch (Exception e) {
						retStr = KuKePayConstants.FAILED;
					}
				} else if ("42".equals(bill_channel_id)) {//live（点播）
					try {
						payAuthorizeService.updateVideo(user_id,bill_item_id);
						retStr = KuKePayConstants.SUCCESS;
					}catch (Exception e) {
						retStr = KuKePayConstants.FAILED;
					}
				}
			}else if (KuKePayConstants.PAY_DOWNLOAD.equals(bill_item_type)){
				//订单表增加字段isshow  下载的资源可查询订单表
//				if ("50".equals(bill_channel_id)) {//乐谱下载
//					try {
//						payAuthorizeService.updateDownAudio(user_id,bill_item_id, bill_channel_id);
						retStr = KuKePayConstants.SUCCESS;
//					}catch (Exception e) {
//						retStr = KuKePayConstants.FAILED;
//					}
//				} else if ("51".equals(bill_channel_id)) {//专辑下载
//					try {
//						payAuthorizeService.updateDownAudio(user_id,bill_item_id, bill_channel_id);
//						retStr = KuKePayConstants.SUCCESS;
//					}catch (Exception e) {
//						retStr = KuKePayConstants.FAILED;
//					}
//				} else if ("52".equals(bill_channel_id)) {//音频下载
//					try {
//						payAuthorizeService.updateDownAudio(user_id,bill_item_id, bill_channel_id);
//						retStr = KuKePayConstants.SUCCESS;
//					}catch (Exception e) {
//						retStr = KuKePayConstants.FAILED;
//					}
//				}
			}
		}
		return MessageFormatUtil.formatResultToObject(retStr);
	}
}
