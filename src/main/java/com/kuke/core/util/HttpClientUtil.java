/**
 * Project Name:live_app
 * Package Name:com.kuke.core.engine
 * File Name:HttpClientUtil.java 
 * Create Time:2012-4-5 下午04:49:55
 * Copyright (c) 2006 ~ 2012 Kuke Tech Dept All rights reserved.
 */
package com.kuke.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kuke.util.PicMd5Util;

/**
 *Class HttpClientUtil
 *Descripton goes here
 *@author：zhjt
 *@version: Revision:2.0.0, 2012-4-5 下午04:49:55
 */
public final class HttpClientUtil
{
	static Logger  logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	static int CONNECTIONTIMEOUTMILLIS = 10000;

	static int SOCKETTIMEOUTMILLIS = 10000;
	
	
	/**
	 * 执行Http的返回结果<Get>
	 */
	public static String executeService(String url)throws Exception {
		logger.debug("SSOService executeService start!");
		logger.debug("url=" + url);
		String result = "";
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		//设置超时事件
		HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),CONNECTIONTIMEOUTMILLIS);   
		HttpConnectionParams.setSoTimeout(httpclient.getParams(), SOCKETTIMEOUTMILLIS);  		 

		try {
			HttpGet httpget = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpclient.execute(httpget, responseHandler);
			result=responseBody;				
			logger.debug("SSOService executeService end!");
		}catch(Exception e){
			logger.debug("SSOService executeService error:"+e);			
		} finally {
			httpclient.getConnectionManager().shutdown();
			logger.debug("SSOService executeService shutdown!");
		}
		return result;
	}

	/**
	 * 执行Http的返回结果<POST>
	 */
	public static String executeServicePOST(String url, List nvps)
			throws IOException {
		logger.debug("SSOService executeServicePOST start!");	
		String result = "";
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		//设置超时事件
		HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), CONNECTIONTIMEOUTMILLIS);   
		HttpConnectionParams.setSoTimeout(httpclient.getParams(), SOCKETTIMEOUTMILLIS);  		 
		try {
			HttpPost httpost = new HttpPost( url + "?");
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpclient.execute(httpost, responseHandler);
			result=responseBody;
			logger.debug("SSOService executeServicePOST end!");
		}catch(Exception e){
			logger.debug("SSOService executeServicePOST error:"+e);			
		} finally {
			httpclient.getConnectionManager().shutdown();
			logger.debug("SSOService executeServicePOST shutdown!");
		}
		return result;
	}
	
	public static String executeMultipartPOST(String url, MultipartEntity reqEntity) {
		logger.debug(" executeMultipartPOST start!");
		String result = "";
		HttpClient httpclient = new DefaultHttpClient();
		try {
		    HttpPost httppost = new HttpPost(url);
		    httppost.setEntity(reqEntity);
		    httppost.getRequestLine();
		    ResponseHandler<String> responseHandler = new BasicResponseHandler();
		    String responseBody = httpclient.execute(httppost, responseHandler);
//		    HttpResponse response = httpclient.execute(httppost);
//		    HttpEntity resEntity = response.getEntity();
		    result=responseBody;
		}catch(Exception e){
			logger.debug("executeMultipartPOST error:"+e);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	/**
	 * 下载文件
	 * @param url
	 * @param tmp
	 * @return
	 */
	public static File downloadFile(String url, String tmp){
		String suffix = ".jpg";
		File file = new File(tmp + File.separator + IdGenerator.getUUIDHex32() + suffix);
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		try {
			URL link = new URL(url);
			BufferedInputStream in = new BufferedInputStream(link.openStream());
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			byte[] b = new byte[1024];
			int len = 0;
			while((len=in.read(b)) != -1){
				out.write(b,0,len);
            }
			in.close(); 
	        out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
		}
		return file;
	}
	
	/**
	 * 上传文件至图片服务器
	 * @param uploadUrl
	 * @param file
	 * @param params
	 * @return
	 */
	public static boolean uploadFile(String uploadUrl, File file, String fileName, String type){
		Boolean flag = false;
		PostMethod postMethod = new PostMethod(uploadUrl);
		try {
			Part[] parts = {
				new FilePart("upfile", file), 
				new StringPart("file_name", fileName),
				new StringPart("type", type),
				new StringPart("md5str", PicMd5Util.getPicMd5(type, fileName))
			};
			
			postMethod.setRequestEntity(new MultipartRequestEntity(parts, postMethod.getParams()));
			
			org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			
			int statusCode = client.executeMethod(postMethod);
			// 上传成功
			if (statusCode == HttpStatus.SC_OK) {
				String response = new String(postMethod.getResponseBody(), "UTF-8");
				response = response.substring(response.indexOf("{"), response.indexOf("}") + 1);
				Map<String, Object> jsonMap = JSONUtil.jsonToMap(response);
				flag = (Boolean)jsonMap.get("result");
				logger.debug(response);
				//FileUtils.forceDelete(file);
			}
			// 上传失败
			else {
				logger.debug("图片 " + file.getName() + " 上传失败 [statusCode:" + statusCode + "]");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return flag;
	}
}

