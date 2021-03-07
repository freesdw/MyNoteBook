package client.io;

import java.io.File;
import java.io.Serializable;
import java.util.Vector;

public class NoteGroup implements Serializable {
	private static final long serialVersionUID = -7384635243920891195L;
	String name;
	Vector<Note> notes = new Vector<Note>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void addNote(Note note){
		notes.add(note);
	}
	public boolean removeNote(String fileName){
		int i, len = notes.size();
		for(i = 0; i < notes.size(); i++)
			if(notes.get(i).getName().equals(fileName)){
				new File(notes.get(i).getPath()).delete();	// 为什么有时候删不掉？？？
				notes.remove(i);
				break;
			}
		return i < len;
	}
	public Vector<Note> getAllNote(){
		return notes;
	}
	public boolean hasNote(String noteName){
		for(int i = 0; i < notes.size(); i++)
			if(noteName.equals(notes.get(i).getName()))
				return true;
		return false;
	}
	public Note getNote(String noteName){
		for(int i = 0; i < notes.size(); i++)
			if(noteName.equals(notes.get(i).getName()))
				return notes.get(i);
		return null;
	}
	
	public NoteGroup(String name) {
		this.name = name;
	}
	
	public String toString(){
		String str = name;
		if(notes.size() != 0){
			str += " ";
			for(int i = 0; i < notes.size() - 1; i++)
				str += notes.get(i).toString() + " ";
			str += notes.get(notes.size() - 1).toString();
		}
		return str;
	}
}