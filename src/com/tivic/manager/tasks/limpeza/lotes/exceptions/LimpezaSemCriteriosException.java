package com.tivic.manager.tasks.limpeza.lotes.exceptions;

public class LimpezaSemCriteriosException extends Exception {

	private static final long serialVersionUID = 1L;

	public LimpezaSemCriteriosException() {
		super("A limpeza deve ter pelo menos um critério");
	}
}
