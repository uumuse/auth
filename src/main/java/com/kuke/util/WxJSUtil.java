package com.kuke.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;

/***
 * 微信JSSDK工具类
 * @author JYZ
 */
public class WxJSUtil {
	//微信公众号ID
	private final static String appId = "wx113817814d1304fc";
	//微信公众号密钥
	private final static String appSecret = "06629840e6d6e5506a2295cedff70fc0";
	private static String token;
	private static String ticket;
	private static Long now ;
	private static Long last ;
	private static int i;

	/**
	 * 获到acess_token,用于获取票据ticket
	 */
	public static String getAccessToken() {
		if(now == null){
			now = System.currentTimeMillis()/1000;
			last = now + 7200;
		}
		//access_token 2小时过期(7200秒),提前2秒重新请求
		if(token == null||(last - System.currentTimeMillis()/1000 <= 2)){
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+appSecret;
			String result = HttpClientUtil.executePost(url, null);
			token = JSONObject.fromObject(result).getString("access_token");
			now = System.currentTimeMillis()/1000;
			last = now + 7200;
			i++;
			System.out.println("~~~~~~~~~~~~~~execute get ACCESS_TOKEN "+i+" times~~~~~~~~~~~~~~~~~~~");
		}
		return token;
	}

	/**
	 * 获取微信票据ticket
	 * @return
	 */
	public static String getJsapiTicket(){
		if(ticket == null||(last - System.currentTimeMillis()/1000 <= 2)){
			String ticketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+getAccessToken()+"&type=jsapi";
			System.out.println("ticketUrl~~~~~~~~~~"+ticketUrl);
			String result = HttpClientUtil.executePost(ticketUrl, null);
			ticket = JSONObject.fromObject(result).getString("ticket");
		}
		return ticket;
	}
	
	/**
	 * 获取微信js签名
	 * @param ticket     票据
	 * @param noncestr   随机字符串
	 * @param timestamp  当前时间戳
	 * @param url        分享的url
	 * @return
	 */
	public static Map<String, Object> getSignature(String ticket,String noncestr, String timestamp,String url){
		 List<String> params = new ArrayList<String>();  
         params.add("jsapi_ticket="+ticket);  
         params.add("timestamp="+timestamp);  
         params.add("noncestr="+noncestr);
         params.add("url="+url);
         //1. 将token、timestamp、nonce三个参数进行字典序排序  
         Collections.sort(params, new Comparator<String>() {  
             @Override  
             public int compare(String o1, String o2) {  
                 return o1.compareTo(o2);  
             }  
         });  
         //2. 将三个参数字符串拼接成一个字符串进行sha1加密  
         String str = params.get(0) +"&"+ params.get(1)+"&"+ params.get(2)+"&"+ params.get(3);
         System.out.println("拼接字符串~~~~~~~~~~~~~~~~~~~~"+str);
         //3. sha1加密
         String signature = SHA1.encode(str);
         System.out.println("sha1加密~~~~~~~~~~~~~~~~~~~~~~"+signature);
         
         Map<String, Object> map = new HashMap<String, Object>();
 		 map.put("appId", appId);
 		 map.put("timestamp", timestamp);
 		 map.put("noncestr", noncestr);
 		 map.put("signature", signature);
		 return map;
	}
	
	/**
	 * 生成微信随机字符串noncestr
	 * @param length
	 * @return
	 */
	public static String getRandomStrNum(int length){
			String val = "";
			Random random = new Random();
			for(int i = 0; i < length; i++)
			{
				String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字
					
				if("char".equalsIgnoreCase(charOrNum)) // 字符串
				{
					int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母
					val += (char) (choice + random.nextInt(26));
				}
				else if("num".equalsIgnoreCase(charOrNum)) // 数字
				{
					val += String.valueOf(random.nextInt(10));
				}
			}
			return val;
		}
	
}