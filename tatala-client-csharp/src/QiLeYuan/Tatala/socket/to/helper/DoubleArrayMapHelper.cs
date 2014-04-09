using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class DoubleArrayMapHelper {
        public static int getByteArrayLength(Dictionary<String, double[]> doubleArrayMap) {
            int blength = 0;
            foreach (KeyValuePair<String, double[]> item in doubleArrayMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                double[] values = item.Value;
                blength += TransferUtil.getLengthOfDoubleArray(values);
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, double[]> doubleArrayMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, double[]> item in doubleArrayMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_DOUBLEARRAY);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                double[] values = item.Value;
                touts.writeDoubleArray(values);
            }
        }
    }
}
