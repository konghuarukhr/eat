package com.yourmall.service.eat;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	public static void doSend(String from, String[] tos, String[] ccs, String subject, String text) throws AddressException, MessagingException {
		Properties prop = System.getProperties();
		prop.setProperty("mail.smtp.host", "127.0.0.1");
		Session session = Session.getDefaultInstance(prop);
		MimeMessage msg = new MimeMessage(session);
		if (from != null) {
			msg.setFrom(new InternetAddress(from));
		}
		for (String to : tos) {
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		}
		if (ccs != null) {
			for (String cc : ccs) {
				msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
			}
		}
		msg.setSubject(subject);
		msg.setText(text);
		Transport.send(msg);
	}
}
