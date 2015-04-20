package com.qileyuan.tatala.example.proxy;

import com.qileyuan.tatala.example.service.ChatRoomServerLogic;
import com.qileyuan.tatala.socket.server.ServerSession;
import com.qileyuan.tatala.socket.to.MappedTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;

public class ChatRoomServerProxy {
	
	private ChatRoomServerLogic serverLogic = ChatRoomServerLogic.getInstance();
	
	public void login(TransferObject baseto){
		MappedTransferObject to = (MappedTransferObject)baseto;
		String username = to.getString("username");
		serverLogic.login(to.getClientId(), username);
	}
	
	public void receiveMessage(TransferObject baseto){
		MappedTransferObject to = (MappedTransferObject)baseto;
		String message = to.getString("message");
		serverLogic.broadcast(to.getClientId(), message);
	}
	
	public static void sendMessage(ServerSession session, String sendMessage){
		MappedTransferObject to = new MappedTransferObject();
		to.setServerCall(true);
		//comment out for call client default proxy
		//to.setCalleeClass("com.qileyuan.tatala.example.proxy.ChatRoomClientProxy");
		to.setDefaultCallee(true);
		to.setCalleeMethod("receiveMessage");
		to.putString("message", sendMessage);
		session.executeServerCall(to);
	}
}
