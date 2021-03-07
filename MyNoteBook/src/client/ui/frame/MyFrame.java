package client.ui.frame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;

import client.data.DataControl;
import client.ui.menu.MyMenuBar;
import client.ui.panel.MyContentPane;
import client.ui.panel.MyNotePane;
import client.ui.panel.MyNoteShowPane;

public class MyFrame {
	private static JFrame frame = null;
	
	public static JFrame getFrame(){
		return frame;
	}
	
	public MyFrame(){
		frame = new JFrame("±Ê¼Ç±¾");
		frame.setIconImage(frame.getToolkit().getImage("image/logo.jpg"));
		
		MyMenuBar menuBar =  new MyMenuBar();
		frame.setJMenuBar(menuBar);
		MyContentPane contentPane = new MyContentPane();
		frame.setContentPane(contentPane);
		
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				MyNotePane.getInstance().saveDocument();
				MyNoteShowPane.getInstance().saveNg();
				DataControl.getInstance().closeSC();
				System.exit(0);
			}
		});
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		
		try{
    		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    	} catch (Exception error){
    		error.printStackTrace();
    	}
		
		new MyFrame();
	}
}
