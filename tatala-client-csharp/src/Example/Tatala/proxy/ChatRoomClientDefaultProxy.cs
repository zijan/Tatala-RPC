using System;
using Example.tatala.client;
using QiLeYuan.Tatala.proxy;
using QiLeYuan.Tatala.socket.to;

namespace Example.tatala.proxy {
    public class ChatRoomClientDefaultProxy : DefaultProxy {

        public override Object execute(TransferObject baseto) {
            StandardTransferObject to = (StandardTransferObject)baseto;
            String calleeMethod = to.getCalleeMethod();
            if (calleeMethod.Equals("receiveMessage")) {
                String message = to.getString("message");
                ChatRoomClient.getInstance().receiveMessage(message);
            }

            return null;
        }
    }
}
