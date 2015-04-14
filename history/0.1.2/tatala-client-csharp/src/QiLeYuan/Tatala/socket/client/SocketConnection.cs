using System;
using System.Text;
using System.Threading;
using QiLeYuan.Tatala.socket.to;

/**
 * This class is the socket connection class, which is what makes the actual socket connection 
 * to a particular server. It stores the IP and port of the server it is connected to, 
 * timeout of socket object, number of retry times when create socket object, and the name of one connection.
 *  
 * @author JimT
 *
 */
namespace QiLeYuan.Tatala.socket.client {

    public class SocketConnection {

        private String hostIp;
        private int hostPort;
        private int timeout;
        private int retryCount;
        private String name;

        private ShortClientSession shortClientSession;
        private LongClientSession longClientSession;
        private static object theLock = new Object();

        /**
         * This method handles all outgoing and incoming data.
         * @param to TransferObject
         * @return Object
         */
        public Object execute(TransferObject to) {
		    if(to.isLongConnection()){
			    Monitor.Enter(theLock);
			    try {
				    if(longClientSession == null){
                        longClientSession = new LongClientSession(hostIp, hostPort, timeout, retryCount);
				    }
				    return longClientSession.start(to);
			    } finally {
                    Monitor.Exit(theLock);
			    }
		    }else{
			    if(shortClientSession == null){
                    shortClientSession = new ShortClientSession(hostIp, hostPort, timeout, retryCount);
			    }
			    return shortClientSession.start(to);
		    }
	    }


        public string HostIP{
            get {
                return hostIp;
            }
            set {
                hostIp = value;
            }
        }

        public int HostPort {
            get {
                return hostPort;
            }
            set {
                hostPort = value;
            }
        }

        public int Timeout {
            get {
                return timeout;
            }
            set {
                timeout = value;
            }
        }

        public int RetryCount {
            get {
                return retryCount;
            }
            set {
                retryCount = value;
            }
        }

        public string Name {
            get {
                return name;
            }
            set {
                name = value;
            }
        }

        public string getName() {
            return name;
        }

        public override String ToString() {
            String TAB = "    ";

            StringBuilder retValue = new StringBuilder();

            retValue.Append("SocketConnection ( ")
                .Append("hostIP = ").Append(this.hostIp).Append(TAB)
                .Append("hostPort = ").Append(this.hostPort).Append(TAB)
                .Append("timeout = ").Append(this.timeout).Append(TAB)
                .Append("retryCount = ").Append(this.retryCount).Append(TAB)
                .Append("name = ").Append(this.name).Append(TAB)
                .Append(" )");

            return retValue.ToString();
        }
    }
}