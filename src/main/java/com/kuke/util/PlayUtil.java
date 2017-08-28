package com.kuke.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class PlayUtil {
	/***
	 * 音频地址
	 * @param labelid
	 * @param itemcode
	 * @param lcode
	 * @param code
	 * @return
	 */
	public static String getPlayAddressOfAudio(String labelid, String itemcode, String lcode, String code) {
		StreamCode streamcode = StreamCode.getStreamCode(code, "mp3");
		String playAddress = "faild";
		try {
			StringBuffer sb = new StringBuffer();
			String http = "https://";
			String mediaServerIp = ResourceUtil.getResVByK("http.mp3.server");// "music.kuke.com";
			String md5key = ResourceUtil.getResVByK("http.mp3.password");// "2012KukEStreaM<!@~";
			String quality = streamcode.val();
			if (StringUtils.isBlank(labelid) || "null".equals(labelid)) {
				labelid = "Other";
			}
			String mediaName = lcode + (quality.equals("wav") ? ".wav" : "_full_wm_" + code + ".mp3");
			String path = labelid + "/" + itemcode + "/" + mediaName;

			Long time = new Date().getTime();
			MD5 md5 = new MD5();
			String md5str = md5.getMD5ofStr(md5key + time + "/mp3/" + quality + "/" + path);
			sb.append(http).append(mediaServerIp).append("/");
			sb.append(time + "/");
			sb.append(md5str + "/");
			sb.append("mp3/" + quality + "/");
			sb.append(path);
			String fileStr = sb.toString();
			playAddress = fileStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return playAddress;
	}

	/***
	 * live地址
	 * @param itemId
	 * @param code
	 * @return
	 */
	public static String getPlayAddressOfLive(String itemId, String code) {
		String result = "faild";
		StreamCode streamCode = StreamCode.getStreamCode(code, "mp4");
		try {
			StringBuffer sb = new StringBuffer();
			String http = "https://";
			// String user_ip = mp3.get("user_ip");
			String mediaServerIp = ResourceUtil.getResVByK("http.media.server");
			// String mediaServerIp = "video.naxos.cn";
			String md5key = ResourceUtil.getResVByK("http.media.password");
			// String md5key ="2012KukEStreaM<!@~";
			String quality = streamCode.value;
			String path = itemId + ".mp4";

			Long time = new Date().getTime();
			MD5 md5 = new MD5();
			String md5str = md5.getMD5ofStr(md5key + time + "/mp4/live/" + quality + "/" + path);

			sb.append(http).append(mediaServerIp).append("/");
			sb.append(time + "/");
			sb.append(md5str + "/");
			sb.append("mp4/live/" + quality + "/");
			sb.append(path);
			String fileStr = sb.toString();
			result = fileStr;
		} catch (Exception e) {
		}
		return result;
	}
	
	/***
	 * 视频播放地址
	 * @param catalogueid
	 * @param partNo
	 * @param code
	 * @return
	 */
	public static String getPlayAddressOfVideo(String catalogueid, String partNo, String code) {
		String result = "faild";
		StreamCode streamCode = StreamCode.getStreamCode(code, "mp4");
		try {
			StringBuffer sb = new StringBuffer();
			String http = "https://";
			String mediaServerIp = ResourceUtil.getResVByK("http.media.server");
			String md5key = ResourceUtil.getResVByK("http.media.password");
			String quality_file = streamCode.val();
			String quality_part = "." + code + "kb.part";
			String path = "/mp4/video/" + quality_file + "/" + catalogueid + quality_part + partNo + ".mp4";
			Long time = new Date().getTime();
			MD5 md5 = new MD5();
			String md5str = md5.getMD5ofStr(md5key + time + path);
			sb.append(http).append(mediaServerIp).append("/");
			sb.append(time + "/");
			sb.append(md5str);
			sb.append(path);
			String fileStr = sb.toString();
			result = fileStr;
		} catch (Exception e) {
		}
		return result;
	}
	
	
	/***
	 * 乐谱下载地址
	 * @param music_book_file
	 * @return
	 */
	public static String initMusicBookPlayUrl(String music_book_file) {
		
		String result = "faild";
		try {
			StringBuffer sb = new StringBuffer();
			String http = "https://";
//			String music_book_file = mp3.get("music_book_file");
			String mediaServerIp = ResourceUtil.getResVByK("http.mp3.server");
			String md5key = ResourceUtil.getResVByK("http.media.password");
			String path =  music_book_file + ".pdf";
			
			Long time = new Date().getTime();
			MD5 md5 = new MD5();
			String md5str = md5.getMD5ofStr(md5key+ time+"/mp3/musicbook/"+path );

			sb.append(http).append(mediaServerIp).append("/");
			sb.append(time+"/");
			sb.append(md5str+"/");
			sb.append("mp3/musicbook/");
			sb.append(path);
			String fileStr = sb.toString();
			result = fileStr;
			System.out.println(result);
		} catch (Exception e) {
		}
		return result;
}
	private enum StreamCode {
		KBPS_64("normbit"), KBPS_192("highbit"), KBPS_320("320kbps"), WAV("wav"), KBPS_200("200kbps"), KBPS_700("700kbps");

		private final String value;
		private final static Map<String, StreamCode> streamCodeMap = new HashMap<String, PlayUtil.StreamCode>();
		static {
			streamCodeMap.put("64", KBPS_64);
			streamCodeMap.put("192", KBPS_192);
			streamCodeMap.put("320", KBPS_320);
			streamCodeMap.put("wav", WAV);
			streamCodeMap.put("200", KBPS_200);
			streamCodeMap.put("700", KBPS_700);
		}

		private StreamCode(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return getValue();
		}

		public String val() {
			return value;
		}

		public static StreamCode getStreamCode(String code, String type) {
			if (StringUtils.isNotBlank(code)) {
				return streamCodeMap.get(code.toLowerCase());
			} else {
				if ("mp3".equals(type)) {
					return streamCodeMap.get("192");
				} else {
					return streamCodeMap.get("700");
				}
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(getPlayAddressOfAudio("HCC", "CD93.108", "H93108_01", "192"));
		System.out.println(getPlayAddressOfAudio("HCC", "CD93.108", "H93108_01", "320") + "\n");

		System.out.println(getPlayAddressOfLive("1D5180A0E58E11E080A08637D0967606", "200"));
		System.out.println(getPlayAddressOfLive("1D5180A0E58E11E080A08637D0967606", "700") + "\n");
		
		System.out.println(getPlayAddressOfVideo("A05500586", "1", "200"));
		System.out.println(getPlayAddressOfVideo("A05500586", "1", "700") + "\n");
	}

}
