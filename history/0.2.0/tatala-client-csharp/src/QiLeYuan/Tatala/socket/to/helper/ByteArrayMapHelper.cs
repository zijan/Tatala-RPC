using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.util;
using QiLeYuan.Tatala.socket.io;

namespace QiLeYuan.Tatala.socket.to.helper {
    public class ByteArrayMapHelper {
        public static int getByteArrayLength(Dictionary<String, byte[]> byteArrayMap) {
            int blength = 0;
            foreach (KeyValuePair<String, byte[]> item in byteArrayMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                byte[] values = item.Value;
                blength += TransferUtil.getLengthOfByteArray(values);
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, byte[]> byteArrayMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, byte[]> item in byteArrayMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_BYTEARRAY);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                byte[] values = item.Value;
                touts.writeByteArray(values);
            }
        }
    }
}
