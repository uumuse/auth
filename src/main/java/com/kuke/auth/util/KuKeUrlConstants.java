package com.kuke.auth.util;

import com.kuke.auth.util.PropertiesHolder;

public class KuKeUrlConstants {


	public static String AUTH_URL = String.valueOf(PropertiesHolder.getContextProperty("auth.url"));//请求HTTP地址
	public static String WWW_URL = String.valueOf(PropertiesHolder.getContextProperty("www.url"));
	public static String wwwUrl = String.valueOf(PropertiesHolder.getContextProperty("wwwurl"));
	//sns登录,绑定
	public static final String userQQLogin_URL = AUTH_URL + "/kuke/sns/qq";//第三方qq登录绑定
	public static final String userWeChatLogin_URL = AUTH_URL + "/kuke/sns/wechat";//第三方微信登录绑定
	public static final String userSinaLogin_URL = AUTH_URL + "/kuke/sns/weibo";//第三方微博登录绑定
	
	//个人
	public static final String userLoginByUP_URL = AUTH_URL + "/kuke/ssouser/authuser";//用户名密码登录
	public static final String userLoginByToken_URL = AUTH_URL + "/kuke/ssouser/authcookie";// Cookies登录
	public static final String userLoginByToken_UID = AUTH_URL + "/kuke/ssouser/authUid";// UID,移动端登录
	public static final String userVerify_URL = AUTH_URL + "/kuke/ssouser/verify";//获取认证信息
	public static final String userUpdateRAM_URL = AUTH_URL + "/kuke/ssouser/userupdateRAM";//更新用户内存信息
	public static final String userRefreshOnline_URL = AUTH_URL + "/kuke/ssouser/initonline";//刷新在线时间
	public static final String userOut_URL = AUTH_URL + "/kuke/ssouser/authout";//登出
	public static final String userPlayAudio_URL = AUTH_URL + "/kuke/authorize/user/audio";//音频播放授权
	public static final String userPlayVideo_URL = AUTH_URL + "/kuke/authorize/video";//视频播放授权
	public static final String userPlayLive_URL = AUTH_URL + "/kuke/authorize/user/live";//Live播放授权
	public static final String userDownLoad_URL = AUTH_URL + "/kuke/authorize/user/download";//音频下载授权 图谱下载授权
	public static final String userSnsInfoList_URL = AUTH_URL + "/kuke/sns/load/index";//第三方信息查询
	public static final String userLoginByUPForAndriod_URL = AUTH_URL + "/kuke/ssouser/authandrioduser";//andriod用户名密码登录
	public static final String userLoginByTokenForAndriod_URL = AUTH_URL + "/kuke/ssouser/authandriodcookie";//andriodCookies登录
	//忘记密码
	public static final String checkUserEmail_URL = AUTH_URL + "/kuke/userCenter/checkUserEmail";//验证邮箱是否存在于用户数据库中
	public static final String sendFindEmail_URL = AUTH_URL + "/kuke/findPassword/sendFindEmail";//发送找回密码的邮件
	
	//个人中心
	public static final String addSourceToFolder_URL = AUTH_URL + "/kuke/userCenter/addSourceToFolder";//将资源添加到夹子中
	
	//机构
	public static final String orgLoginByAddr_URL = AUTH_URL + "/kuke/ssoorg/authaddr";//IP登录
	public static final String orgLoginByUserInfo_URL = AUTH_URL + "/kuke/ssoorg/authuserinfo";//orguser登录
	public static final String orgLoginByToken_URL = AUTH_URL + "/kuke/ssoorg/authcookie";// Cookies登录
	public static final String orgLoginByHoliDay_URL = AUTH_URL + "/kuke/ssoorg/authid";// 寒暑假登录	
	public static final String orgRefreshOnline_URL = AUTH_URL + "/kuke/ssoorg/initonline";//刷新在线时间
	public static final String orgOut_URL = AUTH_URL + "/kuke/ssoorg/authout";//登出
	public static final String orgPlayAuth_URL = AUTH_URL + "/kuke/authorize/org/play";//音频播放授权 视频播放授权 Live播放授权
	//站外分享
	public static final String snsShareOut_URL = AUTH_URL + "/kuke/sns/share";//站外分享地址
	//更新权限
	public static final String payAuth_URL = AUTH_URL + "/kuke/authorize/pay/update";//更新权限地址
	//验证支付方式权限
	public static final String payTypeAuth_URL = AUTH_URL + "/kuke/play/getAuthDownloadUrl";//获取机构的支付方式
	
	public static final String userDownLoadLog_URL = AUTH_URL + "/kuke/log/userDown";//音频下载日志
	public static final String userListenLog_URL = AUTH_URL + "/kuke/log/userListen";//音频收听日志
	public static final String userActionLog_URL = AUTH_URL + "/kuke/log/userAction";//音频操作日志
	public static final String VisitLog_URL = wwwUrl + "/kuke/log/userAction";//访问日志
	public static final String OperateLog_URL = wwwUrl + "/kuke/log/userAction";//操作日志
	
	public static final String LogURL = "http://log.kuke.com/kuke/log/log";//操作日志
	
	
	//pay
	public static final String wechatApplyRefund = AUTH_URL + "/kuke/payment/wechatApplyRefund";//微信退款地址
	public static final String wechatApplyQuery = AUTH_URL + "/kuke/payment/queryWxOrder";//微信查询订单
	public static final String alipayApplyQuery = AUTH_URL + "/kuke/payment/queryAlipayOrder";//支付宝查询订单
	public static final String alipayApplyRefund = AUTH_URL + "/kuke/payment/alipayApplyRefund";//支付宝退款地址
	public static final String userBills = AUTH_URL + "/kuke/userCenter/userBill";//用户我的订单页面
	public static final String getPlayUrl = AUTH_URL + "/kuke/play/getPlayUrl";//得到下载地址
	
	//下发短信
	public static final String userSendPhoneNum = AUTH_URL + "/kuke/issued";//下发短信
	
	//开始页面
	public static final String userLogin_url = "redirect:"+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"));//跳转到开始页面,进行登录
	
	
	//上传地址
	public static final String touploadurl = "http://Upload.kuke.com/v2upload";
	
	
	//www  url
	public static final String getTrackUrl = WWW_URL + "/kuke/dc/common/music/getTrack";//得到单曲详细信息url
	public static final String getCataUrl = WWW_URL + "/kuke/dc/common/music/getCataloguebio";//得到专辑详细信息url
	public static final String getMusicians = WWW_URL + "/kuke/dc/common/music/getMusicians";//得到艺术家详细信息url
	public static final String getCaltlog =  "/album/";//跳到专辑的页面
}
