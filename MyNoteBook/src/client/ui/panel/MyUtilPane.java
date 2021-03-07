package client.ui.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import client.data.DataControl;
import client.io.Note;
import client.io.NoteGroup;
import client.ui.frame.MyFrame;

@SuppressWarnings("serial")
public class MyUtilPane extends JPanel {
	private LabelPane nl = null;
	
	private JPopupMenu pmenu = null;
	private LabelPane chooseNg = null;	// 被选中的笔记本组
	private JPopupMenu ngPmenu = null;
	private LabelPane chooseN = null;	// 被选中的笔记本
	private JPopupMenu nPmenu = null;
	
	private JMenuItem [] noteItems = null;
	
	private Image image1 = new ImageIcon("image/note1.jpg").getImage().getScaledInstance(27,36,Image.SCALE_FAST),
			image2 = new ImageIcon("image/note2.jpg").getImage().getScaledInstance(27,36,Image.SCALE_FAST),
			image3 = new ImageIcon("image/note3.jpg").getImage().getScaledInstance(27,36,Image.SCALE_FAST),
			image4 = new ImageIcon("image/close1.jpg").getImage().getScaledInstance(24,32,Image.SCALE_FAST),
			image5 = new ImageIcon("image/close2.jpg").getImage().getScaledInstance(24,32,Image.SCALE_FAST),
			image6 = new ImageIcon("image/open3.jpg").getImage().getScaledInstance(24,32,Image.SCALE_FAST),
			image7 = new ImageIcon("image/text1.jpg").getImage().getScaledInstance(21,28,Image.SCALE_FAST),
			image8 = new ImageIcon("image/text2.jpg").getImage().getScaledInstance(21,28,Image.SCALE_FAST),
			image9 = new ImageIcon("image/text3.jpg").getImage().getScaledInstance(21,28,Image.SCALE_FAST);
	
	class LabelPane extends JPanel{
		private int count;
		private int true_count;
		private LabelPane parent = null, myself = this;
		private JLabel label = null;
		private ImageIcon limage = null, limage2 = null, limage3 = null;
		private JPanel panel = null;
		private Vector<LabelPane> labels = null;
		private JPanel panel2 = null;
		private boolean flag = false, color_flag = false, sql_flag = false;
		private NoteGroup ng = null;
		private Note note = null;
		
		private void pre(boolean flag){
			count = 1;
			true_count = 1;
			setBackground(Color.WHITE);
			setLayout(null);
			if(flag){
				labels = new Vector<LabelPane>();
			}
		}
		
