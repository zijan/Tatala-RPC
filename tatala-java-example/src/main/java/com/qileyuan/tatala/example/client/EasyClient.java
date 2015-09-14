package com.qileyuan.tatala.example.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.service.ExampleManager;
import com.qileyuan.tatala.example.service.ExampleManagerImpl;
import com.qileyuan.tatala.example.service.model.Account;
import com.qileyuan.tatala.example.service.model.AllTypeBean;
import com.qileyuan.tatala.proxy.ClientProxyFactory;
import com.qileyuan.tatala.socket.to.TransferObjectFactory;

public class EasyClient {

	static Logger log = Logger.getLogger(EasyClient.class);
	
	private TransferObjectFactory transferObjectFactory;
	private ExampleManager manager;
	
	public EasyClient() {
		transferObjectFactory = new TransferObjectFactory("127.0.0.1", 10001, 5000);
		transferObjectFactory.setImplClass(ExampleManagerImpl.class);
		transferObjectFactory.setCompress(true);
		manager = (ExampleManager)ClientProxyFactory.create(ExampleManager.class, transferObjectFactory);
	}

	public EasyClient(TransferObjectFactory transferObjectFactory) {
		this.transferObjectFactory = transferObjectFactory;
		manager = (ExampleManager)ClientProxyFactory.create(ExampleManager.class, transferObjectFactory);
	}
	
	public static void main(String[] args) {
		
		EasyClient easyClient = new EasyClient();
		
		long l = System.currentTimeMillis();
		log.info("connect time: " + (System.currentTimeMillis() - l) + "(ms)");
		
		l = System.currentTimeMillis();
		
		easyClient.remoteTest();
		
		log.info("time: " + (System.currentTimeMillis() - l) + "(ms)");
	}
	
	/**
	 * remote test, Socket test
	 */
	public void remoteTest() {
		try {
			
			//int, String and return String testing
			int Id = 18;
			String name = "JimT";
			String result = manager.sayHello(Id, name);
			log.debug("result: "+result);
			
			//no parameter, void return testing
			manager.doSomething();
			
			//object parameter, object return testing
			Account account = new Account();
			account.setId(1000);
			account.setName("JimT");
			account = manager.getAccount(account);
			log.debug(account);
			
			//all primitive type parameter, object return testing
			AllTypeBean allTypeBean = manager.getAllTypeBean(true, (byte)1, (short)2, 'T', 3, 
					(long)4, 5.5f, 6.66d, new Date(), "Hello JimT!");
			log.debug("allTypeBean: "+allTypeBean);
			
			//int string array parameter, string array return
			byte[] bytearr = new byte[3];
			bytearr[0] = 1;
			bytearr[1] = 2;
			bytearr[2] = 3;
			String[] strarr = new String[3];
			strarr[0] = "Jim ";
			strarr[1] = "Tang ";
			strarr[2] = "Toronto ";
			strarr = manager.getArray(bytearr, strarr);
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
			accountList = manager.getAccountList(accountList);
			log.debug("accountList: "+accountList);
			
			//map parameter, map return testing
			Map<String, Account> accountMap = new HashMap<String, Account>();
			for(int i=0; i<3; i++){
				account = new Account();
				account.setId(i);
				accountMap.put("Key"+i, account);
			}
			accountMap = manager.getAccountMap(accountMap);
			log.debug("accountMap: "+accountMap);
			
			//set parameter, set return testing
			Set<Account> accountSet = new HashSet<Account>();
			for(int i=0; i<3; i++){
				account = new Account();
				account.setId(i);
				accountSet.add(account);
			}
			accountSet = manager.getAccountSet(accountSet);
			log.debug("accountSet: "+accountSet);
			
			//big content test
			Account bigAccount = new Account();
			bigAccount.setId(1000);
			String str = "";
			for (int i = 0; i < 50; i++) {
				str += "The quick brown fox jumps over the lazy dog.  ";
	        }
			bigAccount.setName(str);
			bigAccount = manager.getAccount(bigAccount);
			log.debug(bigAccount);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
