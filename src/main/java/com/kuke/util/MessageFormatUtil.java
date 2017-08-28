package com.kuke.util;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.common.utils.ResponseMsg;

public class MessageFormatUtil {
	
	/**
	 * 把对象转换为ResponseMsg,state为SUCCESS则成功,否则失败
	 * @param state
	 * @param data
	 * @param msg
	 * @return
	 */
	public static ResponseMsg noFormatObject(String state,Object data){
		ResponseMsg responseMsg = null;
		if(KuKeAuthConstants.SUCCESS.equals(state)){//成功
			responseMsg = new ResponseMsg(true, "1", "查询成功", "1:查询成功 ;2:查询失败;3.未登录;", data);
		}else if(KuKeAuthConstants.FAILED.equals(state)){//失败
			responseMsg = new ResponseMsg(false, "2", "查询失败", "1:查询成功 ;2:查询失败;3.未登录;", data);
		}else if(KuKeAuthConstants.NOMALLOGIN.equals(state)){//未登录
			responseMsg = new ResponseMsg(false,KuKeAuthConstants.NOMALLOGIN, "未登录", "1:查询成功 ;2:查询失败;"+KuKeAuthConstants.NOMALLOGIN+".未登录;", null);
		}else{//失败
			responseMsg = new ResponseMsg(false, "2", "查询失败", "1:查询成功 ;2:查询失败;3.未登录;", data);
		}
		return responseMsg;
	}
	/**
	 * 把对象转换为ResponseMsg,state为SUCCESS则成功,否则失败
	 * @param state
	 * @param data
	 * @param msg
	 * @return
	 */
	public static ResponseMsg formatStateToObject(String state,Object data){
		ResponseMsg responseMsg = null;
		JSONObject result = null;
		try {
			result = JSONObject.fromObject(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(KuKeAuthConstants.SUCCESS.equals(state)){//成功
			responseMsg = new ResponseMsg(true, "1", "查询成功", "1:查询成功 ;2:查询失败;3.未登录;", result);
		}else if(KuKeAuthConstants.FAILED.equals(state)){//失败
			responseMsg = new ResponseMsg(false, "2", "查询失败", "1:查询成功 ;2:查询失败;3.未登录;", result);
		}else if(KuKeAuthConstants.NOMALLOGIN.equals(state)){//未登录
			responseMsg = new ResponseMsg(false,KuKeAuthConstants.NOMALLOGIN, "未登录", "1:查询成功 ;2:查询失败;3.未登录;", null);
		}else{//失败
			responseMsg = new ResponseMsg(false, "2", "查询失败", "1:查询成功 ;2:查询失败;3.未登录;", result);
		}
		return responseMsg;
	}
	/**
	 * 把对象转换为ResponseMsg,state为SUCCESS则成功,否则失败
	 * @param state
	 * @param data
	 * @param msg
	 * @return
	 */
	public static ResponseMsg formatStateToJSONArray(String state,Object data){
		ResponseMsg responseMsg = null;
		JSONArray json = null;
		try {
			json = JSONArray.fromObject(data);
			if(KuKeAuthConstants.SUCCESS.equals(state)){//成功
				responseMsg = new ResponseMsg(true, "1", "查询成功", "1:查询成功 ;2:查询失败;3.未登录;", json.toString());
			}else if(KuKeAuthConstants.FAILED.equals(state)){//失败
				responseMsg = new ResponseMsg(false, "2", "查询失败", "1:查询成功 ;2:查询失败;3.未登录;", json.toString());
			}else if(KuKeAuthConstants.NOMALLOGIN.equals(state)){//未登录
				responseMsg = new ResponseMsg(false,KuKeAuthConstants.NOMALLOGIN, "未登录", "1:查询成功 ;2:查询失败;3.未登录;", null);
			}else{//失败
				responseMsg = new ResponseMsg(false, "2", "查询失败", "1:查询成功 ;2:查询失败;3.未登录;", json.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(KuKeAuthConstants.SUCCESS.equals(state)){//成功
				responseMsg = new ResponseMsg(true, "1", "查询成功", "1:查询成功 ;2:查询失败;3.未登录;", data);
			}else if(KuKeAuthConstants.FAILED.equals(state)){//失败
				responseMsg = new ResponseMsg(false, "2", "查询失败", "1:查询成功 ;2:查询失败;3.未登录;", data);
			}else if(KuKeAuthConstants.NOMALLOGIN.equals(state)){//未登录
				responseMsg = new ResponseMsg(false,KuKeAuthConstants.NOMALLOGIN, "未登录", "1:查询成功 ;2:查询失败;3.未登录;", null);
			}else{//失败
				responseMsg = new ResponseMsg(false, "2", "查询失败", "1:查询成功 ;2:查询失败;3.未登录;", data);
			}
		}
		return responseMsg;
	}
	/**
	 * 把对象转换为ResponseMsg,state为SUCCESS则成功,否则失败
	 * @param state
	 * @param data
	 * @param msg
	 * @return
	 */
	public static ResponseMsg formatStateToObject(String state,JSONObject data){
		ResponseMsg responseMsg = null;
		if(KuKeAuthConstants.SUCCESS.equals(state)||KuKeAuthConstants.ACTIVE.equals(state)){//成功  查询成功  查询失败
			responseMsg = new ResponseMsg(true, "1", "查询成功", "1:查询成功 ;2:查询失败;3.未登录;", data);
		}else if(KuKeAuthConstants.FAILED.equals(state)){//失败
			responseMsg = new ResponseMsg(false, "2", "查询失败", "1:查询成功 ;2:查询失败;3.未登录;", data);
		}else if(KuKeAuthConstants.NOMALLOGIN.equals(state)){//未登录
			responseMsg = new ResponseMsg(false,KuKeAuthConstants.NOMALLOGIN, "未登录", "1:查询成功 ;2:查询失败;3.未登录;", null);
		}else{
			responseMsg = new ResponseMsg(false, "2", "查询失败", "1:查询成功 ;2:查询失败;3.未登录;", data);
		}
		System.out.println(responseMsg.toString());
		return responseMsg;
	}
	/**
	 * 把结果信息转换为ResponseMsg,state为SUCCESS则成功,否则失败
	 * @param state
	 * @param data
	 * @param msg
	 * @return
	 */
	public static ResponseMsg formatResultToObject(String state){
		ResponseMsg responseMsg = null;
		if(KuKeAuthConstants.SUCCESS.equals(state)){//成功  执行成功  执行失败
			responseMsg = new ResponseMsg(true, "1", "执行成功", "1:执行成功;2:执行失败;3.用户未登录;");
		}else if(KuKeAuthConstants.FAILED.equals(state)){//失败
			responseMsg = new ResponseMsg(false, "2", "执行失败", "1:执行成功;2:执行失败;3.用户未登录;");
		}else if(KuKeAuthConstants.NOMALLOGIN.equals(state)){//未登录
			responseMsg = new ResponseMsg(false,KuKeAuthConstants.NOMALLOGIN, "用户未登录", "1:执行成功;2:执行失败;3.用户未登录;");
		}else{
			responseMsg = new ResponseMsg(false, "2", "执行失败", "1:执行成功;2:执行失败;3.用户未登录;");
		}
		return responseMsg;
	}
	
	public static void main(String[] args) {
		String result = "";
		System.out.println(JSONObject.fromObject(result));
	}
}
