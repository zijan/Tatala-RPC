using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class IntArrayMapHelper {
        public static int getByteArrayLength(Dictionary<String, int[]> intArrayMap) {
            int blength = 0;
            foreach (KeyValuePair<String, int[]> item in intArrayMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                int[] values = item.Value;
                blength += TransferUtil.getLengthOfIntArray(values);
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, int[]> intArrayMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, int[]> item in intArrayMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_INTARRAY);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                int[] values = item.Value;
                touts.writeIntArray(values);
            }
        }
    }
}