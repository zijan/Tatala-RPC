package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class ShortMapHelper {
	public static int getByteArrayLength(Map<String, Short> shortMap){
		int blength = 0;
		Iterator<String> keyIt = shortMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			blength += TransferUtil.getLengthOfShort();
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, Short> shortMap, TransferOutputStream touts) {
		Iterator<String> keyIt = shortMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_SHORT);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			short value = shortMap.get(key);
			touts.writeShort(value);
		}
	}
}
