package com.kuke.auth.userCenter.bean;

public class TracksOfFolder {
	private String id="";//musicfolder_relation  Id
	private String musicfolder_id="";//夹子ID
	private String user_id="";//用户ID
	private String source_id="";//资源ID
	private String create_date="";//创建时间
	private String itemcode="";//专辑ID
	private String lcode="";//单曲ID
	private String workId="";//作品 ID
	private String ctitle="";//单曲 中文名
	private String title="";//单曲 英文名
	private String trackDesc="";//单曲 英文名
	private String timing="";
	private String isrc="";
	private String cd="";
	private String track="";
	private String duration="";
	private String kbps64="";
	private String kbps192="";
	private String kbps320="";
	private String wav="";
	private String workTitle="";
	private String workCtitle="";
	private String workDesc="";
	private String labelid="";//厂牌ID
	
	private String mcctitle="";//网页端 专辑
	private String mctitle="";//网页端 专辑
	private String imgurl="";//网页端 单曲 封面地址
	private String showable="";//上下架
	
	private String trackTitle="";//单曲要显示的题目
	private String calogTitle="";//专辑要显示的题目
	
	private String orderNum="";
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
	public String getLcode() {
		return lcode;
	}
	public void setLcode(String lcode) {
		this.lcode = lcode;
	}
	public String getWorkId() {
		return workId;
	}
	public void setWorkId(String workId) {
		this.workId = workId;
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
	public String getTrackDesc() {
		return trackDesc;
	}
	public void setTrackDesc(String trackDesc) {
		this.trackDesc = trackDesc;
	}
	public String getTiming() {
		return timing;
	}
	public void setTiming(String timing) {
		this.timing = timing;
	}
	public String getIsrc() {
		return isrc;
	}
	public void setIsrc(String isrc) {
		this.isrc = isrc;
	}
	public String getCd() {
		return cd;
	}
	public void setCd(String cd) {
		this.cd = cd;
	}
	public String getTrack() {
		return track;
	}
	public void setTrack(String track) {
		this.track = track;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getKbps64() {
		return kbps64;
	}
	public void setKbps64(String kbps64) {
		this.kbps64 = kbps64;
	}
	public String getKbps192() {
		return kbps192;
	}
	public void setKbps192(String kbps192) {
		this.kbps192 = kbps192;
	}
	public String getKbps320() {
		return kbps320;
	}
	public void setKbps320(String kbps320) {
		this.kbps320 = kbps320;
	}
	public String getWav() {
		return wav;
	}
	public void setWav(String wav) {
		this.wav = wav;
	}
	public String getWorkTitle() {
		return workTitle;
	}
	public void setWorkTitle(String workTitle) {
		this.workTitle = workTitle;
	}
	public String getWorkCtitle() {
		return workCtitle;
	}
	public void setWorkCtitle(String workCtitle) {
		this.workCtitle = workCtitle;
	}
	public String getWorkDesc() {
		return workDesc;
	}
	public void setWorkDesc(String workDesc) {
		this.workDesc = workDesc;
	}
	public String getLabelid() {
		return labelid;
	}
	public void setLabelid(String labelid) {
		this.labelid = labelid;
	}
	public String getMcctitle() {
		return mcctitle;
	}
	public void setMcctitle(String mcctitle) {
		this.mcctitle = mcctitle;
	}
	public String getMctitle() {
		return mctitle;
	}
	public void setMctitle(String mctitle) {
		this.mctitle = mctitle;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getTrackTitle() {
		return trackTitle;
	}
	public void setTrackTitle(String trackTitle) {
		this.trackTitle = trackTitle;
	}
	public String getCalogTitle() {
		return calogTitle;
	}
	public void setCalogTitle(String calogTitle) {
		this.calogTitle = calogTitle;
	}
	public String getShowable() {
		return showable;
	}
	public void setShowable(String showable) {
		this.showable = showable;
	}
	@Override
	public String toString() {
		return "tracksOfFolder [id=" + id + ", musicfolder_id="
				+ musicfolder_id + ", user_id=" + user_id + ", source_id="
				+ source_id + ", create_date=" + create_date + ", itemcode="
				+ itemcode + ", lcode=" + lcode + ", workId=" + workId
				+ ", ctitle=" + ctitle + ", title=" + title + ", trackDesc="
				+ trackDesc + ", timing=" + timing + ", isrc=" + isrc + ", cd="
				+ cd + ", track=" + track + ", duration=" + duration
				+ ", kbps64=" + kbps64 + ", kbps192=" + kbps192 + ", kbps320="
				+ kbps320 + ", wav=" + wav + ", workTitle=" + workTitle
				+ ", workCtitle=" + workCtitle + ", workDesc=" + workDesc
				+ ", labelid=" + labelid + ", mcctitle=" + mcctitle
				+ ", mctitle=" + mctitle + ", orderNum=" + orderNum + "]";
	}
}
