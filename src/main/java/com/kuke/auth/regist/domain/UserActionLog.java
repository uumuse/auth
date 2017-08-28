package com.kuke.auth.regist.domain;

import java.io.Serializable;


public class UserActionLog  implements Serializable {

	private static final long serialVersionUID = -6732614601705792876L;

	private String id;

	private String action_type;

	private String start_state;

	private String end_state;

	private String operator_ip;

	private String operator_date;
	
	private String user_id;
	
	private String user_type;

	private String channel_type;

	public String getChannel_type() {
		return channel_type;
	}

	public void setChannel_type(String channel_type) {
		this.channel_type = channel_type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public UserActionLog() {
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String userType) {
		user_type = userType;
	}

	public UserActionLog(String id, String action_type, String start_state,
			String end_state, String operator_ip, String operator_date,String user_id) {
		this.setId(id);
		this.setAction_type(action_type);
		this.setStart_state(start_state);
		this.setEnd_state(end_state);
		this.setOperator_ip(operator_ip);
		this.setOperator_date(operator_date);
		this.setUser_id(user_id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction_type() {
		return action_type;
	}

	/**
	 * 
	 * 
	 * 描　述:
	 * 	 action_type=1 注册
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
	 * </pre>
	 * 
	 * @param action_type
	 */
	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}

	public String getStart_state() {
		return start_state;
	}

	public void setStart_state(String start_state) {
		this.start_state = start_state;
	}

	public String getEnd_state() {
		return end_state;
	}

	public void setEnd_state(String end_state) {
		this.end_state = end_state;
	}

	public String getOperator_ip() {
		return operator_ip;
	}

	public void setOperator_ip(String operator_ip) {
		this.operator_ip = operator_ip;
	}

	public String getOperator_date() {
		return operator_date;
	}

	public void setOperator_date(String operator_date) {
		this.operator_date = operator_date;
	}
}
