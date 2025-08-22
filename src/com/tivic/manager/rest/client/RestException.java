package com.tivic.manager.rest.client;

import com.tivic.manager.util.JsonToStringBuilder;

public class RestException extends Exception {
	private static final long serialVersionUID = -5501654739993928372L;
	
	private int status;
	
	public RestException(int status, String message) {
		super(message);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		JsonToStringBuilder builder = new JsonToStringBuilder(this);
		builder.append("status", status);
		builder.append("message", getMessage());
		return builder.toString();
	}

	
}
