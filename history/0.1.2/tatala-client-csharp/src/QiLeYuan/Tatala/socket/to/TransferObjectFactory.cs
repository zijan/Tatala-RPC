using QiLeYuan.Tatala.proxy;

namespace QiLeYuan.Tatala.socket.to {

    public class TransferObjectFactory {

        private string connectionName;
        private bool longConnection;
        private DefaultProxy serverCallProxy;

        private string calleeClass;
        private bool compress;

        public TransferObjectFactory(string connectionName) {
            this.connectionName = connectionName;
        }

        public TransferObjectFactory(string connectionName, bool longConnection) {
            this.connectionName = connectionName;
            this.longConnection = longConnection;
        }

        public void registerServerCallProxy(DefaultProxy serverCallProxy) {
            this.serverCallProxy = serverCallProxy;
        }

        public void setCalleeClass(string calleeClass) {
            this.calleeClass = calleeClass;
        }

        public bool isCompress() {
            return compress;
        }

        public void setCompress(bool compress) {
            this.compress = compress;
        }

        public StandardTransferObject createTransferObject() {
            StandardTransferObject to = new StandardTransferObject();
            to.setConnectionName(connectionName);
            to.setLongConnection(longConnection);
            to.setServerCallProxy(serverCallProxy);

            if (calleeClass != null) {
                to.setCalleeClass(calleeClass);
            }
            to.setCompress(compress);
            return to;
        }

        public NewTransferObject createNewTransferObject() {
            NewTransferObject to = new NewTransferObject();
            to.setConnectionName(connectionName);
            to.setLongConnection(longConnection);
            to.setServerCallProxy(serverCallProxy);
            if (calleeClass != null) {
                to.setCalleeClass(calleeClass);
            }
            to.setCompress(compress);
            to.setNewVersion(true);
            return to;
        }
    }
}
