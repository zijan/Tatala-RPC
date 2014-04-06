package com.qileyuan.tatala.executor;

import com.qileyuan.tatala.socket.to.TransferObject;

public interface ServerTarget {
	public Object execute(TransferObject to);
}
