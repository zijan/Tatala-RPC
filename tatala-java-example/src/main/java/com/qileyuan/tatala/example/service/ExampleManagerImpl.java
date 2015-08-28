package com.qileyuan.tatala.example.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.qileyuan.tatala.example.service.model.Account;
import com.qileyuan.tatala.example.service.model.AllTypeBean;


/**
 * This class is a sample for the socket server provider. 
 * It present real business logic.
 * @author JimT
 *
 */
public class ExampleManagerImpl implements ExampleManager{

	Logger log = Logger.getLogger(ExampleManagerImpl.class);
	
	public String sayHello(int Id, String name) {
		//log.debug("["+Id+"]"+"Hello "+name+" !");
		return "["+Id+"]"+"Hello "+name+" !";
	}

	public void doSomething(){
	}
	
	@Override
	public void exceptionCall(int Id) {
		if(Id < 0){
			throw new ExampleReturnException("Server error: id < 0");
		}
	}
	
	public Account getAccount(Account account){
		account.setAddress("Toronto");
		return account;
	}
	
	public List<Account> getAccountList(List<Account> accountList){
		List<Account> retAccountList = new ArrayList<Account>();
		for (Account account : accountList) {
			Account accountNew = new Account();
			accountNew.setId(account.getId());
			accountNew.setName("Jim");
			accountNew.setAddress("North York");
			retAccountList.add(accountNew);
		}
		return retAccountList;
	}
	
	public Map<String, Account> getAccountMap(Map<String, Account> accountMap){
		Map<String, Account> retAccountMap = new HashMap<String, Account>();
		for (String key : accountMap.keySet()) {
			Account accountNew = new Account();
			accountNew.setId(accountMap.get(key).getId());
			accountNew.setName("Jim");
			accountNew.setAddress("North York");
			retAccountMap.put("ret"+key, accountNew);
		}
		return retAccountMap;
	}

	public Set<Account> getAccountSet(Set<Account> accountSet){
		Set<Account> retAccountSet = new HashSet<Account>();
		for (Account account : accountSet) {
			Account accountNew = new Account();
			accountNew.setId(account.getId());
			accountNew.setName("Jim");
			accountNew.setAddress("Vaughan");
			retAccountSet.add(accountNew);
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
