using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class CharMapHelper {
        public static int getByteArrayLength(Dictionary<String, Char> charMap) {
            int blength = 0;
            foreach (KeyValuePair<String, Char> item in charMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                blength += TransferUtil.getLengthOfChar();
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, Char> charMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, Char> item in charMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_CHAR);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                char value = item.Value;
                touts.writeChar(value);
            }
        }
    }
}
