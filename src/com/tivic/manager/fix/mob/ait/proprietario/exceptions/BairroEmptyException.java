package com.tivic.manager.fix.mob.ait.proprietario.exceptions;

public class BairroEmptyException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public BairroEmptyException() {
        super("Bairro não cadastrado.");
    }

    public BairroEmptyException(String message) {
        super(message);
    }
}
