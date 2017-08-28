/**
 * Project Name:new_app
 * Package Name:com.kuke.core.util
 * File Name:ConvertUtil.java 
 * Create Time:2012-3-21 ����02:41:45
 * Copyright (c) 2006 ~ 2012 Kuke Tech Dept All rights reserved.
 */
package com.kuke.core.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public final class ConvertUtil
{
	protected static final Logger loger = LoggerFactory
	.getLogger(ConvertUtil.class);
	/**
	 *Class ConvertUtil
	 *Utility class to convert one object to another.
	 *@author zhjt
	 *@version: Revision:2.0.0, 2012-3-21 02:41:45
	 */
	
	private ConvertUtil() {
    }
	
	 /**
     * Method to convert a ResourceBundle to a Map object.
     *
     * @param rb a given resource bundle
     * @return Map a populated map
     */
    public static Map<String, String> convertBundleToMap(ResourceBundle rb) {
        Map<String, String> map = new HashMap<String, String>();

        Enumeration<String> keys = rb.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            map.put(key, rb.getString(key));
        }

        return map;
    }

    /**
     * Convert a java.util.List of LabelValue objects to a LinkedHashMap.
     *
     * @param list the list to convert
     * @return the populated map with the label as the key
     
    public static Map<String, String> convertListToMap(List<LabelValue> list) {
        Map<String, String> map = new LinkedHashMap<String, String>();

        for (LabelValue option : list) {
            map.put(option.getLabel(), option.getValue());
        }

        return map;
    }*/

    /**
     * Method to convert a ResourceBundle to a Properties object.
     *
     * @param rb a given resource bundle
     * @return Properties a populated properties object
     */
    public static Properties convertBundleToProperties(ResourceBundle rb) {
        Properties props = new Properties();

        for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements();) {
            String key = keys.nextElement();
            props.put(key, rb.getString(key));
        }

        return props;
    }

    /**
     * Convenience method used by tests to populate an object from a
     * ResourceBundle
     *
     * @param obj an initialized object
     * @param rb a resource bundle
     * @return a populated object
     */
    public static Object populateObject(Object obj, ResourceBundle rb) {
        try {
            Map<String, String> map = convertBundleToMap(rb);
            BeanUtils.copyProperties(obj, map);
        } catch (Exception e) {
            e.printStackTrace();
            loger.error("Exception occurred populating object: " + e.getMessage());
        }

        return obj;
    }
}

