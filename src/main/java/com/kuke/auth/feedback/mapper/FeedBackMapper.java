package com.kuke.auth.feedback.mapper;

import org.apache.ibatis.annotations.Param;

public interface FeedBackMapper {
	
	
	public void saveFeedBack(@Param("content")String content, @Param("contact")String contact,@Param("email")String email,@Param("from_client")String from_client);

}
