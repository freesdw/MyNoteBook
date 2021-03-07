package client.ui.frame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.data.DataControl;
import client.error.AccountAndPasswordErrorException;
import client.ui.frame.MyFrame;

public class RegisterDialog {
//	private int curSec = -1;

	JFrame frame = null;
	private JPasswordField password = null;
	private JTextField mail = null;
	private JTextField verifyCode = null;

	private JLabel lb_pwd;
	private JLabel lb_m;
	private JLabel lb_code;
	private JLabel error_message;

	private JButton b1;
	private JButton b2;
	private JButton getEmail;

	private String userMail;
	private String userPassword;
	private String yanzhengCode = "123";

	public RegisterDialog() {
		frame = new JFrame("�û�ע��");
		frame.setLayout(null);
		init();
		frame.setSize(300, 300);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		
		frame.setResizable(false);
	}

	private void init() {
		lb_pwd = new JLabel("���룺");
		lb_m = new JLabel("���䣺");
		lb_code = new JLabel("��������֤�룺");
		b1 = new JButton("ע��");
		b2 = new JButton("ȡ��");
		getEmail = new JButton("����������֤��");

		error_message = new JLabel();

		password = new JPasswordField(30);
		mail = new JTextField(40);
		verifyCode = new JTextField(10);

		lb_m.setBounds(20, 10, 50, 25);
		mail.setBounds(80, 10, 150, 25);
		lb_pwd.setBounds(20, 45, 50, 25);
		password.setBounds(80, 45, 150, 25);
		getEmail.setBounds(80, 80, 140, 25);
		lb_code.setBounds(20, 115, 120, 25);
		verifyCode.setBounds(130, 115, 100, 25);
		error_message.setBounds(80, 150, 150, 25);
		b1.setBounds(80, 200, 60, 25);
		b2.setBounds(150, 200, 60, 25);

		getEmail.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			//	if(curSec == -1){
					userMail = mail.getText();
					if(checkEmail(userMail)) {
						try {
							yanzhengCode = sendEmail(userMail);
							error_message.setText("");
					//	curSec = 59;
					//		Timer timer = new Timer();
					//		timer.schedule(new TimerTask() {
//								@Override
//								public void run() {
//									getEmail.setText("��" + curSec-- + "������ط���");
//								}
//							}, 0, 1000);
//							try {
//								TimeUnit.SECONDS.sleep(59);
//							} catch (InterruptedException e1) {
//								e1.printStackTrace();
//							}
//							timer.cancel();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					} else {
						error_message.setForeground(Color.red);
						error_message.setText("�����ʽ����ȷ��");
					}
	//			}
			}
		});

		b1.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(verifyCode.getText().equals(yanzhengCode)) {
					//ע��
					userPassword = new String(password.getPassword());
					if(userPassword.length() < 6) {
						error_message.setForeground(Color.red);
						error_message.setText("���벻������6���ַ���");
					} else {
						try {
							if(DataControl.getInstance().register(userMail, userPassword)) {
								frame.setVisible(false);
								JOptionPane.showMessageDialog(null, "ע��ɹ���");
								new MyFrame();
								frame.dispose();
							} else {
								error_message.setForeground(Color.red);
								error_message.setText("�������ѱ�ע�ᣡ");
							}
						} catch (AccountAndPasswordErrorException e) {
							e.printStackTrace();
						}
					}
				} else {
					error_message.setForeground(Color.red);
					error_message.setText("��֤�����");
				}
			}
		});
		b2.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				DataControl.getInstance().closeSC();
				System.exit(0);
			}
		});

		frame.add(lb_m);
		frame.add(mail);
		frame.add(lb_pwd);
		frame.add(password);
		frame.add(getEmail);
		frame.add(lb_code);
		frame.add(verifyCode);
		frame.add(error_message);
		frame.add(b1);
		frame.add(b2);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				DataControl.getInstance().closeSC();
				System.exit(0);
			}
		});
	}

	/**
	 * ���û������ʼ�
	 * @param accountMail �û�������
	 * @return ���ɵ������֤��
	 * @throws Exception
	 */
	private String sendEmail(String accountMail) throws Exception {
		Integer num = (int)((Math.random()*9 + 1)*100000);
		String rdNum = num.toString();

		String content = "���ã���ӭ��ע��ʼǱ�������ע����֤��Ϊ��" + rdNum + "�����Ǳ��˲���������Դ��ʼ���";

		new CheckMail(accountMail, "�д�java��Ŀ�����ʼǱ�", content);

		return rdNum;
	}
	/**
	 * ��֤�û�����������ʽ�Ƿ���ȷ
	 * @param email
	 * @return
	 */
	private boolean checkEmail(String email)
	{// ��֤�����������ʽ 
		boolean tag = true;
		final String str_pattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		final Pattern pattern = Pattern.compile(str_pattern);
		final Matcher matcher = pattern.matcher(email);
		if (!matcher.find()) {
			tag = false;
		}
		return tag;
	}
}
