package com.qileyuan.tatala.example.proxy;

import com.qileyuan.tatala.example.proxy.wrapper.AllTypeBeanWrapper;
import com.qileyuan.tatala.example.proxy.wrapper.TestAccountWrapper;
import com.qileyuan.tatala.example.service.TestManager;
import com.qileyuan.tatala.example.service.TestManagerImpl;
import com.qileyuan.tatala.example.service.model.AllTypeBean;
import com.qileyuan.tatala.example.service.model.TestAccount;
import com.qileyuan.tatala.proxy.DefaultProxy;
import com.qileyuan.tatala.socket.to.StandardTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;

/**
 * This class is a sample for the socket server provider. It is default proxy class
 * which call real business logic through socket connection.
 * @author JimT
 *
 */
public class TestDefaultProxy extends DefaultProxy{
	private TestManager manager = new TestManagerImpl();
	
	public Object execute(TransferObject abstractto){
		StandardTransferObject to = (StandardTransferObject)abstractto;
		String calleeMethod = to.getCalleeMethod();
		
		if(calleeMethod.equals("sayHello")){
			int Id = (Integer)to.getInt("Id")==null ? 0 : to.getInt("Id");
			String name = to.getString("name");
			String result = manager.sayHello(Id, name);
			return result;
		}else if(calleeMethod.equals("doSomething")){
			manager.doSomething();
		}else if(calleeMethod.equals("getAccount")){
			TestAccountWrapper accountWrapper = (TestAccountWrapper) to.getWrapper("account");
			TestAccount account = accountWrapper.getAccount();
			//call real service code
			TestAccount returnAccount = manager.getAccount(account);
			accountWrapper.setAccount(returnAccount);
			return accountWrapper;
		}else if(calleeMethod.equals("getAllTypeBean")){
			AllTypeBean allTypeBean = manager.getAllTypeBean(
					to.getBoolean("aboolean"), to.getByte("abyte"),
					to.getShort("ashort"), to.getChar("achar"), to.getInt("aint"),
					to.getLong("along"), to.getFloat("afloat"),
					to.getDouble("adouble"), to.getDate("adate"),
					to.getString("astring"));

			AllTypeBeanWrapper allTypeBeanWrapper = new AllTypeBeanWrapper(allTypeBean);
			return allTypeBeanWrapper;
		}else if(calleeMethod.equals("getArray")){
			byte[] bytearr = to.getByteArray("bytearr");
			String[] strarr = to.getStringArray("strarr");

			String[] result = manager.getArray(bytearr, strarr);
			return result;
		}
		
		return null;
	}
}
