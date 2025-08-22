package com.tivic.manager.util.mail;

import javax.mail.PasswordAuthentication;

public class Authenticator extends javax.mail.Authenticator {

	private String username;
	private String password;
	private String name;
	
	public Authenticator(String username, String password, String name) {
		this.username = username;
		this.password = password;		
		this.name = name;
	}
	
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication (username, password);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
