package com.qileyuan.tatala.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.qileyuan.tatala.example.proxy.ChatRoomClientProxy;

/**
 * This class is a sample for Chat Room Client.
 * Demonstrate long connection server call client directly.
 * @author JimT
 *
 */
public class ChatRoomClient {
	private static ChatRoomClient chatRoomClient = new ChatRoomClient();
	private static BlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(10);
	private ChatRoomClientProxy proxy;
	private String username;
	
	public static ChatRoomClient getInstance(){
		return chatRoomClient;
	}
	
	public static void main(String[] args) {
		ChatRoomClient room = ChatRoomClient.getInstance();
		try {
			room.init();
			room.login();
			System.out.println("^-- enter room --^");

			while(room.sent()){
				room.display();
			}
			
			System.out.println("v-- exit room --v");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(){
		proxy = new ChatRoomClientProxy();
	}
	
	private void login() throws IOException{
		System.out.print("Input user name: ");
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		username = buf.readLine();
		proxy.login(username);
	}
	
	private boolean sent() throws IOException{
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		String myString = buf.readLine();
		proxy.sendMessage(myString);
		if(myString.equals("exit")){
			return false;
		}
		myString = username + ": " + myString;
		messageQueue.add(myString);
		
		return true;
	}
	
	public void receiveMessage(String message){
		messageQueue.add(message);
		display();
	}
	
	private void display(){
		while(!messageQueue.isEmpty()){
			String message = messageQueue.poll();
			System.out.println(message);
		}
	}
}
