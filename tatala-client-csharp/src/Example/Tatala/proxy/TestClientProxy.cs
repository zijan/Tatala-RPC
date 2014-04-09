using System;
using com.qileyuan.tatala.example.proxy.wrapper;
using Example.tatala.model;
using QiLeYuan.Tatala.executor;
using QiLeYuan.Tatala.socket.client;
using QiLeYuan.Tatala.socket.to;

namespace Example.tatala.proxy {
    public class TestClientProxy {
        private TransferObjectFactory transferObjectFactory = new TransferObjectFactory("test1", true);

        public String sayHello(int Id, String name) {
            NewTransferObject to = transferObjectFactory.createNewTransferObject();
            to.setCalleeClass("com.qileyuan.tatala.example.proxy.NewToServerProxy");
            to.setCalleeMethod("sayHello");
            to.registerReturnType(TransferObject.DATATYPE_STRING);

            to.putInt(Id);
            to.putString(name);

            Object resultObj = ServerExecutor.execute(to);
            String result = (String)resultObj;

            return result;
        }

        public void doSomething() {
            NewTransferObject to = transferObjectFactory.createNewTransferObject();
            to.setCalleeClass("com.qileyuan.tatala.example.proxy.NewToServerProxy");
            to.setCalleeMethod("doSomething");
            to.registerReturnType(TransferObject.DATATYPE_VOID);
            ServerExecutor.execute(to);
        }

        public AllTypeBean getAllTypeBean(bool aboolean, byte abyte,
                short ashort, char achar, int aint, long along, float afloat,
                double adouble, DateTime adate, String astring) {

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

            AllTypeBeanWrapper allTypeBeanWrapper = (AllTypeBeanWrapper)ServerExecutor.execute(to);
            if (allTypeBeanWrapper != null) {
                return allTypeBeanWrapper.getAllTypeBean();
            } else {
                return null;
            }

        }

        public String[] getArray(int[] intarr, String[] strarr) {

            NewTransferObject to = transferObjectFactory.createNewTransferObject();
            to.setCalleeClass("com.qileyuan.tatala.example.proxy.NewToServerProxy");
            to.setCalleeMethod("getArray");
            to.registerReturnType(TransferObject.DATATYPE_STRINGARRAY);

            to.putIntArray(intarr);
            to.putStringArray(strarr);

            Object resultObj = ServerExecutor.execute(to);
            String[] result = (String[])resultObj;

            return result;
        }

        public TestAccount getAccount(TestAccount account) {
            StandardTransferObject to = transferObjectFactory.createTransferObject();

            to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
            to.setCalleeMethod("getAccount");
            to.registerReturnType(TransferObject.DATATYPE_WRAPPER);

            TestAccountWrapper accountWrapper = new TestAccountWrapper(account);
            to.putWrapper("account", accountWrapper);

            accountWrapper = (TestAccountWrapper)ServerExecutor.execute(to);

            if (accountWrapper != null) {
                TestAccount returnAccount = accountWrapper.getAccount();
                return returnAccount;
            } else {
                return null;
            }

        }

        public TestAccount getAccountCompress(TestAccount account) {
            StandardTransferObject to = transferObjectFactory.createTransferObject();

            to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
            to.setCalleeMethod("getAccount");
            to.registerReturnType(TransferObject.DATATYPE_WRAPPER);

            TestAccountWrapper accountWrapper = new TestAccountWrapper(account);
            to.putWrapper("account", accountWrapper);
            to.setCompress(true);

            accountWrapper = (TestAccountWrapper)ServerExecutor.execute(to);

            if (accountWrapper != null) {
                TestAccount returnAccount = accountWrapper.getAccount();
                return returnAccount;
            } else {
                return null;
            }
        }

        public TestAccount getAccountAsynchronous(TestAccount account) {
            StandardTransferObject to = transferObjectFactory.createTransferObject();

            to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
            to.setCalleeMethod("getAccount");
            to.registerReturnType(TransferObject.DATATYPE_WRAPPER);

            TestAccountWrapper accountWrapper = new TestAccountWrapper(account);
            to.putWrapper("account", accountWrapper);
            to.setAsynchronous(true);

            Future future = (Future)ServerExecutor.execute(to);

            accountWrapper = (TestAccountWrapper)future.Get();
            TestAccount returnAccount = accountWrapper.getAccount();

            return returnAccount;
        }
    }
}
