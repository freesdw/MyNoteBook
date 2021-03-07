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
	private static boolean flag = false;			// 是否处于登录状态
	private Vector<NoteGroup> noteGroups = null;	// 本地笔记本组
	private static User user = null;				// 数据库笔记本组与用户
	private SystemClient sc = null;					// 
	
	private static DataControl instance = new DataControl();
	
	public static DataControl getInstance(){
		return instance;
	}
	
	private DataControl(){
		readNativeNoteGroups();
	}
	
	// 打开客户端
	public void openSC(){
		sc = new SystemClient();
	}
	
	// 打开客户端
	public void closeSC(){
		if(sc != null)
			sc.close();
	}
	
	// 是否处于登录状态
	public static boolean getFlag(){
		return flag;
	}
	
	// 给出本地笔记本组
	public Vector<NoteGroup> getNativeNoteGroup(){
		return noteGroups;
	}
	
	// 给出用户数据库笔记本组
	public Vector<NoteGroup> getMySQLNoteGroup(){
		if(flag)
			return user.getAllNoteGroup();
		return new Vector<NoteGroup>();
	}
	
	// 保存本地笔记本组
	public void saveNativeNoteGroups(){
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/Client/noteGroups.txt"));
			oos.writeObject(noteGroups);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 读取本地笔记本组
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
	
	// 创建本地笔记本组
	public boolean createNativeNoteGroup(String groupName){
		for(int i = 0; i < noteGroups.size(); i++)
			if(noteGroups.get(i).getName().equals(groupName))
				return false;
		boolean flag = noteGroups.add(new NoteGroup(groupName));
		saveNativeNoteGroups();
		return flag;
	}
	
	// 删除本地笔记本组
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
	
	// 创建本地笔记本
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

	// 删除本地笔记本
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

	// 创建/删除数据库笔记本组
	public boolean aboutMySQLNoteGroup(String groupName, boolean f){
		if(user.hasNoteGroup(groupName) && f || !user.hasNoteGroup(groupName) && !f)
			return false;
		boolean flag = false;
		user = sc.aboutNoteGroup(user.getEmail(), groupName, f);
		flag = user != null;
		return flag;
	}
	
	// 创建/删除数据库笔记本
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
	
	// 读取指定数据库笔记本组的笔记本
	public NoteGroup readFileInMySQLGroup(NoteGroup chooseNg){
		Vector<Note> notes = chooseNg.getAllNote();
		for(int i = 0; i < notes.size(); i++){
			String path = "data/Client/sql_" + chooseNg.getName() + "_" + i + ".txt";
			notes.get(i).setPath(path);
			download(chooseNg.getName(), notes.get(i).getName(), path);
		}
		return chooseNg;
	}
	
	// 下载数据库笔记
	public File download(String groupName, String noteName, String path){
		File file = null;
		FileClient fc = new FileClient();
		file = fc.download(user.getEmail(), groupName, noteName, path);
		fc.close();
		return file;
	}
	
	// 保存指定数据库笔记本组的笔记本
	public void saveFileInMySQLGroup(NoteGroup chooseNg){
		Vector<Note> notes = chooseNg.getAllNote();
		for(int i = 0; i < notes.size(); i++){
			File file = new File(notes.get(i).getPath());
			notes.get(i).setPath("server");
			upload(chooseNg.getName(), notes.get(i).getName(), file);
			file.delete();
		}
	}
	
	// 上传数据库文件
	public void upload(String groupName, String noteName, File file){
		boolean isExit = user.hasNote(groupName, noteName);		// 判断是不是在数据库已有了
		FileClient fc = new FileClient();
		fc.upload(user.getEmail(), groupName, noteName, file, isExit);
		fc.close();
	}

	// 修改指定数据库笔记本组的笔记本名称
	public Note changeMySQLNoteName(String groupName, String oldNoteName, String newNoteName){
		user = sc.changeNGNoteName(user.getEmail(), groupName, oldNoteName, newNoteName);
		return user.getNote(groupName, newNoteName);
	}
	
	// 修改指定数据库笔记本组的名称
	public NoteGroup changeMySQLNGName(String oldNGName, String newNGName){
		user = sc.changeNGName(user.getEmail(), oldNGName, newNGName);
		return user.getNoteGroup(newNGName);
	}
	
	// 检查账号密码/登录
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
	
	// 注册
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
	
	// 获取用户信息
	public User getUser(){
		return user;
	}
	
	// 保存用户信息
	public boolean saveUser(){
		return sc.saveUser(user);
	}

//	// 请求加好友
//	public boolean addFriend(String email){
//		
//		return false;
//	}
	
//	// 传输文件/沟通聊天
//	public void sendFile(String friend, File file, boolean isMessage){
//		FileClient fc = new FileClient();
//		fc.send(friend, file.getName(), file, isMessage);
//		fc.close();
//	}
	
//	// 接受文件/接受聊天信息
//	public void receiveFile(String mypath){
//		FileClient fc = new FileClient();
//		boolean isM = fc.receive(mypath);
//		if(isM)
//			;
//	}
}
