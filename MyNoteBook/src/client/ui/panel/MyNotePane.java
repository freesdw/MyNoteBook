package client.ui.panel;

/*
 *   JTextPane��Ķ��������ʾ�ı���ͼƬ�ͳ����ӵȣ��ڴ�����һ��JTextPane��Ĵ��ı���ʽ����󣬿�����������д��
 *   ���֣�Ӣ�Ļ��ֵȣ������볬���ӻ�ͼƬ�����޷�ͨ��getText()����������г�������������ݣ�Ҳ���޷����������
 *   �֡�ͼƬ�ͳ����ӵ���ȫ�������ݣ������һ��ʼ��������JTextPane���HTML��ʽ��������Ȼ���Ա������ֵ���ʽ����
 *   ɫ�����塢�ֺŵȣ����޷���ͨ��insertIcon�����������롢��ʾͼƬ��������ֱ�ӱ����ˣ����ǵ��Ľ��˼·����ͨ����
 *   ����HTML�ļ��в���<img src="02.gif">�����ʵ�ֶ�ͼƬ�ı��棬���������Ƚϲ�����ʵ�֣���Ҫ��ͼƬ�������ض�λ
 *   �ã��ſ������´δ�JTextPane���������ʾ��ͼƬ���ڴ���JTextPane��Ĵ��ı������ڲ���ͼƬ�������ӡ���������
 *   ��ʽ�������κζ�JTextPane��Ķ�����еĸ�ʽ����ʱ����¼�����õ�λ�ú����ݣ������ı�����һ�𱣴浽Ӳ���ļ��У�
 *   �´ζ�ȡʱ�����ռ�¼���»ָ�ԭ���ݣ�ʵ�ֵĹ��̸����ӣ��۽�JTextPane����ͨ���������л��ķ�ʽ��Ӳ���б���Ϊһ��
 *   �ļ����´ζ�ȡʱ�ٷ����л�Ϊ��������
 *   ���ѡ�������ʵ�ֵĵ����ַ��������л���JTextPane���󣬱��ֳ�����Ч��������������ʵ�ֹ�����δ���ǳ����
 *   ����Ч�ʣ�����Ҳ��̫����������Ĵ���ʽ�ǲ��ǱȽ�û��Ч������
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
	private JPanel menuPane = null; // ������
	private JPanel toolPane = null; // ������
	private JScrollPane sp = null;
	private JTextPane textPane = null; // ��д���

	private StyleContext sc = null;
	private DefaultStyledDocument doc = null;

	private UndoHandler undoHandler = new UndoHandler(); // ��������������
	private UndoManager undo = new UndoManager(); // ��������������
	private UndoAction undoAction = new UndoAction(); // ����
	private RedoAction redoAction = new RedoAction(); // ����

	private Action cutAction = new DefaultEditorKit.CutAction(); // ����
	private Action copyAction = new DefaultEditorKit.CopyAction(); // ����
	private Action pasteAction = new DefaultEditorKit.PasteAction(); // ���

	private JComboBox<String> alignmentBox = null; // �������
	private JComboBox<String> fontFamiliesBox = null; // ��������
	private JComboBox<String> fontSizesBox = null; // �����С
	private JButton leftIndentButton = null; // ��������
	private JButton deleteIndentButton = null; // ��С����
	private JButton boldButton = new JButton(new StyledEditorKit.BoldAction()); // ����
	private JButton italicButton = new JButton(new StyledEditorKit.ItalicAction()); // б��
	private JButton underlineButton = new JButton(new StyledEditorKit.UnderlineAction()); // �»���
	private JButton insertBreakButton = null; // ����
	private JButton SubscriptButton = null; // �±�
	private JButton SuperscriptButton = null; // �ϱ�
	private JButton imageButton = new JButton(new DocImageAction("i")); // ����ͼƬ
	
	private File file = null; // ���ڴ򿪵��ļ�
	private Note note = null; // ѡ��򿪵ıʼ�
	private int leftIndent = 50; // ������������

	private JTextField fileName = null; // �ʼǱ�����
	private JLabel noteGroupName = null; // �ʼǱ�������
	private JLabel noteNews = null; // �ʼǱ���Ϣ
	private boolean hideFlag = false; // �������Ƿ�����
	private boolean isSql = false; // �Ƿ������ݿ�ʼǱ�
	
//	private boolean isReplace = false; // �Ƿ����滻
//	private int start = 0;
//	private int length = 0;
	
	private static MyNotePane instance = new MyNotePane(); // ����

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
		label.setFont(new Font("΢���ź�", 0, 14));
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
		textPane.setFont(new Font("΢���ź�", 0, 14));
		
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

		// ����
		String[] fontFamilies = { "������", "΢���ź�", "����", "����", "����", "������ͤ��ϸ�ڼ���", "����׼Բ_GBK", "����", "����", "Blackoak Std",
				"Adobe Arabic", "Adobe Fan Heiti Std", "Adobe ���� Std", "Adobe ���� Std", "Adobe ���� Std", "Adobe ���� Std",
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
		fontFamiliesBox.setFont(new Font("΢���ź�", 0, 14));
		toolPane.add(fontFamiliesBox);

		// �����С
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
		fontSizesBox.setFont(new Font("΢���ź�", 0, 14));
		toolPane.add(fontSizesBox);

		// ������ɫ
		JLabel colorLabel = new JLabel("<html><u>a</u></html>");
		colorLabel.setFont(new Font("΢���ź�", 0, 14));
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

		// ���屳����ɫ
		addLabel("g", 250, new JButton(new backgroundAction("g")));
		// ����
		addLabel("<html><b>B</b></html>", 285, boldButton);
		// б��
		addLabel("<html><i>I</i></html>", 320, italicButton);
		// �»���
		addLabel("<html><u>U</u></html>", 355, underlineButton);
		// ����
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
		// �±�
		SubscriptButton = new JButton(new StyledEditorKit.StyledTextAction(StyleConstants.Subscript.toString()) {
			public void actionPerformed(ActionEvent e) {
				boolean subscript = !(StyleConstants.isSubscript(getStyledEditorKit(textPane).getInputAttributes()));
				SimpleAttributeSet sas = new SimpleAttributeSet();
				StyleConstants.setSubscript(sas, subscript);
				setCharacterAttributes(textPane, sas, false);
			}
		});
		addLabel("<html>X<sub>2</sub></html>", 425, SubscriptButton);
		// �ϱ�
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

		// �������
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
		alignmentBox.setFont(new Font("΢���ź�", 0, 14));
		toolPane.add(alignmentBox);

		// ��������
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

		// ��������
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

		// ����ͼƬ
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
		fileName.setFont(new Font("΢���ź�", 0, 15));
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
		noteGroupName.setFont(new Font("΢���ź�", 0, 14));
		noteGroupName.setBounds(0, 0, 100, 30);
		panel.add(noteGroupName);
		panel.setBounds(5, 55, noteGroupName.getWidth(), noteGroupName.getHeight());
		panel.setBackground(new Color(160, 198, 238));
		menuPane.add(panel);

		Date date = new Date();
		noteNews = new JLabel((date.getYear() + 1900) + "/" + date.getMonth() + "/" + date.getDate());
		noteNews.setBounds(980, 55, 70, 30);
		noteNews.setFont(new Font("΢���ź�", 0, 13));
		menuPane.add(noteNews);

		JPanel pane = new JPanel();
		JLabel hideLabel = new JLabel("<html><u>A</u></html>");
		hideLabel.setFont(new Font("΢���ź�", 0, 14));
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

	// �ļ��ϴ�
	public void upFile() {
		if(note != null)
			DataControl.getInstance().upload(noteGroupName.getText(), note.getName(), file);
		else
			DataControl.getInstance().upload(noteGroupName.getText(), file.getName(), file);
	}

	// ���ļ�
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

	// ɾ���ļ�
	public void removeFile(String fileName){		// Ҫ��Ҫ����
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
	
	// �����ַ���
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
	
	// ����ʽ�� -- ����ͼƬ
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

	// ����ʽ���� -- ����ͼƬ
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

	// �½�
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

	// ��
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
//				if(JOptionPane.showConfirmDialog(MyFrame.getFrame(), "�Ƿ񰴸�ʽ��ȡ") != JOptionPane.YES_OPTION){
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
	
	// ����
	public boolean saveDocument() {
		if (file != null) {
			try {
				if(JOptionPane.showConfirmDialog(MyFrame.getFrame(), "�Ƿ񱣴浱ǰ�ĵ�") != JOptionPane.YES_OPTION)
					return false;
//				if(note != null && note.isSavedWithStyle()){
//					saveStyle();
//					return true;
//				}
//				if(JOptionPane.showConfirmDialog(MyFrame.getFrame(), "�Ƿ񰴸�ʽ����") != JOptionPane.YES_OPTION){
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
	
	// ���Ϊ
	public boolean saveDocumentAs() {
		if(textPane.getDocument().getLength() == 0){
			JOptionPane.showMessageDialog(MyFrame.getFrame(), "��δ�༭�ı�");
			return false;
		}
		if(JOptionPane.showConfirmDialog(MyFrame.getFrame(), "�Ƿ񱣴浱ǰ�ĵ�") != JOptionPane.YES_OPTION)
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
//				if(JOptionPane.showConfirmDialog(MyFrame.getFrame(), "�Ƿ񰴸�ʽ����") != JOptionPane.YES_OPTION){
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

	// �ر�
	public void exit() {
		if (textPane.getDocument().getLength() != 0 &&
				JOptionPane.showConfirmDialog(MyFrame.getFrame(), "Are you sure you want to save the document?") == JOptionPane.YES_OPTION)
			saveDocument();
		else
			System.exit(0);
	}

	// ���
	public void clear() {
		startNewDocument();
	}

	// ȫѡ
	public void selectAll() {
		textPane.selectAll();
	}

	// ��ӵ�ǰʱ��
	public void addDate() {
		try {
			textPane.getDocument().insertString(textPane.getDocument().getLength(), new Date().toString(),
					new SimpleAttributeSet());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	// ת��Ϊ��д
	public void stringToUpper() {
		textPane.replaceSelection(textPane.getSelectedText().toUpperCase());
	}

	// ת��ΪСд
	public void stringToLower() {
		textPane.replaceSelection(textPane.getSelectedText().toLowerCase());
	}

	// ��Сдת��
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

	// ���ö������
	public void setAlignmentBox(int i) {
		alignmentBox.setSelectedIndex(i);
	}

	// ������������
	public void setFontFamiliesBox(int i) {
		fontFamiliesBox.setSelectedIndex(i);
	}

	// ���������С
	public void setFontSizesBox(int i) {
		fontSizesBox.setSelectedIndex(i);
	}

	// ��ȡ��������
	public int getFontFamiliesBoxIndex() {
		return fontFamiliesBox.getSelectedIndex();
	}

	// ��������С
	public int getFontSizesBoxIndex() {
		return fontSizesBox.getSelectedIndex();
	}
	
	// ͼƬ
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

	// ����������������
	private void resetUndoManager() {
		undo.discardAllEdits();
		undoAction.update();
		redoAction.update();
	}

	// ����������������
	private class UndoHandler implements UndoableEditListener {
		public void undoableEditHappened(UndoableEditEvent e) {
			undo.addEdit(e.getEdit());
			undoAction.update();
			redoAction.update();
		}
	}

	// ����
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

	// ����
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
	
	// ��ӱ�����ɫ��action
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