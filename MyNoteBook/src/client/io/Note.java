package client.io;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {
	private static final long serialVersionUID = -6802828802331396572L;
	private String name;
	private String path;
	private Date createDate, changeDate;
	private boolean isSavedWithStyle = false;
	
	public Date getCreateDate() {
		return createDate;
	}
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate() {
		changeDate = new Date();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public Note(String name){
		this.name = name;
		this.path = "server";
		createDate = new Date();
		changeDate = new Date();
	}
	
	public Note(String name, String path) {
		this.name = name;
		this.path = path;
		createDate = new Date();
		changeDate = new Date();
	}
	
	public boolean isSavedWithStyle() {
		return isSavedWithStyle;
	}
	public void setSavedWithStyle(boolean isSavedWithStyle) {
		this.isSavedWithStyle = isSavedWithStyle;
	}
	public String toString(){
		return name + " " + path + " " + createDate + " " + changeDate + " " + isSavedWithStyle;
	}
}
