using System;
using System.IO;
using System.Text;
using ICSharpCode.SharpZipLib.Zip.Compression;
using ICSharpCode.SharpZipLib.Zip.Compression.Streams;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.to;
using QiLeYuan.Tools.debug;

namespace QiLeYuan.Tatala.socket.util {
    public class TransferUtil {

        public static byte[] TatalaFlag = new byte[] { 0x54, 0x61, 0x74, 0x61, 0x6c, 0x61 };

        public static int getLengthOfBoolean() {
            return 1;
        }

        public static int getLengthOfByte() {
            return 1;
        }

        public static int getLengthOfByteArray(byte[] v) {
            try {
                if (v == null) {
                    return getLengthOfInt();
                } else {
                    return getLengthOfInt() + getLengthOfByte() * v.Length;
                }
            } catch (Exception e) {
                Logging.LogError(e.ToString());
            }
            return 0;
        }

        public static int getLengthOfShort() {
            return 2;
        }

        public static int getLengthOfChar() {
            return getLengthOfShort();
        }

        public static int getLengthOfInt() {
            return 4;
        }

        public static int getLengthOfIntArray(int[] v) {
            try {
                if (v == null) {
                    return getLengthOfInt();
                } else {
                    return getLengthOfInt() + getLengthOfInt() * v.Length;
                }
            } catch (Exception e) {
                Logging.LogError(e.ToString());
            }
            return 0;
        }

        public static int getLengthOfLong() {
            return 8;
        }

        public static int getLengthOfLongArray(long[] v) {
            try {
                if (v == null) {
                    return getLengthOfInt();
                } else {
                    return getLengthOfInt() + getLengthOfLong() * v.Length;
                }
            } catch (Exception e) {
                Logging.LogError(e.ToString());
            }
            return 0;
        }

        public static int getLengthOfFloat() {
            return 4;
        }

        public static int getLengthOfFloatArray(float[] v) {
            try {
                if (v == null) {
                    return getLengthOfInt();
                } else {
                    return getLengthOfInt() + getLengthOfFloat() * v.Length;
                }
            } catch (Exception e) {
                Logging.LogError(e.ToString());
            }
            return 0;
        }

        public static int getLengthOfDouble() {
            return 8;
        }

        public static int getLengthOfDoubleArray(double[] v) {
            try {
                if (v == null) {
                    return getLengthOfInt();
                } else {
                    return getLengthOfInt() + getLengthOfDouble() * v.Length;
                }
            } catch (Exception e) {
                Logging.LogError(e.ToString());
            }
            return 0;
        }

        public static int getLengthOfDate() {
            return getLengthOfLong();
        }

        public static int getLengthOfString(String s) {
            try {
                if (isEmpty(s)) {
                    return getLengthOfInt();
                } else {
                    byte[] b = Encoding.UTF8.GetBytes(s);
                    return getLengthOfInt() + b.Length;
                }
            } catch (Exception e) {
                Logging.LogError(e.ToString());
            }
            return 0;
        }

        public static int getLengthOfStringArray(String[] strs) {
            try {
                if (strs == null) {
                    return getLengthOfInt();
                } else {
                    int b = getLengthOfInt();
                    foreach (String s in strs) {
                        b += getLengthOfString(s);
                    }
                    return b;
                }
            } catch (Exception e) {
                Logging.LogError(e.ToString());
            }
            return 0;
        }

        public static int getLengthOfWrapper(TransferObjectWrapper value) {
            if (value == null) {
                return getLengthOfInt();
            } else {
                return getLengthOfInt() + getLengthOfString(value.GetType().FullName) + value.getLength();
            }
        }

        public static bool isEmpty(String str) {
            if (str == null || str.Trim().Length == 0) {
                return true;
            } else {
                return false;
            }
        }

        public static void writeInt(Stream outs, int v) {
            outs.WriteByte((byte)((v >> 24) & 0xFF));
            outs.WriteByte((byte)((v >> 16) & 0xFF));
            outs.WriteByte((byte)((v >> 8) & 0xFF));
            outs.WriteByte((byte)((v >> 0) & 0xFF));
        }

        public static int readInt(Stream ins) {
            int ch1 = ins.ReadByte();
            int ch2 = ins.ReadByte();
            int ch3 = ins.ReadByte();
            int ch4 = ins.ReadByte();
            return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
        }

        public static int getExpectReceiveLength(byte[] receiveData) {
            int expectReceiveLength = 0;
            TransferInputStream fis = new TransferInputStream(receiveData);
            if (TransferUtil.isCompress(fis.readByte())) {
                fis.readInt();//uncompresslength
                expectReceiveLength = TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfInt() + TransferUtil.getLengthOfInt() + fis.readInt();
            } else {
                expectReceiveLength = TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfInt() + fis.readInt();
            }
            return expectReceiveLength;
        }

