package com.qileyuan.tatala.example.proxy;

import java.util.Date;

import com.qileyuan.tatala.example.service.model.AllTypeBean;
import com.qileyuan.tatala.example.service.model.Account;
import com.qileyuan.tatala.executor.ServerExecutor;
import com.qileyuan.tatala.socket.to.OrderedTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.to.TransferObjectFactory;

public class NewToClientProxy {

	private String IP = "127.0.0.1";
	private int PORT = 10001;
	private int TIMEOUT = 5000;
	
	private TransferObjectFactory transferObjectFactory;

	public NewToClientProxy(){
		transferObjectFactory = new TransferObjectFactory(IP, PORT, TIMEOUT);
		transferObjectFactory.setCalleeClass("com.qileyuan.tatala.example.proxy.NewToServerProxy");
	}
	
	public String sayHello(int Id, String name) {
		
		OrderedTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeMethod("sayHello");
		to.registerReturnType(TransferObject.DATATYPE_STRING);

		to.putInt(Id);
		to.putString(name);

		Object resultObj = ServerExecutor.execute(to);
		String result = (String) resultObj;

		return result;
	}
	
	public void doSomething() {
		OrderedTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeMethod("doSomething");
		to.registerReturnType(TransferObject.DATATYPE_VOID);

		ServerExecutor.execute(to);
	}
	
	public AllTypeBean allTypeTest(boolean aboolean, byte abyte,
			short ashort, char achar, int aint, long along, float afloat,
			double adouble, Date adate, String astring){
		
		OrderedTransferObject to = transferObjectFactory.createTransferObject();
		
		to.setCalleeMethod("allTypeTest");
		to.registerReturnType(TransferObject.DATATYPE_SERIALIZABLE);
		to.putBoolean(aboolean);
		to.putByte(abyte);
		to.putShort(ashort);
		to.putChar(achar);
		to.putInt(aint);
		to.putLong(along);
		to.putFloat(afloat);
		to.putDouble(adouble);
		to.putDate(adate);
		to.putString(astring);
		
		AllTypeBean bean = (AllTypeBean)ServerExecutor.execute(to);
		return bean;
	}
	
	public String[] getArray(byte[] bytearr, String[] strarr) {

		OrderedTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeMethod("getArray");
		to.registerReturnType(TransferObject.DATATYPE_STRINGARRAY);

		to.putByteArray(bytearr);
		to.putStringArray(strarr);

		Object resultObj = ServerExecutor.execute(to);
		String[] result = (String[]) resultObj;

		return result;
	}
	
	public Account getAccountCompress(Account account) {
		OrderedTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeMethod("getAccount");
		to.registerReturnType(TransferObject.DATATYPE_SERIALIZABLE);

		to.putSerializable(account);
		to.setCompress(true);

		Account ret = (Account) ServerExecutor.execute(to);
		
		return ret;
	}
}
