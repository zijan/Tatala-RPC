package com.qileyuan.tatala.example.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.proxy.ExampleClientProxy;
import com.qileyuan.tatala.example.proxy.ExampleServerProxy;
import com.qileyuan.tatala.example.service.model.Account;
import com.qileyuan.tatala.example.service.model.wrapper.AccountListWrapper;
import com.qileyuan.tatala.example.service.model.wrapper.AccountMapWrapper;
import com.qileyuan.tatala.example.service.model.wrapper.AccountWrapper;
import com.qileyuan.tatala.example.service.model.wrapper.AllTypeBeanWrapper;
import com.qileyuan.tatala.executor.ServerExecutor;
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
		client.normalTest();
		client.compressTest();
		client.asynchronousTest();
		client.defaultProxyTest();
		client.protobufTest();
		try {
			client.returnExceptionTest();
		} catch (Exception e) {
			log.error("Return Exception Test: " + e.getMessage());
		}
		client.noreturnTest();
	}
	
	public void normalTest(){
		
		//int, String and return String testing
		int Id = 18;
		String name = "JimT";
		TransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass(ExampleServerProxy.class);
		to.setCalleeMethod("sayHello");
		to.registerReturnType(TransferObject.DATATYPE_STRING);
		to.putInt(Id);
		to.putString(name);
		String result = (String)ServerExecutor.execute(to);
		log.debug("result: "+result);
		
		
		//no parameter, void return testing
		to = transferObjectFactory.createTransferObject();
		to.setCalleeClass(ExampleServerProxy.class);
		to.setCalleeMethod("doSomething");
		to.registerReturnType(TransferObject.DATATYPE_VOID);
		ServerExecutor.execute(to);

		
		//object parameter, object return testing
		Account account = new Account();
		account.setId(1000);
		account.setName("JimT");
		to = transferObjectFactory.createTransferObject();
		to.setCalleeClass(ExampleServerProxy.class);
		to.setCalleeMethod("getAccount");
		to.registerReturnType(TransferObject.DATATYPE_WRAPPER);
		AccountWrapper accountWrapper = new AccountWrapper(account);
		to.putWrapper(accountWrapper);
		accountWrapper = (AccountWrapper) ServerExecutor.execute(to);
		if(accountWrapper != null){
			account = accountWrapper.getAccount();
		}
		log.debug(account);
		
		
		//all primitive type parameter, object return testing
		to = transferObjectFactory.createTransferObject();
		to.setCalleeClass(ExampleServerProxy.class);
		to.setCalleeMethod("getAllTypeBean");
		to.registerReturnType(TransferObject.DATATYPE_WRAPPER);
		to.putBoolean(true);
		to.putByte((byte)1);
		to.putShort((short)2);
		to.putChar('T');
		to.putInt(3);
		to.putLong((long)4);
		to.putFloat(5.5f);
		to.putDouble(6.66d);
		to.putDate(new Date());
		to.putString("Hello JimT!");
		AllTypeBeanWrapper allTypeBeanWrapper = (AllTypeBeanWrapper) ServerExecutor.execute(to);
		if(allTypeBeanWrapper != null){
			log.debug("allTypeBean: " + allTypeBeanWrapper.getAllTypeBean());
		}else{
			log.debug("allTypeBean: null");
		}
		
		
		//int string array parameter, string array return
		byte[] bytearr = new byte[3];
		bytearr[0] = 1;
		bytearr[1] = 2;
		bytearr[2] = 3;
		String[] strarr = new String[3];
		strarr[0] = "Jim ";
		strarr[1] = "Tang ";
		strarr[2] = "Toronto ";
		to = transferObjectFactory.createTransferObject();
		to.setCalleeClass(ExampleServerProxy.class);
		to.setCalleeMethod("getArray");
		to.registerReturnType(TransferObject.DATATYPE_STRINGARRAY);
		to.putByteArray(bytearr);
		to.putStringArray(strarr);
		strarr = (String[]) ServerExecutor.execute(to);
		if(strarr != null){
			for (int i = 0; i < strarr.length; i++) {
				log.debug("strarr: "+strarr[i]);
			}
		}
		
		
		//list parameter, list return testing
		account = new Account();
		List<Account> accountList = new ArrayList<Account>();
		for(int i=0; i<3; i++){
			account = new Account();
			account.setId(i);
			accountList.add(account);
		}
		to = transferObjectFactory.createTransferObject();
		to.setCalleeClass(ExampleServerProxy.class);
		to.setCalleeMethod("getAccountList");
		to.registerReturnType(TransferObject.DATATYPE_WRAPPER);
		AccountListWrapper testAccountListWrapper = new AccountListWrapper(accountList);
		to.putWrapper(testAccountListWrapper);
		testAccountListWrapper = (AccountListWrapper) ServerExecutor.execute(to);
		if(testAccountListWrapper != null){
			List<Account> retaccountList = testAccountListWrapper.getTestAccountList();
			log.debug("accountList: "+retaccountList);
		}else{
			log.debug("accountList: null");
		}
		
		
		//map parameter, map return testing
		Map<String, Account> accountMap = new HashMap<String, Account>();
		for(int i=0; i<3; i++){
			account = new Account();
			account.setId(i);
			accountMap.put("Key"+i, account);
		}
		to = transferObjectFactory.createTransferObject();
		to.setCalleeClass(ExampleServerProxy.class);
		to.setCalleeMethod("getAccountMap");
		to.registerReturnType(TransferObject.DATATYPE_WRAPPER);
		AccountMapWrapper testAccountMapWrapper = new AccountMapWrapper(accountMap);
		to.putWrapper(testAccountMapWrapper);
		testAccountMapWrapper = (AccountMapWrapper) ServerExecutor.execute(to);
		if(testAccountMapWrapper != null){
			Map<String, Account> retaccountMap = testAccountMapWrapper.getTestAccountMap();
			log.debug("accountMap: "+retaccountMap);
		}else{
			log.debug("accountMap: null");
		}
	}
	
	public void compressTest() {
		Account account = new Account();
		account.setId(1000);
		String str = "";
		for (int i = 0; i < 50; i++) {
			str += "The quick brown fox jumps over the lazy dog.  ";
        }
		account.setName(str);
		
		TransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass(ExampleServerProxy.class);
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
		
		TransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass(ExampleServerProxy.class);
		to.setCalleeMethod("getAccountSerializable");
		to.registerReturnType(TransferObject.DATATYPE_SERIALIZABLE);
		to.putSerializable(account);
		to.setAsynchronous(true);
		
		@SuppressWarnings({"unchecked"})
		Future<Account> future = (Future<Account>) ServerExecutor.execute(to);
		Account returnAccount = future.get();

		log.debug(returnAccount);
	}
	
	public void defaultProxyTest() {
		Account account = new Account();
		account.setId(1000);
		account.setName("...Default Proxy Test...");
		
		TransferObject to = transferObjectFactory.createTransferObject();
		//don't set callee class, handle with default server proxy
		to.setDefaultCallee(true);
		to.setCalleeMethod("getAccount");
		to.registerReturnType(TransferObject.DATATYPE_SERIALIZABLE);
		to.putSerializable(account);

		Account returnAccount = (Account) ServerExecutor.execute(to);

		log.debug(returnAccount);
	}
	
	public void protobufTest() throws Exception {
		Account account = new Account();
		account.setId(1000);
		account.setName("...Protobuf Test...");
		
		ExampleClientProxy proxy = new ExampleClientProxy();
		Account returnAccount = proxy.getAccountProto(account);

		log.debug(returnAccount);
	}
	
	public void returnExceptionTest() {
		TransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass(ExampleServerProxy.class);
		to.setCalleeMethod("exceptionCall");
		to.putInt(-1);
		to.registerReturnType(TransferObject.DATATYPE_VOID);

		ServerExecutor.execute(to);
	}
	
	public void noreturnTest() {
		TransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass(ExampleServerProxy.class);
		to.setCalleeMethod("sayHello");
		to.putInt(0);
		to.putString("Jim");
		to.registerReturnType(TransferObject.DATATYPE_NORETURN);
		ServerExecutor.execute(to);
	}
}
