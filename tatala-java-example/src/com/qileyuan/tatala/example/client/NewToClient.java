package com.qileyuan.tatala.example.client;

import java.util.Date;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.proxy.NewToClientProxy;
import com.qileyuan.tatala.example.service.model.AllTypeBean;
import com.qileyuan.tatala.example.service.model.Account;

/**
 * This class is a sample for client.
 * New transfer object sample.
 * @author Administrator
 *
 */
public class NewToClient {
	static Logger log = Logger.getLogger(NewToClient.class);
	static NewToClientProxy proxy = new NewToClientProxy();
	
	public static void main(String[] args) {
		
		int Id = 18;
		String name = "JimT";
		String result = proxy.sayHello(Id, name);
		log.debug("result: "+result);

		//no parameter, void return testing
		proxy.doSomething();
		
		//all primitive type parameter, object return testing
		AllTypeBean bean = proxy.allTypeTest(true, (byte)1, (short)2, 'T', 3, 
				(long)4, 5.5f, 6.66d, new Date(), "Hello JimT!");
		log.debug("result: "+bean);
		
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
				log.debug("strarr: "+strarr[i]);
			}
		}
		
		compressTest();
		
	}
	
	public static void compressTest() {
		Account account = new Account();
		account.setId(1000);
		String str = "";
		for (int i = 0; i < 50; i++) {
			str += "The quick brown fox jumps over the lazy dog.  ";
        }
		account.setName(str);
		account = proxy.getAccountCompress(account);
		log.debug(account);
	}
}
