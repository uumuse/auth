package com.kuke.auth.play.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface PlayMapper {

	public Map<String,Date> getBothAudioDate(@Param("uid")String uid);
	
	public List<Map<String,String>> getOrgDate(@Param("orgId")String orgId);
	
	public Map<String,String> getMusicBookById(@Param("id")String id);
	
}
