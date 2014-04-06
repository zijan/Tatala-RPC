package com.qileyuan.tatala.example.proxy.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.qileyuan.tatala.example.model.TestAccount;
import com.qileyuan.tatala.socket.io.TransferInputStream;
import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObjectWrapper;
import com.qileyuan.tatala.socket.util.TransferUtil;

public class TestAccountListWrapper implements TransferObjectWrapper{
	private List<TestAccountWrapper> accountWrapperList;
	
	public TestAccountListWrapper() {
	}
	
	public TestAccountListWrapper(List<TestAccount> testAccountList){
		accountWrapperList = new ArrayList<TestAccountWrapper>();
		for (TestAccount testAccount : testAccountList) {
			TestAccountWrapper testAccountWrapper = new TestAccountWrapper();
			testAccountWrapper.setAccount(testAccount);
			accountWrapperList.add(testAccountWrapper);
		}
	}

	public List<TestAccountWrapper> getAccountWrapperList() {
		return accountWrapperList;
	}

	public void setAccountWrapperList(List<TestAccountWrapper> accountWrapperList) {
		this.accountWrapperList = accountWrapperList;
	}

	public int getLength() {
		int length = TransferUtil.getLengthOfInt();
		for(TestAccountWrapper accountWrapper : accountWrapperList){
			length += accountWrapper.getLength();
		}
		return length;
	}
	
	public void getByteArray(TransferOutputStream touts) {
		touts.writeInt(accountWrapperList.size());
		for(TestAccountWrapper accountWrapper : accountWrapperList){
			accountWrapper.getByteArray(touts);
		}
	}

	public TestAccountListWrapper getObjectWrapper(TransferInputStream tins) {
		accountWrapperList = new ArrayList<TestAccountWrapper>();
		int size = tins.readInt();
		for (int i = 0; i < size; i++) {
			TestAccountWrapper accountWrapper = new TestAccountWrapper();
			accountWrapper = accountWrapper.getObjectWrapper(tins);
			accountWrapperList.add(accountWrapper);
		}
		return this;
	}

	public List<TestAccount> getTestAccountList(){
		List<TestAccount> accountList = new ArrayList<TestAccount>();
		for (TestAccountWrapper accountWrapper : accountWrapperList) {
			TestAccount account = accountWrapper.getAccount();
			accountList.add(account);
		}
		return accountList;
	}
}
