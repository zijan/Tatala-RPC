package com.qileyuan.tatala.executor;

import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.util.Configuration;

/**
 * This class is the distribution-method-call executor, which dispatches request to different target.
 * For now, it only supports local and socket distribution.
 * 
 * @author JimT
 *
 */
public class ServerExecutor{
	private static ServerTarget target;
	public static Object execute(TransferObject to){
		Object retObj = null;
		String serverTarget = Configuration.getProperty("Client.ServerTarget");
		if(serverTarget==null || serverTarget.equals("Socket")){
			target = new SocketServerTarget();
		}else if(serverTarget.equals("Local")){
			target = new LocalTarget();
		}
		retObj = target.execute(to);
		return retObj;
	}
}
