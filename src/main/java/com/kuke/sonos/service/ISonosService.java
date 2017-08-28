package com.kuke.sonos.service;

import java.util.List;
import java.util.Map;

public interface ISonosService {
	
	public Map<String,Object> getItemWindow(String serviceid,String uid,String queueVersion,
			String itemId,String previousWindowSize,String upcomingWindowSize);
	
	public void addPlayList(String uid, String serviceid,List<String> playlist);
	
	public void deletePlayList(String uid);
	
	public List<Map<String,String>> getLcodeId(String l_code,String uid, String serviceid);
	
	public List<Map<String,String>> getPlayList(String uid, String serviceid,int start,int end);
}
