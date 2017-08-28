package com.kuke.sonos.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.auth.play.service.PlayService;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.sonos.mapper.SonosMapper;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.ImageUrlUtil;
import com.kuke.util.PlayUtil;
import com.kuke.util.TimingFormatUtil;

@Service
public class SonosService implements ISonosService {

	
	@Autowired
	private SonosMapper sonosMapper;
	
	@Autowired
	private PlayService playService;
	
	@Override
	public Map<String, Object> getItemWindow(String serviceid,String uid,
			String queueVersion, String itemId,String previousWindowSize,
			String upcomingWindowSize) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,String>> playlist = new ArrayList<Map<String,String>>();
		if(itemId==null){
			playlist  = this.getPlayList(uid,serviceid,0,1);
		}else{
			int location = 0;
			List<Map<String,String>> list = this.getPlayList(uid,serviceid,0,0);
			for(int i = 0; i< list.size();i++){
				if(list.get(i).get("playlist").indexOf(itemId)>0){
					System.out.println(list.get(i).get("playlist"));
					location = i + 1;
				}
			}
			int start = 0;
			int end = 0;
			
			int size = list.size();
			if(previousWindowSize!=null){
				if(Integer.parseInt(previousWindowSize)>location){
					start = 0;
				}else
					start = location - Integer.parseInt(previousWindowSize)-1<0?0:location - Integer.parseInt(previousWindowSize);
			}else{
				start = 0;
			}
			
			if(upcomingWindowSize!=null){
				end = size - location ;
			}else{
				end = 1;
			}
			playlist = this.getPlayList(uid,serviceid,start,end);
		}
		
//		JSONArray data = JSONObject.fromObject(JSONArray.fromObject(playlist.get(0)).get(0)).getJSONArray("playlist");
		String item_code = "";
		String labelid = "";
		String code = "";
		String playUrl = "";
		String title = "";
		String ctitle = "";
		String timing =  "";
		String l_code = ""; 
		String workid = "";
			map.put("includesBeginningOfQueue", true);
			map.put("includesEndOfQueue", true);
//			map.put("contextVersion", "kuke_context_version_"+sdf.format(new Date()));
			map.put("queueVersion",queueVersion);
//			map.put("skipsRemaining", 3);///?
			
			List<Object> item_list = new ArrayList<Object>();
			if(!playlist.isEmpty()){
				int i = 1;
					for(Map<String,String> ss:playlist){
						Map<String,Object> item_map = new HashMap<String,Object>();
						List<Object> list = new ArrayList<Object>();
						String source = ss.get("playlist");
						item_code = source.split(",")[0];
						labelid = source.split(",")[1];
						l_code = source.split(",")[2];
						code = playService.getTrackKbps(l_code);
						
						item_map.put("id",l_code);
						item_map.put("deleted", false);
						
						Map<String, String> params = new HashMap<String, String>();
						params.put("lcode", l_code);
						String result =  HttpClientUtil.executePost(KuKeUrlConstants.getTrackUrl, params);
						JSONObject track_data = JSONObject.fromObject(result).getJSONObject("data");
						
						title = JSONObject.fromObject(track_data).getString("title");
						ctitle = JSONObject.fromObject(track_data).getString("ctitle");
						timing = JSONObject.fromObject(track_data).getString("timing");
						workid = JSONObject.fromObject(track_data).getString("workId");
						
						
						Map<String,Object> trackmap = new HashMap<String,Object>();
						
						trackmap.put("mediaUrl", PlayUtil.getPlayAddressOfAudio(labelid, item_code, l_code, code));
						trackmap.put("contentType","audio/mpeg");
						trackmap.put("name", (title==null?ctitle:title));
						trackmap.put("imageUrl", ImageUrlUtil.getItemCodeImage(item_code));
						trackmap.put("durationMillis", TimingFormatUtil.format(timing));
						trackmap.put("trackNumber", i++);
						
						Map<String,Object> track_artist_map = new HashMap<String,Object>();
						
						List<Map<String,String>> musicianId = sonosMapper.getPerformerId(l_code, workid);
						//艺术家
						String fullName = "";
						String musicianIds = "";
						if(musicianId.isEmpty()){
							fullName = "Artist";
						}else{
							for(Map<String,String> a:musicianId){
								musicianIds += String.valueOf(a.get("performer_id")) + ",";
							}
							Map<String, String> para = new HashMap<String, String>();
							para.put("musicianIds", musicianIds.substring(0, musicianIds.lastIndexOf(",")));
							String artist_url = "http://dc.kuke.com/kuke/dc/common/music/getMusicians";
							String re =  HttpClientUtil.executePost(artist_url, para);
							JSONArray artits_data = JSONObject.fromObject(re).getJSONArray("data");
							
							String name = "";
							for(i = 0; i < artits_data.size(); i++){
								JSONObject object = (JSONObject) artits_data.get(i);
								name += object.getString("fullName")+",";
							}
							fullName = name.substring(0, name.lastIndexOf(","));
						}
						track_artist_map.put("name", fullName);
						Map<String,Object> track_artist_id_map = new HashMap<String,Object>();
						track_artist_id_map.put("serviceId", uid);///?
						track_artist_id_map.put("objectId", l_code);///?
						track_artist_map.put("id", track_artist_id_map);
						trackmap.put("artist", track_artist_map);
						
						Map<String,Object> album_map = new HashMap<String,Object>();
						Map<String,Object> album_id_map = new HashMap<String,Object>();
						Map<String,Object> album_artist_map = new HashMap<String,Object>();
						
						//policies
						Map<String,Object> policies_map = new HashMap<String,Object>();
						policies_map.put("canSkip", true);
						policies_map.put("canCrossfade", true);
//					trackmap.put("policies", policies_map);
						
						Map<String, String> params1 = new HashMap<String, String>();
						params1.put("itemcode", item_code);
						String res =  HttpClientUtil.executePost(KuKeUrlConstants.getCataUrl, params1);
						JSONObject cata_data = JSONObject.fromObject(res).getJSONObject("data").getJSONObject("cataloguebio");
						String cata_name = cata_data.getString("title");
						album_map.put("name", cata_name);
//						album_id_map.put("serviceId", uid);
//						album_id_map.put("objectId", l_code);
//					album_map.put("id", album_id_map);
						
						album_artist_map.put("name", fullName);
						album_map.put("artist", album_artist_map);
						
						
						trackmap.put("album", album_map);
//					list.add(trackmap);
						item_map.put("track", trackmap);
						item_list.add(item_map);
					}
				}
			map.put("items", item_list);
			return map;
	}

	@Override
	public void addPlayList(String uid, String serviceid,List<String> playlist) {
		sonosMapper.addPlayList(uid, serviceid, playlist);

	}

	@Override
	public void deletePlayList(String uid) {
		sonosMapper.deletePlayList(uid);

	}

	@Override
	public List<Map<String,String>> getLcodeId(String l_code,String uid, String serviceid) {
		List<Map<String,String>> list = sonosMapper.getLcodeId(l_code, uid, serviceid);
		return list;
	}

	@Override
	public List<Map<String, String>> getPlayList(String uid, String serviceid,int start,int end) {
		List<Map<String,String>> list= sonosMapper.getPlayList(uid,serviceid,start,end);
		return list;
	}


}
