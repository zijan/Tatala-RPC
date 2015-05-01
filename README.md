# Tatala RPC

## Version 0.2.0 Release Note
* Code refactor
* Change ServerExecutor with socket only
* Take away retry times
* Remove short connection
* Add package for exception
* Remove any property file (controller.xml & tatala.properties)
* Change the way of default proxy call
* Change MappedTransferObject and OrderedTransferObject
* Fix when clien close, Receive error show up on server side
* More easy understand example code
* Support protobuf

## Version 0.1.2 Release Note (see history folder)
* Add set on EasyClient
* Add TatalaReturnException, so let client be able to rollback transaction
* fix bugs for List and Map on ServerProxy.java
* fix for multiple thread, void return method, may disorder on server side

## Overview
Tatala is an easy-to-use RPC middleware, cross language and cross platform, that convert method signature (include callee class name, target method name, the number of its arguments and server return) into byte array, communicate with client and server base on socket.

Right now, there are Tatala-Java (client & server) and Tatala-client-csharp available.

https://github.com/zijan/Tatala/wiki/Tatala-中文教程

## Features
* Easy-to-use quickly develop and setup a network communication component
* Cross language and platform
* High performance and distributed
* Binary communication protocol
* Support long socket connection
* Support multi thread on both client and server side
* Support synchronous or asynchronous method call
* Support compression for big content
* Support Server push message to client
* Support Server return runtime exception to clien side, so client be able to rollback transaction
* Support Google Protocol Buffers as object serializing solution
* Can use for cross-language RPC, high performance cache server, distributed message service, MMO game server……

## Get Started
Download tatala.jar from repository. If you're using ant, change your build.xml to include tatala.jar. If you're using eclipse, add the jar to your project build path.

As you known, easy-to-use is the first consideration among Tatala features. It can make developer create RPC just like local method call. They don’t have to care about socket or thread all kind stuff.

For example, we have server logic ExampleManager.class and ExampleManagerImpl.class.

ExampleManager.java
```java
public interface ExampleManager {
    public String sayHello(int Id, String name);
}
```

ExampleManagerImpl.java
```java
public class ExampleManagerImpl implements ExampleManager{
	public String sayHello(int Id, String name) {
		return "["+Id+"]"+"Hello "+name+" !";
	}
}
```
We need to create a socket server class, in order to deploy our server logic on server side. In this sample, socket server listener port is 10001.
ExampleServer.java
```java
public class ExampleServer {
	public static void main(String args[]) {
		int listenPort = 10001;
		int poolSize = 10;
		AioSocketServer server = new AioSocketServer(listenPort, poolSize);
		server.start();
	}
}
```
Then client side code is something like:
EasyClient.java
```java
public class EasyClient {
	private static TransferObjectFactory transferObjectFactory;
	private static ExampleManager manager;
	
	public static void main(String[] args) {
		transferObjectFactory = new TransferObjectFactory("127.0.0.1", 10001, 5000);
		transferObjectFactory.setImplClass("ExampleManagerImpl");
		manager = (ExampleManager)ClientProxyFactory.create(ExampleManager.class, transferObjectFactory);
		
		String result = manager.sayHello(18, "JimT");
		System.out.println("result: "+result);
	}
}
```
Create TransferObjectFactory object with server ip, port ant timeout, and set implement class name. Create a proxy, make method call. Of cause, client side need have that interface class (ExampleManager.class) in classpath. 

That is everything from server to client. Don't have any configuration files. It is so simple, right?

There are more examples on tutorial section.

https://github.com/zijan/Tatala/wiki/Tatala-Tutorial#tutorial

https://github.com/zijan/Tatala/wiki/Tatala-中文教程

## Support Type
Supported parameter and return type table

<table>
<tbody>
<tr><td><em>Type</em></td><td><em>Java</em></td><td><em>C#</em></td></tr>
<tr><td>bool</td><td>Y</td><td>Y</td></tr>
<tr><td>byte</td><td>Y</td><td>Y</td></tr>
<tr><td>short</td><td>Y</td><td>Y</td></tr>
<tr><td>chat</td><td>Y</td><td>Y</td></tr>
<tr><td>int</td><td>Y</td><td>Y</td></tr>
<tr><td>long</td><td>Y</td><td>Y</td></tr>
<tr><td>float</td><td>Y</td><td>Y</td></tr>
<tr><td>double</td><td>Y</td><td>Y</td></tr>
<tr><td>Date</td><td>Y</td><td>Y</td></tr>
<tr><td>String</td><td>Y</td><td>Y</td></tr>
<tr><td>byte[]</td><td>Y</td><td>Y</td></tr>
<tr><td>int[]</td><td>Y</td><td>Y</td></tr>
<tr><td>long[]</td><td>Y</td><td>Y</td></tr>
<tr><td>float[]</td><td>Y</td><td>Y</td></tr>
<tr><td>double[]</td><td>Y</td><td>Y</td></tr>
<tr><td>String[]</td><td>Y</td><td>Y</td></tr>
<tr><td>Serializable</td><td>Y</td><td>N</td></tr>
<tr><td>Protobuf</td><td>Y</td><td>Y</td></tr>
<tr><td>WrapperClass</td><td>Y</td><td>Y</td></tr>
</tbody>
</table>

## Other Notes
Require JDK1.7, because using Java AIO.

Third part libs include Protobuf and Log4j.
## License
This library is distributed under Apache License Version 2.0
## Contact
jimtang@hotmail.ca

QQ: 37287685
