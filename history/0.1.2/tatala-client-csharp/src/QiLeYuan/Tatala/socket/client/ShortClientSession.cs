using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using QiLeYuan.Tatala.socket.to;
using QiLeYuan.Tatala.socket.util;
using QiLeYuan.Tools.debug;

namespace QiLeYuan.Tatala.socket.client {
    public class ShortClientSession {

        private String hostIp;
        private int hostPort;
        private int timeout;
        private int retryTime;

        // State object for receiving data from remote device.
        public class StateObject {
            // Client socket.
            public Socket workSocket = null;
            // Size of receive buffer.
            public const int BufferSize = 256;
            // Receive buffer.
            public byte[] buffer = new byte[BufferSize];
            // TransferObject
            public TransferObject transferObject = null;
            // Received data
            public MemoryStream memoryStream = new MemoryStream();
        }

        public ShortClientSession(String hostIp, int hostPort, int timeout, int retryTime) {
            this.hostIp = hostIp;
            this.hostPort = hostPort;
            this.timeout = timeout;
            this.retryTime = retryTime;
        }

        public Object start(TransferObject to) {
            Object resultObject = null;

            String calleeClass = to.getCalleeClass();
            String calleeMethod = to.getCalleeMethod();

            StateObject stateObject = new StateObject();
            stateObject.transferObject = to;

            try {
                if (Connect(stateObject)) {

                    Send(stateObject);

                    resultObject = Receive(stateObject);
                }
            } catch (Exception e) {
                Logging.LogError("Callee Class and Method: [" + calleeClass + "." + calleeMethod + "]");
                Logging.LogError(e.ToString());
            } finally {
                Close(stateObject);
            }

            return resultObject;
        }

        private bool Connect(StateObject stateObject) {
            Socket client = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            IPEndPoint ipEndPoint = new IPEndPoint(IPAddress.Parse(hostIp), hostPort);

            stateObject.workSocket = client;

            int retry = retryTime;
            while (true) {
                try {
                    // Connect to the remote endpoint.
                    client.Connect(ipEndPoint);
                    break;
                } catch (Exception be) {
                    retry--;
                    Logging.LogError(be.Message);
                    Logging.LogError("Retry time: " + retry);
                }
                if (retry <= 0) {
                    Logging.LogError("Connecntion Error.");
                    return false;
                }
            }

            client.SendTimeout = timeout;
            client.ReceiveTimeout = timeout;

            return true;
        }

        private void Send(StateObject stateObject) {
            TransferObject to = stateObject.transferObject;
            Socket client = stateObject.workSocket;

            byte[] sendData = TransferUtil.transferObjectToByteArray(to);

            client.Send(sendData);
        }

        private Object Receive(StateObject stateObject) {
            Object resultObject = null;

            //if return void, don't call socket receive
            if (stateObject.transferObject.getReturnType() == TransferObject.DATATYPE_VOID) {
                return resultObject;
            }

            Socket client = stateObject.workSocket;
            int receiveLength = 0;
            int expectReceiveLength = 0;
            bool firsttime = true;
            while (true) {
                int bytesReceived = client.Receive(stateObject.buffer, 0, StateObject.BufferSize, SocketFlags.None);

                stateObject.memoryStream.Write(stateObject.buffer, 0, bytesReceived);
                receiveLength += bytesReceived;

                //check whether one server response is done or not
                if (bytesReceived < StateObject.BufferSize) {
                    break;
                }
                if (bytesReceived == StateObject.BufferSize) {
                    if (firsttime) {
                        expectReceiveLength = TransferUtil.getExpectReceiveLength(stateObject.buffer);
                    }
                    //if expectReceiveLength equal receiveLength, the server response is done
                    if (expectReceiveLength == receiveLength) {
                        break;
                    }
                }
                firsttime = false;
            }

            byte[] receiveData = stateObject.memoryStream.ToArray();
            resultObject = TransferUtil.byteArrayToReturnObject(receiveData);

            return resultObject;
        }

        private void Close(StateObject stateObject) {
            stateObject.memoryStream.Close();
            if (stateObject.workSocket.Connected) {
                stateObject.workSocket.Shutdown(SocketShutdown.Both);
            }
            stateObject.workSocket.Close();
        }
    }
}
