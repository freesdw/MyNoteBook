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
		super("文件");
		setFont(FontFrame.menuFont);
		setMargin(new Insets(5, 10, 5, 20));
		
		// 创建菜单项
		String [] texts = {"新建", "打开", "保存                               ", "另存为", "带格式保存", "带格式读取",
				"页面设置", "退出", "文件上传", "文件下载"};
		int [] nums = {1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 1};
		JMenuItem [] menus = MyMenuBar.createJMenuItems(texts, this, nums);
		
		// 新建
		menus[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().startNewDocument();
			}
		});
		menus[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK));
		
		// 打开
		menus[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().openDocument();
			}
		});
		menus[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_MASK));
		
		// 保存
		menus[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().saveDocument();
			}
		});
		menus[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));
		
		// 另存为
		menus[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().saveDocumentAs();
			}
		});
		
		// 带格式保存
		menus[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().saveWithStyle();
			}
		});
		
		// 带格式读取
		menus[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().openWithStyle();
			}
		});
		
		// 页面设置
		menus[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//
			}
		});
		
		// 退出
		menus[7].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().exit();
			}
		});
		menus[7].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,InputEvent.CTRL_MASK));
		
		// 文件上传
		menus[8].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().upFile();
			}
		});
		
		// 文件下载
		menus[9].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}
}