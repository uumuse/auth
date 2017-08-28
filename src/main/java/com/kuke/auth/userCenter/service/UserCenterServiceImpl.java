package com.kuke.auth.userCenter.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.auth.login.bean.User;
import com.kuke.auth.userCenter.bean.UserAuthorize;
import com.kuke.auth.userCenter.bean.UserFolder;
import com.kuke.auth.userCenter.bean.UserMessage;
import com.kuke.auth.userCenter.mapper.UserCenterMapper;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.ImageUrlUtil;
import com.kuke.util.PageInfo;
import com.kuke.util.StringUtil;

@Service
public class UserCenterServiceImpl implements UserCenterService {

	@Autowired
	private UserCenterMapper userCenterMapper;

	@SuppressWarnings("unchecked")
	@Override
	public List getUserBill(Map params, int offset, int rows) {

		return userCenterMapper.getUserBill(params, offset, rows);

	}

	public List getPointsExchangeRecord(Map<String, String> params, int offset,
			int rows) {
		return null;

		// return userCenterMapper.getPointsExchangeRecord(params, offset,
		// rows);
	}

	public List getBillInf(Map<String, String> params, int offset, int rows) {
		return userCenterMapper.getBillInf(params,offset,rows);
	}
	
	public List getBillInfIOS(Map<String, String> params, int offset, int rows) {
		return userCenterMapper.getBillInfIOS(params,offset,rows);
	}

