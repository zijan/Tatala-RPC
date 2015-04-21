package com.qileyuan.tatala.example.client;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.service.model.Account;
import com.qileyuan.tatala.example.service.model.wrapper.AccountWrapper;
import com.qileyuan.tatala.executor.ServerExecutor;
import com.qileyuan.tatala.socket.to.MappedTransferObject;
import com.qileyuan.tatala.socket.to.OrderedTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.to.TransferObjectFactory;

public class ExampleClient {
	
	static Logger log = Logger.getLogger(ExampleClient.class);
	
	private static String IP = "127.0.0.1";
	private static int PORT = 10001;
	private static int TIMEOUT = 5000;
	
	private static TransferObjectFactory transferObjectFactory;
	
	public static void main(String[] args) throws Exception {
		transferObjectFactory = new TransferObjectFactory(IP, PORT, TIMEOUT);
		
		ExampleClient client = new ExampleClient();
		client.compressTest();
		client.asynchronousTest();
		client.defaultProxyTest();
	}
	
	public void compressTest() throws Exception {
		Account account = new Account();
		account.setId(1000);
		String str = "";
		for (int i = 0; i < 50; i++) {
			str += "The quick brown fox jumps over the lazy dog.  ";
        }
		account.setName(str);
		
		OrderedTransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("getAccountSerializable");
		to.registerReturnType(TransferObject.DATATYPE_SERIALIZABLE);
		to.putSerializable(account);
		to.setCompress(true);

		Account returnAccount = (Account) ServerExecutor.execute(to);

		log.debug(returnAccount);
	}
	
	public void asynchronousTest() throws Exception {
		Account account = new Account();
		account.setId(1000);
		account.setName("...Asynchronous Test...");
		
		OrderedTransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("getAccountSerializable");
		to.registerReturnType(TransferObject.DATATYPE_SERIALIZABLE);
		to.putSerializable(account);
		to.setAsynchronous(true);

		Future<Account> future = (Future<Account>) ServerExecutor.execute(to);
		Account returnAccount = future.get();

		log.debug(returnAccount);
	}
	
	public void defaultProxyTest() throws Exception {
		Account account = new Account();
		account.setId(1000);
		account.setName("...Default Proxy Test...");
		
		OrderedTransferObject to = transferObjectFactory.createTransferObject();
		//don't set callee class, handle with default server proxy
		to.setDefaultCallee(true);
		to.setCalleeMethod("getAccount");
		to.registerReturnType(TransferObject.DATATYPE_WRAPPER);

		AccountWrapper accountWrapper = new AccountWrapper(account);
		to.putWrapper(accountWrapper);

		AccountWrapper returnAccountWrapper = (AccountWrapper) ServerExecutor.execute(to);
		if (returnAccountWrapper == null) {
			return;
		}
		Account returnAccount = returnAccountWrapper.getAccount();

		log.debug(returnAccount);
	}
}
