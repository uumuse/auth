package com.kuke.auth.iosReciptAuthPay.util;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class IosUtil {
	/***
	 * 
	 * @param receipt
	 * @return
	 */
	public static JSONObject verifyReceipt(String receipt,boolean f){
		
		String url_formal = "https://buy.itunes.apple.com/verifyReceipt";
//		String url_sandbox = "https://sandbox.itunes.apple.com/verifyReceipt"; 
        String url = "";
//		if(f){
			url = url_formal;
//		}else
//			url = url_sandbox;
		
        JSONObject json = new JSONObject();  
        json.put("receipt-data",receipt);
        
        String returnValue = "这是默认返回值，接口调用失败";  
        CloseableHttpClient httpClient = HttpClients.createDefault();  
        ResponseHandler<String> responseHandler = new BasicResponseHandler();  
        try{  
            //创建HttpClient对象  
        	httpClient = HttpClients.createDefault();  
              
            //创建httpPost对象  
            HttpPost httpPost = new HttpPost(url);  
              
            //给httpPost设置JSON格式的参数  
            StringEntity requestEntity = new StringEntity(json.toString(),"utf-8");  
            requestEntity.setContentEncoding("UTF-8");                
            httpPost.setHeader("Content-type", "application/json");  
            httpPost.setEntity(requestEntity);  
             
            //第四步：发送HttpPost请求，获取返回值  
            returnValue = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，必须用此方法  
            System.out.println("returnValue:"+returnValue);
            
            @SuppressWarnings("static-access")
			JSONObject jsons = json.fromObject(returnValue);
            return jsons;
        }catch(Exception e){  
             e.printStackTrace();
        }finally {  
           try {  
        	   httpClient.close();  
           } catch (IOException e) {  
        	   e.printStackTrace();  
           }  
        } 
        return null;
	}
	
//	public static JSONObject checkThread(final String receipt,final HttpServletRequest request){
//		ExecutorService exec = Executors.newFixedThreadPool(10);  
//        
//		JSONObject obj = null;
//        try {  
//            @SuppressWarnings("unchecked")
//			final
//			Future<JSONObject> future = (Future<JSONObject>) exec.submit(new Runnable() {
//				JSONObject obj = null;
//				@Override
//				public void run() {
//					String status = "";
//					status = obj.getString("status");
//					if(!status.equals("") && status.equals("21007")){
//						obj = IosUtil.verifyReceipt(receipt,false);
//					}
//	                    System.out.println("结束时间："+System.currentTimeMillis());  
//				}
//			});  
////            obj = future.get(30000 * 1, TimeUnit.MILLISECONDS); //任务处理超时时间设为 10 秒  
//            String status = obj.getString("status");
//            if(status.equals("0")){
//            	future.cancel(true);
//            }else{
//            	obj = future.get(30000 * 1, TimeUnit.MILLISECONDS);
//            }
//            System.out.println("验证成功返回:" + obj);  
//        } catch (TimeoutException ex) {  
//            System.out.println("验证超时.");  
//            ex.printStackTrace();  
//        } catch (Exception e) {  
//            System.out.println("验证失败.");  
//            e.printStackTrace();  
//        }  
//        // 关闭线程池  
//        exec.shutdown();
//		return obj;  
//
//	}
}
