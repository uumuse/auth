package com.kuke.util.wechat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

public class WXHttpUtil {
	public WXHttpUtil() {

	}

	public static JSONObject getJsonContent(String url_path) {

		try {
			URL url = new URL(url_path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setConnectTimeout(3000);
			connection.setRequestMethod("GET");

			connection.setDoInput(true);

			int code = connection.getResponseCode();
			if (code == 200) {
				return changeInputStream(connection.getInputStream());

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	private static JSONObject changeInputStream(InputStream inputStream) {
		String jsonString = "";
		String nickname = null;
		String headimgurl = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len = 0;
		byte[] data = new byte[1024];
		try {
			while ((len = inputStream.read(data)) != -1) {

				outputStream.write(data, 0, len);
			}
			jsonString = new String(outputStream.toByteArray(),"UTF-8");
			// 到这里已经是乱码了
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			JSONObject jsonObject = JSONObject.fromObject(jsonString);
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
