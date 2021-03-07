package client.ui.panel;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;

import client.ui.frame.FontFrame;

@SuppressWarnings("serial")
public class MyContentPane extends JSplitPane {
	public static JMenuItem [] createJMenuItems(String [] strArr, JPopupMenu menu, int [] nums){
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
	
	public MyContentPane(){
		JSplitPane subSplitPane = new JSplitPane();
		subSplitPane.setDividerSize(2);
		MyUtilPane utilPane = MyUtilPane.getInstance();
		subSplitPane.setLeftComponent(utilPane);
		MyNoteShowPane notesShowPane = MyNoteShowPane.getInstance();
		subSplitPane.setRightComponent(notesShowPane);
		
		setDividerSize(2);
		setLeftComponent(subSplitPane);
		MyNotePane notePane = MyNotePane.getInstance();
		setRightComponent(notePane);
	}
}
