/**
 * 
 */
package com.kuke.pay.controller;

import com.kuke.auth.util.KuKePayConstants;
import com.kuke.auth.util.PayOauth;
import com.kuke.auth.util.PropertiesHolder;
import com.kuke.pay.bean.PayBill;

/**
 *@Description: TODO(用线程更新权限,调取权限接口)
 * @author lyf
 * @date 2013-4-26 下午04:48:14
 * @version V1.0 
 */
public class UpdataAuthThread extends Thread {
	
	private PayBill payBill;
	
	public UpdataAuthThread(PayBill payBill) {
		this.payBill = payBill;
	}

	@Override
	public void run() {
		
		String retStr = "";
		
		PayOauth payOauth = new PayOauth();
		//判断加密 ：订单号  支付状态  订单数量   md5
		String key = String.valueOf(PropertiesHolder.getContextProperty("passKey"));
		payOauth.SetAuth(payBill.getUser_id(),payBill.getKeyword(), String.valueOf(payBill.getPay_status()),"1", key);
		
		int type = payBill.getBill_type();
		//1支付 0充值
		if(type != 0){
			// 支付  
			//参数  订单想的pay_channel:id  itemid item类型  item_num 单价
			String channels = payBill.getPay_channel_id();//pay_channel:id
			String itemIds = payBill.getItem_id();//itemid
			String channelType = payBill.getPay_channel_type();
			String itemType = "";
			if(!"".equals(channelType) && channelType != null){
				if(channelType.equals("1")){//音频
					itemType = KuKePayConstants.PAY_AUDIO;
				}else if(channelType.equals("3")){//视频
					itemType = KuKePayConstants.PAY_VIDEO;
				}else if(channelType.equals("4")){//live
					itemType = KuKePayConstants.PAY_LIVE;
				}else if(channelType.equals("5")){//下载
					itemType = KuKePayConstants.PAY_DOWNLOAD;
				}
			}
			String itemNums = payBill.getPro_price_num();
			String unitPrices = payBill.getPro_price();
			
			try {
				retStr = payOauth.updateAuth(channels,itemIds,itemType,itemNums,unitPrices);
			} catch (Exception e) {
				
			}
			
		}else{
			//充值
		}

		
	}
}
