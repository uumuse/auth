package com.kuke.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import com.kuke.util.wechat.WXAccessToken;
import com.kuke.util.wechat.WXConfig;
import com.kuke.util.wechat.WXHttpUtil;

import net.sf.json.JSONObject;

/***
 * 微信JSSDK工具类
 * 
 * @author JYZ
 */
public class WxUtil {
	private static String token;
	private static String ticket;
	private static Long now;
	private static Long last;

	/**
	 * 获到acess_token,用于获取票据ticket
	 */
	public static String getAccessToken() {
		if (now == null) {
			now = System.currentTimeMillis() / 1000;
			last = now + 7200;
		}
		// access_token 2小时过期(7200秒),提前2秒重新请求
		if (token == null || (last - System.currentTimeMillis() / 1000 <= 2)) {
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WXConfig.getValue("appId") + "&secret=" + WXConfig.getValue("appSecret");
			String result = HttpClientUtil.executePost(url, null);
			token = JSONObject.fromObject(result).getString("access_token");
			now = System.currentTimeMillis() / 1000;
			last = now + 7200;
		}
		return token;
	}

	/**
	 * 获取微信票据ticket
	 * 
	 * @return
	 */
	public static String getJsapiTicket() {
		if (ticket == null || (last - System.currentTimeMillis() / 1000 <= 2)) {
			String ticketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + getAccessToken() + "&type=jsapi";
			String result = HttpClientUtil.executePost(ticketUrl, null);
			ticket = JSONObject.fromObject(result).getString("ticket");
		}
		return ticket;
	}

	/**
	 * 获取微信js签名
	 * 
	 * @param ticket
	 *            票据
	 * @param noncestr
	 *            随机字符串
	 * @param timestamp
	 *            当前时间戳
	 * @param url
	 *            分享的url
	 * @return
	 */
	public static Map<String, Object> getSignature(String ticket, String noncestr, String timestamp, String url) {
		List<String> params = new ArrayList<String>();
		params.add("jsapi_ticket=" + ticket);
		params.add("timestamp=" + timestamp);
		params.add("noncestr=" + noncestr);
		params.add("url=" + url);
		// 1. 将token、timestamp、nonce三个参数进行字典序排序
		Collections.sort(params, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		// 2. 将三个参数字符串拼接成一个字符串进行sha1加密
		String str = params.get(0) + "&" + params.get(1) + "&" + params.get(2) + "&" + params.get(3);
		// 3. sha1加密
		String signature = SHA1.encode(str);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", WXConfig.getValue("appId"));
		map.put("timestamp", timestamp);
		map.put("noncestr", noncestr);
		map.put("signature", signature);
		return map;
	}

	/**
	 * 生成微信随机字符串noncestr
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomStrNum(int length) {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字

			if ("char".equalsIgnoreCase(charOrNum)) // 字符串
			{
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) // 数字
			{
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}
	
	/** 
     * 描述 : 生成授权URL
     *  
     * @param state_id: 用于保持请求和回调的状态ID
     * @return 
     */  
    public static String authorizeUrl(String state_id){  
        StringBuilder sb = new StringBuilder();  
        try {  
            	 sb.append(WXConfig.getValue("baseURL")). 
            		append("appid=").  
                    append(WXConfig.getValue("appId")).  
                    append("&redirect_uri=").  
                    append(URLEncoder.encode(WXConfig.getValue("redirectURI"),"UTF-8")).  
                    append("&response_type=").
                    append(WXConfig.getValue("responseType")).  
                    append("&scope=").  
                    append(WXConfig.getValue("scope")).  
                    append("&state=").  
                    append(state_id).  
                    append("#wechat_redirect");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        return sb.toString();  
    }
    /** 
     * 描述 : 生成 accessTokenURL
     *  
     * @param state_id: 用于保持请求和回调的状态ID
     * @return 
     */  
    public static String accessTokenUrl(String code){  
        StringBuilder sb = new StringBuilder();  
        try {  
            	 sb.append(WXConfig.getValue("accessTokenBaseURL")). 
            		append("appid=").  
                    append(WXConfig.getValue("appId")).  
                    append("&secret=").  
                    append(WXConfig.getValue("appSecret")).  
                    append("&code=").
                    append(code).  
                    append("&grant_type=").  
                    append(WXConfig.getValue("grantType"));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return sb.toString();  
    }
    /** 
     * 描述 : 生成userInfoURL
     *  
     * @param code: 用于保持请求和回调的状态ID
     * @return 
     */  
    public static String userInfoUrl(String access_token,String openid){  
        StringBuilder sb = new StringBuilder();  
        try {  
            	 sb.append(WXConfig.getValue("userInfoBaseURL")). 
            		append("access_token=").  
                    append(access_token).  
                    append("&openid=").  
                    append(openid);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return sb.toString();  
    }
    /**
     * 通过code获取access_token
     * @param request
     * @param state:
     * @return
     */
    public static WXAccessToken getAccessTokenByQueryString(HttpServletRequest request,String state){
    	WXAccessToken wxAccessToken = new WXAccessToken();
    	try {
        	String queryString = request.getQueryString();
        	String returnState = request.getParameter("state");
        	String code = request.getParameter("code");
        	//验证
        	if(queryString == null) {
        	      return wxAccessToken;
        	}
        	if(returnState == null || "".equals(returnState)) {
      	      return wxAccessToken;
        	}
        	if(code == null || "".equals(code)) {
        	      return wxAccessToken;
          	}
        	
        	//通过code获取access_token
        	String url = accessTokenUrl(code);
        	JSONObject jsonObject = WXHttpUtil.getJsonContent(url);
        	wxAccessToken.setAccess_token(dealNull(jsonObject.getString("access_token")));
        	wxAccessToken.setExpires_in(dealNull(jsonObject.getString("expires_in")));
        	wxAccessToken.setOpenid(dealNull(jsonObject.getString("openid")));
        	wxAccessToken.setRefresh_token(dealNull(jsonObject.getString("refresh_token")));
        	wxAccessToken.setScope(dealNull(jsonObject.getString("scope")));
        	wxAccessToken.setUnionid(dealNull(jsonObject.getString("unionid")));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return wxAccessToken;
    }
    
    /**
     * 得到用户信息
     * @param wxAccessToken
     */
    public static JSONObject  getUserInfo(WXAccessToken wxAccessToken){
    	try {
    		JSONObject jsonObject = null;
    		String access_token = wxAccessToken.getAccess_token();
    		String openid = wxAccessToken.getOpenid();
    		//获取用户个人信息（UnionID机制）
    		String url = userInfoUrl(access_token, openid);
    		jsonObject = WXHttpUtil.getJsonContent(url);
    		return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    /**
     * 
     * @param string
     * @return
     */
    public static String dealNull(String string){
    	if(string == null || "null".equals(string)){
    		string = "";
    	}
    	return string;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	public static void main(String[] args) {
		System.out.println(getAccessToken());
	}
}
