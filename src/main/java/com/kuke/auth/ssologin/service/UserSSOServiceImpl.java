package com.kuke.auth.ssologin.service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.ssologin.mapper.UserSSOMapper;
import com.kuke.core.redis.RedisCached;
import com.kuke.auth.ssologin.service.UserSSOService;

@Service
public class UserSSOServiceImpl implements UserSSOService {

	@Autowired
	private UserSSOMapper userSSOMapper;

	@Override
	public User checkUserLoginByPhone(String loginName, String loginPwd) {
		User user = userSSOMapper.checkUserLoginByPhone(loginName, loginPwd);
		return compareUser(user);
	}
	
	@Override
	public User checkUserLoginByEmail(String loginName, String loginPwd) {
		User user = userSSOMapper.checkUserLoginByEmail(loginName, loginPwd);
		return compareUser(user);
	}
	
	@Override
	public User checkUserLogin(String loginName, String loginPwd) {
		User user = userSSOMapper.checkUserLogin(loginName, loginPwd);
		return compareUser(user);
	}
	
	@RedisCached(name = "checkUserLoginById", key = "userInfo:")
	@Override
	public User checkUserLoginById(String userId) {
		User user = userSSOMapper.checkUserLoginById(userId);
		return compareUser(user);
	}

	@Override
	public User getUserByID(String uid) {
		User user = userSSOMapper.getUserByID(uid);
		return compareUser(user);
	}
	/**
	 * 比较个人与机构的时间
	 * @param user
	 * @return
	 */
	private User compareUser(User user){
		try {
			if(user != null){
				//用户头像
				String nickname = user.getUnickname();
				if(!"".equals(dealNull(nickname))){
					user.setUnickname(URLDecoder.decode(nickname));
				}
				//比较权限日期
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				if(user != null && !"".equals(user.getOrg_id())){//机构非空,查询机构过期时间
					List<Map<String, Object>> list = userSSOMapper.queryOrgChannelDate(user.getOrg_id());
					if(list != null){
						for(int i = 0; i < list.size();i++){
							Map<String, Object> map = list.get(i);
							if(map != null){
								if("1".equals(String.valueOf(map.get("channel_id")))){//音频
											if(!"".equals(dealNull(user.getAudio_date()))){//
												if(format.parse(user.getAudio_date()).compareTo((Date)map.get("end_date")) < 0 ){//机构日期大
													user.setAudio_date(format.format((Date)map.get("end_date")));
												}
											}else{
												user.setAudio_date(format.format((Date)map.get("end_date")));
											}
										}
										if("3".equals(String.valueOf(map.get("channel_id")))){//视频
											if(user.getVideo_date()==null||user.getVideo_date().isEmpty()){
												user.setVideo_date(format.format((Date)map.get("end_date")));
											}else if(map.get("end_date")==null||map.get("end_date")==""){
												user.setVideo_date(user.getVideo_date());
											}
											else{
												if(format.parse(user.getVideo_date()).compareTo((Date)map.get("end_date")) < 0 ){//机构日期大
													user.setVideo_date(format.format((Date)map.get("end_date")));
												}else{
													user.setVideo_date(user.getVideo_date());
												}
											}
										}
										if("4".equals(String.valueOf(map.get("channel_id")))){//直播
											if(user.getLive_date()==null||user.getLive_date().isEmpty()){
												user.setLive_date(format.format((Date)map.get("end_date")));
											}else if(map.get("end_date")==null||map.get("end_date")==""){
												user.setLive_date(user.getLive_date());
											}
											else{
												if(format.parse(user.getLive_date()).compareTo((Date)map.get("end_date")) < 0 ){//机构日期大
													user.setLive_date(format.format((Date)map.get("end_date")));
												}else{
													user.setLive_date(user.getLive_date());
												}
											}
										}
									}
								}
						}
					}else{//机构空,则为个人过期时间
						
					}
				}
			} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * 
	 * @param str
	 * @return
	 */
	private String dealNull(String str){
		if(str == null || "".equals(str.trim()) || "null".equals(str.trim())){
			str = "";
		}
		return str.trim();
	}

}
