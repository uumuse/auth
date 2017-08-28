package com.kuke.test.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface TestMapper {
	
	public int insertUser(@Param("list") List params);
	
	public int insertpertain(@Param("list") List params);
	
	public int insertDownUser(@Param("list") List paramList);
	
	public List getDownUser();
	
}
