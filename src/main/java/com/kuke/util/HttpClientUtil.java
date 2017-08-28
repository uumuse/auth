package com.kuke.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClientUtil {
	
	static Logger  logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	static int CONNECTIONTIMEOUTMILLIS = 20000;

	static int SOCKETTIMEOUTMILLIS = 2000000000;
	
	public static String executePost(String url, Map<String, String> params){
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		if(params != null && !params.isEmpty()){
			for(String key: params.keySet()){
				method.addParameter(new NameValuePair(key, params.get(key)));
			}
		}
		
		String result = "";
		try {
			client.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			method.releaseConnection();
		}
		return result;
	}
	
	/**
	 * 执行Http的返回结果<Get>
	 */
	public static String executeService(String url)throws Exception {
		logger.debug("SSOService executeService start!");
		String result = "";
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		//设置超时事件
		HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),CONNECTIONTIMEOUTMILLIS);   
		HttpConnectionParams.setSoTimeout(httpclient.getParams(), SOCKETTIMEOUTMILLIS);  		 

		try {
			HttpGet httpget = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpclient.execute(httpget, responseHandler);
			result = responseBody;			
			logger.debug("SSOService executeService end!");
		}catch(Exception e){
			logger.debug("SSOService executeService error:"+e);			
		} finally {
			httpclient.getConnectionManager().shutdown();
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
			System.out.println("1111111111111111111111 executeServicePOST result:"+result);
			logger.debug("SSOService executeServicePOST end!");
		}catch(Exception e){
			logger.debug("SSOService executeServicePOST error:"+e);
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
			
			HttpClient client = new HttpClient();
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
	
	//微信支付，发送url，xml参数
		private final static int CONNECT_TIMEOUT = 5000; // in milliseconds
		private final static String DEFAULT_ENCODING = "UTF-8";
		public static String postData(String urlStr, String data){  
	        return postData(urlStr, data, null);  
	    } 
		public static String postData(String urlStr, String data, String contentType){  
		        BufferedReader reader = null;  
		        try {  
		            URL url = new URL(urlStr);  
		            URLConnection conn = url.openConnection();  
		            conn.setDoOutput(true);  
		            conn.setConnectTimeout(CONNECT_TIMEOUT);  
		            conn.setReadTimeout(CONNECT_TIMEOUT);  
		            if(contentType != null)  
		                conn.setRequestProperty("content-type", contentType);  
		            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);  
		            if(data == null)  
		                data = "";  
		            writer.write(data);   
		            writer.flush();  
		            writer.close();    
		  
		            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));  
		            StringBuilder sb = new StringBuilder();  
		            String line = null;  
		            while ((line = reader.readLine()) != null) {  
		                sb.append(line);  
		                sb.append("\r\n");  
		            }  
		            return sb.toString();  
		        } catch (IOException e) {  
		            System.out.println("Error connecting to " + urlStr + ": " + e.getMessage());  
		        } finally {  
		            try {  
		                if (reader != null)  
		                    reader.close();  
		            } catch (IOException e) {  
		            }  
		        }  
		        return null;  
		    }
	
	public static void main(String[] args) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("lcodes", "0181dx_01|0181dx_02");
		String url = "http://api.kuke.com/api/music/track/list.json";
		String result = HttpClientUtil.executePost(url, params);
		System.out.println(result);
		
	}
}
