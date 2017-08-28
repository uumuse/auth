package com.kuke.auth.regist.service.impl;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kuke.auth.log.util.LogUtil;
import com.kuke.auth.regist.domain.User;
import com.kuke.auth.regist.domain.UserBaseVarify;
import com.kuke.auth.regist.mapper.RegistMapper;
import com.kuke.auth.regist.mapper.UserMapper;
import com.kuke.auth.regist.service.IRegistService;
import com.kuke.auth.snslogin.bean.SnsUser;
import com.kuke.auth.snslogin.quartz.ApplicationSns;
import com.kuke.auth.ssologin.bean.Organization;
import com.kuke.auth.ssologin.util.UserAuthUtils;
import com.kuke.auth.userCenter.bean.UserMessage;
import com.kuke.auth.util.KuKeAuthConstants;
import com.kuke.auth.util.LogRecord;
import com.kuke.auth.util.OrgOauth;
import com.kuke.auth.util.SystemConstants;
import com.kuke.auth.util.UserOauth;
import com.kuke.core.engine.ICookie;
import com.kuke.core.util.DateUtil;
import com.kuke.util.IdGenerator;
import com.kuke.util.ImageUrlUtil;
import com.kuke.util.MD5;
import com.kuke.util.StringUtil;

@Service
public class RegistService implements IRegistService {

	@Autowired RegistMapper registMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	public static String SNS_FAST_REGIST_DEFAULT_PASSWORD = "123456";
//	private static String SNS_FAST_REGIST_SYSTEM_MESSAGE = "库客音乐会不定期推荐高质量的音乐内容给您，请<a href=\"javascript:;\" onclick=\"url_href('t=a/kuke/userCenter/updatePassword?position=message&autocompletename=${name}&autocompletepass=123456')\">设置常用邮箱</a>，以便您能及时收到。您的登录账户：";
	
	
	
	@Override
	public String getCodeByMobile(String mobile) {
		String code = registMapper.getCodeByMobile(mobile);
		return code;
	}
	//新用户免费听7天
	public void insertUserAuth(String id,String uid, String audio_date) {
		registMapper.insertUserAuth(id,uid, audio_date);
	}
	@Override
	public void insertMobileCode(String mobile, String code) {
		if(checkCodeByMobile(mobile)>0){
			registMapper.updateMobileCode(mobile,code);
		}else
		registMapper.insertMobileCode(mobile, code);
	}
	@Override
	public int checkCodeByMobile(String mobile) {
		return registMapper.checkMobileCode(mobile);
	}

	@Override
	public void insertUser(User user) {
		if(registMapper.selectUser(user)<1){
			registMapper.insertUser(user);
		}
	}
	public void insertUserSych(User user){
		List list = registMapper.selectUserByEmail(user);
		if(list!=null && list.size()<1){
			registMapper.insertUserSych(user);
		}
	}
	
	@Override
	public void insertUserBasePertains(String user_id,String nick_name) {
		if(registMapper.selectUserPertain(user_id)<1){
			registMapper.insertUserBasePertains(user_id,nick_name);
		}
	}
	@Override
	public void insertUserBasePertain(String user_id) {
		if(registMapper.selectUserPertain(user_id)<1){
			registMapper.insertUserBasePertain(user_id);
		}
	}
	
	public User getSnsUserByOldName(String name) {
		return userMapper.getSnsUserByOldName(name);
	}

