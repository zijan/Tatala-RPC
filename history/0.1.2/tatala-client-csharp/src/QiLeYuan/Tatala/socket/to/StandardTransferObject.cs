using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.to.helper;
using QiLeYuan.Tatala.socket.util;
using QiLeYuan.Tatala.socket.io;

namespace QiLeYuan.Tatala.socket.to {
    public class StandardTransferObject : TransferObject{

        private Dictionary<String, Boolean> booleanMap = new Dictionary<String, Boolean>();
        private Dictionary<String, Byte> byteMap = new Dictionary<String, Byte>();
        private Dictionary<String, Int16> shortMap = new Dictionary<String, Int16>();
        private Dictionary<String, Char> charMap = new Dictionary<String, Char>();
        private Dictionary<String, Int32> intMap = new Dictionary<String, Int32>();
        private Dictionary<String, Int64> longMap = new Dictionary<String, Int64>();
        private Dictionary<String, Single> floatMap = new Dictionary<String, Single>();
        private Dictionary<String, Double> doubleMap = new Dictionary<String, Double>();
        private Dictionary<String, DateTime> dateMap = new Dictionary<String, DateTime>();
        private Dictionary<String, String> stringMap = new Dictionary<String, String>();
        private Dictionary<String, TransferObjectWrapper> wrapperMap = new Dictionary<String, TransferObjectWrapper>();

        private Dictionary<String, byte[]> byteArrayMap = new Dictionary<String, byte[]>();
        private Dictionary<String, int[]> intArrayMap = new Dictionary<String, int[]>();
        private Dictionary<String, long[]> longArrayMap = new Dictionary<String, long[]>();
        private Dictionary<String, float[]> floatArrayMap = new Dictionary<String, float[]>();
        private Dictionary<String, double[]> doubleArrayMap = new Dictionary<String, double[]>();
        private Dictionary<String, String[]> stringArrayMap = new Dictionary<String, String[]>();

        public void putBoolean(String key, bool value) {
            booleanMap.Add(key, value);
        }
        public bool getBoolean(String key) {
            return booleanMap[key];
        }

        public void putByte(String key, byte value) {
            byteMap.Add(key, value);
        }
        public byte getByte(String key) {
            return byteMap[key];
        }

        public void putShort(String key, short value) {
            shortMap.Add(key, value);
        }
        public short getShort(String key) {
            return shortMap[key];
        }

        public void putChar(String key, char value) {
            charMap.Add(key, value);
        }
        public char getChar(String key) {
            return charMap[key];
        }

        public void putInt(String key, int value) {
            intMap.Add(key, value);
        }
        public int getInt(String key) {
            return intMap[key];
        }

        public void putLong(String key, long value) {
            longMap.Add(key, value);
        }
        public long getLong(String key) {
            return longMap[key];
        }

        public void putFloat(String key, float value) {
            floatMap.Add(key, value);
        }
        public float getFloat(String key) {
            return floatMap[key];
        }

        public void putDouble(String key, double value) {
            doubleMap.Add(key, value);
        }
        public double getDouble(String key) {
            return doubleMap[key];
        }

        public void putDate(String key, DateTime value) {
            dateMap.Add(key, value);
        }
        public DateTime getDate(String key) {
            return dateMap[key];
        }

        public void putString(String key, String value) {
            stringMap.Add(key, value);
        }
        public String getString(String key) {
            return stringMap[key];
        }

        public void putWrapper(String key, TransferObjectWrapper value) {
            wrapperMap.Add(key, value);
        }
        public TransferObjectWrapper getWrapper(String key) {
            return wrapperMap[key];
        }

        public void putByteArray(String key, byte[] value) {
            byteArrayMap.Add(key, value);
        }
        public byte[] getByteArray(String key) {
            return byteArrayMap[key];
        }

        public void putIntArray(String key, int[] value) {
            intArrayMap.Add(key, value);
        }
        public int[] getIntArray(String key) {
            return intArrayMap[key];
        }

        public void putLongArray(String key, long[] value) {
            longArrayMap.Add(key, value);
        }
        public long[] getLongArray(String key) {
            return longArrayMap[key];
        }

        public void putFloatArray(String key, float[] value) {
            floatArrayMap.Add(key, value);
        }
        public float[] getFloatArray(String key) {
            return floatArrayMap[key];
        }

        public void putDoubleArray(String key, double[] value) {
            doubleArrayMap.Add(key, value);
        }
        public double[] getDoubleArray(String key) {
            return doubleArrayMap[key];
        }

        public void putStringArray(String key, String[] value) {
            stringArrayMap.Add(key, value);
        }
        public String[] getStringArray(String key) {
            return stringArrayMap[key];
        }

