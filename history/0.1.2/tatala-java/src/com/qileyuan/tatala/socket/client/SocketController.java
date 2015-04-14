package com.qileyuan.tatala.socket.client;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.util.Configuration;
import com.thoughtworks.xstream.XStream;

/**
 * This class is the socket connection controller class, which manage the instances of 
 * socket server connections. A socket server controller class will be instantiated during 
 * site initialization. 
 * 
 * The Socket Connection Controller is responsible for accepting requests for initiation of 
 * connections to socket servers from the client components.
 *  
 * @author JimT
 *
 */
public class SocketController {
	static Logger log = Logger.getLogger(SocketController.class);
	static final int DEFAULT_CLIENT_POOL_SIZE = 10;
	
	private static List<SocketConnection> connectionList;
	
	private static ExecutorService executorService;
	
	private static class DaemonThreadFactory implements ThreadFactory {
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setDaemon(true);
			return t;
		}
	}
	
	public static List<SocketConnection> getConnectionList(){
		if(connectionList == null){
			initialize();
		}
		return connectionList;
	}
	
	/**
	 * Initialize socket server connections through xml configuration file.
	 */
	@SuppressWarnings("unchecked")
	public static void initialize(){
		
		XStream xstream = new XStream();
		xstream.alias("connections", List.class);
		xstream.alias("connection", SocketConnection.class);
		
		InputStream is = SocketController.class.getClassLoader().getResourceAsStream("controller.xml");
		if(is == null){
			throw new RuntimeException("Can't find controller.xml");
		}
		connectionList = (List<SocketConnection>)xstream.fromXML(is);

		int poolSize = Configuration.getIntProperty("Client.Socket.poolSize", DEFAULT_CLIENT_POOL_SIZE);
		executorService = Executors.newFixedThreadPool(poolSize, new DaemonThreadFactory());
	}
	
	/**
	 * Dispatch request to a appointed socket connection.
	 * @param to TransferObject
	 * @return Object
	 */
	public static Object execute(TransferObject to) {
		Object retObject = null;
		
		if (connectionList == null) {
			initialize();
		}

		String connectionName = to.getConnectionName();
		for (SocketConnection connection : connectionList) {
			if (connection.getName().equals(connectionName)) {
				if (to.isAsynchronous()) {
					Worker worker = new Worker(connection, to);
					return executorService.submit(worker);
				} else {
					return connection.execute(to);
				}
			}
		}
		
		return retObject;
	}
	
	static class Worker implements Callable<Object>{

		private TransferObject to;
		private SocketConnection connection;
		public Worker(SocketConnection connection, TransferObject to){
			this.to = to;
			this.connection = connection;
		}
		
		@Override
		public Object call() throws Exception {
			return execute();
		}

		private Object execute() {
			return connection.execute(to);
		}
	}

	public static ExecutorService getExecutorService() {
		return executorService;
	}
}
