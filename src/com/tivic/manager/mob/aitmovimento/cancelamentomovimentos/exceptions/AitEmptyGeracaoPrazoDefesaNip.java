package com.tivic.manager.mob.aitmovimento.cancelamentomovimentos.exceptions;

public class AitEmptyGeracaoPrazoDefesaNip extends Exception {
	
	private static final long serialVersionUID = 1L;

	public AitEmptyGeracaoPrazoDefesaNip() {
        super("AIT não localizado para gerar Fim Prazo Defesa e NIP.");
    }

    public AitEmptyGeracaoPrazoDefesaNip(String message) {
        super(message);
    }
}
