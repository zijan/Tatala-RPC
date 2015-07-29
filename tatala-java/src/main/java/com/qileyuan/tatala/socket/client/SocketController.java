package com.qileyuan.tatala.socket.client;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.socket.to.TransferObject;

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
	
	private static ExecutorService executorService = Executors.newCachedThreadPool(new DaemonThreadFactory());
	
	private static class DaemonThreadFactory implements ThreadFactory {
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setDaemon(true);
			return t;
		}
	}

	/**
	 * Dispatch request to a appointed socket connection.
	 * @param to TransferObject
	 * @return Object
	 */
	public static Object execute(TransferObject to) {
		SocketConnection connection = to.getConnection();
		if (to.isAsynchronous()) {
			Worker worker = new Worker(connection, to);
			return executorService.submit(worker);
		} else {
			return connection.execute(to);
		}
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
