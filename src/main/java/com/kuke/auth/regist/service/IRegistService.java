package com.kuke.auth.regist.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kuke.auth.regist.domain.User;
import com.kuke.auth.regist.domain.UserBaseVarify;

public interface IRegistService {

	public String getCodeByMobile(String mobile);
	
	public void insertMobileCode(String mobile,String code);
	
	public int checkCodeByMobile(String mobile);

	public void insertUser(User user);
	
	public List selectUserByEmail(User user);
	
	public void insertUserSych(User user);
	
	public void insertUserBasePertain(String user_id);
	
	public void insertUserBasePertains(String user_id,String nick_name);
	///
	public com.kuke.auth.login.bean.User snsFastRegist(String user_id,HttpServletRequest request,HttpServletResponse response);
	
	public User getUserInfo(User user);
	
	public int updateUserInf(Map<String, String> params);
	
	public int updateNickName(Map<String, String> params);
	
	public int updateEmail(Map<String, String> params);
	
	public UserBaseVarify getUserVerify(User user);

	public int cancelVerify(Map<String, String> params);
	
	public List getUserFriends(User user) ;
	
	public boolean checkUserByMail(String email);
	
	public int updateUserEmail(String userId, String email);
	
	public int updateUserPhoto(String userId, String photo);
	
	public User getUserByEmail(String email);
	
	public User getUserByPhone(String phone);
	
}
