package com.kuke.auth.feedback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.auth.feedback.mapper.FeedBackMapper;

@Service
public class FeedBackService implements IFeedBackService {
	
	@Autowired
	private FeedBackMapper feedBackMapper;
	
	@Override
	public void saveFeedBack(String content, String contact,String email, String from_client) {
		feedBackMapper.saveFeedBack(content,contact,email , from_client);
	}
}