		private void init(){
			setName(label.getText());
			panel = new JPanel();
			panel.setLayout(null);
			label.setBounds(5, 5, 800, 40);
			panel.add(label);
			panel.setBounds(0, 0, 800, 45);
			panel.setBackground(Color.white);
			add(panel);
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					if(!color_flag){
						panel.setBackground(new Color(230, 230, 230));
						label.setIcon(limage2);
						label.setForeground(Color.white);
					}
				}
				@Override
				public void mouseExited(MouseEvent e) {
					if(!color_flag){
						panel.setBackground(Color.white);
						label.setIcon(limage);
						label.setForeground(Color.black);
					}
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					nl.setAllColorAndFlag();
					label.setIcon(limage3);
					panel.setBackground(new Color(200, 200, 200));
					color_flag = true;
					if(e.getClickCount() == 2 && labels != null && flag == false){	// 展开
						true_count = labels.size() + 1;
						if(parent != null){
							parent.changeLabels();
							MyNoteShowPane.getInstance().changeNg(ng, sql_flag);
							MyNoteShowPane.getInstance().changeShowPane();
						}
						panel2.setBounds(0, 50, 800, count * 50);
						flag = true;
					}
					else if(e.getClickCount() == 2 && labels != null && flag == true){	// 折叠
						true_count = 1;
						if(parent != null)
							parent.changeLabels();
						panel2.setBounds(-800, 50, 800, count * 50);
						flag = false;
					}
					else if(e.getClickCount() == 2 && labels == null){	// 选中笔记
						MyNoteShowPane.getInstance().changeNg(parent.getNg(), sql_flag);
						MyNoteShowPane.getInstance().changeShowPane();
						MyNotePane.getInstance().setNoteAndGroup(note, parent.getName(), sql_flag, new File(note.getPath()));
					}
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					if(e.isPopupTrigger()){
						if(parent == null)
							pmenu.show(e.getComponent(), e.getX(), e.getY());
						else if(labels == null){
							chooseN = myself;
							chooseNg = parent;
							nPmenu.show(e.getComponent(), e.getX(), e.getY());
						}
						else{
							chooseNg = myself;
							ngPmenu.show(e.getComponent(), e.getX(), e.getY());
						}
					}
				}
			});
		}
		
		public NoteGroup getNg() {
			return ng;
		}

		public void changeLabels(){
			int c = 0;
			for(int i = 0; i < labels.size(); i++){
				labels.get(i).setBounds(20, 50 * c, 800 , 50 * labels.get(i).getTrueCount());
				c += labels.get(i).getTrueCount();
			}
			if(parent != null)
				parent.changeLabels();
		}
		
		public void placeLabels(){
			panel2 = new JPanel();
			panel2.setLayout(null);
			panel2.setBackground(Color.white);
			for(int i = 0; i < labels.size(); i++){
				labels.get(i).setBounds(20, 50 * i, 800, 50 * labels.get(i).getTrueCount());
				panel2.add(labels.get(i));
			}
			panel2.setBounds(-800, 50, 800, count * 50);
			add(panel2);
		}
		
		public LabelPane(Note n, LabelPane p, boolean s){
			pre(false);
			parent = p;
			sql_flag = s;
			limage = new ImageIcon(image7);
			limage2 = new ImageIcon(image8);
			limage3 = new ImageIcon(image9);
			label = new JLabel(n.getName(), limage, JLabel.LEFT);
			note = n;
			init();
		}
		
		public LabelPane(NoteGroup ng, LabelPane p, boolean s) {
			pre(true);
			parent = p;
			sql_flag = s;
			this.ng = ng;
			limage = new ImageIcon(image4);
			limage2 = new ImageIcon(image5);
			limage3 = new ImageIcon(image6);
			label = new JLabel(ng.getName(), limage, JLabel.LEFT);
			init();
			Vector<Note> nv = ng.getAllNote();
			for(int i = 0; i < nv.size(); i++){
				LabelPane lp = new LabelPane(nv.get(i), this, s);
				labels.add(lp);
				count += lp.getCount();
			}
			placeLabels();
		}
		
		public LabelPane(String s, int count){
			pre(true);
			limage = new ImageIcon(image1);
			limage2 = new ImageIcon(image2);
			limage3 = new ImageIcon(image3);
			label = new JLabel(s, limage, JLabel.LEFT);
			init();
		}
		
		public void setBigFont(Font font){
			label.setFont(font);
		}
		
		public void setSmallFont(Font font){
			for(int i = 0; i < labels.size(); i++)
				labels.get(i).setBigFont(font);
		}
		
		public void setAllColorAndFlag(){
			label.setForeground(Color.black);
			label.setIcon(limage);
			panel.setBackground(Color.white);
			color_flag = false;
			if(labels != null)
				for(int i = 0; i < labels.size(); i++)
					labels.get(i).setAllColorAndFlag();
		}
		
		public void addLabelPane(LabelPane lp){
			labels.add(lp);
			count += lp.getCount();
		}
		
		public int getCount(){
			return count;
		}
		
		private int getTrueCount() {
			return true_count;
		}
		
		public Vector<LabelPane> getLabels(){
			return labels;
		}
		
		public String getName(){
			return label.getText();
		}
		
		public void setName(String text){
			label.setText(text);
		}
		
		public boolean getSQL_Flag(){
			return sql_flag;
		}
		
		public Note getNote(){
			return note;
		}
	}
	
	private MyUtilPane() {
		setMinimumSize(new Dimension(210, 880));
		setBackground(Color.WHITE);
		setLayout(null);
		
		addNL();
		createPmenu();
		createNgPmenu();
		createNPmenu();
	}

	private static MyUtilPane instance = new MyUtilPane();
	
	public static MyUtilPane getInstance(){
		return instance;
	}
	
	public void setNoteItems(){
		noteItems[2].setEnabled(true);
		noteItems[3].setEnabled(true);
	}
	
	private void createNPmenu() {
		nPmenu = new JPopupMenu();
		JMenuItem [] nItems = MyContentPane.createJMenuItems(new String[]{"重命名", "删除该笔记", "显示笔记信息"}, nPmenu, new int[]{1, 0, 1, 0, 1});

		// 重命名
		nItems[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new myDialog(8, "笔记名字:", "笔记重命名");
			}
		});
		
		// 删除该笔记
		nItems[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chooseN.getSQL_Flag())
					DataControl.getInstance().aboutMySQLNote(chooseNg.getName(), chooseN.getName(), false);
				else
					DataControl.getInstance().deleteNativeNote(chooseNg.getName(), chooseN.getName());
				MyNoteShowPane.getInstance().changeNg(chooseNg.getNg(), chooseNg.getSQL_Flag());
				MyNoteShowPane.getInstance().changeShowPane();
				addNL();
			}
		});
		
		// 显示笔记信息
		nItems[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new NoteMessageDialog(DataControl.getInstance().getUser().getName(), chooseNg.getName(), chooseN.getNote());
			}
		});
	}
	
	private void createNgPmenu() {
		ngPmenu = new JPopupMenu();
		JMenuItem [] ngItems = MyContentPane.createJMenuItems(new String[]{"创建笔记", "删除笔记", "重命名", "删除该笔记本组"}, ngPmenu, new int[]{1, 1, 0, 1, 1});
		
		// 创建笔记
		ngItems[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!MyNotePane.getInstance().isEdited() || MyNotePane.getInstance().saveDocument()){
					if(chooseNg.getSQL_Flag())
						new myDialog(9, "笔记名称：", "创建笔记");
					else
						new myDialog(6, "笔记名称：", "创建笔记");
				}
			}
		});
		
		// 删除笔记
		ngItems[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chooseNg.getSQL_Flag())
					new myDialog(10, "笔记名称：", "删除笔记");
				else
					new myDialog(5, "笔记名称：", "删除笔记");
			}
		});
		
		// 重命名
		ngItems[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new myDialog(7, "笔记本名称：", "重命名");
			}
		});
		
		// 删除该笔记本组
		ngItems[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chooseNg.getSQL_Flag())
					DataControl.getInstance().aboutMySQLNoteGroup(chooseNg.getName(), false);
				else
					DataControl.getInstance().deleteNativeNoteGroup(chooseNg.getName());
				addNL();
			}
		});
	}

	private void createPmenu() {
		pmenu = new JPopupMenu();
		noteItems = MyContentPane.createJMenuItems(
				new String[]{"新建本地笔记本组", "删除本地笔记本组", "新建数据库笔记本组", "删除数据库笔记本组","查找笔记本组"}, 
				pmenu, new int[]{1, 1, 0, 1 ,1 , 0, 1});
		
		// 新建本地笔记本组
		noteItems[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new myDialog(1, "笔记本组名称：", "新建本地笔记本组：");
			}
		});
		
		// 删除本地笔记本组
		noteItems[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new myDialog(2, "笔记本组名称：", "删除本地笔记本组：");
			}
		});
		
		// 新建数据库笔记本组
		noteItems[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new myDialog(3, "笔记本组名称：", "新建数据库笔记本组：");
			}
		});
		noteItems[2].setEnabled(false);
		
		// 删除数据库笔记本组
		noteItems[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new myDialog(4, "笔记本组名称：", "删除数据库笔记本组：");
			}
		});
		noteItems[3].setEnabled(false);
		
		// 查找笔记本组
		noteItems[4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}

	private void addNL() {
		removeAll();
		nl = new LabelPane("笔记本", 0);
		nl.setBigFont(new Font("微软雅黑", 0, 16));
		
		Vector<NoteGroup> ngv = DataControl.getInstance().getNativeNoteGroup();
		for(int i = 0; i < ngv.size(); i++){
			LabelPane lp = new LabelPane(ngv.get(i), nl, false);
			nl.addLabelPane(lp);
			lp.setSmallFont(new Font("微软雅黑", 0, 12));
		}
		Vector<NoteGroup> ngv2 = DataControl.getInstance().getMySQLNoteGroup();
		for(int i = 0; i < ngv2.size(); i++){
			LabelPane lp = new LabelPane(ngv2.get(i), nl, true);
			nl.addLabelPane(lp);
			lp.setSmallFont(new Font("微软雅黑", 0, 12));
		}
		nl.placeLabels();
		nl.setSmallFont(new Font("微软雅黑", 0, 14));
		nl.setBounds(0, 0, 400, nl.getCount() * 50);
		add(nl);
		repaint();
	}
	
	private class myDialog extends JDialog {
		public myDialog(int index, String text, String title) {
			setTitle(title);
			setLayout(null);
			
			JLabel label = new JLabel(text);
			label.setBounds(10, 10, 78, 30);
			add(label);
			
			JTextField tf = new JTextField();
			JButton ok = new JButton("确认");
			tf.setBounds(10, 40, 270, 30);
			tf.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if(e.getKeyCode() == Event.ENTER)
						ok.doClick();
				}
			});
			tf.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					if(e.getDocument().getLength() == 0)
						ok.setEnabled(false);
				}
				@Override
				public void insertUpdate(DocumentEvent e) {
					ok.setEnabled(true);
				}
				@Override
				public void changedUpdate(DocumentEvent e) {}
			});
			add(tf);
			
			JButton cancel = new JButton("取消");
			ok.setMargin(new Insets(5, 5, 5, 5));
			ok.setBounds(170, 75, 50, 30);
			ok.setEnabled(false);
			cancel.setMargin(new Insets(5, 5, 5, 5));
			cancel.setBounds(230, 75, 50, 30);
			ok.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean flag = false;
					if(index == 1) // 建立本地笔记本组
						flag = DataControl.getInstance().createNativeNoteGroup(tf.getText());
					else if(index == 2) // 删除本地笔记本组
						flag = DataControl.getInstance().deleteNativeNoteGroup(tf.getText());
					else if(index == 3)	// 建立数据库笔记本组
						flag = DataControl.getInstance().aboutMySQLNoteGroup(tf.getText(), true);
					else if(index == 4)	// 删除数据库笔记本组
						flag = DataControl.getInstance().aboutMySQLNoteGroup(tf.getText(), false);
					else if(index == 5){ // 删除笔记
						if(!chooseNg.getSQL_Flag())
							flag = DataControl.getInstance().deleteNativeNote(chooseNg.getName(), tf.getText());
						else{
							Note note = DataControl.getInstance().aboutMySQLNote(chooseNg.getName(), tf.getName(), false);
							flag = note == null;
						}
					}
					else if(index == 6){ // 创建本地笔记
						File file = null;
						JFileChooser chooser = new JFileChooser(new File("."));
						if (chooser.showSaveDialog(MyFrame.getFrame()) == JFileChooser.APPROVE_OPTION) {
							File newFile = chooser.getSelectedFile();
							if (newFile.exists()) {
								String message = newFile.getAbsolutePath() + " already exists. \n" + "Do you want to replace it?";
								if (JOptionPane.showConfirmDialog(MyFrame.getFrame(), message) == JOptionPane.YES_OPTION)
									file = newFile;
								else
									return;
							}
							else
								file = new File(newFile.getAbsolutePath());
							if(!file.exists())
								try {
									file.createNewFile();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
						}
						Note note = DataControl.getInstance().createNativeNote(chooseNg.getName(), tf.getText(), file.getAbsolutePath());
						if(note != null){
							flag = true;
							MyNotePane.getInstance().setNoteAndGroup(note, chooseNg.getName(), false, new File(note.getPath()));
						}
					}
					else if(index == 7){ // 笔记本组重命名
						String oldNgName = chooseNg.getName();
						chooseNg.setName(tf.getText());
						MyNotePane.getInstance().changeNoteGroup(oldNgName, tf.getText());
						if(!chooseNg.getSQL_Flag()){
							chooseNg.getNg().setName(tf.getText());
							DataControl.getInstance().saveNativeNoteGroups();
						}
						else
							DataControl.getInstance().changeMySQLNGName(oldNgName, tf.getText());
						MyNoteShowPane.getInstance().changeNgName();
					}
					else if(index == 8){ // 笔记重命名
						chooseN.getNote().setName(tf.getText());
						chooseN.setName(tf.getText());
						if(chooseN.getSQL_Flag())
							DataControl.getInstance().changeMySQLNoteName(chooseNg.getName(), chooseN.getName(), tf.getText());
						else
							DataControl.getInstance().saveNativeNoteGroups();
						MyNotePane.getInstance().setNoteAndGroup(chooseN.getNote(), chooseNg.getName(), false, new File(chooseN.getNote().getPath()));
					}
					else if(index == 9 || index == 10){ // 创建/删除数据库笔记本
						Note note = DataControl.getInstance().aboutMySQLNote(chooseNg.getName(), tf.getText(), index == 9);
						flag = true;
						if(note != null)
							MyNotePane.getInstance().setNoteAndGroup(note, chooseNg.getName(), true, new File(note.getPath()));
						else
							MyNotePane.getInstance().removeFile(tf.getText());
					}
					if(flag){
						addNL();
						if(index == 7 || index == 8 || index == 6 || index == 5 || index == 9){
							MyNoteShowPane.getInstance().changeNg(chooseNg.getNg(), chooseNg.getSQL_Flag());
							MyNoteShowPane.getInstance().changeShowPane();
						}
					}
					dispose();
				}
			});
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			add(ok);
			add(cancel);
			
			setSize(300, 150);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setModal(true);
			setVisible(true);
		}
	}
}
