/**
 * Project Name:service_app
 * Package Name:com.kuke.core.util
 * File Name:JSONUtil.java 
 * Create Time:2012-9-21 下午03:26:58
 * Copyright (c) 2006 ~ 2012 Kuke Tech Dept All rights reserved.
 */

package com.kuke.core.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 *Class JSONUtil
 *Descripton goes here
 *@author：Administrator
 *@version: Revision:2.0.0, 2012-9-21 下午03:26:58
 */
public final class JSONUtil {

    // 将JSON转换成Map,其中valueClz为Map中value的Class,keyArray为Map的key  
    public static Map jsonToMap(String json) {  
        JSONObject jsonObject = JSONObject.fromObject(json);  
        Map classMap = new HashMap();  
        return (Map) JSONObject.toBean(jsonObject, Map.class, classMap);  
    } 
    
    // 将Map转换成JSON
	public static String MapToJson(Map map) {

		JSONArray jsonArray = JSONArray.fromObject(map);
		return jsonArray.toString();
	}
	
	public static String beanToJSON(Object bean){
		JSONObject jsonObject = (JSONObject)JSONSerializer.toJSON(bean);
		return jsonObject.toString();
	}
}

