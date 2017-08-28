package com.kuke.util;  
  
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
  
public class SaveImage {  
	
    //把从服务器获得图片的输入流InputStream写到本地磁盘  
    public static void saveImageToDisk(String URL_PATH,String LOCAL_PATH) {  
  
        InputStream inputStream = getInputStream(URL_PATH);  
        byte[] data = new byte[1024];  
        int len = 0;  
        FileOutputStream fileOutputStream = null;  
        try {  
            fileOutputStream = new FileOutputStream(LOCAL_PATH);  
            while ((len = inputStream.read(data)) != -1) {  
                fileOutputStream.write(data, 0, len);  
  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (inputStream != null) {  
                try {  
                    inputStream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (fileOutputStream != null) {  
                try {  
                    fileOutputStream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
  
        }  
  
    }  
    // 从服务器获得一个输入流(本例是指从服务器获得一个image输入流)  
    private static InputStream getInputStream(String URL_PATH) {  
        InputStream inputStream = null;  
        HttpURLConnection httpURLConnection = null;  
  
        try {  
            URL url = new URL(URL_PATH);  
            httpURLConnection = (HttpURLConnection) url.openConnection();  
            // 设置网络连接超时时间  
            httpURLConnection.setConnectTimeout(3000);  
            // 设置应用程序要从网络连接读取数据  
            httpURLConnection.setDoInput(true);  
  
            httpURLConnection.setRequestMethod("GET");  
            int responseCode = httpURLConnection.getResponseCode();  
            if (responseCode == 200) {  
                // 从服务器返回一个输入流  
                inputStream = httpURLConnection.getInputStream();  
            }  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return inputStream;  
    }  
  
    public static void main(String args[]) {  
        saveImageToDisk("http://image.kuke.com/images/upload/photo/9417799a-5689-4131-81cf-11f4c709dbc4.jpg","D://test1.jpg");  
    }  
}  