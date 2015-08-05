using System;
using QiLeYuan.Tatala.util;
using QiLeYuan.Tatala.socket.to;

/**
 * This class is the distribution-method-call executor, which dispatches request to different target.
 * For now, it only supports local and socket distribution.
 * 
 * @author JimT
 *
 */
namespace QiLeYuan.Tatala.executor {

    public class ServerExecutor {
        private static ServerTarget target;
        public static Object execute(TransferObject to) {
            Object retObj = null;
            //for csharp client, server target is Socket.
            target = new SocketServerTarget();
            retObj = target.execute(to);
            return retObj;
        }
    }
}