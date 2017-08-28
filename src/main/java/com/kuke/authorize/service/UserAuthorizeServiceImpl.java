package com.kuke.authorize.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.authorize.mapper.UserAuthorizeMapper;

@Service
public class UserAuthorizeServiceImpl implements UserAuthorizeService {

	@Autowired
	private UserAuthorizeMapper userAuthorizeMapper;

	@Override
	public String downloadAudioByUserId(String userId, String downId,
			String downType) {
		return userAuthorizeMapper.downloadAudioByUserId(userId, downId, downType);
	}

	@Override
	public String playAudioMonthByUserId(String userId) {
		return userAuthorizeMapper.playAudioMonthByUserId(userId);
	}

	@Override
	public String playLiveByUserId(String userId, String liveId) {
		return userAuthorizeMapper.playLiveByUserId(userId,liveId);
	}
	@Override
	public String playLiveVideoByUserId(String userId, String liveId) {
		return userAuthorizeMapper.playLiveVideoByUserId(userId,liveId);
	}

	@Override
	public String playLiveMonthByUserId(String userId) {
		return userAuthorizeMapper.playLiveMonthByUserId(userId);
	}

	@Override
	public String playVideoByUserId(String userId, String videoId) {
		return userAuthorizeMapper.playVideoByUserId(userId,videoId);
	}

	@Override
	public String playVideoMonthByUserId(String userId) {
		return userAuthorizeMapper.playVideoMonthByUserId(userId);
	}

}
