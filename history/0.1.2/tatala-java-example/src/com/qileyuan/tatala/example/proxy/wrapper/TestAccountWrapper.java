package com.qileyuan.tatala.example.proxy.wrapper;

import com.qileyuan.tatala.example.model.TestAccount;
import com.qileyuan.tatala.socket.io.TransferInputStream;
import com.qileyuan.tatala.socket.io.TransferOutputStream;
import com.qileyuan.tatala.socket.to.TransferObjectWrapper;
import com.qileyuan.tatala.socket.util.TransferUtil;

/**
 * This class is a wrapper class containing customization object.
 * 
 * There are three implemented methods: 
 * getLength - get customization object byte array length
 * getByteArray - convert customization object into byte array
 * getObjectWrapper - convert byte array back to customization object
 * 
 * @author JimT
 *
 */
public class TestAccountWrapper implements TransferObjectWrapper {
	private TestAccount account;

	public TestAccountWrapper() {
	}
	
	public TestAccountWrapper(TestAccount account) {
		this.account = account;
	}

	public TestAccount getAccount() {
		return account;
	}

	public void setAccount(TestAccount account) {
		this.account = account;
	}

	public int getLength(){
		return TransferUtil.getLengthOfInt() + 
			   TransferUtil.getLengthOfString(account.getName()) +
			   TransferUtil.getLengthOfString(account.getAddress());
	}

	public void getByteArray(TransferOutputStream touts) {
		touts.writeInt(account.getId());
		touts.writeString(account.getName());
		touts.writeString(account.getAddress());
	}

	public TestAccountWrapper getObjectWrapper(TransferInputStream tins){
		account = new TestAccount();
		account.setId(tins.readInt());
		account.setName(tins.readString());
		account.setAddress(tins.readString());
		return this;
	}
}
