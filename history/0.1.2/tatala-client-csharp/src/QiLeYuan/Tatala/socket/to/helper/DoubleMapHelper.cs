using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.util;
using QiLeYuan.Tatala.socket.io;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class DoubleMapHelper {
        public static int getByteArrayLength(Dictionary<String, Double> doubleMap) {
            int blength = 0;
            foreach (KeyValuePair<String, Double> item in doubleMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                blength += TransferUtil.getLengthOfDouble();
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, Double> doubleMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, Double> item in doubleMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_DOUBLE);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                double value = item.Value;
                touts.writeDouble(value);
            }
        }
    }
}