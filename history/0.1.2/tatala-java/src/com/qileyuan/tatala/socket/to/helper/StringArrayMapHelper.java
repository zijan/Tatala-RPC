package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class StringArrayMapHelper {
	
	public static int getByteArrayLength(Map<String, String[]> stringArrayMap){
		int blength = 0;
		Iterator<String> keyIt = stringArrayMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			String[] values = stringArrayMap.get(key);
			blength += TransferUtil.getLengthOfStringArray(values);
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, String[]> stringArrayMap, TransferOutputStream touts) {
		Iterator<String> keyIt = stringArrayMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_STRINGARRAY);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			String[] values = stringArrayMap.get(key);
			touts.writeStringArray(values);
		}
	}
}
