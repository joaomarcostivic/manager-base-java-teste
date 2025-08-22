package com.tivic.manager.mob.lotes.exception;

public class UsuarioNaoLocalizadoException extends Exception {
	private static final long serialVersionUID = 1L;

	public UsuarioNaoLocalizadoException() {
        super("Usuário não localizado." );
    }

    public UsuarioNaoLocalizadoException(String message) {
        super(message);
    }
}
