package com.qileyuan.tatala.example.proxy;

import java.util.List;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;
import com.qileyuan.tatala.example.service.ExampleManager;
import com.qileyuan.tatala.example.service.ExampleManagerImpl;
import com.qileyuan.tatala.example.service.model.Account;
import com.qileyuan.tatala.example.service.model.AllTypeBean;
import com.qileyuan.tatala.example.service.model.proto.AccountProto;
import com.qileyuan.tatala.example.service.model.wrapper.AccountListWrapper;
import com.qileyuan.tatala.example.service.model.wrapper.AccountMapWrapper;
import com.qileyuan.tatala.example.service.model.wrapper.AccountWrapper;
import com.qileyuan.tatala.example.service.model.wrapper.AllTypeBeanWrapper;
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
public class ExampleServerProxy {

	private ExampleManager manager = new ExampleManagerImpl();

	public String sayHello(TransferObject to) {
		int Id = to.getInt();
		String name = to.getString();

		String result = manager.sayHello(Id, name);
		return result;
	}

	public void doSomething(TransferObject to) {
		manager.doSomething();
	}
	
	public void exceptionCall(TransferObject to) {
		int Id = to.getInt();
		manager.exceptionCall(Id);
	}

	public AccountWrapper getAccount(TransferObject to) {
		AccountWrapper accountWrapper = (AccountWrapper) to.getWrapper();
		Account account = accountWrapper.getAccount();
		Account returnAccount = manager.getAccount(account);
		accountWrapper.setAccount(returnAccount);

		// server side can be specific to return compress data or not.
		// to.setCompress(false);

		return accountWrapper;
	}

	public Account getAccountSerializable(TransferObject to) {
		Account account = (Account) to.getSerializable();
		Account returnAccount = manager.getAccount(account);
		return returnAccount;
	}

	public AccountListWrapper getAccountList(TransferObject to) {
		AccountListWrapper testAccountListWrapper = (AccountListWrapper) to.getWrapper();
		List<Account> accountList = testAccountListWrapper.getTestAccountList();
		accountList = manager.getAccountList(accountList);
		testAccountListWrapper = new AccountListWrapper(accountList);

		return testAccountListWrapper;
	}

	public AccountMapWrapper getAccountMap(TransferObject to) {
		AccountMapWrapper testAccountMapWrapper = (AccountMapWrapper) to.getWrapper();
		Map<String, Account> accountMap = testAccountMapWrapper.getTestAccountMap();
		accountMap = manager.getAccountMap(accountMap);
		testAccountMapWrapper = new AccountMapWrapper(accountMap);

		return testAccountMapWrapper;
	}
	
	public AllTypeBeanWrapper getAllTypeBean(TransferObject to) {
		AllTypeBean allTypeBean = manager.getAllTypeBean(
				to.getBoolean(), to.getByte(),
				to.getShort(), to.getChar(), to.getInt(),
				to.getLong(), to.getFloat(),
				to.getDouble(), to.getDate(),
				to.getString());

		AllTypeBeanWrapper allTypeBeanWrapper = new AllTypeBeanWrapper(allTypeBean);
		return allTypeBeanWrapper;
	}

	public String[] getArray(TransferObject to) {
		byte[] bytearr = to.getByteArray();
		String[] strarr = to.getStringArray();

		String[] result = manager.getArray(bytearr, strarr);
		return result;
	}
	
	public byte[] getAccountProto(TransferObject to) throws InvalidProtocolBufferException {
		byte[] byteArray = to.getByteArray();
		AccountProto.Account accountProto = AccountProto.Account.parseFrom(byteArray);
		Account account = new Account();
		account.setId(accountProto.getId());
		account.setName(accountProto.getName());
		account.setAddress(accountProto.getAddress());
		
		Account returnAccount = manager.getAccount(account);

		AccountProto.Account.Builder accountProtoBuilder = AccountProto.Account.newBuilder();
		accountProtoBuilder.setId(returnAccount.getId());
		accountProtoBuilder.setName(returnAccount.getName());
		accountProtoBuilder.setAddress(returnAccount.getAddress());

		return accountProtoBuilder.build().toByteArray();
	}
}
