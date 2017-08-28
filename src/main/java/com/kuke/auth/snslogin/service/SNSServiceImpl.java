package com.kuke.auth.snslogin.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.auth.snslogin.bean.SnsUser;
import com.kuke.auth.snslogin.mapper.SNSMapper;


@Service
public class SNSServiceImpl implements SNSService {

	@Autowired
	private SNSMapper snsMapperMapper;

	@Override
	public int boundUserSNS_Phone(String uid,String phone) {
		return snsMapperMapper.boundUserSNS_Phone(uid,phone);
	}
	
	@Override
	public int updateUserSNS_phone(String uid){
		return snsMapperMapper.updateUserSNS_phone(uid);
	}
	
	@Override
	public int updateUserSNS_email(String uid){
		return snsMapperMapper.updateUserSNS_email(uid);
	}
	
	@Override
	public int boundUserSNS_QQ(SnsUser snsUser) {
		return snsMapperMapper.boundUserSNS_QQ(snsUser);
	}

	@Override
	public int boundUserSNS_SINA(SnsUser snsUser) {
		return snsMapperMapper.boundUserSNS_SINA(snsUser);
	}
	
	@Override
	public int boundUserSNS_WeChat(SnsUser snsUser) {
		return snsMapperMapper.boundUserSNS_WX(snsUser);
	}
	
	@Override
	public String checkUserSNS_Phone(String uid) {
		return snsMapperMapper.checkUserSNS_Phone(uid);
	}

	@Override
	public String checkUserSNS_QQ(String snsID) {
		return snsMapperMapper.checkUserSNS_QQ(snsID);
	}
	
	@Override
	public String checkUserSNS_WeChat(String snsID) {
		return snsMapperMapper.checkUserSNS_WX(snsID);
	}
	
	public List<Map<String,String>> checkUnionIdUserSNS_WX(String unionid){
		return snsMapperMapper.checkUnionIdUserSNS_WX(unionid);
	}
	
	@Override
	public Map<String, Object> checkUserBind_QQ(String userId) {
		return snsMapperMapper.getQQInfo(userId);
	}

	@Override
	public String checkUserSNS_SINA(String snsID) {
		return snsMapperMapper.checkUserSNS_SINA(snsID);
	}
	
	@Override
	public Map<String, Object> checkUserBind_SINA(String userId) {
		return snsMapperMapper.getSinaInfo(userId);
	}

	@Override
	public int updateUserSNS_QQ_AccessToken(SnsUser snsUser) {
		return snsMapperMapper.updateUserSNS_QQ_AccessToken(snsUser);
	}

	@Override
	public int updateUserSNS_SINA_AccessToken(SnsUser snsUser) {
		return snsMapperMapper.updateUserSNS_SINA_AccessToken(snsUser);
	}
	
	@Override
	public int updateUserSNS_WX_AccessToken(SnsUser snsUser) {
		return snsMapperMapper.updateUserSNS_WX_AccessToken(snsUser);
	}

	@Override
	public Map<String, String> snsBoundMap(String userID) {
		return snsMapperMapper.snsBoundMap(userID);
	}

	@Override
	public int delUserSNS_QQ(String userID) {
		return snsMapperMapper.delUserSNS_QQ(userID);
	}

	@Override
	public int delUserSNS_SINA(String userID) {
		return snsMapperMapper.delUserSNS_SINA(userID);
	}

	@Override
	public Map<String, Object> checkUserBind_WeChat(String userId) {
		return snsMapperMapper.getWeChatInfo(userId);
	}

	@Override
	public int delUserSNS_WeChat(String userID) {
		return snsMapperMapper.delUserSNS_WeChat(userID);
	}

}
