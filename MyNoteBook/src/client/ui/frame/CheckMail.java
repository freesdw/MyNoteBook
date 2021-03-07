package client.ui.frame;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * �����û�ע��ʱ�����û����䷢����֤��
 * @author chain
 * @time
 * @aim
 */
public class CheckMail {
	//���͵������˺�
	private static final String myEmailAccount = "syslisp_chain@163.com";
	//��������
	private static final String myEmailPassword = "chain0981101082";

	private static final String myEmailSMTPHost = "smtp.163.com";

	private String receiveMailAccount = null;

	private String subjectName = null;

	private String content = null;

	public CheckMail(String accountMail, String subjectName, String content) throws Exception {

		receiveMailAccount = accountMail;
		this.subjectName = subjectName;
		this.content = content;

		Properties pros = new Properties();
		pros.setProperty("mail.transport.protocol", "smtp");
		pros.setProperty("mail.smtp.host", myEmailSMTPHost);
		pros.setProperty("mail.smtp.auth", "true");

		Session session = Session.getInstance(pros);
		session.setDebug(true);

		MimeMessage message = createMimeMessage(session);

		Transport transport = session.getTransport();

		transport.connect(myEmailAccount, myEmailPassword);

		transport.sendMessage(message, message.getAllRecipients());

		transport.close();
	}

	/**
	 * ����һ��ֻ�����ı��ļ��ʼ�
	 * @param session �ͷ����������ĶԻ�
	 * @param sendMail ����������
	 * @param receiveMail �ռ�������
	 * @return
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */

	private MimeMessage createMimeMessage(Session session) throws Exception {
		MimeMessage message = new MimeMessage(session);

		message.setFrom(new InternetAddress(myEmailAccount, "�д�java��Ŀ-�ʼǱ�", "UTF-8"));

		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMailAccount, "�ͻ�", "UTF-8"));
		message.addRecipient(MimeMessage.RecipientType.CC,new InternetAddress(myEmailAccount, "�ͻ�", "UTF-8"));

		message.setSubject(subjectName, "UTF-8");

		message.setContent(content, "text/html;charset=UTF-8");

		message.setSentDate(new Date());

		message.saveChanges();

		return message;
	}

	public static void main(String[] args) {
		try {
			new CheckMail("571683955@qq.com", "����", "�����");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
