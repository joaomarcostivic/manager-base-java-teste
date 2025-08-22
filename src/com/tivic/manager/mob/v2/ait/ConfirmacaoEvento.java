package com.tivic.manager.mob.v2.ait;

public class ConfirmacaoEvento {
	private Integer cdUsuarioConfirmacao;
	private String nrPlaca;
	
	public ConfirmacaoEvento(Integer cdUsuarioConfirmacao, String nrPlaca) {
		this.cdUsuarioConfirmacao = cdUsuarioConfirmacao;
		this.nrPlaca = nrPlaca;
	}

	public Integer getCdUsuarioConfirmacao() {
		return cdUsuarioConfirmacao;
	}

	public String getNrPlaca() {
		return nrPlaca;
	}
	
}
