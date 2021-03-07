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
import client.ui.panel.FindStringDialog;
import client.ui.panel.MyNotePane;

@SuppressWarnings("serial")
public class MyEditMenu extends JMenu {
	public MyEditMenu(){
		super("�༭");
		setFont(FontFrame.menuFont);
		setMargin(new Insets(5, 10, 5, 20));
		
		String [] texts = {"����                              Ctrl+Z", "����                              Ctrl+R", "����                              Ctrl+X", 
				"����                              Ctrl+C", "���                              Ctrl+V", "����",  "������һ��                                F3", 
				"�滻", "ת��", "ת����Сд", "ת����д", "ת��Сд", "ȫѡ", "ʱ��                                         F5"};
		int [] nums = {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1};
		JMenuItem [] menus = MyMenuBar.createJMenuItems(texts, this, nums);
		
		// ����
		menus[0].setAction(MyNotePane.getInstance().getUndoAction());
		menus[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,InputEvent.CTRL_MASK));
		
		// ����
		menus[1].setAction(MyNotePane.getInstance().getRedoAction());
		menus[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,InputEvent.CTRL_MASK));
		
		// ����
		menus[2].setAction(MyNotePane.getInstance().getCutAction());
		menus[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.CTRL_MASK));
		
		// ����
		menus[3].setAction(MyNotePane.getInstance().getCopyAction());
		menus[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_MASK));
		
		// ���
		menus[4].setAction(MyNotePane.getInstance().getPasteAction());
		menus[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.CTRL_MASK));
		
		// ����
		menus[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FindStringDialog();
			}
		});
		menus[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,InputEvent.CTRL_MASK));
		
		// ������һ��
		menus[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
//		menus[6].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,InputEvent.CTRL_MASK));	// F3
		
		// �滻
		menus[7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		menus[7].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,InputEvent.CTRL_MASK));
		
		// ת��
		menus[8].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		menus[8].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,InputEvent.CTRL_MASK));
		
		// ת����Сд
		menus[9].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().changeString();
			}
		});
		menus[9].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,InputEvent.CTRL_MASK));
		
		// ת����д
		menus[10].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().stringToUpper();
			}
		});
		menus[10].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,InputEvent.CTRL_MASK));
		
		// ת��Сд
		menus[11].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().stringToLower();
			}
		});
		menus[11].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,InputEvent.CTRL_MASK));
		
		// ȫѡ
		menus[12].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().selectAll();
			}
		});
		menus[12].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK));
		
		// ʱ��
		menus[13].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().addDate();
			}
		});
//		menus[13].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,InputEvent.CTRL_MASK));	// F5
	}
}
