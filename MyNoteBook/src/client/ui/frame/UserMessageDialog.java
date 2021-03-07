package client.ui.frame;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import client.data.DataControl;
import client.io.User;

public class UserMessageDialog extends JDialog {
	private static final long serialVersionUID = 7746571453583365903L;
	private User user = DataControl.getInstance().getUser();
	private Font font = new Font("΢���ź�", 0, 14);
	private Insets in = new Insets(5, 5, 5, 5);
	private ButtonGroup bg = null;
	private JRadioButton maleButton = null, femaleButton = null;
	private String sex = user.getSex();
	
	public UserMessageDialog(){
		setTitle("�û���Ϣ");
		setLayout(null);
		
		addLabel("����:", 20, 10, 40, 30);
		addLabel("����:", 20, 45, 40, 30);
		addLabel("�ǳ�:", 20, 80, 40, 30);
		addLabel(user.getEmail(), 70, 10, 200, 30);
		
		JTextField passWTF = new JTextField(user.getPassword());
		passWTF.setBounds(70, 45, 180, 30);
		passWTF.setFont(font);
		add(passWTF);
		
		JTextField nameTF = new JTextField(user.getName());
		nameTF.setBounds(70, 80, 180, 30);
		nameTF.setFont(font);
		add(nameTF);
		
		addSexChoose();
		
		// ȷ���޸�
		JButton changeButton = new JButton("ȷ��");
		changeButton.setBounds(50, 290, 100, 30);
		changeButton.setFont(font);
		changeButton.setMargin(in);
		changeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user.setName(nameTF.getText());
				user.setPassword(passWTF.getText());
				user.setSex(sex);
				if(DataControl.getInstance().saveUser())
					JOptionPane.showMessageDialog(null, "�޸ĳɹ���");
				else
					JOptionPane.showMessageDialog(null, "�޸�ʧ�ܣ�");
				dispose();
			}
		});
		add(changeButton);
		
		// ȡ���޸�
		JButton cancelButton = new JButton("����");
		cancelButton.setBounds(160, 290, 100, 30);
		cancelButton.setFont(font);
		cancelButton.setMargin(in);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nameTF.setText(user.getName());
				passWTF.setText(user.getPassword());
				if(user.getSex() != null && user.getSex().equals("��")) {
					maleButton.setSelected(true);
					femaleButton.setSelected(false);
				} 
				else if (user.getSex() != null && user.getSex().equals("Ů")) {
					maleButton.setSelected(false);
					femaleButton.setSelected(true);
				}
			}
		});
		add(cancelButton);
		
		// �ر�
		JButton closeButton = new JButton("ȡ��");
		closeButton.setBounds(160, 330, 100, 30);
		closeButton.setFont(font);
		closeButton.setMargin(in);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(closeButton);
		
		setSize(280, 400);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void addSexChoose() {
		JPanel sexPane = new JPanel();
		sexPane.setBounds(20, 120, 232, 70);
		TitledBorder tb = BorderFactory.createTitledBorder("�Ա�");
		tb.setTitleFont(font);
		sexPane.setBorder(tb);
		add(sexPane);
		
		maleButton = new JRadioButton("��", user.getSex() != null && user.getSex().equals("��"));
		femaleButton = new JRadioButton("Ů", user.getSex() != null && user.getSex().equals("Ů"));
		maleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sex = "��";
			}
		});
		femaleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sex = "Ů";
			}
		});
		maleButton.setFont(font);
		femaleButton.setFont(font);
		sexPane.add(maleButton);
		sexPane.add(femaleButton);
		bg = new ButtonGroup();
		bg.add(maleButton);
		bg.add(femaleButton);
	}
	
	private void addLabel(String string, int i, int j, int k, int l) {
		JLabel label = new JLabel(string);
		label.setBounds(i, j, k, l);
		label.setFont(font);
		add(label);
	}
}
