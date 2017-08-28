package com.kuke.authorize.service;




public interface PayAuthorizeService {
	
	//充值
	public String updateMoney(String user_id,String money);
	//音频包月
	public String updateAudioDate(String user_id, String buyNum);
	
	//视频点播 Live点播
	public String updateVideo(String user_id, String video_id);
	//视频包月
	public String updateVideoDate(String user_id, String buyNum);
	
	//Live直播
	public String updateLive(String user_id, String live_id,String price);
	//Live包月
	public String updateLiveDate(String user_id, String buyNum,String price);
	
	//下载
	public String updateDownAudio(String user_id, String down_id,
			String down_type);
	
	
	
}
