package com.qileyuan.tatala.proxy;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.socket.to.TransferObject;

public class DefaultProxy {
	Logger log = Logger.getLogger(DefaultProxy.class);
	public Object execute(TransferObject to){
		log.error("This is DefaultProxy. You need extend it by specific proxy.");
		return null;
	}
}
