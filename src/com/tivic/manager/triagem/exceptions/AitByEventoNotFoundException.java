package com.tivic.manager.triagem.exceptions;

public class AitByEventoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    public AitByEventoNotFoundException(int cdEvento) {
        super(String.format("AIT não encontrado para o evento: %d", cdEvento));
    }
}