package com.tivic.manager.util.mail.providers;

import com.tivic.manager.util.mail.enums.SecurityType;

public class MailerProvider {

	private String host;
	private Integer port;
	private SecurityType secType;

	public MailerProvider() {}	
	
	public MailerProvider(String host, Integer port, SecurityType secType) {
		this.host = host;
		this.port = port;
		this.secType = secType;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public SecurityType getSecType() {
		return secType;
	}

	public void setSecType(SecurityType secType) {
		this.secType = secType;
	}
	
}
