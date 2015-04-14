using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to.helper {

    public class DateMapHelper {
        public static int getByteArrayLength(Dictionary<String, DateTime> dateMap) {
            int blength = 0;
            foreach (KeyValuePair<String, DateTime> item in dateMap) {
                // type
                blength += TransferUtil.getLengthOfByte();
                // key
                String key = item.Key;
                blength += TransferUtil.getLengthOfString(key);
                // value
                blength += TransferUtil.getLengthOfDate();
            }
            return blength;
        }

        public static void getByteArray(Dictionary<String, DateTime> dateMap, TransferOutputStream touts) {
            foreach (KeyValuePair<String, DateTime> item in dateMap) {
                // type
                touts.writeByte(TransferObject.DATATYPE_DATE);
                // key
                String key = item.Key;
                touts.writeString(key);
                // value
                DateTime value = item.Value;
                touts.writeDate(value);
            }
        }
    }
}