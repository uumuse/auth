package com.kuke.auth.play.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.auth.play.mapper.PlayMapper;
import com.kuke.auth.util.KuKeUrlConstants;
import com.kuke.common.utils.ResponseMsg;
import com.kuke.util.HttpClientUtil;
import com.kuke.util.ImageUrlUtil;
import com.kuke.util.StringUtil;

@Service
public class PlayService implements IPlayService{

	@Autowired PlayMapper playMapper;
	
	@Override
	public Map<String,Date> getBothAudioDate(String uid) {
		return playMapper.getBothAudioDate(uid);
	}
	
	public List<Map<String,String>> getOrgDate(String orgId){
		return playMapper.getOrgDate(orgId);
	}
	@Override
	public Map<String, String> getMusicBookById(String id) {
		return playMapper.getMusicBookById(id);
	}
	
	/**
	 * 判断单曲有无320的音质。
	 * 有：返回true；无：返回false；
	 */
	@Override
	public String getTrackKbpsForM(String l_code,String code) {
		if(code==null||code.equals("")){
			code = getTrackKbps(l_code);
			return code;
		}
		String kbps = "";
		l_code = StringUtil.dealNull(l_code);
		if(!"".equals(l_code)){
			String post_url = KuKeUrlConstants.getTrackUrl;
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("lcode", l_code));
			try {
				String result = HttpClientUtil.executeServicePOST(post_url, nvps);
				ResponseMsg msg = new ResponseMsg(result);
				JSONObject json = null;
				if(msg.getFlag()){
					json = JSONObject.fromObject(msg.getData());
					String flag_192 = StringUtil.dealNull(json.get("kbps192").toString());
					String flag_320 = StringUtil.dealNull(json.get("kbps320").toString());
					if(("1".equals(flag_192))&&("1".equals(flag_320))){
						if(code.equals("192")){
							return "192";
						}else{
							return "320";
							}
					}
					else if((!"1".equals(flag_192))&&("1".equals(flag_320))){
						return "320";
					}
					else if(("1".equals(flag_192))&&(!"1".equals(flag_320))){
						return "192";
					}
					else{
						return "0";
					}
				}else{
					return "0";
				}
					
			} catch (IOException e) {
				return "0";
			}
		}else{
			return "0";
		}
	}
	/**
	 * 判断单曲有无320的音质。
	 * 有：返回true；无：返回false；
	 */
	@Override
	public String getTrackKbps(String l_code) {
		l_code = StringUtil.dealNull(l_code);
		if(!"".equals(l_code)){
			String post_url = KuKeUrlConstants.getTrackUrl;
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("lcode", l_code));
			try {
				String result = HttpClientUtil.executeServicePOST(post_url, nvps);
				ResponseMsg msg = new ResponseMsg(result);
				JSONObject json = null;
				if(msg.getFlag()){
					json = JSONObject.fromObject(msg.getData());
					if("1".equals(StringUtil.dealNull(json.get("kbps320").toString()))){
						return "320";
					}else if("1".equals(StringUtil.dealNull(json.get("kbps192").toString()))){
						return "192";
					}else{
						return "0";
					}
				}else{
					return "0";
				}
			} catch (IOException e) {
				e.printStackTrace();
				return "0";
			}
		}else{
			return "0";
		}
	}

}
