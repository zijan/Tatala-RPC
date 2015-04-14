using System;
using System.Text;
using QiLeYuan.Tatala.socket.to;
using QiLeYuan.Tatala.socket.util;
using QiLeYuan.Tools.debug;

/**
 * This class helps TransferObject convert primitive type data and 
 * user-defined object into byte array.
 * 
 * @author JimT
 */

namespace QiLeYuan.Tatala.socket.io {
    public class TransferOutputStream {

        public const int NULL_PLACE_HOLDER = -99;
        public const byte TRUEBYTE = 1;
        public const byte FALSEBYTE = 0;

        private byte[] buf;
        private int loc;

        public TransferOutputStream(byte[] buf) {
            this.buf = buf;
            this.loc = 0;
        }

        public byte[] getBuf() {
            return buf;
        }

        public void setBuf(byte[] buf) {
            this.buf = buf;
        }

        public void write(byte b) {
            buf[loc++] = b;
        }

        public void writeByte(byte b) {
            write(b);
        }

        public void writeByteArray(byte[] v) {
            if (v == null) {
                writeInt(NULL_PLACE_HOLDER);
                return;
            }

            writeInt(v.Length);
            foreach (byte oneValue in v) {
                writeByte(oneValue);
            }
        }

        public void write(byte[] b, int off, int len) {
            int end = off + len;
            for (int i = off; i < end; i++)
                buf[loc++] = b[i];
        }

        public void writeBoolean(bool v) {
            if (v) {
                write(TRUEBYTE);
            } else {
                write(FALSEBYTE);
            }
        }

        public void writeShort(int v) {
            write((byte)((v >> 8) & 0xFF));
            write((byte)((v >> 0) & 0xFF));
        }

        public void writeChar(int v) {
            writeShort(v);
        }

        public void writeInt(int v) {
            write((byte)((v >> 24) & 0xFF));
            write((byte)((v >> 16) & 0xFF));
            write((byte)((v >> 8) & 0xFF));
            write((byte)((v >> 0) & 0xFF));
        }

        public void writeIntArray(int[] v) {
            if (v == null) {
                writeInt(NULL_PLACE_HOLDER);
                return;
            }

            writeInt(v.Length);
            foreach (int oneValue in v) {
                writeInt(oneValue);
            }
        }

        public void writeLong(long v) {
            write((byte)(v >> 56));
            write((byte)(v >> 48));
            write((byte)(v >> 40));
            write((byte)(v >> 32));
            write((byte)(v >> 24));
            write((byte)(v >> 16));
            write((byte)(v >> 8));
            write((byte)(v >> 0));
        }

        public void writeLongArray(long[] v) {
            if (v == null) {
                writeInt(NULL_PLACE_HOLDER);
                return;
            }

            writeInt(v.Length);
            foreach (long oneValue in v) {
                writeLong(oneValue);
            }
        }

        public void writeFloat(float v) {
            writeInt(BitConverter.ToInt32(BitConverter.GetBytes(v), 0));
        }

        public void writeFloatArray(float[] v) {
            if (v == null) {
                writeInt(NULL_PLACE_HOLDER);
                return;
            }

            writeInt(v.Length);
            foreach (float oneValue in v) {
                writeFloat(oneValue);
            }
        }

        public void writeDouble(double v) {
            writeLong(BitConverter.DoubleToInt64Bits(v));
        }

        public void writeDoubleArray(double[] v) {
            if (v == null) {
                writeInt(NULL_PLACE_HOLDER);
                return;
            }

            writeInt(v.Length);
            foreach (double oneValue in v) {
                writeDouble(oneValue);
            }
        }

        public void writeDate(DateTime date) {
            //need convert local time to UTC long time value base on 1970/01/01 00:00:00 GMT/UTC for Java
            DateTime nowUtc = date.ToUniversalTime();
            DateTime javaUtc = new DateTime(1970, 1, 1);
            long javatimelong = (nowUtc.Ticks - javaUtc.Ticks) / 10000;
            writeLong(javatimelong);
        }

        public void writeString(String s) {
            if (s == null) {
                writeInt(NULL_PLACE_HOLDER);
                return;
            }

            byte[] b = null;
            try {
                b = Encoding.UTF8.GetBytes(s);
            } catch (Exception e) {
                Logging.LogError(e.ToString());
            }
            int len = b.Length;
            writeInt(len);
            write(b, 0, len);
        }

        public void writeStringArray(String[] strs) {
            if (strs == null) {
                writeInt(NULL_PLACE_HOLDER);
                return;
            }

            writeInt(strs.Length);
            foreach (String s in strs) {
                writeString(s);
            }
        }

        public void writeWrapper(TransferObjectWrapper v) {

            if (v == null) {
                writeNull();
                return;
            }

            int blength = TransferUtil.getLengthOfString(v.GetType().FullName) + v.getLength();
            byte[] byteArray = new byte[TransferUtil.getLengthOfInt() + blength];

            TransferOutputStream touts = new TransferOutputStream(byteArray);
            touts.writeInt(blength);
            touts.writeString(v.GetType().FullName);

            v.getByteArray(touts);

            write(byteArray, 0, byteArray.Length);
        }

        public void writeNull() {
            writeInt(NULL_PLACE_HOLDER);
        }

        public void skipAByte() {
            this.loc++;
        }
    }
}