using System;
using System.Reflection;
using System.Text;
using QiLeYuan.Tatala.socket.to;
using QiLeYuan.Tatala.socket.util;
using QiLeYuan.Tools.debug;

/**
 * This class helps TransferObject convert byte array into 
 * primitive type data and user-defined object.
 * 
 * @author JimT
 */

namespace QiLeYuan.Tatala.socket.io {
    public class TransferInputStream {
        private byte[] buf;
        private int loc;

        public TransferInputStream(byte[] buf) {
            this.buf = buf;
            this.loc = 0;
        }

        public byte[] getBuf() {
            return buf;
        }
        public void setBuf(byte[] buf) {
            this.buf = buf;
        }

        public void readFully(byte[] b, int off, int len) {
            int n = 0;
            while (n < len) {
                int count = read(b, off + n, len - n);
                n += count;
            }
        }

        public int read(byte[] b, int off, int len) {
            Array.Copy(buf, loc, b, off, len);
            loc += len;
            return len;
        }

        public int read() {
            int val = buf[loc++] & 0xff;
            return val;
        }

        public byte readByte() {
            int ch = read();
            return (byte)(ch);
        }

        public byte[] readByteArray() {
            int len = readInt();
            if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
                return null;
            }

            byte[] byteArray = new byte[len];
            for (int i = 0; i < len; i++) {
                byteArray[i] = readByte();
            }

            return byteArray;
        }

        public bool readBoolean() {
            int ch = read();
            return (ch == TransferOutputStream.TRUEBYTE);
        }

        public short readShort() {
            int ch1 = read();
            int ch2 = read();
            return (short)((ch1 << 8) + (ch2 << 0));
        }

        public char readChar() {
            return (char)readShort();
        }

        public int readInt() {
            int ch1 = read();
            int ch2 = read();
            int ch3 = read();
            int ch4 = read();
            return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
        }

        public int[] readIntArray() {
            int len = readInt();
            if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
                return null;
            }

            int[] intArray = new int[len];
            for (int i = 0; i < len; i++) {
                intArray[i] = readInt();
            }

            return intArray;
        }

        public long readLong() {
            byte[] b = new byte[8];
            readFully(b, 0, 8);
            return (((long)b[0] << 56) + ((long)(b[1] & 255) << 48)
                    + ((long)(b[2] & 255) << 40) + ((long)(b[3] & 255) << 32)
                    + ((long)(b[4] & 255) << 24) + ((b[5] & 255) << 16)
                    + ((b[6] & 255) << 8) + ((b[7] & 255) << 0));
        }

        public long[] readLongArray() {
            int len = readInt();
            if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
                return null;
            }

            long[] longArray = new long[len];
            for (int i = 0; i < len; i++) {
                longArray[i] = readLong();
            }

            return longArray;
        }

        public float readFloat() {
            return BitConverter.ToSingle(BitConverter.GetBytes(readInt()), 0);
        }

        public float[] readFloatArray() {
            int len = readInt();
            if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
                return null;
            }

            float[] floatArray = new float[len];
            for (int i = 0; i < len; i++) {
                floatArray[i] = readFloat();
            }

            return floatArray;
        }

        public double readDouble() {
            return BitConverter.Int64BitsToDouble(readLong());
        }

        public double[] readDoubleArray() {
            int len = readInt();
            if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
                return null;
            }

            double[] doubleArray = new double[len];
            for (int i = 0; i < len; i++) {
                doubleArray[i] = readDouble();
            }

            return doubleArray;
        }

        public DateTime readDate() {
            //need convert UTC long time value to local time base on 1970/01/01 00:00:00 GMT/UTC for Java
            DateTime javaUtc = new DateTime(1970, 1, 1);
            return new DateTime(readLong() * 10000 + javaUtc.Ticks).ToLocalTime();
        }

        public String readString() {
            int len = readInt();
            if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
                return null;
            }

            byte[] bytearr = new byte[len];
            readFully(bytearr, 0, len);
            String str = null;
            try {
                str = Encoding.UTF8.GetString(bytearr);
            } catch (Exception e) {
                Logging.LogError(e.ToString());
            }
            return str;
        }

        public String[] readStringArray() {
            int len = readInt();
            if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
                return null;
            }

            String[] strArray = new String[len];
            for (int i = 0; i < len; i++) {
                strArray[i] = readString();
            }

            return strArray;
        }

        public TransferObjectWrapper readWrapper() {
            int len = readInt();
            if (len == TransferOutputStream.NULL_PLACE_HOLDER) {
                return null;
            }

            String wrapperClassName = readString();
            len -= TransferUtil.getLengthOfString(wrapperClassName);
            byte[] bytearr = new byte[len];
            readFully(bytearr, 0, len);

            Object retobj = null;
            try {
                Type type = Type.GetType(wrapperClassName);
                Object instance = Activator.CreateInstance(type);
                TransferInputStream tins = new TransferInputStream(bytearr);
                MethodInfo method = type.GetMethod("getObjectWrapper");
                retobj = method.Invoke(instance, new object[] { tins });
            } catch (Exception e) {
                Logging.LogError(e.ToString());
            }

            return (TransferObjectWrapper)retobj;
        }

        public bool isFinished() {
            return loc >= buf.Length;
        }
    }
}