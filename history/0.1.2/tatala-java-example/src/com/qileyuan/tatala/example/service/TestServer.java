package com.qileyuan.tatala.example.service;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.proxy.TestDefaultProxy;
import com.qileyuan.tatala.proxy.DefaultProxy;
import com.qileyuan.tatala.socket.server.AioSocketServer;
import com.qileyuan.tatala.util.Configuration;

public class TestServer {
	static Logger log = Logger.getLogger(TestServer.class);
	
	public static void initialize(){
		log.info("Test Socket Server initialize...");
	}
	
	public static void startup(){
		log.info("Test Socket Server starting...");
		
		int listenPort = Configuration.getIntProperty("Server.Socket.listenPort");
		int poolSize = Configuration.getIntProperty("Server.Socket.poolSize");
		AioSocketServer server = new AioSocketServer(listenPort, poolSize);
		
		try {
			DefaultProxy defaultProxy = new TestDefaultProxy();
			server.registerProxy(defaultProxy);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		log.info("*** Test Socket Server ***");
		initialize();
		startup();
	}
}
