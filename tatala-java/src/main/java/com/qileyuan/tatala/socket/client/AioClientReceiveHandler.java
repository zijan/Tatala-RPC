package com.qileyuan.tatala.socket.client;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

import org.apache.log4j.Logger;

public class AioClientReceiveHandler implements CompletionHandler<Integer, LongClientSession> {
	Logger log = Logger.getLogger(AioClientReceiveHandler.class);
	
	@Override
	public void completed(Integer receivedCount, LongClientSession session) {
		
		//server close socket channel or network issue
		if(receivedCount < 0){
			session.close();
			return;
		}
		try {
			ByteBuffer byteBuffer = session.getByteBuffer();
			if(receivedCount > 0){
				byteBuffer.flip();
				byte[] receiveData = new byte[byteBuffer.limit()];
				byteBuffer.get(receiveData);
				session.write(receiveData);
				byteBuffer.compact();
				
				//chech whether one receive from server is done
				session.checkOneReceiveDone(receivedCount, receiveData);
			}
		} finally {
			session.receive();
		}
	}

	@Override
	public void failed(Throwable exc, LongClientSession session) {
		log.error("Receive error: " + exc.getMessage(), exc);
		session.close();
	}

}
