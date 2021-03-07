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
	private Font font = new Font("微软雅黑", 0, 14);
	private Insets in = new Insets(5, 5, 5, 5);
	private ButtonGroup bg = null;
	private JRadioButton maleButton = null, femaleButton = null;
	private String sex = user.getSex();
	
	public UserMessageDialog(){
		setTitle("用户信息");
		setLayout(null);
		
		addLabel("邮箱:", 20, 10, 40, 30);
		addLabel("密码:", 20, 45, 40, 30);
		addLabel("昵称:", 20, 80, 40, 30);
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
		
		// 确定修改
		JButton changeButton = new JButton("确定");
		changeButton.setBounds(50, 290, 100, 30);
		changeButton.setFont(font);
		changeButton.setMargin(in);
		changeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				user.setName(nameTF.getText());
				user.setPassword(passWTF.getText());
				user.setSex(sex);
				if(DataControl.getInstance().saveUser())
					JOptionPane.showMessageDialog(null, "修改成功！");
				else
					JOptionPane.showMessageDialog(null, "修改失败！");
				dispose();
			}
		});
		add(changeButton);
		
		// 取消修改
		JButton cancelButton = new JButton("重置");
		cancelButton.setBounds(160, 290, 100, 30);
		cancelButton.setFont(font);
		cancelButton.setMargin(in);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nameTF.setText(user.getName());
				passWTF.setText(user.getPassword());
				if(user.getSex() != null && user.getSex().equals("男")) {
					maleButton.setSelected(true);
					femaleButton.setSelected(false);
				} 
				else if (user.getSex() != null && user.getSex().equals("女")) {
					maleButton.setSelected(false);
					femaleButton.setSelected(true);
				}
			}
		});
		add(cancelButton);
		
		// 关闭
		JButton closeButton = new JButton("取消");
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
		TitledBorder tb = BorderFactory.createTitledBorder("性别");
		tb.setTitleFont(font);
		sexPane.setBorder(tb);
		add(sexPane);
		
		maleButton = new JRadioButton("男", user.getSex() != null && user.getSex().equals("男"));
		femaleButton = new JRadioButton("女", user.getSex() != null && user.getSex().equals("女"));
		maleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sex = "男";
			}
		});
		femaleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sex = "女";
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
