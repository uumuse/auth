package com.kuke.auth.log.util;

import java.io.IOException;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;

public class UserAgentUtil {

	public static UASparser uasParser = null;

	// 初始化uasParser对象
	static {
		try {
			uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
