package com.tivic.manager.tasks.limpeza.lotes.exceptions;

public class LimpezaLoteDataMaiorException extends Exception {

	private static final long serialVersionUID = 1L;

	public LimpezaLoteDataMaiorException() {
		super("Data de criação do lote pedida é maior do que a atual");
	}
}
