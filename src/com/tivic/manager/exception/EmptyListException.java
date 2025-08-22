package com.tivic.manager.exception;

public class EmptyListException extends RuntimeException {
	private static final long serialVersionUID = 2118167013402045590L;

	public EmptyListException() {
		super();
	}
	
	public EmptyListException(String message) {
		super(message);
	}
}
