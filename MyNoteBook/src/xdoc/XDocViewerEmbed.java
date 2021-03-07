package xdoc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import com.hg.xdoc.XDocViewer;

public class XDocViewerEmbed {
	private static XDocViewer viewer;
	public XDocViewerEmbed(){
		try {
            viewer = new XDocViewer();
            JFrame frame = new JFrame("XDocViewerǶ��");
            JToolBar bar = new JToolBar("������");
            JButton btn = new JButton("��");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    viewer.open();
                }
            });
            bar.add(btn);
            bar.add(btn);
            btn = new JButton("����");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    viewer.save();
                }
            });
            bar.add(btn);
            btn = new JButton("��ӡ");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    viewer.print();
                }
            });
            bar.add(btn);
            btn = new JButton("ֻ���л�");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    viewer.setSaveEnable(!viewer.isSaveEnable());
                    viewer.setOpenEnable(!viewer.isOpenEnable());
                    viewer.setPrintEnable(!viewer.isPrintEnable());
                }
            });
            bar.add(btn);
            btn = new JButton("����");
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    viewer.about();
                }
            });
            bar.add(btn);
            Container content = frame.getContentPane();
            content.add(bar, BorderLayout.NORTH);
            content.add(viewer, BorderLayout.CENTER);
            frame.setSize(800, 600);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
	}
}