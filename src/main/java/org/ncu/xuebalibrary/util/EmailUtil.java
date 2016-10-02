package org.ncu.xuebalibrary.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.ncu.xuebalibrary.config.Strings;

public class EmailUtil {

	private static Session getSession() {
		
		Properties props = new Properties();
		props.put("mail.smtp.host", Strings.EMAIL_HOST);
		//props.put("mail.store.protocol", Strings.EMAIL_PROTOCOL);
		//props.put("mail.smtp.port", Strings.EMAIL_PORT);
		props.put("mail.smtp.auth", "true");
		
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Strings.EMAIL_FROM, Strings.EMAIL_PWD);
			}
		};
		
		Session session = Session.getDefaultInstance(props, authenticator);
		
		return session;
	}
	
	public static boolean send(String toEmail, String subject, String content) {
		
		boolean flag = true;
		Session session = getSession();
		try {
			Message msg = new MimeMessage(session);
			
			msg.setFrom(new InternetAddress(Strings.EMAIL_FROM));
			
			InternetAddress[] address = {new InternetAddress(toEmail)};
			msg.setRecipients(Message.RecipientType.TO, address);
			
			msg.setSentDate(new Date());
			
			System.setProperty("mail.mime.charset","utf-8");
			msg.setSubject("=?utf-8?B?" + subject + "?=");
			msg.setContent("<a href=\"" + Strings.URL + content + "\">" + Strings.URL + content + "</a>", "text/html;charset=utf-8");
			//msg.setHeader("Content-Transfer-Encoding", "base64");
			
			Transport.send(msg);
		} catch (MessagingException e) {
			flag = false;
			e.printStackTrace();
		}
		
		return flag;
	}
}
