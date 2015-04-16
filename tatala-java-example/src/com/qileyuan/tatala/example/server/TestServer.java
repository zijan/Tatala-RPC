package com.qileyuan.tatala.example.server;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.proxy.TestDefaultProxy;
import com.qileyuan.tatala.proxy.DefaultProxy;
import com.qileyuan.tatala.socket.server.AioSocketServer;

public class TestServer {
	static Logger log = Logger.getLogger(TestServer.class);
	
	public static void initialize(){
		log.info("Test Socket Server initialize...");
	}
	
	public static void startup(int listenPort, int poolSize){
		log.info("Test Socket Server starting...");
		
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
		int listenPort = 10001;
		int poolSize = 16;
		if(args != null && args.length > 1){
			listenPort = Integer.parseInt(args[0]);
			poolSize = Integer.parseInt(args[1]);
		}
		 
		initialize();
		startup(listenPort, poolSize);
	}
}
