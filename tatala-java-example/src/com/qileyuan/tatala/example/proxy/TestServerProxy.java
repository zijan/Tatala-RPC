package com.qileyuan.tatala.example.proxy;

import java.util.List;
import java.util.Map;

import com.qileyuan.tatala.example.proxy.wrapper.AllTypeBeanWrapper;
import com.qileyuan.tatala.example.proxy.wrapper.TestAccountListWrapper;
import com.qileyuan.tatala.example.proxy.wrapper.TestAccountMapWrapper;
import com.qileyuan.tatala.example.proxy.wrapper.TestAccountWrapper;
import com.qileyuan.tatala.example.service.TestManager;
import com.qileyuan.tatala.example.service.TestManagerImpl;
import com.qileyuan.tatala.example.service.model.AllTypeBean;
import com.qileyuan.tatala.example.service.model.TestAccount;
import com.qileyuan.tatala.socket.to.StandardTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;

/**
 * This class is a sample for the socket server provider. It is a proxy class
 * which call real business logic through socket connection.
 * 
 * Follow these steps: 
 * 3)Create callee class and callee method running in server side. Get parameter from
 * transfer object. Call real business logic object. If logic method return
 * customization object, callee method need return TransferObjectWrapper object
 * containing customization object.
 * 
 * @author JimT
 * 
 */
public class TestServerProxy {

	private TestManager manager = new TestManagerImpl();

	public String sayHello(TransferObject baseto) {
		StandardTransferObject to = (StandardTransferObject)baseto;
		int Id = to.getInt("Id");
		String name = to.getString("name");

		String result = manager.sayHello(Id, name);
		return result;
	}

	public void doSomething(TransferObject baseto) {
		manager.doSomething();
	}
	
	public void callServer(TransferObject baseto) {
		StandardTransferObject to = (StandardTransferObject)baseto;
		int Id = to.getInt("Id");
		manager.callServer(Id);
	}

	public TestAccountWrapper getAccount(TransferObject baseto) {
		StandardTransferObject to = (StandardTransferObject)baseto;
		TestAccountWrapper accountWrapper = (TestAccountWrapper) to.getWrapper("account");
		TestAccount account = accountWrapper.getAccount();
		TestAccount returnAccount = manager.getAccount(account);
		accountWrapper.setAccount(returnAccount);

		// server side can be specific to return compress data or not.
		// to.setCompress(false);

		return accountWrapper;
	}

	public TestAccount getAccountSerializable(TransferObject baseto) {
		StandardTransferObject to = (StandardTransferObject)baseto;
		TestAccount account = (TestAccount) to.getSerializable("account");
		TestAccount returnAccount = manager.getAccount(account);
		return returnAccount;
	}

	public TestAccountWrapper getAccount2(TransferObject baseto) {
		StandardTransferObject to = (StandardTransferObject)baseto;
		TestAccountWrapper accountWrapper = (TestAccountWrapper) to.getWrapper("account");
		TestAccount account = accountWrapper.getAccount();
		TestAccountWrapper accountWrapper2 = (TestAccountWrapper) to.getWrapper("account2");
		TestAccount account2 = accountWrapper2.getAccount();

		TestAccount returnAccount = manager.getAccount2(account, account2);
		accountWrapper.setAccount(returnAccount);
		return accountWrapper;
	}

	public TestAccountListWrapper getAccountList(TransferObject baseto) {
		StandardTransferObject to = (StandardTransferObject)baseto;
		TestAccountListWrapper testAccountListWrapper = (TestAccountListWrapper) to.getWrapper("accountList");
		List<TestAccount> accountList = testAccountListWrapper.getTestAccountList();
		accountList = manager.getAccountList(accountList);
		testAccountListWrapper = new TestAccountListWrapper(accountList);

		return testAccountListWrapper;
	}

	public TestAccountMapWrapper getAccountMap(TransferObject baseto) {
		StandardTransferObject to = (StandardTransferObject)baseto;
		TestAccountMapWrapper testAccountMapWrapper = (TestAccountMapWrapper) to.getWrapper("accountMap");
		Map<String, TestAccount> accountMap = testAccountMapWrapper.getTestAccountMap();
		accountMap = manager.getAccountMap(accountMap);
		testAccountMapWrapper = new TestAccountMapWrapper(accountMap);

		return testAccountMapWrapper;
	}
	
	public AllTypeBeanWrapper getAllTypeBean(TransferObject baseto) {
		StandardTransferObject to = (StandardTransferObject)baseto;
		AllTypeBean allTypeBean = manager.getAllTypeBean(
				to.getBoolean("aboolean"), to.getByte("abyte"),
				to.getShort("ashort"), to.getChar("achar"), to.getInt("aint"),
				to.getLong("along"), to.getFloat("afloat"),
				to.getDouble("adouble"), to.getDate("adate"),
				to.getString("astring"));

		AllTypeBeanWrapper allTypeBeanWrapper = new AllTypeBeanWrapper(allTypeBean);
		return allTypeBeanWrapper;
	}

	public String[] getArray(TransferObject baseto) {
		StandardTransferObject to = (StandardTransferObject)baseto;
		byte[] bytearr = to.getByteArray("bytearr");
		String[] strarr = to.getStringArray("strarr");

		String[] result = manager.getArray(bytearr, strarr);
		return result;
	}
}
