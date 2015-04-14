package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class LongArrayMapHelper {

	public static int getByteArrayLength(Map<String, long[]> longArrayMap){
		int blength = 0;
		Iterator<String> keyIt = longArrayMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			long[] values = longArrayMap.get(key);
			blength += TransferUtil.getLengthOfLongArray(values);
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, long[]> longArrayMap, TransferOutputStream touts) {
		Iterator<String> keyIt = longArrayMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_LONGARRAY);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			long[] values = longArrayMap.get(key);
			touts.writeLongArray(values);
		}
	}
}
