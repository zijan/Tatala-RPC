using System;
using QiLeYuan.Tatala.socket.client;
using QiLeYuan.Tatala.socket.to;

/**
 * This class is socket server target, provider socket distribution-method-call.
 * @author JimT
 *
 */
namespace QiLeYuan.Tatala.executor {

    public class SocketServerTarget : ServerTarget {

        public Object execute(TransferObject to) {
            Object resultObject = null;

            resultObject = SocketController.execute(to);

            return resultObject;
        }
    }
}