package com.tivic.manager.mob.lotes.service.dividaativa.exceptions;

public class NenhumLoteDividaAtivaEncontradoException extends Exception {

	private static final long serialVersionUID = 1L;

	public NenhumLoteDividaAtivaEncontradoException() {
		super("Nenhum lote de divida ativa foi encontrado");
	}
}
