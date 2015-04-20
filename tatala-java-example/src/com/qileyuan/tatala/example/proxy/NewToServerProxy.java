package com.qileyuan.tatala.example.proxy;

import com.qileyuan.tatala.example.service.TestManager;
import com.qileyuan.tatala.example.service.TestManagerImpl;
import com.qileyuan.tatala.example.service.model.AllTypeBean;
import com.qileyuan.tatala.example.service.model.TestAccount;
import com.qileyuan.tatala.socket.to.OrderedTransferObject;
import com.qileyuan.tatala.socket.to.TransferObject;

public class NewToServerProxy {
	private TestManager manager = new TestManagerImpl();

	public String sayHello(TransferObject baseto) {
		OrderedTransferObject to = (OrderedTransferObject)baseto;
		int Id = to.getInt();
		String name =  to.getString();

		String result = manager.sayHello(Id, name);
		return result;
	}

	public void doSomething(TransferObject to) {
		manager.doSomething();
	}
	
	public AllTypeBean allTypeTest(TransferObject baseto) {
		OrderedTransferObject to = (OrderedTransferObject)baseto;
		AllTypeBean allTypeBean = manager.getAllTypeBean(
				to.getBoolean(), to.getByte(),
				to.getShort(), to.getChar(), to.getInt(),
				to.getLong(), to.getFloat(),
				to.getDouble(), to.getDate(),
				to.getString());

		return allTypeBean;
	}
	
	public String[] getArray(TransferObject baseto) {
		OrderedTransferObject to = (OrderedTransferObject)baseto;
		byte[] bytearr = to.getByteArray();
		String[] strarr = to.getStringArray();

		String[] result = manager.getArray(bytearr, strarr);
		return result;
	}
	
	public TestAccount getAccount(TransferObject baseto) {
		OrderedTransferObject to = (OrderedTransferObject)baseto;
		TestAccount account = (TestAccount) to.getSerializable();
		TestAccount returnAccount = manager.getAccount(account);

		return returnAccount;
	}
}
