package com.qileyuan.tatala.example.proxy;

import com.qileyuan.tatala.example.service.ChatRoomServerLogic;
import com.qileyuan.tatala.socket.server.ServerSession;
import com.qileyuan.tatala.socket.to.OrderedTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;

public class ChatRoomServerProxy {
	
	private ChatRoomServerLogic serverLogic = ChatRoomServerLogic.getInstance();
	
	public void login(TransferObject to){
		String username = to.getString();
		serverLogic.login(to.getClientId(), username);
	}
	
	public void receiveMessage(TransferObject to){
		String message = to.getString();
		serverLogic.broadcast(to.getClientId(), message);
	}
	
	public static void sendMessage(ServerSession session, String sendMessage){
		TransferObject to = new OrderedTransferObject();
		to.setServerCall(true);
		//comment out for call client default proxy
		//to.setCalleeClass("com.qileyuan.tatala.example.proxy.ChatRoomClientProxy");
		to.setDefaultCallee(true);
		to.setCalleeMethod("receiveMessage");
		to.putString(sendMessage);
		session.executeServerCall(to);
	}
}
