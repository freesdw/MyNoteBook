package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
	Socket socket = null;
	BufferedReader br = null;
	PrintWriter pw = null;
	Scanner input = null;
	
	public void send() throws IOException{
		String message = input.nextLine();							// 输入的信息
		pw.write(socket.getInetAddress().getHostAddress() + "\n");	// 指定用户的ip
		pw.write(message + "\n");
		pw.flush();
	}
	
	public void read() throws IOException{
		String message = br.readLine();
		System.out.println(message);		// 得到别人发送的信息
	}
	
	public ChatClient() throws Exception{
		socket = new Socket("localhost", 8800);
		input = new Scanner(System.in);
		pw = new PrintWriter(socket.getOutputStream());	// 输出流
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));	// 输入流
		// 发信息
		Thread thread = new Thread(new Runnable(){
			public void run(){
				while(true){
					try {
						send();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
		// 得到别人的消息
		while(true){
			read();
		}
	}
	
	public void closeClient(){
		try{
			if(input != null)
				input.close();
			if(pw != null)
				pw.close();
			if(br != null)
				br.close();
			if(socket != null)
				socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
