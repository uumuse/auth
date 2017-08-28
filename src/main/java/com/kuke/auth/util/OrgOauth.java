package com.kuke.auth.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.core.engine.ICookie;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.StringUtil;



public class OrgOauth {
	/**
	 * 获取真正的IP
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request){
		String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
	}
	/**
	 * Cookies登录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static Organization orgLogin(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Organization org = new Organization();
		String post_url = "";
		String result = "";
		String token = ICookie.get(request,KuKeAuthConstants.SSO_ORG_COOKIE_NAME); //KuKeDesktopSSOORGID
		System.out.println("token:"+token);
		if (null != token && !"".equals(token)){
			// token
			post_url = KuKeUrlConstants.orgLoginByToken_URL;//  /kuke/ssoorg/authcookie
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("ticket", token));
			result = HttpClientUtil.executeServicePOST(post_url, nvps);
			ResponseMsg msg = new ResponseMsg(result);
			org = new Organization(msg.getData().toString());
		}
		if (KuKeAuthConstants.FAILED.equals(org.getOrg_status())) {
			// ip
			post_url = KuKeUrlConstants.orgLoginByAddr_URL;// /kuke/ssoorg/authaddr
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			//获取真正IP,之前为request.getHeader("X-Real-IP")
			String ip = StringUtil.dealNull(request.getParameter("downip"));
			if("".equals(ip)){
				ip = getIp(request);
			}
			System.out.println("ip:"+ip);
			nvps.add(new BasicNameValuePair("i", ip));
			nvps.add(new BasicNameValuePair("from_client",request.getParameter("from_client")));
			result = HttpClientUtil.executeServicePOST(post_url, nvps);
			System.out.println("orgLoginByAddr_URL orgLogin:"+result);
			ResponseMsg msg = new ResponseMsg(result);
			result = msg.getData().toString();
			if(!result.contains("Error")){
				org = new Organization(result);
			}
			System.out.println(" orgLoginByAddr_URL后解析: "+org.toString());
			if (org.getOrg_status().equals(KuKeAuthConstants.SUCCESS)) {
				ICookie.set(response, KuKeAuthConstants.SSO_ORG_COOKIE_NAME, org.getOrg_ssoid(), 1*24*3600,"/", ".kuke.com");
			}else{
				//账密登录
				post_url = KuKeUrlConstants.orgLoginByUserInfo_URL;//  /kuke/ssoorg/authuserinfo
				List<NameValuePair> nvps2 = new ArrayList<NameValuePair>();
				nvps2.add(new BasicNameValuePair("n", request.getParameter("orgName")));
				nvps2.add(new BasicNameValuePair("p", request.getParameter("orgPwd")));
				nvps2.add(new BasicNameValuePair("from_client",request.getParameter("from_client")));
				//获取真正ip
				nvps2.add(new BasicNameValuePair("i", getIp(request)));
				
				result = HttpClientUtil.executeServicePOST(post_url, nvps2);
				System.out.println("机构帐密登陆================>"+result);
				msg = new ResponseMsg(result);
				result = msg.getData().toString();
				org = new Organization(result);
				if (org.getOrg_status().equals(KuKeAuthConstants.SUCCESS)) {
					ICookie.set(response, KuKeAuthConstants.SSO_ORG_COOKIE_NAME, org.getOrg_ssoid(), 1*24*3600,"/", ".kuke.com");
				}else if (KuKeAuthConstants.FAILED.equals(org.getOrg_status())) {
					User user = UserOauth.userLoginByToken(request);
					System.out.println(user.toString());
					if (KuKeAuthConstants.SUCCESS.equals(user.getUser_status())) {
						//ID 寒暑假
						post_url = KuKeUrlConstants.orgLoginByHoliDay_URL;
						nvps = new ArrayList<NameValuePair>();
						nvps.add(new BasicNameValuePair("u", user.getUid()));
						nvps.add(new BasicNameValuePair("o", user.getOrg_id()));
						nvps.add(new BasicNameValuePair("from_client",request.getParameter("from_client")));
						nvps.add(new BasicNameValuePair("i", getIp(request)));
						result = HttpClientUtil.executeServicePOST(post_url, nvps);
						msg = new ResponseMsg(result);
						result = msg.getData().toString();
						org = new Organization(result);
						if (org.getOrg_status().equals(KuKeAuthConstants.SUCCESS)) {
							ICookie.set(response, KuKeAuthConstants.SSO_ORG_COOKIE_NAME, org.getOrg_ssoid(), 1*24*3600,"/", ".kuke.com");
						}					
					}
				}
			}
		}
		return org;
	}
	/**
	 * 刷新在线Time
	 * @param request
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	public static JSONObject orgRefreshOnline(HttpServletRequest request,String channelId) throws Exception {
		String post_url = KuKeUrlConstants.orgRefreshOnline_URL;
		String token = ICookie.get(request, KuKeAuthConstants.SSO_ORG_COOKIE_NAME);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("ticket", token));
		nvps.add(new BasicNameValuePair("c", channelId));
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		return  JSONObject.fromObject(result);
	}
	/**
	 * 音频播放 视频播放 Live播放 
	 * @param request
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	public static String orgPlayAuth(HttpServletRequest request,String channelId) throws Exception {
		String post_url = KuKeUrlConstants.orgPlayAuth_URL;  // /kuke/authorize/org/play
		String token = ICookie.get(request,KuKeAuthConstants.SSO_ORG_COOKIE_NAME);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("ticket", token));
		nvps.add(new BasicNameValuePair("c", channelId));
		String result = HttpClientUtil.executeServicePOST(post_url, nvps);
		return result;
	}
}
