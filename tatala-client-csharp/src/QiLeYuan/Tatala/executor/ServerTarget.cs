using System;
using QiLeYuan.Tatala.socket.to;

namespace QiLeYuan.Tatala.executor {

    public interface ServerTarget {
        Object execute(TransferObject to);
    }
}