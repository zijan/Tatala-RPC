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
## Tutorial
### Common Steps
For build a Tatala RPC, need to three steps:
* Real Server Logic and socket server class

Real business logic codes, which run in server side. Socket server deploys server logic, and listens for client.

* Tatala Proxy

It is the agency between client and server Logic. The codes run in both of client and server side.

* Client

Who is service consumer sent request to socket server proxy and receive response from proxy. The codes run in client side.

Let’s go through all steps.

Just build simple server logic, interface and implement class.
```java
public interface TestManager {
	public String sayHello(int Id, String name);
}
```
```java
public class TestManagerImpl implements TestManager{
	public String sayHello(int Id, String name) {
		return "["+Id+"]"+"Hello "+name+" !";
	}
}
```
Create a socket server class, in order to deploy our server logic on server side.
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
Create controller.xml file on client side, which indicates what IP, port and name client want to communicate.
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
Build Tatala client proxy.
```java
public class TestClientProxy {
	public String sayHello(int Id, String name) {
		TransferObjectFactory transferObjectFactory = new TransferObjectFactory("test1", true);
		StandardTransferObject to = transferObjectFactory.createTransferObject();
		to.setCalleeClass("com.qileyuan.tatala.example.proxy.TestServerProxy");
		to.setCalleeMethod("sayHello");
		to.registerReturnType(TransferObject.DATATYPE_STRING);
		to.putInt("Id", Id);
		to.putString("name", name);
		Object resultObj = ServerExecutor.execute(to);
		String result = (String) resultObj;
		return result;
	}
}
```
Create different TransferObjectFactory by passing different parameter. First parameter is socket connection name present different socket server. That connection name must same as the one in controller.xml file. Second parameter is long connection or not, if long connection flag is true, server will keep that connection until client request disconnect or connection broken.
Create caller method running in client side. Create transfer object by TransferObjectFactory. Set callee class (it’s server proxy), callee method and return type. Put parameter into transfer object, include name and value. Call ServerExecutor.

Build Tatala server proxy.
```java
public class TestServerProxy {
	private TestManager manager = new TestManagerImpl();
	public String sayHello(TransferObject baseto) {
		StandardTransferObject to = (StandardTransferObject)baseto;
		int Id = to.getInt("Id");
		String name = to.getString("name");
		String result = manager.sayHello(Id, name);
		return result;
	}
}
```
Create callee class and callee method running in server side. Get parameters from transfer object. Call real business logic object.

Create client, which send request to client proxy and receive response from proxy.
```java
public class TestClient {
	public static void main(String[] args) throws Exception {
		TestClientProxy manager = new TestClientProxy();
		int Id = 18;
		String name = "JimT";
		String result = manager.sayHello(Id, name);
	}
}
```
### Serializable
We can simply use serialization object to transfer java object cross different JVM.
In client proxy, if we want to pass a value object as a parameter. Make object implements serializable, and put into transfer object.
```java
to.putSerializable("account", account);
```
### Wrapper Class
For performance concern, we can use wrapper class instead of serializable object. That is user-defined object, convert data into byte array manually.
```java
TestAccountWrapper accountWrapper = new TestAccountWrapper(account);
to.putWrapper("account", accountWrapper);
```
TestAccountWrapper class contain customization object - TestAccount.
```java
public class TestAccountWrapper implements TransferObjectWrapper {
	private TestAccount account;
	public TestAccountWrapper(TestAccount account) {
		this.account = account;
	}
	public int getLength(){
		return TransferUtil.getLengthOfInt() + 
			   TransferUtil.getLengthOfString(account.getName()) +
			   TransferUtil.getLengthOfString(account.getAddress());
	}
	public void getByteArray(TransferOutputStream touts) {
		touts.writeInt(account.getId());
		touts.writeString(account.getName());
		touts.writeString(account.getAddress());
	}
	public TestAccountWrapper getObjectWrapper(TransferInputStream tins){
		account = new TestAccount();
		account.setId(tins.readInt());
		account.setName(tins.readString());
		account.setAddress(tins.readString());
		return this;
	}
}
```
There are three implemented methods: 
getLength - get customization object byte array length
getByteArray - convert customization object into byte array
getObjectWrapper - convert byte array back to customization object

### Asynchronous
Tatala supports asynchronous call in client side proxy. If we need return object, we can get it from future object.
```java
to.setAsynchronous(true);
Future<TestAccountWrapper> future = (Future<TestAccountWrapper>) ServerExecutor.execute(to);
accountWrapper = future.get();
```
### Compress
Tatala supports transfer data compression. If we want to send a big content object to server or want to retrieve some big content from server, we can set true on compress flag.
```java
to.setCompress(true);
```
### Default Server Proxy
In client side, we put callee class name and callee method name into transfer object. The callee is server side prox, it is executed by reflection. Server side proxy calls real service code. If we don’t want to execute server proxy in reflection way, we can create a server proxy extends DefaultProxy and register our proxy into server before start in server main class.

