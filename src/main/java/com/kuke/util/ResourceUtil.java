package com.kuke.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceUtil {
	public static String getResVByK(String key) {
		String defaultValue;
		try {
			defaultValue = ResourceBundle.getBundle("application").getString(key);
		} catch (MissingResourceException mse) {
			defaultValue = "NULL";
		}
		return defaultValue;
	}
	
	public static String getResVByK(String key, String dftVal) {
		String defaultValue = dftVal;
		try {
			defaultValue = ResourceBundle.getBundle("application").getString(key);
		} catch (MissingResourceException mse) {
			mse.printStackTrace();
		}
		return defaultValue;
	}

	public static void main(String[] args) {
		String authurl = ResourceUtil.getResVByK("AUTH.URL");
		System.out.println(authurl);
	}

}
