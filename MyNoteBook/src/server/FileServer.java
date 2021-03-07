package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

class FileSocketThread implements Runnable{
	private static String path = "data/Server/";
	
	private Socket socket = null;
	private String id = null;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	private FileOutputStream fos = null;
	private FileInputStream fis = null;
	private String message = null;
	
	public FileSocketThread(Socket socket) throws IOException{
		this.socket = socket;
		id = socket.getInetAddress().getHostAddress();
		dis = new DataInputStream(socket.getInputStream());
    	dos = new DataOutputStream(socket.getOutputStream());
	}
	
	// 获取指定id
	public String getId(){
		return id;
	}
	
	// 发送文件给指定用户
	public boolean sendFileToFriend(String id ,File file, boolean isMessage){
		int i;
		for(i = 0; i < FileServer.fileSocketThreads.size(); i++)
			if(FileServer.fileSocketThreads.get(i).getId().equals(id)){
				FileServer.fileSocketThreads.get(i).sendFile(file, isMessage);
				break;
			}
		return i == FileServer.fileSocketThreads.size();
	}
	
	// 发送文件
	public void sendFile(File file, boolean isMessage){
		int length = 0;
        byte[] sendBytes = null;
        try {
        	dos.writeBoolean(isMessage);
        	dos.flush();
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
	
	// 从用户处接受文件
	public File receiveFile(String fileName){
		byte[] inputByte = null;
        int length = 0;
        File file = null;
        try {
            file = new File(path + fileName);
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
        return file;
	}
	
	// 接受并处理命令
	public void run() {
		try {
			message = dis.readUTF();
			File file = null;
			if(message.equals("send")){			// 发送文件给指定用户
				String key = dis.readUTF();
				String fileName = dis.readUTF();
				boolean isMessage = dis.readBoolean();
				file = receiveFile(fileName);
				boolean flag = sendFileToFriend(key, file, isMessage);
				if(flag)
					MySQLManager.getInstance().addMessage(key, fileName, file);
			}
			else if(message.equals("upload")){	// 上传
				String email = dis.readUTF();
				String groupName = dis.readUTF();
				String fileName = dis.readUTF();
				boolean isExit = dis.readBoolean();
				file = receiveFile(fileName);
				if(isExit)
					MySQLManager.getInstance().updateFile(email, groupName, fileName, file);
				else
					MySQLManager.getInstance().addFile(email, groupName, fileName, file);
			}
			else if(message.equals("download")){// 下载
				String email = dis.readUTF();
				String groupName = dis.readUTF();
				String fileName = dis.readUTF();
				file = MySQLManager.getInstance().readFile(email, groupName, fileName);
				sendFile(file, false);
			}
			else if(message.equals("receive")){ // 检查是否有信息
				
			}
			file.delete();
			socket.close();
			dis.close();
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

public class FileServer {
	private boolean flag = true;
	static Vector<FileSocketThread> fileSocketThreads = new Vector<FileSocketThread>();
	
	public FileServer() throws IOException{
		ServerSocket server = new ServerSocket(8880);
		while(flag){
			Socket socket = server.accept();
			FileSocketThread fst = new FileSocketThread(socket);
			fileSocketThreads.add(fst);
			new Thread(fst).start();
		}
		server.close();
	}
	
	public void setFlag(){
		flag = false;
	}
}
