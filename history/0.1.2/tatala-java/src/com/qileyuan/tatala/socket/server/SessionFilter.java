package com.qileyuan.tatala.socket.server;

public interface SessionFilter {
	public void onClose(ServerSession session);
	//if return true, ignore execute code
	public boolean onReceive(ServerSession session, byte[] receiveData);
}
