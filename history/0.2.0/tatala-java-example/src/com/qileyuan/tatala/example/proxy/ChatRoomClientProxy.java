package com.qileyuan.tatala.example.proxy;

import com.qileyuan.tatala.executor.ServerExecutor;
import com.qileyuan.tatala.proxy.DefaultProxy;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.to.TransferObjectFactory;

public class ChatRoomClientProxy {
	private String IP = "127.0.0.1";
	private int PORT = 10002;
	private int TIMEOUT = 5000;
	
	private TransferObjectFactory transferObjectFactory = new TransferObjectFactory(IP, PORT, TIMEOUT);
	
	public ChatRoomClientProxy(){
		DefaultProxy clientDefaultProxy = new ChatRoomClientDefaultProxy();
		transferObjectFactory.registerServerCallProxy(clientDefaultProxy);
	}
	
	public void login(String username){
		TransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass("com.qileyuan.tatala.example.proxy.ChatRoomServerProxy");
		to.setCalleeMethod("login");
		to.putString(username);
		to.registerReturnType(TransferObject.DATATYPE_VOID);
		ServerExecutor.execute(to);
	}
	
	public void sendMessage(String message){
		TransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass("com.qileyuan.tatala.example.proxy.ChatRoomServerProxy");
		to.setCalleeMethod("receiveMessage");
		to.putString(message);
		to.registerReturnType(TransferObject.DATATYPE_VOID);
		ServerExecutor.execute(to);
	}
}
