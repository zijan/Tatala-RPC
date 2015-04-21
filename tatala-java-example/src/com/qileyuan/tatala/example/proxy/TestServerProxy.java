package com.qileyuan.tatala.example.proxy;

import java.util.List;
import java.util.Map;

import com.qileyuan.tatala.example.service.ExampleManager;
import com.qileyuan.tatala.example.service.ExampleManagerImpl;
import com.qileyuan.tatala.example.service.model.Account;
import com.qileyuan.tatala.example.service.model.AllTypeBean;
import com.qileyuan.tatala.example.service.model.wrapper.AccountListWrapper;
import com.qileyuan.tatala.example.service.model.wrapper.AccountMapWrapper;
import com.qileyuan.tatala.example.service.model.wrapper.AccountWrapper;
import com.qileyuan.tatala.example.service.model.wrapper.AllTypeBeanWrapper;
import com.qileyuan.tatala.socket.to.MappedTransferObject;
import com.qileyuan.tatala.socket.to.OrderedTransferObject;
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

	private ExampleManager manager = new ExampleManagerImpl();

	public String sayHello(TransferObject baseto) {
		MappedTransferObject to = (MappedTransferObject)baseto;
		int Id = to.getInt("Id");
		String name = to.getString("name");

		String result = manager.sayHello(Id, name);
		return result;
	}

	public void doSomething(TransferObject baseto) {
		manager.doSomething();
	}
	
	public void exceptionCall(TransferObject baseto) {
		MappedTransferObject to = (MappedTransferObject)baseto;
		int Id = to.getInt("Id");
		manager.exceptionCall(Id);
	}

	public AccountWrapper getAccount(TransferObject baseto) {
		OrderedTransferObject to = (OrderedTransferObject)baseto;
		AccountWrapper accountWrapper = (AccountWrapper) to.getWrapper();
		Account account = accountWrapper.getAccount();
		Account returnAccount = manager.getAccount(account);
		accountWrapper.setAccount(returnAccount);

		// server side can be specific to return compress data or not.
		// to.setCompress(false);

		return accountWrapper;
	}

	public Account getAccountSerializable(TransferObject baseto) {
		OrderedTransferObject to = (OrderedTransferObject)baseto;
		Account account = (Account) to.getSerializable();
		Account returnAccount = manager.getAccount(account);
		return returnAccount;
	}

	public AccountListWrapper getAccountList(TransferObject baseto) {
		MappedTransferObject to = (MappedTransferObject)baseto;
		AccountListWrapper testAccountListWrapper = (AccountListWrapper) to.getWrapper("accountList");
		List<Account> accountList = testAccountListWrapper.getTestAccountList();
		accountList = manager.getAccountList(accountList);
		testAccountListWrapper = new AccountListWrapper(accountList);

		return testAccountListWrapper;
	}

	public AccountMapWrapper getAccountMap(TransferObject baseto) {
		MappedTransferObject to = (MappedTransferObject)baseto;
		AccountMapWrapper testAccountMapWrapper = (AccountMapWrapper) to.getWrapper("accountMap");
		Map<String, Account> accountMap = testAccountMapWrapper.getTestAccountMap();
		accountMap = manager.getAccountMap(accountMap);
		testAccountMapWrapper = new AccountMapWrapper(accountMap);

		return testAccountMapWrapper;
	}
	
	public AllTypeBeanWrapper getAllTypeBean(TransferObject baseto) {
		MappedTransferObject to = (MappedTransferObject)baseto;
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
		MappedTransferObject to = (MappedTransferObject)baseto;
		byte[] bytearr = to.getByteArray("bytearr");
		String[] strarr = to.getStringArray("strarr");

		String[] result = manager.getArray(bytearr, strarr);
		return result;
	}
}
