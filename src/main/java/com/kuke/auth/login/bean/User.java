package com.kuke.auth.login.bean;

import java.io.Serializable;

import net.sf.json.JSONObject;

import com.kuke.auth.util.KuKeAuthConstants;

public class User implements Serializable {

	private static final long serialVersionUID = -4811047760316578113L;

	private String uid="";
	private String unickname="";
	private String uname = "";
	private String uphoto="";
	private String usex="";
	private String uphone="";
	private String uemail="";
	
	private String reg_date="";
	private String end_date="";//账号结束时间
	private String org_id="";
	private String org_name="";
	private String isactive="";
	private String isPop="";
	
	private String ssoid="";
	private String user_status="";
	
	private String is_verify="";
	
	private String audio_date = "";//比较结果,并非数据库值: 与org_channel = 1 的 end_date作比较,哪个长,用哪个
	private String video_date = "";//比较结果,并非数据库值: 与org_channel = 3 的 end_date作比较,哪个长,用哪个
	private String live_date = "";//比较结果,并非数据库值: 与org_channel = 4 的 end_date作比较,哪个长,用哪个
	
	private String countMsg="0";//未读消息数量
	
	private String remain_money = "0";//个人余额
	private String org_money = "0";//机构余额
	private String type = "";//靠什么登录的
	
	public User() {

	}
	
	public User(String res) throws Exception {
		try {
			if(res != null && !"".equals(res)){
				res = res.replaceAll("\\\\", "");
				res = res.startsWith("\"")?res.substring(1):res;
				res = res.endsWith("\"")?res.substring(0,res.length()-1):res;
				JSONObject json = JSONObject.fromObject(res);
				uid = json.getString("uid");
				org_id = json.getString("org_id");
				org_name = json.getString("org_name");
				unickname = json.getString("unickname");
				uphoto = json.getString("uphoto");
				uphone = json.getString("uphone");
				uemail = json.getString("uemail");
				usex = json.getString("usex");
				reg_date = json.getString("reg_date");
				isactive = json.getString("isactive");
				ssoid = json.getString("ssoid");
				user_status = json.getString("user_status");
				is_verify = json.getString("is_verify");
				isPop = json.getString("isPop");
				audio_date = json.getString("audio_date");
				video_date = json.getString("video_date");
				live_date = json.getString("live_date");
			}
		} catch (Exception e) {
			System.out.println("User 创建出错了!");
			e.printStackTrace();
			user_status = KuKeAuthConstants.FAILED;
		}
	}
	
	public String getIs_verify() {
		return is_verify;
	}

	public void setIs_verify(String is_verify) {
		this.is_verify = is_verify;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSsoid() {
		return ssoid;
	}

	public void setSsoid(String ssoid) {
		this.ssoid = ssoid;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String orgId) {
		org_id = orgId;
	}
	
	public String getOrg_name() {
		return org_name;
	}


	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}


	public String getUnickname() {
		return unickname;
	}

	public void setUnickname(String unickname) {
		this.unickname = unickname;
	}

	public String getUphoto() {
		return uphoto;
	}

	public void setUphoto(String uphoto) {
		this.uphoto = uphoto;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String regDate) {
		reg_date = regDate;
	}

	public String getUser_status() {
		return user_status;
	}

	public void setUser_status(String userStatus) {
		user_status = userStatus;
	}

	public String getIsactive() {
		return isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public String getUsex() {
		return usex;
	}

	public void setUsex(String usex) {
		this.usex = usex;
	}


	public String getAudio_date() {
		return audio_date;
	}


	public void setAudio_date(String audioDate) {
		audio_date = audioDate;
	}


	public String getVideo_date() {
		return video_date;
	}


	public void setVideo_date(String videoDate) {
		video_date = videoDate;
	}


	public String getLive_date() {
		return live_date;
	}


	public void setLive_date(String liveDate) {
		live_date = liveDate;
	}


	public String getUphone() {
		return uphone;
	}


	public void setUphone(String uphone) {
		this.uphone = uphone;
	}


	public String getUemail() {
		return uemail;
	}


	public void setUemail(String uemail) {
		this.uemail = uemail;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	
	public String getRemain_money() {
		return remain_money;
	}

	public void setRemain_money(String remain_money) {
		this.remain_money = remain_money;
	}

	public String getOrg_money() {
		return org_money;
	}

	public void setOrg_money(String org_money) {
		this.org_money = org_money;
	}

	public String getCountMsg() {
		return countMsg;
	}

	public void setCountMsg(String countMsg) {
		this.countMsg = countMsg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}
	
	public String getIsPop() {
		return isPop;
	}

	public void setIsPop(String isPop) {
		this.isPop = isPop;
	}

	@Override
	public String toString() {
		return "User [uid=" + uid + ", unickname=" + unickname + ", uphoto="
				+ uphoto + ", usex=" + usex + ", uphone=" + uphone
				+ ", uemail=" + uemail + ", reg_date=" + reg_date + ", org_id="
				+ org_id + ", org_name=" + org_name + ", isactive=" + isactive
				+ ", ssoid=" + ssoid + ", user_status=" + user_status
				+ ", is_verify=" + is_verify + ", audio_date=" + audio_date
				+ ", video_date=" + video_date + ", live_date=" + live_date
				+ "]";
	}
	
}
