package com.kuke.auth.userCenter.bean;

import java.util.Date;

public class UserAuthorize {
	
	/**
	 * notBuy:未购买
	 * outDate:过期
	 * inDate: 未过期
	 */
	
	private Date audio_date;
	private String audio_flag="notBuy";
	private Date video_date;
	private String video_flag="notBuy";
	private Date live_date;
	private String live_flag="notBuy";
	private Date last_date;
	private String remain_money;
	
	
	
	public String getRemain_money() {
		return remain_money==null?"0":remain_money;
	}
	public void setRemain_money(String remain_money) {
		this.remain_money = remain_money;
	}
	public Date getAudio_date() {
		return audio_date;
	}
	public void setAudio_date(Date audio_date) {
		this.audio_date = audio_date;
	}
	public Date getVideo_date() {
		return video_date;
	}
	public void setVideo_date(Date video_date) {
		this.video_date = video_date;
	}
	public Date getLive_date() {
		return live_date;
	}
	public void setLive_date(Date live_date) {
		this.live_date = live_date;
	}
	public Date getLast_date() {
		return last_date;
	}
	public void setLast_date(Date last_date) {
		this.last_date = last_date;
	}
	public String getAudio_flag() {
		return audio_flag;
	}
	public void setAudio_flag(String audio_flag) {
		this.audio_flag = audio_flag;
	}
	public String getVideo_flag() {
		return video_flag;
	}
	public void setVideo_flag(String video_flag) {
		this.video_flag = video_flag;
	}
	public String getLive_flag() {
		return live_flag;
	}
	public void setLive_flag(String live_flag) {
		this.live_flag = live_flag;
	}
	
	
}
