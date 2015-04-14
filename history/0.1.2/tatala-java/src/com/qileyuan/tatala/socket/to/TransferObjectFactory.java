package com.qileyuan.tatala.socket.to;

import com.qileyuan.tatala.proxy.DefaultProxy;
import com.qileyuan.tatala.socket.client.SocketController;

/**
 * This class is transfer object factory class, which generates 
 * transfer object sent to different socket connection.
 * 
 * @author JimT
 *
 */
public class TransferObjectFactory {

	private String connectionName;
	private boolean longConnection;
	private DefaultProxy serverCallProxy;
	
	private String calleeClass;
	private String implClass;
	private boolean compress;
	
	static{
		SocketController.initialize();
	}
	
	public TransferObjectFactory(String connectionName) {
		this.connectionName = connectionName;
	}
	
	public TransferObjectFactory(String connectionName, boolean longConnection) {
		this.connectionName = connectionName;
		this.longConnection = longConnection;
	}
	
	public void registerServerCallProxy(DefaultProxy serverCallProxy){
		this.serverCallProxy = serverCallProxy;
	}
	
	public void setCalleeClass(String calleeClass){
		this.calleeClass = calleeClass;
	}
	
	public String getImplClass() {
		return implClass;
	}

	public void setImplClass(String implClass) {
		this.implClass = implClass;
	}

	public boolean isCompress() {
		return compress;
	}

	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	public StandardTransferObject createTransferObject(){
		StandardTransferObject to = new StandardTransferObject();
		to.setConnectionName(connectionName);
		to.setLongConnection(longConnection);
		to.setServerCallProxy(serverCallProxy);
		if(calleeClass != null){
			to.setCalleeClass(calleeClass);
		}
		to.setCompress(compress);
		return to;
	}
	
	public NewTransferObject createNewTransferObject(){
		NewTransferObject to = new NewTransferObject();
		to.setConnectionName(connectionName);
		to.setLongConnection(longConnection);
		to.setServerCallProxy(serverCallProxy);
		if(calleeClass != null){
			to.setCalleeClass(calleeClass);
		}
		to.setCompress(compress);
		to.setNewVersion(true);
		return to;
	}
}
