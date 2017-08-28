package com.kuke.auth.userCenter.bean;

public class CatalsOfFolder {
	private String id="";//musicfolder_relation  Id
	private String musicfolder_id="";//夹子ID
	private String user_id="";//用户ID
	private String source_id="";//资源ID
	private String create_date="";//
	
	private String itemcode="";//
	private String labelid="";//
	private String ctitle="";//
	private String title="";//
	private String releaseDate="";//
	private String comment="";//
	private String contentType="";//
	private String getType="";//
	private String timing="";//
	private String duration="";//
	private String lastTime="";//
	
	private String showable="";//上下架
	
	private String fid="";//网页端的 ,收藏的ID
	private String imgurl="";//网页端,图片地址
	
	private String price="";//
	private String pv="";//
	private String playCount="";//
	
	private String catalTitle="";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMusicfolder_id() {
		return musicfolder_id;
	}
	public void setMusicfolder_id(String musicfolder_id) {
		this.musicfolder_id = musicfolder_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getSource_id() {
		return source_id;
	}
	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getItemcode() {
		return itemcode;
	}
	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}
	public String getLabelid() {
		return labelid;
	}
	public void setLabelid(String labelid) {
		this.labelid = labelid;
	}
	public String getCtitle() {
		return ctitle;
	}
	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getGetType() {
		return getType;
	}
	public void setGetType(String getType) {
		this.getType = getType;
	}
	public String getTiming() {
		return timing;
	}
	public void setTiming(String timing) {
		this.timing = timing;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public String getPv() {
		return pv;
	}
	public void setPv(String pv) {
		this.pv = pv;
	}
	public String getPlayCount() {
		return playCount;
	}
	public void setPlayCount(String playCount) {
		this.playCount = playCount;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getCatalTitle() {
		return catalTitle;
	}
	public void setCatalTitle(String catalTitle) {
		this.catalTitle = catalTitle;
	}
	public String getShowable() {
		return showable;
	}
	public void setShowable(String showable) {
		this.showable = showable;
	}
	@Override
	public String toString() {
		return "catalsOfFolder [id=" + id + ", musicfolder_id="
				+ musicfolder_id + ", user_id=" + user_id + ", source_id="
				+ source_id + ", create_date=" + create_date + ", itemcode="
				+ itemcode + ", labelid=" + labelid + ", ctitle=" + ctitle
				+ ", title=" + title + ", releaseDate=" + releaseDate
				+ ", comment=" + comment + ", contentType=" + contentType
				+ ", getType=" + getType + ", timing=" + timing + ", duration="
				+ duration + ", lastTime=" + lastTime + ", price=" + price
				+ ", fid=" + fid + ", pv=" + pv + ", playCount=" + playCount
				+ "]";
	}
}
