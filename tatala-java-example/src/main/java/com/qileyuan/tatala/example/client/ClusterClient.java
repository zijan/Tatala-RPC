package com.qileyuan.tatala.example.client;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.socket.to.TransferObjectFactory;
import com.qileyuan.tatala.zookeeper.ServiceDiscovery;

public class ClusterClient {

	static Logger log = Logger.getLogger(ClusterClient.class);

	public static void main(String[] args) {
		String zkRegistryAddress = "1127.0.0.1:2181";
		if(args != null && args.length > 0){
			zkRegistryAddress = args[0];
		}
		ServiceDiscovery serviceDiscovery = new ServiceDiscovery(zkRegistryAddress);
		String serverAddress = serviceDiscovery.discover();
		String host = serverAddress.split(":")[0];
        int port = Integer.parseInt(serverAddress.split(":")[1]);
         
		TransferObjectFactory transferObjectFactory = new TransferObjectFactory(host, port, 5000);
		transferObjectFactory.setImplClass("com.qileyuan.tatala.example.service.ExampleManagerImpl");
		transferObjectFactory.setCompress(true);
		
		EasyClient easyClient = new EasyClient(transferObjectFactory);
		
		long l = System.currentTimeMillis();
		log.info("connect time: " + (System.currentTimeMillis() - l) + "(ms)");
		
		l = System.currentTimeMillis();
		
		easyClient.remoteTest();
		
		log.info("time: " + (System.currentTimeMillis() - l) + "(ms)");
	}
}