        public override byte[] getByteData() {

            int blength = getByteArrayLength();

            byte[] byteArray = new byte[TransferUtil.getLengthOfInt() + blength];

            TransferOutputStream touts = new TransferOutputStream(byteArray);
            touts.writeInt(blength);
            touts.writeString(calleeClass);
            touts.writeString(calleeMethod);
            touts.writeByte(returnType);
            touts.writeBoolean(compress);

            BooleanMapHelper.getByteArray(booleanMap, touts);
            ByteMapHelper.getByteArray(byteMap, touts);
            ShortMapHelper.getByteArray(shortMap, touts);
            CharMapHelper.getByteArray(charMap, touts);
            IntMapHelper.getByteArray(intMap, touts);
            LongMapHelper.getByteArray(longMap, touts);
            FloatMapHelper.getByteArray(floatMap, touts);
            DoubleMapHelper.getByteArray(doubleMap, touts);
            DateMapHelper.getByteArray(dateMap, touts);
            StringMapHelper.getByteArray(stringMap, touts);
            WrapperMapHelper.getByteArray(wrapperMap, touts);

            ByteArrayMapHelper.getByteArray(byteArrayMap, touts);
            IntArrayMapHelper.getByteArray(intArrayMap, touts);
            LongArrayMapHelper.getByteArray(longArrayMap, touts);
            FloatArrayMapHelper.getByteArray(floatArrayMap, touts);
            DoubleArrayMapHelper.getByteArray(doubleArrayMap, touts);
            StringArrayMapHelper.getByteArray(stringArrayMap, touts);

            return byteArray;
        }

        private int getByteArrayLength() {
            int blength = 0;

            // length of class, method, returnType and compress flag
            blength += TransferUtil.getLengthOfString(calleeClass);
            blength += TransferUtil.getLengthOfString(calleeMethod);
            blength += TransferUtil.getLengthOfByte();
            blength += TransferUtil.getLengthOfByte();

            // length of maps data
            blength += BooleanMapHelper.getByteArrayLength(booleanMap);
            blength += ByteMapHelper.getByteArrayLength(byteMap);
            blength += ShortMapHelper.getByteArrayLength(shortMap);
            blength += CharMapHelper.getByteArrayLength(charMap);
            blength += IntMapHelper.getByteArrayLength(intMap);
            blength += LongMapHelper.getByteArrayLength(longMap);
            blength += FloatMapHelper.getByteArrayLength(floatMap);
            blength += DoubleMapHelper.getByteArrayLength(doubleMap);
            blength += DateMapHelper.getByteArrayLength(dateMap);
            blength += StringMapHelper.getByteArrayLength(stringMap);
            blength += WrapperMapHelper.getByteArrayLength(wrapperMap);

            blength += ByteArrayMapHelper.getByteArrayLength(byteArrayMap);
            blength += IntArrayMapHelper.getByteArrayLength(intArrayMap);
            blength += LongArrayMapHelper.getByteArrayLength(longArrayMap);
            blength += FloatArrayMapHelper.getByteArrayLength(floatArrayMap);
            blength += DoubleArrayMapHelper.getByteArrayLength(doubleArrayMap);
            blength += StringArrayMapHelper.getByteArrayLength(stringArrayMap);

            return blength;
        }

        //server call
        public override void setByteData(byte[] buf) {

            TransferInputStream tins = new TransferInputStream(buf);
            this.calleeClass = tins.readString();
            this.calleeMethod = tins.readString();
            this.returnType = tins.readByte();
            this.compress = tins.readBoolean();

            while (!tins.isFinished()) {
                byte type = tins.readByte();
                String key = tins.readString();
                if (type == DATATYPE_BOOLEAN) {
                    putBoolean(key, tins.readBoolean());
                } else if (type == DATATYPE_BYTE) {
                    putByte(key, tins.readByte());
                } else if (type == DATATYPE_SHORT) {
                    putShort(key, tins.readShort());
                } else if (type == DATATYPE_CHAR) {
                    putChar(key, tins.readChar());
                } else if (type == DATATYPE_INT) {
                    putInt(key, tins.readInt());
                } else if (type == DATATYPE_LONG) {
                    putLong(key, tins.readLong());
                } else if (type == DATATYPE_FLOAT) {
                    putFloat(key, tins.readFloat());
                } else if (type == DATATYPE_DOUBLE) {
                    putDouble(key, tins.readDouble());
                } else if (type == DATATYPE_DATE) {
                    putDate(key, tins.readDate());
                } else if (type == DATATYPE_STRING) {
                    putString(key, tins.readString());
                } else if (type == DATATYPE_WRAPPER) {
                    putWrapper(key, tins.readWrapper());

                } else if (type == DATATYPE_BYTEARRAY) {
                    putByteArray(key, tins.readByteArray());
                } else if (type == DATATYPE_INTARRAY) {
                    putIntArray(key, tins.readIntArray());
                } else if (type == DATATYPE_LONGARRAY) {
                    putLongArray(key, tins.readLongArray());
                } else if (type == DATATYPE_FLOATARRAY) {
                    putFloatArray(key, tins.readFloatArray());
                } else if (type == DATATYPE_DOUBLEARRAY) {
                    putDoubleArray(key, tins.readDoubleArray());
                } else if (type == DATATYPE_STRINGARRAY) {
                    putStringArray(key, tins.readStringArray());
                }
            }
        }
        
    }
}
