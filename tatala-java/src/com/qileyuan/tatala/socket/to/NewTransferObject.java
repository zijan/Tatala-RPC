package com.qileyuan.tatala.socket.to;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import com.qileyuan.tatala.socket.exception.SocketExecuteException;
import com.qileyuan.tatala.socket.io.TransferInputStream;
import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.util.TransferUtil;

/**
 * This class is transfer object class. It converts java primitive type data and
 * user-defined object into byte array and vice versa.
 * 
 * This is the new version of transfer object, remove parameter name, call and
 * retrieve by order.
 * 
 * <pre>
 * NewTransferObject
 * Client -> Server (remove parameter key)
 * 	normal:
 * 	TatalaFlag+(Flag)+( BLength+CLASS+METHOD+ReturnType+CompressFlag+[type+value]+[type+value]+[...] )
 * 	compress:
 * 	TatalaFlag+(Flag)+UnCompressLength+CompressLength+( BLength+CLASS+METHOD+ReturnType+CompressFlag+[type+value]+[type+value]+[...] )
 * 	
 * 	Object:
 * 	value=BLength+WCLASS+[value]+[value]+[...]
 * 	
 * 	Flag:
 * 	compress = 1;
 * 	servercall = 1 << 1;
 * 	longconnection = 1 << 2;
 * 	TransferObjectVersion = 1 << 3;(NewTransferObject)
 * 	
 * 	Server -> Client
 * 	normal:
 * 	(Flag)+( BLength+ReturnType+value )
 * 	compress:
 * 	(Flag)+UnCompressLength+CompressLength+( BLength+ReturnType+value )
 * </pre>
 * 
 * @author JimT
 * 
 */
public class NewTransferObject extends TransferObject {

	class ValueObject {
		private byte dataType;
		private Object dataObject;

		public ValueObject(byte dataType, Object dataObject) {
			this.dataType = dataType;
			this.dataObject = dataObject;
		}
	}

	private Queue<ValueObject> paramList = new LinkedList<ValueObject>();

	public NewTransferObject(){
		newVersion = true;
	}
	
	/** Parameter Map **/
	public int paramSize(){
		return paramList.size();
	}
	
	public Object peek(int index){
		if(paramList.size() > 0 && index < paramList.size()){
			ValueObject vo = ((LinkedList<ValueObject>)paramList).get(index);
			return vo.dataObject;
		}
		return null;
	}
	
	public void putBoolean(boolean value) {
		paramList.add(new ValueObject(DATATYPE_BOOLEAN, value));
	}

