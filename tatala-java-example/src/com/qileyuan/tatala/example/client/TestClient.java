package com.qileyuan.tatala.example.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.proxy.TestClientProxy;
import com.qileyuan.tatala.example.service.model.AllTypeBean;
import com.qileyuan.tatala.example.service.model.Account;

/**
 * This class is a sample for client.
 * @author JimT
 *
 * Long Connect Remote test, client and server at same machine. (n=numThread; t=times; e=error)
 * CPU: i7-3610QM; RAM: 8G; OS: Win7-64
 * n(100) t(100) e(0) time: 6498(ms) 1539*8/s
 * n(800) t(100) e(0) time: 62064(ms) 1289*8/s
 *
 */
public class TestClient implements Runnable{

	static int numThread = 1; //100;
	static int times = 1; //100;

	static Logger log = Logger.getLogger(TestClient.class);
	static TestClientProxy manager = new TestClientProxy();
	
	static int sId = -1;
	
	public TestClient() {
	}
	
	public static void main(String[] args) {
		
		long l = System.currentTimeMillis();
		//it is like build connection
		connect();
		log.info("connect time: " + (System.currentTimeMillis() - l) + "(ms)");
		
		l = System.currentTimeMillis();
		
		//singleTest();
		multipleTest();
		
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

			//int parameter, void return testing, Tatala return exception
			log.debug("exceptionCall Id: " + -1);
			manager.exceptionCall(-1);
			
			/*
			synchronized (manager) {
				log.debug("callServer sId: " + sId);
				manager.callServer(sId++);
			}
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * single thread test
	 */
	public static void singleTest(){
		TestClient tc = new TestClient();
		for (int i = 0; i < times; i++) {
			tc.remoteTest();
		}
	}
	
	/**
	 * multiple thread test
	 */
	public static void multipleTest(){
		ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < numThread; i++) {
        	TestClient testClient = new TestClient();
            exec.execute(testClient);
        }
        
        exec.shutdown();
        while (!exec.isTerminated()){
        }
	}
	
	public void run() {
		for (int i = 0; i < times; i++) {
			remoteTest();
		}
	}
	
	public static void connect(){
		TestClient tc = new TestClient();
		tc.doConnect();
	}
	
	public void doConnect(){
		manager.sayHello(1, "connect");
	}
}
