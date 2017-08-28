package com.kuke.auth.download.service;

import java.util.List;
import java.util.Map;

import com.kuke.auth.download.model.UserMoney;

public interface IDownloadService {

	public String getMoneyByIpId(String id);
	
	public List<Map<String,String>> getAssignInfo(String org_id,String product_id);
	
	public int getAssignUserInfo(String org_id,String product_id,String user_id);
	
	public UserMoney getUserMoney(String user_id);
}
