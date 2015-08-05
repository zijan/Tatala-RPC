package com.qileyuan.tatala.example.server;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.proxy.ExampleDefaultProxy;
import com.qileyuan.tatala.proxy.DefaultProxy;
import com.qileyuan.tatala.socket.server.AioSocketServer;

public class ExampleServer {
	static Logger log = Logger.getLogger(ExampleServer.class);
	
	public static void initialize(){
		log.info("Example Tatala Server initialize...");
	}
	
	public static void startup(int listenPort, int poolSize){
		log.info("Example Tatala Server starting...");
		
		AioSocketServer server = new AioSocketServer(listenPort, poolSize);
		
		try {
			//set default proxy or callee class here
			DefaultProxy defaultProxy = new ExampleDefaultProxy();
			server.registerProxy(defaultProxy);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		log.info("*** Example Tatala Server ***");
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
