package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileClient {
	private static String path = "data/Client/new.txt";
	
	private Socket socket = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private FileOutputStream fos = null;
	private FileInputStream fis = null;
	
	public FileClient(){
		try {
			socket = new Socket("localhost", 8880);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			dis.close();
			dos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 传输文件给指定用户
	public void send(String id, String noteName, File file, boolean isMessage){
		try {
			dos.writeUTF("send");
			dos.writeUTF(id);
			dos.writeUTF(noteName);
			dos.writeBoolean(isMessage);
			dos.flush();
			sendFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 下载
	public File download(String email, String groupName, String noteName, String mypath){
		try {
			dos.writeUTF("download");
			dos.writeUTF(email);
			dos.writeUTF(groupName);
			dos.writeUTF(noteName);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = receiveFile(mypath);
		return file;
	}
	
	// 上传
	public void upload(String email, String groupName, String noteName, File file, boolean isExit){
		try {
			dos.writeUTF("upload");
			dos.writeUTF(email);
			dos.writeUTF(groupName);
			dos.writeUTF(noteName);
			dos.writeBoolean(isExit);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendFile(file);
	}
	
	public void sendFile(File file){
		int length = 0;
		byte[] sendBytes = null;
		try {
		    fis = new FileInputStream(file);
		    sendBytes = new byte[1024];
		    while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
		        dos.write(sendBytes, 0, length);
		        dos.flush();
		    }
		    fis.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public File receiveFile(String mypath){
		if(receive(mypath))
			return new File(mypath);
        return null;
	}
	
	public boolean receive(String mypath){
		byte[] inputByte = null;
        int length = 0;
        File file = null;
        boolean isMessage = false;
        try {
        	isMessage = dis.readBoolean();
        	if(mypath == null)
        		file = new File(path);
        	else
        		file = new File(mypath);
            if(!file.exists())
            	file.createNewFile();
            fos = new FileOutputStream(file);
            inputByte = new byte[1024];
            while ((length = dis.read(inputByte, 0, inputByte.length)) > 0) {
                fos.write(inputByte, 0, length);
                fos.flush();
            }
            fos.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return isMessage;
	}
}
