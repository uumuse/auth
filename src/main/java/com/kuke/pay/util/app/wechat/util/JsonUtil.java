package com.kuke.pay.util.app.wechat.util;

import net.sf.json.JSONObject;

public class JsonUtil {

	public static String getJsonValue(String rescontent, String key) {
		String v = null;
		try {
			JSONObject jsonObject = JSONObject.fromObject(rescontent);
			v = jsonObject.getString(key);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return v;
	}
}
