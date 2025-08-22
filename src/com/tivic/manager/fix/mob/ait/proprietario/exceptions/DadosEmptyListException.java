package com.tivic.manager.fix.mob.ait.proprietario.exceptions;

public class DadosEmptyListException extends Exception{

	private static final long serialVersionUID = 1L;

	public DadosEmptyListException() {
        super("Não há dados para essa busca.");
    }

    public DadosEmptyListException(String message) {
        super(message);
    }

}
