using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class BooleanMapHelper {
        public static int getByteArrayLength(Dictionary<String, Boolean> booleanMap) {
            int blength = 0;
            foreach (KeyValuePair<String, Boolean> item in booleanMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                blength += TransferUtil.getLengthOfBoolean();
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, Boolean> booleanMap, TransferOutputStream touts) {

            foreach (KeyValuePair<String, Boolean> item in booleanMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_BOOLEAN);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                bool value = item.Value;
                touts.writeBoolean(value);
            }
        }
    }

}