using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class ShortMapHelper {
        public static int getByteArrayLength(Dictionary<String, Int16> shortMap) {
            int blength = 0;
            foreach (KeyValuePair<String, Int16> item in shortMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                blength += TransferUtil.getLengthOfShort();
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, Int16> shortMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, Int16> item in shortMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_SHORT);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                short value = item.Value;
                touts.writeShort(value);
            }
        }
    }
}