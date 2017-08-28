package com.kuke.auth.regist.domain;

public class User implements Cloneable{

	private String id;

	private String name;

	private String nick_name;

	private String image;

	private String password;

	private String email;

	private String type;

	private String reg_date;

	private String end_date;

	private String isactive;// 是否激活

	private String org_id;// 20090505 zhjt修改，标志该用户注册于哪个机构

	private String real_name;
	
	private String province;
	
	private String from_client;
	
	private String city;
	
	private String sex;
	
	private String birthday;
	
	private String constellation;
	
	private String one_of12animals;
	
	private String blood_type;
	
	private String phone;
	
	private String QQ;
	
	private String MSN;
	
	private String about_me;

	private String points;

	private String extra_email;
	
	private String alternate;
	
	private String signature;
	
	public User() {
		super();
	}
	
	public User(String id) {
		super();
		this.id = id;
	}

	public String getFrom_client() {
		return from_client;
	}

	public void setFrom_client(String from_client) {
		this.from_client = from_client;
	}

	public String getExtra_email() {
		return extra_email;
	}

	public void setExtra_email(String extra_email) {
		this.extra_email = extra_email;
	}

	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getIsactive() {
		return isactive;
	}

	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public String getOne_of12animals() {
		return one_of12animals;
	}

	public void setOne_of12animals(String one_of12animals) {
		this.one_of12animals = one_of12animals;
	}

	public String getBlood_type() {
		return blood_type;
	}

	public void setBlood_type(String blood_type) {
		this.blood_type = blood_type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getMSN() {
		return MSN;
	}

	public void setMSN(String mSN) {
		MSN = mSN;
	}

	public String getAbout_me() {
		return about_me;
	}

	public void setAbout_me(String about_me) {
		this.about_me = about_me;
	}
	
	
	public String getAlternate() {
		return alternate;
	}

	public void setAlternate(String alternate) {
		this.alternate = alternate;
	}
	

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public User clone() {  
        User o = null;  
        try {  
            o = (User) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    }
	
}
