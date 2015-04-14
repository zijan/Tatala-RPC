using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class IntMapHelper {
        public static int getByteArrayLength(Dictionary<String, Int32> intMap) {
            int blength = 0;
            foreach (KeyValuePair<String, Int32> item in intMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                blength += TransferUtil.getLengthOfInt();
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, Int32> intMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, Int32> item in intMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_INT);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                int value = item.Value;
                touts.writeInt(value);
            }
        }
    }
}