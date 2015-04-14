using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class StringMapHelper {
        public static int getByteArrayLength(Dictionary<String, String> stringMap) {
            int blength = 0;
            foreach (KeyValuePair<String, String> item in stringMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                String value = item.Value;
                blength += TransferUtil.getLengthOfString(value);
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, String> stringMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, String> item in stringMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_STRING);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                String value = item.Value;
                touts.writeString(value);
            }
        }
    }
}