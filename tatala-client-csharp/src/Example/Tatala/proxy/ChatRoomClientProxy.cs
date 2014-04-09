using System;
using QiLeYuan.Tatala.executor;
using QiLeYuan.Tatala.proxy;
using QiLeYuan.Tatala.socket.to;

namespace Example.tatala.proxy {
    public class ChatRoomClientProxy {
        private TransferObjectFactory transferObjectFactory = new TransferObjectFactory("test1", true);

        public ChatRoomClientProxy(){
            DefaultProxy clientDefaultProxy = new ChatRoomClientDefaultProxy();
            transferObjectFactory.registerServerCallProxy(clientDefaultProxy);
        }

        public void login(String username) {
            StandardTransferObject to = transferObjectFactory.createTransferObject();
            to.setCalleeClass("com.qileyuan.tatala.example.proxy.ChatRoomServerProxy");
            to.setCalleeMethod("login");
            to.putString("username", username);
            to.registerReturnType(TransferObject.DATATYPE_VOID);
            ServerExecutor.execute(to);
        }

        public void sendMessage(String message) {
            StandardTransferObject to = transferObjectFactory.createTransferObject();
            to.setCalleeClass("com.qileyuan.tatala.example.proxy.ChatRoomServerProxy");
            to.setCalleeMethod("receiveMessage");
            to.putString("message", message);
            to.registerReturnType(TransferObject.DATATYPE_VOID);
            ServerExecutor.execute(to);
        }
    }
}
