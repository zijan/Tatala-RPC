package com.qileyuan.tatala.socket.to;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import com.qileyuan.tatala.proxy.DefaultProxy;
import com.qileyuan.tatala.socket.client.SocketConnection;
import com.qileyuan.tatala.socket.exception.SocketExecuteException;
import com.qileyuan.tatala.socket.io.TransferInputStream;
import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.util.TransferUtil;

/**
 * This class is transfer object class.
 * It converts java primitive type data and user-defined object into byte array and vice versa. 
 * 
 * @author Jim Tang
 *
 */
public abstract class TransferObject {
	
	public static final byte NORMAL_FLAG = 0;
	public static final byte COMPRESS_FLAG = 1;
	public static final byte SERVERCALL_FLAG = 1 << 1;
	public static final byte DEFAULTCALLEE_FLAG = 1 << 2;
	
	public static final byte DATATYPE_VOID = 0;
	public static final byte DATATYPE_NULL = 1;
	public static final byte DATATYPE_BOOLEAN = 2;
	public static final byte DATATYPE_BYTE = 3;
	public static final byte DATATYPE_SHORT = 4;
	public static final byte DATATYPE_CHAR = 5;
	public static final byte DATATYPE_INT = 6;
	public static final byte DATATYPE_LONG = 7;
	public static final byte DATATYPE_FLOAT = 8;
	public static final byte DATATYPE_DOUBLE = 9;
	public static final byte DATATYPE_DATE = 10;
	public static final byte DATATYPE_STRING = 11;
	public static final byte DATATYPE_WRAPPER = 12;
	public static final byte DATATYPE_BYTEARRAY = 13;
	public static final byte DATATYPE_INTARRAY = 14;
	public static final byte DATATYPE_LONGARRAY = 15;
	public static final byte DATATYPE_FLOATARRAY = 16;
	public static final byte DATATYPE_DOUBLEARRAY = 17;
	public static final byte DATATYPE_STRINGARRAY = 18;
	public static final byte DATATYPE_SERIALIZABLE = 19;
	public static final byte DATATYPE_NORETURN = 20;
	
	public static final String DEFAULT_PROXY = "DefaultProxy";
	public static final String DEFAULT_METHOD = "execute";

	protected SocketConnection connection;
	
	protected String calleeClass = DEFAULT_PROXY;
	protected String calleeMethod = DEFAULT_METHOD;
	
	protected boolean asynchronous;
	protected boolean compress;
	protected long clientId;
	protected boolean serverCall;
	protected boolean defaultCallee;
	protected DefaultProxy serverCallProxy;
	
	protected byte returnType;

	public byte getReturnType() {
		return this.returnType;
	}

	public void registerReturnType(byte returnType) {
		this.returnType = returnType;
	}

	public String getCalleeClass() {
		return calleeClass;
	}

	public void setCalleeClass(String calleeClass) {
		this.calleeClass = calleeClass;
	}

	public String getCalleeMethod() {
		return calleeMethod;
	}

	public void setCalleeMethod(String calleeMethod) {
		this.calleeMethod = calleeMethod;
	}
	
	public SocketConnection getConnection() {
		return connection;
	}

	public void setConnection(SocketConnection connection) {
		this.connection = connection;
	}

	public boolean isAsynchronous() {
		return asynchronous;
	}

	public void setAsynchronous(boolean asynchronous) {
		this.asynchronous = asynchronous;
	}

	public boolean isCompress() {
		return compress;
	}

	public void setCompress(boolean compress) {
		this.compress = compress;
	}
	
	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public boolean isServerCall() {
		return serverCall;
	}

	public void setServerCall(boolean serverCall) {
		this.serverCall = serverCall;
	}
	
	public boolean isDefaultCallee() {
		return defaultCallee;
	}

	public void setDefaultCallee(boolean defaultCallee) {
		this.defaultCallee = defaultCallee;
	}

	public DefaultProxy getServerCallProxy() {
		return serverCallProxy;
	}

	public void setServerCallProxy(DefaultProxy serverCallProxy) {
		this.serverCallProxy = serverCallProxy;
	}
	
