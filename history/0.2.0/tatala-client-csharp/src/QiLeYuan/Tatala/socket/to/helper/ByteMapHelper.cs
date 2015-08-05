using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class ByteMapHelper {
        public static int getByteArrayLength(Dictionary<String, Byte> byteMap) {
            int blength = 0;
            foreach (KeyValuePair<String, Byte> item in byteMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                blength += TransferUtil.getLengthOfByte();
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, Byte> byteMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, Byte> item in byteMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_BYTE);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                byte value = item.Value;
                touts.writeByte(value);
            }
        }
    }
}