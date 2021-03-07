package client.ui.menu;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import client.ui.frame.FontFrame;
import client.ui.panel.MyNotePane;

@SuppressWarnings("serial")
public class MyFileMenu extends JMenu {
	public MyFileMenu(){
		super("�ļ�");
		setFont(FontFrame.menuFont);
		setMargin(new Insets(5, 10, 5, 20));
		
		// �����˵���
		String [] texts = {"�½�", "��", "����                               ", "���Ϊ", "����ʽ����", "����ʽ��ȡ",
				"ҳ������", "�˳�", "�ļ��ϴ�", "�ļ�����"};
		int [] nums = {1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1};
		JMenuItem [] menus = MyMenuBar.createJMenuItems(texts, this, nums);
		
		// �½�
		menus[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().startNewDocument();
			}
		});
		menus[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK));
		
		// ��
		menus[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().openDocument();
			}
		});
		menus[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_MASK));
		
		// ����
		menus[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().saveDocument();
			}
		});
		menus[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));
		
		// ���Ϊ
		menus[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().saveDocumentAs();
			}
		});
		
		// ����ʽ����
		menus[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().saveWithStyle();
			}
		});
		
		// ����ʽ��ȡ
		menus[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().openWithStyle();
			}
		});
		
		// ҳ������
		menus[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//
			}
		});
		
		// �˳�
		menus[7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().exit();
			}
		});
		menus[7].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,InputEvent.CTRL_MASK));
		
		// �ļ��ϴ�
		menus[8].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().upFile();
			}
		});
		
		// �ļ�����
		menus[9].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
}