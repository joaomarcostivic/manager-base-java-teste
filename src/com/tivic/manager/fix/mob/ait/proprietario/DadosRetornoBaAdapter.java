package com.tivic.manager.fix.mob.ait.proprietario;

import com.tivic.manager.wsdl.detran.ba.consultarplaca.ConsultarPlacaDadosRetorno;

public class DadosRetornoBaAdapter implements IDadosRetornoAdapter {

	private ConsultarPlacaDadosRetorno dadosRetornoBA;
	
	public DadosRetornoBaAdapter(ConsultarPlacaDadosRetorno dadosRetornoBA) {
	    this.dadosRetornoBA = dadosRetornoBA;
	}
	
	@Override
	public String getCodigoMunicipio() {
		return String.valueOf(dadosRetornoBA.getCodigoMunicipio());
	}

	@Override
	public String getLogradouro() {
	    return dadosRetornoBA.getEndereco();
	}
	
	@Override
	public String getNumero() {
	    return dadosRetornoBA.getNumeroEndereco();
	}

	@Override
	public String getBairro() {
		return dadosRetornoBA.getNomeBairro();
	}

	@Override
	public String getCep() {
		return dadosRetornoBA.getNumeroCep();
	}

}
