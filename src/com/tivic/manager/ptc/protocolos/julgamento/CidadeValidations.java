package com.tivic.manager.ptc.protocolos.julgamento;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.Cidade;


public class CidadeValidations {
	
	public CidadeValidations() {
	}

	public void validate(Cidade cidade) throws Exception {
		if(cidade == null)
			throw new BadRequestException("Sem Cidade cadastrada para o Proprietário!");
	}
	
}
