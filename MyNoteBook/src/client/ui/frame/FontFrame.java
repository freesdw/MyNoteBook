package client.ui.frame;

//http://blog.csdn.net/leoshime/article/details/7035763

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.ui.panel.MyNotePane;

import javax.swing.JDialog;

/*
* 8 9 10 11 12 14 16 18 20 22 24 26 28 36 48 72
* �˺���  5���ߺ���  5.5��С��    6.5������    7.5��С��    9
* ���    10.5��С��    12���ĺ�    14��С��    15������    16
* С��   18������   22��Сһ    24��һ��    26��С��    36������    42
*/

@SuppressWarnings("serial")
public class FontFrame extends JDialog {
	public static Font menuFont = new Font("΢���ź�", Font.PLAIN, 12);
	public static Font defaultPaneFont = new Font("����", 0, 14);

	private JPanel fontPane = null;
	private JPanel buttonPane = null;
	private Font presentPaneFont = defaultPaneFont;
	private JLabel demoLabel = new JLabel("AaBbYyZy");
	private String presentFontFamaly = "����";
	private int presentFontStyle = 0;
	private int presentFontSize = 14;
	private int defaultFamalyItem = 3, defaultStyleItem = 0, defaultSizeItem = 0;

	private class JListSetFont extends DefaultListCellRenderer {
		String[] fontfamalies = null;
		int[] fontStyles = null;

		public JListSetFont(String[] f) {
			fontfamalies = f;
		}

		public JListSetFont(int[] f) {
			fontStyles = f;
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (fontfamalies != null)
				for (int i = 0; i < fontfamalies.length; i++)
					if (i == index)
						setFont(new Font(fontfamalies[i], 0, 14));
			if (fontStyles != null)
				for (int i = 0; i < fontStyles.length; i++)
					if (i == index)
						setFont(new Font("΢���ź�", fontStyles[i], 14));
			return this;
		}
	}