	public abstract int paramSize();
	public abstract Object peek(int index);
	public abstract void putBoolean(boolean value);
	public abstract Boolean getBoolean();
	public abstract void putByte(byte value);
	public abstract Byte getByte();
	public abstract void putShort(short value);
	public abstract Short getShort();
	public abstract void putChar(char value);
	public abstract Character getChar();
	public abstract void putInt(int value);
	public abstract Integer getInt();
	public abstract void putLong(long value);
	public abstract Long getLong();
	public abstract void putFloat(float value);
	public abstract Float getFloat();
	public abstract void putDouble(double value);
	public abstract Double getDouble();
	public abstract void putDate(Date value);
	public abstract Date getDate();
	public abstract void putString(String value);
	public abstract String getString();
	public abstract void putByteArray(byte[] value);
	public abstract byte[] getByteArray();
	public abstract void putIntArray(int[] value);
	public abstract int[] getIntArray();
	public abstract void putLongArray(long[] value);
	public abstract long[] getLongArray();
	public abstract void putFloatArray(float[] value);
	public abstract float[] getFloatArray();
	public abstract void putDoubleArray(double[] value);
	public abstract double[] getDoubleArray();
	public abstract void putStringArray(String[] value);
	public abstract String[] getStringArray();
	public abstract void putSerializable(Serializable value);
	public abstract Serializable getSerializable();
	public abstract void putWrapper(TransferObjectWrapper value);
	public abstract TransferObjectWrapper getWrapper();
	
	public abstract byte[] getByteData() throws IOException;
	
	// server call
	public abstract void setByteData(byte[] buf) throws SocketExecuteException;

	public void convertInputStream(InputStream is) throws IOException, SocketExecuteException {
		// get byte array length
		int blength = TransferUtil.readInt(is);
		
		//create byte array
		byte[] buf = new byte[blength];
		is.read(buf);
		
		setByteData(buf);
	}

	// server call
	public byte[] getReturnByteArray(Object retobj) throws IOException {
		int blength = 0;
		// length of return type
		blength += TransferUtil.getLengthOfByte();
		// length of return value
		if (returnType == DATATYPE_BOOLEAN) {
			blength += TransferUtil.getLengthOfBoolean();
		} else if (returnType == DATATYPE_BYTE) {
			blength += TransferUtil.getLengthOfByte();
		} else if (returnType == DATATYPE_SHORT) {
			blength += TransferUtil.getLengthOfShort();
		} else if (returnType == DATATYPE_CHAR) {
			blength += TransferUtil.getLengthOfChar();
		} else if (returnType == DATATYPE_INT) {
			blength += TransferUtil.getLengthOfInt();
		} else if (returnType == DATATYPE_LONG) {
			blength += TransferUtil.getLengthOfLong();
		} else if (returnType == DATATYPE_FLOAT) {
			blength += TransferUtil.getLengthOfFloat();
		} else if (returnType == DATATYPE_DOUBLE) {
			blength += TransferUtil.getLengthOfDouble();
		} else if (returnType == DATATYPE_DATE) {
			blength += TransferUtil.getLengthOfDate();
		} else if (returnType == DATATYPE_STRING) {
			blength += TransferUtil.getLengthOfString((String) retobj);
		} else if (returnType == DATATYPE_WRAPPER) {
			blength += TransferUtil.getLengthOfWrapper((TransferObjectWrapper) retobj);
		} else if (returnType == DATATYPE_VOID) {
			blength += TransferUtil.getLengthOfInt();
			
		} else if (returnType == DATATYPE_BYTEARRAY) {
			blength += TransferUtil.getLengthOfByteArray((byte[]) retobj);
		} else if (returnType == DATATYPE_INTARRAY) {
			blength += TransferUtil.getLengthOfIntArray((int[]) retobj);
		} else if (returnType == DATATYPE_LONGARRAY) {
			blength += TransferUtil.getLengthOfLongArray((long[]) retobj);
		} else if (returnType == DATATYPE_FLOATARRAY) {
			blength += TransferUtil.getLengthOfFloatArray((float[]) retobj);
		} else if (returnType == DATATYPE_DOUBLEARRAY) {
			blength += TransferUtil.getLengthOfDoubleArray((double[]) retobj);
		} else if (returnType == DATATYPE_STRINGARRAY) {
			blength += TransferUtil.getLengthOfStringArray((String[]) retobj);
		}
		
		byte[] serbytearr = null;
		if (returnType == DATATYPE_SERIALIZABLE) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			(new ObjectOutputStream(bos)).writeObject(retobj);
			serbytearr = bos.toByteArray();
			blength += TransferUtil.getLengthOfInt();
			blength += serbytearr.length;
		}
		
		byte[] buf = new byte[TransferUtil.getLengthOfInt() + blength];

