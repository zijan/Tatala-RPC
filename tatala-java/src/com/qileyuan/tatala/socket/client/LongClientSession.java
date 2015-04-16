package com.qileyuan.tatala.socket.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.DataFormatException;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.proxy.DefaultProxy;
import com.qileyuan.tatala.socket.exception.SocketExecuteException;
import com.qileyuan.tatala.socket.exception.TatalaRollbackException;
import com.qileyuan.tatala.socket.to.StandardTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class LongClientSession{
	static Logger log = Logger.getLogger(LongClientSession.class);
	static final int BUFFER_SIZE = 1024;
	static final int QUEUE_SIZE = 10;
	
	private String ip;
	private int port;
	private int timeout;
	
	private AsynchronousSocketChannel socketChannel;
	private AioClientReceiveHandler aioClientReceiveHandler = new AioClientReceiveHandler();
	private BlockingQueue<byte[]> receiveQueue = new LinkedTransferQueue<byte[]>();
	private DefaultProxy serverCallProxy;
	
	private ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	private ByteArrayOutputStream bos = new ByteArrayOutputStream();
	private boolean handleFlag = false;
	private boolean firsttime = true;
	private int expectReceiveLength = 0;
	private int receiveLength = 0;
	private boolean closed = false;
	
	public LongClientSession(String ip, int port, int timeout){
		this.ip = ip;
		this.port = port;
		this.timeout = timeout;
	}
	
	public Object start(TransferObject to) {
		Object resultObject = null;
		String calleeClass = to.getCalleeClass();
		String calleeMethod = to.getCalleeMethod();
		
		//set default server call proxy
		if(to.getServerCallProxy() != null){
			serverCallProxy = to.getServerCallProxy();
		}
		
		try {
			if(socketChannel == null || !socketChannel.isOpen() || closed){
				connect();
			}
			send(to);
			resultObject = receive(to);
			
			if(resultObject instanceof TatalaRollbackException){
				throw (TatalaRollbackException)resultObject;
			}
			
		} catch (TatalaRollbackException tre) {
			log.error("Tatala Return Exception: Callee Class and Method: [" + calleeClass + "." + calleeMethod + "]");
			throw tre;
		} catch (BindException be) {
			log.error("Connection error: " + be.getMessage());
			log.error("Callee Class and Method: [" + calleeClass + "." + calleeMethod + "]");
			close();
		} catch (TimeoutException te) {
			log.error("Socekt timed out, return null. [" + timeout + "ms]");
			log.error("Callee Class and Method: [" + calleeClass + "." + calleeMethod + "]");
			close();
		} catch (Exception e) {
			log.error("Callee Class and Method: [" + calleeClass + "." + calleeMethod + "]");
			e.printStackTrace();
			close();
		}
		
		return resultObject;
	}
	
	private void connect() throws BindException{
		String errorMessage = "";
		if (socketChannel == null || !socketChannel.isOpen() || closed) {
			try {
				socketChannel = AsynchronousSocketChannel.open();
				socketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
				socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
				socketChannel.connect(new InetSocketAddress(ip, port)).get(timeout, TimeUnit.MILLISECONDS);
				closed = false;
				log.debug("Session start to " + socketChannel.getRemoteAddress());
				
				//when connect to the server, keep receiving data either server response or server call
				receive();
			} catch (Exception e) {
				log.error("Connection error: " + e.getMessage());
				errorMessage = e.getMessage();
			}
		}
		if (socketChannel == null) {
			throw new BindException(errorMessage);
		}
	}
	
	private void send(TransferObject to) throws IOException, InterruptedException, ExecutionException, TimeoutException {
		byte[] sendData = TransferUtil.transferObjectToByteArray(to);
		socketChannel.write(ByteBuffer.wrap(sendData)).get(timeout, TimeUnit.MILLISECONDS);
	}
	
	private Object receive(TransferObject to) throws InterruptedException, DataFormatException, SocketExecuteException  {
		Object resultObject = null;

		//if receiveQueue is empty, wait a while, until server response come 
		byte[] receiveData = receiveQueue.poll(timeout, TimeUnit.MILLISECONDS);

		if(receiveData != null){
			resultObject = TransferUtil.byteArrayToReturnObject(receiveData);
		}

		return resultObject;
	}
	
	public void receive(){
		
		//if handleFlag is true, execute
		if(handleFlag){
			byte[] receiveData = bos.toByteArray();
			try {
				if(TransferUtil.isServerCall(receiveData[0])){
					//it is server call
					Worker worker = new Worker(receiveData);
					SocketController.getExecutorService().submit(worker);
				}else{
					//it is server response
					receiveQueue.put(receiveData);
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			handleFlag = false;
			firsttime = true;
			expectReceiveLength = 0;
			receiveLength = 0;
			bos.reset();
		}
		//socketChannel.read(byteBuffer, timeout, TimeUnit.MILLISECONDS, this, aioClientReceiveHandler);
		socketChannel.read(byteBuffer, this, aioClientReceiveHandler);
	}
	
	public void close(){
		try {
			log.debug("Session close to " + socketChannel.getRemoteAddress());
			closed = true;
			//socketChannel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void write(byte[] receiveData){
		try {
			bos.write(receiveData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void checkOneReceiveDone(int receivedCount, byte[] receiveData){
		receiveLength += receivedCount;
		
		//if finish receive one client request, set handle flag is true
		if(receivedCount < LongClientSession.BUFFER_SIZE){
			handleFlag = true;
		}
		if (receivedCount == LongClientSession.BUFFER_SIZE) {
            if (firsttime) {
                expectReceiveLength = TransferUtil.getExpectReceiveLength(receiveData);
            }
            //if expectReceiveLength equal receiveLength, the server response is done
            if (expectReceiveLength == receiveLength) {
            	handleFlag = true;
            }
        }
		
        firsttime = false;
	}

	private void handleServerCall(byte[] receiveData){
		TransferObject to = new StandardTransferObject();
        try {
	        to = TransferUtil.byteArrayToTransferObject(receiveData);
			execute(to);
		}catch (Exception e) {
			log.error("Callee Class and Method: [" + to.getCalleeClass() + "."+ to.getCalleeMethod() + "]");
			log.error("Handle Receive Data error: " + e);
			close();
		}
	}
	
	private void execute(TransferObject to) throws SocketExecuteException{
		
		try {
			String calleeClassName = to.getCalleeClass();
			String calleeMethod = to.getCalleeMethod();
			
			if(calleeClassName == null){
				throw new SocketExecuteException("No connection with server.");
			}

			//Check default proxy, don't need reflection.
			if(calleeClassName.equals(TransferObject.DEFAULT_PROXY)){
				if(serverCallProxy != null){
					serverCallProxy.execute(to);
				}
			}else{
				Class<?> calleeClass = Class.forName(calleeClassName);
				Object calleeObject = calleeClass.newInstance();

				if (calleeClass == null || calleeObject == null) {
					throw new SocketExecuteException("No connection with server.");
				}

				Method meth = calleeClass.getMethod(calleeMethod, TransferObject.class);
				meth.invoke(calleeObject, to);
			}
			
			

		} catch (Exception e) {
			log.error("Server execute error e: " + e, e);
			throw new SocketExecuteException("Server execute error e: " + e.getMessage());
		} 

	}
	
	class Worker implements Callable<Object>{
		private byte[] receiveData;
		public Worker(byte[] receiveData){
			this.receiveData = receiveData;
		}
		
		@Override
		public Object call() throws Exception {
			handleServerCall(receiveData);
			return 0;
		}
	}
	
	public ByteBuffer getByteBuffer() {
		return byteBuffer;
	}
}
