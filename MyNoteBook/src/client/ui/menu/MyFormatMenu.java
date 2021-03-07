package client.ui.menu;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import client.ui.frame.FontFrame;
import client.ui.panel.MyNotePane;

@SuppressWarnings("serial")
public class MyFormatMenu extends JMenu {
	public MyFormatMenu(){
		super("��ʽ");
		setFont(FontFrame.menuFont);
		setMargin(new Insets(5, 10, 5, 20));
		
		addFontItem();
		addParagraphMenu();
		addStyleMenu();
	}
	
	// ����
	private void addFontItem(){
		JMenuItem fontItem = new JMenuItem("����");
		fontItem.setFont(FontFrame.menuFont);
		fontItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FontFrame();
			}
		});
		add(fontItem);
	}
	
	// ����
	private void addParagraphMenu(){
		JMenu paragraphMenu = new JMenu("����                        ");
		paragraphMenu.setFont(FontFrame.menuFont);
		add(paragraphMenu);
		String [] paragraphTexts = {"�����", "�Ҷ���", "����", "���˶���", "��������", "��С����"};
		int [] paragraphNums = {1, 1, 1, 1, 0, 1, 1};
		JMenuItem [] paragraphMenuItems = MyMenuBar.createJMenuItems(paragraphTexts, paragraphMenu, paragraphNums);
		
		// �����
		paragraphMenuItems[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().setAlignmentBox(0);
			}
		});
		
		// �Ҷ���
		paragraphMenuItems[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().setAlignmentBox(2);
			}
		});
		
		// ����
		paragraphMenuItems[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().setAlignmentBox(1);
			}
		});
		
		// ���˶���
		paragraphMenuItems[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		// ��������
		paragraphMenuItems[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doLeftIndentButton();
			}
		});
		// ��� CTRL + M
		
		// ��������
		paragraphMenuItems[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doDeleteIndentButton();
			}
		});
		// ��� CTRL + SHIFT + M
	}
	
	// ��ʽ
	private void addStyleMenu(){
		JMenu styleMenu = new JMenu("��ʽ");
		styleMenu.setFont(FontFrame.menuFont);
		add(styleMenu);
		String [] styleTexts = {"�Ӵ�", "б��", "�»���", "����", "����", "�ϱ�", "�±�"};
		int [] styleNums = {1, 1, 0, 1, 1, 0, 1, 0, 1, 1};
		JMenuItem [] styleMenuItems = MyMenuBar.createJMenuItems(styleTexts, styleMenu, styleNums);
		
		// �Ӵ�
		styleMenuItems[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doBoldButton();
			}
		});

		// б��
		styleMenuItems[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doItalicButton();
			}
		});

		// �»���
		styleMenuItems[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doUnderlineButton();
			}
		});

		// ����
		styleMenuItems[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doInsertBreakButton();
			}
		});

		// ����
		styleMenuItems[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});

		// �ϱ�
		styleMenuItems[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doSubscriptButton();
			}
		});

		// �±�
		styleMenuItems[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doSuperscriptButton();
			}
		});
	}
}
