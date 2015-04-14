using System;
using Example.tatala.proxy;
using QiLeYuan.Tatala.util;
using QiLeYuan.Tools.debug;

namespace Example.tatala.client {
    /// <summary>
    /// This class is a sample for Chat Room c# Client.
    /// Demonstrate long connection server call client directly.
    /// @author JimT
    /// </summary>
    public class ChatRoomClient {
        private static ChatRoomClient chatRoomClient = new ChatRoomClient();
	    private static BlockingQueue messageQueue = new BlockingQueue(10);
	    private ChatRoomClientProxy proxy;
	    private String username;
    	
	    public static ChatRoomClient getInstance(){
		    return chatRoomClient;
	    }

        //want to test on console, you can change MainStart to Main and build as exe.
        public static void MainStart(String[] args) {
            Logging.AddDebug(new ConsoleDebug());
            Logging.setLevel(Logging.Level.INFO);

		    ChatRoomClient room = ChatRoomClient.getInstance();
		    try {
			    room.init();
			    room.login();
                Console.WriteLine("┌-- enter room --┐");
			    while(room.sent()){
				    room.display();
			    }
                Console.WriteLine("└-- exit room --┘");
		    } catch (Exception e) {
                Logging.LogError(e);
		    }
	    }

	    private void init(){
		    proxy = new ChatRoomClientProxy();
	    }
    	
	    private void login(){
            Console.WriteLine("Input user name: ");
		    username = Console.ReadLine();
		    proxy.login(username);
	    }
    	
	    private bool sent(){
		    String myString = Console.ReadLine();
		    proxy.sendMessage(myString);
		    if(myString.Equals("exit")){
			    return false;
		    }
		    myString = username + ": " + myString;
		    messageQueue.Enqueue(myString);
    		
		    return true;
	    }
    	
	    public void receiveMessage(String message){
		    messageQueue.Enqueue(message);
		    display();
	    }
    	
	    private void display(){
		    while(messageQueue.Count > 0){
			    String message = (String)messageQueue.Dequeue();
                Console.WriteLine(message);
		    }
	    }
    	
    }
}
