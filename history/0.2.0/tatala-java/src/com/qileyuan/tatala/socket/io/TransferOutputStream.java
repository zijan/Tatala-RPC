package com.qileyuan.tatala.socket.io;

import java.util.Date;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.socket.to.TransferObjectWrapper;
import com.qileyuan.tatala.socket.util.TransferUtil;

/**
 * This class helps TransferObject convert primitive type data and 
 * user-defined object into byte array.
 * @author JimT
 *
 */
public class TransferOutputStream {
	Logger log = Logger.getLogger(TransferOutputStream.class);
	public static final int NULL_PLACE_HOLDER = -99;
	public final static byte TRUEBYTE = 1;
	public final static byte FALSEBYTE = 0;
	
	private byte[] buf;
	private int loc;
	
	public TransferOutputStream(byte[] buf) {
		this.buf = buf;
		this.loc = 0;
	}

	public byte[] getBuf() {
		return buf;
	}

	public void setBuf(byte[] buf) {
		this.buf = buf;
	}

	public void write(byte b){
		buf[loc++] = b;
	}
	
	public void writeByte(byte b){
		write(b);
	}

	public void writeByteArray(byte[] v){
		if (v == null){
			writeInt(NULL_PLACE_HOLDER);
			return;
		}
		
		writeInt(v.length);
		for (byte oneValue : v) {
			writeByte(oneValue);
		}
	}
	
	public void write(byte[] b, int off, int len) {
		int end = off + len;
		for (int i = off; i < end; i++)
			buf[loc++] = b[i];
	}

	public  void writeBoolean(boolean v){
		if(v){
			write(TRUEBYTE);
		}else{
			write(FALSEBYTE);
		}
	}
	
	public  void writeShort(int v){
		write((byte)((v >>> 8) & 0xFF));
		write((byte)((v >>> 0) & 0xFF));
	}

	public  void writeChar(int v){
		writeShort(v);
	}
	
	public void writeInt(int v){
		write((byte) ((v >>> 24) & 0xFF));
		write((byte) ((v >>> 16) & 0xFF));
		write((byte) ((v >>> 8) & 0xFF));
		write((byte) ((v >>> 0) & 0xFF));
	}
	
	public void writeIntArray(int[] v){
		if (v == null){
			writeInt(NULL_PLACE_HOLDER);
			return;
		}
		
		writeInt(v.length);
		for (int oneValue : v) {
			writeInt(oneValue);
		}
	}
	
	public  void writeLong(long v){
		write((byte)(v >>> 56));
		write((byte)(v >>> 48));
		write((byte)(v >>> 40));
		write((byte)(v >>> 32));
		write((byte)(v >>> 24));
		write((byte)(v >>> 16));
		write((byte)(v >>>  8));
		write((byte)(v >>>  0));
	}

	public void writeLongArray(long[] v){
		if (v == null){
			writeInt(NULL_PLACE_HOLDER);
			return;
		}
		
		writeInt(v.length);
		for (long oneValue : v) {
			writeLong(oneValue);
		}
	}
	
	public  void writeFloat(float v){
		writeInt(Float.floatToIntBits(v));
	}

	public void writeFloatArray(float[] v){
		if (v == null){
			writeInt(NULL_PLACE_HOLDER);
			return;
		}
		
		writeInt(v.length);
		for (float oneValue : v) {
			writeFloat(oneValue);
		}
	}
	
	public  void writeDouble(double v){
		writeLong(Double.doubleToLongBits(v));
	}

	public void writeDoubleArray(double[] v){
		if (v == null){
			writeInt(NULL_PLACE_HOLDER);
			return;
		}
		
		writeInt(v.length);
		for (double oneValue : v) {
			writeDouble(oneValue);
		}
	}
	
	public void writeDate(Date date){
		writeLong(date.getTime());
	}
	
	public void writeString(String s){
		if (s==null){
			writeInt(NULL_PLACE_HOLDER);
			return;
		}
		
		byte[] b = null;
		try {
			b = s.getBytes("UTF-8");
			//b = s.getBytes();
		} catch (Exception e) {
			log.error("TransferOutputStream.writeString e: "+e.getMessage());
		}
		int len = b.length;
		writeInt(len);
		write(b, 0, len);
	}
	
	public void writeStringArray(String[] strs){
		if (strs == null){
			writeInt(NULL_PLACE_HOLDER);
			return;
		}
		
		writeInt(strs.length);
		for (String s : strs) {
			writeString(s);
		}
	}

	public void writeWrapper(TransferObjectWrapper v){
		
		if(v == null){
			writeNull();
			return;
		}
		
		int blength = TransferUtil.getLengthOfString(v.getClass().getName()) + v.getLength();
		byte[] byteArray = new byte[TransferUtil.getLengthOfInt() + blength];
		
		TransferOutputStream touts = new TransferOutputStream(byteArray);
		touts.writeInt(blength);
		touts.writeString(v.getClass().getName());
		
		v.getByteArray(touts);
		
		write(byteArray, 0, byteArray.length);
	}
	
	public void writeNull(){
		writeInt(NULL_PLACE_HOLDER);
	}
	
    public void skipAByte() {
        this.loc++;
    }
}
