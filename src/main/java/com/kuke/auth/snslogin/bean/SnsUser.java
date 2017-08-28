package com.kuke.auth.snslogin.bean;

import java.io.Serializable;

public class SnsUser implements Serializable {
	
	private static final long serialVersionUID = 6832233562339150648L;

	private String id;

	private String user_id;//用户id

	private String sns_id;//第三方id
	
	private String unionid;//微信唯一标识
	
	private String sns_name;//第三方sns_name

	private String sns_type;//微博,QQ,微信

	private String nick_name;//昵称
	
	private String headimgurl;//头像地址

	private String access_token;//授权令牌

	private String refresh_token;//刷新令牌

	private String end_time;//
	
	private String next_url;

	public SnsUser() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String userId) {
		user_id = userId;
	}

	public String getSns_id() {
		return sns_id;
	}

	public void setSns_id(String snsId) {
		sns_id = snsId;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String accessToken) {
		access_token = accessToken;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refreshToken) {
		refresh_token = refreshToken;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String endTime) {
		end_time = endTime;
	}

	public String getSns_type() {
		return sns_type;
	}

	public void setSns_type(String snsType) {
		sns_type = snsType;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nickName) {
		nick_name = nickName;
	}

	public String getNext_url() {
		return next_url;
	}

	public void setNext_url(String nextUrl) {
		next_url = nextUrl;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	
	public String getSns_name() {
		return sns_name;
	}

	public void setSns_name(String sns_name) {
		this.sns_name = sns_name;
	}

	
	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	@Override
	public String toString() {
		return "SnsUser [id=" + id + ", user_id=" + user_id + ", sns_id="
				+ sns_id + ", sns_type=" + sns_type + ", nick_name="
				+ nick_name + ", headimgurl=" + headimgurl + ",unionid="+unionid+", access_token="
				+ access_token + ", refresh_token=" + refresh_token
				+ ", end_time=" + end_time + ", next_url=" + next_url + "]";
	}
	
}
