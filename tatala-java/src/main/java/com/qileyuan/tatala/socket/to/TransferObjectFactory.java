package com.qileyuan.tatala.socket.to;

import com.qileyuan.tatala.proxy.DefaultProxy;
import com.qileyuan.tatala.socket.client.SocketConnection;

/**
 * This class is transfer object factory class, which generates 
 * transfer object sent to different socket connection.
 * 
 * @author JimT
 *
 */
public class TransferObjectFactory {

	private DefaultProxy serverCallProxy;
	
	private String calleeClass;
	private String implClass;
	private boolean compress;
	
	private SocketConnection connection;

	public TransferObjectFactory(String ip, int port, int timeout) {
		this.connection = new SocketConnection(ip, port, timeout);
	}
	
	public TransferObjectFactory(String zkRegistryAddress, int timeout) {
		this.connection = new SocketConnection(zkRegistryAddress, timeout);
	}
	
	public void registerServerCallProxy(DefaultProxy serverCallProxy){
		this.serverCallProxy = serverCallProxy;
	}
	
	public void setCalleeClass(String calleeClass){
		this.calleeClass = calleeClass;
	}
	
	public void setCalleeClass(Class<?> calleeClass){
		this.calleeClass = calleeClass.getName();
	}
	
	public String getImplClass() {
		return implClass;
	}

	public void setImplClass(String implClass) {
		this.implClass = implClass;
	}
	
	public void setImplClass(Class<?> implClass) {
		this.implClass = implClass.getName();
	}

	public boolean isCompress() {
		return compress;
	}

	public void setCompress(boolean compress) {
		this.compress = compress;
	}
	
	public TransferObject createTransferObject(){
		TransferObject to = new OrderedTransferObject();
		to.setConnection(connection);
		to.setServerCallProxy(serverCallProxy);
		if(calleeClass != null){
			to.setCalleeClass(calleeClass);
		}
		to.setCompress(compress);
		return to;
	}
}
