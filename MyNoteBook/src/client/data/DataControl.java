package client.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import client.FileClient;
import client.SystemClient;
import client.error.AccountAndPasswordErrorException;
import client.io.Note;
import client.io.NoteGroup;
import client.io.User;
import client.ui.panel.MyUtilPane;

public class DataControl {
	private static boolean flag = false;			// �Ƿ��ڵ�¼״̬
	private Vector<NoteGroup> noteGroups = null;	// ���رʼǱ���
	private static User user = null;				// ���ݿ�ʼǱ������û�
	private SystemClient sc = null;					// 
	
	private static DataControl instance = new DataControl();
	
	public static DataControl getInstance(){
		return instance;
	}
	
	private DataControl(){
		readNativeNoteGroups();
	}
	
	// �򿪿ͻ���
	public void openSC(){
		sc = new SystemClient();
	}
	
	// �򿪿ͻ���
	public void closeSC(){
		if(sc != null)
			sc.close();
	}
	
	// �Ƿ��ڵ�¼״̬
	public static boolean getFlag(){
		return flag;
	}
	
	// �������رʼǱ���
	public Vector<NoteGroup> getNativeNoteGroup(){
		return noteGroups;
	}
	
	// �����û����ݿ�ʼǱ���
	public Vector<NoteGroup> getMySQLNoteGroup(){
		if(flag)
			return user.getAllNoteGroup();
		return new Vector<NoteGroup>();
	}
	
