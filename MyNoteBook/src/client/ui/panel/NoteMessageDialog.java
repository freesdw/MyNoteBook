package client.ui.panel;

import javax.swing.JDialog;
import javax.swing.JLabel;

import client.io.Note;

public class NoteMessageDialog extends JDialog {
	private static final long serialVersionUID = -6232560848860332043L;
	
	private Note note = null;
	private String NoteGroupName;
	private String userName;
	
	private JLabel title = null;
	private JLabel noteName = null;
	private JLabel buildTime = null;
	private JLabel updateTime = null;
	private JLabel author = null;

	public NoteMessageDialog(String userName, String NoteGroupName, Note note){
		setTitle("笔记信息");
		this.note = note;
		this.userName = userName;
		this.NoteGroupName = NoteGroupName;
		init();
		
	}

	private void init() {
		setSize(300, 200);
		
		String titleStr = "标题：\t" + note.getName();
		title = new JLabel(titleStr);
		
		String noteNameStr = "笔记本：\t" + NoteGroupName;
		noteName = new JLabel(noteNameStr);
		
		String buildTimeStr = "创建时间：\t" + note.getCreateDate().toString();
		buildTime = new JLabel(buildTimeStr);
		
		String updateTimeStr = "更新时间：\t" + note.getChangeDate().toString();
		updateTime = new JLabel(updateTimeStr);
		
		String autherStr = "作者：\t" + userName;
		author = new JLabel(autherStr);
		
		title.setBounds(10, 10, 280, 25);
		noteName.setBounds(10, 45, 280, 25);
		buildTime.setBounds(10, 80, 280, 25);
		updateTime.setBounds(10, 115, 280, 25);
		author.setBounds(10, 150, 280, 25);
		
		add(title);
		add(noteName);
		add(buildTime);
		add(updateTime);
		add(author);
	}
}
