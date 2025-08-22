package com.tivic.manager.triagem.exceptions;

public class EventoMotivoCancelamentoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    public EventoMotivoCancelamentoNotFoundException(int cdMotivoCancelamento) {
        super(String.format("Motivo cancelamento de evento não encontrado: %d", cdMotivoCancelamento));
    }
}