	@Override
	public com.kuke.auth.login.bean.User snsFastRegist(String user_id,HttpServletRequest request,HttpServletResponse response) {
		com.kuke.auth.login.bean.User new_user=null;
		try {
			String snsKey = (String) request.getAttribute("key");
			SnsUser snsUser = ApplicationSns.getInstance().SNSMapInfo(snsKey);
			
			if(snsUser!=null){
				com.kuke.auth.regist.domain.User user  = this.getSnsUserByOldName(snsUser.getSns_type()+"_"+snsUser.getSns_id());
				boolean msgFlag = false;
				//之前未注册过的，先注册
				if(user==null){
					msgFlag = true;
					String nickName = snsUser.getNick_name();
					nickName=checkName(nickName,snsUser.getSns_type());
					Organization org = OrgOauth.orgLogin(request, response);
					String org_id = org.getOrg_status().equals(KuKeAuthConstants.SUCCESS)?org.getOrg_id():"";
//					String org_id = "AD3F95E0C7F611DCA4D2890DC22AF3D3";
					String requestIp = request.getHeader("X-Real-IP");
					user = this.executeSaveUser(user_id,nickName, UserAuthUtils.getMd5Password(SNS_FAST_REGIST_DEFAULT_PASSWORD), "",
							requestIp,org_id,"1",snsUser.getSns_type()+"_"+snsUser.getSns_id(),snsUser.getHeadimgurl());
				}
				//登录kuke
				String from_client = "".equals(StringUtil.dealNull(request.getParameter("from_client")))?"web":StringUtil.dealNull(request.getParameter("from_client"));
				Map<String, String> map = new HashMap<String, String>();
				map.put("type", "UID");
				map.put("userid",user.getId());
				map.put("from_client",from_client);
				new_user = UserOauth.userLoginByUP(response,map);
				UserAuthUtils.getInstance().setUserCookie(response, new_user,"1");
				ApplicationSns.getInstance().SNSMapOut(snsKey);
				
				if(msgFlag){//通知
					String sysMsg = "您的账户初始密码为："+SNS_FAST_REGIST_DEFAULT_PASSWORD+"；为了您账户的安全，请您及时<a href='http://auth.kuke.com/kuke/userCenter/userInf?site=2'>修改密码</a>";
					UserMessage msg = new UserMessage();
					msg.setTitle("系统通知");
					msg.setContents(sysMsg);
					msg.setFlag(0);//
					msg.setType(0);//
					msg.setReceive_user_id(user.getId());
					msg.setSend_user_id("admin");
					userMapper.insertUserMessage(msg);
				}
				
				//记录日志
				LogUtil.addRegistLog(new_user.getUid(),new_user.getOrg_id(),request);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new_user;
	}
	
	/**
	 * 检查昵称是否存在 
	 */
	public String checkName(String nickName,String snsType) throws Exception {
		if (this.checkUserByName(nickName.toLowerCase())) {
			String ran = Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);
			String preName = URLDecoder.decode(nickName);
			System.out.println(URLDecoder.decode(nickName));
			System.out.println(URLEncoder.encode(preName+"_"+ran));
			System.out.println(URLEncoder.encode("_"+ran));
			return URLEncoder.encode(URLDecoder.decode(nickName)+"_"+ran);
		}
		return nickName;
	}
	/**
	 * 查询用户信息 BY userName
	 * @param userName
	 * @return
	 */
	public String getIdbyName(String userName) {
		return userMapper.checkUserByName(userName).get(0);
	}
	/**
	 * 
	 * @param nickName
	 * @return
	 */
	public boolean checkUserByName(String nickName) {
		boolean result = false;
		List<String> s = userMapper.checkUserByName(nickName);
		if(s!=null && s.size()>0){
			result = StringUtils.isNotEmpty(s.get(0));
		}
		return result;
	}
	
	/**
	 *  保存用户
	 * @param nickName
	 * @param password
	 * @param email
	 * @param requestIp
	 * @param org_id
	 * @param isActive
	 * @param snsName
	 * @return
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public User executeSaveUser(String id,String nickName, String password, String email,
			String requestIp,String org_id, String isActive,String snsName,String headimgurl) {
		User user = new User();
		MD5 md5 = new MD5();
		if(headimgurl == null || "".equals(headimgurl.trim())){
			headimgurl = ImageUrlUtil.getUserImg("");
		}
		user.setName(snsName);//user_base 表的name
		user.setImage(headimgurl);//第三方头像的地址(未下载)
		user.setNick_name(nickName);
		user.setPassword(password);
		user.setEmail(email);
		user.setType(SystemConstants.USER_AUTH_REG);
		user.setIsactive(isActive);
		user.setEnd_date(SystemConstants.USER_DEFAULT_END_DATE);
		user.setOrg_id(org_id);

		// 记录用户信息
		if("".equals(StringUtil.dealNull(id))){
			id = IdGenerator.getUUIDHex32();
		}
		user.setId(id);
		
		//1.创建用户
		if (userMapper.createUser(user) != 0) {
			try {
				//2.仅保存昵称
				userMapper.addUserPertain(user);
				//3.新注册用户更新userauth，audio_date加7天
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				insertUserAuth(user.getId(),user.getId(),sdf.format(DateUtil.getafter7Day(new Date())));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return user;
		} else {
			return null;
		}
	}

	@Override
	public User getUserInfo(User user) {
		return userMapper.getUserInfo(user);
	}

	@Override
	public int updateUserInf(Map<String, String> params) {
		return userMapper.updateUserInf(params);
	}
	
	@Override
	public int updateNickName(Map<String, String> params) {
		return userMapper.updateNickName(params);
	}
	
	@Override
	public int updateEmail(Map<String, String> params) {
		return userMapper.updateEmail(params);
	}

	@Override
	public UserBaseVarify getUserVerify(User user) {
		return userMapper.getUserVerify(user);
	}

	@Override
	public int cancelVerify(Map<String, String> params) {
		return userMapper.cancelVerify(params);
	}

	@Override
	public List getUserFriends(User user) {
		return userMapper.getUserFriends(user);
	}
	
	public int updateUserPass(String id, String md5Pass) {
		return userMapper.updateUserPass(id, md5Pass);

	}
	
	@Override
	public boolean checkUserByMail(String email) {
		return StringUtils.isNotEmpty(userMapper.checkUserByMail(email));
	}
	
	@Override
	public int updateUserEmail(String userId, String email) {
		return userMapper.updateUserEmail(userId, email);
	}
	
	@Override
	public int updateUserPhoto(String userId, String photo) {
		return userMapper.updateUserPhoto(userId, photo);
	}
	/**
	 * 邮箱重置
	 * @param pass
	 * @param email
	 */
	public void resetPassByEmail(String pass, String email) {
		userMapper.resetPassByEmail(pass, email);
	}
	/**
	 * 手机重置
	 * @param pass
	 * @param phone
	 */
	public void resetPassByPhone(String pass, String phone) {
		userMapper.resetPassByPhone(pass, phone);
	}


	@Override
	public User getUserByEmail(String email) {
		return userMapper.getUserByEmail(email);
	}


	@Override
	public User getUserByPhone(String phone) {
		return userMapper.getUserByPhone(phone);
	}
	@Override
	public List selectUserByEmail(User user) {
		List list = registMapper.selectUserByEmail(user);
		return list;
	}




	

}
