package com.kuke.auth.iosReciptAuthPay.util;

import java.util.Date;

import net.sf.json.JSONObject;

import com.kuke.common.utils.ResponseMsg;

public class IosCheck {
	
	public static ResponseMsg check(String receipt){
		ResponseMsg msg = new ResponseMsg(false,"11","订单校验失败","订单校验失败;");
		for(int i=0;i<3;i++){
			JSONObject obj = null;
			int start = new Date().getSeconds();
			try {
				boolean flag = true;
				obj = IosUtil.verifyReceipt(receipt,true);
				String status = obj.getString("status");
				if(!status.equals("") && status.equals("21007")){
					flag = false;
					obj = IosUtil.verifyReceipt(receipt,false);
				}
				int end = new Date().getSeconds();
				if(end-start>=30){
//					if(flag){
						obj = IosUtil.verifyReceipt(receipt,true);
//					}else{
//						obj = IosUtil.verifyReceipt(receipt,false);
//					}
//					obj = IosUtil.verifyReceipt(receipt,true);
//					status = obj.getString("status");
//					if(!status.equals("") && status.equals("21007")){
//						obj = IosUtil.verifyReceipt(receipt,false);
//					}
				}
			} catch (Exception e) {
				return msg;
			}
			String status = obj.getString("status");
			if(status.equals("0")){
				return new ResponseMsg(true,"10","订单校验成功","订单校验成功;");
//				break;
			}else{
				return msg;
			}
		}
		return msg;
	
	}

}
