package com.tivic.manager.triagem.exceptions;

public class EventoEstacionamentoDetalhesNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    public EventoEstacionamentoDetalhesNotFoundException(int cdEvento) {
        super(String.format("EventoEstacionamentoDetalhes não encontrado: %d", cdEvento));
    }
}