package com.qileyuan.tatala.socket.to.helper;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class DateMapHelper {
	public static int getByteArrayLength(Map<String, Date> dateMap){
		int blength = 0;
		Iterator<String> keyIt = dateMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			blength += TransferUtil.getLengthOfDate();
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, Date> dateMap, TransferOutputStream touts) {
		Iterator<String> keyIt = dateMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_DATE);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			Date value = dateMap.get(key);
			touts.writeDate(value);
		}
	}
}
