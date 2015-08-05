package com.qileyuan.tatala.example.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.qileyuan.tatala.example.service.model.Account;
import com.qileyuan.tatala.example.service.model.AllTypeBean;


/**
 * This interface is a sample for the socket server provider.
 * @author JimT
 *
 */
public interface ExampleManager {
	public String sayHello(int Id, String name);
	public void doSomething();
	public void exceptionCall(int Id);
	public Account getAccount(Account account);
	public List<Account> getAccountList(List<Account> accountList);
	public Map<String, Account> getAccountMap(Map<String, Account> accountMap);
	public Set<Account> getAccountSet(Set<Account> accountSet);
	public AllTypeBean getAllTypeBean(boolean aboolean, byte abyte, short ashort, char achar, int aint, long along, 
			float afloat, double adouble, Date adate, String astring);
	public String[] getArray(byte[] bytearr, String[] strarr);
}
