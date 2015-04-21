package com.qileyuan.tatala.example.service.model.wrapper;

import java.util.HashMap;
import java.util.Map;

import com.qileyuan.tatala.example.service.model.Account;
import com.qileyuan.tatala.socket.io.TransferInputStream;
import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObjectWrapper;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class AccountMapWrapper implements TransferObjectWrapper{
	private Map<String, AccountWrapper> accountWrapperMap;
	
	public AccountMapWrapper(){
	}
	
	public AccountMapWrapper(Map<String, Account> accountMap){
		accountWrapperMap = new HashMap<String, AccountWrapper>();
		for (String key : accountMap.keySet()) {
			AccountWrapper accountWrapper = new AccountWrapper();
			Account account = accountMap.get(key);
			accountWrapper.setAccount(account);
			accountWrapperMap.put(key, accountWrapper);
		}
	}
	
	public Map<String, AccountWrapper> getAccountWrapperMap() {
		return accountWrapperMap;
	}

	public void setAccountWrapperMap(Map<String, AccountWrapper> accountWrapperMap) {
		this.accountWrapperMap = accountWrapperMap;
	}

	@Override
	public int getLength() {
		int length = TransferUtil.getLengthOfInt();
		for (String key : accountWrapperMap.keySet()) {
			length += TransferUtil.getLengthOfString(key);
			AccountWrapper accountWrapper = accountWrapperMap.get(key);
			length += accountWrapper.getLength();
		}
		return length;
	}

	@Override
	public void getByteArray(TransferOutputStream touts) {
		touts.writeInt(accountWrapperMap.size());
		for (String key : accountWrapperMap.keySet()) {
			touts.writeString(key);
			AccountWrapper accountWrapper = accountWrapperMap.get(key);
			accountWrapper.getByteArray(touts);
		}
	}

	@Override
	public TransferObjectWrapper getObjectWrapper(TransferInputStream tins) {
		accountWrapperMap = new HashMap<String, AccountWrapper>();
		int size = tins.readInt();
		for (int i = 0; i < size; i++) {
			String key = tins.readString();
			AccountWrapper accountWrapper = new AccountWrapper();
			accountWrapper = accountWrapper.getObjectWrapper(tins);
			accountWrapperMap.put(key, accountWrapper);
		}
		return this;
	}

	public Map<String, Account> getTestAccountMap(){
		Map<String, Account> accountMap = new HashMap<String, Account>();
		for (String key : accountWrapperMap.keySet()) {
			AccountWrapper accountWrapper = accountWrapperMap.get(key);
			Account account = accountWrapper.getAccount();
			accountMap.put(key, account);
		}
		return accountMap;
	}
}
