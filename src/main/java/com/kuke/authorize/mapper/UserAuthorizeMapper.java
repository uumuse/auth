package com.kuke.authorize.mapper;

import org.apache.ibatis.annotations.Param;

public interface UserAuthorizeMapper {

	// 验证用户下载权限
	public String downloadAudioByUserId(@Param("user_id") String user_id,
			@Param("down_id") String down_id, @Param("down_type") String down_type);

	// 验证用户包月播放权限 音频
	public String playAudioMonthByUserId(@Param("user_id") String user_id);

	// 验证用户包月播放权限 视频
	public String playVideoMonthByUserId(@Param("user_id") String user_id);

	// 验证用户单次播放权限 视频
	public String playVideoByUserId(@Param("user_id") String user_id,@Param("video_id") String video_id);

	// 验证用户包月播放权限 LIVE
	public String playLiveMonthByUserId(@Param("user_id") String user_id);

	// 验证用户单次播放权限 LIVE
	public String playLiveByUserId(@Param("user_id") String user_id,
			@Param("live_id") String live_id);

	// 验证用户单次点播权限 LIVE
	public String playLiveVideoByUserId(@Param("user_id") String user_id,
			@Param("live_id") String live_id);

}
