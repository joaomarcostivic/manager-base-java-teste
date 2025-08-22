package com.tivic.manager.fix.mob.ait.proprietario.exceptions;

public class ConsultaDadosDetranException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public ConsultaDadosDetranException() {
        super("Serviço de busca não identificado.");
    }

    public ConsultaDadosDetranException(String message) {
        super(message);
    }
}