	public Boolean getBoolean() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_BOOLEAN) {
			return (Boolean) vo.dataObject;
		}
		return null;
	}

	public void putByte(byte value) {
		paramList.add(new ValueObject(DATATYPE_BYTE, value));
	}

	public Byte getByte() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_BYTE) {
			return (Byte) vo.dataObject;
		}
		return null;
	}

	public void putShort(short value) {
		paramList.add(new ValueObject(DATATYPE_SHORT, value));
	}

	public Short getShort() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_SHORT) {
			return (Short) vo.dataObject;
		}
		return null;
	}

	public void putChar(char value) {
		paramList.add(new ValueObject(DATATYPE_CHAR, value));
	}

	public Character getChar() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_CHAR) {
			return (Character) vo.dataObject;
		}
		return null;
	}

	public void putInt(int value) {
		paramList.add(new ValueObject(DATATYPE_INT, value));
	}

	public Integer getInt() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_INT) {
			return (Integer) vo.dataObject;
		}
		return null;
	}

	public void putLong(long value) {
		paramList.add(new ValueObject(DATATYPE_LONG, value));
	}

	public Long getLong() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_LONG) {
			return (Long) vo.dataObject;
		}
		return null;
	}

	public void putFloat(float value) {
		paramList.add(new ValueObject(DATATYPE_FLOAT, value));
	}

	public Float getFloat() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_FLOAT) {
			return (Float) vo.dataObject;
		}
		return null;
	}

	public void putDouble(double value) {
		paramList.add(new ValueObject(DATATYPE_DOUBLE, value));
	}

	public Double getDouble() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_DOUBLE) {
			return (Double) vo.dataObject;
		}
		return null;
	}

	public void putDate(Date value) {
		paramList.add(new ValueObject(DATATYPE_DATE, value));
	}

	public Date getDate() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_DATE) {
			return (Date) vo.dataObject;
		}
		return null;
	}

	public void putString(String value) {
		paramList.add(new ValueObject(DATATYPE_STRING, value));
	}

	public String getString() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_STRING) {
			return (String) vo.dataObject;
		}
		return null;
	}

	public void putByteArray(byte[] value) {
		paramList.add(new ValueObject(DATATYPE_BYTEARRAY, value));
	}

	public byte[] getByteArray() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_BYTEARRAY) {
			return (byte[]) vo.dataObject;
		}
		return null;
	}
	
	public void putIntArray(int[] value) {
		paramList.add(new ValueObject(DATATYPE_INTARRAY, value));
	}

	public int[] getIntArray() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_INTARRAY) {
			return (int[]) vo.dataObject;
		}
		return null;
	}

	public void putLongArray(long[] value) {
		paramList.add(new ValueObject(DATATYPE_LONGARRAY, value));
	}

	public long[] getLongArray() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_LONGARRAY) {
			return (long[]) vo.dataObject;
		}
		return null;
	}

	public void putFloatArray(float[] value) {
		paramList.add(new ValueObject(DATATYPE_FLOATARRAY, value));
	}

	public float[] getFloatArray() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_FLOATARRAY) {
			return (float[]) vo.dataObject;
		}
		return null;
	}

	public void putDoubleArray(double[] value) {
		paramList.add(new ValueObject(DATATYPE_DOUBLEARRAY, value));
	}

	public double[] getDoubleArray() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_DOUBLEARRAY) {
			return (double[]) vo.dataObject;
		}
		return null;
	}

	public void putStringArray(String[] value) {
		paramList.add(new ValueObject(DATATYPE_STRINGARRAY, value));
	}

	public String[] getStringArray() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_STRINGARRAY) {
			return (String[]) vo.dataObject;
		}
		return null;
	}

	public void putSerializable(Serializable value) {
		paramList.add(new ValueObject(DATATYPE_SERIALIZABLE, value));
	}

	public Serializable getSerializable() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_SERIALIZABLE) {
			return (Serializable) vo.dataObject;
		}
		return null;
	}

	public void putWrapper(TransferObjectWrapper value) {
		paramList.add(new ValueObject(DATATYPE_WRAPPER, value));
	}

	public TransferObjectWrapper getWrapper() {
		ValueObject vo = paramList.poll();
		if (vo != null && vo.dataType == DATATYPE_WRAPPER) {
			return (TransferObjectWrapper) vo.dataObject;
		}
		return null;
	}
	
	public byte[] getByteData() throws IOException {

		int blength = getByteArrayLength();

		byte[] byteArray = new byte[TransferUtil.getLengthOfInt() + blength];

		TransferOutputStream touts = new TransferOutputStream(byteArray);
		touts.writeInt(blength);
		touts.writeString(calleeClass);
		touts.writeString(calleeMethod);
		touts.writeByte(returnType);
		touts.writeBoolean(compress);

		for (ValueObject vo : paramList) {
			if (vo.dataType == DATATYPE_BOOLEAN) {
				touts.writeByte(DATATYPE_BOOLEAN);
				touts.writeBoolean((Boolean) vo.dataObject);
			} else if (vo.dataType == DATATYPE_BYTE) {
				touts.writeByte(DATATYPE_BYTE);
				touts.writeByte((Byte) vo.dataObject);
			} else if (vo.dataType == DATATYPE_SHORT) {
				touts.writeByte(DATATYPE_SHORT);
				touts.writeShort((Short) vo.dataObject);
			} else if (vo.dataType == DATATYPE_CHAR) {
				touts.writeByte(DATATYPE_CHAR);
				touts.writeChar((Character) vo.dataObject);
			} else if (vo.dataType == DATATYPE_INT) {
				touts.writeByte(DATATYPE_INT);
				touts.writeInt((Integer) vo.dataObject);
			} else if (vo.dataType == DATATYPE_LONG) {
				touts.writeByte(DATATYPE_LONG);
				touts.writeLong((Long) vo.dataObject);
			} else if (vo.dataType == DATATYPE_FLOAT) {
				touts.writeByte(DATATYPE_FLOAT);
				touts.writeFloat((Float) vo.dataObject);
			} else if (vo.dataType == DATATYPE_DOUBLE) {
				touts.writeByte(DATATYPE_DOUBLE);
				touts.writeDouble((Double) vo.dataObject);
			} else if (vo.dataType == DATATYPE_DATE) {
				touts.writeByte(DATATYPE_DATE);
				touts.writeDate((Date) vo.dataObject);
			} else if (vo.dataType == DATATYPE_STRING) {
				touts.writeByte(DATATYPE_STRING);
				touts.writeString((String) vo.dataObject);
				
			} else if (vo.dataType == DATATYPE_BYTEARRAY) {
				touts.writeByte(DATATYPE_BYTEARRAY);
				touts.writeByteArray((byte[]) vo.dataObject);
			} else if (vo.dataType == DATATYPE_INTARRAY) {
				touts.writeByte(DATATYPE_INTARRAY);
				touts.writeIntArray((int[]) vo.dataObject);
			} else if (vo.dataType == DATATYPE_LONGARRAY) {
				touts.writeByte(DATATYPE_LONGARRAY);
				touts.writeLongArray((long[]) vo.dataObject);
			} else if (vo.dataType == DATATYPE_FLOATARRAY) {
				touts.writeByte(DATATYPE_FLOATARRAY);
				touts.writeFloatArray((float[]) vo.dataObject);
			} else if (vo.dataType == DATATYPE_DOUBLEARRAY) {
				touts.writeByte(DATATYPE_DOUBLEARRAY);
				touts.writeDoubleArray((double[]) vo.dataObject);
			} else if (vo.dataType == DATATYPE_STRINGARRAY) {
				touts.writeByte(DATATYPE_STRINGARRAY);
				touts.writeStringArray((String[]) vo.dataObject);
				
			} else if (vo.dataType == DATATYPE_SERIALIZABLE) {
				touts.writeByte(DATATYPE_SERIALIZABLE);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				(new ObjectOutputStream(bos)).writeObject((Serializable) vo.dataObject);
				byte[] bytearr = bos.toByteArray();
				touts.writeInt(bytearr.length);
				touts.write(bytearr, 0, bytearr.length);
				
			} else if (vo.dataType == DATATYPE_WRAPPER) {
				touts.writeByte(DATATYPE_WRAPPER);
				touts.writeWrapper((TransferObjectWrapper) vo.dataObject);
			}
		}

		return byteArray;
	}

	private int getByteArrayLength() throws IOException {
		int blength = 0;

		// length of class, method, returnType and compress flag
		blength += TransferUtil.getLengthOfString(calleeClass);
		blength += TransferUtil.getLengthOfString(calleeMethod);
		blength += TransferUtil.getLengthOfByte();
		blength += TransferUtil.getLengthOfByte();

		// length of list data
		for (ValueObject vo : paramList) {
			if (vo.dataType == DATATYPE_BOOLEAN) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfBoolean();
			} else if (vo.dataType == DATATYPE_BYTE) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfByte();
			} else if (vo.dataType == DATATYPE_SHORT) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfShort();
			} else if (vo.dataType == DATATYPE_CHAR) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfChar();
			} else if (vo.dataType == DATATYPE_INT) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfInt();
			} else if (vo.dataType == DATATYPE_LONG) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfLong();
			} else if (vo.dataType == DATATYPE_FLOAT) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfFloat();
			} else if (vo.dataType == DATATYPE_DOUBLE) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfDouble();
			} else if (vo.dataType == DATATYPE_DATE) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfDate();
			} else if (vo.dataType == DATATYPE_STRING) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfString((String) vo.dataObject);
				
			} else if (vo.dataType == DATATYPE_BYTEARRAY) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfByteArray((byte[]) vo.dataObject);
			} else if (vo.dataType == DATATYPE_INTARRAY) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfIntArray((int[]) vo.dataObject);
			} else if (vo.dataType == DATATYPE_LONGARRAY) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfLongArray((long[]) vo.dataObject);
			} else if (vo.dataType == DATATYPE_FLOATARRAY) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfFloatArray((float[]) vo.dataObject);
			} else if (vo.dataType == DATATYPE_DOUBLEARRAY) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfDoubleArray((double[]) vo.dataObject);
			} else if (vo.dataType == DATATYPE_STRINGARRAY) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfStringArray((String[]) vo.dataObject);
				
			} else if (vo.dataType == DATATYPE_SERIALIZABLE) {
				//type
				blength += TransferUtil.getLengthOfByte();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				(new ObjectOutputStream(bos)).writeObject((Serializable) vo.dataObject);
				byte[] bytearr = bos.toByteArray();
				//object data length
				blength += TransferUtil.getLengthOfInt();
				blength += bytearr.length;
				
			} else if (vo.dataType == DATATYPE_WRAPPER) {
				blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfWrapper((TransferObjectWrapper) vo.dataObject);
			}
		}

		return blength;
	}

	// server call
	public void setByteData(byte[] buf) throws SocketExecuteException {

		TransferInputStream tins = new TransferInputStream(buf);
		this.calleeClass = tins.readString();
		this.calleeMethod = tins.readString();
		this.returnType = tins.readByte();
		this.compress = tins.readBoolean();

		while (!tins.isFinished()) {
			byte type = tins.readByte();
			if (type == DATATYPE_BOOLEAN) {
				putBoolean(tins.readBoolean());
			} else if (type == DATATYPE_BYTE) {
				putByte(tins.readByte());
			} else if (type == DATATYPE_SHORT) {
				putShort(tins.readShort());
			} else if (type == DATATYPE_CHAR) {
				putChar(tins.readChar());
			} else if (type == DATATYPE_INT) {
				putInt(tins.readInt());
			} else if (type == DATATYPE_LONG) {
				putLong(tins.readLong());
			} else if (type == DATATYPE_FLOAT) {
				putFloat(tins.readFloat());
			} else if (type == DATATYPE_DOUBLE) {
				putDouble(tins.readDouble());
			} else if (type == DATATYPE_DATE) {
				putDate(tins.readDate());
			} else if (type == DATATYPE_STRING) {
				putString(tins.readString());
				
			} else if (type == DATATYPE_BYTEARRAY) {
				putByteArray(tins.readByteArray());
			} else if (type == DATATYPE_INTARRAY) {
				putIntArray(tins.readIntArray());
			} else if (type == DATATYPE_LONGARRAY) {
				putLongArray(tins.readLongArray());
			} else if (type == DATATYPE_FLOATARRAY) {
				putFloatArray(tins.readFloatArray());
			} else if (type == DATATYPE_DOUBLEARRAY) {
				putDoubleArray(tins.readDoubleArray());
			} else if (type == DATATYPE_STRINGARRAY) {
				putStringArray(tins.readStringArray());
				
			} else if (type == DATATYPE_SERIALIZABLE) {
				putSerializable(tins.readSerializable());
				
			} else if (type == DATATYPE_WRAPPER) {
				putWrapper(tins.readWrapper());
			}
		}

	}

}
