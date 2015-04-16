package com.qileyuan.tatala.socket.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.qileyuan.tatala.socket.exception.SocketExecuteException;
import com.qileyuan.tatala.socket.io.TransferInputStream;
import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.NewTransferObject;
import com.qileyuan.tatala.socket.to.StandardTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.to.TransferObjectWrapper;


public class TransferUtil {
	
	public static byte[] TatalaFlag = new byte[]{0x54, 0x61, 0x74, 0x61, 0x6c, 0x61};
	
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
				return getLengthOfInt() + getLengthOfByte() * v.length ;
			}
		} catch (Exception e) {
			e.printStackTrace();
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
				return getLengthOfInt() + getLengthOfInt() * v.length ;
			}
		} catch (Exception e) {
			e.printStackTrace();
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
				return getLengthOfInt() + getLengthOfLong() * v.length ;
			}
		} catch (Exception e) {
			e.printStackTrace();
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
				return getLengthOfInt() + getLengthOfFloat() * v.length ;
			}
		} catch (Exception e) {
			e.printStackTrace();
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
				return getLengthOfInt() + getLengthOfDouble() * v.length ;
			}
		} catch (Exception e) {
			e.printStackTrace();
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
				byte[] b = s.getBytes("UTF-8");
				return getLengthOfInt() + b.length ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getLengthOfStringArray(String[] strs) {
		try {
			if (strs == null) {
				return getLengthOfInt();
			} else {
				int b = getLengthOfInt();
				for (String s : strs) {
					b += getLengthOfString(s);
				}
				return b;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int getLengthOfWrapper(TransferObjectWrapper value){
		if (value == null) {
			return getLengthOfInt();
		}else{
			return  getLengthOfInt() + getLengthOfString(value.getClass().getName()) + value.getLength();
		}
	}
	
	public static boolean isEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void writeInt(OutputStream os, int v) throws IOException{
		os.write((byte) ((v >>> 24) & 0xFF));
		os.write((byte) ((v >>> 16) & 0xFF));
		os.write((byte) ((v >>> 8) & 0xFF));
		os.write((byte) ((v >>> 0) & 0xFF));
	}
	
	public static int readInt(InputStream is) throws IOException{
		int ch1 = is.read();
		int ch2 = is.read();
		int ch3 = is.read();
		int ch4 = is.read();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}
	
    public static int getExpectReceiveLength(byte[] receiveData) {
        int expectReceiveLength = 0;
        
        //retrieve no flag data
        byte[] newData = getNoTatalaFlagData(receiveData);
        if(newData == null){
        	return expectReceiveLength;
        }
        
        TransferInputStream fis = new TransferInputStream(newData);
        //check compress flag
        if (TransferUtil.isCompress(fis.readByte())) {
            fis.readInt();//uncompresslength
            expectReceiveLength = TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfInt() + TransferUtil.getLengthOfInt() + fis.readInt();
        } else {
            expectReceiveLength = TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfInt() + fis.readInt();
        }
        
        expectReceiveLength += TransferUtil.TatalaFlag.length;
        
        return expectReceiveLength;
    }
    
    public static byte[] getNoTatalaFlagData(byte[] receiveData){
    	if(receiveData.length > TransferUtil.TatalaFlag.length){
    		byte[] newData = new byte[receiveData.length - TransferUtil.TatalaFlag.length];
    		System.arraycopy(receiveData, TransferUtil.TatalaFlag.length, newData, 0, newData.length);
    		return newData;
    	}
		return null;
    }
    
    public static byte[] transferObjectToByteArray(TransferObject to) throws IOException{
    	// out
		byte[] toByteArray = to.getByteData();
		byte[] sendData = null;
		// if compress flag is true
		if (to.isCompress()) {
			sendData = TransferUtil.getOutputByCompress(toByteArray);
		} else {
			sendData = TransferUtil.getOutputByNormal(toByteArray);
		}
		
		//set server call flag
		if(to.isServerCall()){
			sendData[0] |= TransferObject.SERVERCALL_FLAG;
		}

        //set new version flag
        if (to.isNewVersion()) {
            sendData[0] |= TransferObject.NEWVERSION_FLAG;
        }
        
        //if not server call, add tatala flag data at head
        if(!to.isServerCall()){
        	byte[] newData = new byte[sendData.length + TatalaFlag.length];
        	System.arraycopy(TatalaFlag, 0, newData, 0, TatalaFlag.length);
    		System.arraycopy(sendData, 0, newData, TatalaFlag.length, sendData.length);
    		sendData = newData;
        }

		return sendData;
    }
    
    public static TransferObject byteArrayToTransferObject(byte[] byteArray) throws DataFormatException, SocketExecuteException{
		//TransferObject to = new TransferObject();
    	
		int receiveLength = byteArray.length;
		byte[] toByteArray = new byte[receiveLength - 1];
		System.arraycopy(byteArray, 1, toByteArray, 0, receiveLength - 1);

		byte flagbyte = byteArray[0];
		
		TransferObject to = null;
		//check if new version of transfer object
		if(TransferUtil.isNewVersion(flagbyte)){
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
    
    public static byte[] returnObjectToByteArray(TransferObject to, Object returnObject) throws IOException{
		byte[] sendData = null;
		byte[] returnByteArray = to.getReturnByteArray(returnObject);
		//if compress flag is true
		if (to.isCompress()) {
			sendData = TransferUtil.getOutputByCompress(returnByteArray);
		} else {
			sendData = TransferUtil.getOutputByNormal(returnByteArray);
		}
		return sendData;
    }
    
    public static Object byteArrayToReturnObject(byte[] byteArray) throws DataFormatException, SocketExecuteException{
    	byte[] toByteArray = new byte[byteArray.length - 1];
        System.arraycopy(byteArray, 1, toByteArray, 0, byteArray.length - 1);
        
        if (TransferUtil.isCompress(byteArray[0])) {
			toByteArray = TransferUtil.getInputByCompress(toByteArray);
		} else {
			toByteArray = TransferUtil.getInputByNormal(toByteArray);
		}
        
		return TransferObject.convertReturnByteArray(toByteArray);
    }
    
    private static byte[] getOutputByNormal(byte[] toByteArray){
		byte[] sendData = new byte[TransferUtil.getLengthOfByte() + toByteArray.length];
		//set normal flag
		sendData[0] = TransferObject.NORMAL_FLAG;
		System.arraycopy(toByteArray, 0, sendData, TransferUtil.getLengthOfByte(), toByteArray.length);
		return sendData;
    }
    
    private static byte[] getOutputByCompress(byte[] toByteArray){
		int compressedLength = 0;
		int unCompressedLength = 0;

		// out
		unCompressedLength = toByteArray.length;
		Deflater deflater = new Deflater();
	    deflater.setInput(toByteArray);
	    deflater.finish();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    byte[] buf = new byte[1024];
	    while (!deflater.finished()) {
	        int byteCount = deflater.deflate(buf);
	        baos.write(buf, 0, byteCount);
	    }
	    deflater.end();
	    byte[] output = baos.toByteArray();
	    compressedLength = output.length;

		//send return data just once
		byte[] sendData = new byte[TransferUtil.getLengthOfByte() + 
		                           TransferUtil.getLengthOfInt() + TransferUtil.getLengthOfInt() + output.length];
		//set compress flag and server call flag
		sendData[0] = TransferObject.COMPRESS_FLAG;
		
		TransferOutputStream fos = new TransferOutputStream(sendData);
		fos.skipAByte();
		fos.writeInt(unCompressedLength);
		fos.writeInt(compressedLength);
		System.arraycopy(output, 0, sendData, 
				TransferUtil.getLengthOfByte() + TransferUtil.getLengthOfInt() + TransferUtil.getLengthOfInt(), output.length);
		
		return sendData;
    }
    
    private static byte[] getInputByNormal(byte[] toByteArray){
        TransferInputStream fis = new TransferInputStream(toByteArray);
        //get byte array length
        int blength = fis.readInt();
        byte[] buf = new byte[blength];
        System.arraycopy(toByteArray, TransferUtil.getLengthOfInt(), buf, 0, blength);
        
        return buf;
    }
    
    private static byte[] getInputByCompress(byte[] toByteArray) throws DataFormatException{
        TransferInputStream fis = new TransferInputStream(toByteArray);
        int unCompressedLength = fis.readInt();
        int compressedLength = fis.readInt();

        byte[] input = new byte[compressedLength];
        fis.readFully(input, 0, compressedLength);
        byte[] output = new byte[unCompressedLength];
        
        Inflater decompresser = new Inflater();
	    decompresser.setInput(input);
	    decompresser.inflate(output);
	    decompresser.end();

        return getInputByNormal(output);
    }
    
	public static boolean isCompress(byte b){
		return (TransferObject.COMPRESS_FLAG & b) != 0;
	}
	
	public static boolean isServerCall(byte b){
		return (TransferObject.SERVERCALL_FLAG & b) != 0;
	}
	
	public static boolean isNewVersion(byte b){
		return (TransferObject.NEWVERSION_FLAG & b) != 0;
	}
    
    public static String byteArrayToString(byte[] byteArray){
    	StringBuffer sb = new StringBuffer();
    	sb.append("["+byteArray.length+"][");
    	for(byte b : byteArray){
    		sb.append(b + " ");
    	}
    	sb.append("]");
    	return sb.toString();
    }
}
