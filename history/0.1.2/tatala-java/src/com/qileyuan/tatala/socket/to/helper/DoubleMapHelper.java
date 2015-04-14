package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class DoubleMapHelper {
	public static int getByteArrayLength(Map<String, Double> doubleMap){
		int blength = 0;
		Iterator<String> keyIt = doubleMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			blength += TransferUtil.getLengthOfDouble();
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, Double> doubleMap, TransferOutputStream touts) {
		Iterator<String> keyIt = doubleMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_DOUBLE);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			double value = doubleMap.get(key);
			touts.writeDouble(value);
		}
	}
}
