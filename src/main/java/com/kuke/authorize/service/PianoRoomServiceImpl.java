package com.kuke.authorize.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kuke.authorize.bean.ClientInfo;
import com.kuke.authorize.mapper.PianoRoomMapper;

@Service
public class PianoRoomServiceImpl implements PianoRoomService {

	@Autowired
	private PianoRoomMapper pianoRoomMapper;
	
	@Override
	public void addClientInfo(ClientInfo clientInfo) {
		pianoRoomMapper.addClientInfo(clientInfo);
	}

}
