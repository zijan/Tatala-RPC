package com.qileyuan.tatala.example.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.proxy.ExampleClientProxy;
import com.qileyuan.tatala.example.service.model.Account;
import com.qileyuan.tatala.example.service.model.AllTypeBean;

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
public class PerformanceTestClient implements Runnable{

	static int numThread = 100;
	static int times = 100;

	static Logger log = Logger.getLogger(PerformanceTestClient.class);
	static ExampleClientProxy proxy = new ExampleClientProxy();
	
	static int sId = -1;
	
	public PerformanceTestClient() {
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
			String result = proxy.sayHello(Id, name);
			//log.debug("result: "+result);
			
			//no parameter, void return testing
			proxy.doSomething();

			//object parameter, object return testing
			Account account = new Account();
			account.setId(1000);
			account.setName("JimT");
			account = proxy.getAccount(account);
			//log.debug(account);
			
			//Serializable object parameter, object return testing
			account = new Account();
			account.setId(1000);
			account.setName("JimT");
			account = proxy.getAccountSerializable(account);
			//log.debug(account);
			
			//all primitive type parameter, object return testing
			AllTypeBean allTypeBean = proxy.getAllTypeBean(true, (byte)1, (short)2, 'T', 3, 
					(long)4, 5.5f, 6.66d, new Date(), "Hello JimT!");
			//log.debug("allTypeBean: "+allTypeBean);
			
			//int string array parameter, string array return
			byte[] bytearr = new byte[3];
			bytearr[0] = 1;
			bytearr[1] = 2;
			bytearr[2] = 3;
			String[] strarr = new String[3];
			strarr[0] = "Jim ";
			strarr[1] = "Tang ";
			strarr[2] = "Toronto ";
			strarr = proxy.getArray(bytearr, strarr);
			if(strarr != null){
				for (int i = 0; i < strarr.length; i++) {
					//log.debug("strarr: "+strarr[i]);
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
			accountList = proxy.getAccountList(accountList);
			//log.debug("accountList: "+accountList);
			
			//map parameter, map return testing
			Map<String, Account> accountMap = new HashMap<String, Account>();
			for(int i=0; i<3; i++){
				account = new Account();
				account.setId(i);
				accountMap.put("Key"+i, account);
			}
			accountMap = proxy.getAccountMap(accountMap);
			//log.debug("accountMap: "+accountMap);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * single thread test
	 */
	public static void singleTest(){
		PerformanceTestClient tc = new PerformanceTestClient();
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
        	PerformanceTestClient testClient = new PerformanceTestClient();
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
		PerformanceTestClient tc = new PerformanceTestClient();
		tc.doConnect();
	}
	
	public void doConnect(){
		proxy.sayHello(1, "connect");
	}
}
