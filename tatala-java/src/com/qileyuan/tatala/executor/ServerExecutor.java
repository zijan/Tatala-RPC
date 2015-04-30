package com.qileyuan.tatala.executor;

import com.qileyuan.tatala.socket.client.SocketController;
import com.qileyuan.tatala.socket.to.TransferObject;

/**
 * This class is the distribution-method-call executor, which dispatches request to socket controller.
 * 
 * @author JimT
 *
 */
public class ServerExecutor{
	public static Object execute(TransferObject to){
		return SocketController.execute(to);
	}
}
