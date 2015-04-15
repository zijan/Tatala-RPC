package com.qileyuan.tatala.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.socket.exception.TatalaRollbackException;
import com.qileyuan.tatala.socket.to.NewTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;

/**
 * This class is the common server proxy, without create own proxy.
 * @author JimT
 *
 */
public class ServerProxy extends DefaultProxy{
	
	static Logger log = Logger.getLogger(ServerProxy.class);
	
	public Object execute(TransferObject abstractTo){
		NewTransferObject to = (NewTransferObject)abstractTo;
		
		String implClass = to.getString();
		String implMethod = to.getString();
		Object retobj = null;
		
		try {
			Class<?> clazz = Class.forName(implClass);
			Object instance = clazz.newInstance();

			int paramSize = to.paramSize();
			Class<?>[] clazzs = new Class<?>[paramSize];
			Object[] objects = new Object[paramSize];
			
			for (int i = 0; i < paramSize; i++){
				Object paramObject = to.peek(0);
				Class<?> paramType = paramObject.getClass();

				if(paramType.equals(Boolean.class)){
					clazzs[i] = Boolean.TYPE;
					objects[i] = to.getBoolean();
				} else if (paramType.equals(Byte.class)) {
					clazzs[i] = Byte.TYPE;
					objects[i] = to.getByte();
				} else if (paramType.equals(Short.class)) {
					clazzs[i] = Short.TYPE;
					objects[i] = to.getShort();
				} else if (paramType.equals(Character.class)) {
					clazzs[i] = Character.TYPE;
					objects[i] = to.getChar();
				} else if (paramType.equals(Integer.class)) {
					clazzs[i] = Integer.TYPE;
					objects[i] = to.getInt();
				} else if (paramType.equals(Long.class)) {
					clazzs[i] = Long.TYPE;
					objects[i] = to.getLong();
				} else if (paramType.equals(Float.class)) {
					clazzs[i] = Float.TYPE;
					objects[i] = to.getFloat();
				} else if (paramType.equals(Double.class)) {
					clazzs[i] = Double.TYPE;
					objects[i] = to.getDouble();
				} else if (paramType.equals(Date.class)) {
					clazzs[i] = Date.class;
					objects[i] = to.getDate();
				} else if (paramType.equals(String.class)) {
					clazzs[i] = String.class;
					objects[i] = to.getString();
					
				} else if (paramType.equals(byte[].class)) {
					clazzs[i] = byte[].class;
					objects[i] = to.getByteArray();
				} else if (paramType.equals(int[].class)) {
					clazzs[i] = int[].class;
					objects[i] = to.getIntArray();
				} else if (paramType.equals(long[].class)) {
					clazzs[i] = long[].class;
					objects[i] = to.getLongArray();
				} else if (paramType.equals(float[].class)) {
					clazzs[i] = float[].class;
					objects[i] = to.getFloatArray();
				} else if (paramType.equals(double[].class)) {
					clazzs[i] = double[].class;
					objects[i] = to.getDoubleArray();
				} else if (paramType.equals(String[].class)) {
					clazzs[i] = String[].class;
					objects[i] = to.getStringArray();
					
				} else if (List.class.isAssignableFrom(paramType)) {
					clazzs[i] = List.class;
					objects[i] = to.getSerializable();
				} else if (Map.class.isAssignableFrom(paramType)) {
					clazzs[i] = Map.class;
					objects[i] = to.getSerializable();
				} else if (Set.class.isAssignableFrom(paramType)) {
					clazzs[i] = Set.class;
					objects[i] = to.getSerializable();
					
				} else if (Serializable.class.isAssignableFrom(paramType)) {
					clazzs[i] = Class.forName(paramType.getName());
					objects[i] = to.getSerializable();
				}
			}
			
			Method meth = clazz.getMethod(implMethod, clazzs);
			retobj = meth.invoke(instance, objects);
			
		} catch (InvocationTargetException ite) {
			if(ite.getCause() instanceof TatalaRollbackException){
				TatalaRollbackException tre = (TatalaRollbackException)ite.getCause();
				throw tre;
			} else {
				log.error("ServerProxy.execute e: " + ite);
				ite.printStackTrace();
			}
			
		} catch (Exception e) {
			log.error("ServerProxy.execute e: " + e);
			e.printStackTrace();
		}
		
		return retobj;
	}
}
