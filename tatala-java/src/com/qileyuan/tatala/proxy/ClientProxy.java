package com.qileyuan.tatala.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.executor.ServerExecutor;
import com.qileyuan.tatala.socket.to.OrderedTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.to.TransferObjectFactory;

/**
 * This class is the common client proxy, without create own proxy.
 * @author JimT
 *
 */
public class ClientProxy implements InvocationHandler {

	static Logger log = Logger.getLogger(ClientProxy.class);
	
	public static final String SERVER_PROXY = "com.qileyuan.tatala.proxy.ServerProxy";
	
	TransferObjectFactory transferObjectFactory;
	
	public ClientProxy(TransferObjectFactory transferObjectFactory){
		this.transferObjectFactory = transferObjectFactory;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		OrderedTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeClass(SERVER_PROXY);
		
		//return type
		Class<?> returnType = method.getReturnType();
		if (returnType.equals(Boolean.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_BOOLEAN);
		} else if (returnType.equals(Byte.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_BYTE);
		} else if (returnType.equals(Short.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_SHORT);
		} else if (returnType.equals(Character.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_CHAR);
		} else if (returnType.equals(Integer.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_INT);
		} else if (returnType.equals(Long.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_LONG);
		} else if (returnType.equals(Float.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_FLOAT);
		} else if (returnType.equals(Double.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_DOUBLE);
		} else if (returnType.equals(Date.class)) {
			to.registerReturnType(TransferObject.DATATYPE_DATE);
		} else if (returnType.equals(String.class)) {
			to.registerReturnType(TransferObject.DATATYPE_STRING);
			
		} else if (returnType.isArray() && getArrayDimension(returnType) == 1
				&& returnType.getComponentType().equals(Byte.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_BYTEARRAY);
		} else if (returnType.isArray() && getArrayDimension(returnType) == 1
				&& returnType.getComponentType().equals(Integer.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_INTARRAY);
		} else if (returnType.isArray() && getArrayDimension(returnType) == 1
				&& returnType.getComponentType().equals(Long.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_LONGARRAY);
		} else if (returnType.isArray() && getArrayDimension(returnType) == 1
				&& returnType.getComponentType().equals(Float.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_FLOATARRAY);
		} else if (returnType.isArray() && getArrayDimension(returnType) == 1
				&& returnType.getComponentType().equals(Double.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_DOUBLEARRAY);
		} else if (returnType.isArray() && getArrayDimension(returnType) == 1
				&& returnType.getComponentType().equals(String.class)) {
			to.registerReturnType(TransferObject.DATATYPE_STRINGARRAY);
		} else if (Serializable.class.isAssignableFrom(returnType)
				|| Collection.class.isAssignableFrom(returnType)
				|| Map.class.isAssignableFrom(returnType)) {
			to.registerReturnType(TransferObject.DATATYPE_SERIALIZABLE);
		} else if (returnType.equals(Void.TYPE)) {
			to.registerReturnType(TransferObject.DATATYPE_VOID);
		}
		
		//implement class and method
		to.putString(transferObjectFactory.getImplClass());
		to.putString(method.getName());
		
		// parameter
		Class<?>[] params = method.getParameterTypes();
		for (int i = 0; i < params.length; i++) {
			Class<?> paramClass = params[i];

			if (paramClass.equals(Boolean.TYPE)) {
				to.putBoolean((Boolean) args[i]);
			} else if (paramClass.equals(Byte.TYPE)) {
				to.putByte((Byte) args[i]);
			} else if (paramClass.equals(Short.TYPE)) {
				to.putShort((Short) args[i]);
			} else if (paramClass.equals(Character.TYPE)) {
				to.putChar((Character) args[i]);
			} else if (paramClass.equals(Integer.TYPE)) {
				to.putInt((Integer) args[i]);
			} else if (paramClass.equals(Long.TYPE)) {
				to.putLong((Long) args[i]);
			} else if (paramClass.equals(Float.TYPE)) {
				to.putFloat((Float) args[i]);
			} else if (paramClass.equals(Double.TYPE)) {
				to.putDouble((Double) args[i]);
			} else if (paramClass.equals(Date.class)) {
				to.putDate((Date) args[i]);
			} else if (paramClass.equals(String.class)) {
				to.putString((String) args[i]);
				
			} else if (paramClass.isArray()
					&& getArrayDimension(paramClass) == 1
					&& paramClass.getComponentType().equals(Byte.TYPE)) {
				to.putByteArray((byte[]) args[i]);
			} else if (paramClass.isArray()
					&& getArrayDimension(paramClass) == 1
					&& paramClass.getComponentType().equals(Integer.TYPE)) {
				to.putIntArray((int[]) args[i]);
			} else if (paramClass.isArray()
					&& getArrayDimension(paramClass) == 1
					&& paramClass.getComponentType().equals(Long.TYPE)) {
				to.putLongArray((long[]) args[i]);
			} else if (paramClass.isArray()
					&& getArrayDimension(paramClass) == 1
					&& paramClass.getComponentType().equals(Float.TYPE)) {
				to.putFloatArray((float[]) args[i]);
			} else if (paramClass.isArray()
					&& getArrayDimension(paramClass) == 1
					&& paramClass.getComponentType().equals(Double.TYPE)) {
				to.putDoubleArray((double[]) args[i]);
			} else if (paramClass.isArray()
					&& getArrayDimension(paramClass) == 1
					&& paramClass.getComponentType().equals(String.class)) {
				to.putStringArray((String[]) args[i]);
				
			} else if (Serializable.class.isAssignableFrom(paramClass)
					|| Collection.class.isAssignableFrom(paramClass)
					|| Map.class.isAssignableFrom(paramClass)) {
				to.putSerializable((Serializable) args[i]);
			}
		}

		return ServerExecutor.execute(to);
	}

	private int getArrayDimension(Class<?> clazz){
		return clazz.getName().split("\\[").length - 1;
	}
}
