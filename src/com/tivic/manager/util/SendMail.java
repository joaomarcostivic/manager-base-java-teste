package com.tivic.manager.util;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.PasswordAuthentication;
import java.util.Properties;

public class SendMail {
     String host;
     String username;
     String password;
    
    protected Session session= null;

    public SendMail(String host, String username, String password) {
    	this.host=host;
    	this.username=username;
    	this.password=password;
    }

    public void send(String from, String to, String subject, String content) throws Exception {
    	send(from, to, subject, content, null, null);
    }
    
    public void send(String from, String to, String subject, String content, String typeContent, String charset) throws Exception {
    	//Get system properties
    	Properties props = System.getProperties();

    	//Setup mail server
    	props.put("mail.smtp.user", username);
    	props.put("mail.smtp.host", host);
    	//props.put("mail.smtp.port", "465");
    	//props.put("mail.smtp.starttls.enable","true");
    	props.put("mail.smtp.auth", "true");
    	
    	//Get session
    	Session session = Session.getInstance(props, new SMTPAuthenticator());

    	//Define message
    	MimeMessage message = new MimeMessage(session);
    	message.setFrom(new InternetAddress(from));
    	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
    	message.setSubject(subject, charset);
    	if (typeContent == null)
    		typeContent = "text/html";
    	if (charset != null)
        	message.setText(content, charset);
    	else
    		message.setContent(content, typeContent);
  
    	Transport transport = session.getTransport("smtp");
    	transport.connect(host, username, password);
    	transport.sendMessage(message, message.getAllRecipients());
    	transport.close();
    }
    
    private class SMTPAuthenticator extends javax.mail.Authenticator {
    	public PasswordAuthentication getPasswordAuthentication() {
    		return new PasswordAuthentication (username, password);
    	}
    }
}


