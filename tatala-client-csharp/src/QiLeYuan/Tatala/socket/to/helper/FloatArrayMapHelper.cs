using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class FloatArrayMapHelper {
        public static int getByteArrayLength(Dictionary<String, float[]> floatArrayMap) {
            int blength = 0;
            foreach (KeyValuePair<String, float[]> item in floatArrayMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                float[] values = item.Value;
                blength += TransferUtil.getLengthOfFloatArray(values);
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, float[]> floatArrayMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, float[]> item in floatArrayMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_FLOATARRAY);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                float[] values = item.Value;
                touts.writeFloatArray(values);
            }
        }
    }
}
