package com.kuke.share.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.share.mapper.ShareCountMapper;

@Service
public class ShareCountService implements IShareCountService {

	@Autowired
	private ShareCountMapper shareCountMapper;
	
	@Override
	public void insertShare(String user_id, String l_code) {
		shareCountMapper.insertShare(user_id, l_code);
	}

	@Override
	public void updateShareCount(String user_id, String l_code) {
		shareCountMapper.updateShareCount(user_id, l_code);
		
	}
	
	@Override
	public String getCurMonthShareCount(String user_id, String l_code,String date) {
		List<Map<String,String>> list = shareCountMapper.getCurMonthShareCount(user_id, l_code,date);
		int count = 0;
		for(Map<String,String> s:list){
			count += Integer.parseInt(s.get("times"));
		}
		return String.valueOf(count);
	}

}
