package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class BooleanMapHelper {
	public static int getByteArrayLength(Map<String, Boolean> booleanMap){
		int blength = 0;
		Iterator<String> keyIt = booleanMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			blength += TransferUtil.getLengthOfBoolean();
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, Boolean> booleanMap, TransferOutputStream touts) {
		Iterator<String> keyIt = booleanMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_BOOLEAN);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			boolean value = booleanMap.get(key);
			touts.writeBoolean(value);
		}
	}
}
