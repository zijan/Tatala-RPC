package com.qileyuan.tatala.socket.client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.qileyuan.tatala.socket.exception.SocketExecuteException;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.zookeeper.ServiceDiscovery;

/**
 * This class is the socket connection class, which is what makes the actual
 * socket connection to a particular server. It stores the IP and port of the
 * server it is connected to, timeout of socket object when create socket object.
 * 
 * @author JimT
 * 
 */

public class SocketConnection {

	private String ip;
	private int port;
	private int timeout;
	
	private String zkRegistryAddress;

	private LongClientSession longClientSession;
	private Map<String, LongClientSession> sessionMap;
	private final ReentrantLock lock = new ReentrantLock();
	
	public SocketConnection(String ip, int port, int timeout){
		this.ip = ip;
		this.port = port;
		this.timeout = timeout;
	}
	
	public SocketConnection(String zkRegistryAddress, int timeout){
		this.zkRegistryAddress = zkRegistryAddress;
		this.timeout = timeout;
		sessionMap = new HashMap<String, LongClientSession>();
		ServiceDiscovery.init(zkRegistryAddress);
	}
	
	/**
	 * This method handles all outgoing and incoming data.
	 * 
	 * @param to TransferObject
	 * @return Object
	 * @throws SocketExecuteException 
	 */
	public Object execute(TransferObject to) throws SocketExecuteException {
		lock.lock();
		try {
			if(zkRegistryAddress != null){
				return findLongClientSession().start(to);
			}else{
				if(longClientSession == null){
					longClientSession = new LongClientSession(ip, port, timeout);
				}
				return longClientSession.start(to);
			}
		} finally {
			lock.unlock();
		}
	}

	private LongClientSession findLongClientSession() throws SocketExecuteException{
		String serverAddress = ServiceDiscovery.discover();
		
		if(serverAddress == null || serverAddress.isEmpty()){
			throw new SocketExecuteException("Don't have available server.");
		}
		
		if(sessionMap.containsKey(serverAddress)){
			return sessionMap.get(serverAddress);
		}else{
			String ip = serverAddress.split(":")[0];
			int port = Integer.parseInt(serverAddress.split(":")[1]);
			LongClientSession LongClientSession = new LongClientSession(ip, port, timeout);
			sessionMap.put(serverAddress, LongClientSession);
			return LongClientSession;
		}
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getZkRegistryAddress() {
		return zkRegistryAddress;
	}

	public void setZkRegistryAddress(String zkRegistryAddress) {
		this.zkRegistryAddress = zkRegistryAddress;
	}

}
