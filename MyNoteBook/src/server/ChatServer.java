package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

class SocketThread implements Runnable{
	private Socket socket = null;
	private String message = "";
	private String id = null;
	private PrintWriter pw = null;
	private BufferedReader br = null;
	
	public SocketThread(Socket socket) throws IOException {
		this.socket = socket;
		id = socket.getInetAddress().getHostAddress();
		pw = new PrintWriter(socket.getOutputStream());
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public String getId(){
		return id;
	}
	
	// 发送信息
	public void send(String m){
		pw.write(m + "\n");
		pw.flush();
	}
		
	public void run() {
		try{
			while(true){
				message = br.readLine();	// 读取客户信息
				if(message.equals("exit"))
					break;
				String id = br.readLine();	// 发送给指定客户
				for(int i = 0; i < ChatServer.sockets.size(); i++)
					if(id.equals(ChatServer.sockets.get(i).getId())){
						ChatServer.sockets.get(i).send(message);
						break;
					}
			}
			pw.close();
			br.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

public class ChatServer {
	private boolean flag = true;
	static Vector<SocketThread> sockets = new Vector<SocketThread>();
	
	public void setFlag(){
		flag = false;
	}
	
	public ChatServer() throws IOException{
		ServerSocket server = new ServerSocket(8888);
		while(flag){
			Socket socket = server.accept();
			SocketThread st = new SocketThread(socket);
			sockets.add(st);
			new Thread(st).start();
		}
		server.close();
	}
}
