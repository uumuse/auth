package com.kuke.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class TrackUtil {

	/**
	 * JSON字符串特殊字符处理，比如：“\A1;1300”
	 * 
	 * @param s
	 * @return String
	 */
	public static String string2Json(String s) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '/':
				sb.append("\\/");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(c);
			}
		}

		return sb.toString();
	}

	public static String sendGet(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			// String urlNameString = url + "?" + param;
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			// connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			// Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// System.out.println(key + "--->" + map.get(key));
			// }
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			// System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public static String getTrackSrc(String streamCode, String lcode, String labelid, String itemCode) {
		String file = getFileStirng(streamCode, lcode, labelid, itemCode);

		return track(file);
	}

	private static String track(String file) {
		String result = "faild";
		try {
			StringBuffer sb = new StringBuffer();
			String[] ids = file.split("~");
			String label_id = ids[0];
			String quality = ids[1];
			String item_code = ids[2];
			String l_code = ids[3];
			String http = "http://";
			String mediaServerIp = "music.kuke.com";
			String md5key = "2012KukEStreaM<!@~";
			String quality_file = "normbit";
			if (quality != null && quality.equals("192")) {
				quality_file = "highbit";
			} else {
				quality_file = "normbit";
			}
			if (null == label_id || "".equals(label_id) || "null".equals(label_id)) {
				label_id = "Other";
			}
			String type = "_full_wm_" + quality + ".mp3";
			String path = label_id + "/" + item_code + "/" + l_code + type;
			Long time = new Date().getTime();
			MD5 md5 = new MD5();
			String md5str = md5.getMD5ofStr(md5key + time + "/mp3/" + quality_file + "/" + path);
			sb.append(http).append(mediaServerIp).append("/");
			sb.append(time + "/");
			sb.append(md5str + "/");
			sb.append("mp3/" + quality_file + "/");
			sb.append(path);
			String fileStr = sb.toString();
			result = fileStr;
		} catch (Exception e) {
		}
		return result;
	}

	private static String getFileStirng(String flag, String trk, String labelid, String itemCode) {
		String level = "normal128".equals(flag) ? "192" : "192";
		String file = "";
		try {
			if (null == labelid) {
				labelid = "Other";
			}
			file = labelid + "~" + level + "~" + itemCode + "~" + trk;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

}
