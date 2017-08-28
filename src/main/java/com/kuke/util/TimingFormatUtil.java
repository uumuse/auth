package com.kuke.util;

import org.apache.log4j.chainsaw.Main;

import com.kuke.auth.util.PropertiesHolder;

public class TimingFormatUtil {
	/**
	 * 得到专辑的封面
	 * @param ItemCode
	 * @return
	 */
	public static long format(String timing){
		if("".equals(StringUtil.dealNull(timing))){
			return 0;
		}else{
			timing = StringUtil.dealNull(timing);
			String[] ss = timing.split(":");
			String hour = ss[0];
			String min = ss[1];
			String sec = ss[2];
			
			long total = Integer.parseInt(hour) * 3600 * 1000 +Integer.parseInt(min) * 60 * 1000 + Integer.parseInt(sec)*1000;
			return total;
		}
		
	}
	public static void main(String[] args) {
		String s = "07:06:02";
		System.out.println(format(s));
	}
}
