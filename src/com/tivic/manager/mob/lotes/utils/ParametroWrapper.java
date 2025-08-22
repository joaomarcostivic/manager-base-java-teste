package com.tivic.manager.mob.lotes.utils;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class ParametroWrapper {
	public static int asInt(String nmParametro, CustomConnection customConnection) throws Exception {
		IParametroRepository parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		
		return parametroRepository.getValorOfParametroAsInt(nmParametro, customConnection);
	}
}