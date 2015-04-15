package com.qileyuan.tatala.socket.to;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.qileyuan.tatala.socket.exception.SocketExecuteException;
import com.qileyuan.tatala.socket.io.TransferInputStream;
import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.helper.BooleanMapHelper;
import com.qileyuan.tatala.socket.to.helper.ByteArrayMapHelper;
import com.qileyuan.tatala.socket.to.helper.ByteMapHelper;
import com.qileyuan.tatala.socket.to.helper.CharMapHelper;
import com.qileyuan.tatala.socket.to.helper.DateMapHelper;
import com.qileyuan.tatala.socket.to.helper.DoubleArrayMapHelper;
import com.qileyuan.tatala.socket.to.helper.DoubleMapHelper;
import com.qileyuan.tatala.socket.to.helper.FloatArrayMapHelper;
import com.qileyuan.tatala.socket.to.helper.FloatMapHelper;
import com.qileyuan.tatala.socket.to.helper.IntArrayMapHelper;
import com.qileyuan.tatala.socket.to.helper.IntMapHelper;
import com.qileyuan.tatala.socket.to.helper.LongArrayMapHelper;
import com.qileyuan.tatala.socket.to.helper.LongMapHelper;
import com.qileyuan.tatala.socket.to.helper.SerializableMapHelper;
import com.qileyuan.tatala.socket.to.helper.ShortMapHelper;
import com.qileyuan.tatala.socket.to.helper.StringArrayMapHelper;
import com.qileyuan.tatala.socket.to.helper.StringMapHelper;
import com.qileyuan.tatala.socket.to.helper.WrapperMapHelper;
import com.qileyuan.tatala.socket.util.TransferUtil;

