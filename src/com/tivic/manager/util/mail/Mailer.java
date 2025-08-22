package com.tivic.manager.util.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.tivic.manager.util.mail.enums.SecurityType;
import com.tivic.manager.util.mail.providers.MailerProvider;

public class Mailer {

	private Properties props = System.getProperties();
	private Authenticator auth;
	private MailerProvider provider;
	private Message message;

	public Mailer(MailerProvider provider, Authenticator auth) {
		this.provider = provider;
		this.auth = auth;
		build();
	}

	public Mailer addAddress(Address address) throws AddressException, MessagingException, UnsupportedEncodingException {
		if (address != null && address.validate()) {
			this.message.addRecipient(RecipientType.TO, new InternetAddress(address.getMail(), address.getName()));
		}
		return this;
	}

	public Mailer setSubject(String subject) throws MessagingException {
		this.message.setSubject(subject);
		return this;
	}

	public Mailer setContent(String content) throws MessagingException {
		this.message.setContent(content, "text/html; charset=utf-8");
		return this;
	}

	public Mailer send() {
		try {
			Transport.send(this.message);
			return this;
		} catch (MessagingException e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	private Mailer build() {
		try {
			props.put("mail.smtp.host", this.provider.getHost());
			props.put("mail.smtp.port", this.provider.getPort());

			if (this.provider.getSecType() == SecurityType.TLS) {
				props.put("mail.smtp.starttls.enable", "true");
			}

			if (auth != null) {
				props.put("mail.smtp.auth", "true");
			}

			Session session = Session.getInstance(props, auth);
			this.message = new MimeMessage(session);
			this.message.setFrom(new InternetAddress(auth.getUsername(), auth.getName()));
			
			return this;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/**
	 * @TODO Criar classe própria para isso
	 * @return
	 */
	public String getTemplate(String templateName, HashMap<String, String> vars) {
		try {
			Path currDir = new File(Mailer.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toPath();
			List<String> template = Files.readAllLines(Paths.get(currDir + "/com/tivic/manager/util/mail/templates/"+templateName+".html"), StandardCharsets.UTF_8);
						
			Pattern p = Pattern.compile("\\[\\[([a-zA-Z._]+)\\]\\]");
			Matcher m = p.matcher(String.join("", template));
			StringBuffer sb = new StringBuffer();
			while (m.find()) {
			    String reg1 = m.group(1);
			    String value = vars.get(reg1);
			    m.appendReplacement(sb, Matcher.quoteReplacement(value == null ? "" : value));
			}
			m.appendTail(sb);
			
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
