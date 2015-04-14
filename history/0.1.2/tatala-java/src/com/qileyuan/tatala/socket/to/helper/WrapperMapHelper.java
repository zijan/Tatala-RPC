package com.qileyuan.tatala.socket.to.helper;

import java.util.Iterator;
import java.util.Map;

import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.to.TransferObjectWrapper;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class WrapperMapHelper {
	public static int getByteArrayLength(Map<String, TransferObjectWrapper> wrapperMap){
		int blength = 0;
		Iterator<String> keyIt = wrapperMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			blength += TransferUtil.getLengthOfByte();
			// key
			String key = (String) keyIt.next();
			blength += TransferUtil.getLengthOfString(key);
			// value
			TransferObjectWrapper value = wrapperMap.get(key);
			blength += TransferUtil.getLengthOfWrapper(value);
		}
		return blength;
	}
	
	public static void getByteArray(Map<String, TransferObjectWrapper> wrapperMap, TransferOutputStream touts) {
		Iterator<String> keyIt = wrapperMap.keySet().iterator();
		while (keyIt.hasNext()) {
			// type
			touts.writeByte(TransferObject.DATATYPE_WRAPPER);
			// key
			String key = (String) keyIt.next();
			touts.writeString(key);
			// value
			TransferObjectWrapper value = wrapperMap.get(key);
			touts.writeWrapper(value);
		}
	}
}
