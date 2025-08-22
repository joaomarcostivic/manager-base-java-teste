package com.tivic.manager.grl.parametro;

import com.tivic.manager.grl.parametro.dtos.ValorParametroDTO;

public interface IParametroService {

	ValorParametroDTO getValorOfParametroAsString(String nmParametro) throws Exception;

	ValorParametroDTO getValorOfParametroAsInt(String nmParametro) throws Exception;

}
