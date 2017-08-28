package com.kuke.util.wechat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import net.sf.json.JSONObject;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.wechat.WXHttpUtil;

public class WXOauth {
	public static void main(String[] args) {
//		HttpClientUtil util = new HttpClientUtil();
//		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
//				+ "appid=wx2a24ca3ce89ace8f&secret=c7686f8afb17b441f883d7b4c1183495"
//				+ "&code=031AUqJ32GQdWI0xbDG32vkrJ32AUqJy&grant_type=authorization_code";
//		String result = util.executePost(url, new HashMap<String, String>());
//		System.out.println(result);
//		String url1 = "https://api.weixin.qq.com/sns/userinfo?"
//				+ "access_token=qhMCqomVxvQhRLAdlVzT7wz8McMCV7diWPddc1VyQvhHuyfE_soCnw5Ge7M7NY97WG6bJxmtUUvHJXDoY_fqfP8hMK4SI-G1UhQnQKnKaQ0&openid=o57oFwNmsGfBXr0iCUmE94PcpcLI";
//		JSONObject result = WXHttpUtil.getJsonContent(url1);
//		System.out.println(result.get("nickname"));
//		String s = "âï¹âÏ";
//		try {
//			System.out.println(s.getBytes());
//			System.out.println(new String(s.getBytes(), "GB2312"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		String post_url = "http://auths.kuke.com/kuke/ssouser/authuser";
//		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		nvps.add(new BasicNameValuePair("u", "wechat_o57oFwNmsGfBXr0iCUmE94PcpcLI"));
//		nvps.add(new BasicNameValuePair("p", "123456"));
//		nvps.add(new BasicNameValuePair("from_client", "web"));
//		String result = "";
//		try {
//			result = HttpClientUtil.executeServicePOST(post_url, nvps);
//			System.out.println("hahahahahahah:"+result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println(result);
		String s = "{org_name:,uid:189A785086E811E6BF43C619DD6A325F,video_date:,usex:,ssoid:71e45e9aecc0eae93bad3b4f13c28568%26user_web_189A785086E811E6BF43C619DD6A325F%7CUlhZsfwAFt%7C189A785086E811E6BF43C619DD6A325F%7C1475224284534,live_date:,user_status:SUCCESS,reg_date:2016-09-30 16:31:20.0,is_verify:,org_id:AD3F95E0C7F611DCA4D2890DC22AF3D3,isactive:1,unickname:,uphoto:,audio_date:,countFav:0}";
		User user;
		try {
			user = new User(s);
			System.out.println(user.getUid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
