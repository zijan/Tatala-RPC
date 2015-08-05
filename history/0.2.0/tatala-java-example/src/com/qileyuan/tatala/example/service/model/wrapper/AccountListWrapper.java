package com.qileyuan.tatala.example.service.model.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.qileyuan.tatala.example.service.model.Account;
import com.qileyuan.tatala.socket.io.TransferInputStream;
import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObjectWrapper;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class AccountListWrapper implements TransferObjectWrapper{
	private List<AccountWrapper> accountWrapperList;
	
	public AccountListWrapper() {
	}
	
	public AccountListWrapper(List<Account> accountList){
		accountWrapperList = new ArrayList<AccountWrapper>();
		for (Account account : accountList) {
			AccountWrapper accountWrapper = new AccountWrapper();
			accountWrapper.setAccount(account);
			accountWrapperList.add(accountWrapper);
		}
	}

	public List<AccountWrapper> getAccountWrapperList() {
		return accountWrapperList;
	}

	public void setAccountWrapperList(List<AccountWrapper> accountWrapperList) {
		this.accountWrapperList = accountWrapperList;
	}

	public int getLength() {
		int length = TransferUtil.getLengthOfInt();
		for(AccountWrapper accountWrapper : accountWrapperList){
			length += accountWrapper.getLength();
		}
		return length;
	}
	
	public void getByteArray(TransferOutputStream touts) {
		touts.writeInt(accountWrapperList.size());
		for(AccountWrapper accountWrapper : accountWrapperList){
			accountWrapper.getByteArray(touts);
		}
	}

	public AccountListWrapper getObjectWrapper(TransferInputStream tins) {
		accountWrapperList = new ArrayList<AccountWrapper>();
		int size = tins.readInt();
		for (int i = 0; i < size; i++) {
			AccountWrapper accountWrapper = new AccountWrapper();
			accountWrapper = accountWrapper.getObjectWrapper(tins);
			accountWrapperList.add(accountWrapper);
		}
		return this;
	}

	public List<Account> getTestAccountList(){
		List<Account> accountList = new ArrayList<Account>();
		for (AccountWrapper accountWrapper : accountWrapperList) {
			Account account = accountWrapper.getAccount();
			accountList.add(account);
		}
		return accountList;
	}
}
