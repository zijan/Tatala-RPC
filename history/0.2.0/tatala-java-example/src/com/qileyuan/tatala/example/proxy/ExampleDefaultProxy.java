package com.qileyuan.tatala.example.proxy;

import com.qileyuan.tatala.example.service.ExampleManager;
import com.qileyuan.tatala.example.service.ExampleManagerImpl;
import com.qileyuan.tatala.example.service.model.Account;
import com.qileyuan.tatala.example.service.model.AllTypeBean;
import com.qileyuan.tatala.example.service.model.wrapper.AllTypeBeanWrapper;
import com.qileyuan.tatala.proxy.DefaultProxy;
import com.qileyuan.tatala.socket.to.TransferObject;

/**
 * This class is a sample for the socket server provider. It is default proxy class
 * which call real business logic through socket connection.
 * @author JimT
 *
 */
public class ExampleDefaultProxy extends DefaultProxy{
	private ExampleManager manager = new ExampleManagerImpl();
	
	public Object execute(TransferObject to){
		String calleeMethod = to.getCalleeMethod();
		
		if(calleeMethod.equals("sayHello")){
			int Id = to.getInt();
			String name = to.getString();
			String result = manager.sayHello(Id, name);
			return result;
			
		}else if(calleeMethod.equals("doSomething")){
			manager.doSomething();
			
		}else if(calleeMethod.equals("getAccount")){
			Account account = (Account) to.getSerializable();
			//call real service code
			Account returnAccount = manager.getAccount(account);
			return returnAccount;
			
		}else if(calleeMethod.equals("getAllTypeBean")){
			AllTypeBean allTypeBean = manager.getAllTypeBean(
					to.getBoolean(), to.getByte(),
					to.getShort(), to.getChar(), to.getInt(),
					to.getLong(), to.getFloat(),
					to.getDouble(), to.getDate(),
					to.getString());

			AllTypeBeanWrapper allTypeBeanWrapper = new AllTypeBeanWrapper(allTypeBean);
			return allTypeBeanWrapper;
			
		}else if(calleeMethod.equals("getArray")){
			byte[] bytearr = to.getByteArray();
			String[] strarr = to.getStringArray();

			String[] result = manager.getArray(bytearr, strarr);
			return result;
		}
		
		return null;
	}
}
