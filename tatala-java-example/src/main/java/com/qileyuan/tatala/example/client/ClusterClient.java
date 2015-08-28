package com.qileyuan.tatala.example.client;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.service.ExampleManager;
import com.qileyuan.tatala.proxy.ClientProxyFactory;
import com.qileyuan.tatala.socket.to.TransferObjectFactory;
import com.qileyuan.tatala.zookeeper.ServiceDiscovery;

public class ClusterClient {

	static Logger log = Logger.getLogger(ClusterClient.class);

	public static void main(String[] args) {
		singleServerCall(args);
		multipleServerCall(args);
	}
	
	private static void singleServerCall(String[] args){
		log.debug("singleServerCall------------");
		
		String zkRegistryAddress = "127.0.0.1:2181";
		if(args != null && args.length > 0){
			zkRegistryAddress = args[0];
		}
		ServiceDiscovery.init(zkRegistryAddress);
		String serverAddress = ServiceDiscovery.discover();
		if(serverAddress == null || serverAddress.isEmpty()){
			log.error("Don't have available server.");
			return;
		}
		String host = serverAddress.split(":")[0];
        int port = Integer.parseInt(serverAddress.split(":")[1]);
         
		TransferObjectFactory transferObjectFactory = new TransferObjectFactory(host, port, 5000);
		transferObjectFactory.setImplClass("com.qileyuan.tatala.example.service.ExampleManagerImpl");
		transferObjectFactory.setCompress(true);
		ExampleManager manager = (ExampleManager)ClientProxyFactory.create(ExampleManager.class, transferObjectFactory);
		
		String result = manager.sayHello(1, "Jim");
		log.debug("result: "+result);
		
		result = manager.sayHello(2, "Cathy");
		log.debug("result: "+result);
		
		result = manager.sayHello(3, "Carlo");
		log.debug("result: "+result);
		
		result = manager.sayHello(4, "Joyce");
		log.debug("result: "+result);
	}
	
	private static void multipleServerCall(String[] args){
		log.debug("multipleServerCall------------");
		
		String zkRegistryAddress = "127.0.0.1:2181";
		if(args != null && args.length > 0){
			zkRegistryAddress = args[0];
		}
		
		TransferObjectFactory transferObjectFactory = new TransferObjectFactory(zkRegistryAddress, 5000);
		transferObjectFactory.setImplClass("com.qileyuan.tatala.example.service.ExampleManagerImpl");
		transferObjectFactory.setCompress(true);
		ExampleManager manager = (ExampleManager)ClientProxyFactory.create(ExampleManager.class, transferObjectFactory);
		
		String result = manager.sayHello(1, "Jim");
		log.debug("result: "+result);
		
		result = manager.sayHello(2, "Cathy");
		log.debug("result: "+result);
		
		result = manager.sayHello(3, "Carlo");
		log.debug("result: "+result);
		
		result = manager.sayHello(4, "Joyce");
		log.debug("result: "+result);
	}
}