	public Map<String, String> getUserAuthorize(Map<String, String> params) {
		Map<String, String> data = null;
		UserAuthorize userAuthorize = userCenterMapper.getUserAuthorize(params);
		Date nowDate = new Date();
		try {
			if(userAuthorize != null){
				//美化时间
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				//音频
				if (userAuthorize.getAudio_date() != null && (userAuthorize.getAudio_date().getTime() > nowDate.getTime())) {
					userAuthorize.setAudio_flag("inDate");
				}else if(userAuthorize.getAudio_date() != null && (userAuthorize.getAudio_date().getTime() < nowDate.getTime())) {
					userAuthorize.setAudio_flag("outDate");
				}
				//直播
				if (userAuthorize.getLive_date() != null && (userAuthorize.getLive_date().getTime() > nowDate.getTime())) {
					userAuthorize.setLive_flag("inDate");
				} else if (userAuthorize.getLive_date() != null && (userAuthorize.getLive_date().getTime() < nowDate.getTime())) {
					userAuthorize.setLive_flag("outDate");
				}
				data = new HashMap<String, String>();
				if(userAuthorize.getAudio_date() != null){//音频
					userAuthorize.setAudio_date(sf.parse(sf.format(userAuthorize.getAudio_date())));
					data.put("audio_date", sf.format(userAuthorize.getAudio_date()));
					data.put("audio_flag", userAuthorize.getAudio_flag());
					data.put("audio_pro_name", "音频包月");
				}
				if(userAuthorize.getLive_date() != null){//直播
					userAuthorize.setLive_date((sf.parse(sf.format(userAuthorize.getLive_date()))));
					data.put("live_date", sf.format(userAuthorize.getLive_date()));
					data.put("live_flag", userAuthorize.getLive_flag());
					data.put("live_pro_name", "直播包月");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public List getCouponBill(Map<String, String> params) {
		return userCenterMapper.getCouponBill(params);
	}

	@Override
	public UserAuthorize getUserMoney(Map<String, String> params) {
		return userCenterMapper.getUserMoney(params);
	}

	@Override
	public List<Map<String, String>> getMustFavourite() {
		return userCenterMapper.getMustFavourite();
	}

	@Override
	public int addFavourite(String user_id, String id,String type) {
		id = StringUtil.formatCommaForString(id);
		String[] source = id.split(",");
		return userCenterMapper.addFavourite(user_id,source,type);
	}
	
	@Override
	public String getFavourite(String user_id, String id,String type) {
		return userCenterMapper.getFavourite(user_id, id,type);
	}
	@Override
	public List<String> getFavouriteRes(String user_id, String[] source){
		return userCenterMapper.getFavouriteRes(user_id, source);
	}
	
	@Override
	public List<String> getFavouriteTheme(Map params,int offset,int rows){
		return userCenterMapper.getFavouriteTheme(params,offset,rows);
	}
	@Override
	public String checkSubscribe(String user_id, String source_id,String type) {
		return userCenterMapper.checkSubscribe(user_id, source_id,type);
	}

	@Override
	public int getCountFavourite(String uid) {
		return userCenterMapper.getCountFavourite(uid);
	}
	
	@Override
	public List<Map<String, String>> userEdit(String userid,String state) {
		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
		List<Map<String, String>> userEdit=userCenterMapper.userEdit(userid,state);
		if(userEdit.size()>0){
			for(Map<String, String> map:userEdit){
				String str=map.get("item_code");
				String s=str.substring(0,1);
				map.put("url", ImageUrlUtil.getItemCodeImage(str));
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public Map<String,String> getOrgExpireTime(String orgId) {
		List<Map<String, String>> list =userCenterMapper.getOrgExpireTime(orgId);
		Map<String, String> result = new HashMap<String, String>();
		for(Map<String, String> m :list){
			if(String.valueOf(m.get("channel_id")).equals("1")){
				result.put("audiodate", String.valueOf(m.get("end_date")));
			}
			else if(String.valueOf(m.get("channel_id")).equals("3")){
				result.put("videodate", String.valueOf(m.get("end_date")));
			}
			else if(String.valueOf(m.get("channel_id")).equals("4")){
				result.put("livedate", String.valueOf(m.get("end_date")));
			}else{
				result.put("spokendate", String.valueOf(m.get("end_date")));
			}
		}
		return result;
	}

	@Override
	public int checkBoundPhone(String phoneNum) {
		return userCenterMapper.checkBoundPhone(phoneNum);
	}

	@Override
	public int checkUserEmail(String userEmail) {
		return userCenterMapper.checkUserEmail(userEmail);
	}

	@Override
	public List<Map<String, String>> getRecentPlay(String uid) {
		return userCenterMapper.getRecentPlay(uid);
	}

	@Override
	public List getFavoriteTrack(Map params, int offset, int rows) {
		return userCenterMapper.getFavoriteTrack(params, offset, rows);
	}

	@Override
	public List getFavoriteCatal(Map params, int offset, int rows) {
		List list = userCenterMapper.getFavoriteCatal(params, offset, rows);
		for(Map<String,Object> map :(List<Map<String,Object>>)list){
			String item_code = String.valueOf(map.get("itemcode")) ;
//			String item_code = String.valueOf(dataList.get(0).get("item_code"));
			String imgUrl = "";
			if(item_code.equals("CD-0914")){
				System.out.println("11111111");
			}
			imgUrl = "http://image.kuke.com/images/audio/cata200/"+item_code.substring(0, 1)+"/"+item_code+".jpg";
			map.put("imgUrl", imgUrl);
		}
		return list;
	}

	@Override
	public List getFavoriteVedio(Map params, int offset, int rows) {
		List list = userCenterMapper.getFavoriteVedio(params, offset, rows);
		for(Map<String,Object> map :(List<Map<String,Object>>)list){
			String vid = String.valueOf(map.get("source_id")) ;
			map.put("imgUrl", "http://image.kuke.com/images/video/cata/listimage/"+vid+".jpg");
		}
		return list;
	}

	@Override
	public void cancleFavorite(String uid, String[] array,String type) {
		userCenterMapper.cancleFavorite(uid, array,type);
	}
	
	@Override
	public void cancleSubscribe(String uid, String[] array,String type) {
		userCenterMapper.cancleSubscribe(uid, array,type);
	}
	
	@Override
	public void addSubscribe(String uid, List<String> array,String type) {
		userCenterMapper.addSubscribe(uid, array,type);
	}
	
	@Override
	public List<String> getSubscribe(String uid, String[] array,String type) {
		return userCenterMapper.getSubscribe(uid, array,type);
	}

	@Override
	public List getSubscribeLabel(Map params, int offset, int rows) {
		return userCenterMapper.getSubscribeLabel(params, offset, rows);
	}

	@Override
	public List getSubscribeArtist(Map params, int offset, int rows) {
		return userCenterMapper.getSubscribeArtist(params, offset, rows);
	}
	
	@Override
	public List getTrackFolderList(Map params, int offset, int rows) {
		return userCenterMapper.getTrackFolderList(params, offset, rows);
	}
	
	@Override
	public List getTracksOfFolder(Map params, int offset, int rows) {
		return userCenterMapper.getTracksOfFolder(params, offset, rows);
	}
	
	@Override
	public List getCatalFolderList(Map params, int offset, int rows) {
		return userCenterMapper.getCatalFolderList(params, offset, rows);
	}
	
	@Override
	public List getCatalsOfFolder(Map params, int offset, int rows) {
		return userCenterMapper.getCatalsOfFolder(params, offset, rows);
	}

	@Override
	public List<UserFolder> queryMyFolders(Map params) {
		return userCenterMapper.queryMyFolders(params);
	}
	
	@Override
	public List<UserFolder> queryUserFolders(Map params, int offset, int rows) {
		return userCenterMapper.queryUserFolders(params, offset, rows);
	}
	
	@Override
	public UserFolder createFavoritesFolder(Map params) {
		UserFolder userFolder = new UserFolder();
		userFolder.setType((String)params.get("type"));
		userFolder.setFoldername((String)params.get("folder_name"));
		userFolder.setUser_id((String)params.get("userId"));
		userCenterMapper.createFavoritesFolder(userFolder);
		return userFolder;
	}
	
	@Override
	public int editFavFolderName(Map params) {
		return userCenterMapper.editFavFolderName(params);
	}
	
	@Override
	public int delFavoritesFolder(String uid,String[] musicfolder_ids) {
		return userCenterMapper.delFavoritesFolder(uid,musicfolder_ids);
	}
	
	@Override
	public int delFavoritesFolderRelation(String uid,String[] musicfolder_ids) {
		return userCenterMapper.delFavoritesFolderRelation(uid,musicfolder_ids);
	}
	
	@Override
	public int delFavoritesSourceOfFolder(String uid,String musicfolder_id,String[] source_ids) {
		return userCenterMapper.delFavoritesSourceOfFolder(uid,musicfolder_id,source_ids);
	}
	
	@Override
	public int addFolderSourceToFolder(String uid,String musicfolder_id,String premusicfolder_id,String type) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", uid);
		params.put("musicfolder_id", premusicfolder_id);
		//之前的夹子ID
		List list = userCenterMapper.getSourceOfFolder(params);
		params.put("musicfolder_id", musicfolder_id);
		//新的夹子ID
		List newlist = userCenterMapper.getSourceOfFolder(params);
		StringBuffer source_id = new StringBuffer();
		for(int i = 0; i < list.size(); i++){
			String s = (String) list.get(i);
			if(newlist == null || !newlist.contains(s)){//不包含则拼接
				source_id.append(","+s);
			}
		}
		
		//过滤掉失效资源:已下架
		String source = "";
		if("2".equals(type)){//专辑
			source = StringUtil.dealNull(userCenterMapper.getNoOffSourceItem(StringUtil.formatCommaForString(source_id.toString()).split(",")));
		}else{
			String lcode = "";
			for(int i = 0;i < source_id.toString().split(",").length; i++){
				if(!"".equals(StringUtil.dealNull(source_id.toString().split(",")[i]))){
					lcode += "," + (StringUtil.dealNull(source_id.toString().split(",")[i])).split("\\|")[0];
				}
			}
			
			String lcodeSource = StringUtil.dealNull(userCenterMapper.getNoOffFolderLcode(StringUtil.formatCommaForString(lcode).split(",")));
			
			for(int i = 0;i < source_id.toString().split(",").length; i++){
				if(!"".equals(StringUtil.dealNull(source_id.toString().split(",")[i]))){
					String groupSouce = StringUtil.dealNull(source_id.toString().split(",")[i]);//组合资源
					String lid = (StringUtil.dealNull(source_id.toString().split(",")[i])).split("\\|")[0];
					for(int j = 0;j < lcodeSource.split(",").length; j++){
						if(!"".equals(StringUtil.dealNull(lcodeSource.split(",")[j]))){
							if(lid.equals(StringUtil.dealNull(lcodeSource.split(",")[j]))){
								source += "," + groupSouce;
							}
						}
					}
				}
			}
		}
		source = StringUtil.formatCommaForString(source);
		String[] source_ids = source.length() > 0?source.split(","):null;
		if(source_ids != null && source_ids.length > 0){
			return userCenterMapper.addSourceToFolder(uid,musicfolder_id,source_ids);
		}else{
			return 0;
		}
	}
	
	@Override
	public int addSourceToFolder(String uid,String musicfolder_id,String[] source_ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", uid);
		params.put("musicfolder_id", musicfolder_id);
		//要添加的夹子ID中的资源
		List<String> list = userCenterMapper.getSourceOfFolder(params);
		
		//要添加的资源
		List<String> newList = new ArrayList<String>();
		for(int i = 0; i < source_ids.length ;i++){
			if(!"".equals(StringUtil.dealNull(source_ids[i]))){
				newList.add(StringUtil.dealNull(source_ids[i]));
			}
		}
		
		//
		StringBuffer source_id = new StringBuffer();
		for(int i = 0; i < newList.size(); i++){
			String s = (String) newList.get(i);
			if(list == null || !list.contains(s)){//不包含则拼接
				source_id.append(","+s);
			}
		}
		String[] newsource_ids = source_id.toString().length() > 0?source_id.toString().substring(1).split(","):null;
		if(newsource_ids != null && newsource_ids.length > 0){
			return userCenterMapper.addSourceToFolder(uid,musicfolder_id,newsource_ids);
		}else{
			return 0;
		}
	}
	
	@Override
	public int addSourceToFolder(List<String> list,String uid,String musicfolder_id,String[] source_ids) {
		
		//要添加的资源
		List<String> newList = new ArrayList<String>();
		for(int i = 0; i < source_ids.length ;i++){
			if(!"".equals(StringUtil.dealNull(source_ids[i]))){
				newList.add(StringUtil.dealNull(source_ids[i]));
			}
		}
		
		//
		StringBuffer source_id = new StringBuffer();
		for(int i = 0; i < newList.size(); i++){
			String s = (String) newList.get(i);
			if(list == null || !list.contains(s)){//不包含则拼接
				source_id.append(","+s);
			}
		}
		String[] newsource_ids = source_id.toString().length() > 0?source_id.toString().substring(1).split(","):null;
		if(newsource_ids != null && newsource_ids.length > 0){
			return userCenterMapper.addSourceToFolder(uid,musicfolder_id,newsource_ids);
		}else{
			return 0;
		}
	}
	
	@Override
	public List<UserMessage> getSysMessageList(Map params, int offset, int rows) {
		return userCenterMapper.getSysMessageList(params, offset, rows);
	}
	
	@Override
	public int getNoReadSysMessage(String uid) {
		return userCenterMapper.getNoReadSysMessage(uid);
	}

	@Override
	public UserMessage getSingleMessage(Map params) {
		return userCenterMapper.getSingleMessage(params);
	}

	@Override
	public int delSysMessage(Map params, String[] ids) {
		return userCenterMapper.delSysMessage(params,ids);
	}

	@Override
	public List<List<Map<String, String>>> getVIPProInfo(Map params) {
		String flag = (String) params.get("flag");
		List<List<Map<String, String>>> result = new ArrayList<List<Map<String,String>>>();
		String[] channelIDs = null; 
		String audioChannelID = "5";
		String vedioChannelID = "30";
		String liveChannelID = "40";
		if(flag != null && "true".endsWith(flag)){
			channelIDs = new String[3]; 
			channelIDs[0] = audioChannelID;
			channelIDs[1] = vedioChannelID;
			channelIDs[2] = liveChannelID;
		}else{
			channelIDs = new String[2]; 
			channelIDs[0] = audioChannelID;
			channelIDs[1] = liveChannelID;
		}
		List list = userCenterMapper.getVIPProInfo(params, channelIDs);
		List<Map<String, String>> audio = new ArrayList<Map<String,String>>();
		List<Map<String, String>> vedio = new ArrayList<Map<String,String>>();
		List<Map<String, String>> live = new ArrayList<Map<String,String>>();
		for(int i = 0;i < list.size();i++){
			Map m = (Map) list.get(i);
			String pay_channel_id = String.valueOf(m.get("pay_channel_id"));
			String id = String.valueOf(m.get("id"));
			Map<String, String> map = new HashMap<String, String>();
			map.put("month",String.valueOf(m.get("num")));
			map.put("id",id);
			map.put("price",m.get("pro_price").toString());
			if(pay_channel_id != null && !"".equals(pay_channel_id)){
				if(audioChannelID.equals(pay_channel_id)){
					audio.add(map);
				}
				if(vedioChannelID.equals(pay_channel_id)){
					vedio.add(map);
				}
				if(liveChannelID.equals(pay_channel_id)){
					live.add(map);
				}
			}
		}
		result.add(audio);
		result.add(vedio);
		result.add(live);
		return result;
	}
	
	@Override
	public int cancleBill(Map<String, String> params) {
		return userCenterMapper.cancleBill(params);
	}
	
	@Override
	public int invalidBill(String userid,String[] keywords) {
		return userCenterMapper.invalidBill(userid,keywords);
	}
	
	@Override
	public int deleteBill(Map<String, String> params) {
		return userCenterMapper.deleteBill(params);
	}

	@Override
	public int saveFavFolderImg(Map params) {
		return userCenterMapper.saveFavFolderImg(params);
	}

	@Override
	public Map<String, Object> getItemcodeByLcode(String l_code) {
		return userCenterMapper.getItemcodeByLcode(l_code);
	}

	@Override
	public List<String> getUserFavourite(String user_id, String type) {
		return userCenterMapper.getUserFavourite(user_id,type);
	}

	@Override
	public int getFavoriteTrackCount(String uid) {
		return userCenterMapper.getFavoriteTrackCount(uid);
	}

	@Override
	public int getFavoriteCatalCount(String uid) {
		return userCenterMapper.getFavoriteCatalCount(uid);
	}

	@Override
	public int getFavoriteVedioCount(String uid) {
		return userCenterMapper.getFavoriteVedioCount(uid);
	}
}
