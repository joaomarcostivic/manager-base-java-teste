package com.tivic.manager.util.mail;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Address {
	
	private String name;
	private String mail;
	
	public Address(String name, String mail) {
		this.name = name;
		this.mail = mail;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public boolean validate()  {
		try {
			new InternetAddress(this.mail).validate();
			
			return !(this.name == null || this.name.trim().equals("") ||
					   this.mail == null || this.mail.trim().equals(""));
		} catch (AddressException e) {
			return false;
		}
	}
	
	

}
