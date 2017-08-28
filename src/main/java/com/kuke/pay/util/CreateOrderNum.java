package com.kuke.pay.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.kuke.util.IdGenerator;


public class CreateOrderNum {
	
	public static String createOrdNum(){
		String orderCode = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return orderCode + IdGenerator.getUUIDHex32().substring(0, 9);
	}
	/**
	 * 执行查询
	 * @param sql
	 * @param cp
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public static List executeQuery(String sql){
		List list = new ArrayList();
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
//			conn = DriverManager.getConnection("rm-2zey8uka791g2hn6so.mysql.rds.aliyuncs.com/auth?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull", "admin", "kuke.com");
			conn = DriverManager.getConnection("jdbc:mysql://59.110.14.41:3306/auth?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull", "root", "kuke.com");
			System.out.println(conn == null);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	public static void main(String[] args) {
		CreateOrderNum.executeQuery("");
	}
}
