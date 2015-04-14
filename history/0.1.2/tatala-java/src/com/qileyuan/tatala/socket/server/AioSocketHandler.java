package com.qileyuan.tatala.socket.server;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.apache.log4j.Logger;

public class AioSocketHandler implements CompletionHandler<AsynchronousSocketChannel, AioSocketServer> {
	Logger log = Logger.getLogger(AioSocketHandler.class);

	private ServerSession session;
	
	public AioSocketHandler(ServerSession session){
		this.session = session;
	}
	
	@Override
	public void completed(AsynchronousSocketChannel socketChannel, AioSocketServer aioSocketServer) {
		try {
			session.setSocketChannel(socketChannel);
			session.start();
		} finally{
			aioSocketServer.acceptConnections();
		}
	}

	@Override
	public void failed(Throwable exc, AioSocketServer aioSocketServer) {
		log.error("Accept error", exc);
		aioSocketServer.acceptConnections();
	}
	
}