Add some code on TestServer.
```java
DefaultProxy defaultProxy = new TestDefaultProxy();
server.registerProxy(defaultProxy);
server.start();
```
TestDefaultProxy.java
```java
public class TestDefaultProxy extends DefaultProxy{
	private TestManager manager = new TestManagerImpl();
		public Object execute(TransferObject abstractto){
		StandardTransferObject to = (StandardTransferObject)abstractto;
		String calleeMethod = to.getCalleeMethod();
		if(calleeMethod.equals("sayHello")){
			int Id = to.getInt("Id");
			String name = to.getString("name");
			String result = manager.sayHello(Id, name);
			return result;
		}
		return null;
	}
}
```
### NewTransferObject
Create NewTransferObject instead of StandardTransferObject, let you ignore parameter name, just put parameters value without name, get parameters orderly on server side. That can reduce transmission data length. Recommend!
```java
//StandardTransferObject to = transferObjectFactory.createTransferObject();
NewTransferObject newto = transferObjectFactory.createNewTransferObject();
newto.setCalleeMethod("sayHello");
newto.registerReturnType(NewTransferObject.DATATYPE_STRING);
newto.putInt(Id);
newto.putString(name);
Object resultObj = ServerExecutor.execute(newto);
String result = (String) resultObj;
```
### Without Proxy
If you feel it’s boring about writing proxy class. Tatala support create RPC without any client and server proxy. It base on Java dynamic proxy. So performance is slight bad, and don’t support wrapper class and Tatala-client-csharp.

Just look up the first example EasyClient on Get Started section.

### Server Push
After client setup long connection with server, sometime server needs to push message to client, such as server broadcast a message to all players in chat room. You can execute a server call by Tatala session, as long as you keep the client session and handle it on server logic.

Check Chat Room example for more detail.

### Session Filter
Tatala support server intercepts each client request, and injects business logic on it.

Create session filter.
```java
public class MyFilter implements SessionFilter {
	public void onClose(ServerSession session) {
		//on connection close, do something
	}
	public boolean onReceive(ServerSession session, byte[] receiveData) {
		//after each receive, do something
		return true;
	}
}
```
Register my filter on socket server class.
```java
AioSocketServer server = new AioSocketServer(listenPort, poolSize);
MyFilter filter = new MyFilter();
server.registerSessionFilter(filter);
```
## Protocol
In client side, put method signature into transfer object, tatala convert transfer object into byte array and send to server. In server side, convert byte array into transfer object that content method signature, like callee class, method name, arguments and return type. Tatala executor loads that information and invokes that target method.

Following is conversion protocol.

Client -> Server

Normal:

TatalaFlag+Flag+BLength+CLASS+METHOD+ReturnType+CompressFlag+([type+name+value]+[type+name+value]+[...] )

Compress:

TatalaFlag+Flag+UnCompressLength+CompressLength+BLength+CLASS+METHOD+ReturnType+CompressFlag+([type+name+value]+[type+name+value]+[...] )

Wrapper: (if value is wrapper class)

BLength+WCLASS+[value]+[value]+[...]


Server -> Client (server return)

Normal:

Flag+BLength+ReturnType+ReturnValue

Compress:

Flag+UnCompressLength+CompressLength+BLength+ReturnType+ReturnValue


NewTransferObject (remove parameter name)

Client -> Server

Normal:

TatalaFlag+Flag+BLength+CLASS+METHOD+ReturnType+CompressFlag+([type+value]+[type+value]+[...] )

Compress:

TatalaFlag+Flag+UnCompressLength+CompressLength+BLength+CLASS+METHOD+ReturnType+CompressFlag+([type+value]+[type+value]+[...] )


**Notes:**

TatalaFlag: just claim this request is tatala request

Flag: compress = 1; servercall = 1 << 1; longconnection = 1 << 2; NewTOVersion = 1 << 3; (if it is NewTransferObject)

blength: whole byte array length

class: callee class full name (package and classname)

method: callee method name

returntype: return data type

returnvalue: return data

compressflag: compress flag tell server need compress return data

type: this input parameter type

name: this input parameter name

value: this input parameter value

uncompresslength: byte array length before compress

compresslength: byte array length after compress

compressdata: compressed data using zlib

wclass: wrapper class full name

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
