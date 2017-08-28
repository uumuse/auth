package com.kuke.util;

public class ImgUrlConst extends ConfigurableConst{
	static{
		init("imgUrlConst.properties");
	}
	public static final String APP_IMG_URL = getProperty("APP_IMG_URL", "http://img-test.kuke.com/images/app/");
	
}
