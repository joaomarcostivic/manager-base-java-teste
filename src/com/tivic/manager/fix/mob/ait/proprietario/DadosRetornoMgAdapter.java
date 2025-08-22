package com.tivic.manager.fix.mob.ait.proprietario;

import com.tivic.manager.wsdl.detran.mg.consultarplaca.ConsultarPlacaDadosRetorno;

public class DadosRetornoMgAdapter implements IDadosRetornoAdapter  {

	private ConsultarPlacaDadosRetorno dadosRetornoMG;
	
	public DadosRetornoMgAdapter(ConsultarPlacaDadosRetorno dadosRetornoMG) {
	    this.dadosRetornoMG = dadosRetornoMG;
	}
	
	@Override
	public String getCodigoMunicipio() {
		return dadosRetornoMG.getCodigoMunicipio();
	}

	@Override
	public String getLogradouro() {
	    return dadosRetornoMG.getLogradouro();
	}
	
	@Override
	public String getNumero() {
	    return dadosRetornoMG.getNumero();
	}

	@Override
	public String getBairro() {
		return dadosRetornoMG.getBairro();
	}

	@Override
	public String getCep() {
		return dadosRetornoMG.getCep();
	}

}