		TransferOutputStream touts = new TransferOutputStream(buf);
		// length
		touts.writeInt(blength);
		// return type
		touts.writeByte(returnType);
		// return value
		if (returnType == DATATYPE_BOOLEAN) {
			touts.writeBoolean((Boolean)retobj);
		} else if (returnType == DATATYPE_BYTE) {
			touts.writeByte((Byte)retobj);
		} else if (returnType == DATATYPE_SHORT) {
			touts.writeShort((Short)retobj);
		} else if (returnType == DATATYPE_CHAR) {
			touts.writeChar((Character)retobj);
		} else if (returnType == DATATYPE_INT) {
			touts.writeInt((Integer)retobj);
		} else if (returnType == DATATYPE_LONG) {
			touts.writeLong((Long)retobj);
		} else if (returnType == DATATYPE_FLOAT) {
			touts.writeFloat((Float)retobj);
		} else if (returnType == DATATYPE_DOUBLE) {
			touts.writeDouble((Double)retobj);
		} else if (returnType == DATATYPE_DATE) {
			touts.writeDate((Date)retobj);
		} else if (returnType == DATATYPE_STRING) {
			touts.writeString((String)retobj);
		} else if (returnType == DATATYPE_WRAPPER) {
			touts.writeWrapper((TransferObjectWrapper)retobj);
		} else if (returnType == DATATYPE_VOID) {
			touts.writeNull();
			
		} else if (returnType == DATATYPE_BYTEARRAY) {
			touts.writeByteArray((byte[]) retobj);
		} else if (returnType == DATATYPE_INTARRAY) {
			touts.writeIntArray((int[]) retobj);
		} else if (returnType == DATATYPE_LONGARRAY) {
			touts.writeLongArray((long[]) retobj);
		} else if (returnType == DATATYPE_FLOATARRAY) {
			touts.writeFloatArray((float[]) retobj);
		} else if (returnType == DATATYPE_DOUBLEARRAY) {
			touts.writeDoubleArray((double[]) retobj);
		} else if (returnType == DATATYPE_STRINGARRAY) {
			touts.writeStringArray((String[]) retobj);
		} else if (returnType == DATATYPE_SERIALIZABLE) {
			touts.writeInt(serbytearr.length);
			touts.write(serbytearr, 0, serbytearr.length);
		}
		
		return buf;
	}

	// client call
	public static Object convertReturnInputStream(InputStream is) throws IOException, SocketExecuteException {
		// get byte array length
		int blength = TransferUtil.readInt(is);

		byte[] buf = new byte[blength];
		is.read(buf);

		return convertReturnByteArray(buf);
	}
	
	public static Object convertReturnByteArray(byte[] buf) throws SocketExecuteException{
		Object returnObject = null;

		TransferInputStream tins = new TransferInputStream(buf);
		byte returnType = tins.readByte();
		if (returnType == DATATYPE_BOOLEAN) {
			returnObject = new Boolean(tins.readBoolean());
		} else if (returnType == DATATYPE_BYTE) {
			returnObject = new Byte(tins.readByte());
		} else if (returnType == DATATYPE_SHORT) {
			returnObject = new Short(tins.readShort());
		} else if (returnType == DATATYPE_CHAR) {
			returnObject = new Character(tins.readChar());
		} else if (returnType == DATATYPE_INT) {
			returnObject = new Integer(tins.readInt());
		} else if (returnType == DATATYPE_LONG) {
			returnObject = new Long(tins.readLong());
		} else if (returnType == DATATYPE_FLOAT) {
			returnObject = new Float(tins.readFloat());
		} else if (returnType == DATATYPE_DOUBLE) {
			returnObject = new Double(tins.readDouble());
		} else if (returnType == DATATYPE_DATE) {
			returnObject = tins.readDate();
		} else if (returnType == DATATYPE_STRING) {
			returnObject = tins.readString();
		} else if (returnType == DATATYPE_WRAPPER) {
			returnObject = (TransferObjectWrapper)tins.readWrapper();
		} else if (returnType == DATATYPE_VOID) {
			returnObject = null;
			
		} else if (returnType == DATATYPE_BYTEARRAY) {
			returnObject = tins.readByteArray();
		} else if (returnType == DATATYPE_INTARRAY) {
			returnObject = tins.readIntArray();
		} else if (returnType == DATATYPE_LONGARRAY) {
			returnObject = tins.readLongArray();
		} else if (returnType == DATATYPE_FLOATARRAY) {
			returnObject = tins.readFloatArray();
		} else if (returnType == DATATYPE_DOUBLEARRAY) {
			returnObject = tins.readDoubleArray();
		} else if (returnType == DATATYPE_STRINGARRAY) {
			returnObject = tins.readStringArray();
		} else if (returnType == DATATYPE_SERIALIZABLE) {
			returnObject = tins.readSerializable();
		}
		
		return returnObject;
	}

}

