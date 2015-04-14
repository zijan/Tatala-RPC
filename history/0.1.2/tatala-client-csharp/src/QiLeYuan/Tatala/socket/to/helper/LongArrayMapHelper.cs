using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class LongArrayMapHelper {
        public static int getByteArrayLength(Dictionary<String, long[]> longArrayMap) {
            int blength = 0;
            foreach (KeyValuePair<String, long[]> item in longArrayMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                long[] values = item.Value;
                blength += TransferUtil.getLengthOfLongArray(values);
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, long[]> longArrayMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, long[]> item in longArrayMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_LONGARRAY);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                long[] values = item.Value;
                touts.writeLongArray(values);
            }
        }
    }
}