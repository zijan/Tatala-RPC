package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class LongMapHelper {
	public static int getByteArrayLength(Map<String, Long> longMap){
		int blength = 0;
		Iterator<String> keyIt = longMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			blength += TransferUtil.getLengthOfLong();
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, Long> longMap, TransferOutputStream touts) {
		Iterator<String> keyIt = longMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_LONG);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			long value = longMap.get(key);
			touts.writeLong(value);
		}
	}
}