	public FontFrame() {
		setTitle("����");

		fontPane = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		addFontFamily(constraints);
		addFontStyle(constraints);
		addFontSize(constraints);
		addFontDemo(constraints);
		add(fontPane);

		buttonPane = new JPanel(new GridBagLayout());
		GridBagConstraints constraints2 = new GridBagConstraints();
		addButtons(constraints2);
		add(buttonPane, "South");

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(440, 490);
		setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);
	}

	private void addButtons(GridBagConstraints constraints) {
		JButton ok = new JButton("ȷ��");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultPaneFont = presentPaneFont;
				dispose();
			}
		});
		ok.setFont(menuFont);
		ok.setPreferredSize(new Dimension(80, 30));
		constraints.insets = new Insets(0, 240, 10, 10);
		constraints.gridx = 0;
		constraints.gridy = 0;
		buttonPane.add(ok, constraints);

		JButton cancel = new JButton("ȡ��");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancel.setFont(menuFont);
		cancel.setPreferredSize(new Dimension(80, 30));
		constraints.insets = new Insets(0, 0, 10, 0);
		constraints.gridx = 1;
		constraints.gridy = 0;
		buttonPane.add(cancel, constraints);
	}

	// ����
	private void addFontDemo(GridBagConstraints constraints) {
		JPanel fontDemoPane = new JPanel(new GridBagLayout());
		fontDemoPane.setBorder(BorderFactory.createTitledBorder("ʾ��"));
		demoLabel.setFont(presentPaneFont);
		fontDemoPane.add(demoLabel);
		fontDemoPane.setPreferredSize(new Dimension(220, 100));
		constraints.insets = new Insets(10, 13, 100, 0);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		fontPane.add(fontDemoPane, constraints);
	}

	// ����
	private void addFontFamily(GridBagConstraints constraints) {
		JPanel fontFamilyPane = new JPanel(new BorderLayout());

		JLabel label = new JLabel("����:");
		label.setFont(menuFont);
		fontFamilyPane.add(label, "North");

		JTextField fontFamilyShow = new JTextField(presentFontFamaly, 15);
		fontFamilyPane.add(fontFamilyShow);

		String[] fontFamilies = { "������", "΢���ź�", "����", "����", "����", "������ͤ��ϸ�ڼ���", "����׼Բ_GBK", "����", "����", "Blackoak Std",
				"Adobe Arabic", "Adobe Fan Heiti Std", "Adobe ���� Std", "Adobe ���� Std", "Adobe ���� Std", "Adobe ���� Std",
				"Roboto Slab", "Roboto", "Droid Serif", "Cambria Math", "Chaparral Pro", "Adobe Myungjo Std", "Calibri",
				"Adobe Naskh", "Adobe Hebrew", "Adobe Caslon Pro", "Adobe Devanagari", "Adobe Fan Heiti Std" };
		JList<String> list = new JList<String>(fontFamilies);
		list.setSelectedIndex(MyNotePane.getInstance().getFontFamiliesBoxIndex());
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				defaultFamalyItem = list.getSelectedIndex();
				presentFontFamaly = list.getSelectedValue();
				fontFamilyShow.setText(presentFontFamaly);
				presentPaneFont = new Font(presentFontFamaly, presentFontStyle, presentFontSize);
				demoLabel.setFont(presentPaneFont);
				MyNotePane.getInstance().setFontFamiliesBox(defaultFamalyItem);
			}
		});
		list.setVisibleRowCount(8);
		list.setCellRenderer(new JListSetFont(fontFamilies));
		list.setSelectedIndex(defaultFamalyItem);
		fontFamilyPane.add(new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), "South");

		fontFamilyPane.setPreferredSize(new Dimension(175, 185));
		constraints.insets = new Insets(10, 0, 0, 0);
		constraints.gridx = 0;
		constraints.gridy = 0;
		fontPane.add(fontFamilyPane, constraints);
	}

	// ����
	private void addFontStyle(GridBagConstraints constraints) {
		JPanel fontStylePane = new JPanel(new BorderLayout());

		JLabel label = new JLabel("����:");
		label.setFont(menuFont);
		fontStylePane.add(label, "North");

		JTextField fontStyleShow = new JTextField("����", 15);
		fontStylePane.add(fontStyleShow);

		String[] fontStyles = { "����", "����", "б��", "��б��" };
		int[] oppositefontStyles = { 0, 1, 2, 3 };
		JList<String> list = new JList<String>(fontStyles);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				defaultStyleItem = list.getSelectedIndex();
				for (int i = 0; i < fontStyles.length; i++)
					if (fontStyles[i] == list.getSelectedValue())
						presentFontStyle = oppositefontStyles[i];
				fontStyleShow.setText(list.getSelectedValue());
				presentPaneFont = new Font(presentFontFamaly, presentFontStyle, presentFontSize);
				demoLabel.setFont(presentPaneFont);
				if (defaultStyleItem == 1)
					MyNotePane.getInstance().doBoldButton(); //
				else if (defaultStyleItem == 2)
					MyNotePane.getInstance().doItalicButton(); //
				else if (defaultStyleItem == 3) {
					MyNotePane.getInstance().doBoldButton();
					MyNotePane.getInstance().doItalicButton(); //
				} else
					MyNotePane.getInstance().doItalicButton(); //
			}
		});
		list.setVisibleRowCount(7);
		list.setCellRenderer(new JListSetFont(oppositefontStyles));
		list.setSelectedIndex(defaultStyleItem);
		fontStylePane.add(new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), "South");

		fontStylePane.setPreferredSize(new Dimension(132, 185));
		constraints.insets = new Insets(10, 15, 0, 0);
		constraints.gridx = 1;
		constraints.gridy = 0;
		fontPane.add(fontStylePane, constraints);
	}

	// ��С
	private void addFontSize(GridBagConstraints constraints) {
		JPanel fontSizePane = new JPanel(new BorderLayout());

		JLabel label = new JLabel("��С:");
		label.setFont(menuFont);
		fontSizePane.add(label, "North");

		JTextField fontSizeShow = new JTextField(String.valueOf(presentFontSize), 10);
		fontSizePane.add(fontSizeShow);

		String[] fontSizes = { "8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48",
				"72" };
		// , "�˺�", "�ߺ�", "С��", "����", "С��", "���", "С��", "�ĺ�", "С��", "����", "С��",
		// "����", "С��", "����"};
		JList<String> list = new JList<String>(fontSizes);
		list.setSelectedIndex(MyNotePane.getInstance().getFontSizesBoxIndex());
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				defaultSizeItem = list.getSelectedIndex();
				presentFontSize = Integer.valueOf(list.getSelectedValue());
				fontSizeShow.setText(list.getSelectedValue());
				presentPaneFont = new Font(presentFontFamaly, presentFontStyle, presentFontSize);
				demoLabel.setFont(presentPaneFont);
				MyNotePane.getInstance().setFontSizesBox(defaultSizeItem);
			}
		});
		list.setVisibleRowCount(7);
		list.setSelectedIndex(defaultSizeItem);
		fontSizePane.add(new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), "South");

		fontSizePane.setMaximumSize(new Dimension(65, 170));

		constraints.insets = new Insets(10, 15, 25, 0);
		constraints.gridx = 2;
		constraints.gridy = 0;
		fontPane.add(fontSizePane, constraints);
	}
}
