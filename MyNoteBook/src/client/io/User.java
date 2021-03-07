package client.io;

import java.io.Serializable;
import java.util.Vector;

public class User implements Serializable {
	private static final long serialVersionUID = 640192922927506433L;
	private String email;
	private String password;
//	private String image = null;
	private String name;
	private String sex;
	private Vector<String> friends = null;
	private Vector<NoteGroup> noteGroups = null;
	
	public String toString(){
		String str = email + " " + password + " " + name + " " + sex + " " + noteGroups.toString();
		return str;
	}
	
	public User(String e, String p){
		email = e;
		password = p;
		friends = new Vector<String>();
		noteGroups = new Vector<NoteGroup>();
	}
	
	public User(String e, String p, String n, String s){
		email = e;
		password = p;
		name = n;
		sex = s;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSex(){
		return sex;
	}
	public void setSex(String s){
		sex = s;
	}
//	public String getImage(){
//		return image;
//	}
//	public void setImage(String path){
//		image = path;
//	}
	
	public void addNoteGroup(NoteGroup noteGroup){
		noteGroups.add(noteGroup);
	}
	
	public void addNote(String noteGroupName, Note note){
		int i;
		for(i = 0; i < noteGroups.size(); i++)
			if(noteGroups.get(i).getName().equals(noteGroupName)){
				noteGroups.get(i).addNote(note);
				break;
			}
		if(i == noteGroups.size()){
			NoteGroup ng = new NoteGroup(noteGroupName);
			ng.addNote(note);
			noteGroups.add(ng);
		}
	}

	public void removeNoteGroup(String name){
		for(int i = 0; i < noteGroups.size(); i++)
			if(noteGroups.get(i).getName().equals(name)){
				noteGroups.remove(i);
				break;
			}
	}
	
	public void removeNote(String noteGroupName, String fileName){
		for(int i = 0; i < noteGroups.size(); i++)
			if(noteGroups.get(i).getName().equals(noteGroupName)){
				noteGroups.get(i).removeNote(fileName);
				break;
			}
	}
	
	public Vector<NoteGroup> getAllNoteGroup(){
		return noteGroups;
	}
	
	public boolean hasNoteGroup(String noteGroupName){
		for(int i = 0; i < noteGroups.size(); i++)
			if(noteGroups.get(i).getName().equals(noteGroupName))
				return true;
		return false;
	}

	public boolean hasNote(String noteGroupName, String noteName){
		for(int i = 0; i < noteGroups.size(); i++)
			if(noteGroups.get(i).getName().equals(noteGroupName))
				return noteGroups.get(i).hasNote(noteName);
		return false;
	}
	
	public Note getNote(String noteGroupName, String noteName){
		for(int i = 0; i < noteGroups.size(); i++)
			if(noteGroups.get(i).getName().equals(noteGroupName))
				return noteGroups.get(i).getNote(noteName);
		return null;
	}
	
	public NoteGroup getNoteGroup(String noteGroupName){
		for(int i = 0; i < noteGroups.size(); i++)
			if(noteGroups.get(i).getName().equals(noteGroupName))
				return noteGroups.get(i);
		return null;
	}
	
	public void addFriend(String friend){
		friends.add(friend);
	}
	
	public void removeFriend(String friend){
		for(int i = 0; i < friends.size(); i++)
			if(friends.get(i).equals(friend))
				friends.remove(i);
	}
	
	public Vector<String> getAllFriend(){
		return friends;
	}
}