package com.qileyuan.tatala.executor;

import com.qileyuan.tatala.socket.client.SocketController;
import com.qileyuan.tatala.socket.to.TransferObject;

/**
 * This class is socket server target, provider socket distribution-method-call.
 * @author JimT
 *
 */
public class SocketServerTarget implements ServerTarget{
	public Object execute(TransferObject to) {
		return SocketController.execute(to);
	}
}
