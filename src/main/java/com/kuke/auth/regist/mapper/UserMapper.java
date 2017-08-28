package com.kuke.auth.regist.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kuke.auth.regist.domain.User;
import com.kuke.auth.regist.domain.UserActionLog;
import com.kuke.auth.regist.domain.UserBaseVarify;
import com.kuke.auth.regist.domain.UserDownLog;
import com.kuke.auth.regist.domain.UserListenLog;
import com.kuke.auth.regist.domain.UserPertain;
import com.kuke.auth.userCenter.bean.UserMessage;
@Repository
public interface UserMapper {

	public int createUser(User user);
	
	public int addSnsUser(@Param("user")User user);

//	public String checkUserByName(@Param("nickName") String nickName);
	
	public List<String> checkUserByName(@Param("nickName") String nickName);
	
	/**
	 * 获得所有以nickName开头的用户的唯一标识
	 * @param nickName
	 * @return
	 */
	public List<String> getUserIdStartWith(@Param("nickName")String nickName);

	public String checkUserByMail(String email);

	public User getUserInfo(User user);

	public int addUserPertain(User user);
	
	public void addUserActionlog(@Param("userActionList")List<UserActionLog> userActionList);

	public void addUserRegistlog(UserActionLog useractionlog);
	
	public void addUserListenLog(@Param("userListenList")List<UserListenLog> userListenList);
	
	public void addUserDownLog(@Param("userDownList")List<UserDownLog> userDownLog);

	public int updateUserActive(User user);

	public void resetPassByEmail(@Param("password")String pass, @Param("email")String email);
	
	public void resetPassByPhone(@Param("password")String pass, @Param("phone")String phone);

	public User getUserByEmail(@Param("email") String email);
	
	public User getUserByPhone(@Param("phone") String phone);

	public int updateUserPass(@Param("id") String id, @Param("md5Pass") String md5Pass);

	public int updateUserEmail(@Param("userId") String userId,@Param("email") String email);

	public int updateUserInf(@Param("map") Map<String, String> params);
	
	public int updateNickName(@Param("map") Map<String, String> params);
	
	public int updateEmail(@Param("map") Map<String, String> params);
	
	public int updateUserPhoto(@Param("userId") String userId,@Param("photo") String photo);


	public UserBaseVarify getUserVerify(@Param("user")User user);

	public int cancelVerify(@Param("params") Map<String, String> params);

	public List getUserFriends(@Param("user")User user);

	public List<UserMessage> getUserMessage(@Param("params") Map<String, String> map,
			@Param("offset") int offset, @Param("rows") int rows);

	public void updateUserAuthorize(@Param("user")User user);

	public User getSnsUserByOldName(@Param("name") String name);

	public int addUserEmailPass(@Param("id")String id, @Param("email")String email, @Param("password")String password);

	public int updateUserNickName(@Param("id")String id, @Param("nickName")String nickName);

	public List<Map<String,String>> getFollowUser();

	public void addFollowUser(@Param("user_id")String id, @Param("list")List<Map<String, String>> l);

	/**
	 * 插入用户扩展信息
	 * @param userPertain
	 * @return
	 */
	public int insertUserPertain(UserPertain userPertain);
	
	/**
	 * 插入用户第三方登录时的初始密码系统消息
	 * @param userPertain
	 * @return
	 */
	public int insertUserMessage(UserMessage message);
	/**
	 * 更新用户的系统消息
	 * @param userPertain
	 * @return
	 */
	public int updateUserMessage(@Param("flag")String flag, @Param("id")String id);
	/**
	 * 更新用户的提示消息
	 * @param userPertain
	 * @return
	 */
	public int updateUserTip(@Param("user_id")String user_id);
}
