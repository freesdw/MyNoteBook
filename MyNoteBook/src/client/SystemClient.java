package client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import client.io.User;

public class SystemClient {
	Socket socket = null;
	ObjectInputStream is = null;
    ObjectOutputStream os = null;
    
    public SystemClient(){
    	try {
			socket = new Socket("localhost", 8800);
			os = new ObjectOutputStream(socket.getOutputStream());
			is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    // ע��
    public boolean register(String email, String password){
    	boolean flag = false;
    	try {
			os.writeUTF("register");
			os.writeUTF(email);
			os.writeUTF(password);
			os.flush();
			flag = is.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return flag;
    }
    
    // ��¼
    public User login(String email, String password){
    	User user = null;
    	try {
			os.writeUTF("login");
			os.writeUTF(email);
			os.writeUTF(password);
			os.flush();
			boolean flag = is.readBoolean();
			if(flag)
				user = (User)is.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return user;
    }
    
    // �����û���Ϣ
    public boolean saveUser(User user){
    	boolean flag = false;
    	try{
    		os.writeUTF("saveUser");
    		os.writeUTF(user.getEmail());
    		os.writeObject(user);
    		os.flush();
    		flag = is.readBoolean();
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	return flag;
    }
    
//    // ����Ӻ���
//    public boolean addFriend(String email, String password, String friendEmail){
//    	
//    	return false;
//    }
    
    // ����/ɾ�����ݿ�ʼǱ���
    public User aboutNoteGroup(String email, String noteGroupName, boolean f){
    	User user = null;
    	try {
			os.writeUTF("noteGroup");
			os.writeUTF(email);
			os.writeBoolean(f);
			os.writeUTF(noteGroupName);
			os.flush();
			user = (User) is.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return user;
    }
    
    // ����/ɾ�����ݿ�ʼǱ�
    public User aboutNote(String email, String noteGroupName, String noteName, boolean f){
    	User user = null;
    	try {
			os.writeUTF("note");
			os.writeUTF(email);
			os.writeBoolean(f);
			os.writeUTF(noteGroupName);
			os.writeUTF(noteName);
			os.flush();
			user = (User) is.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return user;
    }
    
    // �޸����ݿ�ʼǱ�������
    public User changeNGName(String email, String oldNGName, String newNGName){
    	User user = null;
    	try{
    		os.writeUTF("changeNG");
    		os.writeUTF(email);
    		os.writeUTF(oldNGName);
    		os.writeUTF(newNGName);
    		os.flush();
    		user = (User) is.readObject();
    	} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	return user;
    }
    
    // �޸����ݿ�ʼǱ�����
    public User changeNGNoteName(String email, String groupName, String oldNoteName, String newNoteName){
    	User user = null;
    	try{
    		os.writeUTF("changeNGNote");
    		os.writeUTF(email);
    		os.writeUTF(groupName);
    		os.writeUTF(oldNoteName);
    		os.writeUTF(newNoteName);
    		os.flush();
    		user = (User) is.readObject();
    	} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	return user;
    }
    
    // �ر�
    public void close(){
    	try {
    		os.writeUTF("Exit");
    		os.flush();
			socket.close();
			is.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
