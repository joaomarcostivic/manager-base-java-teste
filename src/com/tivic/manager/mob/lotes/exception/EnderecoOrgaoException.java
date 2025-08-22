package com.tivic.manager.mob.lotes.exception;

public class EnderecoOrgaoException extends Exception {
	private static final long serialVersionUID = 1L;

	public EnderecoOrgaoException() {
        super("Parâmetro de endereço do órgão não configurado." );
    }

    public EnderecoOrgaoException(String message) {
        super(message);
    } 
}
