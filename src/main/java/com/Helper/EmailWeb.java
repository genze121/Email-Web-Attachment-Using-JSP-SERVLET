package com.Helper;

import java.io.File;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

public class EmailWeb {

	public void sendMail(String subject, String message, String to, String from, String path) {

		Properties properties = System.getProperties();
		System.out.println("PROPERTIES:- " + properties);

		String host = "smtp.gmail.com";
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", 465);
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(properties, new Authenticator() {
			final String username = "YourMailId";
			final String password = "YourPassword";

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		session.setDebug(true);

		MimeMessage mimeMessage = new MimeMessage(session);
		try {

			MimeMultipart mimeMultipart = new MimeMultipart();

			MimeBodyPart textBodyPart = new MimeBodyPart();
			MimeBodyPart fileBodyPart = new MimeBodyPart();

			try {
				textBodyPart.setText(message);
				File file = new File(path);
				fileBodyPart.attachFile(file);
				
				mimeMultipart.addBodyPart(textBodyPart);
				mimeMultipart.addBodyPart(fileBodyPart);

			} catch (Exception e) {
				e.printStackTrace();
			}

			mimeMessage.setContent(mimeMultipart);

			mimeMessage.setFrom(from);
			mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(to));
			mimeMessage.setSubject(subject);

			Transport.send(mimeMessage);

			System.out.println("Mail sent successfully....");

		} catch (Exception e) {
			System.out.println("Error while sending the mail.." + e.getMessage());
		}

	}

}
