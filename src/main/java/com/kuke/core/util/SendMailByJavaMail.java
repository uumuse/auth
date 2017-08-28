package com.kuke.core.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.kuke.auth.util.PropertiesHolder;



public class SendMailByJavaMail {
	
	
	public static void main(String[] args) throws Exception {
		String content="尊敬的**企业用户您好："+ 
		"<br/>  请用此xxxxxx登录，并妥善保管，建议您登录后及时修改为您的常用密码" +
		"<br/>  如果您有任何疑问，请与库客客服联系：400-650-1812。" +
		"<br/>  或发邮件至service@kuke.com，谢谢！ " +
		"<br/>                                                        库客（"+String.valueOf(PropertiesHolder.getContextProperty("wwwurl"))+"）";
		SendMailByJavaMail.setMail3("","maxin@kuke.com", "库客背景音乐平台-密码找回邮件", content);
	}
	

	public static void setMail3(String parasPath, String userEmail,
			String title, String content) throws Exception {
		// SimpleEmail email = new SimpleEmail();//如果发送普通的邮件，使用这个类就可以了
		// MultiPartEmail email = new MultiPartEmail();// 如果要发送带附件的邮件，需使用这个类		try {
			HtmlEmail email = new HtmlEmail();// 可以发送html类型的邮件
			email.setHostName(PropertiesUtil.readValue(parasPath, "mail.hostName"));// 指定要使用的邮件服务器
			email.setAuthentication(
					PropertiesUtil.readValue(parasPath,"mail.username"), 
					PropertiesUtil.readValue(parasPath,"mail.password"));// 使用163的邮件服务器需提供在163已注册的用户名、密码
			email.setCharset(PropertiesUtil.readValue(parasPath,"mail.charset"));
			try {
				email.setFrom(PropertiesUtil.readValue(parasPath,"mail.default.from"));// 设置发件人
				email.addTo(userEmail);// 设置收件人
				email.setSubject(title);// 设置主题
				email.setMsg(content);// 设置邮件内容
				email.send();
				System.out.println("发送成功");
			} catch (EmailException e) {
			}
		} catch (Exception ex) {
		}

	}
}