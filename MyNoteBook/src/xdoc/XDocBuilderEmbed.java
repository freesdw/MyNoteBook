package xdoc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.hg.xdoc.XDocBuilder;

public class XDocBuilderEmbed {
    private static XDocBuilder builder;
    
    public XDocBuilderEmbed(){
    	try {
            builder = new XDocBuilder();
            JFrame frame = new JFrame("XDocBuilder");
            frame.getContentPane().add(builder, BorderLayout.CENTER);
            JToolBar bar = new JToolBar("������");
            JButton btn = new JButton("�½�");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    builder.create();
                }
            });
            btn = new JButton("��");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    builder.open();
                }
            });
            bar.add(btn);
            btn = new JButton("�鿴XML");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JTextArea txt = new JTextArea();
                    try {
                        String xml = builder.getXDoc().toXml();
                        txt.setText(xml);
                    } catch (Exception e2) {
                        txt.setText(e2.getMessage());
                    }
                    JScrollPane scroll = new JScrollPane(txt);
                    scroll.setPreferredSize(new Dimension(300, 160));
                    JOptionPane.showMessageDialog(builder, scroll, "XML", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            bar.add(btn);
            btn = new JButton("����");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    builder.save();
                }
            });
            bar.add(btn);
            btn = new JButton("ȫ������");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < builder.getXDocCount(); i++) {
                        builder.activeXDoc(i);
                        builder.save();
                    }
                }
            });
            bar.add(btn);
            btn = new JButton("�ر�");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    builder.close();
                }
            });
            bar.add(btn);
            btn = new JButton("��ӡ");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    builder.print();
                }
            });
            bar.add(btn);
            btn = new JButton("ֱ�Ӵ�ӡ");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    builder.print("HG JPD �����ӡ��");
                }
            });
            bar.add(btn);
            btn = new JButton("����");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    builder.about();
                }
            });
            bar.add(btn);
            Container content = frame.getContentPane();
            content.add(bar, BorderLayout.NORTH);
            frame.setSize(800, 600);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowListener() {
                public void windowOpened(WindowEvent e) {}
                public void windowClosing(WindowEvent e) {
                    //frame�ر�ʱ������
                    for (int i = 0; i < builder.getXDocCount(); i++) {
                       // 
                    }
                    frame.dispose();
                }
                public void windowClosed(WindowEvent e) {}
                public void windowIconified(WindowEvent e) {}
                public void windowDeiconified(WindowEvent e) {}
                public void windowActivated(WindowEvent e) {}
                public void windowDeactivated(WindowEvent e) {}
            });
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}