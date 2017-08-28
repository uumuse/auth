package com.kuke.share.service;



public interface IShareCountService {
	
	public void insertShare( String user_id,String l_code);
	
	public void updateShareCount(String user_id,String l_code);
	
	public String getCurMonthShareCount(String user_id,String l_code,String date);
}
