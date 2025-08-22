package com.tivic.manager.triagem.exceptions;

public class EventoEstacionamentoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    public EventoEstacionamentoNotFoundException(int cdEvento) {
        super(String.format("EventoEstacionamento não encontrado: %d", cdEvento));
    }
}