package com.qileyuan.tatala.socket.client;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.socket.to.TransferObject;

/**
 * This class is the socket connection class, which is what makes the actual
 * socket connection to a particular server. It stores the IP and port of the
 * server it is connected to, timeout of socket object, number of retry times
 * when create socket object, and the name of one connection.
 * 
 * @author JimT
 * 
 */

public class SocketConnection {

	static Logger log = Logger.getLogger(SocketConnection.class);

	private String hostIp;
	private int hostPort;
	private int timeout;
	private int retryTime;
	private String name;

	private ShortClientSession shortClientSession;
	private LongClientSession longClientSession;
	private final ReentrantLock lock = new ReentrantLock();
	
	/**
	 * This method handles all outgoing and incoming data.
	 * 
	 * @param to TransferObject
	 * @return Object
	 */
	public Object execute(TransferObject to) {
		if(to.isLongConnection()){
			lock.lock();
			try {
				if(longClientSession == null){
					longClientSession = new LongClientSession(hostIp, hostPort, timeout, retryTime);
				}
				return longClientSession.start(to);
			} finally {
				lock.unlock();
			}
		}else{
			if(shortClientSession == null){
				shortClientSession = new ShortClientSession(hostIp, hostPort, timeout, retryTime);
			}
			return shortClientSession.start(to);
		}
	}
	
	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public int getHostPort() {
		return hostPort;
	}

	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetryTime() {
		return retryTime;
	}

	public void setRetryTime(int retryTime) {
		this.retryTime = retryTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		final String TAB = "    ";

		StringBuilder retValue = new StringBuilder();

		retValue.append("SocketConnection ( ").append(super.toString())
				.append(TAB).append("hostIp = ").append(this.hostIp)
				.append(TAB).append("hostPort = ").append(this.hostPort)
				.append(TAB).append("timeout = ").append(this.timeout)
				.append(TAB).append("retryTime = ").append(this.retryTime)
				.append(TAB).append("name = ").append(this.name).append(TAB)
				.append(" )");

		return retValue.toString();
	}

}
