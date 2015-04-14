package com.qileyuan.tatala.example.service;

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


/**
 * This class is a sample for the socket server provider. 
 * It present real business logic.
 * @author JimT
 *
 */
public class TestManagerImpl implements TestManager{

	Logger log = Logger.getLogger(TestManagerImpl.class);
	
	public String sayHello(int Id, String name) {
		return "["+Id+"]"+"Hello "+name+" !";
	}

	public void doSomething(){
	}
	
	@Override
	public void callServer(int Id) {
		log.debug("Id: "+Id);
		throw new TestReturnException("Call Server error");
	}
	
	public TestAccount getAccount(TestAccount account){
		account.setAddress("Toronto");
		return account;
	}
	
	public TestAccount getAccount2(TestAccount account, TestAccount account2){
		TestAccount returnAccount = new TestAccount();
		returnAccount.setId(account.getId());
		returnAccount.setName(account2.getName());
		returnAccount.setAddress(account2.getAddress());
		return returnAccount;
	}
	
	public List<TestAccount> getAccountList(List<TestAccount> accountList){
		List<TestAccount> retAccountList = new ArrayList<TestAccount>();
		for (TestAccount account : accountList) {
			TestAccount account2 = new TestAccount();
			account2.setId(account.getId());
			account2.setName("Jim");
			account2.setAddress("North York");
			retAccountList.add(account2);
		}
		return retAccountList;
	}
	
	public Map<String, TestAccount> getAccountMap(Map<String, TestAccount> accountMap){
		Map<String, TestAccount> retAccountMap = new HashMap<String, TestAccount>();
		for (String key : accountMap.keySet()) {
			TestAccount account2 = new TestAccount();
			account2.setId(accountMap.get(key).getId());
			account2.setName("Jim");
			account2.setAddress("North York");
			retAccountMap.put("ret"+key, account2);
		}
		return retAccountMap;
	}

	public Set<TestAccount> getAccountSet(Set<TestAccount> accountSet){
		Set<TestAccount> retAccountSet = new HashSet<TestAccount>();
		for (TestAccount account : accountSet) {
			TestAccount account2 = new TestAccount();
			account2.setId(account.getId());
			account2.setName("Jim");
			account2.setAddress("North York");
			retAccountSet.add(account2);
		}
		return retAccountSet;
	}
	
	public AllTypeBean getAllTypeBean(boolean aboolean, byte abyte,
			short ashort, char achar, int aint, long along, float afloat,
			double adouble, Date adate, String astring) {
		
		AllTypeBean allTypeBean = new AllTypeBean();
		allTypeBean.setAboolean(aboolean);
		allTypeBean.setAbyte(abyte);
		allTypeBean.setAshort(ashort);
		allTypeBean.setAchar(achar);
		allTypeBean.setAint(aint);
		allTypeBean.setAlong(along);
		allTypeBean.setAfloat(afloat);
		allTypeBean.setAdouble(adouble);
		allTypeBean.setAdate(adate);
		allTypeBean.setAstring(astring);

		return allTypeBean;
	}

	public String[] getArray(byte[] bytearr, String[] strarr) {
		if(bytearr == null){
			return null;
		}
		
		String[] rstrarr = new String[bytearr.length];
		
		for (int i = 0; i < rstrarr.length; i++) {
			rstrarr[i] = bytearr[i]+strarr[i];
		}
		
		return rstrarr;
	}
}
