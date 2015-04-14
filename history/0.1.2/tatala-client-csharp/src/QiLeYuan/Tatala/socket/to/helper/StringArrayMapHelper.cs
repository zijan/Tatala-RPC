using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class StringArrayMapHelper {
        public static int getByteArrayLength(Dictionary<String, String[]> stringArrayMap) {
            int blength = 0;
            foreach (KeyValuePair<String, String[]> item in stringArrayMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                String[] values = item.Value;
                blength += TransferUtil.getLengthOfStringArray(values);
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, String[]> stringArrayMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, String[]> item in stringArrayMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_STRINGARRAY);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                String[] values = item.Value;
                touts.writeStringArray(values);
            }
        }
    }
}