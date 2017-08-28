/**
 * Project Name:live_app
 * Package Name:com.kuke.core.engine
 * File Name:MailUtil.java 
 * Create Time:2012-4-6 上午11:46:00
 * Copyright (c) 2006 ~ 2012 Kuke Tech Dept All rights reserved.
 */
package com.kuke.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuke.auth.util.PropertiesHolder;

/**
 *Class MailUtil 发送邮件
 * 
 * @author：Administrator
 *@version: Revision:2.0.0, 2012-4-6 上午11:46:00
 */
public class MailUtil {
	
	private static ExecutorService pool = Executors.newCachedThreadPool();//线程池
	
	static Logger  logger = LoggerFactory.getLogger(MailUtil.class);
	
	private static MailUtil instance = null;
	
	private MailUtil() {

	}
	public static MailUtil getInstance() {
		if (instance == null) {
			instance = new MailUtil();
		}
		return instance;
	}
	
	/**
	 * 发送邮件 
	 * @param userEmail 
	 * @param title
	 * @param content
	 * @throws Exception
	 */
	public static void sendMail(final String userEmail,final String title, final String content) throws Exception {
//		pool.execute(new Runnable() {
//			public void run() {
				// SimpleEmail email = new SimpleEmail();//如果发送普通的邮件，使用这个类就可以了
				// MultiPartEmail email = new MultiPartEmail();// 如果要发送带附件的邮件，需使用这个类
				try {
					HtmlEmail email = new HtmlEmail();// 可以发送html类型的邮件
					email.setHostName(String.valueOf(PropertiesHolder.getContextProperty("mail.hostName")));// 指定要使用的邮件服务器
					email.setAuthentication(
							String.valueOf(PropertiesHolder.getContextProperty("mail.username")), 
							String.valueOf(PropertiesHolder.getContextProperty("mail.password")));// 使用163的邮件服务器需提供在163已注册的用户名、密码
					email.setCharset(String.valueOf(PropertiesHolder.getContextProperty("mail.charset")));
					try {
						email.setFrom(String.valueOf(PropertiesHolder.getContextProperty("mail.default.from")));// 设置发件人
						email.addTo(userEmail);// 设置收件人
						email.setSubject(title);// 设置主题
						email.setHtmlMsg(content);// 设置邮件内容
						email.send();
						logger.debug("Mail success");
					} catch (EmailException e) {
						logger.error("Mail faild:" + e.toString());
						throw e;
					}
				} catch (Exception ex) {
					logger.error("Mail faild:" + ex.toString());
						throw ex;
				}
//			}
//		});
	}
}
