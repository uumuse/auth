package com.kuke.share.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface ShareCountMapper {
	
	public void insertShare(@Param("user_id") String user_id,@Param("l_code")String l_code);
	
	public void updateShareCount(@Param("user_id") String user_id,@Param("l_code")String l_code);
	
	public List<Map<String,String>> getCurMonthShareCount(@Param("user_id") String user_id,@Param("l_code")String l_code,@Param("date") String date);

}
