package com.qileyuan.tatala.example.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.qileyuan.tatala.example.model.AllTypeBean;
import com.qileyuan.tatala.example.model.TestAccount;


/**
 * This interface is a sample for the socket server provider.
 * @author JimT
 *
 */
public interface TestManager {
	public String sayHello(int Id, String name);
	public void doSomething();
	public TestAccount getAccount(TestAccount account);
	public TestAccount getAccount2(TestAccount account, TestAccount account2);
	public List<TestAccount> getAccountList(List<TestAccount> accountList);
	public Map<String, TestAccount> getAccountMap(Map<String, TestAccount> accountMap);
	public AllTypeBean getAllTypeBean(boolean aboolean, byte abyte, short ashort, char achar, int aint, long along, 
			float afloat, double adouble, Date adate, String astring);
	public String[] getArray(byte[] bytearr, String[] strarr);
}
