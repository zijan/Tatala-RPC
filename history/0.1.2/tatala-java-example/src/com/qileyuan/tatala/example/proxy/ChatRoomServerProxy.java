package com.qileyuan.tatala.example.proxy;

import com.qileyuan.tatala.example.service.ChatRoomServerLogic;
import com.qileyuan.tatala.socket.server.ServerSession;
import com.qileyuan.tatala.socket.to.StandardTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;

public class ChatRoomServerProxy {
	
	private ChatRoomServerLogic serverLogic = ChatRoomServerLogic.getInstance();
	
	public void login(TransferObject baseto){
		StandardTransferObject to = (StandardTransferObject)baseto;
		String username = to.getString("username");
		serverLogic.login(to.getClientId(), username);
	}
	
	public void receiveMessage(TransferObject baseto){
		StandardTransferObject to = (StandardTransferObject)baseto;
		String message = to.getString("message");
		serverLogic.broadcast(to.getClientId(), message);
	}
	
	public static void sendMessage(ServerSession session, String sendMessage){
		StandardTransferObject to = new StandardTransferObject();
		to.setServerCall(true);
		//comment out for call client default proxy
		//to.setCalleeClass("com.qileyuan.tatala.example.proxy.ChatRoomClientProxy");
		to.setCalleeMethod("receiveMessage");
		to.putString("message", sendMessage);
		session.executeServerCall(to);
	}
}
