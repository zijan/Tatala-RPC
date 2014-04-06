package com.qileyuan.tatala.example.service;

import java.util.HashMap;
import java.util.Map;

import com.qileyuan.tatala.example.proxy.ChatRoomServerProxy;
import com.qileyuan.tatala.socket.server.ServerSession;

public class ChatRoomServerLogic {
	private static ChatRoomServerLogic serverLogic = new ChatRoomServerLogic();
	private Map<Long, ServerSession> sessionMap; //<clientId, session>
	private Map<Long, String> userMap; //<clientId, userName>
	
	private ChatRoomServerLogic(){
		userMap = new HashMap<Long, String>();
	}
	
	public static ChatRoomServerLogic getInstance(){
		return serverLogic;
	}
	
	public void login(long clientId, String username){
		userMap.put(clientId, username);
		broadcast(clientId, "login");
	}
	
	public void broadcast(long clientId, String message){
		String sendUsername = userMap.get(clientId);
		String sendMessage = sendUsername + ": " + message;
		for (long id : sessionMap.keySet()) {
			if(clientId != id){
				ServerSession session = sessionMap.get(id);
				ChatRoomServerProxy.sendMessage(session, sendMessage);
			}
		}
	}

	public Map<Long, ServerSession> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<Long, ServerSession> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public Map<Long, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<Long, String> userMap) {
		this.userMap = userMap;
	}

}
