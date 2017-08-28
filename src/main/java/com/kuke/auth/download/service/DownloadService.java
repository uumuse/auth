package com.kuke.auth.download.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.auth.download.mapper.DownloadMapper;
import com.kuke.auth.download.model.UserMoney;


@Service
public class DownloadService implements IDownloadService {
	
	@Autowired
	private DownloadMapper downloadMapper; 
	
	
	public String getMoneyByIpId(@Param("id")String id){
		String money = downloadMapper.getMoneyByIpId(id);
		return money;
	}


	@Override
	public List<Map<String, String>> getAssignInfo(String org_id,
			String product_id) {
		List<Map<String, String>> list = downloadMapper.getAssignInfo(org_id,product_id);
		return list;
	}


	@Override
	public UserMoney getUserMoney(String user_id) {
		UserMoney userMoney = downloadMapper.getUserMoney(user_id);
		return userMoney;
	}


	@Override
	public int getAssignUserInfo(String org_id,
			String product_id, String user_id) {
		int count = downloadMapper.getAssignUserInfo(org_id, product_id, user_id);
		return count;
	}
}
