package com.qileyuan.tatala.socket.io;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.socket.exception.SocketExecuteException;
import com.qileyuan.tatala.socket.to.TransferObjectWrapper;
import com.qileyuan.tatala.socket.util.TransferUtil;

/**
 * This class helps TransferObject convert byte array into 
 * primitive type data and user-defined object.
 * @author JimT
 *
 */
public class TransferInputStream {
	Logger log = Logger.getLogger(TransferInputStream.class);
	
	private byte[] buf;
	private int loc;
	
	public TransferInputStream(byte[] buf) {
		this.buf = buf;
		this.loc = 0;
	}

	public byte[] getBuf() {
		return buf;
	}
	public void setBuf(byte[] buf) {
		this.buf = buf;
	}
	
	public void readFully(byte[] b, int off, int len){
		int n = 0;
		while (n < len) {
			int count = read(b, off + n, len - n);
			n += count;
		}
	}

	public int read(byte[] b, int off, int len) {
		System.arraycopy(buf, loc, b, off, len);
		loc += len;
		return len;
	}
	
	public int read(){
		int val = buf[loc++] & 0xff;
		return val;
	}
	
	public byte readByte(){
		int ch = read();
		return (byte)(ch);
	}
	
	public byte[] readByteArray(){
		int len = readInt();
		if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
			return null;
		}
		
		byte[] byteArray = new byte[len];
		for (int i = 0; i < len; i++) {
			byteArray[i] = readByte();
		}
		
		return byteArray;
	}
	
	public boolean readBoolean() {
		int ch = read();
		return (ch == TransferOutputStream.TRUEBYTE);
	}
	
	public short readShort(){
		int ch1 = read();
		int ch2 = read();
		return (short)((ch1 << 8) + (ch2 << 0));
	}
	
	public char readChar(){
		return (char)readShort();
	}
	
	public int readInt(){
		int ch1 = read();
		int ch2 = read();
		int ch3 = read();
		int ch4 = read();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	public int[] readIntArray(){
		int len = readInt();
		if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
			return null;
		}
		
		int[] intArray = new int[len];
		for (int i = 0; i < len; i++) {
			intArray[i] = readInt();
		}
		
		return intArray;
	}
	
	public long readLong(){
		byte[] b = new byte[8];
		readFully(b, 0, 8);
		return (((long) b[0] << 56) + ((long) (b[1] & 255) << 48)
				+ ((long) (b[2] & 255) << 40) + ((long) (b[3] & 255) << 32)
				+ ((long) (b[4] & 255) << 24) + ((b[5] & 255) << 16)
				+ ((b[6] & 255) << 8) + ((b[7] & 255) << 0));
	}

	public long[] readLongArray(){
		int len = readInt();
		if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
			return null;
		}
		
		long[] longArray = new long[len];
		for (int i = 0; i < len; i++) {
			longArray[i] = readLong();
		}
		
		return longArray;
	}
	
	public float readFloat(){
		return Float.intBitsToFloat(readInt());
	}
	
	public float[] readFloatArray(){
		int len = readInt();
		if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
			return null;
		}
		
		float[] floatArray = new float[len];
		for (int i = 0; i < len; i++) {
			floatArray[i] = readFloat();
		}
		
		return floatArray;
	}
	
	public double readDouble(){
		return Double.longBitsToDouble(readLong());
	}
	
	public double[] readDoubleArray(){
		int len = readInt();
		if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
			return null;
		}
		
		double[] doubleArray = new double[len];
		for (int i = 0; i < len; i++) {
			doubleArray[i] = readDouble();
		}
		
		return doubleArray;
	}
	
	public Date readDate(){
		return new Date(readLong());
	}
	
	public String readString(){
		int len = readInt();
		if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
			return null;
		}
		
		byte bytearr[] = new byte[len];
		readFully(bytearr, 0, len);
		String str = null;
		try {
			str = new String(bytearr, "UTF-8");
			//str = new String(bytearr);
		} catch (Exception e) {
			log.error("TransferInputStream.readString e: " + e.getMessage());
		}
		return str;
	}

	public String[] readStringArray(){
		int len = readInt();
		if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
			return null;
		}
		
		String[] strArray = new String[len];
		for (int i = 0; i < len; i++) {
			strArray[i] = readString();
		}
		
		return strArray;
	}
	
	public Serializable readSerializable(){
		int len = readInt();
		if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
			return null;
		}
		
		byte bytearr[] = new byte[len];
		readFully(bytearr, 0, len);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bytearr);   
		Object object = null;
		try {
			object = (new ObjectInputStream(bis)).readObject();
		} catch (Exception e) {
			log.error("TransferInputStream.readSerializable e: " + e.getMessage());
		}   
	    return (Serializable)object;

	}

	public TransferObjectWrapper readWrapper() throws SocketExecuteException{
		int len = readInt();
		if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
			return null;
		}
		
		String wrapperClassName = readString();
		len -= TransferUtil.getLengthOfString(wrapperClassName);
		byte[] bytearr = new byte[len];
		readFully(bytearr, 0, len);
		
		Object retobj = null;
		try {
			Class<?> cls = Class.forName(wrapperClassName);
			Object instance = cls.newInstance();
			TransferInputStream tins = new TransferInputStream(bytearr);
			Method meth = cls.getMethod("getObjectWrapper", TransferInputStream.class);
			retobj = meth.invoke(instance, tins);
		} catch (Exception e) {
			log.error("TransferInputStream.readWrapper e: " + e);
			throw new SocketExecuteException("TransferInputStream.readWrapper e: " + e.getMessage());
		}

		return (TransferObjectWrapper)retobj;
	}
	
	public boolean isFinished(){
		return loc >= buf.length;
	}

}
