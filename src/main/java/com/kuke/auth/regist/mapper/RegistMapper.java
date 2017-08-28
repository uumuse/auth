package com.kuke.auth.regist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kuke.auth.regist.domain.User;


public interface RegistMapper {
	
	public void insertMobileCode(@Param("mobile")String mobile,@Param("code")String code);
	
	public int checkMobileCode(@Param("mobile")String mobile);
	
	public void updateMobileCode(@Param("mobile")String mobile,@Param("code")String code);

	public void insertUser(@Param("user")User user);
	
	public void insertUserSych(@Param("user")User user);
	
	public void insertUserAuth(@Param("id")String id,@Param("uid")String uid, @Param("audio_date")String audio_date);
	
	public void insertUserBasePertain(@Param("user_id") String user_id);
	
	public void insertUserBasePertains(@Param("user_id") String user_id,@Param("nick_name") String nick_name);
	
	public int selectUser(@Param("user")User user);
	
	public List selectUserByEmail(@Param("user")User user);
	
	
	public int selectUserPertain(@Param("user_id") String user_id);
	
	public String getCodeByMobile(@Param("mobile") String mobile);
	
}
