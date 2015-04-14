using Example.tatala.model;
using QiLeYuan.Tatala.socket.io;
using QiLeYuan.Tatala.socket.to;
using QiLeYuan.Tatala.socket.util;

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
namespace com.qileyuan.tatala.example.proxy.wrapper {

    public class TestAccountWrapper : TransferObjectWrapper {
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

        public int getLength() {
            return TransferUtil.getLengthOfInt() +
                   TransferUtil.getLengthOfString(account.getName()) +
                   TransferUtil.getLengthOfString(account.getAddress());
        }

        public void getByteArray(TransferOutputStream touts) {
            touts.writeInt(account.getId());
            touts.writeString(account.getName());
            touts.writeString(account.getAddress());
        }

        public TransferObjectWrapper getObjectWrapper(TransferInputStream tins) {
            account = new TestAccount();
            account.setId(tins.readInt());
            account.setName(tins.readString());
            account.setAddress(tins.readString());
            return this;
        }
    }
}