package com.qileyuan.tatala.example.proxy;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.qileyuan.tatala.example.model.AllTypeBean;
import com.qileyuan.tatala.example.model.TestAccount;
import com.qileyuan.tatala.example.proxy.wrapper.AllTypeBeanWrapper;
import com.qileyuan.tatala.example.proxy.wrapper.TestAccountListWrapper;
import com.qileyuan.tatala.example.proxy.wrapper.TestAccountMapWrapper;
import com.qileyuan.tatala.example.proxy.wrapper.TestAccountWrapper;
import com.qileyuan.tatala.executor.ServerExecutor;
import com.qileyuan.tatala.socket.to.StandardTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;
import com.qileyuan.tatala.socket.to.TransferObjectFactory;

/**
 * This class is a sample for the socket server provider. It is a proxy class
 * which call real business logic through socket connection.
 * 
 * Follow these steps: 
 * 1)Create different TransferObjectFactory by passing different parameter. 
 * Parameter is socket connection name present different socket server.
 *  
 * 2)Create caller method running in client side. Create transfer
 * object by TransferObjectFactory. Set callee class, callee method and return
 * type. Put parameter into transfer object. Call ServerExecutor. 
 * 
 * @author JimT
 * 
 */
public class TestClientProxy {

	private TransferObjectFactory transferObjectFactory;

	public TestClientProxy(){
		//create long connection factory
		transferObjectFactory = new TransferObjectFactory("test1", true);
		//create short connection factory
		//transferObjectFactory = new TransferObjectFactory("test1");
	}
	
	public String sayHello(int Id, String name) {
		
		StandardTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("sayHello");
		to.registerReturnType(TransferObject.DATATYPE_STRING);

		to.putInt("Id", Id);
		to.putString("name", name);

		Object resultObj = ServerExecutor.execute(to);
		String result = (String) resultObj;

		return result;
	}
		
	public void doSomething() {
		StandardTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("doSomething");
		to.registerReturnType(TransferObject.DATATYPE_VOID);

		ServerExecutor.execute(to);
	}
	
	public void callServer(int Id) {
		StandardTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("callServer");
		to.putInt("Id", Id);
		to.registerReturnType(TransferObject.DATATYPE_VOID);

		ServerExecutor.execute(to);
	}

