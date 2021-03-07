package client.ui.menu;

import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import client.ui.frame.FontFrame;

@SuppressWarnings("serial")
public class MyMenuBar extends JMenuBar {
	public static JMenuItem [] createJMenuItems(String [] strArr, JMenu menu, int [] nums){
		JMenuItem [] menuItems = new JMenuItem[strArr.length];
		for(int i = 0, j = 0; i < strArr.length; i++, j++){
			if(nums[j] == 0){
				j++;
				menu.addSeparator();
			}
			if(nums[j] == 1)
				menuItems[i] = new JMenuItem(strArr[i]);
			else
				menuItems[i] = new JMenu(strArr[i]);
			menuItems[i].setFont(FontFrame.menuFont);
			menu.add(menuItems[i]);
		}
		return menuItems;
	}
	
	public MyMenuBar(){
		// ÉèÖÃÍâ¹Û
		setBackground(new Color(240, 240, 240));
				
		add(new MyFileMenu());
		add(new MyEditMenu());
		add(new MyFormatMenu());
		add(new MyOtherMenu());
		add(new MyHelpMenu());
	}
}
