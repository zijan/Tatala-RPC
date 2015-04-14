package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class FloatArrayMapHelper {
	
	public static int getByteArrayLength(Map<String, float[]> floatArrayMap){
		int blength = 0;
		Iterator<String> keyIt = floatArrayMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			float[] values = floatArrayMap.get(key);
			blength += TransferUtil.getLengthOfFloatArray(values);
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, float[]> floatArrayMap, TransferOutputStream touts) {
		Iterator<String> keyIt = floatArrayMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_FLOATARRAY);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			float[] values = floatArrayMap.get(key);
			touts.writeFloatArray(values);
		}
	}
}
