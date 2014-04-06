package com.qileyuan.tatala.example.service;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.socket.server.AioSocketServer;
import com.qileyuan.tatala.util.Configuration;

public class ChatRoomServer {
	static Logger log = Logger.getLogger(ChatRoomServer.class);
	
	public static void initialize(){
		log.info("Chat Room Server initialize...");
	}
	
	public static void startup(){
		log.info("Chat Room Server starting...");
		
		int listenPort = Configuration.getIntProperty("Server.Socket.listenPort");
		int poolSize = Configuration.getIntProperty("Server.Socket.poolSize");
		
		AioSocketServer server = new AioSocketServer(listenPort, poolSize);
		server.start();
		
		ChatRoomServerLogic serverLogic = ChatRoomServerLogic.getInstance();
		serverLogic.setSessionMap(AioSocketServer.getSessionMap());
		
	}
	
	public static void main(String args[]) {
		log.info("*** Chat Room Server ***");
		initialize();
		startup();
	}
}