        public static byte[] transferObjectToByteArray(TransferObject to) {
    	    // out
		    byte[] toByteArray = to.getByteData();
		    byte[] sendData = null;
		    // if compress flag is true
            if (to.isCompress()) {
			    sendData = TransferUtil.getOutputByCompress(toByteArray);
		    } else {
			    sendData = TransferUtil.getOutputByNormal(toByteArray);
		    }

            //set long connection flag
            if (to.isLongConnection()) {
                sendData[0] |= TransferObject.LONGCONNECTION_FLAG;
            }

            //set new version flag
            if (to.isNewVersion()) {
                sendData[0] |= TransferObject.NEWVERSION_FLAG;
            }

            byte[] newData = new byte[sendData.Length + TatalaFlag.Length];
            Array.Copy(TatalaFlag, 0, newData, 0, TatalaFlag.Length);
            Array.Copy(sendData, 0, newData, TatalaFlag.Length, sendData.Length);
            sendData = newData;

		    return sendData;
        }

        public static Object byteArrayToReturnObject(byte[] byteArray){
            byte[] toByteArray = new byte[byteArray.Length - 1];
            Array.Copy(byteArray, 1, toByteArray, 0, byteArray.Length - 1);

            if (TransferUtil.isCompress(byteArray[0])) {
			    toByteArray = TransferUtil.getInputByCompress(toByteArray);
		    } else {
			    toByteArray = TransferUtil.getInputByNormal(toByteArray);
		    }
            
		    return TransferObject.convertReturnByteArray(toByteArray);
        }

        public static TransferObject byteArrayToTransferObject(byte[] byteArray){
		    //TransferObject to = new TransferObject();
    	    int receiveLength = byteArray.Length;
		    byte[] toByteArray = new byte[receiveLength - 1];
            Array.Copy(byteArray, 1, toByteArray, 0, receiveLength - 1);
            byte flagbyte = byteArray[0];

            TransferObject to = null;
            //check if new version of transfer object
            if (TransferUtil.isNewVersion(flagbyte)) {
                to = new NewTransferObject();
            } else {
                to = new StandardTransferObject();
            }

            if (TransferUtil.isCompress(flagbyte)) {
			    toByteArray = TransferUtil.getInputByCompress(toByteArray);
		    } else {
			    toByteArray = TransferUtil.getInputByNormal(toByteArray);
		    }
		    to.setByteData(toByteArray);
    		
		    return to;
        }

        private static byte[] getOutputByNormal(byte[] toByteArray) {
            //set normal flag
            byte[] sendData = new byte[toByteArray.Length + TransferUtil.getLengthOfByte()];
            sendData[0] = TransferObject.NORMAL_FLAG; //0:normal; 1:compress
            Array.Copy(toByteArray, 0, sendData, TransferUtil.getLengthOfByte(), toByteArray.Length);
            return sendData;
        }

        private static byte[] getOutputByCompress(byte[] toByteArray) {
            int unCompressedLength = 0;
            int compressedLength = 0;

            byte[] input = toByteArray;
            unCompressedLength = input.Length;

            MemoryStream memoryStream = new MemoryStream();
            Deflater compressor = new Deflater();
            DeflaterOutputStream defos = new DeflaterOutputStream(memoryStream, compressor);
            defos.Write(input, 0, input.Length);
            defos.Flush();
            defos.Finish();
            byte[] output = memoryStream.ToArray();
            compressedLength = output.Length;

            memoryStream.Close();
            defos.Close();

            //set compress flag and compressedLength, unCompressedLength
            byte[] sendData = new byte[output.Length + TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfInt() + TransferUtil.getLengthOfInt()];
            sendData[0] = TransferObject.COMPRESS_FLAG; //0:normal; 1:compress
            TransferOutputStream fos = new TransferOutputStream(sendData);
            fos.skipAByte();
            fos.writeInt(unCompressedLength);
            fos.writeInt(compressedLength);
            Array.Copy(output, 0, sendData, TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfInt() + TransferUtil.getLengthOfInt(), output.Length);

            return sendData;
        }

        private static byte[] getInputByNormal(byte[] toByteArray) {
            TransferInputStream fis = new TransferInputStream(toByteArray);
            int blength = fis.readInt();
            byte[] buf = new byte[blength];
            Array.Copy(toByteArray, TransferUtil.getLengthOfInt(), buf, 0, blength);

            return buf;
        }

        private static byte[] getInputByCompress(byte[] toByteArray) {
            TransferInputStream fis = new TransferInputStream(toByteArray);
            int unCompressedLength = fis.readInt();
            int compressedLength = fis.readInt();

            byte[] input = new byte[compressedLength];
            fis.readFully(input, 0, compressedLength);

            byte[] output = new byte[unCompressedLength];
            MemoryStream memoryStream = new MemoryStream(input);
            Inflater decompresser = new Inflater();
            InflaterInputStream infis = new InflaterInputStream(memoryStream, decompresser);

            int currentIndex = 0;
            int count = output.Length;
            while (true) {
                int numRead = infis.Read(output, currentIndex, count);
                if (numRead <= 0) {
                    break;
                }
                currentIndex += numRead;
                count -= numRead;
            }

            memoryStream.Close();
            infis.Close();

            return getInputByNormal(output);
        }

        public static bool isCompress(byte b) {
            return (TransferObject.COMPRESS_FLAG & b) != 0;
        }

        public static bool isServerCall(byte b) {
            return (TransferObject.SERVERCALL_FLAG & b) != 0;
        }

        public static bool isLongConnection(byte b) {
            return (TransferObject.LONGCONNECTION_FLAG & b) != 0;
        }

        public static bool isNewVersion(byte b) {
            return (TransferObject.NEWVERSION_FLAG & b) != 0;
        }
    }
}