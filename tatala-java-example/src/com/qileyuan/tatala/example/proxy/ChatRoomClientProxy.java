package com.qileyuan.tatala.example.proxy;

import com.qileyuan.tatala.executor.ServerExecutor;
import com.qileyuan.tatala.proxy.DefaultProxy;
import com.qileyuan.tatala.socket.to.StandardTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.to.TransferObjectFactory;

public class ChatRoomClientProxy {
	private TransferObjectFactory transferObjectFactory = new TransferObjectFactory("test1", true);
	
	public ChatRoomClientProxy(){
		DefaultProxy clientDefaultProxy = new ChatRoomClientDefaultProxy();
		transferObjectFactory.registerServerCallProxy(clientDefaultProxy);
	}
	
	public void login(String username){
		StandardTransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass("com.qileyuan.tatala.example.proxy.ChatRoomServerProxy");
		to.setCalleeMethod("login");
		to.putString("username", username);
		to.registerReturnType(TransferObject.DATATYPE_VOID);
		ServerExecutor.execute(to);
	}
	
	public void sendMessage(String message){
		StandardTransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass("com.qileyuan.tatala.example.proxy.ChatRoomServerProxy");
		to.setCalleeMethod("receiveMessage");
		to.putString("message", message);
		to.registerReturnType(TransferObject.DATATYPE_VOID);
		ServerExecutor.execute(to);
	}
}
