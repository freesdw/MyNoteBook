package client.ui.menu;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import client.ui.frame.FontFrame;
import client.ui.frame.MyFrame;
import client.ui.frame.UserMessageDialog;

@SuppressWarnings("serial")
public class MyHelpMenu extends JMenu {
	public void help() {
		JOptionPane.showMessageDialog(MyFrame.getFrame(), "hahahhahaha");
	}

	public void showShortcuts() {
		String shortcuts = "Navigate in    |  Tab\n" + "Navigate out   |  Ctrl+Tab\n"
				+ "Navigate out backwards    |  Shift+Ctrl+Tab\n" + "Move up/down a line    |  Up/Down Arrown\n"
				+ "Move left/right a component or char    |  Left/Right Arrow\n"
				+ "Move up/down one vertical block    |  PgUp/PgDn\n" + "Move to start/end of line    |  Home/End\n"
				+ "Move to previous/next word    |  Ctrl+Left/Right Arrow\n"
				+ "Move to start/end of data    |  Ctrl+Home/End\n" + "Move left/right one block    |  Ctrl+PgUp/PgDn\n"
				+ "Select All    |  Ctrl+A\n" + "Extend selection up one line    |  Shift+Up Arrow\n"
				+ "Extend selection down one line    |  Shift+Down Arrow\n"
				+ "Extend selection to beginning of line    |  Shift+Home\n"
				+ "Extend selection to end of line    |  Shift+End\n"
				+ "Extend selection to beginning of data    |  Ctrl+Shift+Home\n"
				+ "Extend selection to end of data    |  Ctrl+Shift+End\n"
				+ "Extend selection left    |  Shift+Right Arrow\n" + "Extend selection right    |  Shift+Right Arrow\n"
				+ "Extend selection up one vertical block    |  Shift+PgUp\n"
				+ "Extend selection down one vertical block    |  Shift+PgDn\n"
				+ "Extend selection left one block    |  Ctrl+Shift+PgUp\n"
				+ "Extend selection right one block    |  Ctrl+Shift+PgDn\n"
				+ "Extend selection left one word    |  Ctrl+Shift+Left Arrow\n"
				+ "Extend selection right one word    |  Ctrl+Shift+Right Arrow\n";
		JOptionPane.showMessageDialog(MyFrame.getFrame(), shortcuts);
	}

	public void aboutMe() {
		JOptionPane.showMessageDialog(MyFrame.getFrame(), "This is a notePad");
	}
	
	public MyHelpMenu() {
		super("帮助");
		setFont(FontFrame.menuFont);
		setMargin(new Insets(5, 10, 5, 20));
		
		JMenuItem [] helpItems = MyMenuBar.createJMenuItems(new String[]{"帮助", "快捷键                    ", "关于我", "用户信息显示"}, this, new int[]{1, 1, 1, 1});
		
		// 帮助
		helpItems[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				help();
			}
		});
		
		// 快捷键
		helpItems[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showShortcuts();
			}
		});
		
		// 关于我
		helpItems[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutMe();
			}
		});
		
		// 用户信息
		helpItems[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new UserMessageDialog();
			}
		});
	}
}
