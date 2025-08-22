package com.tivic.manager.mob.lote.impressao;

public class LoteNotificacaoException extends Exception{
	private static final long serialVersionUID = -9177843642019571654L;
	private int codErro;
	public LoteNotificacaoException(int codErro, String mensagem){
		super(mensagem);
		this.codErro = codErro;
	}
	
	public int getCodErro() {
		return this.codErro;
	}
}
