package client.ui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import client.data.DataControl;
import client.io.Note;
import client.io.NoteGroup;

@SuppressWarnings("serial")
public class MyNoteShowPane extends JPanel {
	private JPanel showPane = null;
	private Vector<JTextPane> textpanels = null;
	private HTMLEditorKit editorKit = new HTMLEditorKit();
	
	static DataControl dc = DataControl.getInstance();
	private NoteGroup ng = null;
	private JLabel namelabel = null;
	private boolean sql_flag = false;
	private static MyNoteShowPane instance = new MyNoteShowPane();
	
	public void changeNg(NoteGroup otherng, boolean othersql_flag){
		if(this.ng == otherng)
			return;
		if(this.sql_flag)
			DataControl.getInstance().saveFileInMySQLGroup(this.ng);
		this.sql_flag = othersql_flag;
		this.ng = otherng;
		if(this.sql_flag)
			DataControl.getInstance().readFileInMySQLGroup(this.ng);
	}
	
	public void saveNg(){
		if(sql_flag)
			DataControl.getInstance().saveFileInMySQLGroup(this.ng);
	}
	
	public void changeNgName(){
		namelabel.setText(ng.getName());
	}
	
	public NoteGroup getNg(){
		return ng;
	}
	
	private MyNoteShowPane(){
		setMinimumSize(new Dimension(265, 880));
		setLayout(null);
		setBackground(Color.white);
		
		// 搜索
		JTextField searchField = new JTextField();
		searchField.setBounds(10, 5, 250, 30);
		add(searchField);
		
		addJSeparator(40);
		
		// 显示
		JLabel label = new JLabel("正在显示");
		label.setBounds(10, 50, 50, 40);
		label.setFont(new Font("微软雅黑", 0, 12));
		add(label);
		
		showNoteGroupName();
		
		JLabel label2 = new JLabel("中的一条笔记");
		label2.setBounds(180, 50, 80, 40);
		label2.setFont(new Font("微软雅黑", 0, 12));
		add(label2);
		
		addJSeparator(95);
		
		showPane = new JPanel();
		showPane.setLayout(null);
		showPane.setBackground(Color.white);
		showPane.setBounds(0, 100, 400, 780);
		add(showPane);
		textpanels = new Vector<JTextPane>();
	}

	private void addJSeparator(int height) {
		JSeparator sep = new JSeparator();
		sep.setBounds(0, height, 300, 1);
		sep.setForeground(new Color(230, 230, 230));
		add(sep);
	}
	
	private void showNoteGroupName() {
		namelabel = new JLabel();
		if(ng != null)
			namelabel.setText(ng.getName());
		namelabel.setFont(new Font("微软雅黑", 0, 12));
		namelabel.setHorizontalAlignment(JLabel.CENTER);
		namelabel.setVerticalAlignment(JLabel.CENTER);
		namelabel.setBounds(0, 0, 120, 40);
		JPanel panel = new JPanel();
		panel.setBackground(new Color(217, 232, 239));
		panel.setLayout(null);
		panel.setBounds(60, 50, 120, 40);
		panel.add(namelabel);
		add(panel);
	}

	public static MyNoteShowPane getInstance(){
		return instance;
	}
	
	public void changeShowPane(){
		textpanels.clear();
		showPane.removeAll();
		showPane.repaint();
		Vector<Note> lps = ng.getAllNote();
		namelabel.setText(ng.getName());
		for(int i = 0; i < lps.size(); i++){
			int flag = i;
			
			JTextPane tp = new JTextPane();
			try {
				FileReader fr = new FileReader(new File(lps.get(i).getPath()));
				HTMLDocument htmldoc = (HTMLDocument) editorKit.createDefaultDocument();
				editorKit.read(fr, htmldoc, 0);
				tp.setDocument(htmldoc);
			} catch (IOException | BadLocationException e1) {
				e1.printStackTrace();
			}
			tp.setMinimumSize(new Dimension(400, 95));
			tp.setPreferredSize(new Dimension(400, 95));
			tp.setMaximumSize(new Dimension(400, 95));
			tp.setBounds(0, 100 * i, 400, 95);
			tp.setBackground(Color.white);
			tp.setEnabled(false);
			tp.setName("0");
			textpanels.add(tp);
			showPane.add(tp);
			
			tp.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					if(tp.getName().equals("0"))
						tp.setBackground(new Color(219, 232, 245));
				}
				@Override
				public void mouseExited(MouseEvent e) {
					if(tp.getName().equals("0"))
						tp.setBackground(Color.white);
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 1){
						// 打开文件
						MyNotePane.getInstance().setNoteAndGroup(lps.get(flag), ng.getName(), false, new File(lps.get(flag).getPath()));	System.out.println(lps.get(flag));
						for(int i = 0; i < textpanels.size(); i++)
							if(textpanels.get(i).getName().equals("1")){
								textpanels.get(i).setName("0");
								textpanels.get(i).setBackground(Color.white);
							}
						tp.setName("1");
						tp.setBackground(new Color(160, 198, 238));
					}
				}
			});
			
			JSeparator sep = new JSeparator();
			sep.setBounds(0, 100 * i + 97, 400, 2);
			sep.setForeground(new Color(200, 200, 200));
			showPane.add(sep);
		}
	}
}
