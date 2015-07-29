package com.qileyuan.tatala.proxy;

import java.lang.reflect.Proxy;

import com.qileyuan.tatala.socket.to.TransferObjectFactory;

public class ClientProxyFactory {
	public static Object create(Class<?> clazz,	TransferObjectFactory transferObjectFactory) {
		return Proxy.newProxyInstance(
				ClientProxyFactory.class.getClassLoader(),
				new Class[] { clazz }, 
				new ClientProxy(transferObjectFactory));
	}
}
