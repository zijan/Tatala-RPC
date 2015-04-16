package com.qileyuan.tatala.example.server;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.service.ChatRoomServerLogic;
import com.qileyuan.tatala.socket.server.AioSocketServer;

public class ChatRoomServer {
	static Logger log = Logger.getLogger(ChatRoomServer.class);
	
	public static void initialize(){
		log.info("Chat Room Server initialize...");
	}
	
	public static void startup(int listenPort, int poolSize){
		log.info("Chat Room Server starting...");
		
		AioSocketServer server = new AioSocketServer(listenPort, poolSize);
		server.start();
		
		ChatRoomServerLogic serverLogic = ChatRoomServerLogic.getInstance();
		serverLogic.setSessionMap(AioSocketServer.getSessionMap());
	}
	
	public static void main(String args[]) {
		log.info("*** Chat Room Server ***");
		int listenPort = 10002;
		int poolSize = 16;
		if(args != null && args.length > 1){
			listenPort = Integer.parseInt(args[0]);
			poolSize = Integer.parseInt(args[1]);
		}
		initialize();
		startup(listenPort, poolSize);
	}
}
