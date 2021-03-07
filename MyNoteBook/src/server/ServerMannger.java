package server;

import java.io.IOException;

public class ServerMannger {
	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new SystemServer();
				} catch (ClassNotFoundException e) {
					System.out.println("ClassNotFoundException in ServerMannger SystemServer");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("IOException in ServerMannger SystemServer");
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new FileServer();
				} catch (IOException e) {
					System.out.println("IOException in ServerMannger FileServer");
					e.printStackTrace();
				}
			}
		}).start();
		
		try {
			new ChatServer();
		} catch (IOException e) {
			System.out.println("IOException in ServerMannger ChatServer");
			e.printStackTrace();
		}
	}
}
