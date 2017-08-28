package com.kuke.authorize.service;


public interface UserAuthorizeService {

	// 验证用户下载权限
	public String downloadAudioByUserId(String user_id, String down_id,
			String down_type);

	// 验证用户包月播放权限 音频
	public String playAudioMonthByUserId(String user_id);

	// 验权限 视频
	public String playVideoMonthByUserId(String user_id);

	// 验证用户单次播放权限 视频
	public String playVideoByUserId(String user_id, String video_id);

	// 验权限 LIVE
	public String playLiveMonthByUserId(String user_id);

	// 验证用户单次播放权限 LIVE
	public String playLiveByUserId(String user_id, String live_id);
	
	// 验证用户单点播放权限 LIVE
	public String playLiveVideoByUserId(String user_id, String live_id);
}
