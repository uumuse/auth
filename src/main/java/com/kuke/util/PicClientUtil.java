package com.kuke.util;

import java.io.File;
//import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PicClientUtil {
	static Logger  logger = LoggerFactory.getLogger(HttpClientUtil.class);
	@SuppressWarnings("deprecation")
	public static String executePost(String uri, Map<String, String> params) {
		String returnString = "";

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			param.add(new BasicNameValuePair(key, params.get(key)));
		}
		
		try{

			HttpPost post = new HttpPost(uri);
			post.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
			
			HttpClient http = HttpClients.createDefault();
			HttpResponse response = http.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				HttpEntity entity = response.getEntity();
				returnString = JSONFormatUtil.formatJson(EntityUtils.toString(entity));
			}
			else {
				returnString = response.getStatusLine().toString();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnString;
	}
	/**
	 * 上传文件至图片服务器
	 * 
	 * @param uploadUrl
	 * @param file
	 * @param params
	 * @return
	 */
	public static boolean uploadFile(String uploadUrl, File file, String fileName, String type) {
		Boolean flag = false;
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		HttpClient httpclient = clientBuilder.build();
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addPart("upfile", new FileBody(file));	
		if(StringUtils.isNotBlank(fileName)){
			builder.addTextBody("file_name", fileName);
			builder.addTextBody("md5str", PicMd5Util.getPicMd5(type, fileName));
		}
		else{
			builder.addTextBody("md5str", PicMd5Util.getPicMd5(type));
		}
		builder.addTextBody("type", type);
		
		
		HttpPost httpPost = new HttpPost(uploadUrl);
        httpPost.setEntity(builder.build());
        
		try {
			HttpResponse httpResponse = httpclient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			// 上传成功
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = httpResponse.getEntity();
				String respCnt = EntityUtils.toString(resEntity);
				System.out.println("respCnt:"+respCnt);
				Map<String, Object> jsonMap = JacksonUtil.jsonToMap(respCnt);
				System.out.println("relpath:"+jsonMap.get("relpath"));
				// FileUtils.forceDelete(file);
				flag = true;
			}
			// 上传失败
			else {
				logger.debug("图片 " + file.getName() + " 上传失败 [statusCode:" + statusCode + "]");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return flag;
	}

	public static void main(String[] args) {
		//String uploadUrl = "http://img.kuke.com/v2upload";
		String uploadUrl = "http://Upload.kuke.com/v2upload";
		File file = new File("C://1.jpg");
		String fileName = "222.jpg"; //"20161125170208.jpg";
		String type = "new_photo";
		System.out.println(uploadFile(uploadUrl, file, fileName, type));
	}
}
