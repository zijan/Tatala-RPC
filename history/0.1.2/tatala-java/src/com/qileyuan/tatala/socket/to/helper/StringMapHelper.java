package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class StringMapHelper {
	public static int getByteArrayLength(Map<String, String> stringMap){
		int blength = 0;
		Iterator<String> keyIt = stringMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			String value = stringMap.get(key);
			blength += TransferUtil.getLengthOfString(value);
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, String> stringMap, TransferOutputStream touts) {
		Iterator<String> keyIt = stringMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_STRING);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			String value = stringMap.get(key);
			touts.writeString(value);
		}
	}
}
