package com.Projec.WiZBin;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSend {
	
	public static void email(String from,String password,String to,String subj,String msg) {
		//Get Properties Object
		Properties prop=new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.port", "465");

		//get Session
		Session session=Session.getDefaultInstance(prop,new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from,password);
			}
		});
		//compose message
		
		try {
			MimeMessage message =new MimeMessage(session);
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			message.setSubject(subj);
			message.setText(msg);
			//send message
			Transport.send(message);

			System.out.println("message sent successfully");
		}
		catch(MessagingException  e) {
			throw new RuntimeException(e);			
		}
		
	}

}






