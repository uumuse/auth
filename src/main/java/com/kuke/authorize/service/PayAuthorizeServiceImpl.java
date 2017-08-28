package com.kuke.authorize.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.auth.util.KuKePayConstants;
import com.kuke.authorize.mapper.PayAuthorizeMapper;
import com.kuke.util.IdGenerator;

@Service
public class PayAuthorizeServiceImpl implements PayAuthorizeService {

	@Autowired
	private PayAuthorizeMapper payAuthorizeMapper;
	
	@Override
	public String updateMoney(String user_id,String money) {
		String result = KuKePayConstants.FAILED;
		try{
			if (payAuthorizeMapper.selectMoney(user_id) == 1) {
				payAuthorizeMapper.updateMoney(user_id,money);
			}else{
				payAuthorizeMapper.insertMoney(IdGenerator.getUUIDHex32(), user_id, money);
			}
			 result = KuKePayConstants.SUCCESS;
		}catch (Exception e) {
		}
		return result;
	}

	@Override
	public String updateAudioDate(String user_id, String buyNum) {
		String result = KuKePayConstants.FAILED;
		try{
			if (payAuthorizeMapper.selectAuthorize(user_id) == 1) {
				payAuthorizeMapper.updateAuthorizeDate(user_id, buyNum, "1");
			}else{
				payAuthorizeMapper.insertAuthorizeDate(IdGenerator.getUUIDHex32(),user_id, buyNum, "1");
			}
			result = KuKePayConstants.SUCCESS;
		}catch (Exception e) {
		}
		return result;
	}
	
	
	@Override
	public String updateVideo(String user_id, String video_id ) {
		String result = KuKePayConstants.FAILED;
		try {
			payAuthorizeMapper.updateVideo(IdGenerator.getUUIDHex32(),user_id, video_id);
			result = KuKePayConstants.SUCCESS;
		} catch (Exception e) {
		}
		return result;
	}

	@Override
	public String updateVideoDate(String user_id, String buyNum) {
		String result = KuKePayConstants.FAILED;
		try{
			if (payAuthorizeMapper.selectAuthorize(user_id) == 1) {
				payAuthorizeMapper.updateAuthorizeDate(user_id, buyNum, "3");
			}else{
				payAuthorizeMapper.insertAuthorizeDate(IdGenerator.getUUIDHex32(),user_id, buyNum, "3");
			}
			result = KuKePayConstants.SUCCESS;
		}catch (Exception e) {
		}
		return result;
	}
	
	@Override
	public String updateLiveDate(String user_id, String buyNum,String price) {
		String result = KuKePayConstants.FAILED;
		try{
			if (payAuthorizeMapper.selectAuthorize(user_id) == 1) {
				payAuthorizeMapper.updateAuthorizeDate(user_id, buyNum, "4");
			}else{
				payAuthorizeMapper.insertAuthorizeDate(IdGenerator.getUUIDHex32(),user_id, buyNum, "4");
			}
			 result = KuKePayConstants.SUCCESS;
		}catch (Exception e) {
		}
		
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			String end_date =  sdf.format(calendar.getTime());
			for (int i = 0; i < Integer.parseInt(buyNum); i++) {
				calendar.setTime(sdf.parse(end_date));
				calendar.add(Calendar.MONTH, 1);
				end_date = sdf.format(calendar.getTime());
				payAuthorizeMapper.shareLive(IdGenerator.getUUIDHex32(), user_id, "", end_date,price);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return result;
	}

	@Override
	public String updateLive(String user_id, String live_id,String price) {
		String result = KuKePayConstants.FAILED;
		try{
			payAuthorizeMapper.updateLive(IdGenerator.getUUIDHex32(),user_id, live_id);
		}catch (Exception e) {
		}
		try{
			payAuthorizeMapper.shareLive(IdGenerator.getUUIDHex32(), user_id, live_id, "",price);
		}catch (Exception e) {
		}		
		result = KuKePayConstants.SUCCESS;
		return result;
	}

	@Override
	public String updateDownAudio(String user_id, String down_id,String down_type) {
		String result = KuKePayConstants.FAILED;
		try {
			payAuthorizeMapper.updateDown(IdGenerator.getUUIDHex32(),
					user_id, down_id, down_type);
			result = KuKePayConstants.SUCCESS;
		} catch (Exception e) {
		}
		return result;
	}







	
}
