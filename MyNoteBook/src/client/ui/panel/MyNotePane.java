package client.ui.panel;

/*
 *   JTextPane类的对象可以显示文本、图片和超链接等，在创建了一个JTextPane类的纯文本格式对象后，可以向里面填写入
 *   文字（英文或汉字等）、插入超链接或图片，但无法通过getText()方法获得其中除文字以外的内容，也即无法保存包含文
 *   字、图片和超链接的完全对象内容，而如果一开始创建的是JTextPane类的HTML格式对象，则虽然可以保存文字的样式（颜
 *   色和字体、字号等）但无法再通过insertIcon（）方法插入、显示图片，更不能直接保存了，考虑到的解决思路：①通过手
 *   工在HTML文件中插入<img src="02.gif">语句来实现对图片的保存，可是这样比较不容易实现，需要将图片保存在特定位
 *   置，才可以在下次打开JTextPane类对象是显示出图片；②创建JTextPane类的纯文本对象，在插入图片、超链接、设置字体
 *   样式或其它任何对JTextPane类的对象进行的格式设置时均记录下设置的位置和内容，并和文本内容一起保存到硬盘文件中，
 *   下次读取时，按照记录重新恢复原内容，实现的过程更复杂；③将JTextPane对象通过对象序列化的方式在硬盘中保存为一个
 *   文件，下次读取时再反序列化为完整对象。
 *   最后，选择了最好实现的第三种方法，序列化了JTextPane对象，表现出来的效果不错。不过，在实现过程中未考虑程序的
 *   运行效率，现在也不太清楚，这样的处理方式是不是比较没有效率哩？
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
//import javax.swing.event.CaretEvent;
//import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.StyledEditorKit.StyledTextAction;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import client.data.DataControl;
import client.io.Note;
import client.ui.frame.MyFrame;

@SuppressWarnings("serial")
public class MyNotePane extends JPanel {
	private JPanel menuPane = null; // 标题栏
	private JPanel toolPane = null; // 工具栏
	private JScrollPane sp = null;
	private JTextPane textPane = null; // 编写面板

	private StyleContext sc = null;
	private DefaultStyledDocument doc = null;

	private UndoHandler undoHandler = new UndoHandler(); // 撤销与重做处理
	private UndoManager undo = new UndoManager(); // 撤销与重做管理
	private UndoAction undoAction = new UndoAction(); // 撤销
	private RedoAction redoAction = new RedoAction(); // 重做

	private Action cutAction = new DefaultEditorKit.CutAction(); // 剪切
	private Action copyAction = new DefaultEditorKit.CopyAction(); // 复制
	private Action pasteAction = new DefaultEditorKit.PasteAction(); // 黏贴

	private JComboBox<String> alignmentBox = null; // 段落对齐
	private JComboBox<String> fontFamiliesBox = null; // 字体类型
	private JComboBox<String> fontSizesBox = null; // 字体大小
	private JButton leftIndentButton = null; // 增加缩进
	private JButton deleteIndentButton = null; // 减小缩进
	private JButton boldButton = new JButton(new StyledEditorKit.BoldAction()); // 粗体
	private JButton italicButton = new JButton(new StyledEditorKit.ItalicAction()); // 斜体
	private JButton underlineButton = new JButton(new StyledEditorKit.UnderlineAction()); // 下划线
	private JButton insertBreakButton = null; // 划掉
	private JButton SubscriptButton = null; // 下标
	private JButton SuperscriptButton = null; // 上标
	private JButton imageButton = new JButton(new DocImageAction("i")); // 插入图片
	
	private File file = null; // 现在打开的文件
	private Note note = null; // 选择打开的笔记
	private int leftIndent = 50; // 具体缩进像素

	private JTextField fileName = null; // 笔记本名称
	private JLabel noteGroupName = null; // 笔记本组名称
	private JLabel noteNews = null; // 笔记本信息
	private boolean hideFlag = false; // 工具栏是否隐藏
	private boolean isSql = false; // 是否是数据库笔记本
	
//	private boolean isReplace = false; // 是否是替换
//	private int start = 0;
//	private int length = 0;
	
	private static MyNotePane instance = new MyNotePane(); // 单例

	public static MyNotePane getInstance() {
		return instance;
	}
	
	public void setNoteAndGroup(Note note, String noteGroupName, boolean isSql, File file){
		this.note = note;	System.out.println(note);
		this.file = file;	System.out.println(file);
		readFile(file);
		fileName.setText(this.note.getName());
		this.noteGroupName.setText(noteGroupName);
		this.isSql = isSql;
	}
	
	public void changeNoteGroup(String oldName, String newName){
		if(noteGroupName.getText().equals(oldName))
			noteGroupName.setText(newName);
	}
	
	private MyNotePane() {
		setMinimumSize(new Dimension(400, 772));
		setLayout(null);
		setBackground(Color.white);

		addMenuPane();
		addToolPane();
		addTextPane();
	}
	
	private void addLabel(String text, int x, JButton button) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("微软雅黑", 0, 14));
		JPanel pane = new JPanel();
		pane.setBackground(Color.white);
		pane.setBounds(x, 5, 30, 30);
		pane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				button.doClick();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				pane.setBackground(new Color(219, 232, 245));
				pane.setBorder(new MatteBorder(1, 1, 1, 1, new Color(160, 198, 238)));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				pane.setBackground(Color.white);
				pane.setBorder(new MatteBorder(1, 1, 1, 1, Color.white));
			}
		});
		pane.add(label);
		toolPane.add(pane);
	}

	private void addTextPane() {
		sc = new StyleContext();
		doc = new DefaultStyledDocument(sc);
		doc.addUndoableEditListener(undoHandler);
		textPane = new JTextPane(doc);
		textPane.setBounds(0, 0, 1120, 680);
		textPane.setBackground(Color.white);
		textPane.setMargin(new Insets(20, 50, 0, 50));
		textPane.setFont(new Font("微软雅黑", 0, 14));
		
		sp = new JScrollPane(textPane);
		sp.setBounds(0, 144, 1120, 680);
		add(sp);

		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				// backspace
			}
		});
		
//		textPane.addCaretListener(new CaretListener() {
//			private MutableAttributeSet oldStyle =  new SimpleAttributeSet();
//            private MutableAttributeSet newStyle = new SimpleAttributeSet();
//            
//            {
//            	StyleConstants.setBackground(oldStyle, Color.white);
//            	StyleConstants.setBackground(newStyle, Color.blue);
//            }
//			
//			public void caretUpdate(CaretEvent e) {
//				if(isReplace == false){
//					doc.setCharacterAttributes(start, length, oldStyle, false);
//					start = textPane.getSelectionStart();
//	                length = textPane.getSelectionEnd() - start;
//	                doc.setCharacterAttributes(start, length, newStyle, true);
//				}
//			}
//		});
	}

	private void addToolPane() {
		toolPane = new JPanel();
		toolPane.setLayout(null);
		toolPane.setBounds(0, 102, 1120, 40);
		toolPane.setBackground(Color.white);
		add(toolPane);
		
		JSeparator sep = new JSeparator();
		sep.setBounds(0, 0, 1200, 2);
		sep.setForeground(Color.gray);
		toolPane.add(sep);

		// 字体
		String[] fontFamilies = { "新宋体", "微软雅黑", "宋体", "楷体", "黑体", "方正兰亭超细黑简体", "方正准圆_GBK", "等线", "仿宋", "Blackoak Std",
				"Adobe Arabic", "Adobe Fan Heiti Std", "Adobe 仿宋 Std", "Adobe 黑体 Std", "Adobe 楷体 Std", "Adobe 宋体 Std",
				"Roboto Slab", "Roboto", "Droid Serif", "Cambria Math", "Chaparral Pro", "Adobe Myungjo Std", "Calibri",
				"Adobe Naskh", "Adobe Hebrew", "Adobe Caslon Pro", "Adobe Devanagari", "Adobe Fan Heiti Std" };
		fontFamiliesBox = new JComboBox<String>(fontFamilies);
		fontFamiliesBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				fontFamiliesBox.setAction(new StyledEditorKit.FontFamilyAction((String) e.getItem(), (String) e.getItem()));
			}
		});
		fontFamiliesBox.setSelectedIndex(1);
		fontFamiliesBox.setBounds(5, 5, 120, 30);
		fontFamiliesBox.setFont(new Font("微软雅黑", 0, 14));
		toolPane.add(fontFamiliesBox);

		// 字体大小
		String[] fontSizes = { "8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48",
				"72" };
		fontSizesBox = new JComboBox<String>(fontSizes);
		fontSizesBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				fontSizesBox.setAction(new StyledEditorKit.FontSizeAction((String) e.getItem(), Integer.parseInt((String) e.getItem())));
			}
		});
		fontSizesBox.setSelectedIndex(5);
		fontSizesBox.setBounds(130, 5, 80, 30);
		fontSizesBox.setFont(new Font("微软雅黑", 0, 14));
		toolPane.add(fontSizesBox);

		// 字体颜色
		JLabel colorLabel = new JLabel("<html><u>a</u></html>");
		colorLabel.setFont(new Font("微软雅黑", 0, 14));
		JButton colorButton = new JButton();
		JPanel colorPane = new JPanel();
		colorPane.setBackground(Color.white);
		colorPane.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Color color = JColorChooser.showDialog(instance, "Choose Color", Color.black);
				colorButton.setAction(new StyledEditorKit.ForegroundAction("a", color));
				colorButton.doClick();
			}
			public void mouseEntered(MouseEvent e) {
				colorPane.setBackground(new Color(219, 232, 245));
				colorPane.setBorder(new MatteBorder(1, 1, 1, 1, new Color(160, 198, 238)));
			}
			public void mouseExited(MouseEvent e) {
				colorPane.setBackground(Color.white);
				colorPane.setBorder(new MatteBorder(1, 1, 1, 1, Color.white));
			}
		});
		colorPane.setBounds(215, 5, 30, 30);
		colorPane.add(colorLabel);
		toolPane.add(colorPane);

		// 字体背景颜色
		addLabel("g", 250, new JButton(new backgroundAction("g")));
		// 粗体
		addLabel("<html><b>B</b></html>", 285, boldButton);
		// 斜体
		addLabel("<html><i>I</i></html>", 320, italicButton);
		// 下划线
		addLabel("<html><u>U</u></html>", 355, underlineButton);
		// 划掉
		insertBreakButton = new JButton(new StyledEditorKit.StyledTextAction(StyleConstants.StrikeThrough.toString()) {
			public void actionPerformed(ActionEvent e) {
				boolean strikeThrough = !(StyleConstants
						.isStrikeThrough(getStyledEditorKit(textPane).getInputAttributes()));
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setStrikeThrough(sas, strikeThrough);
				setCharacterAttributes(textPane, sas, false);
			}
		});
		addLabel("<html><span style='text-decoration:line-through'>T</span></html>", 390, insertBreakButton);
		// 下标
		SubscriptButton = new JButton(new StyledEditorKit.StyledTextAction(StyleConstants.Subscript.toString()) {
			public void actionPerformed(ActionEvent e) {
				boolean subscript = !(StyleConstants.isSubscript(getStyledEditorKit(textPane).getInputAttributes()));
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setSubscript(sas, subscript);
				setCharacterAttributes(textPane, sas, false);
			}
		});
		addLabel("<html>X<sub>2</sub></html>", 425, SubscriptButton);
		// 上标
		SuperscriptButton = new JButton(new StyledEditorKit.StyledTextAction(StyleConstants.Superscript.toString()) {
			public void actionPerformed(ActionEvent e) {
				boolean superscript = !(StyleConstants
						.isSuperscript(getStyledEditorKit(textPane).getInputAttributes()));
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setSuperscript(sas, superscript);
				setCharacterAttributes(textPane, sas, false);
			}
		});
		addLabel("<html>X<sup>2</sup></html>", 460, SuperscriptButton);

		JSeparator sep2 = new JSeparator(JSeparator.VERTICAL);
		sep2.setBounds(495, 5, 1, 30);
		sep2.setForeground(new Color(200, 200, 200));
		toolPane.add(sep2);

		// 段落对齐
		// JLabel alignmentAction
		alignmentBox = new JComboBox<String>(new String[] { "Left Align", "Center", "Right Align" });
		alignmentBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (alignmentBox.getSelectedIndex() == 1)
					alignmentBox.setAction(
							new StyledEditorKit.AlignmentAction((String) e.getItem(), StyleConstants.ALIGN_CENTER));
				else if (alignmentBox.getSelectedIndex() == 2)
					alignmentBox.setAction(
							new StyledEditorKit.AlignmentAction((String) e.getItem(), StyleConstants.ALIGN_RIGHT));
				else
					alignmentBox.setAction(
							new StyledEditorKit.AlignmentAction((String) e.getItem(), StyleConstants.ALIGN_LEFT));
			}
		});
		alignmentBox.setBounds(501, 5, 120, 30);
		alignmentBox.setFont(new Font("微软雅黑", 0, 14));
		toolPane.add(alignmentBox);

		// 增加缩进
		leftIndentButton = new JButton();
		leftIndentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (leftIndent < textPane.getWidth())
					leftIndent += 50;
				textPane.setMargin(new Insets(20, leftIndent, 0, 50));
				textPane.repaint();
			}
		});
		addLabel("L", 626, leftIndentButton);

		// 减少缩进
		deleteIndentButton = new JButton();
		deleteIndentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (leftIndent > 0)
					leftIndent -= 50;
				textPane.setMargin(new Insets(20, leftIndent, 0, 50));
				textPane.repaint();
			}
		});
		addLabel("R", 661, deleteIndentButton);

		// 插入图片
		addLabel("i", 696, imageButton);
	}

	@SuppressWarnings("deprecation")
	private void addMenuPane() {
		menuPane = new JPanel();
		menuPane.setLayout(null);
		menuPane.setBounds(0, 0, 1120, 100);
		menuPane.setBackground(Color.white);
		add(menuPane);
		menuPane.removeAll();
		menuPane.repaint();

		fileName = new JTextField("");
		fileName.setFont(new Font("微软雅黑", 0, 15));
		MatteBorder mborder = new MatteBorder(1, 1, 1, 1, Color.white);
		MatteBorder mborder2 = new MatteBorder(1, 1, 1, 1, new Color(200, 200, 200));
		EmptyBorder eborder = new EmptyBorder(0, 5, 0, 0);
		fileName.setBorder(new CompoundBorder(mborder, eborder));
		fileName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (fileName.getName() != null && fileName.getName().equals("0"))
					fileName.setBorder(new CompoundBorder(mborder2, eborder));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (fileName.getName() != null && fileName.getName().equals("0"))
					fileName.setBorder(new CompoundBorder(mborder, eborder)); // bug1
			}
		});
		fileName.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				fileName.setName("0");
				fileName.setBorder(new CompoundBorder(mborder, eborder));
			}

			@Override
			public void focusGained(FocusEvent e) {
				fileName.setName("1");
				fileName.setBorder(new CompoundBorder(mborder2, eborder));
			}
		});
		fileName.setBounds(5, 10, 1000, 35);
		menuPane.add(fileName);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		noteGroupName = new JLabel();
		noteGroupName.setText("");
		noteGroupName.setHorizontalAlignment(JLabel.CENTER);
		noteGroupName.setVerticalAlignment(JLabel.CENTER);
		noteGroupName.setFont(new Font("微软雅黑", 0, 14));
		noteGroupName.setBounds(0, 0, 100, 30);
		panel.add(noteGroupName);
		panel.setBounds(5, 55, noteGroupName.getWidth(), noteGroupName.getHeight());
		panel.setBackground(new Color(160, 198, 238));
		menuPane.add(panel);

		Date date = new Date();
		noteNews = new JLabel((date.getYear() + 1900) + "/" + date.getMonth() + "/" + date.getDate());
		noteNews.setBounds(980, 55, 70, 30);
		noteNews.setFont(new Font("微软雅黑", 0, 13));
		menuPane.add(noteNews);

		JPanel pane = new JPanel();
		JLabel hideLabel = new JLabel("<html><u>A</u></html>");
		hideLabel.setFont(new Font("微软雅黑", 0, 14));
		pane.setBounds(1055, 55, 30, 30);
		pane.add(hideLabel);
		pane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (hideFlag == false) {
					toolPane.setBounds(0, 0, 1120, 40);
					sp.setBounds(0, 102, 1120, 722);
					hideFlag = true;
				} else {
					toolPane.setBounds(0, 102, 1120, 40);
					sp.setBounds(0, 144, 1120, 680);
					hideFlag = false;
				}
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				pane.setBackground(new Color(219, 232, 245));
				pane.setBorder(new MatteBorder(1, 1, 1, 1, new Color(160, 198, 238)));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				pane.setBackground(Color.white);
				pane.setBorder(new MatteBorder(1, 1, 1, 1, Color.white));
			}
		});
		menuPane.add(pane);
	}

	// 文件上传
	public void upFile() {
		if(note != null)
			DataControl.getInstance().upload(noteGroupName.getText(), note.getName(), file);
		else
			DataControl.getInstance().upload(noteGroupName.getText(), file.getName(), file);
	}

	// 打开文件
	private void readFile(File file) {
		try {
			FileReader fr = new FileReader(file);
			Document oldDoc = textPane.getDocument();
			if (oldDoc != null)
				oldDoc.removeUndoableEditListener(undoHandler);
			doc = new DefaultStyledDocument();
			new DefaultEditorKit().read(fr, doc, 0);
			doc.addUndoableEditListener(undoHandler);
			textPane.setDocument(doc);
			resetUndoManager();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	// 删除文件
	public void removeFile(String fileName){		// 要不要保存
		if(fileName.equals(this.fileName.getText())){
			Document oldDoc = textPane.getDocument();
			if (oldDoc != null)
				oldDoc.removeUndoableEditListener(undoHandler);
			doc = new DefaultStyledDocument();
			textPane.setDocument(doc);
			textPane.getDocument().addUndoableEditListener(undoHandler);
			resetUndoManager();
			file.delete();
		}
	}

	public boolean isEdited(){
		return textPane.getDocument().getLength() != 0;
	}
	
	public UndoAction getUndoAction() {
		return undoAction;
	}

	public RedoAction getRedoAction() {
		return redoAction;
	}

	public Action getCutAction() {
		return cutAction;
	}

	public Action getCopyAction() {
		return copyAction;
	}

	public Action getPasteAction() {
		return pasteAction;
	}

	public void doLeftIndentButton() {
		leftIndentButton.doClick();
	}

	public void doDeleteIndentButton() {
		deleteIndentButton.doClick();
	}

	public void doBoldButton() {
		boldButton.doClick();
	}

	public void doItalicButton() {
		italicButton.doClick();
	}

	public void doUnderlineButton() {
		underlineButton.doClick();
	}

	public void doInsertBreakButton() {
		insertBreakButton.doClick();
	}

	public void doSubscriptButton() {
		SubscriptButton.doClick();
	}

	public void doSuperscriptButton() {
		SuperscriptButton.doClick();
	}
	
	// 查找字符串
	public int find(String str, String newStr, boolean back, boolean isSelect){
		String look = isSelect ? textPane.getSelectedText() : textPane.getText();
		int index = 0;
		boolean flag = !newStr.equals("");
		if(back){
			index = look.toLowerCase().lastIndexOf(str.toLowerCase(), textPane.getCaretPosition() - str.length() - 1);
			if(index == -1)
				index = look.toLowerCase().lastIndexOf(str.toLowerCase(), textPane.getDocument().getLength());
		}
		else{
			index = look.toLowerCase().indexOf(str.toLowerCase(), textPane.getCaretPosition());
			if(index == -1)
				index = look.toLowerCase().indexOf(str.toLowerCase(), 0);
		}
		if(index == -1)
			return -1;
		if(flag){
			textPane.select(index, index + str.length());
			textPane.replaceSelection(newStr);
		}
		textPane.setSelectionStart(index);
		textPane.setSelectionEnd(index + (flag ? newStr.length() : str.length()));
		return index;
	}
	
	// 带格式打开 -- 带有图片
	public void openWithStyle() {
		noteGroupName.setText("");
		if (textPane.getDocument().getLength() != 0)
			saveWithStyle();
		try {
			JFileChooser chooser = new JFileChooser(new File("data/Client/"));
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int approval = chooser.showOpenDialog(MyFrame.getFrame());
			if (approval == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
				openStyle();
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openStyle() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		StyledDocument doc = (StyledDocument) ois.readObject();
		ois.close();
		textPane.setStyledDocument(doc);
		validate();
	}

	// 带格式保存 -- 带有图片
	public void saveWithStyle() {
		JFileChooser chooser = new JFileChooser(new File("data/Client"));
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int approval = chooser.showSaveDialog(MyFrame.getFrame());
		if (approval == JFileChooser.APPROVE_OPTION) {
			try {
				File newFile = chooser.getSelectedFile();
				if (newFile.exists()) {
					String message = newFile.getAbsolutePath() + " already exists. \n" + "Do you want to replace it?";
					if (JOptionPane.showConfirmDialog(MyFrame.getFrame(), message) == JOptionPane.YES_OPTION)
						file = newFile;
				} else
					file = new File(newFile.getAbsolutePath());
				saveStyle();
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void saveStyle() throws IOException {
		StyledDocument doc = (StyledDocument) textPane.getDocument();
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(doc);
		oos.flush();
		oos.close();
	}

	// 新建
	public void startNewDocument() {
		noteGroupName.setText("");
		fileName.setText("");
		if(textPane.getDocument().getLength() != 0)
			saveDocument();
		Document oldDoc = textPane.getDocument();
		if (oldDoc != null)
			oldDoc.removeUndoableEditListener(undoHandler);
		doc = new DefaultStyledDocument();
		textPane.setDocument(doc);
		file = null;
		textPane.getDocument().addUndoableEditListener(undoHandler);
		resetUndoManager();
	}

	// 打开
	public void openDocument() {
		try {
			if(textPane.getDocument().getLength() != 0)
				saveDocument();
//			if(note != null && note.isSavedWithStyle()){
//				openStyle();
//				return;
//			}
			JFileChooser chooser = new JFileChooser(new File("data/Client/"));
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			if (chooser.showOpenDialog(MyFrame.getFrame()) == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
//				if(JOptionPane.showConfirmDialog(MyFrame.getFrame(), "是否按格式读取") != JOptionPane.YES_OPTION){
//					openStyle();
//				}
//				else{
					readFile(file);
					noteGroupName.setText("");
					fileName.setText(file.getName());
//				}
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
	}
	
	// 保存
	public boolean saveDocument() {
		if (file != null) {
			try {
				if(JOptionPane.showConfirmDialog(MyFrame.getFrame(), "是否保存当前文档") != JOptionPane.YES_OPTION)
					return false;
//				if(note != null && note.isSavedWithStyle()){
//					saveStyle();
//					return true;
//				}
//				if(JOptionPane.showConfirmDialog(MyFrame.getFrame(), "是否按格式保存") != JOptionPane.YES_OPTION){
//					saveStyle();
//					if(note != null)
//						note.setSavedWithStyle(true);
//				}
//				else{
					FileWriter fw = new FileWriter(file);
					fw.write(textPane.getText());
					fw.close();
//				}
				if(isSql)
					DataControl.getInstance().upload(noteGroupName.getText(), note.getName(), file);
				return true;
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			return saveDocumentAs();
		return false;
	}
	
	// 另存为
	public boolean saveDocumentAs() {
		if(textPane.getDocument().getLength() == 0){
			JOptionPane.showMessageDialog(MyFrame.getFrame(), "尚未编辑文本");
			return false;
		}
		if(JOptionPane.showConfirmDialog(MyFrame.getFrame(), "是否保存当前文档") != JOptionPane.YES_OPTION)
			return false;
		JFileChooser chooser = new JFileChooser(new File("data/Client"));
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (chooser.showSaveDialog(MyFrame.getFrame()) == JFileChooser.APPROVE_OPTION) {
			try {
				File newFile = chooser.getSelectedFile();
				if (newFile.exists()) {
					String message = newFile.getAbsolutePath() + " already exists.\n" + "Do you want to replace it?";
					if (JOptionPane.showConfirmDialog(MyFrame.getFrame(), message) == JOptionPane.YES_OPTION)
						file = newFile;
					else
						return false;
				} else
					file = new File(newFile.getAbsolutePath());
//				if(JOptionPane.showConfirmDialog(MyFrame.getFrame(), "是否按格式保存") != JOptionPane.YES_OPTION){
//					saveStyle();
//					if(note != null)
//						note.setSavedWithStyle(true);
//				}
//				else{
					FileWriter fw = new FileWriter(file);
					fw.write(textPane.getText());
					fw.close();
//				}
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	// 关闭
	public void exit() {
		if (textPane.getDocument().getLength() != 0 &&
				JOptionPane.showConfirmDialog(MyFrame.getFrame(), "Are you sure you want to save the document?") == JOptionPane.YES_OPTION)
			saveDocument();
		else
			System.exit(0);
	}

	// 清空
	public void clear() {
		startNewDocument();
	}

	// 全选
	public void selectAll() {
		textPane.selectAll();
	}

	// 添加当前时间
	public void addDate() {
		try {
			textPane.getDocument().insertString(textPane.getDocument().getLength(), new Date().toString(),
					new SimpleAttributeSet());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	// 转换为大写
	public void stringToUpper() {
		textPane.replaceSelection(textPane.getSelectedText().toUpperCase());
	}

	// 转换为小写
	public void stringToLower() {
		textPane.replaceSelection(textPane.getSelectedText().toLowerCase());
	}

	// 大小写转换
	public void changeString() {
		String str = textPane.getSelectedText();
		byte[] smg = str.getBytes();
		for (int i = 0; i < smg.length; i++)
			if (smg[i] <= 122 && smg[i] >= 97)
				smg[i] -= 32;
			else if (smg[i] >= 'A' && smg[i] <= 'Z')
				smg[i] += 32;
		textPane.replaceSelection(new String(smg));
	}

	// 设置段落对齐
	public void setAlignmentBox(int i) {
		alignmentBox.setSelectedIndex(i);
	}

	// 设置字体类型
	public void setFontFamiliesBox(int i) {
		fontFamiliesBox.setSelectedIndex(i);
	}

	// 设置字体大小
	public void setFontSizesBox(int i) {
		fontSizesBox.setSelectedIndex(i);
	}

	// 获取字体类型
	public int getFontFamiliesBoxIndex() {
		return fontFamiliesBox.getSelectedIndex();
	}

	// 获得字体大小
	public int getFontSizesBoxIndex() {
		return fontSizesBox.getSelectedIndex();
	}
	
	// 图片
	private class DocImageAction extends StyledTextAction {
		public DocImageAction(String nm) {
			super(nm);
		}

		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser(new File("image"));
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int approval = chooser.showOpenDialog(textPane);
			if (approval == JFileChooser.APPROVE_OPTION) {
				ImageIcon icon = new ImageIcon(chooser.getSelectedFile().getAbsolutePath());
				if (textPane != null) 
					textPane.insertIcon(icon);
			}
		}
	}

	// 撤销与重做管理类
	private void resetUndoManager() {
		undo.discardAllEdits();
		undoAction.update();
		redoAction.update();
	}

	// 撤销与重做管理类
	private class UndoHandler implements UndoableEditListener {
		public void undoableEditHappened(UndoableEditEvent e) {
			undo.addEdit(e.getEdit());
			undoAction.update();
			redoAction.update();
		}
	}

	// 撤销
	private class UndoAction extends AbstractAction {
		public UndoAction() {
			setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				undo.undo();
			} catch (CannotUndoException ex) {
				ex.printStackTrace();
			}
			update();
			redoAction.update();
		}

		protected void update() {
			if (undo.canUndo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getUndoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Undo");
			}
		}
	}

	// 重做
	private class RedoAction extends AbstractAction {
		public RedoAction() {
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				undo.redo();
			} catch (CannotRedoException ex) {
				ex.printStackTrace();
			}
			update();
			undoAction.update();
		}

		protected void update() {
			if (undo.canRedo()) {
				setEnabled(true);
				putValue(Action.NAME, undo.getRedoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Redo");
			}
		}
	}
	
	// 添加背景颜色的action
	private class backgroundAction extends StyledTextAction {
		public backgroundAction(String nm) {
			super(nm);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Color color = JColorChooser.showDialog(instance, "color choose", Color.white);
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setBackground(attr, color);
			setCharacterAttributes(textPane, attr, false);
		}
	}
}