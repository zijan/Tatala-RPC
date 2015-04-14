package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class CharMapHelper {
	public static int getByteArrayLength(Map<String, Character> charMap){
		int blength = 0;
		Iterator<String> keyIt = charMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			blength += TransferUtil.getLengthOfChar();
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, Character> charMap, TransferOutputStream touts) {
		Iterator<String> keyIt = charMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_CHAR);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			char value = charMap.get(key);
			touts.writeChar(value);
		}
	}
}
