package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class IntArrayMapHelper {

	public static int getByteArrayLength(Map<String, int[]> intArrayMap){
		int blength = 0;
		Iterator<String> keyIt = intArrayMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			int[] values = intArrayMap.get(key);
			blength += TransferUtil.getLengthOfIntArray(values);
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, int[]> intArrayMap, TransferOutputStream touts) {
		Iterator<String> keyIt = intArrayMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_INTARRAY);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			int[] values = intArrayMap.get(key);
			touts.writeIntArray(values);
		}
	}
}
