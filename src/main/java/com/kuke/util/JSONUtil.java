package com.kuke.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {
	public static String beanToJson(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = null;
		try {
			json = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static <T> T jsonToBean(String json, Class<T> clazz) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			T bean = objectMapper.readValue(json, clazz);
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonToMap(String json) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(json, HashMap.class);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
	}

	public static <T> List<T> jsonToList(String json, Class<T> clazz) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
			List<T> list = objectMapper.readValue(json, javaType);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
