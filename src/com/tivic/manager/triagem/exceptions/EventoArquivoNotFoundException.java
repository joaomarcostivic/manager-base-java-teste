package com.tivic.manager.triagem.exceptions;

public class EventoArquivoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    public EventoArquivoNotFoundException(int cdEvento) {
        super(String.format("EventoArquivo não encontrado: %d", cdEvento));
    }
}