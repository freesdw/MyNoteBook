package xdoc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.hg.xdoc.XDoc;
import com.hg.xdoc.XDocEditor;
import com.hg.xdoc.XDocService;

public class XDocEditorEmbed {
	public XDocEditorEmbed(){
		try {
            final XDocEditor editor = new XDocEditor();
            final JFrame frame = new JFrame("XDocEditor嵌入");
            frame.getContentPane().add(editor, BorderLayout.CENTER);
            JToolBar bar = new JToolBar("工具条");
            JButton btn = new JButton("编辑文件");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	try {
                        JFileChooser fileChooser = new JFileChooser();
                        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                            editor.setXDoc(new XDoc(fileChooser.getSelectedFile().getAbsolutePath()));
                        }
					} catch (Exception e1) {
						e1.printStackTrace();
					}
                }
            });
            bar.add(btn);
            btn = new JButton("编辑HTML");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	try {
						editor.setXDoc(new XDoc("<html><h1>Hello 中国</h1></html>"));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
                }
            });
            bar.add(btn);
            btn = new JButton("编辑XDOC");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	try {
						editor.setXDoc(new XDoc("<xdoc><body><text>Hello XDOC</text></body></xdoc>"));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
                }
            });
            bar.add(btn);
            bar.addSeparator();
            btn = new JButton("查看XML");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	showText(frame, editor.getXDoc().toXml());
                }
            });
            bar.add(btn);
            btn = new JButton("查看JSON");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	showText(frame, editor.getXDoc().toJson());
                }
            });
            bar.add(btn);
            btn = new JButton("保存PDF");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	try {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setSelectedFile(new File("xdoc.pdf"));
                        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        	new XDocService().to(editor.getXDoc().toXml(), new File(fileChooser.getSelectedFile().getAbsolutePath()));
                        }
					} catch (Exception e1) {
						e1.printStackTrace();
					}
                }
            });
            bar.add(btn);
            Container content = frame.getContentPane();
            content.add(bar, BorderLayout.NORTH);
            frame.setSize(680, 400);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
	}

	protected static void showText(JFrame frame, String str) {
        JTextArea txt = new JTextArea();
        try {
            txt.setLineWrap(true);
            txt.setText(str);
            txt.setCaretPosition(0);
        } catch (Exception e2) {
            txt.setText(e2.getMessage());
        }
        JScrollPane scroll = new JScrollPane(txt);
        scroll.setPreferredSize(new Dimension(300, 160));
        JOptionPane.showMessageDialog(frame, scroll, "文本", JOptionPane.INFORMATION_MESSAGE);
	}
}