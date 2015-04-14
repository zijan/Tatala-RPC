using System;
using System.Collections.Generic;
using System.IO;
using QiLeYuan.Tatala.proxy;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.to.helper;
using QiLeYuan.Tatala.socket.util;

namespace QiLeYuan.Tatala.socket.to {
    public abstract class TransferObject {

        public const byte NORMAL_FLAG = 0;
        public const byte COMPRESS_FLAG = 1;
        public const byte SERVERCALL_FLAG = 1 << 1;
        public const byte LONGCONNECTION_FLAG = 1 << 2;
        public const byte NEWVERSION_FLAG = 1 << 3;

        public const byte DATATYPE_VOID = 0;
        public const byte DATATYPE_NULL = 1;
        public const byte DATATYPE_BOOLEAN = 2;
        public const byte DATATYPE_BYTE = 3;
        public const byte DATATYPE_SHORT = 4;
        public const byte DATATYPE_CHAR = 5;
        public const byte DATATYPE_INT = 6;
        public const byte DATATYPE_LONG = 7;
        public const byte DATATYPE_FLOAT = 8;
        public const byte DATATYPE_DOUBLE = 9;
        public const byte DATATYPE_DATE = 10;
        public const byte DATATYPE_STRING = 11;

        public const byte DATATYPE_WRAPPER = 12;

        public const byte DATATYPE_BYTEARRAY = 13;
        public const byte DATATYPE_INTARRAY = 14;
        public const byte DATATYPE_LONGARRAY = 15;
        public const byte DATATYPE_FLOATARRAY = 16;
        public const byte DATATYPE_DOUBLEARRAY = 17;
        public const byte DATATYPE_STRINGARRAY = 18;

        public const byte DATATYPE_SERIALIZABLE = 19;

        public const String DEFAULT_PROXY = "DefaultProxy";
        public const String DEFAULT_METHOD = "execute";

        protected String connectionName;

        protected String calleeClass = DEFAULT_PROXY;
        protected String calleeMethod = DEFAULT_METHOD;

        protected bool asynchronous;
        protected bool compress;
        protected bool longConnection;
        protected bool serverCall;
        protected bool newVersion;
        protected DefaultProxy serverCallProxy;

        protected byte returnType;

        public byte getReturnType() {
            return this.returnType;
        }

        public void registerReturnType(byte returnType) {
            this.returnType = returnType;
        }

        public String getCalleeClass() {
            return calleeClass;
        }

        public void setCalleeClass(String calleeClass) {
            this.calleeClass = calleeClass;
        }

        public String getCalleeMethod() {
            return calleeMethod;
        }

        public void setCalleeMethod(String calleeMethod) {
            this.calleeMethod = calleeMethod;
        }

        public String getConnectionName() {
            return connectionName;
        }

        public void setConnectionName(String connectionName) {
            this.connectionName = connectionName;
        }

        public bool isAsynchronous() {
            return asynchronous;
        }

        public void setAsynchronous(bool asynchronous) {
            this.asynchronous = asynchronous;
        }

        public bool isCompress() {
            return compress;
        }

        public void setCompress(bool compress) {
            this.compress = compress;
        }

        public bool isLongConnection() {
            return longConnection;
        }

        public void setLongConnection(bool longConnection) {
            this.longConnection = longConnection;
        }

        public bool isServerCall() {
            return serverCall;
        }

        public void setServerCall(bool serverCall) {
            this.serverCall = serverCall;
        }

        public bool isNewVersion() {
            return newVersion;
        }

        public void setNewVersion(bool newVersion) {
            this.newVersion = newVersion;
        }

        public DefaultProxy getServerCallProxy() {
            return serverCallProxy;
        }

        public void setServerCallProxy(DefaultProxy serverCallProxy) {
            this.serverCallProxy = serverCallProxy;
        }

        public abstract byte[] getByteData();

        // client call
        public static Object convertReturnInputStream(Stream ins) {
            // get byte array length
            int blength = TransferUtil.readInt(ins);

            byte[] buf = new byte[blength];
            ins.Read(buf, 0, blength);

            return convertReturnByteArray(buf);
        }

        public static Object convertReturnByteArray(byte[] buf) {
            Object returnObject = null;

            TransferInputStream tins = new TransferInputStream(buf);
            byte returnType = tins.readByte();
            if (returnType == DATATYPE_BOOLEAN) {
                returnObject = tins.readBoolean();
            } else if (returnType == DATATYPE_BYTE) {
                returnObject = tins.readByte();
            } else if (returnType == DATATYPE_SHORT) {
                returnObject = tins.readShort();
            } else if (returnType == DATATYPE_CHAR) {
                returnObject = tins.readChar();
            } else if (returnType == DATATYPE_INT) {
                returnObject = tins.readInt();
            } else if (returnType == DATATYPE_LONG) {
                returnObject = tins.readLong();
            } else if (returnType == DATATYPE_FLOAT) {
                returnObject = tins.readFloat();
            } else if (returnType == DATATYPE_DOUBLE) {
                returnObject = tins.readDouble();
            } else if (returnType == DATATYPE_DATE) {
                returnObject = tins.readDate();
            } else if (returnType == DATATYPE_STRING) {
                returnObject = tins.readString();
            } else if (returnType == DATATYPE_WRAPPER) {
                returnObject = (TransferObjectWrapper)tins.readWrapper();
            } else if (returnType == DATATYPE_VOID) {
                returnObject = null;

            } else if (returnType == DATATYPE_BYTEARRAY) {
                returnObject = tins.readByteArray();
            } else if (returnType == DATATYPE_INTARRAY) {
                returnObject = tins.readIntArray();
            } else if (returnType == DATATYPE_LONGARRAY) {
                returnObject = tins.readLongArray();
            } else if (returnType == DATATYPE_FLOATARRAY) {
                returnObject = tins.readFloatArray();
            } else if (returnType == DATATYPE_DOUBLEARRAY) {
                returnObject = tins.readDoubleArray();
            } else if (returnType == DATATYPE_STRINGARRAY) {
                returnObject = tins.readStringArray();
            }

            return returnObject;
        }

        //server call
        public abstract void setByteData(byte[] buf);

    }
}
