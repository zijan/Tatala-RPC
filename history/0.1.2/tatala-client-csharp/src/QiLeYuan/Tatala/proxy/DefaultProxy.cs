using System;
using QiLeYuan.Tatala.socket.to;
using QiLeYuan.Tools.debug;

namespace QiLeYuan.Tatala.proxy {
    public class DefaultProxy {
        public virtual Object execute(TransferObject to) {
            Logging.LogError("This is DefaultProxy. You need extend it by specific proxy.");
            return null;
        }
    }
}
