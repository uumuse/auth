package com.kuke.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.kuke.auth.regist.service.impl.RegistService;

/***
 * 短信下发接口
 * @author Administrator
 *
 */
public class SendPhoneMsgUtil {
	
	/***
	 * 库客音乐序列号：0SDK-EBB-6699-RGTMP
	 * 序列号密码：620118
	 */
	//用户序列号
	private static final String cdkey = "8SDK-EMY-6699-RKXSP";
	private static final String password = "334917";
	
	@Test
	public void active(){
		String activeUrl = "http://hprpt2.eucp.b2m.cn:8080/sdkproxy/regist.action";
		Map<String, String> params = new HashMap<String, String>();
		params.put("cdkey", cdkey);
		params.put("password", password);
		params.put("ename", "北京库客音乐股份有限公司");
		params.put("linkman", "梁利伟");
		params.put("phonenum", "010-65610392-100");
		params.put("mobile", "13911316546");
		params.put("email", "liangliwei@kuke.com");
		params.put("fax", "010-65618079");
		params.put("address", "北京市朝阳区三间房南里4号院96栋办公楼");
		params.put("postcode", "100024");
		
		String result = HttpClientUtil.executePost(activeUrl, params);
		System.out.println(result);
	}
	
	/***
	 * 即时短信下发
	 * @param phones
	 * @param content
	 * @return
	 */
	
	 public static String sendSms(String phones,String randomNum,String type){
		 	//1:注册,2：绑定,3：提醒
	        //短信接口URL提交地址
	        String url = "http://hprpt2.eucp.b2m.cn:8080/sdkproxy/sendsms.action";
	       
	        String message = "";
	        if(type == "1"){
	        	message = "【库客音乐】您的验证码是："+randomNum+"，请完成手机验证（3分钟内有效）。如非本人操作请忽略。";
	        }else if(type == "2"){
	        	message = "";
	        }else{
	        	message = "";
	        }
	         
	        try {
				message = URLEncoder.encode(message, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	        //手机号码，多个号码使用英文逗号进行分割
			url += "?cdkey="+cdkey+"&password="+password+"&phone="+phones+"&message="+message
					+"&seqid=3&smspriority=1";
	 
	        String result = HttpClientUtil.executePost(url, null);
	        System.out.println(result);
	        
	        
			
	        return result;
	    }
	 
	 /***
	  * 定时短信接口
	  * @param phone
	  * @param title
	  * @param time   20090101101010  
	  * @return
	  */
	 public static String sendTimeSms(String phone,String title,String time){
	        String url = "http://hprpt2.eucp.b2m.cn:8080/sdkproxy/sendtimesms.action";
	        String randomNum = RandomNumUtil.createRandomVcode();
	        String message = "【库客音乐】您设置的"+title+"将于"+time+"开播，请您及时观看";
	        try {
				message = URLEncoder.encode(message, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	        //手机号码，多个号码使用英文逗号进行分割
			url += "?cdkey="+cdkey+"&password="+password+"&phone="+phone+"&message="+message
					+"&sendtime="+time+"&seqid=3&smspriority=1";
	 
	        String result = HttpClientUtil.executePost(url, null);
	        System.out.println(result);
	        
	        
//			if(result.contains("<error>0</error>")){
//				registService.insertMobileCode(phones, randomNum);
//			}
//	        return result;
			return result;
	    }
	 public static void main(String[] args) {
//		 sendSms("15810838346","1");
		 sendTimeSms("15810838346", "贝多芬：第二交响曲与第五交响曲BEETHOVEN", "20161020150500");
	}
}
