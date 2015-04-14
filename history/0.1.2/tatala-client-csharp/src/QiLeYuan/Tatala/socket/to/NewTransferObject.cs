using System;
using System.Collections.Generic;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to {
    public class NewTransferObject : TransferObject {

        class ValueObject {
            public byte dataType;
            public Object dataObject;

            public ValueObject(byte dataType, Object dataObject) {
                this.dataType = dataType;
                this.dataObject = dataObject;
            }
        }

        private Queue<ValueObject> paramList = new Queue<ValueObject>();

        public NewTransferObject() {
            newVersion = true;
        }

        /** Parameter Map **/
        public Object peek() {
            if (paramList.Count > 0) {
                ValueObject vo = paramList.Peek();
                return vo.dataObject;
            }
            return null;
        }

        public void putBoolean(bool value) {
            paramList.Enqueue(new ValueObject(DATATYPE_BOOLEAN, value));
        }

        public bool getBoolean() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_BOOLEAN) {
                return (bool)vo.dataObject;
            }
            return false;
        }

        public void putByte(byte value) {
            paramList.Enqueue(new ValueObject(DATATYPE_BYTE, value));
        }

        public byte getByte() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_BYTE) {
                return (byte)vo.dataObject;
            }
            return 0;
        }

        public void putShort(short value) {
            paramList.Enqueue(new ValueObject(DATATYPE_SHORT, value));
        }

        public short getShort() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_SHORT) {
                return (short)vo.dataObject;
            }
            return 0;
        }

        public void putChar(char value) {
            paramList.Enqueue(new ValueObject(DATATYPE_CHAR, value));
        }

        public char getChar() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_CHAR) {
                return (char)vo.dataObject;
            }
            return '\x0000';
        }

        public void putInt(int value) {
            paramList.Enqueue(new ValueObject(DATATYPE_INT, value));
        }

        public int getInt() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_INT) {
                return (int)vo.dataObject;
            }
            return 0;
        }

        public void putLong(long value) {
            paramList.Enqueue(new ValueObject(DATATYPE_LONG, value));
        }

        public long getLong() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_LONG) {
                return (long)vo.dataObject;
            }
            return 0;
        }

        public void putFloat(float value) {
            paramList.Enqueue(new ValueObject(DATATYPE_FLOAT, value));
        }

        public float getFloat() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_FLOAT) {
                return (float)vo.dataObject;
            }
            return 0;
        }

        public void putDouble(double value) {
            paramList.Enqueue(new ValueObject(DATATYPE_DOUBLE, value));
        }

        public double getDouble() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_DOUBLE) {
                return (double)vo.dataObject;
            }
            return 0;
        }

        public void putDate(DateTime value) {
            paramList.Enqueue(new ValueObject(DATATYPE_DATE, value));
        }

        public DateTime getDate() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_DATE) {
                return (DateTime)vo.dataObject;
            }
            return DateTime.MinValue;
        }

        public void putString(string value) {
            paramList.Enqueue(new ValueObject(DATATYPE_STRING, value));
        }

        public string getString() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_STRING) {
                return (string)vo.dataObject;
            }
            return null;
        }

        public void putByteArray(byte[] value) {
            paramList.Enqueue(new ValueObject(DATATYPE_BYTEARRAY, value));
        }

        public byte[] getByteArray() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_BYTEARRAY) {
                return (byte[])vo.dataObject;
            }
            return null;
        }

        public void putIntArray(int[] value) {
            paramList.Enqueue(new ValueObject(DATATYPE_INTARRAY, value));
        }

        public int[] getIntArray() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_INTARRAY) {
                return (int[])vo.dataObject;
            }
            return null;
        }

        public void putLongArray(long[] value) {
            paramList.Enqueue(new ValueObject(DATATYPE_LONGARRAY, value));
        }

        public long[] getLongArray() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_LONGARRAY) {
                return (long[])vo.dataObject;
            }
            return null;
        }

        public void putFloatArray(float[] value) {
            paramList.Enqueue(new ValueObject(DATATYPE_FLOATARRAY, value));
        }

        public float[] getFloatArray() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_FLOATARRAY) {
                return (float[])vo.dataObject;
            }
            return null;
        }

        public void putDoubleArray(double[] value) {
            paramList.Enqueue(new ValueObject(DATATYPE_DOUBLEARRAY, value));
        }

        public double[] getDoubleArray() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_DOUBLEARRAY) {
                return (double[])vo.dataObject;
            }
            return null;
        }

        public void putStringArray(String[] value) {
            paramList.Enqueue(new ValueObject(DATATYPE_STRINGARRAY, value));
        }

        public String[] getStringArray() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_STRINGARRAY) {
                return (String[])vo.dataObject;
            }
            return null;
        }

        public void putWrapper(TransferObjectWrapper value) {
            paramList.Enqueue(new ValueObject(DATATYPE_WRAPPER, value));
        }

        public TransferObjectWrapper getWrapper() {
            ValueObject vo = paramList.Dequeue();
            if (vo != null && vo.dataType == DATATYPE_WRAPPER) {
                return (TransferObjectWrapper)vo.dataObject;
            }
            return null;
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

            foreach (ValueObject vo in paramList) {
                if (vo.dataType == DATATYPE_BOOLEAN) {
                    touts.writeByte(DATATYPE_BOOLEAN);
                    touts.writeBoolean((bool)vo.dataObject);
                } else if (vo.dataType == DATATYPE_BYTE) {
                    touts.writeByte(DATATYPE_BYTE);
                    touts.writeByte((byte)vo.dataObject);
                } else if (vo.dataType == DATATYPE_SHORT) {
                    touts.writeByte(DATATYPE_SHORT);
                    touts.writeShort((short)vo.dataObject);
                } else if (vo.dataType == DATATYPE_CHAR) {
                    touts.writeByte(DATATYPE_CHAR);
                    touts.writeChar((char)vo.dataObject);
                } else if (vo.dataType == DATATYPE_INT) {
                    touts.writeByte(DATATYPE_INT);
                    touts.writeInt((int)vo.dataObject);
                } else if (vo.dataType == DATATYPE_LONG) {
                    touts.writeByte(DATATYPE_LONG);
                    touts.writeLong((long)vo.dataObject);
                } else if (vo.dataType == DATATYPE_FLOAT) {
                    touts.writeByte(DATATYPE_FLOAT);
                    touts.writeFloat((float)vo.dataObject);
                } else if (vo.dataType == DATATYPE_DOUBLE) {
                    touts.writeByte(DATATYPE_DOUBLE);
                    touts.writeDouble((double)vo.dataObject);
                } else if (vo.dataType == DATATYPE_DATE) {
                    touts.writeByte(DATATYPE_DATE);
                    touts.writeDate((DateTime)vo.dataObject);
                } else if (vo.dataType == DATATYPE_STRING) {
                    touts.writeByte(DATATYPE_STRING);
                    touts.writeString((string)vo.dataObject);

                } else if (vo.dataType == DATATYPE_BYTEARRAY) {
                    touts.writeByte(DATATYPE_BYTEARRAY);
                    touts.writeByteArray((byte[])vo.dataObject);
                } else if (vo.dataType == DATATYPE_INTARRAY) {
                    touts.writeByte(DATATYPE_INTARRAY);
                    touts.writeIntArray((int[])vo.dataObject);
                } else if (vo.dataType == DATATYPE_LONGARRAY) {
                    touts.writeByte(DATATYPE_LONGARRAY);
                    touts.writeLongArray((long[])vo.dataObject);
                } else if (vo.dataType == DATATYPE_FLOATARRAY) {
                    touts.writeByte(DATATYPE_FLOATARRAY);
                    touts.writeFloatArray((float[])vo.dataObject);
                } else if (vo.dataType == DATATYPE_DOUBLEARRAY) {
                    touts.writeByte(DATATYPE_DOUBLEARRAY);
                    touts.writeDoubleArray((double[])vo.dataObject);
                } else if (vo.dataType == DATATYPE_STRINGARRAY) {
                    touts.writeByte(DATATYPE_STRINGARRAY);
                    touts.writeStringArray((string[])vo.dataObject);

                } else if (vo.dataType == DATATYPE_WRAPPER) {
                    touts.writeByte(DATATYPE_WRAPPER);
                    touts.writeWrapper((TransferObjectWrapper)vo.dataObject);
                }

            }

            return byteArray;
        }

        private int getByteArrayLength() {
            int blength = 0;

            // length of class, method, returnType and compress flag
            blength += TransferUtil.getLengthOfString(calleeClass);
            blength += TransferUtil.getLengthOfString(calleeMethod);
            blength += TransferUtil.getLengthOfByte();
            blength += TransferUtil.getLengthOfByte();

            // length of list data
            foreach (ValueObject vo in paramList) {
                if (vo.dataType == DATATYPE_BOOLEAN) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfBoolean();
                } else if (vo.dataType == DATATYPE_BYTE) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfByte();
                } else if (vo.dataType == DATATYPE_SHORT) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfShort();
                } else if (vo.dataType == DATATYPE_CHAR) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfChar();
                } else if (vo.dataType == DATATYPE_INT) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfInt();
                } else if (vo.dataType == DATATYPE_LONG) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfLong();
                } else if (vo.dataType == DATATYPE_FLOAT) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfFloat();
                } else if (vo.dataType == DATATYPE_DOUBLE) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfDouble();
                } else if (vo.dataType == DATATYPE_DATE) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfDate();
                } else if (vo.dataType == DATATYPE_STRING) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfString((string)vo.dataObject);

                } else if (vo.dataType == DATATYPE_BYTEARRAY) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfByteArray((byte[])vo.dataObject);
                } else if (vo.dataType == DATATYPE_INTARRAY) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfIntArray((int[])vo.dataObject);
                } else if (vo.dataType == DATATYPE_LONGARRAY) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfLongArray((long[])vo.dataObject);
                } else if (vo.dataType == DATATYPE_FLOATARRAY) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfFloatArray((float[])vo.dataObject);
                } else if (vo.dataType == DATATYPE_DOUBLEARRAY) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfDoubleArray((double[])vo.dataObject);
                } else if (vo.dataType == DATATYPE_STRINGARRAY) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfStringArray((string[])vo.dataObject);

                } else if (vo.dataType == DATATYPE_WRAPPER) {
                    blength += TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfWrapper((TransferObjectWrapper)vo.dataObject);
                }
            }

            return blength;
        }

        // server call
        public override void setByteData(byte[] buf) {
            TransferInputStream tins = new TransferInputStream(buf);
            this.calleeClass = tins.readString();
            this.calleeMethod = tins.readString();
            this.returnType = tins.readByte();
            this.compress = tins.readBoolean();

            while (!tins.isFinished()) {
                byte type = tins.readByte();
                if (type == DATATYPE_BOOLEAN) {
                    putBoolean(tins.readBoolean());
                } else if (type == DATATYPE_BYTE) {
                    putByte(tins.readByte());
                } else if (type == DATATYPE_SHORT) {
                    putShort(tins.readShort());
                } else if (type == DATATYPE_CHAR) {
                    putChar(tins.readChar());
                } else if (type == DATATYPE_INT) {
                    putInt(tins.readInt());
                } else if (type == DATATYPE_LONG) {
                    putLong(tins.readLong());
                } else if (type == DATATYPE_FLOAT) {
                    putFloat(tins.readFloat());
                } else if (type == DATATYPE_DOUBLE) {
                    putDouble(tins.readDouble());
                } else if (type == DATATYPE_DATE) {
                    putDate(tins.readDate());
                } else if (type == DATATYPE_STRING) {
                    putString(tins.readString());

                } else if (type == DATATYPE_BYTEARRAY) {
                    putByteArray(tins.readByteArray());
                } else if (type == DATATYPE_INTARRAY) {
                    putIntArray(tins.readIntArray());
                } else if (type == DATATYPE_LONGARRAY) {
                    putLongArray(tins.readLongArray());
                } else if (type == DATATYPE_FLOATARRAY) {
                    putFloatArray(tins.readFloatArray());
                } else if (type == DATATYPE_DOUBLEARRAY) {
                    putDoubleArray(tins.readDoubleArray());
                } else if (type == DATATYPE_STRINGARRAY) {
                    putStringArray(tins.readStringArray());

                } else if (type == DATATYPE_WRAPPER) {
                    putWrapper(tins.readWrapper());
                }
            }

        }
    }
}
