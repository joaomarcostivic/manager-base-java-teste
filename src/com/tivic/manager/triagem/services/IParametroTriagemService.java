package com.tivic.manager.triagem.services;

import com.tivic.manager.triagem.dtos.ParametroTriagemDTO;

public interface IParametroTriagemService {
	ParametroTriagemDTO getValorOfParametroByName(String nmParametro) throws Exception;
}
