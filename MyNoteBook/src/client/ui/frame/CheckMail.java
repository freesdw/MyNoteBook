package client.ui.frame;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * 用于用户注册时，向用户邮箱发送验证码
 * @author chain
 * @time
 * @aim
 */
public class CheckMail {
	//发送的邮箱账号
	private static final String myEmailAccount = "syslisp_chain@163.com";
	//邮箱密码
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
	 * 创建一封只包含文本的简单邮件
	 * @param session 和服务器交互的对话
	 * @param sendMail 发件人邮箱
	 * @param receiveMail 收件人邮箱
	 * @return
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */

	private MimeMessage createMimeMessage(Session session) throws Exception {
		MimeMessage message = new MimeMessage(session);

		message.setFrom(new InternetAddress(myEmailAccount, "中大java项目-笔记本", "UTF-8"));

		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMailAccount, "客户", "UTF-8"));
		message.addRecipient(MimeMessage.RecipientType.CC,new InternetAddress(myEmailAccount, "客户", "UTF-8"));

		message.setSubject(subjectName, "UTF-8");

		message.setContent(content, "text/html;charset=UTF-8");

		message.setSentDate(new Date());

		message.saveChanges();

		return message;
	}

	public static void main(String[] args) {
		try {
			new CheckMail("571683955@qq.com", "测试", "别管我");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
