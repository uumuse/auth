/*
 * Copyright (c) 2010 Shanda Corporation. All rights reserved.
 *
 * Created on 2010-12-3.
 */

package com.kuke.pay.sdo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.kuke.pay.sdo.config.enums.SignType;

/**
 * Define the basic configure parameters for merchant
 *
 * @author lujian.jay
 */
public final class Configuration_1 {
	
	private Configuration_1() {}

    ///////////////////////基本配置信息，不需要更改///////////////////////////////////////////
    /**
     * 盛付通网关版本号
     */
    public static String version = "3.0";

    /**
     * 货币类型 
     * 目前默认只支持RMB
     */
    public static String currencyType = "RMB";

    
    public static String payChannel;
    
    
    /**
     * 默认选中支付渠道
     * 
     * 用户可以在开通的渠道中任意选择一个，如果不选择默认就是渠道的第一个值
     * 例如，你开通的渠道是03,04，默认就是：03
     */
    public static String defaultChannel;
    
    
    /**
     * 商户在盛付通开通的商户号
     */
    public static String merchantNo;

    /**
     * 商户在盛付通申请的密钥
     */
    public static String key;

    /**
     * 签名方式
     * 1:RSA
     * 2:MD5
     * 3:PKI
     */
    public static String signType = SignType.MD5.getCode();

    /**
     * 后台服务器通知调用的URL
     * 
     * 这个地址主要用于当支付成功后，盛付通系统会调用这个也没通知您可以发货啦
     */
    public static String notifyURL;

    /**
     * 付款完成后的跳转页面
     * 
     * 这个地址主要用于支付成功后用于显示给客户的页面
     */
    public static String postBackURL;

    /**
     * 重新下单地址
     */
    public static String backURL;

    /**
     * 服务器通知的调用方式，http或者https
     */
    public static String notifyURLType = "http";


    /**
     * 默认网关地址
     * 测试地址: http://pre.netpay.sdo.com/paygate/default.aspx
     * 正式地址: http://netpay.sdo.com/paygate/default.aspx
     */
    public static String paymentGateWayURL;
   
    public static String bankCode;
    
    private static String _error = "";
    
    public static String getError() {
    	return _error;
    }
    
    public static void setError(String error) {
    	_error += error;
    }
    
    public static void clearError() {
    	_error = "";
    }
    
    static {
    	InputStream  in = Configuration_1.class.getClassLoader().getResourceAsStream("sdoconfig.properties");
    	
    	Properties p = new Properties();
    	try {
			p.load(in);
		} catch (IOException e) {
			setError(e.getMessage());
		} finally {
			try {
				if(in != null)
					in.close();
			} catch (IOException e) {
				//ignore
			}
		}
		
		Configuration_1.merchantNo = getValue(p , "merchantNo"
				,"用户必须配置自己的商户号码（修改sdoconfig.properties文件的__key键.）<br/>");
		
		Configuration_1.key = getValue(p , "MD5Key" 
				, "用户必须配置自己的MD5 key值（修改sdoconfig.properties文件的__key键.）<br/>");
		
		Configuration_1.paymentGateWayURL = getValue(p , "paymentGateWayURL_1" 
				, "用户应该根据开发环境（测试或者正式）配置相应的支付网关地址（修改sdoconfig.properties文件的__key键.）<br/>");
		
		Configuration_1.payChannel = getValue(p , "payChannel_1" 
				, "用户应该根据自己开通的相应渠道配置好渠道（修改sdoconfig.properties文件的__key键.）<br/>");
		String _value = p.getProperty("defaultChannel");
		if(_value != null && _value.trim().length() != 0)
			Configuration_1.defaultChannel = _value.trim();
		else if(Configuration_1.payChannel != null && Configuration_1.payChannel.length() != 0)
			Configuration_1.defaultChannel = Configuration_1.payChannel.split(",")[0];
			
		Configuration_1.notifyURL = getValue(p , "notifyURL_1" 
				, "用户必须配置发货通知地址，否则盛付通系统无法通知您发货（修改sdoconfig.properties文件的__key键.）<br/>");
		
		Configuration_1.postBackURL = getValue(p , "postBackURL_1" 
				, "用户必须配置显示回调地址，否则无法显示支付成功信息给客户（修改sdoconfig.properties文件的__key键.）<br/>");
		
		Configuration_1.backURL = getValue(p , "backURL_1" 
				, "");
		Configuration_1.backURL = Configuration_1.backURL == null ? "" : Configuration_1.backURL;
		Configuration_1.bankCode = getValue(p , "bankCode" 
				, "");
		Configuration_1.bankCode = Configuration_1.bankCode == null ? "SDTBNK" : Configuration_1.bankCode;
		
		Configuration_1.notifyURLType = getValue(p, "notifyURLType" 
				, "");
		Configuration_1.notifyURLType = Configuration_1.notifyURLType == null ? "http" : Configuration_1.notifyURLType;
    }
    
    private static String getValue(Properties p , String key , String errorInfo) {
    	String _value = p.getProperty(key);
		if(_value == null || _value.trim().length() == 0)
			setError(errorInfo.replace("__key", key));
		else
			_value = _value.trim();
		
		return _value;
    }
}
