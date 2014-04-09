using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class WrapperMapHelper {
        public static int getByteArrayLength(Dictionary<String, TransferObjectWrapper> wrapperMap) {
            int blength = 0;
            foreach (KeyValuePair<String, TransferObjectWrapper> item in wrapperMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                TransferObjectWrapper value = item.Value;
                blength += TransferUtil.getLengthOfWrapper(value);
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, TransferObjectWrapper> wrapperMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, TransferObjectWrapper> item in wrapperMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_WRAPPER);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                TransferObjectWrapper value = item.Value;
                touts.writeWrapper(value);
            }
        }
    }
}