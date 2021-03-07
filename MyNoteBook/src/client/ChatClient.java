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
		String message = input.nextLine();							// �������Ϣ
		pw.write(socket.getInetAddress().getHostAddress() + "\n");	// ָ���û���ip
		pw.write(message + "\n");
		pw.flush();
	}
	
	public void read() throws IOException{
		String message = br.readLine();
		System.out.println(message);		// �õ����˷��͵���Ϣ
	}
	
	public ChatClient() throws Exception{
		socket = new Socket("localhost", 8800);
		input = new Scanner(System.in);
		pw = new PrintWriter(socket.getOutputStream());	// �����
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));	// ������
		// ����Ϣ
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
		// �õ����˵���Ϣ
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
