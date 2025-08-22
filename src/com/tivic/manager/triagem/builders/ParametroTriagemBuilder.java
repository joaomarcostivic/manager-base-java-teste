package com.tivic.manager.triagem.builders;

import com.tivic.manager.triagem.dtos.ParametroTriagemDTO;

public class ParametroTriagemBuilder {
	
	private ParametroTriagemDTO parametroTriagemDTO;
	
	public ParametroTriagemBuilder() {
		parametroTriagemDTO = new ParametroTriagemDTO();
	}
	
	public ParametroTriagemBuilder cdParametro(Integer cdParametro) {
		parametroTriagemDTO.setCdParametro(cdParametro);
		return this;
	}
	
	public ParametroTriagemBuilder nmParametro(String nmParametro) {
		parametroTriagemDTO.setNmParametro(nmParametro);
		return this;
	}
	
	public ParametroTriagemBuilder vlParametro(String vlParametro) {
		this.parametroTriagemDTO.setVlParametro(vlParametro);
		return this;
	}
	
	public ParametroTriagemDTO build() {
		return this.parametroTriagemDTO;
	}
}
