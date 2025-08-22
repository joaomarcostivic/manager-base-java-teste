package com.tivic.manager.tasks.limpeza.lotes;

import com.tivic.sol.connection.CustomConnection;

public interface ILimpezaLotesService {
	public void limparLotes(LimpezaLotesDTO limpezaLotesDTO) throws Exception;
	public void limparLotes(LimpezaLotesDTO limpezaLotesDTO, CustomConnection customConnection) throws Exception;
}
