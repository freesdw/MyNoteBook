package client.ui.menu;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import client.ui.frame.FontFrame;
import xdoc.XDocBuilderEmbed;
import xdoc.XDocEditorEmbed;
import xdoc.XDocViewerEmbed;

@SuppressWarnings("serial")
public class MyOtherMenu extends JMenu {
	public MyOtherMenu(){
		super("����");
		setFont(FontFrame.menuFont);
		setMargin(new Insets(5, 10, 5, 20));
		
		JMenuItem [] otherItems = MyMenuBar.createJMenuItems(new String[]{"��Ƕ�����", "��Ƕ�༭��", "��Ƕ�Ķ���"}, this, new int[]{1, 1, 1});
		
		// XDocBuilder
		otherItems[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new XDocBuilderEmbed();
			}
		});
		
		// XDocEditor
		otherItems[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new XDocEditorEmbed();
			}
		});
		
		// XDocViewer
		otherItems[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new XDocViewerEmbed();
			}
		});
	}
}
