package com.qileyuan.tatala.example.client;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.proxy.TestClientProxy;
import com.qileyuan.tatala.example.service.model.TestAccount;

/**
 * This class is a sample for client, 
 * include compress, asynchronous, default proxy and serializable sample.
 * @author JimT
 *
 */
public class TestClient2 {

	static Logger log = Logger.getLogger(TestClient2.class);
	TestClientProxy manager = new TestClientProxy();
	
	public static void main(String[] args) throws Exception {
		TestClient2 testClient2 = new TestClient2();
		testClient2.compressTest();
		testClient2.asynchronousTest();
		testClient2.defaultProxyTest();
		testClient2.serializableTest();
	}
	
	public void compressTest() throws Exception {
		
		long l = System.currentTimeMillis();

		l = System.currentTimeMillis();
		TestAccount account = new TestAccount();
		account.setId(1000);
		String str = "";
		for (int i = 0; i < 50; i++) {
			str += "The quick brown fox jumps over the lazy dog.  ";
        }
		account.setName(str);
		account = manager.getAccount(account);
		log.debug(account);

		account = new TestAccount();
		account.setId(1000);
		str = "";
		for (int i = 0; i < 50; i++) {
			str += "The quick brown fox jumps over the lazy dog.  ";
        }
		account.setName(str);
		account = manager.getAccountCompress(account);
		log.debug(account);
		log.debug("t: " + (System.currentTimeMillis() - l) + "(ms)");
	}
	
	public void asynchronousTest() throws Exception {
		
		long l = System.currentTimeMillis();

		l = System.currentTimeMillis();
		TestAccount account = new TestAccount();
		account.setId(1000);
		account.setName("...Asynchronous Test...");
		account = manager.getAccountAsynchronous(account);
		log.debug(account);
		log.debug("t: " + (System.currentTimeMillis() - l) + "(ms)");
	}
	
	public void defaultProxyTest() throws Exception {
		
		long l = System.currentTimeMillis();

		l = System.currentTimeMillis();
		TestAccount account = new TestAccount();
		account.setId(1000);
		account.setName("...Default Proxy Test...");
		account = manager.getAccountDefaultProxy(account);
		log.debug(account);
		log.debug("t: " + (System.currentTimeMillis() - l) + "(ms)");
	}
	
	public void serializableTest() throws Exception {

		long l = System.currentTimeMillis();

		l = System.currentTimeMillis();
		TestAccount account = new TestAccount();
		account.setId(1000);
		account.setName("...Serializable Test...");
		account = manager.getAccountSerializable(account);
		log.debug(account);
		log.debug("t: " + (System.currentTimeMillis() - l) + "(ms)");
	}
}
