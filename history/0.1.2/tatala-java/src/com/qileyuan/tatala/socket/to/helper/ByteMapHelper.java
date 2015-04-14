package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class ByteMapHelper {
	public static int getByteArrayLength(Map<String, Byte> byteMap){
		int blength = 0;
		Iterator<String> keyIt = byteMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			blength += TransferUtil.getLengthOfByte();
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, Byte> byteMap, TransferOutputStream touts) {
		Iterator<String> keyIt = byteMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_BYTE);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			byte value = byteMap.get(key);
			touts.writeByte(value);
		}
	}
}
