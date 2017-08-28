package com.kuke.sonos.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SonosMapper {
	
	public void addPlayList(@Param("uid")String uid,@Param("serviceid")String serviceid,@Param("list")List<String> list);
	
	public void deletePlayList(@Param("uid")String uid);
	
	public List<Map<String,String>> getPerformerId(@Param("l_code")String l_code,@Param("work_id")String work_id);
	
	public List<Map<String,String>> getLcodeId(@Param("l_code")String l_code,@Param("uid")String uid,@Param("serviceid")String serviceid);
	
	public List<Map<String,String>> getPlayList(@Param("uid")String uid,@Param("serviceid")String serviceid,
			@Param("start")int start,@Param("end")int end);
}
