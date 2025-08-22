package com.tivic.manager.util.parametro;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class ParametroFacade implements IParametroFacade {
	private IParametroRepository parametroRepository;
	
	public ParametroFacade() throws Exception {
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	public int getVlParametroAsInt(String nmParametro, CustomConnection customConnection) throws Exception {
		int vlParametro = parametroRepository.getValorOfParametroAsInt(nmParametro, customConnection);
		
		if(vlParametro == 0) {
			throw new Exception("Parâmetro " + nmParametro + " não configurado");
		}
		
		return vlParametro;
	}
}
