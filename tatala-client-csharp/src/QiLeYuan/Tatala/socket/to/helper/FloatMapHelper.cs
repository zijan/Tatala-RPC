using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class FloatMapHelper {
        public static int getByteArrayLength(Dictionary<String, Single> floatMap) {
            int blength = 0;
            foreach (KeyValuePair<String, Single> item in floatMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                blength += TransferUtil.getLengthOfFloat();
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, Single> floatMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, Single> item in floatMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_FLOAT);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                float value = item.Value;
                touts.writeFloat(value);
            }
        }
    }
}