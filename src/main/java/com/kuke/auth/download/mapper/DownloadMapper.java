package com.kuke.auth.download.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kuke.auth.download.model.UserMoney;

public interface DownloadMapper {
	
	
	public List<Map<String, String>> getAssignInfo(@Param("org_id")String org_id, @Param("product_id")String product_id);
	
	public int getAssignUserInfo(@Param("org_id")String org_id, 
			@Param("product_id")String product_id,@Param("user_id") String user_id);

	public String getMoneyByIpId(@Param("id")String id);
	
	public UserMoney getUserMoney(@Param("user_id")String user_id);
	
	public Map<String,Date> getBothAudioDate(@Param("uid")String uid);
	
	public List<Map<String,String>> getOrgDate(@Param("orgId")String orgId);
	
	public Map<String,String> getMusicBookById(@Param("id")String id);
	
}
