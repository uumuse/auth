package com.kuke.authorize.mapper;

import org.apache.ibatis.annotations.Param;

import com.kuke.authorize.bean.ClientInfo;

public interface PianoRoomMapper {
	
	public void addClientInfo(@Param(value = "clientInfo") ClientInfo clientInfo);

}
