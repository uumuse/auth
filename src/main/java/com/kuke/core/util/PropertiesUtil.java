package com.kuke.core.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

	public static String readValue(String filePath, String key) {
		Properties properties = new Properties();
		try {
			FileInputStream inputFile = new FileInputStream(filePath);
			properties.load(inputFile);
			inputFile.close();
			String value = properties.getProperty(key);
			return value;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e2) {
			e2.printStackTrace();
			return "";
		} catch (Exception e2) {
			e2.printStackTrace();
			return "";
		}
	}

}
