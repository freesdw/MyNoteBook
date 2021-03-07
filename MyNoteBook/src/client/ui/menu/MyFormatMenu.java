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
		super("格式");
		setFont(FontFrame.menuFont);
		setMargin(new Insets(5, 10, 5, 20));
		
		addFontItem();
		addParagraphMenu();
		addStyleMenu();
	}
	
	// 字体
	private void addFontItem(){
		JMenuItem fontItem = new JMenuItem("字体");
		fontItem.setFont(FontFrame.menuFont);
		fontItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FontFrame();
			}
		});
		add(fontItem);
	}
	
	// 段落
	private void addParagraphMenu(){
		JMenu paragraphMenu = new JMenu("段落                        ");
		paragraphMenu.setFont(FontFrame.menuFont);
		add(paragraphMenu);
		String [] paragraphTexts = {"左对齐", "右对齐", "居中", "两端对齐", "增加缩进", "减小缩进"};
		int [] paragraphNums = {1, 1, 1, 1, 0, 1, 1};
		JMenuItem [] paragraphMenuItems = MyMenuBar.createJMenuItems(paragraphTexts, paragraphMenu, paragraphNums);
		
		// 左对齐
		paragraphMenuItems[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().setAlignmentBox(0);
			}
		});
		
		// 右对齐
		paragraphMenuItems[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().setAlignmentBox(2);
			}
		});
		
		// 居中
		paragraphMenuItems[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().setAlignmentBox(1);
			}
		});
		
		// 两端对齐
		paragraphMenuItems[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		// 增加缩进
		paragraphMenuItems[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doLeftIndentButton();
			}
		});
		// 快捷 CTRL + M
		
		// 减少缩进
		paragraphMenuItems[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doDeleteIndentButton();
			}
		});
		// 快捷 CTRL + SHIFT + M
	}
	
	// 样式
	private void addStyleMenu(){
		JMenu styleMenu = new JMenu("样式");
		styleMenu.setFont(FontFrame.menuFont);
		add(styleMenu);
		String [] styleTexts = {"加粗", "斜体", "下划线", "划掉", "高亮", "上标", "下标"};
		int [] styleNums = {1, 1, 0, 1, 1, 0, 1, 0, 1, 1};
		JMenuItem [] styleMenuItems = MyMenuBar.createJMenuItems(styleTexts, styleMenu, styleNums);
		
		// 加粗
		styleMenuItems[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doBoldButton();
			}
		});

		// 斜体
		styleMenuItems[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doItalicButton();
			}
		});

		// 下划线
		styleMenuItems[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doUnderlineButton();
			}
		});

		// 划掉
		styleMenuItems[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doInsertBreakButton();
			}
		});

		// 高亮
		styleMenuItems[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});

		// 上标
		styleMenuItems[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doSubscriptButton();
			}
		});

		// 下标
		styleMenuItems[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePane.getInstance().doSuperscriptButton();
			}
		});
	}
}