	public TestAccount getAccount(TestAccount account) throws Exception {
		StandardTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("getAccount");
		to.registerReturnType(TransferObject.DATATYPE_WRAPPER);

		TestAccountWrapper accountWrapper = new TestAccountWrapper(account);
		to.putWrapper("account", accountWrapper);
		//to.setLongConnection(true);

		accountWrapper = (TestAccountWrapper) ServerExecutor.execute(to);

		if(accountWrapper != null){
			TestAccount returnAccount = accountWrapper.getAccount();
			return returnAccount;
		}else{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public TestAccount getAccountAsynchronous(TestAccount account) throws Exception {
		StandardTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("getAccount");
		to.registerReturnType(TransferObject.DATATYPE_WRAPPER);

		TestAccountWrapper accountWrapper = new TestAccountWrapper(account);
		to.putWrapper("account", accountWrapper);

		to.setAsynchronous(true);

		Future<TestAccountWrapper> future = (Future<TestAccountWrapper>) ServerExecutor.execute(to);

		accountWrapper = future.get();
		TestAccount returnAccount = accountWrapper.getAccount();

		return returnAccount;
	}

	public TestAccount getAccountCompress(TestAccount account) throws Exception {
		StandardTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("getAccount");
		to.registerReturnType(TransferObject.DATATYPE_WRAPPER);

		TestAccountWrapper accountWrapper = new TestAccountWrapper(account);
		to.putWrapper("account", accountWrapper);
		to.setCompress(true);

		accountWrapper = (TestAccountWrapper) ServerExecutor.execute(to);
		TestAccount returnAccount = accountWrapper.getAccount();

		return returnAccount;
	}

	public TestAccount getAccountDefaultProxy(TestAccount account) {
		StandardTransferObject to = transferObjectFactory.createTransferObject();
		
		//don't set callee class, handle with default server proxy
		//to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("getAccount");
		to.registerReturnType(TransferObject.DATATYPE_WRAPPER);

		TestAccountWrapper accountWrapper = new TestAccountWrapper(account);
		to.putWrapper("account", accountWrapper);

		accountWrapper = (TestAccountWrapper) ServerExecutor.execute(to);
		if (accountWrapper == null) {
			return null;
		}
		TestAccount returnAccount = accountWrapper.getAccount();

		return returnAccount;
	}

	public TestAccount getAccountSerializable(TestAccount account) {
		StandardTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("getAccountSerializable");
		to.registerReturnType(TransferObject.DATATYPE_SERIALIZABLE);
		to.putSerializable("account", account);

		TestAccount returnAccount = (TestAccount) ServerExecutor.execute(to);

		return returnAccount;
	}

	public TestAccount getAccount2(TestAccount account, TestAccount account2) {
		StandardTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("getAccount2");
		to.registerReturnType(TransferObject.DATATYPE_WRAPPER);

		TestAccountWrapper accountWrapper = new TestAccountWrapper(account);
		to.putWrapper("account", accountWrapper);
		TestAccountWrapper accountWrapper2 = new TestAccountWrapper(account2);
		to.putWrapper("account2", accountWrapper2);

		accountWrapper = (TestAccountWrapper) ServerExecutor.execute(to);
		
		if(accountWrapper != null){
			TestAccount returnAccount = accountWrapper.getAccount();
			return returnAccount;
		}else{
			return null;
		}
	}


	public List<TestAccount> getAccountList(List<TestAccount> accountList) {

		StandardTransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("getAccountList");
		to.registerReturnType(TransferObject.DATATYPE_WRAPPER);

		TestAccountListWrapper testAccountListWrapper = new TestAccountListWrapper(accountList);
		to.putWrapper("accountList", testAccountListWrapper);
		testAccountListWrapper = (TestAccountListWrapper) ServerExecutor.execute(to);

		if(testAccountListWrapper != null){
			List<TestAccount> retaccountList = testAccountListWrapper.getTestAccountList();
			return retaccountList;
		}else{
			return null;
		}
	}
	
	public Map<String, TestAccount> getAccountMap(Map<String, TestAccount> accountMap) {

		StandardTransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("getAccountMap");
		to.registerReturnType(TransferObject.DATATYPE_WRAPPER);

		TestAccountMapWrapper testAccountMapWrapper = new TestAccountMapWrapper(accountMap);
		to.putWrapper("accountMap", testAccountMapWrapper);
		testAccountMapWrapper = (TestAccountMapWrapper) ServerExecutor.execute(to);

		if(testAccountMapWrapper != null){
			Map<String, TestAccount> retaccountMap = testAccountMapWrapper.getTestAccountMap();
			return retaccountMap;
		}else{
			return null;
		}
	}
	
	public AllTypeBean getAllTypeBean(boolean aboolean, byte abyte,
			short ashort, char achar, int aint, long along, float afloat,
			double adouble, Date adate, String astring) {

		StandardTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("getAllTypeBean");
		to.registerReturnType(TransferObject.DATATYPE_WRAPPER);

		to.putBoolean("aboolean", aboolean);
		to.putByte("abyte", abyte);
		to.putShort("ashort", ashort);
		to.putChar("achar", achar);
		to.putInt("aint", aint);
		to.putLong("along", along);
		to.putFloat("afloat", afloat);
		to.putDouble("adouble", adouble);
		to.putDate("adate", adate);
		to.putString("astring", astring);

		AllTypeBeanWrapper allTypeBeanWrapper = (AllTypeBeanWrapper) ServerExecutor.execute(to);
		if(allTypeBeanWrapper != null){
			return allTypeBeanWrapper.getAllTypeBean();
		}else{
			return null;
		}
		
	}

	public String[] getArray(byte[] bytearr, String[] strarr) {

		StandardTransferObject to = transferObjectFactory.createTransferObject();

		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("getArray");
		to.registerReturnType(TransferObject.DATATYPE_STRINGARRAY);

		to.putByteArray("bytearr", bytearr);
		to.putStringArray("strarr", strarr);

		Object resultObj = ServerExecutor.execute(to);
		String[] result = (String[]) resultObj;

		return result;
	}
}
