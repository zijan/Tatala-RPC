package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class DoubleArrayMapHelper {

	public static int getByteArrayLength(Map<String, double[]> doubleArrayMap){
		int blength = 0;
		Iterator<String> keyIt = doubleArrayMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			double[] values = doubleArrayMap.get(key);
			blength += TransferUtil.getLengthOfDoubleArray(values);
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, double[]> doubleArrayMap, TransferOutputStream touts) {
		Iterator<String> keyIt = doubleArrayMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_DOUBLEARRAY);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			double[] values = doubleArrayMap.get(key);
			touts.writeDoubleArray(values);
		}
	}
}
