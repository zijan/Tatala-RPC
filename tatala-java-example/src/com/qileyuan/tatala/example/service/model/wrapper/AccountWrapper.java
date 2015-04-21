package com.qileyuan.tatala.example.service.model.wrapper;

import com.qileyuan.tatala.example.service.model.Account;
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
public class AccountWrapper implements TransferObjectWrapper {
	private Account account;

	public AccountWrapper() {
	}
	
	public AccountWrapper(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
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

	public AccountWrapper getObjectWrapper(TransferInputStream tins){
		account = new Account();
		account.setId(tins.readInt());
		account.setName(tins.readString());
		account.setAddress(tins.readString());
		return this;
	}
}
