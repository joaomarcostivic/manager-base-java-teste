package com.tivic.manager.ptc.emailsender;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.tivic.manager.util.mail.Address;
import com.tivic.manager.util.mail.Authenticator;
import com.tivic.manager.util.mail.Mailer;
import com.tivic.manager.util.mail.enums.SecurityType;
import com.tivic.manager.util.mail.providers.MailerProvider;

public class InfMailSender {
	
	private Mailer mailer;
	private EmailContent sendEmail;
	
	public InfMailSender(EmailContent sendEmail) throws UnsupportedEncodingException {
		this.sendEmail = sendEmail;
		autenticar();
	}
	
	private void autenticar() {
		MailerProvider provider = new MailerProvider(
				sendEmail.getEmailProvedorHost(), 
				sendEmail.getEmailProvedorPort(), 
				SecurityType.TLS);
		Authenticator auth = new Authenticator(
				sendEmail.getEmailRemetente(), 
				sendEmail.getSenhaRemetente(), 
				sendEmail.getEmailHeader());
		this.mailer = new Mailer(provider, auth);
	}
	
	
	public void send() throws AddressException, UnsupportedEncodingException, MessagingException {
		this.mailer
			.addAddress(new Address(sendEmail.getNmPessoa(), sendEmail.getNmEmail()))
			.setSubject(sendEmail.getNmAssunto())
			.setContent(this.mailer.getTemplate(sendEmail.getNmTamplate(), sendEmail.getBody())).send();
	}
}
