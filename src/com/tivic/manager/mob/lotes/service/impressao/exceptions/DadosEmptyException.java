package com.tivic.manager.mob.lotes.service.impressao.exceptions;

public class DadosEmptyException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public DadosEmptyException() {
        super("Não foram encontrados registros." );
    }

    public DadosEmptyException(String message) {
        super(message);
    }
}
