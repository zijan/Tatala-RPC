package com.qileyuan.tatala.socket.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.socket.exception.SocketExecuteException;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class ShortClientSession{
	static Logger log = Logger.getLogger(ShortClientSession.class);
	
	private String hostIp;
	private int hostPort;
	private int timeout;
	private int retryTime;
	
	public ShortClientSession(String hostIp, int hostPort, int timeout, int retryTime){
		this.hostIp = hostIp;
		this.hostPort = hostPort;
		this.timeout = timeout;
		this.retryTime = retryTime;
	}
	
	public Object start(TransferObject to) {
		Object resultObject = null;
		Socket client = null;
		String calleeClass = to.getCalleeClass();
		String calleeMethod = to.getCalleeMethod();
		try {
			if(client == null){
				client = connect();
			}

			send(client, to);
			
			resultObject = receive(client, to);
		} catch (BindException be) {
			log.error("Connection error: " + be.getMessage());
			log.error("Callee Class and Method: [" + calleeClass + "." + calleeMethod + "]");
		} catch (SocketTimeoutException ste) {
			log.error("Socekt timed out, return null. [" + timeout + "ms]");
			log.error("Callee Class and Method: [" + calleeClass + "." + calleeMethod + "]");
		} catch (Exception e) {
			log.error("Callee Class and Method: [" + calleeClass + "." + calleeMethod + "]");
			e.printStackTrace();
		} finally {
			try {
				if (client != null) {
					close(client);
				}
			} catch (IOException e) {}
		}

		return resultObject;
	}

	private Socket connect() throws SocketException {
		Socket client = null;
		String errorMessage = "";
		int retry = retryTime;
		while (client == null && retry > 0) {
			try {
				client = new Socket(hostIp, hostPort);
			} catch (Exception e) {
				log.error("Connection error: " + e.getMessage());
				errorMessage = e.getMessage();
			}
			retry--;
			if (client == null) {
				log.error("Retry time: " + retry);
			}
		}
		if (client == null) {
			throw new BindException(errorMessage);
		}

		client.setSoTimeout(timeout);
		client.setReuseAddress(true);
		//this can avoid socket TIME_WAIT state
		//client.setSoLinger(true, 0);

		return client;
	}

	private void send(Socket client, TransferObject to) throws IOException {
		OutputStream os = client.getOutputStream();

		byte[] sendData = TransferUtil.transferObjectToByteArray(to);
		
		os.write(sendData);
	}

	private Object receive(Socket client, TransferObject to) throws IOException, DataFormatException, SocketExecuteException {
		Object resultObject = null;

		InputStream is = client.getInputStream();
		// in
		int compressFlag = is.read();
		if (compressFlag == 1) {
			resultObject = doInCompress(is);
		} else {
			resultObject = doInNormal(is);
		}

		return resultObject;
	}

	private void close(Socket client) throws IOException {
		OutputStream os = client.getOutputStream();
		InputStream is = client.getInputStream();
		os.close();
		is.close();
		client.close();
	}

	private Object doInNormal(InputStream is) throws IOException, SocketExecuteException {
		return TransferObject.convertReturnInputStream(is);
	}
	
	private Object doInCompress(InputStream is) throws IOException,	DataFormatException, SocketExecuteException {
		// in
		int unCompressedLength = TransferUtil.readInt(is);
		int compressedLength = TransferUtil.readInt(is);
		byte[] input = new byte[compressedLength];
		is.read(input);
		byte[] output = new byte[unCompressedLength];
		Inflater decompresser = new Inflater();
		decompresser.setInput(input);
		decompresser.inflate(output);
		decompresser.end();

		InputStream istemp = new ByteArrayInputStream(output);
		return TransferObject.convertReturnInputStream(istemp);
	}
}
