package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class IntMapHelper {

	public static int getByteArrayLength(Map<String, Integer> intMap){
		int blength = 0;
		Iterator<String> keyIt = intMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			blength += TransferUtil.getLengthOfInt();
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, Integer> intMap, TransferOutputStream touts) {
		Iterator<String> keyIt = intMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_INT);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			int value = intMap.get(key);
			touts.writeInt(value);
		}
	}
}
