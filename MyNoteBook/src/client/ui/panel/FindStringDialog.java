package client.ui.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class FindStringDialog extends JFrame {
	private static final long serialVersionUID = 3127072415918288980L;
	private Font font = new Font("微软雅黑", 0, 12);
	private Insets in = new Insets(5, 5, 5, 5);
	private boolean back = false, isSelect = false;
	private JLabel error = null;

	public FindStringDialog() {
		setTitle("查找字符串");
		setLayout(null);
		
		addLabel("Find:", 10, 10, 80, 30);

		JTextField findTf = new JTextField();
		findTf.setBounds(90, 10, 175, 30);
		add(findTf);

		addLabel("Replace with:", 10, 45, 80, 30);

		JTextField replTf = new JTextField();
		replTf.setBounds(90, 45, 175, 30);
		add(replTf);

		direction();

		scope();

		// 查找
		JButton findButton = new JButton("Find");
		findButton.setFont(font);
		findButton.setMargin(in);
		findButton.setBounds(55, 260, 100, 30);
		findButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = MyNotePane.getInstance().find(findTf.getText(), "", back, isSelect);
				if(index == -1)
					error.setText("No such String");
				else
					error.setText("");
			}
		});
		add(findButton);

		// 替换
		JButton replButton = new JButton("Replace");
		replButton.setFont(font);
		replButton.setMargin(in);
		replButton.setBounds(165, 260, 100, 30);
		replButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(replTf.getText().equals("")){
					error.setText("No string to be replaced");
					return;
				}
				int index = MyNotePane.getInstance().find(findTf.getText(), replTf.getText(), back, isSelect);
				if(index == -1)
					error.setText("No such String");
				else
					error.setText("");
			}
		});
		add(replButton);

		// 替换并查找下一个
		JButton fareButton = new JButton("Replace/Find");
		fareButton.setFont(font);
		fareButton.setMargin(in);
		fareButton.setBounds(55, 300, 100, 30);
		fareButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				replButton.doClick();
				findButton.doClick();
			}
		});
		add(fareButton);

		// 替换全部
		JButton reAlButton = new JButton("Replace All");
		reAlButton.setFont(font);
		reAlButton.setMargin(in);
		reAlButton.setBounds(165, 300, 100, 30);
		reAlButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = MyNotePane.getInstance().find(findTf.getText(), replTf.getText(), back, isSelect);
				if(index == -1){
					error.setText("No such String");
				}
				else
					error.setText("");
				while(index != -1)
					index = MyNotePane.getInstance().find(findTf.getText(), replTf.getText(), back, isSelect);
			}
		});
		add(reAlButton);

		// 关闭
		JButton closButton = new JButton("Close");
		closButton.setFont(font);
		closButton.setMargin(in);
		closButton.setBounds(165, 340, 100, 30);
		closButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(closButton);
		
		error = new JLabel("");
		error.setFont(font);
		error.setForeground(Color.RED);
		error.setBounds(10, 340, 150, 30);
		add(error);

		setSize(280, 405);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	// 选择范围
	private void scope() {
		JPanel scoPane = new JPanel();
		scoPane.setBounds(10, 180, 255, 60);
		TitledBorder tb = BorderFactory.createTitledBorder("Scope");
		tb.setTitleFont(font);
		scoPane.setBorder(tb);
		scoPane.setLayout(null);
		JRadioButton allrb = new JRadioButton("All", true);
		JRadioButton selectLinesrb = new JRadioButton("Selected lines");
		allrb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isSelect = false;
			}
		});
		selectLinesrb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isSelect = true;
			}
		});
		allrb.setFont(font);
		selectLinesrb.setFont(font);
		allrb.setBounds(30, 20, 40, 30);
		selectLinesrb.setBounds(120, 20, 120, 30);
		ButtonGroup bg = new ButtonGroup();
		bg.add(allrb);
		bg.add(selectLinesrb);
		scoPane.add(allrb);
		scoPane.add(selectLinesrb);
		add(scoPane);
	}

	// 向前还是向后查找
	private void direction() {
		JPanel dirPane = new JPanel();
		dirPane.setBounds(10, 100, 255, 60);
		TitledBorder tb = BorderFactory.createTitledBorder("Direction");
		tb.setTitleFont(font);
		dirPane.setBorder(tb);
		dirPane.setLayout(null);
		JRadioButton forrb = new JRadioButton("Forward", true);
		JRadioButton backrb = new JRadioButton("Backward");
		forrb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				back = false;
			}
		});
		backrb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				back = true;
			}
		});
		forrb.setBounds(30, 20, 75, 30);
		backrb.setBounds(120, 20, 84, 30);
		forrb.setFont(font);
		backrb.setFont(font);
		ButtonGroup bg = new ButtonGroup();
		bg.add(forrb);
		bg.add(backrb);
		dirPane.add(forrb);
		dirPane.add(backrb);
		add(dirPane);
	}

	private void addLabel(String string, int i, int j, int k, int l) {
		JLabel label = new JLabel(string);
		label.setBounds(i, j, k, l);
		label.setFont(font);
		add(label);
	}
	
	public static void main(String[] args) {
		try{
    		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    	} catch (Exception error){
    		error.printStackTrace();
    	}
		new FindStringDialog();
	}
}
