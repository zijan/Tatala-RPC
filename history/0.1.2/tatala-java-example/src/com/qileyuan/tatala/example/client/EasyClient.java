package com.qileyuan.tatala.example.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.model.AllTypeBean;
import com.qileyuan.tatala.example.model.TestAccount;
import com.qileyuan.tatala.example.service.TestManager;
import com.qileyuan.tatala.proxy.ClientProxyFactory;
import com.qileyuan.tatala.socket.to.TransferObjectFactory;

/**
 * This class is a sample for EasyClient.
 * Without server and client proxy sample.
 * @author JimT
 *
 * Long Connect Remote test, client and server at same machine. (n=numThread; t=times; e=error)
 * CPU: i7-3610QM; RAM: 8G; OS: Win7-64
 * n(100) t(100) e(0) time: 7476(ms) 1338*8/s
 *
 */
public class EasyClient {

	static Logger log = Logger.getLogger(EasyClient.class);
	
	private TransferObjectFactory transferObjectFactory;
	private TestManager manager;
	
	public EasyClient() {
		transferObjectFactory = new TransferObjectFactory("test1", true);
		transferObjectFactory.setImplClass("com.qileyuan.tatala.example.service.TestManagerImpl");
		transferObjectFactory.setCompress(true);
		manager = (TestManager)ClientProxyFactory.create(TestManager.class, transferObjectFactory);
	}

	public static void main(String[] args) {
		
		EasyClient ec = new EasyClient();
		
		long l = System.currentTimeMillis();
		log.info("connect time: " + (System.currentTimeMillis() - l) + "(ms)");
		
		l = System.currentTimeMillis();
		
		ec.remoteTest();
		
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
			TestAccount account = new TestAccount();
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
			account = new TestAccount();
			List<TestAccount> accountList = new ArrayList<TestAccount>();
			for(int i=0; i<3; i++){
				account = new TestAccount();
				account.setId(i);
				accountList.add(account);
			}
			accountList = manager.getAccountList(accountList);
			log.debug("accountList: "+accountList);
			
			//map parameter, map return testing
			Map<String, TestAccount> accountMap = new HashMap<String, TestAccount>();
			for(int i=0; i<3; i++){
				account = new TestAccount();
				account.setId(i);
				accountMap.put("Key"+i, account);
			}
			accountMap = manager.getAccountMap(accountMap);
			log.debug("accountMap: "+accountMap);
			
			//set parameter, set return testing
			Set<TestAccount> accountSet = new HashSet<TestAccount>();
			for(int i=0; i<3; i++){
				account = new TestAccount();
				account.setId(i);
				accountSet.add(account);
			}
			accountSet = manager.getAccountSet(accountSet);
			log.debug("accountSet: "+accountSet);
			
			//more than one object parameter, object return testing
			TestAccount accountt = new TestAccount();
			accountt.setId(1000);
			TestAccount accountt2 = new TestAccount();
			accountt2.setName("Tang");
			accountt2.setAddress("Beijing");
			account = manager.getAccount2(accountt, accountt2);
			log.debug(account);
			
			//big content test
			TestAccount bigAccount = new TestAccount();
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
