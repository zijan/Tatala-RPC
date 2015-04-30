package com.qileyuan.tatala.socket.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

import org.apache.log4j.Logger;

public class AioReceiveHandler implements CompletionHandler<Integer, ServerSession>{
	Logger log = Logger.getLogger(AioReceiveHandler.class);
	
	@Override
	public void completed(Integer receivedCount, ServerSession session) {
		if(receivedCount < 0){
			session.close();
			return;
		}
		try {
			ByteBuffer byteBuffer = session.getByteBuffer();

			if (receivedCount > 0) {
				byteBuffer.flip();
				byte[] receiveData = new byte[byteBuffer.limit()];
				byteBuffer.get(receiveData);
				session.write(receiveData);
				byteBuffer.compact();
				
				//chech whether one client request is done
				session.checkOneReceiveDone(receivedCount, receiveData);
			}
		} finally {
			session.receive();
		}
	}

	@Override
	public void failed(Throwable exc, ServerSession session) {
		//if client close connection, don't log error
		if(!IOException.class.isAssignableFrom(exc.getClass())){
			log.error("Receive error: " + exc.getMessage());
		}
		session.close();
	}
}
