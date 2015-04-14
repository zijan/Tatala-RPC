using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class LongMapHelper {
        public static int getByteArrayLength(Dictionary<String, Int64> longMap) {
            int blength = 0;
            foreach (KeyValuePair<String, Int64> item in longMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                blength += TransferUtil.getLengthOfLong();
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, Int64> longMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, Int64> item in longMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_LONG);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                long value = item.Value;
                touts.writeLong(value);
            }
        }
    }
}