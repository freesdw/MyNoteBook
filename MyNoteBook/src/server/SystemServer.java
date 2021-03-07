package server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import client.io.Note;
import client.io.NoteGroup;
import client.io.User;

class SystemSocketThread implements Runnable {
	Socket socket = null;
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	
	String message = null;
	String email = null;
	String password = null;
	User user = null;
	
	public SystemSocketThread(Socket socket) throws IOException {
		this.socket = socket;
		ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		oos = new ObjectOutputStream(socket.getOutputStream());
	}
	
	
	public void dealWith() throws IOException{
		boolean f = false;
		// 注册
		if(message.equals("register")){
			password = ois.readUTF();
			if(user == null){
				oos.writeBoolean(true);
				MySQLManager.getInstance().add(email, password);
			}
			else
				oos.writeBoolean(false);
		}
		// 登录
		else if(message.equals("login")){
			password = ois.readUTF();
			f = user != null && user.getPassword().equals(password);
			oos.writeBoolean(f);
			if(f)
				oos.writeObject(user);
		}
		// 保存用户信息
		else if(message.equals("saveUser")){
			try {
				User newUser = (User)ois.readObject();
				oos.writeBoolean(MySQLManager.getInstance().update(email, "user", newUser));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		// 创建/删除数据库笔记本组
		else if(message.equals("noteGroup")){
			f = ois.readBoolean();
			String noteGroupName = ois.readUTF();
			if(f)
				user.addNoteGroup(new NoteGroup(noteGroupName));
			else{
				MySQLManager.getInstance().removeFile(email, noteGroupName, "");
				user.removeNoteGroup(noteGroupName);
			}
			boolean flag = MySQLManager.getInstance().update(email, "user", user);
			if(flag)
				oos.writeObject(user);
		}
		// 创建/删除数据库笔记本
		else if(message.equals("note")){
			f = ois.readBoolean();
			String noteGroupName = ois.readUTF();
			String noteName = ois.readUTF();
			if(f){
				user.addNote(noteGroupName, new Note(noteName));
				File file = new File("data/Server/new.txt");
				if(!file.exists())
					file.createNewFile();
				f = MySQLManager.getInstance().addFile(email, noteGroupName, noteName, file);
				file.delete();
			}
			else{
				user.removeNote(noteGroupName, noteName);
				f = MySQLManager.getInstance().removeFile(email, noteGroupName, noteName);
			}
			if(f){
				MySQLManager.getInstance().update(email, "user", user);	
				oos.writeObject(user);
			}
		}
		// 修改数据库笔记本组名称
		else if(message.equals("changeNG")){
			String oldNGName = ois.readUTF();		System.out.println("4" + oldNGName);
			String newNGName = ois.readUTF();		System.out.println("4" + newNGName);
			user.getNoteGroup(oldNGName).setName(newNGName);
			f = MySQLManager.getInstance().changeNote(email, oldNGName, "", newNGName, true);
			if(f){
				MySQLManager.getInstance().update(email, "user", user);
				oos.writeObject(user);
			}
		}
		// 修改数据库笔记本名称
		else if(message.equals("changeNGNote")){
			String noteGroupName = ois.readUTF();
			String oldNoteName = ois.readUTF();
			String newNoteName = ois.readUTF();
			user.getNote(noteGroupName, oldNoteName).setName(newNoteName);
			f = MySQLManager.getInstance().changeNote(email, noteGroupName, oldNoteName, newNoteName, false);
			if(f){
				MySQLManager.getInstance().update(email, "user", user);
				oos.writeObject(user);
			}
		}
		// 加好友 先检查是否存在此用户，然后给此用户发送请求
		
		oos.flush();
	}

	@Override
	public void run() {
		try {
			while(true){
				message = ois.readUTF();
				if(message.equals("Exit"))
					break;
				email = ois.readUTF();
				user = MySQLManager.getInstance().query(email);
				dealWith();
			}
			ois.close();
			oos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

public class SystemServer {
	ServerSocket server = null;
	private boolean flag = true;
	
	public void stopServer(){
		flag = false;
	}
	
	public SystemServer() throws IOException, ClassNotFoundException{
		server = new ServerSocket(8800);
		while(flag){
			Socket socket = server.accept();
			new Thread(new SystemSocketThread(socket)).start();
		}
		server.close();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		new SystemServer();
	}
}
