package com.qileyuan.tatala.socket.to.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class SerializableMapHelper {
	
	public static int getByteArrayLength(Map<String, Serializable> serializableMap, Map<String, byte[]> serByteMap) throws IOException{
		int blength = 0;
		Iterator<String> keyIt = serializableMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			Serializable value = serializableMap.get(key);
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			(new ObjectOutputStream(bos)).writeObject(value);
			byte[] bytearr = bos.toByteArray();
			blength += TransferUtil.getLengthOfInt();
			blength += bytearr.length;
			serByteMap.put(key, bytearr);
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, byte[]> serByteMap, TransferOutputStream touts) {
		Iterator<String> keyIt = serByteMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_SERIALIZABLE);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			byte[] bytearr = serByteMap.get(key);
			touts.writeInt(bytearr.length);
			touts.write(bytearr, 0, bytearr.length);
		}
	}
}
