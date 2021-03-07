package client.ui.frame;

import java.awt.Color;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import client.data.DataControl;
import client.error.AccountAndPasswordErrorException;

public class LoginFrame {
	private JFrame frame = null;
	private JTextField account = null;
	private JPasswordField password = null;
	private JButton enter = null, reload = null, quit = null;
	private JLabel error_message = null;
	
	public LoginFrame(){
		frame = new JFrame("µÇÂ¼½çÃæ");
		frame.setLayout(null);
		DataControl.getInstance().openSC();
		
		account = new JTextField(20);
		account.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e){
				if(e.getKeyCode() == Event.ENTER){
					if(account.getText().equals("")){
						error_message.setForeground(Color.red);
						error_message.setText("ÕËºÅ²»ÄÜÎª¿Õ£¡");
					}
					else
						password.requestFocus();
				}
			}
		});
		JPanel pane1 = new JPanel();
		pane1.add(new JLabel("ÓÊÏä"));
		
		pane1.add(account);
		pane1.setBounds(10, 10, 280, 25);
		
		password = new JPasswordField(20);
		password.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e){
				if(e.getKeyCode() == Event.ENTER){
					if(password.getPassword().length == 0){
						error_message.setForeground(Color.red);
						error_message.setText("ÃÜÂë²»ÄÜÎª¿Õ£¡");
					}
					else
						loginIn(1);
				}
			}
		});
		JPanel pane2 = new JPanel();
		pane2.add(new JLabel("ÃÜÂë"));
		pane2.add(password);
		pane2.setBounds(10, 45, 280, 25);
		
		enter = new JButton("µÇÂ¼");
		enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginIn(1);
			}
		});
		reload = new JButton("×¢²á");
		reload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginIn(2);
			}
		});
		quit = new JButton("È¡Ïû");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataControl.getInstance().closeSC();
				System.exit(0);
			}
		});
		JPanel pane3 = new JPanel();
		pane3.add(enter);
		pane3.add(reload);
		pane3.add(quit);
		pane3.setBounds(10, 80, 280, 35);
		
		error_message  = new JLabel("»¶Ó­µÇÂ¼");
		JPanel pane4 = new JPanel();
		pane4.add(error_message);
		pane4.setBounds(60, 120, 150, 25);
		
		frame.getContentPane().add(pane1);
		frame.getContentPane().add(pane2);
		frame.getContentPane().add(pane3);
		frame.getContentPane().add(pane4);	
		
		frame.setSize(300, 200);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				DataControl.getInstance().closeSC();
				System.exit(0);
			}
		});
		frame.setVisible(true);
	}
	
	// µÇÂ¼/×¢²á
	private void loginIn(int index){
		char[] password_arr = password.getPassword();
		String password_str = "";
		for(int i = 0; i < password_arr.length; i++)
			password_str += password_arr[i];
		boolean flag = false;
		try {
			if(index == 1)
				flag = DataControl.getInstance().login(account.getText(), password_str);
			else {
				frame.setVisible(false);
				new RegisterDialog();
				frame.dispose();
			}
		} catch (AccountAndPasswordErrorException e) {
			error_message.setForeground(Color.RED);
			error_message.setText(e.getMessage());
		}
		if(flag){
			frame.setVisible(false);
			new MyFrame();
			frame.dispose();
		}
	}
	
	public static void main(String[] args) {
		try{
    		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    	} catch (Exception error){
    		error.printStackTrace();
    	}
		new LoginFrame();
	}
}
