package com.tivic.manager.mob.lotes.service.dividaativa.exceptions;

public class LoteFinalizadoException extends Exception {

	private static final long serialVersionUID = 1L;

	public LoteFinalizadoException() {
		super("O lote já foi finalizado");
	}
}
