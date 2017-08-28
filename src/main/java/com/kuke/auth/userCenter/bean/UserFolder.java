package com.kuke.auth.userCenter.bean;

import java.util.Map;

public class UserFolder {
	private long id = 0;// 夹子ID
	private String type="";// 夹子类型
	private String foldername="";// 夹子名称
	private String imgurl="";// 架子封面
	private String user_id="";// 用户名
	private String source_id="";//item_code(夹子封面使用)
	private String create_date="";//创建时间
	private String countres="";//关联资源的数量
	private String itemcode="";//夹子下的专辑code
	private String itemcodeurl="";//夹子下的专辑封面
	private Map<String, String> sourceMap;// 对应该夹子下的资源
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFoldername() {
		return foldername;
	}
	public void setFoldername(String foldername) {
		this.foldername = foldername;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public Map<String, String> getSourceMap() {
		return sourceMap;
	}
	public void setSourceMap(Map<String, String> sourceMap) {
		this.sourceMap = sourceMap;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getCountres() {
		return countres;
	}
	public void setCountres(String countres) {
		this.countres = countres;
	}
	public String getSource_id() {
		return source_id;
	}
	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}
	public String getItemcode() {
		return itemcode;
	}
	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}
	public String getItemcodeurl() {
		return itemcodeurl;
	}
	public void setItemcodeurl(String itemcodeurl) {
		this.itemcodeurl = itemcodeurl;
	}
	@Override
	public String toString() {
		return "UserFolder [id=" + id + ", type=" + type + ", foldername="
				+ foldername + ", imgurl=" + imgurl + ", user_id=" + user_id
				+ ", create_date=" + create_date + ", countres=" + countres
				+ ", sourceMap=" + sourceMap + "]";
	}
}
