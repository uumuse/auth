package com.kuke.auth.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.kuke.util.HttpClientUtil;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.util.MD5;

public class PayOauth {

	private List<NameValuePair> nvps = new ArrayList<NameValuePair>();

	public void SetAuth(String user_id,String keyWord, String bill_status, String bill_num,String key) {
		
		String code = new MD5().getMD5ofStr(keyWord + bill_status + key+ user_id);
		nvps.add(new BasicNameValuePair("keyWord", keyWord));
		nvps.add(new BasicNameValuePair("bill_num", bill_num));
		nvps.add(new BasicNameValuePair("bill_status", bill_status));
		nvps.add(new BasicNameValuePair("uid", user_id));
		nvps.add(new BasicNameValuePair("md5_str", code));
	}
	// 更新用户权限
	public String updateAuth( String bill_channel_id,String bill_item_id, String bill_item_type,
			String bill_item_num,String bill_item_price) throws Exception {
		String post_url = KuKeUrlConstants.payAuth_URL;   //    /kuke/authorize/pay/update
		nvps.add(new BasicNameValuePair("bill_channel_id", bill_channel_id));
		nvps.add(new BasicNameValuePair("bill_item_id", bill_item_id));
		nvps.add(new BasicNameValuePair("bill_item_type", bill_item_type));
		nvps.add(new BasicNameValuePair("bill_item_num", bill_item_num));
		nvps.add(new BasicNameValuePair("bill_item_price", bill_item_price));
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		System.out.println("-----updateAuth result:"+result);
		return result;
	}
	
	// 更新用户权限
	public String updateAuth( String[] bill_channel_ids,String[] bill_ids, String[] bill_types,
			String[] bill_nums,String[] bill_prices) throws Exception {
		String post_url = KuKeUrlConstants.payAuth_URL;
		
		for (String bill_channel_id : bill_channel_ids) {
			nvps.add(new BasicNameValuePair("bill_channel_id", bill_channel_id));
		}
		for (String bill_id : bill_ids) {
			nvps.add(new BasicNameValuePair("bill_item_id", bill_id));
		}
		for (String bill_type : bill_types) {
			nvps.add(new BasicNameValuePair("bill_item_type", bill_type));
		}		
		for (String bill_num : bill_nums) {
			nvps.add(new BasicNameValuePair("bill_item_num", bill_num));
		}		
		for (String bill_price : bill_prices) {
			nvps.add(new BasicNameValuePair("bill_item_price", bill_price));
		}		
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		return result;
	}

}
