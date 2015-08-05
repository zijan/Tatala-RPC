using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Reflection;
using QiLeYuan.Tatala.proxy;
using QiLeYuan.Tatala.socket.to;
using QiLeYuan.Tatala.socket.util;
using QiLeYuan.Tatala.util;
using QiLeYuan.Tools.debug;
using System.Threading;

namespace QiLeYuan.Tatala.socket.client {
    public class LongClientSession {

        private const int QUEUE_SIZE = 10;
        public const string METHOD_CLOSE = "close";

        private String hostIp;
        private int hostPort;
        private int timeout;
        private int retryCount;

        private StateObject mStateObject = new StateObject();
        private bool closed;

        private bool handleFlag = false;
        private bool firsttime = true;
        private int expectReceiveLength = 0;
        private int receiveLength = 0;
        private BlockingQueue receiveQueue = new BlockingQueue(QUEUE_SIZE);
        private DefaultProxy serverCallProxy;

        public delegate void ExecuteDelegate();

        // State object for receiving data from remote device.
        public class StateObject {
            // Client socket.
            public Socket workSocket = null;
            // Size of receive buffer.
            public const int BUFFER_SIZE = 256 * 8;
            // Receive buffer.
            public byte[] buffer = new byte[BUFFER_SIZE];
            // TransferObject
            public TransferObject transferObject = null;
            // Received data
            public MemoryStream memoryStream = new MemoryStream();
        }

        public LongClientSession(String hostIp, int hostPort, int timeout, int retryCount) {
            this.hostIp = hostIp;
            this.hostPort = hostPort;
            this.timeout = timeout;
            this.retryCount = retryCount;
        }

        public Object start(TransferObject to) {
            Object resultObject = null;

            String calleeClass = to.getCalleeClass();
            String calleeMethod = to.getCalleeMethod();

            mStateObject.transferObject = to;

            //check if close method, close client
            if (calleeMethod == METHOD_CLOSE) {
                Close(mStateObject);
                return resultObject;
            }

            //set default server call proxy
            if (to.getServerCallProxy() != null) {
                serverCallProxy = to.getServerCallProxy();
            }

            try {
                if (mStateObject.workSocket == null || !mStateObject.workSocket.Connected || closed) {
                    Connect();
                }
                Send(mStateObject);
                resultObject = Receive(mStateObject);

            } catch (Exception e) {
                Logging.LogError("HostIp and Port: [" + hostIp + ":" + hostPort + "]");
                Logging.LogError("Callee Class and Method: [" + calleeClass + "." + calleeMethod + "]");
                Logging.LogError(e.ToString());
                Close(mStateObject);
                throw e;
            }
            return resultObject;
        }

        private void Connect() {
            //reset close flag is false
            closed = false;

            Socket socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            IPEndPoint ipEndPoint = new IPEndPoint(IPAddress.Parse(hostIp), hostPort);

            mStateObject.workSocket = socket;

            int retry = retryCount;
            while (true) {
                try {
                    // Connect to the remote endpoint.
                    IAsyncResult result = socket.BeginConnect(ipEndPoint, null, null);
                    bool success = result.AsyncWaitHandle.WaitOne(timeout);
                    if (!success) {
                        retry--;
                        Logging.LogError("Connect timeout");
                        Logging.LogError("Retry count: " + retry);
                    } else {
                        break;
                    }

                } catch (Exception be) {
                    retry--;
                    Logging.LogError(be.Message);
                    Logging.LogError("Retry count: " + retry);
                }
                if (retry <= 0) {
                    Logging.LogError("Connection Error.");
                    throw new Exception("Connection Error.");
                }
            }

            socket.SendTimeout = timeout;
            socket.ReceiveTimeout = timeout;

            //when connect to the server, keep receiving data either server response or server call
            Receive();
        }

        private void Receive() {
            //if handleFlag is true, execute
            if (handleFlag) {
                byte[] receiveData = mStateObject.memoryStream.ToArray();
                if (TransferUtil.isServerCall(receiveData[0])) {
                    //it is server call
                    ExecuteDelegate executeDelegate = delegate() {
                        handleServerCall(receiveData);
                    };
                    executeDelegate.BeginInvoke(null, null);

                    //ThreadPool.QueueUserWorkItem(handleServerCall, receiveData);

                } else {
                    //it is server response
                    receiveQueue.Enqueue(receiveData);
                }

                handleFlag = false;
                firsttime = true;
                expectReceiveLength = 0;
                receiveLength = 0;

                //reset memory stream
                mStateObject.memoryStream.Position = 0;
                mStateObject.memoryStream.SetLength(0);
            }
            Socket socket = mStateObject.workSocket;
            socket.BeginReceive(mStateObject.buffer, 0, StateObject.BUFFER_SIZE, SocketFlags.None, new AsyncCallback(ReceiveCallback), mStateObject);
        }

