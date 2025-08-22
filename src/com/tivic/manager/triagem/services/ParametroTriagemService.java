package com.tivic.manager.triagem.services;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.triagem.builders.ParametroTriagemBuilder;
import com.tivic.manager.triagem.dtos.ParametroTriagemDTO;
import com.tivic.sol.cdi.BeansFactory;

public class ParametroTriagemService implements IParametroTriagemService {
	
	private IParametroRepository parametroResitory;
	
	public ParametroTriagemService() throws Exception {
		this.parametroResitory = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}

	@Override
	public ParametroTriagemDTO getValorOfParametroByName(String nmParametro) throws Exception {
		String vlParametro = this.parametroResitory.getValorOfParametroAsString(nmParametro); 
		ParametroTriagemDTO parametroTriagemDTO = new ParametroTriagemBuilder()
				.nmParametro(nmParametro)
				.vlParametro(vlParametro)
				.build();
		return parametroTriagemDTO;
	}

}
