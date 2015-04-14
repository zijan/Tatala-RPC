using System;
using System.Collections.Generic;
using System.IO;
using QiLeYuan.Tatala.socket.to;
using Xstream.Core;
using QiLeYuan.Tools.debug;

/**
 * This class is the socket connection controller class, which manage the instances of 
 * socket server connections. A socket server controller class will be instantiated during 
 * site initialization. 
 * 
 * The Socket Connection Controller is responsible for accepting requests for initiation of 
 * connections to socket servers from the client components.
 *  
 * @author JimT
 *
 */
namespace QiLeYuan.Tatala.socket.client {

    public class SocketController {

        private static List<SocketConnection> connectionList = null;

        public delegate Object ExecuteDelegate(SocketConnection socketConnection, TransferObject transferObject);

        public static List<SocketConnection> ConnectionList {
            get {
                return connectionList;
            }
            set {
                connectionList = value;
            }
        }

        /**
         * Initialize socket server connections through xml configuration file.
         */
        public static void initialize() {
            //http://code.google.com/p/xstream-dot-net/
            StreamReader reader = File.OpenText("controller.xml");
            string xml = reader.ReadToEnd();
            reader.Close();

            XStream xstream = new XStream();
            xstream.Alias<List<SocketConnection>>("connections");
            xstream.Alias<SocketConnection>("connection");
            connectionList = xstream.FromXml<List<SocketConnection>>(xml);
        }


        /**
         * Dispatch request to a appointed socket connection.
         * @param to TransferObject
         * @return Object
         */
        public static object execute(TransferObject to) {
            Object retObject = null;

            if (connectionList == null) {
                initialize();
            }

            String connectionName = to.getConnectionName();

            foreach (SocketConnection connection in connectionList) {
                if (connection.getName().Equals(connectionName)) {
                    if (to.isAsynchronous()) {
                        ExecuteDelegate executeDelegate = delegate(SocketConnection socketConnection, TransferObject transferObject) {
                            return socketConnection.execute(transferObject);
                        };
                        IAsyncResult asyncResult = executeDelegate.BeginInvoke(connection, to, null, null);
                        Future future = new Future(executeDelegate, asyncResult);
                        retObject = future;
                    } else {
                        retObject = connection.execute(to);
                    }
                }
            }

            return retObject;
        }

    }
}