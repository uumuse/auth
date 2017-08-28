package com.kuke.auth.userCenter.bean;

import java.util.Date;

public class UserMessage {
	/**
	 * `id` int(30) NOT NULL,
	   `title` text NOT NULL COMMENT '标题',
	   `contents` text COMMENT '信息内容',
	   `send_user_id` varchar(32) DEFAULT NULL COMMENT '发送人',
	   `receive_user_id` varchar(32) DEFAULT NULL COMMENT '接收人',
	   `send_date` datetime DEFAULT NULL COMMENT '发送时间',
	   `flag` int(2) DEFAULT NULL COMMENT '消息标记：0:未读 ； 1：已读 ',
	   `type` int(2) DEFAULT NULL COMMENT '消息类型 0 通知，1 私信',
	 */
	private int id;//coupon_log 的id
	private String title;
	private String contents;
	private String send_user_id;
	private String receive_user_id;
	private Date send_date;
	private int flag;
	private int type;
	private String nick_name;
	private String user_image;
	
	
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getUser_image() {
		return user_image;
	}
	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getSend_user_id() {
		return send_user_id;
	}
	public void setSend_user_id(String send_user_id) {
		this.send_user_id = send_user_id;
	}
	public String getReceive_user_id() {
		return receive_user_id;
	}
	public void setReceive_user_id(String receive_user_id) {
		this.receive_user_id = receive_user_id;
	}
	public Date getSend_date() {
		return send_date;
	}
	public void setSend_date(Date send_date) {
		this.send_date = send_date;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	

}
