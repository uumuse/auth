package com.kuke.auth.play.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IPlayService {

	public Map<String,Date> getBothAudioDate(String uid);
	
	public List<Map<String,String>> getOrgDate(String orgId);
	
	public Map<String,String> getMusicBookById(String id);
	/**
	 * 得到单曲的音质
	 * @param l_code
	 * @return
	 */
	public String getTrackKbps(String l_code);
	
	/***
	 * 移动端得到单曲的音质
	 * @param l_code
	 * @param code
	 * @return
	 */
	public String getTrackKbpsForM(String l_code,String code);
}
