package com.kuke.auth.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.kuke.util.HttpClientUtil;
import com.kuke.util.StringUtil;
import com.kuke.auth.util.KuKeUrlConstants;

public class LogRecord {
	/**
	 * 得到系统名，浏览器名称
	 * @param request
	 * @return
	 */
	public static Map<String, String> getOsName(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		String userbrowser = "";
		String useros = "";
		try {
			String Agent = request.getHeader("User-Agent");  
			System.out.println("Agent:"+Agent);
			StringTokenizer st = new StringTokenizer(Agent);  
//			st.nextToken();  
//			userbrowser = st.nextToken();  
//			useros = st.nextToken(); 
			while(st.hasMoreElements()){
				System.out.println(st.nextToken());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("userbrowser", StringUtil.dealNull(userbrowser));
		map.put("useros", StringUtil.dealNull(useros));
		return map;
	}
	/**
	 * 访问日志
	 * @param request
	 * @param userid
	 * @param userType
	 * @return
	 * @throws IOException
	 */
	public static String addVisitLog(HttpServletRequest request,String userid,String userType) throws IOException{
		//参数
		String visitType = "2";//访问日志
		String ip = OrgOauth.getIp(request);//ip
		Map<String, String> map = getOsName(request);
		String useros = StringUtil.dealNull(map.get("useros"));//系统
		String userbrowser = StringUtil.dealNull(map.get("userbrowser"));//浏览器名称
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(new Date());//时间
		String url = request.getRequestURI();//获取的访问的url
		//调用日志接口
		String logUrl = KuKeUrlConstants.VisitLog_URL;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("visitType", visitType));
		nvps.add(new BasicNameValuePair("ip", ip));	
		nvps.add(new BasicNameValuePair("userid", userid));
		nvps.add(new BasicNameValuePair("useros", useros));		
		nvps.add(new BasicNameValuePair("userbrowser", userbrowser));
		nvps.add(new BasicNameValuePair("userType", userType));
		nvps.add(new BasicNameValuePair("time", time));
		nvps.add(new BasicNameValuePair("url", url));
		String result = HttpClientUtil.executeServicePOST(url, nvps);
		return result;
	}
	/**
	 * 操作日志
	 * @param request
	 * @param userid
	 * @param userType
	 * @return
	 * @throws IOException
	 */
	public static String addOperateLog(HttpServletRequest request,String userid,String userType) throws IOException{
		//参数
		String visitType = "3";//操作日志
		String ip = OrgOauth.getIp(request);//ip
		Map<String, String> map = getOsName(request);
		String useros = StringUtil.dealNull(map.get("useros"));//系统
		String userbrowser = StringUtil.dealNull(map.get("userbrowser"));//浏览器名称
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(new Date());//时间
		String url = request.getRequestURI();//获取的访问的url
		//调用日志接口
		String logUrl = KuKeUrlConstants.OperateLog_URL;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("visitType", visitType));
		nvps.add(new BasicNameValuePair("ip", ip));	
		nvps.add(new BasicNameValuePair("userid", userid));
		nvps.add(new BasicNameValuePair("useros", useros));		
		nvps.add(new BasicNameValuePair("userbrowser", userbrowser));
		nvps.add(new BasicNameValuePair("userType", userType));
		nvps.add(new BasicNameValuePair("time", time));
		nvps.add(new BasicNameValuePair("url", url));
		String result = HttpClientUtil.executeServicePOST(url, nvps);
		return result;
	}
	/**
	 * 
	 * 用户播放日志记录 
	 * @param operator_ip
	 * @param l_code
	 * @param item_code
	 * @param cname
	 * @param ename
	 * @param listen_type
	 * @param from_client
	 * @param channel_type
	 * @param ischarged
	 * @return
	 * @throws Exception 
	 * @author 高勇
	 * @date, 2013-8-6 下午12:21:16
	 *
	 */
	public static String addUserListenLog(String operator_ip,String l_code,String item_code,String cname,String ename,
		String user_id,	String user_type,String from_client,String channel_type,String ischarged) throws Exception{
		String url = KuKeUrlConstants.userListenLog_URL;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("operator_ip", operator_ip));
		nvps.add(new BasicNameValuePair("l_code", l_code));
		nvps.add(new BasicNameValuePair("user_id", user_id));
		nvps.add(new BasicNameValuePair("user_type", user_type));
		nvps.add(new BasicNameValuePair("item_code", item_code));
		nvps.add(new BasicNameValuePair("from_client", from_client));
		nvps.add(new BasicNameValuePair("cname", cname));
		nvps.add(new BasicNameValuePair("ename", ename));
		nvps.add(new BasicNameValuePair("channel_type", channel_type));
		nvps.add(new BasicNameValuePair("ischarged", ischarged));
		return HttpClientUtil.executeServicePOST(url, nvps);
	}
	/**
	 * 用户播放日志:NEW
	 * @Title: addUserListenLog
	 * @Description: 添加用户播放日志
	 * @param productCode 产品编码：web端对应：cd(乐库)、fm(电台)、spoken(有声读物)、sns(圈子)、live(直播)、video(视频)、lovd(点播);
	 * 			每个产品一个编码，不能重复 
	 * @param platform 平台：android、ios、winphone、window、mac、other ...
	 * @param userId 用户ID
	 * @param orgId 机构ID
	 * @param itemCode 专辑号
	 * @param lCode 单曲号
	 * @param fmId 电台ID
	 * @param themeId 主题ID
	 * @param operatorIp 客户端IP
	 * @Author：zhangGuoMing
	 * @Date: 2015-3-25 下午04:08:18
	 */
	public static String addUserPlayLog(String productCode,String platform,
			String userId,String orgId,
			String itemCode,String lCode,String fmId,String themeId,
			String operatorIp) throws IOException{
			String url = KuKeUrlConstants.userListenLog_URL;
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("product_code", productCode));
			nvps.add(new BasicNameValuePair("platform", platform));
			nvps.add(new BasicNameValuePair("user_id", userId));
			nvps.add(new BasicNameValuePair("org_id", orgId));
			nvps.add(new BasicNameValuePair("item_code", itemCode));
			nvps.add(new BasicNameValuePair("l_code", lCode));
			nvps.add(new BasicNameValuePair("fm_id", fmId));
			nvps.add(new BasicNameValuePair("theme_id", themeId));
			nvps.add(new BasicNameValuePair("operator_ip", operatorIp));
			return HttpClientUtil.executeServicePOST(url, nvps);
	}

	/**
	 * 
	 * 用户下载日志记录
	 * @param operator_ip
	 * @param l_code
	 * @param item_code
	 * @param cname
	 * @param ename
	 * @param user_type
	 * @param down_type
	 * @param channel_type
	 * @param from_client
	 * @return
	 * @throws Exception 
	 * @author 高勇
	 * @date, 2013-8-6 下午12:27:19
	 *
	 */
	public static String addUserDownLog(String operator_ip,String l_code,String item_code,String cname,String ename,
			String user_id,String user_type,String down_type,String channel_type,String from_client) throws Exception{
		String url = KuKeUrlConstants.userDownLoadLog_URL;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("operator_ip", operator_ip));
		nvps.add(new BasicNameValuePair("l_code", l_code));
		nvps.add(new BasicNameValuePair("down_type", down_type));
		nvps.add(new BasicNameValuePair("item_code", item_code));
		nvps.add(new BasicNameValuePair("user_id", user_id));
		nvps.add(new BasicNameValuePair("user_type", user_type));
		nvps.add(new BasicNameValuePair("from_client", from_client));
		nvps.add(new BasicNameValuePair("cname", cname));
		nvps.add(new BasicNameValuePair("ename", ename));
		nvps.add(new BasicNameValuePair("channel_type", channel_type));
		return HttpClientUtil.executeServicePOST(url, nvps);
	}
	/**
	 * 用户下载日志:NEW
	 * @Title: addUserDownLog
	 * @Description: 添加用户下载日志
	 * @param productCode 产品编码：web端对应：cd(乐库)、fm(电台)、spoken(有声读物)、sns(圈子)、live(直播)、video(视频)、lovd(点播);
	 * 			每个产品一个编码，不能重复 
	 * @param platform 平台：android、ios、winphone、window、mac、other ...
	 * @param userId 用户ID
	 * @param orgId 机构ID
	 * @param itemCode 专辑号
	 * @param lCode 单曲号
	 * @param downType 下载类型
	 *   downType=1 专辑
	 * 	 downType=2 单曲
	 * 	 downType=3 乐谱
	 * @param operatorIp 客户端IP
	 * void
	 * @Author：zhangGuoMing
	 * @Date: 2015-3-25 下午05:03:36
	 */
	public static String addUserDownLog(String productCode,String platform,
			String userId,String orgId,
			String itemCode,String lCode,
			String downType,String operatorIp) throws IOException{
		String url = KuKeUrlConstants.userDownLoadLog_URL;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("product_code", productCode));
		nvps.add(new BasicNameValuePair("platform", platform));	
		nvps.add(new BasicNameValuePair("user_id", userId));
		nvps.add(new BasicNameValuePair("org_id", orgId));
		nvps.add(new BasicNameValuePair("item_code", itemCode));
		nvps.add(new BasicNameValuePair("l_code", lCode));
		nvps.add(new BasicNameValuePair("operator_ip", operatorIp));
		nvps.add(new BasicNameValuePair("down_type", downType));
		return HttpClientUtil.executeServicePOST(url, nvps);
	}
		
	/**
	 * 
	 * 用户操作日志记录 
	 * @return 
	 * @author 高勇
	 * @date, 2013-8-6 下午12:28:29
	 *
	 */
	public static String addUserActinoLog(String operator_ip,String user_id,String user_type,String action_type,
			String channel_type,String start_date,String end_date) throws Exception{
		String url = KuKeUrlConstants.userActionLog_URL;// /kuke/log/userAction
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("operator_ip", operator_ip));
		nvps.add(new BasicNameValuePair("user_id", user_id));
		nvps.add(new BasicNameValuePair("user_type", user_type));
		nvps.add(new BasicNameValuePair("channel_type", channel_type));
		nvps.add(new BasicNameValuePair("action_type", action_type));
		nvps.add(new BasicNameValuePair("start_date", start_date));
		nvps.add(new BasicNameValuePair("end_date", end_date));
		return HttpClientUtil.executeServicePOST(url, nvps);
	}
	
	/**
	 * 用户操作日志:NEW
	 * @Title: addUserActionLog
	 * @Description: 添加用户操作日志
	 * @param productCode 产品编码：web端对应：cd(乐库)、fm(电台)、spoken(有声读物)、sns(圈子)、live(直播)、video(视频)、lovd(点播);
	 * 			每个产品一个编码，不能重复 
	 * @param platform 平台：android、ios、winphone、window、mac、other ...
	 * @param userId 用户ID
	 * @param orgId 机构ID
	 * @param actionType 操作类型
	 *   action_type=1 注册
	 * 	 action_type=2 登陆
	 * 	 action_type=3 修改资料
	 * 	 action_type=4 找回密码
	 * 	 action_type=5 更改帐户类型
	 * 	 action_type=6 退出
	 * 	 action_type=7 下载单曲
	 * 	 action_type=8 播放
	 * 	 action_type=99 确认定单
	 * 	 action_type=98 取消定单
	 * 	 action_type=97 续用定单
	 * @param operatorIp 客户端IP
	 * @Author：zhangGuoMing
	 * @Date: 2015-3-25 下午05:11:11
	 */
	public static String addUserActionLog(String productCode,String platform,
			String userId,String orgId,
			String actionType,String operatorIp) throws IOException{
		String url = KuKeUrlConstants.userActionLog_URL;
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("product_code", productCode));
		nvps.add(new BasicNameValuePair("platform", platform));	
		nvps.add(new BasicNameValuePair("user_id", userId));
		nvps.add(new BasicNameValuePair("org_id", orgId));		
		nvps.add(new BasicNameValuePair("action_type", actionType));
		nvps.add(new BasicNameValuePair("operator_ip", operatorIp));
		return HttpClientUtil.executeServicePOST(url, nvps);
	}
}

