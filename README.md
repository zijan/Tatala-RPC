# TATALA

## Overview
Tatala is an easy-to-use RPC middleware, cross language and cross platform, that convert method signature (include callee class name, target method name, the number of its arguments and server return) into byte array, communicate with client and server base on socket.

Right now, there are Tatala-Java (client & server) and Tatala-client-csharp available.

## Features
* Easy-to-use quickly develop and setup a network component
* Cross language and platform
* High performance and distributed
* Binary communication protocol
* Support long and short socket connection
* Support multi thread on both client and server side
* Support synchronous or asynchronous method call
* Support compression for big content
* Can use for cross-language RPC, high performance cache server, distributed message service, MMO game server……

## Get Started
Download tatala.jar from repository. If you're using ant, change your build.xml to include tatala.jar. If you're using eclipse, add the jar to your project build path.

As you known, easy-to-use is the first consideration among Tatala features. It can make developer create RPC just like local method call. They don’t have to care about socket or thread all kind stuff.

For example, we have server logic TestManager.class and TestManagerImpl.class.

TestManager.java
```java
public interface TestManager {
    public String sayHello(int Id, String name);
}
```

TestManagerImpl.java
```java
public class TestManagerImpl implements TestManager{
	public String sayHello(int Id, String name) {
		return "["+Id+"]"+"Hello "+name+" !";
	}
}
```
We need to create a socket server class, in order to deploy our server logic on server side. In this sample, socket server listener port is 10001.
TestServer.java
```java
public class TestServer {
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
	private static TestManager manager;
	
	public static void main(String[] args) {
		transferObjectFactory = new TransferObjectFactory("test1", true);
		transferObjectFactory.setImplClass("TestManagerImpl");
		manager = (TestManager)ClientProxyFactory.create(TestManager.class, transferObjectFactory);
		
		int Id = 18;
		String name = "JimT";
		String result = manager.sayHello(Id, name);
		System.out.println("result: "+result);
	}
}
```
Of cause, we need have that interface class (TestManager.class) and put into classpath. And need have a controller.xml as well, that indicates what IP, port and name client want to communicate. (Notice the connection name “test1”)
controller.xml
```xml
<connections>
  <connection>
    <hostIp>127.0.0.1</hostIp>
    <hostPort>10001</hostPort>
    <timeout>5000</timeout>
    <retryTime>3</retryTime>
    <name>test1</name>
  </connection>
</connections>
```
That is everything from server to client codes and configuration for a full RPC. It is very simple, right?

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
<tr><td>WrapperClass</td><td>Y</td><td>Y</td></tr>
</tbody>
</table>

## Other Notes
Require JDK1.7, because using Java AIO.

Third part libs include XSteam and Log4j.
## License
This library is distributed under Apache License Version 2.0
## Contact
jimtang@hotmail.ca

QQ: 37287685
