using System;
using QiLeYuan.Tatala.socket.client;

namespace QiLeYuan.Tatala.socket.client {
    public class Future {
        private SocketController.ExecuteDelegate executeDelegate;
        private IAsyncResult asyncResult;

        public Future(SocketController.ExecuteDelegate executeDelegate, IAsyncResult asyncResult) {
            this.executeDelegate = executeDelegate;
            this.asyncResult = asyncResult;
        }

        public Object Get() {
            return executeDelegate.EndInvoke(asyncResult);
        }

        public bool IsDone() {
            return asyncResult.IsCompleted;
        }
    }
}