/**
 * This class is transfer object class. It converts java primitive type data and
 * user-defined object into byte array and vice versa.
 * 
 * This is the standard version of transfer object, have parameter name.
 * 
 * <pre>
 * Client -> Server
 * 	normal:
 * 	TatalaFlag+(Flag)+( BLength+CLASS+METHOD+ReturnType+CompressFlag+[type+key+value]+[type+key+value]+[...] )
 * 	compress:
 * 	TatalaFlag+(Flag)+UnCompressLength+CompressLength+( BLength+CLASS+METHOD+ReturnType+CompressFlag+[type+key+value]+[type+key+value]+[...] )
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
public class StandardTransferObject extends TransferObject {

	private Map<String, Boolean> booleanMap = new HashMap<String, Boolean>();
	private Map<String, Byte> byteMap = new HashMap<String, Byte>();
	private Map<String, Short> shortMap = new HashMap<String, Short>();
	private Map<String, Character> charMap = new HashMap<String, Character>();
	private Map<String, Integer> intMap = new HashMap<String, Integer>();
	private Map<String, Long> longMap = new HashMap<String, Long>();
	private Map<String, Float> floatMap = new HashMap<String, Float>();
	private Map<String, Double> doubleMap = new HashMap<String, Double>();
	private Map<String, Date> dateMap = new HashMap<String, Date>();
	private Map<String, String> stringMap = new HashMap<String, String>();
	private Map<String, TransferObjectWrapper> wrapperMap = new HashMap<String, TransferObjectWrapper>();

	private Map<String, byte[]> byteArrayMap = new HashMap<String, byte[]>();
	private Map<String, int[]> intArrayMap = new HashMap<String, int[]>();
	private Map<String, long[]> longArrayMap = new HashMap<String, long[]>();
	private Map<String, float[]> floatArrayMap = new HashMap<String, float[]>();
	private Map<String, double[]> doubleArrayMap = new HashMap<String, double[]>();
	private Map<String, String[]> stringArrayMap = new HashMap<String, String[]>();

	private Map<String, Serializable> serializableMap = new HashMap<String, Serializable>();

	/** Parameter Map **/
	public void putBoolean(String key, boolean value) {
		booleanMap.put(key, value);
	}

	public Boolean getBoolean(String key) {
		return booleanMap.get(key);
	}

	public void putByte(String key, byte value) {
		byteMap.put(key, value);
	}

	public Byte getByte(String key) {
		return byteMap.get(key);
	}

	public void putShort(String key, short value) {
		shortMap.put(key, value);
	}

	public Short getShort(String key) {
		return shortMap.get(key);
	}

	public void putChar(String key, char value) {
		charMap.put(key, value);
	}

	public Character getChar(String key) {
		return charMap.get(key);
	}

	public void putInt(String key, int value) {
		intMap.put(key, value);
	}

	public Integer getInt(String key) {
		return intMap.get(key);
	}

	public void putLong(String key, long value) {
		longMap.put(key, value);
	}

	public Long getLong(String key) {
		return longMap.get(key);
	}

	public void putFloat(String key, float value) {
		floatMap.put(key, value);
	}

	public Float getFloat(String key) {
		return floatMap.get(key);
	}

	public void putDouble(String key, double value) {
		doubleMap.put(key, value);
	}

	public Double getDouble(String key) {
		return doubleMap.get(key);
	}

	public void putDate(String key, Date value) {
		dateMap.put(key, value);
	}

	public Date getDate(String key) {
		return dateMap.get(key);
	}

	public void putString(String key, String value) {
		stringMap.put(key, value);
	}

	public String getString(String key) {
		return stringMap.get(key);
	}

	public void putWrapper(String key, TransferObjectWrapper value) {
		wrapperMap.put(key, value);
	}

	public TransferObjectWrapper getWrapper(String key) {
		return wrapperMap.get(key);
	}

	public void putByteArray(String key, byte[] value) {
		byteArrayMap.put(key, value);
	}

	public byte[] getByteArray(String key) {
		return byteArrayMap.get(key);
	}
	
	public void putIntArray(String key, int[] value) {
		intArrayMap.put(key, value);
	}

	public int[] getIntArray(String key) {
		return intArrayMap.get(key);
	}

	public void putLongArray(String key, long[] value) {
		longArrayMap.put(key, value);
	}

	public long[] getLongArray(String key) {
		return longArrayMap.get(key);
	}

	public void putFloatArray(String key, float[] value) {
		floatArrayMap.put(key, value);
	}

	public float[] getFloatArray(String key) {
		return floatArrayMap.get(key);
	}

	public void putDoubleArray(String key, double[] value) {
		doubleArrayMap.put(key, value);
	}

	public double[] getDoubleArray(String key) {
		return doubleArrayMap.get(key);
	}

	public void putStringArray(String key, String[] value) {
		stringArrayMap.put(key, value);
	}

	public String[] getStringArray(String key) {
		return stringArrayMap.get(key);
	}

	public void putSerializable(String key, Serializable value) {
		serializableMap.put(key, value);
	}

	public Serializable getSerializable(String key) {
		return serializableMap.get(key);
	}

	public byte[] getByteData() throws IOException {

		int blength = getByteArrayLength();

		// deal with Serializable Map
		Map<String, byte[]> serByteMap = new HashMap<String, byte[]>();
		blength += SerializableMapHelper.getByteArrayLength(serializableMap, serByteMap);

		byte[] byteArray = new byte[TransferUtil.getLengthOfInt() + blength];

		TransferOutputStream touts = new TransferOutputStream(byteArray);
		touts.writeInt(blength);
		touts.writeString(calleeClass);
		touts.writeString(calleeMethod);
		touts.writeByte(returnType);
		touts.writeBoolean(compress);

		BooleanMapHelper.getByteArray(booleanMap, touts);
		ByteMapHelper.getByteArray(byteMap, touts);
		ShortMapHelper.getByteArray(shortMap, touts);
		CharMapHelper.getByteArray(charMap, touts);
		IntMapHelper.getByteArray(intMap, touts);
		LongMapHelper.getByteArray(longMap, touts);
		FloatMapHelper.getByteArray(floatMap, touts);
		DoubleMapHelper.getByteArray(doubleMap, touts);
		DateMapHelper.getByteArray(dateMap, touts);
		StringMapHelper.getByteArray(stringMap, touts);
		WrapperMapHelper.getByteArray(wrapperMap, touts);

		ByteArrayMapHelper.getByteArray(byteArrayMap, touts);
		IntArrayMapHelper.getByteArray(intArrayMap, touts);
		LongArrayMapHelper.getByteArray(longArrayMap, touts);
		FloatArrayMapHelper.getByteArray(floatArrayMap, touts);
		DoubleArrayMapHelper.getByteArray(doubleArrayMap, touts);
		StringArrayMapHelper.getByteArray(stringArrayMap, touts);

		SerializableMapHelper.getByteArray(serByteMap, touts);

		return byteArray;
	}

	private int getByteArrayLength() {
		int blength = 0;

		// length of class, method, returnType and compress flag
		blength += TransferUtil.getLengthOfString(calleeClass);
		blength += TransferUtil.getLengthOfString(calleeMethod);
		blength += TransferUtil.getLengthOfByte();
		blength += TransferUtil.getLengthOfByte();

		// length of maps data
		blength += BooleanMapHelper.getByteArrayLength(booleanMap);
		blength += ByteMapHelper.getByteArrayLength(byteMap);
		blength += ShortMapHelper.getByteArrayLength(shortMap);
		blength += CharMapHelper.getByteArrayLength(charMap);
		blength += IntMapHelper.getByteArrayLength(intMap);
		blength += LongMapHelper.getByteArrayLength(longMap);
		blength += FloatMapHelper.getByteArrayLength(floatMap);
		blength += DoubleMapHelper.getByteArrayLength(doubleMap);
		blength += DateMapHelper.getByteArrayLength(dateMap);
		blength += StringMapHelper.getByteArrayLength(stringMap);
		blength += WrapperMapHelper.getByteArrayLength(wrapperMap);

		blength += ByteArrayMapHelper.getByteArrayLength(byteArrayMap);
		blength += IntArrayMapHelper.getByteArrayLength(intArrayMap);
		blength += LongArrayMapHelper.getByteArrayLength(longArrayMap);
		blength += FloatArrayMapHelper.getByteArrayLength(floatArrayMap);
		blength += DoubleArrayMapHelper.getByteArrayLength(doubleArrayMap);
		blength += StringArrayMapHelper.getByteArrayLength(stringArrayMap);

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
			String key = tins.readString();
			if (type == DATATYPE_BOOLEAN) {
				putBoolean(key, tins.readBoolean());
			} else if (type == DATATYPE_BYTE) {
				putByte(key, tins.readByte());
			} else if (type == DATATYPE_SHORT) {
				putShort(key, tins.readShort());
			} else if (type == DATATYPE_CHAR) {
				putChar(key, tins.readChar());
			} else if (type == DATATYPE_INT) {
				putInt(key, tins.readInt());
			} else if (type == DATATYPE_LONG) {
				putLong(key, tins.readLong());
			} else if (type == DATATYPE_FLOAT) {
				putFloat(key, tins.readFloat());
			} else if (type == DATATYPE_DOUBLE) {
				putDouble(key, tins.readDouble());
			} else if (type == DATATYPE_DATE) {
				putDate(key, tins.readDate());
			} else if (type == DATATYPE_STRING) {
				putString(key, tins.readString());
			} else if (type == DATATYPE_WRAPPER) {
				putWrapper(key, tins.readWrapper());
				
			} else if (type == DATATYPE_BYTEARRAY) {
				putByteArray(key, tins.readByteArray());
			} else if (type == DATATYPE_INTARRAY) {
				putIntArray(key, tins.readIntArray());
			} else if (type == DATATYPE_LONGARRAY) {
				putLongArray(key, tins.readLongArray());
			} else if (type == DATATYPE_FLOATARRAY) {
				putFloatArray(key, tins.readFloatArray());
			} else if (type == DATATYPE_DOUBLEARRAY) {
				putDoubleArray(key, tins.readDoubleArray());
			} else if (type == DATATYPE_STRINGARRAY) {
				putStringArray(key, tins.readStringArray());
			} else if (type == DATATYPE_SERIALIZABLE) {
				putSerializable(key, tins.readSerializable());
			}
		}

	}
}
