package com.qileyuan.tatala.executor;

import java.lang.reflect.Method;

import com.qileyuan.tatala.socket.to.TransferObject;

/**
 * This class is local target, simply provider local-method-call.
 * 
 * @author JimT
 *
 */
public class LocalTarget implements ServerTarget{
	public Object execute(TransferObject to) {
		
		String calleeClass = to.getCalleeClass();
		String calleeMethod = to.getCalleeMethod();
		
		Object retobj = null;
		try {
			Class<?> cls = Class.forName(calleeClass);
			Object instance = cls.newInstance();
			Method meth = cls.getMethod(calleeMethod, TransferObject.class);
			retobj = meth.invoke(instance, to);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retobj;
	}
}
