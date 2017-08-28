package com.kuke.authorize.mapper;

import org.apache.ibatis.annotations.Param;

public interface PayAuthorizeMapper {
	
	//查询 金额
	public int selectMoney(@Param("user_id") String user_id);
	//添加金额
	public int insertMoney(@Param("id") String id,@Param("user_id") String user_id,@Param("money") String money);
	// 充值
	public int updateMoney(@Param("user_id") String user_id,@Param("money") String money);

	//查询用户权限
	public int selectAuthorize(@Param("user_id") String user_id);
	
	// 音频包月 视频包月 Live包月
	public int insertAuthorizeDate(@Param("id") String id,@Param("user_id") String user_id,
			@Param("buyNum") String buyNum, @Param("buyType") String buyType);
	
	// 音频包月 视频包月 Live包月
	public int updateAuthorizeDate(@Param("user_id") String user_id,
			@Param("buyNum") String buyNum, @Param("buyType") String buyType);

	// 视频点播 Live点播
	public int updateVideo(@Param("id") String id,@Param("user_id") String user_id,@Param("video_id") String video_id);

	// Live直播
	public int updateLive(@Param("id") String id,@Param("user_id") String user_id,@Param("live_id") String live_id);

	// 下载
	public int updateDown(@Param("id") String id,@Param("user_id") String user_id,
			@Param("down_id") String down_id, @Param("down_type") String down_type);
	
	// Live个人分账
	public int shareLive(@Param("id") String id,
			@Param("user_id") String user_id, @Param("live_id") String live_id,
			@Param("buyDate") String buyDate,@Param("price") String price);
	
	

}
