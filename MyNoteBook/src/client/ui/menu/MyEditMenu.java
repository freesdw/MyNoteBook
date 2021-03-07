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
		super("编辑");
		setFont(FontFrame.menuFont);
		setMargin(new Insets(5, 10, 5, 20));
		
		String [] texts = {"撤销                              Ctrl+Z", "重来                              Ctrl+R", "剪切                              Ctrl+X", 
				"复制                              Ctrl+C", "黏贴                              Ctrl+V", "查找",  "查找下一个                                F3", 
				"替换", "转到", "转换大小写", "转换大写", "转换小写", "全选", "时间                                         F5"};
		int [] nums = {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1};
		JMenuItem [] menus = MyMenuBar.createJMenuItems(texts, this, nums);
		
		// 撤销
		menus[0].setAction(MyNotePane.getInstance().getUndoAction());
		menus[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,InputEvent.CTRL_MASK));
		
		// 重做
		menus[1].setAction(MyNotePane.getInstance().getRedoAction());
		menus[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,InputEvent.CTRL_MASK));
		
		// 剪切
		menus[2].setAction(MyNotePane.getInstance().getCutAction());
		menus[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.CTRL_MASK));
		
		// 复制
		menus[3].setAction(MyNotePane.getInstance().getCopyAction());
		menus[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_MASK));
		
		// 黏贴
		menus[4].setAction(MyNotePane.getInstance().getPasteAction());
		menus[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.CTRL_MASK));
		
		// 查找
		menus[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FindStringDialog();
			}
		});
		menus[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,InputEvent.CTRL_MASK));
		
		// 查找下一个
		menus[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
//		menus[6].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,InputEvent.CTRL_MASK));	// F3
		
		// 替换
		menus[7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		menus[7].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,InputEvent.CTRL_MASK));
		
		// 转到
		menus[8].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		menus[8].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,InputEvent.CTRL_MASK));
		
		// 转换大小写
		menus[9].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().changeString();
			}
		});
		menus[9].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,InputEvent.CTRL_MASK));
		
		// 转换大写
		menus[10].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().stringToUpper();
			}
		});
		menus[10].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,InputEvent.CTRL_MASK));
		
		// 转换小写
		menus[11].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().stringToLower();
			}
		});
		menus[11].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,InputEvent.CTRL_MASK));
		
		// 全选
		menus[12].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().selectAll();
			}
		});
		menus[12].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK));
		
		// 时间
		menus[13].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().addDate();
			}
		});
//		menus[13].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,InputEvent.CTRL_MASK));	// F5
	}
}
