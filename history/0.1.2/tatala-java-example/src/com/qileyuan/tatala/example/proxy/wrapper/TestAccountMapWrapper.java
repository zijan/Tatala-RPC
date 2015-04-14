package com.qileyuan.tatala.example.proxy.wrapper;

import java.util.HashMap;
import java.util.Map;

import com.qileyuan.tatala.example.model.TestAccount;
import com.qileyuan.tatala.socket.io.TransferInputStream;
import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObjectWrapper;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class TestAccountMapWrapper implements TransferObjectWrapper{
	private Map<String, TestAccountWrapper> accountWrapperMap;
	
	public TestAccountMapWrapper(){
	}
	
	public TestAccountMapWrapper(Map<String, TestAccount> testAccountMap){
		accountWrapperMap = new HashMap<String, TestAccountWrapper>();
		for (String key : testAccountMap.keySet()) {
			TestAccountWrapper testAccountWrapper = new TestAccountWrapper();
			TestAccount testAccount = testAccountMap.get(key);
			testAccountWrapper.setAccount(testAccount);
			accountWrapperMap.put(key, testAccountWrapper);
		}
	}
	
	public Map<String, TestAccountWrapper> getAccountWrapperMap() {
		return accountWrapperMap;
	}

	public void setAccountWrapperMap(Map<String, TestAccountWrapper> accountWrapperMap) {
		this.accountWrapperMap = accountWrapperMap;
	}

	@Override
	public int getLength() {
		int length = TransferUtil.getLengthOfInt();
		for (String key : accountWrapperMap.keySet()) {
			length += TransferUtil.getLengthOfString(key);
			TestAccountWrapper accountWrapper = accountWrapperMap.get(key);
			length += accountWrapper.getLength();
		}
		return length;
	}

	@Override
	public void getByteArray(TransferOutputStream touts) {
		touts.writeInt(accountWrapperMap.size());
		for (String key : accountWrapperMap.keySet()) {
			touts.writeString(key);
			TestAccountWrapper accountWrapper = accountWrapperMap.get(key);
			accountWrapper.getByteArray(touts);
		}
	}

	@Override
	public TransferObjectWrapper getObjectWrapper(TransferInputStream tins) {
		accountWrapperMap = new HashMap<String, TestAccountWrapper>();
		int size = tins.readInt();
		for (int i = 0; i < size; i++) {
			String key = tins.readString();
			TestAccountWrapper accountWrapper = new TestAccountWrapper();
			accountWrapper = accountWrapper.getObjectWrapper(tins);
			accountWrapperMap.put(key, accountWrapper);
		}
		return this;
	}

	public Map<String, TestAccount> getTestAccountMap(){
		Map<String, TestAccount> accountMap = new HashMap<String, TestAccount>();
		for (String key : accountWrapperMap.keySet()) {
			TestAccountWrapper accountWrapper = accountWrapperMap.get(key);
			TestAccount account = accountWrapper.getAccount();
			accountMap.put(key, account);
		}
		return accountMap;
	}
}
