package com.kuke.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SyncDataDemo {
	
	public static final String url = "rm-2zey8uka791g2hn6so.mysql.rds.aliyuncs.com/auth?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull";  
    public static final String name = "com.mysql.jdbc.Driver";  
    public static final String user = "admin";  
    public static final String password = "kuke.com";
    
    public static void main(String[] args) {
    	Connection conn = null;  
    	PreparedStatement pst = null; 
    	String sql = "";
    	try {  
            Class.forName(name);//指定连接类型  
            conn = DriverManager.getConnection(url, user, password);//获取连接  
            if(conn != null){
            	System.out.println("梁山了");
            }
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally{
    		try {
    			if(pst != null){
    				pst.close();
    			}
    			if(conn != null){
    				conn.close();
    			}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
	}
}
