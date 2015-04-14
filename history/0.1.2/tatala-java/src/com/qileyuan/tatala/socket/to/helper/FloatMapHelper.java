package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class FloatMapHelper {
	public static int getByteArrayLength(Map<String, Float> floatMap){
		int blength = 0;
		Iterator<String> keyIt = floatMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			blength += TransferUtil.getLengthOfFloat();
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, Float> floatMap, TransferOutputStream touts) {
		Iterator<String> keyIt = floatMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_FLOAT);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			float value = floatMap.get(key);
			touts.writeFloat(value);
		}
	}
}