	// ���汾�رʼǱ���
	public void saveNativeNoteGroups(){
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/Client/noteGroups.txt"));
			oos.writeObject(noteGroups);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ��ȡ���رʼǱ���
	@SuppressWarnings("unchecked")
	public void readNativeNoteGroups(){
		try{
			File file = new File("data/Client/noteGroups.txt");
			if(file.length() != 0){
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				noteGroups = (Vector<NoteGroup>)ois.readObject();
				ois.close();
			} else 
				noteGroups = new Vector<NoteGroup>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// �������رʼǱ���
	public boolean createNativeNoteGroup(String groupName){
		for(int i = 0; i < noteGroups.size(); i++)
			if(noteGroups.get(i).getName().equals(groupName))
				return false;
		boolean flag = noteGroups.add(new NoteGroup(groupName));
		saveNativeNoteGroups();
		return flag;
	}
	
	// ɾ�����رʼǱ���
	public boolean deleteNativeNoteGroup(String groupName){
		boolean flag = false;
		for(int i = 0; i < noteGroups.size(); i++)
			if(noteGroups.get(i).getName().equals(groupName)){
				Vector<Note> notes = noteGroups.get(i).getAllNote();
				for(int j = 0; j < notes.size(); j++)
					new File(notes.get(i).getPath()).delete();
				if(noteGroups.remove(i) != null)
					flag = true;
				saveNativeNoteGroups();
				break;
			}
		return flag;
	}
	
	// �������رʼǱ�
	public Note createNativeNote(String groupName, String noteName, String path){
		Note note = new Note(noteName, path);
		int i;
		for(i = 0; i < noteGroups.size(); i++)
			if(noteGroups.get(i).getName().equals(groupName)){
				if(noteGroups.get(i).hasNote(noteName))
					return null;
				else
					noteGroups.get(i).addNote(note);
				break;
			}
		if(i == noteGroups.size()){
			NoteGroup ng = new NoteGroup(groupName);
			ng.addNote(note);
			noteGroups.add(ng);
		}
		saveNativeNoteGroups();
		return note;
	}

	// ɾ�����رʼǱ�
	public boolean deleteNativeNote(String groupName, String noteName){
		boolean flag = false;	System.out.println(groupName);
		for(int i = 0; i < noteGroups.size(); i++)
			if(noteGroups.get(i).getName().equals(groupName)){
				if(noteGroups.get(i).hasNote(noteName))
					flag = noteGroups.get(i).removeNote(noteName);
				saveNativeNoteGroups();
				break;
			}
		return flag;
	}

	// ����/ɾ�����ݿ�ʼǱ���
	public boolean aboutMySQLNoteGroup(String groupName, boolean f){
		if(user.hasNoteGroup(groupName) && f || !user.hasNoteGroup(groupName) && !f)
			return false;
		boolean flag = false;
		user = sc.aboutNoteGroup(user.getEmail(), groupName, f);
		flag = user != null;
		return flag;
	}
	
	// ����/ɾ�����ݿ�ʼǱ�
	public Note aboutMySQLNote(String groupName, String noteName, boolean f){
		if(user.hasNote(groupName, noteName) && f || !user.hasNote(groupName, noteName) && !f)
			return null;
		Note note = null;
		user = sc.aboutNote(user.getEmail(), groupName, noteName, f);
		if(f){
			note = user.getNote(groupName, noteName);
			readFileInMySQLGroup(user.getNoteGroup(groupName));
		}
		return note;
	}
	
	// ��ȡָ�����ݿ�ʼǱ���ıʼǱ�
	public NoteGroup readFileInMySQLGroup(NoteGroup chooseNg){
		Vector<Note> notes = chooseNg.getAllNote();
		for(int i = 0; i < notes.size(); i++){
			String path = "data/Client/sql_" + chooseNg.getName() + "_" + i + ".txt";
			notes.get(i).setPath(path);
			download(chooseNg.getName(), notes.get(i).getName(), path);
		}
		return chooseNg;
	}
	
	// �������ݿ�ʼ�
	public File download(String groupName, String noteName, String path){
		File file = null;
		FileClient fc = new FileClient();
		file = fc.download(user.getEmail(), groupName, noteName, path);
		fc.close();
		return file;
	}
	
	// ����ָ�����ݿ�ʼǱ���ıʼǱ�
	public void saveFileInMySQLGroup(NoteGroup chooseNg){
		Vector<Note> notes = chooseNg.getAllNote();
		for(int i = 0; i < notes.size(); i++){
			File file = new File(notes.get(i).getPath());
			notes.get(i).setPath("server");
			upload(chooseNg.getName(), notes.get(i).getName(), file);
			file.delete();
		}
	}
	
	// �ϴ����ݿ��ļ�
	public void upload(String groupName, String noteName, File file){
		boolean isExit = user.hasNote(groupName, noteName);		// �ж��ǲ��������ݿ�������
		FileClient fc = new FileClient();
		fc.upload(user.getEmail(), groupName, noteName, file, isExit);
		fc.close();
	}

	// �޸�ָ�����ݿ�ʼǱ���ıʼǱ�����
	public Note changeMySQLNoteName(String groupName, String oldNoteName, String newNoteName){
		user = sc.changeNGNoteName(user.getEmail(), groupName, oldNoteName, newNoteName);
		return user.getNote(groupName, newNoteName);
	}
	
	// �޸�ָ�����ݿ�ʼǱ��������
	public NoteGroup changeMySQLNGName(String oldNGName, String newNGName){
		user = sc.changeNGName(user.getEmail(), oldNGName, newNGName);
		return user.getNoteGroup(newNGName);
	}
	
	// ����˺�����/��¼
	public boolean login(String email, String password) throws AccountAndPasswordErrorException{
		user = sc.login(email, password);
		if(user != null){
			flag = true;
			MyUtilPane.getInstance().setNoteItems();
		}
		else
			throw new AccountAndPasswordErrorException(0);
		return flag;
	}
	
	// ע��
	public boolean register(String email, String password) throws AccountAndPasswordErrorException{
		flag = sc.register(email, password);
		if(flag){
			user = new User(email, password);
			MyUtilPane.getInstance().setNoteItems();
		}
		else
			throw new AccountAndPasswordErrorException(2);
		return flag;
	}
	
	// ��ȡ�û���Ϣ
	public User getUser(){
		return user;
	}
	
	// �����û���Ϣ
	public boolean saveUser(){
		return sc.saveUser(user);
	}

//	// ����Ӻ���
//	public boolean addFriend(String email){
//		
//		return false;
//	}
	
//	// �����ļ�/��ͨ����
//	public void sendFile(String friend, File file, boolean isMessage){
//		FileClient fc = new FileClient();
//		fc.send(friend, file.getName(), file, isMessage);
//		fc.close();
//	}
	
//	// �����ļ�/����������Ϣ
//	public void receiveFile(String mypath){
//		FileClient fc = new FileClient();
//		boolean isM = fc.receive(mypath);
//		if(isM)
//			;
//	}
}