        private void ReceiveCallback(IAsyncResult ar) {
            // Retrieve the state object and the client socket 
            // from the asynchronous state object.

            StateObject state = (StateObject)ar.AsyncState;

            try {
                Socket socket = state.workSocket;

                if (socket == null || !socket.Connected) {
                    return;
                }

                // Read data from the remote device.
                int bytesReceived = socket.EndReceive(ar);

                if (bytesReceived > 0) {
                    // There might be more data, so store the data received so far.
                    state.memoryStream.Write(mStateObject.buffer, 0, bytesReceived);

                    //check whether one receive from server is done
                    CheckOneReceiveDone(bytesReceived, mStateObject.buffer);

                    Receive();
                } else {
                    //socket may colsed, due to network issue
                    Close(state);
                }
            } catch (Exception e) {
                Logging.LogError(e.ToString());
                Close(state);
            }
        }

        private void Send(StateObject stateObject) {
            TransferObject to = stateObject.transferObject;
            Socket socket = stateObject.workSocket;

            byte[] sendData = TransferUtil.transferObjectToByteArray(to);

            //socket.Send(sendData);

            // Begin sending the data to the remote device.     
            socket.BeginSend(sendData, 0, sendData.Length, SocketFlags.None, new AsyncCallback(SendCallback), socket);
        }

        private void SendCallback(IAsyncResult ar) {
            try {
                // Retrieve the socket from the state object.     
                Socket socket = (Socket)ar.AsyncState;
                // Complete sending the data to the remote device.     
                socket.EndSend(ar);
            } catch (Exception e) {
                Logging.LogError("Send error: "+e.ToString());
            }
        }

        private Object Receive(StateObject stateObject) {
            Object resultObject = null;

            //if return void, don't call socket receive
            if (stateObject.transferObject.getReturnType() == TransferObject.DATATYPE_VOID) {
                return resultObject;
            }

            //if receiveQueue is empty, wait a while, until server response come 
            byte[] receiveData = (byte[])receiveQueue.Dequeue(timeout);

            if (receiveData != null) {
                resultObject = TransferUtil.byteArrayToReturnObject(receiveData);
            } else {
                Logging.LogError("receive timeout return null");
            }
            return resultObject;
        }

        public void CheckOneReceiveDone(int receivedCount, byte[] receiveData) {
            receiveLength += receivedCount;

            //if finish receive one client request, set handle flag is true
            if (receivedCount < StateObject.BUFFER_SIZE) {
                handleFlag = true;
            }
            if (receivedCount == StateObject.BUFFER_SIZE) {
                if (firsttime) {
                    expectReceiveLength = TransferUtil.getExpectReceiveLength(receiveData);
                }
                //if expectReceiveLength equal receiveLength, the server response is done
                if (expectReceiveLength == receiveLength) {
                    handleFlag = true;
                }
            }

            firsttime = false;
        }

        private void Close(StateObject stateObject) {
            closed = true;
            if (stateObject.workSocket.Connected) {
                stateObject.workSocket.Shutdown(SocketShutdown.Both);
            }
            stateObject.workSocket.Close();
        }

        private void handleServerCall(object receiveObj) {
            byte[] receiveData = (byte[])receiveObj;
            TransferObject to = new StandardTransferObject();
            try {
                to = TransferUtil.byteArrayToTransferObject(receiveData);
                execute(to);
            } catch (Exception e) {
                Logging.LogError("Callee Class and Method: [" + to.getCalleeClass() + "." + to.getCalleeMethod() + "]");
                Logging.LogError("Handle Receive Data error: " + e);
                Close(mStateObject);
            }
        }

        private void execute(TransferObject to) {

            String calleeClassName = to.getCalleeClass();

            //Check default proxy, don't need reflection.
            if (calleeClassName.Equals(TransferObject.DEFAULT_PROXY)) {
                if (serverCallProxy != null) {
                    serverCallProxy.execute(to);
                }
            } else {
                String calleeMethod = to.getCalleeMethod();
                Type type = Type.GetType(calleeClassName);
                Object instance = Activator.CreateInstance(type);
                MethodInfo method = type.GetMethod(calleeMethod);
                method.Invoke(instance, new object[] { to });
            }

        }

    }
}
