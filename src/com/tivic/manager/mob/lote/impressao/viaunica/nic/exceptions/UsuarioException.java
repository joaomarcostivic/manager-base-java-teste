package com.tivic.manager.mob.lote.impressao.viaunica.nic.exceptions;

public class UsuarioException extends Exception {
	private static final long serialVersionUID = 1L;

	public UsuarioException() {
        super("Usuário não localizado." );
    }

    public UsuarioException(String message) {
        super(message);
    }
}
