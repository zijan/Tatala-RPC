package com.qileyuan.tatala.socket.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.proxy.DefaultProxy;
import com.qileyuan.tatala.socket.exception.SocketExecuteException;
import com.qileyuan.tatala.socket.exception.TatalaRollbackException;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.util.NetworkUtil;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class ServerSession {
	Logger log = Logger.getLogger(ServerSession.class);
	static final int BUFFER_SIZE = 1024;
	
	private final ReentrantLock writeLock = new ReentrantLock(); //socket channel write lock
	//may need not to cache
	private static Map<String, Class<?>> calleeClassCache = new HashMap<String, Class<?>>();
	private static Map<String, Object> calleeObjectCache = new HashMap<String, Object>();
	private static ExecutorService executorService;
	
	private DefaultProxy defaultProxy;
	private AsynchronousSocketChannel socketChannel;
	
	private ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	private AioReceiveHandler aioReceiveHandler = new AioReceiveHandler();
	private ByteArrayOutputStream bos = new ByteArrayOutputStream();
	private boolean handleFlag = false;
	private boolean closed = false;
	private boolean firsttime = true;
	private int expectReceiveLength = 0;
	private int receiveLength = 0;

	private List<SessionFilter> sessionFilterList = new ArrayList<SessionFilter>();
	
	static{
		executorService = Executors.newCachedThreadPool();
	}
	
	public void start(){
		try {
			log.debug("Session start from " + socketChannel.getRemoteAddress());
		} catch (Exception e) {
			e.printStackTrace();
		}
		receive();
	}
	
	public void receive(){
		if(!closed && socketChannel.isOpen()){
			//if handleFlag is true, execute
			if(handleFlag){
				byte[] receiveData = bos.toByteArray();
				Worker worker = new Worker(receiveData);
				executorService.submit(worker);
				handleFlag = false;
				firsttime = true;
				expectReceiveLength = 0;
				receiveLength = 0;
				bos.reset();
			}
			socketChannel.read(byteBuffer, this, aioReceiveHandler);
		} else {
			log.error("Session or Channel has been closed");
		}
	}
	
	public void checkOneReceiveDone(int receivedCount, byte[] receiveData){
		receiveLength += receivedCount;

		//if finish receive one client request, set handle flag is true
		if(receivedCount < ServerSession.BUFFER_SIZE){
			//check if Not Tatala Request, close
			if(firsttime && !checkTatalaFlag(receiveData)){
				log.error("Not Tatala Request.");
				close();
			}
			handleFlag = true;
		}
		
		//here check if receivedCount just equals buffer size
		if (receivedCount == ServerSession.BUFFER_SIZE) {
            if (firsttime) {
            	//check if Not Tatala Request, close
    			if(firsttime && !checkTatalaFlag(receiveData)){
    				log.error("Not Tatala Request.");
    				close();
    			}
                expectReceiveLength = TransferUtil.getExpectReceiveLength(receiveData);
            }
            //if expectReceiveLength equal receiveLength, the server response is done
            if (expectReceiveLength == receiveLength) {
            	handleFlag = true;
            }
        }
		
        firsttime = false;
	}
	
	public void close(){
		closed = true;
		try {
			if(socketChannel.isOpen()){
				log.debug("Session close from " + socketChannel.getRemoteAddress());

				//call session filter on close
				for (SessionFilter sessionFilter : sessionFilterList) {
					sessionFilter.onClose(this);
				}
				
				//remove current session from session map
				long clientId = NetworkUtil.getClientIdBySocketChannel(socketChannel);
				AioSocketServer.getSessionMap().remove(clientId);
				socketChannel.close();
				
				bos.close();
			}
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
	
	private void handleReceiveData(byte[] receiveData){
		//call session filter on receive
		for (SessionFilter sessionFilter : sessionFilterList) {
			//if return false, ignore execute code
			if(!sessionFilter.onReceive(this, receiveData)){
				return;
			}
		}
		
		//retrieve no flag data
		receiveData = TransferUtil.getNoTatalaFlagData(receiveData);
		
		TransferObject to = null;
        try {
	        to = TransferUtil.byteArrayToTransferObject(receiveData);

	        //put current session into session map, key is client IP and port
			long clientId = NetworkUtil.getClientIdBySocketChannel(socketChannel);
			if(!AioSocketServer.getSessionMap().containsKey(clientId)){
				AioSocketServer.getSessionMap().put(clientId, this);
			}
	        //set clientId to TransferObject
	        to.setClientId(clientId);

			Object returnObj = execute(to);
			send(to, returnObj);
			
        } catch (TatalaRollbackException tre) {
			log.error("Tatala Return Exception: Callee Class and Method: [" + to.getCalleeClass() + "."+ to.getCalleeMethod() + "] e: " + tre, tre);
			try {
				to.registerReturnType(TransferObject.DATATYPE_SERIALIZABLE);
				send(to, tre);
			} catch (Exception e) {
				close();
			} 
			
		} catch (SocketExecuteException see) {
        	log.error("Callee Class and Method: [" + to.getCalleeClass() + "."+ to.getCalleeMethod() + "]");
        	try {
				send(to, null);
			} catch (Exception e) {
				close();
			} 
		} catch (Exception e) {
			log.error("Callee Class and Method: [" + to.getCalleeClass() + "."+ to.getCalleeMethod() + "]");
			log.error("Handle Receive Data error: " + e, e);
			close();
		}
	}
	
	private Object execute(TransferObject to) throws SocketExecuteException{
		Object retobj = null;
		
		try {
			String calleeClassName = to.getCalleeClass();
			String calleeMethod = to.getCalleeMethod();
			
			if(calleeClassName == null){
				throw new SocketExecuteException("No connection with client.");
			}
			
			//Check default proxy, don't need reflection.
			if(to.isDefaultCallee() || calleeClassName.equals(TransferObject.DEFAULT_PROXY)){
				if(defaultProxy != null){
					retobj = defaultProxy.execute(to);
				}
			} else {
				Class<?> calleeClass = null;
				Object calleeObject = null;
				
				if(calleeClassCache.containsKey(calleeClassName)){
					calleeClass = calleeClassCache.get(calleeClassName);
					calleeObject = calleeObjectCache.get(calleeClassName);
				} else {
					calleeClass = Class.forName(calleeClassName);
					calleeObject = calleeClass.newInstance();
					
					calleeClassCache.put(calleeClassName, calleeClass);
					calleeObjectCache.put(calleeClassName, calleeObject);
				}
				
				if(calleeClass == null || calleeObject == null){
					throw new SocketExecuteException("No connection with client.");
				}
				
				Method meth = calleeClass.getMethod(calleeMethod, TransferObject.class);
				retobj = meth.invoke(calleeObject, to);
			}
		} catch (InvocationTargetException ite) {
			if(ite.getCause() instanceof TatalaRollbackException){
				TatalaRollbackException tre = (TatalaRollbackException)ite.getCause();
				throw tre;
			} else {
				log.error("Server execute error e: " + ite, ite);
				throw new SocketExecuteException("Server execute error e: " + ite.getMessage());
			}

		} catch (Exception e) {
			log.error("Server execute error e: " + e, e);
			throw new SocketExecuteException("Server execute error e: " + e.getMessage());
		} 
		
		return retobj;
	}

	private void send(TransferObject to, Object returnObj) throws IOException, InterruptedException, ExecutionException{
		//if noreturn type, don't call socket send
		if(to.getReturnType() == TransferObject.DATATYPE_NORETURN){
			return;
		}
		
		byte[] sendData = TransferUtil.returnObjectToByteArray(to, returnObj);
		ByteBuffer byteBuffer = ByteBuffer.wrap(sendData);
		
		writeLock.lock();
		try {
			//send server response
			socketChannel.write(byteBuffer).get();
		} finally {
			writeLock.unlock();
		}
	}
	
	public void executeServerCall(TransferObject to){
		try {
			byte[] sendData = TransferUtil.transferObjectToByteArray(to);
			ByteBuffer byteBuffer = ByteBuffer.wrap(sendData);
			
			writeLock.lock();
			try {
				//send server call
				socketChannel.write(byteBuffer).get();
			} finally {
				writeLock.unlock();
			}
		} catch (Exception e) {
			log.error("Server call Class and Method: [" + to.getCalleeClass() + "."+ to.getCalleeMethod() + "]");
			log.error("Execute Server Call error: " + e);
			close();
		} 
	}
	
	private boolean checkTatalaFlag(byte[] receiveData){
		if(receiveData.length > TransferUtil.TatalaFlag.length){
			for(int i = 0; i<TransferUtil.TatalaFlag.length; i++){
				if(receiveData[i] != TransferUtil.TatalaFlag[i]){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	class Worker implements Callable<Object>{
		private byte[] receiveData;
		public Worker(byte[] receiveData){
			this.receiveData = receiveData;
		}
		
		@Override
		public Object call() throws Exception {
			handleReceiveData(receiveData);
			return 0;
		}
	}
	
	public DefaultProxy getDefaultProxy() {
		return defaultProxy;
	}
	public void setDefaultProxy(DefaultProxy defaultProxy) {
		this.defaultProxy = defaultProxy;
	}
	public AsynchronousSocketChannel getSocketChannel() {
		return socketChannel;
	}
	public void setSocketChannel(AsynchronousSocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}
	public ByteBuffer getByteBuffer() {
		return byteBuffer;
	}
	
	public void addSessionFilter(SessionFilter sessionFilter){
		if(!sessionFilterList.contains(sessionFilter)){
			sessionFilterList.add(sessionFilter);
		}
	}
	public void removeSessionFilter(SessionFilter sessionFilter){
		if(sessionFilterList.contains(sessionFilter)){
			sessionFilterList.remove(sessionFilter);
		}
	}
	public List<SessionFilter> getSessionFilterList() {
		return sessionFilterList;
	}
	public void setSessionFilterList(List<SessionFilter> sessionFilterList) {
		this.sessionFilterList = sessionFilterList;
	}

	public String toString(){
		String str = "";
		try {
			str = socketChannel.getRemoteAddress().toString();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return str;
	}
	
	public String getClientIP(){
		//IPStr: /XXX.XXX.XXX.XXX:XXXXX
		String IPStr = null;
		try {
			IPStr = socketChannel.getRemoteAddress().toString().substring(1, socketChannel.getRemoteAddress().toString().indexOf(":"));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return IPStr;
	}
}
